package com.guardz.passanger.domain;

import com.guardz.station.domain.Route;
import com.guardz.station.domain.Stations;

/**
 * @Created: lidong on 2022/5/10 3:51 PM
 * @Description: 乘客
 * @version: 1.0.0
 */
public class Passenger {

    private Stations start;

    private Stations end;

    private Route route;

    /**
     * 产生的世界时间
     */
    private int createTime;

    private String name;

    public int getCreateTime() {
        return createTime;
    }

    public Stations getStart() {
        return start;
    }

    public Stations getEnd() {
        return end;
    }

    public Route getRoute() {
        return route;
    }

    public String getName() {
        return name;
    }

    public static class PassengerBuilder{
        private Stations start;

        private Stations end;

        private Route route;

        /**
         * 产生的世界时间
         */
        private int createTime;

        private String name;

        public PassengerBuilder setStart(Stations start) {
            this.start = start;
            return this;
        }

        public PassengerBuilder setEnd(Stations end) {
            this.end = end;
            return this;
        }

        public PassengerBuilder setRoute(Route route) {
            this.route = route;
            return this;
        }

        public PassengerBuilder setCreateTime(int createTime) {
            this.createTime = createTime;
            return this;
        }

        public PassengerBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Passenger build(){
            Passenger passenger = new Passenger();
            passenger.start = start;
            passenger.end = end;
            passenger.route = route;
            passenger.name = name;
            passenger.createTime = createTime;
            return passenger;
        }
    }
}
