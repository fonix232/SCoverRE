package com.samsung.android.content.smartclip;

import android.util.Log;
import android.view.View;
import java.util.ArrayList;

public class SemSmartClipViewHelper {
    private static final String TAG = "SemSmartClipViewHelper";

    public static boolean addMetaTag(View view, SemSmartClipMetaTag semSmartClipMetaTag) {
        if (view == null || semSmartClipMetaTag == null || semSmartClipMetaTag.getType() == null) {
            Log.m31e(TAG, "addMetaTag : Have null parameter");
            return false;
        } else if (SmartClipUtils.isValidMetaTag(semSmartClipMetaTag)) {
            ArrayList semGetSmartClipTags = view.semGetSmartClipTags();
            if (semGetSmartClipTags == null) {
                semGetSmartClipTags = new SmartClipMetaTagArrayImpl();
                view.semSetSmartClipTags(semGetSmartClipTags);
            }
            semGetSmartClipTags.add(semSmartClipMetaTag);
            return true;
        } else {
            Log.m31e(TAG, "addMetaTag : Invalid metatag");
            return false;
        }
    }

    public static boolean clearAllMetaTags(View view) {
        return view == null ? false : view.semSetSmartClipTags(null);
    }

    public static int extractDefaultSmartClipData(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SemSmartClipDataElement semSmartClipDataElement) {
        if (view == null) {
            Log.m31e(TAG, "extractDefaultSmartClipData : The view is null!");
            return 0;
        } else if (semSmartClipDataElement == null) {
            Log.m31e(TAG, "extractDefaultSmartClipData : The result element is null!");
            return 0;
        } else if (semSmartClipCroppedArea != null) {
            return view.semExtractSmartClipData(semSmartClipCroppedArea, semSmartClipDataElement);
        } else {
            Log.m31e(TAG, "extractDefaultSmartClipData : The cropped area is null!");
            return 0;
        }
    }

    public static SemSmartClipMetaTagArray getMetaTags(View view) {
        return view == null ? null : view.semGetSmartClipTags();
    }

    public static int removeMetaTag(View view, String str) {
        if (view == null || str == null) {
            return 0;
        }
        SemSmartClipMetaTagArray semGetSmartClipTags = view.semGetSmartClipTags();
        return (semGetSmartClipTags == null || str == null) ? 0 : semGetSmartClipTags.removeMetaTags(str);
    }

    public static boolean setDataExtractionListener(View view, SemSmartClipDataExtractionListener semSmartClipDataExtractionListener) {
        if (view == null) {
            return false;
        }
        view.semSetSmartClipDataExtractionListener(semSmartClipDataExtractionListener);
        return true;
    }

    public static boolean setMetaTags(View view, SemSmartClipMetaTagArray semSmartClipMetaTagArray) {
        if (view == null || semSmartClipMetaTagArray == null) {
            return false;
        }
        view.semSetSmartClipTags(semSmartClipMetaTagArray);
        return true;
    }
}
