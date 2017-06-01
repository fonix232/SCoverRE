package com.samsung.android.contextaware.utilbundle;

import android.os.BaseBundle;
import android.os.Bundle;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CaAkmManager {
    private static final String SETTING_FILE_NAME = "/data/misc/akmd_set.txt";
    private static volatile CaAkmManager instance;

    public static CaAkmManager getInstance() {
        if (instance == null) {
            synchronized (CaAkmManager.class) {
                if (instance == null) {
                    instance = new CaAkmManager();
                }
            }
        }
        return instance;
    }

    private int loadIntValue(String str) {
        return 0;
    }

    private void saveIntValue(String str, int i) {
    }

    public final String[] getOrientationValueNames() {
        return new String[]{"HSUC_HDST_FORM0", "HSUC_HO_FORM0.x", "HSUC_HO_FORM0.y", "HSUC_HO_FORM0.z", "HFLUCV_HREF_FORM0.x", "HFLUCV_HREF_FORM0.y", "HFLUCV_HREF_FORM0.z"};
    }

    public final Bundle loadOrientationInfo() {
        BaseBundle bundle = new Bundle();
        for (String str : getOrientationValueNames()) {
            int loadIntValue = loadIntValue(str);
            bundle.putInt(str, loadIntValue);
            CaLogger.info(str + " : " + Integer.toString(loadIntValue));
        }
        return bundle;
    }

    public final void saveOrientationInfo(Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            CaLogger.error("can't save the orientation information");
            return;
        }
        for (String str : getOrientationValueNames()) {
            int i = bundle.getInt(str);
            saveIntValue(str, i);
            CaLogger.info(str + " : " + Integer.toString(i));
        }
    }
}
