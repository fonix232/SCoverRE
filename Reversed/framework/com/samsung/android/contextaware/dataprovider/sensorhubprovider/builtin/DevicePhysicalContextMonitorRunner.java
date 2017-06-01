package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
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
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;

public class DevicePhysicalContextMonitorRunner extends LibTypeProvider {

    private enum ContextName {
        AODStatus((byte) 0),
        VersionYear((byte) 1),
        VersionMonth((byte) 2),
        VersionDay((byte) 3),
        VersionRevision((byte) 4),
        AODReason((byte) 5);
        
        private byte val;

        private ContextName(byte b) {
            this.val = b;
        }
    }

    public DevicePhysicalContextMonitorRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
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
        return ContextType.SENSORHUB_RUNNER_DEVICE_PHYSICAL_CONTEXT_MONITOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"AODStatus", "VersionYear", "VersionMonth", "VersionDay", "VersionRevision", "AODReason"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        r0 = new byte[4];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        r0[1] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        r0[2] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        r0[3] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return r0;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0, (byte) 0};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE;
    }

    public ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        ArrayList<ArrayList<SensorHubSyntax>> arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        String[] contextValueNames = getContextValueNames();
        arrayList2.add(new SensorHubSyntax((byte) 1));
        arrayList2.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.AODStatus.val]));
        arrayList2.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.AODReason.val]));
        arrayList3.add(new SensorHubSyntax((byte) 3));
        arrayList3.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.VersionYear.val]));
        arrayList3.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.VersionMonth.val]));
        arrayList3.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.VersionDay.val]));
        arrayList3.add(new SensorHubSyntax(DATATYPE.BYTE, 1.0d, contextValueNames[ContextName.VersionRevision.val]));
        arrayList.add(arrayList2);
        arrayList.add(arrayList3);
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
        if (i == 7) {
            CaLogger.info("Get status");
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 1, (byte) 1});
            return true;
        } else if (i == 8) {
            CaLogger.info("Get version");
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 2, (byte) 1});
            return true;
        } else if (i == 80) {
            int intValue;
            try {
                intValue = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e2) {
                CaLogger.error("DPCM setProperty Exception: " + e2.getMessage().toString() + ", sensorProx = 1");
                intValue = 1;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 1, new byte[]{(byte) 1, CaConvertUtil.intToByteArr(intValue, 1)[0]});
            return true;
        } else if (i == 81) {
            int intValue2;
            try {
                intValue2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e22) {
                CaLogger.error("DPCM setProperty Exception: " + e22.getMessage().toString() + ", sensorProx = 1");
                intValue2 = 1;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 2, new byte[]{(byte) 1, CaConvertUtil.intToByteArr(intValue2, 1)[0]});
            return true;
        } else if (i == 82) {
            int intValue3;
            try {
                intValue3 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e222) {
                CaLogger.error("DPCM setProperty Exception: " + e222.getMessage().toString() + ", sensorBright = 1");
                intValue3 = 1;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 2, new byte[]{(byte) 2, CaConvertUtil.intToByteArr(intValue3, 1)[0]});
            return true;
        } else if (i == 83) {
            int intValue4;
            try {
                intValue4 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e2222) {
                CaLogger.error("DPCM setProperty Exception: " + e2222.getMessage().toString() + ", aodDuration = 600");
                intValue4 = 600;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 1, CaConvertUtil.intToByteArr(intValue4, 2)[0], CaConvertUtil.intToByteArr(intValue4, 2)[1]});
            return true;
        } else if (i == 84) {
            try {
                r2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e22222) {
                CaLogger.error("DPCM setProperty Exception: " + e22222.getMessage().toString() + ", duration = 3*1000");
                r2 = 3000;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 2, CaConvertUtil.intToByteArr(r2, 2)[0], CaConvertUtil.intToByteArr(r2, 2)[1]});
            return true;
        } else if (i == 85) {
            try {
                r2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e222222) {
                CaLogger.error("DPCM setProperty Exception: " + e222222.getMessage().toString() + ", duration = 60*1000");
                r2 = 60000;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 3, CaConvertUtil.intToByteArr(r2, 2)[0], CaConvertUtil.intToByteArr(r2, 2)[1]});
            return true;
        } else if (i == 86) {
            try {
                r2 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e2222222) {
                CaLogger.error("DPCM setProperty Exception: " + e2222222.getMessage().toString() + ", duration = 3*1000");
                r2 = 3000;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 3, new byte[]{(byte) 4, CaConvertUtil.intToByteArr(r2, 2)[0], CaConvertUtil.intToByteArr(r2, 2)[1]});
            return true;
        } else if (i == 87) {
            try {
                r6 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e22222222) {
                CaLogger.error("DPCM setProperty Exception: " + e22222222.getMessage().toString() + ", scenario = 15");
                r6 = 15;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 4, new byte[]{CaConvertUtil.intToByteArr(r6, 4)[0], CaConvertUtil.intToByteArr(r6, 4)[1], CaConvertUtil.intToByteArr(r6, 4)[2], CaConvertUtil.intToByteArr(r6, 4)[3], (byte) 1});
            return true;
        } else if (i != 88) {
            return false;
        } else {
            try {
                r6 = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
            } catch (Throwable e222222222) {
                CaLogger.error("DPCM setProperty Exception: " + e222222222.getMessage().toString() + ", scenario = 15");
                r6 = 15;
            }
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE, (byte) 4, new byte[]{CaConvertUtil.intToByteArr(r6, 4)[0], CaConvertUtil.intToByteArr(r6, 4)[1], CaConvertUtil.intToByteArr(r6, 4)[2], CaConvertUtil.intToByteArr(r6, 4)[3], (byte) 2});
            return true;
        }
    }
}
