package com.samsung.android.tsp;

import android.content.Context;
import android.os.Bundle;
import android.os.ServiceManager;
import android.view.IWindowManager.Stub;

public final class SemTspStateManager {
    public static final String DEAD_ZONE_DIRECTION = "dead_zone_direction";
    public static final String DEAD_ZONE_LAND_X1 = "dead_zone_land_x1";
    public static final String DEAD_ZONE_PORT_REAL_Y1 = "dead_zone_port_real_y1";
    public static final String DEAD_ZONE_PORT_X1 = "dead_zone_port_x1";
    public static final String DEAD_ZONE_PORT_X2 = "dead_zone_port_x2";
    public static final String DEAD_ZONE_PORT_Y1 = "dead_zone_port_y1";
    public static final String DEAD_ZONE_PORT_Y2 = "dead_zone_port_y2";
    public static final String DEAD_ZONE_SET_PROCESS_NAME = "dead_zone_process_name";
    public static final String EDGE_ZONE_WIDTH = "edge_zone_width";
    private static final String TAG = "SemTspStateManager";

    public static void setDeadZone(android.view.View r1, android.os.Bundle r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.samsung.android.tsp.SemTspStateManager.setDeadZone(android.view.View, android.os.Bundle):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
Caused by: java.lang.NullPointerException
*/
        /*
        if (r5 == 0) goto L_0x0004;
    L_0x0002:
        if (r6 != 0) goto L_0x0029;
    L_0x0004:
        r2 = new java.lang.IllegalArgumentException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "The argument is null. decorView=";
        r3 = r3.append(r4);
        r3 = r3.append(r5);
        r4 = ",deadZone=";
        r3 = r3.append(r4);
        r3 = r3.append(r6);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x0029:
        r2 = r6.isEmpty();
        if (r2 != 0) goto L_0x0004;
    L_0x002f:
        r1 = r5.getRootView();
        r0 = r1.getParent();
        if (r0 == 0) goto L_0x0050;
    L_0x0039:
        r2 = r0 instanceof android.view.ViewRootImpl;
        if (r2 == 0) goto L_0x0050;
    L_0x003d:
        r2 = "dead_zone_port_x1";
        r3 = -1;
        r2 = r6.getInt(r2, r3);
        if (r2 >= 0) goto L_0x0059;
    L_0x0047:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "The dead zone do not have default zone width.";
        r2.<init>(r3);
        throw r2;
    L_0x0050:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "The decorview is not attached.";
        r2.<init>(r3);
        throw r2;
        r0.setTspDeadzone(r6);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.tsp.SemTspStateManager.setDeadZone(android.view.View, android.os.Bundle):void");
    }

    public static void setDeadZoneHole(Context context, Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            throw new IllegalArgumentException("deadZoneHole is null or empty");
        }
        try {
            Stub.asInterface(ServiceManager.getService("window")).setDeadzoneHole(bundle);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
