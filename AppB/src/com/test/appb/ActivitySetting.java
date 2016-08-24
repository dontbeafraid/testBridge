package com.test.appb;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

public class ActivitySetting extends Activity {
    
    private Switch mSwitchTestMode;
    private RadioGroup mRadioGroupOperator;
    private RadioButton mButtonSkt;
    private RadioButton mButtonKt;
    private RadioButton mButtonLgu;
    
    private EditText mEditTextEmail;

    private String mTestMode = "off";
    private String mOperator = Constant.SKT;
    private String mEmail;  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test", "ActivitySetting onCreate()");
        super.onCreate(savedInstanceState);
        
        initializeRes();
    }  
    
    private void initializeRes() {
        setContentView(R.layout.activity_setting);       
        mEditTextEmail = (EditText)findViewById(R.id.editTextEmail);
        
        mSwitchTestMode = (Switch)findViewById(R.id.switchTestMode);
        mSwitchTestMode.setOnCheckedChangeListener(mTestModeListener);
        
        mRadioGroupOperator = (RadioGroup)findViewById(R.id.radioGroupOperator);
        mRadioGroupOperator.setOnCheckedChangeListener(mOpreatorListener);
        mButtonSkt = (RadioButton)findViewById(R.id.skt);
        mButtonKt = (RadioButton)findViewById(R.id.kt);
        mButtonLgu = (RadioButton)findViewById(R.id.lgu);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        
        HashMap<String, String> setting = DatabaseUtil.loadSettingData(getApplicationContext());
        mTestMode = setting.get(DatabaseUtil.ON_OFF);
        mOperator = setting.get(DatabaseUtil.OPERATOR);
        mEmail = setting.get(DatabaseUtil.EMAIL);

        initUi();
    }

    private void initUi() {
        boolean checked = false;
        if ("on".equals(mTestMode)) {
            checked = true;
        }
        else {
            checked = false;
        }
        mSwitchTestMode.setChecked(checked);
        
        if ("skt".equals(mOperator)) {
            mButtonSkt.setChecked(true);
        }
        else if ("kt".equals(mOperator)) {
            mButtonKt.setChecked(true);
        }
        else if ("lgu".equals(mOperator)) {
            mButtonLgu.setChecked(true);
        }
        
        mEditTextEmail.setText(mEmail);
    }
    
    //Save Button Click event
    public void onClickSaveInformation(View view) {
        mEmail = mEditTextEmail.getText().toString();
        
        if (checkEmailFormat()) {
            saveSettingData();
            finish();
        }
        else {
            Toast.makeText(getBaseContext(), "Email format is wrong.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean checkEmailFormat() {
        boolean rst = true;
        
        mEmail = mEditTextEmail.getText().toString();
        if (mEmail.length() > 0 && !mEmail.contains("@")) {
            rst = false;
        }
        
        return rst;
    }
    
    private void saveSettingData() {
        Log.d("test", "save testmode: " + mTestMode);
        Log.d("test", "save operator: " + mOperator);
        Log.d("test", "save email: " + mEmail);
        
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_ON_OFF, mTestMode);
        values.put(Constant.COLUMN_OPERATOR, mOperator);
        values.put(Constant.COLUMN_EMAIL, mEmail);
        
        int count = DatabaseUtil.saveSettingData(getApplicationContext(), values);        
        if (count > 0) {
            Toast.makeText(getApplicationContext(), "Test data changed", Toast.LENGTH_SHORT).show();
        }
    }
    
    private Switch.OnCheckedChangeListener mTestModeListener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d("test", "Switch onCheckedChanged() isChecked: " + isChecked);
            if (isChecked) {
                mTestMode = "on";
            }
            else {
                mTestMode = "off";
            }
        }
        
    };
    
    private RadioGroup.OnCheckedChangeListener mOpreatorListener = new RadioGroup.OnCheckedChangeListener() {
        private String mOp = "na";
        
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.d("test", "RadioGroup onCheckedChanged() checkedId: " + checkedId);
            switch (checkedId) {
            case R.id.skt :
                mOp = Constant.SKT;
                break;
                
            case R.id.kt:
                mOp = Constant.KT;
                break;
                
            case R.id.lgu:
                mOp = Constant.LGU;
                break;
                
            default:
                break;
            }
            
            mOperator = mOp;
        }
    };
}
