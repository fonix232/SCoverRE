package com.samsung.android.content.clipboard.data;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.util.Log;
import android.text.TextUtils;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.io.File;
import java.io.FileFilter;

public class SemUriClipData extends SemClipData {
    private static final String TAG = "SemUriClipData";
    private static final long serialVersionUID = 1;
    private String mThumbnailFilePath = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    private String mValue = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;

    private static class ImageFileFilter implements FileFilter {
        private final String[] extensions;

        private ImageFileFilter() {
            this.extensions = new String[]{"jpg", "png", "gif", "jpeg"};
        }

        public boolean accept(File file) {
            if (file == null) {
                return false;
            }
            for (String endsWith : this.extensions) {
                if (file.getName().toLowerCase().endsWith(endsWith)) {
                    return true;
                }
            }
            return false;
        }
    }

    public SemUriClipData() {
        super(16);
    }

    public SemUriClipData(Parcel parcel) {
        super(parcel);
        readFromSource(parcel);
    }

    private void setClipData() {
        setClipData(new ClipData(ClipboardConstants.MULTIWINDOW_DRAGNDROP, new String[]{"text/uri-list"}, new Item(Uri.parse(this.mValue))));
    }

    public boolean equals(Object obj) {
        boolean z = false;
        Log.secI(TAG, "uri equals");
        if (!super.equals(obj) || !(obj instanceof SemUriClipData)) {
            return false;
        }
        if (this.mValue.toString().compareTo(obj.getUri().toString()) == 0) {
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

    public ParcelFileDescriptor getParcelFileDescriptor() {
        ParcelFileDescriptor parcelFileDescriptor = super.getParcelFileDescriptor();
        if (parcelFileDescriptor != null) {
            return parcelFileDescriptor;
        }
        if (TextUtils.isEmpty(this.mThumbnailFilePath)) {
            return null;
        }
        try {
            return ParcelFileDescriptor.open(new File(this.mThumbnailFilePath), 268435456);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getThumbnailPath() {
        return this.mThumbnailFilePath;
    }

    public Uri getUri() {
        return Uri.parse(this.mValue);
    }

    public boolean isImagefile() {
        Uri uri = getUri();
        if (uri == null || !"file".equals(uri.getScheme())) {
            return false;
        }
        return new ImageFileFilter().accept(new File(uri.getPath()));
    }

    public boolean isImagefile(File file) {
        return file != null ? new ImageFileFilter().accept(file) : false;
    }

    protected void readFromSource(Parcel parcel) {
        this.mValue = parcel.readString();
        this.mThumbnailFilePath = parcel.readString();
    }

    public boolean setAlternateClipData(int i, SemClipData semClipData) {
        if (!super.setAlternateClipData(i, semClipData) || this.mValue.length() < 1) {
            return false;
        }
        boolean z;
        switch (i) {
            case 16:
                if (!(semClipData instanceof SemUriClipData)) {
                    z = false;
                    break;
                }
                SemClipData semClipData2 = semClipData;
                z = semClipData2.setUri(Uri.parse(this.mValue));
                if (this.mThumbnailFilePath.length() > 1) {
                    z &= semClipData2.setThumbnailPath(this.mThumbnailFilePath);
                    break;
                }
                break;
            default:
                z = false;
                break;
        }
        return z;
    }

    public boolean setThumbnailPath(String str) {
        Log.secI(TAG, "setPreviewImgPath :" + str);
        boolean z = false;
        if (str == null || str.length() < 1) {
            return false;
        }
        File file = new File(str);
        if (file.isFile() && isImagefile(file)) {
            this.mThumbnailFilePath = str;
            z = true;
        } else {
            this.mThumbnailFilePath = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            Log.secE(TAG, "SemUriClipData : value is no file path or not image file");
        }
        return z;
    }

    public boolean setUri(Uri uri) {
        if (uri == null || uri.toString().length() == 0) {
            return false;
        }
        this.mValue = uri.toString();
        return true;
    }

    public String toString() {
        return "SemUriClipData class. Value is " + (this.mValue.length() > 20 ? this.mValue.subSequence(0, 20) : this.mValue);
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.secI(TAG, "Uri write to parcel");
        parcel.writeInt(16);
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mValue);
        parcel.writeString(this.mThumbnailFilePath);
    }
}
