package com.samsung.android.widget;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;

public class SemLockPatternUtils {
    private LockPatternUtils mLockPatternUtils;

    public SemLockPatternUtils(Context context) {
        this.mLockPatternUtils = new LockPatternUtils(context);
    }

    public int getKeyguardStoredPasswordQuality(int i) {
        return this.mLockPatternUtils.getKeyguardStoredPasswordQuality(i);
    }

    public boolean isFmmLockEnabled(int i) {
        return this.mLockPatternUtils.isFMMLockEnabled(i);
    }

    public boolean isLockScreenDisabled(int i) {
        return this.mLockPatternUtils.isLockScreenDisabled(i);
    }

    public boolean isSecure(int i) {
        return this.mLockPatternUtils.isSecure(i);
    }

    public void setLockScreenDisabled(boolean z, int i) {
        this.mLockPatternUtils.setLockScreenDisabled(z, i);
    }
}
