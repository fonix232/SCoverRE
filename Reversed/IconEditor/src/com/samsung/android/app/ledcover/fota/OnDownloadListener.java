package com.samsung.android.app.ledcover.fota;

import java.util.ArrayList;

public interface OnDownloadListener {
    void onCompleted(String str, int i, ArrayList<String> arrayList);

    void onFailed(int i);

    void onGetFirmwareInfo(String str, String str2);

    void onProgress(int i);

    void onStarted();
}
