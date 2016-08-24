package com.test.appa.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.test.appa.R;

public class FrameImpLGU extends FrameImp {

    public FrameImpLGU(Context context) {
        super(context);
    }

    @Override
    public void inflateView(ViewGroup rootView) {
        LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_lgu, rootView, true);

        super.inflateView(rootView);
    }

    @Override
    public void setAttribute() {
        mTitle.setText(R.string.lgu);
        mTitle.setTextColor(Color.BLACK);

        mDescription.setText(R.string.lgu_des);
        mDescription.setTextColor(Color.BLACK);
    }

}
