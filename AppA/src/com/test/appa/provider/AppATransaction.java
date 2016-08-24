/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.test.appa.provider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppATransaction {

    private final boolean mBatch;

    private final List<SQLiteDatabase> mDatabasesForTransaction;

    private final Map<String, SQLiteDatabase> mDatabaseTagMap;

    private boolean mIsDirty;

    private boolean mYieldFailed;

    public AppATransaction(boolean batch) {
        mBatch = batch;
        mDatabasesForTransaction = new ArrayList<SQLiteDatabase> ();
        mDatabaseTagMap = new HashMap<String, SQLiteDatabase> ();
        mIsDirty = false;
    }

    public boolean isBatch() {
        return mBatch;
    }

    public boolean isDirty() {
        return mIsDirty;
    }

    public void markDirty() {
        mIsDirty = true;
    }

    public void markYieldFailed() {
        mYieldFailed = true;
    }

    public void startTransactionForDb(SQLiteDatabase db, String tag,
            SQLiteTransactionListener listener) {
        if (!hasDbInTransaction(tag)) {
            mDatabasesForTransaction.add(0, db);
            mDatabaseTagMap.put(tag, db);
            if (listener != null) {
                db.beginTransactionWithListener(listener);
            } else {
                db.beginTransaction();
            }
        }
    }

    public boolean hasDbInTransaction(String tag) {
        return mDatabaseTagMap.containsKey(tag);
    }

    public SQLiteDatabase getDbForTag(String tag) {
        return mDatabaseTagMap.get(tag);
    }

    public SQLiteDatabase removeDbForTag(String tag) {
        SQLiteDatabase db = mDatabaseTagMap.get(tag);
        mDatabaseTagMap.remove(tag);
        mDatabasesForTransaction.remove(db);
        return db;
    }

    public void markSuccessful(boolean callerIsBatch) {
        if (!mBatch || callerIsBatch) {
            for (SQLiteDatabase db : mDatabasesForTransaction) {
                db.setTransactionSuccessful();
            }
        }
    }

    public void finish(boolean callerIsBatch) {

        if (!mBatch || callerIsBatch) {
            for (SQLiteDatabase db : mDatabasesForTransaction) {

                if (mYieldFailed && !db.isDbLockedByCurrentThread()) {
                    continue;
                }
                db.endTransaction();
            }
            mDatabasesForTransaction.clear();
            mDatabaseTagMap.clear();
            mIsDirty = false;
        }
    }
}
