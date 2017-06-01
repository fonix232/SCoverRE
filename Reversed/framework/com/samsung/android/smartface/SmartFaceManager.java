package com.samsung.android.smartface;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import com.samsung.android.smartface.ISmartFaceClient.Stub;
import com.samsung.android.transcode.core.Encode.BitRate;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SmartFaceManager {
    public static final String FALSE = "false";
    public static final String FEATURE_SMART_PAUSE = "com.sec.android.smartface.smart_pause";
    public static final String FEATURE_SMART_ROTATION = "com.sec.android.smartface.smart_rotation";
    public static final String FEATURE_SMART_SCROLL = "com.sec.android.smartface.smart_scroll";
    public static final String FEATURE_SMART_STAY = "com.sec.android.smartface.smart_stay";
    public static final int MSG_FACEINFO = 0;
    public static final int MSG_REGISTERED = 1;
    public static final int MSG_UNREGISTERED = 2;
    private static final String NULL_VALUE = "";
    public static final String PAGE_BOTTOM = "1";
    public static final String PAGE_MIDDLE = "0";
    public static final String PAGE_TOP = "-1";
    public static final String PAUSE_THIS_CLIENT = "paused-state";
    public static final int SERVICETYPE_HYBRID = 32;
    public static final int SERVICETYPE_MOTION = 16;
    public static final int SERVICETYPE_PAUSE = 2;
    public static final int SERVICETYPE_ROTATION = 8;
    public static final int SERVICETYPE_SCROLL = 1;
    public static final int SERVICETYPE_STAY = 4;
    public static final String SMARTFACE_SERVICE = "samsung.smartfaceservice";
    public static final String SMART_ROTATION_UI_ORIENTATION = "smart-rotation-ui-orientation";
    public static final String SMART_SCREEN_DUMP_PREVIEW = "smart-screen-dump";
    public static final String SMART_SCROLL_PAGE_STATUS = "smart-scroll-page-status";
    public static final String SMART_STAY_FRAMECOUNT_RESET = "smart-stay-framecount-reset";
    private static final String TAG = "SmartFaceManager";
    public static final String TRUE = "true";
    private final Condition complete = this.lock.newCondition();
    private final Lock lock = new ReentrantLock();
    private int mCallbackData;
    private SmartFaceClient mClient = null;
    private Context mContext = null;
    private EventHandler mEventHandler = null;
    private EventHandler mInternalEventHandler = null;
    private SmartFaceInfoListener mListener = null;
    private ISmartFaceService mService = null;
    private int mSmartStayDelay = Integer.parseInt("2950");

    public interface SmartFaceInfoListener {
        void onInfo(FaceInfo faceInfo, int i);
    }

    class C02531 implements SmartFaceInfoListener {
        C02531() {
        }

        public void onInfo(FaceInfo faceInfo, int i) {
            Log.e(SmartFaceManager.TAG, "checkForSmartStay onInfo: " + Integer.toBinaryString(i) + ": " + faceInfo.needToStay);
            if ((i & 4) != 0) {
                SmartFaceManager.this.lock.lock();
                SmartFaceManager.this.mCallbackData = faceInfo.needToStay;
                SmartFaceManager.this.complete.signal();
                SmartFaceManager.this.lock.unlock();
            }
        }
    }

    class C02542 implements SmartFaceInfoListener {
        C02542() {
        }

        public void onInfo(FaceInfo faceInfo, int i) {
            Log.e(SmartFaceManager.TAG, "checkForSmartRotation onInfo: " + Integer.toBinaryString(i) + ": " + faceInfo.needToRotate);
            if ((i & 8) != 0) {
                SmartFaceManager.this.lock.lock();
                SmartFaceManager.this.mCallbackData = faceInfo.needToRotate;
                SmartFaceManager.this.complete.signal();
                SmartFaceManager.this.lock.unlock();
            }
        }
    }

    private class EventHandler extends Handler {
        private SmartFaceManager mManager = null;

        public EventHandler(SmartFaceManager smartFaceManager, Looper looper) {
            super(looper);
            this.mManager = smartFaceManager;
        }

        public void handleMessage(Message message) {
            if (SmartFaceManager.this.mListener != null) {
                switch (message.what) {
                    case 0:
                        SmartFaceManager.this.mListener.onInfo((FaceInfo) message.obj, message.arg1);
                        return;
                    case 1:
                        if (SmartFaceManager.this.mListener instanceof SmartFaceInfoListener2) {
                            ((SmartFaceInfoListener2) SmartFaceManager.this.mListener).onRegistered(this.mManager, message.arg1);
                            return;
                        } else {
                            Log.e(SmartFaceManager.TAG, "Listener does not implements SmartFaceInfoListener2");
                            return;
                        }
                    case 2:
                        if (SmartFaceManager.this.mListener instanceof SmartFaceInfoListener2) {
                            ((SmartFaceInfoListener2) SmartFaceManager.this.mListener).onUnregistered(this.mManager, message.arg1);
                            return;
                        } else {
                            Log.e(SmartFaceManager.TAG, "Listener does not implements SmartFaceInfoListener2");
                            return;
                        }
                    default:
                        return;
                }
            }
            Log.e(SmartFaceManager.TAG, "Listener is null");
        }
    }

    private class SmartFaceClient extends Stub {
        SmartFaceClient() {
            Log.e(SmartFaceManager.TAG, "New SmartFaceClient");
        }

        public void onInfo(int i, FaceInfo faceInfo, int i2) {
            if (SmartFaceManager.this.mInternalEventHandler != null) {
                SmartFaceManager.this.mInternalEventHandler.sendMessage(SmartFaceManager.this.mInternalEventHandler.obtainMessage(i, i2, 0, faceInfo));
            } else if (SmartFaceManager.this.mEventHandler != null) {
                SmartFaceManager.this.mEventHandler.sendMessage(SmartFaceManager.this.mEventHandler.obtainMessage(i, i2, 0, faceInfo));
            } else {
                Log.e(SmartFaceManager.TAG, "EventHandler is null");
            }
        }
    }

    public interface SmartFaceInfoListener2 extends SmartFaceInfoListener {
        void onRegistered(SmartFaceManager smartFaceManager, int i);

        void onUnregistered(SmartFaceManager smartFaceManager, int i);
    }

    private SmartFaceManager(Context context) {
        this.mContext = context;
        this.mClient = new SmartFaceClient();
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            this.mEventHandler = new EventHandler(this, myLooper);
            return;
        }
        myLooper = Looper.getMainLooper();
        if (myLooper != null) {
            this.mEventHandler = new EventHandler(this, myLooper);
        } else {
            this.mEventHandler = null;
        }
    }

    private synchronized boolean ensureServiceConnected() {
        if (this.mService != null) {
            try {
                this.mService.setValue(this.mClient, "empty key for ping", "empty value");
            } catch (RemoteException e) {
                if (e instanceof DeadObjectException) {
                    this.mService = null;
                }
            }
        }
        if (this.mService == null) {
            startSmartFaceService();
            waitForService();
        }
        return this.mService != null;
    }

    public static SmartFaceManager getSmartFaceManager() {
        return null;
    }

    public static SmartFaceManager getSmartFaceManager(Context context) {
        return new SmartFaceManager(context);
    }

    private void startSmartFaceService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.samsung.android.smartface", "com.samsung.android.smartface.SmartFaceServiceStarter"));
        this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
    }

    private long waitForCallback(int i) {
        long j = -1;
        try {
            j = this.complete.awaitNanos((((long) i) * 1000) * 1000);
            if (j <= 0) {
                Log.e(TAG, "No Callback!");
            }
        } catch (Exception e) {
        }
        return j;
    }

    private void waitForService() {
        for (int i = 1; i <= 3; i++) {
            this.mService = ISmartFaceService.Stub.asInterface(ServiceManager.getService(SMARTFACE_SERVICE));
            if (this.mService != null) {
                Log.v(TAG, "Service connected!");
                return;
            }
            try {
                Thread.sleep(300);
                Log.e(TAG, "Wait for " + (i * 300) + "ms...");
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized boolean checkForSmartRotation(int i) {
        boolean z;
        Log.e(TAG, "checkForSmartRotation S: " + i);
        z = false;
        Thread handlerThread = new HandlerThread("Smart Rotation Wait Thread");
        handlerThread.start();
        this.mInternalEventHandler = new EventHandler(this, handlerThread.getLooper());
        SmartFaceInfoListener smartFaceInfoListener = this.mListener;
        setListener(new C02542());
        this.lock.lock();
        try {
            setValue(SMART_ROTATION_UI_ORIENTATION, i);
            if (start(8)) {
                this.mCallbackData = -1;
                waitForCallback(BitRate.MIN_VIDEO_D1_BITRATE);
                if (this.mCallbackData > 0) {
                    z = true;
                }
                this.mCallbackData = -1;
                waitForCallback(BitRate.MIN_VIDEO_D1_BITRATE);
                if (this.mCallbackData > 0) {
                    z = true;
                }
            }
            stop();
            handlerThread.quit();
            this.mInternalEventHandler = null;
            setListener(smartFaceInfoListener);
            Log.e(TAG, "checkForSmartRotation E: " + z);
        } finally {
            this.lock.unlock();
        }
        return z;
    }

    public synchronized boolean checkForSmartStay() {
        boolean z;
        Log.e(TAG, "checkForSmartStay S");
        z = false;
        Thread handlerThread = new HandlerThread("Smart Stay Wait Thread");
        handlerThread.start();
        this.mInternalEventHandler = new EventHandler(this, handlerThread.getLooper());
        SmartFaceInfoListener smartFaceInfoListener = this.mListener;
        setListener(new C02531());
        this.lock.lock();
        try {
            setValue(SMART_STAY_FRAMECOUNT_RESET, "");
            if (start(4)) {
                this.mCallbackData = -1;
                waitForCallback((int) (((float) this.mSmartStayDelay) * 0.43f));
                if (this.mCallbackData > 0) {
                    z = true;
                }
                this.mCallbackData = -1;
                waitForCallback((int) (((float) this.mSmartStayDelay) * 0.37f));
                if (this.mCallbackData > 0) {
                    z = true;
                }
            }
            stop();
            handlerThread.quit();
            this.mInternalEventHandler = null;
            setListener(smartFaceInfoListener);
            Log.e(TAG, "checkForSmartStay X: " + z);
        } finally {
            this.lock.unlock();
        }
        return z;
    }

    public int getSupportedServices() {
        if (!ensureServiceConnected()) {
            return 0;
        }
        int supportedServices;
        try {
            supportedServices = this.mService.getSupportedServices();
        } catch (Throwable e) {
            e.printStackTrace();
            supportedServices = 0;
        }
        return supportedServices;
    }

    public void pause() {
        setValue(PAUSE_THIS_CLIENT, TRUE);
        if (this.mEventHandler != null) {
            this.mEventHandler.removeCallbacksAndMessages(null);
        }
        if (this.mInternalEventHandler != null) {
            this.mInternalEventHandler.removeCallbacksAndMessages(null);
        }
    }

    public void resume() {
        setValue(PAUSE_THIS_CLIENT, FALSE);
    }

    public void setListener(SmartFaceInfoListener smartFaceInfoListener) {
        this.mListener = smartFaceInfoListener;
    }

    public void setValue(String str, int i) {
        setValue(str, Integer.toString(i));
    }

    public void setValue(String str, String str2) {
        if (ensureServiceConnected()) {
            Log.d(TAG, "Sending " + str + ":" + str2 + " to service");
            try {
                this.mService.setValue(this.mClient, str, str2);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public boolean start(int i) {
        if (!ensureServiceConnected()) {
            return false;
        }
        boolean z = false;
        try {
            z = this.mService.register(this.mClient, i);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return z;
    }

    public void startAsync(int i) {
        if (ensureServiceConnected()) {
            try {
                this.mService.registerAsync(this.mClient, i);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (ensureServiceConnected()) {
            try {
                this.mService.unregister(this.mClient);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages(null);
            }
            if (this.mInternalEventHandler != null) {
                this.mInternalEventHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    public void stopAsync() {
        if (ensureServiceConnected()) {
            try {
                this.mService.unregisterAsync(this.mClient);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages(null);
            }
            if (this.mInternalEventHandler != null) {
                this.mInternalEventHandler.removeCallbacksAndMessages(null);
            }
        }
    }
}
