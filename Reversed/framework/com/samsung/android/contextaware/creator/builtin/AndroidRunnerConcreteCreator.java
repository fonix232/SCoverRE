package com.samsung.android.contextaware.creator.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.IListObjectCreator;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.AccelerometerSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.BestLocationRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.GpsRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.GyroscopeSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.MagneticSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.OrientationSensorRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.SatelliteRunner;
import com.samsung.android.contextaware.dataprovider.androidprovider.builtin.WpsRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;

public class AndroidRunnerConcreteCreator extends ContextProviderCreator {

    private enum AndroidRunnerList implements IListObjectCreator {
        RAW_GPS(ContextType.ANDROID_RUNNER_RAW_GPS.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new GpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        RAW_WPS(ContextType.ANDROID_RUNNER_RAW_WPS.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new WpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new WpsRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        RAW_SATELLITE(ContextType.ANDROID_RUNNER_RAW_SATELLITE.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new SatelliteRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new SatelliteRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        BEST_LOCATION(ContextType.ANDROID_RUNNER_BEST_LOCATION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new BestLocationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new BestLocationRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }
        },
        ACCELEROMETER_SENSOR(ContextType.ANDROID_RUNNER_ACCELEROMETER_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObject(Object... objArr) {
                if (objArr.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) objArr[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObjectForSubCollection(Object... objArr) {
                return new AccelerometerSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) objArr[0]).intValue());
            }
        },
        ORIENTATION_SENSOR(ContextType.ANDROID_RUNNER_ORIENTATION_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObject(Object... objArr) {
                if (objArr.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) objArr[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObjectForSubCollection(Object... objArr) {
                return new OrientationSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) objArr[0]).intValue());
            }
        },
        MAGNETIC_SENSOR(ContextType.ANDROID_RUNNER_MAGNETIC_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObject(Object... objArr) {
                if (objArr.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) objArr[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObjectForSubCollection(Object... objArr) {
                return new MagneticSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) objArr[0]).intValue());
            }
        },
        GYROSCOPE_SENSOR(ContextType.ANDROID_RUNNER_GYROSCOPE_SENSOR.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObject(Object... objArr) {
                if (objArr.length != 1) {
                    return null;
                }
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), ContextProviderCreator.getApPowerObservable(), ((Integer) objArr[0]).intValue()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final ContextComponent getObjectForSubCollection() {
                return new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null);
            }

            public final ContextComponent getObjectForSubCollection(Object... objArr) {
                return new GyroscopeSensorRunner(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), null, ((Integer) objArr[0]).intValue());
            }
        };
        
        private final String name;

        private AndroidRunnerList(String str) {
            this.name = str;
        }

        public ContextComponent getObject(Object... objArr) {
            return getObject();
        }

        public ContextComponent getObjectForSubCollection() {
            return getObject();
        }

        public ContextComponent getObjectForSubCollection(Object... objArr) {
            return getObjectForSubCollection();
        }

        public void removeObject(String str) {
            ContextProviderCreator.removeObj(str);
        }
    }

    public AndroidRunnerConcreteCreator(Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable, int i) {
        super(context, looper, iSensorHubResetObservable, i);
    }

    public final IListObjectCreator getValueOfList(String str) {
        for (Object obj : AndroidRunnerList.values()) {
            if (obj.name().equals(str)) {
                return obj;
            }
        }
        return null;
    }
}
