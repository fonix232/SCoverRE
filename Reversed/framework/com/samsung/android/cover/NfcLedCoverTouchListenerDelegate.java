package com.samsung.android.cover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.cover.CoverManager.NfcLedCoverTouchListener;
import com.samsung.android.cover.INfcLedCoverTouchListenerCallback.Stub;

class NfcLedCoverTouchListenerDelegate extends Stub {
    private static final int MSG_LISTEN_COVER_TOUCH_ACCEPT = 0;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT = 1;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT_TAP_LEFT = 2;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT_TAP_MID = 3;
    private static final int MSG_LISTEN_COVER_TOUCH_REJECT_TAP_RIGHT = 4;
    private ListenerDelegateHandler mHandler;
    private NfcLedCoverTouchListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final NfcLedCoverTouchListener mListener;

        ListenerDelegateHandler(Looper looper, NfcLedCoverTouchListener nfcLedCoverTouchListener) {
            super(looper);
            this.mListener = nfcLedCoverTouchListener;
        }

        public void handleMessage(Message message) {
            if (this.mListener != null) {
                switch (message.what) {
                    case 0:
                        this.mListener.onCoverTouchAccept();
                        return;
                    case 1:
                        this.mListener.onCoverTouchReject();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    NfcLedCoverTouchListenerDelegate(NfcLedCoverTouchListener nfcLedCoverTouchListener, Handler handler, Context context) {
        this.mListener = nfcLedCoverTouchListener;
        this.mHandler = new ListenerDelegateHandler(handler == null ? context.getMainLooper() : handler.getLooper(), this.mListener);
    }

    public Object getListener() {
        return this.mListener;
    }

    public void onCoverTapLeft() throws RemoteException {
    }

    public void onCoverTapMid() throws RemoteException {
    }

    public void onCoverTapRight() throws RemoteException {
    }

    public void onCoverTouchAccept() throws RemoteException {
        this.mHandler.obtainMessage(0).sendToTarget();
    }

    public void onCoverTouchReject() throws RemoteException {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    public void onSystemCoverEvent(int i, Bundle bundle) throws RemoteException {
    }
}
