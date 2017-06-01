package com.sec.enterprise.knoxcustom;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.util.Log;

public class CustomDeviceManagerProxy {
    public static final int KEYBOARD_MODE_NORMAL = 0;
    public static final int KEYBOARD_MODE_PREDICTION_OFF = 1;
    public static final int KEYBOARD_MODE_SETTINGS_OFF = 2;
    public static final int NOTIFICATIONS_ALL = 31;
    public static final int NOTIFICATIONS_BATTERY_FULL = 2;
    public static final int NOTIFICATIONS_BATTERY_LOW = 1;
    public static final int NOTIFICATIONS_NITZ_SET_TIME = 16;
    public static final int NOTIFICATIONS_SAFE_VOLUME = 4;
    public static final int NOTIFICATIONS_STATUS_BAR = 8;
    public static final int SENSOR_ACCELEROMETER = 2;
    public static final int SENSOR_ALL = 127;
    public static final int SENSOR_GYROSCOPE = 1;
    public static final int SENSOR_LIGHT = 4;
    public static final int SENSOR_MAGNETIC = 32;
    public static final int SENSOR_ORIENTATION = 8;
    public static final int SENSOR_PRESSURE = 64;
    public static final int SENSOR_PROXIMITY = 16;
    private static final String TAG = "CustomDeviceManagerProxy";
    public static final int VOLUME_CONTROL_STREAM_DEFAULT = 0;
    public static final int VOLUME_CONTROL_STREAM_MUSIC = 3;
    public static final int VOLUME_CONTROL_STREAM_NOTIFICATION = 4;
    public static final int VOLUME_CONTROL_STREAM_RING = 2;
    public static final int VOLUME_CONTROL_STREAM_SYSTEM = 1;
    private static CustomDeviceManagerProxy mProxy;

    public static CustomDeviceManagerProxy getInstance() {
        if (mProxy == null) {
            mProxy = new CustomDeviceManagerProxy();
        }
        return mProxy;
    }

    public int getKeyboardMode() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getKeyboardMode();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getKeyboardMode returning default value");
        }
        return 0;
    }

    public int getProKioskHideNotificationMessages() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getProKioskHideNotificationMessages();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getProKioskHideNotificationMessages returning default value");
        }
        return 0;
    }

    public boolean getProKioskNotificationMessagesState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getProKioskNotificationMessagesState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getProKioskNotificationMessagesState returning default value");
        }
        return true;
    }

    public boolean getProKioskState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getProKioskState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getProKioskState returning default value");
        }
        return false;
    }

    public int getSensorDisabled() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getSensorDisabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getSensorDisabled returning default value");
        }
        return 0;
    }

    public boolean getToastEnabledState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getToastEnabledState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastEnabledState returning default value");
        }
        return true;
    }

    public int getToastGravity() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getToastGravity();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravity returning default value");
        }
        return 0;
    }

    public boolean getToastGravityEnabledState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getToastGravityEnabledState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravityEnabledState returning default value");
        }
        return false;
    }

    public int getToastGravityXOffset() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getToastGravityXOffset();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravityXOffset returning default value");
        }
        return 0;
    }

    public int getToastGravityYOffset() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getToastGravityYOffset();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravityYOffset returning default value");
        }
        return 0;
    }

    public boolean getToastShowPackageNameState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getToastShowPackageNameState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastShowPackageNameState returning default value");
        }
        return false;
    }

    public boolean getVolumeButtonRotationState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getVolumeButtonRotationState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getVolumeButtonRotationState returning default value");
        }
        return false;
    }

    public int getVolumeControlStream() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getVolumeControlStream();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getVolumeControlStream returning default value");
        }
        return 0;
    }

    public boolean getVolumePanelEnabledState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getVolumePanelEnabledState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getVolumePanelEnabledState returning default value");
        }
        return true;
    }

    public int getWifiAutoSwitchDelay() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getWifiAutoSwitchDelay();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiAutoSwitchDelay returning default value");
        }
        return 20;
    }

    public boolean getWifiAutoSwitchState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getWifiAutoSwitchState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiAutoSwitchState returning default value");
        }
        return false;
    }

    public int getWifiAutoSwitchThreshold() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getWifiAutoSwitchThreshold();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiAutoSwitchThreshold returning default value");
        }
        return -200;
    }

    public boolean getWifiState() {
        try {
            IEDMProxy service = EDMProxyServiceHelper.getService();
            if (service != null) {
                return service.getWifiState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiState() FAIL");
        }
        return false;
    }
}
