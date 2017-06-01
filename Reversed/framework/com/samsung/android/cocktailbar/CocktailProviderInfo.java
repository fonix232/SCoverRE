package com.samsung.android.cocktailbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;
import java.util.ArrayList;

public class CocktailProviderInfo implements Parcelable {
    private static final String COCKTAIL_AUTO_SCALE = "autoScale";
    private static final String COCKTAIL_CATEGORY = "category";
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL = 65536;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_EXPRESS_ME = 64;
    public static final int COCKTAIL_CATEGORY_FEEDS = 256;
    public static final int COCKTAIL_CATEGORY_HOME_SCREEN = 8;
    public static final int COCKTAIL_CATEGORY_INVALID = -1;
    public static final int COCKTAIL_CATEGORY_LOCK_SCREEN = 16;
    public static final int COCKTAIL_CATEGORY_NIGHT_MODE = 128;
    public static final int COCKTAIL_CATEGORY_NORMAL = 1;
    public static final int COCKTAIL_CATEGORY_QUICK_TOOL = 4;
    public static final int COCKTAIL_CATEGORY_TABLE_MODE = 32;
    public static final int COCKTAIL_CATEGORY_WHISPER = 512;
    private static final String COCKTAIL_COCKTAIL_WIDTH = "cocktailWidth";
    private static final String COCKTAIL_CONFIGURE = "configure";
    private static final String COCKTAIL_CSC_PREVIEW_IMAGE = "cscPreviewImage";
    private static final String COCKTAIL_DATETIME_ENABLED = "dateTimeEnabled";
    private static final String COCKTAIL_DESCRIPTION = "description";
    private static final String COCKTAIL_ICON = "icon";
    private static final String COCKTAIL_LABEL = "label";
    private static final String COCKTAIL_LAUNCH_ON_CLICK = "launchOnClick";
    private static final String COCKTAIL_LOGO_ID = "logoResourceId";
    private static final String COCKTAIL_PERMIT_VISIBILITY_CHANGED = "permitVisibilityChanged";
    private static final String COCKTAIL_PREVIEW_IMAGE = "previewImage";
    private static final String COCKTAIL_PRIVATE_MODE = "privateMode";
    private static final String COCKTAIL_PULL_TO_REFRESH = "pullToRefresh";
    private static final String COCKTAIL_UPDATE_TIME = "updatePeriodMillis";
    private static final String COCKTAIL_WHISPER = "whisper";
    public static final Creator<CocktailProviderInfo> CREATOR = new C10141();
    private static final String TAG = "CocktailProviderInfo";
    private static final int VAL_DEFAULT_COCKTAIL_WIDTH = 160;
    private static final String XMLVAL_CONTEXTUAL = "contextual";
    private static final String XMLVAL_FEEDS = "feeds";
    private static final String XMLVAL_HOME_SCREEN = "homescreen";
    private static final String XMLVAL_LOCK_SCREEN = "lockscreen";
    private static final String XMLVAL_NIGHT_MODE = "nightmode";
    private static final String XMLVAL_NORMAL = "normal";
    private static final String XMLVAL_QUICK_TOOL = "quicktool";
    private static final String XMLVAL_TABLE_MODE = "tablemode";
    private static final String XMLVAL_WHISPER = "whisper";
    public boolean autoScale;
    public int category;
    public int cocktailWidth;
    public ComponentName configure;
    public boolean cscPreviewImage;
    public int description;
    public int icon;
    public boolean isDateTimeEnabled;
    public int label;
    public String launchOnClick;
    public int logoResourceId;
    public boolean permitVisibilityChanged;
    public int previewImage;
    public String privateMode;
    public ComponentName provider;
    public boolean pullToRefresh;
    public int updatePeriodMillis;
    public String whisper;

    static class C10141 implements Creator<CocktailProviderInfo> {
        C10141() {
        }

        public CocktailProviderInfo createFromParcel(Parcel parcel) {
            return new CocktailProviderInfo(parcel);
        }

        public CocktailProviderInfo[] newArray(int i) {
            return new CocktailProviderInfo[i];
        }
    }

    public CocktailProviderInfo() {
        this.permitVisibilityChanged = false;
    }

