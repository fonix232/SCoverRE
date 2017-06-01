package com.samsung.android.camera.iris;

import android.app.ActivityManagerNative;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.SemSmartGlow;
import android.security.keystore.AndroidKeyStoreProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.camera.iris.IIrisServiceReceiver.Stub;
import java.security.Signature;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class SemIrisManager {
    public static final String CLIENTSPEC_KEY_ALLOW_INDEXES = "request_template_index_list";
    public static final String CLIENT_KEY_PRIVILEGED_ATTR = "privileged_attr";
    public static final int IRIS_ACQUIRED_CAPTURE_COMPLETED = 10003;
    public static final int IRIS_ACQUIRED_CAPTURE_FAILED = 10006;
    public static final int IRIS_ACQUIRED_CAPTURE_IRIS_LEAVE = 10004;
    public static final int IRIS_ACQUIRED_CAPTURE_IRIS_LEAVE_TIMEOUT = 10007;
    public static final int IRIS_ACQUIRED_CAPTURE_READY = 10001;
    public static final int IRIS_ACQUIRED_CAPTURE_STARTED = 10002;
    public static final int IRIS_ACQUIRED_CAPTURE_SUCCESS = 10005;
    public static final int IRIS_ACQUIRED_CHANGE_YOUR_POSITION = 12;
    public static final int IRIS_ACQUIRED_DUPLICATED_SCANNED_IMAGE = 1002;
    public static final int IRIS_ACQUIRED_EYE_NOT_PRESENT = 10;
    public static final int IRIS_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_END = 10009;
    public static final int IRIS_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_START = 10008;
    public static final int IRIS_ACQUIRED_GOOD = 0;
    public static final int IRIS_ACQUIRED_INSUFFICIENT = 2;
    public static final int IRIS_ACQUIRED_MOVE_CLOSER = 3;
    public static final int IRIS_ACQUIRED_MOVE_DOWN = 8;
    public static final int IRIS_ACQUIRED_MOVE_FARTHER = 4;
    public static final int IRIS_ACQUIRED_MOVE_LEFT = 5;
    public static final int IRIS_ACQUIRED_MOVE_RIGHT = 6;
    public static final int IRIS_ACQUIRED_MOVE_SOMEWHERE_DARKER = 11;
    public static final int IRIS_ACQUIRED_MOVE_UP = 7;
    public static final int IRIS_ACQUIRED_OPEN_EYES_WIDER = 9;
    public static final int IRIS_ACQUIRED_PARTIAL = 1;
    public static final int IRIS_ACQUIRED_VENDOR_BASE = 1000;
    public static final int IRIS_ACQUIRED_VENDOR_EVENT_BASE = 10000;
    public static final int IRIS_AUTH_TYPE_NONE = 0;
    public static final int IRIS_AUTH_TYPE_PREVIEW_CALLBACK = 1;
    public static final int IRIS_AUTH_TYPE_UI_NO_PREVIEW = 3;
    public static final int IRIS_AUTH_TYPE_UI_WITH_PREVIEW = 2;
    public static final int IRIS_DISABLE_PREVIEW_CALLBACK = 7;
    public static final int IRIS_ENABLE_PREVIEW_CALLBACK = 6;
    public static final int IRIS_ERROR_AUTH_VIEW_SIZE = 10;
    public static final int IRIS_ERROR_AUTH_WINDOW_TOKEN = 11;
    public static final int IRIS_ERROR_CANCELED = 4;
    public static final int IRIS_ERROR_DEVICE_NEED_RECAL = 1001;
    public static final int IRIS_ERROR_EVICTED = 13;
    public static final int IRIS_ERROR_EVICTED_DUE_TO_VIDEO_CALL = 14;
    public static final int IRIS_ERROR_EYE_SAFETY_TIMEOUT = 9;
    public static final int IRIS_ERROR_HW_UNAVAILABLE = 0;
    public static final int IRIS_ERROR_IDENTIFY_FAILURE_BROKEN_DATABASE = 1004;
    public static final int IRIS_ERROR_IDENTIFY_FAILURE_SENSOR_CHANGED = 1005;
    public static final int IRIS_ERROR_IDENTIFY_FAILURE_SERVICE_FAILURE = 1003;
    public static final int IRIS_ERROR_IDENTIFY_FAILURE_SYSTEM_FAILURE = 1002;
    public static final int IRIS_ERROR_LOCKOUT = 6;
    public static final int IRIS_ERROR_NEED_TO_RETRY = 5000;
    public static final int IRIS_ERROR_NO_EYE_DETECTED = 15;
    public static final int IRIS_ERROR_NO_SPACE = 3;
    public static final int IRIS_ERROR_OPEN_IR_CAMERA_FAIL = 8;
    public static final int IRIS_ERROR_PROXIMITY_TIMEOUT = 12;
    public static final int IRIS_ERROR_START_IR_CAMERA_PREVIEW_FAIL = 7;
    public static final int IRIS_ERROR_TIMEOUT = 2;
    public static final int IRIS_ERROR_UNABLE_TO_PROCESS = 1;
    public static final int IRIS_ERROR_UNABLE_TO_REMOVE = 5;
    public static final int IRIS_ERROR_VENDOR_BASE = 1000;
    public static final int IRIS_INVISIBLE_PREVIEW = 4;
    public static final int IRIS_ONE_EYE = 40000;
    public static final int IRIS_REQUEST_DVFS_FREQUENCY = 1004;
    public static final int IRIS_REQUEST_ENROLL_SESSION = 1002;
    public static final int IRIS_REQUEST_ENUMERATE = 11;
    public static final int IRIS_REQUEST_FACTORY_TEST_ALWAYS_LED_ON = 2001;
    public static final int IRIS_REQUEST_FACTORY_TEST_CAMERA_VERSION = 2004;
    public static final int IRIS_REQUEST_FACTORY_TEST_CAPTURE = 2002;
    public static final int IRIS_REQUEST_FACTORY_TEST_FULL_PREVIEW = 2000;
    public static final int IRIS_REQUEST_FACTORY_TEST_PREVIEW_MODE = 2003;
    public static final int IRIS_REQUEST_GET_IR_IDS = 1003;
    public static final int IRIS_REQUEST_GET_SENSOR_INFO = 5;
    public static final int IRIS_REQUEST_GET_SENSOR_STATUS = 6;
    public static final int IRIS_REQUEST_GET_UNIQUE_ID = 7;
    public static final int IRIS_REQUEST_GET_USERIDS = 12;
    public static final int IRIS_REQUEST_GET_VERSION = 4;
    public static final int IRIS_REQUEST_IR_PREVIEW_ENABLE = 2005;
    public static final int IRIS_REQUEST_LOCKOUT = 1001;
    public static final int IRIS_REQUEST_PAUSE = 0;
    public static final int IRIS_REQUEST_PROCESS_FIDO = 9;
    public static final int IRIS_REQUEST_REMOVE_IRIS = 1000;
    public static final int IRIS_REQUEST_RESUME = 1;
    public static final int IRIS_REQUEST_SENSOR_TEST_NORMALSCAN = 3;
    public static final int IRIS_REQUEST_SESSION_OPEN = 2;
    public static final int IRIS_REQUEST_SET_ACTIVE_GROUP = 8;
    public static final int IRIS_REQUEST_TZ_STATUS = 13;
    public static final int IRIS_REQUEST_UPDATE_SID = 10;
    public static final int IRIS_TWO_EYES = 40001;
    public static final int IRIS_VISIBLE_PREVIEW = 5;
    private static final String MANAGE_IRIS = "com.samsung.android.camera.iris.permission.MANAGE_IRIS";
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_AUTHENTICATION_SUCCEEDED_FIDO_RESULT_DATA = 107;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ERROR = 104;
    private static final int MSG_IR_IMAGE = 106;
    private static final int MSG_REMOVED = 105;
    public static final int PRIVILEGED_ATTR_EXCLUSIVE_IDENTIFY = 4;
    public static final int PRIVILEGED_ATTR_EXTRA_EVENT = 16;
    public static final int PRIVILEGED_ATTR_IRIS_DETECTION = 8;
    public static final int PRIVILEGED_ATTR_NO_LOCKOUT = 2;
    public static final int PRIVILEGED_ATTR_NO_VIBRATION = 1;
    public static final int PRIVILEGED_TYPE_KEYGUARD = Integer.MIN_VALUE;
    public static final int SENSOR_STATUS_ERROR = 100042;
    public static final int SENSOR_STATUS_LED_OFF = 30001;
    public static final int SENSOR_STATUS_LED_ON = 30000;
    public static final int SENSOR_STATUS_OK = 100040;
    public static final int SENSOR_STATUS_SECURE_DISABLE = 20001;
    public static final int SENSOR_STATUS_SECURE_ENALBE = 20000;
    public static final int SENSOR_STATUS_WORKING = 100041;
    private static final String SYSTEM_FEATURE_IRIS = "com.samsung.android.camera.iris";
    private static final String TAG = "SemIrisManager";
    private static final String USE_IRIS = "com.samsung.android.camera.iris.permission.USE_IRIS";
    private static SemIrisManager mSemIrisManager = null;
    private long mAuthBegin = 0;
    private AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    private CryptoObject mCryptoObject;
    private EnrollmentCallback mEnrollmentCallback;
    private GetterHandler mGetterHandler = new GetterHandler();
    private Handler mHandler;
    private RemovalCallback mRemovalCallback;
    private Iris mRemovalIris;
    private RequestCallback mRequestCallback;
    private IIrisService mService;
    private IIrisServiceReceiver mServiceReceiver = new C10021();
    private IBinder mToken = new Binder();

    class C10021 extends Stub {
        C10021() {
        }

        public void onAcquired(long j, int i) {
            SemIrisManager.this.mHandler.obtainMessage(101, i, 0, Long.valueOf(j)).sendToTarget();
        }

        public void onAuthenticationFailed(long j) {
            SemIrisManager.this.mHandler.obtainMessage(103).sendToTarget();
        }

        public void onAuthenticationSucceeded(long j, Iris iris, byte[] bArr) {
            SemIrisManager.this.mHandler.obtainMessage(107, bArr).sendToTarget();
            SemIrisManager.this.mHandler.obtainMessage(102, iris).sendToTarget();
        }

        public void onEnrollResult(long j, int i, int i2, int i3) {
            SemIrisManager.this.mHandler.obtainMessage(100, i3, 0, new Iris(null, i2, i, j)).sendToTarget();
        }

        public void onError(long j, int i) {
            SemIrisManager.this.mHandler.obtainMessage(104, i, 0, Long.valueOf(j)).sendToTarget();
        }

        public void onIRImage(long j, byte[] bArr, int i, int i2) {
            SemIrisManager.this.mHandler.obtainMessage(106, i, i2, bArr).sendToTarget();
        }

        public void onRemoved(long j, int i, int i2) {
            SemIrisManager.this.mHandler.obtainMessage(105, i, i2, Long.valueOf(j)).sendToTarget();
        }
    }

    public static abstract class AuthenticationCallback {
        public void onAuthenticationAcquired(int i) {
        }

        public void onAuthenticationError(int i, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
        }

        public void onIRImage(byte[] bArr, int i, int i2) {
        }
    }

    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private Iris mIris;

        public AuthenticationResult(CryptoObject cryptoObject, Iris iris) {
            this.mCryptoObject = cryptoObject;
            this.mIris = iris;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        public Iris getIris() {
            return this.mIris;
        }
    }

    public static final class CryptoObject {
        private final Object mCrypto;
        private final byte[] mFidoRequestData;
        private byte[] mFidoResultData = null;

        public CryptoObject(Signature signature, byte[] bArr) {
            this.mCrypto = signature;
            this.mFidoRequestData = bArr;
        }

        public CryptoObject(Cipher cipher, byte[] bArr) {
            this.mCrypto = cipher;
            this.mFidoRequestData = bArr;
        }

        public CryptoObject(Mac mac, byte[] bArr) {
            this.mCrypto = mac;
            this.mFidoRequestData = bArr;
        }

        private void setFidoResultData(byte[] bArr) {
            this.mFidoResultData = bArr;
        }

        public Cipher getCipher() {
            return this.mCrypto instanceof Cipher ? (Cipher) this.mCrypto : null;
        }

        public byte[] getFidoRequestData() {
            return this.mFidoRequestData;
        }

        public byte[] getFidoResultData() {
            return this.mFidoResultData;
        }

        public Mac getMac() {
            return this.mCrypto instanceof Mac ? (Mac) this.mCrypto : null;
        }

        public long getOpId() {
            return this.mCrypto != null ? AndroidKeyStoreProvider.getKeyStoreOperationHandle(this.mCrypto) : 0;
        }

        public Signature getSignature() {
            return this.mCrypto instanceof Signature ? (Signature) this.mCrypto : null;
        }
    }

    public static abstract class EnrollmentCallback {
        public void onEnrollmentError(int i, CharSequence charSequence) {
        }

        public void onEnrollmentHelp(int i, CharSequence charSequence) {
        }

        public void onEnrollmentProgress(int i) {
        }

        public void onIRImage(byte[] bArr, int i, int i2) {
        }
    }

    static class GetterHandler extends Handler {
        private static final int IMAGE_GETTER_CALLBACK = 1;

        GetterHandler() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    ((Runnable) message.obj).run();
                    return;
                default:
                    return;
            }
        }

        public void postDelayedGetterCallback(Runnable runnable, long j) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = runnable;
            sendMessageDelayed(obtain, j);
        }

        public void postGetterCallback(Runnable runnable) {
            postDelayedGetterCallback(runnable, 0);
        }

        public void removeAllGetterCallbacks() {
            removeMessages(1);
        }
    }

    public static abstract class LockoutResetCallback {
        public void onLockoutReset() {
        }
    }

    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        private void sendAcquiredResult(long j, int i) {
            if (SemIrisManager.this.mRequestCallback == null || !(i == 10008 || i == 10009)) {
                if (SemIrisManager.this.mAuthenticationCallback != null) {
                    SemIrisManager.this.mAuthenticationCallback.onAuthenticationAcquired(i);
                }
                CharSequence -wrap0 = SemIrisManager.this.getAcquiredString(i);
                if (-wrap0 != null) {
                    if (SemIrisManager.this.mEnrollmentCallback != null) {
                        SemIrisManager.this.mEnrollmentCallback.onEnrollmentHelp(i, -wrap0);
                    } else if (SemIrisManager.this.mAuthenticationCallback != null && -wrap0 != null) {
                        SemIrisManager.this.mAuthenticationCallback.onAuthenticationHelp(i, -wrap0);
                    } else {
                        return;
                    }
                    return;
                }
                return;
            }
            SemIrisManager.this.mRequestCallback.onRequested(i);
        }

        private void sendAuthenticatedFailed() {
            if (SemIrisManager.this.mAuthenticationCallback != null) {
                SemIrisManager.this.mAuthenticationCallback.onAuthenticationFailed();
            }
        }

        private void sendAuthenticatedSucceeded(Iris iris) {
            Log.m37w(SemIrisManager.TAG, "sendAuthenticatedSucceeded, ir : " + iris);
            if (SemIrisManager.this.mAuthenticationCallback != null) {
                SemIrisManager.this.mAuthenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(SemIrisManager.this.mCryptoObject, iris));
            }
        }

        private void sendAuthenticatedSucceededFidoResultData(byte[] bArr) {
            Log.m37w(SemIrisManager.TAG, "sendAuthenticatedSucceededFidoResultData, fidoResultData : " + Arrays.toString(bArr));
            if (SemIrisManager.this.mCryptoObject != null) {
                SemIrisManager.this.mCryptoObject.setFidoResultData(bArr);
            }
        }

        private void sendEnrollResult(Iris iris, int i) {
            if (SemIrisManager.this.mEnrollmentCallback != null) {
                SemIrisManager.this.mEnrollmentCallback.onEnrollmentProgress(i);
            }
        }

        private void sendErrorResult(long j, int i) {
            Log.m37w(SemIrisManager.TAG, "sendErrorResult, errMsgId : " + i);
            if (i != 4) {
                if (SemIrisManager.this.mEnrollmentCallback != null) {
                    SemIrisManager.this.mEnrollmentCallback.onEnrollmentError(i, SemIrisManager.this.getErrorString(i));
                } else if (SemIrisManager.this.mAuthenticationCallback != null) {
                    boolean equals = (SemIrisManager.this.mContext.getOpPackageName().equals("com.samsung.android.server.iris") || SemIrisManager.this.mContext.getOpPackageName().equals("com.android.settings")) ? true : SemIrisManager.this.mContext.getOpPackageName().equals("com.android.systemui");
                    if (!equals && i > 1000) {
                        i = 4;
                    }
                    SemIrisManager.this.mAuthenticationCallback.onAuthenticationError(i, SemIrisManager.this.getErrorString(i));
                } else if (SemIrisManager.this.mRemovalCallback != null) {
                    SemIrisManager.this.mRemovalCallback.onRemovalError(SemIrisManager.this.mRemovalIris, i, SemIrisManager.this.getErrorString(i));
                }
            }
        }

        private void sendIRImage(byte[] bArr, int i, int i2) {
            Log.m37w(SemIrisManager.TAG, "sendIRImage, width : " + i + " height : " + i2);
            if (SemIrisManager.this.mEnrollmentCallback != null) {
                SemIrisManager.this.mEnrollmentCallback.onIRImage(bArr, i, i2);
            }
            if (SemIrisManager.this.mAuthenticationCallback != null) {
                SemIrisManager.this.mAuthenticationCallback.onIRImage(bArr, i, i2);
            }
        }

        private void sendRemovedResult(long j, int i, int i2) {
            if (SemIrisManager.this.mRemovalCallback != null) {
                int irisId = SemIrisManager.this.mRemovalIris.getIrisId();
                int groupId = SemIrisManager.this.mRemovalIris.getGroupId();
                if (i != irisId) {
                    Log.m37w(SemIrisManager.TAG, "Iris id didn't match: " + i + " != " + irisId);
                }
                if (i2 != groupId) {
                    Log.m37w(SemIrisManager.TAG, "Group id didn't match: " + i2 + " != " + groupId);
                }
                SemIrisManager.this.mRemovalCallback.onRemovalSucceeded(SemIrisManager.this.mRemovalIris);
            }
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 100:
                    sendEnrollResult((Iris) message.obj, message.arg1);
                    return;
                case 101:
                    sendAcquiredResult(((Long) message.obj).longValue(), message.arg1);
                    return;
                case 102:
                    sendAuthenticatedSucceeded((Iris) message.obj);
                    return;
                case 103:
                    sendAuthenticatedFailed();
                    return;
                case 104:
                    sendErrorResult(((Long) message.obj).longValue(), message.arg1);
                    return;
                case 105:
                    sendRemovedResult(((Long) message.obj).longValue(), message.arg1, message.arg2);
                    return;
                case 106:
                    sendIRImage((byte[]) message.obj, message.arg1, message.arg2);
                    return;
                case 107:
                    sendAuthenticatedSucceededFidoResultData((byte[]) message.obj);
                    return;
                default:
                    return;
            }
        }
    }

    private class OnAuthenticationCancelListener implements OnCancelListener {
        private CryptoObject mCrypto;

        public OnAuthenticationCancelListener(CryptoObject cryptoObject) {
            this.mCrypto = cryptoObject;
        }

        public void onCancel() {
            SemIrisManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    private class OnEnrollCancelListener implements OnCancelListener {
        private OnEnrollCancelListener() {
        }

        public void onCancel() {
            SemIrisManager.this.cancelEnrollment();
        }
    }

    public static abstract class RemovalCallback {
        public void onRemovalError(Iris iris, int i, CharSequence charSequence) {
        }

        public void onRemovalSucceeded(Iris iris) {
        }
    }

    public static abstract class RequestCallback {
        public void onRequested(int i) {
        }
    }

    public SemIrisManager(Context context) {
        this.mContext = context;
        this.mHandler = new MyHandler(context);
    }

    public SemIrisManager(Context context, IIrisService iIrisService) {
        this.mContext = context;
        this.mService = iIrisService;
        if (this.mService == null) {
            Log.m35v(TAG, "SemIrisManagerService was null");
        }
        this.mHandler = new MyHandler(context);
    }

    private static String byteArrayToHex(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(bArr[i] & 255)}));
        }
        return stringBuilder.toString();
    }

    private static String bytesToString(byte[] bArr, int i) {
        if (i > bArr.length || i < 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(i * 2);
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append(String.format("%c", new Object[]{Integer.valueOf(bArr[i2] & 255)}));
        }
        return stringBuilder.toString();
    }

    private void cancelAuthentication(CryptoObject cryptoObject) {
        Log.m31e(TAG, "cancelAuthentication");
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception while canceling authentication");
            }
        }
    }

    private void cancelEnrollment() {
        Log.m31e(TAG, "cancelEnrollment");
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mService.cancelEnrollment(this.mToken);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception while canceling enrollment");
            }
        }
    }

    private void checkAuthViewWindowToken(CryptoObject cryptoObject, CancellationSignal cancellationSignal, int i, AuthenticationCallback authenticationCallback, Handler handler, int i2, Bundle bundle, View view, long j, byte[] bArr) {
        if (this.mGetterHandler == null) {
            this.mGetterHandler = new GetterHandler();
        }
        if (view.getWindowToken() != null) {
            this.mGetterHandler.removeAllGetterCallbacks();
            try {
                IBinder windowToken = view.getWindowToken();
                int[] iArr = new int[2];
                view.getLocationInWindow(iArr);
                if (this.mToken == null) {
                    Log.m31e(TAG, "mToken null");
                }
                Size minimumIrisViewSize = getMinimumIrisViewSize();
                if ((view.getWidth() < minimumIrisViewSize.getWidth() || view.getHeight() < minimumIrisViewSize.getHeight()) && authenticationCallback != null) {
                    Log.m31e(TAG, "Invalid irisView size. IrisView's proper size:" + minimumIrisViewSize.getWidth() + "x" + minimumIrisViewSize.getHeight() + ", but app's size:" + view.getWidth() + "x" + view.getHeight());
                }
                this.mService.authenticate(this.mToken, windowToken, iArr[0], iArr[1], view.getWidth(), view.getHeight(), j, i2, this.mServiceReceiver, i, this.mContext.getOpPackageName(), bundle, bArr);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception while authenticating");
                if (authenticationCallback != null) {
                    authenticationCallback.onAuthenticationError(1, getErrorString(1));
                }
            }
        } else if (System.currentTimeMillis() - this.mAuthBegin >= 3000) {
            Log.m31e(TAG, "checkAuthViewWindowToken is null");
            this.mGetterHandler.removeAllGetterCallbacks();
        } else {
            final CryptoObject cryptoObject2 = cryptoObject;
            final CancellationSignal cancellationSignal2 = cancellationSignal;
            final int i3 = i;
            final AuthenticationCallback authenticationCallback2 = authenticationCallback;
            final Handler handler2 = handler;
            final int i4 = i2;
            final Bundle bundle2 = bundle;
            final View view2 = view;
            final long j2 = j;
            final byte[] bArr2 = bArr;
            this.mGetterHandler.postGetterCallback(new Runnable() {
                public void run() {
                    SemIrisManager.this.checkAuthViewWindowToken(cryptoObject2, cancellationSignal2, i3, authenticationCallback2, handler2, i4, bundle2, view2, j2, bArr2);
                }
            });
        }
    }

    private void checkEnrollViewWindowToken(byte[] bArr, CancellationSignal cancellationSignal, int i, int i2, EnrollmentCallback enrollmentCallback, Bundle bundle, View view) {
        if (this.mGetterHandler == null) {
            this.mGetterHandler = new GetterHandler();
        }
        if (view.getWindowToken() == null) {
            final byte[] bArr2 = bArr;
            final CancellationSignal cancellationSignal2 = cancellationSignal;
            final int i3 = i;
            final int i4 = i2;
            final EnrollmentCallback enrollmentCallback2 = enrollmentCallback;
            final Bundle bundle2 = bundle;
            final View view2 = view;
            this.mGetterHandler.postGetterCallback(new Runnable() {
                public void run() {
                    SemIrisManager.this.checkEnrollViewWindowToken(bArr2, cancellationSignal2, i3, i4, enrollmentCallback2, bundle2, view2);
                }
            });
            return;
        }
        this.mGetterHandler.removeAllGetterCallbacks();
        try {
            IBinder windowToken = view.getWindowToken();
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            if (this.mToken == null) {
                Log.m31e(TAG, "mToken null");
            }
            this.mService.enroll(this.mToken, windowToken, iArr[0], iArr[1], view.getWidth(), view.getHeight(), bArr, i2, this.mServiceReceiver, i, this.mContext.getOpPackageName(), bundle);
        } catch (RemoteException e) {
            Log.m37w(TAG, "Remote exception in enroll");
            if (enrollmentCallback != null) {
                enrollmentCallback.onEnrollmentError(1, getErrorString(1));
            }
        }
    }

    private synchronized boolean ensureServiceConnected() {
        if (this.mService != null) {
            try {
                this.mService.isHardwareDetected(0, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                if (e instanceof DeadObjectException) {
                    this.mService = null;
                }
            }
        }
        if (this.mService == null) {
            startIrisService();
            waitForService();
        }
        return this.mService != null;
    }

    private String getAcquiredString(int i) {
        Resources resourcesForApplication;
        try {
            resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication("com.samsung.android.server.iris");
        } catch (Exception e) {
            Log.m31e(TAG, "getAcquiredString, Exception = " + e);
            resourcesForApplication = null;
        }
        if (resourcesForApplication == null) {
            Log.m31e(TAG, "mRes is null");
            return null;
        }
        switch (i) {
            case 3:
                try {
                    return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_acquired_move_closer", "string", "com.samsung.android.server.iris"));
                } catch (NotFoundException e2) {
                    Log.m29d(TAG, "getAcquiredString, NotFoundException = " + e2);
                    return null;
                }
            case 4:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_acquired_move_farther", "string", "com.samsung.android.server.iris"));
            case 9:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_acquired_open_wider", "string", "com.samsung.android.server.iris"));
            case 11:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_acquired_move_somewhere_darker", "string", "com.samsung.android.server.iris"));
            case 12:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_acquired_change_your_position", "string", "com.samsung.android.server.iris"));
            default:
                return null;
        }
    }

    private int getCurrentUserId() {
        try {
            return ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (RemoteException e) {
            Log.m37w(TAG, "Failed to get current user id\n");
            return -10000;
        }
    }

    private String getErrorString(int i) {
        Resources resourcesForApplication;
        try {
            resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication("com.samsung.android.server.iris");
        } catch (Exception e) {
            Log.m31e(TAG, "getErrorString, Exception = " + e);
            resourcesForApplication = null;
        }
        if (resourcesForApplication == null) {
            Log.m31e(TAG, "mRes is null");
            return null;
        }
        switch (i) {
            case 0:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_sensor_no_response", "string", "com.samsung.android.server.iris"));
            case 1:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_unable_to_process", "string", "com.samsung.android.server.iris"));
            case 2:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_timeout", "string", "com.samsung.android.server.iris"));
            case 3:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_no_space", "string", "com.samsung.android.server.iris"));
            case 4:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_canceled", "string", "com.samsung.android.server.iris"));
            case 5:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_unable_to_remove", "string", "com.samsung.android.server.iris"));
            case 6:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_lockout", "string", "com.samsung.android.server.iris"));
            case 7:
                return MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            case 8:
                return MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            case 9:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_eye_safety_timeout", "string", "com.samsung.android.server.iris"));
            case 10:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_auth_view_size", "string", "com.samsung.android.server.iris"));
            case 12:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_proximity_timeout", "string", "com.samsung.android.server.iris"));
            case 13:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_evicted", "string", "com.samsung.android.server.iris"));
            case 14:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_video_call_interrupt", "string", "com.samsung.android.server.iris"));
            case 15:
                return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_no_eye_detected", "string", "com.samsung.android.server.iris"));
            default:
                try {
                    return resourcesForApplication.getString(resourcesForApplication.getIdentifier("iris_error_unable_to_process", "string", "com.samsung.android.server.iris"));
                } catch (NotFoundException e2) {
                    Log.m29d(TAG, "getErrorString, NotFoundException = " + e2);
                    return null;
                }
        }
    }

    public static synchronized SemIrisManager getSemIrisManager(Context context) {
        synchronized (SemIrisManager.class) {
            if (context.getPackageManager().hasSystemFeature(SYSTEM_FEATURE_IRIS)) {
                if (mSemIrisManager == null) {
                    mSemIrisManager = new SemIrisManager(context);
                }
                SemIrisManager semIrisManager = mSemIrisManager;
                return semIrisManager;
            }
            return null;
        }
    }

    private byte[] requestGetUniqueID(int i, String str) {
        if (!ensureServiceConnected()) {
            return null;
        }
        byte[] bArr = new byte[256];
        int i2 = 0;
        if (this.mService != null) {
            try {
                i2 = this.mService.request(this.mToken, 7, str.getBytes(), bArr, i, UserHandle.myUserId(), this.mServiceReceiver);
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in request()");
            }
        }
        return i2 <= 0 ? null : Arrays.copyOf(bArr, i2);
    }

    private void startIrisService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.samsung.android.server.iris", "com.samsung.android.server.iris.IrisService"));
            this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
        } catch (Exception e) {
            Log.m31e(TAG, "Starting startIrisService failed: " + e);
        }
    }

    private void useHandler(Handler handler) {
        if (handler != null) {
            this.mHandler = new MyHandler(handler.getLooper());
        } else if (this.mHandler.getLooper() != this.mContext.getMainLooper()) {
            this.mHandler = new MyHandler(this.mContext.getMainLooper());
        }
    }

    private void waitForService() {
        int i = 1;
        while (i <= 20) {
            this.mService = IIrisService.Stub.asInterface(ServiceManager.getService("samsung.iris"));
            if (this.mService != null) {
                Log.m35v(TAG, "Service connected!");
                return;
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                i++;
            }
        }
    }

    public void addLockoutResetCallback(final LockoutResetCallback lockoutResetCallback) {
        if (ensureServiceConnected()) {
            if (this.mService != null) {
                try {
                    final PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
                    this.mService.addLockoutResetCallback(new IIrisServiceLockoutResetCallback.Stub() {
                        public void onLockoutReset(long j) throws RemoteException {
                            final WakeLock newWakeLock = powerManager.newWakeLock(1, "lockoutResetCallback");
                            newWakeLock.acquire();
                            Handler -get4 = SemIrisManager.this.mHandler;
                            final LockoutResetCallback lockoutResetCallback = lockoutResetCallback;
                            -get4.post(new Runnable() {
                                public void run() {
                                    try {
                                        lockoutResetCallback.onLockoutReset();
                                    } finally {
                                        newWakeLock.release();
                                    }
                                }
                            });
                        }
                    });
                } catch (RemoteException e) {
                    Log.m35v(TAG, "Remote exception in addLockoutResetCallback()");
                }
            } else {
                Log.m37w(TAG, "addLockoutResetCallback(): Service not connected!");
            }
        }
    }

    public void authenticate(CryptoObject cryptoObject, CancellationSignal cancellationSignal, int i, AuthenticationCallback authenticationCallback, Handler handler, int i2, Bundle bundle, View view) {
        if (authenticationCallback == null) {
            throw new IllegalArgumentException("Must supply an authentication callback");
        }
        if (cancellationSignal != null) {
            if (cancellationSignal.isCanceled()) {
                Log.m37w(TAG, "authentication already canceled");
                return;
            }
            cancellationSignal.setOnCancelListener(new OnAuthenticationCancelListener(cryptoObject));
        }
        if (ensureServiceConnected() && this.mService != null) {
            try {
                useHandler(handler);
                this.mEnrollmentCallback = null;
                this.mAuthenticationCallback = authenticationCallback;
                this.mCryptoObject = cryptoObject;
                long opId = cryptoObject != null ? cryptoObject.getOpId() : 0;
                byte[] fidoRequestData = cryptoObject != null ? this.mCryptoObject.getFidoRequestData() : null;
                if (view == null) {
                    this.mService.authenticate(this.mToken, null, 0, 0, 0, 0, opId, i2, this.mServiceReceiver, i, this.mContext.getOpPackageName(), bundle, fidoRequestData);
                } else {
                    this.mAuthBegin = System.currentTimeMillis();
                    checkAuthViewWindowToken(cryptoObject, cancellationSignal, i, authenticationCallback, handler, i2, bundle, view, opId, fidoRequestData);
                }
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception while authenticating");
                authenticationCallback.onAuthenticationError(1, getErrorString(1));
            }
        }
    }

    public void authenticate(CryptoObject cryptoObject, CancellationSignal cancellationSignal, int i, AuthenticationCallback authenticationCallback, Handler handler, View view) {
        authenticate(cryptoObject, cancellationSignal, i, authenticationCallback, handler, view, UserHandle.myUserId());
    }

    public void authenticate(CryptoObject cryptoObject, CancellationSignal cancellationSignal, int i, AuthenticationCallback authenticationCallback, Handler handler, View view, int i2) {
        authenticate(cryptoObject, cancellationSignal, i, authenticationCallback, handler, i2, null, view);
    }

    public void enableIRImageCallback(boolean z) {
        if (ensureServiceConnected() && this.mService != null) {
            if (z) {
                try {
                    this.mService.enableIRImageCallback(UserHandle.myUserId(), this.mContext.getOpPackageName(), 6);
                } catch (RemoteException e) {
                    Log.m35v(TAG, "Remote exception in enableIRImageCallback");
                }
            } else {
                this.mService.enableIRImageCallback(UserHandle.myUserId(), this.mContext.getOpPackageName(), 7);
            }
        }
    }

    public void enroll(byte[] bArr, CancellationSignal cancellationSignal, int i, int i2, EnrollmentCallback enrollmentCallback, Bundle bundle, View view) {
        if (i2 == -2) {
            i2 = getCurrentUserId();
        }
        if (enrollmentCallback == null) {
            throw new IllegalArgumentException("Must supply an enrollment callback");
        }
        if (cancellationSignal != null) {
            if (cancellationSignal.isCanceled()) {
                Log.m37w(TAG, "enrollment already canceled");
                return;
            } else {
                cancellationSignal.setOnCancelListener(new OnEnrollCancelListener());
            }
        }
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mAuthenticationCallback = null;
                this.mEnrollmentCallback = enrollmentCallback;
                if (view == null) {
                    this.mService.enroll(this.mToken, null, 0, 0, 0, 0, bArr, i2, this.mServiceReceiver, i, this.mContext.getOpPackageName(), bundle);
                } else {
                    checkEnrollViewWindowToken(bArr, cancellationSignal, i, i2, enrollmentCallback, bundle, view);
                }
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in enroll");
                enrollmentCallback.onEnrollmentError(1, getErrorString(1));
            }
        }
    }

    public void enroll(byte[] bArr, CancellationSignal cancellationSignal, int i, EnrollmentCallback enrollmentCallback, View view) {
        enroll(bArr, cancellationSignal, i, getCurrentUserId(), enrollmentCallback, null, view);
    }

    public long getAuthenticatorId() {
        if (!ensureServiceConnected()) {
            return 0;
        }
        if (this.mService != null) {
            try {
                return this.mService.getAuthenticatorId(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getAuthenticatorId()");
            }
        } else {
            Log.m37w(TAG, "getAuthenticatorId(): Service not connected!");
            return 0;
        }
    }

    public SparseArray getEnrolledIrisUniqueID() {
        if (!ensureServiceConnected()) {
            return null;
        }
        SparseArray sparseArray = new SparseArray();
        Iterable iterable = null;
        int i = 1;
        if (this.mService != null) {
            try {
                iterable = this.mService.getEnrolledIrises(UserHandle.myUserId(), this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getEnrolledIrises");
            }
        }
        if (r4.size() <= 0 || this.mContext == null) {
            sparseArray = null;
        } else {
            for (Iris irisId : r4) {
                sparseArray.put(i, byteArrayToHex(requestGetUniqueID(irisId.getIrisId(), this.mContext.getOpPackageName())));
                i++;
            }
        }
        return sparseArray;
    }

    public List<Iris> getEnrolledIrises() {
        return getEnrolledIrises(UserHandle.myUserId());
    }

    public List<Iris> getEnrolledIrises(int i) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.getEnrolledIrises(i, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getEnrolledIrises");
            }
        }
        return null;
    }

    public Size getMinimumIrisViewSize() {
        int i;
        int i2;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        int round = Math.round(displayMetrics.density);
        if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
            i = displayMetrics.widthPixels / round;
            i2 = (int) (((float) i) / 1.7777778f);
        } else {
            i = displayMetrics.heightPixels / round;
            i2 = (int) (((float) i) / 1.7777778f);
        }
        return new Size(i * round, i2 * round);
    }

    public boolean hasEnrolledIrises() {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.hasEnrolledIrises(UserHandle.myUserId(), this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getEnrolledIrises");
            }
        }
        return false;
    }

    public boolean hasEnrolledIrises(int i) {
        if (this.mService != null) {
            try {
                return this.mService.hasEnrolledIrises(i, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getEnrolledIrises, userId : " + i);
            }
        }
        return false;
    }

    public boolean isEnrollSession() {
        return request(1002, null, null, 0, null) > 0;
    }

    public boolean isHardwareDetected() {
        Log.m37w(TAG, "isIrisHardwareDetected()");
        return this.mContext != null ? this.mContext.getPackageManager().hasSystemFeature(SYSTEM_FEATURE_IRIS) : false;
    }

    public int postEnroll() {
        int i = 0;
        if (!ensureServiceConnected()) {
            return 0;
        }
        if (this.mService != null) {
            try {
                i = this.mService.postEnroll(this.mToken);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in post enroll");
            }
        }
        return i;
    }

    public long preEnroll() {
        long j = 0;
        if (!ensureServiceConnected()) {
            return 0;
        }
        if (this.mService != null) {
            try {
                j = this.mService.preEnroll(this.mToken);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in enroll");
            }
        }
        return j;
    }

    public void remove(Iris iris, int i, RemovalCallback removalCallback) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mRemovalCallback = removalCallback;
                this.mRemovalIris = iris;
                this.mService.remove(this.mToken, iris.getIrisId(), iris.getGroupId(), i, this.mServiceReceiver);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in remove");
                if (removalCallback != null) {
                    removalCallback.onRemovalError(iris, 1, getErrorString(1));
                }
            }
        }
    }

    public void remove(Iris iris, RemovalCallback removalCallback) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mRemovalCallback = removalCallback;
                this.mRemovalIris = iris;
                this.mService.remove(this.mToken, iris.getIrisId(), iris.getGroupId(), getCurrentUserId(), this.mServiceReceiver);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in remove");
                if (removalCallback != null) {
                    removalCallback.onRemovalError(iris, 1, getErrorString(1));
                }
            }
        }
    }

    public void rename(int i, int i2, String str) {
        if (ensureServiceConnected()) {
            if (this.mService != null) {
                try {
                    this.mService.rename(i, i2, str);
                } catch (RemoteException e) {
                    Log.m35v(TAG, "Remote exception in rename()");
                }
            } else {
                Log.m37w(TAG, "rename(): Service not connected!");
            }
        }
    }

    public int request(int i, byte[] bArr, byte[] bArr2, int i2, RequestCallback requestCallback) {
        if (!ensureServiceConnected()) {
            return 0;
        }
        if (this.mService != null) {
            if (bArr == null) {
                try {
                    bArr = new byte[0];
                } catch (RemoteException e) {
                    Log.m35v(TAG, "Remote exception in request()");
                }
            }
            if (bArr2 == null) {
                bArr2 = new byte[0];
            }
            this.mRequestCallback = requestCallback;
            return this.mService.request(this.mToken, i, bArr, bArr2, i2, getCurrentUserId(), this.mServiceReceiver);
        }
        Log.m37w(TAG, "request(): Service not connected!");
        return -2;
    }

    public boolean requestCameraVersion() {
        return request(2004, null, null, 0, null) >= 0;
    }

    public boolean requestCapture() {
        return request(2002, null, null, 0, null) >= 0;
    }

    public int[] requestEnumerate() {
        byte[] bArr = new byte[10];
        int request = request(11, null, bArr, 0, null);
        if (request <= 0) {
            return null;
        }
        int[] iArr = new int[request];
        for (int i = 0; i < request; i++) {
            iArr[i] = bArr[i];
        }
        return iArr;
    }

    public boolean requestFullPreview() {
        return request(2000, null, null, 0, null) >= 0;
    }

    public byte[] requestGetSensorInfo() {
        byte[] bArr = new byte[256];
        int request = request(5, null, bArr, 0, null);
        return request <= 0 ? null : Arrays.copyOf(bArr, request);
    }

    public int requestGetSensorStatus() {
        return request(6, null, null, 0, null);
    }

    public String[] requestGetUserIDs() {
        byte[] bArr = new byte[256];
        int request = request(12, null, bArr, 0, null);
        if (request <= 0) {
            return null;
        }
        String bytesToString = bytesToString(bArr, request);
        if (bytesToString == null) {
            return null;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(bytesToString, SemSmartGlow.COLOR_PACKAGE_SEPARATOR);
        String[] strArr = new String[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            int i2 = i + 1;
            strArr[i] = stringTokenizer.nextToken();
            i = i2;
        }
        return strArr;
    }

    public byte[] requestGetVersion() {
        byte[] bArr = new byte[256];
        int request = request(4, null, bArr, 0, null);
        return request <= 0 ? null : Arrays.copyOf(bArr, request);
    }

    public boolean requestLedOn() {
        return request(2001, null, null, 0, null) >= 0;
    }

    public boolean requestPause() {
        return request(0, null, null, 0, null) >= 0;
    }

    public boolean requestPreviewMode() {
        return request(2003, null, null, 0, null) >= 0;
    }

    public byte[] requestProcessFIDO(byte[] bArr) {
        byte[] bArr2 = new byte[10240];
        int request = request(9, bArr, bArr2, 0, null);
        return request <= 0 ? null : Arrays.copyOf(bArr2, request);
    }

    public boolean requestResume() {
        return request(1, null, null, 0, null) >= 0;
    }

    public boolean requestSessionOpen() {
        return request(2, null, null, 0, null) >= 0;
    }

    public boolean requestSetActiveGroup(String str) {
        if (str == null) {
            if (request(8, null, null, getCurrentUserId(), null) < 0) {
                return false;
            }
        }
        if (request(8, str.getBytes(), null, getCurrentUserId(), null) < 0) {
            return false;
        }
        return true;
    }

    public boolean requestUpdateSID(byte[] bArr) {
        return request(10, bArr, null, 0, null) >= 0;
    }

    public void resetTimeout(byte[] bArr) {
        if (ensureServiceConnected()) {
            if (this.mService != null) {
                try {
                    this.mService.resetTimeout(bArr);
                } catch (RemoteException e) {
                    Log.m35v(TAG, "Remote exception in resetTimeout()");
                }
            } else {
                Log.m37w(TAG, "resetTimeout(): Service not connected!");
            }
        }
    }

    public void setActiveUser(int i) {
        if (this.mService != null) {
            try {
                this.mService.setActiveUser(i);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in setActiveUser");
            }
        }
    }

    public void setIrisViewType(int i) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mService.setIrisViewType(UserHandle.myUserId(), this.mContext.getOpPackageName(), i);
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in setIrisViewType");
            }
        }
    }
}
