package com.samsung.android.app.ledcover.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import java.util.ArrayList;
import java.util.Iterator;

public final class LCoverDbAccessor {
    private static final String TAG = "[LED_COVER]LedCoverDbAccessor";
    private static SQLiteDatabase rDb;
    private static SQLiteDatabase rwDb;
    private LCoverDbHelper dbHelper;

    public LCoverDbAccessor(Context context) {
        this.dbHelper = LCoverSingleton.getInstance().getmDbHelper(context);
        rDb = this.dbHelper.getReadableDatabase();
        rwDb = this.dbHelper.getWritableDatabase();
    }

    public void addLedIcon(LCoverIconInfo mLedCoverIconInfo) {
        SLog.m12v(TAG, "Insert Icon Data :: Icon Array :: " + mLedCoverIconInfo.getIconArray());
        SLog.m12v(TAG, "Insert Icon Data :: Icon Name :: " + mLedCoverIconInfo.getIconName());
        rwDb.insert(Defines.ICON_DB_TABLE_NAME, null, mLedCoverIconInfo.contentValues());
    }

    public int addSelectedApps(LCoverAppInfo mLedCoverAppInfo) {
        SLog.m12v(TAG, "Insert Package Data :: Package Name :: " + mLedCoverAppInfo.getPackageName());
        SLog.m12v(TAG, "Insert Package Data :: Icon Index :: " + mLedCoverAppInfo.getIconId());
        if (isPkgDuplicated(mLedCoverAppInfo.getPackageName())) {
            SLog.m12v(TAG, "pkg update ");
            updateSelectedApps(mLedCoverAppInfo.getPackageName(), mLedCoverAppInfo.getIconId());
            return 1;
        }
        SLog.m12v(TAG, "pkg insert ");
        rwDb.insert(Defines.PKG_DB_TABLE_NAME, null, mLedCoverAppInfo.contentValues());
        return 0;
    }

