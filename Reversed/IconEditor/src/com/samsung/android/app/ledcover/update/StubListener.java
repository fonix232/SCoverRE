package com.samsung.android.app.ledcover.update;

public interface StubListener {
    void onDownloadApkFail();

    void onDownloadApkSuccess(String str);

    void onGetDownloadUrlFail(StubData stubData);

    void onGetDownloadUrlSuccess(StubData stubData);

    void onNoMatchingApplication(StubData stubData);

    void onUpdateAvailable(StubData stubData);

    void onUpdateCheckFail(StubData stubData);

    void onUpdateNotNecessary(StubData stubData);
}
