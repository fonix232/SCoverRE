package com.samsung.android.app.ledcover.info;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import com.samsung.android.app.ledcover.common.LetterTileDrawable;

public class ContactInfo {
    private Drawable mAvatar;
    private String mContactId;
    private String mDisplayName;
    private boolean mIsChecked;

    public ContactInfo(Context context, String contact_id, String display_name, byte[] data15, boolean mIsChecked) {
        this.mContactId = contact_id;
        this.mDisplayName = display_name;
        this.mAvatar = getRoundedDrawable(context, data15);
        this.mIsChecked = mIsChecked;
    }

    public String getID() {
        return this.mContactId;
    }

    public String getName() {
        return this.mDisplayName;
    }

    public Drawable getIconBitmap() {
        return this.mAvatar;
    }

    public boolean getIsChecked() {
        return this.mIsChecked;
    }

    public void setIsChecked(boolean check) {
        this.mIsChecked = check;
    }

    private Drawable getRoundedDrawable(Context context, byte[] bitmapData) {
        if (bitmapData == null || bitmapData.length <= 0) {
            RoundedBitmapDrawable drawable = new LetterTileDrawable(context.getResources());
            drawable.setContactDetails(this.mDisplayName);
            return drawable;
        }
        drawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length));
        drawable.setCircular(true);
        return drawable;
    }
}
