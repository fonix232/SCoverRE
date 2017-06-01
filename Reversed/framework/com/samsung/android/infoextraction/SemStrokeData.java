package com.samsung.android.infoextraction;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class SemStrokeData implements Parcelable {
    private List<PointF> mStroke;

    public SemStrokeData() {
        this.mStroke = null;
        this.mStroke = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public List<PointF> getPoints() {
        return this.mStroke;
    }

    public void readFromParcel(Parcel parcel) {
        if (this.mStroke == null) {
            this.mStroke = new ArrayList();
        }
        parcel.readTypedList(this.mStroke, PointF.CREATOR);
    }

    public void setPoints(PointF[] pointFArr) {
        if (pointFArr != null) {
            for (Object add : pointFArr) {
                this.mStroke.add(add);
            }
        }
    }

    public int size() {
        return this.mStroke.size();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.mStroke);
    }
}
