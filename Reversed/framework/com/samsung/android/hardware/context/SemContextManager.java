package com.samsung.android.hardware.context;

import android.annotation.SuppressLint;
import android.os.BaseBundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.samsung.android.hardware.context.ISemContextService.Stub;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SemContextManager {
    private static final String TAG = "SemContextManager";
    @SuppressLint({"UseSparseArrays"})
    private HashMap<Integer, Integer> mAvailableServiceMap = null;
    private String mClientInfo = "";
    private SemContextListener mIsHistoryDataListener = null;
    private final CopyOnWriteArrayList<SemContextListenerDelegate> mListenerDelegates = new CopyOnWriteArrayList();
    private final Looper mMainLooper;
    private final ISemContextService mSemContextService = Stub.asInterface(ServiceManager.getService("scontext"));

    @SuppressLint({"HandlerLeak"})
    private class SemContextListenerDelegate extends ISemContextCallback.Stub {
        private final String mClientInfo;
        private final Handler mHandler;
        private final boolean mIsHistoryData;
        private final SemContextListener mListener;

        SemContextListenerDelegate(SemContextListener semContextListener, Looper looper, boolean z, String str) {
            this.mListener = semContextListener;
            Looper -get1 = looper != null ? looper : SemContextManager.this.mMainLooper;
            this.mIsHistoryData = z;
            this.mClientInfo = str;
            this.mHandler = new Handler(-get1) {
                public void handleMessage(Message message) {
                    if (SemContextListenerDelegate.this.mListener != null) {
                        SemContextEvent semContextEvent = (SemContextEvent) message.obj;
                        if (semContextEvent != null) {
                            SemContext semContext = semContextEvent.semContext;
                            if (semContext != null) {
                                int type = semContext.getType();
                                if (SemContextListenerDelegate.this.mIsHistoryData) {
                                    Log.d(SemContextManager.TAG, "Data is received so remove listener related HistoryData");
                                    SemContextListenerDelegate.this.mListener.onSemContextChanged(semContextEvent);
                                    SemContextManager.this.unregisterListener(SemContextListenerDelegate.this.mListener, type);
                                } else if (!SemContextManager.this.checkHistoryMode(semContextEvent)) {
                                    SemContextListenerDelegate.this.mListener.onSemContextChanged(semContextEvent);
                                } else if (SemContextManager.this.mIsHistoryDataListener != null && SemContextManager.this.mIsHistoryDataListener.equals(SemContextListenerDelegate.this.mListener)) {
                                    Log.d(SemContextManager.TAG, "Listener is already registered and history data is sent to Application");
                                    SemContextManager.this.mIsHistoryDataListener.onSemContextChanged(semContextEvent);
                                }
                            }
                        }
                    }
                }
            };
        }

        public SemContextListener getListener() {
            return this.mListener;
        }

        public String getListenerInfo() throws RemoteException {
            StringBuilder stringBuilder = new StringBuilder();
            if ("".equals(this.mClientInfo)) {
                stringBuilder.append(this.mListener.toString());
            } else {
                stringBuilder.append(this.mClientInfo);
            }
            return stringBuilder.toString();
        }

        public synchronized void semContextCallback(SemContextEvent semContextEvent) throws RemoteException {
            Message obtain = Message.obtain();
            obtain.what = 0;
            obtain.obj = semContextEvent;
            this.mHandler.sendMessage(obtain);
            notifyAll();
        }
    }

    public SemContextManager(Looper looper) {
        this.mMainLooper = looper;
    }

    private SemContextAttribute addListenerAttribute(int i) {
        switch (i) {
            case 1:
                return new SemContextApproachAttribute();
            case 2:
                return new SemContextPedometerAttribute();
            case 3:
                return new SemContextStepCountAlertAttribute();
            case 6:
                return new SemContextAutoRotationAttribute();
            case 12:
                return new SemContextShakeMotionAttribute();
            case 24:
                return new SemContextActivityLocationLoggingAttribute();
            case 27:
                return new SemContextActivityNotificationAttribute();
            case 28:
                return new SemContextSpecificPoseAlertAttribute();
            case 30:
                return new SemContextActivityNotificationExAttribute();
            case 33:
                return new SemContextStepLevelMonitorAttribute();
            case 35:
                return new SemContextSedentaryTimerAttribute();
            case 36:
                return new SemContextFlatMotionForTableModeAttribute();
            case 39:
                return new SemContextAutoBrightnessAttribute();
            case 47:
                return new SemContextSLocationCoreAttribute();
            case 48:
                return new SemContextInterruptedGyroAttribute();
            case 51:
                return new SemContextCarryingDetectionAttribute();
            case 53:
                return new SemContextActivityCalibrationAttribute();
            case 54:
                return new SemContextLocationChangeTriggerAttribute();
            default:
                return new SemContextAttribute();
        }
    }

    private boolean checkHistoryMode(SemContextEvent semContextEvent) {
        boolean z = true;
        Boolean valueOf = Boolean.valueOf(false);
        StringBuilder stringBuilder = new StringBuilder();
        int type = semContextEvent.semContext.getType();
        stringBuilder.append("onSemContextChanged() : event = ").append(SemContext.getServiceName(type));
        switch (type) {
            case 2:
                if (semContextEvent.getPedometerContext().getMode() != 2) {
                    z = false;
                }
                valueOf = Boolean.valueOf(z);
                break;
            case 6:
                stringBuilder.append(" Angle : ").append(semContextEvent.getAutoRotationContext().getAngle());
                break;
            case 26:
                if (semContextEvent.getActivityBatchContext().getMode() != 1) {
                    z = false;
                }
                valueOf = Boolean.valueOf(z);
                break;
            case 33:
                if (semContextEvent.getStepLevelMonitorContext().getMode() != 1) {
                    z = false;
                }
                valueOf = Boolean.valueOf(z);
                break;
        }
        Log.d(TAG, stringBuilder.toString());
        return valueOf.booleanValue();
    }

    private boolean checkListenerAndService(SemContextListener semContextListener, int i) {
        if (semContextListener != null) {
            return isAvailableService(i);
        }
        Log.d(TAG, "Listener is null!");
        return false;
    }

    private HashMap<Integer, Integer> getAvailableServiceMap() {
        HashMap<Integer, Integer> hashMap = null;
        try {
            return (HashMap) this.mSemContextService.getAvailableServiceMap();
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException in getAvailableServiceMap: ", e);
            return hashMap;
        }
    }

    private SemContextListenerDelegate getListenerDelegate(SemContextListener semContextListener) {
        if (semContextListener == null || this.mListenerDelegates.isEmpty()) {
            return null;
        }
        SemContextListenerDelegate semContextListenerDelegate = null;
        for (SemContextListenerDelegate semContextListenerDelegate2 : this.mListenerDelegates) {
            if (semContextListenerDelegate2.getListener().equals(semContextListener)) {
                semContextListenerDelegate = semContextListenerDelegate2;
                break;
            }
        }
        return semContextListenerDelegate;
    }

    private void initializeClientInfo() {
        this.mClientInfo = "";
    }

    @Deprecated
    public boolean changeParameters(SemContextListener semContextListener, int i, int i2) {
        SemContextAttribute semContextAttribute = null;
        if (i == 2) {
            semContextAttribute = new SemContextPedometerAttribute(i2);
        } else if (i == 33) {
            semContextAttribute = new SemContextStepLevelMonitorAttribute(i2);
        }
        return changeParameters(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean changeParameters(SemContextListener semContextListener, int i, int i2, double d, double d2) {
        SemContextAttribute semContextAttribute = null;
        if (i == 2) {
            semContextAttribute = new SemContextPedometerAttribute(i2, d, d2);
        }
        return changeParameters(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean changeParameters(SemContextListener semContextListener, int i, int i2, int i3, int i4, int i5) {
        SemContextAttribute semContextAttribute = null;
        if (i == 35) {
            semContextAttribute = new SemContextSedentaryTimerAttribute(1, i2, i3, i4, i5);
        }
        return changeParameters(semContextListener, i, semContextAttribute);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean changeParameters(com.samsung.android.hardware.context.SemContextListener r7, int r8, com.samsung.android.hardware.context.SemContextAttribute r9) {
        /*
        r6 = this;
        r5 = 1;
        r4 = 0;
        if (r9 == 0) goto L_0x0011;
    L_0x0004:
        r2 = r9.checkAttribute();
        if (r2 == 0) goto L_0x0011;
    L_0x000a:
        r2 = r6.checkListenerAndService(r7, r8);
        if (r2 != 0) goto L_0x0012;
    L_0x0010:
        return r4;
    L_0x0011:
        return r4;
    L_0x0012:
        if (r8 == r5) goto L_0x0017;
    L_0x0014:
        r2 = 2;
        if (r8 != r2) goto L_0x0027;
    L_0x0017:
        r1 = r6.getListenerDelegate(r7);
        if (r1 != 0) goto L_0x0044;
    L_0x001d:
        r2 = "SemContextManager";
        r3 = "  .changeParameters : SemContextListener is null!";
        android.util.Log.e(r2, r3);
        return r4;
    L_0x0027:
        r2 = 33;
        if (r8 == r2) goto L_0x0017;
    L_0x002b:
        r2 = 35;
        if (r8 == r2) goto L_0x0017;
    L_0x002f:
        r2 = 39;
        if (r8 == r2) goto L_0x0017;
    L_0x0033:
        r2 = 47;
        if (r8 == r2) goto L_0x0017;
    L_0x0037:
        r2 = 51;
        if (r8 == r2) goto L_0x0017;
    L_0x003b:
        r2 = 53;
        if (r8 == r2) goto L_0x0017;
    L_0x003f:
        r2 = 54;
        if (r8 == r2) goto L_0x0017;
    L_0x0043:
        return r4;
    L_0x0044:
        r2 = r6.mSemContextService;	 Catch:{ RemoteException -> 0x0076 }
        r2 = r2.changeParameters(r1, r8, r9);	 Catch:{ RemoteException -> 0x0076 }
        if (r2 == 0) goto L_0x0075;
    L_0x004c:
        r2 = "SemContextManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0076 }
        r3.<init>();	 Catch:{ RemoteException -> 0x0076 }
        r4 = "  .changeParameters : listener = ";
        r3 = r3.append(r4);	 Catch:{ RemoteException -> 0x0076 }
        r3 = r3.append(r7);	 Catch:{ RemoteException -> 0x0076 }
        r4 = ", service=";
        r3 = r3.append(r4);	 Catch:{ RemoteException -> 0x0076 }
        r4 = com.samsung.android.hardware.context.SemContext.getServiceName(r8);	 Catch:{ RemoteException -> 0x0076 }
        r3 = r3.append(r4);	 Catch:{ RemoteException -> 0x0076 }
        r3 = r3.toString();	 Catch:{ RemoteException -> 0x0076 }
        android.util.Log.d(r2, r3);	 Catch:{ RemoteException -> 0x0076 }
    L_0x0075:
        return r5;
    L_0x0076:
        r0 = move-exception;
        r2 = "SemContextManager";
        r3 = "RemoteException in changeParameters: ";
        android.util.Log.e(r2, r3, r0);
        goto L_0x0075;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.hardware.context.SemContextManager.changeParameters(com.samsung.android.hardware.context.SemContextListener, int, com.samsung.android.hardware.context.SemContextAttribute):boolean");
    }

    public int getFeatureLevel(int i) {
        return isAvailableService(i) ? ((Integer) this.mAvailableServiceMap.get(Integer.valueOf(i))).intValue() : 0;
    }

    public void initializeSemContextService(SemContextListener semContextListener, int i) {
        if (isAvailableService(i) && i == 3) {
            IBinder listenerDelegate = getListenerDelegate(semContextListener);
            if (listenerDelegate == null) {
                Log.e(TAG, "  .initializeSemContextService : SemContextListener is null!");
                return;
            }
            try {
                this.mSemContextService.initializeService(listenerDelegate, i);
                Log.d(TAG, "  .initializeSemContextService : listener = " + semContextListener + ", service=" + SemContext.getServiceName(i));
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in initializeSemContextService: ", e);
            }
        }
    }

    public boolean isAvailableService(int i) {
        boolean z = false;
        if (i == -1) {
            return true;
        }
        if (this.mAvailableServiceMap == null) {
            this.mAvailableServiceMap = getAvailableServiceMap();
        }
        if (this.mAvailableServiceMap != null) {
            z = this.mAvailableServiceMap.containsKey(Integer.valueOf(i));
            if (i == 47 && "BCM4773_SLOCATION_CORE".equals(SystemProperties.get("ro.gps.chip.vendor.slocation"))) {
                z = false;
            }
        }
        return z;
    }

    public boolean registerListener(SemContextListener semContextListener, int i) {
        return registerListener(semContextListener, i, addListenerAttribute(i));
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int i2) {
        SemContextAttribute semContextAttribute = null;
        if (i == 3) {
            semContextAttribute = new SemContextStepCountAlertAttribute(i2);
        } else if (i == 6) {
            semContextAttribute = new SemContextAutoRotationAttribute(i2);
        } else if (i == 16) {
            semContextAttribute = new SemContextWakeUpVoiceAttribute(i2);
        } else if (i == 33) {
            semContextAttribute = new SemContextStepLevelMonitorAttribute(i2);
        } else if (i == 36) {
            semContextAttribute = new SemContextFlatMotionForTableModeAttribute(i2);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int i2, double d, double d2) {
        SemContextAttribute semContextAttribute = null;
        if (i == 2) {
            semContextAttribute = new SemContextPedometerAttribute(i2, d, d2);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int i2, int i3) {
        SemContextAttribute semContextAttribute = null;
        if (i == 12) {
            semContextAttribute = new SemContextShakeMotionAttribute(i2, i3);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int i2, int i3, int i4) {
        SemContextAttribute semContextAttribute = null;
        if (i == 35) {
            semContextAttribute = new SemContextSedentaryTimerAttribute(i2, i3, i4, 1500, 1500);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int i2, int i3, int i4, int i5) {
        SemContextAttribute semContextAttribute = null;
        if (i == 28) {
            semContextAttribute = new SemContextSpecificPoseAlertAttribute(i2, i3, i4, i5);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int i2, int i3, int i4, int i5, int i6) {
        SemContextAttribute semContextAttribute = null;
        if (i == 24) {
            semContextAttribute = new SemContextActivityLocationLoggingAttribute(i2, i3, i4, i5, i6);
        } else if (i == 35) {
            semContextAttribute = new SemContextSedentaryTimerAttribute(i2, i3, i4, i5, i6);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    public boolean registerListener(SemContextListener semContextListener, int i, Looper looper) {
        return registerListener(semContextListener, i, addListenerAttribute(i), looper);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean registerListener(com.samsung.android.hardware.context.SemContextListener r8, int r9, com.samsung.android.hardware.context.SemContextAttribute r10) {
        /*
        r7 = this;
        r3 = 0;
        r4 = 0;
        r1 = 48;
        if (r9 != r1) goto L_0x000b;
    L_0x0006:
        r1 = r7.setReferenceData(r9, r10);
        return r1;
    L_0x000b:
        if (r10 == 0) goto L_0x001a;
    L_0x000d:
        r1 = r10.checkAttribute();
        if (r1 == 0) goto L_0x001a;
    L_0x0013:
        r1 = r7.checkListenerAndService(r8, r9);
        if (r1 != 0) goto L_0x001b;
    L_0x0019:
        return r4;
    L_0x001a:
        return r4;
    L_0x001b:
        r0 = r7.getListenerDelegate(r8);
        if (r0 != 0) goto L_0x0032;
    L_0x0021:
        r0 = new com.samsung.android.hardware.context.SemContextManager$SemContextListenerDelegate;
        r5 = r7.mClientInfo;
        r1 = r7;
        r2 = r8;
        r0.<init>(r2, r3, r4, r5);
        r1 = r7.mListenerDelegates;
        r1.add(r0);
        r7.initializeClientInfo();
    L_0x0032:
        r1 = r7.mSemContextService;	 Catch:{ RemoteException -> 0x0062 }
        r1.registerCallback(r0, r9, r10);	 Catch:{ RemoteException -> 0x0062 }
        r1 = "SemContextManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0062 }
        r2.<init>();	 Catch:{ RemoteException -> 0x0062 }
        r3 = "  .registerListener : listener = ";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0062 }
        r2 = r2.append(r8);	 Catch:{ RemoteException -> 0x0062 }
        r3 = ", service=";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0062 }
        r3 = com.samsung.android.hardware.context.SemContext.getServiceName(r9);	 Catch:{ RemoteException -> 0x0062 }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0062 }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x0062 }
        android.util.Log.d(r1, r2);	 Catch:{ RemoteException -> 0x0062 }
    L_0x0060:
        r1 = 1;
        return r1;
    L_0x0062:
        r6 = move-exception;
        r1 = "SemContextManager";
        r2 = "RemoteException in registerListener: ";
        android.util.Log.e(r1, r2, r6);
        goto L_0x0060;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.hardware.context.SemContextManager.registerListener(com.samsung.android.hardware.context.SemContextListener, int, com.samsung.android.hardware.context.SemContextAttribute):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean registerListener(com.samsung.android.hardware.context.SemContextListener r8, int r9, com.samsung.android.hardware.context.SemContextAttribute r10, android.os.Looper r11) {
        /*
        r7 = this;
        r4 = 0;
        if (r10 == 0) goto L_0x0010;
    L_0x0003:
        r1 = r10.checkAttribute();
        if (r1 == 0) goto L_0x0010;
    L_0x0009:
        r1 = r7.checkListenerAndService(r8, r9);
        if (r1 != 0) goto L_0x0011;
    L_0x000f:
        return r4;
    L_0x0010:
        return r4;
    L_0x0011:
        r0 = r7.getListenerDelegate(r8);
        if (r0 != 0) goto L_0x0029;
    L_0x0017:
        r0 = new com.samsung.android.hardware.context.SemContextManager$SemContextListenerDelegate;
        r5 = r7.mClientInfo;
        r1 = r7;
        r2 = r8;
        r3 = r11;
        r0.<init>(r2, r3, r4, r5);
        r1 = r7.mListenerDelegates;
        r1.add(r0);
        r7.initializeClientInfo();
    L_0x0029:
        r1 = r7.mSemContextService;	 Catch:{ RemoteException -> 0x0059 }
        r1.registerCallback(r0, r9, r10);	 Catch:{ RemoteException -> 0x0059 }
        r1 = "SemContextManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0059 }
        r2.<init>();	 Catch:{ RemoteException -> 0x0059 }
        r3 = "  .registerListener : listener = ";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0059 }
        r2 = r2.append(r8);	 Catch:{ RemoteException -> 0x0059 }
        r3 = ", service=";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0059 }
        r3 = com.samsung.android.hardware.context.SemContext.getServiceName(r9);	 Catch:{ RemoteException -> 0x0059 }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0059 }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x0059 }
        android.util.Log.d(r1, r2);	 Catch:{ RemoteException -> 0x0059 }
    L_0x0057:
        r1 = 1;
        return r1;
    L_0x0059:
        r6 = move-exception;
        r1 = "SemContextManager";
        r2 = "RemoteException in registerListener: ";
        android.util.Log.e(r1, r2, r6);
        goto L_0x0057;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.hardware.context.SemContextManager.registerListener(com.samsung.android.hardware.context.SemContextListener, int, com.samsung.android.hardware.context.SemContextAttribute, android.os.Looper):boolean");
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int[] iArr) {
        SemContextAttribute semContextAttribute = null;
        if (i == 27) {
            semContextAttribute = new SemContextActivityNotificationAttribute(iArr);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    @Deprecated
    public boolean registerListener(SemContextListener semContextListener, int i, int[] iArr, int i2) {
        SemContextAttribute semContextAttribute = null;
        if (i == 30) {
            semContextAttribute = new SemContextActivityNotificationExAttribute(iArr, i2);
        }
        return registerListener(semContextListener, i, semContextAttribute);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void requestHistoryData(com.samsung.android.hardware.context.SemContextListener r9, int r10) {
        /*
        r8 = this;
        r3 = 0;
        r1 = r8.isAvailableService(r10);
        if (r1 != 0) goto L_0x0008;
    L_0x0007:
        return;
    L_0x0008:
        r1 = 2;
        if (r10 == r1) goto L_0x001d;
    L_0x000b:
        r1 = 33;
        if (r10 == r1) goto L_0x001d;
    L_0x000f:
        r1 = 26;
        if (r10 == r1) goto L_0x001d;
    L_0x0013:
        r1 = "SemContextManager";
        r2 = "  .requestHistoryData : This service is not supported!";
        android.util.Log.e(r1, r2);
        return;
    L_0x001d:
        r6 = r8.addListenerAttribute(r10);
        if (r6 == 0) goto L_0x0030;
    L_0x0023:
        r1 = r6.checkAttribute();
        if (r1 == 0) goto L_0x0030;
    L_0x0029:
        r1 = r8.checkListenerAndService(r9, r10);
        if (r1 != 0) goto L_0x0031;
    L_0x002f:
        return;
    L_0x0030:
        return;
    L_0x0031:
        r0 = r8.getListenerDelegate(r9);
        r8.mIsHistoryDataListener = r9;
        if (r0 != 0) goto L_0x0079;
    L_0x0039:
        r0 = new com.samsung.android.hardware.context.SemContextManager$SemContextListenerDelegate;
        r5 = r8.mClientInfo;
        r4 = 1;
        r1 = r8;
        r2 = r9;
        r0.<init>(r2, r3, r4, r5);
        r1 = r8.mListenerDelegates;
        r1.add(r0);
        r8.initializeClientInfo();
        r1 = r8.mSemContextService;	 Catch:{ RemoteException -> 0x00a8 }
        r1.registerCallback(r0, r10, r6);	 Catch:{ RemoteException -> 0x00a8 }
        r1 = "SemContextManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x00a8 }
        r2.<init>();	 Catch:{ RemoteException -> 0x00a8 }
        r3 = "  .registerListener : listener = ";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00a8 }
        r2 = r2.append(r9);	 Catch:{ RemoteException -> 0x00a8 }
        r3 = ", service=";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00a8 }
        r3 = com.samsung.android.hardware.context.SemContext.getServiceName(r10);	 Catch:{ RemoteException -> 0x00a8 }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00a8 }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x00a8 }
        android.util.Log.d(r1, r2);	 Catch:{ RemoteException -> 0x00a8 }
    L_0x0079:
        r1 = r8.mSemContextService;	 Catch:{ RemoteException -> 0x00b3 }
        r1.requestHistoryData(r0, r10);	 Catch:{ RemoteException -> 0x00b3 }
        r1 = "SemContextManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x00b3 }
        r2.<init>();	 Catch:{ RemoteException -> 0x00b3 }
        r3 = "  .requestHistoryData : listener = ";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00b3 }
        r2 = r2.append(r9);	 Catch:{ RemoteException -> 0x00b3 }
        r3 = ", service=";
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00b3 }
        r3 = com.samsung.android.hardware.context.SemContext.getServiceName(r10);	 Catch:{ RemoteException -> 0x00b3 }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x00b3 }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x00b3 }
        android.util.Log.d(r1, r2);	 Catch:{ RemoteException -> 0x00b3 }
    L_0x00a7:
        return;
    L_0x00a8:
        r7 = move-exception;
        r1 = "SemContextManager";
        r2 = "RemoteException in registerListener: ";
        android.util.Log.e(r1, r2, r7);
        goto L_0x0079;
    L_0x00b3:
        r7 = move-exception;
        r1 = "SemContextManager";
        r2 = "RemoteException in requestHistoryData: ";
        android.util.Log.e(r1, r2, r7);
        goto L_0x00a7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.hardware.context.SemContextManager.requestHistoryData(com.samsung.android.hardware.context.SemContextListener, int):void");
    }

    public void requestToUpdate(SemContextListener semContextListener, int i) {
        if (!isAvailableService(i)) {
            return;
        }
        if (i == 2 || i == 25 || i == 26 || i == 50 || i == 51 || i == 52 || i == 54) {
            IBinder listenerDelegate = getListenerDelegate(semContextListener);
            if (listenerDelegate == null) {
                Log.e(TAG, "  .requestToUpdate : SemContextListener is null!");
                return;
            }
            try {
                this.mSemContextService.requestToUpdate(listenerDelegate, i);
                Log.d(TAG, "  .requestToUpdate : listener = " + semContextListener + ", service=" + SemContext.getServiceName(i));
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in requestToUpdate: ", e);
            }
            return;
        }
        Log.e(TAG, "  .requestToUpdate : This service is not supported!");
    }

    public void setClientInfo(String str) {
        this.mClientInfo = str;
    }

    public boolean setReferenceData(int i, SemContextAttribute semContextAttribute) {
        if (this.mSemContextService == null || semContextAttribute == null) {
            return false;
        }
        boolean z = false;
        BaseBundle attribute = i == 48 ? semContextAttribute.getAttribute(48) : semContextAttribute.getAttribute(i);
        if (attribute == null) {
            return false;
        }
        switch (i) {
            case 16:
                try {
                    if (attribute.containsKey("net_data") && attribute.containsKey("gram_data")) {
                        byte[] byteArray = attribute.getByteArray("net_data");
                        byte[] byteArray2 = attribute.getByteArray("gram_data");
                        if (byteArray != null && byteArray2 != null) {
                            z = this.mSemContextService.setReferenceData(1, byteArray) ? this.mSemContextService.setReferenceData(2, byteArray2) : false;
                            break;
                        }
                        return false;
                    }
                    return false;
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in initializeSemContextService: ", e);
                    break;
                }
                break;
            case 39:
                if (attribute.containsKey("luminance_config_data")) {
                    byte[] byteArray3 = attribute.getByteArray("luminance_config_data");
                    if (byteArray3 != null) {
                        z = this.mSemContextService.setReferenceData(0, byteArray3);
                        break;
                    }
                    return false;
                }
                return false;
            case 43:
                if (attribute.containsKey("display_status")) {
                    byte[] bArr = new byte[]{(byte) attribute.getInt("display_status")};
                    Log.d(TAG, "Hall Sensor Data : " + String.valueOf(bArr[0]));
                    z = this.mSemContextService.setReferenceData(43, bArr);
                    break;
                }
                Log.d(TAG, "Bundle is not contained key data");
                return false;
            case 48:
                if (attribute.containsKey("interrupt_gyro")) {
                    byte[] bArr2 = new byte[]{(byte) attribute.getInt("interrupt_gyro")};
                    Log.d(TAG, "sysfs data : " + String.valueOf(bArr2[0]));
                    z = this.mSemContextService.setReferenceData(48, bArr2);
                    break;
                }
                Log.d(TAG, "Bundle is not contained key data");
                return false;
        }
        return z;
    }

    @Deprecated
    public boolean setReferenceData(int i, byte[] bArr, byte[] bArr2) {
        SemContextAttribute semContextAttribute = null;
        if (bArr == null || bArr2 == null) {
            return false;
        }
        if (i == 16) {
            semContextAttribute = new SemContextWakeUpVoiceAttribute(bArr, bArr2);
        }
        return setReferenceData(i, semContextAttribute);
    }

    public void unregisterListener(SemContextListener semContextListener) {
        unregisterListener(semContextListener, -1);
    }

    public void unregisterListener(SemContextListener semContextListener, int i) {
        if (checkListenerAndService(semContextListener, i)) {
            IBinder listenerDelegate = getListenerDelegate(semContextListener);
            if (listenerDelegate == null) {
                Log.e(TAG, "  .unregisterListener : SemContextListener is null!");
                return;
            }
            try {
                if (this.mSemContextService.unregisterCallback(listenerDelegate, i)) {
                    this.mListenerDelegates.remove(listenerDelegate);
                }
                Log.d(TAG, "  .unregisterListener : listener = " + semContextListener + ", service=" + SemContext.getServiceName(i));
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in unregisterListener: ", e);
            }
        }
    }
}
