package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorHandler;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class LibTypeParser extends TypeParser {
    LibTypeParser() {
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
                if (SensorHubMultiModeParser.getInstance().containsParser(DATA_TYPE.getCode(enumR.value))) {
                    ISensorHubParser parser2 = SensorHubMultiModeParser.getInstance().getParser(DATA_TYPE.getCode(enumR.value));
                    if (parser2 == null) {
                        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
                    } else {
                        i3 = parser2.parse(bArr, i + 1);
                    }
                } else {
                    i3 = enumR.value == DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.value ? EnvironmentSensorHandler.getInstance().parse(bArr, i + 1) : parser.parse(bArr, i + 1);
                }
                if (i3 == i) {
                    return i3;
                }
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
                return -1;
            }
        }
        if (i3 == i) {
            return i3;
        }
        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
        return -1;
    }
}
