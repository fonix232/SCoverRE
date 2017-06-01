package com.samsung.android.app.ledcover.sdllibrary;

import android.widget.ListView;
import android.widget.ScrollView;
import com.samsung.android.app.ledcover.interfacelibrary.ScrollViewInterface;

public class SdlScrollView implements ScrollViewInterface {
    public void semSetGoToTopEnabled(ScrollView sv, boolean enabled) {
        sv.semEnableGoToTop(enabled);
    }

    public void semSetGoToTopEnabled(ListView lv, boolean enabled) {
        lv.semEnableGoToTop(enabled);
    }
}
