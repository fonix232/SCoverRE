package com.samsung.android.quickconnect;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.quickconnect.IQuickConnectCallback.Stub;
import java.util.ArrayList;
import java.util.Iterator;

public final class QuickConnectManager {
    public static final int DO_QUICK_CONNECT = 1;
    private static final String TAG = "QuickConnectManager";
    private Context mContext = null;
    private final ArrayList<QuickConnectListenerDelegate> mListenerDelegates = new ArrayList();
    IQuickConnectManager mQuickConnectService;

    public interface QuickConnectListener {
        void onItemSelected();
    }

    private static class QuickConnectListenerDelegate extends Stub {
        public Handler mHandler;
        public QuickConnectListener mListener;

        static class ListenerHandler extends Handler {
            public QuickConnectListener mListener = null;

            public ListenerHandler(Looper looper, QuickConnectListener quickConnectListener) {
                super(looper);
                this.mListener = quickConnectListener;
            }

            public void handleMessage(Message message) {
                if (this.mListener != null) {
                    this.mListener.onItemSelected();
                }
            }
        }

        QuickConnectListenerDelegate(QuickConnectListener quickConnectListener, Looper looper) {
            this.mListener = quickConnectListener;
            this.mHandler = new ListenerHandler(looper, this.mListener);
        }

        public QuickConnectListener getListener() {
            return this.mListener;
        }

        public String getListenerInfo() throws RemoteException {
            return this.mListener.toString();
        }

        public void onQuickConnectCallback() throws RemoteException {
            this.mHandler.sendEmptyMessage(1);
        }
    }

    public QuickConnectManager(Context context, IQuickConnectManager iQuickConnectManager) {
        this.mContext = context;
        this.mQuickConnectService = iQuickConnectManager;
    }

    @Deprecated
    public void registerListener(QuickConnectListener quickConnectListener) {
        QuickConnectListenerDelegate quickConnectListenerDelegate;
        Throwable th;
        if (quickConnectListener == null) {
            Log.w(TAG, "registerListener : listener is null");
            return;
        }
        Log.d(TAG, "registerListener");
        synchronized (this.mListenerDelegates) {
            try {
                IBinder quickConnectListenerDelegate2;
                Iterator it = this.mListenerDelegates.iterator();
                while (it.hasNext()) {
                    QuickConnectListenerDelegate quickConnectListenerDelegate3 = (QuickConnectListenerDelegate) it.next();
                    if (quickConnectListenerDelegate3.getListener() != quickConnectListener) {
                        if (quickConnectListenerDelegate3.getListener().equals(quickConnectListener)) {
                        }
                    }
                    quickConnectListenerDelegate = quickConnectListenerDelegate3;
                }
                quickConnectListenerDelegate = null;
                if (quickConnectListenerDelegate == null) {
                    try {
                        quickConnectListenerDelegate2 = new QuickConnectListenerDelegate(quickConnectListener, this.mContext.getMainLooper());
                        this.mListenerDelegates.add(quickConnectListenerDelegate2);
                    } catch (Throwable th2) {
                        th = th2;
                        QuickConnectListenerDelegate quickConnectListenerDelegate4 = quickConnectListenerDelegate;
                        throw th;
                    }
                }
                Object obj = quickConnectListenerDelegate;
                if (this.mQuickConnectService != null) {
                    this.mQuickConnectService.registerCallback(quickConnectListenerDelegate2, new ComponentName(this.mContext.getPackageName(), this.mContext.getClass().getCanonicalName()));
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: " + e);
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public void registerListener(QuickConnectListener quickConnectListener, Context context) {
        Throwable th;
        if (quickConnectListener == null) {
            Log.w(TAG, "registerListener : listener is null");
            return;
        }
        this.mContext = context;
        try {
            Log.d(TAG, "registerListener with context  " + this.mContext.getClass().getCanonicalName());
        } catch (NullPointerException e) {
            Log.e(TAG, "registerListener with context, context is null ");
        }
        synchronized (this.mListenerDelegates) {
            try {
                QuickConnectListenerDelegate quickConnectListenerDelegate;
                IBinder quickConnectListenerDelegate2;
                Iterator it = this.mListenerDelegates.iterator();
                while (it.hasNext()) {
                    QuickConnectListenerDelegate quickConnectListenerDelegate3 = (QuickConnectListenerDelegate) it.next();
                    if (quickConnectListenerDelegate3.getListener() != quickConnectListener) {
                        if (quickConnectListenerDelegate3.getListener().equals(quickConnectListener)) {
                        }
                    }
                    quickConnectListenerDelegate = quickConnectListenerDelegate3;
                }
                quickConnectListenerDelegate = null;
                if (quickConnectListenerDelegate == null) {
                    try {
                        quickConnectListenerDelegate2 = new QuickConnectListenerDelegate(quickConnectListener, this.mContext.getMainLooper());
                        this.mListenerDelegates.add(quickConnectListenerDelegate2);
                    } catch (Throwable th2) {
                        th = th2;
                        QuickConnectListenerDelegate quickConnectListenerDelegate4 = quickConnectListenerDelegate;
                        throw th;
                    }
                }
                Object obj = quickConnectListenerDelegate;
                if (this.mQuickConnectService != null) {
                    this.mQuickConnectService.registerCallback(quickConnectListenerDelegate2, new ComponentName(this.mContext.getPackageName(), this.mContext.getClass().getCanonicalName()));
                }
            } catch (RemoteException e2) {
                Log.e(TAG, "RemoteException in registerListener: " + e2);
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    @Deprecated
    public void terminate() {
    }

    public void unregisterListener(QuickConnectListener quickConnectListener) {
        if (quickConnectListener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
            return;
        }
        synchronized (this.mListenerDelegates) {
            QuickConnectListenerDelegate quickConnectListenerDelegate = null;
            Iterator it = this.mListenerDelegates.iterator();
            while (it.hasNext()) {
                QuickConnectListenerDelegate quickConnectListenerDelegate2 = (QuickConnectListenerDelegate) it.next();
                if (quickConnectListenerDelegate2.getListener() != quickConnectListener) {
                    if (quickConnectListenerDelegate2.getListener().equals(quickConnectListener)) {
                    }
                }
                quickConnectListenerDelegate = quickConnectListenerDelegate2;
                Log.d(TAG, "unregisterListener- found");
            }
            if (quickConnectListenerDelegate == null) {
                Log.i(TAG, "quickconnectListener is null");
                return;
            }
            try {
                if (this.mQuickConnectService != null) {
                    Log.i(TAG, "mQuickConnectService != null");
                    if (this.mQuickConnectService.unregisterCallback(quickConnectListenerDelegate)) {
                        this.mListenerDelegates.remove(quickConnectListenerDelegate);
                        quickConnectListenerDelegate.mHandler = null;
                        quickConnectListenerDelegate.mListener = null;
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterListener: " + e);
            }
        }
    }
}
