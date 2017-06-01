package com.samsung.android.content.clipboard.data;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.util.Log;
import java.util.ArrayList;

public class SemUriListClipData extends SemClipData {
    private static final String TAG = "SemUriListClipData";
    private static final long serialVersionUID = 1;
    private ArrayList<String> mUriArray;

    public SemUriListClipData() {
        super(32);
    }

    public SemUriListClipData(Parcel parcel) {
        super(parcel);
        readFromSource(parcel);
    }

    private void setClipData() {
        ClipData clipData = new ClipData(ClipboardConstants.CLIPBOARD_DRAGNDROP, new String[]{"text/uri-list"}, new Item(Uri.parse((String) this.mUriArray.get(0))));
        for (int i = 1; i < this.mUriArray.size(); i++) {
            clipData.addItem(new Item(Uri.parse((String) this.mUriArray.get(i))));
        }
        setClipData(clipData);
    }

    public boolean equals(Object obj) {
        Log.secI(TAG, "multiple uri equals");
        if (!super.equals(obj) || !(obj instanceof SemUriListClipData)) {
            return false;
        }
        SemUriListClipData semUriListClipData = obj;
        boolean z = false;
        if (semUriListClipData != null) {
            if (semUriListClipData.getUriList() != null) {
                z = this.mUriArray.toString().compareTo(semUriListClipData.getUriList().toString()) == 0;
            } else if (getUriList() == null) {
                return true;
            }
        }
        return z;
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

    public ParcelFileDescriptor getParcelFileDescriptor() {
        return null;
    }

    public ArrayList<Uri> getUriList() {
        if (this.mUriArray == null) {
            return null;
        }
        ArrayList<Uri> arrayList = new ArrayList();
        int size = this.mUriArray.size();
        for (int i = 0; i < size; i++) {
            arrayList.add(Uri.parse((String) this.mUriArray.get(i)));
        }
        return arrayList;
    }

    protected void readFromSource(Parcel parcel) {
        this.mUriArray = new ArrayList();
        parcel.readStringList(this.mUriArray);
    }

    public boolean setAlternateClipData(int i, SemClipData semClipData) {
        if (!super.setAlternateClipData(i, semClipData) || this.mUriArray.size() < 1) {
            return false;
        }
        boolean uriListInternal;
        switch (i) {
            case 32:
                uriListInternal = ((SemUriListClipData) semClipData).setUriListInternal(this.mUriArray);
                break;
            default:
                uriListInternal = false;
                break;
        }
        return uriListInternal;
    }

    public boolean setUriList(ArrayList<Uri> arrayList) {
        if (arrayList == null) {
            return false;
        }
        int size = arrayList.size();
        this.mUriArray = new ArrayList();
        for (int i = 0; i < size; i++) {
            this.mUriArray.add(((Uri) arrayList.get(i)).toString());
        }
        return true;
    }

    public boolean setUriListInternal(ArrayList<String> arrayList) {
        if (arrayList == null) {
            return false;
        }
        int size = arrayList.size();
        this.mUriArray = new ArrayList();
        for (int i = 0; i < size; i++) {
            this.mUriArray.add((String) arrayList.get(i));
        }
        return true;
    }

    public String toString() {
        return "SemUriListClipData class. Value is " + (this.mUriArray.toString().length() > 20 ? this.mUriArray.toString().subSequence(0, 20) : this.mUriArray.toString());
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.secI(TAG, "Multiple Uri write to parcel");
        parcel.writeInt(32);
        super.writeToParcel(parcel, i);
        parcel.writeStringList(this.mUriArray);
    }
}
