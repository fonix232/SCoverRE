package com.sec.android.cover.ledcover;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.cover.BaseCoverController;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.ledcover.reflection.cover.RefCoverState;
import com.sec.android.cover.ledcover.reflection.os.RefFactoryTest;
import com.sec.android.cover.lib.sviewcoverservicereflector.SViewCoverServiceReflector;
import com.sec.android.cover.lib.sviewcoverservicereflector.SViewCoverServiceReflector.SViewCoverBaseServiceStubListener;

public class LedCoverService extends Service {
    private static final int MSG_COVER_STATE_UPDATE = 1;
    private static final String NFC_FEATURE = "com.sec.feature.cover.nfc_authentication";
    public static final String NFC_LED_COVER_FEATURE_LEVEL = "SEC_FLOATING_FEATURE_FRAMEWORK_CONFIG_NFC_LED_COVER_LEVEL";
    public static final int NFC_LED_COVER_LEVEL_DREAM = 30;
    public static final int NFC_LED_COVER_LEVEL_GRACE = 20;
    public static final int NFC_LED_COVER_LEVEL_HERO = 10;
    private static final String TAG = LedCoverService.class.getSimpleName();
    private SViewCoverServiceReflector mBinder;
    private BaseCoverController mCoverController;
    private final Object mCoverControllerLock = new Object();
    private final SViewCoverBaseServiceStubListener mCoverStateHandler = new C00211();
    private boolean mFactoryBinary;
    private int mFeatureLevel = 0;
    private LedCoverServiceHandler mHandler;
    private boolean mHasNfc = false;
    private Looper mLooper;

    class C00211 implements SViewCoverBaseServiceStubListener {
        private final String TAG = SViewCoverBaseServiceStubListener.class.getSimpleName();

        C00211() {
        }

        public void updateCoverState(Object state) {
            Log.d(this.TAG, "updateCoverState");
            ScoverState scoverstate = new ScoverState(RefCoverState.get().getSwitchState(state), RefCoverState.get().getType(state), RefCoverState.get().color(state), RefCoverState.get().widthPixel(state), RefCoverState.get().heightPixel(state), RefCoverState.get().attached(state), RefCoverState.get().model(state), RefCoverState.get().isFakeCover(state), RefCoverState.get().fotaMode(state));
            Message msg = LedCoverService.this.mHandler.obtainMessage(1);
            msg.obj = scoverstate;
            LedCoverService.this.mHandler.sendMessage(msg);
        }

        public void onSystemReady() {
            Log.d(this.TAG, "onSystemReady");
        }

        public void onSViewCoverShow() {
            Log.d(this.TAG, "onSViewCoverShow");
            Log.w(this.TAG, "onSViewCoverShow : Unsupported operation");
        }

        public void onSViewCoverHide() {
            Log.d(this.TAG, "onSViewCoverHide");
            Log.w(this.TAG, "onSViewCoverHide : Unsupported operation");
        }

        public int onCoverAppCovered(boolean covered) {
            Log.d(this.TAG, "onSViewCoverHide : covered=" + String.valueOf(covered));
            Log.w(this.TAG, "onSViewCoverHide : Unsupported operation");
            return 0;
        }

        public boolean isCoverViewShowing() {
            Log.d(this.TAG, "isCoverViewShowing");
            Log.w(this.TAG, "isCoverViewShowing : Unsupported operation");
            return false;
        }
    }

    private class LedCoverServiceHandler extends Handler {
        public LedCoverServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LedCoverService.this.handleUpdateCoverState((ScoverState) msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        this.mBinder = new SViewCoverServiceReflector(this.mCoverStateHandler);
        HandlerThread thread = new HandlerThread("LedCoverService");
        thread.start();
        this.mLooper = thread.getLooper();
        this.mHandler = new LedCoverServiceHandler(this.mLooper);
        PackageManager pm = getPackageManager();
        if (pm != null) {
            this.mHasNfc = pm.hasSystemFeature(NFC_FEATURE);
            if (this.mHasNfc) {
                this.mFeatureLevel = SemFloatingFeature.getInstance().getInt(NFC_LED_COVER_FEATURE_LEVEL);
                Log.d(TAG, "Retrieved NFC LED Cover feature level: " + this.mFeatureLevel);
            }
            this.mHasNfc = true;
            Log.d(TAG, "onCreate : mFeatureLevel=" + String.valueOf(this.mFeatureLevel) + "mHasNfc=" + String.valueOf(this.mHasNfc));
        } else {
            Log.e(TAG, "onCreate : cannot access PackageManager");
        }
        this.mFactoryBinary = RefFactoryTest.get().isFactoryBinary();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        synchronized (this.mCoverControllerLock) {
            if (this.mCoverController != null) {
                this.mCoverController.onCoverDetatched(null);
                CoverExecutiveObservator.getInstance(this).stop();
                this.mCoverController = null;
            }
        }
        this.mLooper.quit();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind : intent=" + String.valueOf(intent));
        return (IBinder) this.mBinder.getStubBinder();
    }

    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind : intent=" + String.valueOf(intent));
        return super.onUnbind(intent);
    }

    private void handleUpdateCoverState(ScoverState state) {
        synchronized (this.mCoverControllerLock) {
            handleUpdateCoverStateLocked(state);
        }
    }

    private void handleUpdateCoverStateLocked(ScoverState state) {
        Log.d(TAG, "updateCoverState : state=" + String.valueOf(state));
        Log.d(TAG, "updateCoverState : mHasNfc=" + String.valueOf(this.mHasNfc) + " mFeatureLevel=" + String.valueOf(this.mFeatureLevel));
        if (state.attached || this.mFactoryBinary) {
            String str;
            if (this.mCoverController == null && (state.getType() == 7 || this.mFactoryBinary)) {
                if (this.mHasNfc) {
                    switch (this.mFeatureLevel) {
                        case 10:
                            this.mCoverController = new NfcLedCoverController(this);
                            break;
                        case 20:
                            this.mCoverController = new GracefulNfcLedCoverController(this);
                            break;
                        case 30:
                            this.mCoverController = new DreamyNfcLedCoverController(this);
                            break;
                        default:
                            Log.e(TAG, "updateCoverState : invalid mFeatureLevel " + String.valueOf(this.mFeatureLevel));
                            return;
                    }
                }
                this.mCoverController = new LedCoverController(this);
                this.mCoverController.onCoverAttached(state);
                CoverExecutiveObservator.getInstance(this).start();
            }
            String str2 = TAG;
            if (this.mCoverController == null) {
                str = "null";
            } else {
                str = this.mCoverController.getClass().getSimpleName();
            }
            Log.d(str2, str);
            if (this.mCoverController != null) {
                this.mCoverController.onCoverEvent(state);
            } else {
                Log.w(TAG, "updateCoverState : mCoverController=null");
            }
            CoverExecutiveObservator.getInstance(this).getCoverPlaybackStateMonitor().onCoverStateChanged(state);
        } else if (this.mCoverController != null) {
            this.mCoverController.onCoverDetatched(state);
            CoverExecutiveObservator.getInstance(this).stop();
            this.mCoverController = null;
        }
    }
}
