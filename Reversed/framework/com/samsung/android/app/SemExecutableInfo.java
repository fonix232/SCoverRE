package com.samsung.android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Xml;
import com.android.internal.C0717R;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.feature.SemCscFeature;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.util.SemLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public final class SemExecutableInfo implements Parcelable {
    private static final String CLASSNAME_PREFIX_FOR_SEC_PRODUCT_FEATURE = "SecProductFeature_";
    public static final Creator<SemExecutableInfo> CREATOR = new C09941();
    private static final String CSC_FEATURE_PREFIX = "CscFeature_";
    private static final boolean DEBUG = Debug.semIsProductDev();
    public static final int LAUNCH_TYPE_ACTIVITY = 0;
    public static final int LAUNCH_TYPE_ACTIVITY_FOR_RESULT = 3;
    public static final int LAUNCH_TYPE_BROADCAST = 2;
    public static final int LAUNCH_TYPE_SERVICE = 1;
    private static final String LOG_TAG = "SemExecutableInfo";
    private static final String MD_LABEL_EXECUTABLE = "com.samsung.android.support.executable";
    private static final int ORDER_INIT_VALUE = -9996;
    private static final int ORDER_INVALID_FORMAT = -9998;
    private static final int ORDER_NOT_ALLOWED = -9997;
    private static final int ORDER_OUT_OF_RANGE = -9999;
    private static final String PACKAGE_PREFIX_FOR_SEC_PRODUCT_FEATURE = "com.sec.android.app.";
    private static final String SEC_FLOATING_FEATURE_PREFIX = "SEC_FLOATING_FEATURE_";
    private static final String SEC_PRODUCT_FEATURE_PREFIX = "SEC_PRODUCT_FEATURE_";
    private static final String XML_ELEMENT_COMMAND = "command";
    private static final String XML_ELEMENT_ENABLED = "enabled";
    private static final String XML_ELEMENT_EXECUTABLE = "executable";
    private static final String XML_ELEMENT_EXTRA_ATTR = "extras-attr";
    private static final String XML_ELEMENT_EXTRA_ATTR_CATEGORY = "category";
    private static final String XML_ELEMENT_EXTRA_ATTR_COMPONENTNAME = "componentName";
    private static final String XML_ELEMENT_EXTRA_ATTR_EXTRAS = "extras";
    private static final String XML_ELEMENT_EXTRA_ATTR_FEATURE = "feature";
    private static final String XML_ELEMENT_EXTRA_ATTR_INTETNACTION = "action";
    private static final String XML_ELEMENT_EXTRA_ATTR_LAUCHMODE = "launchMode";
    private static final String XML_ELEMENT_EXTRA_ATTR_PACKAGENAME = "packageName";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE = "type";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY = "activity";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY_FOR_RESULT = "activityForResult";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_BROADCAST = "broadcast";
    private static final String XML_ELEMENT_EXTRA_ATTR_TYPE_SERVICE = "service";
    private static final String XML_ELEMENT_ICON = "icon";
    private static final String XML_ELEMENT_LABEL = "label";
    private static final String XML_ELEMENT_LAUCHMODE_CLEARTOP = "clearTop";
    private static final String XML_ELEMENT_LAUCHMODE_NEWTASK = "newTask";
    private static final String XML_ELEMENT_LAUCHMODE_SINGLETOP = "singleTop";
    private static final String XML_ELEMENT_SMALL_ICON = "smallIcon";
    String mAction;
    String mActivityLaunchMode;
    Bundle mBundle;
    String mCategory;
    String mComponentName;
    boolean mEnabled;
    List<String> mFeatureNames;
    List<String> mFeatureValues;
    int mIconId;
    int mLabelId;
    int mLaunchType;
    String mPackageName;
    int mSmallIconId;
    String mUid;

    static class C09941 implements Creator<SemExecutableInfo> {
        C09941() {
        }

        public SemExecutableInfo createFromParcel(Parcel parcel) {
            return new SemExecutableInfo(parcel);
        }

        public SemExecutableInfo[] newArray(int i) {
            return new SemExecutableInfo[i];
        }
    }

    public SemExecutableInfo() {
        this.mUid = null;
        this.mEnabled = false;
        this.mBundle = new Bundle();
        this.mFeatureNames = new ArrayList();
        this.mFeatureValues = new ArrayList();
    }

    SemExecutableInfo(Parcel parcel) {
        boolean z = false;
        this();
        this.mUid = parcel.readString();
        if (parcel.readInt() != 0) {
            z = true;
        }
        this.mEnabled = z;
        this.mLabelId = parcel.readInt();
        this.mIconId = parcel.readInt();
        this.mSmallIconId = parcel.readInt();
        this.mLaunchType = parcel.readInt();
        this.mCategory = parcel.readString();
        this.mAction = parcel.readString();
        this.mPackageName = parcel.readString();
        parcel.readStringList(this.mFeatureNames);
        parcel.readStringList(this.mFeatureValues);
        this.mBundle = parcel.readBundle();
        this.mComponentName = parcel.readString();
        this.mActivityLaunchMode = parcel.readString();
    }

    private void addExtraAttribute(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0717R.styleable.extrasCommand);
        String string = obtainStyledAttributes.getString(0);
        Object string2 = obtainStyledAttributes.getString(2);
        Object string3 = obtainStyledAttributes.getString(1);
        if (XML_ELEMENT_EXTRA_ATTR_LAUCHMODE.equals(string)) {
            this.mActivityLaunchMode = string3;
        } else if ("type".equals(string)) {
            if (XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY.equals(string3)) {
                this.mLaunchType = 0;
            } else if ("service".equals(string3)) {
                this.mLaunchType = 1;
            } else if (XML_ELEMENT_EXTRA_ATTR_TYPE_BROADCAST.equals(string3)) {
                this.mLaunchType = 2;
            } else if (XML_ELEMENT_EXTRA_ATTR_TYPE_ACTIVITY_FOR_RESULT.equals(string3)) {
                this.mLaunchType = 3;
            } else {
                this.mLaunchType = 0;
            }
        } else if ("category".equals(string)) {
            this.mCategory = string3;
        } else if ("action".equals(string)) {
            this.mAction = string3;
        } else if ("packageName".equals(string)) {
            this.mPackageName = string3;
        } else if (XML_ELEMENT_EXTRA_ATTR_COMPONENTNAME.equals(string)) {
            this.mComponentName = string3;
        } else if (XML_ELEMENT_EXTRA_ATTR_FEATURE.equals(string)) {
            this.mFeatureNames.add(string2);
            this.mFeatureValues.add(string3);
        } else if (!(!XML_ELEMENT_EXTRA_ATTR_EXTRAS.equals(string) || TextUtils.isEmpty(string2) || TextUtils.isEmpty(string3))) {
            this.mBundle.putString(string2, string3);
        }
        obtainStyledAttributes.recycle();
    }

    private static boolean checkValidate(SemExecutableInfo semExecutableInfo) {
        if (semExecutableInfo == null) {
            if (DEBUG) {
                SemLog.d(LOG_TAG, "Invalid SemExecutableInfo");
            }
            return false;
        } else if (!semExecutableInfo.mEnabled) {
            if (DEBUG) {
                SemLog.d(LOG_TAG, "disabled SemExecutableInfo " + semExecutableInfo.toString());
            }
            return false;
        } else if (semExecutableInfo.getLaunchType() != 2 && (semExecutableInfo.getPackageName() == null || semExecutableInfo.getComponentName() == null)) {
            if (DEBUG) {
                SemLog.d(LOG_TAG, "Invalid packageName or componentName = " + semExecutableInfo.toString());
            }
            return false;
        } else if (semExecutableInfo.getLabelId() == 0 || semExecutableInfo.getIconId() == 0) {
            if (DEBUG) {
                SemLog.d(LOG_TAG, "Invalid label or icon = " + semExecutableInfo.toString());
            }
            return false;
        } else {
            for (int i = 0; i < semExecutableInfo.mFeatureNames.size(); i++) {
                String str = (String) semExecutableInfo.mFeatureNames.get(i);
                String str2 = (String) semExecutableInfo.mFeatureValues.get(i);
                if (str == null || str.length() <= 0 || str2 == null || str2.length() <= 0) {
                    if (str != null && str.length() > 0 && (str2 == null || (str2 != null && str2.length() <= 0))) {
                        if (DEBUG) {
                            SemLog.d(LOG_TAG, "No value for " + str + " " + semExecutableInfo.toString());
                        }
                        return false;
                    } else if (str2 != null && str2.length() > 0 && (str == null || (str != null && str.length() <= 0))) {
                        if (DEBUG) {
                            SemLog.d(LOG_TAG, "No feature name is provided for the value " + str2 + " " + semExecutableInfo.toString());
                        }
                        return false;
                    }
                } else if (str.startsWith(CSC_FEATURE_PREFIX)) {
                    r3 = SemCscFeature.getInstance().getString(str);
                    if (str2.startsWith("!")) {
                        if (r3.equalsIgnoreCase(str2.substring(1))) {
                            return false;
                        }
                    } else if (!r3.equalsIgnoreCase(str2)) {
                        if (DEBUG) {
                            SemLog.d(LOG_TAG, str + " is not [" + str2 + "] " + semExecutableInfo.toString());
                        }
                        return false;
                    }
                } else if (str.startsWith(SEC_FLOATING_FEATURE_PREFIX)) {
                    r3 = SemFloatingFeature.getInstance().getString(str);
                    if (str2.startsWith("!")) {
                        if (r3.equalsIgnoreCase(str2.substring(1))) {
                            return false;
                        }
                    } else if (!r3.equalsIgnoreCase(str2)) {
                        if (DEBUG) {
                            SemLog.d(LOG_TAG, str + " is not [" + str2 + "] " + semExecutableInfo.toString());
                        }
                        return false;
                    }
                } else if (str.startsWith(SEC_PRODUCT_FEATURE_PREFIX)) {
                    return false;
                } else {
                    r3 = SystemProperties.get(str);
                    if (str2.startsWith("!")) {
                        if (r3.equalsIgnoreCase(str2.substring(1))) {
                            return false;
                        }
                    } else if (!r3.equalsIgnoreCase(str2)) {
                        if (DEBUG) {
                            SemLog.d(LOG_TAG, str + " is not [" + str2 + "] " + semExecutableInfo.toString());
                        }
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static Context createActivityContext(Context context, ComponentName componentName) {
        Context context2 = null;
        try {
            context2 = context.createPackageContext(componentName.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            SemLog.e(LOG_TAG, "Package not found " + componentName.getPackageName());
        } catch (Throwable e2) {
            SemLog.e(LOG_TAG, "Can't make context for " + componentName.getPackageName(), e2);
        }
        return context2;
    }

    private static void examineOrderInCategory(SemExecutableInfo semExecutableInfo, boolean z) {
        String str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        int i = ORDER_INIT_VALUE;
        if (!z) {
            i = ORDER_NOT_ALLOWED;
        }
        if (!semExecutableInfo.getCategories().isEmpty()) {
            for (String str2 : semExecutableInfo.mCategory.split("\\|")) {
                String[] split = str2.split("@");
                switch (split.length) {
                    case 1:
                        str = str + str2 + "|";
                        break;
                    case 2:
                        if (i != ORDER_NOT_ALLOWED) {
                            try {
                                i = Integer.parseInt(split[0]);
                                if (i < -1000 || i > 1000) {
                                    i = ORDER_OUT_OF_RANGE;
                                }
                            } catch (Throwable e) {
                                i = ORDER_INVALID_FORMAT;
                                if (DEBUG) {
                                    SemLog.d(LOG_TAG, "Invalid order");
                                    e.printStackTrace();
                                }
                                str = str + split[1] + "|";
                                break;
                            } catch (Throwable th) {
                                if (ORDER_INVALID_FORMAT == ORDER_OUT_OF_RANGE || ORDER_INVALID_FORMAT == ORDER_NOT_ALLOWED || ORDER_INVALID_FORMAT == ORDER_INVALID_FORMAT) {
                                    str = str + split[1] + "|";
                                } else {
                                    str = str + str2 + "|";
                                }
                            }
                        }
                        if (i != ORDER_OUT_OF_RANGE && i != ORDER_NOT_ALLOWED && i != ORDER_INVALID_FORMAT) {
                            str = str + str2 + "|";
                            break;
                        } else {
                            str = str + split[1] + "|";
                            break;
                        }
                    default:
                        str = str + str2 + "|";
                        if (!DEBUG) {
                            break;
                        }
                        SemLog.d(LOG_TAG, "Invalid category format for category order");
                        break;
                }
            }
            semExecutableInfo.mCategory = str.substring(0, str.length() - 1);
        }
    }

    private static SemExecutableInfo getActivityMetaData(Context context, AttributeSet attributeSet, ComponentName componentName) {
        SemExecutableInfo semExecutableInfo = new SemExecutableInfo();
        Context createActivityContext = createActivityContext(context, componentName);
        if (createActivityContext == null) {
            return null;
        }
        TypedArray obtainStyledAttributes = createActivityContext.obtainStyledAttributes(attributeSet, C0717R.styleable.command);
        semExecutableInfo.mUid = obtainStyledAttributes.getString(3);
        semExecutableInfo.mEnabled = obtainStyledAttributes.getBoolean(2, true);
        semExecutableInfo.mLabelId = obtainStyledAttributes.getResourceId(0, 0);
        semExecutableInfo.mIconId = obtainStyledAttributes.getResourceId(1, 0);
        semExecutableInfo.mSmallIconId = obtainStyledAttributes.getResourceId(4, 0);
        obtainStyledAttributes.recycle();
        return semExecutableInfo;
    }

    private String getBundleString() {
        String str = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        if (this.mBundle.isEmpty()) {
            return str;
        }
        Iterable<String> arrayList = new ArrayList(this.mBundle.keySet());
        Collections.sort(arrayList);
        for (String str2 : arrayList) {
            str = str + "{" + str2 + "=" + this.mBundle.get(str2) + "}";
        }
        return str;
    }

    public static List<SemExecutableInfo> scanExecutableInfos(Context context) {
        if (DEBUG) {
            SemLog.d(LOG_TAG, "scan scanExecutableInfos start");
        }
        String str = MD_LABEL_EXECUTABLE;
        PackageManager packageManager = context.getPackageManager();
        List<SemExecutableInfo> arrayList = new ArrayList();
        List queryIntentActivities = packageManager.queryIntentActivities(new Intent(MD_LABEL_EXECUTABLE), DisplayMetrics.DENSITY_XXXHIGH);
        List queryIntentServices = packageManager.queryIntentServices(new Intent(MD_LABEL_EXECUTABLE), DisplayMetrics.DENSITY_XXXHIGH);
        List queryBroadcastReceivers = packageManager.queryBroadcastReceivers(new Intent(MD_LABEL_EXECUTABLE), DisplayMetrics.DENSITY_XXXHIGH);
        for (Iterable<ResolveInfo> iterable : new List[]{queryIntentActivities, queryIntentServices, queryBroadcastReceivers}) {
            if (DEBUG) {
                SemLog.d(LOG_TAG, "list size = " + iterable.size());
            }
            for (ResolveInfo resolveInfo : iterable) {
                PackageItemInfo packageItemInfo = null;
                PackageItemInfo packageItemInfo2 = null;
                boolean z = true;
                boolean z2 = true;
                if (resolveInfo.activityInfo != null) {
                    packageItemInfo = resolveInfo.activityInfo;
                    packageItemInfo2 = resolveInfo.activityInfo.applicationInfo;
                    z = !resolveInfo.activityInfo.applicationInfo.enabled;
                    z2 = !resolveInfo.activityInfo.enabled;
                } else if (resolveInfo.serviceInfo != null) {
                    packageItemInfo = resolveInfo.serviceInfo;
                    packageItemInfo2 = resolveInfo.serviceInfo.applicationInfo;
                    z = !resolveInfo.serviceInfo.applicationInfo.enabled;
                    z2 = !resolveInfo.serviceInfo.enabled;
                }
                if (!z && !z2) {
                    ComponentName componentName = new ComponentName(packageItemInfo.packageName, packageItemInfo.name);
                    try {
                        Object loadXmlMetaData = packageItemInfo2.loadXmlMetaData(context.getPackageManager(), MD_LABEL_EXECUTABLE);
                        if (loadXmlMetaData != null) {
                            SemExecutableInfo semExecutableInfo = null;
                            Object obj = null;
                            Object obj2 = null;
                            for (int next = loadXmlMetaData.next(); next != 1; next = loadXmlMetaData.next()) {
                                String name = loadXmlMetaData.getName();
                                if (next == 2) {
                                    if (XML_ELEMENT_EXECUTABLE.equals(name)) {
                                        obj = 1;
                                    }
                                    if (XML_ELEMENT_COMMAND.equals(name)) {
                                        if (obj == null) {
                                            throw new XmlPullParserException("executable element wasn't started");
                                        }
                                        obj2 = 1;
                                        semExecutableInfo = getActivityMetaData(context, Xml.asAttributeSet(loadXmlMetaData), componentName);
                                    }
                                    if (!XML_ELEMENT_EXTRA_ATTR.equals(name)) {
                                        continue;
                                    } else if (obj == null || r30 == null) {
                                        throw new XmlPullParserException("executable or command element wasn't started");
                                    } else {
                                        AttributeSet asAttributeSet = Xml.asAttributeSet(loadXmlMetaData);
                                        if (semExecutableInfo != null) {
                                            semExecutableInfo.addExtraAttribute(context, asAttributeSet);
                                        }
                                    }
                                } else if (next == 3) {
                                    if (XML_ELEMENT_EXECUTABLE.equals(name)) {
                                        obj = null;
                                    }
                                    if (XML_ELEMENT_COMMAND.equals(name)) {
                                        obj2 = null;
                                        if (checkValidate(semExecutableInfo)) {
                                            examineOrderInCategory(semExecutableInfo, WhiteListForCategoryOrder.getInstance().isAllowedToUseOrder(context, packageItemInfo2.packageName));
                                            semExecutableInfo.setId(packageItemInfo2.packageName);
                                            Object obj3 = null;
                                            for (SemExecutableInfo id : arrayList) {
                                                if (id.getId() == semExecutableInfo.getId()) {
                                                    obj3 = 1;
                                                }
                                            }
                                            if (obj3 == null) {
                                                arrayList.add(semExecutableInfo);
                                            }
                                        }
                                        semExecutableInfo = null;
                                    }
                                } else {
                                    continue;
                                }
                            }
                            continue;
                        } else {
                            continue;
                        }
                    } catch (Throwable e) {
                        SemLog.w(LOG_TAG, "Invalid attribute in metadata for " + componentName.flattenToShortString() + ": " + e.getMessage());
                    } catch (Throwable e2) {
                        SemLog.w(LOG_TAG, "Reading SemExecutableInfo metadata for " + componentName.flattenToShortString(), e2);
                    } catch (Throwable e3) {
                        SemLog.w(LOG_TAG, "Reading SemExecutableInfo metadata for " + componentName.flattenToShortString(), e3);
                    } catch (Throwable e4) {
                        SemLog.w(LOG_TAG, "Unknown Exception while Reading SemExecutableInfo metadata", e4);
                    }
                } else if (DEBUG) {
                    SemLog.d(LOG_TAG, "skip disable component: " + z + ", " + z2);
                }
            }
        }
        if (DEBUG) {
            SemLog.d(LOG_TAG, "scan SemExecutableInfo end: " + arrayList.size());
        }
        return arrayList;
    }

    private void setId(String str) {
        Builder builder = new Builder();
        builder.scheme(XML_ELEMENT_EXECUTABLE).authority(str);
        String valueOf = String.valueOf(((long) (getAction() + getPackageName() + getComponentName() + getLaunchType() + getBundleString()).hashCode()) & 4294967295L);
        try {
            SemLog.d(LOG_TAG, "Use defined mUid: " + Long.parseLong(this.mUid));
            valueOf = this.mUid;
        } catch (Exception e) {
            SemLog.d(LOG_TAG, "Not set mUid: " + this.mUid);
        }
        builder.appendPath(valueOf);
        this.mUid = builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SemExecutableInfo)) {
            return false;
        }
        SemExecutableInfo semExecutableInfo = obj;
        return (this.mEnabled == semExecutableInfo.mEnabled && this.mLabelId == semExecutableInfo.mLabelId && this.mIconId == semExecutableInfo.mIconId && this.mSmallIconId == semExecutableInfo.mSmallIconId && this.mLaunchType == semExecutableInfo.mLaunchType) ? (this.mUid == null ? semExecutableInfo.mUid == null : this.mUid.equals(semExecutableInfo.mUid)) ? (this.mCategory == null ? semExecutableInfo.mCategory == null : this.mCategory.equals(semExecutableInfo.mCategory)) ? (this.mAction == null ? semExecutableInfo.mAction == null : this.mAction.equals(semExecutableInfo.mAction)) ? (this.mPackageName == null ? semExecutableInfo.mPackageName == null : this.mPackageName.equals(semExecutableInfo.mPackageName)) ? (this.mFeatureNames == null ? semExecutableInfo.mFeatureNames == null : this.mFeatureNames.equals(semExecutableInfo.mFeatureNames)) ? (this.mFeatureValues == null ? semExecutableInfo.mFeatureValues == null : this.mFeatureValues.equals(semExecutableInfo.mFeatureValues)) ? (this.mBundle == null ? semExecutableInfo.mBundle == null : this.mBundle.equals(semExecutableInfo.mBundle)) ? (this.mComponentName == null ? semExecutableInfo.mComponentName == null : this.mComponentName.equals(semExecutableInfo.mComponentName)) ? this.mActivityLaunchMode == null ? semExecutableInfo.mActivityLaunchMode == null : this.mActivityLaunchMode.equals(semExecutableInfo.mActivityLaunchMode) : false : false : false : false : false : false : false : false : false;
    }

    public String getAction() {
        return this.mAction;
    }

    public int getActivityLaunchMode() {
        int i = 0;
        if (this.mActivityLaunchMode == null || this.mActivityLaunchMode.length() == 0) {
            return 0;
        }
        String[] split = this.mActivityLaunchMode.split("\\|");
        for (Object obj : split) {
            if (XML_ELEMENT_LAUCHMODE_NEWTASK.equals(obj)) {
                i |= 268435456;
            } else if (XML_ELEMENT_LAUCHMODE_SINGLETOP.equals(obj)) {
                i |= 536870912;
            }
            if (XML_ELEMENT_LAUCHMODE_CLEARTOP.equals(obj)) {
                i |= 67108864;
            }
        }
        return i;
    }

    public List<String> getCategories() {
        return (this.mCategory == null || TextUtils.isEmpty(this.mCategory)) ? new ArrayList() : Arrays.asList(this.mCategory.split("\\|"));
    }

    public String getComponentName() {
        return this.mComponentName;
    }

    public Bundle getExtras() {
        return this.mBundle;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public String getId() {
        return this.mUid;
    }

    public int getLabelId() {
        return this.mLabelId;
    }

    public int getLaunchType() {
        return this.mLaunchType;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getSmallIconId() {
        return this.mSmallIconId;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public String toString() {
        String str = "SemExecutableInfo{enabled=" + this.mEnabled + ", id=" + this.mUid + ", labelId=" + this.mLabelId + ", iconIId=" + this.mIconId + ", smallIconIId=" + this.mSmallIconId + ", type=" + this.mLaunchType + ", category=" + this.mCategory + ", action='" + this.mAction + DateFormat.QUOTE + ", packageName='" + this.mPackageName + DateFormat.QUOTE + ", componentName='" + this.mComponentName + DateFormat.QUOTE + ", launchMode='" + this.mActivityLaunchMode + DateFormat.QUOTE;
        for (int i = 0; i < this.mFeatureNames.size(); i++) {
            str = str + ", featureName ='" + ((String) this.mFeatureNames.get(i)) + DateFormat.QUOTE + ", featureValue = '" + ((String) this.mFeatureValues.get(i)) + DateFormat.QUOTE;
        }
        return (str + ", mBundle ='" + getBundleString() + DateFormat.QUOTE) + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mUid);
        parcel.writeInt(this.mEnabled ? 1 : 0);
        parcel.writeInt(this.mLabelId);
        parcel.writeInt(this.mIconId);
        parcel.writeInt(this.mSmallIconId);
        parcel.writeInt(this.mLaunchType);
        parcel.writeString(this.mCategory);
        parcel.writeString(this.mAction);
        parcel.writeString(this.mPackageName);
        parcel.writeStringList(this.mFeatureNames);
        parcel.writeStringList(this.mFeatureValues);
        parcel.writeBundle(this.mBundle);
        parcel.writeString(this.mComponentName);
        parcel.writeString(this.mActivityLaunchMode);
    }
}
