package com.samsung.android.knox.sdp.core;

public class SdpEngineConstants {

    public interface Flags {
        public static final int SDP_MDFPP = 0;
        public static final int SDP_MINOR = 1;
    }

    public interface Intent {
        public static final String ACTION_SDP_STATE_CHANGED = "com.samsung.android.knox.intent.action.SDP_STATE_CHANGED";
        public static final String EXTRA_SDP_ENGINE_ID = "com.samsung.android.knox.intent.extra.SDP_ENGINE_ID";
        public static final String EXTRA_SDP_ENGINE_STATE = "com.samsung.android.knox.intent.extra.SDP_ENGINE_STATE";
    }

    public interface State {
        public static final int LOCKED = 1;
        public static final int UNLOCKED = 2;
    }

    public interface Type {
        public static final int SDP_ENGINE_ANDROID_DEFAULT = 1;
        public static final int SDP_ENGINE_CUSTOM = 2;
        public static final int SDP_ENGINE_INVALID = -1;
    }

    private SdpEngineConstants() {
    }
}
