package com.sec.android.cover.ledcover.fsm.grace.missedevent;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.sec.android.cover.ledcover.GraceLEDCoverCMD;
import com.sec.android.cover.ledcover.fsm.grace.LedStateMachine;
import java.util.List;

public class MissedEvent {
    private static final int DEFAULT_EFFECT = 0;
    private static final String KEY_COUNT = "count";
    private static final String KEY_EFFECT_DATA = "Pre-definedEffect";
    private static final String KEY_ICON_DATA = "icon_data";
    private static final String KEY_ICON_ID = "icon_id";
    private static final String KEY_PKG_NAME = "pkg_name";
    private static final int PREDEFINED_ID_NONE = -1;
    private static final String TAG = (LedStateMachine.class.getSimpleName() + "." + MissedEvent.class.getSimpleName());
    private final int mCount;
    private final int mEffect;
    private final String mLedPayloadData;
    private final String mPackageName;
    private final int mPredefinedIconId;
    private final long mPriority;
    private final EventType mType;

    public enum EventType {
        CALL,
        MESSAGE,
        CUSTOM_NOTIFICATION
    }

    private MissedEvent(EventType type, String packageName, String ledPayloadData, int predefinedIconId, int effect, int count) {
        this.mType = type;
        this.mPackageName = packageName;
        if (ledPayloadData == null) {
            ledPayloadData = "";
        }
        this.mLedPayloadData = ledPayloadData;
        this.mPredefinedIconId = predefinedIconId;
        this.mPriority = Long.MAX_VALUE - SystemClock.elapsedRealtime();
        this.mEffect = effect;
        this.mCount = count;
        Log.d(TAG, "Missed event: type: " + this.mType + " package:" + this.mPackageName + " data: " + this.mLedPayloadData + " predefinedId: " + this.mPredefinedIconId + " priority: " + this.mPriority + " effect: " + this.mEffect + " count:" + this.mCount);
    }

    public static MissedEvent getMissedMessageEvent(int count) {
        return new MissedEvent(EventType.MESSAGE, "", null, 0, 0, count);
    }

    public static MissedEvent getMissedCallEvent(int count) {
        return new MissedEvent(EventType.CALL, "", null, 0, 0, count);
    }

    public static MissedEvent[] getCustomNotificationEvents(Bundle data) {
        if (data == null) {
            throw new IllegalArgumentException("Custom notification data cannot be null");
        }
        int count = data.getInt("count", 0);
        if (count < 1) {
            throw new IllegalArgumentException("Notification count must be at least 1: " + count);
        }
        String[] packageName = data.getStringArray(KEY_PKG_NAME);
        if (packageName == null || packageName.length < count) {
            throw new IllegalArgumentException("Custom notification data must contain a valid pkg_name array: " + packageName);
        }
        int[] predefinedId = data.getIntArray(KEY_ICON_ID);
        if (predefinedId == null || predefinedId.length < count) {
            throw new IllegalArgumentException("Custom notification data must contain a valid icon_id array: " + predefinedId);
        }
        String[] ledData = data.getStringArray(KEY_ICON_DATA);
        if (ledData == null || ledData.length < count) {
            throw new IllegalArgumentException("Custom notification data must contain a valid icon_data array: " + ledData);
        }
        int i;
        int[] effectData = data.getIntArray(KEY_EFFECT_DATA);
        if (effectData == null || effectData.length < count) {
            Log.e(TAG, "Invalid Pre-definedEffect data");
            effectData = new int[count];
            for (i = 0; i < count; i++) {
                effectData[i] = 0;
            }
        }
        MissedEvent[] returnNotifications = new MissedEvent[count];
        for (i = 0; i < count; i++) {
            returnNotifications[i] = new MissedEvent(EventType.CUSTOM_NOTIFICATION, packageName[i], ledData[i], predefinedId[i], effectData[i], 0);
        }
        return returnNotifications;
    }

    String getPackageName() {
        return this.mPackageName;
    }

    public List<String> getNewEventLedData(GraceLEDCoverCMD graceLedCoverCmd) {
        switch (this.mType) {
            case MESSAGE:
                return graceLedCoverCmd.getNewMessageData();
            default:
                return getMissedEventLedData(graceLedCoverCmd);
        }
    }

    public List<String> getMissedEventLedData(GraceLEDCoverCMD graceLedCoverCmd) {
        switch (this.mType) {
            case MESSAGE:
                return graceLedCoverCmd.getMissedEventData(0, this.mCount);
            case CALL:
                return graceLedCoverCmd.getMissedEventData(this.mCount, 0);
            case CUSTOM_NOTIFICATION:
                return getCustomNotificationLedData(graceLedCoverCmd);
            default:
                return null;
        }
    }

    private List<String> getCustomNotificationLedData(GraceLEDCoverCMD graceLedCoverCmd) {
        if (this.mPredefinedIconId == -1 && TextUtils.isEmpty(this.mLedPayloadData)) {
            return null;
        }
        if (this.mPredefinedIconId == -1 || this.mPredefinedIconId < 0) {
            return graceLedCoverCmd.getUserIconNotificationData(this.mLedPayloadData, this.mEffect);
        }
        return graceLedCoverCmd.getNotificationPresetIdData(this.mPredefinedIconId);
    }

    long getPriority() {
        return this.mPriority;
    }

    public EventType getType() {
        return this.mType;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.mPackageName == null ? 0 : this.mPackageName.hashCode()) + 31) * 31;
        if (this.mType != null) {
            i = this.mType.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MissedEvent other = (MissedEvent) obj;
        if (this.mPackageName == null) {
            if (other.mPackageName != null) {
                return false;
            }
        } else if (!this.mPackageName.equals(other.mPackageName)) {
            return false;
        }
        if (this.mType != other.mType) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "MissedEvent [mType=" + this.mType + ", mPackageName=" + this.mPackageName + ", mLedPayloadData=" + this.mLedPayloadData + ", mPredefinedIconId=" + this.mPredefinedIconId + ", mPriority=" + this.mPriority + ", mEffect=" + this.mEffect + ", mCount=" + this.mCount + "]";
    }
}
