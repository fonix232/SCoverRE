package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.test.FlakyTest;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.sensorhub.SensorHubManager;

public class SensorHubCommManager implements IUtilManager {
    private static volatile SensorHubCommManager instance;
    private SensorHubManager mSensorHubManager;

    private byte[] generatePacket(byte[] bArr, byte... bArr2) {
        int i = 0;
        int length = bArr2.length;
        if (length < 2 || length > 4) {
            return null;
        }
        byte[] bArr3 = new byte[(bArr.length + length)];
        int length2 = bArr2.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length2) {
            int i4 = i3 + 1;
            bArr3[i3] = bArr2[i2];
            i2++;
            i3 = i4;
        }
        i2 = bArr.length;
        while (i < i2) {
            i4 = i3 + 1;
            bArr3[i3] = bArr[i];
            i++;
            i3 = i4;
        }
        return bArr3;
    }

    public static SensorHubCommManager getInstance() {
        if (instance == null) {
            synchronized (SensorHubCommManager.class) {
                if (instance == null) {
                    instance = new SensorHubCommManager();
                }
            }
        }
        return instance;
    }

    private SensorHubManager getSensorHubManager() {
        return this.mSensorHubManager;
    }

    private void setSensorHubManager(SensorHubManager sensorHubManager) {
        this.mSensorHubManager = sensorHubManager;
    }

    public final void initializeManager(Context context) {
        if (context == null) {
            CaLogger.error("Context is null");
            return;
        }
        setSensorHubManager((SensorHubManager) context.getSystemService("sensorhub"));
        if (getSensorHubManager() == null) {
            CaLogger.error("mSensorHubManager is null.");
        }
    }

    public final int sendCmdToSensorHub(byte[] bArr, byte... bArr2) {
        int length = bArr2.length;
        if (length < 2 || length > 4) {
            return SensorHubErrors.ERROR_CMD_PACKET_HEADER_LENGTH.getCode();
        }
        byte[] generatePacket = generatePacket(bArr, bArr2);
        if (generatePacket == null || generatePacket.length <= 0) {
            return SensorHubErrors.ERROR_CMD_PACKET_GENERATION_FAIL.getCode();
        }
        String byteArrToString = CaConvertUtil.byteArrToString(generatePacket);
        if (byteArrToString == null || byteArrToString.isEmpty()) {
            CaLogger.warning("Packet is null");
        } else {
            CaLogger.info(byteArrToString);
        }
        int sendPacketToSensorHub = sendPacketToSensorHub(generatePacket.length, generatePacket);
        if (sendPacketToSensorHub != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(sendPacketToSensorHub));
        }
        return sendPacketToSensorHub;
    }

    public final int sendPacketToSensorHub(int i, byte[] bArr) {
        if (getSensorHubManager() == null) {
            return SensorHubErrors.ERROR_SENSOR_HUB_MANAGER_NULL_EXEPTION.getCode();
        }
        int SendSensorHubData = getSensorHubManager().SendSensorHubData(getSensorHubManager().getDefaultSensorHub(1), i, bArr);
        SendSensorHubData = SendSensorHubData > 0 ? SensorHubErrors.SUCCESS.getCode() : SendSensorHubData == -5 ? SensorHubErrors.ERROR_I2C_COMM.getCode() : SendSensorHubData == -11 ? SensorHubErrors.ERROR_NOT_RECEIVE_ACK.getCode() : SensorHubErrors.ERROR_SENSOR_HUB_MANAGER_FAULT.getCode();
        if (SendSensorHubData != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(SendSensorHubData));
            String byteArrToString = CaConvertUtil.byteArrToString(bArr);
            if (byteArrToString == null || byteArrToString.isEmpty()) {
                CaLogger.warning("Packet is null");
            } else {
                CaLogger.error("Unable to deliver: " + byteArrToString);
            }
            SendSensorHubData = SensorHubErrors.SUCCESS.getCode();
        }
        return SendSensorHubData;
    }

    public final void terminateManager() {
        setSensorHubManager(null);
    }

    @FlakyTest
    public final byte[] testGeneratePacket(byte[] bArr, byte... bArr2) {
        return generatePacket(bArr, bArr2);
    }

    @FlakyTest
    public final SensorHubManager testGetSensorHubManager() {
        return this.mSensorHubManager;
    }
}
