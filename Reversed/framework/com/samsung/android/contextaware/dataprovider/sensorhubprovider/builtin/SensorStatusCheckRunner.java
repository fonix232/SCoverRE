package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax.DATATYPE;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;

public class SensorStatusCheckRunner extends LibTypeProvider {
    private float mMode = 0.0f;

    private enum ContextName {
        Status((byte) 0),
        XAxis((byte) 1),
        YAxis((byte) 2),
        ZAxis((byte) 3);
        
        private byte val;

        private ContextName(byte b) {
            this.val = b;
        }
    }

    public SensorStatusCheckRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SENSOR_STATUS_CHECK.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"Status", "XAxis", "YAxis", "ZAxis"};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return (byte) 59;
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String[] contextValueNames = getContextValueNames();
        arrayList2.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.Status.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.XAxis.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.YAxis.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.ZAxis.val]));
        arrayList.add(arrayList2);
        return arrayList;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }
}
