package com.samsung.android.contextaware.aggregator.lpp;

public class ApdrData {
    public double StepCount;
    public long TimeNoMove;
    public double[] acc;
    public int apdrDevicePosition;
    public double apdrForwardingVector;
    public double apdrHeading;
    public double[] att;
    public double bearing;
    public int carryPos;
    public double[] gyro;
    public double[] mag;
    public int movingStatus;
    public double stepFlag;
    public double stepLength;
    public long systemtime;
    public long utctime;

    public ApdrData() {
        this.acc = new double[3];
        this.gyro = new double[3];
        this.mag = new double[4];
        this.att = new double[3];
        this.movingStatus = 2;
        this.bearing = 0.0d;
        this.StepCount = 0.0d;
        this.TimeNoMove = 0;
        this.stepFlag = 0.0d;
        this.stepLength = 0.0d;
        this.systemtime = 0;
        this.utctime = 0;
        for (int i = 0; i < 3; i++) {
            this.acc[i] = 0.0d;
            this.gyro[i] = 0.0d;
            this.mag[i] = 0.0d;
            this.att[i] = 0.0d;
        }
        this.mag[3] = 0.0d;
        this.apdrForwardingVector = 0.0d;
        this.apdrHeading = 0.0d;
        this.apdrDevicePosition = 1;
    }

    public ApdrData(ApdrData apdrData) {
        this.acc = new double[3];
        this.gyro = new double[3];
        this.mag = new double[4];
        this.att = new double[3];
        this.movingStatus = 2;
        set(apdrData);
    }

    public void set(ApdrData apdrData) {
        this.bearing = apdrData.bearing;
        this.StepCount = apdrData.StepCount;
        this.TimeNoMove = apdrData.TimeNoMove;
        this.stepFlag = apdrData.stepFlag;
        this.stepLength = apdrData.stepLength;
        this.systemtime = apdrData.systemtime;
        this.utctime = apdrData.utctime;
        for (int i = 0; i < 3; i++) {
            this.acc[i] = apdrData.acc[i];
            this.gyro[i] = apdrData.gyro[i];
            this.mag[i] = apdrData.mag[i];
            this.att[i] = apdrData.att[i];
        }
        this.mag[3] = apdrData.mag[3];
        this.movingStatus = apdrData.movingStatus;
        this.apdrForwardingVector = apdrData.apdrForwardingVector;
        this.apdrHeading = apdrData.apdrHeading;
        this.apdrDevicePosition = apdrData.apdrDevicePosition;
    }
}
