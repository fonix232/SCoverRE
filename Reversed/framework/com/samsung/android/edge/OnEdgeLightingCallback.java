package com.samsung.android.edge;

public interface OnEdgeLightingCallback {
    public static final int CONDITION_ALWAYS = 7;
    public static final int CONDITION_SCREEN_OFF = 2;
    public static final int CONDITION_SCREEN_ON = 1;
    public static final int CONDITION_TURN_OVER = 4;
    public static final int REASON_APP_REQUEST = 0;
    public static final int REASON_NOTIFICATION = 1;
    public static final int REASON_TOAST = 2;
    public static final int REASON_TURN_OVER_NOTIFICATION = 7;
    public static final int REASON_TURN_OVER_RINGING = 8;
    public static final int REASON_WAKE_LOCK = 4;
    public static final int REASON_WAKE_LOCK_BY_WINDOW = 6;
    public static final int REASON_WAKE_UP = 3;
    public static final int REASON_WAKE_UP_BY_WINDOW = 5;

    void onScreenChanged(boolean z);

    void onStartEdgeLighting(String str, SemEdgeLightingInfo semEdgeLightingInfo, int i);

    void onStopEdgeLighting(String str, int i);
}
