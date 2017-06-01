package com.samsung.android.codecsolution;

import android.provider.MediaStore.Video.VideoColumns;
import android.provider.SemSmartGlow;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;
import com.samsung.android.camera.core.SemCamera.Parameters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaStatisticsEvent {
    private static final String TAG = MediaStatisticsEvent.class.getSimpleName();
    private Action mAction;
    private Category mCategory;
    private Label mLabel;
    private HashMap<String, Object> mMap = new HashMap();

    enum Action {
        NONE,
        INSTANTIATE;

        public static Action valueOf(int i) {
            Log.m29d(MediaStatisticsEvent.TAG, "Action.valueOf: " + i);
            for (Enum enumR : values()) {
                if (i == enumR.ordinal()) {
                    return enumR;
                }
            }
            return NONE;
        }
    }

    enum Category {
        NONE,
        VENC,
        VDEC,
        AENC,
        ADEC;

        static Category valueOf(int i) {
            Log.m29d(MediaStatisticsEvent.TAG, "Category.valueOf: " + i);
            for (Enum enumR : values()) {
                if (i == enumR.ordinal()) {
                    return enumR;
                }
            }
            return NONE;
        }
    }

    enum Label {
        NONE("none"),
        SEC_HW_H265("shh5"),
        SEC_HW_H264("shh4"),
        SEC_HW_H263("shh3"),
        SEC_HW_VC1("shv1"),
        SEC_HW_VP8("shv8"),
        SEC_HW_VP9("shv9"),
        SEC_HW_MPEG4("shm4"),
        SEC_HW_MPEG2("shm2"),
        SEC_SW_AAC("ssac"),
        SEC_SW_AMR("ssar"),
        SEC_SW_QCELP13("ssqc"),
        SEC_SW_EVRC("ssev"),
        SEC_SW_WMA("sswa"),
        SEC_SW_AMRWBPLUS("ssap"),
        SEC_SW_WMA10PRO("sswp"),
        SEC_SW_WMALOSSLESS("sswl"),
        SEC_SW_FLAC("ssfl"),
        SEC_SW_ALAC("ssal"),
        SEC_SW_APE("ssae"),
        SEC_SW_MULTIAAC("ssma"),
        SEC_SW_MP1("ssp1"),
        SEC_SW_MP2("ssp2"),
        SEC_SW_MP3("ssp3"),
        SEC_SW_ADPCM("ssam"),
        SEC_SW_H265("ssh5"),
        SEC_SW_H264("ssh4"),
        SEC_SW_H263("ssh3"),
        SEC_SW_WMV7("ssw7"),
        SEC_SW_WMV8("ssw8"),
        SEC_SW_VC1("ssv1"),
        SEC_SW_VP8("ssv8"),
        SEC_SW_MPEG4("ssm4"),
        SEC_SW_MP43("ss43"),
        GOOGLE_SW_AAC("gsac"),
        GOOGLE_SW_AMRNB("gsan"),
        GOOGLE_SW_AMRWB("gsaw"),
        GOOGLE_SW_G711ALAW("gsal"),
        GOOGLE_SW_G711MLAW("gsml"),
        GOOGLE_SW_MP3("gsp3"),
        GOOGLE_SW_VORBIS("gsvb"),
        GOOGLE_SW_OPUS("gsop"),
        GOOGLE_SW_RAW("gsrw"),
        GOOGLE_SW_FLAC("gsfl"),
        GOOGLE_SW_GSM("gsgm"),
        GOOGLE_SW_H265("gsh5"),
        GOOGLE_SW_H264("gsh4"),
        GOOGLE_SW_H263("gsh3"),
        GOOGLE_SW_VP8("gsv8"),
        GOOGLE_SW_VP9("gsv9"),
        GOOGLE_SW_MPEG4("gsm4");
        
        private int val;

        private Label(String str) {
            this.val = MediaStatisticsEvent.getFourCCNumber(str);
        }

        static Label valueOf(int i) {
            int i2 = 0;
            Log.m29d(MediaStatisticsEvent.TAG, String.format("Label.valueOf: 0x%x", new Object[]{Integer.valueOf(i)}));
            Label[] values = values();
            int length = values.length;
            while (i2 < length) {
                Label label = values[i2];
                if (i == label.val) {
                    return label;
                }
                i2++;
            }
            return NONE;
        }
    }

    enum Type {
        NONE("none"),
        INT("vint"),
        LONG("vlng"),
        FLOAT("vflt"),
        STRING("vstr"),
        INTARRAY("aint"),
        LONGARRAY("alng"),
        ULONGARRAY("auln"),
        STRINGARRAY("astr");
        
        private int val;

        private Type(String str) {
            this.val = MediaStatisticsEvent.getFourCCNumber(str);
        }

        static Type valueOf(int i) {
            for (Type type : values()) {
                if (i == type.val) {
                    return type;
                }
            }
            return NONE;
        }

        public String toString() {
            return Integer.toString(this.val);
        }
    }

    public MediaStatisticsEvent(String str) {
        Log.m29d(TAG, "MediaStatisticsEvent: " + str);
        unflatten(str);
        this.mCategory = Category.valueOf(((Integer) this.mMap.get(VideoColumns.CATEGORY)).intValue());
        this.mAction = Action.valueOf(((Integer) this.mMap.get(Parameters.SCENE_MODE_ACTION)).intValue());
        this.mLabel = Label.valueOf(((Integer) this.mMap.get("label")).intValue());
        Log.m29d(TAG, "category: " + this.mCategory);
        Log.m29d(TAG, "action: " + this.mAction);
        Log.m29d(TAG, "label: " + this.mLabel);
    }

    private static int getFourCCNumber(String str) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i = (i << 8) | str.charAt(i2);
        }
        return i;
    }

    public String flatten() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : this.mMap.keySet()) {
            Object obj = this.mMap.get(str);
            if (obj != null) {
                stringBuilder.append(str);
                stringBuilder.append("=");
                if (obj instanceof Integer) {
                    stringBuilder.append(obj.toString());
                    stringBuilder.append("@").append(Type.INT);
                } else if (obj instanceof Long) {
                    stringBuilder.append(obj.toString());
                    stringBuilder.append("@").append(Type.LONG);
                } else if (obj instanceof Float) {
                    stringBuilder.append(obj.toString());
                    stringBuilder.append("@").append(Type.FLOAT);
                } else if (obj instanceof String) {
                    stringBuilder.append(obj);
                    stringBuilder.append("@").append(Type.STRING);
                } else if (obj instanceof List) {
                    stringBuilder.append("@a");
                } else {
                    Log.m31e(TAG, "Unknown type: " + obj);
                }
                stringBuilder.append(SemSmartGlow.PACKAGE_SEPARATOR);
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public Object get(String str) {
        return this.mMap.get(str);
    }

    public String getAction() {
        return this.mAction.name();
    }

    public String getCategory() {
        return this.mCategory.name();
    }

    public float getFloat(String str) {
        return ((Float) this.mMap.get(str)).floatValue();
    }

    public int getInt(String str) {
        return ((Integer) this.mMap.get(str)).intValue();
    }

    public String getLabel() {
        return this.mLabel.name();
    }

    public List getList(String str) {
        return (List) this.mMap.get(str);
    }

    public long getLong(String str) {
        return ((Long) this.mMap.get(str)).longValue();
    }

    public String getString(String str) {
        return (String) this.mMap.get(str);
    }

    public void put(Map<? extends String, ?> map) {
        this.mMap.putAll(map);
    }

    public void set(String str, Object obj) {
        this.mMap.put(str, obj);
    }

    public void unflatten(String str) {
        this.mMap.clear();
        Iterable<String> simpleStringSplitter = new SimpleStringSplitter(';');
        simpleStringSplitter.setString(str);
        for (String str2 : simpleStringSplitter) {
            int indexOf = str2.indexOf("=");
            if (indexOf != -1) {
                String substring = str2.substring(0, indexOf);
                int indexOf2 = str2.indexOf("@");
                Type valueOf = Type.valueOf(Integer.parseInt(str2.substring(indexOf2 + 1)));
                Object obj = null;
                if (valueOf == Type.INT) {
                    obj = Integer.valueOf(Integer.parseInt(str2.substring(indexOf + 1, indexOf2)));
                } else if (valueOf == Type.LONG) {
                    obj = Long.valueOf(Long.parseLong(str2.substring(indexOf + 1, indexOf2)));
                } else if (valueOf == Type.FLOAT) {
                    obj = Float.valueOf(Float.parseFloat(str2.substring(indexOf + 1, indexOf2)));
                } else if (valueOf == Type.STRING) {
                    obj = str2.substring(indexOf + 1, indexOf2);
                }
                this.mMap.put(substring, obj);
            }
        }
    }
}
