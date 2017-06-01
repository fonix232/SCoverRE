package com.samsung.android.transcode.surfaces;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.util.Log;
import android.view.Surface;
import com.samsung.android.transcode.renderer.RenderTexture_GL_OES;
import com.samsung.android.transcode.util.Constants;
import com.samsung.android.transcode.util.OpenGlHelper;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class OutputSurface implements OnFrameAvailableListener {
    private static final int EGL_OPENGL_ES2_BIT = 4;
    public static final String EXCEPTION_FRAME_NOT_AVAILABLE = "Surface frame wait timed out";
    private EGL10 mEGL;
    private EGLContext mEGLContext;
    private EGLDisplay mEGLDisplay;
    private EGLSurface mEGLSurface;
    private boolean mFrameAvailable;
    private Object mFrameSyncObject = new Object();
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private RenderTexture_GL_OES mTextureRenderer;

    public OutputSurface(int i) {
        setup(i);
    }

    private void checkEglError(String str) {
        Object obj = null;
        while (true) {
            int eglGetError = this.mEGL.eglGetError();
            if (eglGetError == 12288) {
                break;
            }
            Log.e(Constants.TAG, str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
            obj = 1;
        }
        if (obj != null) {
            throw new RuntimeException("EGL error encountered (see log)");
        }
    }

    private void eglSetup(int i, int i2) {
        this.mEGL = (EGL10) EGLContext.getEGL();
        this.mEGLDisplay = this.mEGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (this.mEGL.eglInitialize(this.mEGLDisplay, null)) {
            EGLConfig[] eGLConfigArr = new EGLConfig[1];
            if (this.mEGL.eglChooseConfig(this.mEGLDisplay, new int[]{EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE, 8, EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_SURFACE_TYPE, 1, EGL10.EGL_RENDERABLE_TYPE, 4, EGL10.EGL_NONE}, eGLConfigArr, 1, new int[1])) {
                this.mEGLContext = this.mEGL.eglCreateContext(this.mEGLDisplay, eGLConfigArr[0], EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, EGL10.EGL_NONE});
                checkEglError("eglCreateContext");
                if (this.mEGLContext != null) {
                    this.mEGLSurface = this.mEGL.eglCreatePbufferSurface(this.mEGLDisplay, eGLConfigArr[0], new int[]{EGL10.EGL_WIDTH, i, EGL10.EGL_HEIGHT, i2, EGL10.EGL_NONE});
                    checkEglError("eglCreatePbufferSurface");
                    if (this.mEGLSurface == null) {
                        throw new RuntimeException("surface was null");
                    }
                    return;
                }
                throw new RuntimeException("null context");
            }
            throw new RuntimeException("unable to find RGB888+pbuffer EGL config");
        }
        throw new RuntimeException("unable to initialize EGL10");
    }

    private void setup(int i) {
        this.mTextureRenderer = new RenderTexture_GL_OES();
        this.mTextureRenderer.prepare(i);
        Log.d(Constants.TAG, "textureID=" + this.mTextureRenderer.getTextureId());
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureRenderer.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    public void awaitNewImage() {
        synchronized (this.mFrameSyncObject) {
            do {
                if (this.mFrameAvailable) {
                    this.mFrameAvailable = false;
                } else {
                    try {
                        this.mFrameSyncObject.wait();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            } while (this.mFrameAvailable);
            throw new RuntimeException(EXCEPTION_FRAME_NOT_AVAILABLE);
        }
        OpenGlHelper.checkGLError("before updateTexImage");
        this.mSurfaceTexture.updateTexImage();
    }

    public boolean checkForNewImage(int i) {
        synchronized (this.mFrameSyncObject) {
            do {
                if (this.mFrameAvailable) {
                    this.mFrameAvailable = false;
                    OpenGlHelper.checkGLError("before updateTexImage");
                    this.mSurfaceTexture.updateTexImage();
                    return true;
                }
                try {
                    this.mFrameSyncObject.wait((long) i);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } while (this.mFrameAvailable);
            return false;
        }
    }

    public void drawImage() {
        this.mTextureRenderer.draw(this.mSurfaceTexture);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void makeCurrent() {
        if (this.mEGL != null) {
            checkEglError("before makeCurrent");
            if (!this.mEGL.eglMakeCurrent(this.mEGLDisplay, this.mEGLSurface, this.mEGLSurface, this.mEGLContext)) {
                throw new RuntimeException("eglMakeCurrent failed");
            }
            return;
        }
        throw new RuntimeException("not configured for makeCurrent");
    }

    public void notifyFrameSyncObject() {
        synchronized (this.mFrameSyncObject) {
            this.mFrameSyncObject.notifyAll();
        }
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.d(Constants.TAG, "new frame available");
        synchronized (this.mFrameSyncObject) {
            if (this.mFrameAvailable) {
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
            this.mFrameAvailable = true;
            this.mFrameSyncObject.notifyAll();
        }
    }

    public void release() {
        if (this.mEGL != null) {
            if (this.mEGL.eglGetCurrentContext().equals(this.mEGLContext)) {
                this.mEGL.eglMakeCurrent(this.mEGLDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            }
            this.mEGL.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            this.mEGL.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        }
        if (this.mSurface != null) {
            this.mSurface.release();
        }
        this.mEGLDisplay = null;
        this.mEGLContext = null;
        this.mEGLSurface = null;
        this.mEGL = null;
        if (this.mTextureRenderer != null) {
            this.mTextureRenderer.release();
        }
        this.mTextureRenderer = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }
}
