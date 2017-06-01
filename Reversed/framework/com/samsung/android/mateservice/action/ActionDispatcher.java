package com.samsung.android.mateservice.action;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.LongSparseArray;
import android.util.SparseArray;
import com.samsung.android.mateservice.common.LoggerContract;
import com.samsung.android.mateservice.util.UtilAccess;
import com.samsung.android.mateservice.util.UtilLog;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ActionDispatcher {
    private static final String TAG = "Dispatcher";
    private static final long TASK_EXPIRED_TIME = 3000;
    private static final int THREAD_COUNT = 2;
    private final Context mContext;
    private ActionAttr mDefault;
    private final ExecutorService mExecService;
    private final SparseArray<ActionAttr> mExecutes = new SparseArray();
    private final LongSparseArray<Future<?>> mFutures;
    private final LoggerContract mLogger;

    private static class ActionAttr {
        private ActionExecutable action;
        private boolean executeOnSameThread;

        private ActionAttr(ActionExecutable actionExecutable, boolean z) {
            this.action = actionExecutable;
            this.executeOnSameThread = z;
        }
    }

    private static class Task implements Runnable {
        private static final String TAG = "Task";
        private final ActionExecutable action;
        private final Bundle args;
        private final int extra;
        private final int flag;
        private long startTime;

        private Task(ActionExecutable actionExecutable, Bundle bundle, int i, int i2) {
            this.action = actionExecutable;
            this.args = bundle;
            this.flag = i;
            this.extra = i2;
        }

        public void run() {
            try {
                if (this.action != null) {
                    UtilLog.m9v(TAG, "Task begins - action[0x%x], task[%d] ", Integer.valueOf(this.extra), Long.valueOf(this.startTime));
                    this.action.execute(this.args, this.flag, this.extra);
                    UtilLog.m9v(TAG, "Task ends - action[0x%x], task[%d]", Integer.valueOf(this.extra), Long.valueOf(this.startTime));
                }
            } catch (Throwable th) {
                if (UtilLog.isDebugLogLevel() || UtilLog.isRoDebugLevelMid()) {
                    th.printStackTrace();
                }
            }
        }
    }

    public ActionDispatcher(Context context, LoggerContract loggerContract) {
        this.mContext = context;
        this.mLogger = loggerContract;
        this.mExecService = Executors.newFixedThreadPool(2);
        this.mFutures = new LongSparseArray();
    }

    private long generateKey() {
        return SystemClock.elapsedRealtime();
    }

    private boolean isExpiredTask(long j) {
        return System.currentTimeMillis() - j > TASK_EXPIRED_TIME;
    }

    private void prepareExecute() {
        Iterable<Long> linkedList = new LinkedList();
        long j = Long.MAX_VALUE;
        Future future = null;
        int i = 0;
        synchronized (this) {
            UtilLog.m9v(TAG, "prepareExecute future count [%d]", Integer.valueOf(this.mFutures.size()));
            for (int i2 = 0; i2 < r3; i2++) {
                long keyAt = this.mFutures.keyAt(i2);
                Future future2 = (Future) this.mFutures.get(keyAt);
                if (future2 != null) {
                    if (future2.isCancelled() || future2.isDone()) {
                        UtilLog.m9v(TAG, "prepareExecute task[%d] - cancelled or done", Long.valueOf(keyAt));
                        linkedList.add(Long.valueOf(keyAt));
                    } else {
                        UtilLog.m9v(TAG, "prepareExecute task[%d] - active", Long.valueOf(keyAt));
                        i++;
                        if (Math.min(keyAt, j) == keyAt) {
                            j = keyAt;
                            future = future2;
                        }
                    }
                }
            }
            for (Long longValue : linkedList) {
                UtilLog.m9v(TAG, "prepareExecute remove future[%d]", Long.valueOf(longValue.longValue()));
                this.mFutures.remove(keyAt);
            }
        }
        this.mLogger.append(UtilLog.m9v(TAG, "prepareExecute activeCount[%d], oldestTask[%d]", Integer.valueOf(i), Long.valueOf(j)), new Object[0]);
        Object obj = null;
        if (future != null && i >= 2 && isExpiredTask(j)) {
            future.cancel(true);
            obj = 1;
            this.mLogger.append(UtilLog.m9v(TAG, "prepareExecute expired task[%d]", Long.valueOf(j)), new Object[0]);
        }
        if (obj != null) {
            synchronized (this) {
                this.mFutures.remove(j);
            }
        }
    }

    public void append(int i, boolean z, ActionExecutable actionExecutable) {
        this.mExecutes.append(i, new ActionAttr(actionExecutable, z));
    }

    public Bundle execute(int i, Bundle bundle) throws RemoteException {
        Bundle bundle2 = null;
        UtilLog.m9v(TAG, "execute 0x%x", Integer.valueOf(i));
        try {
            if (!UtilAccess.isAccessible(this.mContext, i)) {
                return null;
            }
            ActionAttr actionAttr = (ActionAttr) this.mExecutes.get(i, this.mDefault);
            if (actionAttr != null) {
                int i2 = i;
                if (actionAttr.executeOnSameThread) {
                    bundle2 = actionAttr.action.execute(bundle, 0, i);
                } else {
                    prepareExecute();
                    long generateKey = generateKey();
                    Object task = new Task(actionAttr.action, bundle, 0, i);
                    Future submit = this.mExecService.submit(task);
                    synchronized (this) {
                        while (this.mFutures.get(generateKey) != null) {
                            generateKey++;
                        }
                        this.mFutures.put(generateKey, submit);
                    }
                    task.startTime = generateKey;
                    UtilLog.m9v(TAG, "submit task [%d]", Long.valueOf(generateKey));
                }
            }
            return bundle2;
        } catch (Throwable th) {
            UtilLog.printThrowableStackTrace(th);
            if (th instanceof RemoteException) {
            }
        }
    }

    public void setDefault(boolean z, ActionExecutable actionExecutable) {
        this.mDefault = new ActionAttr(actionExecutable, z);
    }
}
