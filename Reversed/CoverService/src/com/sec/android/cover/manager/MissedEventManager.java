package com.sec.android.cover.manager;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.CallLog.Calls;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.Sms.Inbox;
import android.util.Log;
import com.samsung.android.feature.SemFloatingFeature;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.Constants;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.ledcover.reflection.content.RefContentResolver;
import com.sec.android.cover.ledcover.reflection.content.RefUserInfo;
import com.sec.android.cover.ledcover.reflection.os.RefUserManager;
import com.sec.android.cover.ledcover.reflection.provider.RefSecure;
import com.sec.android.cover.monitor.CoverUpdateMonitorCallback;
import java.util.Arrays;
import java.util.List;

public class MissedEventManager extends BaseCoverObservator {
    private static final int MESSAGE_TYPE_NOTIFICATION_IND = 130;
    private static final int MESSAGE_TYPE_RETRIEVE_CONF = 132;
    private static final Uri MSG_PREFERENCE = Uri.parse("content://com.android.mms.csc.PreferenceProvider/key");
    private static final int MSG_UPDATE_CALL = 1;
    private static final int MSG_UPDATE_MESSAGE = 2;
    private static final int MSG_UPDATE_MESSAGE_MUTED = 3;
    private static final String PACKAGE_SAMSUNG_MESSAGES = SemFloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_MESSAGE_CONFIG_PACKAGE_NAME", "com.android.mms");
    private static final String TAG = MissedEventManager.class.getSimpleName();
    private static Object mLock = new Object();
    private CallsContentObserver mCallsContentObserver = new CallsContentObserver();
    private Handler mHandler = new MissedEventManagerHandler();
    private int[] mMissedCallsCounts;
    private MissedEventListener mMissedEventListener;
    private MessagesContentObserver mMmsSmsMessagesContentObserver = new MessagesContentObserver();
    private MessagesContentObserver mRcsChatMessagesContentObserver = new MessagesContentObserver();
    private MessagesContentObserver mRcsFtMessagesContentObserver = new MessagesContentObserver();
    private int[] mUnreadMessagesCounts;
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00681();
    private int[] mUserIds;

    public interface MissedEventListener {
        void onMissedCallsCountChanged(int i, int i2, boolean z, int i3);

        void onUnreadMessagesCountChanged(int i, int i2, boolean z, int i3);
    }

    class C00681 extends CoverUpdateMonitorCallback {
        C00681() {
        }

        public void onUserSwitched(int newUserId, int oldUserId) {
            MissedEventManager.this.refreshTrackedUserId();
            MissedEventManager.this.refreshWithCurrentValues();
        }
    }

    private class CallsContentObserver extends ContentObserver {
        public CallsContentObserver() {
            super(null);
        }

        public void onChange(boolean selfChange, Uri uri) {
            Log.d(MissedEventManager.TAG, "CallsContentObserver.onChange uri=" + String.valueOf(uri));
            int[] oldMissedCallCounts = Arrays.copyOf(MissedEventManager.this.mMissedCallsCounts, MissedEventManager.this.mMissedCallsCounts.length);
            MissedCallUpdate[] callUpdates = MissedEventManager.this.queryMissedCallsCount();
            for (int i = 0; i < MissedEventManager.this.mMissedCallsCounts.length; i++) {
                if (callUpdates[i] != null) {
                    MissedEventManager.this.mMissedCallsCounts[i] = callUpdates[i].count;
                    Log.d(MissedEventManager.TAG, "CallsContentObserver.onChange userId=" + String.valueOf(MissedEventManager.this.mUserIds[i]) + " oldCount=" + String.valueOf(oldMissedCallCounts[i]) + " newCount=" + String.valueOf(MissedEventManager.this.mMissedCallsCounts[i]) + " notify=" + String.valueOf(callUpdates[i].notify));
                    if (oldMissedCallCounts[i] != MissedEventManager.this.mMissedCallsCounts[i]) {
                        Message msg = MissedEventManager.this.mHandler.obtainMessage(1);
                        msg.arg1 = oldMissedCallCounts[i];
                        msg.arg2 = MissedEventManager.this.mMissedCallsCounts[i];
                        msg.obj = callUpdates[i];
                        MissedEventManager.this.mHandler.sendMessage(msg);
                    }
                }
            }
            super.onChange(selfChange, uri);
        }
    }

