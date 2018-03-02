package com.ble.lib.dev;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ble.lib.db.BleContentValuesHelper;
import com.ble.lib.db.BleCursorHelper;
import com.ble.lib.db.BleSqliteDataBase;
import com.ble.lib.db.BleTableHelper;
/**
 * Save searched dev info.
 * @author E
 */
public class BleDevSearchInfoDbHelper {
	
	public static void updateDevSearchInfo(String address , String name){
		update(address, new String[]{"address" , "name"}, new Object[]{address , name});
	}
	
	private static void update(String address , String[] keys , Object[] new_values){
		ContentValues values = BleContentValuesHelper.getUpdateValues(keys, new_values);
		SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
		int count = db.update(DB_NAME, values, "address = ? ", new String[]{address});
		if (count < 1) {
			db.insert(DB_NAME, null, values);
		}
	}
	
	public static BleDevInfo getDevSearchInfo(String address){
		BleDevInfo devInfo = null;
		Cursor cursor = null;
		try {
			SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
			cursor = db.query(DB_NAME, null, "address = ? ", new String[]{address}, null, null, null);
			if (null != cursor && cursor.moveToNext()) {
				cursor.moveToFirst();
				devInfo = fromCursor(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null != cursor) {
				cursor.close();
			}
		}
		return devInfo;
	}
	
	public static String getDevSearchName(String address){
		String name = null;
		Cursor cursor = null;
		try {
			SQLiteDatabase db = BleSqliteDataBase.getSqLiteDatabase();
			cursor = db.query(DB_NAME, null, "address = ? ", new String[]{address}, null, null, null);
			if (null != cursor && cursor.moveToNext()) {
				cursor.moveToFirst();
				name = BleCursorHelper.getString(cursor, "name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null != cursor) {
				cursor.close();
			}
		}
		return name;
	}

	private static BleDevInfo fromCursor(Cursor cursor){
		String address = BleCursorHelper.getString(cursor, "address");
		String name = BleCursorHelper.getString(cursor, "name");
		int type = BleCursorHelper.getInt(cursor, "type");

		BleDevInfo devInfo = new BleDevInfo();
		devInfo.setAddress(address);
		devInfo.setName(name);
		devInfo.setType(type);
		
		return devInfo;
	}

	private final static String DB_NAME = "sdk_db_search_info_dbname";
	
	public static void onCreateTable(SQLiteDatabase db) {
        BleTableHelper.fromTableName(DB_NAME)
                .addColumn_Varchar("address" , 20)
                .addColumn_Varchar("name" , 20)
                .addColumn_Integer("type")

                .buildTable(db);

	}
	
	public static void onDropTable(SQLiteDatabase db){
		db.execSQL("drop table if exists " + DB_NAME);
	}
	
	
}
