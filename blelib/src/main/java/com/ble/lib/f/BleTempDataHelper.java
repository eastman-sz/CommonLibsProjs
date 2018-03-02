package com.ble.lib.f;

import java.util.HashMap;
/**
 * Created by E on 2017/12/11.
 */
public class BleTempDataHelper {

    private final static BleTempDataHelper dataHelper = new BleTempDataHelper();

    private HashMap<String , HashMap<String , String>> dataMap = new HashMap<>();

    private BleTempDataHelper(){
    }

    public static BleTempDataHelper getInstance(){
        return dataHelper;
    }

    public void putData(String address , String uuid , String value){
        if (dataMap.containsKey(address)){
            HashMap<String , String> map = dataMap.get(address);
            map.put(uuid , value);

            dataMap.put(address , map);

            return;
        }
        HashMap<String , String> map = new HashMap<>();
        map.put(uuid , value);

        dataMap.put(address , map);
    }

    public String getData(String address , String uuid){
        if (!dataMap.containsKey(address)){
            return null;
        }
        HashMap<String , String> map = dataMap.get(address);
        if (map.containsKey(uuid)){
            return map.get(uuid);
        }
        return null;
    }

    public void clear(String address){
        if (!dataMap.containsKey(address)){
            return;
        }
        dataMap.get(address).clear();
    }

    public void clear(){
        dataMap.clear();
    }

}
