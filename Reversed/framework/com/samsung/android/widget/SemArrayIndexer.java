package com.samsung.android.widget;

import android.os.Bundle;
import java.util.List;

public class SemArrayIndexer extends SemAbstractIndexer {
    private final String TAG = "SemArrayIndexer";
    private final boolean debug = false;
    protected List<String> mData;

    public SemArrayIndexer(List<String> list, CharSequence charSequence) {
        super(charSequence);
        this.mData = list;
    }

    protected Bundle getBundle() {
        return null;
    }

    protected String getItemAt(int i) {
        return (String) this.mData.get(i);
    }

    protected int getItemCount() {
        return this.mData.size();
    }

    protected boolean isDataToBeIndexedAvailable() {
        return getItemCount() > 0;
    }
}
