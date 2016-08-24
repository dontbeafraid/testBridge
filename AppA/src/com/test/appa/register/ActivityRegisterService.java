package com.test.appa.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.test.appa.ActivityManager;
import com.test.appa.R;
import com.test.appa.setting.Constant;
import com.test.appa.setting.Setting;

public class ActivityRegisterService extends Activity {

    private EditText mEditTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test", "ActivityRegisterService onCreate()");
        super.onCreate(savedInstanceState);

        initializeRes();
        ActivityManager.getInstance().add(this);
    }

    private void initializeRes() {
        setContentView(R.layout.activity_register_service);
        mEditTextEmail = (EditText)findViewById(R.id.editTextEmail);
    }

    @Override
    protected void onResume() {
        Log.d("test", "ActivityRegisterService onResume()");
        super.onResume();

        String stringEmail = Setting.getInstance(getApplicationContext()).getEmail();
        mEditTextEmail.setText(stringEmail);
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().remove(this);
        
        super.onDestroy();
    }
    
    public void onClickRegComplete(View view) {
        Log.d("test", "onClickRegComplete()");
        
        if (checkEmailFormat()) {
            startRegisterService();
            ActivityManager.getInstance().finishAll();
        }
        else {
            Toast.makeText(getApplicationContext(), "Email format is wrong.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean checkEmailFormat() {
        boolean rst = true;
        
        String email = mEditTextEmail.getText().toString();
        if (email.length() > 0 && !email.contains("@")) {
            rst = false;
        }
        
        return rst;
    }
    
    private void startRegisterService() {        
        Intent intent = new Intent(Constant.ACTION_WAIT_REGISTER, null, getApplicationContext(),
                IntentServiceRegister.class);
        this.startService(intent);
    }
}
