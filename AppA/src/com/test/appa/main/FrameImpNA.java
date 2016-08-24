package com.test.appa.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.test.appa.R;

public class FrameImpNA extends FrameImp {

    public FrameImpNA(Context context) {
        super(context);
    }

    @Override
    public void inflateView(ViewGroup rootView) {
        LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_dummy, rootView, true);

        super.inflateView(rootView);
    }

    @Override
    public void setAttribute() {
        //nothing
    }

}
