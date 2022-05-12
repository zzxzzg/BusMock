package com.guardz.bus.domain;

import com.guardz.bus.controller.OnBusEventListener;
import com.guardz.passenger.domain.Passenger;
import com.guardz.station.domain.Stations;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created: lidong on 2022/5/12 10:55 AM
 * @Description: 攻击运行记录统计
 * @version: 1.0.0
 */
public class BusStatistics implements OnBusEventListener {
    /**
     * 总乘客
     */
    private int totalPassengerCount;

    /**
     * 当前车站上车人数
     */
    private int currentStationPassengerGettingOnCount;

    /**
     * 当前车站下车人数
     */
    private int currentStationPassengerGettingOffCount;

    /**
     * 总运行时间
     */
    private int totalBusinessTime = -1;

    /**
     * 总行驶时间
     */
    private int totalDriveTime;



    /**
     * 运行日志
     */
    private final List<Record> records = new ArrayList<>();

    private final Bus target;

    public BusStatistics(Bus target) {
        this.target = target;
    }

    public String getBusStatistics(int worldTime){
        int totalTime = totalBusinessTime;
        if (totalBusinessTime == -1){
            totalTime = worldTime - target.getStartTime();
        }
        return target.getId() + " 总运营时间:" + totalTime +" 总行驶时间:"+totalDriveTime + " 总载客人数:" + totalPassengerCount;
    }

    public String getRecordDetail(){
        StringBuffer buffer = new StringBuffer();
        records.forEach(r -> buffer.append(r.toString()));
        return buffer.toString();
    }

    @Override
    public void onPassengerGetOn(Bus bus, Passenger passenger, Stations stations, int worldTime) {
        totalPassengerCount++;
        currentStationPassengerGettingOnCount++;
        Record record = new Record();
        record.time = worldTime;
        record.name = bus.getId();
        record.event = "乘客: " + passenger.getName() + "在车站:" + stations.stationsName + " 上车了";
        records.add(record);
    }

    @Override
    public void onPassengerGetOff(Bus bus, Passenger passenger, Stations stations, int worldTime) {
        currentStationPassengerGettingOffCount++;
        Record record = new Record();
        record.time = worldTime;
        record.name = bus.getId();
        record.event = "乘客: " + passenger.getName() + "在车站:" + stations.stationsName + " 下车了";
        records.add(record);
    }

    @Override
    public void onBusStartBusiness(Bus bus, int worldTime) {
        Record record = new Record();
        record.time = worldTime;
        record.name = bus.getId();
        record.event = "公交开始运营了";
        records.add(record);
    }

    @Override
    public void onBusArrived(Bus bus, Stations stations, int worldTime) {
        Record record = new Record();
        record.time = worldTime;
        record.name = bus.getId();
        record.event = "公交抵达站点:" + stations.stationsName;
        records.add(record);
    }

    @Override
    public void onBusStop(Bus bus, Stations stations, int worldTime) {
        Record record = new Record();
        record.time = worldTime;
        record.name = bus.getId();
        record.event = "公交停靠站点:" + stations.stationsName;
        records.add(record);
    }

    @Override
    public void onBusBroken(Bus bus, Stations stations, int worldTime) {
        Record record = new Record();
        record.time = worldTime;
        record.name = bus.getId();
        record.event = "公交在站点:" + stations.stationsName + " 抛锚了";
        records.add(record);
        totalBusinessTime = worldTime - bus.getStartTime();
    }

    @Override
    public void onBusStart(Bus bus, Stations stations, int nextStationDistance, int worldTime) {
        Record record = new Record();
        record.time = worldTime;
        record.name = bus.getId();
        record.event = "公交在本站下车 " + currentStationPassengerGettingOffCount + " 人，上车 " + currentStationPassengerGettingOnCount + " 人";
        records.add(record);
        currentStationPassengerGettingOffCount = 0;
        currentStationPassengerGettingOnCount = 0;

        Record record2 = new Record();
        record2.time = worldTime;
        record2.name = bus.getId();
        record2.event = "公交出发，前往站点:" + stations.stationsName;
        records.add(record2);
        totalDriveTime += nextStationDistance;
    }
}
