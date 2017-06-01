package com.samsung.android.app.ledcover.wrapperlibrary;

import android.widget.ListView;
import android.widget.ScrollView;
import com.samsung.android.app.ledcover.interfacelibrary.ScrollViewInterface;
import com.samsung.android.app.ledcover.sdllibrary.SdlScrollView;
import com.samsung.android.app.ledcover.selibrary.SeScrollView;
import com.samsung.android.app.ledcover.wrapperlibrary.utils.Platformutils;

public class ScrollViewWrapper {
    private static ScrollViewInterface instance;

    static {
        if (Platformutils.isSemDevice()) {
            instance = new SeScrollView();
        } else {
            instance = new SdlScrollView();
        }
    }

    public static void semSetGoToTopEnabled(ScrollView sv, boolean enabled) {
        instance.semSetGoToTopEnabled(sv, enabled);
    }

    public static void semSetGoToTopEnabled(ListView lv, boolean enabled) {
        instance.semSetGoToTopEnabled(lv, enabled);
    }
}
