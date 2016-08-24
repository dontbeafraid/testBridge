package com.test.appa.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.test.appa.R;
import com.test.appa.setting.Constant;

public abstract class FrameImp {
    protected Context mContext;
    protected ViewGroup mRootView;
    protected TextView mTitle;
    protected TextView mDescription;
    protected Button mButtonRegister;

    public FrameImp(Context context) {
        initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
    }

    public void inflateView(ViewGroup rootView) {
        mRootView = rootView;
        initializeRes();
    }

    private void initializeRes() {
        mTitle = (TextView)mRootView.findViewById(R.id.textTitle);
        mDescription = (TextView)mRootView.findViewById(R.id.textDescription);

        mButtonRegister = (Button)mRootView.findViewById(R.id.buttionRegister);
        if (mButtonRegister != null) {
            mButtonRegister.setOnClickListener(mClickListener);
        }
    }

    public abstract void setAttribute();

    public void remove() {
        mRootView.removeAllViews();
    }

    protected Button.OnClickListener mClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Constant.ACTION_MOVE_REGISTER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    };
}
