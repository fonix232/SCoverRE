package com.samsung.android.app.ledcover.info;

import com.samsung.android.app.ledcover.BuildConfig;

public class Defines {
    public static final int ACT_APP_ADD_LIST = 3;
    public static final int ACT_APP_EDIT_LIST = 4;
    public static final int ACT_APP_LIST = 2;
    public static final int ACT_MAIN = 1;
    public static final String BROADCAST_ACTION_APP_CHANGED_FROM_ICON = "com.samsung.android.app.ledcover.action.APP_CHANGED_FROM_ICON";
    public static final String BROADCAST_ACTION_SHORTCUT_ENABLE_CHANGED = "com.samsung.android.app.ledcover.action.SHORTCUT_ENABLE_CHANGED";
    public static final int CALLER_ID_UI_MODE_CHANGE_CALLER_ID = 1;
    public static final int CALLER_ID_UI_MODE_DEFAULT = 0;
    public static final String DEL_PKG_AFTER_DEL_ICON_TRIGGER_NAME = "tr_del_pkg_after_del_icon";
    public static final char DOT_DISABLE = '0';
    public static final char DOT_ENABLE = '1';
    private static final int DREAM_LED_MATRIX_COLUMNS = 23;
    private static final int DREAM_LED_MATRIX_ROWS = 9;
    private static final int DREAM_LED_MATRIX_TOTAL = 207;
    public static final String ICON_COL_COUNT = "icon_count";
    public static final String ICON_COL_ICON_ARRAY = "icon_array";
    public static final String ICON_COL_ICON_NAME = "icon_name";
    public static final String ICON_COL_ID = "icon_id";
    public static final String ICON_COL_KEY = "_id";
    public static final String ICON_COUNT_DEL_TRIGGER_NAME = "tr_icon_count_del";
    public static final String ICON_COUNT_INSERT_TRIGGER_NAME = "tr_icon_count_insert";
    public static final String ICON_COUNT_UPDATE_TRIGGER_NAME = "tr_icon_count_update";
    public static final String ICON_DB_TABLE_NAME = "icon";
    public static final int ICON_INIT_ID = -33;
    public static final String ICON_SERVICE_NAME = "com.samsung.android.app.ledcover.service.LCoverIcon";
    public static final float LED_MATRIX_DOT_DIAMETER = 3.0f;
    public static final int LED_MATRIX_DOT_MARGIN = 1;
    public static final int LIMIT_CUSTOM_ICON = 60;
    public static final int LIMIT_EDITTEXT_STRING = 12;
    public static final Boolean LOG_LEVEL1;
    public static final Boolean LOG_LEVEL2;
    public static final Boolean LOG_LEVEL3;
    public static final String MANAGE_APP_PERMISSIONS = "android.intent.action.MANAGE_APP_PERMISSIONS";
    public static final String MANAGE_APP_PERMISSIONS_EXTRA = "android.intent.extra.PACKAGE_NAME";
    public static final int MENU_ADD = 1;
    public static final String MENU_CALL = "CALL";
    public static final int MENU_CHANGE = 2;
    public static final int MENU_DONE = 3;
    public static final int MENU_EDIT = 4;
    public static final String MENU_NOTI = "NOTI";
    public static final int MENU_REMOVE = 5;
    public static final String NAME = "LED_COVER";
    public static final String NOTIFICATION_SERVICE_NAME = "com.samsung.android.app.ledcover.service.LCoverNLS";
    public static final String PERMISSION_LCOVER_LAUNCH = "com.samsung.android.app.ledcover.LAUNCH";
    public static final String PKG_COL_ICON_INDEX = "icon_index";
    public static final String PKG_COL_ID = "pkg_id";
    public static final String PKG_COL_KEY = "_id";
    public static final String PKG_COL_PACKAGE_NAME = "name";
    public static final String PKG_DB_TABLE_NAME = "pkg";
    public static final String PKG_MISSED_CALL = "com.android.server.telecom";
    public static final String PKG_MISSED_CALL_SKT = "com.skt.prod.dialer";
    public static final String PKG_NAME = "com.samsung.android.app.ledcover";
    public static final String PRESET_ICON_ARRAY = "PRESET";
    public static final int PRESET_ICON_NUM = 54;
    public static final String READ_PERMISSIONS_STRING_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String READ_PERMISSIONS_STRING_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final int REQUEST_ADD_LIST_ACTIVITY_BY_OPTION_MENU = 1;
    public static final int REQUEST_ADD_LIST_ACTIVITY_WITH_START = 2;
    public static final int REQUEST_CREATION_ICON_ADD = 12;
    public static final int REQUEST_CREATION_ICON_MODIFY = 11;
    public static final int REQUEST_CREATION_ICON_NONE = 10;
    public static final int REQUEST_ICON_LIST_FOR_CHANGE = 3;
    public static final int REQUEST_PERMISSIONS_CONTACTS = 17;
    public static final int REQUEST_PERMISSIONS_PHONE = 16;
    public static final int RESULT_ADD_SELECTED_APP_FAILED = -1;
    public static final int RESULT_ADD_SELECTED_APP_SUCCESS = 0;
    public static final int RESULT_ADD_SELECTED_APP_UPDATE = 1;
    public static final String SAVE_LOG_FOLDER = "/Log/ledCover/";
    public static final String SAVE_PATTERN_FOLDER = "/LED_COVER/";
    public static final String SA_CALL_MAIN_ACTIVITY_EVENT_CREATE_LED_ICON = "1103";
    public static final String SA_CALL_MAIN_ACTIVITY_EVENT_EDIT = "1105";
    public static final String SA_CALL_MAIN_ACTIVITY_EVENT_GO_TO_TOP = "1104";
    public static final String SA_CALL_MAIN_ACTIVITY_EVENT_LED_ICONS = "1102";
    public static final String SA_CALL_MAIN_ACTIVITY_EVENT_SELECTION_MODE = "1106";
    public static final String SA_CALL_MAIN_ACTIVITY_EVENT_UP_BUTTON = "1101";
    public static final String SA_CALL_MAIN_CHANGE_MODE_EVENT_CHANGE_LED_ICON = "1142";
    public static final String SA_CALL_MAIN_CHANGE_MODE_EVENT_GO_TO_TOP = "1143";
    public static final String SA_CALL_MAIN_CHANGE_MODE_EVENT_UP_BUTTON = "1141";
    public static final String SA_CALL_MAIN_SELECTION_MODE_EVENT_MODIFY = "1113";
    public static final String SA_CALL_MAIN_SELECTION_MODE_EVENT_REMOVE = "1112";
    public static final String SA_CALL_MAIN_SELECTION_MODE_EVENT_SELECT_ALL = "1111";
    public static final String SA_CONTACT_LIST_ACTIVITY_EVENT_ADD_CONTACT = "1122";
    public static final String SA_CONTACT_LIST_ACTIVITY_EVENT_EDIT = "1123";
    public static final String SA_CONTACT_LIST_ACTIVITY_EVENT_SELECTION_MODE = "1124";
    public static final String SA_CONTACT_LIST_ACTIVITY_EVENT_UP_BUTTON = "1121";
    public static final String SA_CONTACT_LIST_SELECTION_MODE_EVENT_CHANGE = "1132";
    public static final String SA_CONTACT_LIST_SELECTION_MODE_EVENT_REMOVE = "1133";
    public static final String SA_CONTACT_LIST_SELECTION_MODE_EVENT_SELECT_ALL = "1131";
    public static final String SA_CREATE_LED_ICON_EVENT_CANCEL = "1310";
    public static final String SA_CREATE_LED_ICON_EVENT_DIALOG_CANCEL = "1307";
    public static final String SA_CREATE_LED_ICON_EVENT_DIALOG_DISCARD = "1308";
    public static final String SA_CREATE_LED_ICON_EVENT_DIALOG_SAVE = "1309";
    public static final String SA_CREATE_LED_ICON_EVENT_DRAW = "1302";
    public static final String SA_CREATE_LED_ICON_EVENT_ENLARGE = "1306";
    public static final String SA_CREATE_LED_ICON_EVENT_ERASER = "1303";
    public static final String SA_CREATE_LED_ICON_EVENT_REDO = "1305";
    public static final String SA_CREATE_LED_ICON_EVENT_SAVE = "1301";
    public static final String SA_CREATE_LED_ICON_EVENT_UNDO = "1304";
    public static final String SA_NOTI_APP_ADD_LIST_EVENT_DONE = "1222";
    public static final String SA_NOTI_APP_ADD_LIST_EVENT_SELECT_ALL = "1221";
    public static final String SA_NOTI_APP_LIST_EVENT_ADD = "1212";
    public static final String SA_NOTI_APP_LIST_EVENT_EDIT = "1213";
    public static final String SA_NOTI_APP_LIST_EVENT_SELECTION_MODE = "1214";
    public static final String SA_NOTI_APP_LIST_EVENT_UP_BUTTON = "1211";
    public static final String SA_NOTI_APP_LIST_SELECTION_MODE_EVENT_CHANGE = "1232";
    public static final String SA_NOTI_APP_LIST_SELECTION_MODE_EVENT_REMOVE = "1233";
    public static final String SA_NOTI_APP_LIST_SELECTION_MODE_EVENT_SELECT_ALL = "1231";
    public static final String SA_NOTI_MAIN_ACTIVITY_EVENT_CREATE_LED_ICON = "1203";
    public static final String SA_NOTI_MAIN_ACTIVITY_EVENT_EDIT = "1205";
    public static final String SA_NOTI_MAIN_ACTIVITY_EVENT_GO_TO_TOP = "1204";
    public static final String SA_NOTI_MAIN_ACTIVITY_EVENT_LED_ICONS = "1202";
    public static final String SA_NOTI_MAIN_ACTIVITY_EVENT_SELECTION_MODE = "1206";
    public static final String SA_NOTI_MAIN_ACTIVITY_EVENT_UP_BUTTON = "1201";
    public static final String SA_NOTI_MAIN_CHANGE_MODE_EVENT_CHANGE_LED_ICON = "1252";
    public static final String SA_NOTI_MAIN_CHANGE_MODE_EVENT_GO_TO_TOP = "1253";
    public static final String SA_NOTI_MAIN_CHANGE_MODE_EVENT_UP_BUTTON = "1251";
    public static final String SA_NOTI_MAIN_SELECTION_MODE_EVENT_MODIFY = "1243";
    public static final String SA_NOTI_MAIN_SELECTION_MODE_EVENT_REMOVE = "1242";
    public static final String SA_NOTI_MAIN_SELECTION_MODE_EVENT_SELECT_ALL = "1241";
    public static final String SA_ROOT_ACTIVITY_EVENT_LED_CALLER_ICONS = "1011";
    public static final String SA_ROOT_ACTIVITY_EVENT_LED_ICON_EDITOR_SHORTCUT = "1013";
    public static final String SA_ROOT_ACTIVITY_EVENT_LED_NOTIFICATION_ICONS = "1012";
    public static final String SA_ROOT_ACTIVITY_EVENT_UP_BUTTON = "1010";
    public static final String SA_SCREEN_CALL_MAIN_ACTIVITY = "111";
    public static final String SA_SCREEN_CALL_MAIN_CHANGE_MODE = "115";
    public static final String SA_SCREEN_CALL_MAIN_SELECTION_MODE = "112";
    public static final String SA_SCREEN_CONTACT_LIST_ACTIVITY = "113";
    public static final String SA_SCREEN_CONTACT_LIST_SELECTION_MODE = "114";
    public static final String SA_SCREEN_CREATE_LED_ICON_ACTIVITY = "131";
    public static final String SA_SCREEN_NOTI_APP_ADD_LIST_ACTIVITY = "123";
    public static final String SA_SCREEN_NOTI_APP_LIST_ACTIVITY = "122";
    public static final String SA_SCREEN_NOTI_APP_LIST_SELECTION_MODE = "124";
    public static final String SA_SCREEN_NOTI_MAIN_ACTIVITY = "121";
    public static final String SA_SCREEN_NOTI_MAIN_CHANGE_MODE = "126";
    public static final String SA_SCREEN_NOTI_MAIN_SELECTION_MODE = "125";
    public static final String SA_SCREEN_ROOT_ACTIVITY = "102";
    public static final String SA_SCREEN_UPDATE_FIRMWARE = "301";
    public static final String SA_SCREEN_UPDATE_SOFTWARE = "151";
    public static final String SA_SCREEN_UPDATING_FIRMWARE = "302";
    public static final String SA_TRACKING_ID = "444-399-5155102";
    public static final String SA_UI_VERSION = "3.0";
    public static final String SA_UPDATE_FIRMWARE_EVENT_HIDE = "3012";
    public static final String SA_UPDATE_FIRMWARE_EVENT_LATER = "3002";
    public static final String SA_UPDATE_FIRMWARE_EVENT_OK = "3003";
    public static final String SA_UPDATE_FIRMWARE_EVENT_TAP_CONFIRM_UPDATE_NOTI = "3001";
    public static final String SA_UPDATE_FIRMWARE_EVENT_TAP_UPDATING_NOTI = "3011";
    public static final String SA_UPDATE_SOFTWARE_EVENT_CANCEL = "1501";
    public static final String SA_UPDATE_SOFTWARE_EVENT_UPDATE = "1502";
    public static final String VERSION_PDA = "ro.build.PDA";

    static {
        LOG_LEVEL1 = Boolean.valueOf(true);
        LOG_LEVEL2 = Boolean.valueOf(false);
        LOG_LEVEL3 = Boolean.valueOf(false);
    }

    public static int getLedMatrixRows() {
        if (BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR)) {
            return DREAM_LED_MATRIX_ROWS;
        }
        return RESULT_ADD_SELECTED_APP_SUCCESS;
    }

    public static int getLedMatrixColumns() {
        if (BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR)) {
            return DREAM_LED_MATRIX_COLUMNS;
        }
        return RESULT_ADD_SELECTED_APP_SUCCESS;
    }

    public static int getLedMatrixTotal() {
        if (BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR)) {
            return DREAM_LED_MATRIX_TOTAL;
        }
        return RESULT_ADD_SELECTED_APP_SUCCESS;
    }
}
