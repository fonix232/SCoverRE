package com.samsung.android.voip;

import android.content.Context;
import android.os.IVoIPCallbackInterface.Stub;
import android.os.IVoIPInterface;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class SemVoipInterfaceManager {
    public static final int CALL_STATE_ACTIVE = 6;
    public static final int CALL_STATE_ALERTING = 4;
    public static final int CALL_STATE_DIALING = 3;
    public static final int CALL_STATE_DISCONNECTED = 2;
    public static final int CALL_STATE_HOLDING = 7;
    public static final int CALL_STATE_IDLE = 1;
    public static final int CALL_STATE_RINGING = 5;
    private static CommandListener mListener;
    public static Stub voipCallback = new C02621();
    private String TAG = "SemVoipInterfaceManager";
    private IVoIPInterface mVoip = IVoIPInterface.Stub.asInterface(ServiceManager.checkService("voip"));

    static class C02621 extends Stub {
        C02621() {
        }

        public boolean answerVoIPCall() throws RemoteException {
            SemVoipInterfaceManager.mListener.onCommandReceived(1);
            return true;
        }

        public boolean hangupVoIPCall() throws RemoteException {
            SemVoipInterfaceManager.mListener.onCommandReceived(2);
            return true;
        }

        public boolean holdVoIPCall() throws RemoteException {
            SemVoipInterfaceManager.mListener.onCommandReceived(3);
            return true;
        }

        public boolean moveVoIPToTop() throws RemoteException {
            SemVoipInterfaceManager.mListener.onCommandReceived(5);
            return true;
        }

        public boolean muteVoIPCall() throws RemoteException {
            SemVoipInterfaceManager.mListener.onCommandReceived(6);
            return true;
        }

        public boolean resumeVoIPCall() throws RemoteException {
            SemVoipInterfaceManager.mListener.onCommandReceived(4);
            return true;
        }
    }

    public interface CommandListener {
        public static final int COMMAND_ANSWER_CALL = 1;
        public static final int COMMAND_DISCONNECT_CALL = 2;
        public static final int COMMAND_HOLD_CALL = 3;
        public static final int COMMAND_MOVE_TO_TOP = 5;
        public static final int COMMAND_MUTE_CALL = 6;
        public static final int COMMAND_RESUME_CALL = 4;

        void onCommandReceived(int i);
    }

    public SemVoipInterfaceManager() {
        if (this.mVoip == null) {
            Log.e(this.TAG, "Failed to connect Voip Service");
        }
    }

    public boolean createCallSession(Context context, String str, CommandListener commandListener) {
        mListener = commandListener;
        if (!(this.mVoip == null || context == null)) {
            try {
                return this.mVoip.createCallSession(context.getPackageName(), "voip", str, voipCallback);
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public boolean destroyCallSession(Context context) {
        if (!(this.mVoip == null || context == null)) {
            try {
                return this.mVoip.destroyCallSession(context.getPackageName());
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public int getCallCount(Context context) {
        if (!(this.mVoip == null || context == null)) {
            try {
                return this.mVoip.getVoIPCallCount(context.getPackageName());
            } catch (RemoteException e) {
            }
        }
        return 0;
    }

    public boolean isVoipActivated() {
        if (this.mVoip != null) {
            try {
                return this.mVoip.isVoIPActivated();
            } catch (RemoteException e) {
            }
        }
        Log.e(this.TAG, "Failed to call isVoipActivated");
        return false;
    }

    public boolean isVoipDialing() {
        if (this.mVoip != null) {
            try {
                return this.mVoip.isVoIPDialing();
            } catch (RemoteException e) {
            }
        }
        Log.e(this.TAG, "Failed to call isVoipDialing");
        return false;
    }

    public boolean isVoipIdle() {
        if (this.mVoip != null) {
            try {
                return this.mVoip.isVoIPIdle();
            } catch (RemoteException e) {
            }
        }
        Log.e(this.TAG, "Failed to call isVoipIdle");
        return false;
    }

    public boolean moveVoipToTop() {
        if (this.mVoip != null) {
            try {
                return this.mVoip.moveVoIPToTop();
            } catch (RemoteException e) {
            }
        }
        Log.e(this.TAG, "Failed to call moveVoipToTop");
        return false;
    }

    public boolean setAudioOutputToBluetoothDevice(boolean z) {
        if (this.mVoip != null) {
            try {
                return this.mVoip.setBTUserWantsAudioOn(z);
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public boolean setCallCount(Context context, int i) {
        if (!(this.mVoip == null || context == null)) {
            try {
                return this.mVoip.setVoIPCallCount(context.getPackageName(), i);
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public boolean setCallState(Context context, int i, String str) {
        if (!(this.mVoip == null || context == null)) {
            if (i == 1) {
                try {
                    return this.mVoip.setVoIPIdle(context.getPackageName());
                } catch (RemoteException e) {
                }
            } else if (i == 2) {
                return this.mVoip.setVoIPDisconnected(context.getPackageName(), str);
            } else {
                if (i == 3) {
                    return this.mVoip.setVoIPDialing(context.getPackageName(), str);
                }
                if (i == 4) {
                    return this.mVoip.setVoIPAlerting(context.getPackageName(), str);
                }
                if (i == 5) {
                    return this.mVoip.setVoIPRinging(context.getPackageName(), str);
                }
                if (i == 6) {
                    return this.mVoip.setVoIPActive(context.getPackageName(), str);
                }
                if (i == 7) {
                    return this.mVoip.setVoIPHolding(context.getPackageName(), str);
                }
            }
        }
        return false;
    }

    public boolean setMissedCallInformation(String str, long j) {
        if (this.mVoip != null) {
            try {
                this.mVoip.notifyMissedCallforVoIP(null, str, null, j);
                return true;
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public boolean setUsingBluetoothDeviceInCall(Context context, boolean z) {
        if (!(this.mVoip == null || context == null)) {
            try {
                return this.mVoip.setUseBTInVoIP(context.getPackageName(), z);
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public boolean setUsingHoldInCall(Context context, boolean z) {
        if (!(this.mVoip == null || context == null)) {
            try {
                return this.mVoip.setUseHoldInVoIP(context.getPackageName(), z);
            } catch (RemoteException e) {
            }
        }
        return false;
    }
}