    private class MessagesContentObserver extends ContentObserver {
        public MessagesContentObserver() {
            super(null);
        }

        public void onChange(boolean selfChange, Uri uri) {
            Log.d(MissedEventManager.TAG, "MessagesContentObserver.onChange uri=" + String.valueOf(uri));
            int[] oldUnreadMsgCounts = Arrays.copyOf(MissedEventManager.this.mUnreadMessagesCounts, MissedEventManager.this.mUnreadMessagesCounts.length);
            UnreadMessageUpdate[] msgUpdates = MissedEventManager.this.queryUnreadMessagesCount();
            for (int i = 0; i < MissedEventManager.this.mUnreadMessagesCounts.length; i++) {
                if (msgUpdates[i] != null) {
                    MissedEventManager.this.mUnreadMessagesCounts[i] = msgUpdates[i].count;
                    Log.d(MissedEventManager.TAG, "MessagesContentObserver.onChange userId=" + String.valueOf(MissedEventManager.this.mUserIds[i]) + " oldCount=" + String.valueOf(oldUnreadMsgCounts[i]) + " newCount=" + String.valueOf(MissedEventManager.this.mUnreadMessagesCounts[i]) + " notify=" + String.valueOf(msgUpdates[i].notify));
                    if (oldUnreadMsgCounts[i] != MissedEventManager.this.mUnreadMessagesCounts[i]) {
                        Message msg = MissedEventManager.this.mHandler.obtainMessage(msgUpdates[i].notify ? 2 : 3);
                        msg.arg1 = oldUnreadMsgCounts[i];
                        msg.arg2 = MissedEventManager.this.mUnreadMessagesCounts[i];
                        msg.obj = msgUpdates[i];
                        MissedEventManager.this.mHandler.sendMessage(msg);
                    }
                }
            }
            super.onChange(selfChange, uri);
        }
    }

    private static class MissedCallUpdate {
        private int count;
        private boolean notify;
        private int userId;

        private MissedCallUpdate() {
        }

        public String toString() {
            return "MissedCallUpdate [count=" + this.count + ", notify=" + this.notify + ", userId=" + this.userId + "]";
        }
    }

