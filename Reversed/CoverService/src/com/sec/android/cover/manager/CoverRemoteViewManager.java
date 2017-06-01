package com.sec.android.cover.manager;

import android.content.Context;
import android.util.Log;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.Constants;
import com.sec.android.cover.CoverExecutiveObservator;
import com.sec.android.cover.monitor.CoverUpdateMonitor.RemoteViewInfo;
import com.sec.android.cover.monitor.CoverUpdateMonitorCallback;
import java.util.HashMap;

public class CoverRemoteViewManager extends BaseCoverObservator {
    private static final String TAG = CoverRemoteViewManager.class.getSimpleName();
    private HashMap<String, RemoteViewInfo> mRemoteViewInfoTable = new HashMap();
    private CoverUpdateMonitorCallback mUpdateMonitorCallback = new C00671();

    class C00671 extends CoverUpdateMonitorCallback {
        C00671() {
        }

        public void onRemoteViewUpdated(RemoteViewInfo remoteViewInfo) {
            Log.d(CoverRemoteViewManager.TAG, "onRemoteViewUpdated : " + remoteViewInfo.toString());
            CoverRemoteViewManager.this.updateRemoteView(remoteViewInfo);
        }
    }

    public CoverRemoteViewManager(Context context) {
        super(context);
        Log.d(Constants.TAG, "create CoverRemoteViewManager");
    }

    public void clear() {
        this.mRemoteViewInfoTable.clear();
    }

    private boolean updateRemoteView(RemoteViewInfo remoteViewInfo) {
        this.mRemoteViewInfoTable.put(remoteViewInfo.mType, remoteViewInfo);
        return true;
    }

    public RemoteViewInfo getRemoteViewInfo(String type) {
        return (RemoteViewInfo) this.mRemoteViewInfoTable.get(type);
    }

    public boolean isRemoteViewAvailable(String type) {
        RemoteViewInfo info = getRemoteViewInfo(type);
        if (info == null) {
            return false;
        }
        if (info.mVisibility) {
            return true;
        }
        return false;
    }

    public void start() {
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().registerCallback(this.mUpdateMonitorCallback);
    }

    public void stop() {
        CoverExecutiveObservator.getInstance(getContext()).getCoverUpdateMonitor().unregisterCallback(this.mUpdateMonitorCallback);
    }
}
