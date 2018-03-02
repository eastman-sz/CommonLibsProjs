package com.ble.lib.f;

import java.util.HashMap;
import android.util.SparseArray;
/**
 * 设备类型控制。每一种类型只能连接一个。
 * @author E
 */
public class BleTypeControl {

	private static BleTypeControl bleTypeControl = null;
	
	//类型------地址
	private SparseArray<String> type_address_array = new SparseArray<String>();
	//地址------类型
	private HashMap<String, Integer> address_type_map = new HashMap<String, Integer>();
	
	public static BleTypeControl getInstance(){
		if (null == bleTypeControl) {
			bleTypeControl = new BleTypeControl();
		}
		return bleTypeControl;
	}
	
	public void addDevice(int dev_type , String address){
		type_address_array.put(dev_type, address);
		address_type_map.put(address, dev_type);
	}
	
	public boolean isDevTypeExist(int dev_type){
		return null != type_address_array.get(dev_type);
	}
	
	public boolean isDevTypeExist(String address){
		return address_type_map.containsKey(address);
	}
	
	public String getPreAddressByDevType(int dev_type){
		return isDevTypeExist(dev_type) ? type_address_array.get(dev_type) : null;
	}
	
	public void removePreRecord(int dev_type){
		if (!isDevTypeExist(dev_type)) {
			return;
		}
		String pre_address = type_address_array.get(dev_type);
		
		type_address_array.remove(dev_type);
		address_type_map.remove(pre_address);
	}
	
	public void removePreRecord(String address){
		if (!isDevTypeExist(address)) {
			return;
		} 
		int dev_type = address_type_map.get(address);
		
		type_address_array.remove(dev_type);
		address_type_map.remove(address);
	}
	
	public int getDevType(String address){
		return address_type_map.containsKey(address) ? address_type_map.get(address) : 0;
	}
	
	public void clearAddressMapping(String address){
		if (address_type_map.containsKey(address)) {
			address_type_map.remove(address);
		}
	}
	
}
