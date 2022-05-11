package com.guardz.bus.controller;

import com.guardz.bus.domain.Bus;
import com.guardz.timer.ITimer;
import com.guardz.timer.TimerForkTask;
import com.guardz.timer.WorldStartForkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @Created: lidong on 2022/5/11 2:13 PM
 * @Description: 公交调度中心，fork join 并行刷新
 * @version: 1.0.0
 */
public class BusCenter implements ITimer {

    private final List<Bus> busList = new ArrayList<>();

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public void addBus(Bus bus) {
        busList.add(bus);
    }

    public void printRecord() {
        busList.forEach(bus -> {
            String record = bus.getRecord();
            System.out.println(record);
        });
    }

    @Override
    public void onWorldStart() {
        forkJoinPool.invoke(new WorldStartForkTask(busList));
    }

    @Override
    public void onTick(int offset, int worldTime) {
        forkJoinPool.invoke(new TimerForkTask(busList, offset, worldTime));
    }

}
