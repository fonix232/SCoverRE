package com.samsung.android.cocktailbar;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import android.widget.RemoteViews;
import com.samsung.android.cocktailbar.ICocktailHost.Stub;
import java.lang.ref.WeakReference;

public class CocktailHost {
    static final int HANDLE_COCKTAIL_CLOSE_CONTEXTUAL = 5;
    static final int HANDLE_COCKTAIL_PARTIALLY_UPDATE = 2;
    static final int HANDLE_COCKTAIL_REMOVE = 3;
    static final int HANDLE_COCKTAIL_SEND_EXTRA_DATA = 12;
    static final int HANDLE_COCKTAIL_SET_PULL_TO_REFRESH = 13;
    static final int HANDLE_COCKTAIL_SHOW = 4;
    static final int HANDLE_COCKTAIL_SWITCH_DEFAULT = 10;
    static final int HANDLE_COCKTAIL_TICKER_DISABLE = 9;
    static final int HANDLE_COCKTAIL_UPDATE = 1;
    static final int HANDLE_COCKTAIL_UPDATE_EXTRA = 8;
    static final int HANDLE_COCKTAIL_UPDATE_TOOL_LAUNCHER = 7;
    static final int HANDLE_COCKTAIL_VIEW_DATA_CHANGED = 6;
    static final int HANDLE_NOTIFY_CHANGE_VISIBLE_EDGE_SERVICE = 102;
    static final int HANDLE_NOTIFY_KEYGUARD_STATE = 100;
    static final int HANDLE_NOTIFY_WAKEUP_STATE = 101;
    private static final String TAG = CocktailHost.class.getSimpleName();
    static ICocktailBarService sService;
    static final Object sServiceLock = new Object();
    ICallbackListener mCallbackListener;
    private final Callbacks mCallbacks;
    private String mContextOpPackageName;
    private final Handler mHandler;
    private int mListeningCategory;

    static class Callbacks extends Stub {
        private final WeakReference<Handler> mWeakHandler;

        public Callbacks(Handler handler) {
            this.mWeakHandler = new WeakReference(handler);
        }

