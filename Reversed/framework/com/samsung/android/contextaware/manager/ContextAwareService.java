package com.samsung.android.contextaware.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.InterruptModeContextList;
import com.samsung.android.contextaware.manager.IContextAwareService.Stub;
import com.samsung.android.contextaware.manager.fault.CmdProcessResultManager;
import com.samsung.android.contextaware.manager.fault.FaultDetectionManager;
import com.samsung.android.contextaware.manager.fault.RestoreManager;
import com.samsung.android.contextaware.utilbundle.autotest.CaAutoTestScenarioManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ContextAwareService extends Stub implements IContextObserver {
    public static final int BINDER_DIED_OPERATION = 3;
    public static final int NORMAL_OPERATION = 1;
    private static final int NOTIFY_WATING_TIME = 6;
    public static final int RESTORE_OPERATION = 2;
    private HandlerThread handlerThread;
    private volatile boolean isVersionSetting;
    private CaAutoTestScenarioManager mAutoTest;
    private boolean mCmdProcessResultNotifyCompletion;
    private boolean mContextCollectionResultNotifyCompletion;
    private ContextManager mContextManager;
    private ReentrantLock mMutex;
    private ServiceHandler mServiceHandler;
    private int mVersion;

    public final class Listener implements DeathRecipient {
        private CmdProcessResultManager mCmdProcessResultManager;
        private final ConcurrentHashMap<Integer, Integer> mServices = new ConcurrentHashMap();
        private final IBinder mToken;

        Listener(IBinder iBinder) {
            this.mToken = iBinder;
        }

        private synchronized void callback(int i, Bundle bundle) {
            try {
                IContextAwareCallback asInterface = IContextAwareCallback.Stub.asInterface(this.mToken);
                if (asInterface != null) {
                    asInterface.caCallback(i, bundle);
                    ContextAwareService.this.mContextCollectionResultNotifyCompletion = true;
                }
                notifyAll();
            } catch (Throwable e) {
                CaLogger.exception(e);
                notifyAll();
            } catch (Throwable e2) {
                CaLogger.exception(e2);
                notifyAll();
            } catch (Throwable th) {
                notifyAll();
            }
        }

        public void binderDied() {
            ContextAwareService.this.mMutex.lock();
            try {
                CaLogger.warning("[binderDied 01] Mutex is locked for binderDied");
                Iterator it = this.mServices.keySet().iterator();
                Iterator it2 = it;
                while (it.hasNext()) {
                    int intValue = ((Integer) it.next()).intValue();
                    this.mServices.remove(Integer.valueOf(intValue));
                    ContextAwareService.this.mContextManager.stop(this, ContextList.getInstance().getServiceCode(intValue), null, this.mCmdProcessResultManager, 3);
                }
                ListenerListManager.getInstance().removeListener(this);
                this.mToken.unlinkToDeath(this, 0);
                CaLogger.warning("[binderDied 02] Mutex is unlocked for binderDied");
            } finally {
                ContextAwareService.this.mMutex.unlock();
            }
        }

        public void decreaseServiceCount(int i) {
            if (this.mServices.containsKey(Integer.valueOf(i))) {
                this.mServices.put(Integer.valueOf(i), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(i))).intValue() - 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }

        public ConcurrentHashMap<Integer, Integer> getServices() {
            return this.mServices;
        }

        public IBinder getToken() {
            return this.mToken;
        }

        public void increaseServiceCount(int i) {
            if (this.mServices.containsKey(Integer.valueOf(i))) {
                this.mServices.put(Integer.valueOf(i), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(i))).intValue() + 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }

        public final void setContextCollectionResultNotifyCompletion(boolean z) {
            ContextAwareService.this.mContextCollectionResultNotifyCompletion = z;
        }
    }

    @SuppressLint({"HandlerLeak"})
    public final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        private void callback(int i, Bundle bundle) {
            Iterator it = ListenerListManager.getInstance().getListenerList().iterator();
            while (it.hasNext()) {
                Listener listener = (Listener) it.next();
                if (listener != null && listener.mServices.containsKey(Integer.valueOf(i))) {
                    listener.callback(i, bundle);
                }
            }
            it = ListenerListManager.getInstance().getWatcherList().iterator();
            while (it.hasNext()) {
                Watcher watcher = (Watcher) it.next();
                if (watcher != null && watcher.mServices.containsKey(Integer.valueOf(i))) {
                    watcher.callback(i, bundle);
                }
            }
        }

        private boolean notifyOperationCheckResult(int i, Bundle bundle) {
            if (i != ContextType.CMD_PROCESS_FAULT_DETECTION.ordinal()) {
                return false;
            }
            if (bundle == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_CONTEXT_NULL_EXCEPTION.getMessage());
                return false;
            }
            BaseBundle bundle2 = bundle.getBundle("Listener");
            if (bundle2 == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_BUNDLE_NULL_EXCEPTION.getMessage());
                return true;
            }
            IBinder iBinder = bundle2.getIBinder("Binder");
            int i2 = bundle2.getInt("Service");
            if (iBinder == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_BINDER_NULL_EXCEPTION.getMessage());
                return true;
            }
            Listener listener = ListenerListManager.getInstance().getListener(iBinder);
            if (listener == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_LISTENER_NULL_EXCEPTION.getMessage());
                if (bundle.getInt("CheckResult") != 0) {
                    CaLogger.info("This cmd proccess was stopped and that's because the fault detection result is not success");
                    ContextAwareService.this.mContextCollectionResultNotifyCompletion = true;
                }
            } else if (bundle.getInt("Service") == i2) {
                bundle.remove("Listener");
                listener.callback(i, bundle);
            } else {
                CaLogger.error(ContextAwareServiceErrors.ERROR_SERVICE_FAULT.getMessage());
            }
            return true;
        }

        public synchronized void handleMessage(Message message) {
            int i = message.what;
            Bundle bundle = (Bundle) ((Bundle) message.obj).clone();
            if (notifyOperationCheckResult(i, bundle)) {
                ContextAwareService.this.mCmdProcessResultNotifyCompletion = true;
            } else {
                callback(i, bundle);
            }
            notifyAll();
        }
    }

    public final class Watcher implements DeathRecipient {
        private final ConcurrentHashMap<Integer, Integer> mServices = new ConcurrentHashMap();
        private final IBinder mToken;

        Watcher(IBinder iBinder) {
            this.mToken = iBinder;
        }

        private synchronized void callback(int i, Bundle bundle) {
            try {
                IContextAwareCallback asInterface = IContextAwareCallback.Stub.asInterface(this.mToken);
                if (asInterface != null) {
                    asInterface.caCallback(i, bundle);
                }
                notifyAll();
            } catch (Throwable e) {
                CaLogger.exception(e);
                notifyAll();
            } catch (Throwable e2) {
                CaLogger.exception(e2);
                notifyAll();
            } catch (Throwable th) {
                notifyAll();
            }
        }

        public void binderDied() {
            ContextAwareService.this.mMutex.lock();
            try {
                CaLogger.warning("[binderDied 01] Mutex is locked for binderDied");
                Iterator it = this.mServices.keySet().iterator();
                Iterator it2 = it;
                while (it.hasNext()) {
                    this.mServices.remove(Integer.valueOf(((Integer) it.next()).intValue()));
                }
                ListenerListManager.getInstance().removeWatcher(this);
                this.mToken.unlinkToDeath(this, 0);
                CaLogger.warning("[binderDied 02] Mutex is unlocked for binderDied");
            } finally {
                ContextAwareService.this.mMutex.unlock();
            }
        }

        public void decreaseServiceCount(int i) {
            if (this.mServices.containsKey(Integer.valueOf(i))) {
                this.mServices.put(Integer.valueOf(i), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(i))).intValue() - 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }

        public ConcurrentHashMap<Integer, Integer> getServices() {
            return this.mServices;
        }

        public void increaseServiceCount(int i) {
            if (this.mServices.containsKey(Integer.valueOf(i))) {
                this.mServices.put(Integer.valueOf(i), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(i))).intValue() + 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }
    }

    public ContextAwareService(Context context) {
        this.mCmdProcessResultNotifyCompletion = true;
        this.mContextCollectionResultNotifyCompletion = true;
        this.mVersion = 1;
        this.isVersionSetting = false;
        this.handlerThread = null;
        this.handlerThread = new HandlerThread("context_aware");
        this.handlerThread.start();
        Looper looper = this.handlerThread.getLooper();
        if (looper == null) {
            this.handlerThread.quitSafely();
            this.handlerThread = null;
            CaLogger.error(ContextAwareServiceErrors.ERROR_LOOPER_NULL_EXCEPTION.getMessage());
            return;
        }
        this.mServiceHandler = new ServiceHandler(looper);
        this.mContextManager = new ContextManager(context, looper, this.mVersion);
        this.mAutoTest = new CaAutoTestScenarioManager(context);
        this.mMutex = new ReentrantLock(true);
        ListenerListManager.getInstance().setCreator(this.mContextManager.getCreator());
        FaultDetectionManager.getInstance().initializeManager(this.mContextManager);
    }

    private void displayUsedCountForService(int i) {
        CaLogger.info("totalCnt = " + Integer.toString(ListenerListManager.getInstance().getUsedTotalCount(ContextList.getInstance().getServiceCode(i))) + ", serviceCount = " + Integer.toString(ListenerListManager.getInstance().getUsedServiceCount(ContextList.getInstance().getServiceCode(i))) + ", subCollectionCount = " + Integer.toString(ListenerListManager.getInstance().getUsedSubCollectionCount(ContextList.getInstance().getServiceCode(i))));
    }

    private void doCommendProcess(String str, Listener listener, int i) {
        if (!InterruptModeContextList.getInstance().isInterruptModeType(i)) {
            if (str.equals(RestoreManager.REGISTER_CMD_RESTORE) && !isUsableService(listener, i)) {
                return;
            }
            if (str.equals(RestoreManager.UNREGISTER_CMD_RESTORE) && isUsableService(listener, i)) {
                return;
            }
        }
        FaultDetectionManager.getInstance().registerCmdProcessResultManager(listener.mCmdProcessResultManager);
        if (str.equals(RestoreManager.REGISTER_CMD_RESTORE)) {
            if (!listener.mServices.containsKey(Integer.valueOf(i))) {
                listener.mServices.put(Integer.valueOf(i), Integer.valueOf(0));
            }
            listener.increaseServiceCount(i);
            this.mContextManager.start(listener, ContextList.getInstance().getServiceCode(i), this, 1);
        } else if (str.equals(RestoreManager.UNREGISTER_CMD_RESTORE)) {
            if (listener.mServices.containsKey(Integer.valueOf(i))) {
                listener.decreaseServiceCount(i);
                if (((Integer) listener.mServices.get(Integer.valueOf(i))).intValue() <= 0) {
                    listener.mServices.remove(Integer.valueOf(i));
                }
            }
            this.mContextManager.stop(listener, ContextList.getInstance().getServiceCode(i), this, null, 1);
        }
        if (!waitForNotifyOperationCheckResult()) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage());
            FaultDetectionManager.getInstance().setRestoreEnable();
        }
        CaLogger.debug("complete notify the operation result.");
        if (FaultDetectionManager.getInstance().isRestoreEnable()) {
            FaultDetectionManager.getInstance().runRestore(str, listener, i, this);
        } else if (str.equals(RestoreManager.REGISTER_CMD_RESTORE)) {
            this.mContextManager.notifyInitContext(ContextList.getInstance().getServiceCode(i));
        }
    }

    private boolean isUsableService(Listener listener, int i) {
        return !listener.mServices.containsKey(Integer.valueOf(i));
    }

    private void showListenerList() {
        CaLogger.debug("===== Context Aware Service List =====");
        Iterator it = ListenerListManager.getInstance().getListenerList().iterator();
        while (it.hasNext()) {
            Listener listener = (Listener) it.next();
            Iterator it2 = listener.mServices.keySet().iterator();
            Iterator it3 = it2;
            while (it2.hasNext()) {
                int intValue = ((Integer) it2.next()).intValue();
                CaLogger.info("Listener : " + listener.toString() + ", Service : " + ContextList.getInstance().getServiceCode(intValue) + "(" + ((Integer) listener.mServices.get(Integer.valueOf(intValue))).intValue() + ")");
            }
        }
    }

    private boolean waitForNotifyOperationCheckResult() {
        int i = 0;
        while (i < 600) {
            try {
                Thread.sleep(10);
                if (this.mCmdProcessResultNotifyCompletion && this.mContextCollectionResultNotifyCompletion) {
                    return true;
                }
                i++;
            } catch (Throwable e) {
                CaLogger.exception(e);
            }
        }
        return false;
    }

    protected void finalize() {
        if (this.handlerThread != null) {
            this.handlerThread.quit();
        }
        this.handlerThread = null;
    }

    public final void getContextInfo(IBinder iBinder, int i) {
        this.mMutex.lock();
        try {
            CaLogger.warning("[getContext 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(i));
            Object obj = null;
            Listener listener = null;
            this.mCmdProcessResultNotifyCompletion = true;
            this.mContextCollectionResultNotifyCompletion = false;
            Iterator it = ListenerListManager.getInstance().getListenerList().iterator();
            while (it.hasNext()) {
                listener = (Listener) it.next();
                if (iBinder.equals(listener.mToken)) {
                    if (!listener.mServices.containsKey(Integer.valueOf(i))) {
                        listener.mServices.put(Integer.valueOf(i), Integer.valueOf(1));
                    }
                    FaultDetectionManager.getInstance().registerCmdProcessResultManager(new CmdProcessResultManager(listener.mToken, this.mServiceHandler));
                    ListenerListManager.getInstance().addListener(listener);
                    this.mContextManager.getContextInfo(listener, ContextList.getInstance().getServiceCode(i), this);
                    obj = 1;
                    if (obj == null) {
                        CaLogger.error(ContextAwareServiceErrors.ERROR_LISTENER_NOT_REGISTERED.getMessage());
                    } else if (!waitForNotifyOperationCheckResult()) {
                        CaLogger.error(ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage());
                    }
                    this.mContextManager.unregisterObservers(ContextList.getInstance().getServiceCode(i), this);
                    if (listener != null) {
                        if (listener.mServices.size() != 1 && listener.mServices.contains(Integer.valueOf(i))) {
                            ListenerListManager.getInstance().removeListener(listener);
                        } else if (listener.mServices.contains(Integer.valueOf(i))) {
                            listener.mServices.remove(Integer.valueOf(i));
                        }
                    }
                    FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
                    CaLogger.warning("[getContext 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
                }
            }
            if (obj == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_LISTENER_NOT_REGISTERED.getMessage());
            } else if (waitForNotifyOperationCheckResult()) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage());
            }
            this.mContextManager.unregisterObservers(ContextList.getInstance().getServiceCode(i), this);
            if (listener != null) {
                if (listener.mServices.size() != 1) {
                }
                if (listener.mServices.contains(Integer.valueOf(i))) {
                    listener.mServices.remove(Integer.valueOf(i));
                }
            }
            FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
            CaLogger.warning("[getContext 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
        } finally {
            this.mMutex.unlock();
        }
    }

    public final int getVersion() {
        return this.mVersion;
    }

    public final void initializeAutoTest() {
        this.mAutoTest.initilizeAutoTest();
    }

    public final void registerCallback(IBinder iBinder, int i) throws RemoteException {
        this.mMutex.lock();
        try {
            CaLogger.warning("[regi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(i));
            FaultDetectionManager.getInstance().initializeRestoreManager();
            this.mCmdProcessResultNotifyCompletion = false;
            this.mContextCollectionResultNotifyCompletion = true;
            Object obj = null;
            Iterator it = ListenerListManager.getInstance().getListenerList().iterator();
            while (it.hasNext()) {
                Listener listener = (Listener) it.next();
                if (iBinder.equals(listener.mToken)) {
                    doCommendProcess(RestoreManager.REGISTER_CMD_RESTORE, listener, i);
                    obj = 1;
                    break;
                }
            }
            if (obj == null) {
                Listener listener2 = new Listener(iBinder);
                listener2.mCmdProcessResultManager = new CmdProcessResultManager(listener2.mToken, this.mServiceHandler);
                iBinder.linkToDeath(listener2, 0);
                ListenerListManager.getInstance().addListener(listener2);
                doCommendProcess(RestoreManager.REGISTER_CMD_RESTORE, listener2, i);
            }
            displayUsedCountForService(i);
            showListenerList();
            FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
            CaLogger.warning("[regi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void registerWatcher(IBinder iBinder, int i) throws RemoteException {
        this.mMutex.lock();
        try {
            Watcher watcher;
            CaLogger.warning("[regi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(i));
            Object obj = null;
            Iterator it = ListenerListManager.getInstance().getWatcherList().iterator();
            while (it.hasNext()) {
                Watcher watcher2 = (Watcher) it.next();
                if (iBinder.equals(watcher2.mToken)) {
                    obj = 1;
                    if (!watcher2.mServices.containsKey(Integer.valueOf(i))) {
                        watcher2.mServices.put(Integer.valueOf(i), Integer.valueOf(1));
                    }
                    if (obj == null) {
                        watcher = new Watcher(iBinder);
                        iBinder.linkToDeath(watcher, 0);
                        ListenerListManager.getInstance().addWatcher(watcher);
                        if (!watcher.mServices.containsKey(Integer.valueOf(i))) {
                            watcher.mServices.put(Integer.valueOf(i), Integer.valueOf(1));
                        }
                    }
                    CaLogger.warning("[regi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
                }
            }
            if (obj == null) {
                watcher = new Watcher(iBinder);
                iBinder.linkToDeath(watcher, 0);
                ListenerListManager.getInstance().addWatcher(watcher);
                if (watcher.mServices.containsKey(Integer.valueOf(i))) {
                    watcher.mServices.put(Integer.valueOf(i), Integer.valueOf(1));
                }
            }
            CaLogger.warning("[regi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void resetCAService(int i) {
        this.mMutex.lock();
        try {
            CaLogger.warning("[reset 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(i));
            CaLogger.info("reset service : " + ContextList.getInstance().getServiceCode(i));
            this.mContextManager.reset(ContextList.getInstance().getServiceCode(i));
            CaLogger.warning("[reset 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void setCALogger(boolean z, boolean z2, int i, boolean z3) {
        CaLogger.setConsoleLoggingEnable(z);
        CaLogger.setFileLoggingEnable(z2);
        CaLogger.setLogOption(i, z3);
    }

    public final boolean setCAProperty(int i, int i2, ContextAwarePropertyBundle contextAwarePropertyBundle) {
        boolean z = false;
        this.mMutex.lock();
        try {
            CaLogger.warning("[setProperty 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(i));
            z = this.mContextManager.setProperty(ContextList.getInstance().getServiceCode(i), i2, contextAwarePropertyBundle);
            CaLogger.info("result : " + Boolean.toString(z));
            CaLogger.warning("[setProperty 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
            return z;
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void setCmdProcessResultNotifyCompletion(boolean z) {
        this.mCmdProcessResultNotifyCompletion = z;
    }

    public final boolean setScenarioForDebugging(int i, int i2, byte[] bArr) {
        return this.mAutoTest.setScenarioForDebugging(i, i2, bArr);
    }

    public final boolean setScenarioForTest(int i, int i2) {
        return this.mAutoTest.setScenarioForTest(i, i2);
    }

    public final void setVersion(int i) {
        if (this.isVersionSetting) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_VERSION_SETTING.getMessage());
            return;
        }
        CaLogger.info("Version : " + Integer.toString(i));
        this.mVersion = i;
        this.mContextManager.setVersion(i);
        this.isVersionSetting = true;
    }

    public final void startAutoTest() {
        this.mAutoTest.startAutoTest();
    }

    public final void stopAutoTest() {
        this.mAutoTest.stopAutoTest();
    }

    public final boolean unregisterCallback(IBinder iBinder, int i) throws RemoteException {
        boolean z = true;
        this.mMutex.lock();
        try {
            CaLogger.warning("[unregi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(i));
            FaultDetectionManager.getInstance().initializeRestoreManager();
            this.mCmdProcessResultNotifyCompletion = false;
            this.mContextCollectionResultNotifyCompletion = true;
            Object obj = null;
            Iterator it = ListenerListManager.getInstance().getListenerList().iterator();
            while (it.hasNext()) {
                Listener listener = (Listener) it.next();
                if (iBinder.equals(listener.mToken)) {
                    obj = listener;
                    doCommendProcess(RestoreManager.UNREGISTER_CMD_RESTORE, listener, i);
                    break;
                }
            }
            if (obj == null || !obj.mServices.isEmpty()) {
                z = false;
            }
            if (z) {
                iBinder.unlinkToDeath(obj, 0);
                ListenerListManager.getInstance().removeListener(obj);
            }
            displayUsedCountForService(i);
            showListenerList();
            FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
            CaLogger.warning("[unregi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
            return z;
        } finally {
            this.mMutex.unlock();
        }
    }

    public final boolean unregisterWatcher(IBinder iBinder, int i) throws RemoteException {
        boolean z = true;
        this.mMutex.lock();
        try {
            CaLogger.warning("[unregi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(i));
            Object obj = null;
            Iterator it = ListenerListManager.getInstance().getWatcherList().iterator();
            while (it.hasNext()) {
                Watcher watcher = (Watcher) it.next();
                if (iBinder.equals(watcher.mToken)) {
                    obj = watcher;
                    if (watcher.mServices.containsKey(Integer.valueOf(i))) {
                        watcher.decreaseServiceCount(i);
                        if (((Integer) watcher.mServices.get(Integer.valueOf(i))).intValue() <= 0) {
                            watcher.mServices.remove(Integer.valueOf(i));
                        }
                    }
                    if (obj == null || !obj.mServices.isEmpty()) {
                        z = false;
                    }
                    if (z) {
                        iBinder.unlinkToDeath(obj, 0);
                        ListenerListManager.getInstance().removeWatcher(obj);
                    }
                    CaLogger.warning("[unregi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
                    return z;
                }
            }
            z = false;
            if (z) {
                iBinder.unlinkToDeath(obj, 0);
                ListenerListManager.getInstance().removeWatcher(obj);
            }
            CaLogger.warning("[unregi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(i));
            return z;
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void updateContext(String str, Bundle bundle) {
        Message obtain = Message.obtain();
        obtain.what = ContextList.getInstance().getServiceOrdinal(str);
        obtain.obj = bundle;
        this.mServiceHandler.sendMessage(obtain);
    }
}
