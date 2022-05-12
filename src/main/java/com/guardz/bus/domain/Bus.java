package com.guardz.bus.domain;

import com.guardz.bus.controller.BusController;
import com.guardz.passenger.domain.Passenger;
import com.guardz.station.domain.Route;
import com.guardz.timer.ITimer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created: lidong on 2022/5/10 3:48 PM
 * @Description: 公交车类
 * @version: 1.0.0
 */
public class Bus implements ITimer {
    /**
     * 向前路线
     */
    private Route forwardRoute;

    /**
     * 返回路线
     */
    private Route backRoute;

    /**
     * 汽车开始运营的时间
     */
    private int startTime;

    private String id;

    /**
     * 状态控制机
     */
    private final BusController busController;

    /**
     * 运行记录统计
     */
    private final BusStatistics busStatistics;

    public Bus() {
        busController = new BusController(this);
        busStatistics = new BusStatistics(this);
        busController.addBusEventListener(busStatistics);
    }

    @Override
    public void onWorldStart() {
        busController.tick(0, 0);
    }

    @Override
    public void onTick(int offset, int worldTime) {
        busController.tick(offset, worldTime);
    }

    public String getStatistics(int worldTime){
        return busStatistics.getBusStatistics(worldTime);
    }

    public String getRecordDetail(){
        return busStatistics.getRecordDetail();
    }

    public boolean isStop() {
        return busController.isStop();
    }

    public boolean isFull() {
        return busController.isFull();
    }

    public boolean isGettingOn() {
        return busController.isGettingOn();
    }

    public void getOn(Passenger passenger) {
        busController.getOn(passenger);
    }

    /**
     * 停靠结束，发车
     */
    public void start(int worldTime) {
        busController.start(worldTime);
    }

    // ----------- get & set ---------------
    public Route getCurrentRoute() {
        return busController.getCurrentRoute();
    }

    public Route getForwardRoute() {
        return forwardRoute;
    }

    public void setForwardRoute(Route forwardRoute) {
        this.forwardRoute = forwardRoute;
    }

    public Route getBackRoute() {
        return backRoute;
    }

    public void setBackRoute(Route backRoute) {
        this.backRoute = backRoute;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class BusBuilder {
        private int startTime;

        private Route forwardRoute;

        private Route backRoute;

        private String id;

        public BusBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public BusBuilder setStartTime(int startTime) {
            this.startTime = startTime;
            return this;
        }

        public BusBuilder setForwardRoute(Route forwardRoute) {
            this.forwardRoute = forwardRoute;
            return this;
        }

        public BusBuilder setBackRoute(Route backRoute) {
            this.backRoute = backRoute;
            return this;
        }

        public Bus build() {
            Bus bus = new Bus();
            bus.setStartTime(startTime);
            bus.setBackRoute(backRoute);
            bus.setForwardRoute(forwardRoute);
            bus.setId(id);
            return bus;
        }
    }
}
