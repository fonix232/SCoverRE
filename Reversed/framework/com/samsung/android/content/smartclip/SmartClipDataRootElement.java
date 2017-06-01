package com.samsung.android.content.smartclip;

import android.graphics.Rect;
import android.text.TextUtils;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.util.ArrayList;

/* compiled from: SemSmartClipDataRepository */
class SmartClipDataRootElement extends SmartClipDataElementImpl {
    SmartClipDataRootElement() {
    }

    public String collectPlainTextTag() {
        SmartClipDataElementImpl smartClipDataElementImpl = this;
        String str = new String();
        Rect rect = new Rect();
        while (smartClipDataElementImpl != null) {
            Object obj = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            ArrayList tags = smartClipDataElementImpl.getTags(SemSmartClipMetaTagType.PLAIN_TEXT);
            int size = tags.size();
            Rect metaAreaRect = smartClipDataElementImpl.getMetaAreaRect();
            for (int i = 0; i < size; i++) {
                Object value = ((SemSmartClipMetaTag) tags.get(i)).getValue();
                if (!(value == null || TextUtils.isEmpty(value))) {
                    obj = obj + value + " ";
                }
            }
            if (obj != null && TextUtils.getTrimmedLength(obj) > 0) {
                if (!(metaAreaRect == null || metaAreaRect.top < r5.bottom || TextUtils.isEmpty(str))) {
                    str = str + "\n";
                }
                str = str + obj + " ";
                if (metaAreaRect != null) {
                    rect = metaAreaRect;
                }
            }
            smartClipDataElementImpl = smartClipDataElementImpl.traverseNextElement(this);
        }
        Object trim = str.trim();
        return TextUtils.isEmpty(trim) ? null : trim;
    }
}
