package com.samsung.android.fingerprint;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.BaseBundle;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.SemSystemProperties;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintIdentifyDialog.FingerprintListener;
import com.samsung.android.fingerprint.IFingerprintClient.Stub;
import com.samsung.android.media.mediacapture.SemMediaCapture;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FingerprintManager implements DeathRecipient {
    public static final String BUNDLE_BACKUP_BUTTON_NAME = "button_name";
    public static final String BUNDLE_DENIED_FINGERPRINT = "denied_fingerprint";
    public static final String BUNDLE_ENABLE_PASSWORD = "password";
    public static final String BUNDLE_ENROLLED_IRIS = "enrolled_iris";
    public static final String BUNDLE_PRIMARY_AUTHORIZATION = "primary_authorization";
    public static final String BUNDLE_STANDBY_STRING = "standby_string";
    public static final String CLIENTSPEC_KEY_ACCURACY = "request_accuracy";
    public static final String CLIENTSPEC_KEY_ALLOW_INDEXES = "request_template_index_list";
    public static final String CLIENTSPEC_KEY_APPNAME = "appName";
    public static final String CLIENTSPEC_KEY_BACKGROUND = "background";
    public static final String CLIENTSPEC_KEY_DEMANDED_PROPERTY_NAME = "propertyName";
    public static final String CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT = "demandExtraEvent";
    public static final String CLIENTSPEC_KEY_OWN_NAME = "ownName";
    public static final String CLIENTSPEC_KEY_PACKAGE_NAME = "packageName";
    public static final String CLIENTSPEC_KEY_PRIVILEGED = "privileged";
    public static final String CLIENTSPEC_KEY_PRIVILEGED_ATTR = "privileged_attr";
    public static final String CLIENTSPEC_KEY_SECURITY_LEVEL = "securityLevel";
    public static final String CLIENTSPEC_KEY_USE_MANUAL_TIMEOUT = "useManualTimeout";
    private static final boolean DEBUG = Debug.semIsProductDev();
    public static final String ENROLL_FINISHED = "com.samsung.android.fingerprint.action.ENROLL_FINISHED";
    private static final String ERROR_MSG_SERVICE_NOT_FOUND = "FingerprintService is not running!";
    public static final String EXTRAS_KEY_TOKEN = "token";
    public static final int FINGER_ALL = 21;
    public static final int FINGER_LEFT_INDEX = 2;
    public static final int FINGER_LEFT_INDEX_2ND = 12;
    public static final int FINGER_LEFT_LITTLE = 5;
    public static final int FINGER_LEFT_LITTLE_2ND = 15;
    public static final int FINGER_LEFT_MIDDLE = 3;
    public static final int FINGER_LEFT_MIDDLE_2ND = 13;
    public static final int FINGER_LEFT_RING = 4;
    public static final int FINGER_LEFT_RING_2ND = 14;
    public static final int FINGER_LEFT_THUMB = 1;
    public static final int FINGER_LEFT_THUMB_2ND = 11;
    public static final int FINGER_NOT_SPECIFIED = 0;
    public static final int FINGER_NUMBER_FOR_ONE = 10;
    public static final String FINGER_PERMISSION_DELIMITER = ",";
    public static final int FINGER_RIGHT_INDEX = 7;
    public static final int FINGER_RIGHT_INDEX_2ND = 17;
    public static final int FINGER_RIGHT_LITTLE = 10;
    public static final int FINGER_RIGHT_LITTLE_2ND = 20;
    public static final int FINGER_RIGHT_MIDDLE = 8;
    public static final int FINGER_RIGHT_MIDDLE_2ND = 18;
    public static final int FINGER_RIGHT_RING = 9;
    public static final int FINGER_RIGHT_RING_2ND = 19;
    public static final int FINGER_RIGHT_THUMB = 6;
    public static final int FINGER_RIGHT_THUMB_2ND = 16;
    public static final int PRIVILEGED_ATTR_EXCLUSIVE_IDENTIFY = 4;
    public static final int PRIVILEGED_ATTR_NO_IDENTIFY_LOCK = 2;
    public static final int PRIVILEGED_ATTR_NO_VIBRATION = 1;
    public static final int PRIVILEGED_TYPE_KEYGUARD = Integer.MIN_VALUE;
    public static final int REQ_CMD_SESSION_OPEN = 1;
    public static final int SECURITY_LEVEL_HIGH = 2;
    public static final int SECURITY_LEVEL_LOW = 0;
    public static final int SECURITY_LEVEL_REGULAR = 1;
    public static final int SECURITY_LEVEL_VERY_HIGH = 3;
    public static final int SENSOR_POSITION_DISPLAY = 2;
    public static final int SENSOR_POSITION_HOMEKEY = 1;
    public static final int SENSOR_POSITION_REAR = 3;
    public static final int SENSOR_TYPE_SWIPE = 1;
    public static final int SENSOR_TYPE_TOUCH = 2;
    public static final String SERVICE_NAME = "fingerprint_service";
    public static final int SERVICE_VERSION = 16973824;
    private static final ComponentName START_ENROLL_ACTIVITY_COMPONENT = new ComponentName("com.samsung.android.fingerprint.service", "com.samsung.android.fingerprint.service.activity.StartEnrollActivity");
    private static final String TAG = "FPMS_FingerprintManager";
    public static final int USE_LAST_QUALITY_FEEDBACK = -1;
    private static Activity mCallerActivity;
    private static Application mCallerApplication;
    private static Context mContext = null;
    private static int mEnrollFinishResult = -1;
    private static EnrollFinishListener mEnrollListener;
    private static IFingerprintClient mFpClient;
    private static FingerprintIdentifyDialog mIdentifyDialog = null;
    private static int mIndex = -1;
    private static boolean mIsAndroidFingerprintSupported = false;
    private static boolean mIsLinkedDeathRecipient = false;
    private static String mOwnName = null;
    private static int mSecurityLevel = 1;
    private static IFingerprintManager mService;
    private static String mStringId = null;
    private static Object mWaitLock = new Object();
    private static android.hardware.fingerprint.FingerprintManager sFingerprintManager;
    private static FingerprintManager sInstance = null;
    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new C00721();
    public FingerprintListener mFingerprintListener = new C00742();
    private final Handler mHandler;
    private IBinder mSpassProcessSdkClientToken = null;

    class C00721 implements ActivityLifecycleCallbacks {
        C00721() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.d(FingerprintManager.TAG, "onActivityCreated");
        }

        public void onActivityDestroyed(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityDestroyed");
            if (FingerprintManager.mCallerActivity != null && FingerprintManager.mCallerActivity.equals(activity)) {
                FingerprintManager.this.unregisterActivityLifeCallback();
            }
        }

        public void onActivityPaused(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityPaused");
            if (FingerprintManager.mCallerActivity != null && FingerprintManager.mCallerActivity.equals(activity)) {
                FingerprintManager.this.notifyAppActivityState(1, null);
                FingerprintManager.this.unregisterActivityLifeCallback();
            }
        }

        public void onActivityResumed(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityResumed");
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            Log.d(FingerprintManager.TAG, "onActivitySaveInstanceState");
        }

        public void onActivityStarted(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityStarted");
        }

        public void onActivityStopped(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityStopped");
            if (FingerprintManager.mCallerActivity != null && FingerprintManager.mCallerActivity.equals(activity)) {
                FingerprintManager.this.notifyAppActivityState(2, null);
                FingerprintManager.this.unregisterActivityLifeCallback();
            }
        }
    }

    class C00742 implements FingerprintListener {
        C00742() {
        }

        public void onEvent(final FingerprintEvent fingerprintEvent) {
            FingerprintEvent fingerprintEvent2 = fingerprintEvent;
            try {
                if (FingerprintManager.this.mHandler != null) {
                    FingerprintManager.this.mHandler.post(new Runnable() {
                        public void run() {
                            switch (fingerprintEvent.eventId) {
                                case 13:
                                    if (fingerprintEvent.eventResult == 0) {
                                        FingerprintManager.this.startSettingEnrollActivity(FingerprintManager.mContext, FingerprintManager.mEnrollListener, FingerprintManager.mStringId, FingerprintManager.mIndex);
                                        return;
                                    } else if (fingerprintEvent.eventResult == -1) {
                                        switch (fingerprintEvent.eventStatus) {
                                            case 4:
                                            case 7:
                                            case 11:
                                            case 51:
                                                FingerprintManager.this.setEnrollFinishResult(1);
                                                FingerprintManager.mEnrollListener.onEnrollFinish();
                                                break;
                                            case 8:
                                                FingerprintManager.this.setEnrollFinishResult(0);
                                                FingerprintManager.mEnrollListener.onEnrollFinish();
                                                break;
                                        }
                                        FingerprintManager.this.notifyEnrollEnd();
                                        return;
                                    } else {
                                        return;
                                    }
                                default:
                                    return;
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log.w(FingerprintManager.TAG, "onFingerprintEvent: Error : " + e);
            }
        }
    }

    class C00753 implements Runnable {
        C00753() {
        }

        public void run() {
            FingerprintEvent fingerprintEvent = new FingerprintEvent((int) FingerprintEvent.EVENT_SERVICE_DIED);
            try {
                if (FingerprintManager.mFpClient != null) {
                    FingerprintManager.mFpClient.onFingerprintEvent(fingerprintEvent);
                }
            } catch (Throwable e) {
                Log.e(FingerprintManager.TAG, "binderDied: failed to call onFingerprintEvent", e);
            }
        }
    }

    class C00774 extends Stub {
        C00774() {
        }

        public void onFingerprintEvent(final FingerprintEvent fingerprintEvent) throws RemoteException {
            FingerprintEvent fingerprintEvent2 = fingerprintEvent;
            try {
                if (FingerprintManager.this.mHandler != null) {
                    FingerprintManager.this.mHandler.post(new Runnable() {
                        public void run() {
                            switch (fingerprintEvent.eventId) {
                                case 13:
                                    if (fingerprintEvent.eventResult == 0) {
                                        FingerprintManager.this.startSettingEnrollActivity(FingerprintManager.mContext, FingerprintManager.mEnrollListener, FingerprintManager.mStringId, FingerprintManager.mIndex);
                                        return;
                                    } else if (fingerprintEvent.eventResult == -1) {
                                        switch (fingerprintEvent.eventStatus) {
                                            case 4:
                                            case 7:
                                            case 11:
                                            case 51:
                                                FingerprintManager.this.setEnrollFinishResult(1);
                                                FingerprintManager.mEnrollListener.onEnrollFinish();
                                                break;
                                            case 8:
                                                FingerprintManager.this.setEnrollFinishResult(0);
                                                FingerprintManager.mEnrollListener.onEnrollFinish();
                                                break;
                                        }
                                        FingerprintManager.this.notifyEnrollEnd();
                                        return;
                                    } else {
                                        return;
                                    }
                                default:
                                    return;
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log.w(FingerprintManager.TAG, "onFingerprintEvent: Error : " + e);
            }
        }
    }

    class EnrollFinishBroadcastReceiver extends BroadcastReceiver {
        private String mId;
        private EnrollFinishListener mListener;

        EnrollFinishBroadcastReceiver(EnrollFinishListener enrollFinishListener, String str) {
            this.mListener = enrollFinishListener;
            this.mId = str;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String stringExtra = intent.getStringExtra("previousStage");
                int intExtra = intent.getIntExtra("enrollResult", 1);
                Log.d(FingerprintManager.TAG, "EnrollFinishBroadcastReceiver onReceive: resultCode=" + intExtra);
                if (stringExtra != null) {
                    Log.d(FingerprintManager.TAG, "previousStage : " + stringExtra);
                    if (stringExtra.equals(this.mId)) {
                        if (FingerprintManager.mOwnName == null || FingerprintManager.mOwnName.length() <= 0) {
                            FingerprintManager.this.notifyEnrollEnd();
                        }
                        FingerprintManager.this.setEnrollFinishResult(intExtra);
                        this.mListener.onEnrollFinish();
                        try {
                            context.unregisterReceiver(this);
                            return;
                        } catch (Exception e) {
                            FingerprintManager.this.logExceptionInDetail("onReceive", e, "Receiver isn't registered");
                            return;
                        }
                    }
                    return;
                }
                Log.e(FingerprintManager.TAG, "ID is not given. Cannot recognize this broadcast.");
            }
        }
    }

    public interface EnrollFinishListener {
        void onEnrollFinish();
    }

    public static class FingerprintClientSpecBuilder {
        private Bundle mBundle = new Bundle();

        public FingerprintClientSpecBuilder(String str) {
            if (str != null && str.length() > 0) {
                this.mBundle.putString(FingerprintManager.CLIENTSPEC_KEY_APPNAME, str);
            }
        }

        public Bundle build() {
            return this.mBundle;
        }

        public FingerprintClientSpecBuilder demandExtraEvent(boolean z) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, z);
            return this;
        }

        @Deprecated
        public FingerprintClientSpecBuilder demandFingerRemovedEvent(boolean z) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, z);
            return this;
        }

        @Deprecated
        public FingerprintClientSpecBuilder demandGestureEvent(boolean z) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, z);
            return this;
        }

        @Deprecated
        public FingerprintClientSpecBuilder demandNavigationEvent(boolean z) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, z);
            return this;
        }

        public FingerprintClientSpecBuilder setAccuracy(float f) {
            this.mBundle.putFloat(FingerprintManager.CLIENTSPEC_KEY_ACCURACY, f);
            return this;
        }

        public FingerprintClientSpecBuilder setAllowFingers(int[] iArr) {
            if (iArr != null && iArr.length > 0) {
                this.mBundle.putIntArray(FingerprintManager.CLIENTSPEC_KEY_ALLOW_INDEXES, iArr);
            }
            return this;
        }

        public FingerprintClientSpecBuilder setBackground(boolean z) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_BACKGROUND, z);
            return this;
        }

        public FingerprintClientSpecBuilder setExtraSpec(Bundle bundle) {
            this.mBundle.putAll(bundle);
            return this;
        }

        public FingerprintClientSpecBuilder setOwnName(String str) {
            if (str != null && str.length() > 0) {
                this.mBundle.putString(FingerprintManager.CLIENTSPEC_KEY_OWN_NAME, str);
            }
            return this;
        }

        public FingerprintClientSpecBuilder setPrivilegedAttr(int i) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_PRIVILEGED, true);
            this.mBundle.putInt(FingerprintManager.CLIENTSPEC_KEY_PRIVILEGED_ATTR, i);
            return this;
        }

        public FingerprintClientSpecBuilder setSecurityLevel(int i) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                    this.mBundle.putInt(FingerprintManager.CLIENTSPEC_KEY_SECURITY_LEVEL, i);
                    break;
                default:
                    this.mBundle.putInt(FingerprintManager.CLIENTSPEC_KEY_SECURITY_LEVEL, 1);
                    break;
            }
            return this;
        }

        public FingerprintClientSpecBuilder useManualTimeout(boolean z) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_USE_MANUAL_TIMEOUT, z);
            return this;
        }
    }

    private FingerprintManager(Context context) {
        this.mHandler = new Handler(context.getMainLooper());
        if (getSensorType() == 2) {
            mIsAndroidFingerprintSupported = true;
        }
    }

    private static synchronized void ensureServiceConnected() {
        synchronized (FingerprintManager.class) {
            if (mService == null) {
                mService = IFingerprintManager.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
            }
            if (mService == null) {
                Log.w(TAG, "ensureServiceConnected: mService is null");
                startFingerprintManagerService();
                if (waitForService() && !mIsLinkedDeathRecipient) {
                    try {
                        mService.asBinder().linkToDeath(sInstance, 0);
                        mIsLinkedDeathRecipient = true;
                    } catch (RemoteException e) {
                        Log.e(TAG, "ensureServiceConnected:" + e);
                    }
                }
            } else {
                try {
                    mService.getVersion();
                } catch (Throwable e2) {
                    if (e2 instanceof DeadObjectException) {
                        Log.i(TAG, "===DeadObjectException===");
                        startFingerprintManagerService();
                        if (waitForService() && !mIsLinkedDeathRecipient) {
                            try {
                                mService.asBinder().linkToDeath(sInstance, 0);
                                mIsLinkedDeathRecipient = true;
                                Log.d(TAG, "ensureServiceConnected: linkToDeath");
                            } catch (RemoteException e3) {
                                Log.d(TAG, "ensureServiceConnected:" + e3);
                            }
                        }
                    } else {
                        Log.e(TAG, "ensureServiceConnected", e2);
                    }
                } catch (Throwable e4) {
                    Log.e(TAG, "ensureServiceConnected", e4);
                }
            }
        }
    }

    public static byte[] generateHash(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(str.getBytes("iso-8859-1"));
            return instance.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "generateHash:" + e);
            return null;
        } catch (UnsupportedEncodingException e2) {
            Log.e(TAG, "generateHash:" + e2);
            return null;
        }
    }

    public static AnimationDrawable getImageQualityAnimation(int i, Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        Resources resources = null;
        int i2 = 0;
        AnimationDrawable animationDrawable = null;
        try {
            resources = context.getPackageManager().getResourcesForApplication("com.samsung.android.fingerprint.service");
        } catch (Throwable e) {
            Log.e(TAG, "getImageQualityAnimation, NameNotFoundException", e);
        }
        if (resources == null) {
            Log.e(TAG, "mRes is null");
            return null;
        }
        if (i == -1) {
            try {
                i = mService.getLastImageQuality(context.getPackageName());
            } catch (Throwable e2) {
                Log.e(TAG, "getImageQualityAnimation: failed to call getLastImageQuality", e2);
            }
        }
        if (getSensorType() != 1) {
            if (getSensorType() == 2) {
                switch (i) {
                    case 0:
                        i2 = resources.getIdentifier("spass_touch_errimage_nomatch", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 2:
                    case 65536:
                    case 524288:
                        i2 = resources.getIdentifier("spass_touch_errimage_too_fast", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 512:
                        i2 = resources.getIdentifier("spass_touch_errimage_something_on_the_sensor", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 4096:
                    case 131072:
                    case 262144:
                    case FingerprintEvent.IMAGE_QUALITY_PATAIL_TOUCH /*1610612736*/:
                        i2 = resources.getIdentifier("spass_touch_errimage_whole", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case FingerprintEvent.IMAGE_QUALITY_WET_FINGER /*16777216*/:
                        i2 = resources.getIdentifier("spass_touch_errimage_wet", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case FingerprintEvent.IMAGE_QUALITY_SAME_AS_PREVIOUS /*805306368*/:
                        i2 = resources.getIdentifier("spass_touch_errimage_position", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    default:
                        i2 = resources.getIdentifier("spass_touch_errimage_default", "anim", "com.samsung.android.fingerprint.service");
                        break;
                }
            }
        }
        switch (i) {
            case 0:
                i2 = resources.getIdentifier("spass_errimage_nomatch", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 2:
            case 16:
                i2 = resources.getIdentifier("spass_errimage_speed", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 3:
                i2 = resources.getIdentifier("spass_errimage_reverse", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 4:
                i2 = resources.getIdentifier("spass_errimage_short", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 512:
                i2 = resources.getIdentifier("spass_errimage_homekey", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 32768:
                i2 = resources.getIdentifier("spass_errimage_diagonal", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 131072:
                i2 = resources.getIdentifier("spass_errimage_left", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 262144:
                i2 = resources.getIdentifier("spass_errimage_right", "anim", "com.samsung.android.fingerprint.service");
                break;
            case FingerprintEvent.IMAGE_QUALITY_WET_FINGER /*16777216*/:
                i2 = resources.getIdentifier("spass_errimage_wet", "anim", "com.samsung.android.fingerprint.service");
                break;
            case FingerprintEvent.IMAGE_QUALITY_SAME_AS_PREVIOUS /*805306368*/:
                i2 = resources.getIdentifier("spass_errimage_same", "anim", "com.samsung.android.fingerprint.service");
                break;
            default:
                i2 = resources.getIdentifier("spass_errimage_default", "anim", "com.samsung.android.fingerprint.service");
                break;
        }
        if (i2 != 0) {
            try {
                animationDrawable = (AnimationDrawable) resources.getDrawable(i2);
            } catch (Throwable e3) {
                Log.e(TAG, "getImageQualityAnimation : failed", e3);
            }
        }
        return animationDrawable;
    }

    public static int getImageQualityFeedback(int i) {
        return 0;
    }

    public static String getImageQualityFeedbackString(int i, Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null.");
        }
        Resources resources = null;
        String str = null;
        try {
            resources = context.getPackageManager().getResourcesForApplication("com.samsung.android.fingerprint.service");
        } catch (Throwable e) {
            Log.e(TAG, "getImageQualityFeedbackString, NameNotFoundException", e);
        }
        if (resources == null) {
            Log.e(TAG, "mRes is null");
            return null;
        }
        int identifier;
        switch (i) {
            case 0:
                if (!"VZW".equals(SemSystemProperties.getSalesCode())) {
                    identifier = resources.getIdentifier("recognize_fail", "string", "com.samsung.android.fingerprint.service");
                    break;
                }
                identifier = resources.getIdentifier("recognize_fail_verizon", "string", "com.samsung.android.fingerprint.service");
                break;
            case 2:
                identifier = resources.getIdentifier("spass_status_too_fast", "string", "com.samsung.android.fingerprint.service");
                break;
            case 512:
                identifier = resources.getIdentifier("spass_something_on_sensor", "string", "com.samsung.android.fingerprint.service");
                break;
            case 4096:
            case 131072:
            case 262144:
                identifier = resources.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service");
                break;
            case 8192:
                identifier = resources.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service");
                break;
            case 65536:
                identifier = resources.getIdentifier("touch_image_quality_pressure_too_light", "string", "com.samsung.android.fingerprint.service");
                break;
            case 524288:
                identifier = resources.getIdentifier("touch_image_quality_pressure_too_hard", "string", "com.samsung.android.fingerprint.service");
                break;
            case FingerprintEvent.IMAGE_QUALITY_WET_FINGER /*16777216*/:
                identifier = resources.getIdentifier("spass_image_quality_wet_finger", "string", "com.samsung.android.fingerprint.service");
                break;
            case FingerprintEvent.IMAGE_QUALITY_SAME_AS_PREVIOUS /*805306368*/:
                identifier = resources.getIdentifier("touch_image_quality_same_as_previous", "string", "com.samsung.android.fingerprint.service");
                break;
            case 1073741824:
                identifier = resources.getIdentifier("spass_image_quality_extraction_failure", "string", "com.samsung.android.fingerprint.service");
                break;
            default:
                identifier = resources.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service");
                break;
        }
        if (identifier != 0) {
            try {
                str = resources.getString(identifier);
            } catch (Throwable e2) {
                Log.e(TAG, "getImageQualityFeedbackString : failed", e2);
            }
        }
        return str;
    }

    public static int getImageQualityIcon(int i) {
        return 0;
    }

    public static synchronized FingerprintManager getInstance(Context context) {
        FingerprintManager instance;
        synchronized (FingerprintManager.class) {
            instance = getInstance(context, 2, null);
        }
        return instance;
    }

    public static synchronized FingerprintManager getInstance(Context context, int i) {
        FingerprintManager instance;
        synchronized (FingerprintManager.class) {
            instance = getInstance(context, i, null);
        }
        return instance;
    }

    public static synchronized FingerprintManager getInstance(Context context, int i, String str) {
        FingerprintManager fingerprintManager;
        synchronized (FingerprintManager.class) {
            if (context == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            mContext = context;
            mSecurityLevel = i;
            if (sInstance == null) {
                sInstance = new FingerprintManager(context);
                if (mIsAndroidFingerprintSupported) {
                    sFingerprintManager = (android.hardware.fingerprint.FingerprintManager) mContext.getSystemService("fingerprint");
                }
            }
            if (str != null) {
                mOwnName = str;
            }
            if (!mIsAndroidFingerprintSupported) {
                ensureServiceConnected();
            }
            fingerprintManager = sInstance;
        }
        return fingerprintManager;
    }

    public static synchronized FingerprintManager getInstance(Context context, String str) {
        FingerprintManager instance;
        synchronized (FingerprintManager.class) {
            instance = getInstance(context, 2, str);
        }
        return instance;
    }

    public static int getSensorPosition() {
        return "google_touch_rear,navi=1".contains("touch_display") ? 2 : "google_touch_rear,navi=1".contains("touch_rear") ? 3 : 1;
    }

    public static int getSensorType() {
        return "google_touch_rear,navi=1".contains("touch") ? 2 : 1;
    }

    private boolean isSpassProcessSDK(String str) {
        return str != null && "com.samsung.android.sdk.pass.process".equals(str);
    }

    private void logExceptionInDetail(String str, Exception exception) {
        logExceptionInDetail(str, exception, null);
    }

    private void logExceptionInDetail(String str, Exception exception, String str2) {
        Log.e(TAG, str + ": failed " + (str2 == null ? "" : "- " + str2), exception);
    }

    private void registerActivityLifeCallback() {
        if (mCallerApplication != null) {
            Log.d(TAG, "registerActivityLifeCallback");
            mCallerApplication.registerActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
        }
    }

    private static void startFingerprintManagerService() {
        try {
            if (!mIsAndroidFingerprintSupported) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.samsung.android.fingerprint.service", "com.samsung.android.fingerprint.service.FingerprintServiceStarter"));
                intent.setAction("com.samsung.android.fingerprint.action.START_SERVICE");
                if (DEBUG) {
                    Log.d(TAG, "Starting service: " + intent);
                }
                if (UserHandle.myUserId() >= 100) {
                    mContext.startServiceAsUser(intent, UserHandle.SYSTEM);
                } else {
                    mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
                }
            } else if (sFingerprintManager == null) {
                Log.e(TAG, ERROR_MSG_SERVICE_NOT_FOUND);
            } else {
                sFingerprintManager.request(SemMediaCapture.KEY_PARAMETER_HEIGHT, null, null, 0, null);
            }
        } catch (Throwable e) {
            Log.e(TAG, "startFingerprintManagerService : failed to start service", e);
        }
    }

    private void unregisterActivityLifeCallback() {
        if (mCallerApplication != null) {
            Log.d(TAG, "unregisterActivityLifeCallback");
            mCallerApplication.unregisterActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
            mCallerActivity = null;
            mCallerApplication = null;
        }
    }

    private static boolean waitForService() {
        long elapsedRealtime = SystemClock.elapsedRealtime() + 2000;
        while (true) {
            synchronized (mWaitLock) {
                mService = IFingerprintManager.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
                if (mService != null) {
                    Log.i(TAG, "waitForService: FPMS started");
                    return true;
                } else if (SystemClock.elapsedRealtime() >= elapsedRealtime) {
                    Log.e(TAG, "waitForService: Timeout");
                    return false;
                } else {
                    try {
                        mWaitLock.wait(300);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void binderDied() {
        Log.e(TAG, "binderDied called");
        mService = null;
        mIsLinkedDeathRecipient = false;
        if (mFpClient != null) {
            Log.i(TAG, "binderDied: Client is not null");
            if (this.mHandler != null) {
                this.mHandler.post(new C00753());
            }
        }
    }

    public boolean cancel(IBinder iBinder) {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("cancel", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            if (mService.cancel(iBinder) == 0) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            logExceptionInDetail("cancel", e, "token=" + iBinder);
            return false;
        }
    }

    public boolean closeTransaction(IBinder iBinder) {
        return true;
    }

    public int enroll(IBinder iBinder, String str, int i) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("enroll", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return -1;
            }
            try {
                return mService.enroll(iBinder, str, i);
            } catch (Exception e) {
                logExceptionInDetail("enroll", e, "token=" + iBinder + ", permissionName=" + str + ", fingerIndex=" + i);
            }
        }
        return -1;
    }

    public String getDaemonVersion() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("getDaemonVersion", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            try {
                return mService.getDaemonVersion();
            } catch (Exception e) {
                logExceptionInDetail("getDaemonVersion", e);
                return null;
            }
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("getDaemonVersion", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else {
            byte[] requestGetVersion = sFingerprintManager.requestGetVersion();
            return (requestGetVersion == null || requestGetVersion.length <= 0) ? null : new String(requestGetVersion);
        }
    }

    public int getEnrollFinishResult() {
        return mEnrollFinishResult;
    }

    public int getEnrollRepeatCount() {
        if (mIsAndroidFingerprintSupported) {
            return -1;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getFingerprintId", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return 0;
        }
        try {
            return mService.getEnrollRepeatCount();
        } catch (Exception e) {
            logExceptionInDetail("getFingerprintId", e);
            return 0;
        }
    }

    public int getEnrolledFingers() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("getEnrolledFingers", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return 0;
            }
            try {
                return mService.getEnrolledFingers(mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("getEnrolledFingers", e);
            }
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("getFingerprintId", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return 0;
        } else {
            try {
                byte[] bArr = new byte[10];
                int request = sFingerprintManager.request(1003, null, bArr, 0, null);
                if (request > 0) {
                    int i = 0;
                    for (int i2 = 0; i2 < request; i2++) {
                        i |= 1 << bArr[i2];
                    }
                    if (DEBUG) {
                        Log.i(TAG, "getEnrolledFingers : " + i + ", fingers = " + Integer.toBinaryString(i));
                    } else {
                        Log.i(TAG, "getEnrolledFingers : " + i);
                    }
                    return i;
                }
            } catch (Exception e2) {
                logExceptionInDetail("getEnrolledFingers", e2);
            }
        }
        Log.i(TAG, "getEnrolledFingers : 0");
        return 0;
    }

    public String getFingerprintId(int i) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("getFingerprintId", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            try {
                return mService.getFingerprintIdByFinger(i, mOwnName, mContext.getPackageName());
            } catch (Exception e) {
                logExceptionInDetail("getFingerprintId", e);
            }
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("getFingerprintId", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else {
            try {
                byte[] requestGetUniqueID = sFingerprintManager.requestGetUniqueID(i, mContext.getOpPackageName());
                if (requestGetUniqueID != null) {
                    StringBuilder stringBuilder = new StringBuilder(requestGetUniqueID.length * 2);
                    int length = requestGetUniqueID.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(requestGetUniqueID[i2] & 255)}));
                    }
                    return stringBuilder.toString();
                }
            } catch (Exception e2) {
                logExceptionInDetail("getFingerprintId", e2);
            }
        }
        return null;
    }

    public String[] getFingerprintIds() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("getFingerprintIds", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            try {
                return mService.getFingerprintIds(mOwnName, mContext.getPackageName());
            } catch (Exception e) {
                logExceptionInDetail("getFingerprintIds", e);
            }
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("getFingerprintId", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else {
            try {
                byte[] bArr = new byte[10];
                int request = sFingerprintManager.request(1003, null, bArr, 0, null);
                if (request > 0) {
                    String[] strArr = new String[request];
                    for (int i = 0; i < request; i++) {
                        strArr[i] = getFingerprintId(bArr[i]);
                    }
                    return strArr;
                }
            } catch (Exception e2) {
                logExceptionInDetail("getFingerprintIds", e2);
            }
        }
        return null;
    }

    public String getIndexName(int i) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getIndexName", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        }
        try {
            return mService.getIndexName(i);
        } catch (Exception e) {
            logExceptionInDetail("getIndexName", e);
            return null;
        }
    }

    public int getLastImageQuality(Context context) {
        int i = 0;
        if (!mIsAndroidFingerprintSupported) {
            if (mService == null) {
                logExceptionInDetail("getLastImageQuality", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return 0;
            } else if (context == null) {
                throw new IllegalArgumentException("context is null.");
            } else {
                try {
                    i = getImageQualityIcon(mService.getLastImageQuality(context.getPackageName()));
                } catch (Exception e) {
                    logExceptionInDetail("getQualityMessage", e);
                }
                Log.i(TAG, "getLastImageQuality: return " + i);
            }
        }
        return i;
    }

    public String getLastImageQualityMessage(Context context) {
        if (!mIsAndroidFingerprintSupported) {
            if (mService == null) {
                logExceptionInDetail("getLastImageQualityMessage", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            } else if (context == null) {
                return null;
            } else {
                try {
                    return mService.getLastImageQualityMessage(context.getPackageName());
                } catch (Exception e) {
                    logExceptionInDetail("getLastImageQualityMessage", e);
                }
            }
        }
        return null;
    }

    public String getSensorInfo() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("getSensorInfo", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            try {
                return mService.getSensorInfo();
            } catch (Exception e) {
                logExceptionInDetail("getSensorInfo", e);
                return null;
            }
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("getSensorInfo", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else {
            byte[] requestGetSensorInfo = sFingerprintManager.requestGetSensorInfo();
            return (requestGetSensorInfo == null || requestGetSensorInfo.length <= 0) ? null : new String(requestGetSensorInfo);
        }
    }

    public String[] getUserIdList() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("getUserIdList", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            try {
                return mService.getUserIdList();
            } catch (Exception e) {
                logExceptionInDetail("getUserIdList", e);
                return null;
            }
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("getUserIdList", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else {
            try {
                return sFingerprintManager.requestGetUserIDs();
            } catch (Exception e2) {
                logExceptionInDetail("getUserIdList", e2);
                return null;
            }
        }
    }

    public int getVersion() {
        if (mIsAndroidFingerprintSupported) {
            return SERVICE_VERSION;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getVersion", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return 0;
        }
        try {
            return mService.getVersion();
        } catch (Exception e) {
            logExceptionInDetail("getVersion", e);
            return 0;
        }
    }

    public boolean hasPendingCommand() {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("hasPendingCommand", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            return mService.hasPendingCommand();
        } catch (Exception e) {
            logExceptionInDetail("hasPendingCommand", e);
            return false;
        }
    }

    public int identify(IBinder iBinder, String str) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identify", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return -1;
        }
        try {
            return mService.identify(iBinder, str);
        } catch (Exception e) {
            logExceptionInDetail("identify", e, "token=" + iBinder);
            return -1;
        }
    }

    public int identifyForMultiUser(IBinder iBinder, int i, String str) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identifyForMultiUser", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return -1;
        }
        try {
            return mService.identifyForMultiUser(iBinder, i, str);
        } catch (Exception e) {
            logExceptionInDetail("identifyForMultiUser", e, "token=" + iBinder + ", userId=" + i);
            return -1;
        }
    }

    public int identifyForMultiUser(IBinder iBinder, String str) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identifyForMultiUser", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return -1;
        }
        try {
            return mService.identifyForMultiUser(iBinder, -1, str);
        } catch (Exception e) {
            logExceptionInDetail("identifyForMultiUser", e, "token=" + iBinder);
            return -1;
        }
    }

    public int identifyWithDialog(Context context, IFingerprintClient iFingerprintClient, Bundle bundle) {
        int i = -1;
        if (context == null) {
            logExceptionInDetail("identifyWithDialog", null, "Context is null");
            return -1;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identifyWithDialog", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return -1;
        }
        ComponentName componentName = null;
        if (context instanceof Activity) {
            unregisterActivityLifeCallback();
            mCallerActivity = context;
            mCallerApplication = context.getApplication();
            registerActivityLifeCallback();
            componentName = context.getComponentName();
        } else {
            mCallerApplication = null;
            mCallerActivity = null;
            Log.w(TAG, "identifyWithDialog : client is not Activity");
        }
        try {
            i = mService.identifyWithDialog(context.getPackageName(), componentName, iFingerprintClient, bundle);
            if (i != 0) {
                unregisterActivityLifeCallback();
            } else {
                mFpClient = iFingerprintClient;
            }
        } catch (Exception e) {
            unregisterActivityLifeCallback();
            logExceptionInDetail("identifyWithDialog", e);
        }
        return i;
    }

    public boolean isEnrolling() {
        if (mIsAndroidFingerprintSupported) {
            try {
                if (sFingerprintManager != null) {
                    return sFingerprintManager.isEnrollSession();
                }
                logExceptionInDetail("isEnrolling", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            } catch (Exception e) {
                logExceptionInDetail("isEnrolling", e);
                return false;
            }
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("isEnrolling", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            return mService.isEnrollSession();
        } catch (Throwable e2) {
            Log.e(TAG, "isEnrolling: failed ", e2);
            return false;
        }
    }

    public boolean isSensorReady() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("isSensorReady", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.isSensorReady();
            } catch (Exception e) {
                logExceptionInDetail("isSensorReady", e);
            }
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("isSensorReady", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        } else {
            try {
                int requestGetSensorStatus = sFingerprintManager.requestGetSensorStatus();
                if (DEBUG) {
                    Log.d(TAG, "isSensorReady: status=" + requestGetSensorStatus);
                }
                if (requestGetSensorStatus == 100040 || requestGetSensorStatus == 100041) {
                    return true;
                }
            } catch (Exception e2) {
                logExceptionInDetail("isSensorReady", e2);
            }
        }
        return false;
    }

    public boolean isSupportBackupPassword() {
        return !mIsAndroidFingerprintSupported;
    }

    public boolean isSupportFingerprintIds() {
        String str = Build.DEVICE;
        return (str.startsWith("klte") || str.startsWith("k3g") || str.startsWith("kmini") || str.startsWith("chagall") || str.startsWith("klimt") || str.startsWith("slte") || str.startsWith("lentislte") || str.startsWith("kccat6") || str.startsWith("hestialte")) ? false : true;
    }

    public boolean isVZWPermissionGranted() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("isVZWPermissionGranted", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.isVZWPermissionGranted(mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("isVZWPermissionGranted", e);
            }
        }
        return false;
    }

    public void notifyAlternativePasswordBegin() {
    }

    public void notifyAppActivityState(int i, Bundle bundle) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("notifyAppActivityState", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return;
        }
        try {
            mService.notifyApplicationState(i, bundle);
        } catch (Exception e) {
            logExceptionInDetail("notifyAppActivityState", e);
        }
    }

    public boolean notifyEnrollBegin() {
        if (mIsAndroidFingerprintSupported) {
            return true;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("notifyEnrollBegin", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            return mService.notifyEnrollBegin();
        } catch (Exception e) {
            logExceptionInDetail("notifyEnrollBegin", e);
            return false;
        }
    }

    public boolean notifyEnrollEnd() {
        if (mIsAndroidFingerprintSupported) {
            return true;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("notifyEnrollEnd", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            return mService.notifyEnrollEnd();
        } catch (Exception e) {
            logExceptionInDetail("notifyEnrollEnd", e);
            return false;
        }
    }

    public boolean openTransaction(IBinder iBinder) {
        return true;
    }

    public boolean pauseEnroll() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("pauseEnroll", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.pauseEnroll();
            } catch (Exception e) {
                logExceptionInDetail("pauseEnroll", e);
            }
        }
        return false;
    }

    public byte[] process(IBinder iBinder, String str, byte[] bArr) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("process", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            if (bArr != null) {
                try {
                    if (!(bArr.length == 0 || iBinder == null)) {
                        return mService.process(iBinder, str, bArr);
                    }
                } catch (Exception e) {
                    logExceptionInDetail("process", e, "token=" + iBinder);
                    return null;
                }
            }
            logExceptionInDetail("process", null, "Invaild params");
            return null;
        } else if (sFingerprintManager == null) {
            logExceptionInDetail("process", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        } else if (str == null || bArr == null || bArr.length == 0) {
            try {
                Log.e(TAG, "process : invalid param");
                return null;
            } catch (Exception e2) {
                logExceptionInDetail("process : ", e2);
                return null;
            }
        } else {
            int length = str.length();
            int length2 = (length + 8) + bArr.length;
            byte[] bArr2 = new byte[((length + 8) + bArr.length)];
            bArr2[0] = (byte) 83;
            bArr2[1] = (byte) 1;
            bArr2[2] = (byte) length2;
            bArr2[3] = (byte) (length2 >> 8);
            bArr2[4] = (byte) 83;
            bArr2[5] = (byte) 2;
            bArr2[6] = (byte) length;
            bArr2[7] = (byte) (length >> 8);
            System.arraycopy(str.getBytes(), 0, bArr2, 8, length);
            System.arraycopy(bArr, 0, bArr2, length + 8, bArr.length);
            return sFingerprintManager.requestProcessFIDO(bArr2);
        }
    }

    public byte[] processFIDO(Context context, IBinder iBinder, String str, byte[] bArr) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("processFIDO", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            if (bArr != null) {
                try {
                    if (!(bArr.length == 0 || context == null)) {
                        return mService.processFIDO(iBinder, null, str, bArr);
                    }
                } catch (Exception e) {
                    logExceptionInDetail("processFIDO", e, "token=" + iBinder + ", permissionName=" + str);
                    return null;
                }
            }
            return null;
        } else if (sFingerprintManager != null) {
            return process(iBinder, "fp_asm", bArr);
        } else {
            logExceptionInDetail("process", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        }
    }

    public IBinder registerClient(IFingerprintClient iFingerprintClient, Bundle bundle) {
        boolean isSpassProcessSDK = bundle == null ? false : isSpassProcessSDK(bundle.getString(CLIENTSPEC_KEY_APPNAME, ""));
        if (mIsAndroidFingerprintSupported && isSpassProcessSDK) {
            this.mSpassProcessSdkClientToken = iFingerprintClient.asBinder();
            return this.mSpassProcessSdkClientToken;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("registerClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        }
        mFpClient = null;
        if (!mIsAndroidFingerprintSupported && bundle.getInt(CLIENTSPEC_KEY_SECURITY_LEVEL, -1) == -1) {
            bundle.putInt(CLIENTSPEC_KEY_SECURITY_LEVEL, mSecurityLevel);
        }
        bundle.putString(CLIENTSPEC_KEY_PACKAGE_NAME, mContext.getPackageName());
        try {
            IBinder registerClient = mService.registerClient(iFingerprintClient, bundle);
            if (!(registerClient == null || isSpassProcessSDK)) {
                mFpClient = iFingerprintClient;
            }
            return registerClient;
        } catch (Exception e) {
            logExceptionInDetail("registerClient", e, "client=" + iFingerprintClient + ", clientSpec=" + bundle);
            return null;
        }
    }

    @Deprecated
    public IBinder registerClient(IFingerprintClient iFingerprintClient, FingerprintClientSpecBuilder fingerprintClientSpecBuilder) {
        return registerClient(iFingerprintClient, fingerprintClientSpecBuilder.build());
    }

    public boolean removeAllEnrolledFingers() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("removeAllEnrolledFingers", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.removeAllEnrolledFingers(mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("removeAllEnrolledFingers", e);
            }
        }
        return false;
    }

    public boolean removeEnrolledFinger(int i) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("removeEnrolledFinger", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.removeEnrolledFinger(i, mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("removeEnrolledFinger", e);
            }
        }
        return false;
    }

    public int request(int i, int i2) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("request", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return -1;
            }
            try {
                return mService.request(i, i2);
            } catch (Exception e) {
                logExceptionInDetail("request", e);
            }
        }
        return -1;
    }

    public boolean resumeEnroll() {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("resumeEnroll", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.resumeEnroll();
            } catch (Exception e) {
                logExceptionInDetail("resumeEnroll", e);
            }
        }
        return false;
    }

    public void setEnrollFinishResult(int i) {
        mEnrollFinishResult = i;
    }

    public boolean setIndexName(int i, String str) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("setIndexName", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            if (mOwnName == null || mOwnName.length() <= 0) {
                return mService.setIndexName(i, str);
            }
        } catch (Exception e) {
            logExceptionInDetail("setIndexName", e);
        }
        return false;
    }

    public boolean setPassword(String str) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("setPassword", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.setPassword(str, mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("setPassword", e);
            }
        }
        return false;
    }

    public Dialog showIdentifyDialog(Context context, FingerprintListener fingerprintListener, String str, boolean z) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("showIdentifyDialog", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        }
        ComponentName componentName = null;
        if (context instanceof Activity) {
            unregisterActivityLifeCallback();
            mCallerActivity = context;
            mCallerApplication = context.getApplication();
            registerActivityLifeCallback();
            componentName = context.getComponentName();
        } else {
            mCallerApplication = null;
            mCallerActivity = null;
        }
        mIdentifyDialog = new FingerprintIdentifyDialog(context, fingerprintListener, mSecurityLevel, mOwnName);
        try {
            if (mService.showIdentifyDialog(mIdentifyDialog.getToken(), componentName, str, z) == 0) {
                return mIdentifyDialog;
            }
            unregisterActivityLifeCallback();
            return null;
        } catch (Exception e) {
            unregisterActivityLifeCallback();
            logExceptionInDetail("showIdentifyDialog", e);
            return null;
        }
    }

    public boolean startEnrollActivity(Context context, EnrollFinishListener enrollFinishListener, String str) {
        if (getSensorType() == 1) {
            return startEnrollActivity(context, enrollFinishListener, str, -1);
        }
        if (context == null) {
            Log.e(TAG, "ActivityContext is null!! startEnrollActivity need activityContext");
            return false;
        } else if (enrollFinishListener == null) {
            Log.e(TAG, "Listener is null!! startEnrollActivity need EnrollFinishListener");
            return false;
        } else if (str == null || str.length() == 0) {
            Log.e(TAG, "Id parameter is needed. Please give a correct id.");
            return false;
        } else if (notifyEnrollBegin()) {
            Log.d(TAG, "startEnrollActivity: ID =" + str + ", Name = " + mOwnName);
            startSettingEnrollActivity(context, enrollFinishListener, str, -1);
            return true;
        } else {
            Log.e(TAG, "startEnrollActivity: notifyEnrollBegin failed");
            return false;
        }
    }

    public boolean startEnrollActivity(Context context, EnrollFinishListener enrollFinishListener, String str, int i) {
        if (context == null) {
            Log.e(TAG, "ActivityContext is null!! startEnrollActivity need activityContext");
            return false;
        } else if (enrollFinishListener == null) {
            Log.e(TAG, "Listener is null!! startEnrollActivity need EnrollFinishListener");
            return false;
        } else if (str == null || str.length() == 0) {
            Log.e(TAG, "Id parameter is needed. Please give a correct id.");
            return false;
        } else if (notifyEnrollBegin()) {
            mContext = context;
            mEnrollListener = enrollFinishListener;
            mStringId = str;
            mIndex = i;
            Log.d(TAG, "startEnrollActivity: previousStage(mStringId)=" + str + ", ownName=" + mOwnName + ", index=" + mIndex);
            if (getEnrolledFingers() != 0) {
                BaseBundle bundle = new Bundle();
                bundle.putBoolean(BUNDLE_ENABLE_PASSWORD, true);
                bundle.putString(CLIENTSPEC_KEY_PACKAGE_NAME, context.getPackageName());
                bundle.putBoolean(CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, true);
                bundle.putString(CLIENTSPEC_KEY_OWN_NAME, mOwnName);
                identifyWithDialog(mContext, new C00774(), bundle);
                return true;
            }
            startSettingEnrollActivity(context, enrollFinishListener, str, i);
            return true;
        } else {
            Log.e(TAG, "startEnrollActivity: notifyEnrollBegin failed");
            return false;
        }
    }

    public void startSettingEnrollActivity(Context context, EnrollFinishListener enrollFinishListener, String str, int i) {
        ensureServiceConnected();
        Intent intent = new Intent();
        intent.setComponent(START_ENROLL_ACTIVITY_COMPONENT);
        intent.putExtra("previousStage", str);
        if (mOwnName != null && mOwnName.length() > 0) {
            intent.putExtra(CLIENTSPEC_KEY_OWN_NAME, mOwnName);
        }
        intent.putExtra("index", i);
        intent.putExtra(CLIENTSPEC_KEY_PACKAGE_NAME, context.getPackageName());
        try {
            if (UserHandle.myUserId() >= 100) {
                context.startActivityAsUser(intent, UserHandle.OWNER);
            } else {
                context.startActivity(intent);
            }
            context.registerReceiver(new EnrollFinishBroadcastReceiver(enrollFinishListener, str), new IntentFilter(ENROLL_FINISHED));
        } catch (Throwable e) {
            Log.e(TAG, "startSettingEnrollActivity: exception", e);
        }
    }

    public boolean unregisterClient(IBinder iBinder) {
        if (mIsAndroidFingerprintSupported && this.mSpassProcessSdkClientToken == iBinder) {
            this.mSpassProcessSdkClientToken = null;
            return true;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("unregisterClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            if (mService.unregisterClient(iBinder)) {
                mFpClient = null;
                return true;
            }
        } catch (Exception e) {
            logExceptionInDetail("unregisterClient", e, "token=" + iBinder);
        }
        return false;
    }

    public boolean verifyPassword(IBinder iBinder, String str) {
        if (!mIsAndroidFingerprintSupported) {
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("verifyPassword", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return false;
            }
            try {
                return mService.verifyPassword(iBinder, str, mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("verifyPassword", e);
            }
        }
        return false;
    }

    public boolean verifyPassword(String str) {
        return verifyPassword(null, str);
    }

    public int verifySensorState(int i, int i2, int i3, int i4, int i5) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("verifySensorState", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return -1;
        }
        try {
            return mService.verifySensorState(i, i2, i3, i4, i5);
        } catch (Exception e) {
            logExceptionInDetail("verifySensorState", e);
            return -1;
        }
    }
}