    private boolean isPkgDuplicated(String pkgName) {
        Cursor cursor = rDb.query(Defines.PKG_DB_TABLE_NAME, new String[]{Defines.PKG_COL_PACKAGE_NAME}, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                int COLUMN_IDX_PKG_NAME = cursor.getColumnIndex(Defines.PKG_COL_PACKAGE_NAME);
                cursor.moveToFirst();
                while (!cursor.getString(COLUMN_IDX_PKG_NAME).equals(pkgName)) {
                    if (!cursor.moveToNext()) {
                    }
                }
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public int updateLedIcon(LCoverIconInfo mLedCoverIconInfo) {
        SLog.m12v(TAG, "Update Icon Data :: Id :: " + mLedCoverIconInfo.getId());
        SLog.m12v(TAG, "Update Icon Data :: Icon Array :: " + mLedCoverIconInfo.getIconArray());
        SLog.m12v(TAG, "Update Icon Data :: Icon Name :: " + mLedCoverIconInfo.getIconName());
        return rwDb.update(Defines.ICON_DB_TABLE_NAME, mLedCoverIconInfo.contentValues(), mLedCoverIconInfo.getId() + " = " + Defines.ICON_COL_ID, null);
    }

    public int updateSelectedApps(String pkgName, int icon_id) {
        SLog.m12v(TAG, "Update Package Data :: Package Name :: " + pkgName);
        SLog.m12v(TAG, "Update Package Data :: Icon Index :: " + icon_id);
        ContentValues values = new ContentValues();
        values.put(Defines.PKG_COL_ICON_INDEX, Integer.valueOf(icon_id));
        int result = rwDb.update(Defines.PKG_DB_TABLE_NAME, values, "name='" + pkgName + "'", null);
        if (result < 0) {
            SLog.m12v(TAG, "updateSelectedApps error");
        } else {
            SLog.m12v(TAG, "updateSelectedApps suc");
        }
        return result;
    }

    public boolean deleteLedIcon(int id) {
        SLog.m12v(TAG, "deleteLedIcon(), id : " + id);
        return rwDb.delete(Defines.ICON_DB_TABLE_NAME, new StringBuilder().append("icon_id = ").append(id).toString(), null) > 0;
    }

    public boolean deleteLedIcons(String where, String[] whereArgs) {
        SLog.m12v(TAG, "deleteLedIcons()");
        return rwDb.delete(Defines.ICON_DB_TABLE_NAME, where, whereArgs) > 0;
    }

    public boolean deleteSelectedApps(String where, String[] whereArgs) {
        SLog.m12v(TAG, "Delete Pkg Info");
        return rwDb.delete(Defines.PKG_DB_TABLE_NAME, where, whereArgs) > 0;
    }

    public ArrayList<LCoverIconInfo> getCustomIconInfo() {
        SLog.m12v(TAG, "getCustomIconInfo");
        Cursor cursor = rDb.query(Defines.ICON_DB_TABLE_NAME, new String[]{Defines.PKG_COL_KEY, Defines.ICON_COL_ID, Defines.ICON_COL_ICON_ARRAY, Defines.ICON_COL_ICON_NAME, Defines.ICON_COL_COUNT}, "icon_array!=?", new String[]{Defines.PRESET_ICON_ARRAY}, null, null, null, null);
        ArrayList<LCoverIconInfo> iconInfoList = new ArrayList();
        if (cursor.getCount() > 0) {
            int COLUMN_IDX_KEY = cursor.getColumnIndex(Defines.PKG_COL_KEY);
            int COLUMN_IDX_ID = cursor.getColumnIndex(Defines.ICON_COL_ID);
            int COLUMN_IDX_ARRAY = cursor.getColumnIndex(Defines.ICON_COL_ICON_ARRAY);
            int COLUMN_IDX_NAME = cursor.getColumnIndex(Defines.ICON_COL_ICON_NAME);
            int COLUMN_IDX_CNT = cursor.getColumnIndex(Defines.ICON_COL_COUNT);
            cursor.moveToFirst();
            do {
                LCoverIconInfo IconInfoUnit = new LCoverIconInfo(cursor.getInt(COLUMN_IDX_ID), cursor.getString(COLUMN_IDX_NAME), cursor.getInt(COLUMN_IDX_CNT));
                IconInfoUnit.setIconArray(cursor.getString(COLUMN_IDX_ARRAY));
                iconInfoList.add(IconInfoUnit);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            SLog.m12v(TAG, "cursor get count is " + cursor.getCount());
            cursor.close();
        }
        return iconInfoList;
    }

    public ArrayList<LCoverAppInfo> getSelectedAppsInfo() {
        SLog.m12v(TAG, "getSelectedAppsInfo");
        String joinQueryStr = String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s from %s INNER JOIN %s ON %s.%s=%s.%s ORDER BY %s.%s ASC;", new Object[]{Defines.PKG_DB_TABLE_NAME, Defines.PKG_COL_KEY, Defines.PKG_DB_TABLE_NAME, Defines.PKG_COL_PACKAGE_NAME, Defines.PKG_DB_TABLE_NAME, Defines.PKG_COL_ICON_INDEX, Defines.ICON_DB_TABLE_NAME, Defines.ICON_COL_ICON_ARRAY, Defines.PKG_DB_TABLE_NAME, Defines.ICON_DB_TABLE_NAME, Defines.PKG_DB_TABLE_NAME, Defines.PKG_COL_ICON_INDEX, Defines.ICON_DB_TABLE_NAME, Defines.ICON_COL_ID, Defines.PKG_DB_TABLE_NAME, Defines.PKG_COL_PACKAGE_NAME});
        SLog.m12v(TAG, "joinQueryString : " + joinQueryStr);
        Cursor cursor = rDb.rawQuery(joinQueryStr, null);
        ArrayList<LCoverAppInfo> selecteAppsInfoList = new ArrayList();
        if (cursor.getCount() > 0) {
            int COLUMN_IDX_KEY = cursor.getColumnIndex(Defines.PKG_COL_KEY);
            int COLUMN_IDX_PKG_NAME = cursor.getColumnIndex(Defines.PKG_COL_PACKAGE_NAME);
            int COLUMN_IDX_ICON_INDEX = cursor.getColumnIndex(Defines.PKG_COL_ICON_INDEX);
            int COLUMN_IDX_ICON_ARRAY = cursor.getColumnIndex(Defines.ICON_COL_ICON_ARRAY);
            cursor.moveToFirst();
            do {
                LCoverAppInfo selectedAppsInfoUnit = new LCoverAppInfo();
                selectedAppsInfoUnit.setPackageName(cursor.getString(COLUMN_IDX_PKG_NAME));
                selectedAppsInfoUnit.setIconId(cursor.getInt(COLUMN_IDX_ICON_INDEX));
                selectedAppsInfoUnit.setIconArray(cursor.getString(COLUMN_IDX_ICON_ARRAY));
                selecteAppsInfoList.add(selectedAppsInfoUnit);
            } while (cursor.moveToNext());
            cursor.close();
            int size = selecteAppsInfoList.size();
            SLog.m12v(TAG, "*** [KEY]\t\t\t\t[PAKAGE_NAME]\t\t\t[ICON_IDX]\t\t\t[ICON_ARRAY]");
            for (int i = 0; i < size; i++) {
                LCoverAppInfo a = (LCoverAppInfo) selecteAppsInfoList.get(i);
                SLog.m12v(TAG, "*** [" + a.getId() + "]\t\t[" + a.getPackageName() + "]\t\t\t\t[" + a.getIconId() + "]\t\t\t\t[" + a.getIconArray() + "]");
            }
        } else {
            SLog.m12v(TAG, "cursor get count is " + cursor.getCount());
            cursor.close();
        }
        return selecteAppsInfoList;
    }

    public int getIconDbCount(int id) {
        Cursor cursor = rDb.query(Defines.ICON_DB_TABLE_NAME, new String[]{Defines.PKG_COL_KEY, Defines.ICON_COL_COUNT}, "icon_id = " + id, null, null, null, null, null);
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            int icon_cnt = cursor.getInt(cursor.getColumnIndex(Defines.ICON_COL_COUNT));
            cursor.close();
            return icon_cnt;
        } else if (count == 0) {
            cursor.close();
            return 0;
        } else {
            cursor.close();
            return 0;
        }
    }

    public int addLedPresetIcons(ArrayList<LCoverIconInfo> iconInfoArray) {
        SLog.m12v(TAG, "addLedPresetIcons");
        int retCount = 0;
        rwDb.beginTransaction();
        Iterator it = iconInfoArray.iterator();
        while (it.hasNext()) {
            if (rwDb.insert(Defines.ICON_DB_TABLE_NAME, null, ((LCoverIconInfo) it.next()).contentValues()) != -1) {
                retCount++;
            }
        }
        rwDb.setTransactionSuccessful();
        rwDb.endTransaction();
        return retCount;
    }

    public LCoverDbHelper getDBHelper() {
        return this.dbHelper;
    }
}
