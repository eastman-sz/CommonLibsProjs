package com.ble.lib.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by E on 2017/11/22.
 */
public final class BleTableHelper {

    private static BleTableHelper dbTableHelper = null;
    private StringBuilder builder = null;

    private BleTableHelper(){
    }

    private BleTableHelper(String dbName){
        builder = new StringBuilder();
        builder.append("create table if not exists " + dbName);
        builder.append("(id INTEGER PRIMARY KEY AUTOINCREMENT,");
    }

    public static BleTableHelper fromTableName(String dbName){
        dbTableHelper = new BleTableHelper(dbName);
        return dbTableHelper;
    }

    public BleTableHelper addColumn_Integer(String columnName){
        builder.append(columnName + " Integer,");
        return dbTableHelper;
    }

    public BleTableHelper addColumn_Long(String columnName){
        builder.append(columnName + " Long,");
        return dbTableHelper;
    }

    public BleTableHelper addColumn_Varchar(String columnName , int length){
        builder.append(columnName + " varchar(" + length + "),");
        return dbTableHelper;
    }

    public void buildTable(SQLiteDatabase db){
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append(")");
        db.execSQL(builder.toString());
    }

}
