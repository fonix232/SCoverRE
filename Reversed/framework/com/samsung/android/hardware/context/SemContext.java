package com.samsung.android.hardware.context;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SemContext implements Parcelable {
    public static final Creator<SemContext> CREATOR = new C01241();
    static final int REPORTING_MODE_BATCH = 5;
    static final int REPORTING_MODE_CONTINUOUS = 1;
    static final int REPORTING_MODE_ONE_SHOT = 2;
    static final int REPORTING_MODE_ON_CHANGE = 3;
    static final int REPORTING_MODE_ON_CHANGE_AND_INITIAL_INFO = 4;
    public static final int TYPE_ABNORMAL_PRESSURE = 41;
    public static final int TYPE_ACTIVE_TIME_MONITOR = 34;
    public static final int TYPE_ACTIVITY_BATCH = 26;
    public static final int TYPE_ACTIVITY_CALIBRATION = 53;
    public static final int TYPE_ACTIVITY_LOCATION_LOGGING = 24;
    public static final int TYPE_ACTIVITY_NOTIFICATION = 27;
    public static final int TYPE_ACTIVITY_NOTIFICATION_EX = 30;
    public static final int TYPE_ACTIVITY_NOTIFICATION_FOR_LOCATION = 30;
    public static final int TYPE_ACTIVITY_TRACKER = 25;
    public static final int TYPE_AIRMOTION = 7;
    public static final int TYPE_ANY_MOTION_DETECTOR = 50;
    public static final int TYPE_APPROACH = 1;
    public static final int TYPE_AUTO_BRIGHTNESS = 39;
    public static final int TYPE_AUTO_ROTATION = 6;
    public static final int TYPE_BOUNCE_LONG_MOTION = 18;
    public static final int TYPE_BOUNCE_SHORT_MOTION = 17;
    public static final int TYPE_CALL_MOTION = 32;
    public static final int TYPE_CALL_POSE = 11;
    public static final int TYPE_CARRYING_DETECTION = 51;
    public static final int TYPE_DEVICE_POSITION = 22;
    public static final int TYPE_ENVIRONMENT_ADAPTIVE_DISPLAY = 44;
    public static final int TYPE_FLAT_MOTION = 20;
    public static final int TYPE_FLAT_MOTION_FOR_TABLE_MODE = 36;
    public static final int TYPE_FLIP_COVER_ACTION = 13;
    @Deprecated
    public static final int TYPE_FLIP_MOTION = 49;
    public static final int TYPE_FREE_FALL_DETECTION = 55;
    public static final int TYPE_GYRO_TEMPERATURE = 14;
    public static final int TYPE_HALL_SENSOR = 43;
    public static final int TYPE_INTERRUPTED_GYRO = 48;
    public static final int TYPE_LOCATION_CHANGE_TRIGGER = 54;
    public static final int TYPE_LOCATION_CORE = 47;
    public static final int TYPE_MOVEMENT = 5;
    @Deprecated
    public static final int TYPE_MOVEMENT_ALERT = 21;
    public static final int TYPE_PEDOMETER = 2;
    public static final int TYPE_PHONE_STATUS_MONITOR = 42;
    public static final int TYPE_PUT_DOWN_MOTION = 15;
    public static final int TYPE_SEDENTARY_TIMER = 35;
    public static final int TYPE_SENSOR_STATUS_CHECK = 52;
    public static final int TYPE_SERVICE_ALL = -1;
    public static final int TYPE_SHAKE_MOTION = 12;
    public static final int TYPE_SLOCATION_CORE = 47;
    public static final int TYPE_SPECIFIC_POSE_ALERT = 28;
    public static final int TYPE_STEP_COUNT_ALERT = 3;
    public static final int TYPE_STEP_LEVEL_MONITOR = 33;
    public static final int TYPE_WAKE_UP_VOICE = 16;
    public static final int TYPE_WIRELESS_CHARGING_DETECTION = 46;
    @Deprecated
    public static final int TYPE_WRIST_UP_MOTION = 19;
    private int mType;

    static class C01241 implements Creator<SemContext> {
        C01241() {
        }

        public SemContext createFromParcel(Parcel parcel) {
            return new SemContext(parcel);
        }

        public SemContext[] newArray(int i) {
            return new SemContext[i];
        }
    }

    SemContext() {
        this.mType = 0;
    }

    SemContext(Parcel parcel) {
        readFromParcel(parcel);
    }

    public static int getReportingMode(int i) {
        switch (i) {
            case 1:
            case 2:
            case 7:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 24:
            case 25:
            case 28:
            case 30:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 39:
            case 41:
            case 44:
            case 47:
            case 48:
            case 49:
            case 51:
            case 53:
            case 54:
            case 55:
                return 3;
            case 3:
            case 5:
            case 21:
            case 50:
                return 2;
            case 6:
            case 20:
            case 22:
            case 27:
            case 43:
            case 46:
            case 52:
                return 4;
            case 14:
            case 42:
                return 1;
            case 26:
                return 5;
            default:
                return 0;
        }
    }

    public static String getServiceName(int i) {
        switch (i) {
            case -1:
                return "Service All";
            case 1:
                return "Approach";
            case 2:
                return "Pedometer";
            case 3:
                return "Step Count Alert";
            case 5:
                return "Movement";
            case 6:
                return "Auto Rotation";
            case 7:
                return "Air Motion";
            case 11:
                return "Call Pose";
            case 12:
                return "Shake Motion";
            case 13:
                return "Flip Cover Action";
            case 14:
                return "Gyro Temperature";
            case 15:
                return "Put Down Motion";
            case 16:
                return "Wake Up Voice";
            case 17:
                return "Bounce Short Motion";
            case 18:
                return "Bounce Long Motion";
            case 19:
                return "Wrist Up Motion";
            case 20:
                return "Flat Motion";
            case 21:
                return "Movement Alert";
            case 22:
                return "Device Position";
            case 24:
                return "Activity Location Logging";
            case 25:
                return "Activity Tracker";
            case 26:
                return "Activity Batch";
            case 27:
                return "Activity Notification";
            case 28:
                return "Specific Pose Alert";
            case 30:
                return "Activity Notification Ex";
            case 32:
                return "Call Motion";
            case 33:
                return "Step Level Monitor";
            case 34:
                return "Active Time Monitor";
            case 35:
                return "Sedentary Timer";
            case 36:
                return "Flat Motion For Table Mode";
            case 39:
                return "Auto Brightness";
            case 41:
                return "Abnormal Pressure";
            case 42:
                return "Phone Status Monitor";
            case 43:
                return "Hall Sensor";
            case 44:
                return "Environment Adaptive Display";
            case 46:
                return "Wireless Charging Detection";
            case 47:
                return "SLocation Core";
            case 48:
                return "Interrupted Gyro";
            case 49:
                return "Flip Motion";
            case 50:
                return "Any Motion Detector";
            case 51:
                return "Carrying Detection";
            case 52:
                return "Sensor Status Check";
            case 53:
                return "Activity Calibration";
            case 54:
                return "Location Change Trigger";
            case 55:
                return "Free Fall Detection";
            default:
                return "";
        }
    }

    private void readFromParcel(Parcel parcel) {
        this.mType = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public int getType() {
        return this.mType;
    }

    void setType(int i) {
        this.mType = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
    }
}
