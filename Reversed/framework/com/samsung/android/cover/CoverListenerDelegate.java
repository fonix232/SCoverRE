package com.samsung.android.cover;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.cover.CoverManager.StateListener;
import com.samsung.android.cover.ICoverManagerCallback.Stub;

class CoverListenerDelegate extends Stub {
    private static final String TAG = "CoverManager";
    private ListenerDelegateHandler mHandler;
    private final StateListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final StateListener mListener;

        ListenerDelegateHandler(Looper looper, StateListener stateListener) {
            super(looper);
            this.mListener = stateListener;
        }

        public void handleMessage(Message message) {
            if (this.mListener != null) {
                CoverState coverState = (CoverState) message.obj;
                if (coverState != null) {
                    this.mListener.onCoverStateChanged(coverState);
                } else {
                    Log.e(CoverListenerDelegate.TAG, "coverState : null");
                }
            }
        }
    }

    CoverListenerDelegate(StateListener stateListener, Handler handler, Context context) {
        this.mListener = stateListener;
        this.mHandler = new ListenerDelegateHandler(handler == null ? context.getMainLooper() : handler.getLooper(), this.mListener);
    }

    public void coverCallback(CoverState coverState) throws RemoteException {
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = coverState;
        this.mHandler.sendMessage(obtain);
    }

    public StateListener getListener() {
        return this.mListener;
    }

    public String getListenerInfo() throws RemoteException {
        return this.mListener.toString();
    }
}
