package com.test.appa.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.test.appa.provider.info.AppAContract;
import com.test.appa.provider.info.AppAUriMatcher.Type;
import com.test.appa.provider.info.AppAUriParser;
import com.test.appa.provider.info.DatabaseInfo.Tables;
import com.test.appa.provider.info.Projections.Projection;

public class Queryer extends Reader {
    public static final String TAG = "Queryer";
    protected static AppAUriParser sUriMatcher;

    public Queryer(Context context, AppADatabaseHelper helper, final AppAUriParser matcher) {
        super(context, helper);
        sUriMatcher = matcher;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        return queryLocal(uri, projection, selection, selectionArgs, sortOrder);
    }

    private Cursor queryLocal(final Uri uri, final String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        Log.d(TAG, "queryLocal: uri=" + uri);
        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String groupBy = null;
        String having = null;
        String limit = getLimit(uri);

        switch (match) {
            case Type.MODE_SETTING: {
                setQueryStuffForModeSetting(qb, uri);
                break;
            }

            default: {
                return null;
            }
        }

        Cursor cursor =
                query(db, qb, projection, selection, selectionArgs, sortOrder, groupBy,
                        having, limit);

        return cursor;
    }

    public Cursor query(final SQLiteDatabase db, SQLiteQueryBuilder qb, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, String groupBy,
            String having, String limit) {
        Log.d("TAG", qb.buildQuery(projection, selection, selectionArgs, groupBy, null, sortOrder, limit)); //Query print

        final Cursor c = qb.query(db, projection, selection, selectionArgs, groupBy, having,
                sortOrder, limit);
        if (c != null) {
            c.setNotificationUri(mContext.getContentResolver(), AppAContract.AUTHORITY_URI);
        }
        return c;
    }

    private void setQueryStuffForModeSetting(SQLiteQueryBuilder qb, Uri uri) {
        StringBuilder sb = new StringBuilder();

        sb.append(Tables.MODE_SETTING);

        qb.setTables(sb.toString());

        qb.setProjectionMap(Projection.MODE_SETTING_PROJECTION_MAP);
    }

  
    private String getLimit(Uri uri) {
        return null;
        //if need implements
        //String limitParam = getQueryParameter(uri, AppAContract.LIMIT_PARAM_KEY);
    }
}
