package com.guardz;

import com.guardz.bus.controller.BusCenter;
import com.guardz.material.MaterialDispatcher;
import com.guardz.station.controller.StationCenter;
import com.guardz.bus.creator.IBusCreator;
import com.guardz.passanger.creator.IPassengerCreator;
import com.guardz.bus.creator.SimpleBusCreator;
import com.guardz.passanger.creator.SimplePassengerCreator;
import com.guardz.timer.TimerExecutor;
import com.guardz.station.manager.StationsManager;
import com.guardz.station.service.RouteService;
import com.guardz.station.service.StationsService;

/**
 * @Created: lidong on 2022/5/10 7:54 PM
 * @Description: TODO
 * @version: 1.0.0
 */
public class MockEngine {
    public static final int DEFAULT_MAX_TIME = 300 * 60;
    private TimerExecutor timerExecutor;

    private BusCenter busCenter;
    public void init() {
        timerExecutor = new TimerExecutor(DEFAULT_MAX_TIME);
        busCenter = new BusCenter();

        StationsManager stationsManager = new StationsManager();
        StationsService stationsService = new StationsService(stationsManager);
        RouteService routeService = new RouteService(stationsService);

        MaterialDispatcher materialDispatcher = new MaterialDispatcher(busCenter);

        IPassengerCreator passengerCreator =
            new SimplePassengerCreator(stationsService, routeService, materialDispatcher);
        IBusCreator busCreator = new SimpleBusCreator(routeService, materialDispatcher);

        StationCenter stationCenter = new StationCenter(stationsService);

        timerExecutor.addTimer(passengerCreator);
        timerExecutor.addTimer(busCreator);
        timerExecutor.addTimer(materialDispatcher);
        timerExecutor.addTimer(busCenter);
        timerExecutor.addTimer(stationCenter);
    }

    public void start() {
        Thread thread = new Thread(timerExecutor);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        busCenter.printRecord();
    }
}
