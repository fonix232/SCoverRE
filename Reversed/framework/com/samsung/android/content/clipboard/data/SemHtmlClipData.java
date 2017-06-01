package com.samsung.android.content.clipboard.data;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.util.ClipboardDataBitmapUrl;
import android.sec.clipboard.util.ClipboardProcText;
import android.sec.clipboard.util.Log;
import android.text.Html;
import android.text.TextUtils;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.io.File;

public class SemHtmlClipData extends SemClipData {
    private static final String TAG = "SemHtmlClipData";
    private static final String regex = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";
    private static final String regex2 = "(?i)<[^/bpd][^>]*>|<p[a-z][^>]*>|<br[a-z][^>]*>|<d[^i][^v][^>]*>|<div[a-z][^>]*>|</[^bpd]+?>|</p[a-z]+>|</br[a-z]+>|</d[^i][^v]+>|</div[a-z]+>";
    private static final long serialVersionUID = 1;
    private String mHtml = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    private String mPlainText = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    private Bitmap mThumbnailBitmap = null;
    private String mThumbnailImagePath = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;

    public SemHtmlClipData() {
        super(4);
    }

    public SemHtmlClipData(Parcel parcel) {
        super(parcel);
        readFromSource(parcel);
    }

    private void setClipData() {
        setClipData(new ClipData(null, new String[]{"text/html"}, new Item(this.mPlainText, this.mHtml, null, Uri.fromFile(new File(this.mThumbnailImagePath)))));
    }

    public boolean equals(Object obj) {
        boolean z = false;
        Log.secI(TAG, "html equals");
        if (!super.equals(obj) || !(obj instanceof SemHtmlClipData)) {
            return false;
        }
        if (this.mHtml.compareTo(obj.getHtml()) == 0) {
            z = true;
        }
        return z;
    }

    public ClipData getClipData() {
        if (this.mClipData == null) {
            setClipData();
        }
        return this.mClipData;
    }

    protected ClipData getClipDataInternal() {
        if (this.mClipData == null) {
            setClipData();
        }
        return this.mClipData;
    }

    public String getHtml() {
        return this.mHtml;
    }

