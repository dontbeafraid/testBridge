package com.test.appa.provider;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.test.appa.provider.info.AppAUriMatcher.Type;
import com.test.appa.provider.info.AppAUriParser;

import java.util.Arrays;
import java.util.List;

public class Deleter extends Writer {
    public static final String TAG = "Deleter";
    protected static AppAUriParser sUriMatcher;

    public Deleter(Context context, AppADatabaseHelper helper, final AppAUriParser matcher) {
        super(context, helper);
        sUriMatcher = matcher;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        AppATransaction transaction = startTransaction(false);
        try {
            int deleted = deleteInTransaction(uri, selection, selectionArgs);
            if (deleted > 0) {
                transaction.markDirty();
            }
            transaction.markSuccessful(false);
            return deleted;
        } finally {
            endTransaction(false);
        }
    }

    protected int deleteInTransaction(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "deleteInTransaction: uri=" + uri
                + " selection=[" + selection + "]  args=" + Arrays.toString(selectionArgs));
        final int match = sUriMatcher.match(uri);
        int count = 0;
        mNotify = true;
        switch (match) {
            case Type.MODE_SETTING: {
                // if need
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

                count = deleteModeSetting(uri, id, selection, selectionArgs);
                break;
            }

            default: {
                return 0;
            }
        }

        return count;
    }

    private int deleteModeSetting(Uri uri, String id, String selection, String[] selectionArgs) {
        int count = 0;
        return count;
    }
}
