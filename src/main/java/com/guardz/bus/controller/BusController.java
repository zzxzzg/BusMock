package com.guardz.bus.controller;

import com.guardz.WorldConfig;
import com.guardz.bus.domain.Bus;
import com.guardz.passenger.domain.Passenger;
import com.guardz.station.domain.Route;
import com.guardz.station.domain.RoutePoint;
import com.guardz.station.domain.Stations;
import com.guardz.util.RandomUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created: lidong on 2022/5/11 2:37 PM
 * @Description: 公交控制器
 * @version: 1.0.0
 */
public class BusController {

    public enum BusStatus {
        WAITING,//等待调度
        DRIVING,//行驶中，(唯一非自控制状态，需要车站触发调度)
        ARRIVE,// 站点停靠
        STOP,// 站点停靠
        BROKEN,//抛锚
    }


    private final Bus target;

    /**
     * 当前乘客
     */
    private final List<Passenger> currentPassengers = new ArrayList<>();

    private final List<OnBusEventListener> busEventListeners = new ArrayList<>();

    /**
     * 当前路线
     */
    private Route currentRoute;

    /**
     * 当前站点
     */
    private Stations currentStations;

    /**
     * 距离下一站剩余时间
     */
    private int leftDriveDistance;

    /**
     * 正在下车的乘客
     */
    private Passenger gettingOffPassenger;
    /**
     * 乘客下车还需多久
     */
    private int gettingOffRemainTime;

    /**
     * 正在上车的乘客
     */
    private Passenger gettingOnPassenger;
    /**
     * 乘客上车还需多久
     */
    private int gettingOnRemainTime;

    /**
     * 公交当前状态
     */
    private BusStatus status = BusStatus.WAITING;

    public BusController(Bus bus) {
        target = bus;
    }

    public void addBusEventListener(OnBusEventListener onBusEventListener){
        busEventListeners.add(onBusEventListener);
    }

    public boolean isFull() {
        return currentPassengers.size() >= WorldConfig.DEFAULT_MAX_PASSENGER;
    }

    public boolean isStop(){
        return status == BusStatus.STOP;
    }

    public boolean isGettingOn() {
        return gettingOnPassenger != null;
    }

    public void tick(int offset, int worldTime) {
        if (status == BusStatus.WAITING) {
            startBusiness(target.getStartTime());
        }
        if (status == BusStatus.DRIVING) {
            // 到站之后立即出发下车，抛锚等操作，所以顺序需要在 Arrive和Broken前
            driving(offset, worldTime);
        }
        if (status == BusStatus.ARRIVE) {
            arrive(offset, worldTime);
        }
        if (status == BusStatus.BROKEN) {
            broken(offset, worldTime);
        }
        if (status == BusStatus.STOP) {
            stop(offset, worldTime);
        }
    }

    /**
     * 公共汽车发车，规划路线，修改状态为行驶中
     */
    public void start(int worldTime) {
        if (status != BusStatus.STOP) {
            return;
        }
        // 如果已经到达终点站，修改当前路线
        if (currentRoute.isLastStations(currentStations)) {
            currentRoute =
                target.getForwardRoute() == currentRoute ? target.getBackRoute() : target.getForwardRoute();
        }

        // 修改站点线路
        RoutePoint routePoint = currentRoute.getCurrentPoint(currentStations);
        currentStations = routePoint.getNextStations();
        leftDriveDistance = getRealDriveDistance(routePoint.getNextStationsDistance());

        status = BusStatus.DRIVING;
        busEventListeners.forEach(listener -> listener.onBusStart(target, currentStations, leftDriveDistance, worldTime));
    }

    /**
     * 乘客上车，由车站调度
     */
    public void getOn(Passenger passenger) {
        currentPassengers.add(passenger);
        gettingOnPassenger = passenger;
        gettingOnRemainTime = WorldConfig.DEFAULT_GET_ON_TIME;
    }

    /**
     * 停靠状态
     * 1. 当前是否有乘客正在上车。上车完毕则记录，否则继续等待乘客上车
     */
    private void stop(int offset, int worldTime) {
        if (gettingOnPassenger != null) {
            gettingOnRemainTime -= offset;
            if (gettingOnRemainTime <= 0) {
                busEventListeners.forEach(
                    onBusEventListener -> onBusEventListener.onPassengerGetOn(target, gettingOnPassenger,
                        currentStations, worldTime));
                gettingOnPassenger = null;
            }
        }
    }

