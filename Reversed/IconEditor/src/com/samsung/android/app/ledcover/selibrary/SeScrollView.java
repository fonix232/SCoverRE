package com.samsung.android.app.ledcover.selibrary;

import android.widget.ListView;
import android.widget.ScrollView;
import com.samsung.android.app.ledcover.interfacelibrary.ScrollViewInterface;

public class SeScrollView implements ScrollViewInterface {
    public void semSetGoToTopEnabled(ScrollView sv, boolean enabled) {
        sv.semSetGoToTopEnabled(enabled);
    }

    public void semSetGoToTopEnabled(ListView lv, boolean enabled) {
        lv.semSetGoToTopEnabled(enabled);
    }
}
