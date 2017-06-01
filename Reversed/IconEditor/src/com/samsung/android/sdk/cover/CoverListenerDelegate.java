package com.samsung.android.sdk.cover;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ICoverManagerCallback.Stub;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import java.lang.ref.WeakReference;

class CoverListenerDelegate extends Stub {
    private static final String TAG;
    static final int hasAttachFieldVersion = 16842752;
    static final int hasModelFieldVersion = 16908288;
    private ListenerDelegateHandler mHandler;
    private final StateListener mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final WeakReference<StateListener> mListenerRef;

        public ListenerDelegateHandler(Looper looper, StateListener listener) {
            super(looper);
            this.mListenerRef = new WeakReference(listener);
        }

        public void handleMessage(Message msg) {
            StateListener listener = (StateListener) this.mListenerRef.get();
            if (listener != null) {
                CoverState coverState = msg.obj;
                if (coverState != null) {
                    ScoverState scoverState;
                    if (ScoverManager.isSupportableVersion(CoverListenerDelegate.hasModelFieldVersion)) {
                        scoverState = new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached, coverState.model);
                    } else if (ScoverManager.isSupportableVersion(CoverListenerDelegate.hasAttachFieldVersion)) {
                        scoverState = new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached);
                    } else {
                        scoverState = new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel);
                    }
                    listener.onCoverStateChanged(scoverState);
                    return;
                }
                Log.e(CoverListenerDelegate.TAG, "coverState : null");
            }
        }
    }

    static {
        TAG = ScoverManager.class.getSimpleName();
    }

    CoverListenerDelegate(StateListener listener, Handler handler, Context context) {
        Looper looper;
        this.mListener = listener;
        if (handler == null) {
            looper = context.getMainLooper();
        } else {
            looper = handler.getLooper();
        }
        this.mHandler = new ListenerDelegateHandler(looper, this.mListener);
    }

    public StateListener getListener() {
        return this.mListener;
    }

    public void coverCallback(CoverState state) throws RemoteException {
        Message msg = Message.obtain();
        msg.what = 0;
        msg.obj = state;
        this.mHandler.sendMessage(msg);
    }

    public String getListenerInfo() throws RemoteException {
        return this.mListener.toString();
    }
}
