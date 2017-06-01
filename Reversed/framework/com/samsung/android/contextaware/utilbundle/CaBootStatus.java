package com.samsung.android.contextaware.utilbundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaBootStatus implements IUtilManager {
    private static final int BOOT_COMPLETED = 4099;
    private static final int LOG_CONTEXT_NULL = 4097;
    private static final int LOG_INTENT_NULL = 4098;
    private static volatile CaBootStatus instance;
    private boolean mBootComplete = false;
    private Context mContext;
    private final Handler mHandler = new C00132();
    private final CopyOnWriteArrayList<IBootStatusObserver> mListeners = new CopyOnWriteArrayList();
    private final BroadcastReceiver mReceiver = new C00121();

    class C00121 extends BroadcastReceiver {
        C00121() {
        }

        public void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaBootStatus.this.mHandler.sendEmptyMessage(CaBootStatus.LOG_CONTEXT_NULL);
            } else if (intent == null) {
                CaBootStatus.this.mHandler.sendEmptyMessage(CaBootStatus.LOG_INTENT_NULL);
            } else {
                if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                    CaBootStatus.this.mHandler.sendEmptyMessage(CaBootStatus.BOOT_COMPLETED);
                }
            }
        }
    }

    class C00132 extends Handler {
        C00132() {
        }

        public void handleMessage(Message message) {
            if (message.what == CaBootStatus.LOG_CONTEXT_NULL) {
                CaLogger.info("context is null");
            } else if (message.what == CaBootStatus.LOG_INTENT_NULL) {
                CaLogger.info("intent is null");
            } else if (message.what == CaBootStatus.BOOT_COMPLETED) {
                CaLogger.info("Boot Complete");
                if (CaBootStatus.this.mContext != null) {
                    CaBootStatus.this.mContext.unregisterReceiver(CaBootStatus.this.mReceiver);
                }
                CaBootStatus.this.mBootComplete = true;
                CaBootStatus.this.notifyObservers();
            }
        }
    }

    private void checkBootComplete() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        if (this.mContext != null) {
            this.mContext.registerReceiver(this.mReceiver, intentFilter);
        }
    }

    public static CaBootStatus getInstance() {
        if (instance == null) {
            synchronized (CaBootStatus.class) {
                if (instance == null) {
                    instance = new CaBootStatus();
                }
            }
        }
        return instance;
    }

    private void notifyObservers() {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            IBootStatusObserver iBootStatusObserver = (IBootStatusObserver) it.next();
            if (iBootStatusObserver != null) {
                iBootStatusObserver.bootCompleted();
            }
        }
    }

    public void initializeManager(Context context) {
        this.mContext = context;
        checkBootComplete();
    }

    public boolean isBootComplete() {
        return this.mBootComplete;
    }

    public final void registerObserver(IBootStatusObserver iBootStatusObserver) {
        if (!this.mListeners.contains(iBootStatusObserver)) {
            this.mListeners.add(iBootStatusObserver);
        }
    }

    public void terminateManager() {
    }

    public final void unregisterObserver(IBootStatusObserver iBootStatusObserver) {
        if (this.mListeners.contains(iBootStatusObserver)) {
            this.mListeners.remove(iBootStatusObserver);
        }
    }
}
