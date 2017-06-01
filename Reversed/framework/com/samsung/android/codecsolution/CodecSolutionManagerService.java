package com.samsung.android.codecsolution;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IActivityManager;
import android.app.IProcessObserver;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import android.view.WindowManager;
import com.samsung.android.codecsolution.ICodecSolutionManagerService.Stub;
import com.samsung.android.desktopmode.SemDesktopModeManager;
import com.samsung.android.smartfitting.ISmartFittingService;
import java.util.ArrayList;
import java.util.List;

public class CodecSolutionManagerService extends Stub {
    private static final String CLASS_MHDR_META_SERVICE = "com.samsung.android.mhdrservice.MhdrMetaService";
    private static final String CLASS_MHDR_PARAM_SERVICE = "com.samsung.android.mhdrservice.MhdrParamService";
    private static final String CLASS_MHDR_SERVICE = "com.samsung.android.mhdrservice.MhdrService";
    private static final String CLASS_SMARTFIT_SERVICE = "com.samsung.android.smartfitting.SmartFittingService";
    private static final int HANDLER_MSG_LOGGING_EVENT = 900;
    private static final int HANDLER_MSG_SEND_BROADCAST = 800;
    private static final int HANDLER_MSG_SET_META_MHDR_SERVICE = 103;
    private static final int HANDLER_MSG_SET_PARAM_MHDR_SERVICE = 102;
    private static final int HANDLER_MSG_START_MHDR_SERVICE = 100;
    private static final int HANDLER_MSG_STOP_MHDR_SERVICE = 101;
    private static final String INTENT_SMARTFIT_FOUND_BLACK_BAR = "com.samsung.intent.action.FOUND_BLACK_BAR";
    private static final String INTENT_SMARTFIT_HIDE_BUTTON = "com.samsung.intent.action.HIDE_BUTTON";
    private static final String INTENT_SMARTFIT_SHOW_BUTTON = "com.samsung.intent.action.SHOW_BUTTON";
    private static final String PACKAGE_MHDR_SERVICE = "com.samsung.android.mhdrservice";
    private static final String PACKAGE_SMARTFIT_SERVICE = "com.samsung.android.smartfitting";
    private static final String TAG = "CodecSolution";
    private static final int TOP_IS_IN_WHITELIST = 1;
    private static final int TOP_IS_NOT_IN_WHITELIST = 0;
    private static final int TOP_IS_SAMSUNG_VIDEO_APP = 2;
    public static final String VERSION = "1.1";
    private ActivityManager mActivityManager;
    private IActivityManager mActivityManagerService;
    private Context mContext;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private int mIsBlackbar;
    private boolean mIsBootCompleted;
    private CSProcessObserver mProcessObserver = new CSProcessObserver();
    private int mSecVideoUseSmartFitting;
    private ServiceConnection mSmartFittingConnection = new C10161();
    private int mSmartFittingMode;
    private ISmartFittingService mSmartFittingServiceBinder = null;
    private String mTopActivityName;
    private WindowManager mWindowManager;

    class C10161 implements ServiceConnection {
        private static final String TAG = "SmartFittingConnection";

