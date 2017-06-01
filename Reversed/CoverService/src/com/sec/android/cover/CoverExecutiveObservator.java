package com.sec.android.cover;

import android.content.Context;
import com.samsung.android.feature.SemFloatingFeature;
import com.sec.android.cover.ledcover.LedCoverService;
import com.sec.android.cover.manager.CallDisconnectReasonManager;
import com.sec.android.cover.manager.CoverMissedEventManager;
import com.sec.android.cover.manager.CoverPowerManager;
import com.sec.android.cover.manager.CoverRemoteViewManager;
import com.sec.android.cover.manager.MissedEventManager;
import com.sec.android.cover.monitor.CoverNotificationMonitor;
import com.sec.android.cover.monitor.CoverPlaybackStateMonitor;
import com.sec.android.cover.monitor.CoverUpdateMonitor;

public class CoverExecutiveObservator extends BaseCoverObservator {
    private static volatile CoverExecutiveObservator sInstance = null;
    private static Object sLock = new Object();
    private CallDisconnectReasonManager mCallDisconnectReasonManager;
    private CoverMissedEventManager mCoverMissedEventManager;
    private CoverNotificationMonitor mCoverNotificationMonitor;
    private CoverPlaybackStateMonitor mCoverPlaybackStateMonitor;
    private CoverPowerManager mCoverPowerManager;
    private CoverRemoteViewManager mCoverRemoteViewManager;
    private CoverUpdateMonitor mCoverUpdateMonitor;
    private int mFeatureLevel;
    private MissedEventManager mMissedEventManager;

    public static CoverExecutiveObservator getInstance(Context context) {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = new CoverExecutiveObservator(context);
                }
            }
        }
        return sInstance;
    }

    public CoverExecutiveObservator(Context context) {
        super(context);
        this.mFeatureLevel = 0;
        this.mFeatureLevel = SemFloatingFeature.getInstance().getInt(LedCoverService.NFC_LED_COVER_FEATURE_LEVEL);
    }

    public void start() {
        if (this.mCoverMissedEventManager == null) {
            this.mCoverMissedEventManager = new CoverMissedEventManager(getContext());
        }
        if (this.mCoverPowerManager == null) {
            this.mCoverPowerManager = new CoverPowerManager(getContext());
        }
        if (this.mCoverRemoteViewManager == null) {
            this.mCoverRemoteViewManager = new CoverRemoteViewManager(getContext());
        }
        if (this.mMissedEventManager == null && this.mFeatureLevel != 30) {
            this.mMissedEventManager = new MissedEventManager(getContext());
        }
        if (this.mCallDisconnectReasonManager == null && (this.mFeatureLevel == 30 || this.mFeatureLevel == 20)) {
            this.mCallDisconnectReasonManager = new CallDisconnectReasonManager(getContext());
        }
        if (this.mCoverPlaybackStateMonitor == null) {
            this.mCoverPlaybackStateMonitor = new CoverPlaybackStateMonitor(getContext());
        }
        if (this.mCoverUpdateMonitor == null) {
            this.mCoverUpdateMonitor = new CoverUpdateMonitor(getContext());
        }
        if (this.mCoverNotificationMonitor == null) {
            this.mCoverNotificationMonitor = new CoverNotificationMonitor(getContext());
        }
        this.mCoverUpdateMonitor.start();
        this.mCoverMissedEventManager.start();
        this.mCoverPowerManager.start();
        this.mCoverRemoteViewManager.start();
        if (this.mFeatureLevel != 30) {
            this.mMissedEventManager.start();
        } else {
            this.mCallDisconnectReasonManager.start();
        }
        this.mCoverPlaybackStateMonitor.start();
        this.mCoverNotificationMonitor.start();
    }

    public void stop() {
        this.mCoverMissedEventManager.stop();
        this.mCoverPowerManager.stop();
        this.mCoverRemoteViewManager.stop();
        if (this.mFeatureLevel != 30) {
            this.mMissedEventManager.stop();
        }
        if (this.mFeatureLevel == 30 || this.mFeatureLevel == 20) {
            this.mCallDisconnectReasonManager.stop();
        }
        this.mCoverPlaybackStateMonitor.stop();
        this.mCoverNotificationMonitor.stop();
        this.mCoverUpdateMonitor.stop();
        this.mCoverMissedEventManager = null;
        this.mCoverPowerManager = null;
        this.mCoverRemoteViewManager = null;
        this.mMissedEventManager = null;
        this.mCallDisconnectReasonManager = null;
        this.mCoverPlaybackStateMonitor = null;
        this.mCoverUpdateMonitor = null;
        this.mCoverNotificationMonitor = null;
    }

    public CoverMissedEventManager getCoverMissedEventManager() {
        if (this.mCoverMissedEventManager == null) {
            this.mCoverMissedEventManager = new CoverMissedEventManager(getContext());
        }
        return this.mCoverMissedEventManager;
    }

    public CoverPowerManager getCoverPowerManager() {
        if (this.mCoverPowerManager == null) {
            this.mCoverPowerManager = new CoverPowerManager(getContext());
        }
        return this.mCoverPowerManager;
    }

    public CoverRemoteViewManager getCoverRemoteViewManager() {
        if (this.mCoverRemoteViewManager == null) {
            this.mCoverRemoteViewManager = new CoverRemoteViewManager(getContext());
        }
        return this.mCoverRemoteViewManager;
    }

    public MissedEventManager getMissedEventManager() {
        if (this.mMissedEventManager == null) {
            this.mMissedEventManager = new MissedEventManager(getContext());
        }
        return this.mMissedEventManager;
    }

    public CallDisconnectReasonManager getCallDisconnectReasonManager() {
        if (this.mCallDisconnectReasonManager == null) {
            this.mCallDisconnectReasonManager = new CallDisconnectReasonManager(getContext());
        }
        return this.mCallDisconnectReasonManager;
    }

    public CoverPlaybackStateMonitor getCoverPlaybackStateMonitor() {
        if (this.mCoverPlaybackStateMonitor == null) {
            this.mCoverPlaybackStateMonitor = new CoverPlaybackStateMonitor(getContext());
        }
        return this.mCoverPlaybackStateMonitor;
    }

    public CoverUpdateMonitor getCoverUpdateMonitor() {
        if (this.mCoverUpdateMonitor == null) {
            this.mCoverUpdateMonitor = new CoverUpdateMonitor(getContext());
        }
        return this.mCoverUpdateMonitor;
    }

    public CoverNotificationMonitor getCoverNotificationMonitor() {
        if (this.mCoverNotificationMonitor == null) {
            this.mCoverNotificationMonitor = new CoverNotificationMonitor(getContext());
        }
        return this.mCoverNotificationMonitor;
    }
}
