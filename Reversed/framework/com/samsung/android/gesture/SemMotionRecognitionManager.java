package com.samsung.android.gesture;

import android.app.ActivityThread;
import android.hardware.scontext.SContext;
import android.hardware.scontext.SContextEvent;
import android.hardware.scontext.SContextListener;
import android.hardware.scontext.SContextManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.samsung.android.hardware.context.ISemContextService;
import com.samsung.android.hardware.context.ISemContextService.Stub;
import java.util.ArrayList;

public class SemMotionRecognitionManager {
    public static final String ACTION_MOTION_RECOGNITION_EVENT = "com.samsung.android.gesture.MOTION_RECOGNITION_EVENT";
    private static final int DEBUG_LEVEL_HIGH = 18760;
    private static final int DEBUG_LEVEL_LOW = 20300;
    private static final int DEBUG_LEVEL_MID = 18765;
    public static final int EVENT_DOUBLE_TAP = 8;
    public static final int EVENT_LOCK_EXECUTE_L = 128;
    public static final int EVENT_LOCK_EXECUTE_R = 256;
    public static final int EVENT_PANNING_GALLERY = 32;
    public static final int EVENT_PANNING_HOME = 64;
    public static final int EVENT_SHAKE = 2;
    public static final int EVENT_SMART_ALERT_SETTING = 32768;
    public static final int EVENT_SMART_SCROLL = 524288;
    public static final int EVENT_TILT = 16;
    public static final int EVENT_TILT_LEVEL_ZERO = 4096;
    public static final int EVENT_TILT_LEVEL_ZERO_LAND = 16384;
    public static final int EVENT_TILT_TO_UNLOCK = 2048;
    public static final int EVENT_VOLUME_DOWN = 512;
    public static final int MOTION_ALL = 1516549;
    public static final int MOTION_CALL_POSE = 262144;
    public static final int MOTION_DIRECT_CALLING = 1024;
    public static final int MOTION_FLAT = 8192;
    public static final int MOTION_NUM = 21;
    public static final int MOTION_OVERTURN = 1;
    public static final int MOTION_OVERTURN_LOW_POWER = 131072;
    public static final int MOTION_PALM_SWIPE = 4194304;
    public static final int MOTION_PALM_TOUCH = 2097152;
    public static final int MOTION_SCREEN_UP_STEADY = 65536;
    public static final int MOTION_SENSOR_NUM = 4;
    public static final int MOTION_SMART_ALERT = 4;
    public static final int MOTION_SMART_RELAY = 1048576;
    public static final int MOTION_USE_ACC = 1;
    public static final int MOTION_USE_ALL = 15;
    public static final int MOTION_USE_ALWAYS = 1073741824;
    public static final int MOTION_USE_GYRO = 2;
    public static final int MOTION_USE_LIGHT = 8;
    public static final int MOTION_USE_PROX = 4;
    protected static final String TAG = "MotionRecognitionManager";
    private static final boolean localLOGV = false;
    private static final int mMotionVersion = 1;
    private Looper mMainLooper;
    private int mMovementCnt;
    private SContextManager mSContextManager;
    private ISemContextService mSContextService = Stub.asInterface(ServiceManager.getService("scontext"));
    private boolean mSSPEnabled;
    private IMotionRecognitionService motionService;
    private final SContextListener mySContextMotionListener = new C00801();
    private final ArrayList<MRListenerDelegate> sListenerDelegates = new ArrayList();
    private final ArrayList<String> sListenerwithSSP = new ArrayList();

    class C00801 implements SContextListener {
        C00801() {
        }

