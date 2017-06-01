package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;

public abstract class EnvironmentSensorProvider extends LibTypeProvider {
    private int mLoggingStatus = 0;

    protected EnvironmentSensorProvider(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    private String getDisplayLoggingStatus(int i) {
        return i == 2 ? "AP_SLEEP" : i == 1 ? "AP_WAKEUP" : null;
    }

    protected final String getDisplayContents(Bundle bundle, String str) {
        int i = 0;
        if (str == null || str.isEmpty()) {
            CaLogger.error("key is null");
            return null;
        } else if (bundle == null || !bundle.containsKey(str)) {
            CaLogger.error("bundle is null");
            return null;
        } else {
            Object obj = getContextValueNames()[0];
            StringBuffer stringBuffer = new StringBuffer();
            if (str.equals(obj)) {
                try {
                    if (bundle.getString(str) == null) {
                        CaLogger.error("bundle.getString(key) is null");
                        return null;
                    }
                    stringBuffer.append(getDisplayLoggingStatus(Integer.valueOf(bundle.getString(str)).intValue()));
                } catch (Throwable e) {
                    CaLogger.exception(e);
                    return null;
                }
            }
            String[] stringArray = bundle.getStringArray(str);
            if (stringArray == null || stringArray.length <= 0) {
                CaLogger.error("bundle.getStringArray(key) is null");
                return null;
            }
            int length = stringArray.length;
            while (i < length) {
                stringBuffer.append(stringArray[i] + ", ");
                i++;
            }
            if (stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                stringBuffer.delete(stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), stringBuffer.length());
            }
            return stringBuffer.toString();
        }
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_BACKEASEINOUT;
    }

    protected final int getInterval() {
        return EnvironmentSensorHandler.getInstance().getInterval();
    }

    protected final int getLoggingStatus() {
        return this.mLoggingStatus;
    }

    protected final void setLoggingStatus(int i) {
        this.mLoggingStatus = i;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i != 13) {
            return false;
        }
        EnvironmentSensorHandler.getInstance().setInterval(((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue());
        CaLogger.info("setProperty (Interval) = " + Integer.toString(getInterval()));
        return true;
    }
}
