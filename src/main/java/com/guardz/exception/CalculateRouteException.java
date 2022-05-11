package com.guardz.exception;

import com.guardz.station.domain.Stations;

/**
 * @Created: lidong on 2022/5/10 5:15 PM
 * @Description: 不可达错误
 * @version: 1.0.0
 */
public class CalculateRouteException extends RuntimeException {

    public CalculateRouteException(Stations start, Stations end) {
        super("calculate " + start.stationsName + "->" + end.stationsName + " fail!");
    }
}
