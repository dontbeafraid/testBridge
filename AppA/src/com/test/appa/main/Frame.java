package com.test.appa.main;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.test.appa.setting.Constant.Operator;
import com.test.appa.setting.Setting;

public class Frame {
    private FrameImp mImp;

    private Context mContext;
    private ViewGroup mRootLayout;

    public Frame(Context context, ViewGroup rootView) {
        initialize(context, rootView);
    }

    private void initialize(Context context, ViewGroup rootView) {
        mContext = context;
        mRootLayout = rootView;
        initFrameImp();
    }

    private void initFrameImp() {
        Setting setting = Setting.getInstance(mContext);

        Operator op = setting.getOperatorCode();
        switch (op) {
            case SKT: {
                mImp = new FrameImpSKT(mContext);
                break;
            }
    
            case KT: {
                mImp = new FrameImpKT(mContext);
                break;
            }
            case LGU: {
                mImp = new FrameImpLGU(mContext);
                break;
            }
    
            default: {
                Log.d("test", "NA operator matching fail");
                mImp = new FrameImpNA(mContext);
                break;
            }
        }
    }

    public void add() {
        mImp.inflateView(mRootLayout);
        mImp.setAttribute();
    }

    public void remove() {
        mImp.remove();
    }
}
