package com.guardz.station.domain;

import com.guardz.exception.UnreachableException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @Created: lidong on 2022/5/10 3:52 PM
 * @Description: 路线图
 * @version: 1.0.0
 */
public class Route {
    /**
     * 节点列表,有序。 一条线路由多个节点组成
     */
    private final ArrayList<RoutePoint> routePoints;

    /**
     * 路线名
     */
    private final String routeName;

    protected Route(ArrayList<RoutePoint> routePoints, String routeName) {
        this.routePoints = routePoints;
        this.routeName = routeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Route route = (Route)o;

        return new EqualsBuilder().append(routeName, route.routeName)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(routeName)
            .toHashCode();
    }

    public String getRouteName() {
        return routeName;
    }

    public RoutePoint getStartPoint() {
        return routePoints.get(0);
    }

    public RoutePoint getCurrentPoint(Stations currentStations) {
        return routePoints.stream()
            .filter(routePoint -> routePoint.getStations() == currentStations)
            .findFirst()
            .orElse(null);
    }

    public boolean isLastStations(Stations currentStations){
        RoutePoint routePoint = routePoints.stream()
            .filter(rp -> rp.getStations() == currentStations)
            .findFirst()
            .orElse(null);
        return Optional.ofNullable(routePoint).map(RoutePoint::isLastStations).orElse(false);
    }

    /**
     * 从start到end理论上会花费的时间
     *
     * @param start
     * @param end
     * @return
     * @throws UnreachableException 站点不可达
     */
    public int getCostTime(Stations start, Stations end) throws UnreachableException {
        int cost = 0;
        int valid = 0;
        for (int i = 0; i < routePoints.size(); i++) {
            RoutePoint routePoint = routePoints.get(i);
            if (routePoint.getStations() == start && valid == 0) {
                valid = 1;
            }
            if (routePoint.getStations() == end && valid == 1) {
                valid = 2;
            }
            if (valid == 1) {
                cost += routePoint.getNextStationsDistance();
            }
        }
        if (valid != 2) {
            throw new UnreachableException(start, end);
        }
        return cost;
    }

}