    private CocktailProviderInfo(Context context, PackageManager packageManager, Resources resources, ComponentName componentName, XmlResourceParser xmlResourceParser, ResolveInfo resolveInfo, int i) throws NotFoundException, IllegalArgumentException {
        this.permitVisibilityChanged = false;
        this.provider = componentName;
        this.icon = xmlResourceParser.getAttributeResourceValue(null, "icon", 0);
        this.label = xmlResourceParser.getAttributeResourceValue(null, "label", 0);
        this.description = xmlResourceParser.getAttributeResourceValue(null, "description", 0);
        Object loadXmlString = loadXmlString(xmlResourceParser, resources, "category", "normal");
        if (TextUtils.isEmpty(loadXmlString)) {
            this.category = 1;
        } else {
            SimpleStringSplitter simpleStringSplitter = new SimpleStringSplitter('|');
            simpleStringSplitter.setString(loadXmlString);
            while (simpleStringSplitter.hasNext()) {
                String trim = simpleStringSplitter.next().trim();
                int categoryId = getCategoryId(trim);
                Object obj = null;
                switch (categoryId) {
                    case -1:
                        Log.m31e(TAG, "Provider: " + componentName + " specified an invalid catetory of " + trim);
                        this.category = -1;
                        return;
                    case 4:
                    case 32:
                    case 128:
                        this.category = categoryId;
                        obj = 1;
                        continue;
                    case 8:
                    case 16:
                    case 256:
                        if (CocktailBarFeatures.isSupportCocktailBar(context)) {
                            this.category |= categoryId | 1;
                            continue;
                        } else {
                            this.category = categoryId;
                            continue;
                        }
                    default:
                        this.category |= categoryId;
                        continue;
                }
                if (obj != null) {
                }
            }
        }
        if (i > 1) {
            this.cocktailWidth = loadXmlDimension(xmlResourceParser, resources, COCKTAIL_COCKTAIL_WIDTH, 160);
            this.launchOnClick = loadXmlString(xmlResourceParser, resources, COCKTAIL_LAUNCH_ON_CLICK, null);
            this.autoScale = loadXmlBoolean(xmlResourceParser, resources, COCKTAIL_AUTO_SCALE, true);
            this.logoResourceId = xmlResourceParser.getAttributeResourceValue(null, COCKTAIL_LOGO_ID, 0);
            this.isDateTimeEnabled = loadXmlBoolean(xmlResourceParser, resources, COCKTAIL_DATETIME_ENABLED, false);
        } else {
            this.cocktailWidth = 160;
        }
        this.privateMode = loadXmlString(xmlResourceParser, resources, COCKTAIL_PRIVATE_MODE, null);
        this.previewImage = xmlResourceParser.getAttributeResourceValue(null, COCKTAIL_PREVIEW_IMAGE, 0);
        this.updatePeriodMillis = loadXmlInt(xmlResourceParser, resources, COCKTAIL_UPDATE_TIME, 0);
        this.permitVisibilityChanged = loadXmlBoolean(xmlResourceParser, resources, COCKTAIL_PERMIT_VISIBILITY_CHANGED, false);
        this.pullToRefresh = loadXmlBoolean(xmlResourceParser, resources, COCKTAIL_PULL_TO_REFRESH, false);
        String loadXmlString2 = loadXmlString(xmlResourceParser, resources, COCKTAIL_CONFIGURE, null);
        if (loadXmlString2 != null) {
            this.configure = new ComponentName(componentName.getPackageName(), loadXmlString2);
        }
        this.cscPreviewImage = loadXmlBoolean(xmlResourceParser, resources, COCKTAIL_CSC_PREVIEW_IMAGE, false);
        if (this.category == 512) {
            this.whisper = loadXmlString(xmlResourceParser, resources, "whisper", null);
        }
    }

