package com.ble.lib.dev;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import com.ble.lib.f.BleState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E on 2017/12/7.
 */
public class BleDevInfo extends BleDevGroup implements Comparable<BleDevInfo>{

    private String address = null;
    private String name = null;
    private String alias = null;
    private int rssi = 0;
    private int state = 0; //0-STATE_DISCONNECTED ,1-STATE_CONNECTING ,2-STATE_CONNECTED ,3-STATE_DISCONNECTING
    private int type = 0;
    private String version = null;
    private String sn = null;
    private int battery = 0;
    private long ctime = 0;

    private boolean focused = false;

    public BleDevInfo() {
    }

    public static BleDevInfo fromBluetoothDevice(BluetoothDevice device, int rssi) {
        BleDevInfo devInfo = new BleDevInfo();
        devInfo.setState(BleState.STATE_DISCONNECTED.getState());
        devInfo.setName(device.getName());
        devInfo.setAddress(device.getAddress());
        devInfo.setRssi(rssi);
        return devInfo;
    }

    public static void save(List<BleDevInfo> devInfos){
        BleDevDbHepler.save(devInfos);
    }

    public static void updateDevState(String address , String name , int state){
        BleDevDbHepler.updateDevState(address , name , state);
    }

    public static void updateDevState(String address , int state){
        BleDevDbHepler.updateDevState(address , state);
    }

    public static void updateAlias(String address , String alias){
        BleDevDbHepler.updateAlias(address , alias);
    }

    public static void updateDevType(String address , int type){
        BleDevDbHepler.updateDevType(address , type);
    }

    public static void updateSn(String address , String sn){
        BleDevDbHepler.updateSn(address , sn);
    }

    public static void updateVersion(String address , String version){
        BleDevDbHepler.updateVersion(address , version);
    }

    public static int getState(String address){
        return BleDevDbHepler.getState(address);
    }

    public static ArrayList<BleDevInfo> getBleDevs(){
        return BleDevDbHepler.getBleDevs();
    }

    public static ArrayList<BleDevInfo> getConnecttedBleDevs(){
        return BleDevDbHepler.getConnecttedBleDevs();
    }

    public static ArrayList<BleDevInfo> getActivedBleDevs(){
        return BleDevDbHepler.getActivedBleDevs();
    }

    public static String getLastConnectedAddress(){
        return BleDevDbHepler.getLastConnectedAddress();
    }

    public static BleDevInfo getDevInfo(String address){
        return BleDevDbHepler.getDevInfo(address);
    }

    public static void updateDevSearchInfo(String address , String name){
        BleDevSearchInfoDbHelper.updateDevSearchInfo(address , name);
    }

    public static String getDevSearchName(String address){
        return BleDevSearchInfoDbHelper.getDevSearchName(address);
    }

    public static void delete(String address){
        BleDevDbHepler.delete(address);
    }

    public static void delete(){
        BleDevDbHepler.delete();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BleDevInfo){
            BleDevInfo info = (BleDevInfo)obj;
            return info.getAddress().equals(this.address);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.address.hashCode();
    }

    @Override
    public int compareTo(@NonNull BleDevInfo o) {
        if (rssi > o.getRssi()) {
            return -1;
        }else if (rssi < o.getRssi()){
            return 1;
        }
        return 0;
    }
}
