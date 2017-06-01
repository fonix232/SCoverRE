package com.sec.android.cover;

import android.content.Context;

public abstract class BaseCoverObservator implements CoverObservator {
    private Context mContext;

    public BaseCoverObservator(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        this.mContext = context;
    }

    protected Context getContext() {
        return this.mContext;
    }

    public void restart() {
        stop();
        start();
    }
}
