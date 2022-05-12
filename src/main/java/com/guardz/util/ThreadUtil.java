package com.guardz.util;

import com.guardz.timer.ITimer;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Created: lidong on 2022/5/12 3:45 PM
 * @Description: TODO
 * @version: 1.0.0
 */
public class ThreadUtil {
    public static ExecutorService executorService = Executors.newFixedThreadPool(32);

    public static void executeWorldStart(Collection<? extends ITimer> list){
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        list.forEach(timer -> ThreadUtil.executorService.execute(() -> {
            timer.onWorldStart();
            countDownLatch.countDown();
        }));
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void executeOnTick(Collection<? extends ITimer> list, int offset, int worldTime){
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        list.forEach(obj -> ThreadUtil.executorService.execute(() -> {
            obj.onTick(offset, worldTime);
            countDownLatch.countDown();
        }));
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
