package com.samsung.android.contextaware.manager;

import android.content.Context;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.Iterator;

public abstract class ContextProvider extends ContextComponent implements ISensorHubResetObserver, IApPowerObserver, ITimeChangeObserver {
    private int mApStatus;
    private final Context mContext;
    private final Looper mLooper;
    private int mPreparedCollection;
    private final ISensorHubResetObservable mSensorHubResetObservable;
    private final IContextTimeOutCheck mTimeOutCheck = new ContextTimeOutCheck(getTimeOutCheckObserver());
    private long mTimeStampForApStatus;
    private int mVersion;

    protected ContextProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        this.mVersion = i;
        this.mContext = context;
        this.mLooper = looper;
        this.mSensorHubResetObservable = iSensorHubResetObservable;
    }

    protected boolean checkFaultDetectionResult() {
        return true;
    }

    public abstract void disable();

    protected void disableForRestore() {
        disable();
    }

    protected void disableForStop(int i) {
        if (i == 2) {
            disableForRestore();
        } else {
            disable();
        }
    }

    protected void display() {
        BaseBundle contextBundleForDisplay = getContextBean().getContextBundleForDisplay();
        if (contextBundleForDisplay != null && !contextBundleForDisplay.isEmpty()) {
            CaLogger.debug("================= " + getContextType() + " =================");
            StringBuffer stringBuffer = new StringBuffer();
            for (String str : contextBundleForDisplay.keySet()) {
                if (str == null || str.isEmpty()) {
                    break;
                }
                stringBuffer.append(str + "=[" + getDisplayContents(contextBundleForDisplay, str) + "], ");
            }
            if (stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                stringBuffer.delete(stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), stringBuffer.length());
            }
            CaLogger.info(stringBuffer.toString());
        }
    }

    protected void doTimeOutChecking(Listener listener, Bundle bundle) {
        if (bundle == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_BUNDLE_NULL_EXCEPTION.getCode()));
        } else if (bundle.getInt("CheckResult") == 0) {
            clear();
            this.mTimeOutCheck.run();
        } else {
            CaLogger.error("FAULT_DETECTION result is not success");
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, bundle.getString("Cause")));
        }
    }

    public abstract void enable();

    protected void enableForRestore() {
        enable();
    }

    protected void enableForStart(int i) {
        if (i == 2) {
            enableForRestore();
        } else {
            enable();
        }
    }

    public final int getAPStatus() {
        return this.mApStatus;
    }

    protected final Context getContext() {
        return this.mContext;
    }

    protected void getContextInfo(Listener listener) {
        String dependentService = getDependentService();
        this.mTimeOutCheck.setTimeOutOccurence(false);
        if (dependentService == null || dependentService.isEmpty()) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NULL_EXCEPTION.getMessage());
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NULL_EXCEPTION.getMessage()));
            return;
        }
        Object obj = null;
        Iterator it = listener.getServices().keySet().iterator();
        Iterator it2 = it;
        while (it.hasNext()) {
            if (ContextList.getInstance().getServiceCode(((Integer) it.next()).intValue()).equals(dependentService)) {
                obj = 1;
                break;
            }
        }
        if (obj == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NOT_REGISTERED.getMessage());
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_DEPENDENT_SERVICE_NOT_REGISTERED.getMessage()));
            return;
        }
        enable();
        doTimeOutChecking(listener, getFaultDetectionResult());
    }

    public final ContextProvider getContextProvider() {
        return this;
    }

    public final String getContextTypeOfFaultDetection() {
        return ContextType.CMD_PROCESS_FAULT_DETECTION.getCode();
    }

    public abstract String[] getContextValueNames();

    protected String getDependentService() {
        return null;
    }

    protected String getDisplayContents(Bundle bundle, String str) {
        if (str == null || str.isEmpty()) {
            CaLogger.error("key is null");
            return null;
        } else if (bundle == null || !bundle.containsKey(str)) {
            CaLogger.error("bundle is null");
            return null;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            if (bundle.get(str) instanceof String[]) {
                for (String str2 : bundle.getStringArray(str)) {
                    stringBuffer.append(str2 + ", ");
                }
                if (stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                    stringBuffer.delete(stringBuffer.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), stringBuffer.length());
                }
                return stringBuffer.toString();
            }
            String string = bundle.getString(str);
            if (string != null && !string.isEmpty()) {
                return string;
            }
            CaLogger.error("bundle.getStringArray(key) is null");
            return null;
        }
    }

    public abstract Bundle getFaultDetectionResult();

    protected final Bundle getFaultDetectionResult(int i, String str) {
        String[] faultDetectionResultValueNames = getFaultDetectionResultValueNames();
        BaseBundle bundle = new Bundle();
        bundle.putInt(faultDetectionResultValueNames[0], ContextList.getInstance().getServiceOrdinal(getContextType()));
        bundle.putInt(faultDetectionResultValueNames[1], i);
        bundle.putString(faultDetectionResultValueNames[2], str);
        return bundle;
    }

    public final String[] getFaultDetectionResultValueNames() {
        return new String[]{"Service", "CheckResult", "Cause"};
    }

    protected final Looper getLooper() {
        return this.mLooper;
    }

    protected abstract IApPowerObserver getPowerObserver();

    protected abstract ISensorHubResetObserver getPowerResetObserver();

    protected ITimeChangeObserver getTimeChangeObserver() {
        return null;
    }

    protected final IContextTimeOutCheck getTimeOutCheckManager() {
        return this.mTimeOutCheck;
    }

    protected ITimeOutCheckObserver getTimeOutCheckObserver() {
        return null;
    }

    public final long getTimeStampForApStatus() {
        return this.mTimeStampForApStatus;
    }

    protected final int getVersion() {
        return this.mVersion;
    }

    public final void initializePreparedSubCollection() {
        this.mPreparedCollection = 0;
    }

    protected final boolean isDisable() {
        return getUsedTotalCount() < 1;
    }

    protected final boolean isEnable() {
        return getUsedTotalCount() <= 1;
    }

    public final void notifyCmdProcessResultObserver(String str, Bundle bundle) {
        if (bundle == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_CONTEXT_NULL_EXCEPTION.getCode()));
            return;
        }
        CaLogger.info("CheckResult = " + Integer.toString(bundle.getInt("CheckResult")) + ", Cause = " + bundle.getString("Cause"));
        super.notifyCmdProcessResultObserver(str, bundle);
    }

    protected final void notifyFaultDetectionResult() {
        Bundle faultDetectionResult = getFaultDetectionResult();
        if (faultDetectionResult == null) {
            CaLogger.error("Fault Detection Result is null.");
        } else {
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), faultDetectionResult);
        }
    }

    public void onTimeChanged() {
        disable();
        enable();
    }

    protected void processApPowerStatus() {
        if (this.mApStatus == -46) {
            updateApSleep();
        } else if (this.mApStatus == -47) {
            updateApWakeup();
        }
    }

    protected final void registerApPowerObserver() {
        if (this.mSensorHubResetObservable != null) {
            this.mSensorHubResetObservable.registerSensorHubResetObserver(getPowerResetObserver());
        }
        CaPowerManager.getInstance().registerApPowerObserver(getPowerObserver());
    }

    public final void setAPStatus(int i) {
        this.mApStatus = i;
    }

    protected final void setVersion(int i) {
        this.mVersion = i;
    }

    public void start(Listener listener, int i) {
        CaLogger.trace();
        if (isEnable()) {
            initialize();
            clear();
            enableForStart(i);
            registerApPowerObserver();
            if (getTimeChangeObserver() != null) {
                CaTimeChangeManager.getInstance().registerObserver(getTimeChangeObserver());
            }
        }
        if (i == 1) {
            notifyFaultDetectionResult();
        }
    }

    public void stop(Listener listener, int i) {
        CaLogger.trace();
        if (isDisable()) {
            clear();
            unregisterApPowerObserver();
            disableForStop(i);
            terminate();
            if (getTimeChangeObserver() != null) {
                CaTimeChangeManager.getInstance().unregisterObserver(getTimeChangeObserver());
            }
        }
        if (i == 1) {
            notifyFaultDetectionResult();
        }
    }

    protected final void unregisterApPowerObserver() {
        if (this.mSensorHubResetObservable != null) {
            this.mSensorHubResetObservable.unregisterSensorHubResetObserver(getPowerResetObserver());
        }
        CaPowerManager.getInstance().unregisterApPowerObserver(getPowerObserver());
    }

    public void updateApPowerStatus(int i, long j) {
        this.mApStatus = i;
        this.mTimeStampForApStatus = j;
        if (getUsedSubCollectionCount() <= 0 || getUsedSubCollectionCount() <= this.mPreparedCollection) {
            processApPowerStatus();
        }
    }

    public final void updateApPowerStatusForPreparedCollection() {
        this.mPreparedCollection++;
        if (getUsedSubCollectionCount() <= this.mPreparedCollection) {
            processApPowerStatus();
        }
    }

    protected void updateApReset() {
        reset();
        this.mApStatus = 0;
    }

    protected void updateApSleep() {
        pause();
        this.mApStatus = 0;
    }

    protected void updateApWakeup() {
        resume();
        this.mApStatus = 0;
    }

    public void updateSensorHubResetStatus(int i) {
        if (i == -43) {
            reset();
        }
    }
}
