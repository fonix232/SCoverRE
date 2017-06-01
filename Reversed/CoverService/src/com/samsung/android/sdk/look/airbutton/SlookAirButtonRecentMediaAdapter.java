package com.samsung.android.sdk.look.airbutton;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;
import android.view.View;
import com.samsung.android.sdk.look.Slook;
import com.samsung.android.sdk.look.SlookResourceManager;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonAdapter.AirButtonItem;
import java.util.ArrayList;

public final class SlookAirButtonRecentMediaAdapter extends SlookAirButtonAdapter {
    public static final String AUDIO_TYPE = "audio";
    private static final String ID = "id";
    public static final String IMAGE_TYPE = "image";
    private static final String MIME_TYPE = "mime_type";
    private static final String ORIENTATION = "orientation";
    private static final String TAG = "AirButtonRecentMediaAdapter";
    private static final String TITLE = "title";
    public static final String VIDEO_TYPE = "video";
    private static final String VOLUME = "external";
    private Context mContext;
    private int mCount = 0;
    private ArrayList<Bundle> mData = new ArrayList();
    private String mFilter;
    private boolean mIsShowing = false;
    private int mMaxCount = 15;
    private boolean mNeedUpdate = true;
    private Slook mSlook = new Slook();

    public SlookAirButtonRecentMediaAdapter(View view, Bundle option) {
        if (view == null && option == null) {
            throw new IllegalArgumentException("You should set the View and Bundle in Param");
        } else if (isSupport(1)) {
            this.mFilter = getFilter(option);
            if (view != null) {
                this.mContext = view.getContext();
                this.mMaxCount = SlookResourceManager.getInt(3);
                setEmptyText(SlookResourceManager.getText(this.mContext, 1));
            }
        }
    }

    public AirButtonItem getItem(int idx) {
        Bundle bundle = (Bundle) this.mData.get(idx);
        int id = bundle.getInt("id");
        int media_type = bundle.getInt(MIME_TYPE);
        return new AirButtonItem(getThumbNail(id, media_type, bundle.getString(TITLE), bundle.getInt(ORIENTATION)), null, getUri(id, media_type));
    }

    public int getCount() {
        return this.mCount;
    }

    private Drawable getThumbNail(int id, int media_type, String title, int orientation) {
        Drawable dr = null;
        switch (media_type) {
            case 1:
                new Options().inSampleSize = 2;
                Bitmap bitmap = Thumbnails.getThumbnail(this.mContext.getContentResolver(), (long) id, 1, null);
                if (bitmap != null) {
                    if (orientation == 90 || orientation == 180 || orientation == 270) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate((float) orientation);
                        dr = new BitmapDrawable(this.mContext.getResources(), Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
                    } else {
                        dr = new BitmapDrawable(this.mContext.getResources(), bitmap);
                    }
                }
                return dr;
            case 2:
                return getAudioDrawable(title);
            case 3:
                return new BitmapDrawable(this.mContext.getResources(), Video.Thumbnails.getThumbnail(this.mContext.getContentResolver(), (long) id, 3, null));
            default:
                return null;
        }
    }

    private Drawable getAudioDrawable(String title) {
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable dr = this.mContext.getResources().getDrawable(SlookResourceManager.getDrawableId(2));
        if (dr != null) {
            dr.setBounds(new Rect(0, 0, 200, 200));
            canvas.drawColor(-2236963, Mode.SRC);
            dr.draw(canvas);
        }
        return new BitmapDrawable(this.mContext.getResources(), bitmap);
    }

    private Uri getUri(int id, int media_type) {
        switch (media_type) {
            case 1:
                return ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, (long) id);
            case 2:
                return ContentUris.withAppendedId(Audio.Media.EXTERNAL_CONTENT_URI, (long) id);
            case 3:
                return ContentUris.withAppendedId(Video.Media.EXTERNAL_CONTENT_URI, (long) id);
            default:
                return null;
        }
    }

    private String getFilter(Bundle option) {
        boolean bAdded = false;
        StringBuilder str = new StringBuilder();
        if (option == null || option.getBoolean(IMAGE_TYPE)) {
            str.append("media_type = ");
            str.append(1);
            str.append(" and (");
            str.append("is_drm");
            str.append("=0 or ");
            str.append("is_drm");
            str.append(" is null)");
            bAdded = true;
        }
        if (option == null || option.getBoolean(VIDEO_TYPE)) {
            if (bAdded) {
                str.append(" or ");
            }
            str.append("media_type = ");
            str.append(3);
            bAdded = true;
        }
        if (option == null || option.getBoolean(AUDIO_TYPE)) {
            if (bAdded) {
                str.append(" or ");
            }
            str.append("media_type = ");
            str.append(2);
        }
        String filter = str.toString();
        if (filter == null || filter.length() <= 0) {
            return null;
        }
        return filter;
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
        super.onHide(parentView);
    }

    public void onDismiss(View parentView) {
        this.mIsShowing = false;
        this.mNeedUpdate = true;
        this.mData.clear();
        super.onDismiss(parentView);
    }

    private synchronized void updateData() {
        if (this.mNeedUpdate && !this.mIsShowing) {
            this.mNeedUpdate = false;
            this.mData.clear();
            Cursor c = null;
            try {
                c = this.mContext.getContentResolver().query(Files.getContentUri(VOLUME), new String[]{"_id", "media_type", TITLE, ORIENTATION}, this.mFilter, null, "date_modified DESC LIMIT " + this.mMaxCount);
                if (c != null) {
                    while (c.moveToNext()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", c.getInt(0));
                        bundle.putInt(MIME_TYPE, c.getInt(1));
                        bundle.putInt(ORIENTATION, c.getInt(3));
                        this.mData.add(bundle);
                    }
                    if (c != null) {
                        c.close();
                    }
                    this.mCount = this.mData.size();
                } else if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
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
