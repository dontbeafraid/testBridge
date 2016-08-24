package com.test.appa.setting;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class NetworkInfo {

    public static String getCurrentMcc(Context context) {
        String mccCode = null;
        String networkOperator = ((TelephonyManager)context
                .getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator();
        String simOperator = ((TelephonyManager)context
                .getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();

        if (networkOperator != null) {
            if (networkOperator.length() >= 3) {
                mccCode = networkOperator.substring(0, 3);
                Log.d("test", "Network MCC: " + mccCode);
            }
        } else {
            if (simOperator != null) {
                if (simOperator.length() >= 3) {
                    mccCode = simOperator.substring(0, 3);
                    Log.d("test", "SIM MCC: " + mccCode);
                }
            }
        }

        if (!TextUtils.isEmpty(mccCode)) {
            return mccCode;
        }
        return "";
    }

    public static String getCurrentMnc(Context context) {
        String mncCode = null;
        String networkOperator = ((TelephonyManager)context
                .getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator();
        String simOperator = ((TelephonyManager)context
                .getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();

        if (networkOperator != null) {
            if (networkOperator.length() >= 5) {
                mncCode = networkOperator.substring(3);
                Log.d("test", "Network MNC: " + mncCode);
            }
        } else {
            if (simOperator != null) {
                if (simOperator.length() >= 5) {
                    mncCode = simOperator.substring(3);
                    Log.d("test", "SIM MNC: " + mncCode);
                }
            }
        }

        if (!TextUtils.isEmpty(mncCode)) {
            return mncCode;
        }
        return "";
    }
}
