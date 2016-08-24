package com.test.appa.provider;

import android.database.sqlite.SQLiteDatabase;

import com.test.appa.provider.info.AppAContract.ModeSetting;
import com.test.appa.provider.info.DatabaseInfo.BaseColumns;
import com.test.appa.provider.info.DatabaseInfo.Tables;


public class DatabaseCreator {
    private static DatabaseCreator creator;

    public static DatabaseCreator getInstance() {
        if (creator == null) {
            creator = new DatabaseCreator();
        }
        return creator;
    }

    public void createTable(SQLiteDatabase db) {
        createModeSettingTable(db);
    }

    public void createView(SQLiteDatabase db) {
        //TODO if need.
    }

    public void createTrigger(SQLiteDatabase db) {
        //TODO if need.
    }

    public void createIndex(SQLiteDatabase db){
        //TODO if need.
    }

    private void createModeSettingTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.MODE_SETTING
                + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + "," + ModeSetting.ON_OFF + " TEXT"
                + "," + ModeSetting.OPERATOR + " TEXT"
                + "," + ModeSetting.EMAIL + " TEXT"
                + ");");
    }
}
