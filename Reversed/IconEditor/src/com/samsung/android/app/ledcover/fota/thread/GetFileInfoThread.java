package com.samsung.android.app.ledcover.fota.thread;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Xml;
import com.samsung.android.app.ledcover.common.DownloadUtils;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.fota.Constants;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.wrapperlibrary.C0270R;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class GetFileInfoThread extends Thread {
    private static final String TAG = "GetFileInfoThread";
    private Context context;
    private int error;
    private Handler handler;
    private boolean isRunning;
    Object obj;
    private String url;

    public GetFileInfoThread(Context context, Handler handler, String url) {
        this.context = null;
        this.handler = null;
        this.isRunning = false;
        this.url = null;
        this.error = 0;
        this.obj = new Object();
        this.context = context;
        this.handler = handler;
        this.url = url;
    }

    public synchronized void start() {
        if (this.isRunning) {
            SLog.m12v(TAG, "Thread is already run");
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
        int latestAuthVersion;
        int latestMcuVersion;
        RuntimeException ex;
        SLog.m12v(TAG, "Run Thread");
        this.isRunning = true;
        int deviceAuthVersion = -1;
        int deviceMcuVersion = -1;
        synchronized (this.obj) {
            String firmwareVersion = Secure.getString(this.context.getContentResolver(), "led_cover_firmware_version");
            if (firmwareVersion != null) {
                String[] versionArr = firmwareVersion.split(" ");
                if (versionArr.length >= 4) {
                    try {
                        deviceMcuVersion = Integer.parseInt(versionArr[0], 16);
                        deviceAuthVersion = Integer.parseInt(versionArr[1] + versionArr[2] + versionArr[3], 16);
                    } catch (NumberFormatException ex2) {
                        ex2.printStackTrace();
                        deviceMcuVersion = -1;
                        deviceAuthVersion = -1;
                    }
                }
            }
            if (deviceAuthVersion < 0 || deviceMcuVersion < 0) {
                SLog.m12v(TAG, "Error when getting Led Cover firmware");
                this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
            } else {
                String xml = getRequest();
                if (xml == null) {
                    SLog.m12v(TAG, "Error when getting request :: error code: " + this.error);
                    this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                } else {
                    Bundle bundle = getFileInfoFromXml(xml);
                    if (bundle == null || bundle.getInt("error") > 0) {
                        SLog.m12v(TAG, "Error when getting file info :: error code: " + this.error);
                        this.handler.obtainMessage(1, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                    } else {
                        String version = bundle.getString("version");
                        if (version == null || version.length() < 13) {
                            SLog.m12v(TAG, "Error when parsing version");
                            this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                        } else {
                            try {
                                int index = version.indexOf("A_");
                                index = index >= 0 ? index + 2 : -1;
                                latestAuthVersion = Integer.parseInt(index > 0 ? version.substring(index, index + 6) : null, 16);
                                index = version.indexOf("M_");
                                index = index >= 0 ? index + 2 : -1;
                                latestMcuVersion = Integer.parseInt(index > 0 ? version.substring(index, index + 2) : null, 16);
                            } catch (RuntimeException e) {
                                ex = e;
                                ex.printStackTrace();
                                latestMcuVersion = -1;
                                latestAuthVersion = -1;
                                if (latestAuthVersion >= 0) {
                                }
                                SLog.m12v(TAG, "Error when parsing version");
                                this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                                this.isRunning = false;
                                this.error = 0;
                                SLog.m12v(TAG, "End Thread");
                            } catch (RuntimeException e2) {
                                ex = e2;
                                ex.printStackTrace();
                                latestMcuVersion = -1;
                                latestAuthVersion = -1;
                                if (latestAuthVersion >= 0) {
                                }
                                SLog.m12v(TAG, "Error when parsing version");
                                this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                                this.isRunning = false;
                                this.error = 0;
                                SLog.m12v(TAG, "End Thread");
                            }
                            if (latestAuthVersion >= 0 || latestMcuVersion < 0) {
                                SLog.m12v(TAG, "Error when parsing version");
                                this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                            } else if ((latestAuthVersion <= deviceAuthVersion || latestMcuVersion < deviceMcuVersion) && (latestAuthVersion < deviceAuthVersion || latestMcuVersion <= deviceMcuVersion)) {
                                SLog.m12v(TAG, "Device firmware already up to date");
                                this.handler.obtainMessage(0, C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle, this.error).sendToTarget();
                            } else {
                                this.handler.obtainMessage(1, 100, this.error, bundle).sendToTarget();
                            }
                        }
                    }
                }
            }
        }
        this.isRunning = false;
        this.error = 0;
        SLog.m12v(TAG, "End Thread");
    }

    private String getRequest() {
        SocketTimeoutException e;
        Throwable th;
        Exception e2;
        String responseString = null;
        HttpURLConnection connection = null;
        BufferedInputStream inputStream = null;
        try {
            SLog.m12v(TAG, "URL: " + this.url);
            if (this.url == null) {
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
            connection = (HttpURLConnection) new URL(this.url).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(Constants.CONNECTION_DEFAULT_TIME_OUT);
            connection.setReadTimeout(Constants.CONNECTION_DEFAULT_TIME_OUT);
            HttpURLConnection httpConn = connection;
            int responseCode = httpConn.getResponseCode();
            String responseMessage = httpConn.getResponseMessage();
            if (responseCode >= Constants.HTTP_ERROR_BAD_REQUEST) {
                SLog.m12v(TAG, "URL reading time error in,eror code: 400");
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
            bundle.putInt("error", this.error);
        } else {
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
                            String downlodUrl = xpp.getText();
                            String fileName = Uri.parse(downlodUrl).getQueryParameter("file");
                            bundle.putString("url", downlodUrl);
                            bundle.putString(Defines.PKG_COL_PACKAGE_NAME, fileName);
                            SLog.m12v(TAG, name + ": " + this.url + ", filename = " + fileName);
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
            } catch (XmlPullParserException e) {
                this.error = C0270R.styleable.AppCompatTheme_autoCompleteTextViewStyle;
                bundle.putInt("error", this.error);
            } catch (IOException e2) {
                this.error = C0270R.styleable.AppCompatTheme_buttonStyle;
                bundle.putInt("error", this.error);
            }
        }
        return bundle;
    }
}
