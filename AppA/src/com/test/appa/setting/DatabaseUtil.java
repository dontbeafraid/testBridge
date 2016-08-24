package com.test.appa.setting;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.test.appa.provider.info.AppAContract.ModeSetting;

public class DatabaseUtil {
    
    public static String getString(Context context, String column) {
        String[] Projection  = new String[] {column};
        String value = null;
        
        Cursor c = context.getContentResolver().query(ModeSetting.CONTENT_URI, Projection, null, null, null);
        if (c != null && c.moveToFirst()) {
            value  = c.getString(c.getColumnIndex(column));

            Log.d("test",  column + " : " + value);            
            c.close();
        }
        
        return value;
    }

    public static int getInt(Context context, String column) {
        String[] Projection  = new String[] {column};
        int value = 0;
        
        Cursor c = context.getContentResolver().query(ModeSetting.CONTENT_URI, Projection, null, null, null);
        if (c != null && c.moveToFirst()) {
            value  = c.getInt(c.getColumnIndex(column));

            Log.d("test",  column + " : " + value);            
            c.close();
        }
        
        return value;
    }

}
