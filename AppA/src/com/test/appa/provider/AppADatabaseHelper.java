package com.test.appa.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;

import com.test.appa.provider.info.DatabaseInfo;
import com.test.appa.provider.info.DatabaseInfo.ModeSettingColumns;
import com.test.appa.provider.info.DatabaseInfo.Tables;

import java.util.HashMap;

public class AppADatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "AppADatabaseHelper";

    private Context mContext;
    private static AppADatabaseHelper sSingleton = null;
    private final HashMap<String, Long> mModeCache = new HashMap<String, Long>();

    protected boolean mUpgradeViewsAndTriggers = false;
    protected boolean mUpgradeIndexes = false;

    public static synchronized AppADatabaseHelper getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new AppADatabaseHelper(context, DatabaseInfo.DATABASE_NAME, true);
        }
        return sSingleton;
    }

    private AppADatabaseHelper(Context context, String databaseName, boolean optimizationEnabled) {
        super(context, databaseName, null, DatabaseInfo.DATABASE_VERSION);
        mContext = context;
    }

    public SQLiteDatabase getDatabase(boolean writable) {
        return writable ? getWritableDatabase() : getReadableDatabase();
    }

    public DatabaseCreator getDatabaseCreator() {
        DatabaseCreator creator = DatabaseCreator.getInstance();
        return creator;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseCreator creator = getDatabaseCreator();
        creator.createTable(db);
        creator.createView(db);
        creator.createTrigger(db);
        creator.createIndex(db);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        initializeCache(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 101) {
            upgradeToVersion101(db);
            mUpgradeViewsAndTriggers = true;
            oldVersion = 101;
        }

        upgradeRest(db);
    }

    private void upgradeRest(SQLiteDatabase db) {
        DatabaseCreator creator = getDatabaseCreator();

        if (mUpgradeViewsAndTriggers) {
            creator.createView(db);
            creator.createTrigger(db);
        }

        if (mUpgradeIndexes) {
            creator.createIndex(db);
        }
    }

    private void upgradeToVersion101(SQLiteDatabase db) {
        //if need
        //upgradeForAddColumns(SQLiteDatabase db, String table, String column, String attr);
    }

    private void initializeCache(SQLiteDatabase db) {
        mModeCache.clear();
        lookupModeSetting(db);
    }

    /*start For init ModeSetting*/
    private long lookupModeSetting(SQLiteDatabase db) {
        final SQLiteStatement settingQuery = db.compileStatement(
                "SELECT " + ModeSettingColumns._ID +
                " FROM " + Tables.MODE_SETTING +
                " WHERE " + ModeSettingColumns._ID + "=?");
        
        final SQLiteStatement settingInsert = db.compileStatement(
                "INSERT INTO " + Tables.MODE_SETTING + "("
                        + ModeSettingColumns.ON_OFF +", "
                        + ModeSettingColumns.OPERATOR + ", "
                        + ModeSettingColumns.EMAIL +
                ") VALUES (?, ?, ?)");
        
        try {
            return lookupAndCache(settingQuery, settingInsert, "mode_setting", mModeCache);
        } finally {
            settingQuery.close();
            settingInsert.close();
        }
    }

    private long lookupAndCache(SQLiteStatement query, SQLiteStatement insert,
            String value, HashMap<String, Long> cache) {
        long id = -1;
        try {
            // Try searching database for mapping
            DatabaseUtils.bindObjectToProgram(query, 1, "1"); //id 1
            id = query.simpleQueryForLong();
        } catch (SQLiteDoneException e) {
            // Nothing found, so try inserting new mapping
            DatabaseUtils.bindObjectToProgram(insert, 1, "off");
            DatabaseUtils.bindObjectToProgram(insert, 2, "skt");
            DatabaseUtils.bindObjectToProgram(insert, 3, "");
            id = insert.executeInsert();
        }
        if (id != -1) {
            // Cache and return the new answer
            cache.put(value, id);
            return id;
        } else {
            // Otherwise throw if no mapping found or created
            throw new IllegalStateException("Couldn't find or create internal "
                    + "lookup table entry for value " + value);
        }
    }

    public void invalidateAllCache() {
        Log.w(TAG, "invalidateAllCache: [" + getClass().getSimpleName() + "]");
        mModeCache.clear();
    }
    /*End For init ModeSetting*/

    /**
     * Returns a detailed exception message for the supplied URI.  It includes the calling
     * user and calling package(s).
     */
    public String exceptionMessage(Uri uri) {
        return exceptionMessage(null, uri);
    }

    /**
     * Returns a detailed exception message for the supplied URI.  It includes the calling
     * user and calling package(s).
     */
    public String exceptionMessage(String message, Uri uri) {
        StringBuilder sb = new StringBuilder();
        if (message != null) {
            sb.append(message).append("; ");
        }
        sb.append("URI: ").append(uri);
        final PackageManager pm = mContext.getPackageManager();
        int callingUid = Binder.getCallingUid();
        sb.append(", calling user: ");
        String userName = pm.getNameForUid(callingUid);
        if (userName != null) {
            sb.append(userName);
        } else {
            sb.append(callingUid);
        }

        final String[] callerPackages = pm.getPackagesForUid(callingUid);
        if (callerPackages != null && callerPackages.length > 0) {
            if (callerPackages.length == 1) {
                sb.append(", calling package:");
                sb.append(callerPackages[0]);
            } else {
                sb.append(", calling package is one of: [");
                for (int i = 0; i < callerPackages.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(callerPackages[i]);
                }
                sb.append("]");
            }
        }

        return sb.toString();
    }

    private void upgradeForAddColumns(SQLiteDatabase db, String table, String column, String attr) {
        //pragma There is no guarantee of backwards compatibility. give up
        boolean hasColumn = hasColumnInTable(db, table, column);
        //if the table doesn't have the upgrading column, it is necessary to add column in the table.
        if (hasColumn == false) {
            db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + column + " " + attr + ";");
        }
    }

    /**
     * Check the existence of the column in the table
     * @param db SQLiteDatagbase object
     * @param table table name
     * @param column column name
     * @return TRUE if the table has the column,
     *         FALSE if the table doesn't have the column
     */
    private boolean hasColumnInTable(SQLiteDatabase db, String table, String column) {
        boolean result = false;
        //Read the all column information in the table
        Cursor c = db.rawQuery("PRAGMA table_info(" + table + ")", null);
        //PRAGMA table_info (TABLE) => (0) cid, (1) name, (2) type, (3) notnull, (4) dflt_value, (5) pk
        int nameIndex = 1;
        if (c != null) {
            while (c.moveToNext()) {
                //Get the column name
                String columnName = c.getString(nameIndex);
                if (columnName.equals(column)) {
                   result = true;
                   break;
                }
            }
        }

        if (c != null) {
            c.close();
        }
        return result;
    }

}
