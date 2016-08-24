package com.test.appa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.test.appa.setting.Constant;
import com.test.appa.setting.Setting;

import java.util.Timer;
import java.util.TimerTask;

public class ActivitySplash extends Activity {
    private TimerTask mLoadingTimerTask;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

        
    @Override
    protected void onResume() {
        super.onResume();
        
        mLoadingTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (Setting.getInstance(getApplicationContext()).loadTestData()) {
                    startActivity();
                    finish();
                  }
            }
        };

        //임의로 3sec loading
        Timer timer = new Timer();
        timer.schedule(mLoadingTimerTask, 3 * 1000);
    }
    
    private void startActivity() {
        Intent intent = new Intent(Constant.ACTION_MOVE_ADDITIONAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
