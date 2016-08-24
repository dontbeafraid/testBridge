package com.test.appb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

//TODO db 작업은 thread에서 해야함.
public class DatabaseUtil {
    public static final String _ID = "_id";
    public static final String ON_OFF = "on_off";
    public static final String OPERATOR = "operator";
    public static final String EMAIL = "email";
    
    
    public static HashMap<String, String> loadSettingData(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        
        Uri uri = Uri.parse(Constant.APPA_DB_URI + "/mode_setting");

        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            int id = c.getInt(c.getColumnIndex(_ID));
            String onoff = c.getString(c.getColumnIndex(ON_OFF));
            String op = c.getString(c.getColumnIndex(OPERATOR));
            String email = c.getString(c.getColumnIndex(EMAIL));

            Log.d("test", "id: " + id);
            Log.d("test", "onoff: " + onoff);
            Log.d("test", "op: " + op);
            Log.d("test", "email: " + email);
            
            map.put(ON_OFF, onoff);
            map.put(OPERATOR, op);
            map.put(EMAIL, email);
            
            c.close();
        }
        
        return map;
    }
    
    
    public static int saveSettingData(Context context, ContentValues values) {
        Uri uri = Uri.parse(Constant.APPA_DB_URI + "/mode_setting");
        int count = context.getContentResolver().update(uri, values, "_id = 1", null);
        
        return count;
    }
    
}
