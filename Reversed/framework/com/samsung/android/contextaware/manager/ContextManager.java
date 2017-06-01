package com.samsung.android.contextaware.manager;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.builtin.AggregatorConcreteCreator;
import com.samsung.android.contextaware.creator.builtin.AndroidRunnerConcreteCreator;
import com.samsung.android.contextaware.creator.builtin.SensorHubParserConcreteCreator;
import com.samsung.android.contextaware.creator.builtin.SensorHubRunnerConcreteCreator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubMultiModeParser;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.fault.FaultDetectionManager;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import com.samsung.android.contextaware.utilbundle.CaAlarmManager;
import com.samsung.android.contextaware.utilbundle.CaAutoBrightnessTableManager;
import com.samsung.android.contextaware.utilbundle.CaBootStatus;
import com.samsung.android.contextaware.utilbundle.CaCoverManager;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.CaTelephonyManager;
import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.IUtilManager;
import com.samsung.android.contextaware.utilbundle.SensorHubCommManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContextManager {
    private final CopyOnWriteArrayList<ContextProviderCreator> mCreator = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<IUtilManager> mUtilManager;

    protected ContextManager(Context context, Looper looper, int i) {
        SensorHubParserConcreteCreator sensorHubParserConcreteCreator = new SensorHubParserConcreteCreator(context);
        this.mCreator.add(new AndroidRunnerConcreteCreator(context, looper, sensorHubParserConcreteCreator.getPowerObservable(), i));
        this.mCreator.add(new SensorHubRunnerConcreteCreator(context, looper, sensorHubParserConcreteCreator.getPowerObservable(), i));
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add((ContextProviderCreator) this.mCreator.get(0));
        copyOnWriteArrayList.add((ContextProviderCreator) this.mCreator.get(1));
        this.mCreator.add(new AggregatorConcreteCreator(copyOnWriteArrayList, context, looper, sensorHubParserConcreteCreator.getPowerObservable(), i));
        this.mUtilManager = new CopyOnWriteArrayList();
        this.mUtilManager.add(CaPowerManager.getInstance());
        this.mUtilManager.add(CaTelephonyManager.getInstance());
        this.mUtilManager.add(CaAlarmManager.getInstance());
        this.mUtilManager.add(SensorHubCommManager.getInstance());
        this.mUtilManager.add(SensorHubMultiModeParser.getInstance());
        this.mUtilManager.add(CaBootStatus.getInstance());
        this.mUtilManager.add(CaTimeChangeManager.getInstance());
        this.mUtilManager.add(CaCoverManager.getInstance(looper));
        this.mUtilManager.add(CaAutoBrightnessTableManager.getInstance());
        initializeUtil(context);
    }

    private void initializeUtil(Context context) {
        for (IUtilManager initializeManager : this.mUtilManager) {
            initializeManager.initializeManager(context);
        }
    }

    protected final void getContextInfo(Listener listener, String str, IContextObserver iContextObserver) {
        ContextComponent contextProviderObj = getContextProviderObj(str);
        if (contextProviderObj != null) {
            contextProviderObj.getContextProvider().registerObserver(iContextObserver);
            contextProviderObj.getContextProvider().registerCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
            contextProviderObj.getContextProvider().getContextInfo(listener);
        }
    }

    protected final ContextComponent getContextProviderObj(String str) {
        Iterator it = this.mCreator.iterator();
        while (it.hasNext()) {
            ContextProviderCreator contextProviderCreator = (ContextProviderCreator) it.next();
            if (contextProviderCreator != null && contextProviderCreator.existContextProvider(str)) {
                return contextProviderCreator.create(str);
            }
        }
        return null;
    }

    protected final CopyOnWriteArrayList<ContextProviderCreator> getCreator() {
        return this.mCreator;
    }

    public final void notifyInitContext(String str) {
        ContextComponent contextProviderObj = getContextProviderObj(str);
        if (contextProviderObj != null) {
            contextProviderObj.getContextProvider().notifyInitContext();
        }
    }

    protected void removeContextProviderObj(String str) {
        Iterator it = this.mCreator.iterator();
        while (it.hasNext()) {
            ContextProviderCreator contextProviderCreator = (ContextProviderCreator) it.next();
            if (contextProviderCreator != null && contextProviderCreator.existContextProvider(str)) {
                contextProviderCreator.removeContextObj(str);
                return;
            }
        }
    }

    protected final void reset(String str) {
        ContextComponent contextProviderObj = getContextProviderObj(str);
        if (contextProviderObj == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_NOT_RUNNING.getCode()));
            return;
        }
        ContextComponent contextProvider = contextProviderObj.getContextProvider();
        if (contextProvider == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_NOT_RUNNING.getCode()));
        } else if (contextProvider.isRunning()) {
            contextProvider.getContextProvider().clearAccordingToRequest();
        } else {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_NOT_RUNNING.getCode()));
        }
    }

    protected final <E> boolean setProperty(String str, int i, E e) {
        ContextComponent contextProviderObj = getContextProviderObj(str);
        return contextProviderObj != null ? contextProviderObj.getContextProvider().setProperty(i, e) : false;
    }

    protected final void setVersion(int i) {
        Iterator it = this.mCreator.iterator();
        while (it.hasNext()) {
            it.next();
            ContextProviderCreator.setVersion(i);
        }
    }

    public final void start(Listener listener, String str, IContextObserver iContextObserver, int i) {
        ContextComponent contextProviderObj = getContextProviderObj(str);
        if (contextProviderObj != null) {
            contextProviderObj.getContextProvider().registerObserver(iContextObserver);
            contextProviderObj.getContextProvider().registerCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
            contextProviderObj.start(listener, i);
        }
    }

    public final void stop(Listener listener, String str, IContextObserver iContextObserver, ICmdProcessResultObserver iCmdProcessResultObserver, int i) {
        ContextComponent contextProviderObj = getContextProviderObj(str);
        if (contextProviderObj != null) {
            contextProviderObj.stop(listener, i);
            int serviceOrdinal = ContextList.getInstance().getServiceOrdinal(str);
            if (!listener.getServices().containsKey(Integer.valueOf(serviceOrdinal)) || ((Integer) listener.getServices().get(Integer.valueOf(serviceOrdinal))).intValue() <= 0) {
                if (iCmdProcessResultObserver != null) {
                    contextProviderObj.getContextProvider().unregisterCmdProcessResultObserver(iCmdProcessResultObserver);
                } else {
                    contextProviderObj.getContextProvider().unregisterCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
                }
                removeContextProviderObj(str);
            }
        }
    }

    protected final void unregisterObservers(String str, IContextObserver iContextObserver) {
        ContextComponent contextProviderObj = getContextProviderObj(str);
        if (contextProviderObj != null) {
            contextProviderObj.getContextProvider().unregisterCmdProcessResultObserver(FaultDetectionManager.getInstance().getCmdProcessResultObserver());
        }
    }
}
