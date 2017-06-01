package com.samsung.android.infoextraction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class SemInfoExtractionManager {
    private static final String EXTRACTED_INFO_DATA = "SemExtractedInfo";
    private static final String EXTRACTION_DATA_TYPE = "data_type";
    private static final String EXTRACTION_REQ_DATA = "req_data";
    private static final String EXTRACTION_REQ_TIME = "req_time";
    private static final int MSG_EXTRACTION_CALCEL = 7073;
    private static final int MSG_EXTRACTION_END = 7072;
    private static final int MSG_EXTRACTION_START = 7071;
    private static final int STRING_DATA_TYPE = 1;
    private static final int STROKE_DATA_TYPE = 3;
    private static String TAG = "semInfoextration";
    private static final int URI_DATA_TYPE = 2;
    private static Context mContext = null;
    private ServiceConnection mConnection = null;
    public InfoExtractionListener mInfoExtractionListener = null;
    private IBinder mInfoExtractionService;
    public OnExtractionCompletedListener mOnExtractionCompletedListener = null;
    private long mRequestNumber = -1;

    public enum ExtractedInfoType {
        UNKNOWN,
        DATE_TIME,
        EMAIL,
        EVENT,
        HOTKEYWORD,
        ORIGINAL,
        PLACE,
        TELNUM,
        URL
    }

    class IncomingHandler extends Handler {
        IncomingHandler() {
        }

        public void handleMessage(Message message) {
            Log.d(SemInfoExtractionManager.TAG, "received Extraction data : success");
            long j = message.getData().getLong(SemInfoExtractionManager.EXTRACTION_REQ_TIME);
            ArrayList arrayList = new ArrayList();
            List parcelableArrayList = message.getData().getParcelableArrayList(SemInfoExtractionManager.EXTRACTED_INFO_DATA);
            if (SemInfoExtractionManager.this.mOnExtractionCompletedListener != null) {
                Log.d(SemInfoExtractionManager.TAG, "sent to mOnExtractionCompletedListener ReqTime : " + j + " extracted size : " + parcelableArrayList.size());
                SemInfoExtractionManager.this.mOnExtractionCompletedListener.onExtractionCompleted(j, parcelableArrayList);
                SemInfoExtractionManager.this.mRequestNumber = -1;
                return;
            }
            Log.d(SemInfoExtractionManager.TAG, "mInfoExtractionResultListener is NULL");
            if (SemInfoExtractionManager.this.mInfoExtractionListener != null) {
                Log.d(SemInfoExtractionManager.TAG, "sent to InfoExtractionListener ReqTime : " + j + " extracted size : " + parcelableArrayList.size());
                SemInfoExtractionManager.this.mInfoExtractionListener.onCompleted((int) j, parcelableArrayList);
            } else {
                Log.d(SemInfoExtractionManager.TAG, "mInfoExtractionListener is NULL");
            }
            SemInfoExtractionManager.this.mRequestNumber = -1;
        }
    }

    public interface InfoExtractionListener {
        void onCompleted(int i, List<SemExtractedInfo> list);
    }

    public interface OnExtractionCompletedListener {
        void onExtractionCompleted(long j, List<SemExtractedInfo> list);
    }

    private static class UIBundleKey {
        private static final String CONTENTS = "contents";
        private static final String DISMISS = "dismiss";
        private static final String POSITION = "position";

        private UIBundleKey() {
        }
    }

    public SemInfoExtractionManager(Context context) throws IllegalStateException {
        Log.d(TAG, "SemInfoExtractionManager setting...");
        if (context == null) {
            Log.d(TAG, "Could not get the SemInfoExtraction service. -> context is NULL");
            throw new IllegalStateException("Could not get the SemInfoExtraction service. -> context is NULL");
        }
        mContext = context;
        if (isPenFeatureModel(mContext)) {
            Log.d(TAG, "SemInfoExtractionManager call by : " + mContext.getPackageName());
        } else {
            Log.d(TAG, "SemInfoExtraction only use for Pen Feature models.");
            throw new IllegalStateException("SemInfoExtraction only use for Pen Feature models.");
        }
    }

    private boolean bindInfoExtractionService() {
        if (mContext == null) {
            Log.d(TAG, "mContext is NULL -> can't try to bind with InfoExtractionService! ");
            return false;
        }
        Intent action = new Intent().setAction("com.samsung.android.service.hermes.InfoExtractionService");
        action.setPackage("com.sec.android.app.SmartClipService");
        boolean bindService = mContext.bindService(action, this.mConnection, 1);
        if (!bindService) {
            Log.d(TAG, "Failed to bind with InfoExtractionService service!");
        }
        return bindService;
    }

    private boolean isPenFeatureModel(Context context) {
        try {
            return context.getPackageManager().hasSystemFeature("com.sec.feature.spen_usp");
        } catch (Throwable e) {
            Log.d(TAG, "isPenFeatureModel Exception : " + e.toString());
            return false;
        }
    }

    private void requestInfoExtraction(IBinder iBinder, int i, Object obj) {
        Log.d(TAG, "requestInfoExtraction data type = " + i);
        BaseBundle bundle = new Bundle();
        bundle.putLong(EXTRACTION_REQ_TIME, this.mRequestNumber);
        bundle.putInt(EXTRACTION_DATA_TYPE, i);
        switch (i) {
            case 1:
                bundle.putString(EXTRACTION_REQ_DATA, (String) obj);
                break;
            case 2:
                bundle.putString(EXTRACTION_REQ_DATA, obj.toString());
                break;
            case 3:
                bundle.putParcelableArrayList(EXTRACTION_REQ_DATA, (ArrayList) obj);
                break;
            default:
                Log.d(TAG, "can't make data type = " + i);
                break;
        }
        Message obtain = Message.obtain(null, MSG_EXTRACTION_START);
        obtain.setData(bundle);
        obtain.replyTo = new Messenger(new IncomingHandler());
        if (iBinder != null) {
            try {
                new Messenger(iBinder).send(obtain);
                Log.d(TAG, "request Extraction : success");
                return;
            } catch (Throwable e) {
                e.printStackTrace();
                return;
            }
        }
        Log.d(TAG, "request Extraction : InfoExtractionService is null!");
    }

    private void setRequestNumber() {
        this.mRequestNumber = System.currentTimeMillis();
    }

    private void startExtraction(final int i, final Object obj) {
        if (this.mConnection == null) {
            Log.d(TAG, "mConnection is NULL");
            this.mConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    SemInfoExtractionManager.this.mInfoExtractionService = iBinder;
                    SemInfoExtractionManager.this.requestInfoExtraction(iBinder, i, obj);
                }

                public void onServiceDisconnected(ComponentName componentName) {
                    SemInfoExtractionManager.this.mInfoExtractionService = null;
                }
            };
            Log.d(TAG, "start : Binding to InfoExtractionService...");
            bindInfoExtractionService();
            return;
        }
        Log.d(TAG, "mConnection is not NULL");
        if (this.mInfoExtractionService == null) {
            Log.d(TAG, "mInfoExtractionService == null");
            bindInfoExtractionService();
            return;
        }
        Log.d(TAG, "mInfoExtractionService != null");
        requestInfoExtraction(this.mInfoExtractionService, i, obj);
    }

    public void addResultRule(int i, String str) throws IllegalStateException, IllegalArgumentException {
        Log.d(TAG, "addResultRule doesn't support in this version");
    }

    public long extract(Uri uri) throws IllegalArgumentException, IllegalStateException {
        if (uri == null) {
            return -1;
        }
        setRequestNumber();
        startExtraction(2, uri);
        return this.mRequestNumber;
    }

    public long extract(SemStrokeData semStrokeData) throws IllegalArgumentException, IllegalStateException {
        if (semStrokeData == null) {
            return -1;
        }
        setRequestNumber();
        startExtraction(3, semStrokeData);
        return this.mRequestNumber;
    }

    public long extract(String str) throws IllegalArgumentException, IllegalStateException {
        if (str == null) {
            return -1;
        }
        setRequestNumber();
        startExtraction(1, str);
        return this.mRequestNumber;
    }

    public long extract(ArrayList<SemStrokeData> arrayList) throws IllegalArgumentException, IllegalStateException {
        if (arrayList == null) {
            return -1;
        }
        setRequestNumber();
        startExtraction(3, arrayList);
        return this.mRequestNumber;
    }

    public void hideLinkPreview() throws IllegalStateException {
        try {
            Log.d(TAG, "hideLinkPreview");
            Intent intent = new Intent();
            intent.setPackage("com.sec.android.app.SmartClipService");
            intent.setAction("com.samsung.android.service.hermes.HermesTickerService");
            intent.putExtra("dismiss", true);
            mContext.startService(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setInfoExtractionListener(InfoExtractionListener infoExtractionListener) throws IllegalArgumentException {
        if (infoExtractionListener == null) {
            Log.d(TAG, "infoExtractionListener is null");
            throw new IllegalArgumentException("infoExtractionListener is null");
        } else {
            this.mInfoExtractionListener = infoExtractionListener;
        }
    }

    public void setOnExtractionCompletedListener(OnExtractionCompletedListener onExtractionCompletedListener) throws IllegalArgumentException {
        if (onExtractionCompletedListener == null) {
            Log.d(TAG, "onExtractionCompletedListener is null");
            throw new IllegalArgumentException("onExtractionCompletedListener is null");
        } else {
            this.mOnExtractionCompletedListener = onExtractionCompletedListener;
        }
    }

    public void showLinkPreview(String str, Rect rect) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalStateException("urlStr is null");
        }
        Log.d(TAG, "infoExtractionListener is null");
        try {
            Log.d(TAG, "showLinkPreview");
            Intent intent = new Intent();
            intent.setPackage("com.sec.android.app.SmartClipService");
            intent.setAction("com.samsung.android.service.hermes.HermesTickerService");
            intent.putExtra("contents", str);
            intent.putExtra("position", rect);
            intent.putExtra("dismiss", false);
            mContext.startService(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void training(String str) throws IllegalStateException, IllegalArgumentException {
        Log.d(TAG, "training doesn't support in this version");
    }
}
