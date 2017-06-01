package com.samsung.android.media;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Process;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public final class SemMediaResourceHelper {
    private static final boolean DEBUG = true;
    private static final int EVENT_ADD_RESOURCE = 1;
    private static final int EVENT_ERROR = 100;
    private static final int EVENT_REMOVE_RESOURCE = 2;
    public static final int RESOURCE_TYPE_ALL = 0;
    public static final int RESOURCE_TYPE_AUDIO = 1;
    public static final int RESOURCE_TYPE_VIDEO = 2;
    private static final String TAG = "SemMediaResourceHelper";
    private static SemMediaResourceHelper mMediaResourceHelper = null;
    private EventHandler mEventHandler;
    private long mNativeContext;
    private boolean mOwnResourceEventExcluded;
    private int mPid = 0;
    private ResourceInfoChangedListener mResourceInfoChangedListener = null;
    private int mResourceType;

    private class EventHandler extends Handler {
        private SemMediaResourceHelper mMediaResourceHelper;

        public EventHandler(SemMediaResourceHelper semMediaResourceHelper, Looper looper) {
            super(looper);
            this.mMediaResourceHelper = semMediaResourceHelper;
        }

        public void handleMessage(Message message) {
            ArrayList arrayList;
            switch (message.what) {
                case 1:
                    Log.i(SemMediaResourceHelper.TAG, "onAdd");
                    arrayList = (ArrayList) message.obj;
                    if (SemMediaResourceHelper.this.mResourceInfoChangedListener != null) {
                        SemMediaResourceHelper.this.mResourceInfoChangedListener.onAdd(arrayList);
                        return;
                    }
                    return;
                case 2:
                    Log.i(SemMediaResourceHelper.TAG, "onRemove");
                    arrayList = (ArrayList) message.obj;
                    if (SemMediaResourceHelper.this.mResourceInfoChangedListener != null) {
                        SemMediaResourceHelper.this.mResourceInfoChangedListener.onRemove(arrayList);
                        return;
                    }
                    return;
                case 100:
                    Log.i(SemMediaResourceHelper.TAG, "onError");
                    if (SemMediaResourceHelper.this.mResourceInfoChangedListener != null) {
                        SemMediaResourceHelper.this.mResourceInfoChangedListener.onError(this.mMediaResourceHelper);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public final class MediaResourceInfo {
        private final boolean mIsSecured;
        private final int mPid;
        private final int mResourceType;

        MediaResourceInfo(int i, boolean z, int i2) {
            this.mResourceType = i;
            this.mIsSecured = z;
            this.mPid = i2;
        }

        public int getPid() {
            return this.mPid;
        }

        public int getResourceType() {
            return this.mResourceType;
        }

        public boolean isSecured() {
            return this.mIsSecured;
        }
    }

    public interface ResourceInfoChangedListener {
        void onAdd(ArrayList<MediaResourceInfo> arrayList);

        void onError(SemMediaResourceHelper semMediaResourceHelper);

        void onRemove(ArrayList<MediaResourceInfo> arrayList);
    }

    static {
        System.loadLibrary("mediaresourcehelper");
    }

    private SemMediaResourceHelper(int i, boolean z) {
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
        this.mResourceType = i;
        this.mOwnResourceEventExcluded = z;
        this.mPid = Process.myPid();
        native_setup(new WeakReference(this));
        Log.i(TAG, "SemMediaResourceHelper() resourceType : " + i + ", ownResourceEventExcluded : " + z + ", myPid : " + this.mPid);
    }

    public static synchronized SemMediaResourceHelper createInstance(int i, boolean z) {
        SemMediaResourceHelper semMediaResourceHelper;
        synchronized (SemMediaResourceHelper.class) {
            if (mMediaResourceHelper == null) {
                mMediaResourceHelper = new SemMediaResourceHelper(i, z);
            } else {
                Log.i(TAG, "SemMediaResourceHelper is already created");
            }
            semMediaResourceHelper = mMediaResourceHelper;
        }
        return semMediaResourceHelper;
    }

    private ArrayList<MediaResourceInfo> makeMediaResourceInfo(Parcel parcel) {
        ArrayList<MediaResourceInfo> arrayList = new ArrayList();
        if (parcel != null) {
            int readInt = parcel.readInt();
            if (readInt > 0) {
                Log.i(TAG, "makeMediaResourceInfo mOwnResourceEventExcluded : " + this.mOwnResourceEventExcluded + ", mPid : " + this.mPid);
                for (int i = 0; i < readInt; i++) {
                    int readInt2 = parcel.readInt();
                    boolean z = parcel.readInt() == 1;
                    int readInt3 = parcel.readInt();
                    Log.i(TAG, "[" + (i + 1) + "] makeMediaResourceInfo resourceType : " + readInt2 + " isSecured : " + z + ", pid : " + readInt3);
                    if ((this.mResourceType == 0 || this.mResourceType == readInt2) && (!this.mOwnResourceEventExcluded || (this.mOwnResourceEventExcluded && this.mPid > 0 && this.mPid != readInt3))) {
                        arrayList.add(new MediaResourceInfo(readInt2, z, readInt3));
                    }
                }
            }
        }
        return arrayList;
    }

    private native void native_enableObserver(boolean z) throws IllegalStateException;

    private final native void native_finalize();

    private native void native_getMediaResourceInfo(int i, Parcel parcel) throws IllegalStateException;

    private final native void native_release();

    private final native void native_setup(Object obj);

    private static void postEventFromNative(Object obj, int i, int i2, int i3, Object obj2) {
        SemMediaResourceHelper semMediaResourceHelper = (SemMediaResourceHelper) ((WeakReference) obj).get();
        if (semMediaResourceHelper == null) {
            Log.w(TAG, "semMediaResourceHelper ref is null");
            return;
        }
        if (semMediaResourceHelper.mEventHandler != null) {
            if (obj2 != null) {
                Parcel parcel = (Parcel) obj2;
                ArrayList makeMediaResourceInfo = semMediaResourceHelper.makeMediaResourceInfo(parcel);
                parcel.recycle();
                obj2 = makeMediaResourceInfo;
            }
            semMediaResourceHelper.mEventHandler.sendMessage(semMediaResourceHelper.mEventHandler.obtainMessage(i, i2, i3, obj2));
        }
    }

    protected void finalize() {
        native_finalize();
    }

    public final ArrayList<MediaResourceInfo> getMediaResourceInfo(int i) throws IllegalStateException {
        Parcel obtain = Parcel.obtain();
        try {
            native_getMediaResourceInfo(i, obtain);
            ArrayList<MediaResourceInfo> makeMediaResourceInfo = makeMediaResourceInfo(obtain);
            return makeMediaResourceInfo;
        } finally {
            obtain.recycle();
        }
    }

    public void release() {
        native_release();
        if (this.mEventHandler != null) {
            this.mEventHandler.removeCallbacksAndMessages(null);
        }
        this.mResourceInfoChangedListener = null;
        this.mEventHandler = null;
        mMediaResourceHelper = null;
    }

    public synchronized void setOwnResourceEventExcluded(boolean z) {
        Log.i(TAG, "setOwnResourceEventExcluded() ownResourceEventExcluded : " + z);
        this.mOwnResourceEventExcluded = z;
    }

    public void setResourceInfoChangedListener(ResourceInfoChangedListener resourceInfoChangedListener) throws IllegalStateException {
        this.mResourceInfoChangedListener = resourceInfoChangedListener;
        if (this.mResourceInfoChangedListener != null) {
            native_enableObserver(true);
        } else {
            native_enableObserver(false);
        }
    }

    public synchronized void setResourceTypeForEvent(int i) {
        Log.i(TAG, "setResourceTypeForEvent() resourceType : " + i);
        this.mResourceType = i;
    }
}
