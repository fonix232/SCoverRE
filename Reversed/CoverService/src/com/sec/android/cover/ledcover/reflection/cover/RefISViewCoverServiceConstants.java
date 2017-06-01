package com.sec.android.cover.ledcover.reflection.cover;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefISViewCoverServiceConstants extends AbstractBaseReflection {
    private static RefISViewCoverServiceConstants sInstance;
    public int SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_NONE;
    public int SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_SET_FLAGS;
    public int SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_UNSET_FLAGS;
    public int SVIEW_ON_COVER_APP_COVERED;
    public int SVIEW_ON_COVER_APP_UNCOVERED;

    public static synchronized RefISViewCoverServiceConstants get() {
        RefISViewCoverServiceConstants refISViewCoverServiceConstants;
        synchronized (RefISViewCoverServiceConstants.class) {
            if (sInstance == null) {
                sInstance = new RefISViewCoverServiceConstants();
            }
            refISViewCoverServiceConstants = sInstance;
        }
        return refISViewCoverServiceConstants;
    }

    protected void loadStaticFields() {
        this.SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_SET_FLAGS = getIntStaticValue("SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_SET_FLAGS");
        this.SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_UNSET_FLAGS = getIntStaticValue("SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_UNSET_FLAGS");
        this.SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_NONE = getIntStaticValue("SVIEW_COVER_SERVICE_SET_APP_COVERED_RESULT_NONE");
        this.SVIEW_ON_COVER_APP_COVERED = getIntStaticValue("SVIEW_ON_COVER_APP_COVERED");
        this.SVIEW_ON_COVER_APP_UNCOVERED = getIntStaticValue("SVIEW_ON_COVER_APP_UNCOVERED");
    }

    protected String getBaseClassName() {
        return "com.samsung.android.cover.ISViewCoverServiceConstants";
    }
}
