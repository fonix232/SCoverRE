package com.samsung.android.cover;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.cover.CoverManager.CoverStateListener;
import com.samsung.android.cover.ICoverStateListenerCallback.Stub;

class CoverStateListenerDelegate extends Stub {
    private static final int MSG_LISTEN_COVER_ATTACH_STATE_CHANGE = 1;
    private static final int MSG_LISTEN_COVER_SWITCH_STATE_CHANGE = 0;
    public static final int TYPE_COVER_STATE_LISTENER = 2;
    private ListenerDelegateHandler mHandler;
    private final CoverStateListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final CoverStateListener mListener;

        ListenerDelegateHandler(Looper looper, CoverStateListener coverStateListener) {
            super(looper);
            this.mListener = coverStateListener;
        }

        public void handleMessage(Message message) {
            boolean z = true;
            if (this.mListener != null) {
                CoverStateListener coverStateListener;
                switch (message.what) {
                    case 0:
                        coverStateListener = this.mListener;
                        if (message.arg1 != 1) {
                            z = false;
                        }
                        coverStateListener.onCoverSwitchStateChanged(z);
                        return;
                    case 1:
                        coverStateListener = this.mListener;
                        if (message.arg1 != 1) {
                            z = false;
                        }
                        coverStateListener.onCoverAttachStateChanged(z);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    CoverStateListenerDelegate(CoverStateListener coverStateListener, Handler handler, Context context) {
        this.mListener = coverStateListener;
        this.mHandler = new ListenerDelegateHandler(handler == null ? context.getMainLooper() : handler.getLooper(), this.mListener);
    }

    public CoverStateListener getListener() {
        return this.mListener;
    }

    public String getListenerInfo() throws RemoteException {
        return this.mListener.toString();
    }

    public void onCoverAttachStateChanged(boolean z) throws RemoteException {
        Message.obtain(this.mHandler, 1, z ? 1 : 0, 0).sendToTarget();
    }

    public void onCoverSwitchStateChanged(boolean z) throws RemoteException {
        Message.obtain(this.mHandler, 0, z ? 1 : 0, 0).sendToTarget();
    }
}
