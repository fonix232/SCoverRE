package com.samsung.android.speech;

import com.samsung.android.emergencymode.SemEmergencyConstants;

public class Config {
    public static final int CMD_ALARM = 3;
    public static final int CMD_CALL = 2;
    public static final int CMD_CAMERA = 7;
    public static final int CMD_CANCEL = 9;
    public static final int CMD_GALLERY = 8;
    public static final int CMD_MUSIC = 4;
    public static final int CMD_RADIO = 5;
    public static final int CMD_VIDEO = 6;
    public static final int CMD_VOICETALK_ALL = 0;
    public static final int CMD_VOICETALK_SCHEDULE = 1;
    public static final int CMD_YES_NO = 10;
    public static final int COUNT_DOMAIN = 11;
    public static final int COUNT_LANGUAGE = 15;
    public static final String DEFAULT_EXTRA_LANG_PATH = "/system/voicebargeindata/include/";
    public static final String DEFAULT_PATH = "/system/voicebargeindata";
    public static final String DEFAULT_PDT_PATH = "/system/voicebargeindata/samsung/";
    public static final String DEFAULT_SAMSUNG_PATH = "/system/voicebargeindata/sasr/";
    public static final String DEFAULT_SENSORY_PATH = "/system/voicebargeindata/sensory/";
    public static final int LANGUAGE_BRAZIL_PORTUGUEE = 9;
    public static final int LANGUAGE_EU_FRENCH = 4;
    public static final int LANGUAGE_EU_GERMAN = 5;
    public static final int LANGUAGE_EU_ITALIAN = 6;
    public static final int LANGUAGE_EU_SPAINISH = 3;
    public static final int LANGUAGE_HK_CHINESE = 13;
    public static final int LANGUAGE_JAPANESE = 7;
    public static final int LANGUAGE_KOREAN = 0;
    public static final int LANGUAGE_RUSSIAN = 8;
    public static final int LANGUAGE_SG_CHINESE = 14;
    public static final int LANGUAGE_TRADITIONAL_CHINESE = 2;
    public static final int LANGUAGE_TW_CHINESE = 12;
    public static final int LANGUAGE_UK_ENGLISH = 10;
    public static final int LANGUAGE_US_ENGLISH = 1;
    public static final int LANGUAGE_US_SPAINISH = 11;
    private static final String[] MODELS_SAMSUNG = new String[]{"models_16k_KOR.bin", "models_hci_daco.bin", "models_16k_CHI.bin", "models_16k_ESP.bin", "models_16k_FRA.bin", "models_16k_GER.bin", "models_16k_ITA.bin", "models_16k_JAPANESE_bi.bin", "models_16k_RUSSIAN_bi.bin", "models_hci_daco.bin", "models_hci_daco.bin", "models_16k_ESP.bin", "models_hci_daco.bin", "models_hci_daco.bin", "models_hci_daco.bin"};
    public static final String PDT_MAIN_SUFFIX = "_v01.raw";
    public static final String PDT_SO_FILE_PATH = "/system/lib/libBargeInEngine.so";
    public static final String PDT_SO_FILE_PATH_64 = "/system/lib64/libBargeInEngine.so";
    public static final String SAMSUNG_SO_FILE_PATH = "/system/lib/libsasr-jni.so";
    public static final String SENSORY_MAIN_SUFFIX = "_v2.raw";
    public static final String SENSORY_SO_FILE_PATH = "/system/lib/libSensoryBargeInEngine.so";
    public static final String SENSORY_SO_FILE_PATH_64 = "/system/lib64/libSensoryBargeInEngine.so";
    public static final String SENSORY_SUB_SUFFIX = "_v2_2.raw";
    private static final String[] STRING_DOMAIN = new String[]{"stop", "schedule", "call", SemEmergencyConstants.TABLE_ALARM, "music", "radio", "video", "camera", "gallery", "cancel", "yesno"};
    private static final String[] STRING_SAMSUNG = new String[]{"kor", "eng", "chi", "spa", "fra", "ger", "ita", "jap", "rus", "eng", "eng", "spa", "chi", "chi", "chi"};
    private static final String[] STRING_SENSORY = new String[]{"ko_kr", "en_us", "zh_cn", "es_es", "fr_fr", "de_de", "it_it", "ja_jp", "ru_ru", "pt_br", "en_uk", "es_la", "zh_tw", "zh_hk", "zh_sg"};
    public static final String VERSION = "16.11.30";

    public static String GetPDTAM(int i, int i2) {
        if (i >= 15) {
            i = 1;
        }
        if (i2 >= 11) {
            return null;
        }
        String str = STRING_SENSORY[i];
        return DEFAULT_PDT_PATH + str + "/samsung_bargein_am_" + str + "_" + STRING_DOMAIN[i2];
    }

    public static String GetPDTGRAMMAR(int i, int i2) {
        if (i >= 15) {
            i = 1;
        }
        if (i2 >= 11) {
            return null;
        }
        String str = STRING_SENSORY[i];
        return DEFAULT_PDT_PATH + str + "/samsung_bargein_grammar_" + str + "_" + STRING_DOMAIN[i2];
    }

    public static String GetSamsungModels(int i) {
        if (i >= 15) {
            i = 1;
        }
        return GetSamsungPath(i) + "param/" + MODELS_SAMSUNG[i];
    }

    public static String GetSamsungNameList(int i) {
        if (i >= 11) {
            return null;
        }
        switch (i) {
            case 0:
                return "nameList_voicetalk_all.txt";
            case 1:
                return "nameList_voicetalk_schedule.txt";
            default:
                return "nameList_" + STRING_DOMAIN[i] + ".txt";
        }
    }

    public static String GetSamsungPath(int i) {
        if (i >= 15) {
            i = 1;
        }
        return DEFAULT_SAMSUNG_PATH + STRING_SAMSUNG[i] + "/16k/";
    }

    public static String GetSensoryAM(int i, int i2) {
        if (i >= 15) {
            i = 1;
        }
        if (i2 >= 11) {
            return null;
        }
        String str = STRING_SENSORY[i];
        return DEFAULT_SENSORY_PATH + str + "/samsung_bargein_am_" + str + "_" + STRING_DOMAIN[i2];
    }

    public static String GetSensoryGRAMMAR(int i, int i2) {
        if (i >= 15) {
            i = 1;
        }
        if (i2 >= 11) {
            return null;
        }
        String str = STRING_SENSORY[i];
        return DEFAULT_SENSORY_PATH + str + "/samsung_bargein_grammar_" + str + "_" + STRING_DOMAIN[i2];
    }
}
