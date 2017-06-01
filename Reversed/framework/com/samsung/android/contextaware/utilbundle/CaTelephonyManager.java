package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CaTelephonyManager implements IUtilManager {
    private static final byte CALL_STATE_ACTIVE = (byte) -39;
    private static final byte CALL_STATE_IDLE = (byte) -40;
    private static int CALL_STATE_IDLE_1 = 1;
    private static int CALL_STATE_INCOMING_ANSWERED = 3;
    private static int CALL_STATE_INCOMING_MISSED = 4;
    private static int CALL_STATE_INCOMING_RINGING = 2;
    private static volatile CaTelephonyManager instance;
    private CellLocation mCellLocation;
    private final PhoneStateListener mPhoneStateListener = new C00371();
    private TelephonyManager mTelephonyManager;

    class C00371 extends PhoneStateListener {
        C00371() {
        }

        public void onCallStateChanged(int i, String str) {
            switch (i) {
                case 0:
                    CaLogger.info("CALL_STATE_IDLE");
                    CaTelephonyManager.this.sendCallStatusToSensorHub(-40);
                    return;
                case 1:
                    CaLogger.info("CALL_STATE_RINGING");
                    CaTelephonyManager.this.sendCallStatusToSensorHub(-39);
                    return;
                case 2:
                    CaLogger.info("CALL_STATE_OFFHOOK");
                    CaTelephonyManager.this.sendCallStatusToSensorHub(-39);
                    return;
                default:
                    CaLogger.info("state is unknown (state : " + Integer.toString(i) + ")");
                    return;
            }
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            CellLocation cellLocation = CaTelephonyManager.this.mTelephonyManager.getCellLocation();
            if (cellLocation != null && CaTelephonyManager.this.isCellLocationChanged(cellLocation)) {
                CaTelephonyManager.this.mCellLocation = cellLocation;
                CaTelephonyManager.this.sendCellInfoToSensorHub();
            }
        }
    }

    private static class CallStateMachine extends StateMachine {
        private final IdleState mIdleState = new IdleState();
        private final IncomingAnsweredState mIncomingAnsweredState = new IncomingAnsweredState();
        private final IncomingState mIncomingState = new IncomingState();

        class IdleState extends State {
            private static final /* synthetic */ int[] f0x57417809 = null;
            final /* synthetic */ int[] f1xcb701b9a;

            private static /* synthetic */ int[] m0xf9f63e5() {
                if (f0x57417809 != null) {
                    return f0x57417809;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.IDLE.ordinal()] = 3;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.INCOMING_RINGING.ordinal()] = 1;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.OFF_HOOK.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                f0x57417809 = iArr;
                return iArr;
            }

            IdleState() {
            }

            public void enter() {
                CaLogger.info("Entering " + getName());
            }

            public boolean processMessage(Message message) {
                CaLogger.info("Handling message " + Msg.values()[message.what] + " in " + getName());
                switch (m0xf9f63e5()[Msg.values()[message.what].ordinal()]) {
                    case 1:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIncomingState);
                        break;
                    case 2:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class IncomingAnsweredState extends State {
            private static final /* synthetic */ int[] f2x57417809 = null;
            final /* synthetic */ int[] f3xcb701b9a;

            private static /* synthetic */ int[] m1xf9f63e5() {
                if (f2x57417809 != null) {
                    return f2x57417809;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.IDLE.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.INCOMING_RINGING.ordinal()] = 3;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.OFF_HOOK.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                f2x57417809 = iArr;
                return iArr;
            }

            IncomingAnsweredState() {
            }

            public void enter() {
                CaLogger.info("Entering " + getName());
            }

            public boolean processMessage(Message message) {
                CaLogger.info("Handling message " + Msg.values()[message.what] + " in " + getName());
                switch (m1xf9f63e5()[Msg.values()[message.what].ordinal()]) {
                    case 1:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIdleState);
                        break;
                    case 2:
                        CaLogger.error("Unexpected call state");
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class IncomingState extends State {
            private static final /* synthetic */ int[] f4x57417809 = null;
            final /* synthetic */ int[] f5xcb701b9a;

            private static /* synthetic */ int[] m2xf9f63e5() {
                if (f4x57417809 != null) {
                    return f4x57417809;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.IDLE.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.INCOMING_RINGING.ordinal()] = 3;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.OFF_HOOK.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                f4x57417809 = iArr;
                return iArr;
            }

            IncomingState() {
            }

            public void enter() {
                CaLogger.info("Entering " + getName());
            }

            public boolean processMessage(Message message) {
                CaLogger.info("Handling message " + Msg.values()[message.what] + " in " + getName());
                switch (m2xf9f63e5()[Msg.values()[message.what].ordinal()]) {
                    case 1:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIdleState);
                        break;
                    case 2:
                        CallStateMachine.this.transitionTo(CallStateMachine.this.mIncomingAnsweredState);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        CallStateMachine(String str) {
            super(str);
            addState(this.mIdleState);
            addState(this.mIncomingState);
            addState(this.mIncomingAnsweredState);
            setInitialState(this.mIdleState);
        }

        public void exit() {
            quit();
        }
    }

    private enum Msg {
        IDLE("MSG_IDLE"),
        INCOMING_RINGING("MSG_INCOMING_RINGING"),
        OFF_HOOK("MSG_OFF_HOOK");
        
        private final String val;

        private Msg(String str) {
            this.val = str;
        }
    }

    public static CaTelephonyManager getInstance() {
        if (instance == null) {
            synchronized (CaTelephonyManager.class) {
                if (instance == null) {
                    instance = new CaTelephonyManager();
                }
            }
        }
        return instance;
    }

    private boolean isCellLocationChanged(CellLocation cellLocation) {
        if (this.mCellLocation != null) {
            if (this.mCellLocation instanceof GsmCellLocation) {
                if (!(cellLocation instanceof GsmCellLocation)) {
                    return true;
                }
                int cid = ((GsmCellLocation) this.mCellLocation).getCid();
                int lac = ((GsmCellLocation) this.mCellLocation).getLac();
                int cid2 = cellLocation.getCid();
                int lac2 = cellLocation.getLac();
                return (cid == cid2 || lac == lac2 || cid2 == 0 || lac2 == 0) ? false : true;
            } else if (!(this.mCellLocation instanceof CdmaCellLocation)) {
                return false;
            } else {
                if (!(cellLocation instanceof CdmaCellLocation)) {
                    return true;
                }
                int baseStationId = ((CdmaCellLocation) this.mCellLocation).getBaseStationId();
                int networkId = ((CdmaCellLocation) this.mCellLocation).getNetworkId();
                int systemId = ((CdmaCellLocation) this.mCellLocation).getSystemId();
                int baseStationId2 = cellLocation.getBaseStationId();
                int networkId2 = cellLocation.getNetworkId();
                int systemId2 = cellLocation.getSystemId();
                if (baseStationId == baseStationId2 && networkId == networkId2) {
                    if (systemId == systemId2) {
                        return false;
                    }
                }
                return (baseStationId2 == 0 || networkId2 == 0 || systemId2 == 0) ? false : true;
            }
        } else if (cellLocation instanceof GsmCellLocation) {
            return (cellLocation.getCid() == 0 || cellLocation.getLac() == 0) ? false : true;
        } else if (!(cellLocation instanceof CdmaCellLocation)) {
            return false;
        } else {
            return (cellLocation.getBaseStationId() == 0 || cellLocation.getNetworkId() == 0 || cellLocation.getSystemId() == 0) ? false : true;
        }
    }

    private void sendCallStatusToSensorHub(int i) {
        SensorHubCommManager.getInstance().sendCmdToSensorHub(new byte[]{(byte) i, (byte) 0}, (byte) -76, (byte) 17);
    }

    private void sendCellInfoToSensorHub() {
        byte[] bArr = new byte[14];
        Object obj = null;
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        bArr[0] = (byte) utcTime[0];
        bArr[1] = (byte) utcTime[1];
        bArr[2] = (byte) utcTime[2];
        int i;
        if (this.mCellLocation instanceof GsmCellLocation) {
            bArr[3] = (byte) 0;
            int cid = ((GsmCellLocation) this.mCellLocation).getCid();
            int lac = ((GsmCellLocation) this.mCellLocation).getLac();
            System.arraycopy(CaConvertUtil.intToByteArr(cid, 4), 0, bArr, 4, 4);
            System.arraycopy(CaConvertUtil.intToByteArr(lac, 2), 0, bArr, 8, 2);
            i = 8 + 2;
            System.arraycopy(CaConvertUtil.intToByteArr(0, 2), 0, bArr, i, 2);
            System.arraycopy(CaConvertUtil.intToByteArr(0, 2), 0, bArr, i + 2, 2);
            obj = 1;
        } else if (this.mCellLocation instanceof CdmaCellLocation) {
            bArr[3] = (byte) 1;
            int baseStationId = ((CdmaCellLocation) this.mCellLocation).getBaseStationId();
            int networkId = ((CdmaCellLocation) this.mCellLocation).getNetworkId();
            int systemId = ((CdmaCellLocation) this.mCellLocation).getSystemId();
            System.arraycopy(CaConvertUtil.intToByteArr(baseStationId, 2), 0, bArr, 4, 2);
            System.arraycopy(CaConvertUtil.intToByteArr(networkId, 2), 0, bArr, 6, 2);
            i = 6 + 2;
            System.arraycopy(CaConvertUtil.intToByteArr(systemId, 2), 0, bArr, i, 2);
            System.arraycopy(CaConvertUtil.intToByteArr(0, 4), 0, bArr, i + 2, 4);
            obj = 1;
        }
        if (obj != null) {
            SensorHubCommManager.getInstance().sendCmdToSensorHub(bArr, (byte) -63, (byte) 17);
        }
    }

    public final void initializeManager(Context context) {
        if (context == null) {
            CaLogger.error("Context is null");
            return;
        }
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (this.mTelephonyManager == null) {
            CaLogger.error("mTelephonyManager is null");
        } else {
            this.mTelephonyManager.listen(this.mPhoneStateListener, 288);
        }
    }

    public final void terminateManager() {
        if (this.mTelephonyManager != null) {
            this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
        }
    }
}
