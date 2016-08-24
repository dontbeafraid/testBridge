package com.test.appa.register;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class IntentServiceRegister extends IntentService {
    private TimerTask mTask;

    public IntentServiceRegister() {
        super("IntentServiceRegister");
    }

    @Override
    protected void onHandleIntent(Intent paramIntent) {
        Log.d("test", "IntentServiceRegister onHandleIntent");
        NotificationUtil.showWaitRegister(getApplicationContext());
        
        mTask = new TimerTask() {
            @Override
            public void run() {
                NotificationUtil.showCompleteRegister(getApplicationContext());
                //TODO 안해도 되는 Email 발송
            }
        };

        //15sec timer
        Timer timer = new Timer();
        timer.schedule(mTask, 15 * 1000);
    }
}
