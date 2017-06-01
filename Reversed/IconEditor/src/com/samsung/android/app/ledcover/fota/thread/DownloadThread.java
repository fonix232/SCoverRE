package com.samsung.android.app.ledcover.fota.thread;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Xml;
import com.samsung.android.app.ledcover.common.DownloadUtils;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.fota.Constants;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.wrapperlibrary.C0270R;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private Context context;
    private Bundle downloadInfo;
    private int error;
    private String firmXmlUrl;
    private Handler handler;
    private String hashXmlUrl;
    private boolean isRunning;
    Object syncObj;

    public DownloadThread(Context context, Handler handler, String prodId, String location) {
        this.context = null;
        this.handler = null;
        this.isRunning = false;
        this.firmXmlUrl = null;
        this.hashXmlUrl = null;
        this.downloadInfo = null;
        this.error = 0;
        this.syncObj = new Object();
        this.context = context;
        this.handler = handler;
        this.firmXmlUrl = DownloadUtils.makeURL(prodId, location, Constants.STR_URL_TYPE_FIRMWARE);
        this.hashXmlUrl = DownloadUtils.makeURL(prodId, location, Constants.STR_URL_TYPE_HASH);
    }

    public DownloadThread(Context context, Handler handler, String url) {
        this.context = null;
        this.handler = null;
        this.isRunning = false;
        this.firmXmlUrl = null;
        this.hashXmlUrl = null;
        this.downloadInfo = null;
        this.error = 0;
        this.syncObj = new Object();
        this.context = context;
        this.handler = handler;
        this.firmXmlUrl = url;
    }

    public synchronized void start() {
        if (this.isRunning) {
            SLog.m12v(TAG, "Thread is already running");
        } else {
            super.start();
        }
    }

    public void close() {
        if (this.isRunning) {
            interrupt();
        }
    }

    public void run() {
        this.isRunning = true;
        synchronized (this.syncObj) {
            SLog.m12v(TAG, "Run Download");
            String hashKey = C0316a.f163d;
            String hashResponse = getRequest(2);
            if (hashResponse == null || this.error != 0) {
                this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                return;
            }
            Bundle bundle = getFileInfoFromXml(hashResponse);
            if (bundle == null || this.error != 0) {
                this.handler.obtainMessage(2, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                return;
            }
            int result;
            if (downloadFile(2, bundle.getString("url"), bundle.getString(Defines.PKG_COL_PACKAGE_NAME))) {
                result = 100;
            } else {
                result = C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle;
            }
            if (result == 100) {
                try {
                    hashKey = DownloadUtils.readFromFile(this.downloadInfo.getString("path"));
                    if (!hashKey.equals(C0316a.f163d)) {
                        String firmwareResponse = getRequest(1);
                        if (firmwareResponse == null || this.error != 0) {
                            this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                        } else {
                            bundle = getFileInfoFromXml(firmwareResponse);
                            if (bundle == null || this.error != 0) {
                                this.handler.obtainMessage(1, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                            } else {
                                if (downloadFile(1, bundle.getString("url"), bundle.getString(Defines.PKG_COL_PACKAGE_NAME))) {
                                    result = 100;
                                } else {
                                    result = C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle;
                                }
                                this.downloadInfo.putString("hashKey", hashKey);
                                this.handler.obtainMessage(3, result, this.error, this.downloadInfo).sendToTarget();
                            }
                        }
                    }
                    this.isRunning = false;
                    this.error = 0;
                    SLog.m12v(TAG, "End Thread");
                    return;
                } catch (IOException e) {
                    SLog.m12v(TAG, "readFromFile Failed : " + this.downloadInfo.getString("path"));
                    this.handler.obtainMessage(4, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, C0270R.styleable.AppCompatTheme_buttonStyle).sendToTarget();
                    return;
                }
            }
            this.handler.obtainMessage(4, result, this.error).sendToTarget();
            return;
        }
    }

    private String getRequest(int urlType) {
        SocketTimeoutException e;
        Throwable th;
        Exception e2;
        String responseString = null;
        HttpURLConnection connection = null;
        BufferedInputStream inputStream = null;
        String serverUrl = urlType == 1 ? this.firmXmlUrl : this.hashXmlUrl;
        try {
            SLog.m12v(TAG, "URL: " + serverUrl);
            if (serverUrl == null) {
                this.error = C0270R.styleable.AppCompatTheme_checkboxStyle;
                SLog.m12v(TAG, "URL is null");
                if (connection != null) {
                    connection.disconnect();
                }
                if (inputStream == null) {
                    return null;
                }
                try {
                    inputStream.close();
                    return null;
                } catch (IOException e3) {
                    SLog.m12v(TAG, "IO Exception while close FileInputStream");
                    return null;
                }
            }
            connection = (HttpURLConnection) new URL(serverUrl).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(Constants.CONNECTION_DEFAULT_TIME_OUT);
            connection.setReadTimeout(Constants.CONNECTION_DEFAULT_TIME_OUT);
            HttpURLConnection httpConn = connection;
            int responseCode = httpConn.getResponseCode();
            String responseMessage = httpConn.getResponseMessage();
            if (responseCode >= Constants.HTTP_ERROR_BAD_REQUEST) {
                SLog.m12v(TAG, "URL reading time error in,eror code:400");
                InputStream is = httpConn.getErrorStream();
                this.error = responseCode;
            } else {
                SLog.m12v(TAG, "Get Input Stream");
                BufferedInputStream inputStream2 = new BufferedInputStream(connection.getInputStream(), AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
                if (inputStream2 != null) {
                    try {
                        responseString = DownloadUtils.convertStreamToString(inputStream2);
                        inputStream = inputStream2;
                    } catch (SocketTimeoutException e4) {
                        e = e4;
                        inputStream = inputStream2;
                        try {
                            SLog.m12v(TAG, "Connection time out");
                            e.printStackTrace();
                            this.error = C0270R.styleable.AppCompatTheme_buttonStyleSmall;
                            if (connection != null) {
                                connection.disconnect();
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e5) {
                                    SLog.m12v(TAG, "IO Exception while close FileInputStream");
                                }
                            }
                            return responseString;
                        } catch (Throwable th2) {
                            th = th2;
                            if (connection != null) {
                                connection.disconnect();
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e6) {
                                    SLog.m12v(TAG, "IO Exception while close FileInputStream");
                                }
                            }
                            throw th;
                        }
                    } catch (Exception e7) {
                        e2 = e7;
                        inputStream = inputStream2;
                        SLog.m12v(TAG, "Connection error");
                        e2.printStackTrace();
                        this.error = Constants.ERROR_GENERAL;
                        if (connection != null) {
                            connection.disconnect();
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e8) {
                                SLog.m12v(TAG, "IO Exception while close FileInputStream");
                            }
                        }
                        return responseString;
                    } catch (Throwable th3) {
                        th = th3;
                        inputStream = inputStream2;
                        if (connection != null) {
                            connection.disconnect();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                }
                inputStream = inputStream2;
            }
            SLog.m12v(TAG, "firmware responseString :" + responseString);
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e9) {
                    SLog.m12v(TAG, "IO Exception while close FileInputStream");
                }
            }
            return responseString;
        } catch (SocketTimeoutException e10) {
            e = e10;
            SLog.m12v(TAG, "Connection time out");
            e.printStackTrace();
            this.error = C0270R.styleable.AppCompatTheme_buttonStyleSmall;
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return responseString;
        } catch (Exception e11) {
            e2 = e11;
            SLog.m12v(TAG, "Connection error");
            e2.printStackTrace();
            this.error = Constants.ERROR_GENERAL;
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return responseString;
        }
    }

    public Bundle getFileInfoFromXml(String xml) {
        Bundle bundle = new Bundle();
        if (xml == null) {
            this.error = C0270R.styleable.AppCompatTheme_checkboxStyle;
            return null;
        }
        try {
            StringReader sr = new StringReader(xml);
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            xpp.setInput(sr);
            xpp.nextTag();
            xpp.require(2, null, Constants.TAG_INFO_XML_START);
            while (xpp.next() != 1) {
                if (xpp.getEventType() == 2) {
                    String name = xpp.getName();
                    if (name.equals(Constants.STR_KEY_INFO_XML_FW_VERSION)) {
                        xpp.next();
                        bundle.putString("version", xpp.getText());
                        SLog.m12v(TAG, name + ": " + xpp.getText());
                    } else if (name.equals(Constants.STR_KEY_INFO_XML_DOWN_URL)) {
                        xpp.next();
                        String downloadUrl = xpp.getText();
                        String fileName = Uri.parse(downloadUrl).getQueryParameter("file");
                        bundle.putString("url", downloadUrl);
                        bundle.putString(Defines.PKG_COL_PACKAGE_NAME, fileName);
                        SLog.m12v(TAG, name + ": " + downloadUrl + ", filename = " + fileName);
                    } else if (name.equals(Constants.STR_KEY_INFO_XML_DESC)) {
                        xpp.next();
                        bundle.putString("description", xpp.getText());
                        SLog.m12v(TAG, name + ": " + xpp.getText());
                    } else if (name.equals(Constants.STR_KEY_INFO_XML_DESC_KR)) {
                        xpp.next();
                        bundle.putString("description_kor", xpp.getText());
                        SLog.m12v(TAG, name + ": " + xpp.getText());
                    }
                }
            }
            return bundle;
        } catch (XmlPullParserException e) {
            this.error = C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle;
            return bundle;
        } catch (IOException e2) {
            this.error = C0270R.styleable.AppCompatTheme_buttonStyle;
            return bundle;
        }
    }

    public boolean downloadFile(int urlType, String fileURL, String fileName) {
        SocketTimeoutException e;
        Throwable th;
        IOException e2;
        SLog.m12v(TAG, "downloadFile: " + fileName);
        int downloadedSize = 0;
        int progress = 0;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) new URL(fileURL).openConnection();
            httpConn.setConnectTimeout(Constants.CONNECTION_DEFAULT_TIME_OUT);
            httpConn.setReadTimeout(Constants.CONNECTION_DEFAULT_TIME_OUT);
            int responseCode = httpConn.getResponseCode();
            if (responseCode == 200) {
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int totalSize = httpConn.getContentLength();
                SLog.m12v(TAG, "Total Size: " + totalSize);
                inputStream = httpConn.getInputStream();
                String internal_path = this.context.getDir("test", 0).getAbsolutePath();
                SLog.m12v(TAG, "internal path: " + internal_path);
                DownloadUtils.set_permission(internal_path, 504);
                String saveFilePath = internal_path + File.separator + fileName;
                FileOutputStream outputStream2 = new FileOutputStream(saveFilePath);
                try {
                    byte[] buffer = new byte[Constants.DOWNLOAD_BUFFER_SIZE];
                    while (true) {
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        outputStream2.write(buffer, 0, bytesRead);
                        downloadedSize += bytesRead;
                        int i = (int) ((((float) downloadedSize) / ((float) totalSize)) * 100.0f);
                        if (r0 > progress) {
                            progress = (int) ((((float) downloadedSize) / ((float) totalSize)) * 100.0f);
                            SLog.m12v(TAG, progress + "% Downloaded");
                            if (urlType == 1) {
                                this.handler.obtainMessage(5, progress, this.error).sendToTarget();
                            }
                        }
                    }
                    this.downloadInfo = new Bundle();
                    this.downloadInfo.putString("path", saveFilePath);
                    this.downloadInfo.putInt("size", downloadedSize);
                    System.out.println("File downloaded");
                    outputStream = outputStream2;
                } catch (SocketTimeoutException e3) {
                    e = e3;
                    outputStream = outputStream2;
                    try {
                        SLog.m12v(TAG, "Connection time out");
                        e.printStackTrace();
                        this.error = C0270R.styleable.AppCompatTheme_buttonStyleSmall;
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e4) {
                                SLog.m12v(TAG, "IO Exception while close FileInputStream");
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpConn != null) {
                            httpConn.disconnect();
                        }
                        if (this.error > 0) {
                            return true;
                        }
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e5) {
                                SLog.m12v(TAG, "IO Exception while close FileInputStream");
                                throw th;
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                } catch (IOException e6) {
                    e2 = e6;
                    outputStream = outputStream2;
                    e2.printStackTrace();
                    this.error = C0270R.styleable.AppCompatTheme_buttonStyle;
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e7) {
                            SLog.m12v(TAG, "IO Exception while close FileInputStream");
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpConn != null) {
                        httpConn.disconnect();
                    }
                    if (this.error > 0) {
                        return false;
                    }
                    return true;
                } catch (Throwable th3) {
                    th = th3;
                    outputStream = outputStream2;
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
            }
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            this.error = responseCode;
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e8) {
                    SLog.m12v(TAG, "IO Exception while close FileInputStream");
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (SocketTimeoutException e9) {
            e = e9;
            SLog.m12v(TAG, "Connection time out");
            e.printStackTrace();
            this.error = C0270R.styleable.AppCompatTheme_buttonStyleSmall;
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpConn != null) {
                httpConn.disconnect();
            }
            if (this.error > 0) {
                return true;
            }
            return false;
        } catch (IOException e10) {
            e2 = e10;
            e2.printStackTrace();
            this.error = C0270R.styleable.AppCompatTheme_buttonStyle;
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpConn != null) {
                httpConn.disconnect();
            }
            if (this.error > 0) {
                return false;
            }
            return true;
        }
        if (httpConn != null) {
            httpConn.disconnect();
        }
        if (this.error > 0) {
            return false;
        }
        return true;
    }
}
