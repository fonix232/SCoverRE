package com.samsung.android.contextaware.creator;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ContextProviderCreator {
    private static ISensorHubResetObservable sAPPowerObservable;
    private static Context sContext;
    private static final ConcurrentHashMap<String, ContextComponent> sContextProviderMap = new ConcurrentHashMap();
    private static Looper sLooper;
    private static int sVersion;

    protected ContextProviderCreator(Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable, int i) {
        setContext(context);
        setLooper(looper);
        setAPPowerObservable(iSensorHubResetObservable);
        setVersion(i);
    }

    private ContextComponent create(String str, boolean z) {
        if (!existContextProvider(str)) {
            return null;
        }
        return z ? getValueOfList(str).getObjectForSubCollection() : getValueOfList(str).getObject();
    }

    protected static final ISensorHubResetObservable getApPowerObservable() {
        return sAPPowerObservable;
    }

    protected static final Context getContext() {
        return sContext;
    }

    protected static final synchronized ConcurrentHashMap<String, ContextComponent> getContextProviderMap() {
        ConcurrentHashMap<String, ContextComponent> concurrentHashMap;
        synchronized (ContextProviderCreator.class) {
            concurrentHashMap = sContextProviderMap;
        }
        return concurrentHashMap;
    }

    protected static Looper getLooper() {
        return sLooper;
    }

    protected static int getVersion() {
        return sVersion;
    }

    protected static final boolean removeObj(String str) {
        if (ListenerListManager.getInstance().getUsedTotalCount(str) >= 1) {
            return false;
        }
        if (getContextProviderMap().containsKey(str)) {
            getContextProviderMap().remove(str);
        }
        return true;
    }

    private static void setAPPowerObservable(ISensorHubResetObservable iSensorHubResetObservable) {
        sAPPowerObservable = iSensorHubResetObservable;
    }

    private static void setContext(Context context) {
        sContext = context;
    }

    private static void setLooper(Looper looper) {
        sLooper = looper;
    }

    public static void setVersion(int i) {
        sVersion = i;
    }

    public ContextComponent create(String str) {
        return create(str, false);
    }

    public final ContextComponent create(String str, boolean z, Object... objArr) {
        if (!existContextProvider(str)) {
            return null;
        }
        if (objArr == null || objArr.length <= 0) {
            return create(str, z);
        }
        return z ? getValueOfList(str).getObjectForSubCollection(objArr) : getValueOfList(str).getObject(objArr);
    }

    public ContextComponent create(String str, Object... objArr) {
        return create(str, false, objArr);
    }

    public final boolean existContextProvider(String str) {
        boolean z = true;
        IListObjectCreator iListObjectCreator = null;
        try {
            iListObjectCreator = getValueOfList(str);
        } catch (IllegalArgumentException e) {
            z = false;
        } catch (Throwable e2) {
            z = false;
            CaLogger.info("existContextProvider (" + str + ") Excp : " + e2.getMessage());
        }
        return iListObjectCreator == null ? false : z;
    }

    public abstract IListObjectCreator getValueOfList(String str);

    public final void removeContextObj(String str) {
        if (existContextProvider(str)) {
            getValueOfList(str).removeObject(str);
        }
    }
}
