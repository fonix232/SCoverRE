package com.samsung.android.content.clipboard.data;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.util.Log;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;

public class SemIntentClipData extends SemClipData {
    private static final String TAG = "SemIntentClipData";
    private static final long serialVersionUID = 1;
    private String mValue = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;

    public SemIntentClipData() {
        super(8);
    }

    public SemIntentClipData(Parcel parcel) {
        super(parcel);
        readFromSource(parcel);
    }

    private void setClipData() {
        Intent intent = null;
        try {
            intent = Intent.parseUri(this.mValue, 1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        setClipData(new ClipData(ClipboardConstants.CLIPBOARD_DRAGNDROP, new String[]{"text/vnd.android.intent"}, new Item(intent)));
    }

    public boolean equals(Object obj) {
        Log.secI(TAG, "intent equals");
        boolean z = false;
        if (!super.equals(obj) || !(obj instanceof SemIntentClipData)) {
            return false;
        }
        SemIntentClipData semIntentClipData = obj;
        if (!(semIntentClipData == null || semIntentClipData.getIntent() == null)) {
            z = this.mValue.compareTo(semIntentClipData.getIntent().toUri(1)) == 0;
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

    public Intent getIntent() {
        Intent intent = null;
        try {
            intent = Intent.parseUri(this.mValue, 1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return intent;
    }

    public ParcelFileDescriptor getParcelFileDescriptor() {
        return null;
    }

    protected void readFromSource(Parcel parcel) {
        this.mValue = parcel.readString();
    }

    public boolean setAlternateClipData(int i, SemClipData semClipData) {
        if (!super.setAlternateClipData(i, semClipData) || this.mValue.length() < 1) {
            return false;
        }
        boolean intent;
        switch (i) {
            case 8:
                try {
                    intent = ((SemIntentClipData) semClipData).setIntent(Intent.parseUri(this.mValue, 1));
                    break;
                } catch (Throwable e) {
                    intent = false;
                    e.printStackTrace();
                    break;
                }
            default:
                intent = false;
                break;
        }
        return intent;
    }

    public boolean setIntent(Intent intent) {
        if (intent == null || intent.toUri(1).length() == 0) {
            return false;
        }
        this.mValue = intent.toUri(1);
        return true;
    }

    public String toString() {
        return "SemIntentClipData class. Value is " + (this.mValue.length() > 20 ? this.mValue.subSequence(0, 20) : this.mValue);
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.secI(TAG, "Intent write to parcel");
        parcel.writeInt(8);
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mValue);
    }
}
