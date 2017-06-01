package com.samsung.android.content.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.util.ArrayList;

public class SmartClipMetaTagArrayImpl extends SemSmartClipMetaTagArray implements Parcelable {
    public static final Creator<SmartClipMetaTagArrayImpl> CREATOR = new C10301();
    private static final String TAG = "SmartClipMetaTagArrayImpl";

    static class C10301 implements Creator<SmartClipMetaTagArrayImpl> {
        C10301() {
        }

        public SmartClipMetaTagArrayImpl createFromParcel(Parcel parcel) {
            Log.m29d(SmartClipMetaTagArrayImpl.TAG, "SmartClipMetaTagArrayImpl.createFromParcel called");
            SmartClipMetaTagArrayImpl smartClipMetaTagArrayImpl = new SmartClipMetaTagArrayImpl();
            smartClipMetaTagArrayImpl.readFromParcel(parcel);
            return smartClipMetaTagArrayImpl;
        }

        public SmartClipMetaTagArrayImpl[] newArray(int i) {
            return new SmartClipMetaTagArrayImpl[i];
        }
    }

    public boolean addMetaTag(SemSmartClipMetaTag semSmartClipMetaTag) {
        return semSmartClipMetaTag == null ? false : add(semSmartClipMetaTag);
    }

    public void addTag(SemSmartClipMetaTagArray semSmartClipMetaTagArray) {
        if (semSmartClipMetaTagArray != null) {
            for (SemSmartClipMetaTag add : semSmartClipMetaTagArray) {
                add(add);
            }
        }
    }

    public boolean addTag(SemSmartClipMetaTag semSmartClipMetaTag) {
        return addMetaTag(semSmartClipMetaTag);
    }

    public int describeContents() {
        return 0;
    }

    public void dump() {
        int size = size();
        for (int i = 0; i < size; i++) {
            SemSmartClipMetaTag semSmartClipMetaTag = (SemSmartClipMetaTag) get(i);
            String type = semSmartClipMetaTag.getType();
            String value = semSmartClipMetaTag.getValue();
            String str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            if (value == null) {
                value = new String("null");
            }
            if (semSmartClipMetaTag instanceof SemSmartClipExtendedMetaTag) {
                SemSmartClipMetaTag semSmartClipMetaTag2 = semSmartClipMetaTag;
                if (semSmartClipMetaTag2.getExtraData() != null) {
                    str = ", Extra data size = " + semSmartClipMetaTag2.getExtraData().length;
                }
            }
            Log.m29d(TAG, type + "(" + value + str + ")");
        }
    }

    public SmartClipMetaTagArrayImpl getCopy() {
        ArrayList smartClipMetaTagArrayImpl = new SmartClipMetaTagArrayImpl();
        int size = size();
        for (int i = 0; i < size; i++) {
            smartClipMetaTagArrayImpl.add((SemSmartClipMetaTag) get(i));
        }
        return smartClipMetaTagArrayImpl;
    }

    public SemSmartClipMetaTagArray getMetaTags(String str) {
        ArrayList smartClipMetaTagArrayImpl = new SmartClipMetaTagArrayImpl();
        int size = size();
        for (int i = 0; i < size; i++) {
            SemSmartClipMetaTag semSmartClipMetaTag = (SemSmartClipMetaTag) get(i);
            if (semSmartClipMetaTag.getType().equals(str)) {
                smartClipMetaTagArrayImpl.add(semSmartClipMetaTag);
            }
        }
        return smartClipMetaTagArrayImpl;
    }

    public SemSmartClipMetaTagArray getTags(String str) {
        return getMetaTags(str);
    }

    public void readFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        int i = 0;
        while (i < readInt) {
            String readString = parcel.readString();
            Object obj = null;
            if (readString.equals("BasicMetaTag")) {
                obj = new SemSmartClipMetaTag(parcel.readString(), parcel.readString());
            } else if (readString.equals("ParcelableMetaTag")) {
                SemSmartClipMetaTag semSmartClipMetaTag = (SemSmartClipMetaTag) parcel.readParcelable(null);
            } else {
                Log.m31e(TAG, "readFromParcel : Unknown meta tag type!!! : " + readString);
            }
            if (obj == null) {
                Log.m31e(TAG, "readFromParcel : Could not read tag!!");
                return;
            } else {
                add(obj);
                i++;
            }
        }
    }

    public int removeMetaTags(String str) {
        int i = 0;
        for (int size = size() - 1; size >= 0; size--) {
            if (((SemSmartClipMetaTag) get(size)).getType().equals(str)) {
                remove(size);
                i++;
            }
        }
        return i;
    }

    public int removeTags(String str) {
        return removeMetaTags(str);
    }

    public void writeToParcel(Parcel parcel, int i) {
        int size = size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            SemSmartClipMetaTag semSmartClipMetaTag = (SemSmartClipMetaTag) get(i2);
            if (semSmartClipMetaTag instanceof SemSmartClipExtendedMetaTag) {
                SemSmartClipMetaTag semSmartClipMetaTag2 = semSmartClipMetaTag;
                parcel.writeString("ParcelableMetaTag");
                parcel.writeParcelable(semSmartClipMetaTag2, 0);
            } else {
                parcel.writeString("BasicMetaTag");
                parcel.writeString(semSmartClipMetaTag.getType());
                parcel.writeString(semSmartClipMetaTag.getValue());
            }
        }
    }
}
