package com.samsung.android.fingerprint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintManager.FingerprintClientSpecBuilder;
import com.samsung.android.fingerprint.IFingerprintClient.Stub;

public class FingerprintIdentifyDialog extends Dialog implements Callback {
    private static final String TAG = "FPMS_FingerprintIdentifyDialog";
    private Context mContext = null;
    private OnDismissListener mDismissListener;
    private IFingerprintClient mFingerprintClient = new C00711();
    private FingerprintManager mFm;
    private Handler mHandler;
    private FingerprintListener mListener;
    private String mOwnName;
    private int mSecurityLevel;
    private IBinder mToken;

    class C00711 extends Stub {
        C00711() {
        }

        public void onFingerprintEvent(FingerprintEvent fingerprintEvent) throws RemoteException {
            FingerprintEvent fingerprintEvent2 = fingerprintEvent;
            if (fingerprintEvent != null && FingerprintIdentifyDialog.this.mHandler != null) {
                FingerprintIdentifyDialog.this.mHandler.sendMessage(Message.obtain(FingerprintIdentifyDialog.this.mHandler, fingerprintEvent.eventId, fingerprintEvent));
            }
        }
    }

    public interface FingerprintListener {
        void onEvent(FingerprintEvent fingerprintEvent);
    }

    public FingerprintIdentifyDialog(Context context, FingerprintListener fingerprintListener, int i) {
        super(context);
        constructFingerprintIdentifyDialog(context, fingerprintListener, i, null);
    }

    public FingerprintIdentifyDialog(Context context, FingerprintListener fingerprintListener, int i, String str) {
        super(context);
        constructFingerprintIdentifyDialog(context, fingerprintListener, i, str);
    }

    private void constructFingerprintIdentifyDialog(Context context, FingerprintListener fingerprintListener, int i, String str) {
        this.mContext = context;
        this.mListener = fingerprintListener;
        this.mSecurityLevel = i;
        this.mHandler = new Handler(this);
        this.mOwnName = str;
        this.mFm = FingerprintManager.getInstance(this.mContext, this.mSecurityLevel, str);
        registerClient();
    }

    private void registerClient() {
        if (this.mFm != null) {
            this.mToken = this.mFm.registerClient(this.mFingerprintClient, new FingerprintClientSpecBuilder("com.samsung.android.fingerprint.FingerprintIdentifyDialog").setSecurityLevel(this.mSecurityLevel).setOwnName(this.mOwnName).build());
            if (this.mToken == null) {
                Log.e(TAG, "Token value is null");
            }
        }
    }

    private void unregistreClient() {
        if (this.mFm != null && this.mToken != null) {
            this.mFm.unregisterClient(this.mToken);
            this.mToken = null;
        }
    }

    public void dismiss() {
        if (this.mDismissListener != null) {
            this.mDismissListener.onDismiss(null);
        }
        unregistreClient();
        if (this.mFm != null) {
            this.mFm.notifyAppActivityState(4, null);
        }
        super.dismiss();
    }

    public IBinder getToken() {
        return this.mToken;
    }

    public boolean handleMessage(Message message) {
        FingerprintEvent fingerprintEvent = (FingerprintEvent) message.obj;
        if (fingerprintEvent != null) {
            this.mListener.onEvent(fingerprintEvent);
            if (fingerprintEvent.eventId == 13) {
                dismiss();
            }
        } else {
            Log.e(TAG, "handleMessage: Invaild event");
        }
        return true;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
        super.setOnDismissListener(onDismissListener);
    }

    public void show() {
    }
}
