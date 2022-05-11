package com.guardz.station.service;

import com.guardz.station.domain.Stations;
import com.guardz.station.manager.StationsManager;
import com.guardz.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Created: lidong on 2022/5/10 7:00 PM
 * @Description: 站点服务
 * @version: 1.0.0
 */
public class StationsService {
    private final StationsManager stationsManager;

    private Map<String, Stations> stationsMap;

    private List<Stations> stationsList;

    public StationsService(StationsManager stationsManager) {
        this.stationsManager = stationsManager;
        init();
    }

    public void init(){
        stationsMap = stationsManager.queryStations();
        stationsList = new ArrayList<>(stationsMap.values());
    }

    /**
     * 获取一个随机站点
     */
    public Stations getRandomStations(){
        int index = RandomUtil.nextInt(stationsList.size());
        return stationsList.get(index);
    }

    public List<Stations> getAllStations(){
        return stationsList;
    }

    public Stations getStationsByName(String name){
        return stationsMap.get(name);
    }

}