    public ParcelFileDescriptor getParcelFileDescriptor() {
        ParcelFileDescriptor parcelFileDescriptor = super.getParcelFileDescriptor();
        if (parcelFileDescriptor != null) {
            return parcelFileDescriptor;
        }
        if (TextUtils.isEmpty(this.mThumbnailImagePath)) {
            return null;
        }
        try {
            return ParcelFileDescriptor.open(new File(this.mThumbnailImagePath), 268435456);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPlainText() {
        return this.mPlainText;
    }

    public Bitmap getThumbnailBitmap(int i, int i2) {
        if (this.mThumbnailBitmap != null) {
            return this.mThumbnailBitmap;
        }
        Bitmap bitmap = null;
        if (this.mHtml.length() < 1) {
            Log.secW(TAG, "getThumbnailBitmap : Data is empty.");
            return null;
        }
        String str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        try {
            str = Html.fromHtml(Uri.decode(ClipboardProcText.getImgFileNameFromHtml(this.mHtml.toString()))).toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (str == null || str.length() >= 1) {
            if (str == null || str.length() <= 7 || str.substring(0, 7).compareTo("http://") != 0) {
                bitmap = (str == null || str.length() <= 7 || str.substring(0, 7).compareTo("file://") != 0) ? ClipboardDataBitmapUrl.getFilePathBitmap(str, i2, i2) : ClipboardDataBitmapUrl.getFilePathBitmap(str.substring(7, str.length()), i, i2);
            }
            this.mThumbnailBitmap = bitmap;
            return bitmap;
        }
        Log.secW(TAG, "getThumbnailBitmap : FileName is empty.");
        return null;
    }

    public ParcelFileDescriptor getThumbnailFileDescriptor() {
        return getParcelFileDescriptor();
    }

    public String getThumbnailImagePath() {
        return this.mThumbnailImagePath;
    }

    protected void readFromSource(Parcel parcel) {
        try {
            this.mHtml = parcel.readString();
            this.mPlainText = parcel.readString();
            this.mThumbnailBitmap = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
            this.mThumbnailImagePath = parcel.readString();
        } catch (Throwable e) {
            Log.secI(TAG, "readFromSource~Exception :" + e.getMessage());
        }
    }

    public boolean setAlternateClipData(int i, SemClipData semClipData) {
        if (!super.setAlternateClipData(i, semClipData) || this.mHtml.length() < 1) {
            return false;
        }
        boolean text;
        switch (i) {
            case 1:
                text = ((SemTextClipData) semClipData).setText(this.mPlainText);
                break;
            case 4:
                ((SemHtmlClipData) semClipData).setHtmlWithImagePathInternal(this.mPlainText, this.mHtml, this.mThumbnailImagePath.toString());
                if (this.mHtml.length() <= 0) {
                    text = false;
                    break;
                }
                text = true;
                break;
            default:
                text = false;
                break;
        }
        return text;
    }

    protected void setClipboardDataHtml(String str, String str2, Bitmap bitmap, String str3) {
        this.mHtml = str;
        this.mPlainText = str2;
        this.mThumbnailBitmap = bitmap;
        this.mThumbnailImagePath = str3;
    }

    public boolean setHtml(CharSequence charSequence) {
        return setHtmlInternal(this.mPlainText, charSequence);
    }

    public boolean setHtmlInternal(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence2 == null || charSequence2.toString().length() == 0) {
            return false;
        }
        if (charSequence2.length() > 131072) {
            charSequence2 = charSequence2.subSequence(0, 131072);
        }
        this.mHtml = charSequence2.toString();
        Log.secD(TAG, this.mHtml);
        if (charSequence == null || charSequence.toString().length() <= 0) {
            this.mPlainText = this.mHtml.replaceAll(regex2, MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
            this.mPlainText = Html.fromHtml(this.mPlainText).toString();
        } else {
            this.mPlainText = charSequence.toString();
        }
        Log.secD(TAG, this.mPlainText.toString());
        if (this.mThumbnailBitmap != null) {
            this.mThumbnailBitmap = null;
        }
        return true;
    }

    public boolean setHtmlWithImagePath(CharSequence charSequence, CharSequence charSequence2) {
        if (!setHtmlInternal(this.mPlainText, charSequence)) {
            return false;
        }
        if (charSequence2 == null || charSequence2.length() < 1) {
            Log.secI(TAG, "filePath is null");
            return false;
        }
        this.mThumbnailImagePath = charSequence2.toString();
        File file = new File(charSequence2.toString());
        ParcelFileDescriptor parcelFileDescriptor = super.getParcelFileDescriptor();
        if (parcelFileDescriptor == null || !parcelFileDescriptor.getFileDescriptor().valid()) {
            if (ClipboardConstants.DEBUG) {
                Log.m3e(TAG, "setHtmlWithImagePath : value is no file descriptor ..check plz");
            }
            return false;
        }
        Log.secI(TAG, "setHtmlWithImagePath : value is GOOD file path.");
        return true;
    }

    public boolean setHtmlWithImagePathInternal(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        if (charSequence != null && charSequence.toString().length() > 0) {
            this.mPlainText = charSequence.toString();
        }
        return setHtmlWithImagePath(charSequence2, charSequence3);
    }

    public boolean setThumbnailImagePath(String str) {
        if (str == null || str.length() < 1) {
            return false;
        }
        this.mThumbnailImagePath = str;
        File file = new File(str);
        ParcelFileDescriptor parcelFileDescriptor = getParcelFileDescriptor();
        if (parcelFileDescriptor != null && parcelFileDescriptor.getFileDescriptor().valid()) {
            return true;
        }
        if (ClipboardConstants.DEBUG) {
            Log.m3e(TAG, "ClipboardDataHtml : value is no file descriptor ..check plz");
        }
        return false;
    }

    public String toString() {
        return "SemHtmlClipData class. Value is " + (this.mHtml.length() > 20 ? this.mHtml.subSequence(0, 20) : this.mHtml);
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.secI(TAG, "html write to parcel");
        parcel.writeInt(4);
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mHtml);
        parcel.writeString(this.mPlainText);
        parcel.writeParcelable(this.mThumbnailBitmap, i);
        parcel.writeString(this.mThumbnailImagePath);
    }
}
