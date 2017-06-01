package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;

class CaOperationDebugging extends OperationDebugging {
    protected CaOperationDebugging(int i) {
        super(i);
    }

    protected final void doDebugging(byte[] bArr) {
        SensorHubParserProvider.getInstance().parseForScenarioTesting(bArr);
    }
}
