package com.samsung.android.codecsolution;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.util.ArrayList;

public class SCPMHelper {
    private static final String AUTHORITY = "com.samsung.android.sm.policy";
    private static final Uri AUTHORITY_URI = Uri.parse("content://com.samsung.android.sm.policy");
    private static final String SMART_FITTING_POLICY_COLUMN_CATEGORY = "category";
    private static final String SMART_FITTING_POLICY_COLUMN_PACKAGENAME = "item";
    private static final String SMART_FITTING_POLICY_COLUMN_TIME = "data2";
    private static final String SMART_FITTING_POLICY_COLUMN_USES = "data1";
    private static final String[] SMART_FITTING_POLICY_PROJECTION = new String[]{SMART_FITTING_POLICY_COLUMN_PACKAGENAME, "category", "data1", "data2"};
    private static final String SMART_FITTING_POLICY_TABLE = "policy_item/SmartFittingService";
    private static final Uri SMART_FITTING_POLICY_URI = Uri.withAppendedPath(AUTHORITY_URI, SMART_FITTING_POLICY_TABLE);
    private static final String SMART_FITTING_SCPM_NAME = "SmartFittingService";
    private static final String TAG = "SCPMHelper";
    private Context mContext = null;

    public SCPMHelper(Context context) {
        this.mContext = context;
    }

    public ArrayList getPackageInfo(String str) {
        Log.m29d(TAG, "getPackageInfo(" + str + ")");
        if (isAvailable()) {
            Cursor query = this.mContext.getContentResolver().query(SMART_FITTING_POLICY_URI, SMART_FITTING_POLICY_PROJECTION, "item='" + str + "'", null, null);
            if (query == null) {
                Log.m37w(TAG, "cursor is null.");
                return null;
            } else if (query.getCount() == 0) {
                query.close();
                return null;
            } else {
                query.moveToFirst();
                String string = query.getString(query.getColumnIndex("category"));
                String string2 = query.getString(query.getColumnIndex("data1"));
                String string3 = query.getString(query.getColumnIndex("data2"));
                query.close();
                if (string == null) {
                    Log.m37w(TAG, "category is null.");
                    return null;
                } else if (string2 == null) {
                    Log.m37w(TAG, "uses is null.");
                    return null;
                } else if (string3 == null) {
                    Log.m37w(TAG, "time is null.");
                    return null;
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(0, string);
                    arrayList.add(1, string2);
                    arrayList.add(2, Integer.valueOf(Integer.parseInt(string3)));
                    return arrayList;
                }
            }
        }
        Log.m37w(TAG, "SCPM is not available.");
        return null;
    }

    public boolean isAvailable() {
        Log.m29d(TAG, "isAvailable()");
        return this.mContext.getPackageManager().resolveContentProvider(AUTHORITY, 0) != null;
    }
}
