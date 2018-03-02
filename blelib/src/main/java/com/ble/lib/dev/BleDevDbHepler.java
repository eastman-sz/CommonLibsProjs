package com.ble.lib.dev;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.ble.lib.db.BleCursorHelper;
import com.ble.lib.db.BleSqliteDataBase;
import com.ble.lib.db.BleTableHelper;
import com.ble.lib.f.BleConfig;
import com.ble.lib.f.BleState;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by E on 2017/12/7.
 */
public class BleDevDbHepler {

    public static void save(List<BleDevInfo> devInfos){
        delete();

        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        db.beginTransaction();
        try{
            for (BleDevInfo devInfo : devInfos){
                ContentValues values = new ContentValues();
                int uid = BleConfig.getInstance().getUid();

                values.put("uid" , uid);
                values.put("address" , devInfo.getAddress());
                values.put("name" , devInfo.getName());
                values.put("alias" , devInfo.getAlias());
                values.put("rssi" , devInfo.getRssi());
                values.put("state" , devInfo.getState());
                values.put("type" , devInfo.getType());
                values.put("version" , devInfo.getVersion());
                values.put("sn" , devInfo.getSn());
                values.put("battery" , devInfo.getBattery());
                values.put("groupType" , devInfo.getGroup());
                values.put("ctime" , devInfo.getCtime());

                db.insert(DBNAME , null , values);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public static void updateDevState(String address , String name , int state){
        ContentValues values = new ContentValues();
        int uid = BleConfig.getInstance().getUid();
        values.put("uid" , uid);
        values.put("address" , address);
        if (!TextUtils.isEmpty(name)){
            values.put("name" , name);
        }
        values.put("state" , state);
        if (state == BleState.STATE_CONNECTED.getState()){
            values.put("ctime" , System.currentTimeMillis()/1000);
        }

        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        int count = db.update(DBNAME , values , "uid = ? and address = ? " ,
                new String[]{String.valueOf(uid) , address});
        if (count < 1){
            db.insert(DBNAME , null , values);
        }
    }

    public static void updateDevState(String address , int state){
            updateDevState(address , null , state);
    }

    public static void updateAlias(String address , String alias){
        ContentValues values = new ContentValues();
        int uid = BleConfig.getInstance().getUid();
        values.put("uid" , uid);
        values.put("address" , address);
        values.put("alias" , alias);

        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        db.update(DBNAME , values , "uid = ? and address = ? " ,
                new String[]{String.valueOf(uid) , address});
    }

    public static void updateDevType(String address , int type){
        ContentValues values = new ContentValues();
        int uid = BleConfig.getInstance().getUid();
        values.put("uid" , uid);
        values.put("address" , address);
        values.put("type" , type);

        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        int count = db.update(DBNAME , values , "uid = ? and address = ? " ,
                new String[]{String.valueOf(uid) , address});
        if (count < 1){
            db.insert(DBNAME , null , values);
        }
    }

    public static void updateSn(String address , String sn){
        ContentValues values = new ContentValues();
        int uid = BleConfig.getInstance().getUid();
        values.put("uid" , uid);
        values.put("address" , address);
        values.put("sn" , sn);

        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        db.update(DBNAME , values , "uid = ? and address = ? " ,
                new String[]{String.valueOf(uid) , address});
    }

    public static void updateVersion(String address , String version){
        ContentValues values = new ContentValues();
        int uid = BleConfig.getInstance().getUid();
        values.put("uid" , uid);
        values.put("address" , address);
        values.put("version" , version);

        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        db.update(DBNAME , values , "uid = ? and address = ? " ,
                new String[]{String.valueOf(uid) , address});
    }

    public static ArrayList<BleDevInfo> getBleDevs(){
        ArrayList<BleDevInfo> list = new ArrayList<>();
        Cursor cursor = null;
        try{
            SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
            cursor = db.query(DBNAME , null , "uid = ? " ,
                    new String[]{String.valueOf(BleConfig.getInstance().getUid())} , null , null, null);
            while (null != cursor && cursor.moveToNext()){
                BleDevInfo bleDevInfo = fromCursor(cursor);

                list.add(bleDevInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return list;
    }

    public static ArrayList<BleDevInfo> getConnecttedBleDevs(){
        ArrayList<BleDevInfo> list = new ArrayList<>();
        Cursor cursor = null;
        try{
            SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
            cursor = db.query(DBNAME , null , "uid = ? and state = ? " ,
                    new String[]{String.valueOf(BleConfig.getInstance().getUid()) , String.valueOf(BleState.STATE_CONNECTED.getState())} ,
                    null , null, null);
            while (null != cursor && cursor.moveToNext()){
                BleDevInfo bleDevInfo = fromCursor(cursor);

                list.add(bleDevInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return list;
    }

    //正在连接或已经连接的设备
    public static ArrayList<BleDevInfo> getActivedBleDevs(){
        ArrayList<BleDevInfo> list = new ArrayList<>();
        Cursor cursor = null;
        try{
            SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
            cursor = db.query(DBNAME , null , "uid = ? and state > ? " ,
                    new String[]{String.valueOf(BleConfig.getInstance().getUid()) , String.valueOf(BleState.STATE_DISCONNECTED.getState())} ,
                    null , null, null);
            while (null != cursor && cursor.moveToNext()){
                BleDevInfo bleDevInfo = fromCursor(cursor);

                list.add(bleDevInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return list;
    }

    //最后一次连接的设备地址
    public static String getLastConnectedAddress(){
        String address = null;
        Cursor cursor = null;
        try{
            SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
            cursor = db.query(DBNAME, null , "uid = ? " ,
                    new String[]{String.valueOf(BleConfig.getInstance().getUid())} , null , null , "ctime desc");
            if (null != cursor && cursor.moveToNext()){
                cursor.moveToFirst();
                address = BleCursorHelper.getString(cursor , "address");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return address;
    }

    public static int getState(String address){
        int state = BleState.STATE_DISCONNECTED.getState();
        Cursor cursor = null;
        try{
            SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
            cursor = db.query(DBNAME , null , "uid = ? and address = ? " ,
                    new String[]{String.valueOf(BleConfig.getInstance().getUid()) , address} , null , null, null);
            if (null != cursor && cursor.moveToNext()){
                cursor.moveToFirst();
                state = BleCursorHelper.getInt(cursor , "state");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return state;
    }

    public static BleDevInfo getDevInfo(String address){
        BleDevInfo devInfo = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
            cursor = db.query(DBNAME , null , "uid = ? and address = ? " ,
                    new String[]{String.valueOf(BleConfig.getInstance().getUid()) , address} , null , null, null);
            if (null != cursor && cursor.moveToNext()){
                cursor.moveToFirst();
                devInfo = fromCursor(cursor);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return devInfo;
    }

    public static void delete(String address){
        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        db.delete(DBNAME , "uid = ? and address = ? " , new String[]{String.valueOf(BleConfig.getInstance().getUid()) , address});
    }

    public static void delete(){
        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        db.delete(DBNAME , "uid = ? " , new String[]{String.valueOf(BleConfig.getInstance().getUid())});
    }

    //all state init
    public static void initState(){
        ContentValues values = new ContentValues();
        values.put("state" , BleState.STATE_DISCONNECTED.getState());
        values.put("rssi" , 100);

        SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
        db.update(DBNAME , values , null , null);
    }

    private static BleDevInfo fromCursor(Cursor cursor){
        String address = BleCursorHelper.getString(cursor , "address");
        String name = BleCursorHelper.getString(cursor , "name");
        String alias = BleCursorHelper.getString(cursor , "alias");
        int rssi = BleCursorHelper.getInt(cursor , "rssi");
        int state = BleCursorHelper.getInt(cursor , "state");
        int type = BleCursorHelper.getInt(cursor , "type");
        String version = BleCursorHelper.getString(cursor , "version");
        String sn = BleCursorHelper.getString(cursor , "sn");
        int battery = BleCursorHelper.getInt(cursor , "battery");
        long ctime = BleCursorHelper.getInt(cursor , "ctime");
        int group = BleCursorHelper.getInt(cursor , "groupType");

        BleDevInfo bleDevInfo = new BleDevInfo();
        bleDevInfo.setAddress(address);
        bleDevInfo.setName(name);
        bleDevInfo.setAlias(alias);
        bleDevInfo.setRssi(state == BleState.STATE_CONNECTED.getState() ? rssi : 100);
        bleDevInfo.setState(state);
        bleDevInfo.setType(type);
        bleDevInfo.setVersion(version);
        bleDevInfo.setSn(sn);
        bleDevInfo.setBattery(battery);
        bleDevInfo.setCtime(ctime);
        bleDevInfo.setGroup(group);

        return bleDevInfo;
    }

    private final static String DBNAME = "bledevinfo";

    public static void onCreateTable(SQLiteDatabase db){
        BleTableHelper.fromTableName(DBNAME)
                .addColumn_Integer("uid")
                .addColumn_Varchar("address" , 20)
                .addColumn_Varchar("name" , 20)
                .addColumn_Varchar("alias" , 20)
                .addColumn_Integer("rssi")
                .addColumn_Integer("state")
                .addColumn_Integer("type")
                .addColumn_Varchar("version" , 20)
                .addColumn_Varchar("sn" , 20)
                .addColumn_Integer("battery")
                .addColumn_Integer("groupType")
                .addColumn_Long("ctime")

                .buildTable(db);

    }

    public static void onDropTable(SQLiteDatabase db){
        db.execSQL("drop table if exists " + DBNAME);
    }


}
