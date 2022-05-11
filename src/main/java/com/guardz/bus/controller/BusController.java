package com.guardz.bus.controller;

import com.guardz.bus.domain.Bus;
import com.guardz.passanger.domain.Passenger;
import com.guardz.station.domain.Route;
import com.guardz.station.domain.RoutePoint;
import com.guardz.station.domain.Stations;
import com.guardz.util.RandomUtil;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @Created: lidong on 2022/5/11 2:37 PM
 * @Description: 公交控制器
 * @version: 1.0.0
 */
public class BusController {
    /**
     * 默认的故障率  1/{DEFAULT_BROKEN_RATE}
     */
    private static final int DEFAULT_BROKEN_RATE = 10;

    /**
     * 默认下车时间
     */
    private static final int DEFAULT_GET_OFF_TIME = 10;

    /**
     * 默认下车时间
     */
    private static final int DEFAULT_GET_ON_TIME = 10;

    public enum BusStatus {
        WAITING,//等待调度
        DRIVING,//行驶中
        ARRIVE,// 站点停靠
        STOP,// 站点停靠
        BROKEN,//抛锚
    }

    private final Bus target;

    private Route currentRoute;

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

    private BusStatus status = BusStatus.WAITING;

    public BusController(Bus bus) {
        target = bus;
    }

    public void tick(int offset, int worldTime) {
        if (status == BusStatus.WAITING) {
            init(target.getStartTime());
        }
        if (status == BusStatus.DRIVING) {
            driving(offset, worldTime);
        }
        if (status == BusStatus.BROKEN) {
            broken(offset, worldTime);
        }
        if (status == BusStatus.ARRIVE) {
            arrive(offset, worldTime);
        }
        if (status == BusStatus.STOP) {
            stop(offset, worldTime);
        }
    }

    public boolean isStop(){
        return status == BusStatus.STOP;
    }

    public boolean isGettingOn() {
        return gettingOnPassenger != null;
    }

    public void getOn(Passenger passenger) {
        gettingOnPassenger = passenger;
        gettingOnRemainTime = DEFAULT_GET_ON_TIME;
    }

    /**
     * 公共汽车发车，状态修改
     */
    public void start(int worldTime) {
        if (status != BusStatus.STOP) {
            return;
        }

        if (currentRoute.isLastStations(currentStations)) {
            currentRoute =
                target.getForwardRoute() == currentRoute ? target.getBackRoute() : target.getForwardRoute();
        }

        RoutePoint routePoint = currentRoute.getCurrentPoint(currentStations);
        currentStations = routePoint.getNextStations();
        leftDriveDistance = getRealDriveDistance(routePoint.getNextStationsDistance());

        target.addRecord(worldTime, "公交前往站点:" + currentStations.stationsName);
        status = BusStatus.DRIVING;
    }

    /**
     * 车辆停靠,乘客上车
     */
    private void stop(int offset, int worldTime) {
        if (gettingOnPassenger != null) {
            gettingOnRemainTime -= offset;
            if (gettingOnRemainTime <= 0) {
                target.addRecord(worldTime,
                    "乘客:" + gettingOnPassenger.getName() + " 在站点:" + currentStations.stationsName + " 上车");
                gettingOnPassenger = null;
            }
        }
    }

    /**
     * 到达站点，下客
     */
    private void arrive(int offset, int worldTime) {
        // 处理下车中用户
        if (gettingOffPassenger != null) {
            gettingOffRemainTime -= offset;
            if (gettingOffRemainTime <= 0) {
                target.addRecord(worldTime,
                    "乘客:" + gettingOffPassenger.getName() + " 在站点:" + currentStations.stationsName + " 下车");
                gettingOffPassenger = null;
            }
            return;
        }
        Passenger passenger = target.getCurrentPassengers()
            .stream()
            .filter(p -> p.getEnd() == currentStations)
            .findFirst()
            .orElse(null);
        if (passenger == null) {
            currentStations.addBus(target);
            status = BusStatus.STOP;
        } else {
            getOff(passenger);
        }
    }

    /**
     * 汽车抛锚，下客
     */
    private void broken(int offset, int worldTime) {
        // 处理下车中用户
        if (gettingOffPassenger != null) {
            gettingOffRemainTime -= offset;
            if (gettingOffRemainTime <= 0) {
                target.addRecord(worldTime,
                    "乘客:" + gettingOffPassenger.getName() + " 在站点:" + currentStations.stationsName + " 下车");
                gettingOffPassenger = null;
            }
            return;
        }

        if (CollectionUtils.isEmpty(target.getCurrentPassengers())) {
            return;
        }

        Passenger passenger = target.getCurrentPassengers()
            .get(0);
        getOff(passenger);
        if (!currentRoute.isLastStations(currentStations)) {
            currentStations.addPassengerInHead(passenger);
        }
    }

    private void getOff(Passenger passenger) {
        target.getCurrentPassengers()
            .remove(passenger);
        gettingOffPassenger = passenger;
        gettingOffRemainTime = DEFAULT_GET_OFF_TIME;
    }

    /**
     * 行驶，判断是否到站。如果到站，判断是否抛锚
     */
    private void driving(int offset, int worldTime) {
        leftDriveDistance = leftDriveDistance - offset;
        if (leftDriveDistance <= 0) {// 到站

            if (brokenJudge()) {
                target.addRecord(worldTime, "公交在站点:" + currentStations.stationsName + " 抛锚");
                status = BusStatus.BROKEN;
            } else {
                target.addRecord(worldTime, "公交到达站点:" + currentStations.stationsName);
                status = BusStatus.ARRIVE;
            }
        }
    }

    /**
     * 汽车开始运营
     */
    private void init(int worldTime) {
        target.addRecord(worldTime, "公交开始运营");
        currentRoute = target.getForwardRoute();
        RoutePoint routePoint = currentRoute.getStartPoint();
        currentStations = routePoint.getStations();
        leftDriveDistance = getRealDriveDistance(routePoint.getNextStationsDistance());
        target.addRecord(worldTime, "公交到达站点:" + currentStations.stationsName);
        currentStations.addBus(target);
        status = BusStatus.STOP;
    }

    /**
     * 判断是否坏了
     */
    private boolean brokenJudge() {
        int random = RandomUtil.nextInt(DEFAULT_BROKEN_RATE);
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
