package com.samsung.android.content.smartclip;

import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.BaseBundle;
import android.util.Log;
import android.view.View;

public class SmartClipUtils {
    private static final String TAG = "SmartClipUtils";

    public static String getChromeViewClassNameFromManifest(Context context, String str) {
        String str2 = null;
        try {
            PackageItemInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
            if (applicationInfo == null) {
                Log.m31e(TAG, "getChromeViewClassNameFromManifest : Could not get appInfo! - " + str);
                return null;
            }
            BaseBundle baseBundle = applicationInfo.metaData;
            if (baseBundle != null) {
                str2 = baseBundle.getString("org.chromium.content.browser.SMART_CLIP_PROVIDER");
                if (str2 != null) {
                    Log.m29d(TAG, "Target chrome view = " + str2);
                }
            }
            return str2;
        } catch (NameNotFoundException e) {
        }
    }

    public static Rect getViewBoundsOnScreen(View view) {
        Rect rect = new Rect();
        Point viewLocationOnScreen = getViewLocationOnScreen(view);
        rect.left = viewLocationOnScreen.x;
        rect.top = viewLocationOnScreen.y;
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
        return rect;
    }

    public static Point getViewLocationOnScreen(View view) {
        Point point = new Point();
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        point.x = iArr[0];
        point.y = iArr[1];
        return point;
    }

    public static boolean isInstanceOf(Object obj, String str) {
        if (obj == null || str == null) {
            return false;
        }
        try {
            return Class.forName(str, true, obj.getClass().getClassLoader()).isInstance(obj);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isValidMetaTag(SemSmartClipMetaTag semSmartClipMetaTag) {
        if (semSmartClipMetaTag == null || semSmartClipMetaTag.getType() == null) {
            return false;
        }
        String value = semSmartClipMetaTag.getValue();
        return (semSmartClipMetaTag.getType().equals("url") && (value == null || value.startsWith("about:") || value.startsWith("email://"))) ? false : true;
    }
}
