package com.sec.android.cover.ledcover.reflection.view;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefWindowManagerLayoutParams extends AbstractBaseReflection {
    private static RefWindowManagerLayoutParams sInstance;
    public int TYPE_SVIEW_COVER_DIALOG;

    public static synchronized RefWindowManagerLayoutParams get() {
        RefWindowManagerLayoutParams refWindowManagerLayoutParams;
        synchronized (RefWindowManagerLayoutParams.class) {
            if (sInstance == null) {
                sInstance = new RefWindowManagerLayoutParams();
            }
            refWindowManagerLayoutParams = sInstance;
        }
        return refWindowManagerLayoutParams;
    }

    protected void loadStaticFields() {
        this.TYPE_SVIEW_COVER_DIALOG = getIntStaticValue("TYPE_SVIEW_COVER_DIALOG");
    }

    protected String getBaseClassName() {
        return "android.view.WindowManager$LayoutParams";
    }
}
