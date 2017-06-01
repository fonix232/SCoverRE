package com.samsung.android.theme;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.Settings.System;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.HashMap;
import java.util.Map.Entry;

public class SThemeManager {
    public static final String ACTION_FESTIVAL_EFFECT_CHANGED = "android.intent.action.FESTIVAL_EFFECT_CHANGED";
    public static final String ACTION_THEME_CHANGED = "android.intent.action.STHEME_CHANGED";
    private static final String CSC_FILE_THEME_APP_LIST = "/system/csc/theme_app_list.xml";
    public static final String CURRENT_FESTIVAL_EFFECT_PACKAGE = "current_festival_effect_package";
    public static final String CURRENT_FESTIVAL_WALLPAPER_CLASS = "current_festival_wallpaper_class";
    public static final String CURRENT_FESTIVAL_WALLPAPER_PACKAGE = "current_festival_wallpaper_package";
    public static final String CURRENT_THEME_PACKAGE = "current_sec_theme_package";
    private static final boolean DBG = false;
    private static final String FESTIVAL_EFFECT_STR = "festival";
    private static final String TAG = "SThemeManager";
    private static final String TAG_APP_LIST = "ThemeAppList";
    private static final String TAG_ATTR_CLASSNAME = "className";
    private static final String TAG_ATTR_ICONID = "iconId";
    private static final String TAG_THEME_APP = "ThemeApp";
    private static final String THEME_STR = "theme";
    public static final int TYPE_FESTIVAL_EFFECT = 1;
    public static final int TYPE_THEME = 0;
    private static HashMap<String, String> sPackageIconMap = new HashMap();
    private Context mContext;
    private boolean mPackageIconLoaded;
    private String mPackageName;
    private int mType;

    public SThemeManager(Context context) {
        this(context, 0);
    }

    public SThemeManager(Context context, int i) {
        this.mPackageIconLoaded = false;
        this.mContext = context;
        this.mType = i;
        resetTheme();
    }

    private void getCurrentResourcePackage() {
        if (this.mType == 0) {
            this.mPackageName = System.getString(this.mContext.getContentResolver(), CURRENT_THEME_PACKAGE);
        } else if (this.mType == 1) {
            this.mPackageName = System.getString(this.mContext.getContentResolver(), CURRENT_FESTIVAL_EFFECT_PACKAGE);
        }
    }

