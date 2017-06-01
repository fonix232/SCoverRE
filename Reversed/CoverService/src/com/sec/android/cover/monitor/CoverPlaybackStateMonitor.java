package com.sec.android.cover.monitor;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaController.Callback;
import android.media.session.MediaSession.QueueItem;
import android.media.session.MediaSessionManager;
import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.KeyEvent;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.ledcover.reflection.media.RefMediaSessionManager;
import com.sec.android.cover.monitor.CoverNotificationMonitor.CoverNotificationListener;
import java.util.Arrays;
import java.util.List;

public class CoverPlaybackStateMonitor extends BaseCoverObservator implements OnActiveSessionsChangedListener {
    private static final String[] AUTO_DISABLED_PACKAGES = new String[]{"com.sec.android.app.voicenote", "com.samsung.android.app.memo", "com.samsung.android.app.notes", "com.shazam.android"};
    private static final String[] AUTO_ENABLED_PACKAGES = new String[]{"com.sec.android.app.music", "com.samsung.android.app.music.chn"};
    private static final String GOOGLE_MUSIC_PACKAGE = "com.google.android.music";
    private static final String META_DATA_SUPPORT_MUSIC = "com.sec.android.cover.ledcover.SUPPORT_MUSIC";
    private static final String TAG = CoverPlaybackStateMonitor.class.getSimpleName();
    private static Object mLock = new Object();
    private MediaController mMediaController;
    private NotiUpdateListener mNotiUpdateListener;
    private OnPlaybackStateChangedListener mOnPlaybackStateChangedListener;
    private Callback mSessionCb = null;
    private MediaSessionManager mSessionManager;
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00711();

    public interface OnPlaybackStateChangedListener {
        void onMetadataChanged(MediaMetadata mediaMetadata);

        void onPlaybackStateChanged(PlaybackState playbackState);

        void onSessionDestroyed();
    }

    class C00711 extends CoverUpdateMonitorCallback {
        C00711() {
        }

        public void onUserSwitched(int newUserId, int oldUserId) {
            synchronized (CoverPlaybackStateMonitor.mLock) {
                CoverPlaybackStateMonitor.this.restart();
            }
        }
    }

    private class NotiUpdateListener implements CoverNotificationListener {
        private NotiUpdateListener() {
        }

        public void onNotificationsPosted(StatusBarNotification sbn) {
        }

        public void onNotificationsRemoved(StatusBarNotification sbn) {
            if (sbn != null && sbn.getPackageName() != null && CoverPlaybackStateMonitor.this.mMediaController != null && sbn.getPackageName().equals(CoverPlaybackStateMonitor.this.mMediaController.getPackageName())) {
                Log.d(CoverPlaybackStateMonitor.TAG, "onNotificationsRemoved sbn=" + String.valueOf(sbn) + " isCurrentlyPlaying=" + CoverPlaybackStateMonitor.this.isCurrentlyPlaying());
                if (!CoverPlaybackStateMonitor.this.isCurrentlyPlaying() && CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener != null) {
                    CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener.onSessionDestroyed();
                }
            }
        }

        public void onNotificationsRankUpdated() {
        }
    }

    private class SessionCallback extends Callback {
        private SessionCallback() {
        }

        public void onQueueChanged(List<QueueItem> queue) {
            if (CoverPlaybackStateMonitor.GOOGLE_MUSIC_PACKAGE.equals(CoverPlaybackStateMonitor.this.mMediaController.getPackageName())) {
                boolean emptyQueue = queue == null || queue.isEmpty();
                Log.d(CoverPlaybackStateMonitor.TAG, "onQueueChanged empty=" + emptyQueue);
                if (emptyQueue && CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener != null) {
                    CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener.onSessionDestroyed();
                }
            }
        }

        public void onPlaybackStateChanged(PlaybackState state) {
            Log.d(CoverPlaybackStateMonitor.TAG, "onPlaybackStateChanged state=" + String.valueOf(state));
            if (CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener != null) {
                CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener.onPlaybackStateChanged(state);
            }
        }

        public void onMetadataChanged(MediaMetadata metadata) {
            Log.d(CoverPlaybackStateMonitor.TAG, "onMetadataChanged metadata=" + String.valueOf(metadata));
            if (CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener != null) {
                CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener.onMetadataChanged(metadata);
            }
        }

        public void onSessionDestroyed() {
            Log.d(CoverPlaybackStateMonitor.TAG, "onSessionDestroyed");
            if (CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener != null) {
                CoverPlaybackStateMonitor.this.mOnPlaybackStateChangedListener.onSessionDestroyed();
            }
        }
    }

