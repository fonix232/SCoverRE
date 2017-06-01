package com.sec.smartcard.pinservice;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.samsung.android.smartface.SmartFaceManager;
import com.sec.smartcard.pinservice.ISmartCardPinService.Stub;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SmartCardPinManager {
    private static final String BIND_PIN_SERVICE = "com.sec.smartcard.pinservice.action.BIND_SMART_CARD_PIN_SERVICE";
    private static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String LOCKSCREEN_TYPE_OTHER = "Other";
    public static final String LOCKSCREEN_TYPE_SMARTCARD = "Smartcard";
    private static final String PROVIDER_NAME = "com.sec.smartcard.manager/smartcards";
    private static final String SAMSUNG_SC_PKG_PREFIX = "com.sec.enterprise.mdm.sc.";
    private static final String[] SMARTCARD_PROJECTION = new String[]{"CardCUID"};
    private static final String TAG = "SmartCardPinManager";
    private static final String URL = "content://com.sec.smartcard.manager/smartcards";
    public static final int VERIFY_PIN_CARDASSOCIATEERROR = 8;
    public static final int VERIFY_PIN_CARDDISCONNECT = 6;
    public static final int VERIFY_PIN_CARDERROR = 5;
    public static final int VERIFY_PIN_CARDEXPIRED = 3;
    public static final int VERIFY_PIN_CARDLOCKED = 2;
    public static final int VERIFY_PIN_CONNECTIONERROR = 4;
    public static final int VERIFY_PIN_FAIL = 1;
    public static final int VERIFY_PIN_SUCCESS = 0;
    public static final int VERIFY_PIN_USERCANCEL = 7;
    private static BlockingQueue queue = null;
    private Context mContext = null;
    private boolean mIsCallbackCalled;
    private char[] mPin;
    private boolean mServiceConnectionProgress = false;
    private ISmartCardPinService mSmartCardPin;
    private ServiceConnection pinServiceConnection = new C03141();

    class C03141 implements ServiceConnection {
        C03141() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SmartCardPinManager.this.mSmartCardPin = Stub.asInterface(iBinder);
            Log.i(SmartCardPinManager.TAG, "onServiceConnected");
            SmartCardPinManager.this.mServiceConnectionProgress = false;
            if (SmartCardPinManager.queue != null) {
                try {
                    Log.i(SmartCardPinManager.TAG, "calling queue.put");
                    SmartCardPinManager.queue.put(SmartFaceManager.PAGE_BOTTOM);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            SmartCardPinManager.this.mSmartCardPin = null;
            Log.i(SmartCardPinManager.TAG, "onServiceDisconnected");
            SmartCardPinManager.this.mServiceConnectionProgress = false;
        }
    }

    public static abstract class PinCallback {
        public ISmartCardGetPinCallback mICallback = new C03161();

        class C03161 extends ISmartCardGetPinCallback.Stub {
            C03161() {
            }

            public void onUserCancelled() throws RemoteException {
                PinCallback.this.onUserCancelled();
            }

            public void onUserEnteredPin(char[] cArr) throws RemoteException {
                PinCallback.this.onUserEnteredPin(cArr);
            }

            public void onUserPinError(int i) throws RemoteException {
                PinCallback.this.onUserPinError(i);
            }
        }

        public abstract void onUserCancelled();

        public abstract void onUserEnteredPin(char[] cArr);

        public abstract void onUserPinError(int i);
    }

    public SmartCardPinManager(Context context, UserHandle userHandle) {
        Log.i(TAG, TAG);
        this.mContext = context;
        this.mSmartCardPin = null;
        queue = null;
        bindSmartCardPinService(userHandle);
    }

    public SmartCardPinManager(IBinder iBinder) {
        this.mSmartCardPin = Stub.asInterface(iBinder);
    }

    public SmartCardPinManager(UserHandle userHandle, Context context, int i) {
        Log.i(TAG, "SmartCardPinManager Sync");
        this.mContext = context;
        this.mSmartCardPin = null;
        if (i != 0) {
            queue = new ArrayBlockingQueue(1);
            return;
        }
        queue = null;
        bindSmartCardPinService(userHandle);
    }

    private void bindSmartCardPinService(UserHandle userHandle) {
        Log.i(TAG, "bindSmartCardPinService()");
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "mSmartCardPin is null");
            if (this.mServiceConnectionProgress) {
                Log.i(TAG, "binding to service is progress. new request to bind is ignored");
                return;
            }
            Intent intent = new Intent(BIND_PIN_SERVICE);
            intent.setComponent(new ComponentName("com.sec.smartcard.manager", "com.sec.smartcard.pinservice.SmartCardService"));
            Log.i(TAG, "binding to smartcard pin service");
            if (userHandle == null) {
                this.mContext.bindService(intent, this.pinServiceConnection, 1);
            } else {
                Log.i(TAG, "binding to smartcard pin service for a user handle: " + userHandle.getIdentifier());
                this.mContext.bindServiceAsUser(intent, this.pinServiceConnection, 1, userHandle);
            }
            this.mServiceConnectionProgress = true;
        }
    }

    public static boolean isCardRegistered(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Log.i(TAG, "context : " + context);
        Log.i(TAG, "content resolver : " + contentResolver);
        Cursor query = contentResolver.query(CONTENT_URI, SMARTCARD_PROJECTION, null, null, null);
        Log.i(TAG, "cursor : " + query);
        Log.i(TAG, "cursor.count : " + query.getCount());
        return query == null ? false : query.getCount() > 0;
    }

    public static boolean isSmartCardAuthenticationInstalled() {
        return false;
    }

    public static boolean isSmartCardAuthenticationInstalled(Context context) {
        if (context != null) {
            boolean z;
            for (PackageInfo packageInfo : context.getPackageManager().getInstalledPackages(0)) {
                if (context.getPackageManager().checkPermission("com.sec.smartcard.permission.SMARTCARD_ADAPTER", packageInfo.packageName) == 0) {
                    Log.i(TAG, "isSmartCardAuthenticationInstalled: True");
                    z = true;
                    break;
                }
            }
            z = false;
            return z;
        }
        Log.d(TAG, "context is null returning");
        return false;
    }

    public void bindSmartCardPinService_Sync(UserHandle userHandle) {
        Log.i(TAG, "bindSmartCardPinService_Sync()");
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "mSmartCardPin is null");
            if (this.mServiceConnectionProgress) {
                Log.i(TAG, "binding to service is progress. new request to bind is ignored");
                return;
            }
            Log.i(TAG, "binding to smartcard pin service");
            Intent intent = new Intent(BIND_PIN_SERVICE);
            intent.setComponent(new ComponentName("com.sec.smartcard.manager", "com.sec.smartcard.pinservice.SmartCardService"));
            if (userHandle == null) {
                this.mContext.bindService(intent, this.pinServiceConnection, 1);
            } else {
                Log.i(TAG, "binding to smartcard pin service for a user handle: " + userHandle.getIdentifier());
                this.mContext.bindServiceAsUser(intent, this.pinServiceConnection, 1, userHandle);
            }
            this.mServiceConnectionProgress = true;
            try {
                Log.i(TAG, "calling queue.take");
                queue.take();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void getCardLoginAttemptRemain(ISmartCardInfoCallback iSmartCardInfoCallback) throws Exception {
        if (this.mSmartCardPin == null) {
            Log.e(TAG, "unable to connect to smartcard pin service");
            throw new Exception("unable to connect to smartcard pin service");
        }
        try {
            this.mSmartCardPin.getCardLoginAttemptRemain(iSmartCardInfoCallback);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void getPin(PinCallback pinCallback) {
        try {
            this.mSmartCardPin.getPin(pinCallback.mICallback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public synchronized char[] getPinSync() {
        final ConditionVariable conditionVariable = new ConditionVariable();
        this.mPin = null;
        this.mIsCallbackCalled = false;
        getPin(new PinCallback() {
            public void onUserCancelled() {
                SmartCardPinManager.this.mPin = null;
                SmartCardPinManager.this.mIsCallbackCalled = true;
                conditionVariable.open();
            }

            public void onUserEnteredPin(char[] cArr) {
                SmartCardPinManager.this.mPin = cArr;
                SmartCardPinManager.this.mIsCallbackCalled = true;
                conditionVariable.open();
            }

            public void onUserPinError(int i) {
                SmartCardPinManager.this.mPin = null;
                SmartCardPinManager.this.mIsCallbackCalled = true;
                conditionVariable.open();
            }
        });
        if (!this.mIsCallbackCalled) {
            conditionVariable.block();
        }
        return this.mPin;
    }

    public boolean isCardRegistered() {
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "unable to connect to smartcard pin service");
            return false;
        }
        try {
            return this.mSmartCardPin.isCardRegistered();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDeviceConnectedWithCard() {
        return true;
    }

    public boolean isSmartCardAuthenticationAvailable() {
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "unable to connect to smartcard pin service");
            return false;
        }
        try {
            return this.mSmartCardPin.isSmartCardAuthenticationAvailable();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerCard(char[] cArr, ISmartCardRegisterCallback iSmartCardRegisterCallback) throws Exception {
        Log.i(TAG, "registerCard");
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "unable to connect to smartcard pin service");
            throw new Exception("unable to connect to smartcard pin service");
        }
        try {
            this.mSmartCardPin.registerCard(cArr, iSmartCardRegisterCallback);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void showCardNotRegisteredDialog() {
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "unable to connect to smartcard pin service");
            return;
        }
        try {
            this.mSmartCardPin.showCardNotRegisteredDialog();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void unRegisterCard(char[] cArr, ISmartCardRegisterCallback iSmartCardRegisterCallback) throws Exception {
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "unable to connect to smartcard pin service");
            throw new Exception("unable to connect to smartcard pin service");
        }
        try {
            this.mSmartCardPin.unRegisterCard(cArr, iSmartCardRegisterCallback);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void unbindSmartCardPinService() {
        Log.i(TAG, "unbindSmartCardPinService()");
        Log.i(TAG, "unbinding to smartcard pin service ");
        if (this.mSmartCardPin != null) {
            Log.i(TAG, "mSmartCardPin is not null");
            this.mContext.unbindService(this.pinServiceConnection);
        }
    }

    public void verifyCard(char[] cArr, ISmartCardVerifyCallback iSmartCardVerifyCallback) throws Exception {
        Log.i(TAG, "verifyCard");
        if (this.mSmartCardPin == null) {
            Log.i(TAG, "unable to connect to smartcard pin service");
            throw new Exception("unable to connect to smartcard pin service");
        }
        try {
            this.mSmartCardPin.verifyCard(cArr, iSmartCardVerifyCallback);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
