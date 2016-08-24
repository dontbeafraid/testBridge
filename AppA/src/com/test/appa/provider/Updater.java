package com.test.appa.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.test.appa.provider.info.AppAContract.ModeSetting;
import com.test.appa.provider.info.AppAUriMatcher.Type;
import com.test.appa.provider.info.AppAUriParser;
import com.test.appa.provider.info.DatabaseInfo.Tables;

import java.util.Arrays;
import java.util.List;

public class Updater extends Writer {
    public static final String TAG = "Updater";
    protected static AppAUriParser sUriMatcher;

    public Updater(Context context, AppADatabaseHelper helper, final AppAUriParser matcher) {
        super(context, helper);
        sUriMatcher = matcher;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        AppATransaction transaction = startTransaction(false);
        try {
            int updated = updateInTransaction(uri, values, selection, selectionArgs);
            if (updated > 0) {
                transaction.markDirty();
            }
            transaction.markSuccessful(false);
            return updated;
        } finally {
            endTransaction(false);
        }
    }

    private int updateInTransaction(Uri uri, ContentValues values,
            String selection, String[] selectionArgs) {
        Log.d(TAG, "updateInTransaction: uri=" + uri + "  values=[" + values + "]"
                + " selection=[" + selection + "]  args=" + Arrays.toString(selectionArgs));
        final int match = sUriMatcher.match(uri);
        int count = 0;
        mNotify = true;

        switch (match) {
            case Type.MODE_SETTING: {
                count = updateModeSetting(uri, values, null, selection, selectionArgs);
                break;
            }

            case Type.MODE_SETTING_ID: {
                final List<String> pathSegments = uri.getPathSegments();
                final int segmentCount = pathSegments.size();
                if (segmentCount < 2) {
                    throw new IllegalArgumentException(mDbHelper.exceptionMessage(
                            "Missing a setting id", uri));
                }
                final String id = pathSegments.get(1);

                count = updateModeSetting(uri, values, id, selection, selectionArgs);
                break;
            }    

            default: {
                return 0;
            }
        }

        return count;
    }


    private int updateModeSetting(Uri uri, ContentValues values, String id,
            String selection, String[] selectionArgs) {
        int count = 0;
        String selectionWithId = selection;

        mValues.clear();
        mValues.putAll(values);

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if (id != null) {
            selectionWithId = (ModeSetting._ID + "=" + id + " ")
                    + (selection == null ? "" : " AND " + selection);
        }

        count = db.update(Tables.MODE_SETTING, mValues, selectionWithId, selectionArgs);

        return count;
    }
}
