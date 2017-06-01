package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.SemSmartGlow;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TimedRemoteCaller;
import com.android.internal.util.IState;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.samsung.android.content.smartclip.SemSmartClipMetaTagType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class LppLocationManager {
    private static final int CHECK_GPS_WIFI_AVAILABILITY = 40;
    private static final int FIRST_TIME_GPS_TIMEOUT = 40;
    private static final int GPSBATCH_ENTRY_COUNT = 4;
    private static final int GPS_BATCH_REQ_TIMEOUT = 5;
    private static final int INDOOR_ENTRY_NO_GPS_COUNT = 3;
    private static final int LOC_VALID_ACCURACY_GPS = 16;
    private static final int LOC_VALID_ACCURACY_NW = 40;
    private static final int LOC_VALID_TIME_GPS = 3;
    private static final int NLP_TIMEOUT = 4;
    private static final int OUTDOOR_ENTRY_GPS_COUNT = 3;
    private static final int OUTDOOR_EXIT_ACCURACY = 50;
    private static final int PASSIVE_INACTIVE_TIME = 20;
    public static final float PASSIVE_LOC_ACC_VALIDITY = 32.0f;
    private static final double PASSIVE_LOC_DIST_VALIDITY = 10.0d;
    private static final int PASSIVE_LOC_MIN_TIME = 5;
    private static final int PASSIVE_LOC_VALIDITY = 3;
    private static final String TAG = "LppLocationManager";
    static final Msg[] vals = Msg.values();
    int count = 0;
    private HandlerThread handlerThread = null;
    private Context mContext = null;
    private final LocationListener mFindGps = new FindGps();
    private final GpsStatusListener mGpsStatusLnr = new GpsStatusListener();
    private long mGpsTimeout;
    private Location mLastLoc = null;
    private final ArrayList<Location> mListLoc = new ArrayList();
    private final ArrayList<Location> mListPassiveLoc = new ArrayList();
    private LppLocationManagerListener mListener;
    private LocationListener mLocLnr;
    private LocationManager mLocMgr;
    private Location mLocMostAccGps = null;
    private Location mLocNw = null;
    private Looper mLooper;
    private int mLppResolution = 0;
    private PassiveSM mPassiveSM = null;
    private LppLocManSM mStateMachine = null;
    private long mTimeRequest;

    private class FindGps implements LocationListener {
        private FindGps() {
        }

        public void onLocationChanged(Location location) {
            Log.m29d(LppLocationManager.TAG, "FindGps- onLocationChanged");
            if (location != null) {
                LppLocationManager.this.mStateMachine.sendMessage(Msg.GPS_AVAILABLE.ordinal(), new Location(location));
            }
        }

        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private class GpsNmeaListener implements NmeaListener {
        private GpsNmeaListener() {
        }

        public void onNmeaReceived(long j, String str) {
            LppLocationManager.this.mListener.logNmeaData(j + "\t" + str);
        }
    }

    private static class GpsStatusListener implements Listener {
        private GpsStatusListener() {
        }

        public void onGpsStatusChanged(int i) {
            switch (i) {
                case 1:
                    Log.m29d(LppLocationManager.TAG, "GPS engine started");
                    return;
                case 2:
                    Log.m29d(LppLocationManager.TAG, "GPS engine stopped");
                    return;
                case 3:
                    Log.m29d(LppLocationManager.TAG, "GPS engine calcuates first fix");
                    return;
                default:
                    return;
            }
        }
    }

    private enum LocValidity {
        VALID,
        INVALID_TIME,
        INVALID_ACC
    }

    private class LppLocManSM extends StateMachine {
        private boolean firstTimeGps = true;
        private AllNM mAllNM = null;
        private GpsBatch mGpsBatch = null;
        private Indoor mIndoor = null;
        private Outdoor mOutdoor = null;
        private Restricted mRestricted = null;
        private StatNM mStatNM = null;
        private VehNM mVehNM = null;
        private WalkNM mWalkNM = null;

        class AllNM extends State {
            private static final /* synthetic */ int[] f134xcb89281e = null;
            final /* synthetic */ int[] f135x3d432ea6;

            private static /* synthetic */ int[] m89x915c5fa() {
                if (f134xcb89281e != null) {
                    return f134xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 6;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 1;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 7;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 8;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 9;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 2;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 10;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 11;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 12;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 13;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 14;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 15;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 16;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 17;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 19;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 20;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 3;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 21;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 4;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 5;
                } catch (NoSuchFieldError e21) {
                }
                f134xcb89281e = iArr;
                return iArr;
            }

            AllNM() {
            }

            public void enter() {
                Log.m29d(LppLocationManager.TAG, "Entering " + getName());
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m89x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        LppLocationManager.this.mListener.gpsOnBatchStopped();
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        LppLocManSM.this.removeMessages(Msg.GPS_NOT_AVAILABLE.ordinal());
                        break;
                    case 2:
                        LppLocationManager.this.mListener.gpsOffBatchStopped();
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        break;
                    case 3:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mStatNM);
                        break;
                    case 4:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mVehNM);
                        break;
                    case 5:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class GpsBatch extends State {
            private static final /* synthetic */ int[] f136xcb89281e = null;
            final /* synthetic */ int[] f137x3d432ea6;
            private long deliveredT = System.currentTimeMillis();
            private boolean exit = false;
            private final ArrayList<Location> mListBatchLoc = new ArrayList();
            private int period = 3;
            private final int requestId = 0;
            private boolean walk = false;

            class C10471 implements Comparator<Location> {
                C10471() {
                }

                public int compare(Location location, Location location2) {
                    return location.getTime() < location2.getTime() ? -1 : location.getTime() > location2.getTime() ? 1 : 0;
                }
            }

            private static /* synthetic */ int[] m90x915c5fa() {
                if (f136xcb89281e != null) {
                    return f136xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 8;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 9;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 10;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 11;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 1;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 12;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 13;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 2;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 14;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 15;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 16;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 17;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 3;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 19;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 20;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 21;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 4;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 5;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 6;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 7;
                } catch (NoSuchFieldError e21) {
                }
                f136xcb89281e = iArr;
                return iArr;
            }

            GpsBatch() {
            }

            public void enter() {
                Log.m35v(LppLocationManager.TAG, "Entering " + getName());
                if (LppLocationManager.this.mLppResolution == 0) {
                    this.period = 3;
                } else if (LppLocationManager.this.mLppResolution == 1) {
                    this.period = 2;
                } else {
                    this.period = 1;
                }
                Log.m31e(LppLocationManager.TAG, "error in start batch:0");
                LppLocManSM.this.transitionTo(LppLocManSM.this.mVehNM);
                this.exit = false;
                this.walk = false;
                this.mListBatchLoc.clear();
                this.deliveredT = System.currentTimeMillis();
            }

            public void exit() {
                Log.m29d(LppLocationManager.TAG, "Exiting " + getName());
                LppLocManSM.this.removeMessages(Msg.GPS_BATCH_TIMEOUT.ordinal());
                LppLocationManager.this.mListener.gpsBatchStopped();
                if (LppLocationManager.this.mLocMgr.isProviderEnabled("gps")) {
                    LppLocationManager.this.mLocMgr.requestSingleUpdate("gps", LppLocationManager.this.mFindGps, LppLocationManager.this.mLooper);
                    LppLocManSM.this.sendMessageDelayed(Msg.GPS_NOT_AVAILABLE.ordinal(), LppLocationManager.this.mGpsTimeout * 1000);
                } else {
                    LppLocationManager.this.mListener.gpsOffBatchStopped();
                }
                LppLocationManager.this.mPassiveSM.sendMessage(Msg.GPS_BATCH_ENDED.ordinal());
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m90x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        LppLocationManager.this.mListener.locationNotFound();
                        if (this.exit) {
                            if (!this.walk) {
                                LppLocManSM.this.transitionTo(LppLocManSM.this.mStatNM);
                                break;
                            }
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                            break;
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mVehNM);
                        break;
                    case 2:
                        ArrayList arrayList = new ArrayList();
                        LppLocManSM.this.removeMessages(Msg.GPS_BATCH_TIMEOUT.ordinal());
                        arrayList.clear();
                        arrayList.addAll(this.mListBatchLoc);
                        Log.m29d(LppLocationManager.TAG, "num of batch locs:" + this.mListBatchLoc.size());
                        if (LppLocationManager.this.mListPassiveLoc.size() != 0) {
                            synchronized (this) {
                                arrayList.addAll(LppLocationManager.this.mListPassiveLoc);
                                LppLocationManager.this.mListPassiveLoc.clear();
                            }
                        }
                        Collections.sort(arrayList, new C10471());
                        Log.m29d(LppLocationManager.TAG, "deliveredT:" + this.deliveredT);
                        int i = 0;
                        while (i < arrayList.size() && ((Location) arrayList.get(i)).getTime() < this.deliveredT) {
                            i++;
                        }
                        for (int i2 = 0; i2 < i; i2++) {
                            arrayList.remove(0);
                        }
                        if (this.mListBatchLoc.size() > 0) {
                            Location location = (Location) this.mListBatchLoc.get(0);
                            for (Location location2 : this.mListBatchLoc) {
                                if (location2.getTime() > location.getTime()) {
                                    location = location2;
                                }
                            }
                            Location location3 = new Location(location);
                            this.mListBatchLoc.clear();
                            LppLocationManager.this.mListener.batchLocUpdate(location3);
                            LppLocationManager.this.mListener.batchLocListUpdate(arrayList);
                            this.deliveredT = System.currentTimeMillis();
                        }
                        if (this.exit) {
                            if (!this.walk) {
                                LppLocManSM.this.transitionTo(LppLocManSM.this.mStatNM);
                                break;
                            }
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                            break;
                        }
                        break;
                    case 3:
                        if (!this.exit) {
                            LppLocManSM.this.sendMessageDelayed(Msg.GPS_BATCH_TIMEOUT.ordinal(), (long) TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                            break;
                        }
                        break;
                    case 4:
                        break;
                    case 5:
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 6:
                        break;
                    case 7:
                        this.walk = true;
                        break;
                    default:
                        return false;
                }
                LppLocManSM.this.removeMessages(Msg.GPS_BATCH_TIMEOUT.ordinal());
                LppLocManSM.this.sendMessageDelayed(Msg.GPS_BATCH_TIMEOUT.ordinal(), (long) TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
                this.exit = true;
                return true;
            }
        }

        class Indoor extends State {
            private static final /* synthetic */ int[] f138xcb89281e = null;
            final /* synthetic */ int[] f139x3d432ea6;

            private static /* synthetic */ int[] m91x915c5fa() {
                if (f138xcb89281e != null) {
                    return f138xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 6;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 7;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 8;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 9;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 10;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 11;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 12;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 13;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 14;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 1;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 15;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 16;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 2;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 17;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 3;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 19;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 20;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 4;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 21;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 5;
                } catch (NoSuchFieldError e21) {
                }
                f138xcb89281e = iArr;
                return iArr;
            }

            Indoor() {
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_NLP_TIMEOUT.ordinal());
                LppLocationManager.this.mLocNw = null;
            }

            public void enter() {
                Log.m35v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocNw = null;
                LppLocationManager.this.mListener.gpsUnavailable();
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m91x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        break;
                    case 2:
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                        LppLocationManager.this.sendStatus("requestSingleUpdate, timeout:4");
                        if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                            LppLocationManager.this.mLocMgr.requestSingleUpdate("network", LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                        }
                        LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_NLP_TIMEOUT.ordinal(), 4000);
                        break;
                    case 3:
                        if (LppLocationManager.this.mLocNw != null) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.m31e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                        break;
                    case 4:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 5:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class Outdoor extends State {
            private static final /* synthetic */ int[] f140xcb89281e = null;
            final /* synthetic */ int[] f141x3d432ea6;

            private static /* synthetic */ int[] m92x915c5fa() {
                if (f140xcb89281e != null) {
                    return f140xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 6;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 7;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 8;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 9;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 10;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 11;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 12;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 13;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 1;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 14;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 15;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 16;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 2;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 3;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 17;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 19;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 20;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 4;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 21;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 5;
                } catch (NoSuchFieldError e21) {
                }
                f140xcb89281e = iArr;
                return iArr;
            }

            Outdoor() {
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
            }

            public void enter() {
                Log.m35v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mListener.gpsAvailable();
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m92x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            Log.m31e(LppLocationManager.TAG, "mLocMostAccGps is null");
                            LppLocationManager.this.mListener.locationNotFound();
                            break;
                        }
                        Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        break;
                    case 2:
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                        Log.m29d(LppLocationManager.TAG, "requestLocationUpdates, timeout:" + LppLocationManager.this.mGpsTimeout);
                        LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                        LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                        LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), LppLocationManager.this.mGpsTimeout * 1000);
                        break;
                    case 3:
                        if (LppLocationManager.this.mLocMostAccGps != null) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.m31e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        if (LppLocationManager.this.mLocMostAccGps == null || LppLocationManager.this.mLocMostAccGps.getAccuracy() <= 50.0f) {
                            if (LppLocationManager.this.mLocMostAccGps == null) {
                            }
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                        break;
                    case 4:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 5:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class Restricted extends State {
            private static final /* synthetic */ int[] f142xcb89281e = null;
            final /* synthetic */ int[] f143x3d432ea6;

            private static /* synthetic */ int[] m93x915c5fa() {
                if (f142xcb89281e != null) {
                    return f142xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 10;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 11;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 12;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 13;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 3;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 14;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 15;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 4;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 16;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 17;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 5;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 6;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 7;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 19;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 20;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 8;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 21;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 9;
                } catch (NoSuchFieldError e21) {
                }
                f142xcb89281e = iArr;
                return iArr;
            }

            Restricted() {
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_NLP_TIMEOUT.ordinal());
                LppLocationManager.this.mLocNw = null;
            }

            public void enter() {
                Log.m35v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocNw = null;
                LppLocationManager.this.mListener.gpsUnavailable();
                LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
            }

            public void exit() {
                Log.m29d(LppLocationManager.TAG, "Exiting " + getName());
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_NLP_TIMEOUT.ordinal());
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                LppLocManSM.this.removeMessages(Msg.CHECK_GPS_WIFI.ordinal());
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m93x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        if (!LppLocManSM.this.isWifiAvailable()) {
                            long j;
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mFindGps, LppLocationManager.this.mLooper);
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                j = 40;
                            } else {
                                j = LppLocationManager.this.mGpsTimeout;
                            }
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * j);
                            break;
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mIndoor);
                        break;
                    case 2:
                    case 3:
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mWalkNM);
                        break;
                    case 4:
                        Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        if (!LppLocManSM.this.isWifiAvailable()) {
                            LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
                            break;
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mIndoor);
                        break;
                    case 5:
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                        Log.m29d(LppLocationManager.TAG, "requestSingleUpdate, timeout:4");
                        if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                            LppLocationManager.this.mLocMgr.requestSingleUpdate("network", LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                        }
                        LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_NLP_TIMEOUT.ordinal(), 4000);
                        LppLocManSM.this.removeMessages(Msg.CHECK_GPS_WIFI.ordinal());
                        LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        break;
                    case 6:
                        LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
                        LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mFindGps);
                        break;
                    case 7:
                        if (LppLocationManager.this.mLocNw != null) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.m31e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        LppLocManSM.this.sendMessageDelayed(Msg.CHECK_GPS_WIFI.ordinal(), 40000);
                        break;
                    case 8:
                        exit();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 9:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class StatNM extends State {
            private static final /* synthetic */ int[] f144xcb89281e = null;
            final /* synthetic */ int[] f145x3d432ea6;
            private boolean nwFound = false;

            private static /* synthetic */ int[] m94x915c5fa() {
                if (f144xcb89281e != null) {
                    return f144xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 8;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 9;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 10;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 11;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 12;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 13;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 14;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 15;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 1;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 2;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 16;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 17;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 3;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 4;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 19;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 5;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 6;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 7;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 20;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 21;
                } catch (NoSuchFieldError e21) {
                }
                f144xcb89281e = iArr;
                return iArr;
            }

            StatNM() {
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeGpsStatusListener(LppLocationManager.this.mGpsStatusLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                this.nwFound = false;
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }

            public void enter() {
                Log.m35v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m94x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy");
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        break;
                    case 2:
                        this.nwFound = true;
                        break;
                    case 3:
                        if (LppLocationManager.this.mLocMgr == null) {
                            LppLocationManager.this.mLocMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
                        }
                        if (LppLocationManager.this.mLocMgr != null) {
                            long j;
                            LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                                LppLocationManager.this.mLocMgr.requestLocationUpdates("network", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            }
                            LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                j = 40;
                            } else {
                                j = LppLocationManager.this.mGpsTimeout;
                            }
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * j);
                            Log.m29d(LppLocationManager.TAG, "requestLocationUpdates,timeout:" + j);
                            break;
                        }
                        Log.m31e(LppLocationManager.TAG, "mLocMgr is null");
                        LppLocationManager.this.mListener.locationNotFound();
                        break;
                    case 4:
                        if (LppLocationManager.this.mLocMostAccGps != null && !this.nwFound) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else if (LppLocationManager.this.mLocNw != null) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.m31e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        break;
                    case 5:
                    case 6:
                        break;
                    case 7:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class VehNM extends State {
            private static final /* synthetic */ int[] f146xcb89281e = null;
            final /* synthetic */ int[] f147x3d432ea6;
            private boolean nwFound = false;
            private int reqCount = 0;

            private static /* synthetic */ int[] m95x915c5fa() {
                if (f146xcb89281e != null) {
                    return f146xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 7;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 8;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 9;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 10;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 11;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 12;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 13;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 14;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 1;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 2;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 15;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 16;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 3;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 4;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 17;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 19;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 20;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 5;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 6;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 21;
                } catch (NoSuchFieldError e21) {
                }
                f146xcb89281e = iArr;
                return iArr;
            }

            VehNM() {
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeGpsStatusListener(LppLocationManager.this.mGpsStatusLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                this.nwFound = false;
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }

            public void enter() {
                Log.m35v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
                this.reqCount = 0;
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m95x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            Log.m31e(LppLocationManager.TAG, "mLocMostAccGps is null!");
                            LppLocationManager.this.mListener.locationNotFound();
                            break;
                        }
                        Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        clear();
                        if (this.reqCount >= 4 && LppLocationManager.this.mLppResolution == 0) {
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mGpsBatch);
                            break;
                        }
                    case 2:
                        this.nwFound = true;
                        break;
                    case 3:
                        if (LppLocationManager.this.mLocMgr == null) {
                            LppLocationManager.this.mLocMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
                        }
                        if (LppLocationManager.this.mLocMgr != null) {
                            long j;
                            this.reqCount++;
                            LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                                LppLocationManager.this.mLocMgr.requestSingleUpdate("network", LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            }
                            LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                j = 40;
                            } else {
                                j = LppLocationManager.this.mGpsTimeout;
                            }
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * j);
                            Log.m29d(LppLocationManager.TAG, "requestLocationUpdates, timeout:" + j);
                            break;
                        }
                        Log.m31e(LppLocationManager.TAG, "mLocMgr is null");
                        LppLocationManager.this.mListener.locationNotFound();
                        break;
                    case 4:
                        if (LppLocationManager.this.mLocMostAccGps != null && !this.nwFound) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else if (LppLocationManager.this.mLocNw != null) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.m31e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        clear();
                        if (this.reqCount >= 4 && LppLocationManager.this.mLppResolution == 0) {
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mGpsBatch);
                            break;
                        }
                        break;
                    case 5:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 6:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class WalkNM extends State {
            private static final /* synthetic */ int[] f148xcb89281e = null;
            final /* synthetic */ int[] f149x3d432ea6;
            private int gpsCount = 0;
            private int noGpsCount = 0;
            private boolean nwFound = false;

            private static /* synthetic */ int[] m96x915c5fa() {
                if (f148xcb89281e != null) {
                    return f148xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 7;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 8;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 9;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 10;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 11;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 12;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 13;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 14;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 1;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 2;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 15;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 16;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 3;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 4;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 17;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 18;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 19;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 20;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 5;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 21;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 6;
                } catch (NoSuchFieldError e21) {
                }
                f148xcb89281e = iArr;
                return iArr;
            }

            WalkNM() {
            }

            private void clear() {
                LppLocationManager.this.mListLoc.clear();
                LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                LppLocationManager.this.mLocMgr.removeGpsStatusListener(LppLocationManager.this.mGpsStatusLnr);
                LppLocManSM.this.removeMessages(Msg.LOC_REQ_GPS_TIMEOUT.ordinal());
                this.nwFound = false;
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
            }

            public void enter() {
                Log.m35v(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mLocMostAccGps = null;
                LppLocationManager.this.mLocNw = null;
                this.noGpsCount = 0;
                this.gpsCount = 0;
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m96x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            Log.m31e(LppLocationManager.TAG, "mLocMostAccGps is null");
                            LppLocationManager.this.mListener.locationNotFound();
                            break;
                        }
                        Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                        LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        this.noGpsCount = 0;
                        this.gpsCount++;
                        clear();
                        if (this.gpsCount >= 3) {
                            LppLocManSM.this.transitionTo(LppLocManSM.this.mOutdoor);
                            break;
                        }
                        break;
                    case 2:
                        this.nwFound = true;
                        break;
                    case 3:
                        if (LppLocationManager.this.mLocMgr == null) {
                            LppLocationManager.this.mLocMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
                        }
                        if (LppLocationManager.this.mLocMgr != null) {
                            long j;
                            LppLocationManager.this.mLocMgr.removeUpdates(LppLocationManager.this.mLocLnr);
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("gps", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            if (LppLocationManager.this.mLocMgr.isProviderEnabled("network")) {
                                LppLocationManager.this.mLocMgr.requestLocationUpdates("network", 0, 0.0f, LppLocationManager.this.mLocLnr, LppLocationManager.this.mLooper);
                            }
                            LppLocationManager.this.mTimeRequest = System.currentTimeMillis();
                            if (LppLocManSM.this.firstTimeGps) {
                                LppLocManSM.this.firstTimeGps = false;
                                j = 40;
                            } else {
                                j = LppLocationManager.this.mGpsTimeout;
                            }
                            Log.m29d(LppLocationManager.TAG, "requestLocationUpdates, timeout:" + j);
                            LppLocManSM.this.sendMessageDelayed(Msg.LOC_REQ_GPS_TIMEOUT.ordinal(), 1000 * j);
                            break;
                        }
                        Log.m31e(LppLocationManager.TAG, "mLocMgr is null");
                        LppLocationManager.this.mListener.locationNotFound();
                        break;
                    case 4:
                        if (LppLocationManager.this.mLocMostAccGps != null && !this.nwFound) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion, Accuracy: " + LppLocationManager.this.mLocMostAccGps.getAccuracy() + " Provider: " + LppLocationManager.this.mLocMostAccGps.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else if (LppLocationManager.this.mLocNw != null) {
                            Log.m29d(LppLocationManager.TAG, "Send Loc to Fusion; Accuracy: " + LppLocationManager.this.mLocNw.getAccuracy() + " Provider: " + LppLocationManager.this.mLocNw.getProvider());
                            LppLocationManager.this.mListener.locUpdate(LppLocationManager.this.mListLoc);
                        } else {
                            Log.m31e(LppLocationManager.TAG, "Cannot find any location");
                            LppLocationManager.this.mListener.locationNotFound();
                        }
                        if (LppLocationManager.this.mLocMostAccGps == null) {
                            this.noGpsCount++;
                            this.gpsCount = 0;
                        } else {
                            this.noGpsCount = 0;
                            this.gpsCount++;
                        }
                        clear();
                        if (this.gpsCount < 3) {
                            if (this.noGpsCount >= 3) {
                                if (!LppLocManSM.this.isWifiAvailable()) {
                                    LppLocManSM.this.transitionTo(LppLocManSM.this.mRestricted);
                                    break;
                                }
                                LppLocManSM.this.transitionTo(LppLocManSM.this.mIndoor);
                                break;
                            }
                        }
                        LppLocManSM.this.transitionTo(LppLocManSM.this.mOutdoor);
                        break;
                        break;
                    case 5:
                        clear();
                        LppLocationManager.this.mStateMachine.exit();
                        break;
                    case 6:
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        protected LppLocManSM(String str) {
            super(str);
            Log.m29d(LppLocationManager.TAG, "Creating State Machine");
            this.mAllNM = new AllNM();
            this.mStatNM = new StatNM();
            addState(this.mStatNM, this.mAllNM);
            this.mWalkNM = new WalkNM();
            addState(this.mWalkNM, this.mAllNM);
            this.mIndoor = new Indoor();
            addState(this.mIndoor, this.mAllNM);
            this.mOutdoor = new Outdoor();
            addState(this.mOutdoor, this.mAllNM);
            this.mVehNM = new VehNM();
            addState(this.mVehNM, this.mAllNM);
            this.mGpsBatch = new GpsBatch();
            addState(this.mGpsBatch, this.mAllNM);
            this.mRestricted = new Restricted();
            addState(this.mRestricted, this.mAllNM);
            setInitialState(this.mStatNM);
        }

        private void exit() {
            quit();
        }

        private IState getState() {
            return getCurrentState();
        }

        private boolean isWifiAvailable() {
            WifiManager wifiManager = (WifiManager) LppLocationManager.this.mContext.getSystemService("wifi");
            return wifiManager != null && wifiManager.isWifiEnabled();
        }
    }

    private class MainLocationListener implements LocationListener {
        private MainLocationListener() {
        }

        public void onLocationChanged(Location location) {
            Log.m29d(LppLocationManager.TAG, "MainLocationListener - onLocationChanged:" + location);
            if (location != null) {
                Log.m33i(LppLocationManager.TAG, "loc time:" + location.getTime());
                LppLocationManager.this.mListLoc.add(new Location(location));
                if (LppLocationManager.this.mStateMachine == null) {
                    Log.m31e(LppLocationManager.TAG, "unhandled update");
                    return;
                }
                if (LppLocationManager.this.mLastLoc == null) {
                    LppLocationManager.this.mLastLoc = new Location(location);
                } else {
                    LppLocationManager.this.mLastLoc.set(location);
                }
                if (location.getProvider().equals("gps")) {
                    Log.m29d(LppLocationManager.TAG, "onLocationChanged provider : " + location.getProvider() + " Accuracy " + location.getAccuracy());
                    LocValidity -wrap0 = LppLocationManager.this.locValidCheckGps(location);
                    if (-wrap0 != LocValidity.INVALID_TIME) {
                        LppLocationManager.this.setMostAccLocGps(location);
                    }
                    if (-wrap0 == LocValidity.VALID) {
                        LppLocationManager.this.mStateMachine.sendMessage(Msg.LOC_FOUND_GPS.ordinal());
                    }
                } else {
                    LppLocationManager.this.mLocNw = new Location(location);
                    if (LppLocationManager.this.locValidCheckNw(location) == LocValidity.VALID) {
                        LppLocationManager.this.mStateMachine.sendMessage(Msg.LOC_FOUND_NETWORK.ordinal());
                    }
                }
                int i = 1;
                if (LppLocationManager.this.mLastLoc.getProvider().equals("gps")) {
                    i = 2;
                }
                LppLocationManager.this.mListener.logData("\t" + i + "\t" + location.getLatitude() + "\t" + location.getLongitude() + "\t" + location.getAltitude() + "\t" + location.getAccuracy() + "\t" + location.getTime());
            }
        }

        public void onProviderDisabled(String str) {
            Log.m37w(LppLocationManager.TAG, "onProviderDisabled:" + str);
        }

        public void onProviderEnabled(String str) {
            Log.m29d(LppLocationManager.TAG, "onProviderEnabled:" + str);
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private enum Msg {
        START,
        STATIONARY,
        WALK,
        VEHICLE,
        LOC_REQ,
        LOC_REQ_GPS_TIMEOUT,
        LOC_REQ_NLP_TIMEOUT,
        LOC_FOUND_GPS,
        LOC_FOUND_NETWORK,
        LOC_FOUND_BATCH,
        LOC_FOUND_PASSIVE,
        LOC_MGR_RETRY,
        PASSIVE_INACTIVE_TIMEOUT,
        GPS_BATCH_STARTED,
        GPS_BATCH_ENDED,
        GPS_BATCH_TIMEOUT,
        GPS_AVAILABLE,
        GPS_PASSIVE_AVAILABLE,
        GPS_NOT_AVAILABLE,
        CHECK_GPS_WIFI,
        STOP
    }

    private class PassiveSM extends StateMachine {
        private Location lastLoc;
        private Listening mListening;
        private LocationManager mLocationMgr;
        private PassGpsBatch mPassGpsBatch;
        private final LocationListener mPassLnr;
        private Pause mPause;

        class Listening extends State {
            private static final /* synthetic */ int[] f150xcb89281e = null;
            final /* synthetic */ int[] f151x3d432ea6;

            private static /* synthetic */ int[] m97x915c5fa() {
                if (f150xcb89281e != null) {
                    return f150xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 5;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 6;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 7;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 1;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 8;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 9;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 10;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 11;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 12;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 13;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 2;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 3;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 4;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 14;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 15;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 16;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 17;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 18;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 19;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 20;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 21;
                } catch (NoSuchFieldError e21) {
                }
                f150xcb89281e = iArr;
                return iArr;
            }

            Listening() {
            }

            public void enter() {
                Log.m29d(LppLocationManager.TAG, "Entering " + getName());
                if (PassiveSM.this.mLocationMgr == null) {
                    Log.m31e(LppLocationManager.TAG, "mLocationMgr is null");
                    PassiveSM.this.sendMessageDelayed(Msg.LOC_MGR_RETRY.ordinal(), (long) DateUtils.MINUTE_IN_MILLIS);
                    return;
                }
                PassiveSM.this.mLocationMgr.requestLocationUpdates("passive", TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS, 0.0f, PassiveSM.this.mPassLnr, LppLocationManager.this.mLooper);
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m97x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        PassiveSM.this.transitionTo(PassiveSM.this.mPassGpsBatch);
                        break;
                    case 2:
                        Location location = (Location) message.obj;
                        LppLocationManager.this.mListener.locPassUpdate(location);
                        PassiveSM.this.lastLoc = location;
                        break;
                    case 3:
                        PassiveSM.this.mLocationMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
                        if (PassiveSM.this.mLocationMgr != null) {
                            LppLocationManager.this.mLocMgr.requestLocationUpdates("passive", TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS, 0.0f, PassiveSM.this.mPassLnr, LppLocationManager.this.mLooper);
                            break;
                        }
                        Log.m31e(LppLocationManager.TAG, "mLocationMgr is null");
                        break;
                    case 4:
                        PassiveSM.this.transitionTo(PassiveSM.this.mPause);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        class PassGpsBatch extends State {
            private static final /* synthetic */ int[] f152xcb89281e = null;
            final /* synthetic */ int[] f153x3d432ea6;

            private static /* synthetic */ int[] m98x915c5fa() {
                if (f152xcb89281e != null) {
                    return f152xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 3;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 4;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 1;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 5;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 6;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 7;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 8;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 9;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 10;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 11;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 2;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 12;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 13;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 14;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 15;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 16;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 17;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 18;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 19;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 20;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 21;
                } catch (NoSuchFieldError e21) {
                }
                f152xcb89281e = iArr;
                return iArr;
            }

            PassGpsBatch() {
            }

            public void enter() {
                Log.m29d(LppLocationManager.TAG, "Entering " + getName());
                LppLocationManager.this.mListPassiveLoc.clear();
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m98x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        PassiveSM.this.transitionTo(PassiveSM.this.mListening);
                        break;
                    case 2:
                        Location location = (Location) message.obj;
                        if (PassiveSM.this.lastLoc == null && location.getAccuracy() < LppLocationManager.PASSIVE_LOC_ACC_VALIDITY) {
                            LppLocationManager.this.mListPassiveLoc.add(location);
                        } else if (location.getAccuracy() < LppLocationManager.PASSIVE_LOC_ACC_VALIDITY && LppLocationManager.validPassDist(PassiveSM.this.lastLoc.getLatitude(), PassiveSM.this.lastLoc.getLongitude(), location.getLatitude(), location.getLongitude())) {
                            LppLocationManager.this.mListPassiveLoc.add(location);
                        }
                        PassiveSM.this.lastLoc = new Location(location);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        private class PassiveListener implements LocationListener {
            private PassiveListener() {
            }

            public void onLocationChanged(Location location) {
                if (location != null) {
                    LppLocationManager.this.sendStatus("PassiveListener:" + location.getAccuracy());
                    LppLocationManager.this.mPassiveSM.sendMessage(Msg.LOC_FOUND_PASSIVE.ordinal(), new Location(location));
                    LppLocationManager.this.mStateMachine.sendMessage(Msg.GPS_PASSIVE_AVAILABLE.ordinal());
                }
            }

            public void onProviderDisabled(String str) {
            }

            public void onProviderEnabled(String str) {
            }

            public void onStatusChanged(String str, int i, Bundle bundle) {
            }
        }

        class Pause extends State {
            private static final /* synthetic */ int[] f154xcb89281e = null;
            final /* synthetic */ int[] f155x3d432ea6;
            boolean batchStart = false;
            boolean firstTime = true;

            private static /* synthetic */ int[] m99x915c5fa() {
                if (f154xcb89281e != null) {
                    return f154xcb89281e;
                }
                int[] iArr = new int[Msg.values().length];
                try {
                    iArr[Msg.CHECK_GPS_WIFI.ordinal()] = 3;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Msg.GPS_AVAILABLE.ordinal()] = 4;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Msg.GPS_BATCH_ENDED.ordinal()] = 5;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Msg.GPS_BATCH_STARTED.ordinal()] = 1;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Msg.GPS_BATCH_TIMEOUT.ordinal()] = 6;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Msg.GPS_NOT_AVAILABLE.ordinal()] = 7;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Msg.GPS_PASSIVE_AVAILABLE.ordinal()] = 8;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Msg.LOC_FOUND_BATCH.ordinal()] = 9;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Msg.LOC_FOUND_GPS.ordinal()] = 10;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Msg.LOC_FOUND_NETWORK.ordinal()] = 11;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[Msg.LOC_FOUND_PASSIVE.ordinal()] = 12;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[Msg.LOC_MGR_RETRY.ordinal()] = 13;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[Msg.LOC_REQ.ordinal()] = 14;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[Msg.LOC_REQ_GPS_TIMEOUT.ordinal()] = 15;
                } catch (NoSuchFieldError e14) {
                }
                try {
                    iArr[Msg.LOC_REQ_NLP_TIMEOUT.ordinal()] = 16;
                } catch (NoSuchFieldError e15) {
                }
                try {
                    iArr[Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal()] = 2;
                } catch (NoSuchFieldError e16) {
                }
                try {
                    iArr[Msg.START.ordinal()] = 17;
                } catch (NoSuchFieldError e17) {
                }
                try {
                    iArr[Msg.STATIONARY.ordinal()] = 18;
                } catch (NoSuchFieldError e18) {
                }
                try {
                    iArr[Msg.STOP.ordinal()] = 19;
                } catch (NoSuchFieldError e19) {
                }
                try {
                    iArr[Msg.VEHICLE.ordinal()] = 20;
                } catch (NoSuchFieldError e20) {
                }
                try {
                    iArr[Msg.WALK.ordinal()] = 21;
                } catch (NoSuchFieldError e21) {
                }
                f154xcb89281e = iArr;
                return iArr;
            }

            Pause() {
            }

            public void enter() {
                Log.m29d(LppLocationManager.TAG, "Entering " + getName());
                if (PassiveSM.this.mLocationMgr != null) {
                    PassiveSM.this.mLocationMgr.removeUpdates(PassiveSM.this.mPassLnr);
                }
                if (this.firstTime) {
                    PassiveSM.this.sendMessageDelayed(Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal(), 45000);
                    this.firstTime = false;
                } else {
                    PassiveSM.this.sendMessageDelayed(Msg.PASSIVE_INACTIVE_TIMEOUT.ordinal(), 20000);
                }
                this.batchStart = false;
            }

            public boolean processMessage(Message message) {
                Log.m29d(LppLocationManager.TAG, "Handling message " + LppLocationManager.vals[message.what] + " in " + getName());
                switch (m99x915c5fa()[LppLocationManager.vals[message.what].ordinal()]) {
                    case 1:
                        this.batchStart = true;
                        break;
                    case 2:
                        if (!this.batchStart) {
                            PassiveSM.this.transitionTo(PassiveSM.this.mListening);
                            break;
                        }
                        PassiveSM.this.mLocationMgr.requestLocationUpdates("passive", TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS, 0.0f, PassiveSM.this.mPassLnr, LppLocationManager.this.mLooper);
                        PassiveSM.this.transitionTo(PassiveSM.this.mPassGpsBatch);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }

        protected PassiveSM(String str) {
            super(str);
            this.mLocationMgr = null;
            this.mPassLnr = new PassiveListener();
            this.lastLoc = null;
            this.mListening = null;
            this.mPause = null;
            this.mPassGpsBatch = null;
            this.mListening = new Listening();
            addState(this.mListening);
            this.mPause = new Pause();
            addState(this.mPause);
            this.mPassGpsBatch = new PassGpsBatch();
            addState(this.mPassGpsBatch);
            setInitialState(this.mListening);
            this.mLocationMgr = (LocationManager) LppLocationManager.this.mContext.getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
        }

        private void exit() {
            if (this.mLocationMgr != null) {
                this.mLocationMgr.removeUpdates(this.mPassLnr);
            }
            quit();
        }

        private Location getLastLoc() {
            return this.lastLoc;
        }
    }

    LppLocationManager() {
    }

    private LocValidity locValidCheckGps(Location location) {
        if (System.currentTimeMillis() - this.mTimeRequest < 3000) {
            Log.m37w(TAG, "time is not enough - " + (System.currentTimeMillis() - this.mTimeRequest));
            return LocValidity.INVALID_TIME;
        } else if (location.getAccuracy() <= 16.0f) {
            return LocValidity.VALID;
        } else {
            Log.m37w(TAG, "GPS: Accuracy is not good:" + location.getAccuracy());
            return LocValidity.INVALID_ACC;
        }
    }

    private LocValidity locValidCheckNw(Location location) {
        if (location.getAccuracy() <= 40.0f) {
            return LocValidity.VALID;
        }
        Log.m37w(TAG, "N/W: Accuracy is not good:" + location.getAccuracy());
        return LocValidity.INVALID_ACC;
    }

    private void setMostAccLocGps(Location location) {
        Log.m29d(TAG, "setMostAccLoc");
        if (this.mLocMostAccGps == null) {
            this.mLocMostAccGps = new Location(location);
        } else if (this.mLocMostAccGps.getAccuracy() > location.getAccuracy()) {
            this.mLocMostAccGps.set(location);
        }
    }

    public static boolean validPassDist(double d, double d2, double d3, double d4) {
        double toRadians = Math.toRadians(d3 - d);
        double toRadians2 = Math.toRadians(d4 - d2);
        double sin = (Math.sin(toRadians / 2.0d) * Math.sin(toRadians / 2.0d)) + (((Math.sin(toRadians2 / 2.0d) * Math.sin(toRadians2 / 2.0d)) * Math.cos(Math.toRadians(d))) * Math.cos(Math.toRadians(d3)));
        double atan2 = (6371.0d * (2.0d * Math.atan2(Math.sqrt(sin), Math.sqrt(1.0d - sin)))) * 1000.0d;
        if (atan2 < PASSIVE_LOC_DIST_VALIDITY) {
            Log.m37w(TAG, "distance not valid:" + atan2);
            return false;
        }
        Log.m29d(TAG, "distance valid:" + atan2);
        return true;
    }

    public double getLastHeight() {
        return this.mLastLoc != null ? this.mLastLoc.getAltitude() : 0.0d;
    }

    public Location getLastLoc() {
        return this.mLastLoc;
    }

    public double getLastLocLat() {
        return this.mLastLoc != null ? this.mLastLoc.getLatitude() : 0.0d;
    }

    public double getLastLocLon() {
        return this.mLastLoc != null ? this.mLastLoc.getLongitude() : 0.0d;
    }

    public void locRequest(int i) {
        Log.m29d(TAG, "LocRequest");
        this.count++;
        if (this.mStateMachine != null) {
            Location -wrap0 = this.mPassiveSM.getLastLoc();
            if (-wrap0 == null || System.currentTimeMillis() - -wrap0.getTime() >= 3000 || this.mStateMachine.getState() == this.mStateMachine.mGpsBatch) {
                if (i == 1) {
                    this.mStateMachine.sendMessage(Msg.STATIONARY.ordinal());
                } else if (i == 2) {
                    this.mStateMachine.sendMessage(Msg.WALK.ordinal());
                } else if (i == 4) {
                    this.mStateMachine.sendMessage(Msg.VEHICLE.ordinal());
                }
                this.mStateMachine.sendMessage(Msg.LOC_REQ.ordinal());
                this.mPassiveSM.sendMessage(Msg.LOC_REQ.ordinal());
            } else {
                Log.m35v(TAG, "passive loc found!: " + System.currentTimeMillis() + SemSmartGlow.COLOR_PACKAGE_SEPARATOR + -wrap0.getTime());
                this.mListLoc.clear();
                this.mListLoc.add(new Location(-wrap0));
                this.mListener.locUpdate(this.mListLoc);
            }
        }
    }

    public void sendStatus(String str) {
        this.mListener.status("LppLocMan: " + str);
    }

    public void setLppResolution(int i) {
        this.mLppResolution = i;
    }

    public void start(LppConfig lppConfig, LppLocationManagerListener lppLocationManagerListener) {
        Log.m35v(TAG, "start");
        if (lppConfig == null) {
            Log.m31e(TAG, "config null");
            return;
        }
        this.mContext = lppConfig.getContext();
        if (this.mContext == null) {
            Log.m31e(TAG, "context null");
            return;
        }
        this.handlerThread = new HandlerThread("CAE_LPPLOCMGR");
        this.handlerThread.start();
        this.mLooper = this.handlerThread.getLooper();
        if (this.mLooper == null) {
            this.handlerThread.quitSafely();
            this.handlerThread = null;
            Log.m31e(TAG, "looper null");
            return;
        }
        this.mLocMgr = (LocationManager) this.mContext.getSystemService(SemSmartClipMetaTagType.GEO_LOCATION);
        if (this.mLocMgr == null) {
            Log.m31e(TAG, "mLocMgr is null");
        }
        this.mLocLnr = new MainLocationListener();
        this.mListener = lppLocationManagerListener;
        this.mGpsTimeout = (long) lppConfig.GPSKeepOn_Timer;
        this.mStateMachine = new LppLocManSM(TAG);
        this.mStateMachine.start();
        this.mStateMachine.sendMessage(Msg.START.ordinal());
        this.mPassiveSM = new PassiveSM(TAG);
        this.mPassiveSM.start();
    }

    public void stop() {
        Log.m35v(TAG, "stop");
        if (this.mStateMachine != null) {
            this.mStateMachine.sendMessage(Msg.STOP.ordinal());
        }
        if (this.mPassiveSM != null) {
            this.mPassiveSM.exit();
        }
        this.handlerThread.quitSafely();
        this.handlerThread = null;
    }
}
