package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.ACTIVITY_TRACKER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.MODE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.PEDOMETER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.POSITIONING_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SLEEP_MONITOR_EXT_LIB_TYPE;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class ExtLibTypeParser extends TypeParser {
    ExtLibTypeParser() {
    }

    private int parseForActivityTracker(byte[] bArr, int i) {
        int i2 = i;
        for (Enum enumR : ACTIVITY_TRACKER_EXT_LIB_TYPE.values()) {
            if ((bArr.length - i) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (bArr[i] == enumR.value) {
                ISensorHubParser parser = super.getParser(enumR.toString());
                if (parser != null) {
                    if ((bArr.length - i) - 1 < 0) {
                        CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                        return -1;
                    }
                    i2 = i + 1;
                    if ((MODE.NORMAL_MODE.value & bArr[i]) != MODE.NORMAL_MODE.value) {
                        CaLogger.error(SensorHubErrors.ERROR_MODE_FAULT.getMessage());
                        return -1;
                    }
                    i2 = parser.parse(bArr, i2 + 1);
                    return i2;
                }
            }
        }
        return i2;
    }

    private int parseForPedometer(byte[] bArr, int i) {
        int i2 = i;
        for (Enum enumR : PEDOMETER_EXT_LIB_TYPE.values()) {
            if ((bArr.length - i) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (bArr[i] == enumR.value) {
                ISensorHubParser parser = super.getParser(enumR.toString());
                if (parser != null) {
                    i2 = parser.parse(bArr, i + 1);
                    break;
                }
            }
        }
        return i2;
    }

    private int parseForPositioning(byte[] bArr, int i) {
        int i2 = i;
        for (Enum enumR : POSITIONING_EXT_LIB_TYPE.values()) {
            if ((bArr.length - i) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (bArr[i] == enumR.value) {
                ISensorHubParser parser = super.getParser(enumR.toString());
                if (parser != null) {
                    i2 = parser.parse(bArr, i + 1);
                    break;
                }
            }
        }
        return i2;
    }

    private int parseForSleepMonitorTracker(byte[] bArr, int i) {
        int i2 = i;
        for (Enum enumR : SLEEP_MONITOR_EXT_LIB_TYPE.values()) {
            if ((bArr.length - i) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (bArr[i] == enumR.value) {
                ISensorHubParser parser = super.getParser(enumR.toString());
                if (parser != null) {
                    if ((bArr.length - i) - 1 < 0) {
                        CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                        return -1;
                    }
                    i2 = i + 1;
                    if ((MODE.NORMAL_MODE.value & bArr[i]) != MODE.NORMAL_MODE.value) {
                        CaLogger.error(SensorHubErrors.ERROR_MODE_FAULT.getMessage());
                        return -1;
                    }
                    i2 = parser.parse(bArr, i2 + 1);
                    return i2;
                }
            }
        }
        return i2;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        for (Enum enumR : DATA_TYPE.values()) {
            if ((bArr.length - i) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            }
            if (bArr[i] == enumR.value) {
                if (enumR.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT_FOR_POSITIONING.name())) {
                    i2 = parseForPositioning(bArr, i + 1);
                    break;
                } else if (enumR.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_PEDOMETER.name())) {
                    i2 = parseForPedometer(bArr, i + 1);
                    break;
                } else if (enumR.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.name())) {
                    i2 = parseForActivityTracker(bArr, i + 1);
                    break;
                } else if (enumR.toString().equals(DATA_TYPE.LIBRARY_DATATYPE_SLEEP_MONITOR.name())) {
                    i2 = parseForSleepMonitorTracker(bArr, i + 1);
                    break;
                }
            }
        }
        return i2;
    }
}
