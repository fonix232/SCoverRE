package com.samsung.android.cover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.cover.CoverManager.LedSystemEventListener;
import com.samsung.android.cover.INfcLedCoverTouchListenerCallback.Stub;

class LedSystemEventListenerDelegate extends Stub {
    private static final int MSG_SYSTEM_COVER_EVENT = 0;
    private ListenerDelegateHandler mHandler;
    private LedSystemEventListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final LedSystemEventListener mListener;

        ListenerDelegateHandler(Looper looper, LedSystemEventListener ledSystemEventListener) {
            super(looper);
            this.mListener = ledSystemEventListener;
        }

        public void handleMessage(Message message) {
            if (this.mListener != null) {
                switch (message.what) {
                    case 0:
                        this.mListener.onSystemCoverEvent(message.arg1, (Bundle) message.obj);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    LedSystemEventListenerDelegate(LedSystemEventListener ledSystemEventListener, Handler handler, Context context) {
        this.mListener = ledSystemEventListener;
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
    }

    public void onCoverTouchReject() throws RemoteException {
    }

    public void onSystemCoverEvent(int i, Bundle bundle) throws RemoteException {
        Message obtainMessage = this.mHandler.obtainMessage(0);
        obtainMessage.arg1 = i;
        obtainMessage.obj = bundle;
        obtainMessage.sendToTarget();
    }
}
