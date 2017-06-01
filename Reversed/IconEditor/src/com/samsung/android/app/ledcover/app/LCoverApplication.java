package com.samsung.android.app.ledcover.app;

import android.app.Application;
import com.samsung.android.app.ledcover.common.Utils;

public class LCoverApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Utils.initSALog(this);
    }
}
