package com.samsung.android.contextaware.utilbundle.autotest;

abstract class CaAutoTest implements ICaAutoTest {
    private int mDelayTime;
    private boolean mStopFlag = false;

    protected CaAutoTest(int i) {
        setDelayTime(i);
    }

    protected final int getDelayTime() {
        return this.mDelayTime;
    }

    protected final boolean isStopTest() {
        return this.mStopFlag;
    }

    public abstract void run();

    protected final void setDelayTime(int i) {
        this.mDelayTime = i;
    }

    public final void setStopFlag(boolean z) {
        this.mStopFlag = z;
    }

    public final void stopAutoTest() {
        this.mStopFlag = true;
    }
}
