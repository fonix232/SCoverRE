package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Message;
import android.util.Log;
import android.view.InputDevice;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.contextaware.aggregator.lpp.algorithm.LppAlgorithm;
import com.samsung.android.contextaware.aggregator.lpp.log.LppLogManager;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LppFusion extends Thread {
    private static final /* synthetic */ int[] f133x1f6bc4f9 = null;
    private static final int PASSIVE_LOC_COLL_FREQ = 60;
    private static final String TAG = "LppFusion";
    private static final long locReqType = 0;
    static final StateMsg[] vals = StateMsg.values();
    String LogFromAPDR = "0   0   0   0   0   0";
    String LogFromLocM = "0   0   0   0";
    private long apdrStepNumber = 100;
    boolean flagGPSAlwaysOn = false;
    private long locRequestInterval = 45;
    private final BlockingQueue<QData> lppQ = new ArrayBlockingQueue(32);
    private final LppConfig mCfg;
    private final LocManListener mLMLnr = new LocManListener();
    private final LppAlgoLnr mLPPAloLnr = new LppAlgoLnr();
    private final ArrayList<Location> mListGPSPos = new ArrayList();
    private final ArrayList<Location> mListLPPPos = new ArrayList();
    private ILppDataProvider mListener;
    private final LppLogManager mLogManager = new LppLogManager();
    private final LppAlgorithm mLppAlgo = new LppAlgorithm();
    private final LppLocationManager mLppLm = new LppLocationManager();
    private LppFusionSM mStateMachine = null;
    private boolean sendBrFlag = false;
    String strAlgo = "\n<<LPosition>>\n\n";
    String strLM = "\n<<LoManager>>\n\n";

    private class LocManListener implements LppLocationManagerListener {
        private LocManListener() {
        }

        public void batchLocListUpdate(ArrayList<Location> arrayList) {
            LppFusion.this.addQ(QMsg.QMSG_BATCH_LOC_LIST_RXED, arrayList);
            for (Location location : arrayList) {
                if (location != null) {
                    LppFusion.this.updateLppFusionStatus("LppLocMan: Batch PosIn => " + LppFusion.this.LocationInfoString(location));
                }
            }
        }

        public void batchLocUpdate(Location location) {
            LppFusion.this.addQ(QMsg.QMSG_BATCH_LOC_RXED, location);
        }

        public void gpsAvailable() {
            LppFusion.this.mListener.gpsAvailable();
        }

        public void gpsBatchStarted() {
            LppFusion.this.mListener.gpsBatchStarted();
            LppFusion.this.mLppAlgo.setGPSBatchingStatus(true);
        }

        public void gpsBatchStopped() {
            LppFusion.this.mLppAlgo.setGPSBatchingStatus(false);
        }

        public void gpsOffBatchStopped() {
            LppFusion.this.mListener.gpsOffBatchStopped();
        }

        public void gpsOnBatchStopped() {
            LppFusion.this.mListener.gpsOnBatchStopped();
        }

        public void gpsUnavailable() {
            LppFusion.this.mListener.gpsUnavailable();
        }

        public void locPassBatchUpdate(Location location) {
            LppFusion.this.addQ(QMsg.QMSG_PASS_LOC_BATCH_RXED, location);
            if (location != null) {
                LppFusion.this.updateLppFusionStatus("LppLocMan: Passive Batch PosIn => " + LppFusion.this.LocationInfoString(location));
            }
        }

        public void locPassUpdate(Location location) {
            LppFusion.this.addQ(QMsg.QMSG_PASS_LOC_RXED, location);
            if (location != null) {
                LppFusion.this.updateLppFusionStatus("LppLocMan: Passive PosIn => " + LppFusion.this.LocationInfoString(location));
            }
        }

        public void locUpdate(ArrayList<Location> arrayList) {
            LppFusion.this.mListGPSPos.addAll(new ArrayList(arrayList));
            LppFusion.this.addQ(QMsg.QMSG_LOCATION_LIST_RXED, arrayList);
            for (Location -wrap0 : (Location[]) arrayList.toArray(new Location[arrayList.size()])) {
                LppFusion.this.updateLppFusionStatus("LppLocMan: GPS/NLP PosIn => " + LppFusion.this.LocationInfoString(-wrap0));
            }
        }

        public void locationNotFound() {
            LppFusion.this.mStateMachine.sendMessage(StateMsg.LOCATION_NOT_FOUND.ordinal());
        }

        public void logData(String str) {
            LppFusion.this.mLogManager.LogData(5, str);
        }

        public void logNmeaData(String str) {
            LppFusion.this.mLogManager.LogData(8, str);
        }

        public void status(String str) {
            LppFusion.this.strLM = str;
            LppFusion.this.updateLppFusionStatus(str);
        }
    }

    private class LppAlgoLnr implements LppAlgoListener {
        private LppAlgoLnr() {
        }

        public void logData(int i, String str) {
            LppFusion.this.mLogManager.LogData(i, str);
        }

        public void onUpdate(Location location) {
            Log.m29d(LppFusion.TAG, "LppAlgoLnr: onUpdate");
        }

        public void onUpdateLPPtraj(ArrayList<LppLocation> arrayList) {
            Log.m29d(LppFusion.TAG, "onUpdateLPPtraj");
            for (int i = 0; i < arrayList.size(); i++) {
                LppFusion.this.mListLPPPos.add(((LppLocation) arrayList.get(i)).getLoc());
            }
            LppFusion.this.mListener.lppUpdate(LppFusion.this.mListLPPPos);
            LppFusion.this.mLogManager.AddCoordinate(LppFusion.this.mListLPPPos);
            LppFusion.this.mLogManager.AddGPSCoordinate(LppFusion.this.mListGPSPos);
            LppFusion.this.mListLPPPos.clear();
            LppFusion.this.mListGPSPos.clear();
        }

        public void requestLoc() {
        }

        public void status(String str) {
            LppFusion.this.strAlgo = str;
            LppFusion.this.updateLppFusionStatus(str);
        }
    }

    private class LppFusionSM extends StateMachine {
        private LFIdleState mIdleState = null;
        private LFWaitLocState mWaitLocState = null;

        class LFIdleState extends State {
            private static final /* synthetic */ int[] f129xe9ecca59 = null;
            final /* synthetic */ int[] f130xe5794a21;

            private static /* synthetic */ int[] m86xa24ab635() {
                if (f129xe9ecca59 != null) {
                    return f129xe9ecca59;
                }
                int[] iArr = new int[StateMsg.values().length];
                try {
                    iArr[StateMsg.LOCATION_BATCH_FOUND.ordinal()] = 7;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[StateMsg.LOCATION_BATCH_LIST_FOUND.ordinal()] = 1;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[StateMsg.LOCATION_FOUND.ordinal()] = 8;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[StateMsg.LOCATION_NOT_FOUND.ordinal()] = 9;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[StateMsg.LOCATION_PASS_FOUND.ordinal()] = 2;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[StateMsg.LOCATION_PASS_IN_BATCH_FOUND.ordinal()] = 3;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[StateMsg.LOCATION_REQUEST.ordinal()] = 4;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[StateMsg.START.ordinal()] = 5;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[StateMsg.STOP.ordinal()] = 6;
                } catch (NoSuchFieldError e9) {
                }
                f129xe9ecca59 = iArr;
                return iArr;
            }

            LFIdleState() {
            }

            public void enter() {
                Log.m29d(LppFusion.TAG, "Entering " + getName());
            }

            public boolean processMessage(Message message) {
                int i = 0;
                Log.m29d(LppFusion.TAG, "Handling message " + LppFusion.vals[message.what] + " in " + getName());
                switch (m86xa24ab635()[LppFusion.vals[message.what].ordinal()]) {
                    case 1:
                        ArrayList arrayList = (ArrayList) message.obj;
                        Log.m29d(LppFusion.TAG, "batch loc list size:" + arrayList.size());
                        Location[] locationArr = (Location[]) arrayList.toArray(new Location[arrayList.size()]);
                        int length = locationArr.length;
                        while (i < length) {
                            LppFusion.this.mLppAlgo.deliverLocationData(new Location(locationArr[i]));
                            i++;
                        }
                        LppFusionSM.this.goToSleep();
                        break;
                    case 2:
                        LppFusion.this.mLppAlgo.deliverLocationData((Location) message.obj);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 3:
                        LppFusionSM.this.goToSleep();
                        break;
                    case 4:
                        LppFusion.this.mLppLm.locRequest(message.arg1);
                        LppFusion.this.mLogManager.LogData(6, "LPPAlgoLnr\t Location is requested");
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mWaitLocState);
                        break;
                    case 5:
                        LppFusion.this.mLogManager.start();
                        LppFusion.this.mLppLm.start(LppFusion.this.mCfg, LppFusion.this.mLMLnr);
                        LppFusion.this.mLppAlgo.start();
                        LppFusion.this.mListLPPPos.clear();
                        LppFusion.this.mListGPSPos.clear();
                        break;
                    case 6:
                        LppFusion.this.mLppLm.stop();
                        LppFusion.this.mLppAlgo.stop();
                        LppFusion.this.mLogManager.stop();
                        LppFusion.this.mStateMachine.exit();
                        break;
                    default:
                        Log.m29d(LppFusion.TAG, "Msg not handled");
                        return false;
                }
                return true;
            }
        }

        class LFWaitLocState extends State {
            private static final /* synthetic */ int[] f131xe9ecca59 = null;
            final /* synthetic */ int[] f132xe5794a21;

            private static /* synthetic */ int[] m87xa24ab635() {
                if (f131xe9ecca59 != null) {
                    return f131xe9ecca59;
                }
                int[] iArr = new int[StateMsg.values().length];
                try {
                    iArr[StateMsg.LOCATION_BATCH_FOUND.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[StateMsg.LOCATION_BATCH_LIST_FOUND.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[StateMsg.LOCATION_FOUND.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[StateMsg.LOCATION_NOT_FOUND.ordinal()] = 4;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[StateMsg.LOCATION_PASS_FOUND.ordinal()] = 6;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[StateMsg.LOCATION_PASS_IN_BATCH_FOUND.ordinal()] = 7;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[StateMsg.LOCATION_REQUEST.ordinal()] = 8;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[StateMsg.START.ordinal()] = 9;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[StateMsg.STOP.ordinal()] = 5;
                } catch (NoSuchFieldError e9) {
                }
                f131xe9ecca59 = iArr;
                return iArr;
            }

            LFWaitLocState() {
            }

            public void enter() {
                Log.m29d(LppFusion.TAG, "Entering " + getName());
            }

            public void exit() {
                Log.m29d(LppFusion.TAG, "Exiting " + getName());
            }

            public boolean processMessage(Message message) {
                int i = 0;
                Log.m29d(LppFusion.TAG, "Handling message " + LppFusion.vals[message.what] + " in " + getName());
                ArrayList arrayList;
                int length;
                switch (m87xa24ab635()[LppFusion.vals[message.what].ordinal()]) {
                    case 1:
                        LppFusion.this.mListener.onLocationChanged((Location) message.obj);
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 2:
                        arrayList = (ArrayList) message.obj;
                        Log.m29d(LppFusion.TAG, "batch loc list size:" + arrayList.size());
                        Location[] locationArr = (Location[]) arrayList.toArray(new Location[arrayList.size()]);
                        length = locationArr.length;
                        while (i < length) {
                            LppFusion.this.mLppAlgo.deliverLocationData(new Location(locationArr[i]));
                            i++;
                        }
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 3:
                        LppFusion.this.mLogManager.LogData(6, "LocManListener\t Location is delivered to Algo");
                        arrayList = (ArrayList) message.obj;
                        Log.m29d(LppFusion.TAG, "loc list size:" + arrayList.size());
                        Location[] locationArr2 = (Location[]) arrayList.toArray(new Location[arrayList.size()]);
                        length = locationArr2.length;
                        while (i < length) {
                            LppFusion.this.mLppAlgo.deliverLocationData(new Location(locationArr2[i]));
                            i++;
                        }
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 4:
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusionSM.this.goToSleep();
                        break;
                    case 5:
                        LppFusion.this.mLppLm.stop();
                        LppFusion.this.mLppAlgo.stop();
                        LppFusion.this.mLogManager.stop();
                        LppFusionSM.this.transitionTo(LppFusionSM.this.mIdleState);
                        LppFusion.this.mStateMachine.exit();
                        break;
                    default:
                        Log.m29d(LppFusion.TAG, "Msg not handled");
                        return false;
                }
                return true;
            }
        }

        protected LppFusionSM(String str) {
            super(str);
            Log.m29d(LppFusion.TAG, "Creating State Machine");
            this.mIdleState = new LFIdleState();
            addState(this.mIdleState);
            this.mWaitLocState = new LFWaitLocState();
            addState(this.mWaitLocState);
            setInitialState(this.mIdleState);
        }

        private void goToSleep() {
            synchronized (this) {
                Log.m29d(LppFusion.TAG, "goToSleep");
                if (LppFusion.this.lppQ.size() == 0 && !smHasMessages()) {
                    LppFusion.this.addQ(QMsg.QMSG_SLEEP);
                }
            }
        }

        private boolean smHasMessages() {
            return (LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_BATCH_LIST_FOUND.ordinal()) || LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_BATCH_FOUND.ordinal()) || LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_FOUND.ordinal()) || LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_PASS_IN_BATCH_FOUND.ordinal())) ? true : LppFusion.this.mStateMachine.getHandler().hasMessages(StateMsg.LOCATION_PASS_FOUND.ordinal());
        }

        public void exit() {
            quit();
        }
    }

    private static class QData {
        private ArrayList<ApdrData> listAPDR;
        private ArrayList<Location> listLoc;
        private Location loc;
        private QMsg msgid;

        <E> QData(QMsg qMsg, E e) {
            this.msgid = qMsg;
            if (e != null) {
                if (qMsg == QMsg.QMSG_APDR_DATA_RXED) {
                    this.listAPDR = new ArrayList();
                    for (ApdrData apdrData : (ArrayList) e) {
                        this.listAPDR.add(new ApdrData(apdrData));
                    }
                } else if (qMsg == QMsg.QMSG_PASS_LOC_RXED || qMsg == QMsg.QMSG_PASS_LOC_BATCH_RXED || qMsg == QMsg.QMSG_BATCH_LOC_RXED) {
                    this.loc = (Location) e;
                } else if (qMsg == QMsg.QMSG_BATCH_LOC_LIST_RXED || qMsg == QMsg.QMSG_LOCATION_LIST_RXED) {
                    Location[] locationArr = (Location[]) ((ArrayList) e).toArray(new Location[((ArrayList) e).size()]);
                    this.listLoc = new ArrayList();
                    for (Location location : locationArr) {
                        this.listLoc.add(new Location(location));
                    }
                }
            }
        }
    }

    private enum QMsg {
        QMSG_APDR_NOTI(4096),
        QMSG_LPPA_PAUSE(4097),
        QMSG_LPPA_RESUME(InputDevice.SOURCE_TOUCHSCREEN),
        QMSG_LPPA_STOP(4099),
        QMSG_SLEEP(4100),
        QMSG_APDR_DATA_RXED(4101),
        QMSG_LOCATION_LIST_RXED(4102),
        QMSG_BATCH_LOC_LIST_RXED(4103),
        QMSG_BATCH_LOC_RXED(4104),
        QMSG_PASS_LOC_RXED(4105),
        QMSG_PASS_LOC_BATCH_RXED(4106);
        
        private int value;

        private QMsg(int i) {
            this.value = i;
        }
    }

    private enum StateMsg {
        START,
        LOCATION_REQUEST,
        LOCATION_FOUND,
        LOCATION_BATCH_LIST_FOUND,
        LOCATION_BATCH_FOUND,
        LOCATION_PASS_FOUND,
        LOCATION_PASS_IN_BATCH_FOUND,
        LOCATION_NOT_FOUND,
        STOP
    }

    private static /* synthetic */ int[] m88x1b71fed5() {
        if (f133x1f6bc4f9 != null) {
            return f133x1f6bc4f9;
        }
        int[] iArr = new int[QMsg.values().length];
        try {
            iArr[QMsg.QMSG_APDR_DATA_RXED.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[QMsg.QMSG_APDR_NOTI.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[QMsg.QMSG_BATCH_LOC_LIST_RXED.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[QMsg.QMSG_BATCH_LOC_RXED.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[QMsg.QMSG_LOCATION_LIST_RXED.ordinal()] = 5;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[QMsg.QMSG_LPPA_PAUSE.ordinal()] = 10;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[QMsg.QMSG_LPPA_RESUME.ordinal()] = 11;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[QMsg.QMSG_LPPA_STOP.ordinal()] = 6;
        } catch (NoSuchFieldError e8) {
        }
        try {
            iArr[QMsg.QMSG_PASS_LOC_BATCH_RXED.ordinal()] = 7;
        } catch (NoSuchFieldError e9) {
        }
        try {
            iArr[QMsg.QMSG_PASS_LOC_RXED.ordinal()] = 8;
        } catch (NoSuchFieldError e10) {
        }
        try {
            iArr[QMsg.QMSG_SLEEP.ordinal()] = 9;
        } catch (NoSuchFieldError e11) {
        }
        f133x1f6bc4f9 = iArr;
        return iArr;
    }

    public LppFusion(LppConfig lppConfig) {
        Log.m35v(TAG, TAG);
        this.mLogManager.init(lppConfig);
        this.mLppAlgo.init(this.mLPPAloLnr);
        this.apdrStepNumber = (long) lppConfig.GPSRequest_APDR;
        this.locRequestInterval = (long) lppConfig.GPSRequest_Timer;
        if (this.locRequestInterval == 0) {
            this.flagGPSAlwaysOn = true;
        } else {
            this.flagGPSAlwaysOn = false;
        }
        this.mCfg = new LppConfig(lppConfig);
    }

    private String LocationInfoString(Location location) {
        return location != null ? "Time : " + location.getTime() + " Pos : " + location.getProvider() + " , " + location.getLatitude() + " , " + location.getLongitude() + " , " + location.getAltitude() + " , " + location.getAccuracy() + " , " + location.getBearing() + " , " + location.getSpeed() : MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    }

    private void addQ(QMsg qMsg) {
        addQ(qMsg, null);
    }

    private <E> void addQ(QMsg qMsg, E e) {
        Log.m29d(TAG, "addQ:" + qMsg);
        boolean offer = this.lppQ.offer(new QData(qMsg, e));
        if (qMsg == QMsg.QMSG_APDR_DATA_RXED && e != null) {
            Log.m29d(TAG, "addQ, array size: " + ((ArrayList) e).size() + " res:" + offer);
        }
        if (qMsg != QMsg.QMSG_SLEEP && getState() == Thread.State.TIMED_WAITING) {
            interrupt();
        }
    }

    private void threadSleep() {
        try {
            Log.m33i(TAG, "Sleep!");
            sleep(Long.MAX_VALUE);
            Log.m29d(TAG, "Out of Sleep!");
        } catch (InterruptedException e) {
            Log.m29d(TAG, "Out of Sleep! 2");
        }
    }

    private void updateLppFusionStatus(String str) {
        if (this.mCfg == null || !this.sendBrFlag) {
            Log.m31e(TAG, "Config is null!");
            return;
        }
        Log.m29d(TAG, str);
        Context context = this.mCfg.getContext();
        if (context != null) {
            Intent putExtra = new Intent("android.hardware.contextaware.aggregator.lpp.LppFusion").putExtra(TAG, str);
            if (putExtra != null) {
                context.sendBroadcast(putExtra);
                Log.m29d(TAG, "Intent sent");
                return;
            }
            Log.m29d(TAG, "Intent creation failed!");
            return;
        }
        Log.m31e(TAG, "Context is null");
    }

    private void waitOnQ() {
        Object obj = 1;
        while (obj != null) {
            try {
                synchronized (this) {
                    QData qData = (QData) this.lppQ.take();
                    Log.m29d(TAG, "Received msg:" + qData.msgid + " in Q:" + this.lppQ.size());
                    switch (m88x1b71fed5()[qData.msgid.ordinal()]) {
                        case 1:
                            Log.m29d(TAG, "size of APDR data " + qData.listAPDR.size());
                            int i = 0;
                            while (i < qData.listAPDR.size()) {
                                updateLppFusionStatus("Location request MovingStatus " + ((ApdrData) qData.listAPDR.get(i)).movingStatus);
                                this.apdrStepNumber = 0;
                                if (((long) (i + 1)) > this.apdrStepNumber || ((ApdrData) qData.listAPDR.get(i)).movingStatus == 4) {
                                    Log.m29d(TAG, "Location request");
                                    this.mStateMachine.sendMessage(StateMsg.LOCATION_REQUEST.ordinal(), ((ApdrData) qData.listAPDR.get(i)).movingStatus);
                                    this.mLppAlgo.deliverAPDRData(qData.listAPDR);
                                    break;
                                }
                                i++;
                            }
                            this.mLppAlgo.deliverAPDRData(qData.listAPDR);
                            break;
                        case 2:
                            break;
                        case 3:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_BATCH_LIST_FOUND.ordinal(), qData.listLoc);
                            break;
                        case 4:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_BATCH_FOUND.ordinal(), qData.loc);
                            break;
                        case 5:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_FOUND.ordinal(), qData.listLoc);
                            break;
                        case 6:
                            obj = null;
                            this.mStateMachine.sendMessage(StateMsg.STOP.ordinal());
                            break;
                        case 7:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_PASS_IN_BATCH_FOUND.ordinal(), qData.loc);
                            break;
                        case 8:
                            this.mStateMachine.sendMessage(StateMsg.LOCATION_PASS_FOUND.ordinal(), qData.loc);
                            break;
                        case 9:
                            threadSleep();
                            break;
                        default:
                            Log.m31e(TAG, "unspecified msg id");
                            break;
                    }
                }
            } catch (Throwable e) {
                Log.m31e(TAG, "IE in q");
                e.printStackTrace();
            }
        }
        Log.m29d(TAG, "polling stopped");
    }

    public void Debug_LogString(String str) {
        this.mLogManager.LogData(6, str);
    }

    public LppLogManager getLogHandle() {
        return this.mLogManager;
    }

    public void notifyApdrData(ArrayList<ApdrData> arrayList) {
        Log.m29d(TAG, "notifyApdrData");
        for (int i = 0; i < arrayList.size(); i++) {
            this.mLogManager.LogData(6, "APDR data from sensor Hub - moving status : " + ((ApdrData) arrayList.get(i)).movingStatus);
        }
        addQ(QMsg.QMSG_APDR_DATA_RXED, arrayList);
    }

    public void notifyStayArea(int i) {
        if (this.mLppAlgo != null) {
            this.mLppAlgo.setStayingAreaFlag(i);
        }
    }

    public void pauseLPP() {
        Log.m35v(TAG, "pause()");
        this.mLogManager.LogData(6, "LppFusion, pause()");
    }

    public void registerListener(ILppDataProvider iLppDataProvider) {
        this.mListener = iLppDataProvider;
        this.mLogManager.setILppDataProviderListener(iLppDataProvider);
    }

    public void resumeLPP() {
        Log.m35v(TAG, "resume()");
        this.mLogManager.LogData(6, "LppFusion, resume()");
    }

    public void run() {
        Log.m35v(TAG, "run");
        setName("LPPThread");
        this.mStateMachine = new LppFusionSM(TAG);
        this.mStateMachine.start();
        this.mStateMachine.sendMessage(StateMsg.START.ordinal());
        waitOnQ();
    }

    public void sendStatusDisable() {
        this.sendBrFlag = false;
    }

    public void sendStatusEnable() {
        this.sendBrFlag = true;
    }

    public void setLppResolution(int i) {
        if (this.mLppLm != null) {
            this.mLppLm.setLppResolution(i);
        }
        Debug_LogString("set property command from APP : " + i);
    }

    public void stopLpp() {
        Log.m29d(TAG, "LPP stop!");
        addQ(QMsg.QMSG_LPPA_STOP);
    }
}
