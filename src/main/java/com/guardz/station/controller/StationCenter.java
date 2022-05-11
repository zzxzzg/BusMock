package com.guardz.station.controller;

import com.guardz.timer.TimerForkTask;
import com.guardz.timer.WorldStartForkTask;
import com.guardz.timer.ITimer;
import com.guardz.station.domain.Stations;
import com.guardz.station.service.StationsService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * @Created: lidong on 2022/5/11 2:13 PM
 * @Description: 站点调度中心，fork join 并行刷新
 * @version: 1.0.0
 */
public class StationCenter implements ITimer {

    private final Set<Stations> stationsSet = new LinkedHashSet<>();

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public StationCenter(StationsService stationsService) {
        stationsSet.addAll(stationsService.getAllStations());
    }

    private void addStations(Stations stations) {
        stationsSet.add(stations);
    }

    @Override
    public void onWorldStart() {
        forkJoinPool.invoke(new WorldStartForkTask(new ArrayList<>(stationsSet)));
    }

    @Override
    public void onTick(int offset, int worldTime) {
        forkJoinPool.invoke(new TimerForkTask(new ArrayList<>(stationsSet), offset, worldTime));
    }
}
