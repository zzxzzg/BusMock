package com.guardz.bus.creator;

import com.guardz.WorldConfig;
import com.guardz.material.IBusDispatcher;
import com.guardz.bus.domain.Bus;
import com.guardz.station.service.RouteService;

/**
 * @Created: lidong on 2022/5/11 11:03 AM
 * @Description: 汽车物料生产实现类, 简单规则，系统开始生成指定数量公交
 * @version: 1.0.0
 */
public class SimpleBusCreator implements IBusCreator {
    private RouteService routeService;
    private IBusDispatcher busDispatcher;

    public SimpleBusCreator(RouteService routeService, IBusDispatcher busDispatcher) {
        this.routeService = routeService;
        this.busDispatcher = busDispatcher;
    }

    @Override
    public void onWorldStart() {
        createBus();
    }

    @Override
    public void onTick(int offset, int worldTime) {
        if (worldTime != 0) {
            return;
        }
    }

    private void createBus(){
        String prefixBus = "A路公交";
        int startTime = 0;
        for (int i = 0; i < WorldConfig.DEFAULT_A_BUS_COUNT; i++) {
            Bus.BusBuilder busBuilder = new Bus.BusBuilder();
            busBuilder.setId(prefixBus + i);
            busBuilder.setStartTime(startTime);
            busBuilder.setForwardRoute(routeService.getRoute1());
            busBuilder.setBackRoute(routeService.getRoute2());
            Bus bus = busBuilder.build();
            startTime += WorldConfig.DEFAULT_A_BUS_START_INTERVAL;
            busDispatcher.addBus(bus);
        }

        prefixBus = "B路公交";
        startTime = 0;
        for (int i = 0; i < WorldConfig.DEFAULT_B_BUS_COUNT; i++) {
            Bus.BusBuilder busBuilder = new Bus.BusBuilder();
            busBuilder.setId(prefixBus + i);
            busBuilder.setStartTime(startTime);
            busBuilder.setForwardRoute(routeService.getRoute2());
            busBuilder.setBackRoute(routeService.getRoute1());
            Bus bus = busBuilder.build();
            startTime += WorldConfig.DEFAULT_B_BUS_START_INTERVAL;
            busDispatcher.addBus(bus);
        }
    }
}
