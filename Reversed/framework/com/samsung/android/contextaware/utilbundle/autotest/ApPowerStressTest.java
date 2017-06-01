package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.INSTRUCTION;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.LIB_TYPE;
import com.samsung.android.contextaware.utilbundle.IApPowerObservable;
import java.util.Random;

class ApPowerStressTest extends InnerProcessStressTest {
    protected ApPowerStressTest(int i) {
        super(i);
    }

    private byte[] getPacket(byte b) {
        return new byte[]{INSTRUCTION.INST_NOTI.value, LIB_TYPE.TYPE_NOTI_POWER.value, b};
    }

    protected final byte[] getPacket(int i) {
        switch (new Random().nextInt(2)) {
            case 0:
                return getPacket((byte) IApPowerObservable.AP_WAKEUP);
            case 1:
                return getPacket((byte) IApPowerObservable.AP_SLEEP);
            default:
                return new byte[0];
        }
    }
}
