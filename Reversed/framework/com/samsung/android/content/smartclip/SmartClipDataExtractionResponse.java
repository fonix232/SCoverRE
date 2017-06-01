package com.samsung.android.content.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipDataExtractionResponse implements Parcelable {
    public static final Creator<SmartClipDataExtractionResponse> CREATOR = new C10291();
    public int mExtractionMode = 0;
    public SemSmartClipDataRepository mRepository = null;
    public int mRequestId = 0;

    static class C10291 implements Creator<SmartClipDataExtractionResponse> {
        C10291() {
        }

        public SmartClipDataExtractionResponse createFromParcel(Parcel parcel) {
            SmartClipDataExtractionResponse smartClipDataExtractionResponse = new SmartClipDataExtractionResponse(0, 0, null);
            smartClipDataExtractionResponse.readFromParcel(parcel);
            return smartClipDataExtractionResponse;
        }

        public SmartClipDataExtractionResponse[] newArray(int i) {
            return new SmartClipDataExtractionResponse[i];
        }
    }

    public SmartClipDataExtractionResponse(int i, int i2, SemSmartClipDataRepository semSmartClipDataRepository) {
        this.mRequestId = i;
        this.mExtractionMode = i2;
        this.mRepository = semSmartClipDataRepository;
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.mRequestId = parcel.readInt();
        this.mExtractionMode = parcel.readInt();
        this.mRepository = (SemSmartClipDataRepository) parcel.readParcelable(SemSmartClipDataRepository.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mRequestId);
        parcel.writeInt(this.mExtractionMode);
        parcel.writeParcelable(this.mRepository, i);
    }
}
