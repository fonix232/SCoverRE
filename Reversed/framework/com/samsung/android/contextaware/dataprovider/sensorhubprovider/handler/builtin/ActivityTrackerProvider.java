package com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public abstract class ActivityTrackerProvider extends LibTypeProvider {
    public ActivityTrackerProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        ActivityTrackerHandler.getInstance().initialize(context, looper);
    }

    protected final long byteArrayToLong(byte[] bArr) {
        int i;
        ByteBuffer allocate = ByteBuffer.allocate(8);
        byte[] bArr2 = new byte[8];
        for (i = 0; i < 8; i++) {
            bArr2[i] = (byte) 0;
        }
        for (i = 0; i < bArr.length; i++) {
            bArr2[7 - i] = bArr[(bArr.length - 1) - i];
        }
        allocate = ByteBuffer.wrap(bArr2);
        allocate.order(ByteOrder.BIG_ENDIAN);
        return allocate.getLong();
    }

    public void disable() {
        ActivityTrackerHandler.getInstance().disable();
        super.disable();
    }

    public void enable() {
        ActivityTrackerHandler.getInstance().enable();
        super.enable();
    }

    protected byte getAccuracyType() {
        return (byte) 0;
    }

    protected int getActivityType() {
        return 0;
    }

    protected int getBatchingPeriod() {
        return 0;
    }

    public String[] getContextValueNames() {
        return new String[]{"OperationMode", "TimeStamp", "ActivityType", "Accuracy"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] bArr = new byte[11];
        bArr[0] = getModeType();
        if (getActivityType() < 0) {
            return null;
        }
        byte[] intToByteArr = CaConvertUtil.intToByteArr(getActivityType(), 4);
        bArr[1] = intToByteArr[0];
        bArr[2] = intToByteArr[1];
        bArr[3] = intToByteArr[2];
        bArr[4] = intToByteArr[3];
        bArr[5] = getAccuracyType();
        byte[] intToByteArr2 = CaConvertUtil.intToByteArr(getBatchingPeriod(), 2);
        bArr[6] = intToByteArr2[0];
        bArr[7] = intToByteArr2[1];
        Calendar instance = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int i = instance.get(11);
        int i2 = instance.get(12);
        int i3 = instance.get(13);
        bArr[8] = (byte) i;
        bArr[9] = (byte) i2;
        bArr[10] = (byte) i3;
        return bArr;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        byte[] bArr = new byte[8];
        bArr[0] = getModeType();
        if (getActivityType() < 0) {
            return null;
        }
        byte[] intToByteArr = CaConvertUtil.intToByteArr(getActivityType(), 4);
        bArr[1] = intToByteArr[0];
        bArr[2] = intToByteArr[1];
        bArr[3] = intToByteArr[2];
        bArr[4] = intToByteArr[3];
        bArr[5] = getAccuracyType();
        byte[] intToByteArr2 = CaConvertUtil.intToByteArr(getBatchingPeriod(), 2);
        bArr[6] = intToByteArr2[0];
        bArr[7] = intToByteArr2[1];
        return bArr;
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_EXPOEASEOUT;
    }

    protected abstract byte getModeType();

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 4 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] contextValueNames = getContextValueNames();
        getContextBean().putContext(contextValueNames[0], (short) getModeType());
        r1 = new byte[4];
        i2 = i + 1;
        r1[0] = bArr[i];
        int i3 = i2 + 1;
        r1[1] = bArr[i2];
        i2 = i3 + 1;
        r1[2] = bArr[i3];
        i3 = i2 + 1;
        r1[3] = bArr[i2];
        getContextBean().putContext(contextValueNames[1], CaTimeManager.getInstance().getTimeStampForUTC(byteArrayToLong(r1)));
        i2 = parseData(bArr, i3);
        if (i2 > 0) {
            notifyObserver();
        }
        return i2;
    }

    protected int parseData(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] contextValueNames = getContextValueNames();
        i2 = i + 1;
        getContextBean().putContext(contextValueNames[2], bArr[i]);
        int i3 = i2 + 1;
        getContextBean().putContext(contextValueNames[3], bArr[i2]);
        return i3;
    }
}
