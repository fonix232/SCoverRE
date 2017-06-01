package com.samsung.android.media;

import android.util.Log;
import com.samsung.android.transcode.EncodeSoundNShot;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SemExtendedFormat {
    private static final boolean DEBUG = false;
    private static final String SEF_VERSION = "1.03";
    private static final String TAG = "SemExtendedFormat";

    public static class AudioJPEGData {
        public int audio_count = 0;
        public ArrayList endOffset = new ArrayList();
        public String filename;
        public ArrayList startOffset = new ArrayList();

        public AudioJPEGData() {
            resetAudioJpegData();
        }

        private void resetAudioJpegData() {
            this.startOffset.clear();
            this.endOffset.clear();
            this.audio_count = 0;
            this.filename = null;
        }

        public int getAudioListSize() {
            return this.audio_count;
        }

        public String getFileName() {
            return this.filename;
        }

        public int getLength(int i) {
            return (i >= 0 && i <= this.endOffset.size()) ? ((Integer) this.endOffset.get(i)).intValue() - ((Integer) this.startOffset.get(i)).intValue() : 0;
        }

        public int getStartOffset(int i) {
            return (i >= 0 && i <= this.startOffset.size()) ? ((Integer) this.startOffset.get(i)).intValue() : 0;
        }
    }

    public static class DataPosition {
        public long length;
        public long offset;
    }

    public static final class DataType {
        public static final int ANIMATED_GIF_INFO = 2400;
        public static final int AUTO_ENHANCE_FOOD_INFO = 2480;
        public static final int AUTO_ENHANCE_INFO = 2240;
        public static final int BACKUP_RESTORE_DATA = 2625;
        public static final int BEAUTY_FACE_INFO = 2368;
        public static final int BURST_SHOT_BEST_PHOTO_INFO = 2529;
        public static final int BURST_SHOT_INFO = 2528;
        public static final int CLIP_MOVIE_INFO = 2496;
        public static final int DUAL_CAMERA_INFO = 2352;
        public static final int DUBBING_SHOT_FACIAL_FEATURE_DATA = 2082;
        public static final int DUBBING_SHOT_FACIAL_FEATURE_NEUTRAL = 2081;
        public static final int DUBBING_SHOT_INFO = 2080;
        public static final int EASY_360_INFO = 2512;
        public static final int EXTRA_A = 1030;
        public static final int EXTRA_DLL = 1029;
        public static final int EXTRA_EXE = 1031;
        public static final int EXTRA_HTML = 1026;
        public static final int EXTRA_LIB = 1028;
        public static final int EXTRA_SO = 1027;
        public static final int EXTRA_SWF = 1025;
        public static final int EXTRA_XML = 1024;
        public static final int FACE_DATA = 2177;
        public static final int FACE_DATA_INFO = 2577;
        public static final int FACE_TAG_DATA = 2178;
        public static final int FAST_MOTION_DATA = 2208;
        public static final int FLIP_PHOTO_INFO = 2592;
        public static final int FOCUS_SHOT_INFO = 2112;
        public static final int FOCUS_SHOT_MAP = 2113;
        public static final int FOOD_SHOT_INFO = 2336;
        public static final int FRONT_CAMERA_SELFIE_AUTO_ENHANCE_INFO = 2321;
        public static final int FRONT_CAMERA_SELFIE_INFO = 2320;
        public static final int GOLF_SHOT_INFO = 2064;
        public static final int HIGHLIGHT_VIDEO_DATA = 2224;
        public static final int IMAGE_BMP = 3;
        public static final int IMAGE_GIF = 4;
        public static final int IMAGE_JPEG = 1;
        public static final int IMAGE_NV21 = 9;
        public static final int IMAGE_NV22 = 10;
        public static final int IMAGE_PNG = 2;
        public static final int IMAGE_RAW_BGRA = 12;
        public static final int IMAGE_RAW_RGB = 13;
        public static final int IMAGE_RAW_RGB565 = 14;
        public static final int IMAGE_RAW_RGBA = 11;
        public static final int IMAGE_TIFF = 5;
        public static final int IMAGE_UTC_DATA = 2561;
        public static final int IMAGE_YUV420 = 8;
        public static final int IMAGE_YUV422 = 7;
        public static final int IMAGE_YUV444 = 6;
        public static final int INTERACTIVE_PANORAMA_DEBUG_DATA = 2257;
        public static final int INTERACTIVE_PANORAMA_INFO = 2256;
        public static final int INTERVAL_SHOT_INFO = 2432;
        public static final int INVALID_DATA = 32766;
        public static final int INVALID_TYPE = -1;
        public static final int JPEG_180_HDR = 2672;
        public static final int JPEG_360_2D_INFO = 2640;
        public static final int JPEG_360_2D_NOTSTITCHED = 2656;
        public static final int JPEG_360_HDR_NOTSTITCHED = 2704;
        public static final int JPEG_360_HDR_STITCHED = 2688;
        public static final int MAGIC_SHOT_BEST_FACE_INFO = 2098;
        public static final int MAGIC_SHOT_BEST_PHOTO_INFO = 2097;
        public static final int MAGIC_SHOT_DRAMA_SHOT_INFO = 2100;
        public static final int MAGIC_SHOT_ERASER_INFO = 2099;
        public static final int MAGIC_SHOT_INFO = 2096;
        public static final int MAGIC_SHOT_PICTURE_MOTION_INFO = 2101;
        public static final int MOBILE_COUNTRY_CODE_DATA = 2721;
        public static final int MOTION_PHOTO_DATA = 2608;
        public static final int MOVIE_AVI = 512;
        public static final int MOVIE_MOV = 515;
        public static final int MOVIE_MP4 = 513;
        public static final int MOVIE_QUICK_TIME = 514;
        public static final int MULTI_SHOT_REFOCUS_DATA = 2144;
        public static final int PANORAMA_MOTION_DEBUG_DATA = 2274;
        public static final int PANORAMA_MOTION_INFO = 2273;
        public static final int PANORAMA_SHOT_INFO = 2272;
        public static final int PRO_MODE_INFO = 2544;
        public static final int REAR_CAMERA_SELFIE_AUTO_ENHANCE_INFO = 2305;
        public static final int REAR_CAMERA_SELFIE_INFO = 2304;
        public static final int SAMSUNG_SPECIFIC_DATA_TYPE_END = 16384;
        public static final int SAMSUNG_SPECIFIC_DATA_TYPE_START = 2048;
        public static final int SEQUENCE_SHOT_DATA = 2160;
        public static final int SLOW_MOTION_DATA = 2192;
        public static final int SOUND_AAC = 260;
        public static final int SOUND_FLAC = 259;
        public static final int SOUND_MP3 = 257;
        public static final int SOUND_OGG = 258;
        public static final int SOUND_PCM_WAV = 256;
        public static final int SOUND_SHOT_INFO = 2048;
        public static final int SPORTS_SHOT_INFO = 2288;
        public static final int SURROUND_SHOT_INFO = 2384;
        public static final int TAG_SHOT_INFO = 2448;
        public static final int USER_DATA = 0;
        public static final int UserData = 0;
        public static final int VIDEO_VIEW_MODE = 2465;
        public static final int VIRTUAL_TOUR_INFO = 2128;
        public static final int WIDE_SELFIE_INFO = 2416;
        public static final int WIDE_SELFIE_MOTION_INFO = 2417;
    }

    public static final class KeyName {
        public static final String ANIMATED_GIF_INFO = "Animated_Gif_Info";
        public static final String AUTO_ENHANCE_FOOD_INFO = "Auto_Enhance_Food_Info";
        public static final String AUTO_ENHANCE_IMAGE_PROCESSED = "Auto_Enhance_Processed";
        public static final String AUTO_ENHANCE_IMAGE_UNPROCESSED = "Auto_Enhance_Unprocessed";
        public static final String AUTO_ENHANCE_INFO = "Auto_Enhance_Info";
        public static final String BACKUP_RESTORE_DATA = "BackupRestore_Data";
        public static final String BEAUTY_FACE_INFO = "Beauty_Face_Info";
        public static final String BURST_SHOT_BEST_PHOTO_INFO = "Burst_Shot_Best_Photo_Info";
        public static final String BURST_SHOT_INFO = "Burst_Shot_Info";
        public static final String CLIP_MOVIE_INFO = "Clip_Movie_Info";
        public static final String DUAL_CAMERA_INFO = "Dual_Camera_Info";
        public static final String EASY_360_INFO = "Easy_360_Info";
        public static final String FACE_DATA = "Face_Data_%03d";
        public static final String FACE_DATA_INFO = "Face_Data_Info";
        public static final String FACE_TAG_DATA = "Face_Tag_Data_%03d";
        public static final String FAST_MOTION_DATA = "FastMotion_Data";
        public static final String FLIP_PHOTO_INFO = "Flip_Photo_Info";
        public static final String FLIP_PHOTO_JEPG_TEMPLATE = "FlipPhoto_%03d";
        public static final String FOCUS_SHOT_INFO = "FocusShot_Meta_Info";
        public static final String FOCUS_SHOT_JEPG_TEMPLATE = "FocusShot_%d";
        public static final String FOCUS_SHOT_MAP = "FocusShot_Map";
        public static final String FOOD_SHOT_INFO = "Food_Shot_Info";
        public static final String FRONT_CAMERA_SELFIE_INFO = "Front_Cam_Selfie_Info";
        public static final String HIGHLIGHT_VIDEO_DATA = "HighlightVideo_Data";
        public static final String IMAGE_UTC_DATA = "Image_UTC_Data";
        public static final String INTERACTIVE_PANORAMA_DEBUG_DATA = "Interactive_Panorama_Debug_Data";
        public static final String INTERACTIVE_PANORAMA_INFO = "Interactive_Panorama_Info";
        public static final String INTERACTIVE_PANORAMA_MP4_TEMPLATE = "Interactive_Panorama_%03d";
        public static final String INTERVAL_SHOT_INFO = "Interval_Shot_Info";
        public static final String INVALID_DATA = "Invalid_Data";
        public static final String JPEG_180_HDR = "Jpeg180_HDR";
        public static final String JPEG_360_2D_INFO = "Jpeg360_2D_Info";
        public static final String JPEG_360_2D_NOTSTITCHED = "Jpeg360_2D_NotStitched";
        public static final String JPEG_360_HDR_NOTSTITCHED = "Jpeg360_HDR_NotStitched";
        public static final String JPEG_360_HDR_STITCHED = "Jpeg360_HDR_Stitched";
        public static final String MAGIC_SHOT_BEST_FACE_INFO = "MagicShot_Best_Face_Info";
        public static final String MAGIC_SHOT_BEST_FACE_JEPG = "MagicShot_Best_Face_JPG";
        public static final String MAGIC_SHOT_BEST_PHOTO_INFO = "MagicShot_Best_Photo_Info";
        public static final String MAGIC_SHOT_BEST_PHOTO_JEPG = "MagicShot_Best_Photo_JPG";
        public static final String MAGIC_SHOT_DRAMA_SHOT_INFO = "MagicShot_Drama_Shot_Info";
        public static final String MAGIC_SHOT_DRAMA_SHOT_JEPG = "MagicShot_Drama_Shot_JPG";
        public static final String MAGIC_SHOT_ERASER_INFO = "MagicShot_Eraser_Info";
        public static final String MAGIC_SHOT_ERASER_JEPG = "MagicShot_Eraser_JPG";
        public static final String MAGIC_SHOT_INFO = "MagicShot_Info";
        public static final String MAGIC_SHOT_JEPG_TEMPLATE = "MagicShot_%03d";
        public static final String MAGIC_SHOT_PICTURE_MOTION_INFO = "MagicShot_Pic_Motion_Info";
        public static final String MAGIC_SHOT_PICTURE_MOTION_JEPG = "MagicShot_Pic_Motion_JPG";
        public static final String MOBILE_COUNTRY_CODE_DATA = "MCC_Data";
        public static final String MOTION_PHOTO_DATA = "MotionPhoto_Data";
        public static final String PANORAMA_MOTION_DEBUG_DATA = "Motion_Panorama_Debug_Data";
        public static final String PANORAMA_MOTION_INFO = "Motion_Panorama_Info";
        public static final String PANORAMA_MOTION_MP4_TEMPLATE = "Motion_Panorama_MP4_%03d";
        public static final String PANORAMA_MOTION_SOUND_TEMPLATE = "Motion_Panorama_Sound_%03d";
        public static final String PANORAMA_SHOT_INFO = "Panorama_Shot_Info";
        public static final String PRO_MODE_INFO = "Pro_Mode_Info";
        public static final String REAR_CAMERA_SELFIE_INFO = "Rear_Cam_Selfie_Info";
        public static final String SEQUENCE_SHOT_DATA = "SequenceShot_Data";
        public static final String SLOW_MOTION_DATA = "SlowMotion_Data";
        public static final String SOUND_SHOT_INFO = "SoundShot_Meta_Info";
        public static final String SOUND_SHOT_WAVE = "SoundShot_000";
        public static final String SPORTS_SHOT_INFO = "Sports_Shot_Info";
        public static final String SURROUND_SHOT_INFO = "Surround_Shot_Info";
        public static final String TAG_SHOT_INFO = "Tag_Shot_Info";
        public static final String VIDEO_VIEW_MODE = "Video_View_Mode";
        public static final String VIRTUAL_TOUR_INFO = "VirtualTour_Info";
        public static final String VIRTUAL_TOUR_JEPG_TEMPLATE = "VirtualTour_%03d";
        public static final String WIDE_SELFIE_INFO = "Wide_Selfie_Info";
        public static final String WIDE_SELFIE_MOTION_INFO = "Wide_Selfie_Motion_Info";
        public static final String WIDE_SELFIE_MOTION_MP4_TEMPLATE = "Wide_Selfie_Motion_MP4_%03d";
    }

    public static final class Options {
        public static final int OVERWRITE_IF_EXISTS = 1;
        public static final int OVERWRITE_IF_EXISTS_MP4 = 4096;
        public static final int SKIP_IF_EXISTS = 0;
        public static final int SKIP_IF_EXISTS_MP4 = 256;
        public static final int SUBSTITUTE_IF_EXIST = 16;
    }

    public static class QdioJPEGData {
        public int audio_count = 0;
        public ArrayList endOffset = new ArrayList();
        public String filename;
        public ArrayList startOffset = new ArrayList();

        public QdioJPEGData() {
            resetQdioJpegData();
        }

        private void resetQdioJpegData() {
            this.startOffset.clear();
            this.endOffset.clear();
            this.audio_count = 0;
            this.filename = null;
        }

        public int getAudioListSize() {
            return this.audio_count;
        }

        public String getFileName() {
            return this.filename;
        }

        public int getLength(int i) {
            return (i >= 0 && i <= this.endOffset.size()) ? ((Integer) this.endOffset.get(i)).intValue() - ((Integer) this.startOffset.get(i)).intValue() : 0;
        }

        public int getStartOffset(int i) {
            return (i >= 0 && i <= this.startOffset.size()) ? ((Integer) this.startOffset.get(i)).intValue() : 0;
        }
    }

    public static class SEFDataPosition64 {
        public long length;
        public long offset;
    }

    public static class SEFDataPosition {
        public int length;
        public int offset;
    }

    public static class SEFSubDataPosition64 {
        public long length;
        public long offset;
    }

    public static class SEFSubDataPosition {
        public int length;
        public int offset;
    }

    public static int addData(File file, String str, File file2, int i, int i2) throws IOException {
        Throwable e;
        Throwable th;
        String canonicalPath = file.getCanonicalPath();
        String canonicalPath2 = file2.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return 0;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return 0;
        } else if (canonicalPath2 == null || canonicalPath2.length() <= 0) {
            Log.e(TAG, "Invalid SEF Data File name: " + canonicalPath2);
            return 0;
        } else if (i == 2048) {
            int i3 = -1;
            if (file2.length() <= 0) {
                return 0;
            }
            byte[] bArr = new byte[((int) file2.length())];
            FileInputStream fileInputStream = null;
            try {
                FileInputStream fileInputStream2 = new FileInputStream(file2);
                try {
                    fileInputStream2.read(bArr);
                    try {
                        fileInputStream2.close();
                    } catch (Throwable e2) {
                        e2.printStackTrace();
                    }
                    return saveAudioJPEG(canonicalPath, bArr);
                } catch (Exception e3) {
                    e = e3;
                    fileInputStream = fileInputStream2;
                    i3 = 0;
                    try {
                        e.printStackTrace();
                        try {
                            fileInputStream.close();
                        } catch (Throwable e22) {
                            e22.printStackTrace();
                        }
                        return 0;
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            fileInputStream.close();
                        } catch (Throwable e222) {
                            e222.printStackTrace();
                        }
                        if (i3 == 0) {
                            return i3;
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = fileInputStream2;
                    fileInputStream.close();
                    if (i3 == 0) {
                        return i3;
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e = e4;
                i3 = 0;
                e.printStackTrace();
                fileInputStream.close();
                return 0;
            }
        } else if (i2 == 256) {
            return SEFJNI.addSEFDataFileToMP4(canonicalPath, str, str.length(), null, 0, canonicalPath2, i, 0);
        } else if (i2 == 4096) {
            return SEFJNI.addSEFDataFileToMP4(canonicalPath, str, str.length(), null, 0, canonicalPath2, i, 1);
        } else {
            return SEFJNI.addSEFDataFile(canonicalPath, str, str.length(), null, 0, canonicalPath2, i, i2);
        }
    }

    public static int addData(File file, String str, byte[] bArr, int i, int i2) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return 0;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return 0;
        } else if (bArr == null || bArr.length <= 0) {
            Log.e(TAG, "Invalid data");
            return 0;
        } else if (i == 2048) {
            return saveAudioJPEG(canonicalPath, bArr);
        } else {
            if (i2 == 256) {
                return SEFJNI.addSEFDataToMP4(canonicalPath, str, str.length(), null, 0, bArr, bArr.length, i, 0);
            } else if (i2 == 4096) {
                return SEFJNI.addSEFDataToMP4(canonicalPath, str, str.length(), null, 0, bArr, bArr.length, i, 1);
            } else {
                return SEFJNI.addSEFData(canonicalPath, str, str.length(), null, 0, bArr, bArr.length, i, i2);
            }
        }
    }

    public static int addFastSEFData(String str, String str2, byte[] bArr, int i, int i2) {
        return addFastSEFData(str, str2, bArr, null, i, i2);
    }

    public static int addFastSEFData(String str, String str2, byte[] bArr, byte[] bArr2, int i, int i2) {
        int i3 = 0;
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return 0;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return 0;
        } else if (bArr == null || bArr.length <= 0) {
            Log.e(TAG, "Invalid data");
            return 0;
        } else {
            int length = str2.length();
            if (bArr2 != null) {
                i3 = bArr2.length;
            }
            return SEFJNI.addFastSEFData(str, str2, length, bArr2, i3, bArr, bArr.length, i, i2);
        }
    }

    public static int addFastSEFDataFile(String str, String str2, String str3, int i, int i2) {
        return addFastSEFDataFile(str, str2, str3, null, i, i2);
    }

    public static int addFastSEFDataFile(String str, String str2, String str3, byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return 0;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return 0;
        } else if (str3 == null || str3.length() <= 0) {
            Log.e(TAG, "Invalid SEF Data File name: " + str3);
            return 0;
        } else {
            int length = str2.length();
            if (bArr != null) {
                i3 = bArr.length;
            }
            return SEFJNI.addFastSEFDataFile(str, str2, length, bArr, i3, str3, i, i2);
        }
    }

    public static int addSEFData(File file, String str, File file2, int i, int i2) throws IOException {
        return addSEFData(file, str, file2, null, i, i2);
    }

    public static int addSEFData(File file, String str, File file2, byte[] bArr, int i, int i2) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        String canonicalPath2 = file2.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return 0;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return 0;
        } else if (canonicalPath2 == null || canonicalPath2.length() <= 0) {
            Log.e(TAG, "Invalid SEF Data File name: " + canonicalPath2);
            return 0;
        } else if (i2 == 16) {
            return SEFJNI.addFastSEFDataFile(canonicalPath, str, str.length(), bArr, bArr == null ? 0 : bArr.length, canonicalPath2, i, i2);
        } else if (i2 == 256) {
            return SEFJNI.addSEFDataFileToMP4(canonicalPath, str, str.length(), bArr, bArr == null ? 0 : bArr.length, canonicalPath2, i, 0);
        } else if (i2 == 4096) {
            return SEFJNI.addSEFDataFileToMP4(canonicalPath, str, str.length(), bArr, bArr == null ? 0 : bArr.length, canonicalPath2, i, 1);
        } else {
            return SEFJNI.addSEFDataFile(canonicalPath, str, str.length(), bArr, bArr == null ? 0 : bArr.length, canonicalPath2, i, i2);
        }
    }

    public static int addSEFData(File file, String str, byte[] bArr, int i, int i2) throws IOException {
        return addSEFData(file, str, bArr, null, i, i2);
    }

    public static int addSEFData(File file, String str, byte[] bArr, byte[] bArr2, int i, int i2) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return 0;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return 0;
        } else if (bArr == null || bArr.length <= 0) {
            Log.e(TAG, "Invalid data");
            return 0;
        } else if (i2 == 16) {
            return SEFJNI.addFastSEFData(canonicalPath, str, str.length(), bArr2, bArr2 == null ? 0 : bArr2.length, bArr, bArr.length, i, i2);
        } else if (i2 == 256) {
            return SEFJNI.addSEFDataToMP4(canonicalPath, str, str.length(), bArr2, bArr2 == null ? 0 : bArr2.length, bArr, bArr.length, i, 0);
        } else if (i2 == 4096) {
            return SEFJNI.addSEFDataToMP4(canonicalPath, str, str.length(), bArr2, bArr2 == null ? 0 : bArr2.length, bArr, bArr.length, i, 1);
        } else {
            return SEFJNI.addSEFData(canonicalPath, str, str.length(), bArr2, bArr2 == null ? 0 : bArr2.length, bArr, bArr.length, i, i2);
        }
    }

    public static int addSEFData(String str, String str2, byte[] bArr, int i, int i2) {
        return addSEFData(str, str2, bArr, null, i, i2);
    }

    public static int addSEFData(String str, String str2, byte[] bArr, byte[] bArr2, int i, int i2) {
        int i3 = 0;
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return 0;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return 0;
        } else if (bArr == null || bArr.length <= 0) {
            Log.e(TAG, "Invalid data");
            return 0;
        } else if (i2 == 16) {
            r2 = str2.length();
            if (bArr2 != null) {
                i3 = bArr2.length;
            }
            return SEFJNI.addFastSEFData(str, str2, r2, bArr2, i3, bArr, bArr.length, i, i2);
        } else {
            r2 = str2.length();
            if (bArr2 != null) {
                i3 = bArr2.length;
            }
            return SEFJNI.addSEFData(str, str2, r2, bArr2, i3, bArr, bArr.length, i, i2);
        }
    }

    public static int addSEFDataFile(String str, String str2, String str3, int i, int i2) {
        return addSEFDataFile(str, str2, str3, null, i, i2);
    }

    public static int addSEFDataFile(String str, String str2, String str3, byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return 0;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return 0;
        } else if (str3 == null || str3.length() <= 0) {
            Log.e(TAG, "Invalid SEF Data File name: " + str3);
            return 0;
        } else {
            int length = str2.length();
            if (bArr != null) {
                i3 = bArr.length;
            }
            return SEFJNI.addSEFDataFile(str, str2, length, bArr, i3, str3, i, i2);
        }
    }

    public static int addSEFDataFiles(String str, String[] strArr, String[] strArr2, int[] iArr, int i) {
        int length = strArr.length;
        if (length != strArr2.length) {
            Log.e(TAG, "Data Count is different. ( keyNames data count= " + length + ", dataFileNames data count= " + strArr2.length + " )");
        } else if (length != iArr.length) {
            Log.e(TAG, "Data Count is different. ( keyNames data count= " + length + ", dataTypes data count= " + iArr.length + " )");
        }
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return 0;
        }
        int[] iArr2 = new int[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            iArr2[i2] = strArr[i2].length();
        }
        return SEFJNI.addSEFDataFiles(str, strArr, iArr2, strArr2, iArr, i, length);
    }

    public static QdioJPEGData checkAudioInJPEG(String str) {
        if (str != null && str.length() > 0) {
            return QdioJNI.checkAudioInJPEG(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return null;
    }

    public static int clearAudioData(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.clearQdioData(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return 0;
    }

    public static int clearFastSEFData(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.fastClearSEFData(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return 0;
    }

    public static int clearQdioData(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.clearQdioData(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return 0;
    }

    public static int clearSEFData(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.clearSEFData(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return 0;
    }

    public static boolean compact(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        return deleteSEFData(file, KeyName.INVALID_DATA);
    }

    public static void convertImageToMP4(String str, String str2) {
        try {
            File file = new File(str);
            int majorDataType = getMajorDataType(str);
            switch (majorDataType) {
                case 2048:
                    new EncodeSoundNShot(str2, 5, str).encode();
                    return;
                case DataType.INTERACTIVE_PANORAMA_INFO /*2256*/:
                    InteractivePanoramaConverter.getInstance().convertToMP4(str, str2);
                    return;
                case DataType.PANORAMA_SHOT_INFO /*2272*/:
                    if (hasSEFData(file, (int) DataType.PANORAMA_MOTION_INFO)) {
                        MotionPanoramaConverter.getInstance().convertToMP4(str, str2);
                        return;
                    }
                    return;
                case DataType.WIDE_SELFIE_INFO /*2416*/:
                    if (hasSEFData(file, (int) DataType.WIDE_SELFIE_MOTION_INFO)) {
                        SelfMotionPanoramaConverter.getInstance().convertToMP4(str, str2);
                        return;
                    }
                    return;
                case DataType.MOTION_PHOTO_DATA /*2608*/:
                    MotionPhotoConverter.getInstance().convertToMp4(str, str2);
                    return;
                default:
                    Log.e(TAG, "This type of file is not yet supported. type=" + majorDataType);
                    return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        e.printStackTrace();
    }

    public static int copyAdioInJPEGtoPNG(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + str);
            return 0;
        } else if (str2 != null && str2.length() > 0) {
            return QdioJNI.copyAdioInJPEGtoPNG(str, str2);
        } else {
            Log.e(TAG, "Invalid dst file name: " + str2);
            return 0;
        }
    }

    public static int copyAllData(File file, File file2) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        String canonicalPath2 = file2.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + canonicalPath);
            return 0;
        } else if (canonicalPath2 != null && canonicalPath2.length() > 0) {
            return SEFJNI.copyAllSEFData(canonicalPath, canonicalPath2);
        } else {
            Log.e(TAG, "Invalid dst file name: " + canonicalPath2);
            return 0;
        }
    }

    public static int copyAllSEFData(File file, File file2) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        String canonicalPath2 = file2.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + canonicalPath);
            return 0;
        } else if (canonicalPath2 != null && canonicalPath2.length() > 0) {
            return SEFJNI.copyAllSEFData(canonicalPath, canonicalPath2);
        } else {
            Log.e(TAG, "Invalid dst file name: " + canonicalPath2);
            return 0;
        }
    }

    public static int copyAllSEFData(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + str);
            return 0;
        } else if (str2 != null && str2.length() > 0) {
            return SEFJNI.copyAllSEFData(str, str2);
        } else {
            Log.e(TAG, "Invalid dst file name: " + str2);
            return 0;
        }
    }

    public static int copyAudioData(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid src file name: " + str);
            return 0;
        } else if (str2 != null && str2.length() > 0) {
            return QdioJNI.copyAdioInJPEGtoPNG(str, str2);
        } else {
            Log.e(TAG, "Invalid dst file name: " + str2);
            return 0;
        }
    }

    public static boolean deleteAllData(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == 1) ? QdioJNI.DeleteQdioFromFile(canonicalPath) == 1 : SEFJNI.clearSEFData(canonicalPath) == 1;
        } else {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return false;
        }
    }

    public static boolean deleteAllSEFData(File file) throws IOException {
        return deleteAllSEFData(file, 1);
    }

    public static boolean deleteAllSEFData(File file, int i) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == 1) ? QdioJNI.DeleteQdioFromFile(canonicalPath) == 1 : i == 16 ? SEFJNI.fastClearSEFData(canonicalPath) == 1 : SEFJNI.clearSEFData(canonicalPath) == 1;
        } else {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return false;
        }
    }

    public static int deleteAudioData(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.deleteQdioData(str, KeyName.SOUND_SHOT_WAVE, KeyName.SOUND_SHOT_WAVE.length());
        }
        Log.e(TAG, "Invalid file name: " + str);
        return 0;
    }

    public static boolean deleteData(File file, String str) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return false;
        } else if (str != null && str.length() > 0) {
            return SEFJNI.deleteSEFData(canonicalPath, str, str.length()) == 1;
        } else {
            Log.e(TAG, "Invalid key name: " + str);
            return false;
        }
    }

    public static boolean deleteFastSEFData(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return false;
        } else if (str2 != null && str2.length() > 0) {
            return SEFJNI.fastDeleteSEFData(str, str2, str2.length()) == 1;
        } else {
            Log.e(TAG, "Invalid key name: " + str2);
            return false;
        }
    }

    public static int deleteQdioData(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.deleteQdioData(str, KeyName.SOUND_SHOT_WAVE, KeyName.SOUND_SHOT_WAVE.length());
        }
        Log.e(TAG, "Invalid file name: " + str);
        return 0;
    }

    public static int deleteSEFData(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return 0;
        } else if (str2 != null && str2.length() > 0) {
            return SEFJNI.deleteSEFData(str, str2, str2.length());
        } else {
            Log.e(TAG, "Invalid key name: " + str2);
            return 0;
        }
    }

    public static boolean deleteSEFData(File file, String str) throws IOException {
        return deleteSEFData(file, str, 1);
    }

    public static boolean deleteSEFData(File file, String str, int i) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return false;
        } else if (str != null && str.length() > 0) {
            return i == 16 ? SEFJNI.fastDeleteSEFData(canonicalPath, str, str.length()) == 1 : SEFJNI.deleteSEFData(canonicalPath, str, str.length()) == 1;
        } else {
            Log.e(TAG, "Invalid key name: " + str);
            return false;
        }
    }

    public static AudioJPEGData getAudioDataInJPEG(String str) {
        if (str != null && str.length() > 0) {
            return QdioJNI.getAudioDataInJPEG(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return null;
    }

    public static byte[] getAudioStreamBuffer(AudioJPEGData audioJPEGData, int i) throws IOException {
        return QdioJNI.getAudioStreamBuffer(audioJPEGData, i);
    }

    public static byte[] getAudioStreamBuffer(QdioJPEGData qdioJPEGData, int i) throws IOException {
        return QdioJNI.getAudioStreamBuffer(qdioJPEGData, i);
    }

    public static byte[] getData(File file, String str) throws IOException {
        Throwable e;
        Throwable th;
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return null;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return null;
        } else {
            byte[] bArr = null;
            FileInputStream fileInputStream = null;
            if (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == 1) {
                return QdioJNI.getAudioStreamBuffer(QdioJNI.checkAudioInJPEG(canonicalPath), 0);
            }
            try {
                FileInputStream fileInputStream2 = new FileInputStream(canonicalPath);
                try {
                    SEFDataPosition64 sEFDataPosition64 = getSEFDataPosition64(canonicalPath, str);
                    if (sEFDataPosition64 == null) {
                        fileInputStream2.close();
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    }
                    long j = sEFDataPosition64.offset;
                    long j2 = j + sEFDataPosition64.length;
                    bArr = new byte[((int) sEFDataPosition64.length)];
                    if (j < 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else if (fileInputStream2.skip(j) == 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else if (fileInputStream2.read(bArr) == 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        fileInputStream = fileInputStream2;
                        return bArr;
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    try {
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return bArr;
            }
        }
    }

    public static int getDataCount(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return SEFJNI.getSEFDataCount(canonicalPath);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath);
        return -1;
    }

    public static DataPosition getDataPosition(File file, String str) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return null;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return null;
        } else {
            DataPosition dataPosition = null;
            DataPosition dataPosition2 = new DataPosition();
            long[] audioDataPositionArray;
            if (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == 1) {
                audioDataPositionArray = QdioJNI.getAudioDataPositionArray(canonicalPath);
                if (audioDataPositionArray == null) {
                    Log.w(TAG, "No Sound data is found in file.");
                    return null;
                }
                dataPosition2.offset = audioDataPositionArray[0];
                dataPosition2.length = audioDataPositionArray[1];
                return dataPosition2;
            }
            audioDataPositionArray = SEFJNI.getSEFDataPosition64(canonicalPath, str);
            if (audioDataPositionArray == null) {
                Log.w(TAG, "No SEF data is found in file.");
                return null;
            }
            dataPosition2.offset = audioDataPositionArray[0];
            dataPosition2.length = audioDataPositionArray[1];
            return dataPosition2;
        }
    }

    public static long[] getDataPositionArray(File file, String str) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return null;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return null;
        } else {
            long[] jArr = null;
            if (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == 1) {
                return QdioJNI.getAudioDataPositionArray(canonicalPath);
            }
            jArr = SEFJNI.getSEFDataPosition64(canonicalPath, str);
            if (jArr != null) {
                return jArr;
            }
            Log.w(TAG, "No SEF data matching to given keyName is found in file.");
            return null;
        }
    }

    public static int getDataType(File file, String str) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return -1;
        } else if (str != null && str.length() > 0) {
            return SEFJNI.getSEFDataType(canonicalPath, str);
        } else {
            Log.e(TAG, "Invalid key name: " + str);
            return -1;
        }
    }

    public static int[] getDataTypeArray(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return SEFJNI.listSEFDataTypes(canonicalPath);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath);
        return null;
    }

    public static String[] getKeyNameArray(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return SEFJNI.listKeyNames(canonicalPath);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath);
        return null;
    }

    public static String[] getKeyNameArray(File file, int i) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0 && i != -1) {
            return SEFJNI.listKeyNamesByDataType(canonicalPath, i);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath + ", Data Type: " + i);
        return null;
    }

    public static int getMajorDataType(String str) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return -1;
        } else if (SEFJNI.isSEFFile(str) == 0) {
            return -1;
        } else {
            try {
                int[] listSEFDataTypes = listSEFDataTypes(new File(str));
                if (listSEFDataTypes == null) {
                    Log.e(TAG, "No data type has been found : " + str);
                    return -1;
                }
                int length = listSEFDataTypes.length - 1;
                while (length > -1) {
                    if (listSEFDataTypes[length] >= 2048 && listSEFDataTypes[length] <= 16384 && (listSEFDataTypes[length] & 15) == 0) {
                        return listSEFDataTypes[length];
                    }
                    length--;
                }
                Log.e(TAG, "No major data type has been found : " + str);
                return -1;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getSEFData(File file, String str) throws IOException {
        Throwable e;
        Throwable th;
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return null;
        } else if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str);
            return null;
        } else {
            byte[] bArr = null;
            FileInputStream fileInputStream = null;
            if (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == 1) {
                return QdioJNI.getAudioStreamBuffer(QdioJNI.checkAudioInJPEG(canonicalPath), 0);
            }
            try {
                FileInputStream fileInputStream2 = new FileInputStream(canonicalPath);
                try {
                    SEFDataPosition64 sEFDataPosition64 = getSEFDataPosition64(canonicalPath, str);
                    if (sEFDataPosition64 == null) {
                        fileInputStream2.close();
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    }
                    long j = sEFDataPosition64.offset;
                    long j2 = j + sEFDataPosition64.length;
                    bArr = new byte[((int) sEFDataPosition64.length)];
                    if (j < 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else if (fileInputStream2.skip(j) == 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else if (fileInputStream2.read(bArr) == 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        fileInputStream = fileInputStream2;
                        return bArr;
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    try {
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return bArr;
            }
        }
    }

    public static byte[] getSEFData(String str, String str2) throws IOException {
        Throwable e;
        Throwable th;
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return null;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return null;
        } else {
            byte[] bArr = null;
            FileInputStream fileInputStream = null;
            try {
                FileInputStream fileInputStream2 = new FileInputStream(str);
                try {
                    SEFDataPosition64 sEFDataPosition64 = getSEFDataPosition64(str, str2);
                    if (sEFDataPosition64 == null) {
                        fileInputStream2.close();
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    }
                    long j = sEFDataPosition64.offset;
                    long j2 = j + sEFDataPosition64.length;
                    bArr = new byte[((int) sEFDataPosition64.length)];
                    if (j < 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else if (fileInputStream2.skip(j) == 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else if (fileInputStream2.read(bArr) == 0) {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        return null;
                    } else {
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        fileInputStream = fileInputStream2;
                        return bArr;
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileInputStream = fileInputStream2;
                    try {
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return bArr;
            }
        }
    }

    public static int getSEFDataCount(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return SEFJNI.getSEFDataCount(canonicalPath);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath);
        return -1;
    }

    public static int getSEFDataCount(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.getSEFDataCount(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return -1;
    }

    public static SEFDataPosition getSEFDataPosition(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return null;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return null;
        } else {
            int[] sEFDataPosition = SEFJNI.getSEFDataPosition(str, str2);
            if (sEFDataPosition == null) {
                Log.w(TAG, "No SEF data is found in file.");
                return null;
            }
            SEFDataPosition sEFDataPosition2 = new SEFDataPosition();
            sEFDataPosition2.offset = sEFDataPosition[0];
            sEFDataPosition2.length = sEFDataPosition[1];
            return sEFDataPosition2;
        }
    }

    public static SEFDataPosition64 getSEFDataPosition64(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return null;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return null;
        } else {
            long[] sEFDataPosition64 = SEFJNI.getSEFDataPosition64(str, str2);
            if (sEFDataPosition64 == null) {
                Log.w(TAG, "No SEF data is found in file.");
                return null;
            }
            SEFDataPosition64 sEFDataPosition642 = new SEFDataPosition64();
            sEFDataPosition642.offset = sEFDataPosition64[0];
            sEFDataPosition642.length = sEFDataPosition64[1];
            return sEFDataPosition642;
        }
    }

    public static int[] getSEFDataPositionArray(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return null;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return null;
        } else {
            int[] sEFDataPosition = SEFJNI.getSEFDataPosition(str, str2);
            if (sEFDataPosition != null) {
                return sEFDataPosition;
            }
            Log.w(TAG, "No SEF data is found in file.");
            return null;
        }
    }

    public static int getSEFDataType(File file, String str) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return -1;
        } else if (str != null && str.length() > 0) {
            return SEFJNI.getSEFDataType(canonicalPath, str);
        } else {
            Log.e(TAG, "Invalid key name: " + str);
            return -1;
        }
    }

    public static int getSEFDataType(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return -1;
        } else if (str2 != null && str2.length() > 0) {
            return SEFJNI.getSEFDataType(str, str2);
        } else {
            Log.e(TAG, "Invalid key name: " + str2);
            return -1;
        }
    }

    public static SEFSubDataPosition getSEFSubDataPosition(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return null;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return null;
        } else {
            int[] sEFSubDataPosition = SEFJNI.getSEFSubDataPosition(str, str2);
            if (sEFSubDataPosition == null) {
                Log.w(TAG, "No SEF sub data is found in file.");
                return null;
            }
            SEFSubDataPosition sEFSubDataPosition2 = new SEFSubDataPosition();
            sEFSubDataPosition2.offset = sEFSubDataPosition[0];
            sEFSubDataPosition2.length = sEFSubDataPosition[1];
            return sEFSubDataPosition2;
        }
    }

    public static SEFSubDataPosition64 getSEFSubDataPosition64(String str, String str2) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return null;
        } else if (str2 == null || str2.length() <= 0) {
            Log.e(TAG, "Invalid key name: " + str2);
            return null;
        } else {
            long[] sEFSubDataPosition64 = SEFJNI.getSEFSubDataPosition64(str, str2);
            if (sEFSubDataPosition64 == null) {
                Log.w(TAG, "No SEF sub data is found in file.");
                return null;
            }
            SEFSubDataPosition64 sEFSubDataPosition642 = new SEFSubDataPosition64();
            sEFSubDataPosition642.offset = sEFSubDataPosition64[0];
            sEFSubDataPosition642.length = sEFSubDataPosition64[1];
            return sEFSubDataPosition642;
        }
    }

    public static String getVersion() {
        return "1.03_" + SEFJNI.getNativeVersion();
    }

    public static boolean hasData(File file, int i) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0 || i == -1) {
            Log.e(TAG, "Invalid file name: " + canonicalPath + ", Data Type: " + i);
            return false;
        } else if (SEFJNI.isSEFFile(canonicalPath) == 0) {
            return false;
        } else {
            int[] listSEFDataTypes = listSEFDataTypes(file);
            if (listSEFDataTypes == null) {
                Log.e(TAG, "Invalid file : " + canonicalPath);
                return false;
            }
            for (int length = listSEFDataTypes.length - 1; length > -1; length--) {
                if (i == listSEFDataTypes[length]) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean hasData(File file, String str) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0 || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath + ", keyName: " + str);
            return false;
        } else if (SEFJNI.isSEFFile(canonicalPath) == 0) {
            return false;
        } else {
            String[] listKeyNames = listKeyNames(file);
            if (listKeyNames == null) {
                Log.e(TAG, "Invalid file : " + canonicalPath);
                return false;
            } else if (listKeyNames.length > 0 || listKeyNames == null) {
                for (int length = listKeyNames.length - 1; length > -1; length--) {
                    if (str.equals(listKeyNames[length])) {
                        return true;
                    }
                }
                return false;
            } else {
                Log.e(TAG, "Invalid file : " + canonicalPath);
                return false;
            }
        }
    }

    public static boolean hasDataType(String str, int i) {
        if (str == null || str.length() <= 0 || i == -1) {
            Log.e(TAG, "Invalid file name: " + str + ", Data Type: " + i);
            return false;
        } else if (SEFJNI.isSEFFile(str) == 0) {
            return false;
        } else {
            int[] listSEFDataTypes = listSEFDataTypes(str);
            if (listSEFDataTypes == null) {
                Log.e(TAG, "Invalid file : " + str);
                return false;
            }
            for (int length = listSEFDataTypes.length - 1; length > -1; length--) {
                if (i == listSEFDataTypes[length]) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean hasSEFData(File file, int i) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0 || i == -1) {
            Log.e(TAG, "Invalid file name: " + canonicalPath + ", Data Type: " + i);
            return false;
        } else if (SEFJNI.isSEFFile(canonicalPath) == 0) {
            return false;
        } else {
            int[] listSEFDataTypes = listSEFDataTypes(file);
            if (listSEFDataTypes == null) {
                Log.e(TAG, "Invalid file : " + canonicalPath);
                return false;
            }
            for (int length = listSEFDataTypes.length - 1; length > -1; length--) {
                if (i == listSEFDataTypes[length]) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean hasSEFData(File file, String str) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0 || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath + ", keyName: " + str);
            return false;
        } else if (SEFJNI.isSEFFile(canonicalPath) == 0) {
            return false;
        } else {
            String[] listKeyNames = listKeyNames(file);
            if (listKeyNames == null) {
                Log.e(TAG, "Invalid file : " + canonicalPath);
                return false;
            } else if (listKeyNames.length > 0 || listKeyNames == null) {
                for (int length = listKeyNames.length - 1; length > -1; length--) {
                    if (str.equals(listKeyNames[length])) {
                        return true;
                    }
                }
                return false;
            } else {
                Log.e(TAG, "Invalid file : " + canonicalPath);
                return false;
            }
        }
    }

    public static int isAudioJPEG(String str) {
        return QdioJNI.isJPEG(str);
    }

    public static int isJPEG(String str) {
        return QdioJNI.isJPEG(str);
    }

    public static boolean isSEFFile(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return false;
        }
        boolean z = (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == -1) ? false : true;
        return z;
    }

    public static boolean isSEFFile(String str) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
            return false;
        }
        return SEFJNI.isSEFFile(str) != 0;
    }

    public static boolean isValidFile(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath == null || canonicalPath.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + canonicalPath);
            return false;
        }
        boolean z = (SEFJNI.isSEFFile(canonicalPath) == 0 && QdioJNI.isJPEG(canonicalPath) == -1) ? false : true;
        return z;
    }

    public static String[] listKeyNames(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return SEFJNI.listKeyNames(canonicalPath);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath);
        return null;
    }

    public static String[] listKeyNames(File file, int i) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0 && i != -1) {
            return SEFJNI.listKeyNamesByDataType(canonicalPath, i);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath + ", Data Type: " + i);
        return null;
    }

    public static String[] listKeyNames(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.listKeyNames(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return null;
    }

    public static String[] listKeyNamesByDataType(String str, int i) {
        if (str != null && str.length() > 0 && i != -1) {
            return SEFJNI.listKeyNamesByDataType(str, i);
        }
        Log.e(TAG, "Invalid file name: " + str + ", Data Type: " + i);
        return null;
    }

    public static int[] listSEFDataTypes(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        if (canonicalPath != null && canonicalPath.length() > 0) {
            return SEFJNI.listSEFDataTypes(canonicalPath);
        }
        Log.e(TAG, "Invalid file name: " + canonicalPath);
        return null;
    }

    public static int[] listSEFDataTypes(String str) {
        if (str != null && str.length() > 0) {
            return SEFJNI.listSEFDataTypes(str);
        }
        Log.e(TAG, "Invalid file name: " + str);
        return null;
    }

    public static int saveAudioJPEG(String str, String str2, byte[] bArr) {
        if (str != null && bArr != null && bArr.length > 0) {
            return SEFJNI.saveAudioJPEG(str, bArr, bArr.length, str2, str2.length());
        }
        Log.e(TAG, "Invalid file name: " + str);
        if (bArr != null) {
            Log.e(TAG, "saveAudioJPEG input parameter is null :  audioStream.length = " + bArr.length);
        } else {
            Log.e(TAG, "saveAudioJPEG input parameter is null ");
        }
        return 0;
    }

    public static int saveAudioJPEG(String str, byte[] bArr) {
        return saveAudioJPEG(str, KeyName.SOUND_SHOT_WAVE, bArr);
    }

    public static void showSEFDataList(String str) {
        if (str == null || str.length() <= 0) {
            Log.e(TAG, "Invalid file name: " + str);
        }
        if (SEFJNI.isSEFFile(str) != 0) {
        }
    }
}