    /**
     * 到达站点，下客
     * 1. 当前是否有乘客正在下车，下车完毕则记录，否则继续等待乘车下车
     * 2. 如果没有正在下车的乘客，那么询问是否有乘客需要下车
     * 3. 如果有乘客需要下车，那么执行下车逻辑
     * 4. 如果没有乘客下车，正式停靠站点，准备接受乘客上车
     */
    private void arrive(int offset, int worldTime) {
        // 处理下车中用户
        if (gettingOffPassenger != null) {
            gettingOffRemainTime -= offset;
            if (gettingOffRemainTime <= 0) {
                busEventListeners.forEach(
                    onBusEventListener -> onBusEventListener.onPassengerGetOff(target, gettingOffPassenger,
                        currentStations, worldTime));
                gettingOffPassenger = null;
            }
            return;
        }

        Passenger passenger = currentPassengers.stream()
            .filter(p -> p.getEnd() == currentStations)
            .findFirst()
            .orElse(null);
        if (passenger == null) {
            currentStations.addBus(target);
            busEventListeners.forEach(
                onBusEventListener -> onBusEventListener.onBusStop(target, currentStations, worldTime));
            status = BusStatus.STOP;
        } else {
            getOff(passenger);
        }
    }

    /**
     * 汽车抛锚
     * 1. 当前是否有乘客正在下车，下车完毕则记录，否则继续等待乘车下车
     * 2. 如果还有其他乘客，让乘客下车。
     * 3. 如果不是终点站，且非乘客目的地，那么添加到车站排队的队列头部
     */
    private void broken(int offset, int worldTime) {
        // 处理下车中用户
        if (gettingOffPassenger != null) {
            gettingOffRemainTime -= offset;
            if (gettingOffRemainTime <= 0) {
                busEventListeners.forEach(
                    listener -> listener.onPassengerGetOff(target, gettingOffPassenger, currentStations, worldTime));
                gettingOffPassenger = null;
            }
            return;
        }

        if (CollectionUtils.isEmpty(currentPassengers)) {
            return;
        }

        Passenger passenger = currentPassengers.get(0);
        getOff(passenger);
        if (!currentRoute.isLastStations(currentStations) && passenger.getEnd() != currentStations) {
            currentStations.addPassengerInHead(passenger);
        }
    }

    private void getOff(Passenger passenger) {
        currentPassengers.remove(passenger);
        gettingOffPassenger = passenger;
        gettingOffRemainTime = WorldConfig.DEFAULT_GET_OFF_TIME;
    }

    /**
     * 行驶
     * 1. 判断是否到站。如果到站，判断是否抛锚
     */
    private void driving(int offset, int worldTime) {
        leftDriveDistance = leftDriveDistance - offset;
        if (leftDriveDistance <= 0) {// 到站

            if (brokenJudge()) {
                busEventListeners.forEach(listener -> listener.onBusBroken(target, currentStations, worldTime));
                status = BusStatus.BROKEN;
            } else {
                busEventListeners.forEach(listener -> listener.onBusArrived(target, currentStations, worldTime));
                status = BusStatus.ARRIVE;
            }
        }
    }

    /**
     * 汽车开始运营
     */
    private void startBusiness(int worldTime) {
        // 初始化起点车站数据
        currentRoute = target.getForwardRoute();
        RoutePoint routePoint = currentRoute.getStartPoint();
        currentStations = routePoint.getStations();
        leftDriveDistance = getRealDriveDistance(routePoint.getNextStationsDistance());
        busEventListeners.forEach(onBusEventListener -> onBusEventListener.onBusStartBusiness(target, worldTime));
        // 抵达起点站点
        status = BusStatus.ARRIVE;
        busEventListeners.forEach(onBusEventListener -> onBusEventListener.onBusArrived(target, currentStations, worldTime));
    }

    /**
     * 判断是否坏了
     */
    private boolean brokenJudge() {
        int random = RandomUtil.nextInt(WorldConfig.DEFAULT_BROKEN_RATE);
        return random == 0;
    }

    private int getRealDriveDistance(int nextStationsDistance) {
        int random = RandomUtil.nextInt(3);
        return nextStationsDistance + random * 60 - 60;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }
}
