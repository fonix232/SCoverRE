package com.samsung.android.contextaware.manager.fault;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.manager.ContextAwareService.ServiceHandler;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CmdProcessResultManager implements ICmdProcessResultObserver {
    private final IBinder mBinder;
    private final ServiceHandler mServiceHandler;

    public CmdProcessResultManager(IBinder iBinder, ServiceHandler serviceHandler) {
        this.mBinder = iBinder;
        this.mServiceHandler = serviceHandler;
    }

    public final void updateCmdProcessResult(String str, Bundle bundle) {
        if (!str.equals(ContextType.CMD_PROCESS_FAULT_DETECTION.getCode())) {
            return;
        }
        if (this.mBinder == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_BINDER_NULL_EXCEPTION.getMessage());
        } else if (this.mServiceHandler == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_SERVICE_HANDLER_NULL_EXCEPTION.getMessage());
        } else if (bundle == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_CONTEXT_INFO_NULL_EXCEPTION.getMessage());
        } else {
            Bundle bundle2 = (Bundle) bundle.clone();
            if (bundle2.getInt("CheckResult") != 0) {
                FaultDetectionManager.getInstance().setRestoreEnable();
            }
            BaseBundle bundle3 = new Bundle();
            bundle3.putIBinder("Binder", this.mBinder);
            bundle3.putInt("Service", bundle2.getInt("Service"));
            bundle2.putBundle("Listener", bundle3);
            Message obtain = Message.obtain();
            obtain.what = ContextType.CMD_PROCESS_FAULT_DETECTION.ordinal();
            obtain.obj = bundle2;
            this.mServiceHandler.sendMessage(obtain);
        }
    }
}
