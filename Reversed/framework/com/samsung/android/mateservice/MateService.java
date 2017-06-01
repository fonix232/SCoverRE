package com.samsung.android.mateservice;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import com.samsung.android.mateservice.IMateService.Stub;
import com.samsung.android.mateservice.action.Action;
import com.samsung.android.mateservice.action.ActionDispatcher;
import com.samsung.android.mateservice.action.ActionExecutable;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.ConnectionMgr;
import com.samsung.android.mateservice.agentsvc.AgentSvcMgr;
import com.samsung.android.mateservice.common.Dump;
import com.samsung.android.mateservice.common.FwDependency;
import com.samsung.android.mateservice.common.Logger;
import com.samsung.android.mateservice.common.LoggerContract;
import com.samsung.android.mateservice.executable.ExecAccessoryMgr;
import com.samsung.android.mateservice.executable.ExecAgentSvcRelay;
import com.samsung.android.mateservice.executable.ExecClientStateMgr;
import com.samsung.android.mateservice.executable.ExecStringCrypto;
import com.samsung.android.mateservice.util.UtilLog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MateService extends Stub {
    private static final int HISTORY_COUNT = 40;
    private static final String TAG = "Impl";
    private int mBootPhase;
    private final Context mContext;
    private final List<Dump> mDumps = new ArrayList();
    private final ActionDispatcher mExecutor;

    public MateService(Context context) {
        this.mContext = context;
        LoggerContract logger = new Logger(getLoggerCount());
        this.mExecutor = new ActionDispatcher(context, logger);
        ConnectionMgr agentSvcMgr = new AgentSvcMgr(context);
        ActionExecutable execStringCrypto = new ExecStringCrypto();
        ActionExecutable execAccessoryMgr = new ExecAccessoryMgr(logger, agentSvcMgr);
        ActionExecutable execClientStateMgr = new ExecClientStateMgr(logger);
        this.mExecutor.append(Action.EXTERNAL_SYS_ACCESSORY_STATE_CHANGED, false, execAccessoryMgr);
        this.mExecutor.append(Action.AGENT_SYS_GET_ACCESSORY, true, execAccessoryMgr);
        this.mExecutor.append(Action.AGENT_SYS_CLIENT_STATE, true, execClientStateMgr);
        this.mExecutor.append(Action.AGENT_SYS_ENCRYPTION, true, execStringCrypto);
        this.mExecutor.append(Action.AGENT_SYS_DECRYPTION, true, execStringCrypto);
        this.mExecutor.append(Action.AGENT_SYS_CLIENT_CONNECTION, true, agentSvcMgr);
        this.mExecutor.setDefault(false, new ExecAgentSvcRelay(logger, agentSvcMgr));
        this.mDumps.add(execAccessoryMgr);
        this.mDumps.add(logger);
        this.mDumps.add(execClientStateMgr);
    }

    private int getLoggerCount() {
        return (FwDependency.isProductDev() || UtilLog.isRoDebugLevelMid()) ? 40 : 0;
    }

    private void setBootPhase(int i) {
        this.mBootPhase = i;
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        UtilLog.m9v(TAG, "dump", new Object[0]);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            UtilLog.m10w(TAG, "permission denied - pid[%d] uid[%d]", Integer.valueOf(Binder.getCallingPid()), Integer.valueOf(Binder.getCallingUid()));
            return;
        }
        try {
            printWriter.write(getDump());
            printWriter.println();
        } catch (Throwable th) {
            UtilLog.printThrowableStackTrace(th);
        }
    }

    public Bundle executeAction(int i, Bundle bundle) throws RemoteException {
        return this.mExecutor.execute(i, bundle);
    }

    String getDump() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("************************************************************************\n");
        stringBuilder.append(UtilLog.getMsg("productDev: %s / logLevel: %d  / safeString: %s\n", Boolean.valueOf(FwDependency.isProductDev()), Integer.valueOf(UtilLog.logLevel()), Boolean.valueOf(UtilLog.useSafeString())));
        synchronized (this) {
            for (Dump dump : this.mDumps) {
                dump.getDump(stringBuilder);
            }
        }
        stringBuilder.append("\n************************************************************************");
        return stringBuilder.toString();
    }

    public void systemReady() {
        UtilLog.m8i(TAG, "systemReady!!", new Object[0]);
    }
}
