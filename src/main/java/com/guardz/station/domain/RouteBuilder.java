package com.guardz.station.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * @Created: lidong on 2022/5/10 5:14 PM
 * @Description: TODO
 * @version: 1.0.0
 */
public class RouteBuilder {
    private ArrayList<RoutePoint> routePointList = new ArrayList<>();
    private String routeName;

    public RouteBuilder addRoutePoint(RoutePoint point) {
        if (point != null) {
            routePointList.add(point);
        }
        return this;
    }

    public RouteBuilder addName(String routeName) {
        this.routeName = routeName;
        return this;
    }

    public Route build() {
        return new Route(routePointList, StringUtils.isBlank(routeName) ? "default" : routeName);
    }
}
