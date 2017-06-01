package com.samsung.android.content.smartclip;

import android.graphics.Rect;

public interface SemSmartClipDataElement {
    boolean addTag(SemSmartClipMetaTag semSmartClipMetaTag);

    void clearMetaData();

    SemSmartClipMetaTagArray getAllTags();

    int getExtractionLevel();

    int getExtractionMode();

    Rect getMetaAreaRect();

    SemSmartClipMetaTagArray getTags(String str);

    int removeTags(String str);

    boolean sendSuspendedExtractionData();

    void setMetaAreaRect(Rect rect);

    boolean setTag(SemSmartClipMetaTag semSmartClipMetaTag);
}
