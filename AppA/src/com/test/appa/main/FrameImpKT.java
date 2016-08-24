package com.test.appa.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.test.appa.R;

public class FrameImpKT extends FrameImp {

    public FrameImpKT(Context context) {
        super(context);
    }

    @Override
    public void inflateView(ViewGroup rootView) {
        LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_kt, rootView, true);

        super.inflateView(rootView);
    }

    @Override
    public void setAttribute() {
        mTitle.setText(R.string.kt);
        mTitle.setTextColor(Color.BLACK);

        mDescription.setText(R.string.kt_des);
        mDescription.setTextColor(Color.BLACK);
    }

}
