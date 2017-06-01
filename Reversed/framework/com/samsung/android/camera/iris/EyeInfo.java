package com.samsung.android.camera.iris;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class EyeInfo implements Parcelable {
    public static final Creator<EyeInfo> CREATOR = new C10001();
    public static final int DISTANCE_CLOSE = 1;
    public static final int DISTANCE_FAR = 4;
    public static final int DISTANCE_GOOD = 0;
    public static final int DISTANCE_TOO_CLOSE = 3;
    public static final int DISTANCE_TOO_FAR = 6;
    public static final int DISTANCE_VERY_CLOSE = 2;
    public static final int DISTANCE_VERY_FAR = 5;
    public static final int INFO_NOT_SUPPORTED = -1;
    public static final int IRIS_ACQUIRED_CHANGE_YOUR_POSITION = 12;
    public static final int IRIS_ACQUIRED_EYE_NOT_PRESENT = 10;
    public static final int IRIS_ACQUIRED_FAIL_IN_DOOR = 15;
    public static final int IRIS_ACQUIRED_FAIL_OUT_DOOR = 16;
    public static final int IRIS_ACQUIRED_GOOD = 0;
    public static final int IRIS_ACQUIRED_INSUFFICIENT = 2;
    public static final int IRIS_ACQUIRED_MOVE_CLOSER = 3;
    public static final int IRIS_ACQUIRED_MOVE_DOWN = 8;
    public static final int IRIS_ACQUIRED_MOVE_FARTHER = 4;
    public static final int IRIS_ACQUIRED_MOVE_LEFT = 5;
    public static final int IRIS_ACQUIRED_MOVE_RIGHT = 6;
    public static final int IRIS_ACQUIRED_MOVE_SOMEWHERE_DARKER = 11;
    public static final int IRIS_ACQUIRED_MOVE_UP = 7;
    public static final int IRIS_ACQUIRED_OPEN_EYES_WIDER = 9;
    public static final int IRIS_ACQUIRED_PARTIAL = 1;
    public static final int IRIS_ACQUIRED_PASS_IN_DOOR = 13;
    public static final int IRIS_ACQUIRED_PASS_OUT_DOOR = 14;
    public static final int IRIS_LEFT_EYE = 0;
    public static final int IRIS_RIGHT_EYE = 1;
    public static final int OPENING_GOOD = 0;
    public static final int OPENING_SMALL = 1;
    public static final int OPENING_TOO_SMALL = 3;
    public static final int OPENING_VERY_SMALL = 2;
    public static final int PUPIL_INFO_EYE_IS_FAKE = 3;
    public static final int PUPIL_INFO_EYE_LOW_IRIS_SCLERA_CONTRAST = 5;
    public static final int PUPIL_INFO_EYE_LOW_PUPIL_IRIS_CONTRAST = 4;
    public static final int PUPIL_INFO_EYE_NOT_PRESENT = 1;
    public static final int PUPIL_INFO_EYE_REGION_LOW_CONSTRAST = 2;
    public static final int PUPIL_INFO_LESS_QUALITY_SCORE = 7;
    public static final int PUPIL_INFO_NONE = 0;
    public static final int PUPIL_INFO_SMALL_MATCH_AREA = 6;
    public static final int REFLECTION_INFO_EYE_HIGHLIGHT_OCCLUSION = 0;
    public static final int REFLECTION_INFO_EYE_REGION_OVERILLUMINATED = 1;
    public int mAcquireInfo;
    public PupilInfo[] mPupilInfo;
    public ReflectionInfo[] mReflectionInfo;
    public int mReflectionNum;

    static class C10001 implements Creator<EyeInfo> {
        C10001() {
        }

        public EyeInfo createFromParcel(Parcel parcel) {
            return new EyeInfo(parcel);
        }

        public EyeInfo[] newArray(int i) {
            return new EyeInfo[i];
        }
    }

    public static class PupilInfo {
        public int mDistance = -1;
        public int mMsgId = -1;
        public int mOpening = -1;
        public Rect mRect = null;
    }

    public static class ReflectionInfo {
        public int mMsgId = -1;
        public Rect mRect = null;
    }

    private EyeInfo(Parcel parcel) {
        int i;
        this.mPupilInfo = null;
        this.mReflectionInfo = null;
        this.mAcquireInfo = -1;
        this.mReflectionNum = -1;
        this.mPupilInfo = new PupilInfo[2];
        this.mReflectionNum = parcel.readInt();
        this.mReflectionInfo = new ReflectionInfo[this.mReflectionNum];
        for (i = 0; i < 2; i++) {
            this.mPupilInfo[i] = new PupilInfo();
            this.mPupilInfo[i].mRect = new Rect();
            this.mPupilInfo[i].mRect.left = parcel.readInt();
            this.mPupilInfo[i].mRect.top = parcel.readInt();
            this.mPupilInfo[i].mRect.right = parcel.readInt();
            this.mPupilInfo[i].mRect.bottom = parcel.readInt();
            this.mPupilInfo[i].mDistance = parcel.readInt();
            this.mPupilInfo[i].mOpening = parcel.readInt();
            this.mPupilInfo[i].mMsgId = parcel.readInt();
        }
        for (i = 0; i < this.mReflectionNum; i++) {
            this.mReflectionInfo[i] = new ReflectionInfo();
            this.mReflectionInfo[i].mRect = new Rect();
            this.mReflectionInfo[i].mRect.left = parcel.readInt();
            this.mReflectionInfo[i].mRect.top = parcel.readInt();
            this.mReflectionInfo[i].mRect.right = parcel.readInt();
            this.mReflectionInfo[i].mRect.bottom = parcel.readInt();
            this.mReflectionInfo[i].mMsgId = parcel.readInt();
        }
        this.mAcquireInfo = parcel.readInt();
    }

    public EyeInfo(PupilInfo[] pupilInfoArr, ReflectionInfo[] reflectionInfoArr, int i, int i2) {
        this.mPupilInfo = null;
        this.mReflectionInfo = null;
        this.mAcquireInfo = -1;
        this.mReflectionNum = -1;
        this.mPupilInfo = pupilInfoArr;
        this.mReflectionInfo = reflectionInfoArr;
        this.mAcquireInfo = i;
        this.mReflectionNum = i2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2;
        parcel.writeInt(this.mReflectionNum);
        for (i2 = 0; i2 < 2; i2++) {
            parcel.writeInt(this.mPupilInfo[i2].mRect.left);
            parcel.writeInt(this.mPupilInfo[i2].mRect.top);
            parcel.writeInt(this.mPupilInfo[i2].mRect.right);
            parcel.writeInt(this.mPupilInfo[i2].mRect.bottom);
            parcel.writeInt(this.mPupilInfo[i2].mDistance);
            parcel.writeInt(this.mPupilInfo[i2].mOpening);
            parcel.writeInt(this.mPupilInfo[i2].mMsgId);
        }
        for (i2 = 0; i2 < this.mReflectionNum; i2++) {
            parcel.writeInt(this.mReflectionInfo[i2].mRect.left);
            parcel.writeInt(this.mReflectionInfo[i2].mRect.top);
            parcel.writeInt(this.mReflectionInfo[i2].mRect.right);
            parcel.writeInt(this.mReflectionInfo[i2].mRect.bottom);
            parcel.writeInt(this.mReflectionInfo[i2].mMsgId);
        }
        parcel.writeInt(this.mAcquireInfo);
    }
}
