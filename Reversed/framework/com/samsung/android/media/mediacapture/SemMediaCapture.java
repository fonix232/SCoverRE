package com.samsung.android.media.mediacapture;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.samsung.android.fingerprint.FingerprintManager;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;

public final class SemMediaCapture {
    public static final int DIRECTION_FORWARD = 0;
    public static final int DIRECTION_FORWARD_REVERSE = 2;
    public static final int DIRECTION_REVERSE = 1;
    public static final int KEY_PARAMETER_DIRECTION = 1003;
    public static final int KEY_PARAMETER_FORMAT = 1006;
    public static final int KEY_PARAMETER_FRAMERATE = 1001;
    public static final int KEY_PARAMETER_HEIGHT = 1005;
    public static final int KEY_PARAMETER_LOOP = 1002;
    public static final int KEY_PARAMETER_PLAYBACK_RATE = 1007;
    public static final int KEY_PARAMETER_WIDTH = 1004;
    public static final int LOOP_OFF = 0;
    public static final int LOOP_ON = 1;
    private static final int MEDIA_CAPTURE_DECODING_COMPLETE = 5;
    private static final int MEDIA_CAPTURE_ERROR = 100;
    private static final int MEDIA_CAPTURE_INFO = 200;
    private static final int MEDIA_CAPTURE_NOP = 0;
    private static final int MEDIA_CAPTURE_PAUSED = 4;
    private static final int MEDIA_CAPTURE_PLAYBACK_COMPLETE = 6;
    private static final int MEDIA_CAPTURE_PREPARE_COMPLETE = 1;
    private static final int MEDIA_CAPTURE_RECORDING_COMPLETE = 7;
    private static final int MEDIA_CAPTURE_RENDERING_STARTED = 8;
    private static final int MEDIA_CAPTURE_STARTED = 2;
    private static final int MEDIA_CAPTURE_STOPPED = 3;
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    public static final int MEDIA_FORMAT_GIF = 0;
    public static final int MEDIA_FORMAT_MP4 = 1;
    public static final int NORMAL = 0;
    public static final int PIP = 1;
    private static final String TAG = "SemMediaCapture";
    private EventHandler mEventHandler;
    private long mNativeContext;
    private long mNativeSurfaceTexture;
    private OnDecodingCompletionListener mOnDecodingCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPlaybackCompletionListener mOnPlaybackCompletionListener;
    private OnPreparedListener mOnPreparedListener;
    private OnRecordingCompletionListener mOnRecordingCompletionListener;
    private OnRenderingStartedListener mOnRenderingStartedListener;
    private boolean mScreenOnWhilePlaying;
    private boolean mStayAwake;
    private SurfaceHolder mSurfaceHolder;

    private class EventHandler extends Handler {
        private SemMediaCapture mMediaCapture;

        public EventHandler(SemMediaCapture semMediaCapture, Looper looper) {
            super(looper);
            this.mMediaCapture = semMediaCapture;
        }

        public void handleMessage(Message message) {
            if (this.mMediaCapture.mNativeContext == 0) {
                Log.w(SemMediaCapture.TAG, "mediacapture went away with unhandled events");
                return;
            }
            switch (message.what) {
                case 0:
                    return;
                case 1:
                    if (SemMediaCapture.this.mOnPreparedListener != null) {
                        SemMediaCapture.this.mOnPreparedListener.onPrepared(this.mMediaCapture);
                    }
                    return;
                case 5:
                    if (SemMediaCapture.this.mOnDecodingCompletionListener != null) {
                        SemMediaCapture.this.mOnDecodingCompletionListener.onDecodingCompletion(this.mMediaCapture);
                    }
                    return;
                case 6:
                    if (SemMediaCapture.this.mOnPlaybackCompletionListener != null) {
                        SemMediaCapture.this.mOnPlaybackCompletionListener.onPlaybackCompletion(this.mMediaCapture);
                    }
                    return;
                case 7:
                    if (SemMediaCapture.this.mOnRecordingCompletionListener != null) {
                        SemMediaCapture.this.mOnRecordingCompletionListener.onRecordingCompletion(this.mMediaCapture);
                    }
                    return;
                case 8:
                    if (SemMediaCapture.this.mOnRenderingStartedListener != null) {
                        SemMediaCapture.this.mOnRenderingStartedListener.onRenderingStarted(this.mMediaCapture);
                    }
                    return;
                case 100:
                    Log.e(SemMediaCapture.TAG, "Error (" + message.arg1 + FingerprintManager.FINGER_PERMISSION_DELIMITER + message.arg2 + ")");
                    if (SemMediaCapture.this.mOnErrorListener != null) {
                        boolean onError = SemMediaCapture.this.mOnErrorListener.onError(this.mMediaCapture, message.arg1, message.arg2);
                    }
                    return;
                default:
                    Log.e(SemMediaCapture.TAG, "Unknown message type " + message.what);
                    return;
            }
        }
    }

    public interface OnDecodingCompletionListener {
        void onDecodingCompletion(SemMediaCapture semMediaCapture);
    }

    public interface OnErrorListener {
        boolean onError(SemMediaCapture semMediaCapture, int i, int i2);
    }

