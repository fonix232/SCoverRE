package com.samsung.android.app.ledcover.fota;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.samsung.android.app.ledcover.common.DownloadUtils;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.UnZipper;
import com.samsung.android.app.ledcover.fota.thread.DownloadThread;
import com.samsung.android.app.ledcover.fota.thread.GetFileInfoThread;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.wrapperlibrary.C0270R;
import com.samsung.android.sdk.cover.ScoverState;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FOTAManager {
    private static final String TAG = "FOTAManager";
    Handler DownloadHandler;
    private Context context;
    DownloadThread downloadThread;
    GetFileInfoThread fileInfoThread;
    Handler getInfoHandler;
    private OnDownloadListener listener;

    /* renamed from: com.samsung.android.app.ledcover.fota.FOTAManager.1 */
    class C02451 extends Handler {
        C02451() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int result = msg.arg1;
            int what = msg.what;
            int error = msg.arg2;
            switch (result) {
                case ScoverState.TYPE_BRAND_MONBLANC_COVER /*100*/:
                    Bundle bundle = msg.obj;
                    String name = bundle.getString(Defines.PKG_COL_PACKAGE_NAME);
                    String version = bundle.getString("version");
                    SLog.m12v(FOTAManager.TAG, "name: " + name + "version: " + version);
                    if (!(version == null || version.equals("-1") || FOTAManager.this.listener == null)) {
                        FOTAManager.this.listener.onGetFirmwareInfo(name, version);
                        break;
                    }
                case C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle /*101*/:
                    SLog.m12v(FOTAManager.TAG, "getFirmwareInfo FAIL : error code: " + error + " on: " + what);
                    break;
            }
            if (FOTAManager.this.fileInfoThread != null) {
                FOTAManager.this.fileInfoThread.close();
                FOTAManager.this.fileInfoThread = null;
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.fota.FOTAManager.2 */
    class C02462 extends Handler {
        C02462() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            int result = msg.arg1;
            int error = msg.arg2;
            if (what == 5) {
                int progress = msg.arg1;
                if (FOTAManager.this.listener != null) {
                    FOTAManager.this.listener.onProgress(progress);
                    return;
                }
                return;
            }
            if (result == C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle) {
                SLog.m12v(FOTAManager.TAG, "Error on " + what);
                if (FOTAManager.this.listener != null) {
                    FOTAManager.this.listener.onFailed(error);
                }
            } else if (what == 3) {
                SLog.m12v(FOTAManager.TAG, "Download completed");
                Bundle bundle = msg.obj;
                if (bundle != null) {
                    int size = bundle.getInt("size");
                    String path = bundle.getString("path");
                    String hashKey = bundle.getString("hashKey");
                    SLog.m12v(FOTAManager.TAG, "file path : " + path);
                    SLog.m12v(FOTAManager.TAG, "hashKey : " + hashKey);
                    if (FOTAManager.this.listener != null) {
                        File zipFile = new File(path);
                        if (DownloadUtils.verifyFileSha256(zipFile, hashKey)) {
                            ArrayList<String> unzipFiles = null;
                            try {
                                unzipFiles = UnZipper.unzip(zipFile, FOTAManager.this.context.getDir("test", 0));
                            } catch (IOException e) {
                                e.printStackTrace();
                                FOTAManager.this.listener.onFailed(C0270R.styleable.AppCompatTheme_buttonStyle);
                            }
                            FOTAManager.this.listener.onCompleted(path, size, unzipFiles);
                        } else {
                            zipFile.delete();
                            FOTAManager.this.listener.onFailed(C0270R.styleable.AppCompatTheme_editTextStyle);
                        }
                    }
                } else if (FOTAManager.this.listener != null) {
                    FOTAManager.this.listener.onFailed(Constants.ERROR_GENERAL);
                }
            }
            FOTAManager.this.downloadThread.close();
            FOTAManager.this.downloadThread = null;
        }
    }

    public FOTAManager(Context context, OnDownloadListener listener) {
        this.listener = null;
        this.context = null;
        this.getInfoHandler = new C02451();
        this.DownloadHandler = new C02462();
        this.context = context;
        Thread mainThread = context.getMainLooper().getThread();
        this.listener = listener;
    }

    public void getFirmwareInfo(String prodId, String location) {
        this.fileInfoThread = new GetFileInfoThread(this.context, this.getInfoHandler, DownloadUtils.makeURL(prodId, location, Constants.STR_URL_TYPE_FIRMWARE));
        this.fileInfoThread.start();
    }

    public void startDownload(String prodId, String location) {
        this.downloadThread = new DownloadThread(this.context, this.DownloadHandler, prodId, location);
        this.downloadThread.start();
        if (this.listener != null) {
            this.listener.onStarted();
        }
    }
}
