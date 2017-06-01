package com.samsung.android.mateservice.agentsvc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.samsung.android.mateservice.IAgentService;
import com.samsung.android.mateservice.IAgentService.Stub;
import com.samsung.android.mateservice.MateConst;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.AgentSvc;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.ConnectionMgr;
import com.samsung.android.mateservice.common.FwDependency;
import com.samsung.android.mateservice.util.UtilLog;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

class AgentSvcClient implements AgentSvc {
    private static final String TAG = "AgentSvcClient";
    private static final long WAIT_TIME = 1500;
    private final BlockingDeque<IAgentService> mBlockingQueue = new LinkedBlockingDeque(1);
    private volatile boolean mConnectedAtLeastOnce = false;
    private final ServiceConnection mConnection = new C02161();
    private final ConnectionMgr mConnectionMgr;
    private final Context mContext;
    private volatile IAgentService mService;

    class C02161 implements ServiceConnection {
        C02161() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (AgentSvcClient.this.mConnectedAtLeastOnce) {
                AgentSvcClient.this.mService = Stub.asInterface(iBinder);
            } else {
                AgentSvcClient.this.mConnectedAtLeastOnce = true;
                try {
                    AgentSvcClient.this.mBlockingQueue.clear();
                    AgentSvcClient.this.mBlockingQueue.put(Stub.asInterface(iBinder));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            UtilLog.m9v(AgentSvcClient.TAG, "agent svc is connected", new Object[0]);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            UtilLog.m9v(AgentSvcClient.TAG, "agent svc is disconnected", new Object[0]);
        }
    }

    AgentSvcClient(Context context, ConnectionMgr connectionMgr) {
        this.mContext = context;
        this.mConnectionMgr = connectionMgr;
    }

    public void close() {
        if (this.mConnectionMgr == null || this.mConnectionMgr.releasableClient()) {
            UtilLog.m9v(TAG, "close", new Object[0]);
            this.mContext.unbindService(this.mConnection);
            this.mService = null;
            this.mConnectedAtLeastOnce = false;
            return;
        }
        UtilLog.m9v(TAG, "close - connection is remained", new Object[0]);
    }

    boolean connect() {
        if (isConnected()) {
            UtilLog.m9v(TAG, "already connected", new Object[0]);
            return true;
        }
        Intent intent = new Intent();
        intent.setClassName(MateConst.MATE_AGENT_PACKAGE_NAME, MateConst.MATE_AGENT_SERVICE_NAME);
        if (FwDependency.bindServiceAsUser(this.mContext, intent, this.mConnection, 1, FwDependency.getUserHandle("SYSTEM"))) {
            try {
                this.mService = (IAgentService) this.mBlockingQueue.poll(WAIT_TIME, TimeUnit.MILLISECONDS);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return isConnected();
        }
        UtilLog.m9v(TAG, "failed to bind agent svc", new Object[0]);
        return false;
    }

    public Bundle execute(int i, Bundle bundle) throws RemoteException {
        return this.mService != null ? this.mService.executeAction(i, bundle) : null;
    }

    boolean isConnected() {
        return this.mService != null;
    }
}
