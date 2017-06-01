package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;

public class CaAutoBrightnessTableManager implements IUtilManager {
    private static CaAutoBrightnessTableManager instance = new CaAutoBrightnessTableManager();
    private byte[] mOffsetTable;

    public static CaAutoBrightnessTableManager getInstance() {
        return instance;
    }

    public void initializeManager(Context context) {
    }

    public boolean sendAutoBrightnessTableToSensorHub() {
        int sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(this.mOffsetTable, (byte) -63, SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT90, (byte) 2);
        if (sendCmdToSensorHub == SensorHubErrors.SUCCESS.getCode()) {
            return true;
        }
        CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
        return false;
    }

    public void setOffsetTable(byte[] bArr) {
        this.mOffsetTable = bArr;
    }

    public void terminateManager() {
    }
}