        public void changeVisibleEdgeService(boolean z, int i) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(102, z ? 1 : 0, i).sendToTarget();
            }
        }

        public void closeContextualCocktail(int i, int i2, int i3) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(5, i, i2, Integer.valueOf(i3)).sendToTarget();
            }
        }

        public void notifyKeyguardState(boolean z, int i) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(100, z ? 1 : 0, i).sendToTarget();
            }
        }

        public void notifyWakeUpState(boolean z, int i, int i2) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(101, z ? 1 : 0, i, Integer.valueOf(i2)).sendToTarget();
            }
        }

        public void partiallyUpdateCocktail(int i, RemoteViews remoteViews, int i2) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(2, i, i2, remoteViews).sendToTarget();
            }
        }

        public void removeCocktail(int i, int i2) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(3, i, i2).sendToTarget();
            }
        }

        public void sendExtraData(int i, Bundle bundle) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(12, i, 0, bundle).sendToTarget();
            }
        }

        public void setDisableTickerView(int i, int i2) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(9, i, i2).sendToTarget();
            }
        }

        public void setPullToRefresh(int i, int i2, PendingIntent pendingIntent, int i3) throws RemoteException {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(13, i, i2, pendingIntent).sendToTarget();
            }
        }

        public void showCocktail(int i, int i2) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(4, i, i2).sendToTarget();
            }
        }

        public void switchDefaultCocktail(int i) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(10, i, 0).sendToTarget();
            }
        }

        public void updateCocktail(int i, Cocktail cocktail, int i2) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(1, i, i2, cocktail).sendToTarget();
            }
        }

        public void updateToolLauncher(int i) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(7, i, 0).sendToTarget();
            }
        }

        public void viewDataChanged(int i, int i2, int i3) {
            Handler handler = (Handler) this.mWeakHandler.get();
            if (handler != null) {
                handler.obtainMessage(6, i, i2, Integer.valueOf(i3)).sendToTarget();
            }
        }
    }

    public interface ICallbackListener {
        void onChangeVisibleEdgeService(boolean z, int i);

        void onCloseContextualCocktail(int i, int i2, int i3);

        void onNotifyKeyguardState(boolean z, int i);

        void onNotifyWakeUpModeState(boolean z, int i, int i2);

        void onPartiallyUpdateCocktail(int i, RemoteViews remoteViews, int i2);

        void onRemoveCocktail(int i, int i2);

        void onSendExtraDataToCocktailBar(Bundle bundle, int i);

        void onSetDisableTickerView(int i, int i2);

        void onSetPullToRefresh(int i, int i2, PendingIntent pendingIntent);

        void onShowCocktail(int i, int i2);

        void onSwitchDefaultCocktail(int i);

        void onUpdateCocktail(int i, Cocktail cocktail, int i2);

        void onUpdateToolLauncher(int i);

        void onViewDataChanged(int i, int i2, int i3);
    }

    class UpdateHandler extends Handler {
        public UpdateHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    CocktailHost.this.updateCocktail(message.arg1, (Cocktail) message.obj, message.arg2);
                    return;
                case 2:
                    CocktailHost.this.partiallyUpdateCocktail(message.arg1, (RemoteViews) message.obj, message.arg2);
                    return;
                case 3:
                    CocktailHost.this.removeCocktail(message.arg1, message.arg2);
                    return;
                case 4:
                    CocktailHost.this.showCocktail(message.arg1, message.arg2);
                    return;
                case 5:
                    CocktailHost.this.closeContextualCocktail(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                    return;
                case 6:
                    CocktailHost.this.viewDataChanged(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                    return;
                case 7:
                    CocktailHost.this.updateToolLauncher(message.arg1);
                    return;
                case 9:
                    CocktailHost.this.setDisableTickerView(message.arg1, message.arg2);
                    return;
                case 10:
                    CocktailHost.this.switchDefaultCocktail(message.arg1);
                    return;
                case 12:
                    CocktailHost.this.sendExtraDataToCocktailBar(message.arg1, (Bundle) message.obj);
                    return;
                case 13:
                    CocktailHost.this.setPullToRefresh(message.arg1, message.arg2, (PendingIntent) message.obj);
                    return;
                case 100:
                    CocktailHost.this.notifyKeyguardState(message.arg1, message.arg2);
                    return;
                case 101:
                    CocktailHost.this.notifyWakeUpState(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                    return;
                case 102:
                    CocktailHost.this.changeVisibleEdgeService(message.arg1, message.arg2);
                    return;
                default:
                    return;
            }
        }
    }

    public CocktailHost(Context context, int i, ICallbackListener iCallbackListener) {
        this(context, i, iCallbackListener, context.getMainLooper());
    }

    public CocktailHost(Context context, int i, ICallbackListener iCallbackListener, Looper looper) {
        this.mListeningCategory = 0;
        this.mContextOpPackageName = context.getOpPackageName();
        this.mCallbackListener = iCallbackListener;
        this.mHandler = new UpdateHandler(looper);
        this.mCallbacks = new Callbacks(this.mHandler);
        this.mListeningCategory = i;
        bindService(i);
    }

    public CocktailHost(Context context, ICallbackListener iCallbackListener) {
        this(context, iCallbackListener, context.getMainLooper());
    }

    public CocktailHost(Context context, ICallbackListener iCallbackListener, Looper looper) {
        this.mListeningCategory = 0;
        this.mContextOpPackageName = context.getOpPackageName();
        this.mCallbackListener = iCallbackListener;
        this.mHandler = new UpdateHandler(looper);
        this.mCallbacks = new Callbacks(this.mHandler);
        bindService(0);
    }

    private void bindService(int i) {
        synchronized (sServiceLock) {
            if (sService == null) {
                sService = ICocktailBarService.Stub.asInterface(ServiceManager.getService("CocktailBarService"));
            }
            try {
                if (sService != null) {
                    sService.setCocktailHostCallbacks(this.mCallbacks, this.mContextOpPackageName, i);
                } else {
                    Slog.m40d(TAG, "bindService: can not get ICocktailBarService");
                }
            } catch (RemoteException e) {
            }
        }
    }

    private void changeVisibleEdgeService(int i, int i2) {
        boolean z = true;
        ICallbackListener iCallbackListener = this.mCallbackListener;
        if (i != 1) {
            z = false;
        }
        iCallbackListener.onChangeVisibleEdgeService(z, i2);
    }

    private void closeContextualCocktail(int i, int i2, int i3) {
        this.mCallbackListener.onCloseContextualCocktail(i, i2, i3);
    }

    private void notifyKeyguardState(int i, int i2) {
        boolean z = true;
        ICallbackListener iCallbackListener = this.mCallbackListener;
        if (i != 1) {
            z = false;
        }
        iCallbackListener.onNotifyKeyguardState(z, i2);
    }

    private void notifyWakeUpState(int i, int i2, int i3) {
        boolean z = true;
        ICallbackListener iCallbackListener = this.mCallbackListener;
        if (i != 1) {
            z = false;
        }
        iCallbackListener.onNotifyWakeUpModeState(z, i2, i3);
    }

    private void partiallyUpdateCocktail(int i, RemoteViews remoteViews, int i2) {
        this.mCallbackListener.onPartiallyUpdateCocktail(i, remoteViews, i2);
    }

    private void removeCocktail(int i, int i2) {
        this.mCallbackListener.onRemoveCocktail(i, i2);
    }

    private void sendExtraDataToCocktailBar(int i, Bundle bundle) {
        this.mCallbackListener.onSendExtraDataToCocktailBar(bundle, i);
    }

    private void setDisableTickerView(int i, int i2) {
        this.mCallbackListener.onSetDisableTickerView(i, i2);
    }

    private void setPullToRefresh(int i, int i2, PendingIntent pendingIntent) {
        this.mCallbackListener.onSetPullToRefresh(i, i2, pendingIntent);
    }

    private void showCocktail(int i, int i2) {
        this.mCallbackListener.onShowCocktail(i, i2);
    }

    private void switchDefaultCocktail(int i) {
        this.mCallbackListener.onSwitchDefaultCocktail(i);
    }

    private void updateCocktail(int i, Cocktail cocktail, int i2) {
        this.mCallbackListener.onUpdateCocktail(i, cocktail, i2);
    }

    private void updateToolLauncher(int i) {
        this.mCallbackListener.onUpdateToolLauncher(i);
    }

    private void viewDataChanged(int i, int i2, int i3) {
        this.mCallbackListener.onViewDataChanged(i, i2, i3);
    }

    public void startListening() {
        try {
            sService.startListening(this.mCallbacks, this.mContextOpPackageName, this.mListeningCategory);
        } catch (Throwable e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public void startListening(int i) {
        try {
            this.mListeningCategory = i;
            sService.startListening(this.mCallbacks, this.mContextOpPackageName, i);
        } catch (Throwable e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public void stopListening() {
        try {
            sService.stopListening(this.mContextOpPackageName);
        } catch (Throwable e) {
            throw new RuntimeException("system server dead?", e);
        }
    }
}
