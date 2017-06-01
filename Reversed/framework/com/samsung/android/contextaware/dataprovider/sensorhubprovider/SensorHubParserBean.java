package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SensorHubParserBean {
    private final ConcurrentHashMap<String, ISensorHubParser> mParserMap = new ConcurrentHashMap();

    public final boolean checkParserMap() {
        return (this.mParserMap == null || this.mParserMap.isEmpty()) ? false : true;
    }

    protected final boolean checkParserMap(String str) {
        return (this.mParserMap == null || this.mParserMap.isEmpty() || getParser(str) == null) ? false : true;
    }

    public final ISensorHubParser getParser(String str) {
        return (this.mParserMap == null || !this.mParserMap.containsKey(str)) ? null : (ISensorHubParser) this.mParserMap.get(str);
    }

    public final void registerParser(String str, ISensorHubParser iSensorHubParser) {
        if (this.mParserMap != null && !this.mParserMap.containsKey(str)) {
            this.mParserMap.put(str, iSensorHubParser);
        }
    }

    public final void unregisterParser(String str) {
        if (this.mParserMap != null && this.mParserMap.containsKey(str)) {
            this.mParserMap.remove(str);
        }
    }
}
