package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Random;

abstract class InnerProcessStressTest extends CaAutoTest {
    protected InnerProcessStressTest(int i) {
        super(i);
    }

    protected abstract byte[] getPacket(int i);

    public final void run() {
        while (true) {
            try {
                Thread.sleep((long) getDelayTime());
                byte[] packet = getPacket(new Random().nextInt(3));
                if (packet != null && packet.length > 0) {
                    for (byte num : packet) {
                        CaLogger.info("Packet = " + Integer.toString(num));
                    }
                    SensorHubParserProvider.getInstance().parseForScenarioTesting(packet);
                    if (super.isStopTest()) {
                        return;
                    }
                }
            } catch (Throwable e) {
                CaLogger.exception(e);
                return;
            }
        }
    }
}
