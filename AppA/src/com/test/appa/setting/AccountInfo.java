package com.test.appa.setting;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class AccountInfo {
    private Context mContext;

    public AccountInfo(Context context) {
        initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
    }

    public ArrayList<Account> getGoogleAccount() {
        AccountManager manager = AccountManager.get(mContext);
        Account[] accounts = manager.getAccounts();
        ArrayList<Account> googles = new ArrayList<Account>();

        for (Account account : accounts) {
            Log.d("test", "Account - name: " + account.name + ", type :" + account.type);

            if (account.type.equals("com.google")) {
                googles.add(account);
            }
        }

        //for test
        //        for (Account google : googles) {
        //            Log.d("test", "Google - name: " + google.name + ", type :" + google.type);
        //        }
        return googles;
    }
}
