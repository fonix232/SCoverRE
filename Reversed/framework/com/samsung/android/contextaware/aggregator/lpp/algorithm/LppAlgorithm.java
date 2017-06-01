package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import android.location.Location;
import android.text.format.Time;
import android.util.Log;
import com.samsung.android.contextaware.aggregator.lpp.ApdrData;
import com.samsung.android.contextaware.aggregator.lpp.LppAlgoListener;
import com.samsung.android.contextaware.aggregator.lpp.LppLocation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class LppAlgorithm {
    int APDRMAXNUMBER = 500;
    LppLocation AlgoLocP = new LppLocation();
    LppLocation AlgoLocPP = new LppLocation();
    LppLocation AlgoLocPPP = new LppLocation();
    long Cnt_SameLocSet = 0;
    LppLocation CurrSetLoc = new LppLocation();
    LppLocation CurrentLoc = new LppLocation();
    LppLocation FilterdOldLoc = new LppLocation();
    LppLocation FilterdOlderLoc = new LppLocation();
    boolean Flag_FineLocAcquired = false;
    boolean Flag_First_loc = false;
    boolean Flag_Loc_init = false;
    LppLocation LastLMLoc;
    final double MAX_MOVEMENT_SPEED_RUN = 2.0d;
    final double MAX_MOVEMENT_SPEED_STATIONARY = 1.0d;
    final double MAX_MOVEMENT_SPEED_VEHICLE = 20.0d;
    final double MAX_MOVEMENT_SPEED_WALK = 1.0d;
    final double MIN_MOVEMENT_DISTANCE = 20.0d;
    final double MIN_UPDTAE_TIME = 5000.0d;
    LppLocation OldLoc = new LppLocation();
    double[] Origin_LLH = new double[]{1.0d, 1.0d, 100.0d};
    final double POS_SET_RADIUS = 5.0d;
    final double POS_SET_TIME = 2.0d;
    KalmanFilter PosKF = new KalmanFilter(3, 3);
    final double Position_Jump_Sec_TH = 10.0d;
    double Prev_StateTime = 0.0d;
    double[] ProcessNoisePerSecondVehicle = new double[]{10.0d, 10.0d, 0.5d};
    double[] ProcessNoisePerSecondWalk = new double[]{1.0d, 1.0d, 0.5d};
    final int RST_LOC_VALID_CHK_NEWSET = 1;
    final int RST_LOC_VALID_CHK_NOUPDATE = 0;
    final int RST_LOC_VALID_CHK_STATIONARY = 2;
    final int STATE_STATIONARY = 1;
    final int STATE_UNKNWON = 0;
    final int STATE_VEHICLE = 4;
    String TAG = "LppAlgorithm";
    final long Trajectory_Time_Gap = 20;
    final int _DATAFROMAPDR = 0;
    final int _DATAFROMGPS = 1;
    final int _DATAFROMNETWORK = 2;
    boolean flag_AlgorithmOn = false;
    int lastStatus = 1;
    long lastTrajTime = 0;
    public ArrayList<ApdrData> mAPDRResults = new ArrayList();
    public ArrayList<ApdrData> mAPDRStack = new ArrayList();
    boolean mFlagIsGPSBatchMode = false;
    boolean mFlagLocInputReady = true;
    boolean mFlagStayingArea = false;
    private final ArrayList<LppLocation> mInputPosBuf = new ArrayList();
    private final ArrayList<LppLocation> mInputPosBufSync = new ArrayList();
    LppAlgoListener mLPPLnr = null;
    private final ArrayList<LppLocation> mLPPPosition = new ArrayList();
    long time_lastSent = 0;

    class C10481 implements Comparator<LppLocation> {
        C10481() {
        }

        public int compare(LppLocation lppLocation, LppLocation lppLocation2) {
            if (lppLocation.Capturedtime == lppLocation2.Capturedtime) {
                return 0;
            }
            return lppLocation.Capturedtime > lppLocation2.Capturedtime ? 1 : -1;
        }
    }

    private int LocValidCheck(LppLocation lppLocation, LppLocation lppLocation2, boolean z) {
        ApdrData apdrData = new ApdrData();
        ApdrData apdrData2 = new ApdrData();
        for (ApdrData apdrData3 : this.mAPDRResults) {
            if (apdrData.utctime < apdrData3.utctime && apdrData3.utctime < lppLocation.getTime()) {
                apdrData.set(apdrData3);
            }
            if (apdrData2.utctime < apdrData3.utctime && apdrData3.utctime < lppLocation2.getTime()) {
                apdrData2.set(apdrData3);
            }
        }
        int i = apdrData.movingStatus;
        double d = (i == 1 || apdrData2.movingStatus == 1) ? (i == 4 || apdrData2.movingStatus == 4) ? 20.0d : 1.0d : (i == 2 || apdrData2.movingStatus == 2) ? 1.0d : (i == 3 || apdrData2.movingStatus == 3) ? 2.0d : 20.0d;
        double distanceTo = lppLocation.distanceTo(lppLocation2);
        double time = ((double) (lppLocation.getTime() - lppLocation2.getTime())) * 0.001d;
        double d2 = d * time;
        if (i == 2 && apdrData2.movingStatus == 2 && d2 > ((double) this.APDRMAXNUMBER) * 0.8d) {
            d2 = ((double) this.APDRMAXNUMBER) * 0.8d;
        }
        if (time <= 0.0d) {
            return 0;
        }
        if (lppLocation.getLoc().getProvider().compareTo("gps") == 0) {
            return 1;
        }
        if (distanceTo > d2 && lppLocation.getLoc().getAccuracy() > 50.0f) {
            for (ApdrData apdrData32 : this.mAPDRResults) {
                SendStatus("Loc Rejection : apdr time - " + apdrData32.utctime + " " + apdrData32.movingStatus);
            }
            SendStatus("Loc Rejection : currStatus - " + i + " oldStatus: " + apdrData2.movingStatus);
            SendStatus("Loc Rejection : timediff - " + time + " MAX_VELOCITY: " + d);
            SendStatus("Loc Rejection : dist - " + distanceTo + " MaxDist: " + d2);
            SendStatus("Loc Rejection :Too long distance, eliminate pos " + lppLocation.getTime() + " , dist: " + distanceTo + " MaxDist: " + d2);
            return 0;
        } else if (distanceTo <= (20.0d * time) * 3.0d) {
            return i == 1 ? 2 : 1;
        } else {
            for (ApdrData apdrData322 : this.mAPDRResults) {
                SendStatus("Loc Rejection : apdr time - " + apdrData322.utctime + " " + apdrData322.movingStatus);
            }
            SendStatus("Loc Rejection : currStatus - " + i + " oldStatus: " + apdrData2.movingStatus);
            SendStatus("Loc Rejection : timediff - " + time + " MAX_VELOCITY: " + d);
            SendStatus("Loc Rejection : dist - " + distanceTo + " MaxDist: " + d2);
            SendStatus("Loc Rejection :Too long distance, eliminate pos " + lppLocation.getTime() + " , dist: " + distanceTo + " MaxDist: " + d2);
            return 0;
        }
    }

    private void LppAlgorithmCheckAndRun(LppLocation lppLocation) {
        SendStatus("Flag_LocOK - loc time is " + lppLocation.getTime());
        if (this.OldLoc.getLoc().getProvider().equals("NOPROVIDER")) {
            this.OldLoc.set(lppLocation);
        }
        Object obj = null;
        this.Cnt_SameLocSet++;
        if (((double) this.lastTrajTime) + 10000.0d < ((double) lppLocation.getTime())) {
            obj = 1;
            this.Cnt_SameLocSet = 0;
        } else if (this.Cnt_SameLocSet > 2) {
            obj = 1;
        }
        if (obj != null) {
            this.CurrSetLoc.set(lppLocation);
            Collection arrayList = new ArrayList();
            for (ApdrData apdrData : this.mAPDRResults) {
                if (apdrData.utctime < this.OldLoc.getTime()) {
                    arrayList.add(apdrData);
                }
            }
            SendStatus("Delete ApdrData num : " + arrayList.size());
            if (arrayList.size() > 0) {
                this.mAPDRResults.removeAll(arrayList);
            }
            this.mAPDRStack.clear();
            for (ApdrData apdrData2 : this.mAPDRResults) {
                if (apdrData2.utctime < this.CurrSetLoc.getTime()) {
                    this.mAPDRStack.add(new ApdrData(apdrData2));
                }
            }
            if (this.flag_AlgorithmOn) {
                LppAlgorithmRun();
            }
            SendStatus("LppAlgorithmRun end");
            this.OldLoc.set(this.CurrSetLoc);
        }
    }

    private void LppAlgorithmRun() {
        this.AlgoLocPPP.set(this.AlgoLocPP);
        this.AlgoLocPP.set(this.OldLoc);
        this.AlgoLocP.set(this.CurrSetLoc);
        if (this.AlgoLocPPP.getLoc().getProvider().equals("NOPROVIDER")) {
            SendStatus("LppAlgorithmRun - Initial update");
            return;
        }
        estimateSinglePoint();
        makeTrajectory();
        if (this.mLPPPosition.size() < 1) {
            SendStatus("ERROR: LppAlgorithmRun() unkwon error - [mLPPPosition.size() < 1]");
            return;
        }
        this.mLPPLnr.onUpdateLPPtraj(this.mLPPPosition);
        this.mLPPPosition.clear();
    }

    private LppLocation PositionFiltering(LppLocation lppLocation) {
        LppLocation lppLocation2 = lppLocation;
        double[] dArr = new double[3];
        lppLocation.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        dArr[0] = lppLocation.getPosEastLocal();
        dArr[1] = lppLocation.getPosNorthLocal();
        dArr[2] = lppLocation.getPosUpLocal();
        if (((dArr[0] * dArr[0]) + (dArr[1] * dArr[1])) + (dArr[2] * dArr[2]) > 1.410065408E9d) {
            this.Origin_LLH[0] = lppLocation.getLatitude();
            this.Origin_LLH[1] = lppLocation.getLongitude();
            this.Origin_LLH[2] = lppLocation.getAltitude();
            lppLocation.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
            PositionFilteringInit();
            this.PosKF.setInitialState(new double[]{lppLocation.getPosEastLocal(), lppLocation.getPosNorthLocal(), lppLocation.getPosUpLocal()});
            this.Prev_StateTime = (double) lppLocation.getTime();
            return lppLocation;
        }
        SendStatus("Measurement: E : " + lppLocation.getPosEastLocal() + " N " + lppLocation.getPosNorthLocal() + " U " + lppLocation.getPosNorthLocal());
        double time = (((double) lppLocation.getTime()) - this.Prev_StateTime) * 0.001d;
        this.Prev_StateTime = (double) lppLocation.getTime();
        if (lppLocation.getMovingStatus() == 2) {
            r18 = new double[3][];
            r18[0] = new double[]{this.ProcessNoisePerSecondWalk[0] * time, 0.0d, 0.0d};
            r18[1] = new double[]{0.0d, this.ProcessNoisePerSecondWalk[1] * time, 0.0d};
            r18[2] = new double[]{0.0d, 0.0d, this.ProcessNoisePerSecondWalk[2] * time};
            if (!this.PosKF.setProcessNoise(r18)) {
                Log.m31e(this.TAG, "PositionFiltering - Process noise error");
                return null;
            }
        }
        if (lppLocation.getMovingStatus() == 4) {
            if (!this.PosKF.setProcessNoise(new double[][]{new double[]{225.0d, 0.0d, 0.0d}, new double[]{225.0d, 100.0d, 0.0d}, new double[]{0.0d, 0.0d, 25.0d}})) {
                Log.m31e(this.TAG, "PositionFiltering - Process noise error");
                return null;
            } else if (!this.PosKF.TimePropagation(time)) {
                Log.m31e(this.TAG, "PositionFiltering - TimePropagation error");
                return null;
            }
        }
        r13 = new double[3][];
        r13[0] = new double[]{lppLocation.getPosEastLocal()};
        r13[1] = new double[]{lppLocation.getPosNorthLocal()};
        r13[2] = new double[]{lppLocation.getPosUpLocal()};
        Matrix matrix = new Matrix(r13);
        double[][] dArr2;
        if (lppLocation.getAccuracy() >= 100.0f) {
            dArr2 = new double[3][];
            dArr2[0] = new double[]{(double) (lppLocation.getAccuracy() * lppLocation.getAccuracy()), 0.0d, 0.0d};
            dArr2[1] = new double[]{0.0d, (double) (lppLocation.getAccuracy() * lppLocation.getAccuracy()), 0.0d};
            dArr2[2] = new double[]{0.0d, 0.0d, (double) ((lppLocation.getAccuracy() * 10.0f) * (lppLocation.getAccuracy() * 10.0f))};
            this.PosKF.setMeasurementNoise(dArr2);
        } else if (lppLocation.getLoc().getProvider().equals("network")) {
            dArr2 = new double[3][];
            dArr2[0] = new double[]{(double) ((lppLocation.getAccuracy() / 20.0f) * (lppLocation.getAccuracy() / 20.0f)), 0.0d, 0.0d};
            dArr2[1] = new double[]{0.0d, (double) ((lppLocation.getAccuracy() / 20.0f) * (lppLocation.getAccuracy() / 20.0f)), 0.0d};
            dArr2[2] = new double[]{0.0d, 0.0d, 1.0E8d};
            this.PosKF.setMeasurementNoise(dArr2);
        } else {
            dArr2 = new double[3][];
            dArr2[0] = new double[]{(double) ((lppLocation.getAccuracy() / 100.0f) * (lppLocation.getAccuracy() / 100.0f)), 0.0d, 0.0d};
            dArr2[1] = new double[]{0.0d, (double) ((lppLocation.getAccuracy() / 100.0f) * (lppLocation.getAccuracy() / 100.0f)), 0.0d};
            dArr2[2] = new double[]{0.0d, 0.0d, (double) ((lppLocation.getAccuracy() * 5.0f) * (lppLocation.getAccuracy() * 5.0f))};
            this.PosKF.setMeasurementNoise(dArr2);
        }
        if (this.PosKF.MeasurementUpdate(matrix)) {
            double[] enu2llh = CoordinateTransform.enu2llh(this.PosKF.getCurrentState(), new double[]{(this.Origin_LLH[0] / 180.0d) * 3.141592d, (this.Origin_LLH[1] / 180.0d) * 3.141592d, this.Origin_LLH[2]});
            lppLocation.setLatitude((enu2llh[0] / 3.141592d) * 180.0d);
            lppLocation.setLongitude((enu2llh[1] / 3.141592d) * 180.0d);
            lppLocation.setAltitude(enu2llh[2]);
            return lppLocation;
        }
        Log.m31e(this.TAG, "PositionFiltering - MeasurementUpdate error");
        return null;
    }

    private void PositionFilteringInit() {
        SendStatus("PositionFilteringInit()");
        this.PosKF.setInitialCovariance(new double[][]{new double[]{100.0d, 0.0d, 0.0d}, new double[]{0.0d, 100.0d, 0.0d}, new double[]{0.0d, 0.0d, 100.0d}});
        double[][] dArr = new double[][]{new double[]{1.0d, 0.0d, 0.0d}, new double[]{0.0d, 1.0d, 0.0d}, new double[]{0.0d, 0.0d, 1.0d}};
        this.PosKF.setMeasurementMatrix(dArr);
        this.PosKF.setInitialState(new double[]{0.0d, 0.0d, 0.0d});
        this.PosKF.setMeasurementNoise(new double[][]{new double[]{16.0d, 0.0d, 0.0d}, new double[]{0.0d, 16.0d, 0.0d}, new double[]{0.0d, 0.0d, 10000.0d}});
        this.PosKF.setProcessNoise(new double[][]{new double[]{200.0d, 0.0d, 0.0d}, new double[]{0.0d, 200.0d, 0.0d}, new double[]{0.0d, 0.0d, 2.0d}});
        this.PosKF.setTransitionMatrix(dArr);
    }

    private void SendStatus(String str) {
        if (this.mLPPLnr != null) {
            this.mLPPLnr.status("LppAlgorithm: " + str);
        }
    }

    private long SetTrajStartTime(long j) {
        Time time = new Time();
        time.set(j);
        return ((((long) (((double) j) / 1000.0d)) - ((long) time.second)) + (((long) time.second) - (((((long) time.second) / 20) - 1) * 20))) * 1000;
    }

    private void estimateSinglePoint() {
        LppLocation lppLocation = null;
        if (!this.AlgoLocPP.getLoc().getProvider().equals("NOPROVIDER")) {
            LppLocation lppLocation2 = new LppLocation(this.AlgoLocPP);
            lppLocation2.setMovingStatus(4);
            lppLocation = PositionFiltering(lppLocation2);
        }
        this.FilterdOlderLoc.set(this.FilterdOldLoc);
        if (lppLocation != null) {
            this.FilterdOldLoc.set(this.AlgoLocPP);
        } else {
            SendStatus("EstimateSinglePoint(), abnormal filter output - null");
            this.FilterdOldLoc.set(this.AlgoLocPP);
        }
        this.CurrentLoc.set(this.AlgoLocP);
        this.FilterdOldLoc.estimateVelocity(this.FilterdOlderLoc, this.CurrentLoc);
    }

    private void makeTrajectory() {
        double d;
        double d2;
        double posEastLocal;
        double posNorthLocal;
        double d3;
        double d4;
        Matrix times;
        Matrix times2;
        LppLocation lppLocation;
        double d5;
        double[] dArr;
        double[] dArr2;
        double[] dArr3;
        LppLocation lppLocation2 = new LppLocation(this.FilterdOlderLoc);
        LppLocation lppLocation3 = new LppLocation(this.FilterdOldLoc);
        lppLocation2.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        lppLocation3.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        if (this.lastTrajTime == 0) {
            if (this.FilterdOlderLoc.getTime() == 0) {
                SendStatus("makeTrajectory : No need to update");
                return;
            }
            this.lastTrajTime = SetTrajStartTime(this.FilterdOlderLoc.getTime());
        }
        double time = 20.0d - (((double) (lppLocation2.getTime() - this.lastTrajTime)) * 0.001d);
        Object obj = 1;
        double time2 = ((double) lppLocation2.getTime()) * 0.001d;
        double time3 = ((double) lppLocation3.getTime()) * 0.001d;
        double d6 = (time3 - time2) * 0.2d;
        double d7 = (time3 - time2) * 0.8d;
        double d8 = time3 - time2;
        if (d8 <= 0.0d) {
            SendStatus("WARNING: makeTrajectory - abnormal t1, t2");
            obj = null;
            d = 0.0d;
            d2 = 0.0d;
        } else if (d8 > 300.0d) {
            SendStatus("WARNING: makeTrajectory - too much gap between t1 and t2");
            obj = null;
            d = 0.0d;
            d2 = 0.0d;
        } else {
            d = (lppLocation3.getPosEastLocal() - lppLocation2.getPosEastLocal()) / (time3 - time2);
            d2 = (lppLocation3.getPosNorthLocal() - lppLocation2.getPosNorthLocal()) / (time3 - time2);
        }
        double[] filteredVelocity = lppLocation2.getFilteredVelocity();
        double[] filteredVelocity2 = lppLocation3.getFilteredVelocity();
        if (!(filteredVelocity[0] == 0.0d && filteredVelocity[1] == 0.0d)) {
            if (filteredVelocity2[0] == 0.0d && filteredVelocity2[1] == 0.0d) {
            }
            if (obj != null) {
                double sqrt = Math.sqrt((d * d) + (d2 * d2));
                posEastLocal = lppLocation2.getPosEastLocal();
                posNorthLocal = lppLocation2.getPosNorthLocal();
                double posEastLocal2 = lppLocation3.getPosEastLocal();
                double posNorthLocal2 = lppLocation3.getPosNorthLocal();
                d3 = filteredVelocity[0] * sqrt;
                d4 = filteredVelocity[1] * sqrt;
                double d9 = filteredVelocity2[0] * sqrt;
                double d10 = filteredVelocity2[1] * sqrt;
                double d11 = d3;
                double d12 = posEastLocal;
                double d13 = d4;
                double d14 = posNorthLocal;
                r57 = new double[4][];
                r57[0] = new double[]{(((5.0d * d6) * d6) * d6) * d6, ((4.0d * d6) * d6) * d6, (3.0d * d6) * d6, 2.0d * d6};
                r57[1] = new double[]{(((5.0d * d7) * d7) * d7) * d7, ((4.0d * d7) * d7) * d7, (3.0d * d7) * d7, 2.0d * d7};
                r57[2] = new double[]{(((d8 * d8) * d8) * d8) * d8, ((d8 * d8) * d8) * d8, (d8 * d8) * d8, d8 * d8};
                r57[3] = new double[]{(((5.0d * d8) * d8) * d8) * d8, ((4.0d * d8) * d8) * d8, (3.0d * d8) * d8, 2.0d * d8};
                Matrix matrix = new Matrix(r57);
                double[][] dArr4 = new double[4][];
                dArr4[0] = new double[]{d - d3};
                dArr4[1] = new double[]{d - d3};
                dArr4[2] = new double[]{(posEastLocal2 - (d3 * d8)) - posEastLocal};
                dArr4[3] = new double[]{d9 - d3};
                Matrix matrix2 = new Matrix(dArr4);
                double[][] dArr5 = new double[4][];
                dArr5[0] = new double[]{d2 - d4};
                dArr5[1] = new double[]{d2 - d4};
                dArr5[2] = new double[]{(posNorthLocal2 - (d4 * d8)) - posNorthLocal};
                dArr5[3] = new double[]{d10 - d4};
                matrix2 = new Matrix(dArr5);
                times = matrix.inverse().times(matrix2);
                times2 = matrix.inverse().times(matrix2);
                lppLocation = new LppLocation(lppLocation3);
                lppLocation.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
                for (d5 = time; d5 < d8; d5 += 20.0d) {
                    lppLocation.setPosENU((((((((((times.get(0, 0) * d5) * d5) * d5) * d5) * d5) + ((((times.get(1, 0) * d5) * d5) * d5) * d5)) + (((times.get(2, 0) * d5) * d5) * d5)) + ((times.get(3, 0) * d5) * d5)) + (d3 * d5)) + posEastLocal, (((((((((times2.get(0, 0) * d5) * d5) * d5) * d5) * d5) + ((((times2.get(1, 0) * d5) * d5) * d5) * d5)) + (((times2.get(2, 0) * d5) * d5) * d5)) + ((times2.get(3, 0) * d5) * d5)) + (d4 * d5)) + posNorthLocal, lppLocation3.getAltitude());
                    lppLocation.getLoc().setTime(lppLocation2.getTime() + ((long) (1000.0d * d5)));
                    this.mLPPPosition.add(new LppLocation(lppLocation));
                    this.lastTrajTime = lppLocation.getTime();
                }
            } else if (d8 > 900.0d) {
                this.mLPPPosition.add(new LppLocation(lppLocation3));
                this.lastTrajTime = lppLocation3.getTime();
            } else if (d8 > 0.0d) {
                lppLocation = new LppLocation(lppLocation2);
                dArr = new double[2];
                dArr2 = new double[2];
                dArr3 = new double[]{lppLocation2.getPosEastLocal(), lppLocation2.getPosNorthLocal()};
                dArr2[0] = lppLocation3.getPosEastLocal();
                dArr2[1] = lppLocation3.getPosNorthLocal();
                for (d5 = time; d5 < d8; d5 += 20.0d) {
                    double d15 = d5 / d8;
                    dArr3[0] = dArr[0] + ((dArr2[0] - dArr[0]) * d15);
                    dArr3[1] = dArr[1] + ((dArr2[1] - dArr[1]) * d15);
                    lppLocation.setPosENU(dArr3[0], dArr3[1], lppLocation2.getAltitude());
                    lppLocation.getLoc().setTime(lppLocation2.getTime() + ((long) (1000.0d * d5)));
                    this.mLPPPosition.add(new LppLocation(lppLocation));
                    this.lastTrajTime = lppLocation.getTime();
                }
            }
        }
        obj = null;
        if (obj != null) {
            double sqrt2 = Math.sqrt((d * d) + (d2 * d2));
            posEastLocal = lppLocation2.getPosEastLocal();
            posNorthLocal = lppLocation2.getPosNorthLocal();
            double posEastLocal22 = lppLocation3.getPosEastLocal();
            double posNorthLocal22 = lppLocation3.getPosNorthLocal();
            d3 = filteredVelocity[0] * sqrt2;
            d4 = filteredVelocity[1] * sqrt2;
            double d92 = filteredVelocity2[0] * sqrt2;
            double d102 = filteredVelocity2[1] * sqrt2;
            double d112 = d3;
            double d122 = posEastLocal;
            double d132 = d4;
            double d142 = posNorthLocal;
            r57 = new double[4][];
            r57[0] = new double[]{(((5.0d * d6) * d6) * d6) * d6, ((4.0d * d6) * d6) * d6, (3.0d * d6) * d6, 2.0d * d6};
            r57[1] = new double[]{(((5.0d * d7) * d7) * d7) * d7, ((4.0d * d7) * d7) * d7, (3.0d * d7) * d7, 2.0d * d7};
            r57[2] = new double[]{(((d8 * d8) * d8) * d8) * d8, ((d8 * d8) * d8) * d8, (d8 * d8) * d8, d8 * d8};
            r57[3] = new double[]{(((5.0d * d8) * d8) * d8) * d8, ((4.0d * d8) * d8) * d8, (3.0d * d8) * d8, 2.0d * d8};
            Matrix matrix3 = new Matrix(r57);
            double[][] dArr42 = new double[4][];
            dArr42[0] = new double[]{d - d3};
            dArr42[1] = new double[]{d - d3};
            dArr42[2] = new double[]{(posEastLocal22 - (d3 * d8)) - posEastLocal};
            dArr42[3] = new double[]{d92 - d3};
            Matrix matrix22 = new Matrix(dArr42);
            double[][] dArr52 = new double[4][];
            dArr52[0] = new double[]{d2 - d4};
            dArr52[1] = new double[]{d2 - d4};
            dArr52[2] = new double[]{(posNorthLocal22 - (d4 * d8)) - posNorthLocal};
            dArr52[3] = new double[]{d102 - d4};
            matrix22 = new Matrix(dArr52);
            times = matrix3.inverse().times(matrix22);
            times2 = matrix3.inverse().times(matrix22);
            lppLocation = new LppLocation(lppLocation3);
            lppLocation.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
            for (d5 = time; d5 < d8; d5 += 20.0d) {
                lppLocation.setPosENU((((((((((times.get(0, 0) * d5) * d5) * d5) * d5) * d5) + ((((times.get(1, 0) * d5) * d5) * d5) * d5)) + (((times.get(2, 0) * d5) * d5) * d5)) + ((times.get(3, 0) * d5) * d5)) + (d3 * d5)) + posEastLocal, (((((((((times2.get(0, 0) * d5) * d5) * d5) * d5) * d5) + ((((times2.get(1, 0) * d5) * d5) * d5) * d5)) + (((times2.get(2, 0) * d5) * d5) * d5)) + ((times2.get(3, 0) * d5) * d5)) + (d4 * d5)) + posNorthLocal, lppLocation3.getAltitude());
                lppLocation.getLoc().setTime(lppLocation2.getTime() + ((long) (1000.0d * d5)));
                this.mLPPPosition.add(new LppLocation(lppLocation));
                this.lastTrajTime = lppLocation.getTime();
            }
        } else if (d8 > 900.0d) {
            this.mLPPPosition.add(new LppLocation(lppLocation3));
            this.lastTrajTime = lppLocation3.getTime();
        } else if (d8 > 0.0d) {
            lppLocation = new LppLocation(lppLocation2);
            dArr = new double[2];
            dArr2 = new double[2];
            dArr3 = new double[]{lppLocation2.getPosEastLocal(), lppLocation2.getPosNorthLocal()};
            dArr2[0] = lppLocation3.getPosEastLocal();
            dArr2[1] = lppLocation3.getPosNorthLocal();
            for (d5 = time; d5 < d8; d5 += 20.0d) {
                double d152 = d5 / d8;
                dArr3[0] = dArr[0] + ((dArr2[0] - dArr[0]) * d152);
                dArr3[1] = dArr[1] + ((dArr2[1] - dArr[1]) * d152);
                lppLocation.setPosENU(dArr3[0], dArr3[1], lppLocation2.getAltitude());
                lppLocation.getLoc().setTime(lppLocation2.getTime() + ((long) (1000.0d * d5)));
                this.mLPPPosition.add(new LppLocation(lppLocation));
                this.lastTrajTime = lppLocation.getTime();
            }
        }
    }

    private void resetwithLastLoc(Location location) {
        if (location == null) {
            Log.m31e(this.TAG, "resetwithLastLoc() unknown error - lastloc is null");
            return;
        }
        if (this.LastLMLoc == null) {
            this.LastLMLoc = new LppLocation(location);
        } else {
            this.LastLMLoc.set(location);
        }
        this.OldLoc.set(location);
        this.Origin_LLH[0] = location.getLatitude();
        this.Origin_LLH[1] = location.getLongitude();
        this.Origin_LLH[2] = location.getAltitude();
        this.OldLoc.setOrigin(this.Origin_LLH[0], this.Origin_LLH[1], this.Origin_LLH[2]);
        this.CurrentLoc.set(this.OldLoc);
        this.mAPDRStack.clear();
        this.mAPDRStack.clear();
        this.mLPPPosition.clear();
        this.mInputPosBuf.clear();
        this.mInputPosBuf.add(new LppLocation(location));
        this.mInputPosBufSync.clear();
        this.mFlagLocInputReady = true;
        this.lastTrajTime = 0;
        this.Cnt_SameLocSet = 0;
    }

    public void deliverAPDRData(ArrayList<ApdrData> arrayList) {
        Log.m29d(this.TAG, "deliverAPDRData(), array size \t\t: " + arrayList.size());
        ApdrData apdrData = new ApdrData();
        for (int i = 0; i < arrayList.size(); i++) {
            SendStatus("APDR time - " + ((ApdrData) arrayList.get(i)).utctime + " status - " + ((ApdrData) arrayList.get(i)).movingStatus + " step length - " + ((ApdrData) arrayList.get(i)).stepLength + " step heading - " + ((ApdrData) arrayList.get(i)).apdrHeading);
            apdrData.set((ApdrData) arrayList.get(i));
            this.mAPDRResults.add(new ApdrData(apdrData));
            if (apdrData.movingStatus == 1) {
                setStayingAreaFlag(1);
            }
        }
    }

    public void deliverLocationData(Location location) {
        if (location == null) {
            Log.m31e(this.TAG, "deliverLocationData - loc is null");
            return;
        }
        SendStatus("LppAlgorithm - PosIn => Time : " + location.getTime() + " Pos : " + location.getProvider() + " , " + location.getLatitude() + " , " + location.getLongitude() + " , " + location.getAltitude() + " , " + location.getAccuracy() + " , " + location.getBearing() + " , " + location.getSpeed());
        if (this.LastLMLoc == null) {
            resetwithLastLoc(location);
        }
        this.mInputPosBufSync.add(new LppLocation(location));
        if (this.mInputPosBufSync.size() > 100) {
            this.mInputPosBufSync.remove(0);
        }
        SendStatus("Size of mInputPosBufSync : " + this.mInputPosBufSync.size());
        SendStatus("Position filter Status - " + this.mFlagLocInputReady);
        if (this.mFlagLocInputReady) {
            this.mFlagLocInputReady = false;
            for (LppLocation add : this.mInputPosBufSync) {
                this.mInputPosBuf.add(add);
            }
            this.mInputPosBufSync.clear();
            Collections.sort(this.mInputPosBuf, new C10481());
            if (this.mFlagIsGPSBatchMode) {
                SendStatus("Position Filtering hold - GPS batching mode , accumulated index : " + this.mInputPosBuf.size());
            } else {
                Object obj = null;
                for (LppLocation lppLocation : this.mInputPosBuf) {
                    Object obj2;
                    SendStatus("Fine Location Acq Flag : " + this.Flag_FineLocAcquired);
                    if (this.Flag_FineLocAcquired) {
                        switch (LocValidCheck(lppLocation, this.LastLMLoc, true)) {
                            case 0:
                                obj2 = null;
                                break;
                            case 1:
                                obj2 = 1;
                                break;
                            case 2:
                                obj2 = 1;
                                obj = 1;
                                break;
                            default:
                                obj2 = null;
                                break;
                        }
                    } else if (lppLocation.getAccuracy() > 50.0f) {
                        SendStatus("LppAlgorithm - check Initial Fine location : false - accuracy is " + lppLocation.getAccuracy());
                        obj2 = null;
                    } else {
                        SendStatus("LppAlgorithm - check Initial Fine location : OK - accuracy is " + lppLocation.getAccuracy());
                        this.Flag_FineLocAcquired = true;
                        obj2 = 1;
                    }
                    if (obj2 != null) {
                        LppAlgorithmCheckAndRun(lppLocation);
                        if (obj != null) {
                            lppLocation.getLoc().setTime(lppLocation.getLoc().getTime() + 1000);
                            LppAlgorithmCheckAndRun(lppLocation);
                        }
                        this.LastLMLoc.set(lppLocation);
                    } else {
                        SendStatus("Flag_Loc false - loc time is " + lppLocation.getTime());
                    }
                }
                this.mInputPosBuf.clear();
            }
            this.mFlagLocInputReady = true;
            return;
        }
        SendStatus("Position filter is not ready");
    }

    public void init(LppAlgoListener lppAlgoListener) {
        this.mLPPLnr = lppAlgoListener;
        Log.m29d(this.TAG, "init()");
        this.flag_AlgorithmOn = false;
        this.Flag_FineLocAcquired = false;
        PositionFilteringInit();
    }

    public void setGPSBatchingStatus(boolean z) {
        this.mFlagIsGPSBatchMode = z;
    }

    public void setStayingAreaFlag(int i) {
        this.mFlagStayingArea = true;
    }

    public void start() {
        this.flag_AlgorithmOn = true;
    }

    public void stop() {
        Log.m29d(this.TAG, "Stop - mLPPPosition size() : " + this.mLPPPosition.size());
        if (this.mLPPPosition.size() > 0 && this.mLPPLnr != null) {
            this.mLPPLnr.onUpdateLPPtraj(this.mLPPPosition);
            this.mLPPPosition.clear();
        }
        this.LastLMLoc = null;
        this.flag_AlgorithmOn = false;
    }
}
