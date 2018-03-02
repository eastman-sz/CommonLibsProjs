package com.ble.lib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ble.lib.dev.BleDevDbHepler;
import com.ble.lib.dev.BleDevSearchInfoDbHelper;
/**
 * Created by E on 2017/12/7.
 */
public class BleDbHelper extends SQLiteOpenHelper {

    private final static String DBNAME = "blelibsdbname";
    private final static int VERSION = 1;

    public BleDbHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BleDevDbHepler.onCreateTable(db);
        BleDevSearchInfoDbHelper.onCreateTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        BleDevDbHepler.onDropTable(db);
        BleDevSearchInfoDbHelper.onDropTable(db);

        onCreate(db);
    }
}
