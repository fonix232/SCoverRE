package com.samsung.android.smartface;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class FaceInfo implements Parcelable {
    public static final int CHECK_FACE_EXISTENCE = 1;
    public static final int CHECK_FACE_EXISTENCE_WITH_ORIENTATION = 64;
    public static final int CHECK_FACE_ROTATION = 4;
    public static final Creator<FaceInfo> CREATOR = new C02521();
    public static final int FIND_FACES = 2;
    public static final int FIND_FACE_AND_PERSON_INFO = 8;
    public static final int FIND_FACE_COMPONENT = 32;
    public static final int FIND_FACE_POSE_INFO = 16;
    public static final int NEED_TO_PAUSE = 1;
    public static final int NEED_TO_PLAY = 0;
    public static final int NEED_TO_SLEEP = 0;
    public static final int NEED_TO_STAY = 1;
    public boolean bFaceDetected;
    public boolean bLowLightBackLighting;
    public int faceDistance;
    public int guideDir;
    public int horizontalMovement;
    public int needToPause;
    public int needToRotate;
    public int needToStay;
    public int numberOfPerson;
    public Person[] person = null;
    public int processStatus;
    public int responseType;
    public int verticalMovement;

    static class C02521 implements Creator<FaceInfo> {
        C02521() {
        }

        public FaceInfo createFromParcel(Parcel parcel) {
            return new FaceInfo(parcel);
        }

        public FaceInfo[] newArray(int i) {
            return new FaceInfo[i];
        }
    }

    public static class Face {
        public FaceExpression expression = null;
        public int id;
        public Point leftEye = null;
        public Point mouth = null;
        public Point nose = null;
        public FacePoseInfo pose = null;
        public Rect rect = null;
        public Point rightEye = null;
        public int score;
    }

    public static class FaceExpression {
        public static final int FACIAL_EXPRESSION_ANGER = 8;
        public static final int FACIAL_EXPRESSION_DISGUST = 4;
        public static final int FACIAL_EXPRESSION_FEAR = 32;
        public static final int FACIAL_EXPRESSION_JOY = 2;
        public static final int FACIAL_EXPRESSION_NONE = 1;
        public static final int FACIAL_EXPRESSION_SADNESS = 64;
        public static final int FACIAL_EXPRESSION_SURPRISE = 16;
        public int expression;
    }

    public static class FacePoseInfo {
        public int pitch;
        public int roll;
        public int yaw;
    }

    public static class Person {
        public Face face = null;
        public PersonInfo personInfo = null;
    }

    public static class PersonInfo {
        public String address = null;
        public String addressEMail = null;
        public String name = null;
        public String phoneNumber = null;
    }

    public FaceInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        boolean z = true;
        this.responseType = parcel.readInt();
        this.numberOfPerson = parcel.readInt();
        this.horizontalMovement = parcel.readInt();
        this.verticalMovement = parcel.readInt();
        this.processStatus = parcel.readInt();
        this.needToRotate = parcel.readInt();
        this.needToPause = parcel.readInt();
        this.needToStay = parcel.readInt();
        this.guideDir = parcel.readInt();
        this.bFaceDetected = parcel.readByte() == (byte) 1;
        if (parcel.readByte() != (byte) 1) {
            z = false;
        }
        this.bLowLightBackLighting = z;
        this.faceDistance = parcel.readInt();
        this.person = new Person[this.numberOfPerson];
        for (int i = 0; i < this.numberOfPerson; i++) {
            this.person[i].face = new Face();
            this.person[i].face.rect = new Rect();
            this.person[i].face.rect.left = parcel.readInt();
            this.person[i].face.rect.top = parcel.readInt();
            this.person[i].face.rect.bottom = parcel.readInt();
            this.person[i].face.rect.right = parcel.readInt();
            this.person[i].face.score = parcel.readInt();
            this.person[i].face.id = parcel.readInt();
            this.person[i].face.leftEye = new Point();
            this.person[i].face.leftEye.x = parcel.readInt();
            this.person[i].face.leftEye.y = parcel.readInt();
            this.person[i].face.rightEye = new Point();
            this.person[i].face.rightEye.x = parcel.readInt();
            this.person[i].face.rightEye.y = parcel.readInt();
            this.person[i].face.mouth = new Point();
            this.person[i].face.mouth.x = parcel.readInt();
            this.person[i].face.mouth.y = parcel.readInt();
            this.person[i].face.nose = new Point();
            this.person[i].face.nose.x = parcel.readInt();
            this.person[i].face.nose.y = parcel.readInt();
            this.person[i].face.pose = new FacePoseInfo();
            this.person[i].face.pose.pitch = parcel.readInt();
            this.person[i].face.pose.roll = parcel.readInt();
            this.person[i].face.pose.yaw = parcel.readInt();
            this.person[i].face.expression = new FaceExpression();
            this.person[i].face.expression.expression = parcel.readInt();
            this.person[i].personInfo = new PersonInfo();
            this.person[i].personInfo.addressEMail = parcel.readString();
            this.person[i].personInfo.phoneNumber = parcel.readString();
            this.person[i].personInfo.address = parcel.readString();
            this.person[i].personInfo.name = parcel.readString();
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2 = 1;
        parcel.writeInt(this.responseType);
        parcel.writeInt(this.numberOfPerson);
        parcel.writeInt(this.horizontalMovement);
        parcel.writeInt(this.verticalMovement);
        parcel.writeInt(this.processStatus);
        parcel.writeInt(this.needToRotate);
        parcel.writeInt(this.needToPause);
        parcel.writeInt(this.needToStay);
        parcel.writeInt(this.guideDir);
        parcel.writeByte((byte) (this.bFaceDetected ? 1 : 0));
        if (!this.bLowLightBackLighting) {
            i2 = 0;
        }
        parcel.writeByte((byte) i2);
        parcel.writeInt(this.faceDistance);
        for (int i3 = 0; i3 < this.numberOfPerson; i3++) {
            parcel.writeInt(this.person[i3].face.rect.left);
            parcel.writeInt(this.person[i3].face.rect.top);
            parcel.writeInt(this.person[i3].face.rect.bottom);
            parcel.writeInt(this.person[i3].face.rect.right);
            parcel.writeInt(this.person[i3].face.score);
            parcel.writeInt(this.person[i3].face.id);
            parcel.writeInt(this.person[i3].face.leftEye.x);
            parcel.writeInt(this.person[i3].face.leftEye.y);
            parcel.writeInt(this.person[i3].face.rightEye.x);
            parcel.writeInt(this.person[i3].face.rightEye.y);
            parcel.writeInt(this.person[i3].face.mouth.x);
            parcel.writeInt(this.person[i3].face.mouth.y);
            parcel.writeInt(this.person[i3].face.nose.x);
            parcel.writeInt(this.person[i3].face.nose.y);
            parcel.writeInt(this.person[i3].face.pose.pitch);
            parcel.writeInt(this.person[i3].face.pose.roll);
            parcel.writeInt(this.person[i3].face.pose.yaw);
            parcel.writeInt(this.person[i3].face.expression.expression);
            parcel.writeString(this.person[i3].personInfo.addressEMail);
            parcel.writeString(this.person[i3].personInfo.phoneNumber);
            parcel.writeString(this.person[i3].personInfo.address);
            parcel.writeString(this.person[i3].personInfo.name);
        }
    }
}
