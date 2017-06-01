package com.samsung.android.mateservice.agentsvc;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.mateservice.action.ActionExecutable;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.AgentSvc;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.ConnectionMgr;
import com.samsung.android.mateservice.common.BundleArgs;
import com.samsung.android.mateservice.util.UtilLog;

public class AgentSvcMgr implements ConnectionMgr, ActionExecutable {
    private static final String TAG = "AgentSvcMgr";
    private final AgentSvcClient mAgentSvc;
    private boolean mContPreserved = false;
    private int mRefCount = 0;

    public AgentSvcMgr(Context context) {
        this.mAgentSvc = new AgentSvcClient(context, this);
    }

    private void setConnectionPreserved(boolean z) {
        UtilLog.m9v(TAG, "setConnectionPreserved %s => %s", Boolean.valueOf(this.mContPreserved), Boolean.valueOf(z));
        this.mContPreserved = z;
        if (!z) {
            synchronized (this) {
                if (this.mRefCount == 0) {
                    this.mAgentSvc.close();
                }
            }
        }
    }

    public Bundle execute(Bundle bundle, int i, int i2) {
        if (bundle != null) {
            setConnectionPreserved(bundle.getBoolean(BundleArgs.KEEP_CONNECTION_STATE, false));
        }
        return null;
    }

    public AgentSvc getClient() {
        synchronized (this) {
            if (this.mAgentSvc.connect()) {
                this.mRefCount++;
                AgentSvc agentSvc = this.mAgentSvc;
                return agentSvc;
            }
            return null;
        }
    }

    public boolean releasableClient() {
        int i;
        synchronized (this) {
            if (this.mRefCount > 0) {
                this.mRefCount--;
            }
            i = this.mRefCount;
        }
        return i == 0 && !this.mContPreserved;
    }
}
