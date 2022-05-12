package com.guardz.bus.domain;

/**
 * @Created: lidong on 2022/5/11 6:27 PM
 * @Description: 时间记录
 * @version: 1.0.0
 */
public class Record {
    public String name;

    public int time;

    public String event;

    @Override
    public String toString() {
        return "name='" + name + '\'' + ", time=" + time + ", event='" + event + '\'' + '\n';
    }
}
