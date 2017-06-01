package com.samsung.android.transcode.renderer;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import com.samsung.android.transcode.util.Constants;
import com.samsung.android.transcode.util.OpenGlHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class RenderTexture_GL_OES {
    private static final String A_POSITION = "a_Position";
    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private static final int FLOAT_SIZE_BYTES = 4;
    public static final int PREPARE_FAILURE = 0;
    public static final int PREPARE_SUCCESS = 1;
    private static final String TEXTURE_FRAGMENT_SHADER_CODE = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 v_TextureCoord;\nuniform samplerExternalOES u_TextureUnit;\nvoid main() {\n  gl_FragColor = texture2D(u_TextureUnit, v_TextureCoord);\n}\n";
    private static final String TEXTURE_VERTEX_SHADER_CODE = "uniform mat4 u_MVPMatrix;\nuniform mat4 u_STMatrix;\nattribute vec4 a_Position;\nattribute vec4 a_TextureCoordinates;\nvarying vec2 v_TextureCoord;\nvoid main() {\n  gl_Position = u_MVPMatrix * a_Position;\n  v_TextureCoord = (u_STMatrix * a_TextureCoordinates).xy;\n}\n";
    private static final String U_MVPMATRIX = "u_MVPMatrix";
    private static final String U_STMATRIX = "u_STMatrix";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";
    private static final int VERTICES_DATA_POS_COUNT = 2;
    private static final int VERTICES_DATA_POS_OFFSET = 0;
    private static final int VERTICES_DATA_STRIDE_BYTES = 16;
    private static final int VERTICES_DATA_TEX_COORD_COUNT = 2;
    private static final int VERTICES_DATA_TEX_COORD_OFFSET = 2;
    private final float[] mMVPMatrix = new float[16];
    private int mProgram;
    private final float[] mSTMatrix = new float[16];
    private int mTextureId;
    private final float[] mVerticesData = new float[]{-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private FloatBuffer mVerticesFloatBuffer = ByteBuffer.allocateDirect(this.mVerticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.mVerticesData);
    private int ma_PositionHandle;
    private int ma_TextureCoordinatesHandle;
    private int mu_MVPMatrixHandle;
    private int mu_STMatrixHandle;
    private int mu_TextureUnitHandle;

    public RenderTexture_GL_OES() {
        this.mVerticesFloatBuffer.position(0);
    }

    private void deleteTexture() {
        GLES20.glBindTexture(36197, 0);
        OpenGlHelper.deleteTexture(this.mTextureId);
        this.mTextureId = 0;
    }

    public void draw(SurfaceTexture surfaceTexture) {
        surfaceTexture.getTransformMatrix(this.mSTMatrix);
        GLES20.glUseProgram(this.mProgram);
        GLES20.glActiveTexture(GL10.GL_TEXTURE0);
        GLES20.glBindTexture(36197, this.mTextureId);
        GLES20.glUniformMatrix4fv(this.mu_MVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(this.mu_STMatrixHandle, 1, false, this.mSTMatrix, 0);
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

    public int getTextureId() {
        return this.mTextureId;
    }

    public int loadTexture(int i) {
        if (this.mTextureId != 0) {
            deleteTexture();
        }
        this.mTextureId = OpenGlHelper.loadTextureOES();
        if (this.mTextureId == 0) {
            Log.d(Constants.TAG, "not able to load new texture");
        }
        Matrix.setIdentityM(this.mMVPMatrix, 0);
        Matrix.setRotateM(this.mMVPMatrix, 0, (float) i, 0.0f, 0.0f, 1.0f);
        return this.mTextureId;
    }

    public int prepare(int i) {
        this.mProgram = OpenGlHelper.createProgram(TEXTURE_VERTEX_SHADER_CODE, TEXTURE_FRAGMENT_SHADER_CODE);
        if (this.mProgram == 0) {
            return 0;
        }
        this.mu_MVPMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, U_MVPMATRIX);
        this.mu_STMatrixHandle = GLES20.glGetUniformLocation(this.mProgram, U_STMATRIX);
        this.ma_PositionHandle = GLES20.glGetAttribLocation(this.mProgram, A_POSITION);
        this.ma_TextureCoordinatesHandle = GLES20.glGetAttribLocation(this.mProgram, A_TEXTURE_COORDINATES);
        this.mu_TextureUnitHandle = GLES20.glGetUniformLocation(this.mProgram, U_TEXTURE_UNIT);
        loadTexture(i);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        return 1;
    }

    public void release() {
        deleteTexture();
        GLES20.glDeleteProgram(this.mProgram);
        this.mProgram = 0;
        this.mu_MVPMatrixHandle = 0;
        this.mu_STMatrixHandle = 0;
        this.ma_PositionHandle = 0;
        this.ma_TextureCoordinatesHandle = 0;
        this.mu_TextureUnitHandle = 0;
        this.mVerticesFloatBuffer = null;
    }
}
