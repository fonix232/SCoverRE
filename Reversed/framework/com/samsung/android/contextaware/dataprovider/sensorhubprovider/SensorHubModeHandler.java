package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.MODE;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SensorHubModeHandler extends SensorHubParserBean implements ISensorHubParser {
    private boolean isDuplicatedMode(int i) {
        int i2 = 0;
        for (MODE mode : MODE.values()) {
            if ((MODE.BATCH_MODE.value & i) != MODE.BATCH_MODE.value) {
                i2 += (mode.value & i) == mode.value ? 1 : 0;
            }
        }
        return i2 > 1;
    }

    private int parse(byte[] bArr, int i, String str) {
        int i2 = i;
        if (!super.checkParserMap(str)) {
            return -1;
        }
        ISensorHubParser parser = super.getParser(str);
        return parser == null ? -1 : parser.parse(bArr, i);
    }

    private int parseDuplicatedMode(int i, byte[] bArr, int i2) {
        int i3 = i2;
        int i4 = i2;
        for (MODE mode : MODE.values()) {
            if ((MODE.BATCH_MODE.value & i) != MODE.BATCH_MODE.value && (mode.value & i) == mode.value) {
                String parserKey = getParserKey(mode.value);
                if (parserKey == null) {
                    CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
                    return -1;
                }
                i3 = parse(bArr, i2, parserKey);
            }
        }
        if (i3 != i2) {
            return i3;
        }
        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
        return -1;
    }

    protected abstract CopyOnWriteArrayList<String> getModeList();

    protected final String getParserKey(int i) {
        for (Enum enumR : MODE.values()) {
            if (i == enumR.value && super.checkParserMap(enumR.toString())) {
                return enumR.toString();
            }
        }
        return null;
    }

    protected final boolean isRunning() {
        Iterator it = getModeList().iterator();
        while (it.hasNext()) {
            if (ListenerListManager.getInstance().getUsedTotalCount((String) it.next()) > 0) {
                return true;
            }
        }
        return false;
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        if (!super.checkParserMap() || (bArr.length - i) - 1 < 0) {
            return -1;
        }
        i2 = i + 1;
        byte b = bArr[i];
        if (isDuplicatedMode(b)) {
            return parseDuplicatedMode(b, bArr, i2);
        }
        String str = null;
        for (MODE mode : MODE.values()) {
            if ((mode.value & b) == mode.value) {
                str = getParserKey(b);
                break;
            }
        }
        if (str != null) {
            return parse(bArr, i2, str);
        }
        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_NOT_REGISTERED_SERVICE.getCode()));
        return -1;
    }
}
