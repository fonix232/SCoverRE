package com.samsung.android.knox;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum SemPersonaState implements Parcelable {
    INVALID(-1),
    CREATING(1),
    ACTIVE(0),
    LOCKED(2),
    SUPER_LOCKED(-1),
    LICENSE_LOCKED(9),
    ADMIN_LOCKED(8),
    ADMIN_LICENSE_LOCKED(-1),
    TERMINUS(-1),
    DELETING(3),
    TIMA_COMPROMISED(7),
    CONTAINER_APPS_URGENT_UPDATE(-1);
    
    public static final Creator<SemPersonaState> CREATOR = null;
    private int knox2_0_state_id;

    static class C02031 implements Creator<SemPersonaState> {
        C02031() {
        }

        public SemPersonaState createFromParcel(Parcel parcel) {
            return SemPersonaState.valueOf(parcel.readString());
        }

        public SemPersonaState[] newArray(int i) {
            return new SemPersonaState[i];
        }
    }

    static {
        CREATOR = new C02031();
    }

    private SemPersonaState(int i) {
        this.knox2_0_state_id = -1;
        this.knox2_0_state_id = i;
    }

    public int describeContents() {
        return 0;
    }

    public int getKnox2_0State() {
        return this.knox2_0_state_id;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name());
    }
}
