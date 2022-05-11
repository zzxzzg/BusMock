package com.guardz.util;

import java.util.Random;

/**
 * @Created: lidong on 2022/5/10 6:49 PM
 * @Description: 随机数工具
 * @version: 1.0.0
 */
public class RandomUtil {
    private static final Random random = new Random(System.currentTimeMillis());

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
