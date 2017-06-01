package com.samsung.android.sdk.cover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.cover.INfcLedCoverTouchListenerCallback.Stub;
import com.samsung.android.sdk.cover.ScoverManager.CoverPowerKeyListener;
import java.lang.ref.WeakReference;

class CoverPowerKeyListenerDelegate extends Stub {
    private static final int MSG_SYSTEM_COVER_EVENT = 0;
    private ListenerDelegateHandler mHandler;
    private CoverPowerKeyListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final WeakReference<CoverPowerKeyListener> mListenerRef;

        public ListenerDelegateHandler(Looper looper, CoverPowerKeyListener listener) {
            super(looper);
            this.mListenerRef = new WeakReference(listener);
        }

        public void handleMessage(Message msg) {
            CoverPowerKeyListener listener = (CoverPowerKeyListener) this.mListenerRef.get();
            if (msg.what == 0 && listener != null) {
                listener.onPowerKeyPress();
            }
        }
    }

    private static final class SystemEvents {
        private static final String KEY_DISABLE_LCD_OFF_BY_COVER = "lcd_off_disabled_by_cover";
        private static final int LCD_OFF_DISABLED_BY_COVER = 4;
        private static final int LED_OFF = 0;
        private static final int NOTIFICATION_ADD = 2;
        private static final int NOTIFICATION_REMOVE = 3;
        private static final int POWER_BUTTON = 1;

        private SystemEvents() {
        }
    }

    CoverPowerKeyListenerDelegate(CoverPowerKeyListener listener, Handler handler, Context context) {
        this.mListener = listener;
        this.mHandler = new ListenerDelegateHandler(handler == null ? context.getMainLooper() : handler.getLooper(), this.mListener);
    }

    public Object getListener() {
        return this.mListener;
    }

    public void onCoverTouchAccept() throws RemoteException {
    }

    public void onCoverTouchReject() throws RemoteException {
    }

    public void onCoverTapLeft() throws RemoteException {
    }

    public void onCoverTapMid() throws RemoteException {
    }

    public void onCoverTapRight() throws RemoteException {
    }

    public void onSystemCoverEvent(int event, Bundle args) throws RemoteException {
        if (event == 1) {
            this.mHandler.sendEmptyMessage(0);
        }
    }
}
