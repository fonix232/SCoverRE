package com.samsung.android.contextaware.manager;

import android.os.Bundle;

public class ContextBean {
    private Bundle mContextBundle;
    private Bundle mContextBundleForDisplay;

    protected ContextBean() {
        clearContextBean();
    }

    private void putContextForDisplay(String str, String str2) {
        if (str2 != null && !str2.isEmpty()) {
            this.mContextBundleForDisplay.putString(str, str2);
        }
    }

    private void putContextForDisplay(String str, String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            this.mContextBundleForDisplay.putStringArray(str, strArr);
        }
    }

    protected final void clearContextBean() {
        this.mContextBundle = new Bundle();
        this.mContextBundleForDisplay = new Bundle();
    }

    protected final Bundle getContextBundle() {
        return this.mContextBundle;
    }

    public final Bundle getContextBundleForDisplay() {
        return this.mContextBundleForDisplay;
    }

    public final void putContext(String str, double d) {
        this.mContextBundle.putDouble(str, d);
        putContextForDisplay(str, Double.toString(d));
    }

    public final void putContext(String str, float f) {
        this.mContextBundle.putFloat(str, f);
        putContextForDisplay(str, Float.toString(f));
    }

    public final void putContext(String str, int i) {
        this.mContextBundle.putInt(str, i);
        putContextForDisplay(str, Integer.toString(i));
    }

    public final void putContext(String str, long j) {
        this.mContextBundle.putLong(str, j);
        putContextForDisplay(str, Long.toString(j));
    }

    public final void putContext(String str, Bundle bundle) {
        this.mContextBundle.putBundle(str, bundle);
    }

    public final void putContext(String str, String str2) {
        this.mContextBundle.putString(str, str2);
        putContextForDisplay(str, str2);
    }

    public final void putContext(String str, short s) {
        this.mContextBundle.putShort(str, s);
        putContextForDisplay(str, Short.toString(s));
    }

    public final void putContext(String str, boolean z) {
        this.mContextBundle.putBoolean(str, z);
        putContextForDisplay(str, Boolean.toString(z));
    }

    public final void putContext(String str, double[] dArr) {
        if (dArr != null && dArr.length >= 0) {
            this.mContextBundle.putDoubleArray(str, dArr);
            String[] strArr = new String[dArr.length];
            for (int i = 0; i < dArr.length; i++) {
                strArr[i] = Double.toString(dArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContext(String str, float[] fArr) {
        if (fArr != null && fArr.length > 0) {
            this.mContextBundle.putFloatArray(str, fArr);
            String[] strArr = new String[fArr.length];
            for (int i = 0; i < fArr.length; i++) {
                strArr[i] = Float.toString(fArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContext(String str, int[] iArr) {
        if (iArr != null && iArr.length >= 0) {
            this.mContextBundle.putIntArray(str, iArr);
            String[] strArr = new String[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                strArr[i] = Integer.toString(iArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContext(String str, long[] jArr) {
        if (jArr != null && jArr.length >= 0) {
            this.mContextBundle.putLongArray(str, jArr);
            String[] strArr = new String[jArr.length];
            for (int i = 0; i < jArr.length; i++) {
                strArr[i] = Long.toString(jArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContext(String str, boolean[] zArr) {
        if (zArr != null && zArr.length > 0) {
            this.mContextBundle.putBooleanArray(str, zArr);
            String[] strArr = new String[zArr.length];
            for (int i = 0; i < zArr.length; i++) {
                strArr[i] = Boolean.toString(zArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContextForDisplay(String str, double d) {
        putContextForDisplay(str, Double.toString(d));
    }

    public final void putContextForDisplay(String str, float f) {
        putContextForDisplay(str, Float.toString(f));
    }

    public final void putContextForDisplay(String str, int i) {
        putContextForDisplay(str, Integer.toString(i));
    }

    public final void putContextForDisplay(String str, long j) {
        putContextForDisplay(str, Long.toString(j));
    }

    public final void putContextForDisplay(String str, short s) {
        putContextForDisplay(str, Short.toString(s));
    }

    public final void putContextForDisplay(String str, boolean z) {
        putContextForDisplay(str, Boolean.toString(z));
    }

    public final void putContextForDisplay(String str, double[] dArr) {
        if (dArr != null && dArr.length > 0) {
            String[] strArr = new String[dArr.length];
            for (int i = 0; i < dArr.length; i++) {
                strArr[i] = Double.toString(dArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContextForDisplay(String str, float[] fArr) {
        if (fArr != null && fArr.length > 0) {
            String[] strArr = new String[fArr.length];
            for (int i = 0; i < fArr.length; i++) {
                strArr[i] = Float.toString(fArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContextForDisplay(String str, int[] iArr) {
        if (iArr != null && iArr.length > 0) {
            String[] strArr = new String[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                strArr[i] = Integer.toString(iArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContextForDisplay(String str, long[] jArr) {
        if (jArr != null && jArr.length > 0) {
            String[] strArr = new String[jArr.length];
            for (int i = 0; i < jArr.length; i++) {
                strArr[i] = Long.toString(jArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }

    public final void putContextForDisplay(String str, boolean[] zArr) {
        if (zArr != null && zArr.length > 0) {
            String[] strArr = new String[zArr.length];
            for (int i = 0; i < zArr.length; i++) {
                strArr[i] = Boolean.toString(zArr[i]);
            }
            putContextForDisplay(str, strArr);
        }
    }
}
