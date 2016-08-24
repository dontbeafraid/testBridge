package com.test.appa.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.test.appa.provider.info.AppAUriMatcher;
import com.test.appa.provider.info.AppAUriParser;

import java.util.concurrent.CountDownLatch;

public class AppAProvider extends ContentProvider {
    public static final String TAG = "AppAProvider";

    private AppADatabaseHelper mDbHelper;

    private Inserter mInserter;
    private Updater mUpdater;
    private Deleter mDeleter;
    private Queryer mQueryer;

    private volatile CountDownLatch mReadAccessLatch;
    private volatile CountDownLatch mWriteAccessLatch;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    private static final int BACKGROUND_TASK_INITIALIZE = 0;
    private static final int BACKGROUND_TASK_OPEN_WRITE_ACCESS = 1;

    protected static final AppAUriParser URI_MATCHER =
            new AppAUriParser(UriMatcher.NO_MATCH);

    static {
        new AppAUriMatcher().addURIs(URI_MATCHER);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = getDatabaseHelper(context);

        try {
            return initialize();
        }
        catch (RuntimeException e) {
            Log.e(TAG, "Cannot start provider", e);
            if (shouldThrowExceptionForInitializationError()) {
                throw e;
            }
            return false;
        }
        finally {
            Log.d(TAG, "AppAProvider.onCreate finish");
        }
    }

    private boolean shouldThrowExceptionForInitializationError() {
        return false;
    }

    private boolean initialize() {
        Log.d(TAG, "AppAProvider initialize");
        boolean rst = true;

        mInserter = new Inserter(getContext(), mDbHelper, URI_MATCHER);
        mUpdater = new Updater(getContext(), mDbHelper, URI_MATCHER);
        mDeleter = new Deleter(getContext(), mDbHelper, URI_MATCHER);
        mQueryer = new Queryer(getContext(), mDbHelper, URI_MATCHER);


        mReadAccessLatch = new CountDownLatch(1);
        mWriteAccessLatch = new CountDownLatch(1);

        mBackgroundThread = new HandlerThread("AppAProviderWorker",
                Process.THREAD_PRIORITY_BACKGROUND);
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                performBackgroundTask(msg.what, msg.obj);
            }
        };
        scheduleBackgroundTask(BACKGROUND_TASK_INITIALIZE);
        scheduleBackgroundTask(BACKGROUND_TASK_OPEN_WRITE_ACCESS);

        return rst;
    }

    private void scheduleBackgroundTask(int task) {
        mBackgroundHandler.sendEmptyMessage(task);
    }

    private void performBackgroundTask(int task, Object arg) {
        switch (task) {
            case BACKGROUND_TASK_INITIALIZE: {
                mReadAccessLatch.countDown();
                mReadAccessLatch = null;
                break;
            }

            case BACKGROUND_TASK_OPEN_WRITE_ACCESS: {
                mWriteAccessLatch.countDown();
                mWriteAccessLatch = null;
                break;
            }

            default:
                break;
        }
    }

    private AppADatabaseHelper getDatabaseHelper(final Context context) {
        return AppADatabaseHelper.getInstance(context);
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        waitForAccess(mWriteAccessLatch);

        if (mInserter == null) {
            return null;
        }
        else {
            return mInserter.insert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        waitForAccess(mWriteAccessLatch);

        if (mUpdater == null) {
            return 0;
        }
        else {
            return mUpdater.update(uri, values, selection, selectionArgs);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        waitForAccess(mWriteAccessLatch);

        if (mDeleter == null) {
            return 0;
        }
        else {
            return mDeleter.delete(uri, selection, selectionArgs);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        waitForAccess(mReadAccessLatch);

        if (mQueryer == null) {
            return null;
        }
        else {
            return mQueryer.query(uri, projection, selection, selectionArgs,
                    sortOrder);
        }
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    private void waitForAccess(CountDownLatch latch) {
        if (latch == null) {
            return;
        }

        while (true) {
            try {
                latch.await();
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
