package com.samsung.android.camera.core;

import android.app.ActivityThread;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.IAudioService;
import android.media.IAudioService.Stub;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Trace;
import android.provider.SemSmartGlow;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Element.DataKind;
import android.renderscript.Element.DataType;
import android.renderscript.RSIllegalArgumentException;
import android.renderscript.RenderScript;
import android.renderscript.Type.Builder;
import android.service.notification.ZenModeConfig;
import android.system.OsConstants;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Surface;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemCamera {
    @Deprecated
    public static final String ACTION_NEW_PICTURE = "android.hardware.action.NEW_PICTURE";
    @Deprecated
    public static final String ACTION_NEW_VIDEO = "android.hardware.action.NEW_VIDEO";
    private static final int AE_RESULT = 62289;
    private static final int AUTO_LOW_LIGHT_DETECTION_CHANGED = 62001;
    private static final int BATTERY_OVER_HEAT = 1393;
    private static final int BEAUTY_EYE_ENLARGE_LEVEL = 1184;
    private static final int BEAUTY_FACE_DISTORTION_COMPENSATION = 1186;
    private static final int BEAUTY_FACE_ENABLE_RELIGHT = 1187;
    private static final int BEAUTY_FACE_RELIGHT_DIRECTION = 1189;
    private static final int BEAUTY_FACE_RELIGHT_LEVEL = 1188;
    private static final int BEAUTY_FACE_RETOUCH_LEVEL = 1181;
    private static final int BEAUTY_SHOT_CAMERA_MSG_PREVIEW_METADATA_FROM_HAL = 61778;
    private static final int BEAUTY_SHOT_PROGRESS_RENDERING = 61777;
    private static final int BEAUTY_SHOT_RELIGHT_TRANSFORM_DATA = 61779;
    private static final int BEAUTY_SKIN_COLOR_LEVEL = 1185;
    private static final int BEAUTY_SLIM_FACE_LEVEL = 1183;
    private static final int BRIGHTNESS_VALUE = 62290;
    private static final int BURST_SHOT_CAPTURE = 1162;
    private static final int BURST_SHOT_CAPTURING_PROGRESSED = 61585;
    private static final int BURST_SHOT_CAPTURING_STOPPED = 61586;
    private static final int BURST_SHOT_FILE_STRING = 61588;
    private static final int BURST_SHOT_POSTVIEW_DATA = 61589;
    private static final int BURST_SHOT_REQUEST_IMAGE = 1161;
    private static final int BURST_SHOT_SAVING_COMPLETED = 61587;
    private static final int BURST_SHOT_SETUP = 1164;
    private static final int BURST_SHOT_TERMINATE = 1163;
    private static final int CAMERA_CMD_RESET_WB_CUSTOM_VALUE = 1391;
    private static final int CAMERA_CMD_START_MOTION_PHOTO = 1431;
    private static final int CAMERA_CMD_START_QRCODE_DETECTION = 1481;
    private static final int CAMERA_CMD_STOP_MOTION_PHOTO = 1432;
    private static final int CAMERA_CMD_STOP_QRCODE_DETECTION = 1482;
    public static final int CAMERA_ERROR_EVICTED = 2;
    public static final int CAMERA_ERROR_PREVIEWFRAME_TIMEOUT = 1001;
    public static final int CAMERA_ERROR_SERVER_DIED = 100;
    public static final int CAMERA_ERROR_UNKNOWN = 1;
    private static final int CAMERA_EXTRA_INFO_NOTIFY = 62465;
    private static final int CAMERA_FACE_DETECTION_DMC_SW = 5;
    private static final int CAMERA_FACE_DETECTION_HW = 0;
    private static final int CAMERA_FACE_DETECTION_SW = 1;
    private static final int CAMERA_FACE_DETECTION_SW_ONE_EYE = 2;
    private static final int CAMERA_FACE_DETECTION_SW_TWO_EYE = 3;
    public static final int CAMERA_HAL_API_VERSION_1_0 = 256;
    private static final int CAMERA_HAL_API_VERSION_NORMAL_CONNECT = -2;
    private static final int CAMERA_HAL_API_VERSION_UNSPECIFIED = -1;
    private static final int CAMERA_MSG_ALL_MSGS = -1;
    private static final int CAMERA_MSG_AUTO_PARAMETERS_NOTIFY = 131072;
    private static final int CAMERA_MSG_COMPRESSED_IMAGE = 256;
    private static final int CAMERA_MSG_DEPTH_MAP_DATA = 62498;
    private static final int CAMERA_MSG_ERROR = 1;
    private static final int CAMERA_MSG_FIRMWARE_NOTIFY = 524288;
    private static final int CAMERA_MSG_FOCUS = 4;
    private static final int CAMERA_MSG_FOCUS_MOVE = 2048;
    private static final int CAMERA_MSG_IRIS_DATA = 65536;
    private static final int CAMERA_MSG_IRIS_PREVIEW_DATA = 32768;
    private static final int CAMERA_MSG_MANUAL_FOCUS_NOTIFY = 1048576;
    private static final int CAMERA_MSG_POSTVIEW_FRAME = 64;
    private static final int CAMERA_MSG_PREVIEW_FRAME = 16;
    private static final int CAMERA_MSG_PREVIEW_METADATA = 1024;
    private static final int CAMERA_MSG_RAW_IMAGE = 128;
    private static final int CAMERA_MSG_RAW_IMAGE_NOTIFY = 512;
    private static final int CAMERA_MSG_SHOT_END = 262144;
    private static final int CAMERA_MSG_SHUTTER = 2;
    private static final int CAMERA_MSG_VIDEO_FRAME = 32;
    private static final int CAMERA_MSG_ZOOM = 8;
    private static final int CHANGE_MAIN_LCD = 1392;
    private static final int CHECK_MARKER_OF_SAMSUNG_DEFINED_CALLBACK_MSGS = 61440;
    private static final int COMMON_CANCEL_TAKE_PICTURE = 1472;
    private static final int COMMON_SHOT_CANCEL_PICTURE_COMPLETED = 62481;
    private static final int COMMON_SHOT_PREVIEW_STARTED = 62482;
    private static final int COMMON_STOP_TAKE_PICTURE = 1471;
    private static final int DEVICE_ORIENTATION = 1521;
    private static final int DISTORTION_EFFECTS_SHOT_CAPTURE = 1466;
    private static final int DISTORTION_EFFECTS_SHOT_INFO = 1464;
    private static final int DISTORTION_EFFECTS_SHOT_SET_MODE = 1465;
    private static final int DRAMA_SHOT_CANCEL = 1333;
    private static final int DRAMA_SHOT_CAPTURING_PROGRESS = 61985;
    private static final int DRAMA_SHOT_ERROR = 61987;
    private static final int DRAMA_SHOT_INPUT_YUV_STRING = 61988;
    private static final int DRAMA_SHOT_MODE = 1334;
    private static final int DRAMA_SHOT_PROGRESS_POSTPROCESSING = 61986;
    private static final int DRAMA_SHOT_RESULT_YUV_STRING = 61989;
    private static final int DRAMA_SHOT_START = 1331;
    private static final int DRAMA_SHOT_STOP = 1332;
    private static final int DRAMA_SHOT_STORAGE = 1335;
    private static final int DUAL_CAMERA_CAPTURE_STATUS_CHANGED = 62033;
    private static final int DUAL_CAMERA_TRACKING_STATUS_CHANGED = 62035;
    private static final int DUAL_MODE_SHOT_ASYNC_CAPTURE = 1374;
    private static final int EFFECT_REAR_BOTTOM_FRONT_TOP = 0;
    private static final int EFFECT_REAR_TOP_FRONT_BOTTOM = 1;
    private static final int ERROR_ALREADY_EXISTS = 2;
    private static final int ERROR_CAMERA_IN_USE = 7;
    private static final int ERROR_DEPRECATED_HAL = 9;
    private static final int ERROR_DISABLED = 6;
    private static final int ERROR_DISCONNECTED = 4;
    private static final int ERROR_ILLEGAL_ARGUMENT = 3;
    private static final int ERROR_INVALID_OPERATION = 10;
    private static final int ERROR_MAX_CAMERAS_IN_USE = 8;
    private static final int ERROR_NO_RESOURCE = 99;
    private static final int ERROR_PERMISSION_DENIED = 1;
    private static final int ERROR_TIMED_OUT = 5;
    public static final int EXTRA_INFO_TYPE_FLASH = 1;
    public static final int EXTRA_INFO_TYPE_HDR = 2;
    public static final int EXTRA_INFO_VALUE_DISABLE = 0;
    public static final int EXTRA_INFO_VALUE_ENABLE = 1;
    public static final int FOCUS_MODE_ALL_IN_FOCUS = 4;
    public static final int FOCUS_MODE_OUT_FOCUS = 1;
    public static final int FOCUS_MODE_OUT_FOCUS_FAST = 2;
    public static final int FOCUS_MODE_REFOCUS = 3;
    private static final int FOOD_SHOT_RESULT = 61505;
    private static final int GESTURE_CONTROL_MODE = 1345;
    private static final int GOLF_SHOT_CAPTURED = 61845;
    private static final int GOLF_SHOT_CREATING_RESULT_COMPLETED = 61843;
    private static final int GOLF_SHOT_CREATING_RESULT_PROGRESS = 61842;
    private static final int GOLF_SHOT_CREATING_RESULT_STARTED = 61841;
    private static final int GOLF_SHOT_ERROR = 61846;
    private static final int GOLF_SHOT_SAVE = 1313;
    private static final int GOLF_SHOT_SAVE_RESULT_PROGRESS = 61844;
    private static final int GOLF_SHOT_START = 1311;
    private static final int GOLF_SHOT_STOP = 1312;
    private static final int HAL_AE_AWB_LOCK_UNLOCK = 1501;
    private static final int HAL_AE_CONTROL = 1633;
    private static final int HAL_AE_POSITION = 1631;
    private static final int HAL_AE_SIZE = 1632;
    private static final int HAL_BURST_SHOT_FPS_VALUE = 1575;
    private static final int HAL_CANCEL_AF_FOCUS_AREA = 1553;
    private static final int HAL_CAPTURE_END = 1554;
    private static final int HAL_DELETE_BURST_TAKE = 1573;
    private static final int HAL_DONE_CHK_DATALINE = 61442;
    private static final int HAL_ENABLE_BRIGHTNESS_VALUE_CALLBACK = 1806;
    private static final int HAL_ENABLE_BURSTSHOT_FPS_CALLBACK = 1807;
    private static final int HAL_ENABLE_CURRENT_SET = 1643;
    private static final int HAL_ENABLE_FLASH_AUTO_CALLBACK = 1802;
    private static final int HAL_ENABLE_HDR_AUTO_CALLBACK = 1803;
    private static final int HAL_ENABLE_IRIS_IR_DATA_CALLBACK = 1831;
    private static final int HAL_ENABLE_IRIS_PREVIEW_DATA_CALLBACK = 1832;
    private static final int HAL_ENABLE_LIGHT_CONDITION = 1801;
    private static final int HAL_ENABLE_MULTI_AF = 1645;
    private static final int HAL_ENABLE_PAF_RESULT = 1644;
    private static final int HAL_ENABLE_SCREEN_FLASH = 1805;
    private static final int HAL_FACE_DETECT_LOCK_UNLOCK = 1502;
    private static final int HAL_MULTI_INSTANCE_PREPARE_PREVIEW = 1811;
    private static final int HAL_MULTI_INSTANCE_STANDBY_PREVIEW = 1812;
    private static final int HAL_OBJECT_POSITION = 1503;
    private static final int HAL_OBJECT_TRACKING_STARTSTOP = 1504;
    private static final int HAL_ON_AE_AWB = 1700;
    private static final int HAL_QUICK_VIEW_CANCEL = 1586;
    private static final int HAL_RECORDING_PAUSE = 1722;
    private static final int HAL_RECORDING_RESUME = 1721;
    private static final int HAL_SEND_FACE_ORIENTATION = 1530;
    private static final int HAL_SET_CAMERA_USAGE = 1821;
    private static final int HAL_SET_DEFAULT_IMEI = 1507;
    private static final int HAL_SET_FOCUS_PEAKING = 1841;
    private static final int HAL_SET_FRONT_SENSOR_MIRROR = 1510;
    private static final int HAL_SET_LVB_RECORDING_MODE = 1730;
    private static final int HAL_SET_SAMSUNG_CAMERA = 1508;
    private static final int HAL_START_ANIMATED_PHOTO = 1623;
    private static final int HAL_START_BURST_TAKE = 1571;
    private static final int HAL_START_CONTINUOUS_AF = 1551;
    private static final int HAL_START_FACEZOOM = 1531;
    private static final int HAL_START_PAF_CALLBACK = 1650;
    private static final int HAL_START_ZOOM = 1661;
    private static final int HAL_STOP_ANIMATED_PHOTO = 1624;
    private static final int HAL_STOP_BURST_TAKE = 1572;
    private static final int HAL_STOP_CHK_DATALINE = 1506;
    private static final int HAL_STOP_CONTINUOUS_AF = 1552;
    private static final int HAL_STOP_FACEZOOM = 1532;
    private static final int HAL_STOP_PAF_CALLBACK = 1651;
    private static final int HAL_STOP_ZOOM = 1662;
    private static final int HAL_TOUCH_AF_STARTSTOP = 1505;
    private static final int HAZE_REMOVAL_SHOT_PROGRESS_POSTPROCESSING = 62081;
    private static final int HAZE_STRENGTH = 1320;
    private static final int HDR_PICTURE_MODE_CHANGE = 1272;
    private static final int HDR_SHOT_ALL_PROGRESS_COMPLETED = 61572;
    private static final int HDR_SHOT_MODE_CHANGE = 1271;
    private static final int HDR_SHOT_RESULT_COMPLETED = 61571;
    private static final int HDR_SHOT_RESULT_PROGRESS = 61570;
    private static final int HDR_SHOT_RESULT_STARTED = 61569;
    private static final int HISTOGRAM_DATA = 62049;
    private static final int HISTOGRAM_SET_INCREMENT = 1363;
    private static final int HISTOGRAM_SET_SKIP_RATE = 1364;
    private static final int HISTOGRAM_START = 1361;
    private static final int HISTOGRAM_STOP = 1362;
    private static final int INTERACTIVE_SHOT_CAPTURE_PROGRESS = 62307;
    private static final int INTERACTIVE_SHOT_DIRECTION_CHANGED = 62305;
    private static final int INTERACTIVE_SHOT_DIRECTION_WARNING = 62306;
    private static final int INTERACTIVE_SHOT_PROCESS_COMPLETE = 62309;
    private static final int INTERACTIVE_SHOT_PROCESS_PROGRESS = 62308;
    private static final int LIGHT_CONDITION_CHANGED = 62002;
    private static final int LIGHT_CONDITION_ENABLE = 1351;
    private static final int LOW_LIGHT_SHOT_SET = 1264;
    private static final int MAGIC_SHOT_APPLICABLE_MODES = 62260;
    private static final int MAGIC_SHOT_CANCEL = 1423;
    private static final int MAGIC_SHOT_CAPTURE_PROGRESS = 62257;
    private static final int MAGIC_SHOT_CAPTURING_STOPPED = 62261;
    private static final int MAGIC_SHOT_PROCESS_COMPLETE = 62259;
    private static final int MAGIC_SHOT_PROCESS_PROGRESS = 62258;
    private static final int MAGIC_SHOT_START = 1421;
    private static final int MAGIC_SHOT_STATE_BESTPHOTO = 1;
    private static final int MAGIC_SHOT_STATE_PICACTION = 8;
    private static final int MAGIC_SHOT_STATE_PICBEST = 2;
    private static final int MAGIC_SHOT_STATE_PICLEAR = 4;
    private static final int MAGIC_SHOT_STATE_PICMOTION = 16;
    private static final int MAGIC_SHOT_STOP = 1422;
    private static final int MAX_IRIS_FD_QUEUE_SIZE = 3;
    private static final int METADATA_BURSTSHOT_FPS_VALUE = 62277;
    private static final int METADATA_CURRENT_SET_DATA = 62274;
    private static final int METADATA_MULTI_AF = 62276;
    private static final int METADATA_OBJECT_TRACKING_AF = 62275;
    private static final int METADATA_SINGLEPAF = 62273;
    public static final int MOTION_PANORAMA_DIRECTION_DOWN = 8;
    public static final int MOTION_PANORAMA_DIRECTION_LEFT = 2;
    public static final int MOTION_PANORAMA_DIRECTION_RIGHT = 1;
    public static final int MOTION_PANORAMA_DIRECTION_UP = 4;
    public static final int MOTION_PANORAMA_ERROR_NO_DIRECTION = 1;
    public static final int MOTION_PANORAMA_ERROR_REACHED_MAX_FRAME_COUNT = 3;
    public static final int MOTION_PANORAMA_ERROR_STITCHING = 0;
    public static final int MOTION_PANORAMA_ERROR_TRACING = 2;
    public static final int MOTION_PANORAMA_ERROR_UNKNOWN = 4;
    private static final int MOTION_PANORAMA_SHOT_CANCEL = 1493;
    private static final int MOTION_PANORAMA_SHOT_CAPTURED = 62340;
    private static final int MOTION_PANORAMA_SHOT_CAPTURED_MAX_FRAMES = 62346;
    private static final int MOTION_PANORAMA_SHOT_CAPTURE_RESULT = 62341;
    private static final int MOTION_PANORAMA_SHOT_DIR = 62342;
    private static final int MOTION_PANORAMA_SHOT_ENABLE_MOTION = 1494;
    private static final int MOTION_PANORAMA_SHOT_ERR = 62337;
    private static final int MOTION_PANORAMA_SHOT_LIVE_PREVIEW_DATA = 62344;
    private static final int MOTION_PANORAMA_SHOT_PROGRESS_STITCHING = 62339;
    private static final int MOTION_PANORAMA_SHOT_RECT_CENTER_POINT = 62338;
    private static final int MOTION_PANORAMA_SHOT_SLOW_MOVE = 62347;
    private static final int MOTION_PANORAMA_SHOT_START = 1491;
    private static final int MOTION_PANORAMA_SHOT_STOP = 1492;
    private static final int MOTION_PANORAMA_SHOT_THUMBNAIL_DATA = 62343;
    private static final int MOTION_PANORAMA_SHOT_UIIMAGE_DATA = 62345;
    private static final int MULTI_FRAME_SHOT_CAPTURING_STOPPED = 61730;
    private static final int MULTI_FRAME_SHOT_PROGRESS_POSTPROCESSING = 61731;
    private static final int MULTI_FRAME_SHOT_TERMINATE = 1263;
    private static final int NO_ERROR = 0;
    public static final int OUTFOCUS_ERR_AF = -1;
    public static final int OUTFOCUS_ERR_INF = -2;
    public static final int OUTFOCUS_ERR_NONE = 0;
    public static final int OUTFOCUS_ERR_SAVE_DATA = -4;
    public static final int OUTFOCUS_ERR_SEGMENTATION = -3;
    private static final int OUTFOCUS_SHOT_CAPTURE_PROGRESS = 62242;
    private static final int OUTFOCUS_SHOT_PROCESS_COMPLETE = 62243;
    private static final int OUTFOCUS_SHOT_PROCESS_PROGRESS = 62241;
    public static final int PANORAMA_DIRECTION_DOWN = 8;
    public static final int PANORAMA_DIRECTION_DOWN_LEFT = 10;
    public static final int PANORAMA_DIRECTION_DOWN_RIGHT = 9;
    public static final int PANORAMA_DIRECTION_LEFT = 2;
    public static final int PANORAMA_DIRECTION_RIGHT = 1;
    public static final int PANORAMA_DIRECTION_UP = 4;
    public static final int PANORAMA_DIRECTION_UP_LEFT = 6;
    public static final int PANORAMA_DIRECTION_UP_RIGHT = 5;
    private static final int PANORAMA_SHOT_CANCEL = 1114;
    private static final int PANORAMA_SHOT_CAPTURED = 61477;
    private static final int PANORAMA_SHOT_CAPTURED_MAX_FRAMES = 61481;
    private static final int PANORAMA_SHOT_CAPTURED_NEW = 61475;
    private static final int PANORAMA_SHOT_DIR = 61478;
    private static final int PANORAMA_SHOT_ERR = 61473;
    private static final int PANORAMA_SHOT_FINALIZE = 1113;
    private static final int PANORAMA_SHOT_LIVE_PREVIEW_DATA = 61480;
    private static final int PANORAMA_SHOT_LOW_RESOLUTION_DATA = 61479;
    private static final int PANORAMA_SHOT_PROGRESS_STITCHING = 61476;
    private static final int PANORAMA_SHOT_RECT_CENTER_POINT = 61474;
    private static final int PANORAMA_SHOT_SLOW_MOVE = 61482;
    private static final int PANORAMA_SHOT_START = 1111;
    private static final int PANORAMA_SHOT_STOP = 1112;
    private static final int PREVIEW_CALLBACK_FOR_GL_EFFECT = 61955;
    private static final int PRO_SUGGEST_SEND_LAST_PREVIEW_FRAME = 61793;
    public static final int RECORDING_MODE_PAUSE = 3;
    public static final int RECORDING_MODE_RESUME = 2;
    public static final int RECORDING_MODE_START = 0;
    public static final int RECORDING_MODE_STOP = 1;
    private static final int REQUEST_NOTIFY_PREVIEW_STARTED = 1473;
    private static final int SAMSUNG_BURST_PANORAMA_SHOT_COMPRESSED_IMAGE = 62097;
    private static final int SAMSUNG_SHOT_COMPRESSED_IMAGE = 61953;
    private static final int SAMSUNG_SHOT_POSTVIEW_FRAME = 61954;
    private static final int SAMSUNG_WIDE_SELFIE_SHOT_COMPRESSED_IMAGE = 62226;
    private static final int SCREEN_FLASH_TAKEPICTURE_COMPLETED = 62497;
    private static final int SECIMAGING_CALLBACK_STRING = 62034;
    private static final int SEC_IMAGE_EFFECT_SHOT_CREATING_RESULT_COMPLETED = 62019;
    private static final int SEC_IMAGE_EFFECT_SHOT_CREATING_RESULT_PROGRESS = 62018;
    private static final int SEC_IMAGE_EFFECT_SHOT_CREATING_RESULT_STARTED = 62017;
    public static final int SEMCAMERA_VERSION = 2401;
    private static final int SET_DISPLAY_ORIENTATION_MIRROR = 1511;
    private static final int SET_DUAL_MODE_SYNC = 1373;
    private static final int SET_DUAL_TRACKING_COORDINATE = 1375;
    private static final int SET_DUAL_TRACKING_MODE = 1376;
    private static final int SET_EFFECT_COORDINATE = 1293;
    private static final int SET_EFFECT_EXTERNAL_MODE = 1297;
    private static final int SET_EFFECT_FILTER = 1291;
    private static final int SET_EFFECT_LAYER_ORDER = 1294;
    private static final int SET_EFFECT_MODE = 1543;
    private static final int SET_EFFECT_OPTION = 1292;
    private static final int SET_EFFECT_ORIENTATION = 1296;
    private static final int SET_EFFECT_SAVE_AS_FLIPPED = 1298;
    private static final int SET_EFFECT_VISIBLE = 1295;
    private static final int SET_EFFECT_VISIBLE_FOR_RECORDING = 1352;
    private static final int SET_ENABLE_SHUTTER_SOUND = 1541;
    private static final int SET_MULTI_TRACK_MODE = 1394;
    private static final int SET_PREVIEW_CALLBACK_FOR_GL_EFFECT = 1306;
    private static final int SET_SECIMAGING_RECORDING_MODE = 1395;
    private static final int SET_SEED_POINT_TO_DETECT_FOOD_REGION = 1401;
    private static final int SET_STICKER_ENABLED = 1451;
    private static final int SET_WATERMARK_ENABLED = 1441;
    private static final int SET_WATERMARK_POSITION = 1442;
    private static final int SHOT_3DPANORAMA = 1037;
    private static final int SHOT_3DTOUR = 1048;
    private static final int SHOT_ANIMATED_SCENE = 1038;
    private static final int SHOT_AQUA_SCENE = 1042;
    private static final int SHOT_AUTO = 1030;
    private static final int SHOT_AUTO_NIGHT = 1022;
    private static final int SHOT_BEAUTY = 1007;
    private static final int SHOT_BESTFACE = 1025;
    private static final int SHOT_BESTPHOTO = 1024;
    private static final int SHOT_BUDDY_PHOTOSHARING = 1018;
    private static final int SHOT_BURST = 1017;
    private static final int SHOT_CARTOON = 1013;
    private static final int SHOT_CHILDREN = 1073;
    private static final int SHOT_CLIP_MOVIE = 1069;
    private static final int SHOT_CONTINUOUS = 1001;
    private static final int SHOT_COUPLE = 1036;
    private static final int SHOT_DISTORTION_EFFECTS = 1057;
    private static final int SHOT_DRAMA = 1031;
    private static final int SHOT_DUAL = 1047;
    private static final int SHOT_DUAL_PORTRAIT = 1080;
    private static final int SHOT_FACESHOT = 1016;
    private static final int SHOT_FASTMOTION = 1064;
    private static final int SHOT_FOOD = 1068;
    private static final int SHOT_GIFMAKER = 1058;
    private static final int SHOT_GOLF = 1028;
    private static final int SHOT_HAZE = 1049;
    private static final int SHOT_HDR = 1014;
    private static final int SHOT_HYPER_MOTION = 1074;
    private static final int SHOT_INTERACTIVE = 1062;
    private static final int SHOT_INTERVAL = 1060;
    private static final int SHOT_MAGIC = 1046;
    private static final int SHOT_MODELING3D = 1066;
    private static final int SHOT_MOTION_PANORAMA = 1072;
    private static final int SHOT_NIGHT = 1023;
    private static final int SHOT_NIGHT_SCENE = 1039;
    private static final int SHOT_OUTFOCUS = 1045;
    private static final int SHOT_PANORAMA = 1002;
    private static final int SHOT_PRO = 1059;
    private static final int SHOT_PRODUCT_SEARCH = 1079;
    private static final int SHOT_PROGRAM = 1033;
    private static final int SHOT_PRO_LITE = 1075;
    private static final int SHOT_SECFACE_INTERFACE = 1041;
    private static final int SHOT_SELFIE = 1055;
    private static final int SHOT_SELFIE_ALARM = 1056;
    private static final int SHOT_SHARESHOT = 1015;
    private static final int SHOT_SINGLE = 1000;
    private static final int SHOT_SINGLE_EFFECT = 1071;
    private static final int SHOT_SLOWMOTION = 1063;
    private static final int SHOT_SMILE = 1003;
    private static final int SHOT_SNAPMOVIE = 1061;
    private static final int SHOT_SPORTS_SCENE = 1040;
    private static final int SHOT_STORY = 1035;
    private static final int SHOT_SUPER_RESOLUTION = 1065;
    private static final int SHOT_TAG = 1067;
    private static final int SHOT_THEME = 1032;
    private static final int SHOT_WIDE_MOTION_SELFIE = 1078;
    private static final int SHOT_WIDE_SELFIE = 1052;
    private static final int SHOT_WIDE_SELFIE_LITE = 1077;
    public static final int SHUTTER_SOUND_AT_SHUTTER_CALLBACK = 2;
    public static final int SHUTTER_SOUND_AT_TAKE_PICTURE = 1;
    public static final int SHUTTER_SOUND_DISABLE = 0;
    private static final int SINGLE_SHOT_BRACKET_NEXT_SHOT_READY = 62067;
    private static final int SINGLE_SHOT_ERROR = 62066;
    private static final int SINGLE_SHOT_QRCODE_DETECT = 62068;
    private static final int SINGLE_SHOT_QRCODE_DETECT_ERR = 62069;
    private static final int SINGLE_SHOT_RAW_IMAGE_STRING = 62065;
    private static final int SLOW_MOTION_EVENT_RESULT = 61521;
    private static final int SMART_FILTER_PARAMETERS_CHANGED = 62513;
    private static final int SMILE_SHOT_DETECTION_REINIT = 1103;
    private static final int SMILE_SHOT_DETECTION_START = 1101;
    private static final int SMILE_SHOT_DETECTION_STOP = 1102;
    private static final int START_SMART_FILTER = 1321;
    private static final int STICKER_FACE_ALIGNMENT_DATA = 61761;
    private static final int STOP_SMART_FILTER = 1322;
    private static final String TAG = "SemCamera-JNI-Java";
    private static final int TAKEPICTURE_FLIP_PHOTO = 1396;
    private static final int THEME_SHOT_CAPTURE = 1343;
    private static final int THEME_SHOT_INFO = 1342;
    private static final int THEME_SHOT_MASK_SET = 1341;
    public static final int USAGE_NATIVE_CAMERA_APP = 0;
    private static final int WIDE_MOTION_SELFIE_SHOT_BEAUTY_LEVEL = 1498;
    private static final int WIDE_MOTION_SELFIE_SHOT_CANCEL = 1497;
    private static final int WIDE_MOTION_SELFIE_SHOT_CAPTURED = 62357;
    private static final int WIDE_MOTION_SELFIE_SHOT_CAPTURED_MAX_FRAMES = 62361;
    private static final int WIDE_MOTION_SELFIE_SHOT_CAPTURED_NEW = 62355;
    private static final int WIDE_MOTION_SELFIE_SHOT_DIR = 62358;
    private static final int WIDE_MOTION_SELFIE_SHOT_ERR = 62353;
    private static final int WIDE_MOTION_SELFIE_SHOT_LIVE_PREVIEW_DATA = 62360;
    private static final int WIDE_MOTION_SELFIE_SHOT_LOW_RESOLUTION_DATA = 62359;
    private static final int WIDE_MOTION_SELFIE_SHOT_MOTION_ENABLED = 1499;
    private static final int WIDE_MOTION_SELFIE_SHOT_MOVE_SLOWLY = 62362;
    private static final int WIDE_MOTION_SELFIE_SHOT_NEXT_CAPTURE_POSITION = 62363;
    private static final int WIDE_MOTION_SELFIE_SHOT_PROCESS_COMPLETE = 62365;
    private static final int WIDE_MOTION_SELFIE_SHOT_PROGRESS_STITCHING = 62356;
    private static final int WIDE_MOTION_SELFIE_SHOT_RECT_CENTER_POINT = 62354;
    private static final int WIDE_MOTION_SELFIE_SHOT_SINGLE_CAPTURE_DONE = 62364;
    private static final int WIDE_MOTION_SELFIE_SHOT_START = 1495;
    private static final int WIDE_MOTION_SELFIE_SHOT_STOP = 1496;
    private static final int WIDE_SELFIE_SHOT_BEAUTY_LEVEL = 1468;
    private static final int WIDE_SELFIE_SHOT_CANCEL = 1463;
    private static final int WIDE_SELFIE_SHOT_CAPTURED = 62213;
    private static final int WIDE_SELFIE_SHOT_CAPTURED_MAX_FRAMES = 62217;
    private static final int WIDE_SELFIE_SHOT_CAPTURED_NEW = 62211;
    private static final int WIDE_SELFIE_SHOT_DIR = 62214;
    private static final int WIDE_SELFIE_SHOT_ERR = 62209;
    private static final int WIDE_SELFIE_SHOT_LIVE_PREVIEW_DATA = 62216;
    private static final int WIDE_SELFIE_SHOT_LOW_RESOLUTION_DATA = 62215;
    private static final int WIDE_SELFIE_SHOT_NEXT_CAPTURE_POSITION = 62224;
    private static final int WIDE_SELFIE_SHOT_PROGRESS_STITCHING = 62212;
    private static final int WIDE_SELFIE_SHOT_RECT_CENTER_POINT = 62210;
    private static final int WIDE_SELFIE_SHOT_SINGLE_CAPTURE_DONE = 62225;
    private static final int WIDE_SELFIE_SHOT_SLOW_MOVE = 62218;
    private static final int WIDE_SELFIE_SHOT_START = 1461;
    private static final int WIDE_SELFIE_SHOT_STOP = 1462;
    private final Lock irisIrLock = new ReentrantLock();
    private final Lock irisPreviewLock = new ReentrantLock();
    private AutoExposureCallback mAutoExposureCallback = null;
    private AutoFocusCallback mAutoFocusCallback;
    private final Object mAutoFocusCallbackLock = new Object();
    private AutoFocusMoveCallback mAutoFocusMoveCallback;
    private BeautyEventListener mBeautyEventListener = null;
    private BrightnessValueCallback mBrightnessValueCallback = null;
    private BurstEventListener mBurstEventListener = null;
    private BurstShotFpsCallback mBurstShotFpsCallback = null;
    private CameraSensorDataListener mCameraSensorDataListener = null;
    private CommonEventListener mCommonEventListener = null;
    private DepthMapEventListener mDepthMapEventListener = null;
    private DualEventListener mDualEventListener = null;
    private ErrorCallback mErrorCallback;
    private EventHandler mEventHandler;
    private ExtraInfoListener mExtraInfoListener;
    private boolean mFaceDetectionRunning = false;
    private FaceDetectionListener mFaceListener;
    private FoodShotEventListener mFoodShotEventListener = null;
    private HardwareFaceDetectionListener mHardwareFaceDetectionListener;
    private HazeRemovalEventListener mHazeRemovalShotEventListener = null;
    private HdrEventListener mHdrEventListener = null;
    private ImageEffectEventListener mImageEffectEventListener = null;
    private InteractiveShotEventListener mInteractiveShotEventListener = null;
    private IrisDataCallback mIrisDataCallback;
    private boolean mIrisIrCallbackEnabled = false;
    Queue<ParcelFileDescriptor> mIrisIrQueue = new LinkedList();
    private boolean mIrisPreviewCallbackEnabled = false;
    Queue<ParcelFileDescriptor> mIrisPreviewQueue = new LinkedList();
    private PictureCallback mJpegCallback;
    private LightConditionChangedListener mLightConditionChangedListener = null;
    private MotionPanoramaEventListener mMotionPanoramaEventListener = null;
    private MultiAutoFocusCallback mMultiAutoFocusCallback = null;
    private MultiFrameEventListener mMultiFrameEventListener = null;
    private long mNativeContext;
    private ObjectTrackingAutoFocusCallback mObjectTrackingAutoFocusCallback = null;
    private boolean mOneShot;
    private PanoramaEventListener mPanoramaEventListener = null;
    private PhaseAutoFocusCallback mPhaseAutoFocusCallback = null;
    private PostEventListener mPostEventListener = null;
    private PictureCallback mPostviewCallback;
    private PreviewCallback mPreviewCallback;
    private PreviewCallback mPreviewCallbackForGLEffect;
    private PreviewCallbackTimeStamp mPreviewCallbackTimeStamp;
    private QrCodeDetectionEventListener mQrCodeDetectionEventListener = null;
    private PictureCallback mRawImageCallback;
    private RelightEventListener mRelightEventListener = null;
    private ScreenFlashEventListener mScreenFlashEventListener = null;
    private SelectiveFocusEventListener mSelectiveFocusEventListener = null;
    private ShotAndMoreEventListener mShotAndMoreEventListener = null;
    private ShutterCallback mShutterCallback;
    private SlowMotionEventListener mSlowMotionEventListener = null;
    private SmartFilterListener mSmartFilterListener = null;
    private StickerEventListener mStickerEventListener = null;
    private boolean mUsingPreviewAllocation;
    private WideMotionSelfieEventListener mWideMotionSelfieEventListener = null;
    private WideSelfieEventListener mWideSelfieEventListener = null;
    private boolean mWithBuffer;
    private OnZoomChangeListener mZoomListener;

    public static class Area {
        public Rect rect;
        public int weight;

        public Area(Rect rect, int i) {
            this.rect = rect;
            this.weight = i;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof Area)) {
                return false;
            }
            Area area = obj;
            if (this.rect == null) {
                if (area.rect != null) {
                    return false;
                }
            } else if (!this.rect.equals(area.rect)) {
                return false;
            }
            if (this.weight == area.weight) {
                z = true;
            }
            return z;
        }
    }

    public interface AutoExposureCallback {
        void onAutoExposure(int i);
    }

    public interface AutoFocusCallback {
        void onAutoFocus(int i, SemCamera semCamera);
    }

    public interface AutoFocusMoveCallback {
        void onAutoFocusMoving(boolean z, SemCamera semCamera);
    }

    public interface BeautyEventListener {
        void onBeautySavingProgress(int i);
    }

    public interface BrightnessValueCallback {
        void onBrightnessValue(float f);
    }

    public interface BurstEventListener {
        void onBurstCapturingProgressed(int i, int i2);

        void onBurstCapturingStopped(int i);

        void onBurstSavingCompleted(int i);

        void onBurstStringProgressed(byte[] bArr);
    }

    public interface BurstShotFpsCallback {
        void onBurstShotFpsChanged(int i);
    }

    public static class CameraBusyRuntimeException extends RuntimeException {
        public CameraBusyRuntimeException(String str) {
            super(str);
        }
    }

    public static class CameraInfo {
        public static final int CAMERA_FACING_BACK = 0;
        public static final int CAMERA_FACING_FRONT = 1;
        public boolean canDisableShutterSound;
        public int facing;
        public int orientation;
    }

    public static class CameraMaxUsersRuntimeException extends RuntimeException {
        public CameraMaxUsersRuntimeException(String str) {
            super(str);
        }
    }

    public static class CameraNoResourceException extends RuntimeException {
        public CameraNoResourceException(String str) {
            super(str);
        }
    }

    public static class CameraSensorData {
        public short driverResolution;
        public long exposureTime;
        public short exposureValue;
        public short iso;
        public short lensPositionCurrent;
        public short lensPositionMax;
        public short lensPositionMin;

        public CameraSensorData() {
            this.exposureTime = 0;
            this.exposureValue = (short) 0;
            this.iso = (short) 0;
            this.lensPositionMin = (short) 0;
            this.lensPositionMax = (short) 0;
            this.lensPositionCurrent = (short) 0;
            this.driverResolution = (short) 0;
        }

        public CameraSensorData(byte[] bArr) {
            this.exposureTime = (long) ((((bArr[0] & 255) | ((bArr[1] & 255) << 8)) | ((bArr[2] & 255) << 16)) | ((bArr[3] & 255) << 24));
            this.exposureValue = (short) ((bArr[4] & 255) | ((bArr[5] & 255) << 8));
            this.iso = (short) ((bArr[6] & 255) | ((bArr[7] & 255) << 8));
            this.lensPositionMin = (short) ((bArr[8] & 255) | ((bArr[9] & 255) << 8));
            this.lensPositionMax = (short) ((bArr[10] & 255) | ((bArr[11] & 255) << 8));
            this.lensPositionCurrent = (short) ((bArr[12] & 255) | ((bArr[13] & 255) << 8));
            this.driverResolution = (short) ((bArr[14] & 255) | ((bArr[15] & 255) << 8));
        }
    }

    public interface CameraSensorDataListener {
        void onCameraSensorData(CameraSensorData cameraSensorData);
    }

    public interface CommonEventListener {
        void onPreviewStarted();

        void onTakePictureCanceled();
    }

    public interface DepthMapEventListener {
        void onDepthMapData(byte[] bArr);
    }

    public interface DualEventListener {
        void onDualCaptureAvailable(int i);

        void onDualTrackingAvailable(int i);
    }

    public interface ErrorCallback {
        void onError(int i, SemCamera semCamera);
    }

    private class EventHandler extends Handler {
        private final SemCamera mCamera;

        public EventHandler(SemCamera semCamera, Looper looper) {
            super(looper);
            this.mCamera = semCamera;
        }

        public void handleMessage(Message message) {
            if (message.what < 1024) {
                Log.m29d(SemCamera.TAG, "handleMessage: " + message.what);
            }
            Trace.traceBegin(1024, "SemCamera handleMessage(" + message.what + ")");
            try {
                switch (message.what) {
                    case 1:
                        Log.m31e(SemCamera.TAG, "Error " + message.arg1);
                        if (SemCamera.this.mErrorCallback != null) {
                            SemCamera.this.mErrorCallback.onError(message.arg1, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 2:
                        if (SemCamera.this.mShutterCallback != null) {
                            SemCamera.this.mShutterCallback.onShutter();
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 4:
                        AutoFocusCallback -get3;
                        synchronized (SemCamera.this.mAutoFocusCallbackLock) {
                            -get3 = SemCamera.this.mAutoFocusCallback;
                        }
                        if (-get3 != null) {
                            -get3.onAutoFocus(message.arg1, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 8:
                        if (SemCamera.this.mZoomListener != null) {
                            SemCamera.this.mZoomListener.onZoomChange(message.arg1, message.arg2 != 0, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 16:
                        if (message.arg1 == 1) {
                            TimeStampFrame timeStampFrame = (TimeStampFrame) message.obj;
                            PreviewCallbackTimeStamp -get40 = SemCamera.this.mPreviewCallbackTimeStamp;
                            if (-get40 != null) {
                                if (SemCamera.this.mOneShot) {
                                    SemCamera.this.mPreviewCallback = null;
                                } else if (!SemCamera.this.mWithBuffer) {
                                    SemCamera.this.setHasPreviewCallback(true, false, false);
                                }
                                -get40.onPreviewFrame(timeStampFrame.FrameData, timeStampFrame.timeStamp, this.mCamera);
                            }
                            Trace.traceEnd(1024);
                            return;
                        }
                        PreviewCallback -get38 = SemCamera.this.mPreviewCallback;
                        if (-get38 != null) {
                            if (SemCamera.this.mOneShot) {
                                SemCamera.this.mPreviewCallback = null;
                            } else if (!SemCamera.this.mWithBuffer) {
                                SemCamera.this.setHasPreviewCallback(true, false, false);
                            }
                            -get38.onPreviewFrame((byte[]) message.obj, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 64:
                        if (SemCamera.this.mPostviewCallback != null) {
                            SemCamera.this.mPostviewCallback.onPictureTaken((byte[]) message.obj, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 128:
                        if (SemCamera.this.mRawImageCallback != null) {
                            SemCamera.this.mRawImageCallback.onPictureTaken((byte[]) message.obj, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 256:
                        if (SemCamera.this.mJpegCallback != null) {
                            SemCamera.this.mJpegCallback.onPictureTaken((byte[]) message.obj, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 1024:
                        if (SemCamera.this.mFaceListener != null && SemCamera.this.mFaceDetectionRunning) {
                            if (message.obj != null) {
                                SemCamera.this.mFaceListener.onFaceDetection((Face[]) message.obj, this.mCamera);
                            } else {
                                Log.m31e(SemCamera.TAG, "Error : face object is null.");
                            }
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 2048:
                        if (SemCamera.this.mAutoFocusMoveCallback != null) {
                            SemCamera.this.mAutoFocusMoveCallback.onAutoFocusMoving(message.arg1 != 0, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case 32768:
                        if (SemCamera.this.mIrisDataCallback != null) {
                            ParcelFileDescriptor parcelFileDescriptor = null;
                            try {
                                SemCamera.this.irisPreviewLock.lock();
                                if (!SemCamera.this.mIrisPreviewCallbackEnabled) {
                                    Log.m31e(SemCamera.TAG, "Check if IrisPreviewCallback is Enabled or not");
                                } else if (SemCamera.this.mIrisPreviewQueue != null && SemCamera.this.mIrisPreviewQueue.size() > 0) {
                                    parcelFileDescriptor = (ParcelFileDescriptor) SemCamera.this.mIrisPreviewQueue.poll();
                                    Log.m29d(SemCamera.TAG, "handleMessage: onIrisDataCallback irisPreviewFd = " + parcelFileDescriptor.getFileDescriptor().getInt$());
                                }
                            } catch (Throwable e) {
                                Log.m32e(SemCamera.TAG, "CAMERA_MSG_IRIS_PREVIEW_DATA : onIrisDataCallback fail", e);
                            } finally {
                            }
                            if (parcelFileDescriptor != null) {
                                try {
                                    SemCamera.this.mIrisDataCallback.onIrisDataCallback(parcelFileDescriptor, this.mCamera);
                                    parcelFileDescriptor.close();
                                } catch (Throwable e2) {
                                    Log.m32e(SemCamera.TAG, "CAMERA_MSG_IRIS_PREVIEW_DATA : ", e2);
                                }
                            }
                        } else {
                            Log.m31e(SemCamera.TAG, "mIrisDataCallback is null !!!");
                        }
                        Trace.traceEnd(1024);
                        return;
                    case SemCamera.PANORAMA_SHOT_ERR /*61473*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaError(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_RECT_CENTER_POINT /*61474*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaRectChanged(message.arg1, message.arg2);
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_CAPTURED_NEW /*61475*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaCapturedNew();
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_PROGRESS_STITCHING /*61476*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaProgressStitching(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_CAPTURED /*61477*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaCaptured();
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_DIR /*61478*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaDirectionChanged(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_LOW_RESOLUTION_DATA /*61479*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaLowResolutionData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_LIVE_PREVIEW_DATA /*61480*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaLivePreviewData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_CAPTURED_MAX_FRAMES /*61481*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaCapturedMaxFrames();
                            break;
                        }
                        break;
                    case SemCamera.PANORAMA_SHOT_SLOW_MOVE /*61482*/:
                        if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaSlowMove();
                            break;
                        }
                        break;
                    case SemCamera.FOOD_SHOT_RESULT /*61505*/:
                        Log.m33i(SemCamera.TAG, "FOOD_SHOT_RESULT");
                        if (SemCamera.this.mFoodShotEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mFoodShotEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mFoodShotEventListener.onFoodShotComplete((byte[]) message.obj);
                            break;
                        }
                    case SemCamera.SLOW_MOTION_EVENT_RESULT /*61521*/:
                        Log.m33i(SemCamera.TAG, "SLOW_MOTION_EVENT_RESULT");
                        if (SemCamera.this.mSlowMotionEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mSlowMotionEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mSlowMotionEventListener.onSlowMotionEventResult(new SlowMotionEventResult((byte[]) message.obj).events);
                            break;
                        }
                    case SemCamera.HDR_SHOT_RESULT_STARTED /*61569*/:
                        if (SemCamera.this.mHdrEventListener != null) {
                            SemCamera.this.mHdrEventListener.onHdrResultStarted();
                            break;
                        }
                        break;
                    case SemCamera.HDR_SHOT_RESULT_PROGRESS /*61570*/:
                        if (SemCamera.this.mHdrEventListener != null) {
                            SemCamera.this.mHdrEventListener.onHdrResultProgress(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.HDR_SHOT_RESULT_COMPLETED /*61571*/:
                        if (SemCamera.this.mHdrEventListener != null) {
                            SemCamera.this.mHdrEventListener.onHdrResultCompleted(message.arg1 == 1);
                            break;
                        }
                        break;
                    case SemCamera.HDR_SHOT_ALL_PROGRESS_COMPLETED /*61572*/:
                        if (SemCamera.this.mHdrEventListener != null) {
                            SemCamera.this.mHdrEventListener.onHdrAllProgressCompleted(message.arg1 == 1);
                            break;
                        }
                        break;
                    case SemCamera.BURST_SHOT_CAPTURING_PROGRESSED /*61585*/:
                        Log.m33i(SemCamera.TAG, "BURST_SHOT_CAPTURING_PROGRESSED(" + message.arg1 + "/" + message.arg2 + ")");
                        if (SemCamera.this.mBurstEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mBurstEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mBurstEventListener.onBurstCapturingProgressed(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.BURST_SHOT_CAPTURING_STOPPED /*61586*/:
                        Log.m33i(SemCamera.TAG, "BURST_SHOT_CAPTURING_STOPPED");
                        if (SemCamera.this.mBurstEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mBurstEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mBurstEventListener.onBurstCapturingStopped(message.arg1);
                            break;
                        }
                    case SemCamera.BURST_SHOT_SAVING_COMPLETED /*61587*/:
                        Log.m33i(SemCamera.TAG, "BURST_SHOT_SAVING_COMPLETED");
                        if (SemCamera.this.mBurstEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mBurstEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mBurstEventListener.onBurstSavingCompleted(message.arg1);
                            break;
                        }
                    case SemCamera.BURST_SHOT_FILE_STRING /*61588*/:
                        Log.m33i(SemCamera.TAG, "BURST_SHOT_FILE_STRING");
                        if (SemCamera.this.mBurstEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mBurstEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mBurstEventListener.onBurstStringProgressed((byte[]) message.obj);
                            break;
                        }
                    case SemCamera.BURST_SHOT_POSTVIEW_DATA /*61589*/:
                        Log.m33i(SemCamera.TAG, "BURST_SHOT_POSTVIEW_DATA");
                        if (SemCamera.this.mPostviewCallback == null) {
                            Log.m31e(SemCamera.TAG, "mPostviewCallback is null !!!");
                            break;
                        } else {
                            SemCamera.this.mPostviewCallback.onPictureTaken((byte[]) message.obj, this.mCamera);
                            break;
                        }
                    case SemCamera.MULTI_FRAME_SHOT_PROGRESS_POSTPROCESSING /*61731*/:
                        if (SemCamera.this.mMultiFrameEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mMultiFrameEventListener is null !!!");
                            break;
                        }
                        Log.m29d(SemCamera.TAG, "MULTI_FRAME_SHOT_PROGRESS_POSTPROCESSING ");
                        SemCamera.this.mMultiFrameEventListener.onMultiFrameCapturingProgressed(message.arg1, message.arg2);
                        break;
                    case SemCamera.STICKER_FACE_ALIGNMENT_DATA /*61761*/:
                        Log.m33i(SemCamera.TAG, "STICKER_FACE_ALIGNMENT_DATA");
                        if (SemCamera.this.mStickerEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mStickerEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mStickerEventListener.onFaceAlignmentData((byte[]) message.obj);
                            break;
                        }
                    case SemCamera.BEAUTY_SHOT_PROGRESS_RENDERING /*61777*/:
                        if (SemCamera.this.mBeautyEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mBeautyEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mBeautyEventListener.onBeautySavingProgress(message.arg1);
                            break;
                        }
                    case SemCamera.BEAUTY_SHOT_CAMERA_MSG_PREVIEW_METADATA_FROM_HAL /*61778*/:
                        if (SemCamera.this.mHardwareFaceDetectionListener == null) {
                            Log.m31e(SemCamera.TAG, "mHardwareFaceDetectionListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mHardwareFaceDetectionListener.onHardwareFaceDetection((Face[]) message.obj, this.mCamera);
                            break;
                        }
                    case SemCamera.BEAUTY_SHOT_RELIGHT_TRANSFORM_DATA /*61779*/:
                        Log.m33i(SemCamera.TAG, "BEAUTY_SHOT_RELIGHT_TRANSFORM_DATA");
                        if (SemCamera.this.mRelightEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mRelightEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mRelightEventListener.onRelightTransformDataUpdated(new RelightTransformData((byte[]) message.obj).mTransformData);
                            break;
                        }
                    case SemCamera.SAMSUNG_SHOT_COMPRESSED_IMAGE /*61953*/:
                        Log.m37w(SemCamera.TAG, "SAMSUNG_SHOT_COMPRESSED_IMAGE");
                        if (SemCamera.this.mJpegCallback != null) {
                            SemCamera.this.mJpegCallback.onPictureTaken((byte[]) message.obj, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case SemCamera.PREVIEW_CALLBACK_FOR_GL_EFFECT /*61955*/:
                        if (SemCamera.this.mPreviewCallbackForGLEffect != null) {
                            SemCamera.this.mPreviewCallbackForGLEffect.onPreviewFrame((byte[]) message.obj, this.mCamera);
                            SemCamera.this.mPreviewCallbackForGLEffect = null;
                            break;
                        }
                        break;
                    case SemCamera.LIGHT_CONDITION_CHANGED /*62002*/:
                        if (SemCamera.this.mLightConditionChangedListener != null) {
                            SemCamera.this.mLightConditionChangedListener.onLightConditionChanged(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.DUAL_CAMERA_CAPTURE_STATUS_CHANGED /*62033*/:
                        if (SemCamera.this.mDualEventListener != null) {
                            SemCamera.this.mDualEventListener.onDualCaptureAvailable(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.SECIMAGING_CALLBACK_STRING /*62034*/:
                        if (SemCamera.this.mImageEffectEventListener != null) {
                            SemCamera.this.mImageEffectEventListener.onImageEffectInfo(new String((byte[]) message.obj));
                            break;
                        }
                        break;
                    case SemCamera.DUAL_CAMERA_TRACKING_STATUS_CHANGED /*62035*/:
                        if (SemCamera.this.mDualEventListener != null) {
                            SemCamera.this.mDualEventListener.onDualTrackingAvailable(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.SINGLE_SHOT_QRCODE_DETECT /*62068*/:
                        Log.m33i(SemCamera.TAG, "SINGLE_SHOT_QRCODE_DETECT");
                        if (SemCamera.this.mQrCodeDetectionEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mQrCodeDetectionEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mQrCodeDetectionEventListener.onQrCodeDetection((byte[]) message.obj, this.mCamera);
                            break;
                        }
                    case SemCamera.SINGLE_SHOT_QRCODE_DETECT_ERR /*62069*/:
                        Log.m33i(SemCamera.TAG, "SINGLE_SHOT_QRCODE_DETECT_ERR(" + message.arg1 + ")");
                        if (SemCamera.this.mQrCodeDetectionEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mQrCodeDetectionEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mQrCodeDetectionEventListener.onQrCodeDetectionError(message.arg1);
                            break;
                        }
                    case SemCamera.HAZE_REMOVAL_SHOT_PROGRESS_POSTPROCESSING /*62081*/:
                        Log.m33i(SemCamera.TAG, "HAZE_REMOVAL_SHOT_PROGRESS_POSTPROCESSING ");
                        if (SemCamera.this.mHazeRemovalShotEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mHazeRemovalShotEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mHazeRemovalShotEventListener.onHazeRemovalCapturingProgressed(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.SAMSUNG_BURST_PANORAMA_SHOT_COMPRESSED_IMAGE /*62097*/:
                        Log.m37w(SemCamera.TAG, "SAMSUNG_BURST_PANORAMA_SHOT_COMPRESSED_IMAGE");
                        if (SemCamera.this.mJpegCallback != null) {
                            SemCamera.this.mJpegCallback.onPictureTaken((byte[]) message.obj, this.mCamera);
                        } else if (SemCamera.this.mPanoramaEventListener != null) {
                            SemCamera.this.mPanoramaEventListener.onPanoramaError(0);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case SemCamera.WIDE_SELFIE_SHOT_ERR /*62209*/:
                        Log.m33i(SemCamera.TAG, "WIDE_SELFIE_SHOT_ERR(" + message.arg1 + ")");
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieError(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_RECT_CENTER_POINT /*62210*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieRectChanged((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_CAPTURED_NEW /*62211*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieCapturedNew();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_PROGRESS_STITCHING /*62212*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieProgressStitching(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_CAPTURED /*62213*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieCaptured();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_DIR /*62214*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieDirectionChanged(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_LOW_RESOLUTION_DATA /*62215*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieLowResolutionData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_LIVE_PREVIEW_DATA /*62216*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieLivePreviewData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_CAPTURED_MAX_FRAMES /*62217*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieCapturedMaxFrames();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_SLOW_MOVE /*62218*/:
                        if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieSlowMove();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_SELFIE_SHOT_NEXT_CAPTURE_POSITION /*62224*/:
                        Log.m33i(SemCamera.TAG, "WIDE_SELFIE_SHOT_NEXT_CAPTURE_POSITION");
                        if (SemCamera.this.mWideSelfieEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mWideSelfieEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieNextCapturePosition(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.WIDE_SELFIE_SHOT_SINGLE_CAPTURE_DONE /*62225*/:
                        Log.m33i(SemCamera.TAG, "WIDE_SELFIE_SHOT_SINGLE_CAPTURE_DONE");
                        if (SemCamera.this.mWideSelfieEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mWideSelfieEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieSingleCaptureDone();
                            break;
                        }
                    case SemCamera.SAMSUNG_WIDE_SELFIE_SHOT_COMPRESSED_IMAGE /*62226*/:
                        Log.m33i(SemCamera.TAG, "SAMSUNG_WIDE_SELFIE_SHOT_COMPRESSED_IMAGE");
                        if (SemCamera.this.mJpegCallback != null) {
                            SemCamera.this.mJpegCallback.onPictureTaken((byte[]) message.obj, this.mCamera);
                        } else if (SemCamera.this.mWideSelfieEventListener != null) {
                            SemCamera.this.mWideSelfieEventListener.onWideSelfieError(0);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case SemCamera.OUTFOCUS_SHOT_PROCESS_PROGRESS /*62241*/:
                        Log.m33i(SemCamera.TAG, "OUTFOCUS_SHOT_PROCESS_PROGRESS(" + message.arg1 + ", " + message.arg2 + ")");
                        if (SemCamera.this.mSelectiveFocusEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mSelectiveFocusEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mSelectiveFocusEventListener.onSelectiveFocusProcessProgress(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.OUTFOCUS_SHOT_CAPTURE_PROGRESS /*62242*/:
                        Log.m33i(SemCamera.TAG, "OUTFOCUS_SHOT_CAPTURE_PROGRESS(" + message.arg1 + ", " + message.arg2 + ")");
                        if (SemCamera.this.mSelectiveFocusEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mSelectiveFocusEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mSelectiveFocusEventListener.onSelectiveFocusCaptureProgress(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.OUTFOCUS_SHOT_PROCESS_COMPLETE /*62243*/:
                        Log.m33i(SemCamera.TAG, "OUTFOCUS_SHOT_PROCESS_COMPLETE(" + message.arg1 + ")");
                        if (SemCamera.this.mSelectiveFocusEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mSelectiveFocusEventListener is null !!!");
                            break;
                        }
                        int i = 0;
                        if (message.arg1 == -1 || message.arg1 == -3) {
                            i = -1;
                        }
                        if (message.arg1 == -2) {
                            i = -2;
                        }
                        if (message.arg1 == 21) {
                            i = -4;
                        }
                        if (message.arg1 >= 11 || message.arg1 == -4) {
                            i = -3;
                        }
                        SemCamera.this.mSelectiveFocusEventListener.onSelectiveFocusComplete(i);
                        break;
                    case SemCamera.MAGIC_SHOT_CAPTURE_PROGRESS /*62257*/:
                        if (SemCamera.this.mShotAndMoreEventListener != null) {
                            SemCamera.this.mShotAndMoreEventListener.onShotAndMoreCaptureProgress(message.arg1, message.arg2);
                            break;
                        }
                        break;
                    case SemCamera.MAGIC_SHOT_PROCESS_PROGRESS /*62258*/:
                        if (SemCamera.this.mShotAndMoreEventListener != null) {
                            SemCamera.this.mShotAndMoreEventListener.onShotAndMoreProcessProgress(message.arg1, message.arg2);
                            break;
                        }
                        break;
                    case SemCamera.MAGIC_SHOT_PROCESS_COMPLETE /*62259*/:
                        if (SemCamera.this.mShotAndMoreEventListener != null) {
                            SemCamera.this.mShotAndMoreEventListener.onShotAndMoreComplete();
                            break;
                        }
                        break;
                    case SemCamera.MAGIC_SHOT_APPLICABLE_MODES /*62260*/:
                        if (SemCamera.this.mShotAndMoreEventListener != null) {
                            SemCamera.this.mShotAndMoreEventListener.onShotAndMoreApplicableMode(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.METADATA_SINGLEPAF /*62273*/:
                        if (SemCamera.this.mPhaseAutoFocusCallback != null) {
                            PafResult pafResult = new PafResult((byte[]) message.obj);
                            SemCamera.this.mPhaseAutoFocusCallback.onPhaseAutoFocus(pafResult.mode, pafResult.goalPos, pafResult.reliability, pafResult.lensPositionCurrent, pafResult.driverResolution);
                            break;
                        }
                        break;
                    case SemCamera.METADATA_CURRENT_SET_DATA /*62274*/:
                        if (SemCamera.this.mCameraSensorDataListener == null) {
                            Log.m31e(SemCamera.TAG, "mCameraSensorDataListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mCameraSensorDataListener.onCameraSensorData(new CameraSensorData((byte[]) message.obj));
                            break;
                        }
                    case SemCamera.METADATA_OBJECT_TRACKING_AF /*62275*/:
                        Log.m33i(SemCamera.TAG, "METADATA_OBJECT_TRACKING_AF");
                        if (SemCamera.this.mObjectTrackingAutoFocusCallback == null) {
                            Log.m31e(SemCamera.TAG, "mObjectTrackingAutoFocusCallback is null !!!");
                            break;
                        } else {
                            SemCamera.this.mObjectTrackingAutoFocusCallback.onObjectTrackingAutoFocus(new ObjectTrackingData((byte[]) message.obj));
                            break;
                        }
                    case SemCamera.METADATA_MULTI_AF /*62276*/:
                        if (SemCamera.this.mMultiAutoFocusCallback == null) {
                            Log.m31e(SemCamera.TAG, "mMultiAutoFocusCallback is null !!!");
                            break;
                        } else {
                            SemCamera.this.mMultiAutoFocusCallback.onMultiAutoFocus((byte[]) message.obj);
                            break;
                        }
                    case SemCamera.METADATA_BURSTSHOT_FPS_VALUE /*62277*/:
                        Log.m31e(SemCamera.TAG, "METADATA_BURSTSHOT_FPS_CHANGED(" + message.arg1 + ")");
                        if (SemCamera.this.mBurstShotFpsCallback == null) {
                            Log.m31e(SemCamera.TAG, "mBurstShotFpsCallback is null !!!");
                            break;
                        } else {
                            SemCamera.this.mBurstShotFpsCallback.onBurstShotFpsChanged(message.arg1);
                            break;
                        }
                    case SemCamera.AE_RESULT /*62289*/:
                        Log.m33i(SemCamera.TAG, "AE_RESULT(" + message.arg1 + ")");
                        if (SemCamera.this.mAutoExposureCallback == null) {
                            Log.m31e(SemCamera.TAG, "mAutoExposureCallback is null !!!");
                            break;
                        } else {
                            SemCamera.this.mAutoExposureCallback.onAutoExposure(message.arg1);
                            break;
                        }
                    case SemCamera.BRIGHTNESS_VALUE /*62290*/:
                        Log.m33i(SemCamera.TAG, "BRIGHTNESS_VALUE(" + (((float) message.arg1) / 256.0f) + ")");
                        if (SemCamera.this.mBrightnessValueCallback == null) {
                            Log.m31e(SemCamera.TAG, "mBrightnessValueCallback is null !!!");
                            break;
                        } else {
                            SemCamera.this.mBrightnessValueCallback.onBrightnessValue(((float) message.arg1) / 256.0f);
                            break;
                        }
                    case SemCamera.INTERACTIVE_SHOT_DIRECTION_CHANGED /*62305*/:
                        Log.m33i(SemCamera.TAG, "INTERACTIVE_SHOT_DIRECTION_CHANGED");
                        if (SemCamera.this.mInteractiveShotEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mInteractiveShotEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mInteractiveShotEventListener.onInteractiveDirectionChanged(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.INTERACTIVE_SHOT_DIRECTION_WARNING /*62306*/:
                        Log.m33i(SemCamera.TAG, "INTERACTIVE_SHOT_DIRECTION_WARNING");
                        if (SemCamera.this.mInteractiveShotEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mInteractiveShotEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mInteractiveShotEventListener.onInteractiveDirectionWarning(message.arg1);
                            break;
                        }
                    case SemCamera.INTERACTIVE_SHOT_CAPTURE_PROGRESS /*62307*/:
                        Log.m33i(SemCamera.TAG, "INTERACTIVE_SHOT_CAPTURE_PROGRESS");
                        if (SemCamera.this.mInteractiveShotEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mInteractiveShotEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mInteractiveShotEventListener.onInteractiveCaptureProgressed(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.INTERACTIVE_SHOT_PROCESS_PROGRESS /*62308*/:
                        Log.m33i(SemCamera.TAG, "INTERACTIVE_SHOT_PROCESS_PROGRESS");
                        if (SemCamera.this.mInteractiveShotEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mInteractiveShotEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mInteractiveShotEventListener.onInteractiveProcessProgressed(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.INTERACTIVE_SHOT_PROCESS_COMPLETE /*62309*/:
                        Log.m33i(SemCamera.TAG, "INTERACTIVE_SHOT_PROCESS_COMPLETE");
                        if (SemCamera.this.mInteractiveShotEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mInteractiveShotEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mInteractiveShotEventListener.onInteractiveProcessCompleted();
                            break;
                        }
                    case SemCamera.MOTION_PANORAMA_SHOT_ERR /*62337*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaError(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_RECT_CENTER_POINT /*62338*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaRectChanged(message.arg1, message.arg2);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_PROGRESS_STITCHING /*62339*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaProgressStitching(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_CAPTURED /*62340*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaCaptured();
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_CAPTURE_RESULT /*62341*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaCaptureResult(message.arg1 == 1);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_DIR /*62342*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaDirectionChanged(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_THUMBNAIL_DATA /*62343*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaThumbnailData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_LIVE_PREVIEW_DATA /*62344*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaLivePreviewData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_UIIMAGE_DATA /*62345*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaUiImageData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_CAPTURED_MAX_FRAMES /*62346*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaCapturedMaxFrames();
                            break;
                        }
                        break;
                    case SemCamera.MOTION_PANORAMA_SHOT_SLOW_MOVE /*62347*/:
                        if (SemCamera.this.mMotionPanoramaEventListener != null) {
                            SemCamera.this.mMotionPanoramaEventListener.onMotionPanoramaSlowMove();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_ERR /*62353*/:
                        Log.m33i(SemCamera.TAG, "WIDE_MOTION_SELFIE_SHOT_ERR(" + message.arg1 + ")");
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieError(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_RECT_CENTER_POINT /*62354*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieRectChanged((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_CAPTURED_NEW /*62355*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieCapturedNew();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_PROGRESS_STITCHING /*62356*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieProgressStitching(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_CAPTURED /*62357*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieCaptured();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_DIR /*62358*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieDirectionChanged(message.arg1);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_LIVE_PREVIEW_DATA /*62360*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieLivePreviewData((byte[]) message.obj);
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_CAPTURED_MAX_FRAMES /*62361*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieCapturedMaxFrames();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_MOVE_SLOWLY /*62362*/:
                        if (SemCamera.this.mWideMotionSelfieEventListener != null) {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieSlowMove();
                            break;
                        }
                        break;
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_NEXT_CAPTURE_POSITION /*62363*/:
                        Log.m33i(SemCamera.TAG, "WIDE_MOTION_SELFIE_SHOT_NEXT_CAPTURE_POSITION");
                        if (SemCamera.this.mWideMotionSelfieEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mWideMotionSelfieEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieNextCapturePosition(message.arg1, message.arg2);
                            break;
                        }
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_SINGLE_CAPTURE_DONE /*62364*/:
                        Log.m33i(SemCamera.TAG, "WIDE_MOTION_SELFIE_SHOT_SINGLE_CAPTURE_DONE");
                        if (SemCamera.this.mWideMotionSelfieEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mWideMotionSelfieEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieSingleCaptureDone();
                            break;
                        }
                    case SemCamera.WIDE_MOTION_SELFIE_SHOT_PROCESS_COMPLETE /*62365*/:
                        Log.m33i(SemCamera.TAG, "WIDE_MOTION_SELFIE_SHOT_PROCESS_COMPLETE");
                        if (SemCamera.this.mWideMotionSelfieEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mWideMotionSelfieEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mWideMotionSelfieEventListener.onWideMotionSelfieCompleted();
                            break;
                        }
                    case SemCamera.CAMERA_EXTRA_INFO_NOTIFY /*62465*/:
                        if (SemCamera.this.mExtraInfoListener != null) {
                            SemCamera.this.mExtraInfoListener.onExtraInfo(message.arg1, message.arg2, this.mCamera);
                        }
                        Trace.traceEnd(1024);
                        return;
                    case SemCamera.COMMON_SHOT_CANCEL_PICTURE_COMPLETED /*62481*/:
                        Log.m33i(SemCamera.TAG, "COMMON_SHOT_CANCEL_PICTURE_COMPLETED");
                        if (SemCamera.this.mCommonEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mCommonEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mCommonEventListener.onTakePictureCanceled();
                            break;
                        }
                    case SemCamera.COMMON_SHOT_PREVIEW_STARTED /*62482*/:
                        Log.m33i(SemCamera.TAG, "COMMON_SHOT_PREVIEW_STARTED");
                        if (SemCamera.this.mCommonEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mCommonEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mCommonEventListener.onPreviewStarted();
                            break;
                        }
                    case SemCamera.SCREEN_FLASH_TAKEPICTURE_COMPLETED /*62497*/:
                        Log.m33i(SemCamera.TAG, "SCREEN_FLASH_TAKEPICTURE_COMPLETED");
                        if (SemCamera.this.mScreenFlashEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mScreenFlashEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mScreenFlashEventListener.onTakePictureCompleted();
                            break;
                        }
                    case SemCamera.CAMERA_MSG_DEPTH_MAP_DATA /*62498*/:
                        if (SemCamera.this.mDepthMapEventListener == null) {
                            Log.m31e(SemCamera.TAG, "mDepthMapEventListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mDepthMapEventListener.onDepthMapData((byte[]) message.obj);
                            break;
                        }
                    case SemCamera.SMART_FILTER_PARAMETERS_CHANGED /*62513*/:
                        Log.m33i(SemCamera.TAG, "SMART_FILTER_PARAMETERS_CHANGED");
                        if (SemCamera.this.mSmartFilterListener == null) {
                            Log.m31e(SemCamera.TAG, "mSmartFilterListener is null !!!");
                            break;
                        } else {
                            SemCamera.this.mSmartFilterListener.onParametersChanged((byte[]) message.obj);
                            break;
                        }
                    case 65536:
                        if (SemCamera.this.mIrisDataCallback != null) {
                            ParcelFileDescriptor parcelFileDescriptor2 = null;
                            try {
                                SemCamera.this.irisIrLock.lock();
                                if (!SemCamera.this.mIrisIrCallbackEnabled) {
                                    Log.m31e(SemCamera.TAG, "Check if IrisIrCallback is Enabled or not");
                                } else if (SemCamera.this.mIrisIrQueue != null && SemCamera.this.mIrisIrQueue.size() > 0) {
                                    parcelFileDescriptor2 = (ParcelFileDescriptor) SemCamera.this.mIrisIrQueue.poll();
                                    Log.m29d(SemCamera.TAG, "handleMessage: onIrisDataCallback irisIrFd = " + parcelFileDescriptor2.getFileDescriptor().getInt$());
                                }
                            } catch (Throwable e22) {
                                Log.m32e(SemCamera.TAG, "CAMERA_MSG_IRIS_DATA : onIrisDataCallback fail", e22);
                            } finally {
                                SemCamera.this.irisIrLock.unlock();
                            }
                            if (parcelFileDescriptor2 != null) {
                                try {
                                    SemCamera.this.mIrisDataCallback.onIrisDataCallback(parcelFileDescriptor2, this.mCamera);
                                    parcelFileDescriptor2.close();
                                } catch (Throwable e222) {
                                    Log.m32e(SemCamera.TAG, "CAMERA_MSG_IRIS_DATA : ", e222);
                                }
                            }
                        } else {
                            Log.m31e(SemCamera.TAG, "mIrisDataCallback is null !!!");
                        }
                        Trace.traceEnd(1024);
                        return;
                    default:
                        if (SemCamera.this.mPostEventListener != null) {
                            SemCamera.this.mPostEventListener.onHandleMessage(message.what, message.arg1, message.arg2, message.obj);
                        } else {
                            Log.m31e(SemCamera.TAG, "Unknown message type " + message.what);
                        }
                        Trace.traceEnd(1024);
                        return;
                }
                Trace.traceEnd(1024);
                return;
                SemCamera.this.irisPreviewLock.unlock();
            } catch (Throwable e2222) {
                Log.m32e(SemCamera.TAG, "CAMERA_MSG_PREVIEW_FRAME", e2222);
            } catch (Throwable th) {
                Trace.traceEnd(1024);
            }
        }
    }

    public interface ExtraInfoListener {
        void onExtraInfo(int i, int i2, SemCamera semCamera);
    }

    public static class Face {
        public int id = -1;
        public Point leftEye = null;
        public Point mouth = null;
        public Rect rect;
        public Point rightEye = null;
        public int score;
    }

    public interface FaceDetectionListener {
        void onFaceDetection(Face[] faceArr, SemCamera semCamera);
    }

    public interface FoodShotEventListener {
        void onFoodShotComplete(byte[] bArr);
    }

    public interface HardwareFaceDetectionListener {
        void onHardwareFaceDetection(Face[] faceArr, SemCamera semCamera);
    }

    public interface HazeRemovalEventListener {
        void onHazeRemovalCapturingProgressed(int i, int i2);
    }

    public interface HdrEventListener {
        void onHdrAllProgressCompleted(boolean z);

        void onHdrResultCompleted(boolean z);

        void onHdrResultProgress(int i);

        void onHdrResultStarted();
    }

    public interface ImageEffectEventListener {
        void onImageEffectInfo(String str);
    }

    public interface InteractiveShotEventListener {
        void onInteractiveCaptureProgressed(int i, int i2);

        void onInteractiveDirectionChanged(int i, int i2);

        void onInteractiveDirectionWarning(int i);

        void onInteractiveProcessCompleted();

        void onInteractiveProcessProgressed(int i, int i2);
    }

    public interface IrisDataCallback {
        void onIrisDataCallback(ParcelFileDescriptor parcelFileDescriptor, SemCamera semCamera);
    }

    public interface LightConditionChangedListener {
        void onLightConditionChanged(int i);
    }

    public interface MotionPanoramaEventListener {
        void onMotionPanoramaCaptureResult(boolean z);

        void onMotionPanoramaCaptured();

        void onMotionPanoramaCapturedMaxFrames();

        void onMotionPanoramaDirectionChanged(int i);

        void onMotionPanoramaError(int i);

        void onMotionPanoramaLivePreviewData(byte[] bArr);

        void onMotionPanoramaProgressStitching(int i);

        void onMotionPanoramaRectChanged(int i, int i2);

        void onMotionPanoramaSlowMove();

        void onMotionPanoramaThumbnailData(byte[] bArr);

        void onMotionPanoramaUiImageData(byte[] bArr);
    }

    public interface MultiAutoFocusCallback {
        void onMultiAutoFocus(byte[] bArr);
    }

    public interface MultiFrameEventListener {
        void onMultiFrameCapturingProgressed(int i, int i2);
    }

    public interface ObjectTrackingAutoFocusCallback {
        void onObjectTrackingAutoFocus(ObjectTrackingData objectTrackingData);
    }

    public static class ObjectTrackingData {
        public short focusState;
        public Rect focusedArea = null;

        public ObjectTrackingData(byte[] bArr) {
            this.focusState = (short) ((bArr[0] & 255) | ((bArr[1] & 255) << 8));
            this.focusedArea = new Rect((short) ((bArr[2] & 255) | ((bArr[3] & 255) << 8)), (short) ((bArr[4] & 255) | ((bArr[5] & 255) << 8)), (short) ((bArr[6] & 255) | ((bArr[7] & 255) << 8)), (short) ((bArr[8] & 255) | ((bArr[9] & 255) << 8)));
        }
    }

    public interface OnZoomChangeListener {
        void onZoomChange(int i, boolean z, SemCamera semCamera);
    }

    private class PafResult {
        private short driverResolution;
        private short focused;
        private short goalPos;
        private short lensPositionCurrent;
        private short mode;
        private short reliability;

        private PafResult(byte[] bArr) {
            int i = 1 + 1;
            this.mode = (short) ((bArr[0] & 255) | ((bArr[1] & 255) << 8));
            int i2 = i + 1;
            i = i2 + 1;
            this.goalPos = (short) ((bArr[i] & 255) | ((bArr[i2] & 255) << 8));
            i2 = i + 1;
            i = i2 + 1;
            this.reliability = (short) ((bArr[i] & 255) | ((bArr[i2] & 255) << 8));
            i2 = i + 1;
            i = i2 + 1;
            this.focused = (short) ((bArr[i] & 255) | ((bArr[i2] & 255) << 8));
            i2 = i + 1;
            i = i2 + 1;
            this.lensPositionCurrent = (short) ((bArr[i] & 255) | ((bArr[i2] & 255) << 8));
            i2 = i + 1;
            i = i2 + 1;
            this.driverResolution = (short) ((bArr[i] & 255) | ((bArr[i2] & 255) << 8));
        }
    }

    public interface PanoramaEventListener {
        void onPanoramaCaptured();

        void onPanoramaCapturedMaxFrames();

        void onPanoramaCapturedNew();

        void onPanoramaDirectionChanged(int i);

        void onPanoramaError(int i);

        void onPanoramaLivePreviewData(byte[] bArr);

        void onPanoramaLowResolutionData(byte[] bArr);

        void onPanoramaProgressStitching(int i);

        void onPanoramaRectChanged(int i, int i2);

        void onPanoramaSlowMove();
    }

    public class Parameters {
        public static final String ANTIBANDING_50HZ = "50hz";
        public static final String ANTIBANDING_60HZ = "60hz";
        public static final String ANTIBANDING_AUTO = "auto";
        public static final String ANTIBANDING_OFF = "off";
        public static final String EFFECT_AQUA = "aqua";
        public static final String EFFECT_BLACKBOARD = "blackboard";
        public static final String EFFECT_MONO = "mono";
        public static final String EFFECT_NEGATIVE = "negative";
        public static final String EFFECT_NONE = "none";
        public static final String EFFECT_POSTERIZE = "posterize";
        public static final String EFFECT_SEPIA = "sepia";
        public static final String EFFECT_SOLARIZE = "solarize";
        public static final String EFFECT_WHITEBOARD = "whiteboard";
        private static final String FALSE = "false";
        public static final String FLASH_MODE_AUTO = "auto";
        public static final String FLASH_MODE_OFF = "off";
        public static final String FLASH_MODE_ON = "on";
        public static final String FLASH_MODE_RED_EYE = "red-eye";
        public static final String FLASH_MODE_TORCH = "torch";
        public static final int FOCUS_DISTANCE_FAR_INDEX = 2;
        public static final int FOCUS_DISTANCE_NEAR_INDEX = 0;
        public static final int FOCUS_DISTANCE_OPTIMAL_INDEX = 1;
        public static final String FOCUS_MODE_AUTO = "auto";
        public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";
        public static final String FOCUS_MODE_CONTINUOUS_VIDEO = "continuous-video";
        public static final String FOCUS_MODE_EDOF = "edof";
        public static final String FOCUS_MODE_FIXED = "fixed";
        public static final String FOCUS_MODE_INFINITY = "infinity";
        public static final String FOCUS_MODE_MACRO = "macro";
        private static final String KEY_ANTIBANDING = "antibanding";
        private static final String KEY_AUTO_EXPOSURE_LOCK = "auto-exposure-lock";
        private static final String KEY_AUTO_EXPOSURE_LOCK_SUPPORTED = "auto-exposure-lock-supported";
        private static final String KEY_AUTO_WHITEBALANCE_LOCK = "auto-whitebalance-lock";
        private static final String KEY_AUTO_WHITEBALANCE_LOCK_SUPPORTED = "auto-whitebalance-lock-supported";
        private static final String KEY_CITYID = "contextualtag-cityid";
        private static final String KEY_EFFECT = "effect";
        private static final String KEY_EXPOSURE_COMPENSATION = "exposure-compensation";
        private static final String KEY_EXPOSURE_COMPENSATION_STEP = "exposure-compensation-step";
        private static final String KEY_FLASH_MODE = "flash-mode";
        private static final String KEY_FOCAL_LENGTH = "focal-length";
        private static final String KEY_FOCUS_AREAS = "focus-areas";
        private static final String KEY_FOCUS_DISTANCES = "focus-distances";
        private static final String KEY_FOCUS_MODE = "focus-mode";
        private static final String KEY_GPS_ALTITUDE = "gps-altitude";
        private static final String KEY_GPS_LATITUDE = "gps-latitude";
        private static final String KEY_GPS_LONGITUDE = "gps-longitude";
        private static final String KEY_GPS_PROCESSING_METHOD = "gps-processing-method";
        private static final String KEY_GPS_TIMESTAMP = "gps-timestamp";
        private static final String KEY_HORIZONTAL_VIEW_ANGLE = "horizontal-view-angle";
        private static final String KEY_JPEG_QUALITY = "jpeg-quality";
        private static final String KEY_JPEG_THUMBNAIL_HEIGHT = "jpeg-thumbnail-height";
        private static final String KEY_JPEG_THUMBNAIL_QUALITY = "jpeg-thumbnail-quality";
        private static final String KEY_JPEG_THUMBNAIL_SIZE = "jpeg-thumbnail-size";
        private static final String KEY_JPEG_THUMBNAIL_WIDTH = "jpeg-thumbnail-width";
        private static final String KEY_MAX_EXPOSURE_COMPENSATION = "max-exposure-compensation";
        private static final String KEY_MAX_NUM_DETECTED_FACES_HW = "max-num-detected-faces-hw";
        private static final String KEY_MAX_NUM_DETECTED_FACES_SW = "max-num-detected-faces-sw";
        private static final String KEY_MAX_NUM_FOCUS_AREAS = "max-num-focus-areas";
        private static final String KEY_MAX_NUM_METERING_AREAS = "max-num-metering-areas";
        private static final String KEY_MAX_ZOOM = "max-zoom";
        private static final String KEY_METERING_AREAS = "metering-areas";
        private static final String KEY_MIN_EXPOSURE_COMPENSATION = "min-exposure-compensation";
        private static final String KEY_PICTURE_FORMAT = "picture-format";
        private static final String KEY_PICTURE_SIZE = "picture-size";
        private static final String KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO = "preferred-preview-size-for-video";
        private static final String KEY_PREVIEW_FORMAT = "preview-format";
        private static final String KEY_PREVIEW_FPS_RANGE = "preview-fps-range";
        private static final String KEY_PREVIEW_FRAME_RATE = "preview-frame-rate";
        private static final String KEY_PREVIEW_SIZE = "preview-size";
        private static final String KEY_RECORDING_HINT = "recording-hint";
        private static final String KEY_ROTATION = "rotation";
        private static final String KEY_SCENE_MODE = "scene-mode";
        private static final String KEY_SMOOTH_ZOOM_SUPPORTED = "smooth-zoom-supported";
        private static final String KEY_VERTICAL_VIEW_ANGLE = "vertical-view-angle";
        private static final String KEY_VIDEO_SIZE = "video-size";
        private static final String KEY_VIDEO_SNAPSHOT_SUPPORTED = "video-snapshot-supported";
        private static final String KEY_VIDEO_STABILIZATION = "video-stabilization";
        private static final String KEY_VIDEO_STABILIZATION_SUPPORTED = "video-stabilization-supported";
        private static final String KEY_WEATHER = "contextualtag-weather";
        private static final String KEY_WHITE_BALANCE = "whitebalance";
        private static final String KEY_ZOOM = "zoom";
        private static final String KEY_ZOOM_RATIOS = "zoom-ratios";
        private static final String KEY_ZOOM_SUPPORTED = "zoom-supported";
        private static final String PIXEL_FORMAT_BAYER_RGGB = "bayer-rggb";
        private static final String PIXEL_FORMAT_JPEG = "jpeg";
        private static final String PIXEL_FORMAT_RGB565 = "rgb565";
        private static final String PIXEL_FORMAT_YUV420P = "yuv420p";
        private static final String PIXEL_FORMAT_YUV420SP = "yuv420sp";
        private static final String PIXEL_FORMAT_YUV422I = "yuv422i-yuyv";
        private static final String PIXEL_FORMAT_YUV422SP = "yuv422sp";
        public static final int PREVIEW_FPS_MAX_INDEX = 1;
        public static final int PREVIEW_FPS_MIN_INDEX = 0;
        public static final String SCENE_MODE_ACTION = "action";
        public static final String SCENE_MODE_AUTO = "auto";
        public static final String SCENE_MODE_BARCODE = "barcode";
        public static final String SCENE_MODE_BEACH = "beach";
        public static final String SCENE_MODE_CANDLELIGHT = "candlelight";
        public static final String SCENE_MODE_FIREWORKS = "fireworks";
        public static final String SCENE_MODE_HDR = "hdr";
        public static final String SCENE_MODE_LANDSCAPE = "landscape";
        public static final String SCENE_MODE_NIGHT = "night";
        public static final String SCENE_MODE_NIGHT_PORTRAIT = "night-portrait";
        public static final String SCENE_MODE_PARTY = "party";
        public static final String SCENE_MODE_PORTRAIT = "portrait";
        public static final String SCENE_MODE_SNOW = "snow";
        public static final String SCENE_MODE_SPORTS = "sports";
        public static final String SCENE_MODE_STEADYPHOTO = "steadyphoto";
        public static final String SCENE_MODE_SUNSET = "sunset";
        public static final String SCENE_MODE_THEATRE = "theatre";
        private static final String SUPPORTED_VALUES_SUFFIX = "-values";
        private static final String TRUE = "true";
        public static final String WHITE_BALANCE_AUTO = "auto";
        public static final String WHITE_BALANCE_CLOUDY_DAYLIGHT = "cloudy-daylight";
        public static final String WHITE_BALANCE_DAYLIGHT = "daylight";
        public static final String WHITE_BALANCE_FLUORESCENT = "fluorescent";
        public static final String WHITE_BALANCE_INCANDESCENT = "incandescent";
        public static final String WHITE_BALANCE_SHADE = "shade";
        public static final String WHITE_BALANCE_TWILIGHT = "twilight";
        public static final String WHITE_BALANCE_WARM_FLUORESCENT = "warm-fluorescent";
        private final LinkedHashMap<String, String> mMap;

        private Parameters() {
            this.mMap = new LinkedHashMap(64);
        }

        private String cameraFormatForPixelFormat(int i) {
            switch (i) {
                case 4:
                    return PIXEL_FORMAT_RGB565;
                case 16:
                    return PIXEL_FORMAT_YUV422SP;
                case 17:
                    return PIXEL_FORMAT_YUV420SP;
                case 20:
                    return PIXEL_FORMAT_YUV422I;
                case 256:
                    return PIXEL_FORMAT_JPEG;
                case 842094169:
                    return PIXEL_FORMAT_YUV420P;
                default:
                    return null;
            }
        }

        private synchronized float getFloat(String str, float f) {
            try {
            } catch (NumberFormatException e) {
                return f;
            }
            return Float.parseFloat((String) this.mMap.get(str));
        }

        private synchronized int getInt(String str, int i) {
            try {
            } catch (NumberFormatException e) {
                return i;
            }
            return Integer.parseInt((String) this.mMap.get(str));
        }

        private SemCamera getOuter() {
            return SemCamera.this;
        }

        private int pixelFormatForCameraFormat(String str) {
            return str == null ? 0 : str.equals(PIXEL_FORMAT_YUV422SP) ? 16 : str.equals(PIXEL_FORMAT_YUV420SP) ? 17 : str.equals(PIXEL_FORMAT_YUV422I) ? 20 : str.equals(PIXEL_FORMAT_YUV420P) ? 842094169 : str.equals(PIXEL_FORMAT_RGB565) ? 4 : str.equals(PIXEL_FORMAT_JPEG) ? 256 : 0;
        }

        private void put(String str, String str2) {
            this.mMap.remove(str);
            this.mMap.put(str, str2);
        }

        private boolean same(String str, String str2) {
            return (str == null && str2 == null) ? true : str != null && str.equals(str2);
        }

        private synchronized void set(String str, List<Area> list) {
            if (list == null) {
                set(str, "(0,0,0,0,0)");
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    Area area = (Area) list.get(i);
                    Rect rect = area.rect;
                    stringBuilder.append('(');
                    stringBuilder.append(rect.left);
                    stringBuilder.append(',');
                    stringBuilder.append(rect.top);
                    stringBuilder.append(',');
                    stringBuilder.append(rect.right);
                    stringBuilder.append(',');
                    stringBuilder.append(rect.bottom);
                    stringBuilder.append(',');
                    stringBuilder.append(area.weight);
                    stringBuilder.append(')');
                    if (i != list.size() - 1) {
                        stringBuilder.append(',');
                    }
                }
                set(str, stringBuilder.toString());
            }
        }

        private ArrayList<String> split(String str) {
            if (str == null) {
                return null;
            }
            Iterable<String> simpleStringSplitter = new SimpleStringSplitter(',');
            simpleStringSplitter.setString(str);
            ArrayList<String> arrayList = new ArrayList();
            for (String add : simpleStringSplitter) {
                arrayList.add(add);
            }
            return arrayList;
        }

        private ArrayList<Area> splitArea(String str) {
            if (str != null && str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
                ArrayList<Area> arrayList = new ArrayList();
                int i = 1;
                int[] iArr = new int[5];
                int indexOf;
                do {
                    indexOf = str.indexOf("),(", i);
                    if (indexOf == -1) {
                        indexOf = str.length() - 1;
                    }
                    splitInt(str.substring(i, indexOf), iArr);
                    arrayList.add(new Area(new Rect(iArr[0], iArr[1], iArr[2], iArr[3]), iArr[4]));
                    i = indexOf + 3;
                } while (indexOf != str.length() - 1);
                if (arrayList.size() == 0) {
                    return null;
                }
                if (arrayList.size() == 1) {
                    Area area = (Area) arrayList.get(0);
                    Rect rect = area.rect;
                    return (rect.left == 0 && rect.top == 0 && rect.right == 0 && rect.bottom == 0 && area.weight == 0) ? null : arrayList;
                }
            }
            Log.m31e(SemCamera.TAG, "Invalid area string=" + str);
            return null;
        }

        private void splitFloat(String str, float[] fArr) {
            if (str != null) {
                Iterable<String> simpleStringSplitter = new SimpleStringSplitter(',');
                simpleStringSplitter.setString(str);
                int i = 0;
                for (String parseFloat : simpleStringSplitter) {
                    int i2 = i + 1;
                    fArr[i] = Float.parseFloat(parseFloat);
                    i = i2;
                }
            }
        }

        private ArrayList<Integer> splitInt(String str) {
            if (str == null) {
                return null;
            }
            Iterable<String> simpleStringSplitter = new SimpleStringSplitter(',');
            simpleStringSplitter.setString(str);
            ArrayList<Integer> arrayList = new ArrayList();
            for (String parseInt : simpleStringSplitter) {
                arrayList.add(Integer.valueOf(Integer.parseInt(parseInt)));
            }
            return arrayList.size() == 0 ? null : arrayList;
        }

        private void splitInt(String str, int[] iArr) {
            if (str != null) {
                Iterable<String> simpleStringSplitter = new SimpleStringSplitter(',');
                simpleStringSplitter.setString(str);
                int i = 0;
                for (String parseInt : simpleStringSplitter) {
                    int i2 = i + 1;
                    iArr[i] = Integer.parseInt(parseInt);
                    i = i2;
                }
            }
        }

        private ArrayList<int[]> splitRange(String str) {
            if (str != null && str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
                ArrayList<int[]> arrayList = new ArrayList();
                int i = 1;
                int indexOf;
                do {
                    Object obj = new int[2];
                    indexOf = str.indexOf("),(", i);
                    if (indexOf == -1) {
                        indexOf = str.length() - 1;
                    }
                    splitInt(str.substring(i, indexOf), obj);
                    arrayList.add(obj);
                    i = indexOf + 3;
                } while (indexOf != str.length() - 1);
                return arrayList.size() == 0 ? null : arrayList;
            } else {
                Log.m31e(SemCamera.TAG, "Invalid range list string=" + str);
                return null;
            }
        }

        private ArrayList<Size> splitSize(String str) {
            if (str == null) {
                return null;
            }
            Iterable<String> simpleStringSplitter = new SimpleStringSplitter(',');
            simpleStringSplitter.setString(str);
            ArrayList<Size> arrayList = new ArrayList();
            for (String strToSize : simpleStringSplitter) {
                Size strToSize2 = strToSize(strToSize);
                if (strToSize2 != null) {
                    arrayList.add(strToSize2);
                }
            }
            return arrayList.size() == 0 ? null : arrayList;
        }

        private Size strToSize(String str) {
            if (str == null) {
                return null;
            }
            int indexOf = str.indexOf(120);
            if (indexOf != -1) {
                return new Size(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(indexOf + 1)));
            }
            Log.m31e(SemCamera.TAG, "Invalid size parameter string=" + str);
            return null;
        }

        public void copyFrom(Parameters parameters) {
            if (parameters == null) {
                throw new NullPointerException("other must not be null");
            }
            this.mMap.putAll(parameters.mMap);
        }

        @Deprecated
        public synchronized void dump() {
            Log.m31e(SemCamera.TAG, "dump: size=" + this.mMap.size());
            for (String str : this.mMap.keySet()) {
                Log.m31e(SemCamera.TAG, "dump: " + str + "=" + ((String) this.mMap.get(str)));
            }
        }

        public synchronized String flatten() {
            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder(128);
            for (String str : this.mMap.keySet()) {
                stringBuilder.append(str);
                stringBuilder.append("=");
                stringBuilder.append((String) this.mMap.get(str));
                stringBuilder.append(SemSmartGlow.PACKAGE_SEPARATOR);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

        public synchronized String get(String str) {
            return (String) this.mMap.get(str);
        }

        public String getAntibanding() {
            return get(KEY_ANTIBANDING);
        }

        public boolean getAutoExposureLock() {
            return TRUE.equals(get(KEY_AUTO_EXPOSURE_LOCK));
        }

        public boolean getAutoWhiteBalanceLock() {
            return TRUE.equals(get(KEY_AUTO_WHITEBALANCE_LOCK));
        }

        public String getColorEffect() {
            return get(KEY_EFFECT);
        }

        public int getExposureCompensation() {
            return getInt(KEY_EXPOSURE_COMPENSATION, 0);
        }

        public float getExposureCompensationStep() {
            return getFloat(KEY_EXPOSURE_COMPENSATION_STEP, 0.0f);
        }

        public String getFlashMode() {
            return get(KEY_FLASH_MODE);
        }

        public float getFocalLength() {
            return Float.parseFloat(get(KEY_FOCAL_LENGTH));
        }

        public List<Area> getFocusAreas() {
            return splitArea(get(KEY_FOCUS_AREAS));
        }

        public void getFocusDistances(float[] fArr) {
            if (fArr == null || fArr.length != 3) {
                throw new IllegalArgumentException("output must be a float array with three elements.");
            }
            splitFloat(get(KEY_FOCUS_DISTANCES), fArr);
        }

        public String getFocusMode() {
            return get(KEY_FOCUS_MODE);
        }

        public float getHorizontalViewAngle() {
            return Float.parseFloat(get(KEY_HORIZONTAL_VIEW_ANGLE));
        }

        public synchronized int getInt(String str) {
            return Integer.parseInt((String) this.mMap.get(str));
        }

        public int getJpegQuality() {
            return getInt(KEY_JPEG_QUALITY);
        }

        public int getJpegThumbnailQuality() {
            return getInt(KEY_JPEG_THUMBNAIL_QUALITY);
        }

        public Size getJpegThumbnailSize() {
            return new Size(getInt(KEY_JPEG_THUMBNAIL_WIDTH), getInt(KEY_JPEG_THUMBNAIL_HEIGHT));
        }

        public int getMaxExposureCompensation() {
            return getInt(KEY_MAX_EXPOSURE_COMPENSATION, 0);
        }

        public int getMaxNumDetectedFaces() {
            return getInt(KEY_MAX_NUM_DETECTED_FACES_HW, 0);
        }

        public int getMaxNumFocusAreas() {
            return getInt(KEY_MAX_NUM_FOCUS_AREAS, 0);
        }

        public int getMaxNumMeteringAreas() {
            return getInt(KEY_MAX_NUM_METERING_AREAS, 0);
        }

        public int getMaxZoom() {
            return getInt(KEY_MAX_ZOOM, 0);
        }

        public List<Area> getMeteringAreas() {
            return splitArea(get(KEY_METERING_AREAS));
        }

        public int getMinExposureCompensation() {
            return getInt(KEY_MIN_EXPOSURE_COMPENSATION, 0);
        }

        public int getPictureFormat() {
            return pixelFormatForCameraFormat(get(KEY_PICTURE_FORMAT));
        }

        public Size getPictureSize() {
            return strToSize(get(KEY_PICTURE_SIZE));
        }

        public Size getPreferredPreviewSizeForVideo() {
            return strToSize(get(KEY_PREFERRED_PREVIEW_SIZE_FOR_VIDEO));
        }

        public int getPreviewFormat() {
            return pixelFormatForCameraFormat(get(KEY_PREVIEW_FORMAT));
        }

        public void getPreviewFpsRange(int[] iArr) {
            if (iArr == null || iArr.length != 2) {
                throw new IllegalArgumentException("range must be an array with two elements.");
            }
            splitInt(get(KEY_PREVIEW_FPS_RANGE), iArr);
        }

        @Deprecated
        public int getPreviewFrameRate() {
            return getInt(KEY_PREVIEW_FRAME_RATE);
        }

        public Size getPreviewSize() {
            return strToSize(get(KEY_PREVIEW_SIZE));
        }

        public String getSceneMode() {
            return get(KEY_SCENE_MODE);
        }

        public List<String> getSupportedAntibanding() {
            return split(get("antibanding-values"));
        }

        public List<String> getSupportedColorEffects() {
            return split(get("effect-values"));
        }

        public List<String> getSupportedFlashModes() {
            return split(get("flash-mode-values"));
        }

        public List<String> getSupportedFocusModes() {
            return split(get("focus-mode-values"));
        }

        public List<Size> getSupportedJpegThumbnailSizes() {
            return splitSize(get("jpeg-thumbnail-size-values"));
        }

        public List<Integer> getSupportedPictureFormats() {
            String str = get("picture-format-values");
            List arrayList = new ArrayList();
            for (String pixelFormatForCameraFormat : split(str)) {
                int pixelFormatForCameraFormat2 = pixelFormatForCameraFormat(pixelFormatForCameraFormat);
                if (pixelFormatForCameraFormat2 != 0) {
                    arrayList.add(Integer.valueOf(pixelFormatForCameraFormat2));
                }
            }
            return arrayList;
        }

        public List<Size> getSupportedPictureSizes() {
            return splitSize(get("picture-size-values"));
        }

        public List<Integer> getSupportedPreviewFormats() {
            String str = get("preview-format-values");
            List arrayList = new ArrayList();
            for (String pixelFormatForCameraFormat : split(str)) {
                int pixelFormatForCameraFormat2 = pixelFormatForCameraFormat(pixelFormatForCameraFormat);
                if (pixelFormatForCameraFormat2 != 0) {
                    arrayList.add(Integer.valueOf(pixelFormatForCameraFormat2));
                }
            }
            return arrayList;
        }

        public List<int[]> getSupportedPreviewFpsRanges() {
            return splitRange(get("preview-fps-range-values"));
        }

        @Deprecated
        public List<Integer> getSupportedPreviewFrameRates() {
            return splitInt(get("preview-frame-rate-values"));
        }

        public List<Size> getSupportedPreviewSizes() {
            return splitSize(get("preview-size-values"));
        }

        public List<String> getSupportedSceneModes() {
            return split(get("scene-mode-values"));
        }

        public List<Size> getSupportedVideoSizes() {
            return splitSize(get("video-size-values"));
        }

        public List<String> getSupportedWhiteBalances() {
            return split(get("whitebalance-values"));
        }

        public float getVerticalViewAngle() {
            return Float.parseFloat(get(KEY_VERTICAL_VIEW_ANGLE));
        }

        public boolean getVideoStabilization() {
            return TRUE.equals(get(KEY_VIDEO_STABILIZATION));
        }

        public String getWhiteBalance() {
            return get(KEY_WHITE_BALANCE);
        }

        public int getZoom() {
            return getInt(KEY_ZOOM, 0);
        }

        public List<Integer> getZoomRatios() {
            return splitInt(get(KEY_ZOOM_RATIOS));
        }

        public boolean isAutoExposureLockSupported() {
            return TRUE.equals(get(KEY_AUTO_EXPOSURE_LOCK_SUPPORTED));
        }

        public boolean isAutoWhiteBalanceLockSupported() {
            return TRUE.equals(get(KEY_AUTO_WHITEBALANCE_LOCK_SUPPORTED));
        }

        public boolean isSmoothZoomSupported() {
            return TRUE.equals(get(KEY_SMOOTH_ZOOM_SUPPORTED));
        }

        public boolean isVideoSnapshotSupported() {
            return TRUE.equals(get(KEY_VIDEO_SNAPSHOT_SUPPORTED));
        }

        public boolean isVideoStabilizationSupported() {
            return TRUE.equals(get(KEY_VIDEO_STABILIZATION_SUPPORTED));
        }

        public boolean isZoomSupported() {
            return TRUE.equals(get(KEY_ZOOM_SUPPORTED));
        }

        public synchronized void remove(String str) {
            this.mMap.remove(str);
        }

        public void removeGpsData() {
            remove(KEY_GPS_LATITUDE);
            remove(KEY_GPS_LONGITUDE);
            remove(KEY_GPS_ALTITUDE);
            remove(KEY_GPS_TIMESTAMP);
            remove(KEY_GPS_PROCESSING_METHOD);
        }

        public boolean same(Parameters parameters) {
            if (this == parameters) {
                return true;
            }
            return parameters != null ? this.mMap.equals(parameters.mMap) : false;
        }

        public synchronized void set(String str, int i) {
            put(str, Integer.toString(i));
        }

        public synchronized void set(String str, String str2) {
            if (str.indexOf(61) == -1 && str.indexOf(59) == -1) {
                if (str.indexOf(0) == -1) {
                    if (str2.indexOf(61) == -1 && str2.indexOf(59) == -1) {
                        if (str2.indexOf(0) == -1) {
                            put(str, str2);
                            return;
                        }
                    }
                    Log.m31e(SemCamera.TAG, "Value \"" + str2 + "\" contains invalid character (= or ; or \\0)");
                    return;
                }
            }
            Log.m31e(SemCamera.TAG, "Key \"" + str + "\" contains invalid character (= or ; or \\0)");
        }

        public void setAntibanding(String str) {
            set(KEY_ANTIBANDING, str);
        }

        public void setAutoExposureLock(boolean z) {
            set(KEY_AUTO_EXPOSURE_LOCK, z ? TRUE : FALSE);
        }

        public void setAutoWhiteBalanceLock(boolean z) {
            set(KEY_AUTO_WHITEBALANCE_LOCK, z ? TRUE : FALSE);
        }

        public void setCityId(long j) {
            set(KEY_CITYID, Long.toString(j));
        }

        public void setColorEffect(String str) {
            set(KEY_EFFECT, str);
        }

        public void setExposureCompensation(int i) {
            set(KEY_EXPOSURE_COMPENSATION, i);
        }

        public void setFlashMode(String str) {
            set(KEY_FLASH_MODE, str);
        }

        public void setFocusAreas(List<Area> list) {
            set(KEY_FOCUS_AREAS, (List) list);
        }

        public void setFocusMode(String str) {
            set(KEY_FOCUS_MODE, str);
        }

        public void setGpsAltitude(double d) {
            set(KEY_GPS_ALTITUDE, Double.toString(d));
        }

        public void setGpsLatitude(double d) {
            set(KEY_GPS_LATITUDE, Double.toString(d));
        }

        public void setGpsLongitude(double d) {
            set(KEY_GPS_LONGITUDE, Double.toString(d));
        }

        public void setGpsProcessingMethod(String str) {
            set(KEY_GPS_PROCESSING_METHOD, str);
        }

        public void setGpsTimestamp(long j) {
            set(KEY_GPS_TIMESTAMP, Long.toString(j));
        }

        public void setJpegQuality(int i) {
            set(KEY_JPEG_QUALITY, i);
        }

        public void setJpegThumbnailQuality(int i) {
            set(KEY_JPEG_THUMBNAIL_QUALITY, i);
        }

        public void setJpegThumbnailSize(int i, int i2) {
            set(KEY_JPEG_THUMBNAIL_WIDTH, i);
            set(KEY_JPEG_THUMBNAIL_HEIGHT, i2);
        }

        public void setMeteringAreas(List<Area> list) {
            set(KEY_METERING_AREAS, (List) list);
        }

        public void setPictureFormat(int i) {
            String cameraFormatForPixelFormat = cameraFormatForPixelFormat(i);
            if (cameraFormatForPixelFormat == null) {
                throw new IllegalArgumentException("Invalid pixel_format=" + i);
            }
            set(KEY_PICTURE_FORMAT, cameraFormatForPixelFormat);
        }

        public void setPictureSize(int i, int i2) {
            set(KEY_PICTURE_SIZE, Integer.toString(i) + "x" + Integer.toString(i2));
        }

        public void setPreviewFormat(int i) {
            String cameraFormatForPixelFormat = cameraFormatForPixelFormat(i);
            if (cameraFormatForPixelFormat == null) {
                throw new IllegalArgumentException("Invalid pixel_format=" + i);
            }
            set(KEY_PREVIEW_FORMAT, cameraFormatForPixelFormat);
        }

        public void setPreviewFpsRange(int i, int i2) {
            set(KEY_PREVIEW_FPS_RANGE, MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET + i + "," + i2);
        }

        @Deprecated
        public void setPreviewFrameRate(int i) {
            set(KEY_PREVIEW_FRAME_RATE, i);
        }

        public void setPreviewSize(int i, int i2) {
            set(KEY_PREVIEW_SIZE, Integer.toString(i) + "x" + Integer.toString(i2));
        }

        public void setRecordingHint(boolean z) {
            set(KEY_RECORDING_HINT, z ? TRUE : FALSE);
        }

        public void setRotation(int i) {
            if (i == 0 || i == 90 || i == 180 || i == 270) {
                set(KEY_ROTATION, Integer.toString(i));
                return;
            }
            throw new IllegalArgumentException("Invalid rotation=" + i);
        }

        public void setSceneMode(String str) {
            set(KEY_SCENE_MODE, str);
        }

        public void setVideoStabilization(boolean z) {
            set(KEY_VIDEO_STABILIZATION, z ? TRUE : FALSE);
        }

        public void setWeather(int i) {
            set(KEY_WEATHER, i);
        }

        public void setWhiteBalance(String str) {
            if (!same(str, get(KEY_WHITE_BALANCE))) {
                set(KEY_WHITE_BALANCE, str);
                set(KEY_AUTO_WHITEBALANCE_LOCK, FALSE);
            }
        }

        public void setZoom(int i) {
            set(KEY_ZOOM, i);
        }

        public synchronized void unflatten(String str) {
            this.mMap.clear();
            Iterable<String> simpleStringSplitter = new SimpleStringSplitter(';');
            simpleStringSplitter.setString(str);
            for (String str2 : simpleStringSplitter) {
                int indexOf = str2.indexOf(61);
                if (indexOf != -1) {
                    this.mMap.put(str2.substring(0, indexOf), str2.substring(indexOf + 1));
                }
            }
        }
    }

    public interface PhaseAutoFocusCallback {
        void onPhaseAutoFocus(short s, short s2, short s3, short s4, short s5);
    }

    public interface PictureCallback {
        void onPictureTaken(byte[] bArr, SemCamera semCamera);
    }

    public interface PostEventListener {
        void onHandleMessage(int i, int i2, int i3, Object obj);
    }

    public interface PreviewCallback {
        void onPreviewFrame(byte[] bArr, SemCamera semCamera);
    }

    public interface PreviewCallbackTimeStamp {
        void onPreviewFrame(byte[] bArr, long j, SemCamera semCamera);
    }

    public interface QrCodeDetectionEventListener {
        void onQrCodeDetection(byte[] bArr, SemCamera semCamera);

        void onQrCodeDetectionError(int i);
    }

    public interface RelightEventListener {
        void onRelightTransformDataUpdated(TransformData[] transformDataArr);
    }

    private class RelightTransformData {
        private TransformData[] mTransformData;
        private int number_of_faces;

        private RelightTransformData(byte[] bArr) {
            this.number_of_faces = 0;
            this.mTransformData = null;
            int i = 1 + 1;
            int i2 = i + 1;
            i = i2 + 1;
            this.number_of_faces = (((bArr[0] & 255) | ((bArr[1] & 255) << 8)) | ((bArr[i] & 255) << 16)) | ((bArr[i2] & 255) << 24);
            this.mTransformData = new TransformData[this.number_of_faces];
            i2 = i;
            for (int i3 = 0; i3 < this.number_of_faces; i3++) {
                this.mTransformData[i3] = new TransformData();
                this.mTransformData[i3].range = new int[4];
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].range[0] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].range[1] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].range[2] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].range[3] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                r4 = new int[6];
                this.mTransformData[i3].transformX = r4;
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformX[0] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformX[1] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformX[2] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformX[3] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformX[4] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformX[5] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                r4 = new int[6];
                this.mTransformData[i3].transformY = r4;
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformY[0] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformY[1] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformY[2] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformY[3] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformY[4] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].transformY[5] = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].faceAngle = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].lightIntensity = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                this.mTransformData[i3].id = (((bArr[i2] & 255) | ((bArr[i] & 255) << 8)) | ((bArr[i2] & 255) << 16)) | ((bArr[i] & 255) << 24);
            }
        }
    }

    public interface ScreenFlashEventListener {
        void onTakePictureCompleted();
    }

    public interface SelectiveFocusEventListener {
        void onSelectiveFocusCaptureProgress(int i, int i2);

        void onSelectiveFocusComplete(int i);

        void onSelectiveFocusProcessProgress(int i, int i2);
    }

    public interface ShotAndMoreEventListener {
        void onShotAndMoreApplicableMode(int i);

        void onShotAndMoreCaptureProgress(int i, int i2);

        void onShotAndMoreComplete();

        void onShotAndMoreProcessProgress(int i, int i2);
    }

    public interface ShutterCallback {
        void onShutter();
    }

    public class Size {
        public int height;
        public int width;

        public Size(int i, int i2) {
            this.width = i;
            this.height = i2;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof Size)) {
                return false;
            }
            Size size = obj;
            if (this.width == size.width && this.height == size.height) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return (this.width * 32713) + this.height;
        }
    }

    public class SlowMotionEvent {
        public int endMillisecond;
        public int startMillisecond;

        private SlowMotionEvent() {
        }
    }

    public interface SlowMotionEventListener {
        void onSlowMotionEventResult(SlowMotionEvent[] slowMotionEventArr);
    }

    private class SlowMotionEventResult {
        private SlowMotionEvent[] events;

        private SlowMotionEventResult(byte[] bArr) {
            this.events = null;
            int i = 1 + 1;
            int i2 = i + 1;
            i = i2 + 1;
            int i3 = (((bArr[0] & 255) | ((bArr[1] & 255) << 8)) | ((bArr[i] & 255) << 16)) | ((bArr[i2] & 255) << 24);
            Log.m31e(SemCamera.TAG, "SlowMotionEventResult : numberOfEvent=" + i3);
            this.events = new SlowMotionEvent[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                this.events[i4] = new SlowMotionEvent();
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                this.events[i4].startMillisecond = (((bArr[i] & 255) | ((bArr[i2] & 255) << 8)) | ((bArr[i] & 255) << 16)) | ((bArr[i2] & 255) << 24);
                i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                i = i2 + 1;
                this.events[i4].endMillisecond = (((bArr[i] & 255) | ((bArr[i2] & 255) << 8)) | ((bArr[i] & 255) << 16)) | ((bArr[i2] & 255) << 24);
            }
        }
    }

    public interface SmartFilterListener {
        void onParametersChanged(byte[] bArr);
    }

    public interface StickerEventListener {
        void onFaceAlignmentData(byte[] bArr);
    }

    public static class TimeStampFrame {
        public byte[] FrameData;
        public long timeStamp;
    }

    public static class TransformData {
        public int faceAngle = 0;
        public int id = -1;
        public int lightIntensity = 0;
        public int[] range = null;
        public int[] transformX = null;
        public int[] transformY = null;
    }

    public interface WideMotionSelfieEventListener {
        void onWideMotionSelfieCaptured();

        void onWideMotionSelfieCapturedMaxFrames();

        void onWideMotionSelfieCapturedNew();

        void onWideMotionSelfieCompleted();

        void onWideMotionSelfieDirectionChanged(int i);

        void onWideMotionSelfieError(int i);

        void onWideMotionSelfieLivePreviewData(byte[] bArr);

        void onWideMotionSelfieNextCapturePosition(int i, int i2);

        void onWideMotionSelfieProgressStitching(int i);

        void onWideMotionSelfieRectChanged(byte[] bArr);

        void onWideMotionSelfieSingleCaptureDone();

        void onWideMotionSelfieSlowMove();
    }

    public interface WideSelfieEventListener {
        void onWideSelfieCaptured();

        void onWideSelfieCapturedMaxFrames();

        void onWideSelfieCapturedNew();

        void onWideSelfieDirectionChanged(int i);

        void onWideSelfieError(int i);

        void onWideSelfieLivePreviewData(byte[] bArr);

        void onWideSelfieLowResolutionData(byte[] bArr);

        void onWideSelfieNextCapturePosition(int i, int i2);

        void onWideSelfieProgressStitching(int i);

        void onWideSelfieRectChanged(byte[] bArr);

        void onWideSelfieSingleCaptureDone();

        void onWideSelfieSlowMove();
    }

    static {
        System.loadLibrary("semcamera_jni");
    }

    SemCamera() {
    }

    private SemCamera(int i, int i2) {
        int cameraInitVersion = cameraInitVersion(i, i2, null, true);
        if (!checkInitErrors(cameraInitVersion)) {
            return;
        }
        if (cameraInitVersion == (-OsConstants.EACCES)) {
            throw new RuntimeException("Fail to connect to camera service");
        } else if (cameraInitVersion == (-OsConstants.ENODEV)) {
            throw new RuntimeException("Camera initialization failed");
        } else if (cameraInitVersion == 1) {
            throw new RuntimeException("No permission to open camera device");
        } else if (cameraInitVersion == 3) {
            throw new RuntimeException("Camera initialization failed because the input arguments are invalid");
        } else if (cameraInitVersion == 4) {
            throw new RuntimeException("Camera device currently not available");
        } else if (cameraInitVersion == 1) {
            throw new RuntimeException("No permission to open camera");
        } else if (cameraInitVersion == 7) {
            throw new CameraBusyRuntimeException("Camera initialization failed because the camera device was already opened");
        } else if (cameraInitVersion == 8) {
            throw new CameraMaxUsersRuntimeException("Camera initialization failed because the max number of camera devices were already opened");
        } else if (cameraInitVersion == 10) {
            throw new RuntimeException("Camera device currently not available due to invalid operation");
        } else if (cameraInitVersion == 99) {
            throw new CameraNoResourceException("Camera initialization failed because the camera resource was already used");
        } else {
            throw new RuntimeException("Unknown camera error");
        }
    }

    SemCamera(int i, Looper looper, boolean z) {
        Log.m33i(TAG, "Semcamera Version is 2401");
        int cameraInitNormal = cameraInitNormal(i, looper, z);
        if (!checkInitErrors(cameraInitNormal)) {
            return;
        }
        if (cameraInitNormal == (-OsConstants.EACCES)) {
            throw new RuntimeException("Fail to connect to camera service");
        } else if (cameraInitNormal == (-OsConstants.ENODEV)) {
            throw new RuntimeException("Camera initialization failed");
        } else if (cameraInitNormal == 1) {
            throw new RuntimeException("No permission to open camera device");
        } else if (cameraInitNormal == 3) {
            throw new RuntimeException("Camera initialization failed because the input arguments are invalid");
        } else if (cameraInitNormal == 4) {
            throw new RuntimeException("Camera device currently not available");
        } else if (cameraInitNormal == 1) {
            throw new RuntimeException("No permission to open camera");
        } else if (cameraInitNormal == 7) {
            throw new CameraBusyRuntimeException("Camera initialization failed because the camera device was already opened");
        } else if (cameraInitNormal == 8) {
            throw new CameraMaxUsersRuntimeException("Camera initialization failed because the max number of camera devices were already opened");
        } else if (cameraInitNormal == 10) {
            throw new RuntimeException("Camera device currently not available due to invalid operation");
        } else if (cameraInitNormal == 99) {
            throw new CameraNoResourceException("Camera initialization failed because the camera resource was already used");
        } else {
            throw new RuntimeException("Unknown camera error");
        }
    }

    private final native void _addCallbackBuffer(byte[] bArr, int i);

    private final native boolean _enableShutterSound(boolean z);

    private static native void _getCameraInfo(int i, CameraInfo cameraInfo);

    private native void _setOutputFile(FileDescriptor fileDescriptor);

    private native void _setOutputFileArray(FileDescriptor[] fileDescriptorArr, int i);

    private final native void _startFaceDetection(int i);

    private final native void _stopFaceDetection();

    private final native void _stopPreview();

    private final void addCallbackBuffer(byte[] bArr, int i) {
        if (i == 16 || i == 128) {
            _addCallbackBuffer(bArr, i);
            return;
        }
        throw new IllegalArgumentException("Unsupported message type: " + i);
    }

    private int cameraInitNormal(int i, Looper looper, boolean z) {
        return cameraInitVersion(i, -2, looper, z);
    }

    private int cameraInitVersion(int i, int i2, Looper looper, boolean z) {
        this.mShutterCallback = null;
        this.mRawImageCallback = null;
        this.mJpegCallback = null;
        this.mPreviewCallback = null;
        this.mPreviewCallbackForGLEffect = null;
        this.mPreviewCallbackTimeStamp = null;
        this.mPostviewCallback = null;
        this.mUsingPreviewAllocation = false;
        this.mZoomListener = null;
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            Looper myLooper = Looper.myLooper();
            if (myLooper != null) {
                this.mEventHandler = new EventHandler(this, myLooper);
            } else {
                myLooper = Looper.getMainLooper();
                if (myLooper != null) {
                    this.mEventHandler = new EventHandler(this, myLooper);
                } else {
                    this.mEventHandler = null;
                }
            }
        }
        String currentOpPackageName = ActivityThread.currentOpPackageName();
        if (Process.myUid() == 1000 && currentOpPackageName == null) {
            currentOpPackageName = ZenModeConfig.SYSTEM_AUTHORITY;
        }
        int native_setup = native_setup(new WeakReference(this), i, i2, currentOpPackageName);
        if (native_setup == 0 && z) {
            native_sendcommand(HAL_SET_SAMSUNG_CAMERA, 0, 0);
        }
        return native_setup;
    }

    public static boolean checkInitErrors(int i) {
        return i != 0;
    }

    private native void enableFocusMoveCallback(int i);

    public static void getCameraInfo(int i, CameraInfo cameraInfo) {
        _getCameraInfo(i, cameraInfo);
        IAudioService asInterface = Stub.asInterface(ServiceManager.getService("audio"));
        if (asInterface != null) {
            try {
                if (asInterface.isCameraSoundForced()) {
                    cameraInfo.canDisableShutterSound = false;
                }
            } catch (RemoteException e) {
                Log.m31e(TAG, "Audio service is unavailable for queries");
            }
        }
    }

    public static native int getNumberOfCameras();

    private final native void native_autoFocus();

    private final native void native_cancelAutoFocus();

    private final native String native_getParameters();

    private final native void native_release();

    private final native void native_setParameters(String str);

    private final native int native_setup(Object obj, int i, int i2, String str);

    private final native void native_takePicture(int i);

    public static SemCamera open() {
        int numberOfCameras = getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 0) {
                return new SemCamera(i, null, true);
            }
        }
        return null;
    }

    public static SemCamera open(int i) {
        Log.m31e(TAG, "SemCamera.open()");
        return new SemCamera(i, null, true);
    }

    public static SemCamera open(int i, Looper looper) {
        Log.m31e(TAG, "SemCamera.open()");
        return new SemCamera(i, looper, true);
    }

    public static SemCamera open(int i, Looper looper, boolean z) {
        Log.m31e(TAG, "SemCamera.open()");
        return new SemCamera(i, looper, z);
    }

    public static SemCamera openLegacy(int i, int i2) {
        if (i2 >= 256) {
            return new SemCamera(i, i2);
        }
        throw new IllegalArgumentException("Invalid HAL version " + i2);
    }

    public static SemCamera openUninitialized() {
        return new SemCamera();
    }

    private static void postEventFromNative(Object obj, int i, int i2, int i3, Object obj2) {
        if (i < 16) {
            Log.m29d(TAG, "postEventFromNative: " + i);
        }
        Trace.traceBegin(1024, "postEventFromNative(" + i + ")");
        SemCamera semCamera;
        try {
            semCamera = (SemCamera) ((WeakReference) obj).get();
            if (semCamera == null) {
                Log.m31e(TAG, "postEventFromNative : SemCamera is null");
                Trace.traceEnd(1024);
                return;
            }
            if (semCamera.mEventHandler == null) {
                Log.m31e(TAG, "mEventHandler is null");
            } else if (i == 65536) {
                semCamera.irisIrLock.lock();
                if (semCamera.mIrisIrCallbackEnabled) {
                    if (semCamera.mIrisIrQueue.size() >= 3) {
                        ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) semCamera.mIrisIrQueue.poll();
                        if (parcelFileDescriptor != null) {
                            Log.m29d(TAG, "remove old queued ir fd(" + parcelFileDescriptor.getFileDescriptor().getInt$() + ")");
                            parcelFileDescriptor.close();
                        }
                    }
                    ParcelFileDescriptor dup = ParcelFileDescriptor.dup((FileDescriptor) obj2);
                    if (semCamera.mIrisIrQueue.offer(dup)) {
                        Log.m29d(TAG, "add new ir fd(" + dup.getFileDescriptor().getInt$() + "), queue size=" + semCamera.mIrisIrQueue.size());
                    } else {
                        Log.m29d(TAG, "fail to add new ir fd(" + dup.getFileDescriptor().getInt$() + ")");
                        semCamera.irisIrLock.unlock();
                        Trace.traceEnd(1024);
                        return;
                    }
                }
                semCamera.irisIrLock.unlock();
                if (semCamera.mIrisIrCallbackEnabled) {
                    semCamera.mEventHandler.sendMessage(semCamera.mEventHandler.obtainMessage(i, i2, i3, null));
                } else {
                    Log.m31e(TAG, "ERROR : check IrisCallback Message");
                }
            } else if (i == 32768) {
                semCamera.irisPreviewLock.lock();
                try {
                    if (semCamera.mIrisPreviewCallbackEnabled) {
                        if (semCamera.mIrisPreviewQueue.size() >= 3) {
                            ParcelFileDescriptor parcelFileDescriptor2 = (ParcelFileDescriptor) semCamera.mIrisPreviewQueue.poll();
                            if (parcelFileDescriptor2 != null) {
                                Log.m29d(TAG, "remove old queued preview fd(" + parcelFileDescriptor2.getFileDescriptor().getInt$() + ")");
                                parcelFileDescriptor2.close();
                            }
                        }
                        ParcelFileDescriptor dup2 = ParcelFileDescriptor.dup((FileDescriptor) obj2);
                        if (semCamera.mIrisPreviewQueue.offer(dup2)) {
                            Log.m29d(TAG, "add new preview fd(" + dup2.getFileDescriptor().getInt$() + "), queue size=" + semCamera.mIrisPreviewQueue.size());
                        } else {
                            Log.m29d(TAG, "fail to add new preview fd(" + dup2.getFileDescriptor().getInt$() + ")");
                            semCamera.irisPreviewLock.unlock();
                            Trace.traceEnd(1024);
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.m31e(TAG, "ParcelFileDescriptor dup exception, so return");
                } finally {
                    semCamera.irisPreviewLock.unlock();
                }
                if (semCamera.mIrisPreviewCallbackEnabled) {
                    semCamera.mEventHandler.sendMessage(semCamera.mEventHandler.obtainMessage(i, i2, i3, null));
                } else {
                    Log.m31e(TAG, "ERROR : check IrisPreviewCallback Enabled");
                }
            } else {
                semCamera.mEventHandler.sendMessage(semCamera.mEventHandler.obtainMessage(i, i2, i3, obj2));
            }
            Trace.traceEnd(1024);
        } catch (Exception e2) {
            Log.m31e(TAG, "ParcelFileDescriptor dup exception, so return");
            semCamera.irisIrLock.unlock();
        } catch (Throwable th) {
            Trace.traceEnd(1024);
        }
    }

    private final native void setHasPreviewCallback(boolean z, boolean z2, boolean z3);

    private final native void setPreviewCallbackSurface(Surface surface);

    public final void addCallbackBuffer(byte[] bArr) {
        _addCallbackBuffer(bArr, 16);
    }

    public final void addRawImageCallbackBuffer(byte[] bArr) {
        addCallbackBuffer(bArr, 128);
    }

    public final void autoFocus(AutoFocusCallback autoFocusCallback) {
        synchronized (this.mAutoFocusCallbackLock) {
            this.mAutoFocusCallback = autoFocusCallback;
        }
        native_autoFocus();
    }

    public int cameraInitUnspecified(int i) {
        return cameraInitVersion(i, -1, null, true);
    }

    public final void cancelAutoFocus() {
        native_cancelAutoFocus();
        this.mEventHandler.removeMessages(4);
    }

    public void cancelMagicShot() {
        native_sendcommand(MAGIC_SHOT_CANCEL, 0, 0);
    }

    public void cancelMotionPanorama() {
        native_sendcommand(MOTION_PANORAMA_SHOT_CANCEL, 0, 0);
    }

    public void cancelTakePicture() {
        native_sendcommand(COMMON_CANCEL_TAKE_PICTURE, 0, 0);
    }

    public void cancelWideMotionSelfie() {
        native_sendcommand(WIDE_MOTION_SELFIE_SHOT_CANCEL, 0, 0);
    }

    public void cancelWideSelfie() {
        native_sendcommand(WIDE_SELFIE_SHOT_CANCEL, 0, 0);
    }

    public final Allocation createPreviewAllocation(RenderScript renderScript, int i) throws RSIllegalArgumentException {
        Size previewSize = getParameters().getPreviewSize();
        Builder builder = new Builder(renderScript, Element.createPixel(renderScript, DataType.UNSIGNED_8, DataKind.PIXEL_YUV));
        builder.setYuvFormat(842094169);
        if (previewSize != null) {
            builder.setX(previewSize.width);
            builder.setY(previewSize.height);
        }
        return Allocation.createTyped(renderScript, builder.create(), i | 32);
    }

    public final void disableMotionPhoto() {
        native_sendcommand(CAMERA_CMD_STOP_MOTION_PHOTO, 0, 0);
    }

    public final boolean disableShutterSound() {
        return _enableShutterSound(false);
    }

    public final void enableMotionPhoto(int i) {
        native_sendcommand(CAMERA_CMD_START_MOTION_PHOTO, i, 0);
    }

    public final boolean enableShutterSound(boolean z) {
        if (!z) {
            IAudioService asInterface = Stub.asInterface(ServiceManager.getService("audio"));
            if (asInterface != null) {
                try {
                    if (asInterface.isCameraSoundForced()) {
                        return false;
                    }
                } catch (RemoteException e) {
                    Log.m31e(TAG, "Audio service is unavailable for queries");
                }
            }
        }
        return _enableShutterSound(z);
    }

    protected void finalize() {
        release();
    }

    public synchronized String flatten(HashMap<String, String> hashMap) {
        if (hashMap != null) {
            if (hashMap.size() != 0) {
                StringBuilder stringBuilder = new StringBuilder(128);
                for (String str : hashMap.keySet()) {
                    stringBuilder.append(str);
                    stringBuilder.append("=");
                    stringBuilder.append((String) hashMap.get(str));
                    stringBuilder.append(SemSmartGlow.PACKAGE_SEPARATOR);
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                return stringBuilder.toString();
            }
        }
        return MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
    }

    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        String native_getParameters = native_getParameters();
        if (native_getParameters != null) {
            parameters.unflatten(native_getParameters);
            return parameters;
        }
        Log.m31e(TAG, "getParameters string is null, return null parameter");
        return null;
    }

    public final native void lock();

    public final native void native_sendcommand(int i, int i2, int i3);

    public void pauseRecording() {
        native_sendcommand(HAL_RECORDING_PAUSE, 0, 0);
    }

    public final void preparePreview() {
        native_sendcommand(HAL_MULTI_INSTANCE_PREPARE_PREVIEW, 0, 0);
        startPreview();
        stopPreview();
    }

    public final native boolean previewEnabled();

    public final native void reconnect() throws IOException;

    public final native void registerRecordingSurface(Surface surface) throws IOException;

    public final void release() {
        native_release();
        if (this.mEventHandler != null) {
            this.mEventHandler.removeCallbacksAndMessages(null);
        }
        this.mFaceDetectionRunning = false;
        this.irisIrLock.lock();
        this.mIrisIrCallbackEnabled = false;
        this.irisIrLock.unlock();
        this.irisPreviewLock.lock();
        this.mIrisPreviewCallbackEnabled = false;
        this.irisPreviewLock.unlock();
        Log.m31e(TAG, "release::mIrisIrQueue FD Size = " + this.mIrisIrQueue.size());
        while (this.mIrisIrQueue.size() > 0) {
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) this.mIrisIrQueue.poll();
            this.irisIrLock.lock();
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (Exception e) {
                    Log.m31e(TAG, "ParcelFileDescriptor irisIrFd close exception.");
                } finally {
                    this.irisIrLock.unlock();
                }
            }
            this.irisIrLock.unlock();
        }
        this.irisIrLock.lock();
        this.mIrisIrQueue.clear();
        this.irisIrLock.unlock();
        Log.m31e(TAG, "release::mIrisPreviewQueue FD Size = " + this.mIrisPreviewQueue.size());
        while (this.mIrisPreviewQueue.size() > 0) {
            ParcelFileDescriptor parcelFileDescriptor2 = (ParcelFileDescriptor) this.mIrisPreviewQueue.poll();
            this.irisPreviewLock.lock();
            if (parcelFileDescriptor2 != null) {
                try {
                    parcelFileDescriptor2.close();
                } catch (Exception e2) {
                    Log.m31e(TAG, "ParcelFileDescriptor irisPreviewFd close exception.");
                } finally {
                    this.irisPreviewLock.unlock();
                }
            }
            this.irisPreviewLock.unlock();
        }
        this.irisPreviewLock.lock();
        this.mIrisPreviewQueue.clear();
        this.irisPreviewLock.unlock();
    }

    public void requestBurstShotImage() {
        native_sendcommand(BURST_SHOT_REQUEST_IMAGE, 0, 0);
    }

    public void resumeRecording() {
        native_sendcommand(HAL_RECORDING_RESUME, 0, 0);
    }

    public void sendCommand(int i, int i2, int i3) {
        native_sendcommand(i, i2, i3);
    }

    public void sendOrientationInfoToHal(int i) {
        native_sendcommand(DEVICE_ORIENTATION, i, 0);
    }

    public void setAntiFogLevel(int i) {
        native_sendcommand(HAZE_STRENGTH, i, 1);
    }

    public void setAutoExposureCallback(AutoExposureCallback autoExposureCallback) {
        this.mAutoExposureCallback = autoExposureCallback;
    }

    public final void setAutoFocusCb(AutoFocusCallback autoFocusCallback) {
        synchronized (this.mAutoFocusCallbackLock) {
            this.mAutoFocusCallback = autoFocusCallback;
        }
    }

    public void setAutoFocusMoveCallback(AutoFocusMoveCallback autoFocusMoveCallback) {
        this.mAutoFocusMoveCallback = autoFocusMoveCallback;
        enableFocusMoveCallback(this.mAutoFocusMoveCallback != null ? 1 : 0);
    }

    public void setBeautyEventListener(BeautyEventListener beautyEventListener) {
        this.mBeautyEventListener = beautyEventListener;
    }

    public void setBeautyLevel(boolean z, int i) {
        Log.m33i(TAG, "setBeautyLevel(" + z + ", " + i + ")");
        native_sendcommand(BEAUTY_FACE_RETOUCH_LEVEL, z ? 1 : 0, i);
    }

    public void setBrightnessValueCallback(BrightnessValueCallback brightnessValueCallback) {
        this.mBrightnessValueCallback = brightnessValueCallback;
    }

    public void setBrightnessValueCallbackEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_BRIGHTNESS_VALUE_CALLBACK, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_BRIGHTNESS_VALUE_CALLBACK, 0, 0);
        }
    }

    public void setBurstEventListener(BurstEventListener burstEventListener) {
        this.mBurstEventListener = burstEventListener;
    }

    public void setBurstShotFpsCallback(BurstShotFpsCallback burstShotFpsCallback) {
        this.mBurstShotFpsCallback = burstShotFpsCallback;
    }

    public void setBurstShotFpsCallbackEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_BURSTSHOT_FPS_CALLBACK, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_BURSTSHOT_FPS_CALLBACK, 0, 0);
        }
    }

    public void setBurstShotFpsValue(int i) {
        native_sendcommand(HAL_BURST_SHOT_FPS_VALUE, i, 0);
    }

    public void setBurstShotSetup(int i, int i2) {
        native_sendcommand(BURST_SHOT_SETUP, i, i2);
    }

    public void setCameraSensorDataListener(CameraSensorDataListener cameraSensorDataListener) {
        this.mCameraSensorDataListener = cameraSensorDataListener;
    }

    public void setCameraSensorDataListenerEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_CURRENT_SET, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_CURRENT_SET, 0, 0);
        }
    }

    public void setCameraUsage(int i) {
        native_sendcommand(HAL_SET_CAMERA_USAGE, i, 0);
    }

    public void setCaptureFlipPhotoMode() {
        Log.m35v(TAG, "setCaptureFlipPhotoMode");
        native_sendcommand(TAKEPICTURE_FLIP_PHOTO, 1, 0);
    }

    public void setCommonEventListener(CommonEventListener commonEventListener) {
        this.mCommonEventListener = commonEventListener;
    }

    public void setDepthMapEventListener(DepthMapEventListener depthMapEventListener) {
        this.mDepthMapEventListener = depthMapEventListener;
    }

    public final native void setDisplayOrientation(int i);

    public void setDistortionEffectsInfo(int i, int i2) {
        native_sendcommand(DISTORTION_EFFECTS_SHOT_INFO, i, i2);
    }

    public void setDistortionEffectsMode(int i, int i2) {
        native_sendcommand(DISTORTION_EFFECTS_SHOT_SET_MODE, i, i2);
    }

    public void setDualEffectCoordinate(int i, int i2) {
        native_sendcommand(SET_EFFECT_COORDINATE, i, i2);
    }

    public void setDualEffectLayerOrder(boolean z) {
        if (z) {
            native_sendcommand(SET_EFFECT_LAYER_ORDER, 0, 0);
        } else {
            native_sendcommand(SET_EFFECT_LAYER_ORDER, 1, 0);
        }
    }

    public void setDualEventListener(DualEventListener dualEventListener) {
        this.mDualEventListener = dualEventListener;
    }

    public void setEffectMode(int i) {
        native_sendcommand(SET_EFFECT_MODE, i, 0);
    }

    public void setEffectOrientation(int i) {
        native_sendcommand(SET_EFFECT_ORIENTATION, i, 0);
    }

    public void setEffectSaveAsFlipped(int i) {
        native_sendcommand(SET_EFFECT_SAVE_AS_FLIPPED, i, 0);
    }

    public final void setErrorCallback(ErrorCallback errorCallback) {
        this.mErrorCallback = errorCallback;
    }

    public final void setExtraInfoListener(ExtraInfoListener extraInfoListener) {
        this.mExtraInfoListener = extraInfoListener;
    }

    public void setEyeEnlargeLevel(int i) {
        native_sendcommand(BEAUTY_EYE_ENLARGE_LEVEL, i, 0);
    }

    public final void setFaceDetectionListener(FaceDetectionListener faceDetectionListener) {
        this.mFaceListener = faceDetectionListener;
    }

    public void setFaceDistortionCompensationEnabled(boolean z) {
        if (z) {
            native_sendcommand(BEAUTY_FACE_DISTORTION_COMPENSATION, 1, 0);
        } else {
            native_sendcommand(BEAUTY_FACE_DISTORTION_COMPENSATION, 0, 0);
        }
    }

    public void setFaceRelightDirection(int i, int i2) {
        native_sendcommand(BEAUTY_FACE_RELIGHT_DIRECTION, i, i2);
    }

    public void setFaceRelightEnabled(boolean z) {
        native_sendcommand(BEAUTY_FACE_ENABLE_RELIGHT, z ? 1 : 0, 0);
    }

    public void setFaceRelightLevel(int i) {
        native_sendcommand(BEAUTY_FACE_RELIGHT_LEVEL, i, 0);
    }

    public void setFlashAutoCallbackEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_FLASH_AUTO_CALLBACK, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_FLASH_AUTO_CALLBACK, 0, 0);
        }
    }

    public void setFocusPeakingEnabled(boolean z) {
        native_sendcommand(HAL_SET_FOCUS_PEAKING, z ? 1 : 0, 0);
    }

    public void setFoodShotEventListener(FoodShotEventListener foodShotEventListener) {
        this.mFoodShotEventListener = foodShotEventListener;
    }

    public void setGenericParameters(String str) {
        String str2 = "Generic-Param=1;" + str + "=0;";
        Log.m29d(TAG, "setGenericParameters : " + str2);
        native_setParameters(str2);
    }

    public void setGestureControlMode(int i) {
        native_sendcommand(GESTURE_CONTROL_MODE, i, 0);
    }

    public final void setHardwareFaceDetectionListener(HardwareFaceDetectionListener hardwareFaceDetectionListener) {
        this.mHardwareFaceDetectionListener = hardwareFaceDetectionListener;
    }

    public void setHazeRemovalShotEventListener(HazeRemovalEventListener hazeRemovalEventListener) {
        Log.m33i(TAG, "setHazeRemovalShotEventListener");
        this.mHazeRemovalShotEventListener = hazeRemovalEventListener;
    }

    public void setHdrAutoCallbackEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_HDR_AUTO_CALLBACK, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_HDR_AUTO_CALLBACK, 0, 0);
        }
    }

    public void setHdrEventListener(HdrEventListener hdrEventListener) {
        this.mHdrEventListener = hdrEventListener;
    }

    public void setHdrSavingMode(int i) {
        native_sendcommand(HDR_PICTURE_MODE_CHANGE, i, 0);
    }

    public void setImageEffect(int i) {
        native_sendcommand(SET_EFFECT_FILTER, i, 0);
    }

    public void setImageEffect(String str) {
        setGenericParameters(str);
    }

    public void setImageEffectEventListener(ImageEffectEventListener imageEffectEventListener) {
        this.mImageEffectEventListener = imageEffectEventListener;
    }

    public void setImageEffectVisibleForRecording(boolean z) {
        if (z) {
            native_sendcommand(SET_EFFECT_VISIBLE_FOR_RECORDING, 1, 0);
        } else {
            native_sendcommand(SET_EFFECT_VISIBLE_FOR_RECORDING, 0, 0);
        }
    }

    public void setInteractiveEventListener(InteractiveShotEventListener interactiveShotEventListener) {
        this.mInteractiveShotEventListener = interactiveShotEventListener;
    }

    public final void setIrisDataCallback(IrisDataCallback irisDataCallback) {
        this.mIrisDataCallback = irisDataCallback;
    }

    public final void setIrisIrDataCallbackEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_IRIS_IR_DATA_CALLBACK, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_IRIS_IR_DATA_CALLBACK, 0, 0);
        }
        this.mIrisIrCallbackEnabled = z;
    }

    public final void setIrisPreviewDataCallbackEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_IRIS_PREVIEW_DATA_CALLBACK, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_IRIS_PREVIEW_DATA_CALLBACK, 0, 0);
        }
        this.mIrisPreviewCallbackEnabled = z;
    }

    public void setLightConditionChangedListener(LightConditionChangedListener lightConditionChangedListener) {
        this.mLightConditionChangedListener = lightConditionChangedListener;
    }

    public void setLightConditionDetectEnabled(boolean z) {
        if (z) {
            native_sendcommand(LIGHT_CONDITION_ENABLE, 1, 0);
        } else {
            native_sendcommand(LIGHT_CONDITION_ENABLE, 0, 0);
        }
    }

    public void setLiveVideoBroadcastRecordingModeEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_SET_LVB_RECORDING_MODE, 1, 0);
        } else {
            native_sendcommand(HAL_SET_LVB_RECORDING_MODE, 0, 0);
        }
    }

    public void setLowLightShotEnabled(boolean z) {
        native_sendcommand(LOW_LIGHT_SHOT_SET, z ? 1 : 0, 0);
    }

    public void setMotionPanoramaEventListener(MotionPanoramaEventListener motionPanoramaEventListener) {
        this.mMotionPanoramaEventListener = motionPanoramaEventListener;
    }

    public void setMotionPanoramaModeEnabled(boolean z) {
        if (z) {
            native_sendcommand(MOTION_PANORAMA_SHOT_ENABLE_MOTION, 1, 0);
        } else {
            native_sendcommand(MOTION_PANORAMA_SHOT_ENABLE_MOTION, 0, 0);
        }
    }

    public void setMultiAutoFocusCallback(MultiAutoFocusCallback multiAutoFocusCallback) {
        this.mMultiAutoFocusCallback = multiAutoFocusCallback;
    }

    public void setMultiAutoFocusEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_MULTI_AF, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_MULTI_AF, 0, 0);
        }
    }

    public void setMultiFrameEventListener(MultiFrameEventListener multiFrameEventListener) {
        Log.m33i(TAG, "setMultiFrameShotEventListener");
        this.mMultiFrameEventListener = multiFrameEventListener;
    }

    public void setMultiTrackModeEnabled(boolean z) {
        native_sendcommand(SET_MULTI_TRACK_MODE, z ? 1 : 0, 0);
    }

    public void setObjectTrackingAutoFocusCallback(ObjectTrackingAutoFocusCallback objectTrackingAutoFocusCallback) {
        this.mObjectTrackingAutoFocusCallback = objectTrackingAutoFocusCallback;
    }

    public final void setOneShotPreviewCallback(PreviewCallback previewCallback) {
        boolean z = true;
        this.mPreviewCallback = previewCallback;
        this.mOneShot = true;
        this.mWithBuffer = false;
        if (previewCallback != null) {
            this.mUsingPreviewAllocation = false;
        }
        if (previewCallback == null) {
            z = false;
        }
        setHasPreviewCallback(z, false, false);
    }

    public final void setOneShotPreviewCallback(PreviewCallback previewCallback, boolean z) {
        boolean z2 = true;
        if (z) {
            this.mPreviewCallbackForGLEffect = previewCallback;
            native_sendcommand(SET_PREVIEW_CALLBACK_FOR_GL_EFFECT, 0, 0);
            return;
        }
        this.mPreviewCallback = previewCallback;
        this.mOneShot = true;
        this.mWithBuffer = false;
        if (previewCallback != null) {
            this.mUsingPreviewAllocation = false;
        }
        if (previewCallback == null) {
            z2 = false;
        }
        setHasPreviewCallback(z2, false, false);
    }

    public void setOutputFile(String str) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(str, "rw");
        try {
            _setOutputFile(randomAccessFile.getFD());
        } finally {
            randomAccessFile.close();
        }
    }

    public void setOutputFileArray(String[] strArr) throws IOException {
        int i;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int length = strArr.length;
        for (i = 0; i < length; i++) {
            arrayList.add(new RandomAccessFile(strArr[i], "rw"));
            try {
                arrayList2.add(((RandomAccessFile) arrayList.get(i)).getFD());
            } catch (Throwable e) {
                Log.m32e(TAG, "setOutputFileArray :", e);
                ((RandomAccessFile) arrayList.get(i)).close();
            }
        }
        _setOutputFileArray((FileDescriptor[]) arrayList2.toArray(new FileDescriptor[arrayList2.size()]), length);
        for (i = 0; i < length; i++) {
            ((RandomAccessFile) arrayList.get(i)).close();
        }
    }

    public void setPanoramaEventListener(PanoramaEventListener panoramaEventListener) {
        this.mPanoramaEventListener = panoramaEventListener;
    }

    public synchronized void setParameters(Parameters parameters) {
        if (this.mUsingPreviewAllocation) {
            Size previewSize = parameters.getPreviewSize();
            Size previewSize2 = getParameters().getPreviewSize();
            if (!(previewSize.width == previewSize2.width && previewSize.height == previewSize2.height)) {
                throw new IllegalStateException("Cannot change preview size while a preview allocation is configured.");
            }
        }
        native_setParameters(parameters.flatten());
    }

    public void setPhaseAutoFocusCallback(PhaseAutoFocusCallback phaseAutoFocusCallback) {
        if (phaseAutoFocusCallback != null) {
            this.mPhaseAutoFocusCallback = phaseAutoFocusCallback;
            native_sendcommand(HAL_ENABLE_PAF_RESULT, 1, 0);
            return;
        }
        this.mPhaseAutoFocusCallback = null;
        native_sendcommand(HAL_ENABLE_PAF_RESULT, 0, 0);
    }

    public void setPostEventListener(PostEventListener postEventListener) {
        this.mPostEventListener = postEventListener;
    }

    public final void setPreviewCallback(PreviewCallback previewCallback) {
        this.mPreviewCallback = previewCallback;
        this.mOneShot = false;
        this.mWithBuffer = false;
        if (previewCallback != null) {
            this.mUsingPreviewAllocation = false;
        }
        setHasPreviewCallback(previewCallback != null, false, false);
    }

    public final void setPreviewCallbackAllocation(Allocation allocation) throws IOException {
        Surface surface = null;
        if (allocation != null) {
            Size previewSize = getParameters().getPreviewSize();
            if (previewSize.width != allocation.getType().getX() || previewSize.height != allocation.getType().getY()) {
                throw new IllegalArgumentException("Allocation dimensions don't match preview dimensions: Allocation is " + allocation.getType().getX() + ", " + allocation.getType().getY() + ". Preview is " + previewSize.width + ", " + previewSize.height);
            } else if ((allocation.getUsage() & 32) == 0) {
                throw new IllegalArgumentException("Allocation usage does not include USAGE_IO_INPUT");
            } else if (allocation.getType().getElement().getDataKind() != DataKind.PIXEL_YUV) {
                throw new IllegalArgumentException("Allocation is not of a YUV type");
            } else {
                surface = allocation.getSurface();
                this.mUsingPreviewAllocation = true;
            }
        } else {
            this.mUsingPreviewAllocation = false;
        }
        setPreviewCallbackSurface(surface);
    }

    public final void setPreviewCallbackTimeStamp(PreviewCallbackTimeStamp previewCallbackTimeStamp) {
        this.mPreviewCallbackTimeStamp = previewCallbackTimeStamp;
        this.mOneShot = false;
        this.mWithBuffer = false;
        if (previewCallbackTimeStamp != null) {
            this.mUsingPreviewAllocation = false;
        }
        setHasPreviewCallback(previewCallbackTimeStamp != null, false, false);
    }

    public final void setPreviewCallbackTimeStampWithBuffer(PreviewCallbackTimeStamp previewCallbackTimeStamp) {
        this.mPreviewCallbackTimeStamp = previewCallbackTimeStamp;
        this.mOneShot = false;
        this.mWithBuffer = true;
        if (previewCallbackTimeStamp != null) {
            this.mUsingPreviewAllocation = false;
        }
        setHasPreviewCallback(previewCallbackTimeStamp != null, true, false);
    }

    public final void setPreviewCallbackWithBuffer(PreviewCallback previewCallback) {
        this.mPreviewCallback = previewCallback;
        this.mOneShot = false;
        this.mWithBuffer = true;
        if (previewCallback != null) {
            this.mUsingPreviewAllocation = false;
        }
        setHasPreviewCallback(previewCallback != null, true, false);
    }

    public final void setPreviewCallbackWithBufferNoDisable(PreviewCallback previewCallback) {
        boolean z = false;
        this.mPreviewCallback = previewCallback;
        this.mOneShot = false;
        this.mWithBuffer = true;
        if (previewCallback != null) {
            z = true;
        }
        setHasPreviewCallback(z, true, true);
    }

    public final void setPreviewDisplay(android.view.SurfaceHolder r1) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.samsung.android.camera.core.SemCamera.setPreviewDisplay(android.view.SurfaceHolder):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = 0;
        if (r2 == 0) goto L_0x000b;
    L_0x0003:
        r0 = r2.getSurface();
        r1.setPreviewSurface(r0);
        return;
        r1.setPreviewSurface(r0);
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.camera.core.SemCamera.setPreviewDisplay(android.view.SurfaceHolder):void");
    }

    public void setPreviewStartedCallbackEnabled(boolean z) {
        native_sendcommand(REQUEST_NOTIFY_PREVIEW_STARTED, z ? 1 : 0, 0);
    }

    public final native void setPreviewSurface(Surface surface) throws IOException;

    public final native void setPreviewTexture(SurfaceTexture surfaceTexture) throws IOException;

    public void setQrCodeDetectionEventListener(QrCodeDetectionEventListener qrCodeDetectionEventListener) {
        this.mQrCodeDetectionEventListener = qrCodeDetectionEventListener;
    }

    public void setRecordingLocation(float f, float f2) {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("loc=1");
        stringBuilder.append(",");
        stringBuilder.append("latitude=");
        stringBuilder.append(Float.toString(f));
        stringBuilder.append(",");
        stringBuilder.append("longitude=");
        stringBuilder.append(Float.toString(f2));
        setGenericParameters(stringBuilder.toString());
    }

    public void setRecordingMaxFileSize(long j) {
        int i = (int) (j / TimeUtils.NANOS_PER_MS);
        int i2 = (int) (j % TimeUtils.NANOS_PER_MS);
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("maxfilesize=1");
        stringBuilder.append(",");
        stringBuilder.append("maxfilesizeh=");
        stringBuilder.append(String.valueOf(i));
        stringBuilder.append(",");
        stringBuilder.append("maxfilesizel=");
        stringBuilder.append(String.valueOf(i2));
        setGenericParameters(stringBuilder.toString());
    }

    public void setRecordingMode(int i) {
        native_sendcommand(SET_SECIMAGING_RECORDING_MODE, i, 0);
    }

    public void setRelightEventListener(RelightEventListener relightEventListener) {
        this.mRelightEventListener = relightEventListener;
    }

    public void setSKinColorLevel(int i) {
        native_sendcommand(BEAUTY_SKIN_COLOR_LEVEL, i, 0);
    }

    public void setSaveAsFlipped(boolean z, int i) {
        if (z) {
            native_sendcommand(HAL_SET_FRONT_SENSOR_MIRROR, 1, 0);
            native_sendcommand(SET_DISPLAY_ORIENTATION_MIRROR, i, 1);
            return;
        }
        native_sendcommand(HAL_SET_FRONT_SENSOR_MIRROR, 0, 0);
        native_sendcommand(SET_DISPLAY_ORIENTATION_MIRROR, i, 0);
    }

    public void setScreenFlashEnabled(boolean z) {
        if (z) {
            native_sendcommand(HAL_ENABLE_SCREEN_FLASH, 1, 0);
        } else {
            native_sendcommand(HAL_ENABLE_SCREEN_FLASH, 0, 0);
        }
    }

    public void setScreenFlashEventListener(ScreenFlashEventListener screenFlashEventListener) {
        this.mScreenFlashEventListener = screenFlashEventListener;
    }

    public void setSeedPointToDetectFoodRegion(int i, int i2) {
        native_sendcommand(1401, i, i2);
    }

    public void setSelectiveFocusEventListener(SelectiveFocusEventListener selectiveFocusEventListener) {
        this.mSelectiveFocusEventListener = selectiveFocusEventListener;
    }

    public void setShootingMode(int i) {
        setShootingMode(i, 0, 0);
    }

    public void setShootingMode(int i, int i2, int i3) {
        native_sendcommand(i + 1000, i2, i3);
    }

    public void setShootingModeCallbacks(ShutterCallback shutterCallback, PictureCallback pictureCallback, PictureCallback pictureCallback2) {
        this.mShutterCallback = shutterCallback;
        this.mRawImageCallback = pictureCallback;
        this.mPostviewCallback = null;
        this.mJpegCallback = pictureCallback2;
    }

    public void setShotAndMoreEventListener(ShotAndMoreEventListener shotAndMoreEventListener) {
        this.mShotAndMoreEventListener = shotAndMoreEventListener;
    }

    public void setShutterSoundMode(int i) {
        if (i < 0 || i > 2) {
            Log.m31e(TAG, "setShutterSoundEnabled : not supported value = " + i);
        } else {
            native_sendcommand(SET_ENABLE_SHUTTER_SOUND, i, 1);
        }
    }

    public void setSlimFaceLevel(int i) {
        native_sendcommand(BEAUTY_SLIM_FACE_LEVEL, i, 0);
    }

    public void setSlowMotionEventListener(SlowMotionEventListener slowMotionEventListener) {
        this.mSlowMotionEventListener = slowMotionEventListener;
    }

    public final void setSmartFilterListener(SmartFilterListener smartFilterListener) {
        this.mSmartFilterListener = smartFilterListener;
    }

    public void setStickerEnabled(boolean z) {
        native_sendcommand(SET_STICKER_ENABLED, z ? 1 : 0, 0);
    }

    public void setStickerEventListener(StickerEventListener stickerEventListener) {
        this.mStickerEventListener = stickerEventListener;
    }

    public void setThemeMask(int i) {
        native_sendcommand(THEME_SHOT_MASK_SET, i, 0);
    }

    public void setThemeOrientationInfo(int i, int i2) {
        native_sendcommand(THEME_SHOT_INFO, i, i2);
    }

    public void setWaterMarkEnabled(boolean z) {
        native_sendcommand(SET_WATERMARK_ENABLED, z ? 1 : 0, 0);
    }

    public void setWaterMarkPosition(int i, int i2) {
        native_sendcommand(SET_WATERMARK_POSITION, i, i2);
    }

    public void setWideMotionSelfieBeautyLevel(int i) {
        native_sendcommand(WIDE_MOTION_SELFIE_SHOT_BEAUTY_LEVEL, i, 0);
    }

    public void setWideMotionSelfieEventListener(WideMotionSelfieEventListener wideMotionSelfieEventListener) {
        this.mWideMotionSelfieEventListener = wideMotionSelfieEventListener;
    }

    public void setWideMotionSelfieMotionEnabled(boolean z) {
        if (z) {
            native_sendcommand(WIDE_MOTION_SELFIE_SHOT_MOTION_ENABLED, 1, 0);
        } else {
            native_sendcommand(WIDE_MOTION_SELFIE_SHOT_MOTION_ENABLED, 0, 0);
        }
    }

    public void setWideSelfieBeautyLevel(int i) {
        native_sendcommand(WIDE_SELFIE_SHOT_BEAUTY_LEVEL, i, 0);
    }

    public void setWideSelfieEventListener(WideSelfieEventListener wideSelfieEventListener) {
        this.mWideSelfieEventListener = wideSelfieEventListener;
    }

    public final void setZoomChangeListener(OnZoomChangeListener onZoomChangeListener) {
        this.mZoomListener = onZoomChangeListener;
    }

    public final void standbyPreview() {
        native_sendcommand(HAL_MULTI_INSTANCE_STANDBY_PREVIEW, 0, 0);
        stopPreview();
    }

    public void startContinuousAutoFocus() {
        native_sendcommand(HAL_START_CONTINUOUS_AF, 0, 0);
    }

    public final void startFaceDetection() {
        if (this.mFaceDetectionRunning) {
            throw new RuntimeException("Face detection is already running");
        }
        _startFaceDetection(0);
        this.mFaceDetectionRunning = true;
    }

    public void startMagicShot() {
        native_sendcommand(MAGIC_SHOT_START, 0, 0);
    }

    public void startMotionPanorama() {
        native_sendcommand(MOTION_PANORAMA_SHOT_START, 0, 0);
    }

    public final void startObjectTrackingAutoFocus() {
        native_sendcommand(HAL_OBJECT_TRACKING_STARTSTOP, 1, 0);
    }

    public void startPanorama() {
        native_sendcommand(PANORAMA_SHOT_START, 0, 0);
    }

    public final native void startPreview();

    public final void startQrCodeDetection(int i) {
        native_sendcommand(CAMERA_CMD_START_QRCODE_DETECTION, i, 0);
    }

    public void startSmartFilter() {
        native_sendcommand(START_SMART_FILTER, 0, 0);
    }

    public final native void startSmoothZoom(int i);

    public void startTouchAutoFocus() {
        native_sendcommand(HAL_TOUCH_AF_STARTSTOP, 1, 0);
    }

    public void startWideMotionSelfie(int i, int i2) {
        native_sendcommand(WIDE_MOTION_SELFIE_SHOT_START, i, i2);
    }

    public void startWideSelfie(int i, int i2) {
        native_sendcommand(WIDE_SELFIE_SHOT_START, i, i2);
    }

    public void startZoom() {
        native_sendcommand(HAL_START_ZOOM, 0, 0);
    }

    public void stopContinuousAutoFocus() {
        native_sendcommand(HAL_STOP_CONTINUOUS_AF, 0, 0);
    }

    public final void stopFaceDetection() {
        _stopFaceDetection();
        this.mFaceDetectionRunning = false;
        if (this.mEventHandler != null) {
            this.mEventHandler.removeMessages(1024);
        }
    }

    public void stopMagicShot() {
        native_sendcommand(MAGIC_SHOT_STOP, 0, 0);
    }

    public void stopMotionPanorama() {
        native_sendcommand(MOTION_PANORAMA_SHOT_STOP, 0, 0);
    }

    public final void stopObjectTrackingAutoFocus() {
        native_sendcommand(HAL_OBJECT_TRACKING_STARTSTOP, 0, 0);
    }

    public void stopPanorama() {
        native_sendcommand(PANORAMA_SHOT_STOP, 0, 0);
    }

    public final void stopPreview() {
        _stopPreview();
        this.mFaceDetectionRunning = false;
        Log.m33i(TAG, "stopPreview");
    }

    public final void stopQrCodeDetection() {
        native_sendcommand(CAMERA_CMD_STOP_QRCODE_DETECTION, 0, 0);
    }

    public void stopSmartFilter() {
        native_sendcommand(STOP_SMART_FILTER, 0, 0);
    }

    public final native void stopSmoothZoom();

    public void stopTakePicture() {
        native_sendcommand(COMMON_STOP_TAKE_PICTURE, 0, 0);
    }

    public void stopTouchAutoFocus() {
        native_sendcommand(HAL_TOUCH_AF_STARTSTOP, 0, 0);
    }

    public void stopWideMotionSelfie() {
        native_sendcommand(WIDE_MOTION_SELFIE_SHOT_STOP, 0, 0);
    }

    public void stopWideSelfie() {
        native_sendcommand(WIDE_SELFIE_SHOT_STOP, 0, 0);
    }

    public void stopZoom() {
        native_sendcommand(HAL_STOP_ZOOM, 0, 0);
    }

    public final void takePicture(ShutterCallback shutterCallback, PictureCallback pictureCallback, PictureCallback pictureCallback2) {
        takePicture(shutterCallback, pictureCallback, null, pictureCallback2);
    }

    public final void takePicture(ShutterCallback shutterCallback, PictureCallback pictureCallback, PictureCallback pictureCallback2, PictureCallback pictureCallback3) {
        this.mShutterCallback = shutterCallback;
        this.mRawImageCallback = pictureCallback;
        this.mPostviewCallback = pictureCallback2;
        this.mJpegCallback = pictureCallback3;
        int i = 0;
        if (this.mShutterCallback != null) {
            i = 2;
        }
        if (this.mRawImageCallback != null) {
            i |= 128;
        }
        if (this.mPostviewCallback != null) {
            i |= 64;
        }
        if (this.mJpegCallback != null) {
            i |= 256;
        }
        native_takePicture(i);
        this.mFaceDetectionRunning = false;
    }

    public void terminateBurstShot() {
        native_sendcommand(BURST_SHOT_TERMINATE, 0, 0);
    }

    public synchronized HashMap<String, String> unflatten(String str) {
        HashMap<String, String> hashMap;
        hashMap = new HashMap();
        Iterable<String> simpleStringSplitter = new SimpleStringSplitter(';');
        simpleStringSplitter.setString(str);
        for (String str2 : simpleStringSplitter) {
            int indexOf = str2.indexOf(61);
            if (indexOf != -1) {
                hashMap.put(str2.substring(0, indexOf), str2.substring(indexOf + 1));
            }
        }
        return hashMap;
    }

    public final native void unlock();

    public final native void unregisterRecordingSurface() throws IOException;
}
