package com.samsung.android.sdk.cover;

public class ScoverState {
    public static final int COLOR_BLACK = 1;
    public static final int COLOR_BLUE = 5;
    public static final int COLOR_BLUSH_PINK = 8;
    public static final int COLOR_CARBON_METAL = 6;
    public static final int COLOR_CHARCOAL = 10;
    public static final int COLOR_CHARCOAL_GRAY = 10;
    public static final int COLOR_CLASSIC_WHITE = 2;
    public static final int COLOR_DEFAULT = 0;
    public static final int COLOR_GOLD = 7;
    public static final int COLOR_GRAYISH_BLUE = 9;
    public static final int COLOR_GREEN = 11;
    public static final int COLOR_INDIGO_BLUE = 5;
    public static final int COLOR_JET_BLACK = 1;
    public static final int COLOR_MAGENTA = 3;
    public static final int COLOR_MINT = 9;
    public static final int COLOR_MINT_BLUE = 9;
    public static final int COLOR_MUSTARD_YELLOW = 12;
    public static final int COLOR_NAVY = 4;
    public static final int COLOR_OATMEAL = 12;
    public static final int COLOR_OATMEAL_BEIGE = 12;
    public static final int COLOR_ORANGE = 13;
    public static final int COLOR_PEAKCOCK_GREEN = 11;
    public static final int COLOR_PEARL_WHITE = 2;
    public static final int COLOR_PINK = 8;
    public static final int COLOR_PLUM = 3;
    public static final int COLOR_PLUM_RED = 3;
    public static final int COLOR_ROSE_GOLD = 7;
    public static final int COLOR_SILVER = 6;
    public static final int COLOR_SOFT_PINK = 8;
    public static final int COLOR_WHITE = 2;
    public static final int COLOR_WILD_ORANGE = 13;
    public static final int COLOR_YELLOW = 12;
    public static final boolean COVER_ATTACHED = true;
    public static final boolean COVER_DETACHED = false;
    public static final int FOTA_MODE_NONE = 0;
    public static final int MODEL_DEFAULT = 0;
    public static final boolean SWITCH_STATE_COVER_CLOSE = false;
    public static final boolean SWITCH_STATE_COVER_OPEN = true;
    private static final String TAG = "ScoverState";
    public static final int TYPE_ALCANTARA_COVER = 12;
    public static final int TYPE_BRAND_MONBLANC_COVER = 100;
    public static final int TYPE_CLEAR_COVER = 8;
    public static final int TYPE_FLIP_COVER = 0;
    public static final int TYPE_HEALTH_COVER = 4;
    public static final int TYPE_KEYBOARD_KOR_COVER = 9;
    public static final int TYPE_KEYBOARD_US_COVER = 10;
    public static final int TYPE_LED_COVER = 7;
    public static final int TYPE_NEON_COVER = 11;
    public static final int TYPE_NFC_SMART_COVER = 255;
    public static final int TYPE_NONE = 2;
    public static final int TYPE_SVIEW_CHARGER_COVER = 3;
    public static final int TYPE_SVIEW_COVER = 1;
    public static final int TYPE_S_CHARGER_COVER = 5;
    public static final int TYPE_S_VIEW_WALLET_COVER = 6;
    public boolean attached;
    public int color;
    private boolean fakeCover;
    private int fotaMode;
    private int heightPixel;
    public int model;
    private boolean switchState;
    public int type;
    private int widthPixel;

    public ScoverState() {
        this.switchState = SWITCH_STATE_COVER_OPEN;
        this.type = TYPE_NONE;
        this.color = TYPE_FLIP_COVER;
        this.widthPixel = TYPE_FLIP_COVER;
        this.heightPixel = TYPE_FLIP_COVER;
        this.attached = SWITCH_STATE_COVER_CLOSE;
        this.model = TYPE_FLIP_COVER;
        this.fakeCover = SWITCH_STATE_COVER_CLOSE;
        this.fotaMode = TYPE_FLIP_COVER;
    }

    public ScoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = SWITCH_STATE_COVER_CLOSE;
        this.model = TYPE_FLIP_COVER;
        this.fakeCover = SWITCH_STATE_COVER_CLOSE;
        this.fotaMode = TYPE_FLIP_COVER;
    }

    public ScoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel, boolean attached) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
        this.model = TYPE_FLIP_COVER;
        this.fakeCover = SWITCH_STATE_COVER_CLOSE;
        this.fotaMode = TYPE_FLIP_COVER;
    }

    public ScoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel, boolean attached, int model) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
        this.model = model;
        this.fakeCover = SWITCH_STATE_COVER_CLOSE;
        this.fotaMode = TYPE_FLIP_COVER;
    }

    public ScoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel, boolean attached, int model, boolean fakeCover) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
        this.model = model;
        this.fakeCover = fakeCover;
        this.fotaMode = TYPE_FLIP_COVER;
    }

    public ScoverState(boolean switchState, int type, int color, int widthPixel, int heightPixel, boolean attached, int model, boolean fakeCover, int fotaMode) {
        this.switchState = switchState;
        this.type = type;
        this.color = color;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;
        this.attached = attached;
        this.model = model;
        this.fakeCover = fakeCover;
        this.fotaMode = fotaMode;
    }

    public boolean getSwitchState() {
        return this.switchState;
    }

    public int getType() {
        return this.type;
    }

    public int getColor() {
        return this.color;
    }

    public int getWindowWidth() {
        return this.widthPixel;
    }

    public int getWindowHeight() {
        return this.heightPixel;
    }

    public boolean getAttachState() {
        return this.attached;
    }

    public int getModel() {
        return this.model;
    }

    public boolean isFakeCover() {
        return this.fakeCover;
    }

    public int getFotaMode() {
        return this.fotaMode;
    }

    public String toString() {
        Object[] objArr = new Object[TYPE_CLEAR_COVER];
        objArr[TYPE_FLIP_COVER] = Boolean.valueOf(this.switchState);
        objArr[TYPE_SVIEW_COVER] = Integer.valueOf(this.type);
        objArr[TYPE_NONE] = Integer.valueOf(this.color);
        objArr[TYPE_SVIEW_CHARGER_COVER] = Integer.valueOf(this.widthPixel);
        objArr[TYPE_HEALTH_COVER] = Integer.valueOf(this.heightPixel);
        objArr[TYPE_S_CHARGER_COVER] = Boolean.valueOf(this.attached);
        objArr[TYPE_S_VIEW_WALLET_COVER] = Boolean.valueOf(this.fakeCover);
        objArr[TYPE_LED_COVER] = Integer.valueOf(this.fotaMode);
        return String.format("ScoverState(switchState=%b type=%d color=%d widthPixel=%d heightPixel=%d attached=%b fakeCover=%b fotaMode=%d)", objArr);
    }
}
