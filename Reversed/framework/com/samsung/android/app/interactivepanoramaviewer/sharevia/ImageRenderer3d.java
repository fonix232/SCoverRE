package com.samsung.android.app.interactivepanoramaviewer.sharevia;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import com.android.internal.app.DumpHeapActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ImageRenderer3d implements Renderer {
    public static final float[] IDENTITY_MATRIX = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private static final int LANDSCAPE_MODE = 0;
    private static final int PORTRAIT_MODE = 1;
    private static final int REVERSE_LANDSCAPE_MODE = 2;
    private static final int REVERSE_PORTRAIT_MODE = 3;
    private static final String TAG = "GL_Viewer3D";
    private static int prevmode = -1;
    private int Capture = 0;
    private int CaptureHandle;
    private int CropH;
    private int CropW;
    private int ImgH;
    private int ImgW;
    private int PMatHandle;
    private float[] PerspMatrix;
    private int ScreenH;
    private int ScreenW;
    private int TLx;
    private int TLy;
    private int TexCordHandle;
    private FloatBuffer TextureBuffer;
    float[] TextureData;
    private int[] TextureHandle;
    private final FloatBuffer VertexBuffer;
    private int anglehandle;
    float anglerot = 0.0f;
    private int fragmentShader;
    private String fragmentShaderCodeTriangle;
    private int grid_num = 10;
    boolean imageset = false;
    boolean isOffscreen;
    private boolean isRunningInTestMode = false;
    private int multiplierhandle;
    boolean onCreatecalled = false;
    private int positionHandle;
    private int shaderProgram;
    private int sizeHandle;
    private float[] sizearray;
    private ByteBuffer uvBuffer;
    private int uvhandle;
    float[] vertexData;
    private int vertexShader;
    private String vertexShaderCodeTriangle;
    private int viewportHandle;
    private ByteBuffer yBuffer;
    private int yhandle;

    public ImageRenderer3d(Context context, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z, boolean z2) {
        Log.m33i(TAG, "Inside constructor");
        this.isOffscreen = z;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        if (i9 == 0) {
            inputStream = getClass().getResourceAsStream("/fragmentshadernv12.glsl");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.fragmentShaderCodeTriangle = readFile(bufferedReader);
        } else if (i9 == 1) {
            inputStream = getClass().getResourceAsStream("/fragmentshadernv12.glsl");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.fragmentShaderCodeTriangle = readFile(bufferedReader);
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            inputStream.close();
        }
        InputStream resourceAsStream = getClass().getResourceAsStream("/vertexshader3d.glsl");
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(resourceAsStream));
        this.vertexShaderCodeTriangle = readFile(bufferedReader2);
        try {
            bufferedReader2.close();
            resourceAsStream.close();
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        this.vertexData = new float[((this.grid_num * 12) * this.grid_num)];
        this.TextureData = new float[((this.grid_num * 12) * this.grid_num)];
        this.TextureHandle = new int[2];
        this.PerspMatrix = new float[9];
        System.arraycopy(this.PerspMatrix, 0, IDENTITY_MATRIX, 0, this.PerspMatrix.length);
        this.sizearray = new float[2];
        this.sizearray[0] = (float) i;
        this.sizearray[1] = (float) i2;
        this.ScreenH = i8;
        this.ScreenW = i7;
        Log.m33i(TAG, "Inside constructor  H = " + this.ScreenH + " W = " + this.ScreenW);
        Log.m33i(TAG, "Inside constructor after swap  H = " + this.ScreenH + " W = " + this.ScreenW);
        this.Capture = i10;
        int calVertices = calVertices(this.vertexData, -1.0f, -1.0f, 1.0f, 1.0f, this.grid_num);
        this.VertexBuffer = ByteBuffer.allocateDirect(calVertices * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.VertexBuffer.put(this.vertexData, 0, calVertices);
        this.VertexBuffer.position(0);
        this.ImgW = i;
        this.ImgH = i2;
        this.TLx = i3;
        this.TLy = i4;
        this.CropW = i5;
        this.CropH = i6;
        Log.m33i(TAG, " W = " + this.ImgW + " H = " + this.ImgH + " TLx = " + this.TLx + " TLy = " + this.TLy + " CropW = " + this.CropW + " CropH = " + this.CropH + " screen W = " + this.ScreenW + " screen H = " + this.ScreenH);
        calVertices = 0;
        if (!z2) {
            calVertices = calVertices(this.TextureData, ((float) this.TLx) / ((float) this.ImgW), ((float) (this.TLy + this.CropH)) / ((float) this.ImgH), ((float) (this.TLx + this.CropW)) / ((float) this.ImgW), ((float) this.TLy) / ((float) this.ImgH), this.grid_num);
        } else if (i10 == 0 || i10 == 2) {
            calVertices = calVertices(this.TextureData, ((float) (this.TLx + this.CropW)) / ((float) this.ImgW), ((float) (this.TLy + this.CropH)) / ((float) this.ImgH), ((float) this.TLx) / ((float) this.ImgW), ((float) this.TLy) / ((float) this.ImgH), this.grid_num);
        } else if (i10 == 1 || i10 == 3) {
            calVertices = calVertices(this.TextureData, ((float) this.TLx) / ((float) this.ImgW), ((float) this.TLy) / ((float) this.ImgH), ((float) (this.TLx + this.CropW)) / ((float) this.ImgW), ((float) (this.TLy + this.CropH)) / ((float) this.ImgH), this.grid_num);
        }
        this.TextureBuffer = ByteBuffer.allocateDirect(calVertices * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.TextureBuffer.put(this.TextureData, 0, calVertices);
        this.TextureBuffer.position(0);
        this.yBuffer = ByteBuffer.allocate(this.ImgW * this.ImgH);
        this.uvBuffer = ByteBuffer.allocate((this.ImgW * this.ImgH) / 2);
    }

    private int calVertices(float[] fArr, float f, float f2, float f3, float f4, int i) {
        float f5 = (f3 - f) / ((float) i);
        float f6 = (f4 - f2) / ((float) i);
        int i2 = 0;
        int i3 = 0;
        float f7 = f;
        while (i2 < i) {
            f2 += f6;
            float f8 = f7;
            int i4 = i3;
            float f9 = f8;
            for (int i5 = 0; i5 < i; i5++) {
                f9 += f5;
                int i6 = i4 + 1;
                fArr[i4] = f9 - f5;
                i4 = i6 + 1;
                fArr[i6] = f2;
                i6 = i4 + 1;
                fArr[i4] = f9 - f5;
                i4 = i6 + 1;
                fArr[i6] = f2 - f6;
                i6 = i4 + 1;
                fArr[i4] = f9;
                i4 = i6 + 1;
                fArr[i6] = f2 - f6;
                i6 = i4 + 1;
                fArr[i4] = f9 - f5;
                i4 = i6 + 1;
                fArr[i6] = f2;
                i6 = i4 + 1;
                fArr[i4] = f9;
                i4 = i6 + 1;
                fArr[i6] = f2 - f6;
                i6 = i4 + 1;
                fArr[i4] = f9;
                i4 = i6 + 1;
                fArr[i6] = f2;
            }
            i2++;
            i3 = i4;
            f7 = f;
        }
        return i3;
    }

    private int compileShader(String str, int i) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                GLES20.glDeleteShader(glCreateShader);
                glCreateShader = 0;
            }
        }
        if (glCreateShader != 0) {
            return glCreateShader;
        }
        throw new RuntimeException("Error creating shader.");
    }

    private int createShaderProgram(int i, int i2) {
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, i);
            GLES20.glAttachShader(glCreateProgram, i2);
            GLES20.glBindAttribLocation(glCreateProgram, 0, "vPos");
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] == 0) {
                GLES20.glDeleteProgram(glCreateProgram);
                glCreateProgram = 0;
            }
        }
        if (glCreateProgram != 0) {
            return glCreateProgram;
        }
        throw new RuntimeException("Error creating shader program.");
    }

    private String readFile(BufferedReader bufferedReader) {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(readLine);
                stringBuilder.append("\n");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    void Init_loadToGPU() {
        Log.m33i(TAG, "Inside InitLoadToGPU W = " + this.ImgW + " H = " + this.ImgH);
        GLES20.glGenTextures(2, this.TextureHandle, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.TextureHandle[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexImage2D(3553, 0, 6409, this.ImgW, this.ImgH, 0, 6409, 5121, this.yBuffer);
        GLES20.glUniform1i(this.yhandle, 0);
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, this.TextureHandle[1]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexImage2D(3553, 0, 6410, this.ImgW / 2, this.ImgH / 2, 0, 6410, 5121, this.uvBuffer);
        GLES20.glUniform1i(this.uvhandle, 1);
    }

    public boolean isRunningInTestMode() {
        return this.isRunningInTestMode;
    }

    void loadToGPU() {
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.TextureHandle[0]);
        GLES20.glTexImage2D(3553, 0, 6409, this.ImgW, this.ImgH, 0, 6409, 5121, this.yBuffer);
        GLES20.glUniform1i(this.yhandle, 0);
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, this.TextureHandle[1]);
        GLES20.glTexImage2D(3553, 0, 6410, this.ImgW / 2, this.ImgH / 2, 0, 6410, 5121, this.uvBuffer);
        GLES20.glUniform1i(this.uvhandle, 1);
    }

    public void onDrawFrame(GL10 gl10) {
        System.out.println("----------skkv");
        synchronized (this) {
            loadToGPU();
            GLES20.glClear(16640);
            GLES20.glUniformMatrix3fv(this.PMatHandle, 1, false, this.PerspMatrix, 0);
        }
        if (this.imageset) {
            GLES20.glDrawArrays(4, 0, ((this.grid_num * 2) * this.grid_num) * 3);
        }
        GLES20.glFinish();
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        Log.m33i(TAG, "Inside surface changed");
        GLES20.glViewport(0, 0, i, i2);
        int i3 = (this.Capture == 0 || this.Capture == 2) ? this.ImgW : this.ImgH;
        float f = (float) i3;
        i3 = (this.Capture == 0 || this.Capture == 2) ? this.ImgH : this.ImgW;
        float f2 = (float) i3;
        float f3 = ((float) i) / f < ((float) i2) / f2 ? ((float) i) / f : ((float) i2) / f2;
        GLES20.glUniform2f(this.viewportHandle, f / (((float) i) / f3), f2 / (((float) i2) / f3));
        Log.m33i("skkv", new StringBuilder(String.valueOf(f3)).append(">>>>").append(f).append(" , ").append(f2).append(" <<>>").append(i).append(" , ").append(i2).toString());
        Log.m33i("skkv", "-->" + ((((float) i) / f3) / f) + " , " + ((((float) i2) / f3) / f2));
        Log.m33i("skkv", "---->" + this.sizearray[0] + " , " + this.sizearray[1]);
        Log.m33i("screen width", Integer.toString(this.ScreenW));
        Log.m33i("screen height", Integer.toString(this.ScreenH));
        Log.m33i("crop width", Integer.toString(this.CropW));
        Log.m33i("crop height", Integer.toString(this.CropH));
        if (this.onCreatecalled) {
            if (i2 <= i) {
                prevmode = 1;
            } else {
                prevmode = 0;
            }
            this.onCreatecalled = false;
        }
        i3 = i <= i2 ? 0 : 1;
        if (prevmode == i3) {
            Log.m33i("mode", "equal");
        } else {
            int i4 = this.ScreenH;
            this.ScreenH = this.ScreenW;
            this.ScreenW = i4;
        }
        if (this.isRunningInTestMode) {
            this.ScreenH = i2;
            this.ScreenW = i;
            this.CropW = 1280;
            this.CropH = 720;
        }
        Log.m33i("view width", Integer.toString(this.ScreenW));
        Log.m33i("view height", Integer.toString(this.ScreenH));
        GLES20.glUniform1f(this.multiplierhandle, !this.isOffscreen ? 1.0f : -1.0f);
        prevmode = i3;
        if (i <= i2) {
            if (this.Capture == 0) {
                GLES20.glUniform1f(this.anglehandle, 0.0f);
                Log.m33i("onSurfaceChanged", "5");
            } else if (this.Capture == 2) {
                GLES20.glUniform1f(this.anglehandle, 180.0f);
                Log.m33i("onSurfaceChanged", "6");
            } else if (this.Capture == 1) {
                GLES20.glUniform1f(this.anglehandle, -90.0f);
                Log.m33i("onSurfaceChanged", "7");
            } else if (this.Capture == 3) {
                GLES20.glUniform1f(this.anglehandle, 90.0f);
                Log.m33i("onSurfaceChanged", "8");
            }
        } else if (this.Capture == 0) {
            GLES20.glUniform1f(this.anglehandle, 0.0f);
            Log.m33i("onSurfaceChanged", "1");
        } else if (this.Capture == 2) {
            GLES20.glUniform1f(this.anglehandle, 180.0f);
            Log.m33i("onSurfaceChanged", "2");
        } else if (this.Capture == 1) {
            GLES20.glUniform1f(this.anglehandle, -90.0f);
            Log.m33i("onSurfaceChanged", "3");
        } else if (this.Capture == 3) {
            GLES20.glUniform1f(this.anglehandle, 90.0f);
            Log.m33i("onSurfaceChanged", "4");
        }
        Init_loadToGPU();
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.onCreatecalled = true;
        Log.m33i(TAG, "Inside surface created");
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        if (this.shaderProgram != 0) {
            GLES20.glDeleteShader(this.vertexShader);
            GLES20.glDeleteShader(this.fragmentShader);
            GLES20.glDeleteProgram(this.shaderProgram);
        }
        String str = this.vertexShaderCodeTriangle;
        String str2 = this.fragmentShaderCodeTriangle;
        this.vertexShader = compileShader(str, 35633);
        this.fragmentShader = compileShader(str2, 35632);
        this.shaderProgram = createShaderProgram(this.vertexShader, this.fragmentShader);
        GLES20.glUseProgram(this.shaderProgram);
        this.positionHandle = GLES20.glGetAttribLocation(this.shaderProgram, "a_position");
        this.TexCordHandle = GLES20.glGetAttribLocation(this.shaderProgram, "a_texCoord");
        this.PMatHandle = GLES20.glGetUniformLocation(this.shaderProgram, "s_PMatrix");
        this.yhandle = GLES20.glGetUniformLocation(this.shaderProgram, "y_texture");
        this.uvhandle = GLES20.glGetUniformLocation(this.shaderProgram, "uv_texture");
        this.sizeHandle = GLES20.glGetUniformLocation(this.shaderProgram, DumpHeapActivity.KEY_SIZE);
        this.viewportHandle = GLES20.glGetUniformLocation(this.shaderProgram, "viewportSize");
        this.CaptureHandle = GLES20.glGetUniformLocation(this.shaderProgram, "captureFlag");
        this.anglehandle = GLES20.glGetUniformLocation(this.shaderProgram, "anglerot");
        this.multiplierhandle = GLES20.glGetUniformLocation(this.shaderProgram, "multiplier");
        GLES20.glDisable(2929);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnableVertexAttribArray(this.positionHandle);
        GLES20.glVertexAttribPointer(this.positionHandle, 2, 5126, true, 0, this.VertexBuffer);
        GLES20.glEnableVertexAttribArray(this.TexCordHandle);
        GLES20.glVertexAttribPointer(this.TexCordHandle, 2, 5126, true, 0, this.TextureBuffer);
        GLES20.glUniform2fv(this.sizeHandle, 1, this.sizearray, 0);
    }

    public void setImage(byte[] bArr) {
        synchronized (this) {
            Log.m33i(TAG, "Inside setImage");
            this.yBuffer.clear();
            this.uvBuffer.clear();
            this.yBuffer.put(bArr, 0, this.ImgW * this.ImgH);
            this.yBuffer.position(0);
            this.uvBuffer.put(bArr, this.ImgW * this.ImgH, (this.ImgW * this.ImgH) / 2);
            this.uvBuffer.position(0);
            this.imageset = true;
        }
    }

    public void setImage(byte[] bArr, float[] fArr) {
        if (bArr == null || fArr == null) {
            Log.m31e(TAG, "In SetImage: YUVimg or Pmat is NULL" + Arrays.toString(bArr) + "  " + Arrays.toString(fArr));
            return;
        }
        synchronized (this) {
            Log.m33i(TAG, "Inside setImage");
            this.yBuffer.clear();
            this.uvBuffer.clear();
            this.yBuffer.put(bArr, 0, this.ImgW * this.ImgH);
            this.yBuffer.position(0);
            this.uvBuffer.put(bArr, this.ImgW * this.ImgH, (this.ImgW * this.ImgH) / 2);
            this.uvBuffer.position(0);
            this.imageset = true;
            System.arraycopy(fArr, 0, this.PerspMatrix, 0, this.PerspMatrix.length);
        }
    }

    public void setRunningInTestMode(boolean z) {
        this.isRunningInTestMode = z;
    }
}
