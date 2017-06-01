package com.samsung.android.sdk.look;

import android.content.Context;

public class SlookResourceManager {
    public static final int AIR_BUTTON_WARN_NO_CONTACT_HISTORY = 0;
    public static final int AIR_BUTTON_WARN_NO_IMAGES = 1;
    public static final int CLIPED_TEXT_MAX_COUNT = 5;
    public static final int DRAWABLE_AUDIO = 2;
    public static final int DRAWABLE_CONTACT = 1;
    public static final int FREQUENT_CONTACT_MAX_COUNT = 2;
    public static final int RECENT_MEDIA_MAX_COUNT = 3;
    public static final int RECENT_SNOTE_MAX_COUNT = 4;

    public static int getDrawableId(int i) {
        switch (i) {
            case 1:
                return 17301719;
            case 2:
                return 17301718;
            default:
                throw new IllegalArgumentException("id(" + i + ") was wrong.");
        }
    }

    public static int getInt(int i) {
        switch (i) {
            case 2:
                return 15;
            case 3:
                return 15;
            case 4:
                return 15;
            case 5:
                return 15;
            default:
                throw new IllegalArgumentException("id(" + i + ") was wrong.");
        }
    }

    public static CharSequence getText(Context context, int i) {
        switch (i) {
            case 0:
                return context.getResources().getText(17041061);
            case 1:
                return context.getResources().getText(17041062);
            default:
                return null;
        }
    }
}
