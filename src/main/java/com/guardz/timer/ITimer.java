package com.guardz.timer;

/**
 * @Description:
 * @version: 1.0.0
 */
public interface ITimer {
    /**
     * 世界开始转动
     */
    void onWorldStart();

    /**
     * tick
     * @param offset 和上一次调度的时间差
     * @param worldTime 世界时间
     */
    void onTick(int offset, int worldTime);
}
