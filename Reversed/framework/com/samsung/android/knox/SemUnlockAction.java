package com.samsung.android.knox;

import com.samsung.android.knox.SemIUnlockAction.Stub;

public abstract class SemUnlockAction {
    private SemIUnlockAction f14s = new SubSemUnlockAction(this);

    public class SubSemUnlockAction extends Stub {
        SemUnlockAction parent = null;

        public SubSemUnlockAction(SemUnlockAction semUnlockAction) {
            this.parent = semUnlockAction;
        }

        public void onUnlock() {
            if (this.parent != null) {
                this.parent.onUnlock();
            }
        }
    }

    public SemIUnlockAction getChild() {
        return this.f14s;
    }

    public abstract void onUnlock();
}
