package com.samsung.android.app.ledcover.creationpattern;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings.System;

public class ShowButtonBackgroundSettingObserver extends ContentObserver {
    private static final String SHOW_BUTTON_BACKGROUND = "show_button_background";
    private ContentResolver mContentResolver;
    private OnSettingValueChangeListener mOnSettingValueChangeListener;

    public interface OnSettingValueChangeListener {
        void onChange(boolean z);
    }

    public ShowButtonBackgroundSettingObserver(ContentResolver res) {
        super(new Handler());
        this.mContentResolver = res;
    }

    public void releaaseObserver() {
        this.mContentResolver = null;
    }

    public void setOnContentChangeListener(OnSettingValueChangeListener listener) {
        this.mOnSettingValueChangeListener = listener;
        if (this.mOnSettingValueChangeListener != null) {
            startObserving();
        } else {
            stopObserving();
        }
    }

    private void startObserving() {
        boolean z = false;
        this.mContentResolver.registerContentObserver(System.getUriFor(SHOW_BUTTON_BACKGROUND), false, this);
        if (System.getInt(this.mContentResolver, SHOW_BUTTON_BACKGROUND, 0) != 0) {
            z = true;
        }
        onChange(z);
    }

    private void stopObserving() {
        this.mContentResolver.unregisterContentObserver(this);
    }

    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (this.mOnSettingValueChangeListener != null) {
            this.mOnSettingValueChangeListener.onChange(selfChange);
        }
    }
}
