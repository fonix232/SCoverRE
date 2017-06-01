package com.samsung.context.sdk.samsunganalytics;

public class Configuration {
    private int auidType;
    private String deviceId;
    private boolean enableAutoDeviceId;
    private boolean enableFastReady;
    private boolean enableUseInAppLogging;
    private boolean isAlwaysRunningApp;
    private String overrideIp;
    private int restrictedNetworkType;
    private String trackingId;
    private boolean useAnonymizeIp;
    private UserAgreement userAgreement;
    private String userId;
    private String version;

    public Configuration() {
        this.enableAutoDeviceId = false;
        this.enableUseInAppLogging = false;
        this.useAnonymizeIp = false;
        this.isAlwaysRunningApp = false;
        this.enableFastReady = false;
        this.auidType = -1;
        this.restrictedNetworkType = -1;
    }

    public Configuration disableAutoDeviceId() {
        this.enableAutoDeviceId = false;
        return this;
    }

    public Configuration enableAutoDeviceId() {
        this.enableAutoDeviceId = true;
        return this;
    }

    public Configuration enableFastReady(boolean z) {
        this.enableFastReady = z;
        return this;
    }

    public Configuration enableUseInAppLogging(UserAgreement userAgreement) {
        setUserAgreement(userAgreement);
        this.enableUseInAppLogging = true;
        return this;
    }

    public int getAuidType() {
        return this.auidType;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getOverrideIp() {
        return this.overrideIp;
    }

    public int getRestrictedNetworkType() {
        return this.restrictedNetworkType;
    }

    public String getTrackingId() {
        return this.trackingId;
    }

    public UserAgreement getUserAgreement() {
        return this.userAgreement;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isAlwaysRunningApp() {
        return this.isAlwaysRunningApp;
    }

    public boolean isEnableAutoDeviceId() {
        return this.enableAutoDeviceId;
    }

    public boolean isEnableFastReady() {
        return this.enableFastReady;
    }

    public boolean isEnableUseInAppLogging() {
        return this.enableUseInAppLogging;
    }

    public boolean isUseAnonymizeIp() {
        return this.useAnonymizeIp;
    }

    public Configuration setAlwaysRunningApp(boolean z) {
        this.isAlwaysRunningApp = z;
        return this;
    }

    public void setAuidType(int i) {
        this.auidType = i;
    }

    public Configuration setDeviceId(String str) {
        this.deviceId = str;
        return this;
    }

    public Configuration setOverrideIp(String str) {
        this.overrideIp = str;
        return this;
    }

    protected void setRestrictedNetworkType(int i) {
        this.restrictedNetworkType = i;
    }

    public Configuration setTrackingId(String str) {
        this.trackingId = str;
        return this;
    }

    public Configuration setUseAnonymizeIp(boolean z) {
        this.useAnonymizeIp = z;
        return this;
    }

    public Configuration setUserAgreement(UserAgreement userAgreement) {
        this.userAgreement = userAgreement;
        return this;
    }

    public Configuration setUserId(String str) {
        this.userId = str;
        return this;
    }

    public Configuration setVersion(String str) {
        this.version = str;
        return this;
    }
}
