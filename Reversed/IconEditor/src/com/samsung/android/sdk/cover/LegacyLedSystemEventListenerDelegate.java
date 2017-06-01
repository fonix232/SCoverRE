package com.samsung.android.sdk.cover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.cover.INfcLedCoverTouchListenerCallback.Stub;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LegacyLedSystemEventListenerDelegate extends Stub {
    private static final int MSG_SYSTEM_COVER_EVENT = 0;
    private static final String SYSTEM_EVENT_LED_OFF_COMMAND = "led_off_command";
    private static final String TAG;
    private ListenerDelegateHandler mHandler;
    private Object mListener;

    private static class ListenerDelegateHandler extends Handler {
        private final Object mListener;

        public ListenerDelegateHandler(Looper looper, Object listener) {
            super(looper);
            this.mListener = listener;
        }

        public void handleMessage(Message msg) {
            if (this.mListener != null) {
                switch (msg.what) {
                    case LegacyLedSystemEventListenerDelegate.MSG_SYSTEM_COVER_EVENT /*0*/:
                        int[] args = (int[]) msg.obj;
                        Method onSystemCoverEventMethod = null;
                        try {
                            onSystemCoverEventMethod = this.mListener.getClass().getMethod("onSystemCoverEvent", new Class[]{Integer.TYPE, Bundle.class});
                        } catch (SecurityException e) {
                            Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error getting onSystemCoverEvent method", e);
                        } catch (NoSuchMethodException e2) {
                            Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error getting onSystemCoverEvent method", e2);
                        }
                        if (onSystemCoverEventMethod != null) {
                            if (args != null) {
                                try {
                                    if (args.length >= 1) {
                                        new Bundle().putInt(LegacyLedSystemEventListenerDelegate.SYSTEM_EVENT_LED_OFF_COMMAND, args[LegacyLedSystemEventListenerDelegate.MSG_SYSTEM_COVER_EVENT]);
                                        onSystemCoverEventMethod.invoke(this.mListener, new Object[]{Integer.valueOf(msg.arg1), argsBundle});
                                        return;
                                    }
                                } catch (IllegalAccessException e3) {
                                    Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error invoking " + onSystemCoverEventMethod.getName(), e3);
                                    return;
                                } catch (IllegalArgumentException e4) {
                                    Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error invoking " + onSystemCoverEventMethod.getName(), e4);
                                    return;
                                } catch (InvocationTargetException e5) {
                                    Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error invoking " + onSystemCoverEventMethod.getName(), e5);
                                    return;
                                }
                            }
                            Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error: system event args empty: " + args);
                        }
                    default:
                }
            }
        }
    }

    static {
        TAG = LegacyLedSystemEventListenerDelegate.class.getSimpleName();
    }

    LegacyLedSystemEventListenerDelegate(Object listener, Handler handler, Context context) {
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

    public void onSystemCoverEvent(int event, int[] args) throws RemoteException {
        Message msg = this.mHandler.obtainMessage(MSG_SYSTEM_COVER_EVENT);
        msg.arg1 = event;
        msg.obj = args;
        msg.sendToTarget();
    }
}
