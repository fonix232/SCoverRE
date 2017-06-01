package com.sec.android.cover.ledcover.fsm.dream.missedevent;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.sec.android.cover.ledcover.fsm.dream.DreamUtils;
import com.sec.android.cover.ledcover.fsm.dream.LedStateMachine;
import java.util.Arrays;

public class MissedEvent {
    public static final byte CMD_LED_DEFAULT_NOTIFICATION = (byte) 16;
    public static final byte CMD_MISSED_CALL_MESSAGE = (byte) 9;
    public static final byte CMD_PRESET_NOTIFICATION = (byte) 23;
    public static final byte CMD_USER_DEFINED_NOTIFICATION = (byte) 21;
    public static final String DEFAULT_CALL_APP_PKGNAME = "com.android.server.telecom";
    public static final String DEFAULT_TPHONE_CALL_APP_PKGNAME = "com.skt.prod.dialer";
    private static final String KEY_COUNT = "count";
    private static final String KEY_ICON_CALL = "icon_call";
    private static final String KEY_ICON_DATA = "icon_data";
    private static final String KEY_ICON_ID = "icon_id";
    private static final String KEY_ICON_MESSAGE = "icon_msg";
    private static final String KEY_PKG_NAME = "pkg_name";
    private static final int PREDEFINED_ID_NONE = -1;
    private static final String TAG = (LedStateMachine.class.getSimpleName() + "." + MissedEvent.class.getSimpleName());
    private byte mCommand = CMD_LED_DEFAULT_NOTIFICATION;
    private byte[] mData = new byte[]{(byte) 0};
    private final boolean mIconCall;
    private final boolean mIconMsg;
    private final boolean mIsCallApp;
    private final byte[] mLedPayloadData;
    private final String mPackageName;
    private final int mPredefinedIconId;
    private final long mPriority;

    private MissedEvent(String packageName, byte[] ledPayloadData, int predefinedIconId, boolean iconCall, boolean iconMsg, boolean isCallApp) {
        this.mPackageName = packageName;
        this.mLedPayloadData = ledPayloadData;
        this.mPredefinedIconId = predefinedIconId;
        this.mPriority = Long.MAX_VALUE - SystemClock.elapsedRealtime();
        this.mIconCall = iconCall;
        this.mIconMsg = iconMsg;
        this.mIsCallApp = isCallApp;
        initMissedEventLedData();
        Log.d(TAG, toString());
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
            throw new IllegalArgumentException("Custom notification data must contain a valid pkg_name array: " + Arrays.toString(packageName));
        }
        int[] predefinedId = data.getIntArray(KEY_ICON_ID);
        if (predefinedId == null || predefinedId.length < count) {
            throw new IllegalArgumentException("Custom notification data must contain a valid icon_id array: " + Arrays.toString(predefinedId));
        }
        String[] ledData = data.getStringArray(KEY_ICON_DATA);
        if (ledData == null || ledData.length < count) {
            throw new IllegalArgumentException("Custom notification data must contain a valid icon_data array: " + Arrays.toString(ledData));
        }
        boolean[] iconCall = data.getBooleanArray(KEY_ICON_CALL);
        if (iconCall == null || iconCall.length < count) {
            throw new IllegalArgumentException("Custom notification data must contain a valid icon_call array: " + Arrays.toString(iconCall));
        }
        boolean[] iconMsg = data.getBooleanArray(KEY_ICON_MESSAGE);
        if (iconMsg == null || iconMsg.length < count) {
            throw new IllegalArgumentException("Custom notification data must contain a valid icon_msg array: " + Arrays.toString(iconMsg));
        }
        MissedEvent[] returnNotifications = new MissedEvent[count];
        int i = 0;
        while (i < count) {
            if (!TextUtils.isEmpty(packageName[i])) {
                returnNotifications[i] = new MissedEvent(packageName[i], DreamUtils.getPayload(ledData[i]), predefinedId[i], iconCall != null ? iconCall[i] : false, iconMsg != null ? iconMsg[i] : false, isPredefinedDefaultCallApp(packageName[i]));
            }
            i++;
        }
        return returnNotifications;
    }

    String getPackageName() {
        return this.mPackageName;
    }

    public byte getEventLedCommand() {
        return this.mCommand;
    }

    public byte[] getEventLedData() {
        return this.mData;
    }

    private void initMissedEventLedData() {
        if (this.mIconCall) {
            this.mData = new byte[]{(byte) 1};
            this.mCommand = (byte) 9;
        } else if (this.mIconMsg) {
            this.mData = new byte[]{(byte) 2};
            this.mCommand = (byte) 9;
        } else {
            initCustomNotificationLedDataAndCommand();
        }
    }

    private void initCustomNotificationLedDataAndCommand() {
        if (this.mPredefinedIconId != -1 || this.mLedPayloadData != null) {
            if (this.mPredefinedIconId == -1 || this.mPredefinedIconId < 0) {
                this.mCommand = CMD_USER_DEFINED_NOTIFICATION;
                this.mData = this.mLedPayloadData;
                return;
            }
            this.mCommand = CMD_PRESET_NOTIFICATION;
            this.mData = new byte[]{(byte) this.mPredefinedIconId};
        }
    }

    long getPriority() {
        return this.mPriority;
    }

    public boolean isCallApp() {
        return this.mIsCallApp;
    }

    public static boolean isPredefinedDefaultCallApp(String pkgName) {
        if (DEFAULT_CALL_APP_PKGNAME.equals(pkgName) || DEFAULT_TPHONE_CALL_APP_PKGNAME.equals(pkgName)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.mPackageName == null ? 0 : this.mPackageName.hashCode()) + 31;
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
            return true;
        } else if (this.mPackageName.equals(other.mPackageName)) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return "MissedEvent [mPackageName=" + this.mPackageName + ", mLedPayloadData=" + this.mLedPayloadData + ", mPredefinedIconId=" + this.mPredefinedIconId + ", mPriority=" + this.mPriority + " mIconCall=" + this.mIconCall + " " + "mIconMsg=" + this.mIconMsg + " " + "mIsCallApp=" + this.mIsCallApp + "]";
    }
}
