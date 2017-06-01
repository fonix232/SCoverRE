package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner;
import com.samsung.android.contextaware.manager.CaUserInfo;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaAlarmManager;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocationAggregator extends Aggregator {
    private static final int ACCURACY_REQUIREMENT_DEFAULT = 100;
    private boolean mApdrNoti = false;
    private float mCurAccuracy;
    private double mCurAltitude;
    private double mCurLatitude;
    private double mCurLongitude;
    private long mCurSysTime;
    private long mCurTimeStamp;
    private int mPedestrianStatus;
    private int mUserWantedAccuracy;

    public LocationAggregator(int i, CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, null, null, copyOnWriteArrayList, iSensorHubResetObservable);
    }

    private boolean isFilterInitialized() {
        return true;
    }

    private void notifyLocationContext(long j, long j2, double[] dArr, float[] fArr, boolean z, int i) {
        String[] contextValueNames = getContextValueNames();
        super.getContextBean().putContext(contextValueNames[0], j);
        super.getContextBean().putContext(contextValueNames[1], j2);
        super.getContextBean().putContext(contextValueNames[2], dArr[0]);
        super.getContextBean().putContext(contextValueNames[3], dArr[1]);
        super.getContextBean().putContext(contextValueNames[4], dArr[2]);
        super.getContextBean().putContext(contextValueNames[5], fArr[0]);
        super.getContextBean().putContext(contextValueNames[6], fArr[1]);
        super.getContextBean().putContext(contextValueNames[7], fArr[2]);
        super.getContextBean().putContext(contextValueNames[8], z);
        super.getContextBean().putContext(contextValueNames[9], i);
        super.notifyObserver();
    }

    private void receiveApdrNoti(Bundle bundle) {
        if (bundle != null) {
            int i = bundle.getInt("Alert");
            if (i == 1 || i == 2) {
                this.mApdrNoti = true;
                CaLogger.trace();
                super.resume();
            }
        }
    }

    private void requestGpsData(long j, long j2) {
        double[] dArr = new double[3];
        float[] fArr = new float[3];
        boolean z = new int[1][0] == 1;
        if (z) {
            notifyLocationContext(j, j2, dArr, fArr, z, this.mPedestrianStatus);
            setCurLocationforHubApdr(j, j2, dArr[0], dArr[1], dArr[2], fArr[2]);
        }
    }

    private void sendSleepModeCmdToSensorHub() {
        ApdrRunner apdrRunner = (ApdrRunner) getSubCollectionObj(ContextType.SENSORHUB_RUNNER_APDR.getCode());
        if (apdrRunner != null) {
            this.mCurLatitude = 37.0d;
            this.mCurLongitude = 128.0d;
            this.mCurAltitude = 0.0d;
            this.mCurAccuracy = 10.0f;
            byte[] intToByteArr = CaConvertUtil.intToByteArr((int) (this.mCurLatitude * 1000000.0d), 4);
            byte[] intToByteArr2 = CaConvertUtil.intToByteArr((int) (this.mCurLongitude * 1000000.0d), 4);
            byte[] intToByteArr3 = CaConvertUtil.intToByteArr((int) (this.mCurAltitude * 1000.0d), 3);
            byte[] intToByteArr4 = CaConvertUtil.intToByteArr((int) this.mCurAccuracy, 1);
            byte[] intToByteArr5 = CaConvertUtil.intToByteArr(this.mUserWantedAccuracy, 1);
            byte[] bArr = new byte[((((intToByteArr.length + intToByteArr2.length) + intToByteArr3.length) + intToByteArr4.length) + intToByteArr5.length)];
            System.arraycopy(intToByteArr, 0, bArr, 0, intToByteArr.length);
            int length = intToByteArr.length + 0;
            System.arraycopy(intToByteArr2, 0, bArr, length, intToByteArr2.length);
            length += intToByteArr2.length;
            System.arraycopy(intToByteArr3, 0, bArr, length, intToByteArr3.length);
            length += intToByteArr3.length;
            System.arraycopy(intToByteArr4, 0, bArr, length, intToByteArr4.length);
            System.arraycopy(intToByteArr5, 0, bArr, length + intToByteArr4.length, intToByteArr5.length);
            apdrRunner.sendSleepModeCmdToSensorHub(bArr);
        }
    }

    private void setCurLocationforHubApdr(long j, long j2, double d, double d2, double d3, float f) {
        this.mCurSysTime = j;
        this.mCurTimeStamp = j2;
        this.mCurLatitude = d;
        this.mCurLongitude = d2;
        this.mCurAltitude = d3;
        this.mCurAccuracy = f;
    }

    private void updateApdrData(Bundle bundle) {
        if (isFilterInitialized() && bundle != null) {
            this.mCurSysTime += (long) bundle.getDouble("DeltaTime");
            this.mCurTimeStamp += (long) bundle.getDouble("DeltaTime");
            CaLogger.trace();
            requestGpsData(this.mCurSysTime, this.mCurTimeStamp);
        }
    }

    private void updateRawGpsData(Bundle bundle) {
        CaLogger.trace();
        requestGpsData(bundle.getLong("SystemTime"), bundle.getLong("TimeStamp"));
    }

    private void updateRawSatelliteData(Bundle bundle) {
        CaLogger.trace();
        boolean isScreenOn = CaPowerManager.getInstance().isScreenOn();
        Object obj = super.getAPStatus() == -46 ? 1 : null;
        if (!isScreenOn && isFilterInitialized()) {
            if (obj != null || this.mApdrNoti) {
                CaLogger.trace();
                CaAlarmManager.getInstance().vibrateAlarm(true);
                notifyApStatus();
                sendSleepModeCmdToSensorHub();
            }
        }
    }

    private void updateRawWpsData(Bundle bundle) {
        CaLogger.trace();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mCurSysTime = 0;
        this.mCurTimeStamp = 0;
        this.mCurLatitude = 0.0d;
        this.mCurLongitude = 0.0d;
        this.mCurAltitude = 0.0d;
        this.mCurAccuracy = 0.0f;
        this.mPedestrianStatus = -1;
        this.mApdrNoti = false;
        this.mUserWantedAccuracy = 100;
    }

    public final void disable() {
        CaLogger.trace();
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_LOCATION.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"SystemTime", "TimeStamp", "Latitude", "Longitude", "Altitude", "Heading", "Speed", "Accuracy", "Valid", "PedestrianStatus"};
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final void initializeAggregator() {
    }

    public final void pause() {
    }

    public final void resume() {
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 1) {
            this.mUserWantedAccuracy = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("setProperty (User Wanted Accuracy) = " + Integer.toString(this.mUserWantedAccuracy));
        } else if (i == 3) {
            CaUserInfo.getInstance().setUserHeight(((Double) ((ContextAwarePropertyBundle) e).getValue()).doubleValue());
            CaLogger.info("setProperty (User Height) = " + Double.toString(CaUserInfo.getInstance().getUserHeight()));
        } else if (i == 2) {
            CaUserInfo.getInstance().setUserWeight(((Double) ((ContextAwarePropertyBundle) e).getValue()).doubleValue());
            CaLogger.info("setProperty (User Weight) = " + Double.toString(CaUserInfo.getInstance().getUserWeight()));
        }
        return true;
    }

    protected final void terminateAggregator() {
    }

    protected final void updateApSleep() {
        CaLogger.trace();
        this.mApdrNoti = false;
    }

    protected final void updateApWakeup() {
        CaLogger.trace();
        this.mApdrNoti = false;
        super.updateApWakeup();
    }

    public final void updateContext(String str, Bundle bundle) {
        if (bundle != null) {
            if (str.equals(ContextType.ANDROID_RUNNER_RAW_GPS.getCode())) {
                updateRawGpsData(bundle);
            } else if (str.equals(ContextType.ANDROID_RUNNER_RAW_SATELLITE.getCode())) {
                updateRawSatelliteData(bundle);
            } else if (str.equals(ContextType.ANDROID_RUNNER_RAW_WPS.getCode())) {
                updateRawWpsData(bundle);
            } else if (str.equals(ContextType.SENSORHUB_RUNNER_APDR.getCode())) {
                if (bundle.size() > 1) {
                    updateApdrData(bundle);
                } else {
                    receiveApdrNoti(bundle);
                }
            }
        }
    }
}
