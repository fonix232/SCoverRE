package com.samsung.android.app.ledcover.update;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.app.LCoverRootActivity;
import com.samsung.android.app.ledcover.common.SLog;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ApkDownload extends AsyncTask<String, Integer, String> {
    public static final String TAG = "[LED_COVER]ApkDownload";
    public static boolean isApkDownloading;
    Handler apkHandler;
    private StubListener listener;
    private String signature;
    private String url;

    static {
        isApkDownloading = false;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setListener(StubListener listener) {
        this.listener = listener;
    }

    public void run() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{this.url});
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.apkHandler = LCoverRootActivity.apkHandler;
        this.apkHandler.obtainMessage(1).sendToTarget();
        SLog.m12v(TAG, "[ApkDownload] ==> download start");
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Dialog mDialog = StubUtil.detailsDialog;
        if (mDialog != null) {
            TextView percentAPKCopied = (TextView) mDialog.findViewById(C0198R.id.percentage_copied_apk);
            ((ProgressBar) mDialog.findViewById(C0198R.id.progress_bar_apk)).setProgress(values[0].intValue());
            if (percentAPKCopied != null) {
                percentAPKCopied.setText(values[0] + "%");
            }
        }
    }

    protected String doInBackground(String... params) {
        Throwable ex;
        Throwable th;
        File apkFile = null;
        InputStream in = null;
        OutputStream out = null;
        SLog.m12v(TAG, "[ApkDownload] ==> do downloading...");
        try {
            String requestUrl = params[0];
            URL url = new URL(requestUrl);
            StubUtil.log(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            Map<String, List<String>> properties = connection.getRequestProperties();
            for (String key : properties.keySet()) {
                StubUtil.log(key + ": " + ((String) ((List) properties.get(key)).get(0)));
            }
            StubUtil.log("\n");
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (String key2 : headers.keySet()) {
                if (key2 == null) {
                    StubUtil.log((String) ((List) headers.get(key2)).get(0));
                } else {
                    for (String value : (List) headers.get(key2)) {
                        StubUtil.log(key2 + ": " + value);
                    }
                }
            }
            StubUtil.log("\n");
            if (200 != connection.getResponseCode()) {
                throw new IOException("status code " + connection.getResponseCode() + " != " + 200);
            }
            long length = (long) connection.getContentLength();
            apkFile = File.createTempFile(StubUtil.APK_FILE_PREFIX, StubUtil.APK_FILE_SUFFIX, StubUtil.APK_DOWNLOAD_PATH);
            if (apkFile == null) {
                connection.disconnect();
                StubUtil.close(null);
                StubUtil.close(null);
                return null;
            }
            apkFile.setReadable(true, false);
            apkFile.setExecutable(true, false);
            apkFile.deleteOnExit();
            SLog.m12v(TAG, "irin length ==> " + length);
            SLog.m12v(TAG, "APK_DOWNLOAD_PATH ==> " + StubUtil.APK_DOWNLOAD_PATH);
            InputStream in2 = new BufferedInputStream(connection.getInputStream(), AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
            try {
                OutputStream fileOutputStream = new FileOutputStream(apkFile);
                long total = 0;
                try {
                    byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                    while (true) {
                        int count = in2.read(buffer);
                        if (count == -1) {
                            break;
                        }
                        total += (long) count;
                        SLog.m12v(TAG, "irin total ==> " + total);
                        Integer[] numArr = new Integer[1];
                        numArr[0] = Integer.valueOf((int) ((100 * total) / length));
                        publishProgress(numArr);
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.flush();
                    connection.disconnect();
                    if (StubUtil.validateApkSignature(apkFile.getAbsolutePath(), this.signature)) {
                        StubUtil.log(apkFile.getAbsolutePath());
                        String absolutePath = apkFile.getAbsolutePath();
                        StubUtil.close(fileOutputStream);
                        StubUtil.close(in2);
                        out = fileOutputStream;
                        in = in2;
                        return absolutePath;
                    }
                    StubUtil.log("Validation failed !!");
                    if (apkFile != null) {
                        apkFile.delete();
                    }
                    StubUtil.close(fileOutputStream);
                    StubUtil.close(in2);
                    out = fileOutputStream;
                    in = in2;
                    return null;
                } catch (Exception e) {
                    ex = e;
                    out = fileOutputStream;
                    in = in2;
                    try {
                        StubUtil.log(ex);
                        if (apkFile != null) {
                            apkFile.delete();
                        }
                        StubUtil.close(out);
                        StubUtil.close(in);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        StubUtil.close(out);
                        StubUtil.close(in);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    out = fileOutputStream;
                    in = in2;
                    StubUtil.close(out);
                    StubUtil.close(in);
                    throw th;
                }
            } catch (Exception e2) {
                ex = e2;
                in = in2;
                StubUtil.log(ex);
                if (apkFile != null) {
                    apkFile.delete();
                }
                StubUtil.close(out);
                StubUtil.close(in);
                return null;
            } catch (Throwable th4) {
                th = th4;
                in = in2;
                StubUtil.close(out);
                StubUtil.close(in);
                throw th;
            }
        } catch (Exception e3) {
            ex = e3;
        }
    }

    protected void onPostExecute(String apkFilePath) {
        SLog.m12v(TAG, "[ApkDownload] ==> finish to download");
        SLog.m12v(TAG, "irin ==> " + apkFilePath);
        this.apkHandler.obtainMessage(2, Integer.valueOf(0)).sendToTarget();
        if (apkFilePath == null) {
            this.listener.onDownloadApkFail();
        } else {
            this.listener.onDownloadApkSuccess(apkFilePath);
        }
    }
}
