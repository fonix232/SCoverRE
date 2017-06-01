package com.samsung.android.app.ledcover.cp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.samsung.android.app.ledcover.db.LCoverDbHelper;
import com.samsung.android.app.ledcover.info.Defines;

public class LCoverContentProvider extends ContentProvider {
    SQLiteDatabase db;

    public boolean onCreate() {
        this.db = new LCoverDbHelper(getContext()).getReadableDatabase();
        return this.db == null ? false : false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return this.db.query(Defines.ICON_DB_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
