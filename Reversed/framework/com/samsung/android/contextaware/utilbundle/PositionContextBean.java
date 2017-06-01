package com.samsung.android.contextaware.utilbundle;

class PositionContextBean {
    static final int FUSED_TYPE = 3;
    static final int GPS_TYPE = 1;
    static final int NONE_TYPE = 0;
    static final int SLOCATION_TYPE = 4;
    static final int WPS_TYPE = 2;
    private float accuracy;
    private double altitude;
    private double distance;
    private double latitude;
    private double longitude;
    private int satelliteCount;
    private float speed;
    private int type;
    private int[] utcTime;

    PositionContextBean() {
        clearPosition();
    }

    PositionContextBean(float f) {
        clearPosition();
        this.accuracy = f;
    }

    static final double calculationDistance(double d, double d2, double d3, double d4) {
        return (d < 0.0d || d2 < 0.0d) ? -1.0d : Math.sqrt(Math.pow(d3 - d, 2.0d) + Math.pow(d4 - d2, 2.0d));
    }

    final void clearPosition() {
        this.type = 0;
        this.utcTime = new int[3];
        this.latitude = 0.0d;
        this.longitude = 0.0d;
        this.altitude = 0.0d;
        this.distance = 0.0d;
        this.speed = 0.0f;
        this.accuracy = 1000.0f;
        this.satelliteCount = 0;
    }

    final float getAccuracy() {
        return this.accuracy;
    }

    final double getAltitude() {
        return this.altitude;
    }

    final double getDistance() {
        return this.distance;
    }

    final double getLatitude() {
        return this.latitude;
    }

    final double getLongitude() {
        return this.longitude;
    }

    final int getSatelliteCount() {
        return this.satelliteCount;
    }

    final float getSpeed() {
        return this.speed;
    }

    final int getType() {
        return this.type;
    }

    final int[] getUtcTime() {
        return this.utcTime;
    }

    final void setAccuracy(float f) {
        this.accuracy = f;
    }

    final void setAltitude(double d) {
        this.altitude = d;
    }

    final void setDistance(double d) {
        this.distance = d;
    }

    final void setLatitude(double d) {
        this.latitude = d;
    }

    final void setLongitude(double d) {
        this.longitude = d;
    }

    final void setPosition(int i, int[] iArr, double d, double d2, double d3, double d4, float f, float f2, int i2) {
        this.type = i;
        this.utcTime = iArr;
        this.latitude = d;
        this.longitude = d2;
        this.altitude = d3;
        this.distance = d4;
        this.speed = f;
        this.accuracy = f2;
        this.satelliteCount = i2;
    }

    final void setSatelliteCount(int i) {
        this.satelliteCount = i;
    }

    final void setSpeed(float f) {
        this.speed = f;
    }

    final void setType(int i) {
        this.type = i;
    }

    final void setUtcTime(int[] iArr) {
        this.utcTime = iArr;
    }
}