    public interface OnInfoListener {
        boolean onInfo(SemMediaCapture semMediaCapture, int i, int i2);
    }

    public interface OnPlaybackCompletionListener {
        void onPlaybackCompletion(SemMediaCapture semMediaCapture);
    }

    public interface OnPreparedListener {
        void onPrepared(SemMediaCapture semMediaCapture);
    }

    public interface OnRecordingCompletionListener {
        void onRecordingCompletion(SemMediaCapture semMediaCapture);
    }

    public interface OnRenderingStartedListener {
        void onRenderingStarted(SemMediaCapture semMediaCapture);
    }

    static {
        System.loadLibrary("mediacapture_jni");
        native_init();
    }

    public SemMediaCapture() {
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            this.mEventHandler = new EventHandler(this, myLooper);
        } else {
            myLooper = Looper.getMainLooper();
            if (myLooper != null) {
                this.mEventHandler = new EventHandler(this, myLooper);
            } else {
                this.mEventHandler = null;
            }
        }
        native_setup(new WeakReference(this));
    }

    private native int _getCurrentPosition();

    private native void _pause() throws IllegalStateException;

    private native void _prepare() throws IOException, IllegalStateException;

    private native void _release();

    private native void _reset();

    private native void _seekTo(int i);

    private native void _setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IllegalArgumentException, IllegalStateException;

    private native void _setOutputFile(FileDescriptor fileDescriptor) throws IllegalArgumentException, IllegalStateException;

    private native void _setParameter(int i, int i2);

    private native void _setStartEndTime(int i, int i2);

    private native void _setVideoSurface(Surface surface);

    private native void _start() throws IllegalStateException;

    private native void _startCapture();

    private native void _stop() throws IllegalStateException;

    private native void _stopCapture();

    private final native void native_finalize();

    private static final native void native_init();

    private final native void native_setup(Object obj);

    private static void postEventFromNative(Object obj, int i, int i2, Object obj2) {
        SemMediaCapture semMediaCapture = (SemMediaCapture) ((WeakReference) obj).get();
        if (!(semMediaCapture == null || semMediaCapture.mEventHandler == null)) {
            semMediaCapture.mEventHandler.sendMessage(semMediaCapture.mEventHandler.obtainMessage(i, i2, 0, obj2));
        }
    }

    private void updateSurfaceScreenOn() {
        if (this.mSurfaceHolder != null) {
            this.mSurfaceHolder.setKeepScreenOn(this.mScreenOnWhilePlaying ? this.mStayAwake : false);
        }
    }

    protected void finalize() {
        native_finalize();
    }

    public int getPositionForPreview() throws IllegalStateException {
        return _getCurrentPosition();
    }

    public void pausePlayback() throws IllegalStateException {
        _pause();
    }

    public void prepare() throws IOException, IllegalStateException {
        _prepare();
    }

    public void release() throws IllegalStateException {
        this.mOnPreparedListener = null;
        this.mOnRecordingCompletionListener = null;
        this.mOnPlaybackCompletionListener = null;
        this.mOnErrorListener = null;
        _release();
    }

    public void reset() throws IllegalStateException {
        _reset();
        this.mEventHandler.removeCallbacksAndMessages(null);
    }

    public void seekForPreview(int i) throws IllegalStateException {
        _seekTo(i);
    }

    public void setDataSource(FileDescriptor fileDescriptor) throws IOException, IllegalStateException, IllegalArgumentException {
        _setDataSource(fileDescriptor, 0, 576460752303423487L);
    }

    public void setDisplay(SurfaceHolder surfaceHolder) {
        this.mSurfaceHolder = surfaceHolder;
        _setVideoSurface(surfaceHolder != null ? surfaceHolder.getSurface() : null);
        updateSurfaceScreenOn();
    }

    public void setOnDecodingCompletionListener(OnDecodingCompletionListener onDecodingCompletionListener) {
        this.mOnDecodingCompletionListener = onDecodingCompletionListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    public void setOnPlaybackCompletionListener(OnPlaybackCompletionListener onPlaybackCompletionListener) {
        this.mOnPlaybackCompletionListener = onPlaybackCompletionListener;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void setOnRecordingCompletionListener(OnRecordingCompletionListener onRecordingCompletionListener) {
        this.mOnRecordingCompletionListener = onRecordingCompletionListener;
    }

    public void setOnRenderingStartedListener(OnRenderingStartedListener onRenderingStartedListener) {
        this.mOnRenderingStartedListener = onRenderingStartedListener;
    }

    public void setOutputFile(FileDescriptor fileDescriptor) throws IOException, IllegalStateException, IllegalArgumentException {
        _setOutputFile(fileDescriptor);
    }

    public void setParameter(int i, int i2) throws IllegalStateException {
        _setParameter(i, i2);
    }

    public void setStartEndTime(int i, int i2) throws IllegalStateException {
        _setStartEndTime(i, i2);
    }

    public void startCapture() throws IllegalStateException {
        _startCapture();
    }

    public void startPlayback() throws IllegalStateException {
        _start();
    }

    public void stopCapture() throws IllegalStateException {
        _stopCapture();
    }

    public void stopPlayback() throws IllegalStateException {
        _stop();
    }
}
