package com.samsung.android.app.ledcover.noti.applist;

import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import java.util.ArrayList;

public interface ILCoverManager {
    ArrayList<LCoverAppInfo> getInstalledAppList();

    void updateAppList();
}
