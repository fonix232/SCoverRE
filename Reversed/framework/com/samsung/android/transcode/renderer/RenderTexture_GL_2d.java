package com.samsung.android.transcode.renderer;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import com.samsung.android.transcode.util.Constants;
import com.samsung.android.transcode.util.OpenGlHelper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class RenderTexture_GL_2d {
    private static final String A_POSITION = "a_Position";
    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private static final int FLOAT_SIZE_BYTES = 4;
    public static final int PREPARE_FAILURE = 0;
    public static final int PREPARE_SUCCESS = 1;
    private static final String TEXTURE_FRAGMENT_SHADER_CODE = "precision mediump float;\nuniform sampler2D u_TextureUnit;\nvarying vec2 v_TextureCoordinates;\nvoid main(){ \ngl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);\n}\n";
    private static final String TEXTURE_VERTEX_SHADER_CODE = "uniform mat4 u_Matrix;\nattribute vec4 a_Position;\nattribute vec2 a_TextureCoordinates;\nvarying vec2 v_TextureCoordinates;\nvoid main(){\nv_TextureCoordinates = a_TextureCoordinates;\ngl_Position =u_Matrix*a_Position;\n}\n";
    private static final String U_MATRIX = "u_Matrix";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";
    private static final int VERTICES_DATA_POS_COUNT = 2;
    private static final int VERTICES_DATA_POS_OFFSET = 0;
    private static final int VERTICES_DATA_STRIDE_BYTES = 16;
    private static final int VERTICES_DATA_TEX_COORD_COUNT = 2;
    private static final int VERTICES_DATA_TEX_COORD_OFFSET = 2;
    private int mProgram;
    private int mTextureId;
    private final float[] mVerticesData = new float[]{-1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f};
    private FloatBuffer mVerticesFloatBuffer = ByteBuffer.allocateDirect(this.mVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.mVerticesData);
    private int ma_PositionHandle;
    private int ma_TextureCoordinatesHandle;
    private int mu_MatrixHandle;
    private int mu_TextureUnitHandle;
    private final float[] projectionMatrix = new float[16];

    public RenderTexture_GL_2d() {
        this.mVerticesFloatBuffer.position(0);
    }

    private void deleteTexture() {
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        OpenGlHelper.deleteTexture(this.mTextureId);
        this.mTextureId = 0;
    }

    public void draw() {
        GLES20.glUseProgram(this.mProgram);
        GLES20.glUniformMatrix4fv(this.mu_MatrixHandle, 1, false, this.projectionMatrix, 0);
        GLES20.glActiveTexture(GL10.GL_TEXTURE0);
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, this.mTextureId);
        GLES20.glUniform1i(this.mu_TextureUnitHandle, 0);
        this.mVerticesFloatBuffer.position(0);
        GLES20.glVertexAttribPointer(this.ma_PositionHandle, 2, GL10.GL_FLOAT, false, 16, this.mVerticesFloatBuffer);
        GLES20.glEnableVertexAttribArray(this.ma_PositionHandle);
        OpenGlHelper.checkGLError("glEnableVertexAttribArray ma_PositionHandle");
        this.mVerticesFloatBuffer.position(2);
        GLES20.glVertexAttribPointer(this.ma_TextureCoordinatesHandle, 2, GL10.GL_FLOAT, false, 16, this.mVerticesFloatBuffer);
        GLES20.glEnableVertexAttribArray(this.ma_TextureCoordinatesHandle);
        OpenGlHelper.checkGLError("glEnableVertexAttribArray ma_TextureCoordinatesHandle");
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, 771);
        GLES20.glDrawArrays(5, 0, 4);
        Log.d(Constants.TAG, "Calling glFinish blocking call");
        GLES20.glFinish();
        Log.d(Constants.TAG, "Finished glFinish");
    }

    public int loadTexture(Bitmap bitmap, int i, int i2) {
        if (this.mTextureId != 0) {
            deleteTexture();
        }
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = bitmap.getWidth();
        rect.bottom = bitmap.getHeight();
        this.mTextureId = OpenGlHelper.loadTexture(bitmap);
        if (this.mTextureId == 0) {
            Log.d(Constants.TAG, "not able to load new texture");
        }
        float width = ((float) rect.width()) / ((float) i);
        float height = ((float) rect.height()) / ((float) i2);
        if (width >= height) {
            height /= width;
            width = 1.0f;
        } else {
            width /= height;
            height = 1.0f;
        }
        Matrix.setIdentityM(this.projectionMatrix, 0);
        Matrix.scaleM(this.projectionMatrix, 0, width, height, 1.0f);
        return this.mTextureId;
    }

    public int loadTexture(String str, int i, int i2) throws IOException {
        if (this.mTextureId != 0) {
            deleteTexture();
        }
        Rect rect = new Rect();
        this.mTextureId = OpenGlHelper.loadTexture(str, i, i2, rect);
        if (this.mTextureId == 0) {
            Log.d(Constants.TAG, "not able to load new texture");
        }
        float width = ((float) rect.width()) / ((float) i);
        float height = ((float) rect.height()) / ((float) i2);
        if (width >= height) {
            height /= width;
            width = 1.0f;
        } else {
            width /= height;
            height = 1.0f;
        }
        Matrix.setIdentityM(this.projectionMatrix, 0);
        Matrix.scaleM(this.projectionMatrix, 0, width, height, 1.0f);
        return this.mTextureId;
    }

    public int prepare() {
        this.mProgram = OpenGlHelper.createProgram(TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE);
        if (this.mProgram == 0) {
            return 0;
        }
        this.mu_MatrixHandle = GLES20.glGetUniformLocation(this.mProgram, U_MATRIX);
        this.ma_PositionHandle = GLES20.glGetAttribLocation(this.mProgram, A_POSITION);
        this.ma_TextureCoordinatesHandle = GLES20.glGetAttribLocation(this.mProgram, A_TEXTURE_COORDINATES);
        this.mu_TextureUnitHandle = GLES20.glGetUniformLocation(this.mProgram, U_TEXTURE_UNIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        return 1;
    }

    public void release() {
        deleteTexture();
        GLES20.glDeleteProgram(this.mProgram);
        this.mProgram = 0;
        this.mu_MatrixHandle = 0;
        this.ma_PositionHandle = 0;
        this.ma_TextureCoordinatesHandle = 0;
        this.mu_TextureUnitHandle = 0;
        this.mVerticesFloatBuffer = null;
    }

    public void setProjectionMatrixIdentity() {
        Matrix.setIdentityM(this.projectionMatrix, 0);
    }

    public void setProjectionMatrixRotate(float f, float f2, float f3, float f4) {
        Matrix.rotateM(this.projectionMatrix, 0, f, f2, f3, f4);
    }

    public void setProjectionMatrixScale(float f, float f2) {
        Matrix.scaleM(this.projectionMatrix, 0, f, f2, 1.0f);
    }

    public void setProjectionMatrixTranslate(float f, float f2) {
        Matrix.translateM(this.projectionMatrix, 0, f, f2, 0.0f);
    }
}
