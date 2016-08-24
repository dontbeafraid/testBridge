package com.test.appa.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.test.appa.ActivityManager;
import com.test.appa.R;
import com.test.appa.setting.Setting;

public class ActivityAdditionalService extends Activity {

    private ViewGroup mRootLayout;
    private Frame mFrameOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        initializeRes();
        ActivityManager.getInstance().add(this);
    }

    private void initializeRes() {
        setContentView(R.layout.activity_additional_service);
        mRootLayout = (LinearLayout)findViewById(R.id.layout_operator);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        Setting.getInstance(getApplicationContext()).loadTestData();
        mFrameOperator = new Frame(this, mRootLayout);
        mFrameOperator.add();
    }
   
    
    @Override
    protected void onPause() {       
        mFrameOperator.remove();
        
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mFrameOperator = null;
        ActivityManager.getInstance().remove(this);
        
        super.onDestroy();
    }
}
