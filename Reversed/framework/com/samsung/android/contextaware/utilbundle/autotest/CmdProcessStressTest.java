package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

abstract class CmdProcessStressTest extends CaAutoTest {
    private boolean mChange = true;

    protected CmdProcessStressTest(int i) {
        super(i);
    }

    protected abstract void clear();

    protected abstract int getType();

    protected abstract void registerListener();

    public final void run() {
        do {
            try {
                Thread.sleep((long) getDelayTime());
                if (this.mChange) {
                    this.mChange = false;
                    registerListener();
                } else {
                    this.mChange = true;
                    unregisterListener();
                }
            } catch (Throwable e) {
                CaLogger.exception(e);
                return;
            }
        } while (!super.isStopTest());
        clear();
    }

    protected abstract void unregisterListener();
}
