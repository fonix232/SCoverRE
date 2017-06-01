package com.samsung.android.mateservice.executable;

import android.os.Bundle;
import com.samsung.android.mateservice.action.Action;
import com.samsung.android.mateservice.action.ActionExecutable;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.AgentSvc;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.ConnectionMgr;
import com.samsung.android.mateservice.common.BundleArgs;
import com.samsung.android.mateservice.common.BundleArgs.Builder;
import com.samsung.android.mateservice.common.Dump;
import com.samsung.android.mateservice.common.Logger;
import com.samsung.android.mateservice.util.UtilLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

public class ExecAccessoryMgr implements ActionExecutable, Dump {
    private static final String TAG = "AccessoryMgr";
    private final HashMap<UUID, Bundle> mAccessories = new LinkedHashMap();
    private final ConnectionMgr mAgentSvcMgr;
    private final Logger mHistoryMgr;

    public ExecAccessoryMgr(Logger logger, ConnectionMgr connectionMgr) {
        this.mHistoryMgr = logger;
        this.mAgentSvcMgr = connectionMgr;
    }

    private Bundle accessoryStateChanged(Bundle bundle) {
        Throwable th;
        boolean z = bundle.getBoolean(BundleArgs.ATTACHED, false);
        byte[] byteArray = bundle.getByteArray("data");
        byte[] byteArray2 = bundle.getByteArray(BundleArgs.EXTRA_DATA);
        if (byteArray == null) {
            this.mHistoryMgr.append(UtilLog.m10w(TAG, "wrong parameter %s %d %d", Boolean.valueOf(z), Integer.valueOf(0), Integer.valueOf(getLength(byteArray2))), new Object[0]);
            return null;
        }
        UUID uniqueKey = getUniqueKey(byteArray);
        long currentTimeMillis = System.currentTimeMillis();
        bundle.putLong(BundleArgs.TIMESTAMP, currentTimeMillis);
        if (!update(z, uniqueKey, bundle)) {
            this.mHistoryMgr.append(currentTimeMillis, UtilLog.m10w(TAG, "not found matched with key[%s] attached[%s]", UtilLog.getSafe(uniqueKey.toString()), Boolean.valueOf(z)), new Object[0]);
        }
        this.mHistoryMgr.append(currentTimeMillis, UtilLog.m8i(TAG, "accessory state changed %s %s", UtilLog.getSafe(uniqueKey.toString()), Boolean.valueOf(z)), new Object[0]);
        Throwable th2 = null;
        AgentSvc agentSvc = null;
        try {
            agentSvc = this.mAgentSvcMgr.getClient();
            if (agentSvc != null) {
                agentSvc.execute(Action.SYS_AGENT_ACCESSORY_STATE_CHANGED, bundle);
            } else {
                this.mHistoryMgr.append(UtilLog.m9v(TAG, "failed to connect agent svc", new Object[0]), new Object[0]);
            }
            if (agentSvc != null) {
                try {
                    agentSvc.close();
                } catch (Throwable th3) {
                    th2 = th3;
                }
            }
            if (th2 != null) {
                try {
                    throw th2;
                } catch (Throwable e) {
                    UtilLog.printThrowableStackTrace(e);
                }
            }
            return null;
        } catch (Throwable th22) {
            Throwable th4 = th22;
            th22 = th;
            th = th4;
        }
        if (agentSvc != null) {
            try {
                agentSvc.close();
            } catch (Throwable th5) {
                if (th22 == null) {
                    th22 = th5;
                } else if (th22 != th5) {
                    th22.addSuppressed(th5);
                }
            }
        }
        if (th22 != null) {
            throw th22;
        }
        throw th;
    }

    private Bundle getAccessoryList() {
        Bundle build;
        synchronized (this.mAccessories) {
            build = Builder.get().put(BundleArgs.ACCESSORY_LIST, new ArrayList(this.mAccessories.values())).build();
        }
        return build;
    }

    private String getHexString(byte[] bArr, int i, int i2) {
        int i3 = i + i2;
        if (bArr == null || bArr.length < i3) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i4 = i; i4 < i3; i4++) {
            String toHexString = Integer.toHexString(bArr[i4] & 255);
            if (toHexString.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(toHexString);
        }
        return stringBuilder.length() != i2 * 2 ? null : stringBuilder.toString().toLowerCase(Locale.ENGLISH);
    }

    private int getLength(byte[] bArr) {
        return bArr != null ? bArr.length : -1;
    }

    private UUID getUniqueKey(byte[] bArr) {
        return UUID.nameUUIDFromBytes(bArr);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean update(boolean r5, java.util.UUID r6, android.os.Bundle r7) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r6 == 0) goto L_0x0006;
    L_0x0004:
        if (r7 != 0) goto L_0x0007;
    L_0x0006:
        return r1;
    L_0x0007:
        r2 = r4.mAccessories;
        monitor-enter(r2);
        if (r5 == 0) goto L_0x0013;
    L_0x000c:
        r1 = r4.mAccessories;	 Catch:{ all -> 0x001f }
        r1.put(r6, r7);	 Catch:{ all -> 0x001f }
        monitor-exit(r2);
        return r0;
    L_0x0013:
        r3 = r4.mAccessories;	 Catch:{ all -> 0x001f }
        r3 = r3.remove(r6);	 Catch:{ all -> 0x001f }
        if (r3 == 0) goto L_0x001d;
    L_0x001b:
        monitor-exit(r2);
        return r0;
    L_0x001d:
        r0 = r1;
        goto L_0x001b;
    L_0x001f:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.mateservice.executable.ExecAccessoryMgr.update(boolean, java.util.UUID, android.os.Bundle):boolean");
    }

    public Bundle execute(Bundle bundle, int i, int i2) {
        switch (i2) {
            case Action.EXTERNAL_SYS_ACCESSORY_STATE_CHANGED /*1114113*/:
                UtilLog.m9v(TAG, "ActionAccessoryStateChanged", new Object[0]);
                if (bundle != null) {
                    return accessoryStateChanged(bundle);
                }
                break;
            case Action.AGENT_SYS_GET_ACCESSORY /*1179649*/:
                UtilLog.m9v(TAG, "ActionGetAccessory", new Object[0]);
                return getAccessoryList();
        }
        return null;
    }

    public void getDump(StringBuilder stringBuilder) {
        stringBuilder.append("\n---- active accessory info.\n");
        synchronized (this.mAccessories) {
            for (Entry entry : this.mAccessories.entrySet()) {
                if (entry != null) {
                    Bundle bundle = (Bundle) entry.getValue();
                    UUID uuid = (UUID) entry.getKey();
                    if (!(bundle == null || uuid == null)) {
                        long j = bundle.getLong(BundleArgs.TIMESTAMP, -1);
                        stringBuilder.append(j != -1 ? UtilLog.getDateString(j) : "wrong time");
                        stringBuilder.append("  ");
                        stringBuilder.append(UtilLog.getSafe(uuid.toString()));
                        String[] strArr = new String[]{"\n\tdata: ", "\n\textra: "};
                        int i = 0;
                        for (byte[] bArr : new byte[][]{bundle.getByteArray("data"), bundle.getByteArray(BundleArgs.EXTRA_DATA)}) {
                            if (bArr != null && bArr.length > 0) {
                                stringBuilder.append(strArr[i]);
                                stringBuilder.append(UtilLog.getSafe(getHexString(bArr, 0, bArr.length)));
                            }
                            i++;
                        }
                        stringBuilder.append("\n");
                    }
                }
            }
        }
    }
}
