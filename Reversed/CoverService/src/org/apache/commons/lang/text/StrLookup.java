package org.apache.commons.lang.text;

import java.util.Map;

public abstract class StrLookup {
    private static final StrLookup NONE_LOOKUP = new MapStrLookup(null);
    private static final StrLookup SYSTEM_PROPERTIES_LOOKUP;

    static class MapStrLookup extends StrLookup {
        private final Map map;

        MapStrLookup(Map map) {
            this.map = map;
        }

        public String lookup(String key) {
            if (this.map == null) {
                return null;
            }
            Object obj = this.map.get(key);
            if (obj != null) {
                return obj.toString();
            }
            return null;
        }
    }

    public abstract String lookup(String str);

    static {
        StrLookup lookup;
        try {
            lookup = new MapStrLookup(System.getProperties());
        } catch (SecurityException e) {
            lookup = NONE_LOOKUP;
        }
        SYSTEM_PROPERTIES_LOOKUP = lookup;
    }

    public static StrLookup noneLookup() {
        return NONE_LOOKUP;
    }

    public static StrLookup systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    public static StrLookup mapLookup(Map map) {
        return new MapStrLookup(map);
    }

    protected StrLookup() {
    }
}