    public CoverPlaybackStateMonitor(Context context) {
        super(context);
        Arrays.sort(AUTO_ENABLED_PACKAGES);
        Arrays.sort(AUTO_DISABLED_PACKAGES);
        this.mSessionManager = (MediaSessionManager) getContext().getSystemService("media_session");
    }

    public void setOnPlaybackStateChangedListener(OnPlaybackStateChangedListener onPlaybackStateChangedListener) {
        this.mOnPlaybackStateChangedListener = onPlaybackStateChangedListener;
        if (this.mMediaController != null && this.mOnPlaybackStateChangedListener != null) {
            this.mOnPlaybackStateChangedListener.onPlaybackStateChanged(this.mMediaController.getPlaybackState());
            this.mOnPlaybackStateChangedListener.onMetadataChanged(this.mMediaController.getMetadata());
        }
    }

    public void onCoverStateChanged(ScoverState coverState) {
        if (coverState.getAttachState() && !coverState.getSwitchState()) {
            onActiveSessionsChanged(RefMediaSessionManager.get().getActiveSessionsForUser(this.mSessionManager, null, UserHandle.SEM_CURRENT.semGetIdentifier()));
        }
    }

    private boolean isRelevant(MediaController controller) {
        int playQueueNonEmpty = 0;
        String packageName = controller.getPackageName();
        if (AUTO_DISABLED_PACKAGES == null || AUTO_DISABLED_PACKAGES.length <= 0 || Arrays.binarySearch(AUTO_DISABLED_PACKAGES, packageName) < 0) {
            boolean result = false;
            if (AUTO_ENABLED_PACKAGES != null && AUTO_ENABLED_PACKAGES.length > 0 && Arrays.binarySearch(AUTO_ENABLED_PACKAGES, packageName) >= 0) {
                Log.d(TAG, "isRelevant: white list");
                result = true;
            }
            if (GOOGLE_MUSIC_PACKAGE.equals(packageName)) {
                boolean playQueueNonEmpty2;
                if (!(controller.getQueue() == null || controller.getQueue().isEmpty())) {
                    playQueueNonEmpty2 = true;
                }
                Log.d(TAG, "isRelevant packageName=" + String.valueOf(packageName) + " result=" + playQueueNonEmpty2 + "reason=WHITE_LIST_QUEUE");
                return playQueueNonEmpty2;
            }
            if (!result) {
                try {
                    ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(packageName, 128);
                    if (!(ai == null || ai.metaData == null)) {
                        result = ai.metaData.getBoolean(META_DATA_SUPPORT_MUSIC, false);
                    }
                    if (result) {
                        Log.d(TAG, "isRelevant: proper metadata");
                    }
                } catch (NameNotFoundException e) {
                    Log.e(TAG, String.valueOf(packageName) + " not found", e);
                }
            }
            if (!result) {
                StatusBarNotification[] notifications = CoverExecutiveObservator.getInstance(getContext()).getCoverNotificationMonitor().getActiveNotifications();
                if (notifications != null) {
                    int length = notifications.length;
                    while (playQueueNonEmpty < length) {
                        StatusBarNotification sb = notifications[playQueueNonEmpty];
                        if (sb.getPackageName().equals(packageName) && sb.isOngoing()) {
                            Log.d(TAG, "isRelevant: notification exists");
                            result = true;
                            break;
                        }
                        playQueueNonEmpty++;
                    }
                }
            }
            Log.d(TAG, "isRelevant packageName=" + String.valueOf(packageName) + " result=" + String.valueOf(result));
            return result;
        }
        Log.d(TAG, "isRelevant: black list");
        return false;
    }

    public void onActiveSessionsChanged(List<MediaController> controllers) {
        MediaController controller;
        StringBuilder sb = new StringBuilder();
        if (controllers != null) {
            sb.append("Size: ");
            sb.append(controllers.size());
            for (MediaController controller2 : controllers) {
                sb.append(" Package: ");
                sb.append(controller2.getPackageName());
                sb.append(" State: ");
                sb.append(controller2.getPlaybackState());
            }
        }
        Log.d(TAG, "onActiveSessionsChanged controllers: " + sb.toString());
        if (controllers != null && !controllers.isEmpty()) {
            long flags;
            PlaybackState playbackState;
            for (MediaController controller22 : controllers) {
                if (!(controller22 == null || !isRelevant(controller22) || controller22.getPlaybackState() == null)) {
                    flags = controller22.getFlags();
                    playbackState = controller22.getPlaybackState();
                    if (!(playbackState == null || playbackState.getState() != 3 || (flags & 2) == 0)) {
                        updateController(controller22);
                        return;
                    }
                }
            }
            for (MediaController controller222 : controllers) {
                if (controller222 != null && isRelevant(controller222)) {
                    playbackState = controller222.getPlaybackState();
                    if (playbackState != null) {
                        flags = controller222.getFlags();
                        if (playbackState.getState() == 6 && (flags & 2) != 0) {
                            updateController(controller222);
                            return;
                        }
                    }
                    continue;
                }
            }
            if (this.mMediaController != null && ((MediaController) controllers.get(0)).getSessionToken().equals(this.mMediaController.getSessionToken())) {
                controller222 = (MediaController) controllers.get(0);
                if ((controller222.getFlags() & 2) != 0) {
                    Log.i(TAG, "onActiveSessionsChanged().Top of List<MediaController> is same as before : " + controller222.getPackageName());
                    return;
                }
            }
            updateController(null);
        }
    }

