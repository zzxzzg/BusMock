package com.guardz.timer;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @Created: lidong on 2022/5/11 7:10 PM
 * @Description: 世界开始Fork Join任务
 * @version: 1.0.0
 */
public class WorldStartForkTask extends RecursiveTask<Void> {

    public List<? extends ITimer> timerList;
    public WorldStartForkTask(List<? extends ITimer> timerList){
        this.timerList = timerList;
    }

    @Override
    protected Void compute() {
        if (timerList.size() > 1) {
            int mid = timerList.size() / 2;
            WorldStartForkTask taskLeft = new WorldStartForkTask(timerList.subList(0, mid));
            WorldStartForkTask taskRight = new WorldStartForkTask(timerList.subList(mid, timerList.size()));
            taskLeft.fork();
            taskRight.fork();
            taskLeft.join();
            taskRight.join();
            return null;
        } else if(timerList.size() == 1){
            timerList.get(0).onWorldStart();
        }
        return null;
    }
}
