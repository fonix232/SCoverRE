package com.samsung.android.mateservice.agentsvc;

import android.os.Bundle;
import android.os.RemoteException;
import java.io.Closeable;

public interface AgentSvcContract {

    public interface AgentSvc extends Closeable {
        Bundle execute(int i, Bundle bundle) throws RemoteException;
    }

    public interface ConnectionMgr {
        AgentSvc getClient();

        boolean releasableClient();
    }
}
