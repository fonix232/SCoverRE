package com.samsung.android.sdk.cover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.cover.INfcLedCoverTouchListenerCallback.Stub;
import com.samsung.android.sdk.cover.ScoverManager.NfcLedCoverTouchListener;
import java.lang.ref.WeakReference;

class NfcLedCoverTouchListenerDelegate extends Stub {
    private static final int MSG_LISTEN_COVER_TOUCH_ACCEPT = 0;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT = 1;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT_TAP_LEFT = 2;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT_TAP_MID = 3;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT_TAP_RIGHT = 4;
    private ListenerDelegateHandler mHandler;
    private NfcLedCoverTouchListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final WeakReference<NfcLedCoverTouchListener> mListenerRef;

        public ListenerDelegateHandler(Looper looper, NfcLedCoverTouchListener listener) {
            super(looper);
            this.mListenerRef = new WeakReference(listener);
        }

        public void handleMessage(Message msg) {
            NfcLedCoverTouchListener listener = (NfcLedCoverTouchListener) this.mListenerRef.get();
            if (listener != null) {
                switch (msg.what) {
                    case NfcLedCoverTouchListenerDelegate.MSG_LISTEN_COVER_TOUCH_ACCEPT /*0*/:
                        listener.onCoverTouchAccept();
                    case NfcLedCoverTouchListenerDelegate.MSG_LISTEN_COVER_TOUCH_REJECT /*1*/:
                        listener.onCoverTouchReject();
                    case NfcLedCoverTouchListenerDelegate.MSG_LISTEN_COVER_TOUCH_REJECT_TAP_LEFT /*2*/:
                        listener.onCoverTapLeft();
                    case NfcLedCoverTouchListenerDelegate.MSG_LISTEN_COVER_TOUCH_REJECT_TAP_MID /*3*/:
                        listener.onCoverTapMid();
                    case NfcLedCoverTouchListenerDelegate.MSG_LISTEN_COVER_TOUCH_REJECT_TAP_RIGHT /*4*/:
                        listener.onCoverTapRight();
                    default:
                }
            }
        }
    }

    NfcLedCoverTouchListenerDelegate(NfcLedCoverTouchListener listener, Handler handler, Context context) {
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
        this.mHandler.obtainMessage(MSG_LISTEN_COVER_TOUCH_ACCEPT).sendToTarget();
    }

    public void onCoverTouchReject() throws RemoteException {
        this.mHandler.obtainMessage(MSG_LISTEN_COVER_TOUCH_REJECT).sendToTarget();
    }

    public void onCoverTapLeft() throws RemoteException {
        this.mHandler.obtainMessage(MSG_LISTEN_COVER_TOUCH_REJECT_TAP_LEFT).sendToTarget();
    }

    public void onCoverTapMid() throws RemoteException {
        this.mHandler.obtainMessage(MSG_LISTEN_COVER_TOUCH_REJECT_TAP_MID).sendToTarget();
    }

    public void onCoverTapRight() throws RemoteException {
        this.mHandler.obtainMessage(MSG_LISTEN_COVER_TOUCH_REJECT_TAP_RIGHT).sendToTarget();
    }

    public void onSystemCoverEvent(int arg0, Bundle arg1) throws RemoteException {
    }
}
