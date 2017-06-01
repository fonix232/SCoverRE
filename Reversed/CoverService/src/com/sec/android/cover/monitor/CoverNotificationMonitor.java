package com.sec.android.cover.monitor;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.NotificationListenerService.RankingMap;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.ledcover.reflection.service.RefNotificationListenerService;

public class CoverNotificationMonitor extends BaseCoverObservator {
    private static final int MSG_NOTIFICATIONS_POSTED = 300;
    private static final int MSG_NOTIFICATIONS_REMOVED = 301;
    private static final int MSG_NOTIFICATIONS_UPDATE = 302;
    private static final String TAG = CoverNotificationMonitor.class.getSimpleName();
    private static final Object mLock = new Object();
    private Handler mHandler = new C00691();
    private CoverNotificationListener mListener;
    private final NotificationListenerService mNotificationListener = new C00702();

    class C00691 extends Handler {
        C00691() {
        }

        public void handleMessage(Message msg) {
            synchronized (CoverNotificationMonitor.mLock) {
                switch (msg.what) {
                    case CoverNotificationMonitor.MSG_NOTIFICATIONS_POSTED /*300*/:
                        if (CoverNotificationMonitor.this.mListener != null) {
                            CoverNotificationMonitor.this.mListener.onNotificationsPosted((StatusBarNotification) msg.obj);
                            break;
                        }
                        break;
                    case CoverNotificationMonitor.MSG_NOTIFICATIONS_REMOVED /*301*/:
                        if (CoverNotificationMonitor.this.mListener != null) {
                            CoverNotificationMonitor.this.mListener.onNotificationsRemoved((StatusBarNotification) msg.obj);
                            break;
                        }
                        break;
                    case CoverNotificationMonitor.MSG_NOTIFICATIONS_UPDATE /*302*/:
                        if (CoverNotificationMonitor.this.mListener != null) {
                            CoverNotificationMonitor.this.mListener.onNotificationsRankUpdated();
                            break;
                        }
                        break;
                }
            }
        }
    }

    class C00702 extends NotificationListenerService {
        C00702() {
        }

        public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
            Log.d(CoverNotificationMonitor.TAG, "onNotificationPosted");
            CoverNotificationMonitor.this.mHandler.removeMessages(CoverNotificationMonitor.MSG_NOTIFICATIONS_POSTED);
            Message msg = CoverNotificationMonitor.this.mHandler.obtainMessage(CoverNotificationMonitor.MSG_NOTIFICATIONS_POSTED);
            msg.obj = sbn;
            msg.sendToTarget();
        }

        public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
            Log.d(CoverNotificationMonitor.TAG, "onNotificationRemoved");
            CoverNotificationMonitor.this.mHandler.removeMessages(CoverNotificationMonitor.MSG_NOTIFICATIONS_REMOVED);
            Message msg = CoverNotificationMonitor.this.mHandler.obtainMessage(CoverNotificationMonitor.MSG_NOTIFICATIONS_REMOVED);
            msg.obj = sbn;
            msg.sendToTarget();
        }

        public void onNotificationRankingUpdate(RankingMap rankingMap) {
            Log.d(CoverNotificationMonitor.TAG, "onNotificationRankingUpdate");
            CoverNotificationMonitor.this.mHandler.removeMessages(CoverNotificationMonitor.MSG_NOTIFICATIONS_UPDATE);
            CoverNotificationMonitor.this.mHandler.sendEmptyMessage(CoverNotificationMonitor.MSG_NOTIFICATIONS_UPDATE);
        }
    }

    public interface CoverNotificationListener {
        void onNotificationsPosted(StatusBarNotification statusBarNotification);

        void onNotificationsRankUpdated();

        void onNotificationsRemoved(StatusBarNotification statusBarNotification);
    }

    public CoverNotificationMonitor(Context context) {
        super(context);
    }

    public void setCoverNotificationListener(CoverNotificationListener listener) {
        synchronized (mLock) {
            this.mListener = listener;
        }
    }

    public void start() {
        Log.d(TAG, "Start");
        synchronized (mLock) {
            RefNotificationListenerService.get().registerAsSystemService(this.mNotificationListener, getContext(), new ComponentName(getContext().getPackageName(), getClass().getCanonicalName()), UserHandle.SEM_ALL.semGetIdentifier());
        }
    }

    public void stop() {
        Log.d(TAG, "Stop");
        synchronized (mLock) {
            RefNotificationListenerService.get().unregisterAsSystemService(this.mNotificationListener);
        }
    }

    public StatusBarNotification[] getActiveNotifications() {
        StatusBarNotification[] statusBarNotificationArr = null;
        synchronized (mLock) {
            try {
                statusBarNotificationArr = this.mNotificationListener.getActiveNotifications();
            } catch (SecurityException e) {
                Log.e(TAG, "Service unbound, please call start before gettig notifications", e);
            } catch (Exception e2) {
                Log.e(TAG, "Error getting active notifications", e2);
            }
        }
        return statusBarNotificationArr;
    }
}
