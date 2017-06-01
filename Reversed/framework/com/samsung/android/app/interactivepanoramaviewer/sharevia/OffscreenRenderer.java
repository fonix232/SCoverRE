package com.samsung.android.app.interactivepanoramaviewer.sharevia;

import android.content.Context;
import android.opengl.GLES20;
import android.os.SemSystemProperties;
import android.util.Log;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

public class OffscreenRenderer {
    private static String LSI_BOARD_HERO = "samsungexynos8890";
    private static int[] fboId = new int[1];
    private static String mBoardType = SemSystemProperties.get("ro.hardware");
    private static EGL10 mEgl;
    private static EGLContext mEglContext;
    private static EGLDisplay mEglDisplay;
    private static EGLSurface mEglSurface;
    private static GL10 mGl;
    private static ImageRenderer3d mImageRenderer;
    private static int[] renderBuffId = new int[1];
    private static int surfaceHeight;
    private static int surfaceWidth;
    private static int[] textureId = new int[1];

    public static void offscreenFinalize() {
        if (mEglSurface != null) {
            if (mEglContext != null) {
                if (!mBoardType.equals(LSI_BOARD_HERO)) {
                    GLES20.glBindFramebuffer(36160, 0);
                    GLES20.glDeleteFramebuffers(1, fboId, 0);
                    GLES20.glGenRenderbuffers(1, renderBuffId, 0);
                    GLES20.glGenTextures(1, textureId, 0);
                }
                mEgl.eglDestroyContext(mEglDisplay, mEglContext);
            }
            mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
        }
        mEgl.eglTerminate(mEglDisplay);
        mGl = null;
    }

    public static void offscreenInitialize(Context context, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z) {
        mImageRenderer = new ImageRenderer3d(context, i, i2, i3, i4, i5, i6, i7, i8, i9, i10, true, z);
        surfaceWidth = i7;
        surfaceHeight = i8;
        mEgl = (EGL10) EGLContext.getEGL();
        mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        mEgl.eglInitialize(mEglDisplay, new int[2]);
        int[] iArr = new int[1];
        mEgl.eglGetConfigs(mEglDisplay, null, 0, iArr);
        EGLConfig[] eGLConfigArr = new EGLConfig[iArr[0]];
        if (mEgl.eglChooseConfig(mEglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12344}, eGLConfigArr, 1, iArr)) {
            mEglSurface = null;
            try {
                int[] iArr2;
                if (mBoardType.equals(LSI_BOARD_HERO)) {
                    iArr2 = new int[]{12375, surfaceWidth, 12374, surfaceHeight, 12344};
                    Log.m35v(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET, "offscreenInitialize: LSI_BOARD_HERO" + mBoardType);
                } else {
                    iArr2 = new int[]{12375, 10, 12374, 10, 12344};
                    Log.m35v(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET, "offscreenInitialize: " + mBoardType);
                }
                mEglSurface = mEgl.eglCreatePbufferSurface(mEglDisplay, eGLConfigArr[0], iArr2);
                if (mEglSurface != null) {
                    mEglContext = mEgl.eglCreateContext(mEglDisplay, eGLConfigArr[0], EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                    if (mEglContext != null && mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
                        mGl = (GL10) mEglContext.getGL();
                        mImageRenderer.onSurfaceCreated(mGl, null);
                        mImageRenderer.onSurfaceChanged(mGl, i7, i8);
                        if (!mBoardType.equals(LSI_BOARD_HERO)) {
                            GLES20.glGenFramebuffers(1, fboId, 0);
                            GLES20.glGenRenderbuffers(1, renderBuffId, 0);
                            GLES20.glGenTextures(1, textureId, 0);
                            GLES20.glBindTexture(3553, textureId[0]);
                            GLES20.glTexParameteri(3553, 10242, 33071);
                            GLES20.glTexParameteri(3553, 10243, 33071);
                            GLES20.glTexParameteri(3553, 10240, 9729);
                            GLES20.glTexParameteri(3553, 10241, 9729);
                            GLES20.glTexImage2D(3553, 0, 6408, i7, i8, 0, 6408, 5121, null);
                            GLES20.glBindRenderbuffer(36161, renderBuffId[0]);
                            GLES20.glRenderbufferStorage(36161, 33189, i7, i8);
                            GLES20.glBindFramebuffer(36160, fboId[0]);
                            GLES20.glFramebufferTexture2D(36160, 36064, 3553, textureId[0], 0);
                            GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, renderBuffId[0]);
                            if (GLES20.glCheckFramebufferStatus(36160) == 36053) {
                                System.out.println("--fbo pass");
                            } else {
                                throw new RuntimeException("FBO not complete.");
                            }
                        }
                        return;
                    }
                }
                mGl = null;
                return;
            } catch (Exception e) {
                System.out.println("Failed to create surface");
                mEgl.eglTerminate(mEglDisplay);
                return;
            }
        }
        System.out.println("Could not find config for GLES2");
        mEgl.eglTerminate(mEglDisplay);
    }

    public static void offscreenTransformFrame(byte[] bArr, byte[] bArr2, float[] fArr) {
        mImageRenderer.setImage(bArr, fArr);
        mImageRenderer.onDrawFrame(mGl);
        if (!mBoardType.equals(LSI_BOARD_HERO)) {
            mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
        }
        Buffer wrap = ByteBuffer.wrap(bArr2);
        wrap.position(0);
        long currentTimeMillis = System.currentTimeMillis();
        mGl.glReadPixels(0, 0, surfaceWidth, surfaceHeight, 6408, 5121, wrap);
        System.out.println("--time: " + (System.currentTimeMillis() - currentTimeMillis));
    }
}
