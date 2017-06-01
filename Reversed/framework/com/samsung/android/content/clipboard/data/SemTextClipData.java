package com.samsung.android.content.clipboard.data;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.util.HtmlUtils;
import android.sec.clipboard.util.Log;
import android.text.Html;
import android.text.Spanned;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;

public class SemTextClipData extends SemClipData {
    private static final String TAG = "SemTextClipData";
    private static final long serialVersionUID = 1;
    private int mNumberOfTrailingWhiteLines;
    private transient CharSequence mText;
    private String mValue;

    public SemTextClipData() {
        super(1);
        this.mValue = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        this.mText = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        this.mNumberOfTrailingWhiteLines = 0;
        this.mNumberOfTrailingWhiteLines = 0;
    }

    public SemTextClipData(Parcel parcel) {
        super(parcel);
        this.mValue = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        this.mText = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        this.mNumberOfTrailingWhiteLines = 0;
        readFromSource(parcel);
    }

    private void setClipData() {
        setClipData(new ClipData(ClipboardConstants.CLIPBOARD_DRAGNDROP, new String[]{"text/plain"}, new Item(this.mText)));
    }

    public boolean equals(Object obj) {
        boolean z = false;
        Log.secI(TAG, "text equals");
        if (!super.equals(obj) || !(obj instanceof SemTextClipData)) {
            return false;
        }
        if (this.mText.toString().compareTo(obj.getText().toString()) == 0) {
            z = true;
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

    public CharSequence getText() {
        return this.mText;
    }

    protected void readFromSource(Parcel parcel) {
        this.mText = (CharSequence) parcel.readValue(CharSequence.class.getClassLoader());
    }

    public boolean setAlternateClipData(int i, SemClipData semClipData) {
        if (!super.setAlternateClipData(i, semClipData) || this.mText.length() < 1) {
            return false;
        }
        boolean text;
        switch (i) {
            case 1:
                text = ((SemTextClipData) semClipData).setText(this.mText);
                break;
            case 4:
                text = ((SemHtmlClipData) semClipData).setHtml(this.mText);
                break;
            default:
                text = false;
                break;
        }
        return text;
    }

    public void setNumberOfTrailingWhiteLines(int i) {
        this.mNumberOfTrailingWhiteLines = i;
    }

    public boolean setText(CharSequence charSequence) {
        if (charSequence == null || charSequence.toString().length() == 0) {
            return false;
        }
        if (charSequence.length() > 131072) {
            charSequence = charSequence.subSequence(0, 131072);
        }
        this.mText = charSequence;
        return true;
    }

    public void setTextValue(String str) {
        this.mValue = str;
    }

    public void toLoad() {
        if (this.mValue != null) {
            if (HtmlUtils.isHtml(this.mValue)) {
                this.mText = Html.fromHtml(this.mValue);
            } else {
                if (this.mValue.contains(HtmlUtils.HTML_LINE_FEED)) {
                    this.mValue = this.mValue.replaceAll(HtmlUtils.HTML_LINE_FEED, "\n");
                }
                this.mText = this.mValue;
            }
            int i = 0;
            int i2 = 1;
            while (i2 <= this.mText.length() - 1 && this.mText.charAt(this.mText.length() - i2) == '\n') {
                i++;
                i2++;
            }
            if (i > this.mNumberOfTrailingWhiteLines) {
                this.mText = this.mText.subSequence(0, this.mText.length() - (i - this.mNumberOfTrailingWhiteLines));
            }
            Log.secD(TAG, "mValue = " + this.mValue.toString());
            Log.secD(TAG, "mText = " + this.mText.toString());
        }
    }

    public void toSave() {
        if (this.mText == null) {
            return;
        }
        if (this.mText instanceof Spanned) {
            this.mNumberOfTrailingWhiteLines = 0;
            int i = 1;
            while (i <= this.mText.length() - 1 && this.mText.charAt(this.mText.length() - i) == '\n') {
                this.mNumberOfTrailingWhiteLines++;
                i++;
            }
            this.mValue = Html.toHtml((Spanned) this.mText);
            Log.secD(TAG, "mText is an instance of Spanned: mValue = " + this.mValue.toString());
            return;
        }
        this.mValue = this.mText.toString();
        Log.secD(TAG, "mText is not an instance of Spanned: mValue = " + this.mValue.toString());
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("SemTextClipData class. Value is ");
        CharSequence subSequence = (this.mText == null || this.mText.length() <= 20) ? this.mText : this.mText.subSequence(0, 20);
        return append.append(subSequence).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.secI(TAG, "text write to parcel");
        parcel.writeInt(1);
        super.writeToParcel(parcel, i);
        parcel.writeValue(this.mText);
    }
}
