package com.test.appa.setting;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import com.test.appa.provider.info.AppAContract.ModeSetting;
import com.test.appa.setting.Constant.Operator;

import java.util.ArrayList;

public class Setting {

    private static Setting mInstance = null;
    private Context mContext;

    private boolean mTestMode = false;
    private Operator mOpCode;
    private String mEmail;

    public static Setting getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Setting(context);
        }
        return mInstance;
    }

    private Setting(Context context) {
        Log.d("test", "create Setting");
        mContext = context;
        loadTestData();
    }

    public boolean loadTestData() {
        boolean isLoad = false;

        String mode = DatabaseUtil.getString(mContext, ModeSetting.ON_OFF);
        if ("on".equals(mode)) {
            mTestMode = true;
        } else {
            mTestMode = false;
        }

        if (mTestMode) {
            mOpCode = loadOpFromShared();
            mEmail = DatabaseUtil.getString(mContext, ModeSetting.EMAIL);
        } else {
            mOpCode = loadOp();
            mEmail = loadGmail();
        }
        
        isLoad = true;
        
        return isLoad;
    }

    private Operator loadOpFromShared() {
        Operator op = Operator.NA;

        String opName = DatabaseUtil.getString(mContext, ModeSetting.OPERATOR);
        if ("skt".equals(opName)) {
            op = Operator.SKT;
        } else if ("kt".equals(opName)) {
            op = Operator.KT;
        } else if ("lgu".equals(opName)) {
            op = Operator.LGU;
        }

        return op;
    }

    private Operator loadOp() {
        Operator op = Operator.NA;

        String mnc = NetworkInfo.getCurrentMnc(mContext);
        if ("05".equals(mnc)) {
            op = Operator.SKT;
        } else if ("02".equals(mnc) || "04".equals(mnc) || "08".equals(mnc)) {
            op = Operator.KT;
        } else if ("06".equals(mnc)) {
            op = Operator.LGU;
        }

        return op;
    }

    private String loadGmail() {
        String email = null;

        AccountInfo ai = new AccountInfo(mContext);
        ArrayList<Account> ac = ai.getGoogleAccount();

        if (ac.size() > 0) {
            email = ac.get(0).name;
        }

        if (ac.size() > 1) {
            //TODO  선택popup
        }

        return email;
    }

    public Operator getOperatorCode() {
        return mOpCode;
    }

    public String getEmail() {
        return mEmail;
    }

    public boolean isTestMode() {
        return mTestMode;
    }
}
