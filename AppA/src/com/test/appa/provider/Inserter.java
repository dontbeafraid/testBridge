package com.test.appa.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.test.appa.provider.info.AppAUriMatcher.Type;
import com.test.appa.provider.info.AppAUriParser;
import com.test.appa.provider.info.DatabaseInfo.Tables;

public class Inserter extends Writer {
    public static final String TAG = "Inserter";
    protected static AppAUriParser sUriMatcher;

    public Inserter(Context context, AppADatabaseHelper helper, final AppAUriParser matcher) {
        super(context, helper);
        sUriMatcher = matcher;
    }

    public Uri insert(Uri uri, ContentValues values) {
        AppATransaction transaction = startTransaction(false);
        try {
            Uri result = insertInTransaction(uri, values);
            if (result != null) {
                transaction.markDirty();
            }
            transaction.markSuccessful(false);
            return result;
        } finally {
            endTransaction(false);
        }
    }

    private Uri insertInTransaction(Uri uri, ContentValues values) {
        Log.d(TAG, "insertInTransaction: uri=" + uri + "  values=[" + values + "]");
        final int match = sUriMatcher.match(uri);
        long id = 0;
        mNotify = true;
        switch (match) {
            case Type.MODE_SETTING: {
                id = insertModeSetting(uri, values);
                break;
            }

            default: {
                return null;
            }
        }

        if (id < 0) {
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

    private long insertModeSetting(Uri uri, ContentValues values) {
        mValues.clear();
        mValues.putAll(values);
        
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(Tables.MODE_SETTING, null, mValues);
        
        return id;
    }
    
}
