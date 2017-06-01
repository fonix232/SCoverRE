package com.sec.android.cover.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.Constants;

public class CoverMissedEventManager extends BaseCoverObservator {
    public static final int MISSED_TYPE_NOTIFICATION = 0;
    public static final int MISSED_TYPE_REMOTEVIEWS = 1;
    private static final String TAG = "MissedEventManager";
    private int mMissedEventCallCount = 0;
    private int mMissedEventMessageCount = 0;

    public class MissedEventItem {
        public final int mType;
        public final long mWhen;

        public MissedEventItem(int type, long when) {
            this.mType = type;
            this.mWhen = when;
        }
    }

    public class RemoteViewsItem extends MissedEventItem {
        public PendingIntent mContentIntent;
        public int mEventCount;
        public String mRemoteType;
        public final RemoteViews mRemoteViews;
        public final RemoteViews mSecureRemoteViews;

        public RemoteViewsItem(int type, long when, RemoteViews remoteViews, PendingIntent pendingIntent, int eventCount, String remoteType) {
            super(type, when);
            this.mRemoteViews = remoteViews;
            this.mSecureRemoteViews = remoteViews;
            this.mContentIntent = pendingIntent;
            this.mEventCount = eventCount;
            this.mRemoteType = remoteType;
        }

        public RemoteViewsItem(int type, long when, RemoteViews remoteViews, RemoteViews secureRemoteViews, PendingIntent pendingIntent, int eventCount, String remoteType) {
            super(type, when);
            this.mRemoteViews = remoteViews;
            this.mSecureRemoteViews = secureRemoteViews;
            this.mContentIntent = pendingIntent;
            this.mEventCount = eventCount;
            this.mRemoteType = remoteType;
        }

        public RemoteViews getRemoteViews() {
            return this.mRemoteViews;
        }

        public RemoteViews getSecureRemoteViews() {
            return this.mSecureRemoteViews;
        }

        public PendingIntent getContentIntent() {
            return this.mContentIntent;
        }

        public int getEventCount() {
            return this.mEventCount;
        }

        public String getRemoteType() {
            return this.mRemoteType;
        }
    }

    public CoverMissedEventManager(Context context) {
        super(context);
    }

    public void addMissedEvent(MissedEventItem data) {
        if (data.mType == 1 && (data instanceof RemoteViewsItem)) {
            if (((RemoteViewsItem) data).getRemoteType().equals(Constants.TYPE_MISSED_CALL)) {
                this.mMissedEventCallCount = ((RemoteViewsItem) data).getEventCount();
            } else if (((RemoteViewsItem) data).getRemoteType().equals(Constants.TYPE_MISSED_MESSAGE)) {
                this.mMissedEventMessageCount = ((RemoteViewsItem) data).getEventCount();
            }
        }
        Log.d(TAG, "mMissedEventCallCount:" + this.mMissedEventCallCount + ", mMissedEventMessageCount:" + this.mMissedEventMessageCount);
    }

    public void clearMissedEvent() {
        this.mMissedEventMessageCount = 0;
        this.mMissedEventCallCount = 0;
    }

    public int getMissedEventCount() {
        return this.mMissedEventMessageCount + this.mMissedEventCallCount;
    }

    public int getMissedEventMessageCount() {
        return this.mMissedEventMessageCount;
    }

    public int getMissedEventCallCount() {
        return this.mMissedEventCallCount;
    }

    public void start() {
    }

    public void stop() {
    }
}
