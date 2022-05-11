package com.guardz.timer;

import com.guardz.WorldConfig;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Created: lidong
 * @Description: 世界时间调度器
 * @version: 1.0.0
 */
public class TimerExecutor implements Runnable {
    /**
     * 最长模拟世界时间
     */
    private final int maxTime;

    /**
     * 当前世界时间 单位秒
     */
    private int worldTime = 0;

    /**
     * 需要响应世界时间变化的对象列表
     */
    public Set<ITimer> timerList = new LinkedHashSet<>();

    public TimerExecutor(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getWorldTime() {
        return worldTime;
    }

    public void addTimer(ITimer ITimer) {
        if (ITimer != null) {
            timerList.add(ITimer);
        }
    }

    @Override
    public void run() {
        timerList.forEach(ITimer::onWorldStart);
        while (worldTime < maxTime) {
            worldTime += WorldConfig.DEFAULT_WORLD_STEP;
            timerList.forEach(ITimer -> ITimer.onTick(WorldConfig.DEFAULT_WORLD_STEP, worldTime));
        }
    }
}
