package com.samsung.android.sdk.look.airbutton;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.samsung.android.sdk.look.Slook;
import com.samsung.android.sdk.look.SlookResourceManager;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonAdapter.AirButtonItem;
import java.util.ArrayList;

public final class SlookAirButtonFrequentContactAdapter extends SlookAirButtonAdapter {
    public static final String DATA = "data";
    public static final String DISPLAY_NAME = "display_name";
    private static final String EMAIL = "vnd.android.cursor.item/email_v2";
    private static final String EXTRA_CONDITION = "extra_condition";
    private static final String FREQUENT_URI = "content://com.android.contacts/contacts/frequent_data";
    public static final String ID = "id";
    private static final String IS_PRIVATE = "is_private";
    public static final String LOOKUP_KEY = "lookup_key";
    private static final String MIME_TYPE = "MIME_TYPE";
    private static final String PHONE = "vnd.android.cursor.item/phone_v2";
    public static final String PHOTO = "photo";
    public static final String PHOTO_URI = "photo_uri";
    private static final String TAG = "AirButtonFrequentContactAdapter";
    private Context mContext;
    private int mCount = 0;
    private ArrayList<Bundle> mData = new ArrayList();
    private boolean mIsShowing = false;
    private int mMaxCount = 15;
    private boolean mNeedUpdate = true;
    private StringBuilder mSelectionSb;
    private Slook mSlook = new Slook();

    public SlookAirButtonFrequentContactAdapter(View view, Bundle option) {
        if (view == null && option == null) {
            throw new IllegalArgumentException("You should set the View and Bundle in Param");
        } else if (isSupport(1)) {
            if (option != null) {
                String mimeType = option.getString(MIME_TYPE);
                this.mSelectionSb = new StringBuilder();
                boolean isMimeType = false;
                if (mimeType != null) {
                    this.mSelectionSb.append("view_data.");
                    this.mSelectionSb.append("mimetype");
                    this.mSelectionSb.append("='");
                    this.mSelectionSb.append(mimeType);
                    this.mSelectionSb.append("'");
                    isMimeType = true;
                }
                boolean isPrivate = option.getBoolean("PRIVATE", false);
                if (!isPrivate) {
                    if (isMimeType) {
                        this.mSelectionSb.append(" and ");
                    }
                    this.mSelectionSb.append("is_private=0");
                }
                String selection = option.getString(EXTRA_CONDITION);
                if (selection != null) {
                    if (isMimeType || !isPrivate) {
                        this.mSelectionSb.append(" and ");
                    }
                    this.mSelectionSb.append(selection);
                }
            }
            this.mMaxCount = SlookResourceManager.getInt(2);
            if (view != null) {
                this.mContext = view.getContext();
                setEmptyText(SlookResourceManager.getText(this.mContext, 0));
            }
        }
    }

    public void onShow(View parentView) {
        updateData();
        this.mIsShowing = true;
        super.onShow(parentView);
    }

    public void onHide(View parentView) {
        this.mIsShowing = false;
        this.mNeedUpdate = true;
        this.mData.clear();
        this.mCount = 0;
        super.onHide(parentView);
    }

    public void onDismiss(View parentView) {
        this.mIsShowing = false;
        this.mNeedUpdate = true;
        this.mData.clear();
        this.mCount = 0;
        super.onDismiss(parentView);
    }

    public int getCount() {
        return this.mCount;
    }

    public AirButtonItem getItem(int idx) {
        Drawable drawable;
        Bundle bundle = (Bundle) this.mData.get(idx);
        String name = bundle.getString(DISPLAY_NAME);
        String data = bundle.getString(DATA);
        byte[] photo = bundle.getByteArray(PHOTO);
        if (photo == null) {
            drawable = this.mContext.getResources().getDrawable(SlookResourceManager.getDrawableId(1));
        } else {
            drawable = new BitmapDrawable(this.mContext.getResources(), BitmapFactory.decodeByteArray(photo, 0, photo.length));
        }
        return new AirButtonItem(drawable, name, data, bundle);
    }

    private synchronized void updateData() {
        if (this.mNeedUpdate && !this.mIsShowing) {
            this.mNeedUpdate = false;
            this.mData.clear();
            this.mCount = 0;
            String[] PROJECTION = new String[]{"_id", DISPLAY_NAME, PHOTO_URI, "data15", "lookup", "mimetype", "data1", IS_PRIVATE};
            Cursor c = this.mContext.getContentResolver().query(Uri.parse(FREQUENT_URI), PROJECTION, this.mSelectionSb != null ? this.mSelectionSb.toString() : null, null, "_id LIMIT " + this.mMaxCount);
            if (c != null) {
                while (c.moveToNext()) {
                    try {
                        boolean z;
                        Bundle bundle = new Bundle();
                        bundle.putInt(ID, c.getInt(0));
                        bundle.putString(DISPLAY_NAME, c.getString(1));
                        bundle.putString(PHOTO_URI, c.getString(2));
                        bundle.putByteArray(PHOTO, c.getBlob(3));
                        bundle.putString(LOOKUP_KEY, c.getString(4));
                        bundle.putString(MIME_TYPE, c.getString(5));
                        bundle.putString(DATA, c.getString(6));
                        String str = IS_PRIVATE;
                        if (c.getInt(7) == 1) {
                            z = true;
                        } else {
                            z = false;
                        }
                        bundle.putBoolean(str, z);
                        this.mData.add(bundle);
                    } catch (NoSuchMethodError e) {
                        e.printStackTrace();
                    } catch (Throwable th) {
                        if (c != null) {
                            c.close();
                        }
                    }
                }
                if (c != null) {
                    c.close();
                }
                this.mCount = this.mData.size();
            }
        }
    }

    private boolean isSupport(int ver) {
        if (this.mSlook.isFeatureEnabled(1)) {
            return true;
        }
        return false;
    }
}
