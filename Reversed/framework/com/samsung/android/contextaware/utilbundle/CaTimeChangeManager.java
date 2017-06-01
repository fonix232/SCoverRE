package com.samsung.android.contextaware.utilbundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Global;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaTimeChangeManager implements IUtilManager {
    private static final int LOG_CONTEXT_NULL = 4113;
    private static final int LOG_INTENT_NULL = 4114;
    private static final int TIME_CHANGED = 4115;
    private static volatile CaTimeChangeManager instance;
    private boolean mAutoCheck;
    private Context mContext;
    private final Handler mHandler = new C00392();
    private final CopyOnWriteArrayList<ITimeChangeObserver> mListeners = new CopyOnWriteArrayList();
    private final BroadcastReceiver mReceiver = new C00381();

    class C00381 extends BroadcastReceiver {
        C00381() {
        }

        public void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaTimeChangeManager.this.mHandler.sendEmptyMessage(CaTimeChangeManager.LOG_CONTEXT_NULL);
            } else if (intent == null) {
                CaTimeChangeManager.this.mHandler.sendEmptyMessage(CaTimeChangeManager.LOG_INTENT_NULL);
            } else {
                if (intent.getAction().equals("android.intent.action.TIME_SET")) {
                    CaTimeChangeManager.this.mHandler.sendEmptyMessage(CaTimeChangeManager.TIME_CHANGED);
                }
            }
        }
    }

    class C00392 extends Handler {
        C00392() {
        }

        public void handleMessage(Message message) {
            if (message.what == CaTimeChangeManager.LOG_CONTEXT_NULL) {
                CaLogger.info("context is null");
            } else if (message.what == CaTimeChangeManager.LOG_INTENT_NULL) {
                CaLogger.info("intent is null");
            } else if (message.what == CaTimeChangeManager.TIME_CHANGED) {
                boolean z = false;
                try {
                    z = Global.getInt(CaTimeChangeManager.this.mContext.getContentResolver(), "auto_time") > 0;
                } catch (Throwable e) {
                    CaLogger.error("settings not found");
                    e.printStackTrace();
                }
                CaLogger.info("Time Change, auto old:" + CaTimeChangeManager.this.mAutoCheck + " new:" + z);
                if (CaTimeChangeManager.this.mAutoCheck || !z) {
                    if (!z) {
                    }
                    CaTimeChangeManager.this.mAutoCheck = z;
                }
                CaTimeChangeManager.this.notifyObservers();
                CaTimeChangeManager.this.mAutoCheck = z;
            }
        }
    }

    private void checkTimeChange() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_SET");
        if (this.mContext != null) {
            this.mContext.registerReceiver(this.mReceiver, intentFilter);
        }
    }

    public static CaTimeChangeManager getInstance() {
        if (instance == null) {
            synchronized (CaTimeChangeManager.class) {
                if (instance == null) {
                    instance = new CaTimeChangeManager();
                }
            }
        }
        return instance;
    }

    private void notifyObservers() {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ITimeChangeObserver iTimeChangeObserver = (ITimeChangeObserver) it.next();
            if (iTimeChangeObserver != null) {
                iTimeChangeObserver.onTimeChanged();
            }
        }
    }

    public void initializeManager(Context context) {
        boolean z = false;
        this.mContext = context;
        checkTimeChange();
        try {
            if (Global.getInt(context.getContentResolver(), "auto_time") > 0) {
                z = true;
            }
            this.mAutoCheck = z;
        } catch (Throwable e) {
            CaLogger.error("settings not found");
            e.printStackTrace();
        }
    }

    public final void registerObserver(ITimeChangeObserver iTimeChangeObserver) {
        if (!this.mListeners.contains(iTimeChangeObserver)) {
            this.mListeners.add(iTimeChangeObserver);
        }
    }

    public void terminateManager() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
    }

    public final void unregisterObserver(ITimeChangeObserver iTimeChangeObserver) {
        if (this.mListeners.contains(iTimeChangeObserver)) {
            this.mListeners.remove(iTimeChangeObserver);
        }
    }
}
