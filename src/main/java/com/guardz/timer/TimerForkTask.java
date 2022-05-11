package com.guardz.timer;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @Created: lidong on 2022/5/11 7:09 PM
 * @Description: 世界Tick Fork Join任务
 * @version: 1.0.0
 */
public class TimerForkTask extends RecursiveTask<Void> {
    private final List<? extends ITimer> timerList;
    private final int worldTime;
    private final int offset;

    public TimerForkTask(List<? extends ITimer> busList, int offset, int worldTime) {
        this.timerList = busList;
        this.offset = offset;
        this.worldTime = worldTime;
    }

    @Override
    protected Void compute() {
        if (timerList.size() > 1) {
            int mid = timerList.size() / 2;
            TimerForkTask taskLeft = new TimerForkTask(timerList.subList(0, mid), offset, worldTime);
            TimerForkTask taskRight = new TimerForkTask(timerList.subList(mid, timerList.size()), offset, worldTime);
            taskLeft.fork();
            taskRight.fork();
            taskLeft.join();
            taskRight.join();
            return null;
        } else if(timerList.size() == 1){
            timerList.get(0).onTick(offset, worldTime);
        }
        return null;
    }
}
