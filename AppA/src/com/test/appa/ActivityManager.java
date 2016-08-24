package com.test.appa;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityManager {

    private static ActivityManager mInstance = null;
    private ArrayList<Activity> mList = null;

    public static ActivityManager getInstance() {

        if (mInstance == null) {
            mInstance = new ActivityManager();
        }
        return mInstance;
    }

    private ActivityManager() {
        mList = new ArrayList<Activity>();
    }

    public void add(Activity activity) {
        mList.add(activity);
    }

    public boolean remove(Activity activity) {
        return mList.remove(activity);
    }

    public void finishAll() {
        for (Activity activity : mList) {
            activity.finish();
        }
    }

}
