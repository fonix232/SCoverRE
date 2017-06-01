package com.samsung.android.contextaware.aggregator.lpp;

import android.location.Location;
import android.util.Log;
import com.samsung.android.contextaware.aggregator.lpp.algorithm.CoordinateTransform;

public class LppLocation {
    private static String TAG = "LppLocation";
    public long Capturedtime;
    private boolean Updated;
    private final double[] filteredVelocity;
    private Location loc;
    private double mOrgHei;
    private double mOrgLat;
    private double mOrgLon;
    private double mPosECEF_X;
    private double mPosECEF_Y;
    private double mPosECEF_Z;
    private double mPosEast;
    private double mPosNorth;
    private double mPosUp;
    private int movingStatus;

    public LppLocation() {
        this.mOrgLat = 0.0d;
        this.mOrgLon = 0.0d;
        this.mOrgHei = 0.0d;
        this.Updated = true;
        this.Capturedtime = 0;
        this.movingStatus = 2;
        this.filteredVelocity = new double[]{0.0d, 0.0d, 0.0d};
        this.loc = new Location("NOPROVIDER");
        this.Updated = true;
    }

    public LppLocation(Location location) {
        this.mOrgLat = 0.0d;
        this.mOrgLon = 0.0d;
        this.mOrgHei = 0.0d;
        this.Updated = true;
        this.Capturedtime = 0;
        this.movingStatus = 2;
        this.filteredVelocity = new double[]{0.0d, 0.0d, 0.0d};
        if (location != null) {
            this.loc = new Location(location);
            this.Capturedtime = location.getTime();
            this.Updated = true;
            setOrigin(location.getLatitude(), location.getLongitude(), location.getAltitude());
            return;
        }
        this.loc = new Location("NOPROVIDER");
        this.Updated = true;
    }

    public LppLocation(LppLocation lppLocation) {
        this.mOrgLat = 0.0d;
        this.mOrgLon = 0.0d;
        this.mOrgHei = 0.0d;
        this.Updated = true;
        this.Capturedtime = 0;
        this.movingStatus = 2;
        this.filteredVelocity = new double[]{0.0d, 0.0d, 0.0d};
        this.loc = new Location(lppLocation.loc);
        set(lppLocation);
    }

    private void CalCoordinate() {
        this.Updated = false;
        r2 = new double[3];
        double[] dArr = new double[]{Math.toRadians(this.loc.getLatitude()), Math.toRadians(this.loc.getLongitude()), this.loc.getAltitude()};
        dArr[0] = Math.toRadians(this.mOrgLat);
        dArr[1] = Math.toRadians(this.mOrgLon);
        dArr[2] = this.mOrgHei;
        double[] llh2enu = CoordinateTransform.llh2enu(r2, dArr);
        this.mPosEast = llh2enu[0];
        this.mPosNorth = llh2enu[1];
        this.mPosUp = llh2enu[2];
        double[] llh2xyz = CoordinateTransform.llh2xyz(r2);
        this.mPosECEF_X = llh2xyz[0];
        this.mPosECEF_Y = llh2xyz[1];
        this.mPosECEF_Z = llh2xyz[2];
    }

    private void SendStatus(String str) {
        Log.m29d(TAG, str);
    }

    public void PosPropation(double d, double d2) {
        if (this.Updated) {
            CalCoordinate();
        }
        double sin = Math.sin(d);
        double cos = Math.cos(d);
        this.mPosEast += sin * d2;
        this.mPosNorth += cos * d2;
        r6 = new double[3];
        double[] dArr = new double[]{this.mPosEast, this.mPosNorth, this.mPosUp};
        r6[0] = Math.toRadians(this.mOrgLat);
        r6[1] = Math.toRadians(this.mOrgLon);
        r6[2] = this.mOrgHei;
        double[] enu2llh = CoordinateTransform.enu2llh(dArr, r6);
        this.loc.setLatitude(Math.toDegrees(enu2llh[0]));
        this.loc.setLongitude(Math.toDegrees(enu2llh[1]));
        this.loc.setAltitude(enu2llh[2]);
        CalCoordinate();
    }

    public double distanceTo(LppLocation lppLocation) {
        LppLocation lppLocation2 = new LppLocation(lppLocation);
        lppLocation2.setOrigin(getOriginLat(), getOriginLon(), getOriginAltitude());
        return Math.sqrt(((getPosEastLocal() - lppLocation2.getPosEastLocal()) * (getPosEastLocal() - lppLocation2.getPosEastLocal())) + ((getPosNorthLocal() - lppLocation2.getPosNorthLocal()) * (getPosNorthLocal() - lppLocation2.getPosNorthLocal())));
    }