        C10161() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.m29d(TAG, "onServiceConnected");
            CodecSolutionManagerService.this.mSmartFittingServiceBinder = ISmartFittingService.Stub.asInterface(iBinder);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.m29d(TAG, "onServiceDisconnected");
            CodecSolutionManagerService.this.mSmartFittingServiceBinder = null;
        }
    }

    class CSProcessObserver extends IProcessObserver.Stub {
        CSProcessObserver() {
        }

        public void onForegroundActivitiesChanged(int i, int i2, boolean z) {
            updateTopProcessName();
        }

        public void onProcessDied(int i, int i2) {
        }

        public void onProcessStateChanged(int i, int i2, int i3) throws RemoteException {
        }

        public void updateTopProcessName() {
            List runningTasks = CodecSolutionManagerService.this.mActivityManager.getRunningTasks(1);
            if (runningTasks == null) {
                Log.m37w(CodecSolutionManagerService.TAG, "runningTaskInfoList is null.");
                CodecSolutionManagerService.this.mTopActivityName = null;
            } else if (runningTasks.isEmpty()) {
                Log.m37w(CodecSolutionManagerService.TAG, "runningTaskInfoList is empty.");
                CodecSolutionManagerService.this.mTopActivityName = null;
            } else {
                RunningTaskInfo runningTaskInfo = (RunningTaskInfo) runningTasks.get(0);
                if (runningTaskInfo == null) {
                    Log.m37w(CodecSolutionManagerService.TAG, "runningTaskInfo is null.");
                    CodecSolutionManagerService.this.mTopActivityName = null;
                    return;
                }
                String packageName = runningTaskInfo.topActivity.getPackageName();
                if (packageName == null) {
                    Log.m37w(CodecSolutionManagerService.TAG, "topName is null.");
                    CodecSolutionManagerService.this.mTopActivityName = null;
                    return;
                }
                CodecSolutionManagerService.this.mTopActivityName = packageName;
            }
        }
    }

    public class CodecSolutionReceiver extends BroadcastReceiver {
        private static final String TAG = "SmartFittingReceiver";

        public void onReceive(Context context, Intent intent) {
            Log.m29d(TAG, "onReceive()");
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                Log.m29d(TAG, " : android.intent.action.BOOT_COMPLETED");
                CodecSolutionManagerService.this.mIsBootCompleted = true;
                try {
                    Log.m29d(TAG, " : registerProcessObserver()");
                    CodecSolutionManagerService.this.mActivityManagerService.registerProcessObserver(CodecSolutionManagerService.this.mProcessObserver);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public CodecSolutionManagerService(Context context, IActivityManager iActivityManager) {
        Log.m29d(TAG, "create");
        this.mContext = context;
        this.mActivityManager = (ActivityManager) this.mContext.getSystemService("activity");
        this.mActivityManagerService = iActivityManager;
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mSmartFittingMode = 1;
        this.mIsBlackbar = 0;
        this.mSecVideoUseSmartFitting = 0;
        this.mTopActivityName = null;
        this.mIsBootCompleted = false;
        context.registerReceiver(new CodecSolutionReceiver(), new IntentFilter("android.intent.action.BOOT_COMPLETED"));
        this.mHandlerThread = new HandlerThread("CodecSolutionHandler", 1);
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
            public void handleMessage(Message message) {
                Log.m29d(CodecSolutionManagerService.TAG, "handleMessage : " + message.what);
                Bundle data;
                Intent intent;
                switch (message.what) {
                    case 100:
                        data = message.getData();
                        intent = new Intent();
                        intent.setClassName(CodecSolutionManagerService.PACKAGE_MHDR_SERVICE, CodecSolutionManagerService.CLASS_MHDR_SERVICE);
                        intent.putExtras(data);
                        CodecSolutionManagerService.this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
                        return;
                    case 101:
                        intent = new Intent();
                        intent.setClassName(CodecSolutionManagerService.PACKAGE_MHDR_SERVICE, CodecSolutionManagerService.CLASS_MHDR_SERVICE);
                        CodecSolutionManagerService.this.mContext.stopServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
                        return;
                    case 102:
                        data = message.getData();
                        intent = new Intent();
                        intent.setClassName(CodecSolutionManagerService.PACKAGE_MHDR_SERVICE, CodecSolutionManagerService.CLASS_MHDR_PARAM_SERVICE);
                        intent.putExtras(data);
                        CodecSolutionManagerService.this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
                        return;
                    case 103:
                        data = message.getData();
                        intent = new Intent();
                        intent.setClassName(CodecSolutionManagerService.PACKAGE_MHDR_SERVICE, CodecSolutionManagerService.CLASS_MHDR_META_SERVICE);
                        intent.putExtras(data);
                        CodecSolutionManagerService.this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
                        return;
                    case 800:
                        String string = message.getData().getString("intent");
                        if (string == null) {
                            Log.m37w(CodecSolutionManagerService.TAG, "intent is null.");
                            return;
                        }
                        Log.m29d(CodecSolutionManagerService.TAG, "sendBroadcast " + string);
                        CodecSolutionManagerService.this.mContext.sendBroadcastAsUser(new Intent(string), UserHandle.ALL);
                        return;
                    case CodecSolutionManagerService.HANDLER_MSG_LOGGING_EVENT /*900*/:
                        if (CodecSolutionManagerService.this.mIsBootCompleted) {
                            MediaStatisticsEvent mediaStatisticsEvent = (MediaStatisticsEvent) message.obj;
                            Log.m29d(CodecSolutionManagerService.TAG, "event : " + mediaStatisticsEvent.getCategory());
                            Logging.insertLog(CodecSolutionManagerService.this.mContext, mediaStatisticsEvent.getCategory(), mediaStatisticsEvent.getLabel());
                            return;
                        }
                        Log.m29d(CodecSolutionManagerService.TAG, "ignore before boot completed");
                        return;
                    default:
                        return;
                }
            }
        };
        SystemProperties.set("secmm.codecsolution.ready", "1");
    }

    public int checkblackbarstatus() {
        Log.m29d(TAG, "checkblackbarstatus");
        return this.mIsBlackbar;
    }

    public void debug() {
        Log.m29d(TAG, "debug!!");
    }

    public int getSmartFittingMode() {
        return this.mSmartFittingMode;
    }

    public int getWhiteListStatus() {
        Log.m29d(TAG, "getWhiteListStatus");
        if (isDesktopMode()) {
            Log.m29d(TAG, "In Knox Desktop mode.");
            return 0;
        } else if (isSupportedRatio()) {
            String str = this.mTopActivityName;
            if (str == null) {
                Log.m37w(TAG, "Top is null.");
                return 0;
            }
            Log.m33i(TAG, "Top : " + str);
            if (!str.equals("com.samsung.android.video") && !str.equals("com.samsung.android.onlinevideo") && !str.equals("com.samsung.android.videolist")) {
                ArrayList packageInfo = new SCPMHelper(this.mContext).getPackageInfo(str);
                if (packageInfo == null) {
                    Log.m37w(TAG, "PackageInfo is null.");
                    return str.equals("com.google.android.youtube") ? 1 : 0;
                } else {
                    String str2 = (String) packageInfo.get(0);
                    String str3 = (String) packageInfo.get(1);
                    int intValue = ((Integer) packageInfo.get(2)).intValue();
                    Log.m29d(TAG, "c:" + str2 + " u:" + str3 + " t:" + intValue);
                    if (!str3.equals("yes")) {
                        return 0;
                    }
                    if (!str2.equals("sec")) {
                        return intValue + 1;
                    }
                    if (this.mSecVideoUseSmartFitting != 0) {
                        return 2;
                    }
                    Log.m29d(TAG, "SEC Video don't use SmartFitting.");
                    return 0;
                }
            } else if (this.mSecVideoUseSmartFitting != 0) {
                return 2;
            } else {
                Log.m29d(TAG, "SEC Video don't use SmartFitting.");
                return 0;
            }
        } else {
            Log.m29d(TAG, "Not supported ratio.");
            return 0;
        }
    }

    public void hideSmartFittingButton() {
        Log.m29d(TAG, "hideSmartFittingButton : sendBroadcast com.samsung.intent.action.HIDE_BUTTON");
        Message obtainMessage = this.mHandler.obtainMessage(800);
        BaseBundle bundle = new Bundle();
        bundle.putString("intent", INTENT_SMARTFIT_HIDE_BUTTON);
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    public boolean isDesktopMode() {
        Log.m29d(TAG, "isDesktopMode()");
        if (((SemDesktopModeManager) this.mContext.getSystemService("desktopmode")) == null) {
            Log.m37w(TAG, " : Can't get the DesktopModeManager.");
            return false;
        } else if (!SemDesktopModeManager.isDesktopMode()) {
            return false;
        } else {
            Log.m29d(TAG, " : In Knox Desktop mode.");
            return true;
        }
    }

    public boolean isSupportedRatio() {
        Log.m29d(TAG, "isSupportedRatio()");
        Point point = new Point();
        this.mWindowManager.getDefaultDisplay().getRealSize(point);
        if (point == null) {
            Log.m37w(TAG, "point is null.");
            return false;
        }
        int i;
        int i2;
        if (point.x > point.y) {
            i = point.x;
            i2 = point.y;
        } else {
            i = point.y;
            i2 = point.x;
        }
        Double valueOf = Double.valueOf(((double) i) / ((double) i2));
        Log.m29d(TAG, "a:" + i + " b:" + i2 + " r:" + valueOf);
        return valueOf.doubleValue() > 1.86d;
    }

    public void reportMediaStatisticsEvent(String str) {
        Log.m29d(TAG, "reportMediaStatisticsEvent: " + str);
        MediaStatisticsEvent mediaStatisticsEvent = new MediaStatisticsEvent(str);
        Message obtainMessage = this.mHandler.obtainMessage(HANDLER_MSG_LOGGING_EVENT);
        obtainMessage.obj = mediaStatisticsEvent;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void setMhdrEnable(int i) {
        boolean z = false;
        Log.m29d(TAG, "setMhdrEnable : " + i);
        Message obtainMessage = this.mHandler.obtainMessage(102);
        BaseBundle bundle = new Bundle();
        String str = "HDR-ENABLE";
        if (i != 0) {
            z = true;
        }
        bundle.putBoolean(str, z);
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    public void setMhdrMetaData(int i, int i2, int i3, int i4) {
        Log.m29d(TAG, "maxAvgLight : " + i);
        Log.m29d(TAG, "maxContentLight : " + i2);
        Log.m29d(TAG, "maxDispL : " + i3);
        Log.m29d(TAG, "minDispL : " + i4);
        Message obtainMessage = this.mHandler.obtainMessage(103);
        BaseBundle bundle = new Bundle();
        bundle.putInt("MaxAvgLight", i);
        bundle.putInt("MaxContentLight", i2);
        bundle.putInt("MaxDispL", i3);
        bundle.putInt("MinDispL", i4);
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    public void setSecVideoUseSmartFitting(int i) {
        Log.m29d(TAG, "setSecVideoUseSmartFitting(" + i + ")");
        this.mSecVideoUseSmartFitting = i;
    }

    public void setSmartFittingMode(int i) {
        Log.m29d(TAG, "setSmartFittingMode : " + i);
        this.mSmartFittingMode = i;
    }

    public void setWhiteListStatus(int i) {
        Log.m29d(TAG, "setWhiteListStatus : " + i);
    }

    public void showSmartFittingButton() {
        Log.m29d(TAG, "showSmartFittingButton : sendBroadcast com.samsung.intent.action.SHOW_BUTTON");
        Message obtainMessage = this.mHandler.obtainMessage(800);
        BaseBundle bundle = new Bundle();
        bundle.putString("intent", INTENT_SMARTFIT_SHOW_BUTTON);
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    public void startMhdrService(int i, String str, int i2) {
        boolean z = false;
        Log.m29d(TAG, "startMhdrService");
        Log.m29d(TAG, "pid = " + Binder.getCallingPid() + ", uid = " + Binder.getCallingUid());
        Log.m29d(TAG, "permission : " + (this.mContext.checkCallingOrSelfPermission("com.samsung.permission.USE_MHDR_SERVICE") == 0));
        Log.m29d(TAG, "calling uid : " + UserHandle.getUserId(Binder.getCallingUid()));
        Log.m29d(TAG, "my uid : " + UserHandle.semGetMyUserId());
        Message obtainMessage = this.mHandler.obtainMessage(100);
        BaseBundle bundle = new Bundle();
        bundle.putInt("pid", i);
        bundle.putString("vendor", str);
        String str2 = "initialOff";
        if (i2 != 0) {
            z = true;
        }
        bundle.putBoolean(str2, z);
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    public synchronized void startSmartFittingService() {
        Log.m29d(TAG, "startSmartFittingService");
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_SMARTFIT_SERVICE, CLASS_SMARTFIT_SERVICE);
        ComponentName startServiceAsUser = this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
    }

    public void stopMhdrService() {
        Log.m29d(TAG, "stopMhdrService");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(101));
    }

    public synchronized void stopSmartFittingService() {
        Log.m29d(TAG, "stopSmartFittingService");
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_SMARTFIT_SERVICE, CLASS_SMARTFIT_SERVICE);
        this.mContext.stopServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
        this.mSmartFittingMode = 1;
    }

    public void updateblackbarstatus(int i) {
        Log.m29d(TAG, "updateblackbarstatus");
        this.mIsBlackbar = i;
        if (i > 0) {
            Message obtainMessage = this.mHandler.obtainMessage(800);
            BaseBundle bundle = new Bundle();
            bundle.putString("intent", INTENT_SMARTFIT_FOUND_BLACK_BAR);
            obtainMessage.setData(bundle);
            this.mHandler.sendMessage(obtainMessage);
        }
    }
}
