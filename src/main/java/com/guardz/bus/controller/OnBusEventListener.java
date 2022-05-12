package com.guardz.bus.controller;

import com.guardz.bus.domain.Bus;
import com.guardz.passenger.domain.Passenger;
import com.guardz.station.domain.Stations;

/**
 * @Created: lidong on 2022/5/12 10:56 AM
 * @Description: 公交状态变更监听
 * @version: 1.0.0
 */
public interface OnBusEventListener {
    /**
     * 乘客上车
     * @param passenger
     * @param stations
     * @param worldTime
     */
    void onPassengerGetOn(Bus bus, Passenger passenger, Stations stations, int worldTime);

    /**
     * 乘客下车
     * @param passenger
     * @param stations
     * @param worldTime
     */
    void onPassengerGetOff(Bus bus, Passenger passenger, Stations stations, int worldTime);

    /**
     * 公交开始运营
     * @param worldTime
     */
    void onBusStartBusiness(Bus bus, int worldTime);

    /**
     * 公交到达某个站点,开始下客
     * @param worldTime
     */
    void onBusArrived(Bus bus, Stations stations, int worldTime);

    /**
     * 公交停靠某个站点，开始上客
     * @param worldTime
     */
    void onBusStop(Bus bus, Stations stations, int worldTime);

    /**
     * 公交在某个站点抛锚
     * @param worldTime
     */
    void onBusBroken(Bus bus, Stations stations, int worldTime);

    /**
     * 公交从站点出发
     * @param worldTime
     */
    void onBusStart(Bus bus, Stations stations, int nextStationDistance, int worldTime);
}
