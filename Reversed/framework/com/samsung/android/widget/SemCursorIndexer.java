package com.samsung.android.widget;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class SemCursorIndexer extends SemAbstractIndexer {
    public static final String EXTRA_INDEX_COUNTS = "indexscroll_index_counts";
    public static final String EXTRA_INDEX_TITLES = "indexscroll_index_titles";
    private final String TAG = "SemCursorIndexer";
    private final boolean debug = true;
    protected int mColumnIndex;
    protected Cursor mCursor;
    protected int mSavedCursorPos;

    public SemCursorIndexer(Cursor cursor, int i, CharSequence charSequence) {
        super(charSequence);
        this.mCursor = cursor;
        this.mColumnIndex = i;
        Log.d("SemCursorIndexer", "SemCursorIndexer constructor");
        if (i < 0) {
            Throwable runtimeException = new RuntimeException("here");
            runtimeException.fillInStackTrace();
            Log.w("SemCursorIndexer", "SemCursorIndexer() called with " + i, runtimeException);
        }
    }

    public SemCursorIndexer(Cursor cursor, int i, CharSequence charSequence, int i2, int i3) {
        super(charSequence, i2, i3);
        this.mCursor = cursor;
        this.mColumnIndex = i;
        Log.e("SemCursorIndexer", "SemCursorIndexer constructor, profileCount:" + i2 + ", favoriteCount:" + i3);
        if (i < 0) {
            Throwable runtimeException = new RuntimeException("here");
            runtimeException.fillInStackTrace();
            Log.w("SemCursorIndexer", "SemCursorIndexer() called with " + i, runtimeException);
        }
    }

    public SemCursorIndexer(Cursor cursor, int i, String[] strArr, int i2) {
        super(strArr, i2);
        this.mCursor = cursor;
        this.mColumnIndex = i;
        Log.d("SemCursorIndexer", "SemCursorIndexer constructor");
        if (i < 0) {
            Throwable runtimeException = new RuntimeException("here");
            runtimeException.fillInStackTrace();
            Log.w("SemCursorIndexer", "SemCursorIndexer() called with " + i, runtimeException);
        }
    }

    public SemCursorIndexer(Cursor cursor, int i, String[] strArr, int i2, int i3, int i4) {
        super(strArr, i2, i3, i4);
        this.mCursor = cursor;
        this.mColumnIndex = i;
        Log.e("SemCursorIndexer", "SemCursorIndexer constructor, profileCount:" + i3 + ", favoriteCount:" + i4);
        if (i < 0) {
            Throwable runtimeException = new RuntimeException("here");
            runtimeException.fillInStackTrace();
            Log.w("SemCursorIndexer", "SemCursorIndexer() called with " + i, runtimeException);
        }
    }

    protected Bundle getBundle() {
        Log.d("SemCursorIndexer", "SemCursorIndexer getBundle : Bundle was used by Indexer");
        return this.mCursor.getExtras();
    }

    protected String getItemAt(int i) {
        if (this.mCursor.isClosed()) {
            Log.d("SemCursorIndexer", "SemCursorIndexer getItemCount : mCursor is closed  ");
            return null;
        }
        if (this.mColumnIndex < 0) {
            Log.d("SemCursorIndexer", "getItemAt() mColumnIndex : " + this.mColumnIndex);
        }
        this.mCursor.moveToPosition(i);
        try {
            return this.mCursor.getString(this.mColumnIndex);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    protected int getItemCount() {
        if (!this.mCursor.isClosed()) {
            return this.mCursor.getCount();
        }
        Log.d("SemCursorIndexer", "SemCursorIndexer getItemCount : mCursor is closed  ");
        return 0;
    }

    protected boolean isDataToBeIndexedAvailable() {
        return getItemCount() > 0 && !this.mCursor.isClosed();
    }

    protected void onBeginTransaction() {
        this.mSavedCursorPos = this.mCursor.getPosition();
        Log.d("SemCursorIndexer", "SemCursorIndexer.onBeginTransaction() : Current cursor pos to save is :  " + this.mSavedCursorPos);
    }

    protected void onEndTransaction() {
        Log.d("SemCursorIndexer", "SemCursorIndexer.onEndTransaction() : Saved cursor pos to restore  is :  " + this.mSavedCursorPos);
        this.mCursor.moveToPosition(this.mSavedCursorPos);
    }

    public void setFavoriteItemsCount(int i) {
        setFavoriteItem(i);
    }

    public void setMiscItemsCount(int i) {
        setDigitItem(i);
    }

    public void setProfileItemsCount(int i) {
        setProfileItem(i);
    }
}
