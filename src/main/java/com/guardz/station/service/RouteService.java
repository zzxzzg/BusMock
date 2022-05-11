package com.guardz.station.service;

import com.guardz.station.domain.Route;
import com.guardz.station.domain.RouteBuilder;
import com.guardz.station.domain.RoutePoint;
import com.guardz.station.domain.Stations;
import com.guardz.exception.CalculateRouteException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Created: lidong on 2022/5/10 4:14 PM
 * @Description: 路线服务
 * @version: 1.0.0
 */
public class RouteService {

    private StationsService stationsService;

    private Map<String, Route> routeMap = new HashMap<>();

    public RouteService(StationsService stationsService) {
        this.stationsService = stationsService;
        init();
    }

    /**
     * 创建联调线路，扩展可以从配置文件读取配置，根据配置生成线路
     */
    public void init(){
        Route route1 = new RouteBuilder().addRoutePoint(
                new RoutePoint(stationsService.getStationsByName("01"), stationsService.getStationsByName("02"), 5 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("02"), stationsService.getStationsByName("03"), 6 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("03"), stationsService.getStationsByName("04"), 7 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("04"), stationsService.getStationsByName("05"), 8 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("05"), stationsService.getStationsByName("06"), 4 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("06"), stationsService.getStationsByName("07"), 3 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("07"), stationsService.getStationsByName("08"), 6 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("08"), stationsService.getStationsByName("09"), 5 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("09"), stationsService.getStationsByName("10"), 6 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("10"), stationsService.getStationsByName("11"), 7 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("11"), stationsService.getStationsByName("12"), 4 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("12"), stationsService.getStationsByName("13"), 3 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("13"), stationsService.getStationsByName("14"), 6 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("14"), stationsService.getStationsByName("15"), 3 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("15"), null, 0))
            .addName("Route1")
            .build();

        Route route2 = new RouteBuilder().addRoutePoint(
                new RoutePoint(stationsService.getStationsByName("15"), stationsService.getStationsByName("14"), 4 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("14"), stationsService.getStationsByName("13"), 5 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("13"), stationsService.getStationsByName("12"), 4 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("12"), stationsService.getStationsByName("11"), 5 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("11"), stationsService.getStationsByName("10"), 4 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("10"), stationsService.getStationsByName("09"), 7 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("09"), stationsService.getStationsByName("08"), 3 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("08"), stationsService.getStationsByName("07"), 5 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("07"), stationsService.getStationsByName("06"), 4 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("06"), stationsService.getStationsByName("05"), 3 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("05"), stationsService.getStationsByName("04"), 6 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("04"), stationsService.getStationsByName("03"), 5 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("03"), stationsService.getStationsByName("02"), 7 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("02"), stationsService.getStationsByName("01"), 4 * 60))
            .addRoutePoint(new RoutePoint(stationsService.getStationsByName("01"), null, 0))
            .addName("Route2")
            .build();

        routeMap.put(route1.getRouteName(), route1);
        routeMap.put(route2.getRouteName(), route2);
    }

    public Route getRoute1() {
        return routeMap.get("Route1");
    }

    public Route getRoute2() {
        return routeMap.get("Route2");
    }

    /**
     * 通过起点到终点寻找一条路线
     *
     * @param start 起点
     * @param end   终点
     */
    public Route calculateRoute(Stations start, Stations end) throws CalculateRouteException{
        Route fastestRoute = null;
        int time = Integer.MAX_VALUE;
        for (Route route : routeMap.values()) {
            try {
                int cost = route.getCostTime(start, end);
                if (cost < time) {
                    time = cost;
                    fastestRoute = route;
                }
            } catch (Exception e) {
                // TODO
            }
        }
        if (fastestRoute == null) {
            throw new CalculateRouteException(start, end);
        }
        return fastestRoute;
    }
}
