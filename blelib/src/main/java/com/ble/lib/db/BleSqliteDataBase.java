package com.ble.lib.db;

import android.database.sqlite.SQLiteDatabase;
import com.ble.lib.application.BleApplication;
/**
 * @author E
 */
public class BleSqliteDataBase {

	private static SQLiteDatabase sqLiteDatabase = null;

	private final static Object object = new Object();

	public static SQLiteDatabase getSqLiteDatabase(){
		if (null == sqLiteDatabase) {
			synchronized (object.getClass()) {
				if (null == sqLiteDatabase) {
					sqLiteDatabase = new BleDbHelper(BleApplication.getContext()).getWritableDatabase();
				}
			}
		}
		return sqLiteDatabase;
	}

}
