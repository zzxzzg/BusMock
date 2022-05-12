package com.guardz;

import com.guardz.util.ThreadUtil;

/**
 * @Description: 入口
 * @version: 1.0.0
 */
public class MainClass {

    private static final int LOOP_TIME = 100;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < LOOP_TIME; i++) {
            MockEngine mockEngine = new MockEngine();
            mockEngine.init();
            mockEngine.start(LOOP_TIME == 1);
        }

        long endTime = System.currentTimeMillis();
        long spend = (endTime - startTime) / LOOP_TIME;

        System.out.println("平均耗时:" + spend + "ms");
        ThreadUtil.executorService.shutdown();
//        System.exit(0);
    }

}