    public void start() {
        Log.d(TAG, "Start");
        Handler handler = null;
        if (Looper.myLooper() == null) {
            handler = new Handler();
        }
        RefMediaSessionManager.get().addOnActiveSessionsChangedListener(this.mSessionManager, this, null, UserHandle.SEM_CURRENT.semGetIdentifier(), handler);
        onActiveSessionsChanged(RefMediaSessionManager.get().getActiveSessionsForUser(this.mSessionManager, null, UserHandle.SEM_CURRENT.semGetIdentifier()));
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().registerCallback(this.mUpdateMonitorCallback);
        this.mNotiUpdateListener = new NotiUpdateListener();
        CoverExecutiveObservator.getInstance(getContext()).getCoverNotificationMonitor().setCoverNotificationListener(this.mNotiUpdateListener);
    }

    public void stop() {
        Log.d(TAG, "Stop");
        if (this.mOnPlaybackStateChangedListener != null) {
            this.mOnPlaybackStateChangedListener.onSessionDestroyed();
        }
        if (!(this.mMediaController == null || this.mSessionCb == null)) {
            this.mMediaController.unregisterCallback(this.mSessionCb);
            this.mSessionCb = null;
        }
        this.mSessionManager.removeOnActiveSessionsChangedListener(this);
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
        this.mNotiUpdateListener = null;
        CoverExecutiveObservator.getInstance(getContext()).getCoverNotificationMonitor().setCoverNotificationListener(null);
    }

    private void updateController(MediaController controller) {
        Log.d(TAG, "updateController: controller=" + (controller != null ? controller.getPackageName() : "null"));
        synchronized (this) {
            if (controller == null) {
                if (this.mMediaController != null) {
                    if (this.mSessionCb != null) {
                        this.mMediaController.unregisterCallback(this.mSessionCb);
                        this.mSessionCb = null;
                    }
                    this.mMediaController = null;
                    if (this.mOnPlaybackStateChangedListener != null) {
                        this.mOnPlaybackStateChangedListener.onSessionDestroyed();
                    }
                }
            } else if (this.mMediaController == null || !controller.getSessionToken().equals(this.mMediaController.getSessionToken())) {
                if (!(this.mMediaController == null || this.mSessionCb == null)) {
                    this.mMediaController.unregisterCallback(this.mSessionCb);
                    this.mSessionCb = null;
                }
                this.mSessionCb = new SessionCallback();
                this.mMediaController = controller;
                this.mMediaController.registerCallback(this.mSessionCb);
                if (this.mOnPlaybackStateChangedListener != null) {
                    this.mOnPlaybackStateChangedListener.onPlaybackStateChanged(this.mMediaController.getPlaybackState());
                    this.mOnPlaybackStateChangedListener.onMetadataChanged(this.mMediaController.getMetadata());
                }
            }
        }
    }

    private boolean isCurrentlyPlaying() {
        boolean z = false;
        synchronized (this) {
            if (this.mMediaController == null) {
            } else {
                PlaybackState state = this.mMediaController.getPlaybackState();
                if (state == null) {
                } else {
                    boolean playQueueNonEmpty;
                    if (GOOGLE_MUSIC_PACKAGE.equals(this.mMediaController.getPackageName()) && (this.mMediaController.getQueue() == null || this.mMediaController.getQueue().isEmpty())) {
                        playQueueNonEmpty = false;
                    } else {
                        playQueueNonEmpty = true;
                    }
                    if ((state.getState() == 3 || state.getState() == 6) && playQueueNonEmpty) {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    public boolean sendMediaButtonEvent(KeyEvent keyEvent) {
        boolean z = false;
        Log.d(TAG, "sendMediaButtonEvent: " + keyEvent);
        synchronized (this) {
            if (keyEvent == null) {
                Log.e(TAG, "sendMediaButtonEvent: KeyEvent is null");
            } else if (this.mMediaController == null) {
                Log.e(TAG, "sendMediaButtonEvent: No current media session");
            } else {
                z = this.mMediaController.dispatchMediaButtonEvent(keyEvent);
            }
        }
        return z;
    }
}
