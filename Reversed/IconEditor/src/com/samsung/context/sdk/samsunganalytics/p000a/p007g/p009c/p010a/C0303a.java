package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.p010a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0309d;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.c.a.a */
public class C0303a {
    private C0304b f123a;

    public C0303a(Context context) {
        this.f123a = new C0304b(context);
        m111a(5);
    }

    public Queue<C0309d> m110a() {
        Cursor rawQuery = this.f123a.getReadableDatabase().rawQuery("select * from logs", null);
        Queue<C0309d> linkedBlockingQueue = new LinkedBlockingQueue();
        while (rawQuery.moveToNext()) {
            C0309d c0309d = new C0309d();
            c0309d.m134a(rawQuery.getString(rawQuery.getColumnIndex(Defines.PKG_COL_KEY)));
            c0309d.m136b(rawQuery.getString(rawQuery.getColumnIndex(C0305c.f129c)));
            c0309d.m133a(rawQuery.getLong(rawQuery.getColumnIndex(C0305c.f128b)));
            linkedBlockingQueue.add(c0309d);
        }
        rawQuery.close();
        return linkedBlockingQueue;
    }

    public void m111a(long j) {
        this.f123a.getWritableDatabase().delete(C0305c.f127a, "timestamp <= " + j, null);
    }

    public void m112a(C0309d c0309d) {
        SQLiteDatabase writableDatabase = this.f123a.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C0305c.f128b, Long.valueOf(c0309d.m135b()));
        contentValues.put(C0305c.f129c, c0309d.m137c());
        writableDatabase.insert(C0305c.f127a, null, contentValues);
    }

    public void m113a(String str) {
        this.f123a.getWritableDatabase().delete(C0305c.f127a, "_id = " + str, null);
    }

    public void m114b() {
        if (this.f123a != null) {
            this.f123a.close();
        }
    }

    public boolean m115c() {
        return DatabaseUtils.queryNumEntries(this.f123a.getReadableDatabase(), C0305c.f127a) <= 0;
    }
}