    private CocktailProviderInfo(Parcel parcel) {
        String str = null;
        boolean z = true;
        this.permitVisibilityChanged = false;
        this.provider = parcel.readInt() != 0 ? new ComponentName(parcel) : null;
        this.updatePeriodMillis = parcel.readInt();
        this.label = parcel.readInt();
        this.description = parcel.readInt();
        this.icon = parcel.readInt();
        this.previewImage = parcel.readInt();
        this.category = parcel.readInt();
        this.cocktailWidth = parcel.readInt();
        this.privateMode = parcel.readInt() != 0 ? parcel.readString() : null;
        this.permitVisibilityChanged = parcel.readByte() == (byte) 1;
        this.pullToRefresh = parcel.readByte() == (byte) 1;
        this.configure = parcel.readInt() != 0 ? new ComponentName(parcel) : null;
        if (parcel.readInt() != 0) {
            str = parcel.readString();
        }
        this.launchOnClick = str;
        this.cscPreviewImage = parcel.readByte() == (byte) 1;
        this.autoScale = parcel.readByte() == (byte) 1;
        this.logoResourceId = parcel.readInt();
        if (parcel.readByte() != (byte) 1) {
            z = false;
        }
        this.isDateTimeEnabled = z;
    }

    public static com.samsung.android.cocktailbar.CocktailProviderInfo create(android.content.Context r16, android.content.pm.ResolveInfo r17, android.content.ComponentName r18, android.content.res.XmlResourceParser r19, int r20, int r21) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:26:? in {10, 15, 16, 18, 19, 21, 23, 25, 27, 28} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r4 = r16.getPackageManager();
        r14 = android.os.Binder.clearCallingIdentity();
        r3 = r18.getPackageName();	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r0 = r17;	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r6 = r0.activityInfo;	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r6 = r6.applicationInfo;	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r6 = r6.uid;	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r6 = android.os.UserHandle.getUserId(r6);	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r5 = r4.getResourcesForApplicationAsUser(r3, r6);	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        android.os.Binder.restoreCallingIdentity(r14);
        r2 = new com.samsung.android.cocktailbar.CocktailProviderInfo;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r3 = r16;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r6 = r18;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r7 = r19;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r8 = r17;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r9 = r21;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r2.<init>(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r0 = r20;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r3 = enforceValidCategory(r0, r2);	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        if (r3 == 0) goto L_0x003b;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
    L_0x0036:
        r3 = r2.category;	 Catch:{ NotFoundException -> 0x005e, IllegalArgumentException -> 0x0052 }
        r6 = -1;
        if (r3 != r6) goto L_0x0051;
    L_0x003b:
        r3 = 0;
        return r3;
    L_0x003d:
        r10 = move-exception;
        r3 = "CocktailProviderInfo";	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r6 = "failed to load find package";	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        android.util.Log.m32e(r3, r6, r10);	 Catch:{ NameNotFoundException -> 0x003d, all -> 0x004c }
        r3 = 0;
        android.os.Binder.restoreCallingIdentity(r14);
        return r3;
    L_0x004c:
        r3 = move-exception;
        android.os.Binder.restoreCallingIdentity(r14);
        throw r3;
    L_0x0051:
        return r2;
    L_0x0052:
        r12 = move-exception;
        r3 = "CocktailProviderInfo";
        r6 = "IllegalArgumentException";
        android.util.Log.m31e(r3, r6);
    L_0x005c:
        r3 = 0;
        return r3;
    L_0x005e:
        r11 = move-exception;
        r3 = "CocktailProviderInfo";
        r6 = "XML resources failed";
        android.util.Log.m31e(r3, r6);
        goto L_0x005c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.cocktailbar.CocktailProviderInfo.create(android.content.Context, android.content.pm.ResolveInfo, android.content.ComponentName, android.content.res.XmlResourceParser, int, int):com.samsung.android.cocktailbar.CocktailProviderInfo");
    }

    private static boolean enforceValidCategory(int i, CocktailProviderInfo cocktailProviderInfo) {
        if (i != 0) {
            return cocktailProviderInfo.privateMode == null && (cocktailProviderInfo.category & i) != 0;
        } else {
            Log.m33i(TAG, "enforceValidCategory: there is no category filters");
            return true;
        }
    }

