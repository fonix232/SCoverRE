package com.samsung.android.sdk.cover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.cover.INfcLedCoverTouchListenerCallback.Stub;
import com.samsung.android.sdk.cover.ScoverManager.LedSystemEventListener;
import java.lang.ref.WeakReference;

class LedSystemEventListenerDelegate extends Stub {
    private static final int MSG_SYSTEM_COVER_EVENT = 0;
    private ListenerDelegateHandler mHandler;
    private LedSystemEventListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final WeakReference<LedSystemEventListener> mListenerRef;

        public ListenerDelegateHandler(Looper looper, LedSystemEventListener listener) {
            super(looper);
            this.mListenerRef = new WeakReference(listener);
        }

        public void handleMessage(Message msg) {
            LedSystemEventListener listener = (LedSystemEventListener) this.mListenerRef.get();
            if (listener != null) {
                switch (msg.what) {
                    case ConnectionResult.SUCCESS /*0*/:
                        listener.onSystemCoverEvent(msg.arg1, msg.obj);
                    default:
                }
            }
        }
    }

    LedSystemEventListenerDelegate(LedSystemEventListener listener, Handler handler, Context context) {
        Looper looper;
        this.mListener = listener;
        if (handler == null) {
            looper = context.getMainLooper();
        } else {
            looper = handler.getLooper();
        }
        this.mHandler = new ListenerDelegateHandler(looper, this.mListener);
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
        Message msg = this.mHandler.obtainMessage(0);
        msg.arg1 = event;
        msg.obj = args;
        msg.sendToTarget();
    }
}
