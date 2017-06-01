package com.samsung.android.transcode.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import java.io.IOException;
import javax.microedition.khronos.opengles.GL10;

public class OpenGlHelper {
    public static int checkGLError(String str) {
        while (true) {
            int glGetError = GLES20.glGetError();
            if (glGetError == 0) {
                return glGetError;
            }
            Log.e(Constants.TAG, str + ": glError " + glGetError);
        }
    }

    private static int compileFragmentShader(String str) {
        return compileShader(35632, str);
    }

    private static int compileShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] != 0) {
                return glCreateShader;
            }
            Log.e(Constants.TAG, "Could not compile shader " + i + " " + GLES20.glGetShaderInfoLog(glCreateShader));
            GLES20.glDeleteShader(glCreateShader);
            return 0;
        }
        checkGLError("shader type " + i + " creation failded");
        return 0;
    }

    private static int compileVertexShader(String str) {
        return compileShader(35633, str);
    }

    public static int createProgram(String str, String str2) {
        int compileVertexShader = compileVertexShader(str);
        if (compileVertexShader == 0) {
            return 0;
        }
        int compileFragmentShader = compileFragmentShader(str2);
        if (compileFragmentShader != 0) {
            compileFragmentShader = linkProgram(compileVertexShader, compileFragmentShader);
            GLES20.glDeleteShader(compileVertexShader);
            GLES20.glDeleteShader(compileVertexShader);
            return compileFragmentShader;
        }
        GLES20.glDeleteShader(compileVertexShader);
        return 0;
    }

    public static void deleteTexture(int i) {
        GLES20.glDeleteTextures(1, new int[]{i}, 0);
    }

    public static void deleteTexture(int[] iArr) {
        GLES20.glDeleteTextures(1, iArr, 0);
    }

    private static int getOptimalSamplingSize(String str, int i, int i2, int i3) throws IOException {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        float f = (float) options.outHeight;
        float f2 = ((float) options.outWidth) / ((float) i);
        float f3 = f / ((float) i2);
        if (f2 < f3) {
            f2 = f3;
        }
        return Math.round(f2);
    }

    private static int linkProgram(int i, int i2) {
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, i);
            GLES20.glAttachShader(glCreateProgram, i2);
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] != 0) {
                return glCreateProgram;
            }
            Log.e(Constants.TAG, "Couldn't link program :" + GLES20.glGetProgramInfoLog(glCreateProgram));
            GLES20.glDeleteProgram(glCreateProgram);
            return 0;
        }
        checkGLError("CreateProgram failed");
        return 0;
    }

    public static int loadTexture(Bitmap bitmap) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        if (iArr[0] != 0) {
            GLES20.glBindTexture(GL10.GL_TEXTURE_2D, iArr[0]);
            if (checkGLError("glBindTexture error") == 0) {
                GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);
                GLES20.glTexParameteri(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
                GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, 33071.0f);
                GLES20.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, 33071.0f);
                if (checkGLError("glTexParameter error") == 0) {
                    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
                    if (checkGLError("texImage2D error") == 0) {
                        GLES20.glGenerateMipmap(GL10.GL_TEXTURE_2D);
                        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, 0);
                        return iArr[0];
                    }
                    GLES20.glDeleteTextures(1, iArr, 0);
                    return 0;
                }
                GLES20.glDeleteTextures(1, iArr, 0);
                return 0;
            }
            GLES20.glDeleteTextures(1, iArr, 0);
            return 0;
        }
        Log.e(Constants.TAG, "Could not create new opengl texture object");
        return 0;
    }

    public static int loadTexture(String str, int i, int i2, Rect rect) throws IOException {
        int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 1);
        Options options = new Options();
        options.inSampleSize = getOptimalSamplingSize(str, i, i2, attributeInt);
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        if (decodeFile != null) {
            rect.left = 0;
            rect.top = 0;
            rect.right = decodeFile.getWidth();
            rect.bottom = decodeFile.getHeight();
            int loadTexture = loadTexture(decodeFile);
            decodeFile.recycle();
            return loadTexture;
        }
        Log.e(Constants.TAG, "Could not decode bitmap. error.");
        return 0;
    }

    public static int loadTextureOES() {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        if (iArr[0] != 0) {
            GLES20.glBindTexture(36197, iArr[0]);
            if (checkGLError("glBindTexture error") == 0) {
                GLES20.glTexParameterf(36197, GL10.GL_TEXTURE_MIN_FILTER, 9728.0f);
                GLES20.glTexParameterf(36197, GL10.GL_TEXTURE_MAG_FILTER, 9729.0f);
                GLES20.glTexParameteri(36197, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(36197, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
                if (checkGLError("External OES parameter set error.") == 0) {
                    return iArr[0];
                }
                GLES20.glDeleteTextures(1, iArr, 0);
                return 0;
            }
            GLES20.glDeleteTextures(1, iArr, 0);
            return 0;
        }
        Log.e(Constants.TAG, "Could not create new opengl oes texture object");
        return 0;
    }
}
