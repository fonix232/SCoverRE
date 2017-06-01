package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.SemSmartGlow;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner.ContextValIndex;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class LppAggregator extends Aggregator {
    private static final int DEFAULT_LPP_RESOLUTION = 0;
    private static final int LPP_DEBUG_MSG_END = 8095150;
    private static final int LPP_DEBUG_MSG_START = 19316221;
    private static final int NEXT_APDR = 43946;
    private static final String TAG = "LppAggregator";
    private final LPPFusionListener LPPLnr = new LPPFusionListener();
    private double[] altitude;
    int count = 0;
    private final int gpsKeepOnTimer = 10;
    private final int gpsRequestApdr = 100;
    private final int gpsRequestTimer = 20;
    private double[] latitude;
    private double[] longitude;
    private ApdrRunner mApdrRunner = null;
    private LppFusion mLPPFusion = null;
    int[] mStatus = new int[]{1, 2, 2, 2, 2, 2, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2, 1};
    Handler sendApdr;
    private final String strConfigEdit = "[Note] \n";
    private final String strConfigText = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    private long[] timestamp;

    class C10463 implements Runnable {
        C10463() {
        }

        public void run() {
            if (LppAggregator.this.mLPPFusion != null) {
                LppAggregator.this.mLPPFusion.stopLpp();
            }
        }
    }

    private class LPPFusionListener implements ILppDataProvider {
        private LPPFusionListener() {
        }

        public void gpsAvailable() {
            LppAggregator.this.mApdrRunner.gpsAvailable();
        }

        public void gpsBatchStarted() {
            LppAggregator.this.mApdrRunner.gpsBatchStarted();
        }

        public void gpsOffBatchStopped() {
            LppAggregator.this.mApdrRunner.gpsOffBatchStopped();
        }

        public void gpsOnBatchStopped() {
            LppAggregator.this.mApdrRunner.gpsOnBatchStopped();
        }

        public void gpsUnavailable() {
            LppAggregator.this.mApdrRunner.gpsUnavailable();
        }

        public void lppStatus(String str) {
        }

        public void lppUpdate(ArrayList<Location> arrayList) {
            Log.m29d(LppAggregator.TAG, "LPPUpdate");
            LppAggregator.this.notifyPositionContext(arrayList);
        }

        public void onLocationChanged(Location location) {
            Log.m33i(LppAggregator.TAG, "loc time:" + location.getTime());
            if (LppAggregator.this.mApdrRunner != null) {
                LppAggregator.this.mApdrRunner.locationUpdate(location);
            }
        }
    }

    public LppAggregator(int i, Context context, Looper looper, CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, copyOnWriteArrayList, iSensorHubResetObservable);
        for (ContextComponent contextComponent : copyOnWriteArrayList) {
            if (contextComponent.getContextType().equals(ContextType.SENSORHUB_RUNNER_APDR.getCode())) {
                this.mApdrRunner = (ApdrRunner) contextComponent;
                return;
            }
        }
    }

    private long convertToUtc(int i, int i2, int i3) {
        Time syncTime = this.mApdrRunner.getSyncTime();
        Log.m29d(TAG, "syncT:" + syncTime);
        DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String str = syncTime.hour + SemSmartGlow.COLOR_PACKAGE_SEPARATOR + syncTime.minute + SemSmartGlow.COLOR_PACKAGE_SEPARATOR + syncTime.second;
        String str2 = i + SemSmartGlow.COLOR_PACKAGE_SEPARATOR + i2 + SemSmartGlow.COLOR_PACKAGE_SEPARATOR + i3;
        Date date = null;
        Date date2 = null;
        try {
            date = simpleDateFormat.parse(str);
            date2 = simpleDateFormat.parse(str2);
            Log.m29d(TAG, "syncDate:" + date);
            Log.m29d(TAG, "rxDate:" + date2);
            if (date.after(date2)) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date2);
                instance.add(5, 1);
                date2 = instance.getTime();
            }
        } catch (Throwable e) {
            Log.m31e(TAG, "time parse error");
            e.printStackTrace();
        }
        long j = 0;
        if (date2 != null) {
            j = date2.getTime() - date.getTime();
        }
        Log.m29d(TAG, "lapse:" + j);
        if (j < 0) {
            Log.m31e(TAG, "lapse is -ve");
        }
        if (j > 43200000) {
            Log.m31e(TAG, "lapse is more than 12 hours");
        }
        long toHours = TimeUnit.MILLISECONDS.toHours(j);
        long toMinutes = TimeUnit.MILLISECONDS.toMinutes(j - (((60 * toHours) * 60) * 1000));
        long j2 = (j - ((((60 * toHours) * 60) * 1000) + ((60 * toMinutes) * 1000))) / 1000;
        Log.m29d(TAG, "lapHr:" + toHours + " lapMin:" + toMinutes + " lapSec:" + j2);
        Time time = new Time(syncTime);
        time.hour = (int) (((long) time.hour) + toHours);
        time.minute = (int) (((long) time.minute) + toMinutes);
        time.second = (int) (((long) time.second) + j2);
        Log.m29d(TAG, "rxTime:" + time);
        return time.toMillis(false);
    }

    private long convertToUtc2(int i, int i2, int i3) {
        return System.currentTimeMillis();
    }

    private void notifyLppContext(int i) {
        String[] contextValueNames = getContextValueNames();
        super.getContextBean().putContext(contextValueNames[0], i);
        super.getContextBean().putContext(contextValueNames[1], this.timestamp);
        super.getContextBean().putContext(contextValueNames[2], this.latitude);
        super.getContextBean().putContext(contextValueNames[3], this.longitude);
        super.getContextBean().putContext(contextValueNames[4], this.altitude);
        super.notifyObserver();
    }

    private void notifyPositionContext(ArrayList<Location> arrayList) {
        Log.m29d(TAG, "notifyPositionContext");
        Time time = new Time();
        int i = 0;
        int size = arrayList.size();
        this.timestamp = new long[size];
        this.latitude = new double[size];
        this.longitude = new double[size];
        this.altitude = new double[size];
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            Location location = (Location) arrayList.get(i2);
            time.set(location.getTime());
            time.switchTimezone("GMT+00:00");
            this.timestamp[i2] = location.getTime();
            this.latitude[i2] = location.getLatitude();
            this.longitude[i2] = location.getLongitude();
            this.altitude[i2] = location.getAltitude();
            i++;
        }
        notifyLppContext(i);
    }

    private void test() {
        this.sendApdr = new Handler(super.getLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == LppAggregator.NEXT_APDR) {
                    Log.m29d(LppAggregator.TAG, "send apdr");
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < 1; i++) {
                        ApdrData apdrData = new ApdrData();
                        apdrData.stepFlag = 1.0d;
                        apdrData.stepLength = 0.0d;
                        apdrData.utctime = System.currentTimeMillis() + System.currentTimeMillis();
                        apdrData.mag[3] = 1.0d;
                        apdrData.movingStatus = LppAggregator.this.mStatus[new Random().nextInt(LppAggregator.this.mStatus.length)];
                        apdrData.carryPos = 0;
                        arrayList.add(apdrData);
                    }
                    LppAggregator.this.mLPPFusion.notifyApdrData(arrayList);
                }
                LppAggregator.this.sendApdr.sendEmptyMessageDelayed(LppAggregator.NEXT_APDR, 25000);
            }
        };
        this.sendApdr.sendEmptyMessageDelayed(NEXT_APDR, DateUtils.MINUTE_IN_MILLIS);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        new Handler(super.getLooper()).postDelayed(new C10463(), 0);
    }

    protected void display() {
    }

    public final void enable() {
        CaLogger.trace();
        final LppConfig lppConfig = new LppConfig(super.getContext(), 100, 20, 10);
        lppConfig.setContext(super.getContext());
        lppConfig.looper = super.getLooper();
        new Handler(super.getLooper()).postDelayed(new Runnable() {
            public void run() {
                LppAggregator.this.mLPPFusion = new LppFusion(lppConfig);
                LppAggregator.this.mLPPFusion.registerListener(LppAggregator.this.LPPLnr);
                LppAggregator.this.mLPPFusion.start();
            }
        }, 0);
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_LPP.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"TrajectoryCount", "TrajectoryTimeStamp", "TrajectoryLatitude", "TrajectoryLongitude", "TrajectoryAltitude"};
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final void initializeAggregator() {
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        boolean z = true;
        if (i == 32) {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Resolution = " + Integer.toString(intValue));
            if (intValue == 0 || intValue == 1 || intValue == 2) {
                if (this.mApdrRunner != null) {
                    this.mApdrRunner.setLppResolution(intValue);
                }
                if (this.mLPPFusion != null) {
                    this.mLPPFusion.setLppResolution(intValue);
                }
            } else {
                if (this.mLPPFusion != null) {
                    if (intValue == LPP_DEBUG_MSG_START) {
                        this.mLPPFusion.sendStatusEnable();
                    }
                    if (intValue == LPP_DEBUG_MSG_END) {
                        this.mLPPFusion.sendStatusDisable();
                    }
                }
                CaLogger.warning("Invalid value for LPP resolution");
                return false;
            }
        }
        z = false;
        return z;
    }

    protected final void terminateAggregator() {
    }

    public final void updateContext(String str, Bundle bundle) {
        CaLogger.debug("Context type " + str);
        if (str.equals(ContextType.SENSORHUB_RUNNER_APDR.getCode()) && this.mLPPFusion != null && this.mApdrRunner != null) {
            String[] contextValueNames = this.mApdrRunner.getContextValueNames();
            int i = bundle.getInt(contextValueNames[ContextValIndex.StayingArea.index()]);
            if (i != 0) {
                this.mLPPFusion.notifyStayArea(i);
                return;
            }
            int i2 = bundle.getInt(contextValueNames[ContextValIndex.Count.index()]);
            int i3 = bundle.getInt(contextValueNames[ContextValIndex.Hour.index()]);
            int i4 = bundle.getInt(contextValueNames[ContextValIndex.Minute.index()]);
            int i5 = bundle.getInt(contextValueNames[ContextValIndex.Second.index()]);
            int i6 = bundle.getInt(contextValueNames[ContextValIndex.doe.index()]);
            long[] longArray = bundle.getLongArray(contextValueNames[ContextValIndex.TimeDifference.index()]);
            int[] intArray = bundle.getIntArray(contextValueNames[ContextValIndex.IncrementEast.index()]);
            int[] intArray2 = bundle.getIntArray(contextValueNames[ContextValIndex.IncrementNorth.index()]);
            int[] intArray3 = bundle.getIntArray(contextValueNames[ContextValIndex.ActivityType.index()]);
            long convertToUtc2 = convertToUtc2(i3, i4, i5);
            ArrayList arrayList = new ArrayList();
            for (int i7 = 0; i7 < i2; i7++) {
                ApdrData apdrData = new ApdrData();
                apdrData.stepFlag = 1.0d;
                apdrData.stepLength = Math.sqrt((((double) (intArray[i7] * intArray[i7])) * 1.0d) + (((double) (intArray2[i7] * intArray2[i7])) * 1.0d));
                apdrData.utctime = convertToUtc2 + (0 + longArray[i7]);
                apdrData.apdrHeading = Math.asin(((double) intArray[i7]) / apdrData.stepLength);
                if (apdrData.apdrHeading < 0.0d) {
                    apdrData.apdrHeading += 3.141592653589793d;
                }
                apdrData.mag[3] = (double) i6;
                apdrData.movingStatus = intArray3[i7] & 15;
                apdrData.carryPos = intArray3[i7] & 240;
                arrayList.add(apdrData);
            }
            this.mLPPFusion.notifyApdrData(arrayList);
        }
    }
}
