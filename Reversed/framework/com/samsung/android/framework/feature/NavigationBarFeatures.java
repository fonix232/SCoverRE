package com.samsung.android.framework.feature;

public class NavigationBarFeatures {
    public static final String NAVIGATION_BAR_THEME = "SupportLightNavigationBar|SupportForceTouch|SupportCustomBgColor|SupportNaviBarRemoteView";
    public static final boolean SUPPORT_APP_SWITCHER = NAVIGATION_BAR_THEME.contains("SupportAppSwitcher");
    public static final boolean SUPPORT_CUSTOM_BG_COLOR = NAVIGATION_BAR_THEME.contains("SupportCustomBgColor");
    public static final boolean SUPPORT_CUSTOM_HEIGHT = NAVIGATION_BAR_THEME.contains("SupportCustomHeight");
    public static final boolean SUPPORT_IMMERSIVE_FORCE_TOUCH = NAVIGATION_BAR_THEME.contains("SupportForceTouch");
    public static final boolean SUPPORT_LIGHT_NAVIGATION_BAR = NAVIGATION_BAR_THEME.contains("SupportLightNavigationBar");
    public static final boolean SUPPORT_NAVIGATION_BAR = (!NAVIGATION_BAR_THEME.isEmpty());
    public static final boolean SUPPORT_NAVIGATION_BAR_REMOTEVIEW = NAVIGATION_BAR_THEME.contains("SupportNaviBarRemoteView");
}
