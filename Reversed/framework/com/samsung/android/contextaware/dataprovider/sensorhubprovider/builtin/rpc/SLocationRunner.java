package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.rpc;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax.DATATYPE;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SLocationRunner extends LibTypeProvider {
    private boolean isARStarted;
    private HashMap<Integer, int[]> mGeofenceUpdates;
    private HashMap<Integer, int[]> mGeofences;
    private int mPauseResume;

    private enum ContextName {
        GeoFenceId((byte) 0),
        GeoFenceStatus((byte) 1),
        Latitude((byte) 2),
        Longitude((byte) 3),
        TotalGpsCount((byte) 4),
        SuccessGpsCount((byte) 5),
        Distance((byte) 6),
        Timestamp((byte) 7),
        Accuracy((byte) 8),
        FunctionType((byte) 9),
        ErrorCode((byte) 10),
        EventTypeArray(SprAnimatorBase.INTERPOLATOR_TYPE_BACKEASEOUT),
        EventStatusArray(SprAnimatorBase.INTERPOLATOR_TYPE_BACKEASEINOUT),
        DataArray(SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEIN),
        TimestampArray(SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEOUT),
        DataCount(SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEINOUT),
        LatitudeArray((byte) 16),
        LongitudeArray((byte) 17),
        AltitudeArray(SprAnimatorBase.INTERPOLATOR_TYPE_CIRCEASEINOUT),
        SpeedArray(SprAnimatorBase.INTERPOLATOR_TYPE_CUBICEASEIN),
        BearingArray(SprAnimatorBase.INTERPOLATOR_TYPE_CUBICEASEOUT),
        AccuracyArray(SprAnimatorBase.INTERPOLATOR_TYPE_CUBICEASEINOUT),
        Version(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEIN),
        DataSequence(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT),
        TotalSequence(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEINOUT);
        
        private byte val;

        private ContextName(byte b) {
            this.val = b;
        }
    }

    public SLocationRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
        this.mPauseResume = 16;
        this.isARStarted = false;
        this.mGeofences = null;
        this.mGeofenceUpdates = null;
        this.mPauseResume = 16;
        this.isARStarted = false;
        this.mGeofences = new HashMap();
        this.mGeofenceUpdates = new HashMap();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    protected void display() {
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerTypeCode(), Integer.valueOf(this.mPauseResume));
        setProperty(103, contextAwarePropertyBundle);
        if (!this.mGeofences.isEmpty()) {
            for (int[] iArr : this.mGeofences.values()) {
                contextAwarePropertyBundle = new ContextAwarePropertyBundle();
                contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerArrayTypeCode(), iArr);
                setProperty(101, contextAwarePropertyBundle);
            }
        }
        if (!this.mGeofenceUpdates.isEmpty()) {
            for (int[] iArr2 : this.mGeofenceUpdates.values()) {
                contextAwarePropertyBundle = new ContextAwarePropertyBundle();
                contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerArrayTypeCode(), iArr2);
                setProperty(104, contextAwarePropertyBundle);
            }
        }
        if (this.isARStarted) {
            contextAwarePropertyBundle = new ContextAwarePropertyBundle();
            contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerArrayTypeCode(), new int[]{0, 0});
            setProperty(SemMotionRecognitionEvent.SMART_SCROLL_TILT_FACE_OUT_STOP, contextAwarePropertyBundle);
        }
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SLOCATION.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"GeoFenceId", "GeoFenceStatus", "Latitude", "Longitude", "TotalGpsCount", "SuccessGpsCount", "Distance", "Timestamp", "Accuracy", "FunctionType", "ErrorCode", "EventTypeArray", "EventStatusArray", "DataArray", "TimeStampArray", "DataCount", "LatitudeArray", "LongitudeArray", "AltitudeArray", "SpeedArray", "BearingArray", "AccuracyArray", "Version", "DataSequence", "TotalSequence"};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return (byte) 55;
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayList arrayList9 = new ArrayList();
        String[] contextValueNames = getContextValueNames();
        arrayList9.add(new SensorHubSyntax((byte) 0));
        arrayList9.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.Version.val]));
        arrayList2.add(new SensorHubSyntax((byte) 1));
        arrayList2.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.GeoFenceId.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.GeoFenceStatus.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, contextValueNames[ContextName.Latitude.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, contextValueNames[ContextName.Longitude.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.Accuracy.val]));
        arrayList3.add(new SensorHubSyntax((byte) 2));
        arrayList3.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.GeoFenceId.val]));
        arrayList3.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.TotalGpsCount.val]));
        arrayList3.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.SuccessGpsCount.val]));
        arrayList4.add(new SensorHubSyntax((byte) 3));
        arrayList5.add(new SensorHubSyntax((byte) 4));
        arrayList5.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10.0d, contextValueNames[ContextName.Distance.val]));
        arrayList5.add(new SensorHubSyntax(DATATYPE.LONG, 1.0d, contextValueNames[ContextName.Timestamp.val]));
        arrayList5.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, contextValueNames[ContextName.Latitude.val]));
        arrayList5.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, contextValueNames[ContextName.Longitude.val]));
        arrayList5.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.Accuracy.val]));
        arrayList6.add(new SensorHubSyntax((byte) 5));
        arrayList6.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.FunctionType.val]));
        arrayList6.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.ErrorCode.val]));
        arrayList6.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.GeoFenceId.val]));
        arrayList7.add(new SensorHubSyntax((byte) 6));
        arrayList7.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.DataCount.val]));
        ArrayList arrayList10 = new ArrayList();
        arrayList10.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.EventTypeArray.val]));
        arrayList10.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.EventStatusArray.val]));
        arrayList10.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.DataArray.val]));
        arrayList10.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.TimestampArray.val]));
        arrayList7.add(new SensorHubSyntax(arrayList10));
        arrayList8.add(new SensorHubSyntax((byte) 7));
        arrayList8.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.DataCount.val]));
        arrayList8.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.DataSequence.val]));
        arrayList8.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.TotalSequence.val]));
        ArrayList arrayList11 = new ArrayList();
        arrayList11.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, contextValueNames[ContextName.LatitudeArray.val]));
        arrayList11.add(new SensorHubSyntax(DATATYPE.DOUBLE4, 1.0E7d, contextValueNames[ContextName.LongitudeArray.val]));
        arrayList11.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.AltitudeArray.val]));
        arrayList11.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.SpeedArray.val]));
        arrayList11.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.BearingArray.val]));
        arrayList11.add(new SensorHubSyntax(DATATYPE.SHORT, 1.0d, contextValueNames[ContextName.AccuracyArray.val]));
        arrayList11.add(new SensorHubSyntax(DATATYPE.LONG, 1.0d, contextValueNames[ContextName.TimestampArray.val]));
        arrayList8.add(new SensorHubSyntax(arrayList11));
        arrayList.add(arrayList2);
        arrayList.add(arrayList3);
        arrayList.add(arrayList4);
        arrayList.add(arrayList5);
        arrayList.add(arrayList6);
        arrayList.add(arrayList7);
        arrayList.add(arrayList8);
        arrayList.add(arrayList9);
        return arrayList;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 100) {
            CaLogger.info("Version");
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 17, new byte[]{(byte) 0});
            return true;
        } else if (i == 101) {
            CaLogger.info("Add");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 6) {
                CaLogger.error("missing data");
                return false;
            }
            Object obj = new int[6];
            for (r12 = 0; r12 < 6; r12++) {
                obj[r12] = r7[r12];
            }
            this.mGeofences.put(Integer.valueOf(obj[0]), obj);
            r6 = ByteBuffer.allocate(r7.length * 4);
            r6.asIntBuffer().put(r7);
            r5 = r6.array();
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 1, Arrays.copyOfRange(r5, 2, r5.length));
            return true;
        } else if (i == 102) {
            CaLogger.info("Remove");
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            this.mGeofences.remove(Integer.valueOf(intValue));
            this.mGeofenceUpdates.remove(Integer.valueOf(intValue));
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 2, CaConvertUtil.intToByteArr(intValue, 2));
            return true;
        } else if (i == 103) {
            CaLogger.info("PauseResume");
            int intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            this.mPauseResume = intValue2;
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 3, CaConvertUtil.intToByteArr(intValue2, 2));
            return true;
        } else if (i == 104) {
            CaLogger.info("Update");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 3) {
                CaLogger.error("missing data");
                return false;
            }
            Object obj2 = new int[3];
            for (r12 = 0; r12 < 3; r12++) {
                obj2[r12] = r7[r12];
            }
            this.mGeofenceUpdates.put(Integer.valueOf(obj2[0]), obj2);
            r13 = new ByteArrayOutputStream();
            try {
                r13.write(CaConvertUtil.intToByteArr(r7[0], 2));
                r13.write(CaConvertUtil.intToByteArr(r7[1], 4));
                r13.write(CaConvertUtil.intToByteArr(r7[2], 1));
            } catch (Throwable e2) {
                CaLogger.error("error converting");
                e2.printStackTrace();
            }
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 4, r13.toByteArray());
            return true;
        } else if (i == 105) {
            CaLogger.info("Set Loc");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 5) {
                CaLogger.error("missing data");
                return false;
            }
            r6 = ByteBuffer.allocate(r7.length * 4);
            r6.asIntBuffer().put(r7);
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 5, r6.array());
            return true;
        } else if (i == 106) {
            CaLogger.info("Start AR");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 2) {
                CaLogger.error("missing data");
                return false;
            }
            this.isARStarted = true;
            r13 = new ByteArrayOutputStream();
            try {
                r13.write(CaConvertUtil.intToByteArr(r7[0], 4));
                r13.write(CaConvertUtil.intToByteArr(r7[1], 1));
            } catch (Throwable e22) {
                CaLogger.error("error converting");
                e22.printStackTrace();
            }
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 6, r13.toByteArray());
            return true;
        } else if (i == 107) {
            CaLogger.info("Stop AR");
            this.isARStarted = false;
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 7, new byte[]{(byte) 0});
            return true;
        } else if (i == 108) {
            CaLogger.info("CurLoc func");
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 8, CaConvertUtil.intToByteArr(((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue(), 1));
            return true;
        } else if (i == 110) {
            CaLogger.info("Start core FIFO Batch");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 6) {
                CaLogger.error("missing data");
                return false;
            }
            r6 = ByteBuffer.allocate(r7.length * 4);
            r6.asIntBuffer().put(r7);
            r5 = r6.array();
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 9, Arrays.copyOfRange(r5, 2, r5.length));
            return true;
        } else if (i == 111) {
            CaLogger.info("Update core FIFO Batch");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 6) {
                CaLogger.error("missing data");
                return false;
            }
            r6 = ByteBuffer.allocate(r7.length * 4);
            r6.asIntBuffer().put(r7);
            r5 = r6.array();
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, (byte) 10, Arrays.copyOfRange(r5, 2, r5.length));
            return true;
        } else if (i == 112) {
            CaLogger.info("Stop core FIFO Batch");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 1) {
                CaLogger.error("missing data");
                return false;
            }
            r6 = ByteBuffer.allocate(r7.length * 4);
            r6.asIntBuffer().put(r7);
            r5 = r6.array();
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, SprAnimatorBase.INTERPOLATOR_TYPE_BACKEASEOUT, Arrays.copyOfRange(r5, 2, r5.length));
            return true;
        } else if (i == 113) {
            CaLogger.info("Request core FIFO Batch Location");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 1) {
                CaLogger.error("missing data");
                return false;
            }
            r6 = ByteBuffer.allocate(r7.length * 4);
            r6.asIntBuffer().put(r7);
            r5 = r6.array();
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, SprAnimatorBase.INTERPOLATOR_TYPE_BACKEASEINOUT, Arrays.copyOfRange(r5, 2, r5.length));
            return true;
        } else if (i == 114) {
            CaLogger.info("Flush FIFO Batch Location");
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEIN, new byte[]{(byte) 0});
            return true;
        } else if (i == 115) {
            CaLogger.info("Inject core FIFO Batch Location");
            r7 = (int[]) ((ContextAwarePropertyBundle) e).getValue();
            if (r7.length != 1) {
                CaLogger.error("missing data");
                return false;
            }
            r6 = ByteBuffer.allocate(r7.length * 4);
            r6.asIntBuffer().put(r7);
            r5 = r6.array();
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEOUT, Arrays.copyOfRange(r5, 2, r5.length));
            return true;
        } else if (i == 116) {
            CaLogger.info("Cleanup FIFO Batch Location");
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 55, SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEINOUT, new byte[]{(byte) 0});
            return true;
        } else if (i != 109) {
            return false;
        } else {
            CaLogger.info("Status remove");
            this.mPauseResume = 16;
            this.isARStarted = false;
            this.mGeofences.clear();
            this.mGeofenceUpdates.clear();
            return true;
        }
    }
}
