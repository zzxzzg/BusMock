package com.guardz.bus.domain;

import com.guardz.bus.controller.BusController;
import com.guardz.timer.ITimer;
import com.guardz.station.domain.Route;
import com.guardz.passanger.domain.Passenger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created: lidong on 2022/5/10 3:48 PM
 * @Description: 公交车类
 * @version: 1.0.0
 */
public class Bus implements ITimer {
    private static final int DEFAULT_MAX_PASSENGER = 29;
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
     * 总乘客
     */
    private int totalPassengerCount;

    /**
     * 当前乘客
     */
    private List<Passenger> currentPassengers = new ArrayList<>();

    private BusController busController;

    private List<Record> records = new ArrayList<>();

    public Bus() {
        busController = new BusController(this);
    }

    public void addRecord(int worldTime, String event){
        Record record = new Record();
        record.name = id;
        record.time = worldTime;
        record.event = event;
        records.add(record);
    }

    public String getRecord(){
        StringBuffer stringBuffer = new StringBuffer();
        try {
            records.forEach(record -> stringBuffer.append(record.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @Override
    public void onWorldStart() {
        busController.tick(0, 0);
    }

    @Override
    public void onTick(int offset, int worldTime) {
        busController.tick(offset, worldTime);
    }

    public boolean isStop(){
        return busController.isStop();
    }

    public boolean isFull() {
        if (currentPassengers.size() >= DEFAULT_MAX_PASSENGER) {
            return true;
        }
        return false;
    }

    public boolean isGettingOn() {
        return busController.isGettingOn();
    }

    public void getOn(Passenger passenger) {
        currentPassengers.add(passenger);
        totalPassengerCount++;
        busController.getOn(passenger);
    }

    /**
     * 停靠结束，发车
     */
    public void start(int worldTime) {
        busController.start(worldTime);
    }

    // 获取当前线路
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

    public List<Passenger> getCurrentPassengers() {
        return currentPassengers;
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
