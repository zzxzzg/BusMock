package com.guardz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Created: lidong
 * @Description: 世界配置
 * @version: 1.0.0
 */
public class WorldConfig {
    /**
     * 世界运行的默认步长，最大为所有事件耗时的最大公约数。
     * PS: 步长太小会导致运行时间延长。
     */
    public static final int DEFAULT_WORLD_STEP = 10;

    /**
     * 每 DEFAULT_RANDOM_TIME 分钟 DEFAULT_RANDOM_COUNT 个乘客
     */
    public static final int DEFAULT_PASSENGER_RANDOM_TIME = 10 * 60;

    public static final int DEFAULT_PASSENGER_RANDOM_COUNT = 5;

    /**
     * A 路公交数量
     */
    public static final int DEFAULT_A_BUS_COUNT = 5;

    /**
     * B 路公交数量
     */
    public static final int DEFAULT_B_BUS_COUNT = 5;
    /**
     * A 路公交发车间隔
     */
    public static final int DEFAULT_A_BUS_START_INTERVAL = 15 * 60;

    /**
     * B 路公交发车间隔
     */
    public static final int DEFAULT_B_BUS_START_INTERVAL = 15 * 60;

    /**
     * 默认的故障率  1/{DEFAULT_BROKEN_RATE}
     */
    public static final int DEFAULT_BROKEN_RATE = 10;

    /**
     * 默认下车时间
     */
    public static final int DEFAULT_GET_OFF_TIME = 10;

    /**
     * 默认下车时间
     */
    public static final int DEFAULT_GET_ON_TIME = 10;

    /**
     * 公交最大载荷
     */
    public static final int DEFAULT_MAX_PASSENGER = 29;
}
