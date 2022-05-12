package com.guardz.passenger.creator;

import com.guardz.WorldConfig;
import com.guardz.exception.CalculateRouteException;
import com.guardz.material.IPassengerDispatcher;
import com.guardz.passenger.domain.Passenger;
import com.guardz.station.domain.Route;
import com.guardz.station.domain.Stations;
import com.guardz.station.service.RouteService;
import com.guardz.station.service.StationsService;
import com.guardz.util.RandomUtil;

/**
 * @Created: lidong on 2022/5/11 10:44 AM
 * @Description: 乘客物料生产实现类, 乘客生产业务规则实现
 * @version: 1.0.0
 */
public class SimplePassengerCreator implements IPassengerCreator {
    private static final String PREFIX_NAME = "乘客";

    private int currentIndex = 0;

    private final StationsService stationsService;

    private final RouteService routeService;

    private final IPassengerDispatcher passengerDispatcher;

    public SimplePassengerCreator(StationsService stationsService, RouteService routeService,
        IPassengerDispatcher passengerDispatcher) {
        this.routeService = routeService;
        this.stationsService = stationsService;
        this.passengerDispatcher = passengerDispatcher;
    }

    @Override
    public void onWorldStart() {
        onTick(0, 0);
    }

    @Override
    public void onTick(int offset, int worldTime) {
        if (worldTime % WorldConfig.DEFAULT_PASSENGER_RANDOM_TIME == 0) {
            preCreatePassenger(worldTime);
        }
    }

    /**
     * 预创建，创建之后放入物料调度器
     */
    private void preCreatePassenger(int worldTimeSlot) {
        int i = WorldConfig.DEFAULT_PASSENGER_RANDOM_COUNT;
        while (i > 0) {
            int randomTime = RandomUtil.nextInt(WorldConfig.DEFAULT_PASSENGER_RANDOM_TIME);
            Passenger passenger = createPassenger(worldTimeSlot + randomTime);
            if (passenger != null) {
                passengerDispatcher.addPassenger(passenger);
                i--;
            }
        }
    }

    /**
     * 随机创建一个乘客
     */
    private Passenger createPassenger(int createTime) {
        Stations startStations = stationsService.getRandomStations();
        Stations endStations = stationsService.getRandomStations();
        while (startStations == endStations) {
            endStations = stationsService.getRandomStations();
        }
        try {
            Route route = routeService.calculateRoute(startStations, endStations);
            Passenger.PassengerBuilder builder = new Passenger.PassengerBuilder();
            builder.setStart(startStations);
            builder.setEnd(endStations);
            builder.setRoute(route);
            builder.setCreateTime(createTime);
            builder.setName(PREFIX_NAME + currentIndex);
            currentIndex++;
            return builder.build();
        } catch (CalculateRouteException e) {
            // TODO
        }
        return null;
    }
}
