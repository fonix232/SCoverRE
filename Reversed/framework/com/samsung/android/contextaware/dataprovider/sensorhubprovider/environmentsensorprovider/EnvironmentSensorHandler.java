package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserBean;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SUB_DATA_TYPE;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class EnvironmentSensorHandler extends SensorHubParserBean {
    private static final int DEFAULT_INTERVAL = 60;
    private static volatile EnvironmentSensorHandler instance;
    private int mInterval = 60;

    public static EnvironmentSensorHandler getInstance() {
        if (instance == null) {
            synchronized (EnvironmentSensorHandler.class) {
                if (instance == null) {
                    instance = new EnvironmentSensorHandler();
                }
            }
        }
        return instance;
    }

    private String getParserKey(int i) {
        for (SUB_DATA_TYPE sub_data_type : SUB_DATA_TYPE.values()) {
            if (i == sub_data_type.value && super.checkParserMap(sub_data_type.toString())) {
                return sub_data_type.toString();
            }
        }
        return null;
    }

    protected final int getInterval() {
        return this.mInterval;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        if (!super.checkParserMap() || (bArr.length - i) - 1 < 0) {
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        if (b != (byte) 2 && b != (byte) 1) {
            CaLogger.error(SensorHubErrors.ERROR_ENVIRONMENT_LOGGING_STATE.getMessage());
            return -1;
        } else if ((bArr.length - i2) - 1 < 0) {
            return -1;
        } else {
            int i3 = i2 + 1;
            byte b2 = bArr[i2];
            if (b2 <= (byte) 0) {
                CaLogger.error(SensorHubErrors.ERROR_ENVIRONMENT_PACKAGE_SIZE.getMessage());
                return -1;
            }
            byte b3 = (byte) 0;
            while (b3 < b2) {
                if ((bArr.length - i3) - 1 < 0) {
                    return -1;
                }
                i2 = i3 + 1;
                String parserKey = getParserKey(bArr[i3]);
                if (parserKey != null && super.checkParserMap(parserKey)) {
                    ISensorHubParser parser = super.getParser(parserKey);
                    if (parser != null) {
                        ((EnvironmentSensorProvider) parser).setLoggingStatus(b);
                        i2 = parser.parse(bArr, i2);
                    }
                }
                b3++;
                i3 = i2;
            }
            return i3;
        }
    }

    protected final void setInterval(int i) {
        this.mInterval = i;
    }
}
