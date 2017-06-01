package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.DataProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubSyntax.DATATYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.ISensorHubRequestParser;
import com.samsung.android.contextaware.manager.ContextBean;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.SensorHubCommManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SensorHubProvider extends DataProvider implements ISensorHubParser, ISensorHubCmdProtocol, ISensorHubRequest {
    private static final /* synthetic */ int[] f170x876dae4d = null;
    public static final int I2C_COMM_ERROR = -5;
    public static final int NOT_RECEIVE_ACK = -11;
    private int mFaultDetectionResult;
    private final CopyOnWriteArrayList<ISensorHubRequestParser> mRequestParserList = new CopyOnWriteArrayList();

    private static /* synthetic */ int[] m100x36590d29() {
        if (f170x876dae4d != null) {
            return f170x876dae4d;
        }
        int[] iArr = new int[DATATYPE.values().length];
        try {
            iArr[DATATYPE.BOOLEAN.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[DATATYPE.BYTE.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[DATATYPE.DOUBLE2.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[DATATYPE.DOUBLE3.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[DATATYPE.DOUBLE4.ordinal()] = 5;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[DATATYPE.FLOAT2.ordinal()] = 6;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[DATATYPE.FLOAT3.ordinal()] = 7;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[DATATYPE.FLOAT4.ordinal()] = 8;
        } catch (NoSuchFieldError e8) {
        }
        try {
            iArr[DATATYPE.INTEGER.ordinal()] = 9;
        } catch (NoSuchFieldError e9) {
        }
        try {
            iArr[DATATYPE.INTEGER3.ordinal()] = 10;
        } catch (NoSuchFieldError e10) {
        }
        try {
            iArr[DATATYPE.LONG.ordinal()] = 11;
        } catch (NoSuchFieldError e11) {
        }
        try {
            iArr[DATATYPE.MESSAGE_TYPE.ordinal()] = 13;
        } catch (NoSuchFieldError e12) {
        }
        try {
            iArr[DATATYPE.REPEATLIST.ordinal()] = 14;
        } catch (NoSuchFieldError e13) {
        }
        try {
            iArr[DATATYPE.SHORT.ordinal()] = 12;
        } catch (NoSuchFieldError e14) {
        }
        f170x876dae4d = iArr;
        return iArr;
    }

    protected SensorHubProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
    }

    protected final void addRequestParser(ISensorHubRequestParser iSensorHubRequestParser) {
        if (this.mRequestParserList != null && !this.mRequestParserList.contains(iSensorHubRequestParser)) {
            this.mRequestParserList.add(iSensorHubRequestParser);
        }
    }

    protected final boolean checkFaultDetectionResult() {
        return this.mFaultDetectionResult == SensorHubErrors.SUCCESS.getCode();
    }

    public void clear() {
        this.mFaultDetectionResult = 1;
        super.clear();
    }

    public void disable() {
        byte[] dataPacketToUnregisterLib = getDataPacketToUnregisterLib();
        if (getInstLibType() < (byte) 0 || getInstructionForDisable() == (byte) 0 || dataPacketToUnregisterLib == null || dataPacketToUnregisterLib.length <= 0) {
            this.mFaultDetectionResult = SensorHubErrors.ERROR_CMD_PACKET_CREATION_FAULT.getCode();
        } else {
            sendCmdToSensorHub(getInstructionForDisable(), getInstLibType(), dataPacketToUnregisterLib);
        }
    }

    protected final void disableForRestore() {
    }

    public void enable() {
        byte[] dataPacketToRegisterLib = getDataPacketToRegisterLib();
        if (getInstLibType() < (byte) 0 || dataPacketToRegisterLib == null || dataPacketToRegisterLib.length <= 0) {
            this.mFaultDetectionResult = SensorHubErrors.ERROR_CMD_PACKET_CREATION_FAULT.getCode();
        } else {
            sendCmdToSensorHub(getInstructionForEnable(), getInstLibType(), dataPacketToRegisterLib);
        }
    }

    protected final void enableForRestore() {
    }

    public String[] getContextValueNames() {
        return new String[]{"Action"};
    }

    protected byte[] getDataPacketToRegisterLib() {
        return new byte[2];
    }

    protected byte[] getDataPacketToUnregisterLib() {
        return new byte[2];
    }

    public Bundle getFaultDetectionResult() {
        return getFaultDetectionResult(checkFaultDetectionResult() ? 0 : 1, SensorHubErrors.getMessage(this.mFaultDetectionResult));
    }

    protected abstract byte getInstLibType();

    protected abstract byte getInstructionForDisable();

    protected abstract byte getInstructionForEnable();

    protected ArrayList<ArrayList<SensorHubSyntax>> getParseSyntaxTable() {
        return null;
    }

    protected final void initializeManager() {
    }

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        Iterable<ArrayList> parseSyntaxTable = getParseSyntaxTable();
        if (parseSyntaxTable == null || parseSyntaxTable.size() == 0) {
            String str = getContextValueNames()[0];
            if (str == null || str.isEmpty() || (bArr.length - i) - 1 < 0) {
                return -1;
            }
            i2 = i + 1;
            super.getContextBean().putContext(str, bArr[i]);
        } else {
            Iterable iterable = null;
            if (((SensorHubSyntax) ((ArrayList) parseSyntaxTable.get(0)).get(0)).dataType() != DATATYPE.MESSAGE_TYPE) {
                iterable = (ArrayList) parseSyntaxTable.get(0);
            } else if ((bArr.length - i) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else {
                i2 = i + 1;
                byte b = bArr[i];
                for (ArrayList arrayList : parseSyntaxTable) {
                    if (((SensorHubSyntax) arrayList.get(0)).messageType() == b) {
                        iterable = arrayList;
                        super.getContextBean().putContext(((SensorHubSyntax) arrayList.get(0)).name(), b);
                        break;
                    }
                }
                if (iterable == null) {
                    CaLogger.error(SensorHubErrors.ERROR_EMPTY_REQUEST_LIST.getMessage());
                    return -1;
                }
            }
            int i3 = 0;
            byte b2 = (byte) 0;
            for (SensorHubSyntax sensorHubSyntax : r22) {
                if ((bArr.length - i2) - sensorHubSyntax.size() < 0) {
                    CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                    return -1;
                }
                int i4;
                byte[] bArr2;
                ContextBean contextBean;
                String name;
                byte[] bArr3;
                switch (m100x36590d29()[sensorHubSyntax.dataType().ordinal()]) {
                    case 1:
                        i4 = i2 + 1;
                        super.getContextBean().putContext(sensorHubSyntax.name(), bArr[i2] != (byte) 0);
                        i2 = i4;
                        break;
                    case 2:
                        i4 = i2 + 1;
                        b2 = bArr[i2];
                        super.getContextBean().putContext(sensorHubSyntax.name(), b2 / ((int) sensorHubSyntax.scale()));
                        i2 = i4;
                        break;
                    case 3:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[4];
                        i4 = i2 + 1;
                        bArr3[2] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[3] = bArr[i4];
                        contextBean.putContext(name, ((double) ByteBuffer.wrap(bArr3).getInt()) / sensorHubSyntax.scale());
                        break;
                    case 4:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[4];
                        bArr3[0] = (byte) 0;
                        i4 = i2 + 1;
                        bArr3[1] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[2] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[3] = bArr[i2];
                        contextBean.putContext(name, ((double) ByteBuffer.wrap(bArr3).getInt()) / sensorHubSyntax.scale());
                        i2 = i4;
                        break;
                    case 5:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[4];
                        i4 = i2 + 1;
                        bArr3[0] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[1] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[2] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[3] = bArr[i4];
                        contextBean.putContext(name, ((double) ByteBuffer.wrap(bArr3).getInt()) / sensorHubSyntax.scale());
                        break;
                    case 6:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[4];
                        i4 = i2 + 1;
                        bArr3[2] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[3] = bArr[i4];
                        contextBean.putContext(name, ((float) ByteBuffer.wrap(bArr3).getInt()) / ((float) sensorHubSyntax.scale()));
                        break;
                    case 7:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[4];
                        bArr3[0] = (byte) 0;
                        i4 = i2 + 1;
                        bArr3[1] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[2] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[3] = bArr[i2];
                        contextBean.putContext(name, ((float) ByteBuffer.wrap(bArr3).getInt()) / ((float) sensorHubSyntax.scale()));
                        i2 = i4;
                        break;
                    case 8:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[4];
                        i4 = i2 + 1;
                        bArr3[0] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[1] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[2] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[3] = bArr[i4];
                        contextBean.putContext(name, ((float) ByteBuffer.wrap(bArr3).getInt()) / ((float) sensorHubSyntax.scale()));
                        break;
                    case 9:
                        if (!sensorHubSyntax.name().equals("TimeStamp")) {
                            contextBean = super.getContextBean();
                            name = sensorHubSyntax.name();
                            bArr3 = new byte[4];
                            i4 = i2 + 1;
                            bArr3[0] = bArr[i2];
                            i2 = i4 + 1;
                            bArr3[1] = bArr[i4];
                            i4 = i2 + 1;
                            bArr3[2] = bArr[i2];
                            i2 = i4 + 1;
                            bArr3[3] = bArr[i4];
                            contextBean.putContext(name, ByteBuffer.wrap(bArr3).getInt() / ((int) sensorHubSyntax.scale()));
                            break;
                        }
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        CaTimeManager instance = CaTimeManager.getInstance();
                        r30 = new byte[8];
                        i4 = i2 + 1;
                        r30[4] = bArr[i2];
                        i2 = i4 + 1;
                        r30[5] = bArr[i4];
                        i4 = i2 + 1;
                        r30[6] = bArr[i2];
                        i2 = i4 + 1;
                        r30[7] = bArr[i4];
                        contextBean.putContext(name, instance.getTimeStampForUTC(ByteBuffer.wrap(r30).getLong()));
                        break;
                    case 10:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[4];
                        bArr3[0] = (byte) 0;
                        i4 = i2 + 1;
                        bArr3[1] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[2] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[3] = bArr[i2];
                        contextBean.putContext(name, ByteBuffer.wrap(bArr3).getInt() / ((int) sensorHubSyntax.scale()));
                        i2 = i4;
                        break;
                    case 11:
                        contextBean = super.getContextBean();
                        name = sensorHubSyntax.name();
                        bArr3 = new byte[8];
                        i4 = i2 + 1;
                        bArr3[0] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[1] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[2] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[3] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[4] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[5] = bArr[i4];
                        i4 = i2 + 1;
                        bArr3[6] = bArr[i2];
                        i2 = i4 + 1;
                        bArr3[7] = bArr[i4];
                        contextBean.putContext(name, ByteBuffer.wrap(bArr3).getLong() / ((long) ((int) sensorHubSyntax.scale())));
                        break;
                    case 12:
                        bArr2 = new byte[4];
                        i4 = i2 + 1;
                        bArr2[2] = bArr[i2];
                        i2 = i4 + 1;
                        bArr2[3] = bArr[i4];
                        b2 = ByteBuffer.wrap(bArr2).getInt();
                        super.getContextBean().putContext(sensorHubSyntax.name(), b2 / ((int) sensorHubSyntax.scale()));
                        break;
                }
                if (sensorHubSyntax.name().equals("DataSize") || sensorHubSyntax.name().equals("DataCount")) {
                    i3 = b2;
                }
                Iterable<SensorHubSyntax> repeatList = sensorHubSyntax.repeatList();
                if (repeatList != null) {
                    int i5 = 0;
                    HashMap hashMap = new HashMap();
                    for (SensorHubSyntax sensorHubSyntax2 : repeatList) {
                        switch (m100x36590d29()[sensorHubSyntax2.dataType().ordinal()]) {
                            case 2:
                            case 9:
                            case 10:
                            case 12:
                                if (!sensorHubSyntax2.name().equals("TimeStampArray")) {
                                    hashMap.put(sensorHubSyntax2.name(), new int[i3]);
                                    break;
                                }
                                hashMap.put(sensorHubSyntax2.name(), new long[i3]);
                                break;
                            case 3:
                            case 4:
                            case 5:
                                hashMap.put(sensorHubSyntax2.name(), new double[i3]);
                                break;
                            case 6:
                            case 7:
                            case 8:
                                hashMap.put(sensorHubSyntax2.name(), new float[i3]);
                                break;
                            case 11:
                                hashMap.put(sensorHubSyntax2.name(), new long[i3]);
                                break;
                            default:
                                break;
                        }
                        i5 += sensorHubSyntax2.size();
                    }
                    if ((bArr.length - i2) - (i5 * i3) >= 0) {
                        for (int i6 = 0; i6 < i3; i6++) {
                            for (SensorHubSyntax sensorHubSyntax22 : repeatList) {
                                int[] iArr;
                                double[] dArr;
                                float[] fArr;
                                long[] jArr;
                                switch (m100x36590d29()[sensorHubSyntax22.dataType().ordinal()]) {
                                    case 2:
                                        iArr = (int[]) hashMap.get(sensorHubSyntax22.name());
                                        i4 = i2 + 1;
                                        iArr[i6] = bArr[i2] / ((int) sensorHubSyntax22.scale());
                                        hashMap.put(sensorHubSyntax22.name(), iArr);
                                        i2 = i4;
                                        break;
                                    case 3:
                                        dArr = (double[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        i4 = i2 + 1;
                                        bArr2[2] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[3] = bArr[i4];
                                        dArr[i6] = ((double) ByteBuffer.wrap(bArr2).getInt()) / sensorHubSyntax22.scale();
                                        hashMap.put(sensorHubSyntax22.name(), dArr);
                                        break;
                                    case 4:
                                        dArr = (double[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        bArr2[0] = (byte) 0;
                                        i4 = i2 + 1;
                                        bArr2[1] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[2] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[3] = bArr[i2];
                                        dArr[i6] = ((double) ByteBuffer.wrap(bArr2).getInt()) / sensorHubSyntax22.scale();
                                        hashMap.put(sensorHubSyntax22.name(), dArr);
                                        i2 = i4;
                                        break;
                                    case 5:
                                        dArr = (double[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        i4 = i2 + 1;
                                        bArr2[0] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[1] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[2] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[3] = bArr[i4];
                                        dArr[i6] = ((double) ByteBuffer.wrap(bArr2).getInt()) / sensorHubSyntax22.scale();
                                        hashMap.put(sensorHubSyntax22.name(), dArr);
                                        break;
                                    case 6:
                                        fArr = (float[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        i4 = i2 + 1;
                                        bArr2[2] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[3] = bArr[i4];
                                        fArr[i6] = ((float) ByteBuffer.wrap(bArr2).getInt()) / ((float) sensorHubSyntax22.scale());
                                        hashMap.put(sensorHubSyntax22.name(), fArr);
                                        break;
                                    case 7:
                                        fArr = (float[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        bArr2[0] = (byte) 0;
                                        i4 = i2 + 1;
                                        bArr2[1] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[2] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[3] = bArr[i2];
                                        fArr[i6] = ((float) ByteBuffer.wrap(bArr2).getInt()) / ((float) sensorHubSyntax22.scale());
                                        hashMap.put(sensorHubSyntax22.name(), fArr);
                                        i2 = i4;
                                        break;
                                    case 8:
                                        fArr = (float[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        i4 = i2 + 1;
                                        bArr2[0] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[1] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[2] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[3] = bArr[i4];
                                        fArr[i6] = ((float) ByteBuffer.wrap(bArr2).getInt()) / ((float) sensorHubSyntax22.scale());
                                        hashMap.put(sensorHubSyntax22.name(), fArr);
                                        break;
                                    case 9:
                                        if (!sensorHubSyntax22.name().equals("TimeStampArray")) {
                                            iArr = (int[]) hashMap.get(sensorHubSyntax22.name());
                                            bArr2 = new byte[4];
                                            i4 = i2 + 1;
                                            bArr2[0] = bArr[i2];
                                            i2 = i4 + 1;
                                            bArr2[1] = bArr[i4];
                                            i4 = i2 + 1;
                                            bArr2[2] = bArr[i2];
                                            i2 = i4 + 1;
                                            bArr2[3] = bArr[i4];
                                            iArr[i6] = ByteBuffer.wrap(bArr2).getInt() / ((int) sensorHubSyntax22.scale());
                                            hashMap.put(sensorHubSyntax22.name(), iArr);
                                            break;
                                        }
                                        jArr = (long[]) hashMap.get(sensorHubSyntax22.name());
                                        CaTimeManager instance2 = CaTimeManager.getInstance();
                                        r28 = new byte[8];
                                        i4 = i2 + 1;
                                        r28[4] = bArr[i2];
                                        i2 = i4 + 1;
                                        r28[5] = bArr[i4];
                                        i4 = i2 + 1;
                                        r28[6] = bArr[i2];
                                        i2 = i4 + 1;
                                        r28[7] = bArr[i4];
                                        jArr[i6] = instance2.getTimeStampForUTC(ByteBuffer.wrap(r28).getLong());
                                        break;
                                    case 10:
                                        iArr = (int[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        bArr2[0] = (byte) 0;
                                        i4 = i2 + 1;
                                        bArr2[1] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[2] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[3] = bArr[i2];
                                        iArr[i6] = ByteBuffer.wrap(bArr2).getInt() / ((int) sensorHubSyntax22.scale());
                                        hashMap.put(sensorHubSyntax22.name(), iArr);
                                        i2 = i4;
                                        break;
                                    case 11:
                                        jArr = (long[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[8];
                                        i4 = i2 + 1;
                                        bArr2[0] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[1] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[2] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[3] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[4] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[5] = bArr[i4];
                                        i4 = i2 + 1;
                                        bArr2[6] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[7] = bArr[i4];
                                        jArr[i6] = ByteBuffer.wrap(bArr2).getLong() / ((long) ((int) sensorHubSyntax22.scale()));
                                        hashMap.put(sensorHubSyntax22.name(), jArr);
                                        break;
                                    case 12:
                                        iArr = (int[]) hashMap.get(sensorHubSyntax22.name());
                                        bArr2 = new byte[4];
                                        i4 = i2 + 1;
                                        bArr2[2] = bArr[i2];
                                        i2 = i4 + 1;
                                        bArr2[3] = bArr[i4];
                                        iArr[i6] = ByteBuffer.wrap(bArr2).getInt() / ((int) sensorHubSyntax22.scale());
                                        hashMap.put(sensorHubSyntax22.name(), iArr);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        for (SensorHubSyntax sensorHubSyntax222 : repeatList) {
                            switch (m100x36590d29()[sensorHubSyntax222.dataType().ordinal()]) {
                                case 2:
                                case 9:
                                case 10:
                                case 12:
                                    if (!sensorHubSyntax222.name().equals("TimeStampArray")) {
                                        super.getContextBean().putContext(sensorHubSyntax222.name(), (int[]) hashMap.get(sensorHubSyntax222.name()));
                                        break;
                                    }
                                    super.getContextBean().putContext(sensorHubSyntax222.name(), (long[]) hashMap.get(sensorHubSyntax222.name()));
                                    break;
                                case 3:
                                case 4:
                                case 5:
                                    super.getContextBean().putContext(sensorHubSyntax222.name(), (double[]) hashMap.get(sensorHubSyntax222.name()));
                                    break;
                                case 6:
                                case 7:
                                case 8:
                                    super.getContextBean().putContext(sensorHubSyntax222.name(), (float[]) hashMap.get(sensorHubSyntax222.name()));
                                    break;
                                case 11:
                                    super.getContextBean().putContext(sensorHubSyntax222.name(), (long[]) hashMap.get(sensorHubSyntax222.name()));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                    return -1;
                }
            }
        }
        super.notifyObserver();
        return i2;
    }

    public final int parseForRequestType(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        if (this.mRequestParserList == null || this.mRequestParserList.isEmpty()) {
            CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_EMPTY_REQUEST_LIST.getCode()));
            return -1;
        }
        for (ISensorHubRequestParser iSensorHubRequestParser : this.mRequestParserList) {
            if (b == iSensorHubRequestParser.getRequestType()) {
                i2 = iSensorHubRequestParser.parse(bArr, i2);
                break;
            }
        }
        return i2;
    }

    public final void pause() {
    }

    protected final void removeRequestParser(ISensorHubRequestParser iSensorHubRequestParser) {
        if (this.mRequestParserList != null && this.mRequestParserList.contains(iSensorHubRequestParser)) {
            this.mRequestParserList.remove(iSensorHubRequestParser);
        }
    }

    protected final void reset() {
        enable();
    }

    public final void resume() {
    }

    public final void sendCmdToSensorHub(byte b, byte b2, byte[] bArr) {
        this.mFaultDetectionResult = SensorHubCommManager.getInstance().sendCmdToSensorHub(bArr, new byte[]{b, b2});
    }

    protected final boolean sendCommonValueToSensorHub(byte b, byte[] bArr) {
        int sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(bArr, new byte[]{ISensorHubCmdProtocol.INST_LIB_PUTVALUE, b});
        if (sendCmdToSensorHub == SensorHubErrors.SUCCESS.getCode()) {
            return true;
        }
        CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
        return false;
    }

    protected final boolean sendPropertyValueToSensorHub(byte b, byte b2, byte b3, byte[] bArr) {
        int sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(bArr, new byte[]{ISensorHubCmdProtocol.INST_LIB_PUTVALUE, b, b2, b3});
        if (sendCmdToSensorHub == SensorHubErrors.SUCCESS.getCode()) {
            return true;
        }
        CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
        return false;
    }

    protected final boolean sendPropertyValueToSensorHub(byte b, byte b2, byte[] bArr) {
        int sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(bArr, new byte[]{ISensorHubCmdProtocol.INST_LIB_PUTVALUE, b, b2});
        if (sendCmdToSensorHub == SensorHubErrors.SUCCESS.getCode()) {
            return true;
        }
        CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
        return false;
    }

    protected final void terminateManager() {
    }
}
