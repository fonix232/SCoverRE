package com.sec.android.cover.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.CallLog.Calls;
import android.provider.Settings.Global;
import android.util.Log;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.Constants;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.ledcover.reflection.content.RefContentResolver;
import com.sec.android.cover.ledcover.reflection.content.RefUserInfo;
import com.sec.android.cover.ledcover.reflection.os.RefUserManager;
import com.sec.android.cover.monitor.CoverUpdateMonitorCallback;
import java.util.Arrays;
import java.util.List;

public class CallDisconnectReasonManager extends BaseCoverObservator {
    private static final int MSG_CALL_DISCONNECT_REASON = 0;
    private static final String TAG = CallDisconnectReasonManager.class.getSimpleName();
    private static Object mLock = new Object();
    private CallDisconnectReasonListener mCallDisconnectReasonListener;
    private CallsContentObserver mCallsContentObserver = new CallsContentObserver();
    private int[] mCallsCounts;
    private Handler mHandler = new DisconectReasonManagerHandler();
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00661();
    private int[] mUserIds;

    public interface CallDisconnectReasonListener {
        void onCallDisconnectReasonChanged(int i, String str, int i2);
    }

    class C00661 extends CoverUpdateMonitorCallback {
        C00661() {
        }

        public void onUserSwitched(int newUserId, int oldUserId) {
            CallDisconnectReasonManager.this.refreshTrackedUserId();
            CallDisconnectReasonManager.this.refreshWithCurrentValues();
        }
    }

    public static class CallDissconnectReason {
        private String number;
        private int type;
        private int userId;

        public String toString() {
            return "CallDissconnectReason [type=" + this.type + ", number=" + (this.number != null ? this.number.replaceAll("[0-9]", "*") : this.number) + ", userId=" + this.userId + "]";
        }
    }

    private class CallsContentObserver extends ContentObserver {
        public CallsContentObserver() {
            super(null);
        }

        public void onChange(boolean selfChange, Uri uri) {
            Log.d(CallDisconnectReasonManager.TAG, "CallsContentObserver.onChange uri=" + String.valueOf(uri));
            int[] oldCallCounts = Arrays.copyOf(CallDisconnectReasonManager.this.mCallsCounts, CallDisconnectReasonManager.this.mCallsCounts.length);
            int[] callUpdates = CallDisconnectReasonManager.this.queryCallsCount();
            for (int i = 0; i < CallDisconnectReasonManager.this.mCallsCounts.length; i++) {
                if (callUpdates[i] >= 0) {
                    CallDisconnectReasonManager.this.mCallsCounts[i] = callUpdates[i];
                    Log.d(CallDisconnectReasonManager.TAG, "CallsContentObserver.onChange userId=" + String.valueOf(CallDisconnectReasonManager.this.mUserIds[i]) + " oldCount=" + String.valueOf(oldCallCounts[i]) + " newCount=" + String.valueOf(CallDisconnectReasonManager.this.mCallsCounts[i]));
                    if (oldCallCounts[i] != CallDisconnectReasonManager.this.mCallsCounts[i]) {
                        CallDissconnectReason reason = CallDisconnectReasonManager.this.queryCallsDisconnectReasonForUser(CallDisconnectReasonManager.this.mUserIds[i]);
                        Log.d(CallDisconnectReasonManager.TAG, "CallsContentObserver.onChange reason=" + String.valueOf(reason));
                        Message msg = CallDisconnectReasonManager.this.mHandler.obtainMessage(0);
                        msg.arg1 = reason.type;
                        msg.obj = reason;
                        CallDisconnectReasonManager.this.mHandler.sendMessage(msg);
                    }
                }
            }
            super.onChange(selfChange, uri);
        }
    }

