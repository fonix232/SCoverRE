package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;

public abstract class VoiceLibProvider extends SensorHubProvider {
    protected VoiceLibProvider(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    protected final byte getInstructionForDisable() {
        return ISensorHubCmdProtocol.INST_VOICE_REMOVE;
    }

    protected final byte getInstructionForEnable() {
        return ISensorHubCmdProtocol.INST_VOICE_ADD;
    }
}
