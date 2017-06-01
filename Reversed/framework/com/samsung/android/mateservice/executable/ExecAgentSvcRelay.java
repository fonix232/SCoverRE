package com.samsung.android.mateservice.executable;

import com.samsung.android.mateservice.action.ActionExecutable;
import com.samsung.android.mateservice.agentsvc.AgentSvcContract.ConnectionMgr;
import com.samsung.android.mateservice.common.LoggerContract;

public class ExecAgentSvcRelay implements ActionExecutable {
    private static final String TAG = "AgentSvcRelay";
    private final ConnectionMgr mAgentSvcMgr;
    private final LoggerContract mLogger;

    public ExecAgentSvcRelay(LoggerContract loggerContract, ConnectionMgr connectionMgr) {
        this.mLogger = loggerContract;
        this.mAgentSvcMgr = connectionMgr;
    }

    public android.os.Bundle execute(android.os.Bundle r11, int r12, int r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.samsung.android.mateservice.executable.ExecAgentSvcRelay.execute(android.os.Bundle, int, int):android.os.Bundle. bs: [B:11:0x0031, B:36:0x0062]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r10 = this;
        r8 = 0;
        r4 = 0;
        r2 = r10.mLogger;
        r3 = "AgentSvcRelay";
        r5 = "relay action 0x%x";
        r6 = 1;
        r6 = new java.lang.Object[r6];
        r7 = java.lang.Integer.valueOf(r13);
        r6[r8] = r7;
        r3 = com.samsung.android.mateservice.util.UtilLog.m8i(r3, r5, r6);
        r5 = new java.lang.Object[r8];
        r2.append(r3, r5);
        r0 = 0;
        r2 = r10.mAgentSvcMgr;	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r0 = r2.getClient();	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        if (r0 == 0) goto L_0x003a;	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
    L_0x0025:
        r3 = r0.execute(r13, r11);	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        if (r0 == 0) goto L_0x002e;
    L_0x002b:
        r0.close();	 Catch:{ Throwable -> 0x0037 }
    L_0x002e:
        r2 = r4;
    L_0x002f:
        if (r2 == 0) goto L_0x0039;
    L_0x0031:
        throw r2;	 Catch:{ Throwable -> 0x0032 }
    L_0x0032:
        r1 = move-exception;
        com.samsung.android.mateservice.util.UtilLog.printThrowableStackTrace(r1);
    L_0x0036:
        return r4;
    L_0x0037:
        r2 = move-exception;
        goto L_0x002f;
    L_0x0039:
        return r3;
    L_0x003a:
        r2 = r10.mLogger;	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r3 = "AgentSvcRelay";	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r5 = "failed to connect agent svc";	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r6 = 0;	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r6 = new java.lang.Object[r6];	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r3 = com.samsung.android.mateservice.util.UtilLog.m9v(r3, r5, r6);	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r5 = 0;	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r5 = new java.lang.Object[r5];	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        r2.append(r3, r5);	 Catch:{ Throwable -> 0x005a, all -> 0x0074 }
        if (r0 == 0) goto L_0x0054;
    L_0x0051:
        r0.close();	 Catch:{ Throwable -> 0x0058 }
    L_0x0054:
        r2 = r4;
    L_0x0055:
        if (r2 == 0) goto L_0x0036;
    L_0x0057:
        throw r2;	 Catch:{ Throwable -> 0x0032 }
    L_0x0058:
        r2 = move-exception;
        goto L_0x0055;
    L_0x005a:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x005c }
    L_0x005c:
        r3 = move-exception;
        r9 = r3;
        r3 = r2;
        r2 = r9;
    L_0x0060:
        if (r0 == 0) goto L_0x0065;
    L_0x0062:
        r0.close();	 Catch:{ Throwable -> 0x0068 }
    L_0x0065:
        if (r3 == 0) goto L_0x0073;
    L_0x0067:
        throw r3;	 Catch:{ Throwable -> 0x0032 }
    L_0x0068:
        r5 = move-exception;	 Catch:{ Throwable -> 0x0032 }
        if (r3 != 0) goto L_0x006d;	 Catch:{ Throwable -> 0x0032 }
    L_0x006b:
        r3 = r5;	 Catch:{ Throwable -> 0x0032 }
        goto L_0x0065;	 Catch:{ Throwable -> 0x0032 }
    L_0x006d:
        if (r3 == r5) goto L_0x0065;	 Catch:{ Throwable -> 0x0032 }
    L_0x006f:
        r3.addSuppressed(r5);	 Catch:{ Throwable -> 0x0032 }
        goto L_0x0065;	 Catch:{ Throwable -> 0x0032 }
    L_0x0073:
        throw r2;	 Catch:{ Throwable -> 0x0032 }
    L_0x0074:
        r2 = move-exception;
        r3 = r4;
        goto L_0x0060;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.mateservice.executable.ExecAgentSvcRelay.execute(android.os.Bundle, int, int):android.os.Bundle");
    }
}
