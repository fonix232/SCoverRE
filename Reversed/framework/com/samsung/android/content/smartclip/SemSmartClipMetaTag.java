package com.samsung.android.content.smartclip;

public class SemSmartClipMetaTag {
    private String mType = null;
    private String mValue = null;

    public SemSmartClipMetaTag(String str, String str2) {
        this.mType = str;
        this.mValue = str2;
    }

    public String getType() {
        return this.mType;
    }

    public String getValue() {
        return this.mValue;
    }

    public void setType(String str) {
        this.mType = str;
    }

    public void setValue(String str) {
        this.mValue = str;
    }
}