        public void onSContextChanged(SContextEvent sContextEvent) {
            SContext sContext = sContextEvent.scontext;
            SemMotionRecognitionEvent semMotionRecognitionEvent = new SemMotionRecognitionEvent();
            boolean z = false;
            switch (sContext.getType()) {
                case 5:
                    if (sContextEvent.getMovementContext().getAction() == 1) {
                        try {
                            z = SemMotionRecognitionManager.this.motionService.getPickUpMotionStatus();
                            Log.d(SemMotionRecognitionManager.TAG, "  >> check setting smart alert enabled : " + z);
                        } catch (Throwable e) {
                            Log.e(SemMotionRecognitionManager.TAG, "RemoteException in getPickUpMotionStatus: ", e);
                        }
                        if (z) {
                            semMotionRecognitionEvent.setMotion(67);
                            Log.d(SemMotionRecognitionManager.TAG, "mySContextMotionListener : Send Smart alert event");
                            synchronized (SemMotionRecognitionManager.this.sListenerDelegates) {
                                int size = SemMotionRecognitionManager.this.sListenerDelegates.size();
                                for (int i = 0; i < size; i++) {
                                    ((MRListenerDelegate) SemMotionRecognitionManager.this.sListenerDelegates.get(i)).motionCallback(semMotionRecognitionEvent);
                                }
                            }
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private class MRListenerDelegate extends IMotionRecognitionCallback.Stub {
        private final int EVENT_FROM_SERVICE = 53;
        private final Handler mHandler;
        private final SemMotionEventListener mListener;
        private String mListenerPackageName = null;
        private int mMotionEvents;

        MRListenerDelegate(SemMotionEventListener semMotionEventListener, int i, Handler handler) {
            this.mListener = semMotionEventListener;
            Looper looper = handler != null ? handler.getLooper() : SemMotionRecognitionManager.this.mMainLooper;
            this.mMotionEvents = i;
            this.mListenerPackageName = ActivityThread.currentPackageName();
            this.mHandler = new Handler(looper) {
                public void handleMessage(Message message) {
                    try {
                        if (MRListenerDelegate.this.mListener != null && message != null && message.what == 53) {
                            MRListenerDelegate.this.mListener.onMotionEvent((SemMotionRecognitionEvent) message.obj);
                        }
                    } catch (Throwable e) {
                        Log.e(SemMotionRecognitionManager.TAG, "ClassCastException in handleMessage: msg.obj = " + message.obj, e);
                    }
                }
            };
        }

        public SemMotionEventListener getListener() {
            return this.mListener;
        }

        public String getListenerInfo() {
            return this.mListener.toString();
        }

        public String getListenerPackageName() {
            return this.mListenerPackageName;
        }

        public int getMotionEvents() {
            return this.mMotionEvents;
        }

        public void motionCallback(SemMotionRecognitionEvent semMotionRecognitionEvent) {
            Message obtain = Message.obtain();
            obtain.what = 53;
            obtain.obj = semMotionRecognitionEvent;
            this.mHandler.sendMessage(obtain);
        }

        public void setMotionEvents(int i) {
            this.mMotionEvents = i;
        }
    }

    public SemMotionRecognitionManager(Looper looper) {
        int i = 0;
        Log.d(TAG, "mSContextService = " + this.mSContextService);
        this.mMainLooper = looper;
        this.mSContextManager = new SContextManager(this.mMainLooper);
        this.mMovementCnt = 0;
        if (this.mSContextService != null) {
            try {
                this.motionService = IMotionRecognitionService.Stub.asInterface(this.mSContextService.getMotionRecognitionService());
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in motionService: ", e);
            } finally {
                String str = TAG;
                StringBuilder append = new StringBuilder().append("motionService = ");
                i = this.motionService;
                Log.d(str, append.append(i).toString());
            }
        } else {
            this.motionService = IMotionRecognitionService.Stub.asInterface(ServiceManager.getService("motion_recognition"));
        }
        Log.d(TAG, "motionService = " + this.motionService);
        this.mMovementCnt = i;
        try {
            if (this.motionService != null) {
                this.mSSPEnabled = this.motionService.getSSPstatus();
            }
        } catch (Throwable e2) {
            Log.e(TAG, "RemoteException in getSSPstatus: ", e2);
        }
    }

    private static String EncodeLog(String str) {
        String str2 = SystemProperties.get("ro.debug_level", "Unknown");
        if (str2.equals("Unknown")) {
            return " ";
        }
        try {
            int parseInt = Integer.parseInt(str2.substring(2), 16);
            return parseInt == DEBUG_LEVEL_LOW ? " " : (parseInt == DEBUG_LEVEL_MID || parseInt == DEBUG_LEVEL_HIGH) ? str : " ";
        } catch (NumberFormatException e) {
            return " ";
        }
    }

    public static int getMotionVersion() {
        return 1;
    }

    @Deprecated
    public static boolean isValidMotionSensor(int i) {
        return i == 1 || i == 2 || i == 4 || i == 8;
    }

    public boolean isAvailable(int i) {
        boolean z = false;
        if (this.motionService == null) {
            return z;
        }
        switch (i) {
            case 1:
            case 4:
            case 1024:
            case 2097152:
            case 4194304:
                try {
                    z = this.motionService.isAvailable(i);
                    break;
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in getSSPstatus: ", e);
                    break;
                }
            default:
                z = false;
                break;
        }
        return z;
    }

    public void registerListener(SemMotionEventListener semMotionEventListener, int i) {
        registerListener(semMotionEventListener, i, null);
    }

    public void registerListener(SemMotionEventListener semMotionEventListener, int i, int i2, Handler handler) {
        Throwable th;
        if (!(semMotionEventListener == null || this.motionService == null)) {
            synchronized (this.sListenerDelegates) {
                MRListenerDelegate mRListenerDelegate = null;
                int size = this.sListenerDelegates.size();
                Object obj = null;
                for (int i3 = 0; i3 < size; i3++) {
                    MRListenerDelegate mRListenerDelegate2 = (MRListenerDelegate) this.sListenerDelegates.get(i3);
                    if (mRListenerDelegate2.getListener() == semMotionEventListener) {
                        String EncodeLog = EncodeLog("name :" + semMotionEventListener);
                        if ((mRListenerDelegate2.getMotionEvents() & i2) != 0) {
                            Log.d(TAG, "  .registerListener : fail. already registered / listener count = " + this.sListenerDelegates.size() + ", " + EncodeLog);
                            obj = 1;
                        } else {
                            mRListenerDelegate = mRListenerDelegate2;
                            Log.d(TAG, "  .registerListener : already registered but need to update motion events / listener count = " + this.sListenerDelegates.size() + ", " + EncodeLog);
                        }
                    }
                }
                if (obj != null) {
                    return;
                }
                IBinder iBinder;
                IBinder mRListenerDelegate3;
                if (mRListenerDelegate != null) {
                    i2 |= mRListenerDelegate.getMotionEvents();
                    unregisterListener(semMotionEventListener);
                    iBinder = null;
                } else {
                    Object obj2 = mRListenerDelegate;
                }
                if (iBinder == null) {
                    try {
                        mRListenerDelegate3 = new MRListenerDelegate(semMotionEventListener, i2, handler);
                    } catch (Throwable th2) {
                        th = th2;
                        mRListenerDelegate3 = iBinder;
                        throw th;
                    }
                }
                mRListenerDelegate3 = iBinder;
                try {
                    this.sListenerDelegates.add(mRListenerDelegate3);
                    if ((i2 & 4) != 0) {
                        if (this.mSSPEnabled) {
                            if (this.mySContextMotionListener == null || this.mMovementCnt != 0) {
                                Log.e(TAG, " [MOVEMENT_SERVICE] registerListener : fail. already registered ");
                            } else {
                                Log.d(TAG, " [MOVEMENT_SERVICE] registerListener ");
                                this.mSContextManager.registerListener(this.mySContextMotionListener, 5);
                            }
                            this.mMovementCnt++;
                            i2 &= -5;
                        } else {
                            try {
                                this.mSSPEnabled = this.motionService.getSSPstatus();
                            } catch (Throwable e) {
                                Log.e(TAG, "RemoteException in getSSPstatus: ", e);
                            }
                            Log.d(TAG, "SSP disabled : " + this.mSSPEnabled);
                        }
                    }
                    if (i2 != 0) {
                        this.motionService.registerCallback(mRListenerDelegate3, i, i2);
                    }
                } catch (Throwable e2) {
                    Log.e(TAG, "RemoteException in registerListener : ", e2);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
                Log.v(TAG, "  .registerListener : success. listener count = " + size + "->" + this.sListenerDelegates.size() + ", motion_events=" + i2 + ", " + EncodeLog("name :" + semMotionEventListener));
            }
        }
    }

    public void registerListener(SemMotionEventListener semMotionEventListener, int i, Handler handler) {
        registerListener(semMotionEventListener, 0, i, handler);
    }

    public int resetMotionEngine() {
        if (this.motionService == null) {
            return -1;
        }
        try {
            return this.motionService.resetMotionEngine();
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException in resetMotionEngine: ", e);
            return 0;
        }
    }

    public void setMotionAngle(SemMotionEventListener semMotionEventListener, int i) {
    }

    @Deprecated
    public void setMotionTiltLevel(int i, int i2, int i3, int i4, int i5, int i6) {
        if (this.motionService != null) {
            try {
                this.motionService.setMotionTiltLevel(i, i2, i3, i4, i5, i6);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in setMotionTiltLevel: ", e);
            }
            Log.d(TAG, "  .setMotionTiltLevel : 1");
        }
    }

    public void setSmartMotionAngle(SemMotionEventListener semMotionEventListener, int i) {
        if (this.motionService != null) {
            synchronized (this.sListenerDelegates) {
                int size = this.sListenerDelegates.size();
                int i2 = 0;
                while (i2 < size) {
                    MRListenerDelegate mRListenerDelegate = (MRListenerDelegate) this.sListenerDelegates.get(i2);
                    if (mRListenerDelegate.getListener() == semMotionEventListener) {
                        try {
                            this.motionService.setMotionAngle(mRListenerDelegate, i);
                        } catch (Throwable e) {
                            Log.e(TAG, "RemoteException in setSmartMotionAngle: ", e);
                        }
                    } else {
                        i2++;
                    }
                }
                Log.d(TAG, "  .setSmartMotionAngle : listener has to be registered first");
                return;
            }
        }
        return;
    }

    public boolean setTestSensor() {
        try {
            return this.motionService.setTestSensor();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setTestSensor");
            return false;
        }
    }

    public void unregisterListener(SemMotionEventListener semMotionEventListener) {
        if (this.motionService != null) {
            synchronized (this.sListenerDelegates) {
                int i;
                int size = this.sListenerDelegates.size();
                for (i = 0; i < size; i++) {
                    Log.d(TAG, "@ member " + i + " = " + EncodeLog(((MRListenerDelegate) this.sListenerDelegates.get(i)).getListener().toString()));
                }
                i = 0;
                while (i < size) {
                    MRListenerDelegate mRListenerDelegate = (MRListenerDelegate) this.sListenerDelegates.get(i);
                    if (mRListenerDelegate.getListener() == semMotionEventListener) {
                        this.sListenerDelegates.remove(i);
                        Object obj = null;
                        try {
                            if ((mRListenerDelegate.getMotionEvents() & 4) != 0) {
                                if (this.mSSPEnabled) {
                                    this.mMovementCnt--;
                                    if (this.mMovementCnt <= 0) {
                                        Log.d(TAG, " [MOVEMENT_SERVICE] unregisterListener ");
                                        this.mMovementCnt = 0;
                                        this.mSContextManager.unregisterListener(this.mySContextMotionListener, 5);
                                    }
                                    Log.d(TAG, "unregisterListener - mMovementCnt : " + this.mMovementCnt);
                                } else {
                                    try {
                                        this.mSSPEnabled = this.motionService.getSSPstatus();
                                    } catch (Throwable e) {
                                        Log.e(TAG, "RemoteException in getSSPstatus: ", e);
                                    }
                                    Log.d(TAG, "SSP disabled : " + this.mSSPEnabled);
                                    this.motionService.unregisterCallback(mRListenerDelegate);
                                    obj = 1;
                                }
                            }
                            if ((mRListenerDelegate.getMotionEvents() & -5) != 0 && r0 == null) {
                                this.motionService.unregisterCallback(mRListenerDelegate);
                            }
                        } catch (Throwable e2) {
                            Log.e(TAG, "RemoteException in unregisterListener: ", e2);
                        }
                    } else {
                        i++;
                    }
                }
                Log.i(TAG, "  .unregisterListener : / listener count = " + size + "->" + this.sListenerDelegates.size() + ", " + EncodeLog("name :" + semMotionEventListener));
            }
        }
    }

    public void unregisterListener(com.samsung.android.gesture.SemMotionEventListener r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.samsung.android.gesture.SemMotionRecognitionManager.unregisterListener(com.samsung.android.gesture.SemMotionEventListener, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: not-int
	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:568)
	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:56)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:102)
	... 4 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.gesture.SemMotionRecognitionManager.unregisterListener(com.samsung.android.gesture.SemMotionEventListener, int):void");
    }

    @Deprecated
    public void useMotionAlways(SemMotionEventListener semMotionEventListener, boolean z) {
    }
}
