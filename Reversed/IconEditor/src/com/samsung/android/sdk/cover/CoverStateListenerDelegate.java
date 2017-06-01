package com.samsung.android.sdk.cover;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.cover.ICoverStateListenerCallback.Stub;
import com.samsung.android.sdk.cover.ScoverManager.CoverStateListener;
import java.lang.ref.WeakReference;

class CoverStateListenerDelegate extends Stub {
    private static final int MSG_LISTEN_COVER_ATTACH_STATE_CHANGE = 1;
    private static final int MSG_LISTEN_COVER_SWITCH_STATE_CHANGE = 0;
    public static final int TYPE_COVER_STATE_LISTENER = 2;
    private ListenerDelegateHandler mHandler;
    private final CoverStateListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final WeakReference<CoverStateListener> mListenerRef;

        public ListenerDelegateHandler(Looper looper, CoverStateListener listener) {
            super(looper);
            this.mListenerRef = new WeakReference(listener);
        }

        public void handleMessage(Message msg) {
            boolean z = true;
            CoverStateListener listener = (CoverStateListener) this.mListenerRef.get();
            if (listener != null) {
                switch (msg.what) {
                    case CoverStateListenerDelegate.MSG_LISTEN_COVER_SWITCH_STATE_CHANGE /*0*/:
                        if (msg.arg1 != CoverStateListenerDelegate.MSG_LISTEN_COVER_ATTACH_STATE_CHANGE) {
                            z = false;
                        }
                        listener.onCoverSwitchStateChanged(z);
                    case CoverStateListenerDelegate.MSG_LISTEN_COVER_ATTACH_STATE_CHANGE /*1*/:
                        if (msg.arg1 != CoverStateListenerDelegate.MSG_LISTEN_COVER_ATTACH_STATE_CHANGE) {
                            z = false;
                        }
                        listener.onCoverAttachStateChanged(z);
                    default:
                }
            }
        }
    }

    CoverStateListenerDelegate(CoverStateListener listener, Handler handler, Context context) {
        Looper looper;
        this.mListener = listener;
        if (handler == null) {
            looper = context.getMainLooper();
        } else {
            looper = handler.getLooper();
        }
        this.mHandler = new ListenerDelegateHandler(looper, this.mListener);
    }

    public CoverStateListener getListener() {
        return this.mListener;
    }

    public void onCoverSwitchStateChanged(boolean switchState) throws RemoteException {
        int i;
        Handler handler = this.mHandler;
        if (switchState) {
            i = MSG_LISTEN_COVER_ATTACH_STATE_CHANGE;
        } else {
            i = MSG_LISTEN_COVER_SWITCH_STATE_CHANGE;
        }
        Message.obtain(handler, MSG_LISTEN_COVER_SWITCH_STATE_CHANGE, i, MSG_LISTEN_COVER_SWITCH_STATE_CHANGE).sendToTarget();
    }

    public void onCoverAttachStateChanged(boolean attached) throws RemoteException {
        int i;
        Handler handler = this.mHandler;
        if (attached) {
            i = MSG_LISTEN_COVER_ATTACH_STATE_CHANGE;
        } else {
            i = MSG_LISTEN_COVER_SWITCH_STATE_CHANGE;
        }
        Message.obtain(handler, MSG_LISTEN_COVER_ATTACH_STATE_CHANGE, i, MSG_LISTEN_COVER_SWITCH_STATE_CHANGE).sendToTarget();
    }

    public String getListenerInfo() throws RemoteException {
        return this.mListener.toString();
    }
}