    public void estimateVelocity(LppLocation lppLocation, LppLocation lppLocation2) {
        double d;
        double d2;
        double sqrt;
        double sin;
        double cos;
        double d3;
        double d4;
        LppLocation lppLocation3 = new LppLocation(lppLocation);
        LppLocation lppLocation4 = new LppLocation(lppLocation2);
        lppLocation3.setOrigin(getLatitude(), getLongitude(), getAltitude());
        lppLocation4.setOrigin(getLatitude(), getLongitude(), getAltitude());
        long time = lppLocation3.getTime();
        long time2 = getTime();
        long time3 = lppLocation4.getTime();
        double d5 = ((double) time) * 0.001d;
        double d6 = ((double) time2) * 0.001d;
        double d7 = ((double) time3) * 0.001d;
        if (time2 == time || time3 == time2) {
            SendStatus("WARNING: estimateVelocity - abnormal t0, t1, t2");
            d = 0.0d;
            d2 = 0.0d;
        } else {
            d = ((lppLocation4.getPosEastLocal() / (d7 - d6)) + ((0.0d - lppLocation3.getPosEastLocal()) / (d6 - d5))) / 2.0d;
            d2 = ((lppLocation4.getPosNorthLocal() / (d7 - d6)) + ((0.0d - lppLocation3.getPosNorthLocal()) / (d6 - d5))) / 2.0d;
            sqrt = Math.sqrt((d * d) + (d2 * d2));
            if (sqrt > 0.001d) {
                d /= sqrt;
                d2 /= sqrt;
            } else {
                d = 0.0d;
                d2 = 0.0d;
            }
        }
        if (this.loc.getSpeed() > 15.0f) {
            sin = Math.sin(((double) (this.loc.getBearing() / 180.0f)) * 3.141592653589793d);
            cos = Math.cos(((double) (this.loc.getBearing() / 180.0f)) * 3.141592653589793d);
        } else {
            sin = 0.0d;
            cos = 0.0d;
        }
        if (time3 == time) {
            SendStatus("WARNING: estimateVelocity - abnormal t0, t2");
            d3 = 0.0d;
            d4 = 0.0d;
        } else {
            sqrt = lppLocation4.distanceTo(lppLocation3);
            d3 = ((lppLocation4.getPosEastLocal() - lppLocation3.getPosEastLocal()) / (d7 - d5)) / sqrt;
            d4 = ((lppLocation4.getPosNorthLocal() - lppLocation3.getPosNorthLocal()) / (d7 - d5)) / sqrt;
        }
        if (this.loc.getSpeed() > 15.0f) {
            this.filteredVelocity[0] = ((0.0d * d) + (0.7d * sin)) + (0.3d * d3);
            this.filteredVelocity[1] = ((0.0d * d2) + (0.7d * cos)) + (0.3d * d4);
            return;
        }
        this.filteredVelocity[0] = d;
        this.filteredVelocity[1] = d2;
    }

    public float getAccuracy() {
        return this.loc.getAccuracy();
    }

    public double getAltitude() {
        return this.loc.getAltitude();
    }

    public double[] getFilteredVelocity() {
        return this.filteredVelocity;
    }

    public double getLatitude() {
        return this.loc.getLatitude();
    }

    public Location getLoc() {
        return this.loc;
    }

    public double getLongitude() {
        return this.loc.getLongitude();
    }

    public int getMovingStatus() {
        return this.movingStatus;
    }

    public double getOriginAltitude() {
        return this.mOrgHei;
    }

    public double getOriginLat() {
        return this.mOrgLat;
    }

    public double getOriginLon() {
        return this.mOrgLon;
    }

    public double getPosECEF_X() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosECEF_X;
    }

    public double getPosECEF_Y() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosECEF_Y;
    }

    public double getPosECEF_Z() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosECEF_Z;
    }

    public double getPosEastLocal() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosEast;
    }

    public double getPosNorthLocal() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosNorth;
    }

    public double getPosUpLocal() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosUp;
    }

    public long getSystemTime() {
        return this.Capturedtime;
    }

    public long getTime() {
        return this.loc.getTime();
    }

    public void set(Location location) {
        if (location != null) {
            this.loc.set(location);
            this.Capturedtime = location.getTime();
            setSystemTime();
            this.Updated = true;
        }
    }

    public void set(LppLocation lppLocation) {
        this.loc.set(lppLocation.loc);
        setOrigin(lppLocation.getOriginLat(), lppLocation.getOriginLon(), lppLocation.getOriginAltitude());
        this.Capturedtime = lppLocation.getSystemTime();
        this.movingStatus = lppLocation.getMovingStatus();
        double[] filteredVelocity = lppLocation.getFilteredVelocity();
        this.filteredVelocity[0] = filteredVelocity[0];
        this.filteredVelocity[1] = filteredVelocity[1];
        this.filteredVelocity[2] = filteredVelocity[2];
        this.Updated = true;
    }

    public void setAltitude(double d) {
        this.loc.setAltitude(d);
        this.Updated = true;
    }

    public void setLatitude(double d) {
        this.loc.setLatitude(d);
        this.Updated = true;
    }

    public void setLongitude(double d) {
        this.loc.setLongitude(d);
        this.Updated = true;
    }

    public void setMovingStatus(int i) {
        this.movingStatus = i;
    }

    public void setOrigin(double d, double d2, double d3) {
        this.mOrgLat = d;
        this.mOrgLon = d2;
        this.mOrgHei = d3;
        this.Updated = true;
    }

    public void setPosENU(double d, double d2, double d3) {
        r2 = new double[3];
        double[] dArr = new double[]{d, d2, d3};
        r2[0] = Math.toRadians(this.mOrgLat);
        r2[1] = Math.toRadians(this.mOrgLon);
        r2[2] = this.mOrgHei;
        double[] enu2llh = CoordinateTransform.enu2llh(dArr, r2);
        this.loc.setLatitude(Math.toDegrees(enu2llh[0]));
        this.loc.setLongitude(Math.toDegrees(enu2llh[1]));
        this.loc.setAltitude(enu2llh[2]);
        CalCoordinate();
    }

    public void setSystemTime() {
        this.Capturedtime = System.nanoTime();
    }

    public void setSystemTime(long j) {
        if (this.Capturedtime == 0) {
            Log.m31e(TAG, "setSystemTime() - Abnormal method calling");
            return;
        }
        double d = (double) (j - this.Capturedtime);
        if (d > 1.0E15d) {
            Log.m31e(TAG, "systemtime" + j + "     Capturedtime" + this.Capturedtime);
            Log.m31e(TAG, "setSystemTime() - systemtime overflow or propagation error timediff" + d);
            return;
        }
        this.Capturedtime = j;
        this.loc.setTime(this.loc.getTime() + ((long) (1.0E-6d * d)));
    }
}
