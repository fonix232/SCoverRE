package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class RequestLibTypeParser extends TypeParser implements ISensorHubParser {
    RequestLibTypeParser() {
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = 0;
        int i3 = i;
        DATA_TYPE[] values = DATA_TYPE.values();
        int length = values.length;
        while (i2 < length) {
            Enum enumR = values[i2];
            if ((bArr.length - i) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else if (bArr[i] != enumR.value) {
                i2++;
            } else {
                ISensorHubParser parser = super.getParser(enumR.toString());
                if (parser == null) {
                    CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
                    return -1;
                }
                i3 = ((ISensorHubRequest) parser).parseForRequestType(bArr, i + 1);
                return i3;
            }
        }
        return i3;
    }
}
