package com.guardz.station.domain;

/**
 * @Created: lidong on 2022/5/10 5:14 PM
 * @Description: 路径节点
 * @version: 1.0.0
 */
public class RoutePoint {
    /**
     * 节点当前站点
     */
    private final Stations stations;

    /**
     * 下一站
     */
    private final Stations nextStations;

    /**
     * 下一站时间 单位秒
     */
    private final int nextStationsDistance;

    public RoutePoint(Stations stations, Stations nextStations, int nextStationsDistance) {
        this.stations = stations;
        this.nextStations = nextStations;
        this.nextStationsDistance = nextStationsDistance;
    }

    public boolean isLastStations(){
        return nextStations == null;
    }

    public Stations getStations() {
        return stations;
    }

    public Stations getNextStations() {
        return nextStations;
    }

    public int getNextStationsDistance() {
        return nextStationsDistance;
    }
}
