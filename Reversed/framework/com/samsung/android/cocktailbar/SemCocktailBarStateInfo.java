package com.samsung.android.cocktailbar;

public class SemCocktailBarStateInfo {
    @Deprecated
    public static final int BACKGROUND_DIM = 2;
    @Deprecated
    public static final int BACKGROUND_OPAQUE = 1;
    @Deprecated
    public static final int BACKGROUND_TRANSPARENT = 3;
    @Deprecated
    public static final int BACKGROUND_UNKNOWN = 0;
    public static final int POSITION_BOTTOM = 4;
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_RIGHT = 2;
    public static final int POSITION_TOP = 3;
    public static final int POSITION_UNKNOWN = 0;
    public static final int STATE_INVISIBLE = 2;
    public static final int STATE_VISIBLE = 1;
    public static final int WINDOW_TYPE_FULLSCREEN = 2;
    public static final int WINDOW_TYPE_MINIMIZE = 1;
    public static final int WINDOW_TYPE_UNKNOWN = 0;
    public int background = 0;
    public int position = 0;
    public int visibility;
    public int windowType = 0;
}
