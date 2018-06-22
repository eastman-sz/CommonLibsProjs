package com.ble.lib.f;

import com.ble.lib.impl.CommonBleServiceListener;
import com.ble.lib.impl.Ld1BleServiceListener;
import com.ble.lib.impl.Ld2BleServiceListener;
import com.ble.lib.impl.LdShoesBleServiceListener;
import com.ble.lib.impl.LdTreadMillBoxBleServiceListener;
import com.ble.lib.impl.ShuHuaBleServiceListener;
import java.util.ArrayList;
/**
 * Created by E on 2017/12/7.
 */
public class BleCallBackControl {

    private final static BleCallBackControl instance = new BleCallBackControl();

    private ArrayList<BleServiceListener> serviceListeners = new ArrayList<>();

    private ArrayList<BleStateListener> stateListeners = new ArrayList<>();

    private ArrayList<BleDataReceiveListener> dataReceiveListeners = new ArrayList<>();

    private final static Object object = new Object();

    private BleCallBackControl() {
        serviceListeners.clear();
        serviceListeners.add(new CommonBleServiceListener());
//        serviceListeners.add(new Ld1BleServiceListener());
//        serviceListeners.add(new Ld2BleServiceListener());
//        serviceListeners.add(new LdTreadMillBoxBleServiceListener());
//        serviceListeners.add(new ShuHuaBleServiceListener());
//        serviceListeners.add(new LdShoesBleServiceListener());
    }

    public static BleCallBackControl getInstance(){
        return instance;
    }

    public BleCallBackControl registerServiceListener(BleServiceListener bleServiceListener){
        if (null == bleServiceListener){
            return instance;
        }
        synchronized (object){
            synchronized (bleServiceListener.getClass()){
                if (serviceListeners.contains(bleServiceListener)){
                    return instance;
                }
                serviceListeners.add(bleServiceListener);
            }
        }
        return instance;
    }

    public BleCallBackControl unRegisterServiceListener(BleServiceListener bleServiceListener){
        if (null == bleServiceListener){
            return instance;
        }
        synchronized (object){
            synchronized (bleServiceListener.getClass()){
                if (serviceListeners.contains(bleServiceListener)){
                    serviceListeners.remove(bleServiceListener);
                }
            }
        }
        return instance;
    }

    public ArrayList<BleServiceListener> getServiceListeners(){
        synchronized (serviceListeners){
            return serviceListeners;
        }
    }

    public BleCallBackControl registerBleStateListener(BleStateListener bleStateListener){
        if (null == bleStateListener){
            return instance;
        }
        synchronized (object){
            synchronized (bleStateListener.getClass()){
                if (stateListeners.contains(bleStateListener)){
                    return instance;
                }
                stateListeners.add(bleStateListener);
            }
        }
        return instance;
    }

    public BleCallBackControl unRegisterBleStateListener(BleStateListener bleStateListener){
        if (null == bleStateListener){
            return instance;
        }
        synchronized (object){
            synchronized (bleStateListener.getClass()){
                if (stateListeners.contains(bleStateListener)){
                    stateListeners.remove(bleStateListener);
                }
            }
        }
        return instance;
    }

    public ArrayList<BleStateListener> getStateListeners(){
        synchronized (stateListeners){
            return stateListeners;
        }
    }

    public BleCallBackControl registerBleDataReceiveListener(BleDataReceiveListener bleDataReceiveListener){
        if (null == bleDataReceiveListener){
            return instance;
        }
        synchronized (object){
            synchronized (bleDataReceiveListener.getClass()){
                synchronized (dataReceiveListeners){
                    if (dataReceiveListeners.contains(bleDataReceiveListener)){
                        return instance;
                    }
                    dataReceiveListeners.add(bleDataReceiveListener);
                }
            }
        }
        return instance;
    }

    public BleCallBackControl unRegisterBleDataReceiveListener(BleDataReceiveListener bleDataReceiveListener){
        if (null == bleDataReceiveListener){
            return instance;
        }
        synchronized (object){
            synchronized (bleDataReceiveListener.getClass()){
                synchronized (dataReceiveListeners){
                    if (dataReceiveListeners.contains(bleDataReceiveListener)){
                        dataReceiveListeners.remove(bleDataReceiveListener);
                    }
                }
            }
        }
        return instance;
    }

    public ArrayList<BleDataReceiveListener> getDataReceiveListeners(){
        synchronized (dataReceiveListeners){
            return dataReceiveListeners;
        }
    }

}
