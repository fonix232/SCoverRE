package com.samsung.android.content.clipboard.data;

import android.content.ClipData;
import android.os.Binder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardDataFactory;
import android.sec.clipboard.util.Log;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

public abstract class SemClipData implements Parcelable, Serializable {
    public static final Creator<SemClipData> CREATOR = new C10231();
    private static final String TAG = "SemClipData";
    private static final long serialVersionUID = 1;
    private long mCallerUid = -1;
    protected ClipData mClipData = null;
    private String mClipId;
    private boolean mIsProtected = false;
    private transient ParcelFileDescriptor mParcelFd = null;
    private long mTimestamp = 0;
    private int mType;

    static class C10231 implements Creator<SemClipData> {
        C10231() {
        }

        public SemClipData createFromParcel(Parcel parcel) {
            return ClipboardDataFactory.createClipBoardData(parcel);
        }

        public SemClipData[] newArray(int i) {
            return new SemClipData[i];
        }
    }

    public SemClipData(int i) {
        this.mType = i;
        this.mCallerUid = (long) Binder.getCallingUid();
        this.mTimestamp = System.currentTimeMillis();
        this.mParcelFd = null;
        this.mClipId = createUniqueId();
    }

    public SemClipData(Parcel parcel) {
        this.mType = parcel.readInt();
        this.mTimestamp = parcel.readLong();
        this.mIsProtected = ((Boolean) parcel.readValue(Boolean.class.getClassLoader())).booleanValue();
        this.mCallerUid = parcel.readLong();
        this.mClipData = (ClipData) parcel.readParcelable(ClipData.class.getClassLoader());
        this.mParcelFd = (ParcelFileDescriptor) parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.mClipId = parcel.readString();
    }

    private String createUniqueId() {
        int hashCode = hashCode();
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        Calendar instance = Calendar.getInstance();
        stringBuffer.append(hashCode);
        stringBuffer.append(instance.get(12));
        stringBuffer.append(instance.get(13));
        stringBuffer.append(instance.get(14));
        stringBuffer.append(random.nextInt(instance.get(14) + 1));
        return stringBuffer.toString();
    }

    public boolean canAlternateClipData(int i) {
        return (i == -1 || this.mType == i) ? true : setAlternateClipData(i, ClipboardDataFactory.createClipBoardData(i));
    }

    public void checkClipId() {
        if (this.mClipId == null) {
            this.mClipId = createUniqueId();
        }
    }

    public void closeParcelFileDescriptor() {
        if (this.mParcelFd != null) {
            try {
                this.mParcelFd.close();
            } catch (Throwable e) {
                if (ClipboardConstants.DEBUG) {
                    e.printStackTrace();
                } else {
                    Log.secD(TAG, "IOException!");
                }
            }
            this.mParcelFd = null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return obj != null ? obj instanceof SemClipData ? obj.getClipType() == getClipType() : super.equals(obj) : false;
    }

    public SemClipData getAlternateClipData(int i) {
        SemClipData createClipBoardData = ClipboardDataFactory.createClipBoardData(i);
        if (createClipBoardData != null) {
            return !setAlternateClipData(i, createClipBoardData) ? null : createClipBoardData;
        } else {
            Log.secI(TAG, "ClipBoardDataFactory.createClipBoardData() is null : " + i);
            return createClipBoardData;
        }
    }

    public long getCallerUid() {
        return this.mCallerUid;
    }

    public ClipData getClipData() {
        return getClipDataInternal();
    }

    protected abstract ClipData getClipDataInternal();

    public String getClipId() {
        return this.mClipId;
    }

    public int getClipType() {
        return this.mType;
    }

    public ParcelFileDescriptor getParcelFileDescriptor() {
        return this.mParcelFd;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public boolean isProtected() {
        return this.mIsProtected;
    }

    protected abstract void readFromSource(Parcel parcel);

    public boolean setAlternateClipData(int i, SemClipData semClipData) {
        if (semClipData == null) {
            return false;
        }
        semClipData.setParcelFileDescriptor(this.mParcelFd);
        semClipData.setTimestamp(this.mTimestamp);
        semClipData.setCallerUid(this.mCallerUid);
        semClipData.setClipData(this.mClipData);
        semClipData.setClipId(this.mClipId);
        return true;
    }

    public void setCallerUid(long j) {
        this.mCallerUid = j;
    }

    public void setClipData(ClipData clipData) {
        this.mClipData = clipData;
    }

    public void setClipId(String str) {
        this.mClipId = str;
    }

    public void setParcelFileDescriptor(ParcelFileDescriptor parcelFileDescriptor) {
        this.mParcelFd = parcelFileDescriptor;
    }

    @Deprecated
    public void setProtectState(boolean z) {
        this.mIsProtected = z;
    }

    public void setProtected(boolean z) {
        this.mIsProtected = z;
    }

    public void setTimestamp(long j) {
        this.mTimestamp = j;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeLong(this.mTimestamp);
        parcel.writeValue(Boolean.valueOf(this.mIsProtected));
        parcel.writeLong(this.mCallerUid);
        parcel.writeParcelable(this.mClipData, i);
        parcel.writeParcelable(this.mParcelFd, i);
        parcel.writeString(this.mClipId);
    }
}
