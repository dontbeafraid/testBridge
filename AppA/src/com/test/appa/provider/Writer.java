package com.test.appa.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;

import com.test.appa.provider.info.AppAContract;


public class Writer implements SQLiteTransactionListener {

    protected boolean mNotify = true;

    private final ThreadLocal<AppATransaction> mTransactionHolder = 
            new ThreadLocal<AppATransaction>();

    private SQLiteOpenHelper mSerializeOnDbHelper;
    private String mSerializeDbTag;
    private SQLiteTransactionListener mSerializedDbTransactionListener;
    static final String APPA_DB_TAG = "appa";
    protected Context mContext;
    AppADatabaseHelper mDbHelper;
    ContentValues mValues = new ContentValues();

    public Writer(Context context, AppADatabaseHelper helper) {
        initialize(context, helper);
    }

    private void initialize(Context context, AppADatabaseHelper helper) {
        mContext = context;
        mDbHelper = helper;
        // Set up the DB helper for keeping transactions serialized.
        setDbHelperToSerializeOn(helper, APPA_DB_TAG, this);
    }

    public void setDbHelperToSerializeOn(SQLiteOpenHelper serializeOnDbHelper, String tag,
            SQLiteTransactionListener listener) {
        mSerializeOnDbHelper = serializeOnDbHelper;
        mSerializeDbTag = tag;
        mSerializedDbTransactionListener = listener;
    }

    protected AppATransaction startTransaction(boolean callerIsBatch) {
        AppATransaction transaction = mTransactionHolder.get();
        if (transaction == null) {
            transaction = new AppATransaction(callerIsBatch);
            if (mSerializeOnDbHelper != null) {
                transaction.startTransactionForDb(mSerializeOnDbHelper.getWritableDatabase(),
                        mSerializeDbTag, mSerializedDbTransactionListener);
            }
            mTransactionHolder.set(transaction);
        }
        return transaction;
    }

    protected void endTransaction(boolean callerIsBatch) {
        AppATransaction transaction = mTransactionHolder.get();
        if (transaction != null && (!transaction.isBatch() || callerIsBatch)) {
            try {
                if (transaction.isDirty()) {
                    if (mNotify) {
                        notifyChange();
                    }
                }
                transaction.finish(callerIsBatch);
            } finally {
                // No matter what, make sure we clear out the thread-local transaction reference.
                mTransactionHolder.set(null);
            }
        }
    }

    protected void notifyChange() {
        mContext.getContentResolver().notifyChange(AppAContract.AUTHORITY_URI, null, false);
    }

    @Override
    public void onBegin() {
        onBeginTransactionInternal();
    }

    private void onBeginTransactionInternal() {
        //do nothing
    }

    @Override
    public void onCommit() {
        onCommitTransactionInternal();
    }

    private void onCommitTransactionInternal() {
        //do nothing
    }

    @Override
    public void onRollback() {
        onRollbackTransactionInternal(false);
    }

    private void onRollbackTransactionInternal(boolean forProfile) {
        mDbHelper.invalidateAllCache();
    }
}
