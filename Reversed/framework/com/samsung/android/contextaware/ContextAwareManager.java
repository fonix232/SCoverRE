package com.samsung.android.contextaware;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.manager.ContextAwareListener;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.IContextAwareCallback;
import com.samsung.android.contextaware.manager.IContextAwareService;
import com.samsung.android.contextaware.manager.IContextAwareService.Stub;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger.Level;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextAwareManager {
    public static final int ABNORMAL_PRESSURE_MONITOR = ContextType.SENSORHUB_RUNNER_ABNORMAL_PRESSURE_MONITOR.ordinal();
    public static final int ABNORMAL_SHOCK_SERVICE = ContextType.SENSORHUB_RUNNER_ABNORMAL_SHOCK.ordinal();
    public static final int ACCELEROMETER_SENSOR_SERVICE = ContextType.ANDROID_RUNNER_ACCELEROMETER_SENSOR.ordinal();
    public static final int ACTIVE_TIME_SERVICE = ContextType.SENSORHUB_RUNNER_ACTIVE_TIME.ordinal();
    public static final int ACTIVITY_CALIBRATION_SERVICE = ContextType.SENSORHUB_RUNNER_ACTIVITY_CALIBRATION.ordinal();
    public static final int ACTIVITY_TRACKER_BATCH_SERVICE = ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.ordinal();
    public static final int ACTIVITY_TRACKER_EXTANDED_INTERRUPT_SERVICE = ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_EXTANDED_INTERRUPT.ordinal();
    public static final int ACTIVITY_TRACKER_INTERRUPT_SERVICE = ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.ordinal();
    public static final int ACTIVITY_TRACKER_SERVICE = ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER.ordinal();
    public static final int ANY_MOTION_DETECTOR_SERVICE = ContextType.SENSORHUB_RUNNER_ANY_MOTION_DETECTOR_RUNNER.ordinal();
    public static final int APDR_SERVICE = ContextType.SENSORHUB_RUNNER_APDR.ordinal();
    public static final int AUTO_BRIGHTNESS_SERVICE = ContextType.SENSORHUB_RUNNER_AUTO_BRIGHTNESS.ordinal();
    public static final int AUTO_ROTATION_SERVICE = ContextType.SENSORHUB_RUNNER_AUTO_ROTATION.ordinal();
    public static final int BEST_LOCATION_SERVICE = ContextType.ANDROID_RUNNER_BEST_LOCATION.ordinal();
    public static final int BOTTOM_FLAT_DETECTOR_SERVICE = ContextType.SENSORHUB_RUNNER_BOTTOM_FLAT_DETECTOR.ordinal();
    public static final int BOUNCE_LONG_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_BOUNCE_LONG_MOTION.ordinal();
    public static final int BOUNCE_SHORT_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_BOUNCE_SHORT_MOTION.ordinal();
    public static final int CALL_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_CALL_MOTION.ordinal();
    public static final int CALL_POSE_SERVICE = ContextType.SENSORHUB_RUNNER_CALL_POSE.ordinal();
    public static final int CAPTURE_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_CAPTURE_MOTION.ordinal();
    public static final int CARE_GIVER_SERVICE = ContextType.SENSORHUB_RUNNER_CARE_GIVER.ordinal();
    public static final int CARRYING_STATUS_MONITOR_SERVICE = ContextType.SENSORHUB_RUNNER_CARRYING_STATUS_MONITOR.ordinal();
    public static final int CH_LOC_TRIGGER_SERVICE = ContextType.SENSORHUB_RUNNER_CH_LOC_TRIGGER.ordinal();
    public static final int CMD_PROCESS_FAULT_DETECTION = ContextType.CMD_PROCESS_FAULT_DETECTION.ordinal();
    public static final int CURRENT_STATUS_FOR_POSITIONING_SERVICE = 0;
    public static final int DEFAULT_VERSION = 1;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_SERVICE = ContextType.SENSORHUB_RUNNER_DEVICE_PHYSICAL_CONTEXT_MONITOR.ordinal();
    public static final int DIRECT_CALL_SERVICE = ContextType.SENSORHUB_RUNNER_DIRECT_CALL.ordinal();
    public static final int DUAL_DISPLAY_ANGLE_SERVICE = ContextType.SENSORHUB_RUNNER_DUAL_DISPLAY_ANGLE.ordinal();
    public static final int EAD_SERVICE = ContextType.SENSORHUB_RUNNER_EAD.ordinal();
    public static final int EXERCISE_SERVICE = ContextType.SENSORHUB_RUNNER_EXERCISE.ordinal();
    public static final int FLAT_MOTION_FOR_TABLE_MODE_SERVICE = ContextType.SENSORHUB_RUNNER_FLAT_MOTION_FOR_TABLE_MODE.ordinal();
    public static final int FLAT_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_FLAT_MOTION.ordinal();
    public static final int FLIP_COVER_ACTION_SERVICE = ContextType.SENSORHUB_RUNNER_FLIP_COVER_ACTION.ordinal();
    public static final int FREE_FALL_DETECTION_SERVICE = ContextType.SENSORHUB_RUNNER_FREE_FALL_DETECTION.ordinal();
    public static final int GESTURE_APPROACH_SERVICE = ContextType.SENSORHUB_RUNNER_GESTURE_APPROACH.ordinal();
    public static final int GYROSCOPE_SENSOR_SERVICE = ContextType.ANDROID_RUNNER_GYROSCOPE_SENSOR.ordinal();
    public static final int GYRO_TEMPERATURE_SERVICE = ContextType.SENSORHUB_RUNNER_GYRO_TEMPERATURE.ordinal();
    public static final int HALL_SENSOR_SERVICE = ContextType.SENSORHUB_RUNNER_HALL_SENSOR.ordinal();
    public static final int LIFE_LOG_COMPONENT_SERVICE = ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.ordinal();
    public static final int LIFE_LOG_SERVICE = ContextType.AGGREGATOR_LIFE_LOG.ordinal();
    public static final int LOCATION_SERVICE = ContextType.AGGREGATOR_LOCATION.ordinal();
    public static final int LOG_LEVEL_DEBUG = Level.DEBUG.ordinal();
    public static final int LOG_LEVEL_ERROR = Level.ERROR.ordinal();
    public static final int LOG_LEVEL_INFO = Level.INFO.ordinal();
    public static final int LOG_LEVEL_TRACE = Level.TRACE.ordinal();
    public static final int LOG_LEVEL_WARN = Level.WARN.ordinal();
    public static final int LPP_SERVICE = ContextType.AGGREGATOR_LPP.ordinal();
    public static final int MAGNETIC_SENSOR_SERVICE = ContextType.ANDROID_RUNNER_MAGNETIC_SENSOR.ordinal();
    public static final int MAIN_SCREEN_DETECTION_SERVICE = ContextType.SENSORHUB_RUNNER_MAIN_SCREEN_DETECTION.ordinal();
    public static final int MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_MOTION.ordinal();
    public static final int MOVEMENT_ALERT_SERVICE = ContextType.SENSORHUB_RUNNER_MOVEMENT_ALERT.ordinal();
    public static final int MOVEMENT_FOR_POSITIONING_SERVICE = ContextType.SENSORHUB_RUNNER_MOVEMENT_FOR_POSITIONING.ordinal();
    public static final int MOVEMENT_SERVICE = ContextType.SENSORHUB_RUNNER_MOVEMENT.ordinal();
    public static final int MOVING_SERVICE = ContextType.AGGREGATOR_MOVING.ordinal();
    public static final int ORIENTATION_SENSOR_SERVICE = ContextType.ANDROID_RUNNER_ORIENTATION_SENSOR.ordinal();
    public static final int OTHER_VERSION = 2;
    public static final int PEDOMETER_SERVICE = ContextType.SENSORHUB_RUNNER_PEDOMETER.ordinal();
    public static final int PHONE_STATE_MONITOR_SERVICE = ContextType.SENSORHUB_RUNNER_PHONE_STATE_MONITOR.ordinal();
    public static final int POWER_NOTI_SERVICE = ContextType.SENSORHUB_RUNNER_POWER_NOTI.ordinal();
    public static final int PROPERTYGET_ACTIVITY_TRACKER_BATCH_HISTORY_DATA = 3;
    public static final int PROPERTYGET_ANY_MOTION_DETECTOR_DATA = 5;
    public static final int PROPERTYGET_CH_LOC_TRIGGER_CUR_STATUS = 9;
    public static final int PROPERTYGET_DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_STATUS = 7;
    public static final int PROPERTYGET_DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_VERSION = 8;
    public static final int PROPERTYGET_EXERCISE_DATA = 0;
    public static final int PROPERTYGET_PEDOMETER_HISTORY_DATA = 2;
    public static final int PROPERTYGET_PHONESTATE_DATA = 1;
    public static final int PROPERTYGET_STEP_LEVEL_MONITOR = 4;
    public static final int PROPERTY_ABNORMAL_SHOCK_DURATION = 45;
    public static final int PROPERTY_ABNORMAL_SHOCK_STRENGTH = 44;
    public static final int PROPERTY_ABNORMAL_SHOCK_USER_ACTION = 46;
    public static final int PROPERTY_ACTIVITY_CALIBRATION_CURRENT_STATE = 91;
    public static final int PROPERTY_ACTIVITY_CALIBRATION_SPEED = 92;
    public static final int PROPERTY_ACTIVITY_TRACKER_EXTANDED_INTTERUPT_MODE_ACTIVITY = 38;
    public static final int PROPERTY_ACTIVITY_TRACKER_EXTANDED_INTTERUPT_MODE_PERIOD = 39;
    public static final int PROPERTY_ACTIVITY_TRACKER_INTTERUPT_MODE_ACTIVITY = 37;
    public static final int PROPERTY_AUTOROTATION_DEVICETYPE = 8;
    public static final int PROPERTY_AUTO_BRIGHTNESS_DEVICE_MODE = 64;
    public static final int PROPERTY_AUTO_BRIGHTNESS_EBOOK_CONFIG_DATA = 65;
    public static final int PROPERTY_CARE_GIVER_DURATION = 41;
    public static final int PROPERTY_CARE_GIVER_STRENGTH = 40;
    public static final int PROPERTY_CH_LOC_TRIGGER_DURATION = 90;
    public static final int PROPERTY_CH_LOC_TRIGGER_TRIGGER_TYPE = 89;
    public static final int PROPERTY_DPCM_AOD_ON_OFF = 80;
    public static final int PROPERTY_DPCM_SENSOR_AOD_OVER_TURN_DURATION = 84;
    public static final int PROPERTY_DPCM_SENSOR_AOD_PROXI_CHECK_DURATION = 86;
    public static final int PROPERTY_DPCM_SENSOR_AOD_PROXI_USE_PERIOD = 85;
    public static final int PROPERTY_DPCM_SENSOR_AOD_SCENARIO_OFF = 88;
    public static final int PROPERTY_DPCM_SENSOR_AOD_SCENARIO_ON = 87;
    public static final int PROPERTY_DPCM_SENSOR_AOD_TIMEOUT = 83;
    public static final int PROPERTY_DPCM_SENSOR_ON_OFF_BRIGHTNESS = 82;
    public static final int PROPERTY_DPCM_SENSOR_ON_OFF_PROXIMITY = 81;
    public static final int PROPERTY_DUAL_DISPLAY_ANGLE_OFF_ANGLE = 77;
    public static final int PROPERTY_DUAL_DISPLAY_ANGLE_ON_ANGLE = 76;
    public static final int PROPERTY_EAD_DURATION = 67;
    public static final int PROPERTY_EAD_MODE = 66;
    public static final int PROPERTY_ENVIRONMENT_SENSOR_INTERVAL = 13;
    public static final int PROPERTY_EXERCISE_DATA_TYPE = 63;
    public static final int PROPERTY_FLAT_MOTION_FOR_TABLE_MODE = 61;
    public static final int PROPERTY_LIFE_LOG_COMPONENT_STAYING_AREA_RADIUS = 31;
    public static final int PROPERTY_LIFE_LOG_COMPONENT_STAYING_RADIUS = 30;
    public static final int PROPERTY_LIFE_LOG_COMPONENT_STOP_PERIOD = 28;
    public static final int PROPERTY_LIFE_LOG_COMPONENT_WAIT_PERIOD = 29;
    public static final int PROPERTY_LIFE_LOG_LPP_RESOLUTION = 32;
    public static final int PROPERTY_LIFE_LOG_STAYING_AREA_RADIUS = 31;
    public static final int PROPERTY_LIFE_LOG_STAYING_RADIUS = 30;
    public static final int PROPERTY_LIFE_LOG_STAYING_STOP_PERIOD = 28;
    public static final int PROPERTY_LIFE_LOG_STAYING_WAIT_PERIOD = 29;
    public static final int PROPERTY_LOCATION_ACCURACY = 1;
    public static final int PROPERTY_LOCATION_HEIGHT = 3;
    public static final int PROPERTY_LOCATION_WEIGHT = 2;
    public static final int PROPERTY_LPP_RESOLUTION = 32;
    public static final int PROPERTY_MOVEMENT_FOR_POSITIONING_MOVE_DISTANCE = 12;
    public static final int PROPERTY_MOVEMENT_FOR_POSITIONING_MOVE_DURATION = 10;
    public static final int PROPERTY_MOVEMENT_FOR_POSITIONING_MOVE_MIN_DURATION = 11;
    public static final int PROPERTY_MOVEMENT_FOR_POSITIONING_NOMOVE_DURATION = 9;
    public static final int PROPERTY_PEDOMETER_CURRENT_INFO_COLLECTION_TIME = 19;
    public static final int PROPERTY_PEDOMETER_DELIVERY_COUNT = 17;
    public static final int PROPERTY_PEDOMETER_EXERCISE_MODE = 62;
    public static final int PROPERTY_PEDOMETER_GENDER = 6;
    public static final int PROPERTY_PEDOMETER_HEIGHT = 5;
    public static final int PROPERTY_PEDOMETER_WEIGHT = 4;
    public static final int PROPERTY_POI_CREATION_DB_SETTING = 27;
    public static final int PROPERTY_SENSORHUB_TIMER_COUNT = 26;
    public static final int PROPERTY_SHAKE_MOTION_DURATION = 15;
    public static final int PROPERTY_SHAKE_MOTION_STRENGTH = 14;
    public static final int PROPERTY_SLEEP_MONITOR_SAMPLING_INTERVAL = 43;
    public static final int PROPERTY_SLEEP_MONITOR_SENSIBILITY = 42;
    public static final int PROPERTY_SLOCATION_CURLOC_CORE_FUNC = 108;
    public static final int PROPERTY_SLOCATION_FLP_BATCHING_CORE_CLEANUP = 116;
    public static final int PROPERTY_SLOCATION_FLP_BATCHING_CORE_FLUSH = 114;
    public static final int PROPERTY_SLOCATION_FLP_BATCHING_CORE_INJECT_LOCATION = 115;
    public static final int PROPERTY_SLOCATION_FLP_BATCHING_CORE_REQUEST_LOCATION = 113;
    public static final int PROPERTY_SLOCATION_FLP_BATCHING_CORE_START = 110;
    public static final int PROPERTY_SLOCATION_FLP_BATCHING_CORE_STOP = 112;
    public static final int PROPERTY_SLOCATION_FLP_BATCHING_CORE_UPDATE = 111;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_ADD = 101;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_PAUSE_RESUME = 103;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_REMOVE = 102;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_SET_LOC_DATA = 105;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_START_AR = 106;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_STOP_AR = 107;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_UPDATE = 104;
    public static final int PROPERTY_SLOCATION_GEOFENCE_CORE_VERSION = 100;
    public static final int PROPERTY_SLOCATION_STATUS_REMOVE = 109;
    public static final int PROPERTY_SPECIFIC_POSE_ALERT_MAXIMUM_ANGLE = 35;
    public static final int PROPERTY_SPECIFIC_POSE_ALERT_MINUMUM_ANGLE = 34;
    public static final int PROPERTY_SPECIFIC_POSE_ALERT_MOVING_THRS = 36;
    public static final int PROPERTY_SPECIFIC_POSE_ALERT_RETENTION_TIME = 33;
    public static final int PROPERTY_STAYING_ALERT_STOP_PERIOD = 23;
    public static final int PROPERTY_STAYING_ALERT_WAIT_PERIOD = 24;
    public static final int PROPERTY_STEPCOUNTALERT_STEPCOUNT = 7;
    public static final int PROPERTY_STEP_COUNT_TIMER_STEPCOUNT = 25;
    public static final int PROPERTY_STEP_LEVEL_MONITOR_DEVICE_TYPE = 57;
    public static final int PROPERTY_STEP_LEVEL_MONITOR_NOTIFICATION_COUNT = 60;
    public static final int PROPERTY_STEP_LEVEL_MONITOR_NOTIFICATION_END_TIME = 56;
    public static final int PROPERTY_STEP_LEVEL_MONITOR_NOTIFICATION_START_TIME = 55;
    public static final int PROPERTY_STEP_LEVEL_MONITOR_STEP_LEVEL_DURATION = 59;
    public static final int PROPERTY_STEP_LEVEL_MONITOR_STEP_LEVEL_TYPE = 58;
    public static final int PROPERTY_TEMPERATURE_ALERT_HIGH_TEMPERATURE = 21;
    public static final int PROPERTY_TEMPERATURE_ALERT_IS_INCLUDING = 22;
    public static final int PROPERTY_TEMPERATURE_ALERT_LOW_TEMPERATURE = 20;
    public static final int PROPERTY_TEST_AGGREATION_SHAKE_MOTION_DURATION = 15;
    public static final int PROPERTY_TEST_AGGREATION_SHAKE_MOTION_STRENGTH = 14;
    public static final int PROPERTY_WAKE_UP_VOICE_MODE = 53;
    public static final int PROPERTY_WAKE_UP_VOICE_SOUND_SOURCE_AM = 16;
    public static final int PROPERTY_WAKE_UP_VOICE_SOUND_SOURCE_GRAMMER = 18;
    public static final int PUT_DOWN_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_PUT_DOWN_MOTION.ordinal();
    public static final int RAW_BAROMETER_SENSOR_SERVICE = ContextType.SENSORHUB_RUNNER_RAW_BAROMETER_SENSOR.ordinal();
    public static final int RAW_GPS_SERVICE = ContextType.ANDROID_RUNNER_RAW_GPS.ordinal();
    public static final int RAW_SATELLITE_SERVICE = ContextType.ANDROID_RUNNER_RAW_SATELLITE.ordinal();
    public static final int RAW_TEMPERATURE_HUMIDITY_SENSOR_SERVICE = ContextType.SENSORHUB_RUNNER_RAW_TEMPERATURE_HUMIDITY_SENSOR.ordinal();
    public static final int RAW_WPS_SERVICE = ContextType.ANDROID_RUNNER_RAW_WPS.ordinal();
    public static final int REQUEST_ACTIVITY_TRACKER_BATCH_CURRENT_INFO = ContextType.REQUEST_SENSORHUB_ACTIVITY_TRACKER_BATCH_CURRENT_INFO.ordinal();
    public static final int REQUEST_ACTIVITY_TRACKER_CURRENT_INFO = ContextType.REQUEST_SENSORHUB_ACTIVITY_TRACKER_CURRENT_INFO.ordinal();
    public static final int REQUEST_MOVEMENT_FOR_POSITIONING_CURRENT_STATUS = ContextType.REQUEST_SENSORHUB_MOVEMENT_FOR_POSITIONING_CURRENT_STATUS.ordinal();
    public static final int REQUEST_PEDOMETER_CURRENT_INFO = ContextType.REQUEST_SENSORHUB_PEDOMETER_CURRENT_INFO.ordinal();
    public static final int REQUEST_SLEEP_MONITOR_CURRENT_INFO = ContextType.REQUEST_SENSORHUB_SLEEP_MONITOR_CURRENT_INFO.ordinal();
    public static final String SENSORHUB_RESET_ACTION = "com.samsung.android.contextaware.SENSORHUB_RESET";
    public static final int SENSOR_STATUS_CHECK_SERVICE = ContextType.SENSORHUB_RUNNER_SENSOR_STATUS_CHECK.ordinal();
    public static final int SHAKE_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_SHAKE_MOTION.ordinal();
    public static final int SLEEP_MONITOR_SERVICE = ContextType.SENSORHUB_RUNNER_SLEEP_MONITOR.ordinal();
    public static final int SLOCATION_SERVICE = ContextType.SENSORHUB_RUNNER_SLOCATION.ordinal();
    public static final int SPECIFIC_POSE_ALERT_SERVICE = ContextType.SENSORHUB_RUNNER_SPECIFIC_POSE_ALERT.ordinal();
    public static final int STAYING_ALERT_SERVICE = ContextType.SENSORHUB_RUNNER_STAYING_ALERT.ordinal();
    public static final int STEP_COUNT_ALERT_SERVICE = ContextType.SENSORHUB_RUNNER_STEP_COUNT_ALERT.ordinal();
    public static final int STEP_LEVEL_MONITOR_EXTENDED_INTERRUPT_SERVICE = ContextType.SENSORHUB_RUNNER_SL_MONITOR_EXTENDED_INTERRUPT.ordinal();
    public static final int STEP_LEVEL_MONITOR_SERVICE = ContextType.SENSORHUB_RUNNER_SL_MONITOR.ordinal();
    public static final int STOP_ALERT_SERVICE = ContextType.SENSORHUB_RUNNER_STOP_ALERT.ordinal();
    private static final String TAG = "CAE";
    public static final int TEMPERATURE_ALERT_SERVICE = ContextType.SENSORHUB_RUNNER_TEMPERATURE_ALERT.ordinal();
    public static final int TEMPERATURE_HUMIDITY_SERVICE = ContextType.AGGREGATOR_TEMPERATURE_HUMIDITY.ordinal();
    public static final int TEST_FLAT_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_TEST_FLAT_MOTION.ordinal();
    public static final int WAKE_UP_VOICE_SERVICE = ContextType.SENSORHUB_RUNNER_WAKE_UP_VOICE.ordinal();
    public static final int WIRELESS_CHARGING_MONITOR = ContextType.SENSORHUB_RUNNER_WIRELESS_CHARGING_MONITOR.ordinal();
    public static final int WRIST_UP_MOTION_SERVICE = ContextType.SENSORHUB_RUNNER_WRIST_UP_MOTION.ordinal();
    private HandlerThread handlerThread = null;
    private final ContextAwareListener mCaListenerForTest = new C10381();
    private final Looper mCaeMgrLooper;
    private final IContextAwareService mContextAwareService = Stub.asInterface(ServiceManager.getService("context_aware"));
    private final CopyOnWriteArrayList<CaListenerDelegate> mListenerDelegates = new CopyOnWriteArrayList();
    private final Looper mMainLooper;

    class C10381 implements ContextAwareListener {
        C10381() {
        }

        public void onContextChanged(int i, Bundle bundle) {
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class CaListenerDelegate extends IContextAwareCallback.Stub {
        private final Handler mHandler;
        private final ContextAwareListener mListener;

        CaListenerDelegate(ContextAwareListener contextAwareListener, Handler handler) {
            this.mListener = contextAwareListener;
            this.mHandler = new Handler(ContextAwareManager.this.mCaeMgrLooper == null ? ContextAwareManager.this.mMainLooper : ContextAwareManager.this.mCaeMgrLooper) {
                public void handleMessage(Message message) {
                    if (CaListenerDelegate.this.mListener != null) {
                        CaListenerDelegate.this.mListener.onContextChanged(message.what, (Bundle) message.obj);
                    }
                }
            };
        }

        public synchronized void caCallback(int i, Bundle bundle) throws RemoteException {
            if (bundle == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_CONTEXT_NULL_EXCEPTION.getMessage());
                return;
            }
            Message obtain = Message.obtain();
            obtain.what = i;
            obtain.obj = bundle.clone();
            this.mHandler.sendMessage(obtain);
            notifyAll();
        }

        public ContextAwareListener getListener() {
            return this.mListener;
        }

        public String getListenerInfo() throws RemoteException {
            return this.mListener == null ? null : this.mListener.toString();
        }
    }

    public ContextAwareManager(Looper looper) {
        this.mMainLooper = looper;
        this.handlerThread = new HandlerThread("CAEMgr");
        this.handlerThread.start();
        this.mCaeMgrLooper = this.handlerThread.getLooper();
        if (this.mCaeMgrLooper == null) {
            this.handlerThread.quitSafely();
            this.handlerThread = null;
            CaLogger.error(ContextAwareServiceErrors.ERROR_LOOPER_NULL_EXCEPTION.getMessage());
        }
    }

    private CaListenerDelegate getListnerDelegate(ContextAwareListener contextAwareListener) {
        if (contextAwareListener == null) {
            return null;
        }
        CaListenerDelegate caListenerDelegate = null;
        Iterator it = this.mListenerDelegates.iterator();
        while (it.hasNext()) {
            CaListenerDelegate caListenerDelegate2 = (CaListenerDelegate) it.next();
            if (caListenerDelegate2.getListener().equals(contextAwareListener)) {
                caListenerDelegate = caListenerDelegate2;
                break;
            }
        }
        return caListenerDelegate;
    }

    private boolean setCAProperty(int i, int i2, ContextAwarePropertyBundle contextAwarePropertyBundle) {
        boolean z = false;
        try {
            z = this.mContextAwareService.setCAProperty(i, i2, contextAwarePropertyBundle);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return z;
    }

    protected void finalize() {
        if (this.handlerThread != null) {
            this.handlerThread.quit();
        }
        this.handlerThread = null;
    }

    public final void getContextInfo(ContextAwareListener contextAwareListener, int i) {
        IBinder listnerDelegate = getListnerDelegate(contextAwareListener);
        if (listnerDelegate == null) {
            listnerDelegate = new CaListenerDelegate(contextAwareListener, null);
        }
        try {
            this.mContextAwareService.getContextInfo(listnerDelegate, i);
        } catch (Throwable e) {
            Log.m32e(TAG, "RemoteException in getContextInfo: ", e);
        }
    }

    public final int getVersion() {
        int i = 0;
        try {
            i = this.mContextAwareService.getVersion();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return i;
    }

    public final void initializeAutoTest() {
        try {
            this.mContextAwareService.initializeAutoTest();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public final void registerListener(ContextAwareListener contextAwareListener, int i) {
        IBinder listnerDelegate = getListnerDelegate(contextAwareListener);
        if (listnerDelegate == null) {
            listnerDelegate = new CaListenerDelegate(contextAwareListener, null);
            this.mListenerDelegates.add(listnerDelegate);
        }
        try {
            this.mContextAwareService.registerCallback(listnerDelegate, i);
        } catch (Throwable e) {
            Log.m32e(TAG, "RemoteException in registerListener: ", e);
        }
    }

    public final void registerWatcher(ContextAwareListener contextAwareListener, int i) {
        IBinder listnerDelegate = getListnerDelegate(contextAwareListener);
        if (listnerDelegate == null) {
            listnerDelegate = new CaListenerDelegate(contextAwareListener, null);
            this.mListenerDelegates.add(listnerDelegate);
        }
        try {
            this.mContextAwareService.registerWatcher(listnerDelegate, i);
        } catch (Throwable e) {
            Log.m32e(TAG, "RemoteException in registerWatcher: ", e);
        }
    }

    public final void resetCAService(int i) {
        try {
            this.mContextAwareService.resetCAService(i);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public final void setCALogger(boolean z, boolean z2, int i, boolean z3) {
        try {
            this.mContextAwareService.setCALogger(z, z2, i, z3);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public final boolean setCAProperty(int i, int i2, double d) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getDoubleTypeCode(), Double.valueOf(d));
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, float f) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getFloatTypeCode(), Float.valueOf(f));
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, int i3) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerTypeCode(), Integer.valueOf(i3));
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, long j) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getLongTypeCode(), Long.valueOf(j));
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, String str) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getStringTypeCode(), str);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, ArrayList<Integer> arrayList) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerArrayListTypeCode(), arrayList);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, HashSet<Integer> hashSet) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerHashSetTypeCode(), hashSet);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, boolean z) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getBooleanTypeCode(), Boolean.valueOf(z));
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, byte[] bArr) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getStringTypeCode(), CaConvertUtil.byteArrToString(bArr));
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, char[] cArr) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getCharArrayTypeCode(), cArr);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, double[] dArr) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getDoubleArrayTypeCode(), dArr);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, float[] fArr) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getFloatArrayTypeCode(), fArr);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, int[] iArr) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getIntegerArrayTypeCode(), iArr);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, long[] jArr) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getLongArrayTypeCode(), jArr);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setCAProperty(int i, int i2, String[] strArr) {
        ContextAwarePropertyBundle contextAwarePropertyBundle = new ContextAwarePropertyBundle();
        contextAwarePropertyBundle.setValue(contextAwarePropertyBundle.getStringArrayTypeCode(), strArr);
        return setCAProperty(i, i2, contextAwarePropertyBundle);
    }

    public final boolean setScenarioForDebugging(int i, int i2, List<Integer> list, List<byte[]> list2) {
        boolean z = true;
        if (list != null) {
            for (Integer intValue : list) {
                registerListener(this.mCaListenerForTest, intValue.intValue());
            }
        }
        int i3 = 0;
        while (i3 < list2.size()) {
            try {
                z = this.mContextAwareService.setScenarioForDebugging(i, i2, (byte[]) list2.get(i3));
                if (z) {
                    i3++;
                } else {
                    Log.m31e(TAG, "setScenarioForDebugging error");
                    return false;
                }
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
        return z;
    }

    public final boolean setScenarioForTest(int i, int i2) {
        boolean z = false;
        try {
            z = this.mContextAwareService.setScenarioForTest(i, i2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return z;
    }

    public final void setVersion(int i) {
        try {
            this.mContextAwareService.setVersion(i);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public final void startAutoTest() {
        try {
            this.mContextAwareService.startAutoTest();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public final void stopAutoTest() {
        try {
            this.mContextAwareService.stopAutoTest();
            unregisterListener(this.mCaListenerForTest);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public final void unregisterListener(ContextAwareListener contextAwareListener) {
        for (Enum ordinal : ContextType.values()) {
            unregisterListener(contextAwareListener, ordinal.ordinal());
        }
    }

    public final void unregisterListener(ContextAwareListener contextAwareListener, int i) {
        CaListenerDelegate listnerDelegate = getListnerDelegate(contextAwareListener);
        if (listnerDelegate != null) {
            try {
                if (this.mContextAwareService.unregisterCallback(listnerDelegate, i)) {
                    this.mListenerDelegates.remove(listnerDelegate);
                }
            } catch (Throwable e) {
                Log.m32e(TAG, "RemoteException in unregisterListener: ", e);
            }
        }
    }

    public final void unregisterWatcher(ContextAwareListener contextAwareListener, int i) {
        CaListenerDelegate listnerDelegate = getListnerDelegate(contextAwareListener);
        if (listnerDelegate != null) {
            try {
                if (this.mContextAwareService.unregisterWatcher(listnerDelegate, i)) {
                    this.mListenerDelegates.remove(listnerDelegate);
                }
            } catch (Throwable e) {
                Log.m32e(TAG, "RemoteException in unregisterWatcher: ", e);
            }
        }
    }
}
