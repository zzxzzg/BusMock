package com.guardz.station.manager;

import com.guardz.station.domain.Stations;

import java.util.HashMap;
import java.util.Map;

/**
 * @Created: lidong on 2022/5/10 4:47 PM
 * @Description: 站点源数据，当前写死15个站，且站名01~15。扩展可从数据库或者其他等数据源获取
 * @version: 1.0.0
 */
public class StationsManager {

    public Map<String, Stations> queryStations(){
        Map<String, Stations> stationsMap = new HashMap<>();
        stationsMap.put("01", new Stations("01"));
        stationsMap.put("02", new Stations("02"));
        stationsMap.put("03", new Stations("03"));
        stationsMap.put("04", new Stations("04"));
        stationsMap.put("05", new Stations("05"));
        stationsMap.put("06", new Stations("06"));
        stationsMap.put("07", new Stations("07"));
        stationsMap.put("08", new Stations("08"));
        stationsMap.put("09", new Stations("09"));
        stationsMap.put("10", new Stations("10"));
        stationsMap.put("11", new Stations("11"));
        stationsMap.put("12", new Stations("12"));
        stationsMap.put("13", new Stations("13"));
        stationsMap.put("14", new Stations("14"));
        stationsMap.put("15", new Stations("15"));
        return stationsMap;
    }
}
