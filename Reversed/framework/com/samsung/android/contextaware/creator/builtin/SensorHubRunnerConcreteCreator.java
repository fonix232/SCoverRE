package com.samsung.android.contextaware.creator.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.MultiModeContextList;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.IListObjectCreator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubMultiModeParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserBean;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.ACTIVITY_TRACKER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.MODE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.PEDOMETER_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.POSITIONING_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SLEEP_MONITOR_EXT_LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.SUB_DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AbnormalPressureMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AbnormalShockRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActiveTimeRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityCalibrationRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerBatchCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerBatchRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerExtandedInterruptRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerInterruptRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ActivityTrackerRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AnyMotionDetectorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AutoBrightnessRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.AutoRotationRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.BottomFlatDetectorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.BounceLongMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.BounceShortMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CallMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CallPoseRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CaptureMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CareGiverRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CarryingStatusMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ChLocTriggerRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.CurrentStatusForMovementPositioningRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.DevicePhysicalContextMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.DirectCallRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.DualDisplayAngleRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.EADRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ExerciseRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.FlatMotionForTableModeRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.FlatMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.FlipCoverActionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.FreeFallDetection;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.GestureApproachRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.GyroTemperatureRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.HallSensorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.LifeLogComponentRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MainScreenDetectionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MovementAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MovementForPositioningRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.MovementRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PedometerCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PedometerOtherVerRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PedometerRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PowerNotiRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.PutDownMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SLMonitorExtendedInterruptRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SLMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SensorStatusCheckRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ShakeMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SleepMonitorCurrentInfoRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SleepMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.SpecificPoseAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.StayingAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.StepCountAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.StopAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.TemperatureAlertRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.TestFlatMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.WakeUpVoiceRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.WirelessChargingMonitorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.WristUpMotionRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.rpc.SLocationRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorHandler;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin.BarometerSensorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin.TemperatureHumiditySensorRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.internal.PhoneStateMonitorRunner;
import com.samsung.android.contextaware.manager.BatchContextProvider;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ExtandedInterruptContextProvider;
import com.samsung.android.contextaware.manager.InterruptContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class SensorHubRunnerConcreteCreator extends ContextProviderCreator {

    private enum SensorHubRunnerList implements IListObjectCreator {
        APDR(ContextType.SENSORHUB_RUNNER_APDR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_APDR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ApdrRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        PEDOMETER(ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_PEDOMETER.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), ContextProviderCreator.getVersion() == 2 ? new PedometerOtherVerRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()) : new PedometerRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        GESTURE_APPROACH(ContextType.SENSORHUB_RUNNER_GESTURE_APPROACH.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_GESTURE_APPROACH.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GestureApproachRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        STEP_COUNT_ALERT(ContextType.SENSORHUB_RUNNER_STEP_COUNT_ALERT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_COUNT_ALERT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new StepCountAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        MOTION(ContextType.SENSORHUB_RUNNER_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        MOVEMENT(ContextType.SENSORHUB_RUNNER_MOVEMENT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovementRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        AUTO_ROTATION(ContextType.SENSORHUB_RUNNER_AUTO_ROTATION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_AUTO_ROTATION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AutoRotationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        POWER_NOTI(ContextType.SENSORHUB_RUNNER_POWER_NOTI.getCode()) {
            protected String getKey() {
                return null;
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PowerNotiRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        MOVEMENT_FOR_POSITIONING(ContextType.SENSORHUB_RUNNER_MOVEMENT_FOR_POSITIONING.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT_FOR_POSITIONING.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovementForPositioningRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        CURRENT_STATUS_FOR_MOVEMENT_POSITIONING(ContextType.REQUEST_SENSORHUB_MOVEMENT_FOR_POSITIONING_CURRENT_STATUS.getCode()) {
            protected String getKey() {
                return POSITIONING_EXT_LIB_TYPE.POSITIONING_CURRENT_STATUS.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CurrentStatusForMovementPositioningRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        DIRECT_CALL(ContextType.SENSORHUB_RUNNER_DIRECT_CALL.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_DIRECT_CALL.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new DirectCallRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        STOP_ALERT(ContextType.SENSORHUB_RUNNER_STOP_ALERT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STOP_ALERT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new StopAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        RAW_TEMPERATURE_HUMIDITY_SENSOR(ContextType.SENSORHUB_RUNNER_RAW_TEMPERATURE_HUMIDITY_SENSOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TemperatureHumiditySensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return SUB_DATA_TYPE.ENVIRONMENT_SENSORTYPE_TEMPERATURE_HUMIDITY.toString();
            }
        },
        RAW_BAROMETER_SENSOR(ContextType.SENSORHUB_RUNNER_RAW_BAROMETER_SENSOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BarometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return SUB_DATA_TYPE.ENVIRONMENT_SENSORTYPE_BAROMETER.toString();
            }
        },
        CALL_POSE(ContextType.SENSORHUB_RUNNER_CALL_POSE.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CALL_POSE.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CallPoseRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        SHAKE_MOTION(ContextType.SENSORHUB_RUNNER_SHAKE_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SHAKE_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ShakeMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        CARE_GIVER(ContextType.SENSORHUB_RUNNER_CARE_GIVER.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CARE_GIVER.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CareGiverRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        ABNORMAL_SHOCK(ContextType.SENSORHUB_RUNNER_ABNORMAL_SHOCK.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ABNORMAL_SHOCK.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AbnormalShockRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        FLIP_COVER_ACTION(ContextType.SENSORHUB_RUNNER_FLIP_COVER_ACTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_FLIP_COVER_ACTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new FlipCoverActionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        GYRO_TEMPERATURE(ContextType.SENSORHUB_RUNNER_GYRO_TEMPERATURE.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_GYRO_TEMPERATURE.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GyroTemperatureRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        PUT_DOWN_MOTION(ContextType.SENSORHUB_RUNNER_PUT_DOWN_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_PUT_DOWN_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PutDownMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        WAKE_UP_VOICE(ContextType.SENSORHUB_RUNNER_WAKE_UP_VOICE.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_WAKE_UP_VOICE.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WakeUpVoiceRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        BOUNCE_SHORT_MOTION(ContextType.SENSORHUB_RUNNER_BOUNCE_SHORT_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_BOUNCE_SHORT_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BounceShortMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        BOUNCE_LONG_MOTION(ContextType.SENSORHUB_RUNNER_BOUNCE_LONG_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_BOUNCE_LONG_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BounceLongMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        WRIST_UP_MOTION(ContextType.SENSORHUB_RUNNER_WRIST_UP_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_WRIST_UP_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WristUpMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        FLAT_MOTION(ContextType.SENSORHUB_RUNNER_FLAT_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_FLAT_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new FlatMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        MOVEMENT_ALERT(ContextType.SENSORHUB_RUNNER_MOVEMENT_ALERT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MOVEMENT_ALERT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovementAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        TEST_FLAT_MOTION(ContextType.SENSORHUB_RUNNER_TEST_FLAT_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_TEST_FLAT_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TestFlatMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        TEMPERATURE_ALERT(ContextType.SENSORHUB_RUNNER_TEMPERATURE_ALERT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_TEMPERATURE_ALERT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TemperatureAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        PEDOMETER_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_PEDOMETER_CURRENT_INFO.getCode()) {
            protected String getKey() {
                return PEDOMETER_EXT_LIB_TYPE.PEDOMETER_CURRENT_INFO.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PedometerCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        STAYING_ALERT(ContextType.SENSORHUB_RUNNER_STAYING_ALERT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STAYING_ALERT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new StayingAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        LIFE_LOG_COMPONENT(ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_LIFE_LOG_COMPONENT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LifeLogComponentRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                setOptionForRequestLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        ACTIVITY_TRACKER(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActivityTrackerRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.NORMAL_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_INTERRUPT(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new InterruptContextProvider(new ActivityTrackerInterruptRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.INTERRUPT_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_BATCH(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BatchContextProvider(new ActivityTrackerBatchRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.BATCH_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_EXTANDED_INTERRUPT(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_EXTANDED_INTERRUPT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_TRACKER.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ExtandedInterruptContextProvider(new ActivityTrackerExtandedInterruptRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.EXTANDED_INTERRUPT_MODE.toString();
            }
        },
        ACTIVITY_TRACKER_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_ACTIVITY_TRACKER_CURRENT_INFO.getCode()) {
            protected String getKey() {
                return ACTIVITY_TRACKER_EXT_LIB_TYPE.ACTIVITY_TRACKER_CURRENT_INFO.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActivityTrackerCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        ACTIVITY_TRACKER_BATCH_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_ACTIVITY_TRACKER_BATCH_CURRENT_INFO.getCode()) {
            protected String getKey() {
                return ACTIVITY_TRACKER_EXT_LIB_TYPE.ACTIVITY_TRACKER_BATCH_CURRENT_INFO.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActivityTrackerBatchCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        SPECIFIC_POSE_ALERT(ContextType.SENSORHUB_RUNNER_SPECIFIC_POSE_ALERT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SPECIFIC_POSE_ALERT.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SpecificPoseAlertRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        SLEEP_MONITOR(ContextType.SENSORHUB_RUNNER_SLEEP_MONITOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SLEEP_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SleepMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        SLEEP_MONITOR_CURRENT_INFO(ContextType.REQUEST_SENSORHUB_SLEEP_MONITOR_CURRENT_INFO.getCode()) {
            protected String getKey() {
                return SLEEP_MONITOR_EXT_LIB_TYPE.SLEEP_MONITOR_CURRENT_INFO.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SleepMonitorCurrentInfoRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForExtLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        CAPTURE_MOTION(ContextType.SENSORHUB_RUNNER_CAPTURE_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CAPTURE_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CaptureMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        CALL_MOTION(ContextType.SENSORHUB_RUNNER_CALL_MOTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CALL_MOTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CallMotionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        STEP_LEVEL_MONITOR(ContextType.SENSORHUB_RUNNER_SL_MONITOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_LEVEL_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SLMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.NORMAL_MODE.toString();
            }
        },
        STEP_LEVEL_MONITOR_EXTENDED_INTERRUPT(ContextType.SENSORHUB_RUNNER_SL_MONITOR_EXTENDED_INTERRUPT.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_LEVEL_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ExtandedInterruptContextProvider(new SLMonitorExtendedInterruptRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable())));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.EXTANDED_INTERRUPT_MODE.toString();
            }
        },
        ACTIVE_TIME(ContextType.SENSORHUB_RUNNER_ACTIVE_TIME.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_STEP_LEVEL_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActiveTimeRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.BATCH_MODE.toString();
            }
        },
        FLAT_MOTION_FOR_TABLE_MODE(ContextType.SENSORHUB_RUNNER_FLAT_MOTION_FOR_TABLE_MODE.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_FLAT_MOTION_FOR_TABLE_MODE.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new FlatMotionForTableModeRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        CARRYING_STATUS_MONITOR(ContextType.SENSORHUB_RUNNER_CARRYING_STATUS_MONITOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CARRYING_STATUS_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new CarryingStatusMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        BOTTOM_FLAT_DETECTOR(ContextType.SENSORHUB_RUNNER_BOTTOM_FLAT_DETECTOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_BOTTOM_FLAT_DETECTOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BottomFlatDetectorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        PHONE_STATE_MONITOR(ContextType.SENSORHUB_RUNNER_PHONE_STATE_MONITOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_PHONE_STATE_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new PhoneStateMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        EXERCISE(ContextType.SENSORHUB_RUNNER_EXERCISE.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_EXERCISE.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ExerciseRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        AUTO_BRIGHTNESS(ContextType.SENSORHUB_RUNNER_AUTO_BRIGHTNESS.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_AUTO_BRIGHTNESS.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AutoBrightnessRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        ABNORMAL_PRESSURE_MONITOR(ContextType.SENSORHUB_RUNNER_ABNORMAL_PRESSURE_MONITOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ABNORMAL_PRESSURE_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AbnormalPressureMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        HALL_SENSOR(ContextType.SENSORHUB_RUNNER_HALL_SENSOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_HALL_SENSOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new HallSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        EAD(ContextType.SENSORHUB_RUNNER_EAD.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_EAD.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new EADRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        DUAL_DISPLAY_ANGLE(ContextType.SENSORHUB_RUNNER_DUAL_DISPLAY_ANGLE.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_DUAL_DISPLAY_ANGLE.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new DualDisplayAngleRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        WIRELESS_CHARGING_MONITOR(ContextType.SENSORHUB_RUNNER_WIRELESS_CHARGING_MONITOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_WIRELESS_CHARGING_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WirelessChargingMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        SLOCATION_RUNNER(ContextType.SENSORHUB_RUNNER_SLOCATION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SLOCATION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SLocationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        DEVICE_PHYSICAL_CONTEXT_MONITOR(ContextType.SENSORHUB_RUNNER_DEVICE_PHYSICAL_CONTEXT_MONITOR.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new DevicePhysicalContextMonitorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), getSubKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            protected String getSubKey() {
                return MODE.NORMAL_MODE.toString();
            }
        },
        MAIN_SCREEN_DETECTION_RUNNER(ContextType.SENSORHUB_RUNNER_MAIN_SCREEN_DETECTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_MAIN_SCREEN_DETECTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MainScreenDetectionRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        ANY_MOTION_DETECTOR_RUNNER(ContextType.SENSORHUB_RUNNER_ANY_MOTION_DETECTOR_RUNNER.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ANY_MOTION_DETECTOR.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AnyMotionDetectorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        SENSOR_STATUS_CHECK_RUNNER(ContextType.SENSORHUB_RUNNER_SENSOR_STATUS_CHECK.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_SENSOR_STATUS_CHECK.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SensorStatusCheckRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        CH_LOC_TRIGGER(ContextType.SENSORHUB_RUNNER_CH_LOC_TRIGGER.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_CH_LOC_TRIGGER.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ChLocTriggerRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        FREE_FALL_DETECTION(ContextType.SENSORHUB_RUNNER_FREE_FALL_DETECTION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_FREE_FALL_DETECTION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new FreeFallDetection(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        },
        ACTIVITY_CALIBRATION(ContextType.SENSORHUB_RUNNER_ACTIVITY_CALIBRATION.getCode()) {
            protected String getKey() {
                return DATA_TYPE.LIBRARY_DATATYPE_ACTIVITY_CALIBRATION.toString();
            }

            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new ActivityCalibrationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getApPowerObservable()));
                }
                setOptionForLib(getKey(), name());
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }
        };
        
        private final String name;

        private SensorHubRunnerList(String str) {
            this.name = str;
        }

        private String getParserMapKey() {
            return getSubKey() != null ? getSubKey() : getKey();
        }

        private void registerExtLibParser(String str, String str2) {
            SensorHubParserBean extLibParser = SensorHubParserProvider.getInstance().getExtLibParser();
            if (extLibParser != null) {
                extLibParser.registerParser(str, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(str2)).getContextProvider());
            }
        }

        private void registerLibParser(String str, String str2) {
            SensorHubParserBean libParser = SensorHubParserProvider.getInstance().getLibParser();
            if (libParser != null) {
                libParser.registerParser(str, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(str2)).getContextProvider());
            }
        }

        private void registerRequestLibParser(String str, String str2) {
            SensorHubParserBean requestLibParser = SensorHubParserProvider.getInstance().getRequestLibParser();
            if (requestLibParser != null) {
                requestLibParser.registerParser(str, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(str2)).getContextProvider());
            }
        }

        private void unregisterLibParser(String str, String str2) {
            if (str == null || str.isEmpty()) {
                CaLogger.error("Key is null");
                return;
            }
            SensorHubParserBean sensorHubParserBean;
            if (SensorHubMultiModeParser.getInstance().containsParser(str)) {
                sensorHubParserBean = (SensorHubParserBean) SensorHubMultiModeParser.getInstance().getParser(str);
                if (!(sensorHubParserBean == null || str2 == null || str2.isEmpty())) {
                    sensorHubParserBean.unregisterParser(str2);
                }
            }
            SensorHubParserBean libParser = SensorHubParserProvider.getInstance().getLibParser();
            if (libParser != null) {
                if (MultiModeContextList.getInstance().isMultiModeType(str)) {
                    sensorHubParserBean = (SensorHubParserBean) SensorHubMultiModeParser.getInstance().getParser(str);
                    if (sensorHubParserBean == null || sensorHubParserBean.checkParserMap()) {
                        return;
                    }
                }
                libParser.unregisterParser(str);
                if (!(str2 == null || str2.isEmpty())) {
                    libParser.unregisterParser(str2);
                }
            }
        }

        protected abstract String getKey();

        public final ContextComponent getObject(Object... objArr) {
            return getObject();
        }

        public final ContextComponent getObjectForSubCollection() {
            return getObject();
        }

        public final ContextComponent getObjectForSubCollection(Object... objArr) {
            return getObjectForSubCollection();
        }

        protected String getSubKey() {
            return null;
        }

        public void removeObject(String str) {
            if (str == null || str.isEmpty()) {
                CaLogger.error("Service is null");
                return;
            }
            if (ContextProviderCreator.removeObj(str)) {
                if (getKey() == null || getKey().isEmpty()) {
                    CaLogger.error("Key is null");
                    return;
                }
                unregisterLibParser(getKey(), getSubKey());
            }
        }

        protected final void setOptionForExtLib(String str, String str2) {
            registerExtLibParser(str, str2);
        }

        protected final void setOptionForLib(String str, String str2) {
            registerLibParser(str, str2);
        }

        protected final void setOptionForLib(String str, String str2, String str3) {
            registerLibParser(str, str3);
            if (SensorHubMultiModeParser.getInstance().containsParser(str)) {
                SensorHubParserBean sensorHubParserBean = (SensorHubParserBean) SensorHubMultiModeParser.getInstance().getParser(str);
                if (sensorHubParserBean == null) {
                    CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
                    return;
                }
                sensorHubParserBean.registerParser(str2, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(str3)).getContextProvider());
            } else if (str.equals(DATA_TYPE.LIBRARY_DATATYPE_ENVIRONMENT_SENSOR.toString())) {
                EnvironmentSensorHandler.getInstance().registerParser(str2, (ISensorHubParser) ((ContextComponent) ContextProviderCreator.getContextProviderMap().get(str3)).getContextProvider());
            }
        }

        protected final void setOptionForRequestLib(String str, String str2) {
            registerRequestLibParser(str, str2);
        }
    }

    public SensorHubRunnerConcreteCreator(Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable, int i) {
        super(context, looper, iSensorHubResetObservable, i);
    }

    public final IListObjectCreator getValueOfList(String str) {
        for (Object obj : SensorHubRunnerList.values()) {
            if (obj.name().equals(str)) {
                return obj;
            }
        }
        return null;
    }
}