    private static int getCategoryId(String str) {
        return "normal".equals(str) ? 1 : XMLVAL_CONTEXTUAL.equals(str) ? 65536 : XMLVAL_HOME_SCREEN.equals(str) ? 8 : XMLVAL_FEEDS.equals(str) ? 256 : "whisper".equals(str) ? 512 : XMLVAL_QUICK_TOOL.equals(str) ? 4 : XMLVAL_TABLE_MODE.equals(str) ? 32 : XMLVAL_NIGHT_MODE.equals(str) ? 128 : XMLVAL_LOCK_SCREEN.equals(str) ? 16 : -1;
    }

    public static int getCategoryIds(ArrayList<String> arrayList) {
        int i = 0;
        if (arrayList == null || arrayList.size() == 0) {
            return 0;
        }
        for (String categoryId : arrayList) {
            i |= getCategoryId(categoryId);
        }
        return i;
    }

    private boolean loadXmlBoolean(XmlResourceParser xmlResourceParser, Resources resources, String str, boolean z) {
        int attributeResourceValue = xmlResourceParser.getAttributeResourceValue(null, str, 0);
        if (attributeResourceValue == 0) {
            return xmlResourceParser.getAttributeBooleanValue(null, str, z);
        }
        try {
            return resources.getBoolean(attributeResourceValue);
        } catch (NotFoundException e) {
            return z;
        }
    }

    private int loadXmlDimension(XmlResourceParser xmlResourceParser, Resources resources, String str, int i) {
        int attributeResourceValue = xmlResourceParser.getAttributeResourceValue(null, str, 0);
        if (attributeResourceValue == 0) {
            return xmlResourceParser.getAttributeIntValue(null, str, i);
        }
        try {
            return resources.getDimensionPixelSize(attributeResourceValue);
        } catch (NotFoundException e) {
            return i;
        }
    }

    private int loadXmlInt(XmlResourceParser xmlResourceParser, Resources resources, String str, int i) {
        int attributeResourceValue = xmlResourceParser.getAttributeResourceValue(null, str, 0);
        if (attributeResourceValue == 0) {
            return xmlResourceParser.getAttributeIntValue(null, str, i);
        }
        try {
            return resources.getInteger(attributeResourceValue);
        } catch (NotFoundException e) {
            return i;
        }
    }

    private String loadXmlString(XmlResourceParser xmlResourceParser, Resources resources, String str, String str2) {
        int attributeResourceValue = xmlResourceParser.getAttributeResourceValue(null, str, 0);
        if (attributeResourceValue != 0) {
            try {
                return resources.getString(attributeResourceValue);
            } catch (NotFoundException e) {
                return str2;
            }
        }
        String attributeValue = xmlResourceParser.getAttributeValue(null, str);
        return attributeValue == null ? str2 : attributeValue;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.provider != null) {
            parcel.writeInt(1);
            this.provider.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.updatePeriodMillis);
        parcel.writeInt(this.label);
        parcel.writeInt(this.description);
        parcel.writeInt(this.icon);
        parcel.writeInt(this.previewImage);
        parcel.writeInt(this.category);
        parcel.writeInt(this.cocktailWidth);
        if (this.privateMode != null) {
            parcel.writeInt(1);
            parcel.writeString(this.privateMode);
        } else {
            parcel.writeInt(0);
        }
        if (this.permitVisibilityChanged) {
            parcel.writeByte((byte) 1);
        } else {
            parcel.writeByte((byte) 0);
        }
        if (this.pullToRefresh) {
            parcel.writeByte((byte) 1);
        } else {
            parcel.writeByte((byte) 0);
        }
        if (this.configure != null) {
            parcel.writeInt(1);
            this.configure.writeToParcel(parcel, i);
        } else {
            parcel.writeInt(0);
        }
        if (this.launchOnClick != null) {
            parcel.writeInt(1);
            parcel.writeString(this.launchOnClick);
        } else {
            parcel.writeInt(0);
        }
        if (this.cscPreviewImage) {
            parcel.writeByte((byte) 1);
        } else {
            parcel.writeByte((byte) 0);
        }
        if (this.autoScale) {
            parcel.writeByte((byte) 1);
        } else {
            parcel.writeByte((byte) 0);
        }
        parcel.writeInt(this.logoResourceId);
        if (this.isDateTimeEnabled) {
            parcel.writeByte((byte) 1);
        } else {
            parcel.writeByte((byte) 0);
        }
    }
}
