package com.samsung.android.media;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SemSoundAssistantManager {
    public static final String ADJUST_MEDIA_ONLY = "adjust_media_volume_only";
    public static final String ALL_SOUND_MUTE = "all_sound_mute";
    public static final int DEVICE_DEFAULT = 0;
    public static final int DEVICE_SPEAKER_OR_HEADSET = 1;
    public static final String ENABLE_FLOATING_BUTTON = "enable_floating_button";
    public static final String GET_APP_VOLUME_LIST = "get_app_volume_list";
    public static final String IGNORE_AUDIO_FOCUS = "ignore_audio_focus";
    public static final int MODE_ADJUST_MEDIA_VOLUME_ONLY = 1;
    public static final int MODE_DEFAULT = 0;
    public static final String MONO_SOUND = "mono_sound";
    public static final String PARAMETER_PREFIX = "sound_assistant";
    public static final String REMOVE_APP_VOLUME = "remove_app_volume";
    public static final String SET_FORCE_OUTPUT_FOR_APP = "set_force_output_for_app";
    public static final String SOUND_BALANCE = "sound_balance";
    private static final String TAG = "SemSoundAssistant";
    public static final String UID_FOR_SOUNDASSISTANT = "uid_for_soundassistant";
    public static final String VERSION = "version";
    private AudioManager mAudioManager = ((AudioManager) this.mContext.getSystemService("audio"));
    private Context mContext;

    public SemSoundAssistantManager(Context context) {
        this.mContext = context;
    }

    private ArrayList<Integer> getIntegerArrayFromString(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ArrayList<Integer> arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(str, ";");
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            if (nextToken.length() != 0) {
                try {
                    arrayList.add(Integer.valueOf(nextToken));
                } catch (NumberFormatException e) {
                }
            }
        }
        return arrayList.size() == 0 ? null : arrayList;
    }

    public void activateFloatingButton(boolean z) {
        setSoundAssistantParam("enable_floating_button=" + (z ? 1 : 0));
    }

    public void adjustSoundBalance(int i) {
        if (i < 0 || i > 100) {
            throw new IllegalArgumentException("Bad raio value");
        }
        setSoundAssistantParam("sound_balance=" + i);
    }

    public void forceMonoSound(boolean z) {
        setSoundAssistantParam("mono_sound=" + (z ? 1 : 0));
    }

    public ArrayList<Integer> getApplicationUidListUsingAudio() {
        return getIntegerArrayFromString(this.mAudioManager.getParameters("use_audio_uids"));
    }

    public int getApplicationVolume(int i) {
        return this.mAudioManager.getAppVolume(i);
    }

    public int getAudioFrameworkVersion() {
        int i = 0;
        try {
            i = Integer.valueOf(getSoundAssistantProperty(VERSION)).intValue();
        } catch (NumberFormatException e) {
        }
        return i;
    }

    public String[] getRecommandedPackagesForSoundAssistant() {
        return this.mAudioManager.getMediaAppList();
    }

    public String getSoundAssistantParam(String str) {
        return this.mAudioManager.getAudioServiceConfig("sound_assistant;" + str);
    }

    public String getSoundAssistantProperty(String str) {
        return this.mAudioManager.getAudioServiceConfig("sound_assistant;" + str);
    }

    public int getUidIgnoredAudioFocus() {
        int i = 0;
        try {
            i = Integer.valueOf(getSoundAssistantParam(IGNORE_AUDIO_FOCUS)).intValue();
        } catch (NumberFormatException e) {
        }
        return i;
    }

    public int getVolumeKeyMode() {
        int i = 0;
        try {
            i = Integer.valueOf(getSoundAssistantProperty(ADJUST_MEDIA_ONLY)).intValue();
        } catch (NumberFormatException e) {
        }
        return i == 1 ? 1 : 0;
    }

    public void ignoreAudioFocusForApp(int i, boolean z) {
        setSoundAssistantParam("ignore_audio_focus=" + (z ? SmartFaceManager.PAGE_BOTTOM : SmartFaceManager.PAGE_MIDDLE) + ";" + UID_FOR_SOUNDASSISTANT + "=" + i);
    }

    public void initApplicationVolume(int i) {
        setSoundAssistantProperty("remove_app_volume=" + i);
    }

    public boolean isFloatingButtonActivated() {
        return Integer.valueOf(getSoundAssistantParam(ENABLE_FLOATING_BUTTON)).intValue() == 1;
    }

    public void setApplicationVolume(int i, int i2) {
        this.mAudioManager.setAppVolume(i, i2);
    }

    public void setForceDeviceForAppSoundOutput(int i, int i2) {
        if (i2 == 0 || i2 == 1) {
            setSoundAssistantParam("set_force_output_for_app=" + (i2 == 1 ? 2 : 0) + ";" + UID_FOR_SOUNDASSISTANT + "=" + i);
            return;
        }
        throw new IllegalArgumentException("Invalid parameter");
    }

    public void setSoundAssistantParam(String str) {
        this.mAudioManager.setAudioServiceConfig("sound_assistant=1;" + str);
    }

    public void setSoundAssistantProperty(String str) {
        this.mAudioManager.setAudioServiceConfig("sound_assistant=1;" + str);
    }

    public void setVolumeKeyMode(int i) {
        if (i == 0 || i == 1) {
            setSoundAssistantProperty("adjust_media_volume_only=" + i);
        } else {
            Log.e(TAG, "Invalide mode");
        }
    }
}
