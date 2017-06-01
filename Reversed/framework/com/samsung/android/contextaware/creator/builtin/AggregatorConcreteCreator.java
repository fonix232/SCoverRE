package com.samsung.android.contextaware.creator.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.builtin.LifeLogAggregator;
import com.samsung.android.contextaware.aggregator.builtin.LocationAggregator;
import com.samsung.android.contextaware.aggregator.builtin.MovingAggregator;
import com.samsung.android.contextaware.aggregator.builtin.TemperatureHumidityAggregator;
import com.samsung.android.contextaware.aggregator.lpp.LppAggregator;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.IListObjectCreator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class AggregatorConcreteCreator extends ContextProviderCreator {
    private static CopyOnWriteArrayList<ContextProviderCreator> sRunnerCreator;

    private enum AggregatorList implements IListObjectCreator {
        LOCATION(ContextType.AGGREGATOR_LOCATION.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LocationAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
                copyOnWriteArrayList.add(ContextType.ANDROID_RUNNER_RAW_GPS.getCode());
                copyOnWriteArrayList.add(ContextType.ANDROID_RUNNER_RAW_SATELLITE.getCode());
                copyOnWriteArrayList.add(ContextType.ANDROID_RUNNER_RAW_WPS.getCode());
                return copyOnWriteArrayList;
            }
        },
        MOVING(ContextType.AGGREGATOR_MOVING.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new MovingAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
                copyOnWriteArrayList.add(ContextType.AGGREGATOR_LOCATION.getCode());
                copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode());
                return copyOnWriteArrayList;
            }
        },
        LPP(ContextType.AGGREGATOR_LPP.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LppAggregator(ContextProviderCreator.getVersion(), ContextProviderCreator.getContext(), ContextProviderCreator.getLooper(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
                copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_APDR.getCode());
                return copyOnWriteArrayList;
            }
        },
        TEMPERATURE_HUMIDITY(ContextType.AGGREGATOR_TEMPERATURE_HUMIDITY.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new TemperatureHumidityAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
                copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_RAW_TEMPERATURE_HUMIDITY_SENSOR.getCode());
                return copyOnWriteArrayList;
            }
        },
        LIFE_LOG(ContextType.AGGREGATOR_LIFE_LOG.getCode()) {
            public final ContextComponent getObject() {
                if (!ContextProviderCreator.getContextProviderMap().containsKey(name())) {
                    ContextProviderCreator.getContextProviderMap().put(name(), new LifeLogAggregator(ContextProviderCreator.getVersion(), makeListForContextCreation(), ContextProviderCreator.getApPowerObservable()));
                }
                return (ContextComponent) ContextProviderCreator.getContextProviderMap().get(name());
            }

            public final CopyOnWriteArrayList<String> getSubCollectionList() {
                CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList();
                copyOnWriteArrayList.add(ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode());
                copyOnWriteArrayList.add(ContextType.AGGREGATOR_LPP.getCode());
                return copyOnWriteArrayList;
            }
        };
        
        private final String name;

        private AggregatorList(String str) {
            this.name = str;
        }

        private static ContextComponent getRunnerObj(String str, Object... objArr) {
            ContextComponent contextComponent = null;
            Iterator it = AggregatorConcreteCreator.getRunnerCreator().iterator();
            while (it.hasNext()) {
                ContextProviderCreator contextProviderCreator = (ContextProviderCreator) it.next();
                if (contextProviderCreator != null) {
                    contextComponent = contextProviderCreator.create(str, true, objArr);
                    if (contextComponent != null) {
                        break;
                    }
                }
            }
            return contextComponent;
        }

        public final ContextComponent getObject(Object... objArr) {
            return getObject();
        }

        public final ContextComponent getObjectForSubCollection() {
            return getObject();
        }

        public final ContextComponent getObjectForSubCollection(Object... objArr) {
            return getObjectForSubCollection();
        }

        public CopyOnWriteArrayList<String> getSubCollectionList() {
            return new CopyOnWriteArrayList();
        }

        protected final CopyOnWriteArrayList<ContextComponent> makeListForContextCreation() {
            CopyOnWriteArrayList<ContextComponent> copyOnWriteArrayList = new CopyOnWriteArrayList();
            Iterable<String> subCollectionList = getSubCollectionList();
            if (subCollectionList == null) {
                CaLogger.error("list is null.");
                return null;
            }
            for (String runnerObj : subCollectionList) {
                copyOnWriteArrayList.add(getRunnerObj(runnerObj, new Object[0]));
            }
            return copyOnWriteArrayList;
        }

        public void removeObject(String str) {
            ContextProviderCreator.removeObj(str);
        }
    }

    public AggregatorConcreteCreator(CopyOnWriteArrayList<ContextProviderCreator> copyOnWriteArrayList, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable, int i) {
        super(context, looper, iSensorHubResetObservable, i);
        setRunnerCreator(new CopyOnWriteArrayList());
        setRunnerCreator(copyOnWriteArrayList);
        sRunnerCreator.add(this);
    }

    private static CopyOnWriteArrayList<ContextProviderCreator> getRunnerCreator() {
        return sRunnerCreator;
    }

    private static void setRunnerCreator(CopyOnWriteArrayList<ContextProviderCreator> copyOnWriteArrayList) {
        sRunnerCreator = copyOnWriteArrayList;
    }

    public final CopyOnWriteArrayList<String> getSubCollectionList(String str) {
        return AggregatorList.valueOf(str).getSubCollectionList();
    }

    public final IListObjectCreator getValueOfList(String str) {
        for (Object obj : AggregatorList.values()) {
            if (obj.name().equals(str)) {
                return obj;
            }
        }
        return null;
    }
}
