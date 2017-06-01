package com.samsung.android.hardware.context;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.ArrayList;

public class SemContextActivityNotificationAttribute extends SemContextAttribute {
    public static final Creator<SemContextActivityNotificationAttribute> CREATOR = new C01331();
    private static final int STATUS_MAX = 5;
    private static final String TAG = "SemContextActivityNotificationAttribute";
    private int[] mActivityFilter = new int[]{4};

    static class C01331 implements Creator<SemContextActivityNotificationAttribute> {
        C01331() {
        }

        public SemContextActivityNotificationAttribute createFromParcel(Parcel parcel) {
            return new SemContextActivityNotificationAttribute(parcel);
        }

        public SemContextActivityNotificationAttribute[] newArray(int i) {
            return new SemContextActivityNotificationAttribute[i];
        }
    }

    SemContextActivityNotificationAttribute() {
        setAttribute();
    }

    SemContextActivityNotificationAttribute(Parcel parcel) {
        super(parcel);
    }

    public SemContextActivityNotificationAttribute(int[] iArr) {
        this.mActivityFilter = iArr;
        setAttribute();
    }

    private void setAttribute() {
        BaseBundle bundle = new Bundle();
        bundle.putIntArray("activity_filter", this.mActivityFilter);
        super.setAttribute(27, bundle);
    }

    public boolean checkAttribute() {
        if (this.mActivityFilter == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < this.mActivityFilter.length) {
            if (this.mActivityFilter[i] < 0 || this.mActivityFilter[i] > 5) {
                Log.e(TAG, "The activity status is wrong.");
                return false;
            }
            arrayList.add(Integer.valueOf(this.mActivityFilter[i]));
            for (int i2 = 0; i2 < i; i2++) {
                if (((Integer) arrayList.get(i)).equals(arrayList.get(i2))) {
                    Log.e(TAG, "This activity status cannot have duplicated status.");
                    return false;
                }
            }
            i++;
        }
        return true;
    }
}
