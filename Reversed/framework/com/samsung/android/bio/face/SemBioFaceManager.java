package com.samsung.android.bio.face;

import android.app.ActivityManagerNative;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.security.keystore.AndroidKeyStoreProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import com.samsung.android.bio.face.IFaceServiceReceiver.Stub;
import java.security.Signature;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class SemBioFaceManager {
    private static final boolean DEBUG = Debug.semIsProductDev();
    public static final int FACE_ACQUIRED_FAKE = 4;
    public static final int FACE_ACQUIRED_GOOD = 0;
    public static final int FACE_ACQUIRED_INVALID = 2;
    public static final int FACE_ACQUIRED_LOW_QUALITY = 3;
    public static final int FACE_ACQUIRED_MISALIGNED = 7;
    public static final int FACE_ACQUIRED_PROCESS_FAIL = 1;
    public static final int FACE_ACQUIRED_TOO_BIG = 5;
    public static final int FACE_ACQUIRED_TOO_SMALL = 6;
    public static final int FACE_ERROR_CAMERA_FAILURE = 10003;
    public static final int FACE_ERROR_CAMERA_UNAVAILABLE = 10005;
    public static final int FACE_ERROR_CANCELED = 5;
    public static final int FACE_ERROR_HW_UNAVAILABLE = 1;
    public static final int FACE_ERROR_IDENTIFY_FAILURE_BROKEN_DATABASE = 1004;
    public static final int FACE_ERROR_LOCKOUT = 10001;
    public static final int FACE_ERROR_NO_SPACE = 4;
    public static final int FACE_ERROR_TEMPLATE_CORRUPTED = 1004;
    public static final int FACE_ERROR_TIMEOUT = 3;
    public static final int FACE_ERROR_UNABLE_TO_PROCESS = 2;
    public static final int FACE_OK = 0;
    private static final String MANAGE_FACE = "com.samsung.android.bio.face.permission.MANAGE_FACE";
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_AUTHENTICATION_SUCCEEDED_FIDO_RESULT_DATA = 107;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ERROR = 104;
    private static final int MSG_REMOVED = 105;
    private static final String TAG = "SemBioFaceManager";
    private static final String USE_FACE = "com.samsung.android.bio.face.permission.USE_FACE";
    private static SemBioFaceManager mSemBioFaceManager = null;
    private AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    private CryptoObject mCryptoObject;
    private EnrollmentCallback mEnrollmentCallback;
    private Handler mHandler;
    private RemovalCallback mRemovalCallback;
    private Face mRemovalFace;
    private IFaceService mService;
    private IFaceServiceReceiver mServiceReceiver = new C09981();
    private IBinder mToken = new Binder();

    class C09981 extends Stub {
        C09981() {
        }

        public void onAcquired(long j, int i, String str) {
            SemBioFaceManager.this.mHandler.obtainMessage(101, i, 0, str).sendToTarget();
        }

        public void onAuthenticationFailed(long j) {
            SemBioFaceManager.this.mHandler.obtainMessage(103).sendToTarget();
        }

        public void onAuthenticationSucceeded(long j, Face face, byte[] bArr) {
            SemBioFaceManager.this.mHandler.obtainMessage(107, bArr).sendToTarget();
            SemBioFaceManager.this.mHandler.obtainMessage(102, face).sendToTarget();
        }

        public void onEnrollResult(long j, int i, int i2, int i3) {
            SemBioFaceManager.this.mHandler.obtainMessage(100, i3, 0, new Face(null, i2, i, j)).sendToTarget();
        }

        public void onError(long j, int i, String str) {
            SemBioFaceManager.this.mHandler.obtainMessage(104, i, 0, str).sendToTarget();
        }

        public void onRemoved(long j, int i, int i2) {
            SemBioFaceManager.this.mHandler.obtainMessage(105, i, i2, Long.valueOf(j)).sendToTarget();
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
    }

    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private Face mFace;

        public AuthenticationResult(CryptoObject cryptoObject, Face face) {
            this.mCryptoObject = cryptoObject;
            this.mFace = face;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        public Face getFace() {
            return this.mFace;
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
    }

    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        private void sendAcquiredResult(int i, String str) {
            if (SemBioFaceManager.this.mEnrollmentCallback != null) {
                SemBioFaceManager.this.mEnrollmentCallback.onEnrollmentHelp(i, str);
            } else if (SemBioFaceManager.this.mAuthenticationCallback != null) {
                SemBioFaceManager.this.mAuthenticationCallback.onAuthenticationAcquired(i);
                if (str != null) {
                    SemBioFaceManager.this.mAuthenticationCallback.onAuthenticationHelp(i, str);
                }
            }
        }

        private void sendAuthenticatedFailed() {
            if (SemBioFaceManager.this.mAuthenticationCallback != null) {
                SemBioFaceManager.this.mAuthenticationCallback.onAuthenticationFailed();
            }
        }

        private void sendAuthenticatedSucceeded(Face face) {
            if (SemBioFaceManager.this.mAuthenticationCallback != null) {
                SemBioFaceManager.this.mAuthenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(SemBioFaceManager.this.mCryptoObject, face));
            }
        }

        private void sendAuthenticatedSucceededFidoResultData(byte[] bArr) {
            if (SemBioFaceManager.this.mCryptoObject != null) {
                SemBioFaceManager.this.mCryptoObject.setFidoResultData(bArr);
            }
        }

        private void sendEnrollResult(Face face, int i) {
            if (SemBioFaceManager.this.mEnrollmentCallback != null) {
                SemBioFaceManager.this.mEnrollmentCallback.onEnrollmentProgress(i);
            }
        }

        private void sendErrorResult(int i, String str) {
            if (SemBioFaceManager.this.mEnrollmentCallback != null) {
                SemBioFaceManager.this.mEnrollmentCallback.onEnrollmentError(i, str);
            } else if (SemBioFaceManager.this.mAuthenticationCallback != null) {
                SemBioFaceManager.this.mAuthenticationCallback.onAuthenticationError(i, str);
            } else if (SemBioFaceManager.this.mRemovalCallback != null) {
                SemBioFaceManager.this.mRemovalCallback.onRemovalError(SemBioFaceManager.this.mRemovalFace, i, str);
            }
        }

        private void sendRemovedResult(long j, int i, int i2) {
            if (SemBioFaceManager.this.mRemovalCallback != null) {
                int faceId = SemBioFaceManager.this.mRemovalFace.getFaceId();
                int groupId = SemBioFaceManager.this.mRemovalFace.getGroupId();
                if (i != faceId) {
                    Log.m37w(SemBioFaceManager.TAG, "Face id didn't match: " + i + " != " + faceId);
                }
                if (i2 != groupId) {
                    Log.m37w(SemBioFaceManager.TAG, "Group id didn't match: " + i2 + " != " + groupId);
                }
                SemBioFaceManager.this.mRemovalCallback.onRemovalSucceeded(SemBioFaceManager.this.mRemovalFace);
            }
        }

        public void handleMessage(Message message) {
            Log.m33i(SemBioFaceManager.TAG, "handleMessage : " + message.what + ", " + message.arg1 + ", " + message.arg2);
            switch (message.what) {
                case 100:
                    sendEnrollResult((Face) message.obj, message.arg1);
                    return;
                case 101:
                    sendAcquiredResult(message.arg1, (String) message.obj);
                    return;
                case 102:
                    sendAuthenticatedSucceeded((Face) message.obj);
                    return;
                case 103:
                    sendAuthenticatedFailed();
                    return;
                case 104:
                    sendErrorResult(message.arg1, (String) message.obj);
                    return;
                case 105:
                    sendRemovedResult(((Long) message.obj).longValue(), message.arg1, message.arg2);
                    return;
                case 107:
                    sendAuthenticatedSucceededFidoResultData((byte[]) message.obj);
                    return;
                default:
                    Log.m37w(SemBioFaceManager.TAG, "handleMessage : Unknown msg");
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
            SemBioFaceManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    private class OnEnrollCancelListener implements OnCancelListener {
        private OnEnrollCancelListener() {
        }

        public void onCancel() {
            SemBioFaceManager.this.cancelEnrollment();
        }
    }

    private class OnFaceViewAttachStateChangeListener implements OnAttachStateChangeListener {
        static final int VIEW_TYPE_AUTH = 1;
        static final int VIEW_TYPE_ENROLL = 2;
        Bundle mAttr;
        byte[] mData;
        View mFaceView;
        int mFlag;
        long mSessionId;
        int mType;
        int mUserId;

        class C09991 implements Runnable {
            C09991() {
            }

            public void run() {
                OnFaceViewAttachStateChangeListener.this.runCommand();
                OnFaceViewAttachStateChangeListener.this.mFaceView.removeOnAttachStateChangeListener(OnFaceViewAttachStateChangeListener.this);
            }
        }

        public OnFaceViewAttachStateChangeListener(int i, View view, int i2, int i3, long j, byte[] bArr, Bundle bundle) {
            this.mType = i;
            this.mFaceView = view;
            this.mFlag = i2;
            this.mUserId = i3;
            this.mSessionId = j;
            this.mData = bArr;
            this.mAttr = bundle;
        }

        public void onViewAttachedToWindow(View view) {
            Log.m29d(SemBioFaceManager.TAG, "OnFaceViewAttachStateChangeListener : onViewAttachedToWindow");
            SemBioFaceManager.this.mHandler.post(new C09991());
        }

        public void onViewDetachedFromWindow(View view) {
            Log.m29d(SemBioFaceManager.TAG, "OnFaceViewAttachStateChangeListener : onViewDetachedFromWindow");
        }

        public void runCommand() {
            try {
                int[] iArr = new int[2];
                this.mFaceView.getLocationOnScreen(iArr);
                int width = this.mFaceView.getWidth();
                int height = this.mFaceView.getHeight();
                if (SemBioFaceManager.DEBUG) {
                    Log.m29d(SemBioFaceManager.TAG, "OnFaceViewAttachStateChangeListener : runCommand : [" + iArr[0] + ", " + iArr[1] + "], " + "[" + width + ", " + height + "]");
                }
                if (this.mType == 2) {
                    SemBioFaceManager.this.mService.enroll(SemBioFaceManager.this.mToken, this.mFaceView.getWindowToken(), iArr[0], iArr[1], width, height, this.mData, this.mUserId, SemBioFaceManager.this.mServiceReceiver, this.mFlag, SemBioFaceManager.this.mContext.getOpPackageName(), this.mAttr);
                } else if (this.mType == 1) {
                    SemBioFaceManager.this.mService.authenticate(SemBioFaceManager.this.mToken, this.mFaceView.getWindowToken(), iArr[0], iArr[1], width, height, this.mSessionId, this.mUserId, SemBioFaceManager.this.mServiceReceiver, this.mFlag, SemBioFaceManager.this.mContext.getOpPackageName(), this.mAttr, this.mData);
                }
            } catch (RemoteException e) {
                Log.m37w(SemBioFaceManager.TAG, "Remote exception to call enroll");
                if (this.mType == 2 && SemBioFaceManager.this.mEnrollmentCallback != null) {
                    SemBioFaceManager.this.mEnrollmentCallback.onEnrollmentError(2, null);
                } else if (this.mType == 1 && SemBioFaceManager.this.mAuthenticationCallback != null) {
                    SemBioFaceManager.this.mAuthenticationCallback.onAuthenticationError(2, null);
                }
            }
        }
    }

    public static abstract class RemovalCallback {
        public void onRemovalError(Face face, int i, CharSequence charSequence) {
        }

        public void onRemovalSucceeded(Face face) {
        }
    }

    private SemBioFaceManager(Context context) {
        this.mContext = context;
        this.mHandler = new MyHandler(context);
        ensureServiceConnected();
    }

    private void cancelAuthentication(CryptoObject cryptoObject) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception while canceling authentication");
            }
        }
    }

    private void cancelEnrollment() {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mService.cancelEnrollment(this.mToken);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception while canceling enrollment");
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized boolean ensureServiceConnected() {
        /*
        r6 = this;
        r1 = 1;
        monitor-enter(r6);
        r2 = r6.mService;	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x0037;
    L_0x0006:
        r2 = r6.mService;	 Catch:{ RemoteException -> 0x0015 }
        r4 = 0;
        r3 = r6.mContext;	 Catch:{ RemoteException -> 0x0015 }
        r3 = r3.getOpPackageName();	 Catch:{ RemoteException -> 0x0015 }
        r2.isHardwareDetected(r4, r3);	 Catch:{ RemoteException -> 0x0015 }
        monitor-exit(r6);
        return r1;
    L_0x0015:
        r0 = move-exception;
        r2 = "SemBioFaceManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0056 }
        r3.<init>();	 Catch:{ all -> 0x0056 }
        r4 = "ensureServiceConnected : failed ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0056 }
        r3 = r3.append(r0);	 Catch:{ all -> 0x0056 }
        r3 = r3.toString();	 Catch:{ all -> 0x0056 }
        android.util.Log.m37w(r2, r3);	 Catch:{ all -> 0x0056 }
        r2 = r0 instanceof android.os.DeadObjectException;	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x0037;
    L_0x0034:
        r2 = 0;
        r6.mService = r2;	 Catch:{ all -> 0x0056 }
    L_0x0037:
        r2 = "samsung.face";
        r2 = android.os.ServiceManager.getService(r2);	 Catch:{ all -> 0x0056 }
        r2 = com.samsung.android.bio.face.IFaceService.Stub.asInterface(r2);	 Catch:{ all -> 0x0056 }
        r6.mService = r2;	 Catch:{ all -> 0x0056 }
        r2 = r6.mService;	 Catch:{ all -> 0x0056 }
        if (r2 != 0) goto L_0x004e;
    L_0x0048:
        r6.startFaceService();	 Catch:{ all -> 0x0056 }
        r6.waitForService();	 Catch:{ all -> 0x0056 }
    L_0x004e:
        r2 = r6.mService;	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x0054;
    L_0x0052:
        monitor-exit(r6);
        return r1;
    L_0x0054:
        r1 = 0;
        goto L_0x0052;
    L_0x0056:
        r1 = move-exception;
        monitor-exit(r6);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.bio.face.SemBioFaceManager.ensureServiceConnected():boolean");
    }

    private int getCurrentUserId() {
        try {
            return ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (RemoteException e) {
            Log.m37w(TAG, "Failed to get current user id\n");
            return -10000;
        }
    }

    public static synchronized SemBioFaceManager getInstance(Context context) {
        SemBioFaceManager semBioFaceManager;
        synchronized (SemBioFaceManager.class) {
            if (mSemBioFaceManager == null) {
                mSemBioFaceManager = new SemBioFaceManager(context);
            }
            semBioFaceManager = mSemBioFaceManager;
        }
        return semBioFaceManager;
    }

    private void startFaceService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.samsung.android.bio.face.service", "com.samsung.android.bio.face.service.FaceService"));
            int myUserId = UserHandle.myUserId();
            if (myUserId < 100 || myUserId > 200) {
                this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
            } else {
                this.mContext.startServiceAsUser(intent, UserHandle.SYSTEM);
            }
            if (DEBUG) {
                Log.m29d(TAG, "startFaceService : " + myUserId);
            }
        } catch (Exception e) {
            Log.m31e(TAG, "Starting startFaceService failed: " + e);
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
            this.mService = IFaceService.Stub.asInterface(ServiceManager.getService("samsung.face"));
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
                    OnAttachStateChangeListener onFaceViewAttachStateChangeListener = new OnFaceViewAttachStateChangeListener(1, view, i, i2, opId, fidoRequestData, bundle);
                    if (view.getWindowToken() != null) {
                        onFaceViewAttachStateChangeListener.runCommand();
                    } else {
                        view.addOnAttachStateChangeListener(onFaceViewAttachStateChangeListener);
                    }
                }
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception while authenticating");
                authenticationCallback.onAuthenticationError(2, null);
            }
        }
    }

    public void authenticate(CryptoObject cryptoObject, CancellationSignal cancellationSignal, int i, AuthenticationCallback authenticationCallback, Handler handler, View view) {
        authenticate(cryptoObject, cancellationSignal, i, authenticationCallback, handler, UserHandle.myUserId(), null, view);
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
            }
            SemBioFaceManager semBioFaceManager = this;
            cancellationSignal.setOnCancelListener(new OnEnrollCancelListener());
        }
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mAuthenticationCallback = null;
                this.mEnrollmentCallback = enrollmentCallback;
                if (view == null) {
                    this.mService.enroll(this.mToken, null, 0, 0, 0, 0, bArr, i2, this.mServiceReceiver, i, this.mContext.getOpPackageName(), bundle);
                } else {
                    OnAttachStateChangeListener onFaceViewAttachStateChangeListener = new OnFaceViewAttachStateChangeListener(2, view, i, i2, 0, bArr, bundle);
                    if (view.getWindowToken() != null) {
                        onFaceViewAttachStateChangeListener.runCommand();
                    } else {
                        view.addOnAttachStateChangeListener(onFaceViewAttachStateChangeListener);
                    }
                }
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in enroll");
                enrollmentCallback.onEnrollmentError(2, null);
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

    public List<Face> getEnrolledFaces() {
        return getEnrolledFaces(UserHandle.myUserId());
    }

    public List<Face> getEnrolledFaces(int i) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.getEnrolledFaces(i, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getEnrolledFaces");
            }
        }
        return null;
    }

    public boolean hasEnrolledFaces() {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.hasEnrolledFaces(UserHandle.myUserId(), this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getEnrolledFaces");
            }
        }
        return false;
    }

    public boolean hasEnrolledFaces(int i) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.hasEnrolledFaces(i, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in getEnrolledFaces, userId : " + i);
            }
        }
        return false;
    }

    public boolean isEnrollSession() {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.isEnrollSession();
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in isEnrollSession()");
            }
        }
        return false;
    }

    public boolean isHardwareDetected() {
        return true;
    }

    public boolean isSessionClosed() {
        if (!ensureServiceConnected()) {
            return false;
        }
        if (this.mService != null) {
            try {
                return this.mService.isSessionClosed();
            } catch (RemoteException e) {
                Log.m35v(TAG, "Remote exception in isSessionClosed()");
            }
        } else {
            Log.m37w(TAG, "isSessionClosed(): Service not connected!");
            return false;
        }
    }

    public int postEnroll() {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.postEnroll(this.mToken);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in post enroll");
            }
        }
        return 0;
    }

    public long preEnroll() {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                return this.mService.preEnroll(this.mToken);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in enroll");
            }
        }
        return 0;
    }

    public void remove(Face face, int i, RemovalCallback removalCallback) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mRemovalCallback = removalCallback;
                this.mRemovalFace = face;
                this.mService.remove(this.mToken, face.getFaceId(), face.getGroupId(), i, this.mServiceReceiver);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in remove");
                if (removalCallback != null) {
                    removalCallback.onRemovalError(face, 2, null);
                }
            }
        }
    }

    public void remove(Face face, RemovalCallback removalCallback) {
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mRemovalCallback = removalCallback;
                this.mRemovalFace = face;
                this.mService.remove(this.mToken, face.getFaceId(), face.getGroupId(), getCurrentUserId(), this.mServiceReceiver);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in remove");
                if (removalCallback != null) {
                    removalCallback.onRemovalError(face, 2, null);
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

    public void requestSessionClose() {
        if (ensureServiceConnected()) {
            if (this.mService != null) {
                try {
                    this.mService.requestSessionClose();
                } catch (RemoteException e) {
                    Log.m35v(TAG, "Remote exception in requestSessionClose()");
                }
            } else {
                Log.m37w(TAG, "resetTimeout(): Service not connected!");
            }
        }
    }

    public void requestSessionOpen() {
        if (ensureServiceConnected()) {
            if (this.mService != null) {
                try {
                    this.mService.requestSessionOpen();
                } catch (RemoteException e) {
                    Log.m35v(TAG, "Remote exception in requestSessionOpen()");
                }
            } else {
                Log.m37w(TAG, "resetTimeout(): Service not connected!");
            }
        }
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
        if (ensureServiceConnected() && this.mService != null) {
            try {
                this.mService.setActiveUser(i);
            } catch (RemoteException e) {
                Log.m37w(TAG, "Remote exception in setActiveUser");
            }
        }
    }
}
