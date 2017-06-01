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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SemImageClipData extends SemClipData {
    private static final String TAG = "SemImageClipData";
    private static final long serialVersionUID = 1;
    private String mExtraDataPath = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    private transient ParcelFileDescriptor mExtraParcelFd = null;
    private String mImagePath = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    private String mInitBaseValue = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    private boolean mInitBaseValueCheck = true;

    public SemImageClipData() {
        super(2);
    }

    public SemImageClipData(Parcel parcel) {
        super(parcel);
        readFromSource(parcel);
    }

    private boolean compareFile(FileInputStream fileInputStream, FileInputStream fileInputStream2) {
        boolean z = false;
        try {
            int size = (int) fileInputStream.getChannel().size();
            int size2 = (int) fileInputStream2.getChannel().size();
            if (size != size2 || size < 1 || size2 < 1) {
                try {
                    fileInputStream.close();
                    fileInputStream2.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                }
                return false;
            }
            int i = size <= 128 ? size : 128;
            int i2 = size / i;
            int i3 = i2 >= 5 ? 5 : i2;
            int i4 = (size - (i * i3)) / i3;
            int i5 = 0;
            byte[] bArr = new byte[i];
            byte[] bArr2 = new byte[i];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(fileInputStream2);
            for (int i6 = 0; i6 < i3; i6++) {
                bufferedInputStream.read(bArr, 0, i);
                bufferedInputStream2.read(bArr2, 0, i);
                i5 += i + i4;
                bufferedInputStream.skip((long) i5);
                bufferedInputStream2.skip((long) i5);
                for (int i7 = 0; i7 < i; i7++) {
                    z = bArr[i7] == bArr2[i7];
                }
            }
            try {
                fileInputStream.close();
                fileInputStream2.close();
            } catch (Throwable e2) {
                e2.printStackTrace();
            } catch (Throwable th2) {
            }
            return z;
            return z;
        } catch (Throwable e3) {
            e3.printStackTrace();
            z = false;
            try {
                fileInputStream.close();
                fileInputStream2.close();
            } catch (Throwable e22) {
                e22.printStackTrace();
            } catch (Throwable th3) {
            }
        } catch (Throwable th4) {
            try {
                fileInputStream.close();
                fileInputStream2.close();
            } catch (Throwable e222) {
                e222.printStackTrace();
            } catch (Throwable th5) {
            }
        }
    }

    private boolean compareFile(String str, FileDescriptor fileDescriptor) {
        Throwable e;
        Throwable th;
        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2 = null;
        boolean z = false;
        try {
            FileInputStream fileInputStream3 = new FileInputStream(str);
            try {
                FileInputStream fileInputStream4 = new FileInputStream(fileDescriptor);
                try {
                    z = compareFile(fileInputStream3, fileInputStream4);
                    if (fileInputStream3 != null) {
                        try {
                            fileInputStream3.close();
                        } catch (Throwable e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (fileInputStream4 != null) {
                        fileInputStream4.close();
                    }
                    fileInputStream2 = fileInputStream4;
                    fileInputStream = fileInputStream3;
                } catch (FileNotFoundException e3) {
                    e = e3;
                    fileInputStream2 = fileInputStream4;
                    fileInputStream = fileInputStream3;
                    try {
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable e222) {
                                e222.printStackTrace();
                                throw th;
                            }
                        }
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream2 = fileInputStream4;
                    fileInputStream = fileInputStream3;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (fileInputStream2 != null) {
                        fileInputStream2.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                e = e4;
                fileInputStream = fileInputStream3;
                e.printStackTrace();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                return z;
            } catch (Throwable th4) {
                th = th4;
                fileInputStream = fileInputStream3;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileInputStream2 != null) {
                fileInputStream2.close();
            }
            return z;
        }
        return z;
    }

    private boolean compareFile(String str, String str2) {
        boolean compareFile;
        Throwable e;
        Throwable th;
        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2 = null;
        try {
            FileInputStream fileInputStream3 = new FileInputStream(str);
            try {
                FileInputStream fileInputStream4 = new FileInputStream(str2);
                try {
                    compareFile = compareFile(fileInputStream3, fileInputStream4);
                    if (fileInputStream3 != null) {
                        try {
                            fileInputStream3.close();
                        } catch (Throwable e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (fileInputStream4 != null) {
                        fileInputStream4.close();
                    }
                    fileInputStream2 = fileInputStream4;
                    fileInputStream = fileInputStream3;
                } catch (FileNotFoundException e3) {
                    e = e3;
                    fileInputStream2 = fileInputStream4;
                    fileInputStream = fileInputStream3;
                    try {
                        e.printStackTrace();
                        compareFile = str.equals(str2);
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return compareFile;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable e222) {
                                e222.printStackTrace();
                                throw th;
                            }
                        }
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream2 = fileInputStream4;
                    fileInputStream = fileInputStream3;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (fileInputStream2 != null) {
                        fileInputStream2.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                e = e4;
                fileInputStream = fileInputStream3;
                e.printStackTrace();
                compareFile = str.equals(str2);
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                return compareFile;
            } catch (Throwable th4) {
                th = th4;
                fileInputStream = fileInputStream3;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            compareFile = str.equals(str2);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileInputStream2 != null) {
                fileInputStream2.close();
            }
            return compareFile;
        }
        return compareFile;
    }

    private void setClipData() {
        setClipData(new ClipData(ClipboardConstants.CLIPBOARD_DRAGNDROP, new String[]{"text/uri-list"}, new Item(Uri.fromFile(new File(this.mImagePath)))));
    }

    public boolean HasExtraData() {
        return this.mExtraDataPath != null && this.mExtraDataPath.length() >= 1;
    }

    public boolean equals(Object obj) {
        Log.secI(TAG, "bitmap equals");
        boolean z = false;
        if (!super.equals(obj) || !(obj instanceof SemImageClipData)) {
            return false;
        }
        SemImageClipData semImageClipData = obj;
        String bitmapPath = semImageClipData.getBitmapPath();
        String initBasePath = semImageClipData.getInitBasePath();
        if (initBasePath != null && initBasePath.compareTo(this.mInitBaseValue) == 0) {
            ParcelFileDescriptor parcelFileDescriptor = semImageClipData.getParcelFileDescriptor();
            if (parcelFileDescriptor != null) {
                if (compareFile(this.mImagePath, parcelFileDescriptor.getFileDescriptor())) {
                    z = true;
                    Log.secE(TAG, "bitmap equals");
                }
            } else if (compareFile(this.mImagePath, bitmapPath)) {
                z = true;
                Log.secE(TAG, "bitmap equals");
            }
        }
        return z;
    }

    public String getBitmapPath() {
        return this.mImagePath;
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

    public String getExtraDataPath() {
        return this.mExtraDataPath;
    }

    public ParcelFileDescriptor getExtraParcelFileDescriptor() {
        if (this.mExtraParcelFd != null) {
            return this.mExtraParcelFd;
        }
        if (TextUtils.isEmpty(this.mExtraDataPath)) {
            return null;
        }
        try {
            return ParcelFileDescriptor.open(new File(this.mExtraDataPath), 268435456);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public ParcelFileDescriptor getImageFileDescriptor() {
        return getParcelFileDescriptor();
    }

    public String getInitBasePath() {
        return this.mInitBaseValue;
    }

    public ParcelFileDescriptor getParcelFileDescriptor() {
        ParcelFileDescriptor parcelFileDescriptor = super.getParcelFileDescriptor();
        if (parcelFileDescriptor != null) {
            return parcelFileDescriptor;
        }
        if (TextUtils.isEmpty(this.mImagePath)) {
            return null;
        }
        try {
            return ParcelFileDescriptor.open(new File(this.mImagePath), 268435456);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void readFromSource(Parcel parcel) {
        boolean z = false;
        try {
            this.mImagePath = parcel.readString();
            this.mInitBaseValue = parcel.readString();
            if (parcel.readByte() != (byte) 0) {
                z = true;
            }
            this.mInitBaseValueCheck = z;
            this.mExtraDataPath = parcel.readString();
            this.mExtraParcelFd = (ParcelFileDescriptor) parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        } catch (Throwable e) {
            Log.secI(TAG, "readFromSource~Exception :" + e.getMessage());
        }
    }

    public boolean setAlternateClipData(int i, SemClipData semClipData) {
        if (!super.setAlternateClipData(i, semClipData) || this.mImagePath == null) {
            return false;
        }
        boolean bitmapPath;
        switch (i) {
            case 2:
                ((SemImageClipData) semClipData).setExtraParcelFileDescriptor(this.mExtraParcelFd);
                bitmapPath = ((SemImageClipData) semClipData).setBitmapPath(getBitmapPath(), getExtraDataPath());
                break;
            default:
                bitmapPath = false;
                break;
        }
        return bitmapPath;
    }

    public boolean setBitmapPath(String str, String str2) {
        Log.secI(TAG, "setBitmapPath");
        boolean z = false;
        if (str == null || str.length() < 1) {
            return false;
        }
        if (this.mInitBaseValueCheck) {
            this.mInitBaseValue = str;
            this.mInitBaseValueCheck = false;
        }
        this.mImagePath = str;
        if (str2 != null && str2.length() > 0) {
            Log.secI(TAG, "ExtraDataPath =" + str2);
            this.mExtraDataPath = str2;
        }
        ParcelFileDescriptor parcelFileDescriptor = super.getParcelFileDescriptor();
        if (parcelFileDescriptor != null && parcelFileDescriptor.getFileDescriptor().valid()) {
            z = true;
            if (!(this.mExtraParcelFd == null || this.mExtraParcelFd.getFileDescriptor().valid())) {
                this.mExtraParcelFd = null;
            }
        }
        return z;
    }

    public boolean setExtraDataPath(String str) {
        boolean z = false;
        if (str == null || str.length() < 1) {
            return false;
        }
        this.mExtraDataPath = str;
        if (new File(str).isFile()) {
            z = true;
        } else {
            Log.secE(TAG, "ClipboardDataBitmap : ExtraDataPath is no file path ..check plz");
        }
        return z;
    }

    public void setExtraParcelFileDescriptor(ParcelFileDescriptor parcelFileDescriptor) {
        this.mExtraParcelFd = parcelFileDescriptor;
    }

    public boolean setImagePath(String str) {
        boolean z = false;
        if (str == null || str.length() < 1) {
            return false;
        }
        if (this.mInitBaseValueCheck) {
            this.mInitBaseValue = str;
            this.mInitBaseValueCheck = false;
        }
        this.mImagePath = str;
        if (new File(str).isFile()) {
            z = true;
        } else {
            Log.secE(TAG, "ClipboardDataBitmap : value is no file path ..check plz");
        }
        return z;
    }

    public String toString() {
        return "SemImageClipData class. Value is " + (this.mImagePath.length() > 20 ? this.mImagePath.subSequence(0, 20) : this.mImagePath);
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.secI(TAG, "Bitmap write to parcel");
        parcel.writeInt(2);
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mImagePath);
        parcel.writeString(this.mInitBaseValue);
        parcel.writeByte((byte) (this.mInitBaseValueCheck ? 1 : 0));
        parcel.writeString(this.mExtraDataPath);
        parcel.writeParcelable(this.mExtraParcelFd, i);
    }
}