    private class DisconectReasonManagerHandler extends Handler {
        private DisconectReasonManagerHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    synchronized (CallDisconnectReasonManager.mLock) {
                        if (CallDisconnectReasonManager.this.mCallDisconnectReasonListener != null) {
                            CallDisconnectReasonManager.this.mCallDisconnectReasonListener.onCallDisconnectReasonChanged(msg.arg1, ((CallDissconnectReason) msg.obj).number, ((CallDissconnectReason) msg.obj).userId);
                        } else {
                            Log.w(CallDisconnectReasonManager.TAG, "No listener");
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public CallDisconnectReasonManager(Context context) {
        super(context);
        if (isTwoPhoneRegistered()) {
            this.mUserIds = new int[]{0, get2PhoneSecondUserId()};
        } else {
            this.mUserIds = new int[]{ActivityManager.semGetCurrentUser()};
        }
        this.mCallsCounts = new int[this.mUserIds.length];
    }

    private void refreshTrackedUserId() {
        if (!isTwoPhoneRegistered()) {
            this.mUserIds = new int[]{ActivityManager.semGetCurrentUser()};
        }
    }

    private int get2PhoneSecondUserId() {
        int id = 0;
        Object userInfo = null;
        try {
            UserManager um = (UserManager) getContext().getSystemService("user");
            if (um != null) {
                List<Object> uiList = RefUserManager.get().getUsers(um);
                if (uiList != null) {
                    for (Object info : uiList) {
                        if (RefUserInfo.get().isBMode(info)) {
                            userInfo = info;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while searching for 2Phone second user", e);
        }
        if (userInfo != null) {
            id = RefUserInfo.get().id(userInfo);
        }
        Log.d(TAG, "get2PhoneSecondUserId : id=" + String.valueOf(id));
        return id;
    }

    public void setListener(CallDisconnectReasonListener callDisconnectReasonListener) {
        synchronized (mLock) {
            this.mCallDisconnectReasonListener = callDisconnectReasonListener;
        }
    }

    private void refreshWithCurrentValues() {
        int[] callUpdates = queryCallsCount();
        synchronized (mLock) {
            if (this.mCallDisconnectReasonListener != null) {
                for (int i = 0; i < this.mUserIds.length; i++) {
                    this.mCallsCounts[i] = callUpdates[i];
                }
            }
        }
    }

    public void start() {
        refreshWithCurrentValues();
        RefContentResolver.get().registerContentObserver(getContext().getContentResolver(), Calls.CONTENT_URI, true, this.mCallsContentObserver, UserHandle.SEM_ALL.semGetIdentifier());
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().registerCallback(this.mUpdateMonitorCallback);
    }

    public void stop() {
        getContext().getContentResolver().unregisterContentObserver(this.mCallsContentObserver);
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
    }

    private int queryCallsCountForUser(int userId) {
        Log.d(TAG, "queryCallsCountForUser : userId=" + String.valueOf(userId));
        Cursor cursor = getContext().getContentResolver().query(getMissedCallUri(userId), new String[]{Constants.REMOTE_VIEW_INFO_TYPE, "new"}, "new = ?", new String[]{String.valueOf(1)}, "date DESC");
        if (cursor == null) {
            return -1;
        }
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public CallDissconnectReason queryCallsDisconnectReasonForUser(int userId) {
        Log.d(TAG, "queryCallsDisconnectReasonForUser : userId=" + String.valueOf(userId));
        CallDissconnectReason reason = new CallDissconnectReason();
        reason.type = -1;
        reason.number = null;
        reason.userId = userId;
        Cursor cursor = getContext().getContentResolver().query(getMissedCallUri(userId), new String[]{Constants.REMOTE_VIEW_INFO_TYPE, "number", "new"}, "new = ?", new String[]{String.valueOf(1)}, "date DESC");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                reason.type = cursor.getInt(0);
                reason.number = cursor.getString(1);
            }
            Log.d(TAG, "queryCallsDisconnectReasonForUser : callType=" + reason.type);
            cursor.close();
        }
        return reason;
    }

    private int[] queryCallsCount() {
        int[] callUpdates = new int[this.mUserIds.length];
        for (int i = 0; i < this.mUserIds.length; i++) {
            try {
                callUpdates[i] = queryCallsCountForUser(this.mUserIds[i]);
            } catch (Exception e) {
                Log.e(TAG, "Error while querying calls count for user " + String.valueOf(this.mUserIds[i]), e);
                callUpdates[i] = -1;
            }
        }
        Log.d(TAG, "queryMissedCallsCount : callUpdates=" + Arrays.toString(callUpdates));
        return callUpdates;
    }

    private static Uri getMissedCallUri(int userId) {
        Uri uri = Calls.CONTENT_URI;
        if (userId != 0) {
            return Uri.parse("content://" + userId + "@logs/call");
        }
        return uri;
    }

    private boolean isTwoPhoneRegistered() {
        if (Global.getInt(getContext().getContentResolver(), Constants.SETTINGS_TWOPHONE_REGISTERED, 0) == 1) {
            return true;
        }
        return false;
    }
}
