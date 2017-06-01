package com.samsung.android.mateservice.executable;

import android.os.Bundle;
import com.samsung.android.mateservice.action.ActionExecutable;
import com.samsung.android.mateservice.common.BundleArgs;
import com.samsung.android.mateservice.common.Dump;
import com.samsung.android.mateservice.common.Logger;
import java.util.HashMap;
import java.util.Map.Entry;

public class ExecClientStateMgr implements ActionExecutable, Dump {
    private static final int CMD_CLEAR_CLIENT_STATE = 3;
    private static final int CMD_REMOVE_CLIENT_STATE = 2;
    private static final int CMD_RESTORE_CLIENT_STATE = 1;
    private static final int CMD_SAVE_CLIENT_STATE = 0;
    private final Logger mLogger;
    private final HashMap<String, Bundle> mState = new HashMap();

    public ExecClientStateMgr(Logger logger) {
        this.mLogger = logger;
    }

    private Bundle commandClearClientState() {
        synchronized (this.mState) {
            this.mState.clear();
        }
        return BundleArgs.getResultBundle(true);
    }

    private Bundle commandRemoveClientState(String str) {
        synchronized (this.mState) {
            this.mState.remove(str);
        }
        return BundleArgs.getResultBundle(true);
    }

    private Bundle commandRestoreClientState(String str) {
        Bundle bundle;
        synchronized (this.mState) {
            bundle = (Bundle) this.mState.get(str);
        }
        return bundle;
    }

    private Bundle commandSaveClientState(String str, Bundle bundle) {
        synchronized (this.mState) {
            this.mState.put(str, bundle);
        }
        return BundleArgs.getResultBundle(true);
    }

    public Bundle execute(Bundle bundle, int i, int i2) {
        if (bundle == null) {
            return null;
        }
        int i3 = bundle.getInt(BundleArgs.CLIENT_STATE_ACTION, -1);
        String string = bundle.getString(BundleArgs.STATE_ID);
        switch (i3) {
            case 0:
                return commandSaveClientState(string, bundle.getBundle(BundleArgs.CLIENT_STATE_BUNDLE));
            case 1:
                return commandRestoreClientState(string);
            case 2:
                return commandRemoveClientState(string);
            case 3:
                return commandClearClientState();
            default:
                return null;
        }
    }

    public void getDump(StringBuilder stringBuilder) {
        stringBuilder.append("\n---- client state\n");
        synchronized (this.mState) {
            for (Entry entry : this.mState.entrySet()) {
                if (entry != null) {
                    stringBuilder.append((String) entry.getKey()).append(": ").append(((Bundle) entry.getValue()).toString()).append('\n');
                }
            }
        }
    }
}
