package com.samsung.android.content.smartclip;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipDataExtractionEvent implements Parcelable {
    public static final Creator<SmartClipDataExtractionEvent> CREATOR = new C10281();
    public static final int EXTRACTION_MODE_DRAG_AND_DROP = 2;
    public static final int EXTRACTION_MODE_FULL_SCREEN = 1;
    public static final int EXTRACTION_MODE_NORMAL = 0;
    public static final int EXTRACTION_MODE_SINGLE_WORD = 3;
    public Rect mCropRect;
    public int mExtractionMode;
    public int mRequestId;
    public int mTargetWindowLayer;

    static class C10281 implements Creator<SmartClipDataExtractionEvent> {
        C10281() {
        }

        public SmartClipDataExtractionEvent createFromParcel(Parcel parcel) {
            SmartClipDataExtractionEvent smartClipDataExtractionEvent = new SmartClipDataExtractionEvent();
            smartClipDataExtractionEvent.readFromParcel(parcel);
            return smartClipDataExtractionEvent;
        }

        public SmartClipDataExtractionEvent[] newArray(int i) {
            return new SmartClipDataExtractionEvent[i];
        }
    }

    public SmartClipDataExtractionEvent() {
        this.mRequestId = 0;
        this.mExtractionMode = 0;
        this.mCropRect = new Rect();
        this.mTargetWindowLayer = -1;
    }

    public SmartClipDataExtractionEvent(int i, Rect rect) {
        this.mRequestId = 0;
        this.mExtractionMode = 0;
        this.mCropRect = new Rect();
        this.mTargetWindowLayer = -1;
        this.mRequestId = i;
        this.mCropRect = rect;
    }

    public SmartClipDataExtractionEvent(int i, Rect rect, int i2) {
        this(i, rect);
        this.mExtractionMode = i2;
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.mRequestId = parcel.readInt();
        this.mExtractionMode = parcel.readInt();
        this.mTargetWindowLayer = parcel.readInt();
        this.mCropRect = (Rect) parcel.readParcelable(Rect.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mRequestId);
        parcel.writeInt(this.mExtractionMode);
        parcel.writeInt(this.mTargetWindowLayer);
        parcel.writeParcelable(this.mCropRect, i);
    }
}
