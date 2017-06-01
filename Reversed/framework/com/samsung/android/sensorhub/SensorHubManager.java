package com.samsung.android.sensorhub;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SensorHubManager {
    public static final int CONTEXT_DELAY = 0;
    public static final int GESTURE_SENSOR_DELAY = 1;
    private static final int SENSORHUB_DISABLE = -1;
    private static final int SENSORHUB_DISABLE_FOR_DELAY = -1;
    private static final int SENSORHUB_ENABLE = 1;
    private static final int SENSORHUB_EVENT_SIZE = 16384;
    private static final String TAG = "SensorHubManager";
    private static ArrayList<SensorHub> sFullSensorHubsList = new ArrayList();
    static SparseArray<SensorHub> sHandleToSensorHub = new SparseArray();
    static final ArrayList<ListenerDelegate> sListeners = new ArrayList();
    private static SensorHubEventPool sPool;
    private static long sQueue;
    private static SparseArray<List<SensorHub>> sSensorHubListByType = new SparseArray();
    private static boolean sSensorHubModuleInitialized = false;
    private static SensorHubThread sSensorHubThread;
    Looper mMainLooper;

    private class ListenerDelegate {
        private final Handler mHandler;
        private final SensorHubEventListener mSensorHubEventListener;
        private final ArrayList<SensorHub> mSensorHubList = new ArrayList();
        public SparseBooleanArray mSensorHubs = new SparseBooleanArray();

        ListenerDelegate(SensorHubEventListener sensorHubEventListener, SensorHub sensorHub, Handler handler) {
            this.mSensorHubEventListener = sensorHubEventListener;
            this.mHandler = new Handler(handler != null ? handler.getLooper() : SensorHubManager.this.mMainLooper) {
                public void handleMessage(Message message) {
                    SensorHubEvent sensorHubEvent = (SensorHubEvent) message.obj;
                    ListenerDelegate.this.mSensorHubEventListener.onGetSensorHubData(sensorHubEvent);
                    SensorHubManager.sPool.returnToPool(sensorHubEvent);
                }
            };
            addSensorHub(sensorHub);
        }

        void addSensorHub(SensorHub sensorHub) {
            this.mSensorHubs.put(sensorHub.getHandle(), true);
            this.mSensorHubList.add(sensorHub);
        }

        Object getListener() {
            return this.mSensorHubEventListener;
        }

        List<SensorHub> getSensorHubs() {
            return this.mSensorHubList;
        }

        boolean hasSensorHub(SensorHub sensorHub) {
            return this.mSensorHubs.get(sensorHub.getHandle());
        }

        void onGetSensorHubDataLocked(SensorHub sensorHub, byte[] bArr, int i, float[] fArr, long[] jArr) {
            SensorHubEvent fromPool = SensorHubManager.sPool.getFromPool();
            fromPool.sensorhub = sensorHub;
            StringBuffer stringBuffer = new StringBuffer("onGetSensorHubDataLocked: ");
            int i2;
            if (i > 0) {
                fromPool.buffer = new byte[i];
                fromPool.length = i;
                stringBuffer.append("library(" + i + ") = ");
                fromPool.buffer[0] = bArr[0];
                stringBuffer.append(fromPool.buffer[0]);
                i2 = 1;
                while (i2 < i) {
                    fromPool.buffer[i2] = bArr[i2];
                    if (i >= 256 && i2 >= 6) {
                        if (i2 >= i - 6) {
                        }
                        if (i > 256 && i2 == 6) {
                            stringBuffer.append(" ...");
                        }
                        i2++;
                    }
                    stringBuffer.append(", ");
                    stringBuffer.append(fromPool.buffer[i2]);
                    stringBuffer.append(" ...");
                    i2++;
                }
            } else {
                fromPool.values = fArr;
                fromPool.timestamp = jArr[0];
                stringBuffer.append("gesture = ");
                fromPool.values[0] = fArr[0];
                stringBuffer.append(fromPool.values[0]);
                for (i2 = 1; i2 < fArr.length; i2++) {
                    fromPool.values[i2] = fArr[i2];
                    stringBuffer.append(", ");
                    stringBuffer.append(fromPool.values[i2]);
                }
            }
            Log.d(SensorHubManager.TAG, stringBuffer.toString());
            Message obtain = Message.obtain();
            obtain.what = 0;
            obtain.obj = fromPool;
            this.mHandler.sendMessage(obtain);
        }

        int removeSensorHub(SensorHub sensorHub) {
            this.mSensorHubs.delete(sensorHub.getHandle());
            this.mSensorHubList.remove(sensorHub);
            return this.mSensorHubs.size();
        }
    }

    private static class SensorHubEventPool {
        private int mNumItemsInPool;
        private final SensorHubEvent[] mPool;
        private final int mPoolSize;

        SensorHubEventPool(int i) {
            this.mPoolSize = i;
            this.mNumItemsInPool = i;
            this.mPool = new SensorHubEvent[i];
        }

        private SensorHubEvent createSensorHubEvent() {
            return new SensorHubEvent(16384);
        }

        SensorHubEvent getFromPool() {
            SensorHubEvent sensorHubEvent = null;
            synchronized (this) {
                if (this.mNumItemsInPool > 0) {
                    int i = this.mPoolSize - this.mNumItemsInPool;
                    sensorHubEvent = this.mPool[i];
                    this.mPool[i] = null;
                    this.mNumItemsInPool--;
                }
            }
            return sensorHubEvent == null ? createSensorHubEvent() : sensorHubEvent;
        }

        void returnToPool(SensorHubEvent sensorHubEvent) {
            synchronized (this) {
                if (this.mNumItemsInPool < this.mPoolSize) {
                    this.mNumItemsInPool++;
                    this.mPool[this.mPoolSize - this.mNumItemsInPool] = sensorHubEvent;
                }
            }
        }
    }

    private static class SensorHubThread {
        boolean mSensorHubsReady;
        Thread mThread;

        private class SensorHubThreadRunnable implements Runnable {
            SensorHubThreadRunnable() {
            }

            private boolean open() {
                SensorHubManager.sQueue = SensorHubManager.sensorhubs_create_queue();
                return true;
            }

            public void run() {
                byte[] bArr = new byte[16384];
                float[] fArr = new float[9];
                int[] iArr = new int[1];
                int[] iArr2 = new int[1];
                int[] iArr3 = new int[1];
                long[] jArr = new long[1];
                int i = 100;
                Process.setThreadPriority(-8);
                Log.d(SensorHubManager.TAG, "=======>>> SensorHubManager Thread RUNNING <<<=======");
                if (open()) {
                    synchronized (this) {
                        SensorHubThread.this.mSensorHubsReady = true;
                        notify();
                    }
                    while (true) {
                        bArr[0] = (byte) 0;
                        int sensorhubs_data_poll = SensorHubManager.sensorhubs_data_poll(SensorHubManager.sQueue, bArr, iArr, iArr2, iArr3, fArr, jArr, 16384);
                        int i2 = iArr3[0];
                        synchronized (SensorHubManager.sListeners) {
                            if (sensorhubs_data_poll != -1) {
                                if (!SensorHubManager.sListeners.isEmpty()) {
                                    if (bArr[0] == (byte) 0) {
                                        Log.e(SensorHubManager.TAG, "sensorhubs_data_poll() buffer 0 =" + i);
                                        i--;
                                        if (i <= 0) {
                                            break;
                                        }
                                    }
                                    i = 100;
                                    SensorHub sensorHub = (SensorHub) SensorHubManager.sHandleToSensorHub.get(sensorhubs_data_poll);
                                    if (sensorHub != null) {
                                        int size = SensorHubManager.sListeners.size();
                                        for (int i3 = 0; i3 < size; i3++) {
                                            ListenerDelegate listenerDelegate = (ListenerDelegate) SensorHubManager.sListeners.get(i3);
                                            if (listenerDelegate.hasSensorHub(sensorHub)) {
                                                listenerDelegate.onGetSensorHubDataLocked(sensorHub, bArr, i2, fArr, jArr);
                                            }
                                        }
                                    }
                                }
                            }
                            if (sensorhubs_data_poll != -1 || SensorHubManager.sListeners.isEmpty()) {
                                SensorHubManager.sensorhubs_destroy_queue(SensorHubManager.sQueue);
                                SensorHubManager.sQueue = 0;
                                SensorHubThread.this.mThread = null;
                            } else {
                                Log.e(SensorHubManager.TAG, "sensorhubs_data_poll() failed, we bail out: sensorHub=" + sensorhubs_data_poll);
                            }
                        }
                    }
                    Log.e(SensorHubManager.TAG, "sensorhubs_data_poll() destroy queue =" + i);
                    SensorHubManager.sensorhubs_destroy_queue(SensorHubManager.sQueue);
                    SensorHubManager.sQueue = 0;
                    SensorHubThread.this.mThread = null;
                }
            }
        }

        SensorHubThread() {
        }

        protected void finalize() {
        }

        boolean startLocked() {
            try {
                if (this.mThread == null) {
                    this.mSensorHubsReady = false;
                    Runnable sensorHubThreadRunnable = new SensorHubThreadRunnable();
                    Thread thread = new Thread(sensorHubThreadRunnable, SensorHubThread.class.getName());
                    thread.start();
                    synchronized (sensorHubThreadRunnable) {
                        while (!this.mSensorHubsReady) {
                            sensorHubThreadRunnable.wait();
                        }
                    }
                    this.mThread = thread;
                }
            } catch (InterruptedException e) {
            }
            return this.mThread != null;
        }
    }

    public SensorHubManager(Context context, Looper looper) {
        this.mMainLooper = looper;
        synchronized (sListeners) {
            if (!sSensorHubModuleInitialized) {
                sSensorHubModuleInitialized = true;
                nativeClassInit();
                sensorhubs_module_init();
                Log.d(TAG, "sensorhubs_module_init()");
                ArrayList arrayList = sFullSensorHubsList;
                int i = 0;
                do {
                    SensorHub sensorHub = new SensorHub();
                    i = sensorhubs_get_next_module(sensorHub, i);
                    Log.d(TAG, "Num SensorHub= " + i);
                    if (i >= 0) {
                        Log.d(TAG, "found sensorhub= " + sensorHub.getName() + ", handle=" + sensorHub.getHandle());
                        arrayList.add(sensorHub);
                        sHandleToSensorHub.append(sensorHub.getHandle(), sensorHub);
                        continue;
                    }
                } while (i > 0);
                sPool = new SensorHubEventPool(sFullSensorHubsList.size() * 2);
                sSensorHubThread = new SensorHubThread();
            }
        }
    }

    private static int SendSensorHubData(int i, byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = 0;
        while (i2 < i) {
            if (i2 == 0) {
                stringBuffer.append("send data = ");
            } else {
                if (i >= 256 && i2 >= 6) {
                    if (i2 >= i - 6) {
                    }
                }
                stringBuffer.append(", ");
            }
            if (i >= 256 && i2 >= 6) {
                if (i2 >= i - 6) {
                }
                if (i > 256 && i2 == 6) {
                    stringBuffer.append(" ...");
                }
                i2++;
            }
            stringBuffer.append(bArr[i2]);
            stringBuffer.append(" ...");
            i2++;
        }
        Log.d(TAG, "SendSensorHubData: " + stringBuffer.toString());
        int sensorhubs_send_data = sensorhubs_send_data(sQueue, 0, i, bArr);
        if (sensorhubs_send_data < 0) {
            Log.e(TAG, "SendSensorHubData: error(" + sensorhubs_send_data + ")");
        }
        return sensorhubs_send_data;
    }

    private boolean disableSensorHubLocked(SensorHub sensorHub) {
        return sensorhubs_enabledisable(sQueue, sensorHub.getHandle(), -1, -1);
    }

    private boolean enableSensorHubLocked(SensorHub sensorHub, int i) {
        for (ListenerDelegate hasSensorHub : sListeners) {
            if (hasSensorHub.hasSensorHub(sensorHub)) {
                return sensorhubs_enabledisable(sQueue, sensorHub.getHandle(), 1, i);
            }
        }
        return false;
    }

    private static native void nativeClassInit();

    static native long sensorhubs_create_queue();

    static native int sensorhubs_data_poll(long j, byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, float[] fArr, long[] jArr, int i);

    static native void sensorhubs_destroy_queue(long j);

    static native boolean sensorhubs_enabledisable(long j, int i, int i2, int i3);

    private static native int sensorhubs_get_next_module(SensorHub sensorHub, int i);

    private static native int sensorhubs_module_init();

    static native int sensorhubs_send_data(long j, int i, int i2, byte[] bArr);

    static native boolean sensorhubs_send_delay(long j, int i, int i2);

    private void unregisterListener(Object obj) {
        if (obj != null) {
            synchronized (sListeners) {
                int size = sListeners.size();
                int i = 0;
                while (i < size) {
                    ListenerDelegate listenerDelegate = (ListenerDelegate) sListeners.get(i);
                    if (listenerDelegate.getListener() == obj) {
                        sListeners.remove(i);
                        for (SensorHub disableSensorHubLocked : listenerDelegate.getSensorHubs()) {
                            disableSensorHubLocked(disableSensorHubLocked);
                            Log.d(TAG, "unregisterListener: disable all sensorhubs for this listener, name=  listener= " + obj);
                        }
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    private void unregisterListener(Object obj, SensorHub sensorHub) {
        if (obj != null && sensorHub != null) {
            synchronized (sListeners) {
                int size = sListeners.size();
                int i = 0;
                while (i < size) {
                    ListenerDelegate listenerDelegate = (ListenerDelegate) sListeners.get(i);
                    if (listenerDelegate.getListener() == obj) {
                        if (listenerDelegate.removeSensorHub(sensorHub) == 0) {
                            sListeners.remove(i);
                        }
                        disableSensorHubLocked(sensorHub);
                        Log.d(TAG, "unregisterListener: handle= " + sensorHub.getHandle() + " Listener= " + obj);
                    } else {
                        i++;
                    }
                }
                disableSensorHubLocked(sensorHub);
                Log.d(TAG, "unregisterListener: handle= " + sensorHub.getHandle() + " Listener= " + obj);
            }
        }
    }

    public int SendSensorHubData(SensorHub sensorHub, int i, byte[] bArr) {
        return SendSensorHubData(i, bArr);
    }

    public SensorHub getDefaultSensorHub(int i) {
        List sensorHubList = getSensorHubList(i);
        return sensorHubList.isEmpty() ? null : (SensorHub) sensorHubList.get(0);
    }

    public List<SensorHub> getSensorHubList(int i) {
        List<SensorHub> list;
        Iterable<SensorHub> iterable = sFullSensorHubsList;
        synchronized (iterable) {
            list = (List) sSensorHubListByType.get(i);
            if (list == null) {
                List arrayList = new ArrayList();
                for (SensorHub sensorHub : iterable) {
                    if (sensorHub.getType() == i) {
                        arrayList.add(sensorHub);
                    }
                }
                list = Collections.unmodifiableList(arrayList);
                sSensorHubListByType.append(i, list);
            }
        }
        return list;
    }

    public boolean registerListener(SensorHubEventListener sensorHubEventListener, SensorHub sensorHub, int i) {
        return registerListener(sensorHubEventListener, sensorHub, i, null);
    }

    public boolean registerListener(SensorHubEventListener sensorHubEventListener, SensorHub sensorHub, int i, Handler handler) {
        if (i >= 0) {
            return registerListener(sensorHubEventListener, sensorHub, i, handler, 0);
        }
        throw new IllegalArgumentException("rate must be >=0");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean registerListener(com.samsung.android.sensorhub.SensorHubEventListener r11, com.samsung.android.sensorhub.SensorHub r12, int r13, android.os.Handler r14, int r15) {
        /*
        r10 = this;
        r6 = 0;
        if (r11 == 0) goto L_0x0005;
    L_0x0003:
        if (r12 != 0) goto L_0x0006;
    L_0x0005:
        return r6;
    L_0x0006:
        if (r13 >= 0) goto L_0x0011;
    L_0x0008:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "rate must be >=0";
        r6.<init>(r7);
        throw r6;
    L_0x0011:
        r5 = 1;
        r0 = -1;
        switch(r13) {
            case 0: goto L_0x009c;
            case 1: goto L_0x009f;
            default: goto L_0x0016;
        };
    L_0x0016:
        r0 = r13;
    L_0x0017:
        r7 = sListeners;
        monitor-enter(r7);
        r3 = 0;
        r6 = sListeners;	 Catch:{ all -> 0x00c4 }
        r2 = r6.iterator();	 Catch:{ all -> 0x00c4 }
    L_0x0021:
        r6 = r2.hasNext();	 Catch:{ all -> 0x00c4 }
        if (r6 == 0) goto L_0x00cc;
    L_0x0027:
        r1 = r2.next();	 Catch:{ all -> 0x00c4 }
        r1 = (com.samsung.android.sensorhub.SensorHubManager.ListenerDelegate) r1;	 Catch:{ all -> 0x00c4 }
        r6 = r1.getListener();	 Catch:{ all -> 0x00c4 }
        if (r6 != r11) goto L_0x0021;
    L_0x0033:
        r3 = r1;
        r4 = r3;
    L_0x0035:
        r6 = "SensorHubManager";
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c7 }
        r8.<init>();	 Catch:{ all -> 0x00c7 }
        r9 = "registerListener: handle= ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c7 }
        r9 = r12.getHandle();	 Catch:{ all -> 0x00c7 }
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c7 }
        r9 = " delay= ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c7 }
        r8 = r8.append(r0);	 Catch:{ all -> 0x00c7 }
        r9 = " Listener= ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c7 }
        r8 = r8.append(r11);	 Catch:{ all -> 0x00c7 }
        r8 = r8.toString();	 Catch:{ all -> 0x00c7 }
        android.util.Log.d(r6, r8);	 Catch:{ all -> 0x00c7 }
        if (r4 != 0) goto L_0x00ac;
    L_0x006b:
        r3 = new com.samsung.android.sensorhub.SensorHubManager$ListenerDelegate;	 Catch:{ all -> 0x00c7 }
        r3.<init>(r11, r12, r14);	 Catch:{ all -> 0x00c7 }
        r6 = sListeners;	 Catch:{ all -> 0x00c4 }
        r6.add(r3);	 Catch:{ all -> 0x00c4 }
        r6 = sListeners;	 Catch:{ all -> 0x00c4 }
        r6 = r6.isEmpty();	 Catch:{ all -> 0x00c4 }
        if (r6 != 0) goto L_0x00aa;
    L_0x007d:
        r6 = sSensorHubThread;	 Catch:{ all -> 0x00c4 }
        r6 = r6.startLocked();	 Catch:{ all -> 0x00c4 }
        if (r6 == 0) goto L_0x00a3;
    L_0x0085:
        r6 = r10.enableSensorHubLocked(r12, r0);	 Catch:{ all -> 0x00c4 }
        if (r6 != 0) goto L_0x009a;
    L_0x008b:
        r6 = sListeners;	 Catch:{ all -> 0x00c4 }
        r6.remove(r3);	 Catch:{ all -> 0x00c4 }
        r5 = 0;
        r6 = "SensorHubManager";
        r8 = "registerListener: enableSensorHubLocked fail 1";
        android.util.Log.d(r6, r8);	 Catch:{ all -> 0x00c4 }
    L_0x009a:
        monitor-exit(r7);
        return r5;
    L_0x009c:
        r0 = 0;
        goto L_0x0017;
    L_0x009f:
        r0 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        goto L_0x0017;
    L_0x00a3:
        r6 = sListeners;	 Catch:{ all -> 0x00c4 }
        r6.remove(r3);	 Catch:{ all -> 0x00c4 }
        r5 = 0;
        goto L_0x009a;
    L_0x00aa:
        r5 = 0;
        goto L_0x009a;
    L_0x00ac:
        r4.addSensorHub(r12);	 Catch:{ all -> 0x00c7 }
        r6 = r10.enableSensorHubLocked(r12, r0);	 Catch:{ all -> 0x00c7 }
        if (r6 != 0) goto L_0x00ca;
    L_0x00b5:
        r4.removeSensorHub(r12);	 Catch:{ all -> 0x00c7 }
        r5 = 0;
        r6 = "SensorHubManager";
        r8 = "registerListener: enableSensorHubLocked fail 2";
        android.util.Log.d(r6, r8);	 Catch:{ all -> 0x00c7 }
        r3 = r4;
        goto L_0x009a;
    L_0x00c4:
        r6 = move-exception;
    L_0x00c5:
        monitor-exit(r7);
        throw r6;
    L_0x00c7:
        r6 = move-exception;
        r3 = r4;
        goto L_0x00c5;
    L_0x00ca:
        r3 = r4;
        goto L_0x009a;
    L_0x00cc:
        r4 = r3;
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.sensorhub.SensorHubManager.registerListener(com.samsung.android.sensorhub.SensorHubEventListener, com.samsung.android.sensorhub.SensorHub, int, android.os.Handler, int):boolean");
    }

    public void unregisterListener(SensorHubEventListener sensorHubEventListener) {
        unregisterListener((Object) sensorHubEventListener);
    }

    public void unregisterListener(SensorHubEventListener sensorHubEventListener, SensorHub sensorHub) {
        unregisterListener((Object) sensorHubEventListener, sensorHub);
    }
}
