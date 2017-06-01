package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.MultiModeContextList.MultiModeContextType;
import com.samsung.android.contextaware.utilbundle.IUtilManager;
import java.util.concurrent.ConcurrentHashMap;

public class SensorHubMultiModeParser implements IUtilManager {
    private static volatile SensorHubMultiModeParser instance;
    private final ConcurrentHashMap<String, ISensorHubParser> mParserMap = new ConcurrentHashMap();

    public static SensorHubMultiModeParser getInstance() {
        if (instance == null) {
            synchronized (SensorHubMultiModeParser.class) {
                if (instance == null) {
                    instance = new SensorHubMultiModeParser();
                }
            }
        }
        return instance;
    }

    public final boolean containsParser(String str) {
        return (this.mParserMap == null || this.mParserMap.isEmpty() || getParser(str) == null) ? false : true;
    }

    public final ISensorHubParser getParser(String str) {
        return (this.mParserMap == null || !this.mParserMap.containsKey(str)) ? null : (ISensorHubParser) this.mParserMap.get(str);
    }

    public final void initializeManager(Context context) {
        for (MultiModeContextType multiModeContextType : MultiModeContextType.values()) {
            ISensorHubParser parserHandler = multiModeContextType.getParserHandler();
            if (parserHandler != null) {
                registerParser(multiModeContextType.getCode(), parserHandler);
            }
        }
    }

    public final void registerParser(String str, ISensorHubParser iSensorHubParser) {
        if (this.mParserMap != null && !this.mParserMap.containsKey(str)) {
            this.mParserMap.put(str, iSensorHubParser);
        }
    }

    public final void terminateManager() {
        if (this.mParserMap != null) {
            this.mParserMap.clear();
        }
    }

    public final void unregisterParser(String str) {
        if (this.mParserMap != null && this.mParserMap.containsKey(str)) {
            this.mParserMap.remove(str);
        }
    }
}
