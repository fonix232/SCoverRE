package com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ICurrentPositionRequestObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.REQUEST_DATA;
import com.samsung.android.contextaware.manager.ICurrrentPositionObserver;
import com.samsung.android.contextaware.utilbundle.CaCurrentPositionManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CurrentPositionRequestRunner implements ISensorHubRequestParser, ICurrrentPositionObserver, ICurrentPositionRequest {
    private static final int LOCATION_MODE_SLOCATION = 2;
    private final CaCurrentPositionManager mCurrentPosition;
    private ICurrentPositionRequestObserver mListener;
    private final Position mPosition = new Position();

    public static class Position {
        private float accuracy;
        private double altitude;
        private double distance;
        private double latitude;
        private double longitude;
        private int satelliteCount;
        private float speed;
        private int type;
        private int[] utcTime;

        public float getAccuracy() {
            return this.accuracy;
        }

        public double getAltitude() {
            return this.altitude;
        }

        public double getDistance() {
            return this.distance;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public int getSatelliteCount() {
            return this.satelliteCount;
        }

        public float getSpeed() {
            return this.speed;
        }

        public int getType() {
            return this.type;
        }

        public int[] getUtcTime() {
            return this.utcTime;
        }
    }

    public CurrentPositionRequestRunner(Context context, Looper looper) {
        this.mCurrentPosition = new CaCurrentPositionManager(context, looper, this);
    }

    public Position getPosition() {
        return this.mPosition;
    }

    public final int getRequestType() {
        return REQUEST_DATA.REQUEST_CURRENT_POSITION.value;
    }

    public final void notifyObserver(Position position) {
        if (this.mListener != null) {
            this.mListener.updatePosition(position);
        }
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        this.mCurrentPosition.enable(2, bArr[i] * 10);
        return i2;
    }

    public final void registerObserver(ICurrentPositionRequestObserver iCurrentPositionRequestObserver) {
        this.mListener = iCurrentPositionRequestObserver;
    }

    public final void unregisterObserver() {
        this.mListener = null;
    }

    public final void updateCurrentPosition(int i, int[] iArr, double d, double d2, double d3, double d4, float f, float f2, int i2) {
        this.mPosition.type = i;
        this.mPosition.utcTime = iArr;
        this.mPosition.latitude = d;
        this.mPosition.longitude = d2;
        this.mPosition.altitude = d3;
        this.mPosition.distance = d4;
        this.mPosition.speed = f;
        this.mPosition.accuracy = f2;
        this.mPosition.satelliteCount = i2;
        notifyObserver(this.mPosition);
    }
}
