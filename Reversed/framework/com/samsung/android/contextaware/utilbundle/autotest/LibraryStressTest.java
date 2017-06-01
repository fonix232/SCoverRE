package com.samsung.android.contextaware.utilbundle.autotest;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextAwareManager;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.manager.ContextAwareListener;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Random;

class LibraryStressTest extends CmdProcessStressTest {
    private final ContextAwareListener mCaListenerForTest = new C00411();
    private final ContextAwareManager mContextAwareManagerForTest;

    class C00411 implements ContextAwareListener {
        C00411() {
        }

        public final void onContextChanged(int i, Bundle bundle) {
            if (i == ContextAwareManager.CMD_PROCESS_FAULT_DETECTION) {
                CaLogger.error("Service=" + Integer.toString(bundle.getInt("Service")) + ", CheckResult=" + Integer.toString(bundle.getInt("CheckResult")));
            }
        }
    }

    protected LibraryStressTest(Context context, int i) {
        super(i);
        this.mContextAwareManagerForTest = (ContextAwareManager) context.getSystemService("context_aware");
    }

    protected final void clear() {
        if (this.mContextAwareManagerForTest != null) {
            this.mContextAwareManagerForTest.unregisterListener(this.mCaListenerForTest);
        }
    }

    protected final int getType() {
        switch (new Random().nextInt(3)) {
            case 0:
                return ContextAwareManager.SHAKE_MOTION_SERVICE;
            case 1:
                return ContextAwareManager.GESTURE_APPROACH_SERVICE;
            case 2:
                return ContextAwareManager.AUTO_ROTATION_SERVICE;
            default:
                return 0;
        }
    }

    protected final void registerListener() {
        if (this.mContextAwareManagerForTest != null) {
            int type = getType();
            CaLogger.info("[TYPE : " + ContextList.getInstance().getServiceCode(type) + "] register");
            this.mContextAwareManagerForTest.registerListener(this.mCaListenerForTest, type);
        }
    }

    protected final void unregisterListener() {
        if (this.mContextAwareManagerForTest != null) {
            int type = getType();
            CaLogger.info("[TYPE : " + ContextList.getInstance().getServiceCode(type) + "] unregister");
            this.mContextAwareManagerForTest.unregisterListener(this.mCaListenerForTest, type);
        }
    }
}
