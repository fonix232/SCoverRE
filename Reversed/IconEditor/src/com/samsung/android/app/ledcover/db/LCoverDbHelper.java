package com.samsung.android.app.ledcover.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.samsung.android.app.ledcover.common.SLog;

public class LCoverDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "lcover.db";
    private static final int DB_VERSION = 2;
    private static final String TAG = "[LED_COVER]LCoverDbHelper";
    public static SQLiteDatabase mDB;
    public static LCoverDbHelper mDbHelper;

    static {
        mDbHelper = null;
    }

    public LCoverDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        SLog.m12v(TAG, "onCreate");
        mDB = db;
        mDB.execSQL("create table icon(_id integer primary key autoincrement, icon_id NUMERIC default (strftime('%s', 'now')), icon_array TEXT, icon_name TEXT, icon_count NUMERIC )");
        mDB.execSQL("create table pkg(_id integer primary key autoincrement, name TEXT, icon_index NUMERIC )");
        mDB.execSQL("create trigger tr_icon_count_insert after insert ON pkg for each row  begin  update icon SET icon_count = icon_count + 1  where new.icon_index = icon_id; end;");
        mDB.execSQL("create trigger tr_icon_count_update after update of icon_index ON pkg for each row  begin  update icon SET icon_count = icon_count - 1  where old.icon_index = icon_id; update icon SET icon_count = icon_count + 1  where new.icon_index = icon_id; end;");
        mDB.execSQL("create trigger tr_icon_count_del after delete ON pkg for each row  begin  update icon SET icon_count = icon_count - 1  where old.icon_index = icon_id; end;");
        for (int i = DB_VERSION; i <= DB_VERSION; i++) {
            updateDB(i);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SLog.m12v(TAG, "onUpgrade, oldVer:" + oldVersion + " newVersion:" + newVersion);
        mDB = db;
        for (int i = oldVersion + 1; i <= newVersion; i++) {
            updateDB(i);
        }
    }

    private void updateDB(int version) {
        SLog.m12v(TAG, "updateDB, version = " + version);
        if (version == DB_VERSION) {
            mDB.execSQL("create trigger tr_del_pkg_after_del_icon after delete ON icon for each row  begin  delete from pkg where old.icon_id = icon_index; end;");
        }
    }
}
