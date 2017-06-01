package com.sec.android.app.interactiveshot.jni;

public class MP4Writer {
    private static MP4Writer mInstance = null;

    public enum VmVideoColorFormat {
        VM_COLOR_FORMAT_UNSUPPORTED(0),
        VM_COLOR_FORMAT_YUV420_PLANAR(1),
        VM_COLOR_FORMAT_YUV420_SEMI_PLANAR(2),
        VM_COLOR_FORMAT_YUV420SP_TILED(3),
        VM_COLOR_FORMAT_ARGB(4),
        VM_COLOR_FORMAT_RGBA(5),
        VM_COLOR_FORMAT_RGB565(6),
        VM_COLOR_FORMAT_NV21(7);
        
        private final int mColorFormatValue;

        private VmVideoColorFormat(int i) {
            this.mColorFormatValue = i;
        }

        public int getValue() {
            return this.mColorFormatValue;
        }
    }

    public enum VmVideoOrientation {
        VM_ORIENTATION_LANDSCAPE(0),
        VM_ORIENTATION_PORTRAIT(90),
        VM_ORIENTATION_REVERSE_LANDSCAPE(180),
        VM_ORIENTATION_REVERSE_PORTRAIT(270);
        
        private final int mOrientationValue;

        private VmVideoOrientation(int i) {
            this.mOrientationValue = i;
        }

        public int getValue() {
            return this.mOrientationValue;
        }
    }

    public enum VmVideoQuality {
        VM_QUALITY_HIGH(0),
        VM_QUALITY_MEDIUM(1),
        VM_QUALITY_LOW(2);
        
        private final int mQualityValue;

        private VmVideoQuality(int i) {
            this.mQualityValue = i;
        }

        public int getValue() {
            return this.mQualityValue;
        }
    }

    private MP4Writer() {
    }

    public static MP4Writer getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        mInstance = new MP4Writer();
        return mInstance;
    }

    public native int DeInitMP4Engine();

    public native int EncodeFrame(byte[] bArr);

    public native int InitMP4Engine(int i, int i2, String str, int i3, int i4, int i5, int i6, int i7);

    public int InitMp4EngineJava(int i, int i2, String str, int i3, int i4, int i5, int i6) {
        return InitMP4Engine(i, i2, str, i3, i4, 15, i5, i6);
    }
}
