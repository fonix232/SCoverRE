package com.samsung.android.content.smartclip;

import android.view.View;

public interface SemSmartClipDataExtractionListener {
    int onExtractSmartClipData(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SemSmartClipDataElement semSmartClipDataElement);
}
