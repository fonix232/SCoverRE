package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.p010a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.c.a.b */
public class C0304b extends SQLiteOpenHelper {
    public static final String f124a = "SamsungAnalytics.db";
    public static final int f125b = 1;
    public static final String f126c = "create table logs (_id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp INTEGER, data TEXT)";

    public C0304b(Context context) {
        super(context, f124a, null, f125b);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(f126c);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
