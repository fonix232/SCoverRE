package com.samsung.android.content.clipboard;

import com.samsung.android.content.clipboard.data.SemClipData;

public interface SemClipboardEventListener {
    void onClipboardUpdated(int i, SemClipData semClipData);

    void onFilterUpdated(int i);
}
