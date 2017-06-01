package com.samsung.android.feature;

import android.os.SystemProperties;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class SemCscFeature {
    private static final String FEATURE_XML = "/system/csc/feature.xml";
    private static final String MPS_FEATURE_XML = "/system/csc/others.xml";
    private static final String TAG = "SemCscFeature";
    private static SemCscFeature sInstance = null;
    private Hashtable<String, String> mFeatureList = new Hashtable();

    private SemCscFeature() {
        boolean z = false;
        try {
            if (new File("/system/omc/SW_Configuration.xml").exists()) {
                z = true;
            }
            String str = SystemProperties.get("persist.sys.omc_path", "");
            String str2 = SystemProperties.get("persist.sys.omcnw_path", "");
            if (loadFeatureFile(z, str)) {
                loadNetworkFeatureFile(z, str2);
            }
        } catch (Throwable e) {
            Log.w(TAG, e.toString());
        }
    }

    public static SemCscFeature getInstance() {
        if (sInstance == null) {
            sInstance = new SemCscFeature();
        }
        return sInstance;
    }

    private boolean loadFeatureFile(boolean z, String str) {
        Throwable e;
        Throwable e2;
        Throwable th;
        FileInputStream fileInputStream = null;
        Object obj = null;
        boolean z2 = false;
        try {
            this.mFeatureList.clear();
            String str2 = z ? str : "/system/csc";
            File file = new File(str2 + "/cscfeature.xml");
            if (!file.exists() || file.length() <= 0) {
                file = new File(str2 + "/feature.xml");
                if (!file.exists() || file.length() <= 0) {
                    file = new File(str2 + "/others.xml");
                    if (!file.exists() || file.length() <= 0) {
                        return false;
                    }
                }
            }
            z2 = true;
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            XmlPullParser newPullParser = newInstance.newPullParser();
            InputStream fileInputStream2 = new FileInputStream(file);
            try {
                newPullParser.setInput(fileInputStream2, null);
                int eventType = newPullParser.getEventType();
                while (eventType != 1) {
                    if (eventType == 2) {
                        obj = newPullParser.getName();
                    } else if (eventType == 4) {
                        String text = newPullParser.getText();
                        if (!(obj == null || text == null)) {
                            if (this.mFeatureList.containsKey(obj)) {
                                try {
                                    eventType = newPullParser.next();
                                } catch (Throwable e3) {
                                    Log.w(TAG, e3.toString());
                                }
                            } else {
                                try {
                                    this.mFeatureList.put(obj, text.trim());
                                } catch (Throwable e4) {
                                    Log.w(TAG, e4.toString());
                                }
                            }
                        }
                    }
                    try {
                        eventType = newPullParser.next();
                    } catch (Throwable e32) {
                        Log.w(TAG, e32.toString());
                    }
                }
                try {
                    fileInputStream2.close();
                } catch (Throwable e322) {
                    Log.w(TAG, e322.toString());
                }
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (Throwable e3222) {
                        Log.w(TAG, e3222.toString());
                    }
                    return z2;
                }
                InputStream inputStream = fileInputStream2;
            } catch (XmlPullParserException e5) {
                e = e5;
                fileInputStream = fileInputStream2;
            } catch (FileNotFoundException e6) {
                e2 = e6;
                fileInputStream = fileInputStream2;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = fileInputStream2;
            }
        } catch (XmlPullParserException e7) {
            e = e7;
            try {
                Log.w(TAG, e.toString());
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e32222) {
                        Log.w(TAG, e32222.toString());
                    }
                }
                return z2;
            } catch (Throwable th3) {
                th = th3;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e322222) {
                        Log.w(TAG, e322222.toString());
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e8) {
            e2 = e8;
            Log.w(TAG, e2.toString());
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable e3222222) {
                    Log.w(TAG, e3222222.toString());
                }
            }
            return z2;
        }
        return z2;
    }

    private void loadNetworkFeatureFile(boolean z, String str) {
        Throwable e;
        Throwable e2;
        Throwable th;
        FileInputStream fileInputStream = null;
        Object obj = null;
        try {
            File file = new File((z ? str : "/system/csc") + "/cscfeature_network.xml");
            if (file.exists() && file.length() > 0) {
                XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
                newInstance.setNamespaceAware(true);
                XmlPullParser newPullParser = newInstance.newPullParser();
                InputStream fileInputStream2 = new FileInputStream(file);
                try {
                    newPullParser.setInput(fileInputStream2, null);
                    int eventType = newPullParser.getEventType();
                    while (eventType != 1) {
                        if (eventType == 2) {
                            obj = newPullParser.getName();
                        } else if (eventType == 4) {
                            String text = newPullParser.getText();
                            if (!(obj == null || text == null)) {
                                if (this.mFeatureList.containsKey(obj)) {
                                    try {
                                        eventType = newPullParser.next();
                                    } catch (Throwable e3) {
                                        Log.w(TAG, e3.toString());
                                    }
                                } else {
                                    try {
                                        this.mFeatureList.put(obj, text.trim());
                                    } catch (Throwable e4) {
                                        Log.w(TAG, e4.toString());
                                    }
                                }
                            }
                        }
                        try {
                            eventType = newPullParser.next();
                        } catch (Throwable e32) {
                            Log.w(TAG, e32.toString());
                        }
                    }
                    try {
                        fileInputStream2.close();
                    } catch (Throwable e322) {
                        Log.w(TAG, e322.toString());
                    }
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (Throwable e3222) {
                            Log.w(TAG, e3222.toString());
                        }
                    }
                    InputStream inputStream = fileInputStream2;
                } catch (XmlPullParserException e5) {
                    e = e5;
                    fileInputStream = fileInputStream2;
                } catch (FileNotFoundException e6) {
                    e2 = e6;
                    fileInputStream = fileInputStream2;
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = fileInputStream2;
                }
            }
        } catch (XmlPullParserException e7) {
            e = e7;
            try {
                Log.w(TAG, e.toString());
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e32222) {
                        Log.w(TAG, e32222.toString());
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e322222) {
                        Log.w(TAG, e322222.toString());
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e8) {
            e2 = e8;
            Log.w(TAG, e2.toString());
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable e3222222) {
                    Log.w(TAG, e3222222.toString());
                }
            }
        }
    }

    public boolean getBoolean(String str) {
        try {
            String str2 = (String) this.mFeatureList.get(str);
            return str2 != null ? Boolean.parseBoolean(str2) : false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getBoolean(String str, boolean z) {
        try {
            String str2 = (String) this.mFeatureList.get(str);
            return str2 != null ? Boolean.parseBoolean(str2) : z;
        } catch (Exception e) {
            return z;
        }
    }

    public int getInt(String str) {
        try {
            String str2 = (String) this.mFeatureList.get(str);
            return str2 != null ? Integer.parseInt(str2) : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public int getInt(String str, int i) {
        try {
            String str2 = (String) this.mFeatureList.get(str);
            return str2 != null ? Integer.parseInt(str2) : i;
        } catch (Exception e) {
            return i;
        }
    }

    public int getInteger(String str) {
        return getInt(str);
    }

    public int getInteger(String str, int i) {
        return getInt(str, i);
    }

    public String getString(String str) {
        try {
            String str2 = (String) this.mFeatureList.get(str);
            return str2 != null ? str2 : "";
        } catch (Exception e) {
            return "";
        }
    }

    public String getString(String str, String str2) {
        try {
            String str3 = (String) this.mFeatureList.get(str);
            return str3 != null ? str3 : str2;
        } catch (Exception e) {
            return str2;
        }
    }
}
