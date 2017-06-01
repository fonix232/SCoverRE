package com.samsung.android.contextaware.utilbundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.WindowManager;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaPowerManager implements IApPowerObservable, IUtilManager {
    private static final int AP_IS_SLEEP = 4099;
    private static final int AP_IS_WAKEUP = 4100;
    private static final int LOG_CONTEXT_NULL = 4097;
    private static final int LOG_INTENT_NULL = 4098;
    private static final int POWER_IS_CONNECTED = 4101;
    private static final int POWER_IS_DISCONNECTED = 4102;
    private static volatile CaPowerManager instance;
    private WakeLock mAPWakeLock;
    private Context mContext;
    private final Handler mHandler = new C00362();
    private final CopyOnWriteArrayList<IApPowerObserver> mListeners = new CopyOnWriteArrayList();
    private final BroadcastReceiver mReceiver = new C00351();

    class C00351 extends BroadcastReceiver {
        private static final String AP_SLEEP = "android.intent.action.SCREEN_OFF";
        private static final String AP_WAKEUP = "android.intent.action.SCREEN_ON";

        C00351() {
        }

        public final void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.LOG_CONTEXT_NULL);
            } else if (intent == null) {
                CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.LOG_INTENT_NULL);
            } else {
                if (intent.getAction().equals(AP_SLEEP)) {
                    CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.AP_IS_SLEEP);
                } else if (intent.getAction().equals(AP_WAKEUP)) {
                    CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.AP_IS_WAKEUP);
                } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                    CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.POWER_IS_CONNECTED);
                } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                    CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.POWER_IS_DISCONNECTED);
                }
            }
        }
    }

    class C00362 extends Handler {
        C00362() {
        }

        public void handleMessage(Message message) {
            long currentTimeMillis = System.currentTimeMillis();
            if (message.what == CaPowerManager.LOG_CONTEXT_NULL) {
                CaLogger.info("context is null");
            } else if (message.what == CaPowerManager.LOG_INTENT_NULL) {
                CaLogger.info("intent is null");
            } else if (message.what == CaPowerManager.AP_IS_SLEEP) {
                CaLogger.info("AP_SLEEP");
                CaPowerManager.this.notifyApPowerObserver(-46, currentTimeMillis);
                CaPowerManager.this.sendApStatusToSensorHub(-46);
                CaTimeManager.getInstance().sendCurTimeToSensorHub();
            } else if (message.what == CaPowerManager.AP_IS_WAKEUP) {
                CaLogger.info("AP_WAKEUP");
                CaPowerManager.this.notifyApPowerObserver(-47, currentTimeMillis);
                CaPowerManager.this.sendApStatusToSensorHub(-47);
            } else if (message.what == CaPowerManager.POWER_IS_CONNECTED) {
                CaLogger.info("POWER_CONNECTED");
                CaPowerManager.this.sendApStatusToSensorHub(-42);
            } else if (message.what == CaPowerManager.POWER_IS_DISCONNECTED) {
                CaLogger.info("POWER_DISCONNECTED");
                CaPowerManager.this.sendApStatusToSensorHub(-41);
            }
        }
    }

    public static CaPowerManager getInstance() {
        if (instance == null) {
            synchronized (CaPowerManager.class) {
                if (instance == null) {
                    instance = new CaPowerManager();
                }
            }
        }
        return instance;
    }

    private void sendApStatusToSensorHub(int i) {
        int sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(new byte[]{(byte) i, (byte) 0}, (byte) -76, SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEIN);
        if (sendCmdToSensorHub != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
        }
    }

    public final void acquireAPWakeLock() {
        if (this.mContext == null) {
            CaLogger.error("mContext is null");
            return;
        }
        PowerManager powerManager = (PowerManager) this.mContext.getSystemService("power");
        if (powerManager == null) {
            CaLogger.error("pm is null");
        } else if (this.mAPWakeLock == null || !this.mAPWakeLock.isHeld()) {
            CaLogger.trace();
            this.mAPWakeLock = powerManager.newWakeLock(1, "CA_WAKELOCK");
            this.mAPWakeLock.acquire();
        } else {
            CaLogger.warning("WakeLock is already held.");
        }
    }

    public final void initializeManager(Context context) {
        if (context == null) {
            CaLogger.error("Context is null");
            return;
        }
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        IntentFilter intentFilter2 = new IntentFilter("android.intent.action.SCREEN_ON");
        IntentFilter intentFilter3 = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        IntentFilter intentFilter4 = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        this.mContext.registerReceiver(this.mReceiver, intentFilter2);
        this.mContext.registerReceiver(this.mReceiver, intentFilter3);
        this.mContext.registerReceiver(this.mReceiver, intentFilter4);
    }

    public final boolean isScreenOn() {
        boolean z = true;
        if (this.mContext == null) {
            CaLogger.error("mContext is null");
            return false;
        }
        int state = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getState();
        if (state == 1) {
            CaLogger.debug("Screen Off.");
        } else {
            CaLogger.debug("Screen On.");
        }
        if (state == 1) {
            z = false;
        }
        return z;
    }

    public final void notifyApPowerObserver(int i, long j) {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            IApPowerObserver iApPowerObserver = (IApPowerObserver) it.next();
            if (iApPowerObserver != null) {
                iApPowerObserver.initializePreparedSubCollection();
                iApPowerObserver.updateApPowerStatus(i, j);
            }
        }
    }

    public final void registerApPowerObserver(IApPowerObserver iApPowerObserver) {
        if (!this.mListeners.contains(iApPowerObserver)) {
            this.mListeners.add(iApPowerObserver);
        }
    }

    public final void releaseAPWakeLock() {
        if (this.mAPWakeLock == null || !this.mAPWakeLock.isHeld()) {
            CaLogger.warning("WakeLock is not held.");
            return;
        }
        CaLogger.trace();
        this.mAPWakeLock.release();
        this.mAPWakeLock = null;
    }

    public final void terminateManager() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
    }

    public final void unregisterApPowerObserver(IApPowerObserver iApPowerObserver) {
        if (this.mListeners.contains(iApPowerObserver)) {
            this.mListeners.remove(iApPowerObserver);
        }
    }
}
