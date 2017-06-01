package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class CaTimeManager {
    private static volatile CaTimeManager instance;

    public static CaTimeManager getInstance() {
        if (instance == null) {
            synchronized (CaTimeManager.class) {
                if (instance == null) {
                    instance = new CaTimeManager();
                }
            }
        }
        return instance;
    }

    public final long getTimeStampForUTC(long j) {
        Calendar instance = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int i = instance.get(11);
        int i2 = instance.get(12);
        return getTimeStampForUTC24((long) (((((i * 3600) + (i2 * 60)) + instance.get(13)) * 1000) + instance.get(14)), instance.getTimeInMillis(), j);
    }

    public final long getTimeStampForUTC(long j, long j2, long j3) {
        long j4 = j - j3;
        if (j4 < 0) {
            j4 += 86400000;
        }
        return j2 - j4;
    }

    public final long getTimeStampForUTC24(long j, long j2, long j3) {
        long j4 = j - j3;
        if (j4 < 0) {
            j4 = j4 < -84500000 ? j4 + 86400000 : 0;
        }
        return j2 - j4;
    }

    public final void sendCurTimeToSensorHub() {
        r0 = new byte[3];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        r0[0] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        r0[1] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        r0[2] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        int sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(r0, (byte) -63, SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEOUT);
        if (sendCmdToSensorHub != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
        }
    }
}
