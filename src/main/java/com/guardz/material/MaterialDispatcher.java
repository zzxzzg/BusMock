package com.guardz.material;

import com.guardz.bus.controller.BusCenter;
import com.guardz.bus.domain.Bus;
import com.guardz.passanger.domain.Passenger;
import com.guardz.timer.ITimer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Created: lidong on 2022/5/11 10:46 AM
 * @Description: 物料分发器
 * @version: 1.0.0
 */
public class MaterialDispatcher implements IBusDispatcher, IPassengerDispatcher, ITimer {
    /**
     * 等待调度的乘客
     */
    private List<Passenger> passengersWaitDispatch = new ArrayList<>();

    /**
     * 等待调度的公交
     */
    private List<Bus> busesWaitDispatch = new ArrayList<>();

    private BusCenter busCenter;

    public MaterialDispatcher(BusCenter busCenter){
        this.busCenter = busCenter;
    }

    @Override
    public void addBus(Bus bus) {
        busesWaitDispatch.add(bus);
    }

    @Override
    public void addPassenger(Passenger passenger) {
        passengersWaitDispatch.add(passenger);
    }

    @Override
    public void onWorldStart() {
        onTick(0,0);
    }

    @Override
    public void onTick(int offset, int worldTime) {
        dispatchPassenger(worldTime);
        dispatchBus(worldTime);
    }

    /**
     * 乘客分发，相对比较简单，将到达分发时间的乘客，放入对应的车站队列中
     */
    private void dispatchPassenger(int worldTime) {
        Iterator<Passenger> it = passengersWaitDispatch.iterator();
        while (it.hasNext()) {
            Passenger passenger = it.next();
            if (passenger.getCreateTime() <=  worldTime){
                passenger.getStart().addPassenger(passenger);
                it.remove();
            }
        }
    }

    /**
     * 调度待发公交，到了发车时间就把公交放入运行表中。
     * @param worldTime
     */
    private void dispatchBus(int worldTime){
        Iterator<Bus> it = busesWaitDispatch.iterator();
        while (it.hasNext()) {
            Bus bus = it.next();
            if (bus.getStartTime() <=  worldTime){
                busCenter.addBus(bus);
                it.remove();
            }
        }
    }
}