    private class MissedEventManagerHandler extends Handler {
        private MissedEventManagerHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    synchronized (MissedEventManager.mLock) {
                        if (MissedEventManager.this.mMissedEventListener != null) {
                            MissedEventManager.this.mMissedEventListener.onMissedCallsCountChanged(msg.arg1, msg.arg2, false, ((MissedCallUpdate) msg.obj).userId);
                        } else {
                            Log.w(MissedEventManager.TAG, "No listener");
                        }
                    }
                    return;
                case 2:
                    synchronized (MissedEventManager.mLock) {
                        if (MissedEventManager.this.mMissedEventListener != null) {
                            MissedEventManager.this.mMissedEventListener.onUnreadMessagesCountChanged(msg.arg1, msg.arg2, false, ((UnreadMessageUpdate) msg.obj).userId);
                        } else {
                            Log.w(MissedEventManager.TAG, "No listener");
                        }
                    }
                    return;
                case 3:
                    synchronized (MissedEventManager.mLock) {
                        if (MissedEventManager.this.mMissedEventListener != null) {
                            MissedEventManager.this.mMissedEventListener.onUnreadMessagesCountChanged(msg.arg1, msg.arg2, true, ((UnreadMessageUpdate) msg.obj).userId);
                        } else {
                            Log.w(MissedEventManager.TAG, "No listener");
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private static class UnreadMessageUpdate {
        private int count;
        private boolean notify;
        private int userId;

        private UnreadMessageUpdate() {
        }

        public String toString() {
            return "UnreadMessageUpdate [count=" + this.count + ", notify=" + this.notify + ", userId=" + this.userId + "]";
        }
    }

    public MissedEventManager(Context context) {
        super(context);
        if (isTwoPhoneRegistered()) {
            this.mUserIds = new int[]{0, get2PhoneSecondUserId()};
        } else {
            this.mUserIds = new int[]{ActivityManager.semGetCurrentUser()};
        }
        this.mUnreadMessagesCounts = new int[this.mUserIds.length];
        this.mMissedCallsCounts = new int[this.mUserIds.length];
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

    public void setListener(MissedEventListener missedEventListener) {
        synchronized (mLock) {
            this.mMissedEventListener = missedEventListener;
        }
    }

    private void refreshWithCurrentValues() {
        int[] oldUnreadMsgCounts = Arrays.copyOf(this.mUnreadMessagesCounts, this.mUnreadMessagesCounts.length);
        UnreadMessageUpdate[] msgUpdates = queryUnreadMessagesCount();
        int[] oldMissedCallCounts = Arrays.copyOf(this.mMissedCallsCounts, this.mMissedCallsCounts.length);
        MissedCallUpdate[] callUpdates = queryMissedCallsCount();
        synchronized (mLock) {
            if (this.mMissedEventListener != null) {
                for (int i = 0; i < this.mUserIds.length; i++) {
                    this.mUnreadMessagesCounts[i] = msgUpdates[i].count;
                    this.mMissedCallsCounts[i] = callUpdates[i].count;
                    this.mMissedEventListener.onUnreadMessagesCountChanged(oldUnreadMsgCounts[i], this.mUnreadMessagesCounts[i], true, msgUpdates[i].userId);
                    this.mMissedEventListener.onMissedCallsCountChanged(oldMissedCallCounts[i], this.mMissedCallsCounts[i], true, callUpdates[i].userId);
                }
            }
        }
    }

    public void start() {
        refreshWithCurrentValues();
        ContentResolver contentResolver = getContext().getContentResolver();
        RefContentResolver.get().registerContentObserver(contentResolver, MmsSms.CONTENT_URI, false, this.mMmsSmsMessagesContentObserver, UserHandle.SEM_ALL.semGetIdentifier());
        RefContentResolver.get().registerContentObserver(contentResolver, Calls.CONTENT_URI, true, this.mCallsContentObserver, UserHandle.SEM_ALL.semGetIdentifier());
        RefContentResolver.get().registerContentObserver(contentResolver, Uri.parse("content://im/chat"), true, this.mRcsChatMessagesContentObserver, UserHandle.SEM_ALL.semGetIdentifier());
        RefContentResolver.get().registerContentObserver(contentResolver, Uri.parse("content://im/ft"), true, this.mRcsFtMessagesContentObserver, UserHandle.SEM_ALL.semGetIdentifier());
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().registerCallback(this.mUpdateMonitorCallback);
    }

    public void stop() {
        getContext().getContentResolver().unregisterContentObserver(this.mMmsSmsMessagesContentObserver);
        getContext().getContentResolver().unregisterContentObserver(this.mCallsContentObserver);
        getContext().getContentResolver().unregisterContentObserver(this.mRcsChatMessagesContentObserver);
        getContext().getContentResolver().unregisterContentObserver(this.mRcsFtMessagesContentObserver);
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
    }

    public int[] getUnreadMessagesCounts() {
        return this.mUnreadMessagesCounts;
    }

    public int[] getMissedCallsCounts() {
        return this.mMissedCallsCounts;
    }

    public int getUnreadMessagesCountForUser(int userId) {
        int i = Arrays.binarySearch(this.mUserIds, userId);
        if (i >= 0) {
            return this.mUnreadMessagesCounts[i];
        }
        return 0;
    }

    public int getMissedCallsCountForUser(int userId) {
        int i = Arrays.binarySearch(this.mUserIds, userId);
        if (i >= 0) {
            return this.mMissedCallsCounts[i];
        }
        return 0;
    }

    private UnreadMessageUpdate queryUnreadMessagesCountForUser(int userId) {
        StringBuilder where;
        String[] whereValues;
        Cursor cursor;
        Log.d(TAG, "queryUnreadMessagesCountForUser : userId=" + String.valueOf(userId));
        long mmsSmsUpdateDate = 0;
        long imMsgUpdateDate = 0;
        long latest_thread_id = -1;
        UnreadMessageUpdate unreadMessageUpdate = new UnreadMessageUpdate();
        unreadMessageUpdate.count = 0;
        unreadMessageUpdate.notify = true;
        unreadMessageUpdate.userId = userId;
        try {
            where = new StringBuilder();
            where.append("read");
            where.append(" = ?");
            whereValues = new String[]{String.valueOf(0)};
            if (isTwoPhoneRegistered()) {
                where.append(" AND (using_mode = ?)");
                whereValues = new String[]{whereValues[0], String.valueOf(userId)};
            }
            Log.d(TAG, "queryUnreadMessagesCountForUser : whereSms=" + String.valueOf(where));
            cursor = getContext().getContentResolver().query(Inbox.CONTENT_URI, new String[]{"date", "thread_id"}, where.toString(), whereValues, "date DESC");
            if (cursor != null) {
                Log.d(TAG, "queryUnreadMessagesCountForUser : countSms=" + String.valueOf(cursor.getCount()));
                unreadMessageUpdate.count = unreadMessageUpdate.count + cursor.getCount();
                if (cursor.moveToFirst()) {
                    mmsSmsUpdateDate = cursor.getLong(0);
                    latest_thread_id = cursor.getLong(1);
                }
                cursor.close();
            } else {
                Log.e(TAG, "queryUnreadMessagesCountForUser : SMS cursor null");
            }
        } catch (Exception e) {
            Log.e(TAG, "queryUnreadMessagesCountForUser : error querying SMS count", e);
        }
        try {
            where = new StringBuilder();
            where.append("read");
            where.append(" = ?");
            where.append(" AND (");
            where.append("m_type");
            where.append(" = ?");
            where.append(" OR ");
            where.append("m_type");
            where.append(" = ?)");
            whereValues = new String[]{String.valueOf(0), String.valueOf(MESSAGE_TYPE_NOTIFICATION_IND), String.valueOf(MESSAGE_TYPE_RETRIEVE_CONF)};
            if (isTwoPhoneRegistered()) {
                where.append(" AND (using_mode = ?)");
                whereValues = new String[]{whereValues[0], whereValues[1], whereValues[2], String.valueOf(userId)};
            }
            Log.d(TAG, "queryUnreadMessagesCountForUser : whereMms=" + String.valueOf(where));
            cursor = getContext().getContentResolver().query(Mms.Inbox.CONTENT_URI, new String[]{"date", "thread_id"}, where.toString(), whereValues, "date DESC");
            if (cursor != null) {
                Log.d(TAG, "queryUnreadMessagesCountForUser : countMms=" + String.valueOf(cursor.getCount()));
                unreadMessageUpdate.count = unreadMessageUpdate.count + cursor.getCount();
                if (cursor.moveToFirst()) {
                    long mmsDate = cursor.getLong(0) * 1000;
                    if (mmsDate > mmsSmsUpdateDate) {
                        mmsSmsUpdateDate = mmsDate;
                        latest_thread_id = cursor.getLong(1);
                    }
                }
                cursor.close();
            } else {
                Log.e(TAG, "queryUnreadMessagesCountForUser : MMS cursor null");
            }
        } catch (Exception e2) {
            Log.e(TAG, "queryUnreadMessagesCountForUser : error querying MMS count", e2);
        }
        try {
            where = new StringBuilder();
            where.append("read");
            where.append(" = ?");
            whereValues = new String[]{String.valueOf(0)};
            if (isTwoPhoneRegistered()) {
                where.append(" AND (using_mode = ?)");
                whereValues = new String[]{whereValues[0], String.valueOf(userId)};
            }
            Log.d(TAG, "queryUnreadMessagesCountForUser : whereWapPush=" + String.valueOf(where));
            cursor = getContext().getContentResolver().query(Uri.withAppendedPath(MmsSms.CONTENT_URI, "wap-push-messages"), new String[]{"date", "thread_id"}, where.toString(), whereValues, "date DESC");
            if (cursor != null) {
                Log.d(TAG, "queryUnreadMessagesCountForUser : countWapPush=" + String.valueOf(cursor.getCount()));
                unreadMessageUpdate.count = unreadMessageUpdate.count + cursor.getCount();
                if (cursor.moveToFirst()) {
                    mmsSmsUpdateDate = cursor.getLong(0);
                    latest_thread_id = cursor.getLong(1);
                }
                cursor.close();
            } else {
                Log.e(TAG, "queryUnreadMessagesCountForUser : WAP-PUSH cursor null");
            }
        } catch (Exception e22) {
            Log.e(TAG, "queryUnreadMessagesCountForUser : error querying WAP-PUSH count", e22);
        }
        try {
            where = new StringBuilder();
            where.append("read = ?");
            whereValues = new String[]{String.valueOf(0)};
            if (isTwoPhoneRegistered()) {
                where.append(" AND (using_mode = ?)");
                whereValues = new String[]{whereValues[0], String.valueOf(userId)};
            }
            Log.d(TAG, "queryUnreadMessagesCountForUser : whereRcsChat=" + String.valueOf(where));
            cursor = getContext().getContentResolver().query(Uri.parse("content://im/chat/"), new String[]{"date", "thread_id"}, where.toString(), whereValues, "date DESC");
            if (cursor != null) {
                Log.d(TAG, "queryUnreadMessagesCountForUser : countRcsChat=" + String.valueOf(cursor.getCount()));
                unreadMessageUpdate.count = unreadMessageUpdate.count + cursor.getCount();
                if (cursor.moveToFirst()) {
                    imMsgUpdateDate = cursor.getLong(0);
                    if (imMsgUpdateDate > mmsSmsUpdateDate) {
                        latest_thread_id = cursor.getLong(1);
                    }
                }
                cursor.close();
            } else {
                Log.e(TAG, "queryUnreadMessagesCountForUser : RCS/chat cursor null");
            }
        } catch (Exception e222) {
            Log.e(TAG, "queryUnreadMessagesCountForUser : error querying RCS/chat count", e222);
        }
        try {
            where = new StringBuilder();
            where.append("read = ?");
            whereValues = new String[]{String.valueOf(0)};
            if (isTwoPhoneRegistered()) {
                where.append(" AND (using_mode = ?)");
                whereValues = new String[]{whereValues[0], String.valueOf(userId)};
            }
            Log.d(TAG, "queryUnreadMessagesCountForUser : whereRcsFt=" + String.valueOf(where));
            cursor = getContext().getContentResolver().query(Uri.parse("content://im/ft/"), new String[]{"date", "thread_id"}, where.toString(), whereValues, "date DESC");
            if (cursor != null) {
                Log.d(TAG, "queryUnreadMessagesCountForUser : countRcsFt=" + String.valueOf(cursor.getCount()));
                unreadMessageUpdate.count = unreadMessageUpdate.count + cursor.getCount();
                if (cursor.moveToFirst()) {
                    long imFt = cursor.getLong(0);
                    if (imFt > mmsSmsUpdateDate && imFt > imMsgUpdateDate) {
                        imMsgUpdateDate = imFt;
                        latest_thread_id = cursor.getLong(1);
                    }
                }
                cursor.close();
            } else {
                Log.e(TAG, "queryUnreadMessagesCountForUser : RCS/ft cursor null");
            }
        } catch (Exception e2222) {
            Log.e(TAG, "queryUnreadMessagesCountForUser : error querying RCS/ft count", e2222);
        }
        if (latest_thread_id == -1) {
            Log.d(TAG, "queryUnreadMessagesCountForUser : countSmsMms=" + String.valueOf(unreadMessageUpdate.count) + " notify: " + unreadMessageUpdate.notify);
        } else {
            if (mmsSmsUpdateDate > imMsgUpdateDate) {
                unreadMessageUpdate.notify = !isMmsSmsMessageMutedForUser(latest_thread_id, userId);
            } else {
                unreadMessageUpdate.notify = !isImMessageMutedForUser(latest_thread_id, userId);
            }
            Log.d(TAG, "queryUnreadMessagesCountForUser Muted: countSmsMms=" + String.valueOf(unreadMessageUpdate.count) + " notify: " + unreadMessageUpdate.notify);
        }
        return unreadMessageUpdate;
    }

    private UnreadMessageUpdate[] queryUnreadMessagesCount() {
        UnreadMessageUpdate[] msgUpdates = new UnreadMessageUpdate[this.mUserIds.length];
        for (int i = 0; i < this.mUserIds.length; i++) {
            try {
                msgUpdates[i] = queryUnreadMessagesCountForUser(this.mUserIds[i]);
            } catch (Exception e) {
                Log.e(TAG, "Error while querying unread messages count for user " + String.valueOf(this.mUserIds[i]), e);
                msgUpdates[i] = null;
            }
        }
        Log.d(TAG, "queryUnreadMessagesCount : msgUpdates=" + Arrays.toString(msgUpdates));
        return msgUpdates;
    }

    private boolean isMmsSmsMessageMutedForUser(long thread_id, int userId) {
        Log.d(TAG, "isMmsSmsMessageMutedForUser : threadId=" + String.valueOf(thread_id) + " userId=" + String.valueOf(userId));
        Cursor cursor = null;
        boolean muted = false;
        boolean isTwoPhone = isTwoPhoneRegistered();
        if (isSamsungMessagingApp()) {
            boolean messageOwnerIsUserOwner = userId == 0;
            boolean ownerNotificationsEnabled = isNotificationEnabled();
            boolean twoPhoneNotificationsEnabled = isTwoPhoneBNotificationEnabled();
            Log.d(TAG, "isMmsSmsMessageMutedForUser : messageOwnerIsUserOwner=" + String.valueOf(messageOwnerIsUserOwner) + " ownerNotificationsEnabled=" + String.valueOf(ownerNotificationsEnabled) + " isTwoPhone=" + String.valueOf(isTwoPhone) + " twoPhoneNotificationsEnabled=" + String.valueOf(twoPhoneNotificationsEnabled) + " sim2NotificationEnabled=" + String.valueOf(isSim2NotificationEnabled()));
            if (!messageOwnerIsUserOwner) {
                return !twoPhoneNotificationsEnabled;
            } else {
                if (ownerNotificationsEnabled) {
                    return false;
                }
                return true;
            }
        }
        try {
            StringBuilder where = new StringBuilder();
            where.append("_id");
            where.append(" = ?");
            String[] whereValues = new String[]{String.valueOf(thread_id)};
            if (isTwoPhone) {
                where.append(" AND (using_mode = ?)");
                whereValues = new String[]{whereValues[0], String.valueOf(userId)};
            }
            Log.d(TAG, "isMmsSmsMessageMutedForUser : threadId=" + String.valueOf(thread_id));
            cursor = getContext().getContentResolver().query(Uri.withAppendedPath(MmsSms.CONTENT_URI, "threads"), new String[]{"is_mute"}, where.toString(), whereValues, null);
            if (cursor == null || !cursor.moveToFirst()) {
                Log.e(TAG, "isMmsSmsMessageMutedForUser : Mute cursor null");
            } else {
                muted = cursor.getInt(0) > 0;
                cursor.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "isMmsSmsMessageMutedForUser : error querying Mute count", e);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return muted;
    }

    private boolean isImMessageMutedForUser(long thread_id, int userId) {
        Log.d(TAG, "isImMessageMutedForUser : threadId=" + String.valueOf(thread_id) + " userId=" + String.valueOf(userId));
        Cursor cursor = null;
        boolean muted = false;
        try {
            StringBuilder where = new StringBuilder();
            where.append("normal_thread_id");
            where.append(" = ?");
            String[] whereValues = new String[]{String.valueOf(thread_id)};
            if (isTwoPhoneRegistered()) {
                where.append(" AND (using_mode = ?)");
                whereValues = new String[]{whereValues[0], String.valueOf(userId)};
            }
            Log.d(TAG, "isImMessageMutedForUser : threadId=" + String.valueOf(thread_id));
            cursor = getContext().getContentResolver().query(Uri.parse("content://mms-sms/im-threads"), new String[]{"is_mute"}, where.toString(), whereValues, null);
            if (cursor == null || !cursor.moveToFirst()) {
                Log.e(TAG, "isImMessageMutedForUser : Mute cursor null");
            } else {
                muted = cursor.getInt(0) > 0;
                cursor.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "isImMessageMutedForUser : error querying Mute count", e);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return muted;
    }

    private MissedCallUpdate queryMissedCallsCountForUser(int userId) {
        Log.d(TAG, "queryMissedCallsCountForUser : userId=" + String.valueOf(userId));
        MissedCallUpdate callUpdate = new MissedCallUpdate();
        callUpdate.count = 0;
        callUpdate.notify = true;
        callUpdate.userId = userId;
        Cursor cursor = getContext().getContentResolver().query(getMissedCallUri(userId), new String[]{Constants.REMOTE_VIEW_INFO_TYPE, "new"}, "type = ? AND new = ?", new String[]{String.valueOf(3), String.valueOf(1)}, "date DESC");
        if (cursor != null) {
            callUpdate.count = cursor.getCount();
            cursor.close();
        }
        return callUpdate;
    }

    private MissedCallUpdate[] queryMissedCallsCount() {
        MissedCallUpdate[] callUpdates = new MissedCallUpdate[this.mUserIds.length];
        for (int i = 0; i < this.mUserIds.length; i++) {
            try {
                callUpdates[i] = queryMissedCallsCountForUser(this.mUserIds[i]);
            } catch (Exception e) {
                Log.e(TAG, "Error while querying calls count for user " + String.valueOf(this.mUserIds[i]), e);
                callUpdates[i] = null;
            }
        }
        Log.d(TAG, "queryMissedCallsCount : callUpdates=" + Arrays.toString(callUpdates));
        return callUpdates;
    }

    private Uri getMissedCallUri(int userId) {
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

    public boolean isSamsungMessagingApp() {
        String defaultApplication = Secure.getString(getContext().getContentResolver(), RefSecure.get().SMS_DEFAULT_APPLICATION);
        Log.d(TAG, "isSamsungMessagingApp : defaultApplication=" + String.valueOf(defaultApplication) + " PACKAGE_SAMSUNG_MESSAGES=" + String.valueOf(PACKAGE_SAMSUNG_MESSAGES));
        return PACKAGE_SAMSUNG_MESSAGES.equals(defaultApplication);
    }

    public boolean isSim2NotificationEnabled() {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(MSG_PREFERENCE, null, "BOOLEAN", new String[]{"pref_key_enable_notifications_sim2"}, null);
            int value = 0;
            if (cursor != null && cursor.moveToFirst()) {
                value = cursor.getInt(0);
            }
            if (value == 1) {
                result = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLException at isSim2NotificationEnabled()");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "isSim2NotificationEnabled : result=" + String.valueOf(result));
        return result;
    }

    public boolean isNotificationEnabled() {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(MSG_PREFERENCE, null, "BOOLEAN", new String[]{"pref_key_enable_notifications"}, null);
            int value = 0;
            if (cursor != null && cursor.moveToFirst()) {
                value = cursor.getInt(0);
            }
            if (value == 1) {
                result = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLException at isNotificationEnabled()");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "isNotificationEnabled : result=" + String.valueOf(result));
        return result;
    }

    public boolean isTwoPhoneBNotificationEnabled() {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(MSG_PREFERENCE, null, "BOOLEAN", new String[]{"pref_key_enable_notifications_two_phone_B"}, null);
            int value = 0;
            if (cursor != null && cursor.moveToFirst()) {
                value = cursor.getInt(0);
            }
            if (value == 1) {
                result = true;
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLException at isTwoPhoneBNotificationEnabled()");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "isTwoPhoneBNotificationEnabled : result=" + String.valueOf(result));
        return result;
    }
}
