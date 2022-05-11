package com.guardz.station.domain;

import com.guardz.bus.domain.Bus;
import com.guardz.passanger.domain.Passenger;
import com.guardz.timer.ITimer;
import org.apache.commons.collections4.CollectionUtils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Created: lidong on 2022/5/10 3:51 PM
 * @Description: 车站
 * @version: 1.0.0
 */
public class Stations implements ITimer {
    /**
     * 站点名称
     */
    public String stationsName;

    /**
     * 当前站点等待上车的乘客
     */
    private CopyOnWriteArrayList<Passenger> waitingPassenger = new CopyOnWriteArrayList<>();

    /**
     * 当前站点停靠的公交
     */
    private CopyOnWriteArrayList<Bus> waitingBus = new CopyOnWriteArrayList<>();

    public Stations(String stationsName) {
        this.stationsName = stationsName;
    }

    @Override
    public void onWorldStart() {
        onTick(0,0);
    }

    @Override
    public void onTick(int offset, int worldTime) {
        // 当前站点无停靠车辆，无需调度
        if (CollectionUtils.isEmpty(waitingBus)) {
            return;
        }

        // 调度车辆
        for (Bus bus : waitingBus) {
            tickBus(bus, worldTime);
        }

        // 移除已发车车辆
        waitingBus.removeIf(bus -> !bus.isStop());
    }

    /**
     * 乘客到站准备乘车
     */
    public void addPassenger(Passenger passenger) {
        waitingPassenger.add(passenger);
    }

    /**
     * 汽车到站准备上下客
     *
     * @param bus
     */
    public void addBus(Bus bus) {
        waitingBus.add(bus);
    }

    /**
     * 车辆抛锚时调用，将乘客放入队首
     * @param passenger
     */
    public void addPassengerInHead(Passenger passenger) {
        waitingPassenger.add(0, passenger);
    }

    /**
     * 调度车辆及乘客关系。
     * 1. 如果车辆已满，则发车。
     * 2. 如果上一个乘客还在上车，则等待下次调度。
     * 3. 如果没有需要上车的乘客，则发车。
     * 4. 不满足以上，则让乘客上车。
     * @param bus
     * @param worldTime
     */
    private void tickBus(Bus bus, int worldTime){
        if (bus.isFull()) {
            bus.start(worldTime);
            return;
        }
        if (bus.isGettingOn()) {
            return;
        }
        Route route = bus.getCurrentRoute();
        Passenger passenger = getFirstPassenger(route);
        if (passenger != null) {
            bus.getOn(passenger);
            waitingPassenger.remove(passenger);
        } else {
            bus.start(worldTime);
        }
    }

    /**
     * 获取第一个乘坐该线路的乘客
     */
    private Passenger getFirstPassenger(Route route){
        return waitingPassenger.stream()
            .filter(p -> p.getRoute() == route)
            .findFirst()
            .orElse(null);
    }

}
