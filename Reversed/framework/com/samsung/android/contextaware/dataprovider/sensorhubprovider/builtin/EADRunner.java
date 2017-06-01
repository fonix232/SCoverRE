package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax.DATATYPE;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;

public class EADRunner extends LibTypeProvider {
    private float mMode = 0.0f;

    private enum ContextName {
        R((byte) 0),
        G((byte) 1),
        B((byte) 2),
        Lux((byte) 3),
        CCT((byte) 4);
        
        private byte val;

        private ContextName(byte b) {
            this.val = b;
        }
    }

    public EADRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_EAD.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"R", "G", "B", "Lux", "CCT"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return CaConvertUtil.intToByteArr((int) (this.mMode * 10000.0f), 4);
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_EAD_SERVICE;
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String[] contextValueNames = getContextValueNames();
        arrayList2.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10000.0d, contextValueNames[ContextName.R.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10000.0d, contextValueNames[ContextName.G.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.FLOAT4, 10000.0d, contextValueNames[ContextName.B.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.LONG, 1.0d, contextValueNames[ContextName.Lux.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.INTEGER, 1.0d, contextValueNames[ContextName.CCT.val]));
        arrayList.add(arrayList2);
        return arrayList;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 66) {
            float floatValue = ((Float) ((ContextAwarePropertyBundle) e).getValue()).floatValue();
            CaLogger.info("Mode = " + Float.toString(floatValue));
            this.mMode = floatValue;
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EAD_SERVICE, (byte) 1, CaConvertUtil.intToByteArr((int) (10000.0f * floatValue), 4));
            return true;
        } else if (i != 67) {
            return false;
        } else {
            int intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            CaLogger.info("Duration = " + intValue);
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EAD_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(intValue, 1));
            return true;
        }
    }
}
