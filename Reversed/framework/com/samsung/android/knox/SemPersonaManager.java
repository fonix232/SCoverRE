package com.samsung.android.knox;

import android.app.ActivityManager;
import android.app.AppGlobals;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.CustomCursor;
import android.content.IRCPGlobalContactsDir;
import android.content.IRCPInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IKnoxModeChangeObserver;
import android.content.pm.IPersonaCallback;
import android.content.pm.IPersonaPolicyManager.Stub;
import android.content.pm.ISystemPersonaObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PersonaAttribute;
import android.content.pm.PersonaNewEvent;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersonaPolicyManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings.Secure;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.widget.Toast;
import com.samsung.android.emergencymode.SemEmergencyConstants;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.media.SemSoundAssistantManager;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import com.samsung.android.share.SShareConstants;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.transcode.core.Encode.BitRate;
import com.samsung.android.widget.SemHoverPopupWindow.Gravity;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class SemPersonaManager {
    public static final String ACCESS_TYPE_BLUETOOTH = "bluetooth";
    public static final String ACCESS_TYPE_SDCARD = "sdcard";
    private static final String ADAPT_SOUND_PACKAGE_NAME = "com.sec.hearingadjust";
    private static final String BBC_METADATA = "com.samsung.android.knoxenabled";
    public static final String BRIDGE_COMPONENT1 = "com.sec.knox.bridge/com.sec.knox.bridge.activity.SwitchB2BActivity";
    public static final String BRIDGE_COMPONENT2 = "com.sec.knox.bridge/com.sec.knox.bridge.activity.SwitchB2BActivity2";
    public static final String BRIDGE_PKG = "com.sec.knox.bridge";
    public static final String CALLER_DISPLAY_NAME = "caller_display_name";
    public static final String CALLER_PHONE_NUMBER = "caller_phone_number";
    public static final String CALLER_PHOTO = "caller_photo";
    public static final String CONTACT_OWNER_ID = "contact_owner_id";
    public static final int CONTAINER_COM_TYPE = 3;
    public static final int CONTAINER_DEFAULT_TYPE = 1;
    public static final int CONTAINER_LWC_TYPE = 2;
    public static final int CONTAINER_TYPE_MY_KNOX = 1;
    public static final int CONTAINER_TYPE_SECURE_FOLDER = 2;
    private static final boolean DEBUG = false;
    public static String DEFAULT_KNOX_NAME = "KNOX";
    public static final int DEFAULT_SDP_ACTIVATION_TIME = 5000;
    public static final String ENABLE_EULA = "enable_eula";
    public static final int ERROR_CREATE_PERSONA_ADMIN_ACTIVATION_FAILED = -1009;
    public static final int ERROR_CREATE_PERSONA_ADMIN_INSTALLATION_FAILED = -1008;
    public static final int ERROR_CREATE_PERSONA_EC_MAX_PERSONA_LIMIT_REACHED = -1037;
    public static final int ERROR_CREATE_PERSONA_EMERGENCY_MODE_FAILED = -1031;
    public static final int ERROR_CREATE_PERSONA_FILESYSTEM_ERROR = -1011;
    public static final int ERROR_CREATE_PERSONA_GENERATE_CMK_FAILED = -1034;
    public static final int ERROR_CREATE_PERSONA_HANDLER_INSTALLATION_FAILED = -1006;
    public static final int ERROR_CREATE_PERSONA_INTERNAL_ERROR = -1014;
    public static final int ERROR_CREATE_PERSONA_MAX_PERSONA_LIMIT_REACHED = -1012;
    public static final int ERROR_CREATE_PERSONA_NO_HANDLER_APK = -1002;
    public static final int ERROR_CREATE_PERSONA_NO_NAME = -1001;
    public static final int ERROR_CREATE_PERSONA_NO_PERSONA_ADMIN_APK = -1004;
    public static final int ERROR_CREATE_PERSONA_NO_PERSONA_TYPE = -1005;
    public static final int ERROR_CREATE_PERSONA_NO_SETUPWIZARD_APK = -1003;
    public static final int ERROR_CREATE_PERSONA_RUNTIME_PERMISSION_GRANT = -1035;
    public static final int ERROR_CREATE_PERSONA_SECURE_FOLDER_MAX_PERSONA_LIMIT_REACHED = -1036;
    public static final int ERROR_CREATE_PERSONA_SETUPWIZARD_INSTALLATION_FAILED = -1007;
    public static final int ERROR_CREATE_PERSONA_SUB_USER_FAILED = -1027;
    public static final int ERROR_CREATE_PERSONA_SYSTEM_APP_INSTALLATION_FAILED = -1010;
    public static final int ERROR_CREATE_PERSONA_TIMA_PWD_KEY_FAILED = -1033;
    public static final int ERROR_CREATE_PERSONA_USER_INFO_INVALID = -1032;
    public static final int ERROR_INVAILD_CONTAINER_ID = -1301;
    public static final int ERROR_NO_PERSONA_SERVICE = -1300;
    public static final int ERROR_PERSONA_APP_INSTALLATION_FAILED = -2001;
    public static final int ERROR_REMOVE_NOT_PERSONA_OWNER = -1203;
    public static final int ERROR_REMOVE_PERSONA_FAILED = -1201;
    public static final int ERROR_REMOVE_PERSONA_NOT_EXIST = -1202;
    public static final int ERROR_SWITCH_EQUALS_CURRENT_USER = -1105;
    public static final int ERROR_SWITCH_INVALID_PERSONA_ID = -1100;
    public static final int ERROR_SWITCH_OUTSIDE_PERSONA_GROUP = -1106;
    public static final int ERROR_SWITCH_PERSONA_FILESYSTEM = -1103;
    public static final int ERROR_SWITCH_PERSONA_HANDLER_NOT_RESPONDING = -1104;
    public static final int ERROR_SWITCH_PERSONA_LOCKED = -1102;
    public static final int ERROR_SWITCH_PERSONA_NOT_INITIALIZED = -1101;
    public static final int FLAG_ADMIN_TYPE_APK = 16;
    public static final int FLAG_ADMIN_TYPE_NONE = 64;
    public static final int FLAG_ADMIN_TYPE_PACKAGENAME = 32;
    private static final int FLAG_BASE = 1;
    public static final int FLAG_BBC_CONTAINER = 4096;
    public static final int FLAG_CREATOR_SELF_DESTROY = 8;
    public static final int FLAG_ECRYPT_FILESYSTEM = 2;
    public static final int FLAG_EC_ENABLED = 65536;
    public static final int FLAG_KIOSK_ENABLED = 1024;
    public static final int FLAG_LIGHT_WEIGHT_CONTAINER = 512;
    public static final int FLAG_MIGRATION = 256;
    public static final int FLAG_PURE_ENABLED = 2048;
    public static final int FLAG_SECURE_FOLDER_CONTAINER = 8192;
    public static final int FLAG_TIMA_STORAGE = 4;
    public static final int FLAG_USER_MANAGED_CONTAINER = 128;
    public static final String FOLDERCONTAINER_PKG_NAME = "com.sec.knox.foldercontainer";
    public static final String INTENT_ACCESS_EXT_SDCARD = "com.sec.knox.container.access.extsdcard";
    public static final String INTENT_ACTION_CONTAINER_REMOVAL_STARTED = "com.sec.knox.container.action.containerremovalstarted";
    public static final String INTENT_ACTION_LAUNCH_INFO = "com.sec.knox.container.action.launchinfo";
    public static final String INTENT_ACTION_NFC_POLICY = "com.sec.knox.container.action.nfc.policy";
    public static final String INTENT_ACTION_OBSERVER = "com.sec.knox.container.action.observer";
    public static final String INTENT_ACTION_SDP_TIMEOUT = "com.sec.knox.container.INTENT_KNOX_SDP_ACTIVATED";
    public static final String INTENT_CATEGORY_OBSERVER_CONTAINERID = "com.sec.knox.container.category.observer.containerid";
    public static final String INTENT_CATEGORY_OBSERVER_ONATTRIBUTECHANGE = "com.sec.knox.container.category.observer.onattributechange";
    public static final String INTENT_CATEGORY_OBSERVER_ONKEYGUARDSTATECHANGED = "com.sec.knox.container.category.observer.onkeyguardstatechanged";
    public static final String INTENT_CATEGORY_OBSERVER_ONPERSONASWITCH = "com.sec.knox.container.category.observer.onpersonaswitch";
    public static final String INTENT_CATEGORY_OBSERVER_ONSESSIONEXPIRED = "com.sec.knox.container.category.observer.onsessionexpired";
    public static final String INTENT_CATEGORY_OBSERVER_ONSTATECHANGE = "com.sec.knox.container.category.observer.onstatechange";
    public static final String INTENT_CONTAINER_NEED_RESTART = "com.sec.knox.container.need.restart";
    public static final String INTENT_EXTRA_CONTAINER_ID = "containerId";
    public static final String INTENT_EXTRA_OBSERVER_ATTRIBUTE = "com.sec.knox.container.extra.observer.attribute";
    public static final String INTENT_EXTRA_OBSERVER_ATTRIBUTE_STATE = "com.sec.knox.container.extra.observer.attribute.state";
    public static final String INTENT_EXTRA_OBSERVER_KEYGUARDSTATE = "com.sec.knox.container.extra.observer.keyguardstate";
    public static final String INTENT_EXTRA_OBSERVER_NEWSTATE = "com.sec.knox.container.extra.observer.newstate";
    public static final String INTENT_EXTRA_OBSERVER_PREVIOUSSTATE = "com.sec.knox.container.extra.observer.previousstate";
    public static final String INTENT_EXTRA_SOURCE = "source";
    public static final int INTENT_EXTRA_SOURCE_SBA = 1;
    public static final int INTENT_EXTRA_SOURCE_SBA_BLACKLIST = 2;
    public static final int INTENT_EXTRA_SOURCE_WHITELIST = 0;
    public static final String INTENT_EXTRA_UPDATED_VALUE = "com.sec.knox.container.extra.updated.value";
    public static final String INTENT_PERMISSION_LAUNCH_INFO = "com.samsung.container.LAUNCH_INFO";
    public static final String INTENT_PERMISSION_OBSERVER = "com.samsung.container.OBSERVER";
    public static final String INTENT_PERMISSION_RECEIVE_KNOX_APPS_UPDATE = "com.sec.knox.container.permission.RECEIVE_KNOX_APPS_UPDATE";
    private static final String KNOX_SETTINGS_PACKAGE_NAME = "com.sec.knox.containeragent2";
    public static final String KNOX_SWITCH1_PKG = "com.sec.knox.switchknoxI";
    public static final String KNOX_SWITCH2_PKG = "com.sec.knox.switchknoxII";
    public static final String KNOX_SWITCHER_PKG = "com.sec.knox.switcher";
    public static final String KNOX_SWITCH_1_PKG = "com.sec.knox.switchknoxI";
    public static final String KNOX_SWITCH_2_PKG = "com.sec.knox.switchknoxII";
    public static final String KNOX_SWITCH_COMPONENT1 = "com.sec.knox.switcher/com.sec.knox.switcher.SwitchB2bActivityI";
    public static final String KNOX_SWITCH_COMPONENT2 = "com.sec.knox.switcher/com.sec.knox.switcher.SwitchB2bActivityII";
    public static final int MAX_ACTIVE_BUTTONS_ZERO_FOR_KIOSK = 10;
    public static final int MAX_BBC_ID = 199;
    public static final int MAX_PERSONA_ALLOWED = 2;
    public static final int MAX_PERSONA_ALLOWED_SECURE_FOLDER = 1;
    public static final int MAX_PERSONA_ID = 200;
    public static final int MAX_SECURE_FOLDER_ID = 160;
    private static final String MESSAGE_PACKAGE_NAME = "com.sec.knox.shortcutsms";
    private static final String METADATA_VR_APPLICATION_MODE = "com.samsung.android.vr.application.mode";
    private static final String METADATA_VR_MODE_DUAL = "dual";
    private static final String METADATA_VR_MODE_VR_ONLY = "vr_only";
    public static final int MINIMUM_SCREEN_OFF_TIMEOUT = 5000;
    public static final int MIN_BBC_ID = 195;
    public static final int MIN_PERSONA_ID = 100;
    public static final int MIN_SECURE_FOLDER_ID = 150;
    public static final int MOVE_TO_APP_TYPE_GALLERY = 1;
    public static final int MOVE_TO_APP_TYPE_MUSIC = 3;
    public static final int MOVE_TO_APP_TYPE_MYFILES = 4;
    public static final int MOVE_TO_APP_TYPE_VIDEO = 2;
    public static final int MOVE_TO_CONTAINER_TYPE_ENTERPRISE_CONTAINER = 1000;
    public static final int MOVE_TO_CONTAINER_TYPE_KNOX = 1001;
    public static final int MOVE_TO_CONTAINER_TYPE_SECURE_FOLDER = 1002;
    public static final int MOVE_TO_PERSONAL_TYPE_KNOX = 1004;
    public static final int MOVE_TO_PERSONAL_TYPE_SECURE_FOLDER = 1003;
    public static final String NOTIFICATION_LIST_FOR_KIOSK = "Wifi;MultiWindow;Location;SilentMode;AutoRotate;Bluetooth;NetworkBooster;MobileData;AirplaneMode;Nfc;SmartStay;PowerSaving;TorchLight;WiFiHotspot;";
    public static final String NOTIFICATION_LIST_FOR_KIOSK_M = "Wifi,Location,SilentMode,RotationLock,Bluetooth,MobileData,PowerSaving,AirplaneMode,Flashlight,WifiHotspot,SmartStay,Nfc,custom(com.android.nfc/com.samsung.android.nfc.quicktile.NfcTile),custom(com.android.settings/com.samsung.android.settings.qstile.PowerSavingTile)";
    public static final String PERMISSION_KEYGUARD_ACCESS = "com.sec.knox.container.keyguard.ACCESS";
    public static final String PERMISSION_PERIPHERAL_POLICY_UPDATE = "com.sec.knox.container.peripheral.POLICY_UPDATE";
    private static final String PERSONAL_HOME_PACKAGE_NAME = "com.sec.knox.switcher";
    public static final String PERSONA_ID = "persona_id";
    public static final String PERSONA_POLICY_SERVICE = "persona_policy";
    public static final int PERSONA_TIMA_ECRPTFS_INDEX1 = 100;
    public static final int PERSONA_TIMA_ECRPTFS_INDEX2 = 102;
    public static final int PERSONA_TIMA_PASSWORDHINT_INDEX = 104;
    public static final int PERSONA_TIMA_PASSWORD_INDEX1 = 101;
    public static final int PERSONA_TIMA_PASSWORD_INDEX2 = 103;
    public static final String PERSONA_VALIDATOR_HANDLER = "persona_validator";
    private static final String PHONE_PACKAGE_NAME = "com.sec.knox.shortcutsms";
    public static final int REMOVE_OP_SUCCESS = 0;
    public static String SECOND_KNOX_NAME = "KNOX II";
    public static String SECURE_FOLDER_NAME = "secure-folder";
    public static final String SETUP_WIZARD_PKG_NAME = "com.sec.knox.setup";
    static final String[] SHORTCUT_FILTER = new String[]{"com.sec.knox.shortcutsms", "com.sec.knox.shortcutsms", "com.sec.knox.switcher", ADAPT_SOUND_PACKAGE_NAME, KNOX_SETTINGS_PACKAGE_NAME};
    private static String TAG = "SemPersonaManager";
    public static final int TIMA_COMPROMISED_TYPE1 = 65548;
    public static final int TIMA_COMPROMISED_TYPE2 = 65549;
    public static final int TIMA_COMPROMISED_TYPE3 = 65550;
    public static final int TIMA_COMPROMISED_TYPE4 = 65551;
    public static final int TIMA_VALIDATION_SUCCESS = 0;
    public static final int TYPE_B2B_KNOX = 0;
    public static final String[] approvedPackages = new String[]{"com.android.chrome", "com.google.android.apps", "com.google.android.apps.plus", "com.google.android.apps.docs", "com.google.android.gm", "com.google.android.googlequicksearchbox", "com.google.android.talk", "com.google.android.apps.maps", "com.google.android.apps.books", "com.google.android.play.games", "com.google.android.music", "com.google.android.videos", "com.google.android.apps.magazines", "com.google.android.apps.photos", "com.google.android.youtube", "com.samsung.android.app.memo", "com.sec.keystringscreen", "com.infraware.polarisoffice5", "com.microsoft.office.excel", "com.microsoft.office.powerpoint", "com.microsoft.office.word", "com.hancom.androidpc.viewer.launcher", "com.hancom.office.editor", "com.whatsapp", "com.tencent.mm", "com.facebook.katana", "com.facebook.orca", "com.instagram.android", "com.skype.raider", "com.microsoft.office.onenote", "com.microsoft.skydrive"};
    public static final String[] chinaStorePackages = new String[]{"com.baidu.appsearch", "com.qihoo.appstore", "com.tencent.android.qqdownloader", "com.wandoujia.phoenix2", "com.dragon.android.pandaspace", "com.android.vending"};
    public static final String[] excludedPackages = new String[]{getFloatingPackageName("SEC_FLOATING_FEATURE_MESSAGE_CONFIG_PACKAGE_NAME", "com.android.mms"), SShareConstants.SCREEN_MIRRORING_PKG, "com.sec.knox.knoxsetupwizardclient", KNOX_SETTINGS_PACKAGE_NAME, "com.sec.knox.setupwizardstub", "com.sec.chaton", "com.sec.pcw", "com.sec.knox.shortcutsms", "com.sec.knox.switcher", "com.sec.watchon.phone", "com.sec.android.automotive.drivelink", "com.samsung.android.app.lifetimes", "com.sec.android.app.shealth", "com.sec.android.app.voicenote", "com.sec.android.app.kidshome", "com.sec.knox.app.container", "com.sec.knox.shortcuti", "com.sec.knox.containeragent", "com.sec.android.app.samsungapps", "tv.peel.smartremote", "com.skt.prod.phonebook", "com.sec.enterprise.knox.express", "com.google.android.apps.walletnfcrel", "com.samsung.android.voc", "com.skt.tservice", "com.sktelecom.minit", "com.skt.prod.dialer", "com.skt.skaf.A000VODBOX", "com.skt.skaf.OA00050017", "com.skt.skaf.A000Z00040", "com.skt.skaf.OA00026910", "com.skt.skaf.l001mtm091", "com.skt.prod.phonebook", "com.skt.smartbill", "com.skt.tbagplus", "com.sktelecom.tguard", "com.skt.tdatacoupon", "com.skb.btvmobile", "com.iloen.melon", "com.nate.android.portalmini", "com.tms", "com.skmc.okcashbag.home_google", "com.elevenst", "com.elevenst.deals", "com.moent.vas", "com.skmnc.gifticon", "com.skt.tmaphot", "com.skplanet.mbuzzer", "com.skt.tgift", "com.sktelecom.tsmartpay", "com.cyworld.camera", "com.kt.android.showtouch", "com.kt.wificm", "com.ktshow.cs", "com.kt.olleh.storefront", "com.kth.kshop", "com.show.greenbill", "com.estsoft.alyac", "com.kt.accessory", "kt.navi", "com.olleh.android.oc2", "com.kt.ollehfamilybox", "com.kt.otv", "com.olleh.webtoon", "com.kt.shodoc", "com.ktmusic.geniemusic", "com.ktcs.whowho", "com.kt.apptong", "com.mtelo.ktAPP", "com.kt.bellringolleh", "com.kt.mpay", "com.kt.aljjapackplus", "com.lguplus.appstore", "com.uplus.onphone", "com.lguplus.mobile.cs", "lg.uplusbox", "com.lgu.app.appbundle", "lgt.call", "com.mnet.app", "com.lguplus.usimsvcm", "com.lguplus.navi", "com.lguplus.paynow", "com.uplus.movielte", "com.estsoft.alyac", "com.lguplus.ltealive", "com.uplus.ipagent", "com.lguplus.homeiot", "com.uplus.baseballhdtv", "com.lgu", "com.lgt.tmoney", "com.lguplus.smartotp", "net.daum.android.map", "com.sds.mms.ui", "com.navitime.local.naviwalk", "jp.id_credit_sp.android", "jp.id_credit_sp.android.devappli", "com.nttdocomo.android.dpoint", "com.nttdocomo.android.voicetranslation", "com.nttdocomo.android.moneyrecord", "com.kddi.android.videopass", "com.nttdocomo.android.photocollection", SShareConstants.SYSTEM_UI_PACKAGE, "com.sec.sprint.wfcstub", "com.sec.sprint.wfc", "com.oculus.horizon", "com.samsung.android.app.watchmanager", "com.samsung.android.wms", "com.samsung.android.gear360manager", "com.samsung.android.samsunggear360manager", "com.samsung.android.video360"};
    public static final String[] excludedPackagesForJPN = new String[]{"com.facebook.katana", "com.facebook.orca", "com.instagram.android", "com.twitter.android", "jp.gamegift"};
    public static final String[] excludedPackagesForMyKnox = new String[]{"com.samsung.android.voc"};
    private static Object mBTSecureManagerSync = new Object();
    private static Bundle mKnoxInfo = null;
    private static ArrayList<Bundle> mMoveToInfo = null;
    public static boolean m_bIsKnoxInfoInitialized = false;
    public static final String[] mdmPackages = new String[]{"com.samsung.mdmtest1", "com.samsung.mdmtest2", "com.samsung.edmtest", "com.samsung.containertool"};
    private static SparseArray<PathStrategy> pathstrategy = new SparseArray(1);
    private static SemPersonaManager personaManager = null;
    private static PersonaPolicyManager personaPolicyMgr = null;
    private static SemRemoteContentManager rcpManager = null;
    private String[] NFCblackList = new String[]{"com.google.android.gms.smartdevice.setup.d2d:nfc2bt_bootstrap", "com.google.android.gms.auth.setup.d2d:nfc2bt_bootstrap"};
    private final Context mContext;
    private final ISemPersonaManager mService;

    public enum AppType {
        IME("TYPE_IME"),
        INSTALLER_WHITELIST("installerWhitelist"),
        DISABLED_LAUNCHERS("disabledLaunchers"),
        COM_DISABLED_OWNER_LAUNCHERS("comDisabledOwnerLaunchers");
        
        private final String mName;

        private AppType(String str) {
            this.mName = str;
        }

        public AppType fromName(String str) {
            for (AppType appType : values()) {
                if (appType.mName.equals(str)) {
                    return appType;
                }
            }
            return null;
        }

        public String getName() {
            return this.mName;
        }
    }

    public enum KnoxContainerVersion {
        KNOX_CONTAINER_VERSION_NONE,
        KNOX_CONTAINER_VERSION_1_0_0,
        KNOX_CONTAINER_VERSION_2_0_0,
        KNOX_CONTAINER_VERSION_2_1_0,
        KNOX_CONTAINER_VERSION_2_2_0,
        KNOX_CONTAINER_VERSION_2_3_0,
        KNOX_CONTAINER_VERSION_2_3_1,
        KNOX_CONTAINER_VERSION_2_4_0,
        KNOX_CONTAINER_VERSION_2_4_1,
        KNOX_CONTAINER_VERSION_2_5_0,
        KNOX_CONTAINER_VERSION_2_5_1,
        KNOX_CONTAINER_VERSION_2_5_2,
        KNOX_CONTAINER_VERSION_2_6_0,
        KNOX_CONTAINER_VERSION_2_6_1,
        KNOX_CONTAINER_VERSION_2_7_0,
        KNOX_CONTAINER_VERSION_2_7_1,
        KNOX_CONTAINER_VERSION_2_8_0;

        public int getVersionNumber() {
            switch (m5xe397a8a()[ordinal()]) {
                case 1:
                    return 100;
                case 2:
                    return 200;
                case 3:
                    return 210;
                case 4:
                    return 220;
                case 5:
                    return 230;
                case 6:
                    return 231;
                case 7:
                    return 240;
                case 8:
                    return 241;
                case 9:
                    return 250;
                case 10:
                    return 251;
                case 11:
                    return 252;
                case 12:
                    return 260;
                case 13:
                    return Gravity.RIGHT_CENTER_AXIS;
                case 14:
                    return 270;
                case 15:
                    return 271;
                case 16:
                    return BitRate.VIDEO_QCIF_BITRATE;
                default:
                    return -1;
            }
        }

        public String toString() {
            switch (m5xe397a8a()[ordinal()]) {
                case 1:
                    return "1.0.0";
                case 2:
                    return "2.0.0";
                case 3:
                    return "2.1.0";
                case 4:
                    return "2.2.0";
                case 5:
                    return "2.3.0";
                case 6:
                    return "2.3.1";
                case 7:
                    return "2.4.0";
                case 8:
                    return "2.4.1";
                case 9:
                    return "2.5.0";
                case 10:
                    return "2.5.1";
                case 11:
                    return "2.5.2";
                case 12:
                    return "2.6.0";
                case 13:
                    return "2.6.1";
                case 14:
                    return "2.7.0";
                case 15:
                    return "2.7.1";
                case 16:
                    return "2.8.0";
                default:
                    return "N/A";
            }
        }
    }

    public static class PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        public PathStrategy(String str, int i) {
            this.mAuthority = str;
            addRoot(SemPersonaManager.ACCESS_TYPE_SDCARD, getexternalStorage(i));
        }

        private static File buildPath(File file, String... strArr) {
            File file2 = file;
            int i = 0;
            int length = strArr.length;
            File file3 = file2;
            while (i < length) {
                String str = strArr[i];
                i++;
                file3 = file3 == null ? new File(str) : new File(file3, str);
            }
            return file3;
        }

        private static File getexternalStorage(int i) {
            String str = "EMULATED_STORAGE_TARGET";
            Object obj = System.getenv("EMULATED_STORAGE_TARGET");
            if (TextUtils.isEmpty(obj)) {
                return Environment.getExternalStorageDirectory();
            }
            return buildPath(new File(obj), String.valueOf(i));
        }

        public void addRoot(String str, File file) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(str, file.getCanonicalFile());
            } catch (Throwable e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file, e);
            }
        }

        public Uri getUriForFile(Uri uri) {
            try {
                String path;
                String canonicalPath = new File(uri.getPath()).getCanonicalPath();
                Entry entry = null;
                for (Entry entry2 : this.mRoots.entrySet()) {
                    path = ((File) entry2.getValue()).getPath();
                    if (canonicalPath.startsWith(path) && (entry == null || path.length() > ((File) entry.getValue()).getPath().length())) {
                        entry = entry2;
                    }
                }
                if (entry == null) {
                    return uri;
                }
                path = ((File) entry.getValue()).getPath();
                return new Builder().scheme("content").authority(this.mAuthority).encodedPath(Uri.encode((String) entry.getKey()) + '/' + Uri.encode(path.endsWith("/") ? canonicalPath.substring(path.length()) : canonicalPath.substring(path.length() + 1), "/")).build();
            } catch (IOException e) {
                return uri;
            }
        }
    }

    public static class StateManager {
        private final Context mContext;
        private final ISemPersonaManager mService;
        private int userId;

        private StateManager(Context context, ISemPersonaManager iSemPersonaManager, int i) {
            this.mService = iSemPersonaManager;
            this.mContext = context;
            this.userId = i;
        }

        public SemPersonaState fireEvent(PersonaNewEvent personaNewEvent) {
            Log.d(SemPersonaManager.TAG, "StateManager.fireEvent()");
            if (this.mService != null) {
                try {
                    return this.mService.fireEvent(personaNewEvent, this.userId);
                } catch (Throwable e) {
                    Log.w(SemPersonaManager.TAG, "getState() Cannot make call", e);
                }
            }
            return null;
        }

        public SemPersonaState getPreviousState() {
            Log.d(SemPersonaManager.TAG, "StateManager.getState()");
            if (this.mService != null) {
                try {
                    return this.mService.getPreviousState(this.userId);
                } catch (Throwable e) {
                    Log.w(SemPersonaManager.TAG, "getPreviousState() Cannot make call", e);
                }
            }
            return null;
        }

        public SemPersonaState getState() {
            if (this.mService != null) {
                try {
                    return this.mService.getState(this.userId);
                } catch (Throwable e) {
                    Log.w(SemPersonaManager.TAG, "getState() Cannot make call", e);
                }
            }
            return null;
        }

        public boolean inState(SemPersonaState semPersonaState) {
            if (this.mService != null) {
                try {
                    return this.mService.inState(semPersonaState, this.userId);
                } catch (Throwable e) {
                    Log.w(SemPersonaManager.TAG, "getState() Cannot make call", e);
                }
            }
            return false;
        }

        public boolean isAttribute(PersonaAttribute personaAttribute) {
            Log.d(SemPersonaManager.TAG, "StateManager.isAttribute()");
            if (this.mService != null) {
                try {
                    return this.mService.isAttribute(personaAttribute, this.userId);
                } catch (Throwable e) {
                    Log.w(SemPersonaManager.TAG, "getState() Cannot make call", e);
                }
            }
            return false;
        }

        public boolean setAttribute(PersonaAttribute personaAttribute, boolean z) {
            Log.d(SemPersonaManager.TAG, "StateManager.setAttribute()");
            if (this.mService != null) {
                try {
                    return this.mService.setAttribute(personaAttribute, z, this.userId);
                } catch (Throwable e) {
                    Log.w(SemPersonaManager.TAG, "getState() Cannot make call", e);
                }
            }
            return false;
        }
    }

    public SemPersonaManager(Context context, ISemPersonaManager iSemPersonaManager) {
        this.mService = iSemPersonaManager;
        this.mContext = context;
    }

    private static boolean containerExists(String str, int i) {
        if (SmartFaceManager.PAGE_MIDDLE.equals(str)) {
            return false;
        }
        String str2 = ":";
        String[] split = str.split(":");
        for (int i2 = 1; i2 < split.length; i2++) {
            if (split[i2].equals(String.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public static Bundle exchangeData(Context context, Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = null;
        try {
            if ("RequestProxy".equals(bundle.getString(SemEmergencyConstants.ACTION))) {
                SemRemoteContentManager rCPManager = getRCPManager(context);
                if (rCPManager != null) {
                    bundle2 = rCPManager.exchangeData(context, 0, bundle);
                }
            } else if ("MoveTo".equals(bundle.getString(SemEmergencyConstants.ACTION))) {
                BaseBundle bundle3 = new Bundle();
                BaseBundle baseBundle;
                try {
                    Intent intent = new Intent("action.com.sec.knox.container.exchangeData");
                    intent.putExtras(bundle);
                    intent.putExtra("launchFromSemPersonaManager", true);
                    intent.setClassName("com.samsung.knox.rcp.components", "com.sec.knox.bridge.operations.ExchangeDataReceiver");
                    context.sendBroadcast(intent);
                    bundle3.putInt("result", 0);
                    baseBundle = bundle3;
                } catch (Exception e) {
                    Throwable e2 = e;
                    baseBundle = bundle3;
                    e2.printStackTrace();
                    return bundle2;
                }
            }
        } catch (Exception e3) {
            e2 = e3;
            e2.printStackTrace();
            return bundle2;
        }
        return bundle2;
    }

    public static Drawable getBBCBadgeIcon(ActivityInfo activityInfo, int i) {
        BaseBundle baseBundle = activityInfo.applicationInfo.metaData;
        if (!(baseBundle == null || baseBundle.getInt(BBC_METADATA) == 0 || (i != 0 && !isBBCContainer(i)))) {
            try {
                byte[] applicationIconFromDb = EnterpriseDeviceManager.getInstance().getApplicationPolicy().getApplicationIconFromDb(activityInfo.packageName, 0);
                if (applicationIconFromDb != null) {
                    InputStream byteArrayInputStream = new ByteArrayInputStream(applicationIconFromDb);
                    TypedValue typedValue = new TypedValue();
                    typedValue.density = 0;
                    Drawable createFromResourceStream = Drawable.createFromResourceStream(null, typedValue, byteArrayInputStream, null);
                    Log.i(TAG, "EDM:ApplicationIcon got from EDM database ");
                    return createFromResourceStream;
                }
            } catch (Exception e) {
                Log.w(TAG, "EDM: Get Icon EX: " + e);
            }
        }
        return null;
    }

    public static int getBbcEnabled() {
        String str = SystemProperties.get("sys.knox.bbcid", null);
        int i = DeviceRootKeyServiceManager.ERR_SERVICE_ERROR;
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    i = Integer.valueOf(str).intValue();
                }
            } catch (Exception e) {
            }
        }
        return i;
    }

    private static String getFloatingPackageName(String str, String str2) {
        String str3 = str2;
        try {
            str3 = SemFloatingFeature.getInstance().getString(str, str2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return str3;
    }

    public static KnoxContainerVersion getKnoxContainerVersion() {
        BaseBundle knoxInfo = getKnoxInfo();
        if (knoxInfo == null) {
            return null;
        }
        if ("2.0".equals(knoxInfo.getString(SemSoundAssistantManager.VERSION))) {
            return Integer.parseInt("14") == 0 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_0_0 : Integer.parseInt("14") == 1 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_1_0 : Integer.parseInt("14") == 2 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_2_0 : Integer.parseInt("14") == 3 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_3_0 : Integer.parseInt("14") == 4 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_3_1 : Integer.parseInt("14") == 5 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0 : Integer.parseInt("14") == 6 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_1 : Integer.parseInt("14") == 7 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_0 : Integer.parseInt("14") == 8 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_1 : Integer.parseInt("14") == 9 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_2 : Integer.parseInt("14") == 10 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_6_0 : Integer.parseInt("14") == 11 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_6_1 : Integer.parseInt("14") == 12 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_7_0 : Integer.parseInt("14") == 13 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_7_1 : Integer.parseInt("14") == 14 ? KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_8_0 : null;
        } else {
            if ("1.0".equals(knoxInfo.getString(SemSoundAssistantManager.VERSION))) {
                Log.i(TAG, "mKnoxInfo returns 1.0");
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_1_0_0;
            }
            Log.i(TAG, "mKnoxInfo is empty");
            return KnoxContainerVersion.KNOX_CONTAINER_VERSION_NONE;
        }
    }

    public static Bundle getKnoxInfo() {
        synchronized (SemPersonaManager.class) {
            if (mKnoxInfo == null) {
                mKnoxInfo = new Bundle();
                try {
                    String str = SystemProperties.get("ro.config.knox");
                    if (str == null || str.isEmpty()) {
                        mKnoxInfo.putString(SemSoundAssistantManager.VERSION, "");
                    } else {
                        if ("v30".equals(str)) {
                            str = "2.0";
                        } else if (SmartFaceManager.PAGE_BOTTOM.equals(str)) {
                            str = "1.0";
                        }
                        mKnoxInfo.putString(SemSoundAssistantManager.VERSION, str);
                    }
                    mKnoxInfo.putString("isSupportCallerInfo", SmartFaceManager.FALSE);
                } catch (Throwable e) {
                    e.printStackTrace();
                    mKnoxInfo.putString(SemSoundAssistantManager.VERSION, "");
                }
            }
        }
        return mKnoxInfo;
    }

    public static Bundle getKnoxInfoForApp(Context context) {
        if (mKnoxInfo == null) {
            getKnoxInfo();
        }
        try {
            if ("2.0".equals(mKnoxInfo.getString(SemSoundAssistantManager.VERSION))) {
                getKnoxInfoForApp(context, "isSupportMoveTo");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return mKnoxInfo;
    }

    public static Bundle getKnoxInfoForApp(Context context, String str) {
        synchronized (SemPersonaManager.class) {
            String str2;
            SemPersonaManager personaService;
            byte[] bArr;
            if (mKnoxInfo == null) {
                getKnoxInfo();
            }
            int myUserId = UserHandle.myUserId();
            mKnoxInfo.putInt(FingerprintEvent.IDENTIFY_INFO_KEY_USER_ID, myUserId);
            mKnoxInfo.putInt("checkVersion", getKnoxContainerVersion().ordinal());
            if (!m_bIsKnoxInfoInitialized) {
                if (myUserId >= 100) {
                    mKnoxInfo.putString("isKnoxMode", SmartFaceManager.TRUE);
                    IEDMProxy service = EDMProxyServiceHelper.getService();
                    if (service == null || !service.isPackageAllowedToAccessExternalSdcard(myUserId, Binder.getCallingUid())) {
                        try {
                            mKnoxInfo.putString("isBlockExternalSD", SmartFaceManager.TRUE);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } else {
                        mKnoxInfo.putString("isBlockExternalSD", SmartFaceManager.FALSE);
                    }
                    mKnoxInfo.putString("isBlockBluetoothMenu", SmartFaceManager.TRUE);
                    mKnoxInfo.putString("isSamsungAccountBlocked", SmartFaceManager.TRUE);
                }
                if (isSecureFolderId(myUserId)) {
                    mKnoxInfo.putString("isBlockExternalSD", SmartFaceManager.TRUE);
                }
                if (isKioskModeEnabled(context)) {
                    mKnoxInfo.putString("isKioskModeEnabled", SmartFaceManager.TRUE);
                } else {
                    mKnoxInfo.putString("isKioskModeEnabled", SmartFaceManager.FALSE);
                }
                m_bIsKnoxInfoInitialized = true;
            }
            if (!SmartFaceManager.TRUE.equals(mKnoxInfo.getString("isKioskModeEnabled")) && "isSupportMoveTo".equals(str)) {
                setMoveToKnoxInfo(context, mKnoxInfo, myUserId);
            }
            if ("isKnoxModeActive".equals(str)) {
                if (ActivityManager.getCurrentUser() >= 100) {
                    mKnoxInfo.putString("isKnoxModeActive", SmartFaceManager.TRUE);
                } else {
                    mKnoxInfo.putString("isKnoxModeActive", SmartFaceManager.FALSE);
                }
            }
            if ("isKioskModeEnabled".equals(str)) {
                if (isKioskModeEnabled(context)) {
                    mKnoxInfo.putString("isKioskModeEnabled", SmartFaceManager.TRUE);
                } else {
                    mKnoxInfo.putString("isKioskModeEnabled", SmartFaceManager.FALSE);
                }
            }
            if ("isSecureFolderExist".equals(str)) {
                if (isSecureFolderExist(context)) {
                    mKnoxInfo.putString("isSecureFolderExist", SmartFaceManager.TRUE);
                } else {
                    mKnoxInfo.putString("isSecureFolderExist", SmartFaceManager.FALSE);
                }
            }
            if ("isSmartSwitchBnRAvailable".equals(str)) {
                if (isMyknoxExist(context)) {
                    mKnoxInfo.putString("isMyknoxExist", SmartFaceManager.TRUE);
                } else {
                    mKnoxInfo.putString("isMyknoxExist", SmartFaceManager.FALSE);
                }
                if (isSecureFolderExist(context)) {
                    mKnoxInfo.putString("isSecureFolderExist", SmartFaceManager.TRUE);
                } else {
                    mKnoxInfo.putString("isSecureFolderExist", SmartFaceManager.FALSE);
                }
            }
            if ("getContainerLabel".equals(str)) {
                str2 = null;
                personaService = getPersonaService(context);
                if (personaService != null) {
                    try {
                        str2 = personaService.mService.getContainerName(myUserId);
                    } catch (Throwable e2) {
                        Log.e(TAG, "failed to container name", e2);
                    }
                }
                mKnoxInfo.putString("getContainerLabel", str2);
            }
            if ("getContainerAppIcon".equals(str)) {
                bArr = null;
                personaService = getPersonaService(context);
                if (personaService != null) {
                    try {
                        bArr = personaService.mService.getContainerAppIcon(myUserId);
                    } catch (Throwable e22) {
                        Log.e(TAG, "failed to container name", e22);
                    }
                }
                mKnoxInfo.putByteArray("getContainerAppIcon", bArr);
            }
            if ("getActiveUserId".equals(str)) {
                int i = 0;
                personaService = getPersonaService(context);
                if (personaService != null) {
                    try {
                        i = personaService.mService.getFocusedUser();
                    } catch (Throwable e222) {
                        Log.e(TAG, "failed to container name", e222);
                    }
                }
                mKnoxInfo.putInt("getActiveUserId", i);
            }
            if ("getAllPersonaInfo".equals(str)) {
                mKnoxInfo.putInt("getContainerCount", 0);
                personaService = getPersonaService(context);
                if (personaService != null) {
                    try {
                        int[] personaIds = personaService.mService.getPersonaIds();
                        if (!(personaIds == null || personaIds.length == 0)) {
                            mKnoxInfo.putInt("getContainerCount", personaIds.length);
                            for (int i2 = 0; i2 < personaIds.length; i2++) {
                                int i3 = personaIds[i2];
                                bArr = personaService.mService.getContainerAppIcon(i3);
                                str2 = personaService.mService.getContainerName(i3);
                                mKnoxInfo.putInt("getContainerOrder_" + i2, personaService.mService.getContainerOrder(i3));
                                mKnoxInfo.putInt("getContainerId_" + i2, i3);
                                mKnoxInfo.putString("getContainerLabel_" + i2, str2);
                                mKnoxInfo.putByteArray("getContainerAppIcon_" + i2, bArr);
                            }
                        }
                    } catch (Throwable e2222) {
                        Log.e(TAG, "failed to get container info:", e2222);
                        mKnoxInfo.putInt("getContainerCount", 0);
                    }
                }
            }
            if ("isSupportSecureFolder".equals(str)) {
                personaService = getPersonaService(context);
                if (personaService == null) {
                    mKnoxInfo.putString("isSupportSecureFolder", SmartFaceManager.FALSE);
                } else if (personaService.isUserManaged()) {
                    mKnoxInfo.putString("isSupportSecureFolder", SmartFaceManager.TRUE);
                } else {
                    mKnoxInfo.putString("isSupportSecureFolder", SmartFaceManager.FALSE);
                }
            }
        }
        return mKnoxInfo;
    }

    public static PathStrategy getPathStrategy(int i) {
        PathStrategy pathStrategy = (PathStrategy) pathstrategy.get(i);
        if (pathStrategy != null) {
            return pathStrategy;
        }
        pathStrategy = new PathStrategy("bbcfileprovider", i);
        pathstrategy.put(i, pathStrategy);
        return pathStrategy;
    }

    public static String getPersonaName(Context context, int i) {
        if (i >= 100) {
            UserInfo userInfo = ((UserManager) context.getSystemService("user")).getUserInfo(i);
            if (userInfo == null) {
                Log.e(TAG, "User doesn't exist. : " + i);
            } else if (isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_7_0)) {
                return userInfo.name;
            } else {
                String packageName = context.getPackageName();
                Log.e(TAG, "getPersonaName called from " + packageName);
                return (packageName.equals(KNOX_SETTINGS_PACKAGE_NAME) || packageName.equals("com.sec.knox.switcher") || packageName.equals("com.samsung.knox.kss") || packageName.equals(FOLDERCONTAINER_PKG_NAME) || packageName.equals(SShareConstants.SCREEN_MIRRORING_PKG)) ? "KNOX" : userInfo.name;
            }
        }
        return null;
    }

    private static SemPersonaManager getPersonaService(Context context) {
        if (context != null) {
            SemPersonaManager semPersonaManager = (SemPersonaManager) context.getSystemService("persona");
            return (semPersonaManager == null || semPersonaManager.mService == null) ? null : semPersonaManager;
        }
    }

    public static SemRemoteContentManager getRCPManager(Context context) {
        synchronized (SemPersonaManager.class) {
            if (rcpManager == null) {
                rcpManager = (SemRemoteContentManager) context.getSystemService("rcp");
            }
        }
        return rcpManager;
    }

    public static int getSecureFolderId(Context context) {
        SemPersonaManager personaService = getPersonaService(context);
        if (personaService == null) {
            Log.e(TAG, "Failed to get SemPersonaManagerService in getSecureFolderId");
            return ERROR_NO_PERSONA_SERVICE;
        }
        try {
            return personaService.mService.getSecureFolderId();
        } catch (Throwable e) {
            Log.e(TAG, "failed to getSecureFolderId", e);
            return ERROR_INVAILD_CONTAINER_ID;
        }
    }

    public static String getSecureFolderName(Context context) {
        SemPersonaManager personaService = getPersonaService(context);
        if (personaService == null) {
            Log.e(TAG, "Failed to get SemPersonaManagerService in getSecureFolderId");
            return "";
        }
        Iterable<SemPersonaInfo> personas = personaService.getPersonas();
        if (personas == null || personas.size() == 0) {
            return "";
        }
        for (SemPersonaInfo type : personas) {
            String type2 = type.getType();
            if (type2.equals(SECURE_FOLDER_NAME)) {
                return type2;
            }
        }
        return "";
    }

    public static boolean isBBCContainer(int i) {
        return i >= MIN_BBC_ID && i <= MAX_BBC_ID;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isCACEnabled(int r7) {
        /*
        r6 = 0;
        r2 = 0;
        r4 = mBTSecureManagerSync;
        monitor-enter(r4);
        r3 = 100;
        if (r7 < r3) goto L_0x0019;
    L_0x0009:
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r7 > r3) goto L_0x0019;
    L_0x000d:
        r0 = android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper.getService();	 Catch:{ RemoteException -> 0x001b }
        if (r0 == 0) goto L_0x0017;
    L_0x0013:
        r2 = r0.isBTSecureAccessAllowedAsUser(r7);	 Catch:{ RemoteException -> 0x001b }
    L_0x0017:
        monitor-exit(r4);
        return r6;
    L_0x0019:
        monitor-exit(r4);
        return r6;
    L_0x001b:
        r1 = move-exception;
        r3 = TAG;	 Catch:{ all -> 0x0026 }
        r5 = "failed to isCACEnabled";
        android.util.Log.w(r3, r5, r1);	 Catch:{ all -> 0x0026 }
        monitor-exit(r4);
        return r6;
    L_0x0026:
        r3 = move-exception;
        monitor-exit(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.knox.SemPersonaManager.isCACEnabled(int):boolean");
    }

    public static boolean isKioskModeEnabled(Context context) {
        String str = SystemProperties.get("sys.knox.exists", "");
        if (str != null && str.length() > 0) {
            return str.startsWith("5");
        }
        if (context == null) {
            return false;
        }
        SemPersonaManager semPersonaManager = (SemPersonaManager) context.getSystemService("persona");
        if (semPersonaManager == null || semPersonaManager.mService == null) {
            return false;
        }
        try {
            return semPersonaManager.mService.isKioskContainerExistOnDevice();
        } catch (Throwable e) {
            Log.w(TAG, "failed to isKioskContainerExistOnDevice", e);
            return false;
        }
    }

    public static boolean isKnoxAppRunning(Context context) {
        SemPersonaManager semPersonaManager = (SemPersonaManager) context.getSystemService("persona");
        if (semPersonaManager == null) {
            return false;
        }
        int focusedUser = semPersonaManager.getFocusedUser();
        boolean z = focusedUser >= 100 && focusedUser <= 200;
        Log.d(TAG, "isKnoxAppRunning userId = " + focusedUser + " result = " + z);
        return z;
    }

    public static boolean isKnoxId(int i) {
        return i >= 100 && i <= 200;
    }

    public static boolean isKnoxMultiwindowsSupported() {
        return isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_6_0);
    }

    public static boolean isKnoxSupported() {
        boolean z = false;
        try {
            KnoxContainerVersion knoxContainerVersion = getKnoxContainerVersion();
            if (knoxContainerVersion != null) {
                z = knoxContainerVersion.getVersionNumber() > 0;
                Log.d(TAG, "Supported Knox Version : " + knoxContainerVersion.toString());
            } else {
                Log.e(TAG, "Unexpected... failed to get knox version...");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return z;
    }

    public static boolean isKnoxVersionSupported(int i) {
        if (i > 0) {
            KnoxContainerVersion knoxContainerVersion = getKnoxContainerVersion();
            if (knoxContainerVersion != null && knoxContainerVersion.getVersionNumber() >= i) {
                return true;
            }
        }
        return false;
    }

    public static boolean isKnoxVersionSupported(KnoxContainerVersion knoxContainerVersion) {
        if (knoxContainerVersion != null) {
            Enum knoxContainerVersion2 = getKnoxContainerVersion();
            if (knoxContainerVersion2 != null && knoxContainerVersion2.compareTo(knoxContainerVersion) >= 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean isMyknoxExist(Context context) {
        try {
            int myknoxId = getPersonaService(context).mService.getMyknoxId();
            if (myknoxId != 0 && isKnoxId(myknoxId)) {
                return true;
            }
        } catch (Throwable e) {
            Log.e(TAG, "failed to isMyknoxExist", e);
        }
        return false;
    }

    public static boolean isPkgAllowedToListenKnoxNoti(Context context, String str) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 0);
            if (applicationInfo == null || (applicationInfo.flags & 1) != 0) {
                return applicationInfo != null && (applicationInfo.flags & 1) == 1;
            } else {
                String str2 = "com.samsung.permission.READ_KNOX_NOTIFICATION";
                if (context.getPackageManager().checkPermission("com.samsung.permission.READ_KNOX_NOTIFICATION", str) == 0) {
                    return true;
                }
            }
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isPossibleAddToPersonal(String str) {
        if (str == null || "".equalsIgnoreCase(str) || "null".equalsIgnoreCase(str)) {
            return false;
        }
        for (String equalsIgnoreCase : SHORTCUT_FILTER) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSecureFolderAppRunning(Context context) {
        SemPersonaManager semPersonaManager = (SemPersonaManager) context.getSystemService("persona");
        if (semPersonaManager == null) {
            return false;
        }
        int focusedUser = semPersonaManager.getFocusedUser();
        boolean z = focusedUser >= 150 && focusedUser <= MAX_SECURE_FOLDER_ID;
        Log.d(TAG, "isSecureFolderAppRunning userId = " + focusedUser + " result = " + z);
        return z;
    }

    public static boolean isSecureFolderExist(Context context) {
        int secureFolderId = getSecureFolderId(context);
        return secureFolderId >= 150 && secureFolderId <= MAX_SECURE_FOLDER_ID;
    }

    public static boolean isSecureFolderId() {
        int callingUserId = UserHandle.getCallingUserId();
        return callingUserId >= 150 && callingUserId <= MAX_SECURE_FOLDER_ID;
    }

    public static boolean isSecureFolderId(int i) {
        return i >= 150 && i <= MAX_SECURE_FOLDER_ID;
    }

    private boolean isSecureFolderMetaDataEnabled() {
        boolean z = false;
        try {
            PackageItemInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo("com.samsung.knox.securefolder", 128);
            if (applicationInfo == null) {
                return false;
            }
            BaseBundle baseBundle = applicationInfo.metaData;
            if (baseBundle != null) {
                z = baseBundle.getBoolean("com.samsung.knox.securefolder.enable", false);
            }
            return z;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isSupported(Context context, String str, String str2, int i) {
        if (str2 != null && str2.startsWith("sec_container_1")) {
            return false;
        }
        if ("2.0".equals(getKnoxInfo().getString(SemSoundAssistantManager.VERSION))) {
            synchronized (SemPersonaManager.class) {
                if (personaManager == null) {
                    personaManager = (SemPersonaManager) context.getSystemService("persona");
                }
            }
            synchronized (PersonaPolicyManager.class) {
                if (personaPolicyMgr == null) {
                    personaPolicyMgr = (PersonaPolicyManager) personaManager.getPersonaService(PERSONA_POLICY_SERVICE);
                }
            }
            if (i >= 100) {
                if (SemPersonaInfo.PERSONA_TYPE_DEFAULT.equals(str)) {
                    return false;
                }
                if (str.equals("cameraMode")) {
                    return personaPolicyMgr.getCameraModeChangeEnabled(i);
                }
                if (str.equals("dlnaDataTransfer")) {
                    return personaPolicyMgr.getAllowDLNADataTransfer(i);
                }
                if (str.equals("print")) {
                    return personaPolicyMgr.getAllowPrint(i);
                }
                if (str.equals("allShare")) {
                    return personaPolicyMgr.getAllowAllShare(i);
                }
                if (str.equals("gearSupport")) {
                    return personaPolicyMgr.getGearSupportEnabled(i);
                }
                if (str.equals("penWindow")) {
                    return personaPolicyMgr.getPenWindowEnabled(i);
                }
                if (str.equals("airCommand")) {
                    return personaPolicyMgr.getAirCommandEnabled(i);
                }
                if (str.equals("importFiles")) {
                    return personaPolicyMgr.getAllowImportFiles(i);
                }
                if (str.equals("exportFiles")) {
                    return personaPolicyMgr.getAllowExportFiles(i);
                }
                if (str.equals("move-file-to-container")) {
                    return personaPolicyMgr.isMoveFilesToContainerAllowed(i);
                }
                if (str.equals("move-file-to-owner")) {
                    return personaPolicyMgr.isMoveFilesToOwnerAllowed(i);
                }
                if (str.equals("exportAndDeleteFiles")) {
                    return personaPolicyMgr.getAllowExportAndDeleteFiles(i);
                }
                if (str.equals("print")) {
                    return personaPolicyMgr.getAllowPrint(i);
                }
                if (str.equals("contacts-import-export")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String readOMCSalesCode() {
        String str = "";
        try {
            str = SystemProperties.get("persist.omc.sales_code");
            if ("".equals(str) || str == null) {
                str = SystemProperties.get("ro.csc.sales_code");
                if ("".equals(str) || str == null) {
                    str = SystemProperties.get("ril.sales_code");
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "readOMCSalesCode failed");
        }
        return str == null ? "" : str;
    }

    private static void setMoveToKnoxInfo(Context context, Bundle bundle, int i) {
        bundle.putString("isSupportMoveTo", SmartFaceManager.FALSE);
        String packageName;
        if (i == 0) {
            BaseBundle bundle2 = new Bundle();
            int[] iArr = null;
            String[] strArr = null;
            bundle2.putString(SemEmergencyConstants.ACTION, "RequestProxy");
            bundle2.putString("cmd", "queryPersonaInfos");
            BaseBundle exchangeData = exchangeData(context, bundle2);
            if (exchangeData != null) {
                iArr = exchangeData.getIntArray("personaIds");
                String[] stringArray = exchangeData.getStringArray("personaTypes");
                strArr = exchangeData.getStringArray("personaNames");
                mKnoxInfo.putIntArray("personaIds", iArr);
                mKnoxInfo.putStringArray("personaTypes", stringArray);
                mKnoxInfo.putStringArray("personaNames", strArr);
            }
            try {
                Serializable linkedHashMap = new LinkedHashMap();
                if (iArr != null && iArr.length > 0) {
                    for (int i2 = 0; i2 < iArr.length; i2++) {
                        linkedHashMap.put(Integer.valueOf(iArr[i2]), strArr[i2]);
                    }
                    packageName = context.getPackageName();
                    if (!("com.sec.android.app.voicenote".equalsIgnoreCase(packageName) || "com.samsung.android.snote".equalsIgnoreCase(packageName))) {
                        bundle.putString("isSupportMoveTo", SmartFaceManager.TRUE);
                    }
                }
                bundle.putSerializable("KnoxIdNamePair", linkedHashMap);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (i >= 100) {
            try {
                packageName = context.getPackageName();
                if (!"com.sec.android.app.voicenote".equalsIgnoreCase(packageName) && !"com.samsung.android.snote".equalsIgnoreCase(packageName)) {
                    bundle.putString("isSupportMoveTo", SmartFaceManager.TRUE);
                }
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
    }

    public void addAppForPersona(AppType appType, String str, int i) {
        if (this.mService != null) {
            try {
                this.mService.addAppForPersona(appType.getName(), str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to addAppForPersona", e);
            }
        }
    }

    public Bitmap addLockOnImage(Bitmap bitmap) {
        if (this.mService != null) {
            try {
                return this.mService.addLockOnImage(bitmap);
            } catch (Throwable e) {
                Log.d(TAG, "Could not get addLockOnImage , inside SemPersonaManager with exception:", e);
            }
        }
        return null;
    }

    public void addPackageToInstallWhiteList(String str, int i) {
        if (this.mService != null) {
            try {
                this.mService.addPackageToInstallWhiteList(str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to addPackageToInstallWhiteList", e);
            }
        }
    }

    public void addPackageToNonSecureAppList(String str) {
        if (this.mService != null) {
            try {
                this.mService.addPackageToNonSecureAppList(str);
            } catch (Throwable e) {
                Log.w(TAG, "failed to addPackageToNonSecureAppList from PMS", e);
            }
        }
    }

    public boolean adminLockPersona(int i, String str) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.adminLockPersona(i, str);
            } catch (Throwable e) {
                Log.w(TAG, "failed to execute adminLockPersona", e);
            }
        }
        return z;
    }

    public boolean adminUnLockPersona(int i) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.adminUnLockPersona(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to execute adminUnLockPersona", e);
            }
        }
        return z;
    }

    public boolean broadcastIntentThroughPersona(Intent intent) {
        if (this.mService != null) {
            try {
                return this.mService.broadcastIntentThroughPersona(intent);
            } catch (Throwable e) {
                Log.e(TAG, "Could not broadcastIntentThroughPersona", e);
            }
        }
        return false;
    }

    public boolean canAccess(String str, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "canAccess for type " + str + " personaId " + i);
                return this.mService.canAccess(str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to get access permission", e);
            }
        }
        return false;
    }

    public void clearAppListForPersona(AppType appType, int i) {
        if (this.mService != null) {
            try {
                this.mService.clearAppListForPersona(appType.getName(), i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to clearAppListForPersona", e);
            }
        }
    }

    public void clearNonSecureAppList() {
        if (this.mService != null) {
            try {
                this.mService.clearNonSecureAppList();
            } catch (Throwable e) {
                Log.w(TAG, "failed to clearNonSecureAppList from PMS", e);
            }
        }
    }

    public void convertContainerType(int i, int i2) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "convertContainerType for " + i + ":" + i2);
                this.mService.convertContainerType(i, i2);
            } catch (Throwable e) {
                Log.w(TAG, "failed to convertContainerType", e);
            }
        }
    }

    public int copyFileBNR(int i, String str, int i2, String str2) {
        try {
            Log.d(TAG, "SemPersonaManager.copyFileBNR() srcContainerId=" + i + "; srcFilePath=" + str + "; destContainerId=" + i2 + "; destFilePath=" + str2);
            return ((SemRemoteContentManager) this.mContext.getSystemService("rcp")).copyFile(i, str, i2, str2);
        } catch (Throwable e) {
            Log.w(TAG, "SemPersonaManager.copyFileBNR(), inside persona manager with exception:", e);
            return -1;
        }
    }

    public int createPersona(String str, String str2, long j, String str3, String str4, Uri uri, String str5, int i) {
        if (this.mService != null) {
            try {
                return this.mService.createPersona(str, str2, j, str3, str4, uri, str5, i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not create a user", e);
            }
        }
        return -1;
    }

    public boolean deleteFile(String str, int i) {
        try {
            Log.d(TAG, "SemPersonaManager.deleteFile() ContainerId=" + i + "; FilePath=" + str);
            return ((SemRemoteContentManager) this.mContext.getSystemService("rcp")).deleteFile(str, i);
        } catch (Throwable e) {
            Log.w(TAG, "SemPersonaManager.deleteFile(), inside persona manager with exception:", e);
            return false;
        }
    }

    public boolean disablePersonaKeyGuard(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "disablePersonaKeyGuard  persona " + i);
                return this.mService.disablePersonaKeyGuard(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to disablePersonaKeyGuard", e);
            }
        }
        return false;
    }

    public void doWhenUnlock(int i, SemUnlockAction semUnlockAction) {
        if (this.mService != null) {
            try {
                this.mService.doWhenUnlock(i, semUnlockAction.getChild());
            } catch (Throwable e) {
                Log.w(TAG, "Could not showKeyguard", e);
            }
        }
    }

    public boolean enablePersonaKeyGuard(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "enablePersonaKeyGuard  persona " + i);
                return this.mService.enablePersonaKeyGuard(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to enablePersonaKeyGuard", e);
            }
        }
        return false;
    }

    public boolean exists(int i) {
        if (this.mService != null) {
            try {
                String str = SystemProperties.get("sys.knox.exists", "");
                return i < 100 ? false : (str == null || str.length() <= 0) ? this.mService.exists(i) : containerExists(str, i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get user info", e);
            }
        }
        return false;
    }

    public void forceRollup(Bundle bundle) {
        Log.i(TAG, "about to force switch to owner!");
    }

    public int getAdminUidForPersona(int i) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in SemPersonaManager, getAdminUidForPersona()");
                return this.mService.getAdminUidForPersona(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to get getAdminUidForPersona id", e);
            }
        }
        return -1;
    }

    public HashMap<Integer, String> getAllKnoxNamesAndIds(boolean z) {
        HashMap<Integer, String> hashMap = new HashMap();
        UserManager userManager = (UserManager) this.mContext.getSystemService("user");
        Iterable<SemPersonaInfo> personas = getPersonas(z);
        if (personas != null) {
            for (SemPersonaInfo semPersonaInfo : personas) {
                hashMap.put(Integer.valueOf(semPersonaInfo.id), userManager.getUserInfo(semPersonaInfo.id).name);
            }
        }
        return hashMap;
    }

    public List<String> getAppListForPersona(AppType appType, int i) {
        if (this.mService != null) {
            try {
                return this.mService.getAppListForPersona(appType.getName(), i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getAppListForPersona", e);
            }
        }
        return null;
    }

    public List<String> getAppPackageNamesAllWhiteLists(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getAppPackageNamesAllWhiteLists(i);
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return null;
    }

    public CustomCursor getCallerInfo(String str) {
        Log.d(TAG, "PM.getCallerInfo()");
        SemRemoteContentManager semRemoteContentManager = (SemRemoteContentManager) this.mContext.getSystemService("rcp");
        if (semRemoteContentManager == null) {
            Log.e(TAG, "Received mRCPGlobalContactsDir as null from bridge manager: rcpm == null");
            return null;
        }
        IRCPGlobalContactsDir rCPProxy = semRemoteContentManager.getRCPProxy();
        if (rCPProxy == null) {
            Log.e(TAG, "Received result as null from bridge manager for getCallerInfo: mRCPGlobalContactsDir == null");
            return null;
        }
        try {
            CustomCursor callerInfo = rCPProxy.getCallerInfo(str);
            Log.d(TAG, "PM.getCallerInfo(): Received result: " + callerInfo);
            return callerInfo;
        } catch (Throwable e) {
            Log.e(TAG, "Couldn't complete call to getCallerInfo , inside SemPersonaManager with exception:", e);
            return null;
        }
    }

    public List<String> getContainerHideUsageStatsApps() {
        if (this.mService != null) {
            try {
                return this.mService.getContainerHideUsageStatsApps();
            } catch (Throwable e) {
                Log.w(TAG, "Failed to get usage stats app hide list ", e);
            }
        }
        return Collections.EMPTY_LIST;
    }

    public byte[] getContainerIcon(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getContainerAppIcon(i);
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return null;
    }

    public String getContainerName(int i, Context context) {
        if (this.mService != null) {
            try {
                if (isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_7_0)) {
                    return this.mService.getContainerName(i);
                }
                String packageName = context.getPackageName();
                Log.e(TAG, "getPersonaName called from " + packageName);
                if (packageName.equals(KNOX_SETTINGS_PACKAGE_NAME) || packageName.equals("com.sec.knox.switcher") || packageName.equals("com.samsung.knox.kss") || packageName.equals(FOLDERCONTAINER_PKG_NAME) || packageName.equals(SShareConstants.SCREEN_MIRRORING_PKG)) {
                    return "KNOX";
                }
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return null;
    }

    public Drawable getCustomBadgedIconifRequired(Context context, Drawable drawable, String str, UserHandle userHandle) {
        return getCustomBadgedIconifRequired(context, drawable, str, userHandle, 4);
    }

    public Drawable getCustomBadgedIconifRequired(Context context, Drawable drawable, String str, UserHandle userHandle, int i) {
        int identifier = userHandle.getIdentifier();
        if (!isKnoxId(identifier)) {
            return drawable;
        }
        if (isKioskContainerExistOnDevice() && isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_0)) {
            return drawable;
        }
        try {
            return this.mService.getCustomBadgedResourceIdIconifRequired(str, identifier) == 0 ? drawable : drawable;
        } catch (RemoteException e) {
            return drawable;
        }
    }

    public String getDefaultQuickSettings() {
        if (this.mService != null) {
            try {
                return this.mService.getDefaultQuickSettings();
            } catch (Throwable e) {
                Log.w(TAG, "failed to getDefaultQuickSettings from PMS", e);
            }
        }
        return "";
    }

    public List<String> getDisabledHomeLaunchers(int i, boolean z) {
        if (this.mService != null) {
            try {
                return this.mService.getDisabledHomeLaunchers(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getDisabledHomeLaunchers", e);
            }
        }
        return null;
    }

    public byte[] getECBadge(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getECBadge(i);
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return null;
    }

    public byte[] getECIcon(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getECIcon(i);
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return null;
    }

    public String getECName(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getECName(i);
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return null;
    }

    public String getFidoRpContext(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getFidoRpContext");
                return this.mService.getFidoRpContext(i);
            } catch (Throwable e) {
                Log.w(TAG, "can't read getFidoRpContext from PMS", e);
            }
        }
        return "";
    }

    public int getFingerCount() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getFingerCount");
                return this.mService.getFingerCount(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getFingerCount from PMS", e);
            }
        }
        return 0;
    }

    public List<String> getFingerprintHash(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getFingerprintHash");
                return this.mService.getFingerprintHash(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getFingerprintHash from PMS", e);
            }
        }
        return null;
    }

    public int[] getFingerprintIndex(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getFingerprintIndex");
                return this.mService.getFingerprintIndex(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getFingerprintIndex from PMS", e);
            }
        }
        return null;
    }

    public int getFocusedKnoxId() {
        int focusedUser = getFocusedUser();
        return isKnoxId(focusedUser) ? focusedUser : 0;
    }

    public int getFocusedUser() {
        if (this.mService != null) {
            try {
                return this.mService.getFocusedUser();
            } catch (Throwable e) {
                Log.w(TAG, "getFocusedUser error", e);
            }
        }
        return 0;
    }

    public int getForegroundUser() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getForegroundUser");
                return this.mService.getForegroundUser();
            } catch (Throwable e) {
                Log.w(TAG, "getForegroundUser error", e);
            }
        }
        return 0;
    }

    public boolean getIsAdminLockedJustBefore() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsAdminLockedJustBefore");
                return this.mService.getIsAdminLockedJustBefore(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsAdminLockedJustBefore from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsFingerAsSupplement() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerAsSupplement");
                return this.mService.getIsFingerAsSupplement(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsFingerAsSupplement from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsFingerIdentifyFailed() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerIdentifyFailed");
                return this.mService.getIsFingerIdentifyFailed(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsFingerIdentifyFailed from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsFingerReset() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerReset");
                return this.mService.getIsFingerReset(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsFingerReset from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsFingerTimeout() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerTimeout");
                return this.mService.getIsFingerTimeout(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsFingerTimeout from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsIrisReset() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsBioReset");
                return this.mService.getIsIrisReset(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsBioReset from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsIrisTimeout() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsBioTimeout");
                return this.mService.getIsIrisTimeout(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsBioTimeout from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsQuickAccessUIEnabled() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsQuickAccessUIEnabled");
                return this.mService.getIsQuickAccessUIEnabled(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsQuickAccessUIEnabled from PMS", e);
            }
        }
        return false;
    }

    public boolean getIsUnlockedAfterTurnOn() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsUnlockedAfterTurnOn");
                return this.mService.getIsUnlockedAfterTurnOn(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getIsUnlockedAfterTurnOn from PMS", e);
            }
        }
        return false;
    }

    public boolean getKeyguardShowState() {
        Iterable<SemPersonaInfo> personas = getPersonas();
        if (personas == null || personas.size() == 0) {
            return false;
        }
        for (SemPersonaInfo semPersonaInfo : personas) {
            if (getKeyguardShowState(semPersonaInfo.id)) {
                return true;
            }
        }
        return false;
    }

    public boolean getKeyguardShowState(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getKeyguardShowState(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getKeyguardShowState", e);
            }
        }
        return false;
    }

    public int getKioskId() {
        int[] personaIds = getPersonaIds();
        return (!isKioskModeEnabled(this.mContext) || personaIds == null || personaIds.length <= 0) ? -1 : personaIds[0];
    }

    public Bitmap getKnoxIconChanged(String str, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxIconChanged");
                return this.mService.getKnoxIconChanged(str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getKnoxIconChanged from PMS", e);
            }
        }
        return null;
    }

    public Bitmap getKnoxIconChangedAsUser(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxIconChangedAsUser");
                return this.mService.getKnoxIconChangedAsUser(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getKnoxIconChangedAsUser from PMS", e);
            }
        }
        return null;
    }

    public int getKnoxId(int i, boolean z) {
        Iterable<SemPersonaInfo> personas = getPersonas(z);
        if (personas == null) {
            return -1;
        }
        if (i == 1) {
            for (SemPersonaInfo semPersonaInfo : personas) {
                if (!semPersonaInfo.getType().startsWith("centrify-pb-")) {
                    if (semPersonaInfo.getType().startsWith("myknox-type")) {
                    }
                }
                return semPersonaInfo.id;
            }
        }
        if (i == 2) {
            for (SemPersonaInfo semPersonaInfo2 : personas) {
                if (semPersonaInfo2.isSecureFolder) {
                    return semPersonaInfo2.id;
                }
            }
        }
        return -1;
    }

    public List<Integer> getKnoxIds(boolean z) {
        List<Integer> arrayList = new ArrayList();
        if (this.mService != null) {
            try {
                for (SemPersonaInfo semPersonaInfo : this.mService.getPersonas(z)) {
                    arrayList.add(Integer.valueOf(semPersonaInfo.id));
                }
            } catch (Throwable e) {
                Log.w(TAG, "Could not get persona info", e);
            }
        }
        return arrayList;
    }

    public String getKnoxNameChanged(String str, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxNameChanged");
                return this.mService.getKnoxNameChanged(str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getKnoxNameChanged from PMS", e);
            }
        }
        return "";
    }

    public String getKnoxNameChangedAsUser(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxNameChangedAsUser");
                return this.mService.getKnoxNameChangedAsUser(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getKnoxNameChangedAsUser from PMS", e);
            }
        }
        return "";
    }

    public int getKnoxSecurityTimeout() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxSecurityTimeout");
                return this.mService.getKnoxSecurityTimeout(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getKnoxSecurityTimeout from PMS. return default value", e);
            }
        }
        return SemPersonaInfo.KNOX_SECURITY_TIMEOUT_DEFAULT;
    }

    public List<UserHandle> getKnoxUserHandles() {
        List<UserHandle> arrayList = new ArrayList();
        int[] personaIds = getPersonaIds();
        if (personaIds != null) {
            for (int userHandle : personaIds) {
                arrayList.add(new UserHandle(userHandle));
            }
        }
        return arrayList;
    }

    public long getLastKeyguardUnlockTime() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getLastKeyguadUnlockTime");
                return this.mService.getLastKeyguardUnlockTime(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read getLastKeyguardUnlockTime from PMS", e);
            }
        }
        return -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<android.os.Bundle> getMoveToKnoxMenuList(android.content.Context r14) {
        /*
        r13 = this;
        r9 = new java.util.ArrayList;
        r9.<init>();
        r3 = 0;
        r10 = 230; // 0xe6 float:3.22E-43 double:1.136E-321;
        r10 = isKnoxVersionSupported(r10);
        if (r10 != 0) goto L_0x000f;
    L_0x000e:
        return r9;
    L_0x000f:
        r10 = r14.getContentResolver();
        r11 = "emergency_mode";
        r12 = 0;
        r10 = android.provider.Settings.System.getInt(r10, r11, r12);
        r11 = 1;
        if (r10 != r11) goto L_0x001f;
    L_0x001e:
        return r9;
    L_0x001f:
        r10 = "persona";
        r6 = r14.getSystemService(r10);
        r6 = (com.samsung.android.knox.SemPersonaManager) r6;
        r7 = r6.getPersonaIds();
        if (r7 == 0) goto L_0x0031;
    L_0x002e:
        r10 = r7.length;
        if (r10 != 0) goto L_0x0032;
    L_0x0031:
        return r9;
    L_0x0032:
        r10 = r7.length;
        if (r10 <= 0) goto L_0x017d;
    L_0x0035:
        r4 = 0;
        r10 = r14.getUserId();
        if (r10 != 0) goto L_0x0127;
    L_0x003c:
        r10 = isKioskModeEnabled(r14);
        if (r10 == 0) goto L_0x0043;
    L_0x0042:
        return r9;
    L_0x0043:
        r2 = 0;
    L_0x0044:
        r10 = r7.length;
        if (r2 >= r10) goto L_0x017d;
    L_0x0047:
        r5 = new android.os.Bundle;
        r5.<init>();
        r10 = "move-file-to-container";
        r11 = r7[r2];
        r12 = 0;
        r10 = isSupported(r14, r10, r12, r11);
        if (r10 == 0) goto L_0x00bd;
    L_0x0058:
        r3 = "true";
    L_0x005b:
        r10 = r7[r2];
        r10 = isSecureFolderId(r10);
        if (r10 == 0) goto L_0x00c3;
    L_0x0063:
        r8 = r14.getPackageManager();
        r0 = new android.content.ComponentName;
        r10 = "com.sec.knox.switcher";
        r11 = "com.sec.knox.switcher.SwitchSfActivity";
        r0.<init>(r10, r11);
        r1 = new android.content.ComponentName;
        r10 = "com.samsung.knox.securefolder";
        r11 = "com.samsung.knox.securefolder.switcher.SwitchSfActivity";
        r1.<init>(r10, r11);
        r10 = r8.getComponentEnabledSetting(r0);
        r11 = 1;
        if (r10 == r11) goto L_0x008b;
    L_0x0084:
        r10 = r8.getComponentEnabledSetting(r1);
        r11 = 1;
        if (r10 != r11) goto L_0x00c1;
    L_0x008b:
        r10 = TAG;
        r11 = "is secure folder";
        android.util.Log.d(r10, r11);
        r10 = "com.sec.knox.moveto.name";
        r11 = r7[r2];
        r11 = r6.getContainerName(r11, r14);
        r5.putString(r10, r11);
        r10 = "com.sec.knox.moveto.containerType";
        r11 = 1002; // 0x3ea float:1.404E-42 double:4.95E-321;
        r5.putInt(r10, r11);
        r10 = "com.sec.knox.moveto.containerId";
        r11 = r7[r2];
        r5.putInt(r10, r11);
    L_0x00af:
        if (r5 == 0) goto L_0x00ba;
    L_0x00b1:
        r10 = "com.sec.knox.moveto.isSupportMoveTo";
        r5.putString(r10, r3);
        r9.add(r5);
    L_0x00ba:
        r2 = r2 + 1;
        goto L_0x0044;
    L_0x00bd:
        r3 = "false";
        goto L_0x005b;
    L_0x00c1:
        r5 = 0;
        goto L_0x00af;
    L_0x00c3:
        r10 = r7[r2];
        r10 = r13.isECContainer(r10);
        if (r10 == 0) goto L_0x00f0;
    L_0x00cb:
        r10 = TAG;
        r11 = "is enterprise contianer";
        android.util.Log.d(r10, r11);
        r10 = "com.sec.knox.moveto.name";
        r11 = r7[r2];
        r11 = r13.getECName(r11);
        r5.putString(r10, r11);
        r10 = "com.sec.knox.moveto.containerType";
        r11 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r5.putInt(r10, r11);
        r10 = "com.sec.knox.moveto.containerId";
        r11 = r7[r2];
        r5.putInt(r10, r11);
        goto L_0x00af;
    L_0x00f0:
        r10 = r7[r2];
        r10 = isBBCContainer(r10);
        if (r10 == 0) goto L_0x0101;
    L_0x00f8:
        r10 = TAG;
        r11 = "is BBCContainer";
        android.util.Log.d(r10, r11);
        goto L_0x00ba;
    L_0x0101:
        r10 = TAG;
        r11 = "is knox";
        android.util.Log.d(r10, r11);
        if (r4 != 0) goto L_0x0125;
    L_0x010b:
        r4 = 1;
        r10 = "com.sec.knox.moveto.name";
        r11 = "Knox";
        r5.putString(r10, r11);
        r10 = "com.sec.knox.moveto.containerType";
        r11 = 1001; // 0x3e9 float:1.403E-42 double:4.946E-321;
        r5.putInt(r10, r11);
        r10 = "com.sec.knox.moveto.containerId";
        r11 = -1;
        r5.putInt(r10, r11);
        goto L_0x00af;
    L_0x0125:
        r5 = 0;
        goto L_0x00af;
    L_0x0127:
        r10 = r14.getUserId();
        r10 = isKnoxId(r10);
        if (r10 == 0) goto L_0x017d;
    L_0x0131:
        r10 = isKioskModeEnabled(r14);
        if (r10 != 0) goto L_0x017d;
    L_0x0137:
        r5 = new android.os.Bundle;
        r5.<init>();
        r10 = "com.sec.knox.moveto.name";
        r11 = r14.getUserId();
        r11 = r6.getContainerName(r11, r14);
        r5.putString(r10, r11);
        r10 = r14.getUserId();
        r10 = isSecureFolderId(r10);
        if (r10 == 0) goto L_0x017e;
    L_0x0154:
        r10 = "com.sec.knox.moveto.containerType";
        r11 = 1003; // 0x3eb float:1.406E-42 double:4.955E-321;
        r5.putInt(r10, r11);
    L_0x015c:
        r10 = "com.sec.knox.moveto.containerId";
        r11 = -1;
        r5.putInt(r10, r11);
        r10 = "move-file-to-owner";
        r11 = r14.getUserId();
        r12 = 0;
        r10 = isSupported(r14, r10, r12, r11);
        if (r10 == 0) goto L_0x0187;
    L_0x0171:
        r3 = "true";
    L_0x0174:
        r10 = "com.sec.knox.moveto.isSupportMoveTo";
        r5.putString(r10, r3);
        r9.add(r5);
    L_0x017d:
        return r9;
    L_0x017e:
        r10 = "com.sec.knox.moveto.containerType";
        r11 = 1004; // 0x3ec float:1.407E-42 double:4.96E-321;
        r5.putInt(r10, r11);
        goto L_0x015c;
    L_0x0187:
        r3 = "false";
        goto L_0x0174;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.knox.SemPersonaManager.getMoveToKnoxMenuList(android.content.Context):java.util.ArrayList<android.os.Bundle>");
    }

    public boolean getMoveToKnoxStatus() {
        if (this.mService != null) {
            try {
                return this.mService.getMoveToKnoxStatus();
            } catch (Throwable e) {
                Log.w(TAG, "Could not get move to knox status", e);
            }
        }
        return false;
    }

    public List<String> getNonSecureAppList() {
        if (this.mService != null) {
            try {
                return this.mService.getNonSecureAppList();
            } catch (Throwable e) {
                Log.w(TAG, "failed to getNonSecureAppList from PMS", e);
            }
        }
        return null;
    }

    public int getNormalizedState(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getNormalizedState(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get user info", e);
            }
        }
        return -1;
    }

    public PackageInfo getPackageInfo(String str, int i, int i2) {
        if (this.mService != null) {
            try {
                return this.mService.getPackageInfo(str, i, i2);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getPackageInfo from PMS", e);
            }
        }
        return null;
    }

    public List<String> getPackagesFromInstallWhiteList(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getPackagesFromInstallWhiteList(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to getPackagesFromInstallWhiteList", e);
            }
        }
        return null;
    }

    public int getParentId(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getParentId(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get parent id", e);
            }
        }
        return -1;
    }

    public int getParentUserForCurrentPersona() {
        if (this.mService != null) {
            try {
                return this.mService.getParentUserForCurrentPersona();
            } catch (Throwable e) {
                Log.w(TAG, "Could not get parent of persona", e);
            }
        }
        return -1;
    }

    public String getPasswordHint() {
        String str = null;
        if (this.mService != null) {
            try {
                Log.d(TAG, "getPasswordHint");
                str = this.mService.getPasswordHint();
            } catch (Throwable e) {
                Log.w(TAG, "failed to getPasswordHint", e);
            }
        }
        return str;
    }

    public long getPersonaBackgroundTime(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaBackgroundTime(i);
            } catch (Throwable e) {
                Log.d(TAG, "Could not get getPersonaBackgroundTime , inside SemPersonaManager with exception:", e);
            }
        }
        return -1;
    }

    public Bitmap getPersonaIcon(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaIcon(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get the persona icon ", e);
            }
        }
        return null;
    }

    public String getPersonaIdentification(int i) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in SemPersonaManager, getPersonaIdentification()");
                return this.mService.getPersonaIdentification(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to get getPersonaIdentification id", e);
            }
        }
        return null;
    }

    public int[] getPersonaIds() {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaIds();
            } catch (Throwable e) {
                Log.w(TAG, "failed to getPersonaIds", e);
            }
        }
        return null;
    }

    public SemPersonaInfo getPersonaInfo(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaInfo(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get persona info", e);
            }
        }
        return null;
    }

    public String getPersonaSamsungAccount(int i) {
        try {
            return this.mService.getPersonaSamsungAccount(i);
        } catch (Throwable e) {
            Log.w(TAG, "Could not retrieve persona SamsungAccount", e);
            return "";
        }
    }

    public Object getPersonaService(String str) {
        Log.i(TAG, "getPersonaService() name " + str);
        return (this.mService == null || !str.equals(PERSONA_POLICY_SERVICE)) ? null : new PersonaPolicyManager(this.mContext, Stub.asInterface(ServiceManager.getService(PERSONA_POLICY_SERVICE)));
    }

    public String getPersonaType(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaType(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not retrieve persona Type", e);
            }
        }
        return "";
    }

    public List<SemPersonaInfo> getPersonas() {
        if (this.mService != null) {
            try {
                return this.mService.getPersonas(false);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get persona list", e);
            }
        }
        return null;
    }

    public List<SemPersonaInfo> getPersonas(boolean z) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonas(z);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get persona list", e);
            }
        }
        return null;
    }

    public List<SemPersonaInfo> getPersonasForCreator(int i, boolean z) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonasForCreator(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get the personas for a creator uid ", e);
            }
        }
        return null;
    }

    public List<SemPersonaInfo> getPersonasForUser(int i) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonasForUser(i, true);
            } catch (Throwable e) {
                Log.w(TAG, "Could not get persona list for user", e);
            }
        }
        return null;
    }

    public IRCPInterface getRCPInterface() {
        Log.d(TAG, "in getRCPInterface");
        SemRemoteContentManager semRemoteContentManager = (SemRemoteContentManager) this.mContext.getSystemService("rcp");
        if (semRemoteContentManager != null) {
            IRCPInterface rCPInterface = semRemoteContentManager.getRCPInterface();
            Log.d(TAG, "in getRCPInterface rcpInterface: " + rCPInterface);
            return rCPInterface;
        }
        Log.e(TAG, "Received getRCPInterface as null from bridge manager");
        return null;
    }

    public long getScreenOffTime(int i) {
        if (this.mService != null) {
            try {
                this.mService.getScreenOffTime(i);
            } catch (Throwable e) {
                Log.e(TAG, "failed to screenOffTime" + Log.getStackTraceString(e));
            }
        }
        return 5000;
    }

    public StateManager getStateManager(int i) {
        Log.d(TAG, "getStateManager()");
        return new StateManager(this.mContext, this.mService, i);
    }

    public List<SemPersonaInfo> getUserManagedPersonas(boolean z) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "getUserManagedPersonas() excludeDying is " + z);
                return this.mService.getUserManagedPersonas(z);
            } catch (Throwable e) {
                Log.w(TAG, "failed getUserManagedPersonas()", e);
            }
        }
        return null;
    }

    public boolean handleHomeShow() {
        try {
            Log.d(TAG, "in SemPersonaManager, handleHomeShow()");
            return this.mService.handleHomeShow();
        } catch (Throwable e) {
            Log.w(TAG, "failed to get handleHomeShow ", e);
            return true;
        }
    }

    public void handleNotificationWhenUnlock(int i, PendingIntent pendingIntent, Bundle bundle, String str) {
        if (this.mService != null) {
            try {
                this.mService.handleNotificationWhenUnlock(i, pendingIntent, bundle, str);
            } catch (Throwable e) {
                Log.w(TAG, "Could not call handleNotificationWhenUnlock", e);
            }
        }
    }

    public void hideScrim() {
        if (this.mService != null) {
            try {
                this.mService.hideScrim();
            } catch (Throwable e) {
                Log.w(TAG, "failed to hideScrim()", e);
            }
        }
    }

    public boolean installApplications(int i, List<String> list) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in SemPersonaManager, installDefaultApplications(), calling service API");
                return this.mService.installApplications(i, list);
            } catch (Throwable e) {
                Log.w(TAG, "Could not install default apps into persona with exception:", e);
            }
        }
        return false;
    }

    public boolean isBootCompleted() {
        if (this.mService != null) {
            try {
                return this.mService.isBootCompleted();
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return false;
    }

    public boolean isECContainer(int i) {
        if (this.mService != null) {
            try {
                return this.mService.isECContainer(i);
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return false;
    }

    public boolean isEnabledFingerprintIndex(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "isEnabledFingerprintIndex");
                return this.mService.isEnabledFingerprintIndex(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to isEnabledFingerprintIndex from PMS", e);
            }
        }
        return false;
    }

    public boolean isExternalStorageEnabled(int i) {
        if (this.mService != null) {
            try {
                return this.mService.isExternalStorageEnabled(i);
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return false;
    }

    public boolean isFOTAUpgrade() {
        if (this.mService != null) {
            try {
                return this.mService.isFOTAUpgrade();
            } catch (Throwable e) {
                Log.w(TAG, "Could not get FOTAUpgrade", e);
            }
        }
        return false;
    }

    public boolean isFingerLockscreenActivated() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "isFingerLockscreenActivated, pid : " + UserHandle.myUserId());
                return this.mService.isFingerLockscreenActivated(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read finger activated value from PMS", e);
            }
        }
        return false;
    }

    public boolean isFingerSupplementActivated() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "isFingerSupplementActivated, pid : " + UserHandle.myUserId());
                return this.mService.isFingerSupplementActivated(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read finger activated value from PMS", e);
            }
        }
        return false;
    }

    public boolean isFotaUpgradeVersionChanged() {
        if (this.mService != null) {
            try {
                return this.mService.isFotaUpgradeVersionChanged();
            } catch (Throwable e) {
                Log.w(TAG, "Could not get isFotaUpgradeVersionChanged", e);
            }
        }
        return false;
    }

    public boolean isInstallableAppInContainer(Context context, int i, String str) {
        Object obj = null;
        if (str == null || "".equalsIgnoreCase(str) || "null".equalsIgnoreCase(str)) {
            return false;
        }
        if (!isUserManaged()) {
            return false;
        }
        for (String equalsIgnoreCase : excludedPackages) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return false;
            }
        }
        try {
            if (personaManager == null) {
                personaManager = (SemPersonaManager) context.getSystemService("persona");
            }
            if (personaPolicyMgr == null) {
                personaPolicyMgr = (PersonaPolicyManager) personaManager.getPersonaService(PERSONA_POLICY_SERVICE);
            }
            for (String equalsIgnoreCase2 : personaPolicyMgr.getSecureFolderPolicy("DisallowPackage", i)) {
                if (equalsIgnoreCase2.equalsIgnoreCase(str)) {
                    return false;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        for (String equalsIgnoreCase22 : mdmPackages) {
            if (equalsIgnoreCase22.equalsIgnoreCase(str)) {
                return false;
            }
        }
        for (String equalsIgnoreCase222 : approvedPackages) {
            if (equalsIgnoreCase222.equalsIgnoreCase(str)) {
                obj = 1;
            }
        }
        try {
            if (personaManager == null) {
                personaManager = (SemPersonaManager) context.getSystemService("persona");
            }
            if (personaPolicyMgr == null) {
                personaPolicyMgr = (PersonaPolicyManager) personaManager.getPersonaService(PERSONA_POLICY_SERVICE);
            }
            if (obj == null) {
                for (String equalsIgnoreCase2222 : personaPolicyMgr.getSecureFolderPolicy("AllowPackage", i)) {
                    if (equalsIgnoreCase2222.equalsIgnoreCase(str)) {
                        obj = 1;
                    }
                }
            }
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        if (obj == null) {
            try {
                PackageItemInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
                if (applicationInfo == null) {
                    return false;
                }
                BaseBundle baseBundle = applicationInfo.metaData;
                boolean z = baseBundle != null ? baseBundle.getBoolean("com.samsung.android.multiuser.install_only_owner", false) : false;
                if (baseBundle == null || !z) {
                    Object string = baseBundle != null ? baseBundle.getString(METADATA_VR_APPLICATION_MODE, null) : null;
                    if (string != null && (METADATA_VR_MODE_VR_ONLY.equals(string) || METADATA_VR_MODE_DUAL.equals(string))) {
                        Log.d(TAG, "isVrApp true - " + str);
                        return false;
                    } else if ((applicationInfo.flags & 1) == 1 || (applicationInfo.flags & 128) == 128) {
                        return false;
                    }
                }
                Log.d(TAG, "isOnlyForOwner() true - " + str);
                return false;
            } catch (NameNotFoundException e3) {
                return false;
            }
        }
        if (this.mService != null) {
            try {
                return this.mService.isPossibleAddAppsToContainer(str, i);
            } catch (Throwable e4) {
                Log.d(TAG, "Could not get isPossibleAddAppsToContainer , inside SemPersonaManager with exception:", e4);
            }
        }
        return false;
    }

    public boolean isIrisLockscreenActivated() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "isIrisLockscreenActivated, pid : " + UserHandle.myUserId());
                return this.mService.isIrisLockscreenActivated(UserHandle.myUserId());
            } catch (Throwable e) {
                Log.w(TAG, "can't read iris activated value from PMS", e);
            }
        }
        return false;
    }

    public boolean isKioskContainerExistOnDevice() {
        if (this.mService == null) {
            return false;
        }
        try {
            String str = SystemProperties.get("sys.knox.exists", "");
            String str2 = "5";
            return (str == null || str.length() <= 0) ? this.mService.isKioskContainerExistOnDevice() : str.startsWith("5");
        } catch (Throwable e) {
            Log.w(TAG, "failed to isKioskContainerExistOnDevice", e);
            return false;
        }
    }

    public boolean isKioskModeEnabled(int i) {
        if (this.mService == null) {
            return false;
        }
        try {
            String str = SystemProperties.get("sys.knox.exists", "");
            String str2 = "5";
            Log.d(TAG, "isKioskModeEnabled  persona ");
            return (str == null || str.length() <= 0) ? this.mService.isKioskModeEnabled(i) : str.startsWith("5") ? containerExists(str, i) : false;
        } catch (Throwable e) {
            Log.w(TAG, "failed to isKioskModeEnabled", e);
            return false;
        }
    }

    public boolean isKnoxActivated() {
        boolean z = false;
        if (getPersonas() == null) {
            return false;
        }
        if (getPersonas().size() > 0) {
            z = true;
        }
        return z;
    }

    public boolean isKnoxKeyguardShown() {
        return getKeyguardShowState();
    }

    public boolean isKnoxKeyguardShown(int i) {
        if (this.mService != null) {
            try {
                return this.mService.isKnoxKeyguardShown(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to isKnoxKeyguardShown", e);
            }
        }
        return false;
    }

    public boolean isKnoxMultiWindowExist() {
        if (this.mService != null) {
            try {
                return this.mService.isKnoxMultiWindowExist();
            } catch (Throwable e) {
                Log.d(TAG, "Failed to call Persona service", e);
            }
        }
        return false;
    }

    public boolean isKnoxReachedToMax() {
        if (this.mService == null) {
            return false;
        }
        try {
            Iterable<SemPersonaInfo> personas = this.mService.getPersonas(true);
            int i = 0;
            int i2 = 0;
            if (personas == null || personas.size() <= 0) {
                return false;
            }
            for (SemPersonaInfo semPersonaInfo : personas) {
                i++;
                if (semPersonaInfo.id >= MIN_BBC_ID) {
                    i2++;
                }
            }
            return i - i2 >= 2;
        } catch (Throwable e) {
            Log.d(TAG, "Failed to call isKnoxReachedtoMax", e);
            return false;
        }
    }

    public boolean isLightWeightContainer(int i) {
        SemPersonaInfo personaInfo = getPersonaInfo(i);
        return personaInfo == null ? false : personaInfo.lightWeightContainer;
    }

    public boolean isNFCAllowed(int i) {
        return isNFCAllowed(i, null);
    }

    public boolean isNFCAllowed(int i, Intent intent) {
        if (intent != null) {
            String dataString = intent.getDataString();
            if (dataString != null) {
                for (CharSequence contains : this.NFCblackList) {
                    if (dataString.contains(contains)) {
                        Log.d(TAG, "NFC action is in blacklist. return false");
                        return false;
                    }
                }
            }
        }
        if (this.mService != null) {
            try {
                Log.d(TAG, "isNFCAllowed");
                return this.mService.isNFCAllowed(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to isNFCAllowed from PMS", e);
            }
        }
        return true;
    }

    public boolean isPackageInInstallWhiteList(String str, int i) {
        if (this.mService != null) {
            try {
                return this.mService.isPackageInInstallWhiteList(str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to isPackageInInstallWhiteList", e);
            }
        }
        return false;
    }

    public boolean isPersonaActivated() {
        int[] personaIds = getPersonaIds();
        if (personaIds == null) {
            return false;
        }
        for (int i : personaIds) {
            if (getStateManager(i).inState(SemPersonaState.ACTIVE) || getStateManager(i).inState(SemPersonaState.LOCKED) || getStateManager(i).isAttribute(PersonaAttribute.PASSWORD_CHANGE_REQUEST)) {
                return true;
            }
        }
        return false;
    }

    public boolean isResetPersonaOnRebootEnabled(int i) {
        boolean z = false;
        if (this.mService != null) {
            try {
                Log.d(TAG, "isResetPersonaOnRebootEnabled  persona ");
                z = this.mService.isResetPersonaOnRebootEnabled(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to isResetPersonaOnRebootEnabled", e);
            }
        }
        return z;
    }

    public boolean isSessionExpired(int i) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "isSessionExpired() " + i);
                return this.mService.isSessionExpired(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to execute lockPersona", e);
            }
        }
        return true;
    }

    public boolean isUserManaged() {
        return (Integer.parseInt("2") == 1 || Secure.getInt(this.mContext.getContentResolver(), "shopdemo", 0) == 1 || !isSecureFolderMetaDataEnabled()) ? false : true;
    }

    public boolean launchPersonaHome(int i) {
        if (this.mService != null) {
            try {
                return this.mService.launchPersonaHome(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not switch to profile user", e);
            }
        }
        return false;
    }

    public boolean launchPersonaHome(int i, boolean z) {
        String containerName = getContainerName(UserHandle.getUserId(Binder.getCallingUid()), this.mContext);
        Log.d(TAG, "launchPersonaHome : " + containerName);
        if (containerName == null || containerName.isEmpty()) {
            containerName = "Knox";
        }
        CharSequence format = String.format(this.mContext.getResources().getText(17040964).toString(), new Object[]{containerName});
        if (!z) {
            return false;
        }
        Toast.makeText(this.mContext, format, 1).show();
        return launchPersonaHome(i);
    }

    public void lockPersona(int i) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "lockPersona() " + i);
                this.mService.lockPersona(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to execute lockPersona", e);
            }
        }
    }

    public void markForRemoval(int i, ComponentName componentName) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "markForRemoval() " + i);
                this.mService.markForRemoval(i, componentName);
            } catch (Throwable e) {
                Log.w(TAG, "markForRemoval()", e);
            }
        }
    }

    public boolean mountOldContainer(String str, String str2, String str3, int i, int i2) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "mountOldContainer, pid : " + UserHandle.myUserId());
                return this.mService.mountOldContainer(str, str2, str3, i, i2);
            } catch (Throwable e) {
                Log.w(TAG, "can't mount Knox 1.0 partition from PMS", e);
            }
        }
        return false;
    }

    public boolean needToSkipResetOnReboot() {
        if (this.mService != null) {
            try {
                return this.mService.needToSkipResetOnReboot();
            } catch (Throwable e) {
                Log.w(TAG, "Could not get needToSkipResetOnReboot", e);
            }
        }
        return false;
    }

    public void notifyKeyguardShow(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "notifyKeyguardShow for " + i + " is shown" + z);
                this.mService.notifyKeyguardShow(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "failed to notifyKeyguardShow", e);
            }
        }
    }

    public void onKeyguardBackPressed(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "onKeyguardBackPressed for " + i);
                this.mService.onKeyguardBackPressed(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to onKeyguardBackPressed from PMS", e);
            }
        }
    }

    public void onWakeLockChange(boolean z, int i, int i2, int i3, String str) {
        if (this.mService != null) {
            try {
                this.mService.onWakeLockChange(z, i, i2, i3, str);
            } catch (Throwable e) {
                Log.e(TAG, "failed to onWakeLockChange" + Log.getStackTraceString(e));
            }
        }
    }

    public void refreshTimer(int i) {
        if (this.mService != null) {
            try {
                this.mService.refreshTimer(i);
            } catch (Throwable e) {
                Log.e(TAG, "failed to refreshTimer" + Log.getStackTraceString(e));
            }
        }
    }

    public boolean registerKnoxModeChangeObserver(IKnoxModeChangeObserver iKnoxModeChangeObserver) {
        if (this.mService != null) {
            try {
                return this.mService.registerKnoxModeChangeObserver(iKnoxModeChangeObserver);
            } catch (Throwable e) {
                Log.w(TAG, "Could not registerKnoxModeChangeObserver a callback", e);
            }
        }
        return false;
    }

    public boolean registerSystemPersonaObserver(ISystemPersonaObserver iSystemPersonaObserver) {
        if (this.mService != null) {
            try {
                return this.mService.registerSystemPersonaObserver(iSystemPersonaObserver);
            } catch (Throwable e) {
                Log.w(TAG, "Could not registerSystemPersonaObserver a callback", e);
            }
        }
        return false;
    }

    public boolean registerUser(IPersonaCallback iPersonaCallback) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in SemPersonaManager, registerUser(), calling service API");
                return this.mService.registerUser(iPersonaCallback);
            } catch (Throwable e) {
                Log.w(TAG, "Could not create a user", e);
            }
        }
        return false;
    }

    public void removeAppForPersona(AppType appType, String str, int i) {
        if (this.mService != null) {
            try {
                this.mService.removeAppForPersona(appType.getName(), str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to removeAppForPersona", e);
            }
        }
    }

    public void removeKnoxAppsinFota(int i) {
        if (this.mService != null) {
            try {
                this.mService.removeKnoxAppsinFota(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not removeKnoxAppsinFota", e);
            }
        }
    }

    public void removePackageFromInstallWhiteList(String str, int i) {
        if (this.mService != null) {
            try {
                this.mService.removePackageFromInstallWhiteList(str, i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to removePackageFromInstallWhiteList", e);
            }
        }
    }

    public int removePersona(int i) {
        if (this.mService != null) {
            try {
                return this.mService.removePersona(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not remove Persona", e);
            }
        }
        return ERROR_REMOVE_PERSONA_FAILED;
    }

    public boolean resetPassword(String str) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.resetPassword(str);
            } catch (Throwable e) {
                Log.w(TAG, "failed to resetPassword", e);
            }
        }
        return z;
    }

    public int resetPersona(int i) {
        if (this.mService != null) {
            try {
                return this.mService.resetPersona(i);
            } catch (Throwable e) {
                Log.w(TAG, "Could not reset the persona ", e);
            }
        }
        return 0;
    }

    public boolean resetPersonaOnReboot(int i, boolean z) {
        boolean z2 = false;
        if (this.mService != null) {
            try {
                Log.d(TAG, "resetPersonaOnReboot  persona ");
                z2 = this.mService.resetPersonaOnReboot(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "failed to resetPersonaOnReboot", e);
            }
        }
        return z2;
    }

    public void resetPersonaPassword(int i, String str, int i2) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "resetPersonaPassword for " + i);
                this.mService.resetPersonaPassword(i, str, i2);
            } catch (Throwable e) {
                Log.w(TAG, "failed to resetPersonaPassword from PMS", e);
            }
        }
    }

    public boolean savePasswordInTima(int i, String str) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.savePasswordInTima(i, str);
            } catch (Throwable e) {
                Log.w(TAG, "failed to savePasswordInTima", e);
            }
        }
        return z;
    }

    public void setAccessPermission(String str, int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setAccessPermission for type " + str + " personaId " + i + " canAccess " + z);
                this.mService.setAccessPermission(str, i, z);
            } catch (Throwable e) {
                Log.w(TAG, "failed to setAccessPermission", e);
            }
        }
    }

    public void setBackPressed(int i, boolean z) {
        if (this.mService != null) {
            try {
                this.mService.setBackPressed(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "Could not set back pressed", e);
            }
        }
    }

    public void setComponentEnabledSettingForKnox(ComponentName componentName, int i, int i2, int i3) {
        if (isKnoxId(i3)) {
            try {
                AppGlobals.getPackageManager().setComponentEnabledSetting(componentName, i, i2, i3);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("knoxId is not for knox user.");
    }

    public void setFidoRpContext(int i, String str) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsBioReset");
                this.mService.setFidoRpContext(i, str);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setFidoRpContext from PMS", e);
            }
        }
    }

    public void setFingerCount(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFingerCount");
                this.mService.setFingerCount(UserHandle.myUserId(), i);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setFingerCount from PMS", e);
            }
        }
    }

    public void setFingerprintHash(int i, List<String> list) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFingerprintHash");
                this.mService.setFingerprintHash(i, list);
            } catch (Throwable e) {
                Log.w(TAG, "failed to setFingerprintHash from PMS", e);
            }
        }
    }

    public void setFingerprintIndex(int i, boolean z, int[] iArr) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFingerprintIndex");
                this.mService.setFingerprintIndex(i, z, iArr);
            } catch (Throwable e) {
                Log.w(TAG, "failed to setFingerprintIndex from PMS", e);
            }
        }
    }

    public void setFocusedUser(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFocusedUser");
                this.mService.setFocusedUser(i);
            } catch (Throwable e) {
                Log.w(TAG, "setFocusedUser error", e);
            }
        }
    }

    public void setFsMountState(int i, boolean z) {
        if (this.mService != null) {
            try {
                this.mService.setFsMountState(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "Could not setFsMountState", e);
            }
        }
    }

    public void setIsAdminLockedJustBefore(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsAdminLockedJustBefore");
                this.mService.setIsAdminLockedJustBefore(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsAdminLockedJustBefore from PMS", e);
            }
        }
    }

    public void setIsFingerAsSupplement(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerAsSupplement");
                this.mService.setIsFingerAsSupplement(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsFingerAsSupplement from PMS", e);
            }
        }
    }

    public void setIsFingerIdentifyFailed(boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerIdentifyFailed");
                this.mService.setIsFingerIdentifyFailed(UserHandle.myUserId(), z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsFingerIdentifyFailed from PMS", e);
            }
        }
    }

    public void setIsFingerReset(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerReset");
                this.mService.setIsFingerReset(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsFingerReset from PMS", e);
            }
        }
    }

    public void setIsFingerTimeout(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerTimeout");
                this.mService.setIsFingerTimeout(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsFingerTimeout from PMS", e);
            }
        }
    }

    public void setIsIrisReset(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsBioReset");
                this.mService.setIsIrisReset(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsBioReset from PMS", e);
            }
        }
    }

    public void setIsIrisTimeout(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsBioTimeout");
                this.mService.setIsIrisTimeout(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsBioTimeout from PMS", e);
            }
        }
    }

    public void setIsQuickAccessUIEnabled(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsQuickAccessUIEnabled");
                this.mService.setIsQuickAccessUIEnabled(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsQuickAccessUIEnabled from PMS", e);
            }
        }
    }

    public void setIsUnlockedAfterTurnOn(int i, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsUnlockedAfterTurnOn");
                this.mService.setIsUnlockedAfterTurnOn(i, z);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setIsUnlockedAfterTurnOn from PMS", e);
            }
        }
    }

    public void setKnoxBackupPin(int i, String str) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setKnoxBackupPin");
                this.mService.setKnoxBackupPin(i, str);
            } catch (Throwable e) {
                Log.w(TAG, "failed to verifyKnoxBackupPin from PMS", e);
            }
        }
    }

    public void setKnoxSecurityTimeout(int i) {
        setKnoxSecurityTimeout(UserHandle.myUserId(), i);
    }

    public void setKnoxSecurityTimeout(int i, int i2) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setKnoxSecurityTimeout");
                this.mService.setKnoxSecurityTimeout(i, i2);
            } catch (Throwable e) {
                Log.w(TAG, "failed to setKnoxSecurityTimeout from PMS", e);
            }
        }
    }

    public void setLastKeyguardUnlockTime(int i, long j) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setLastKeyguardUnlockTime");
                this.mService.setLastKeyguardUnlockTime(i, j);
            } catch (Throwable e) {
                Log.w(TAG, "can't read setLastKeyguardUnlockTime from PMS", e);
            }
        }
    }

    public void setMaximumScreenOffTimeoutFromDeviceAdmin(long j, int i) {
        if (this.mService != null) {
            try {
                this.mService.setMaximumScreenOffTimeoutFromDeviceAdmin(j, i);
            } catch (Throwable e) {
                Log.e(TAG, "failed to setMaximumScreenOffTimeoutFromDeviceAdmin" + Log.getStackTraceString(e));
            }
        }
    }

    public void setMoveToKnoxStatus(boolean z) {
        if (this.mService != null) {
            try {
                this.mService.setMoveToKnoxStatus(z);
            } catch (Throwable e) {
                Log.w(TAG, "Could not set move to knox status", e);
            }
        }
    }

    public void setPersonaIcon(int i, Bitmap bitmap) {
        if (this.mService != null) {
            try {
                this.mService.setPersonaIcon(i, bitmap);
            } catch (Throwable e) {
                Log.w(TAG, "Could not set the persona icon ", e);
            }
        }
    }

    public void setPersonaName(int i, String str) {
        if (this.mService != null) {
            try {
                this.mService.setPersonaName(i, str);
            } catch (Throwable e) {
                Log.w(TAG, "Could not set persona name", e);
            }
        }
    }

    public void setPersonaSamsungAccount(int i, String str) {
        try {
            this.mService.setPersonaSamsungAccount(i, str);
        } catch (Throwable e) {
            Log.w(TAG, "Could not set persona SamsungAccount", e);
        }
    }

    public void setPersonaType(int i, String str) {
        if (this.mService != null) {
            try {
                this.mService.setPersonaType(i, str);
            } catch (Throwable e) {
                Log.w(TAG, "Could not set persona Type", e);
            }
        }
    }

    public void setShownHelp(int i, int i2, boolean z) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setShownHelp for " + i + ", " + i2 + ":" + z);
                this.mService.setShownHelp(i, i2, z);
            } catch (Throwable e) {
                Log.w(TAG, "failed to setShownHelp", e);
            }
        }
    }

    public boolean settingSyncAllowed(int i) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.settingSyncAllowed(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to execute settingSyncAllowed", e);
            }
        }
        return z;
    }

    public void setupComplete(int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setupComplete");
                this.mService.setupComplete(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed to setupComplete", e);
            }
        }
    }

    public void showKeyguard(int i, int i2) {
        if (this.mService != null) {
            try {
                this.mService.showKeyguard(i, i2);
            } catch (Throwable e) {
                Log.w(TAG, "Could not showKeyguard", e);
            }
        }
    }

    public boolean startActivityThroughPersona(Intent intent) {
        if (this.mService != null) {
            try {
                return this.mService.startActivityThroughPersona(intent);
            } catch (Throwable e) {
                Log.e(TAG, "Could not startActivityThroughPersona", e);
            }
        }
        return false;
    }

    public boolean switchPersonaAndLaunch(int i, Intent intent) {
        if (this.mService != null) {
            try {
                return this.mService.switchPersonaAndLaunch(i, intent);
            } catch (Throwable e) {
                Log.w(TAG, "Could not switch to persona and launch", e);
            }
        }
        return false;
    }

    public int unInstallSystemApplications(int i, List<String> list) {
        int i2 = -1;
        if (!isKnoxId(i)) {
            return i2;
        }
        if (this.mService != null) {
            try {
                Log.w(TAG, "in SemPersonaManager, unInstallSystemApplications(), calling service API");
                i2 = this.mService.unInstallSystemApplications(i, list);
            } catch (Throwable e) {
                Log.w(TAG, "Could not uninstall system package into persona with exception:", e);
            }
        }
        return i2;
    }

    public void unmarkForRemoval(int i) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "unmarkForRemoval() " + i);
                this.mService.unmarkForRemoval(i);
            } catch (Throwable e) {
                Log.w(TAG, "failed unmarkForRemoval()", e);
            }
        }
    }

    public boolean unmountOldContainer(String str) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "unmountOldContainer, pid : " + UserHandle.myUserId());
                return this.mService.unmountOldContainer(str);
            } catch (Throwable e) {
                Log.w(TAG, "can't unmount Knox 1.0 partition from PMS", e);
            }
        }
        return false;
    }

    public boolean updatePersonaInfo(int i, String str, int i2, int i3) {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.updatePersonaInfo(i, str, i2, i3);
            } catch (Throwable e) {
                Log.w(TAG, "failed to updatePersonaInfo", e);
            }
        }
        Log.d(TAG, "updateSemPersonaInfo : " + z);
        return z;
    }

    public void userActivity(int i) {
        if (this.mService != null) {
            try {
                this.mService.userActivity(i);
            } catch (Throwable e) {
                Log.e(TAG, "failed to userActivity" + Log.getStackTraceString(e));
            }
        }
    }

    public boolean verifyKnoxBackupPin(int i, String str) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "verifyKnoxBackupPin");
                return this.mService.verifyKnoxBackupPin(i, str);
            } catch (Throwable e) {
                Log.w(TAG, "can't verify Knox Backup Pin from PMS. return default value", e);
            }
        }
        return false;
    }
}