    private Resources getResources() {
        Resources resources = null;
        if (this.mPackageName.isEmpty()) {
            return this.mContext.getResources();
        }
        try {
            return this.mContext.getPackageManager().getResourcesForApplication(this.mPackageName);
        } catch (Throwable e) {
            e.printStackTrace();
            return resources;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadThemeAppList() {
        /*
        r26 = this;
        r18 = 0;
        r14 = 0;
        r6 = 0;
        r21 = "SThemeManager";
        r22 = "Theme app list path: /system/csc/theme_app_list.xml";
        android.util.Log.d(r21, r22);
        r5 = new java.io.File;	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r21 = "/system/csc/theme_app_list.xml";
        r0 = r21;
        r5.<init>(r0);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r21 = r5.exists();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        if (r21 == 0) goto L_0x0052;
    L_0x001d:
        r22 = r5.length();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r24 = 0;
        r21 = (r22 > r24 ? 1 : (r22 == r24 ? 0 : -1));
        if (r21 <= 0) goto L_0x0052;
    L_0x0027:
        r7 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x0094 }
        r21 = new java.io.InputStreamReader;	 Catch:{ FileNotFoundException -> 0x0094 }
        r22 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0094 }
        r23 = "/system/csc/theme_app_list.xml";
        r22.<init>(r23);	 Catch:{ FileNotFoundException -> 0x0094 }
        r21.<init>(r22);	 Catch:{ FileNotFoundException -> 0x0094 }
        r0 = r21;
        r7.<init>(r0);	 Catch:{ FileNotFoundException -> 0x0094 }
        r6 = r7;
    L_0x003c:
        if (r6 == 0) goto L_0x0052;
    L_0x003e:
        r14 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ XmlPullParserException -> 0x00c5, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r21 = 1;
        r0 = r21;
        r14.setNamespaceAware(r0);	 Catch:{ XmlPullParserException -> 0x00c5, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r18 = r14.newPullParser();	 Catch:{ XmlPullParserException -> 0x00c5, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r0 = r18;
        r0.setInput(r6);	 Catch:{ XmlPullParserException -> 0x00c5, IOException -> 0x0160, NotFoundException -> 0x0133 }
    L_0x0052:
        if (r18 == 0) goto L_0x008e;
    L_0x0054:
        r21 = "ThemeAppList";
        r0 = r18;
        r1 = r21;
        com.android.internal.util.XmlUtils.beginDocument(r0, r1);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r8 = r18.getDepth();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
    L_0x0062:
        r20 = r18.next();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r21 = 3;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x0076;
    L_0x006e:
        r21 = r18.getDepth();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r0 = r21;
        if (r0 <= r8) goto L_0x0086;
    L_0x0076:
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x0086;
    L_0x007e:
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x00cc;
    L_0x0086:
        r21 = 1;
        r0 = r21;
        r1 = r26;
        r1.mPackageIconLoaded = r0;	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
    L_0x008e:
        if (r6 == 0) goto L_0x0093;
    L_0x0090:
        r6.close();	 Catch:{ Exception -> 0x012d }
    L_0x0093:
        return;
    L_0x0094:
        r10 = move-exception;
        r6 = 0;
        r10.printStackTrace();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        goto L_0x003c;
    L_0x009a:
        r13 = move-exception;
        r21 = "SThemeManager";
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x018d }
        r22.<init>();	 Catch:{ all -> 0x018d }
        r23 = "Exception during parsing theme app list";
        r22 = r22.append(r23);	 Catch:{ all -> 0x018d }
        r0 = r22;
        r22 = r0.append(r13);	 Catch:{ all -> 0x018d }
        r22 = r22.toString();	 Catch:{ all -> 0x018d }
        android.util.Log.e(r21, r22);	 Catch:{ all -> 0x018d }
        r13.printStackTrace();	 Catch:{ all -> 0x018d }
        if (r6 == 0) goto L_0x0093;
    L_0x00bc:
        r6.close();	 Catch:{ Exception -> 0x00c0 }
        goto L_0x0093;
    L_0x00c0:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0093;
    L_0x00c5:
        r13 = move-exception;
        r13.printStackTrace();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r18 = 0;
        goto L_0x0052;
    L_0x00cc:
        r21 = 2;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x0062;
    L_0x00d4:
        r4 = 0;
        r16 = 0;
        r17 = r18.getName();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        if (r17 == 0) goto L_0x0062;
    L_0x00dd:
        r19 = r18.getAttributeCount();	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r15 = 0;
    L_0x00e2:
        r0 = r19;
        if (r15 >= r0) goto L_0x0112;
    L_0x00e6:
        r0 = r18;
        r2 = r0.getAttributeName(r15);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r0 = r18;
        r3 = r0.getAttributeValue(r15);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        if (r2 == 0) goto L_0x0100;
    L_0x00f4:
        r21 = "className";
        r0 = r21;
        r21 = r2.equals(r0);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        if (r21 == 0) goto L_0x0100;
    L_0x00ff:
        r4 = r3;
    L_0x0100:
        if (r2 == 0) goto L_0x010f;
    L_0x0102:
        r21 = "iconId";
        r0 = r21;
        r21 = r2.equals(r0);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        if (r21 == 0) goto L_0x010f;
    L_0x010d:
        r16 = r3;
    L_0x010f:
        r15 = r15 + 1;
        goto L_0x00e2;
    L_0x0112:
        r21 = "ThemeApp";
        r0 = r17;
        r1 = r21;
        r21 = r0.equals(r1);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        if (r21 == 0) goto L_0x0062;
    L_0x011f:
        r21 = sPackageIconMap;	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r0 = r21;
        r1 = r16;
        r0.put(r4, r1);	 Catch:{ XmlPullParserException -> 0x009a, IOException -> 0x0160, NotFoundException -> 0x0133 }
        r4 = 0;
        r16 = 0;
        goto L_0x0062;
    L_0x012d:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0093;
    L_0x0133:
        r9 = move-exception;
        r21 = "SThemeManager";
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x018d }
        r22.<init>();	 Catch:{ all -> 0x018d }
        r23 = "Exception during parsing theme app list";
        r22 = r22.append(r23);	 Catch:{ all -> 0x018d }
        r0 = r22;
        r22 = r0.append(r9);	 Catch:{ all -> 0x018d }
        r22 = r22.toString();	 Catch:{ all -> 0x018d }
        android.util.Log.e(r21, r22);	 Catch:{ all -> 0x018d }
        r9.printStackTrace();	 Catch:{ all -> 0x018d }
        if (r6 == 0) goto L_0x0093;
    L_0x0155:
        r6.close();	 Catch:{ Exception -> 0x015a }
        goto L_0x0093;
    L_0x015a:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0093;
    L_0x0160:
        r11 = move-exception;
        r21 = "SThemeManager";
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x018d }
        r22.<init>();	 Catch:{ all -> 0x018d }
        r23 = "Exception during parsing theme app list";
        r22 = r22.append(r23);	 Catch:{ all -> 0x018d }
        r0 = r22;
        r22 = r0.append(r11);	 Catch:{ all -> 0x018d }
        r22 = r22.toString();	 Catch:{ all -> 0x018d }
        android.util.Log.e(r21, r22);	 Catch:{ all -> 0x018d }
        r11.printStackTrace();	 Catch:{ all -> 0x018d }
        if (r6 == 0) goto L_0x0093;
    L_0x0182:
        r6.close();	 Catch:{ Exception -> 0x0187 }
        goto L_0x0093;
    L_0x0187:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0093;
    L_0x018d:
        r21 = move-exception;
        if (r6 == 0) goto L_0x0193;
    L_0x0190:
        r6.close();	 Catch:{ Exception -> 0x0194 }
    L_0x0193:
        throw r21;
    L_0x0194:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0193;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.theme.SThemeManager.loadThemeAppList():void");
    }

    public Context getCurrentContext() {
        Context context = null;
        if (this.mPackageName.isEmpty()) {
            return this.mContext;
        }
        try {
            return this.mContext.createPackageContext(this.mPackageName, 0);
        } catch (Throwable e) {
            e.printStackTrace();
            return context;
        }
    }

    public Bitmap getItemBitmap(String str) {
        Resources resources = getResources();
        if (resources == null) {
            return null;
        }
        try {
            int identifier = resources.getIdentifier(str, "drawable", this.mPackageName);
            return identifier == 0 ? null : BitmapFactory.decodeResource(resources, identifier);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getItemColor(String str) {
        Resources resources = getResources();
        if (resources == null) {
            return 0;
        }
        try {
            int identifier = resources.getIdentifier(str, "color", this.mPackageName);
            return identifier == 0 ? 0 : resources.getColor(identifier);
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Drawable getItemDrawable(String str) {
        Drawable drawable = null;
        Resources resources = getResources();
        if (resources != null) {
            try {
                int identifier = resources.getIdentifier(str, "drawable", this.mPackageName);
                if (identifier == 0) {
                    return null;
                }
                drawable = resources.getDrawable(identifier);
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
        return drawable;
    }

    public CharSequence getItemText(String str) {
        CharSequence charSequence = null;
        Resources resources = getResources();
        if (resources != null) {
            try {
                int identifier = resources.getIdentifier(str, "string", this.mPackageName);
                if (identifier == 0) {
                    return null;
                }
                charSequence = resources.getText(identifier);
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
        return charSequence;
    }

    public XmlResourceParser getItemXml(String str) {
        Resources resources = getResources();
        if (resources == null) {
            return null;
        }
        try {
            int identifier = resources.getIdentifier(str, "xml", this.mPackageName);
            return identifier == 0 ? null : resources.getXml(identifier);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public Drawable getPackageIcon(String str) {
        if (!this.mPackageIconLoaded) {
            loadThemeAppList();
        }
        String str2 = (String) sPackageIconMap.get(str);
        return str2 != null ? getItemDrawable(str2) : null;
    }

    public Bitmap getPackageIconBitmap(String str) {
        if (!this.mPackageIconLoaded) {
            loadThemeAppList();
        }
        String str2 = (String) sPackageIconMap.get(str);
        return str2 != null ? getItemBitmap(str2) : null;
    }

    public Bitmap getPackageIconBitmapStartsWith(String str) {
        if (!this.mPackageIconLoaded) {
            loadThemeAppList();
        }
        String str2 = null;
        for (Entry entry : sPackageIconMap.entrySet()) {
            if (((String) entry.getKey()).startsWith(str)) {
                str2 = (String) entry.getValue();
                break;
            }
        }
        return str2 != null ? getItemBitmap(str2) : null;
    }

    public String getVersionFromFeature(int i) {
        String str = SmartFaceManager.PAGE_MIDDLE;
        String str2 = "";
        String string = SemFloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_COMMON_CONFIG_CHANGEABLE_UI", SmartFaceManager.PAGE_MIDDLE);
        if (i == 0) {
            str2 = THEME_STR;
        } else if (i == 1) {
            str2 = FESTIVAL_EFFECT_STR;
        }
        if (string == null || string.isEmpty() || str2.isEmpty() || !string.contains(str2)) {
            return str;
        }
        int indexOf = string.indexOf(str2);
        return (indexOf <= -1 || (str2.length() + indexOf) + 1 >= string.length()) ? str : String.valueOf(string.charAt((str2.length() + indexOf) + 1));
    }

    public void resetTheme() {
        getCurrentResourcePackage();
        if (this.mPackageName == null || this.mPackageName.isEmpty()) {
            this.mPackageName = this.mContext.getPackageName();
        }
    }
}
