package com.test.appa.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.test.appa.R;

public class FrameImpSKT extends FrameImp {

    public FrameImpSKT(Context context) {
        super(context);
    }

    @Override
    public void inflateView(ViewGroup rootView) {
        LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_skt, rootView, true);

        super.inflateView(rootView);
    }

    @Override
    public void setAttribute() {
        mTitle.setText(R.string.skt);
        mTitle.setTextColor(Color.BLACK);

        mDescription.setText(R.string.skt_des);
        mDescription.setTextColor(Color.BLACK);
    }

}
