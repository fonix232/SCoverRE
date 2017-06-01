package com.samsung.android.cocktailbar;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CocktailBarStateInfo implements Parcelable, Cloneable {
    @Deprecated
    public static final int BACKGROUND_DIM = 2;
    @Deprecated
    public static final int BACKGROUND_OPAQUE = 1;
    @Deprecated
    public static final int BACKGROUND_TRANSPARENT = 3;
    @Deprecated
    public static final int BACKGROUND_UNKNOWN = 0;
    public static final Creator<CocktailBarStateInfo> CREATOR = new C10121();
    public static final int FLAG_CHANGE_ACTIVATE = 64;
    public static final int FLAG_CHANGE_BACKGROUND_TYPE = 2;
    public static final int FLAG_CHANGE_LOCK_STATE = 8;
    public static final int FLAG_CHANGE_MODE = 16;
    public static final int FLAG_CHANGE_POSITION = 4;
    public static final int FLAG_CHANGE_SHOW_TIMEOUT = 32;
    public static final int FLAG_CHANGE_VISIBILITY = 1;
    public static final int FLAG_CHANGE_WINDOW_TYPE = 128;
    public static final int LOCK_STATE_HIDE = 2;
    public static final int LOCK_STATE_NONE = 0;
    public static final int LOCK_STATE_RESTRICT = 4;
    public static final int LOCK_STATE_SHOW = 1;
    @Deprecated
    public static final int MODE_IMMERSIVE = 2;
    @Deprecated
    public static final int MODE_MULTITASKING = 1;
    @Deprecated
    public static final int MODE_UNKNOWN = 0;
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
    public boolean activate = true;
    public int background = 0;
    public int backgroundType = 0;
    public int changeFlag = 0;
    public int lockState = 0;
    public int mode = 0;
    public int position = 0;
    public int showTimeout = -1;
    public int visibility;
    public int windowType = 0;

    static class C10121 implements Creator<CocktailBarStateInfo> {
        C10121() {
        }

        public CocktailBarStateInfo createFromParcel(Parcel parcel) {
            return new CocktailBarStateInfo(parcel);
        }

        public CocktailBarStateInfo[] newArray(int i) {
            return new CocktailBarStateInfo[i];
        }
    }

    public CocktailBarStateInfo(int i) {
        this.visibility = i;
    }

    public CocktailBarStateInfo(Parcel parcel) {
        boolean z = true;
        this.visibility = parcel.readInt();
        this.background = parcel.readInt();
        this.backgroundType = parcel.readInt();
        this.position = parcel.readInt();
        this.lockState = parcel.readInt();
        this.mode = parcel.readInt();
        this.showTimeout = parcel.readInt();
        if (parcel.readInt() != 1) {
            z = false;
        }
        this.activate = z;
        this.windowType = parcel.readInt();
        this.changeFlag = parcel.readInt();
    }

    public CocktailBarStateInfo(CocktailBarStateInfo cocktailBarStateInfo) {
        this.visibility = cocktailBarStateInfo.visibility;
        this.background = cocktailBarStateInfo.background;
        this.backgroundType = cocktailBarStateInfo.backgroundType;
        this.position = cocktailBarStateInfo.position;
        this.lockState = cocktailBarStateInfo.lockState;
        this.showTimeout = cocktailBarStateInfo.showTimeout;
        this.activate = cocktailBarStateInfo.activate;
        this.windowType = cocktailBarStateInfo.windowType;
    }

    public CocktailBarStateInfo clone() {
        Parcel obtain = Parcel.obtain();
        writeToParcel(obtain, 0);
        obtain.setDataPosition(0);
        CocktailBarStateInfo cocktailBarStateInfo = new CocktailBarStateInfo(obtain);
        obtain.recycle();
        return cocktailBarStateInfo;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.visibility);
        parcel.writeInt(this.background);
        parcel.writeInt(this.backgroundType);
        parcel.writeInt(this.position);
        parcel.writeInt(this.lockState);
        parcel.writeInt(this.mode);
        parcel.writeInt(this.showTimeout);
        parcel.writeInt(this.activate ? 1 : 0);
        parcel.writeInt(this.windowType);
        parcel.writeInt(this.changeFlag);
    }
}
