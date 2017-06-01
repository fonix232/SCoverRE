package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.util.ArrayList;

public class SensorHubSyntax {
    private final double mConversionScale;
    private final DATATYPE mDataType;
    private byte mMessageType;
    private final String mName;
    private final ArrayList<SensorHubSyntax> mRepeatList;
    private byte mSize;

    public enum DATATYPE {
        BOOLEAN,
        BYTE,
        SHORT,
        INTEGER3,
        INTEGER,
        LONG,
        FLOAT2,
        FLOAT3,
        FLOAT4,
        DOUBLE2,
        DOUBLE3,
        DOUBLE4,
        REPEATLIST,
        MESSAGE_TYPE
    }

    public SensorHubSyntax(byte b) {
        this.mSize = (byte) 0;
        this.mMessageType = (byte) -1;
        this.mDataType = DATATYPE.MESSAGE_TYPE;
        this.mConversionScale = 1.0d;
        this.mName = "DataType";
        this.mRepeatList = null;
        this.mMessageType = b;
        this.mSize = (byte) 0;
    }

    public SensorHubSyntax(DATATYPE datatype, double d, String str) {
        this.mSize = (byte) 0;
        this.mMessageType = (byte) -1;
        this.mDataType = datatype;
        this.mConversionScale = d;
        this.mName = str;
        this.mRepeatList = null;
        computeLength();
    }

    public SensorHubSyntax(ArrayList<SensorHubSyntax> arrayList) {
        this.mSize = (byte) 0;
        this.mMessageType = (byte) -1;
        this.mDataType = DATATYPE.REPEATLIST;
        this.mConversionScale = 1.0d;
        this.mName = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        this.mRepeatList = arrayList;
        this.mSize = (byte) 0;
    }

    private void computeLength() {
        if (this.mDataType == DATATYPE.INTEGER || this.mDataType == DATATYPE.FLOAT4 || this.mDataType == DATATYPE.DOUBLE4) {
            this.mSize = (byte) 4;
        } else if (this.mDataType == DATATYPE.BYTE || this.mDataType == DATATYPE.BOOLEAN) {
            this.mSize = (byte) 1;
        } else if (this.mDataType == DATATYPE.LONG) {
            this.mSize = (byte) 8;
        } else if (this.mDataType == DATATYPE.SHORT || this.mDataType == DATATYPE.FLOAT2 || this.mDataType == DATATYPE.DOUBLE2) {
            this.mSize = (byte) 2;
        } else {
            this.mSize = (byte) 3;
        }
    }

    DATATYPE dataType() {
        return this.mDataType;
    }

    byte messageType() {
        return this.mMessageType;
    }

    String name() {
        return this.mName;
    }

    ArrayList<SensorHubSyntax> repeatList() {
        return this.mRepeatList;
    }

    double scale() {
        return this.mConversionScale;
    }

    byte size() {
        return this.mSize;
    }
}
