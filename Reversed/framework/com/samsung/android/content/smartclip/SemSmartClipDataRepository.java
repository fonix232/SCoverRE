package com.samsung.android.content.smartclip;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SemSmartClipDataRepository implements Parcelable {
    public static final String CONTENT_TYPE_AUDIO = "music";
    public static final String CONTENT_TYPE_DEFAULT = "image";
    public static final String CONTENT_TYPE_IMAGE = "image";
    public static final String CONTENT_TYPE_VIDEO = "video";
    public static final String CONTENT_TYPE_WEB = "web";
    public static final String CONTENT_TYPE_YOUTUBE = "youtube";
    public static final Creator<SemSmartClipDataRepository> CREATOR = new C10241();
    protected static final String FIELD_CAPTURED_IMAGE_PATH = "captured_image_path";
    protected static final String FIELD_CAPTURED_IMAGE_STYLE = "captured_image_style";
    protected static final String FIELD_CONTENT_RECT = "content_rect";
    protected static final String FIELD_CONTENT_TYPE = "content_type";
    protected static final String FIELD_META_TAGS = "meta_tags";
    protected static final String FIELD_META_TAG_EXTRA_DATA = "meta_tag_extra_value";
    protected static final String FIELD_META_TAG_TYPE = "meta_tag_type";
    protected static final String FIELD_META_TAG_VALUE = "meta_tag_value";
    protected static final String FIELD_REPOSITORY_ID = "repository_id";
    public static final int IMAGE_STYLE_LASSO = 0;
    public static final int IMAGE_STYLE_PIN_MODE = 3;
    public static final int IMAGE_STYLE_RECTANGLE = 1;
    public static final int IMAGE_STYLE_SEGMENTATION = 2;
    protected static final String TAG = "SemSmartClipDataRepository";
    protected String mAppPackageName;
    protected String mCapturedImageFilePath;
    protected int mCapturedImageFileStyle;
    protected Rect mContentRect;
    protected String mContentType;
    protected SmartClipDataCropperImpl mCropper;
    private int mPenWindowBorder;
    protected String mRepositoryId;
    protected SmartClipDataRootElement mRootElement;
    private RectF mScaleRect;
    protected SmartClipMetaTagArrayImpl mTags;
    protected int mTargetWindowLayer;
    private Rect mWinFrameRect;

    static class C10241 implements Creator<SemSmartClipDataRepository> {
        C10241() {
        }

        public SemSmartClipDataRepository createFromParcel(Parcel parcel) {
            Log.m29d(SemSmartClipDataRepository.TAG, "SemSmartClipDataRepository.createFromParcel called");
            SemSmartClipDataRepository semSmartClipDataRepository = new SemSmartClipDataRepository();
            semSmartClipDataRepository.readFromParcel(parcel);
            return semSmartClipDataRepository;
        }

        public SemSmartClipDataRepository[] newArray(int i) {
            return new SemSmartClipDataRepository[i];
        }
    }

    public SemSmartClipDataRepository() {
        this(null);
    }

    public SemSmartClipDataRepository(SemSmartClipDataCropper semSmartClipDataCropper) {
        this(semSmartClipDataCropper, new Rect(0, 0, 0, 0), new RectF(0.0f, 0.0f, 1.0f, 1.0f));
    }

    public SemSmartClipDataRepository(SemSmartClipDataCropper semSmartClipDataCropper, Rect rect, RectF rectF) {
        this(semSmartClipDataCropper, new Rect(0, 0, 0, 0), new RectF(0.0f, 0.0f, 1.0f, 1.0f), 0);
    }

    public SemSmartClipDataRepository(SemSmartClipDataCropper semSmartClipDataCropper, Rect rect, RectF rectF, int i) {
        this.mRootElement = new SmartClipDataRootElement();
        this.mContentType = null;
        this.mContentRect = null;
        this.mTags = null;
        this.mCropper = null;
        this.mCapturedImageFilePath = null;
        this.mCapturedImageFileStyle = 1;
        this.mAppPackageName = null;
        this.mTargetWindowLayer = -1;
        this.mRepositoryId = null;
        this.mWinFrameRect = null;
        this.mScaleRect = null;
        this.mPenWindowBorder = 0;
        this.mCropper = (SmartClipDataCropperImpl) semSmartClipDataCropper;
        this.mWinFrameRect = new Rect(rect);
        this.mScaleRect = new RectF(rectF);
        this.mPenWindowBorder = i;
    }

    public SemSmartClipDataRepository(String str) {
        this();
        if (str != null) {
            setupRepositoryFromString(str, this);
            return;
        }
        throw new IllegalArgumentException();
    }

    private void setupRepositoryFromString(String str, SemSmartClipDataRepository semSmartClipDataRepository) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has(FIELD_CONTENT_TYPE)) {
                semSmartClipDataRepository.mContentType = jSONObject.getString(FIELD_CONTENT_TYPE);
            }
            if (jSONObject.has(FIELD_REPOSITORY_ID)) {
                semSmartClipDataRepository.mRepositoryId = jSONObject.getString(FIELD_REPOSITORY_ID);
            }
            if (jSONObject.has(FIELD_CONTENT_RECT)) {
                JSONArray jSONArray = jSONObject.getJSONArray(FIELD_CONTENT_RECT);
                semSmartClipDataRepository.mContentRect = new Rect(jSONArray.getInt(0), jSONArray.getInt(1), jSONArray.getInt(2), jSONArray.getInt(3));
            }
            if (jSONObject.has(FIELD_CAPTURED_IMAGE_PATH)) {
                String string = jSONObject.getString(FIELD_CAPTURED_IMAGE_PATH);
                int i = jSONObject.getInt(FIELD_CAPTURED_IMAGE_STYLE);
                if (string != null) {
                    semSmartClipDataRepository.setCapturedImage(string, i);
                }
            }
            if (jSONObject.has(FIELD_META_TAGS)) {
                semSmartClipDataRepository.mTags = new SmartClipMetaTagArrayImpl();
                JSONArray jSONArray2 = jSONObject.getJSONArray(FIELD_META_TAGS);
                int length = jSONArray2.length();
                for (int i2 = 0; i2 < length; i2++) {
                    SemSmartClipMetaTag semSmartClipExtendedMetaTag;
                    JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                    String string2 = jSONObject2.getString(FIELD_META_TAG_TYPE);
                    String str2 = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
                    try {
                        str2 = jSONObject2.getString(FIELD_META_TAG_VALUE);
                    } catch (JSONException e) {
                        Log.m31e(TAG, "There is no meta value! type=" + string2);
                    }
                    try {
                        byte[] decode = Base64.decode(jSONObject2.getString(FIELD_META_TAG_EXTRA_DATA), 0);
                        Log.m29d(TAG, "Decoding : Length of extra data = " + decode.length);
                        semSmartClipExtendedMetaTag = new SemSmartClipExtendedMetaTag(string2, str2, decode);
                    } catch (JSONException e2) {
                        semSmartClipExtendedMetaTag = new SemSmartClipMetaTag(string2, str2);
                    }
                    semSmartClipDataRepository.mTags.addMetaTag(semSmartClipExtendedMetaTag);
                }
            }
        } catch (Throwable e3) {
            Log.m31e(TAG, "JSONException throwed : " + e3.toString());
            e3.printStackTrace();
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean determineContentType() {
        Object obj = null;
        Object obj2 = null;
        Object obj3 = null;
        Object obj4 = null;
        Object obj5 = null;
        SmartClipDataElementImpl smartClipDataElementImpl = this.mRootElement;
        while (smartClipDataElementImpl != null) {
            View view = smartClipDataElementImpl.getView();
            if (view != null) {
                Object obj6 = null;
                for (SemSmartClipMetaTag value : getMetaTag("url")) {
                    String value2 = value.getValue();
                    if (value2 != null && !value2.isEmpty()) {
                        obj6 = 1;
                        break;
                    }
                }
                if (getMetaTag(SemSmartClipMetaTagType.FILE_PATH_AUDIO).size() > 0) {
                    obj3 = 1;
                }
                if (getMetaTag(SemSmartClipMetaTagType.FILE_PATH_VIDEO).size() > 0) {
                    obj4 = 1;
                }
                if (getMetaTag(SemSmartClipMetaTagType.FILE_PATH_IMAGE).size() > 0) {
                    obj5 = 1;
                }
                if (obj6 != null) {
                    if ((view instanceof WebView) || view.getClass().getName().equals("android.webkitsec.WebView") || view.getClass().getName().equals("org.chromium.content.browser.ChromeView") || view.getClass().getName().equals("org.samsung.content.sbrowser.SbrContentView") || view.getClass().getName().equals("com.sec.chromium.content.browser.SbrContentView") || view.getClass().getName().equals("org.chromium.content.browser.JellyBeanContentView")) {
                        obj = 1;
                    } else if (this.mAppPackageName != null && this.mAppPackageName.equals("com.google.android.youtube") && view.getClass().getName().endsWith("PlayerView")) {
                        obj2 = 1;
                    }
                }
                if (getMetaTag(SemSmartClipMetaTagType.HTML).size() > 0) {
                    obj = 1;
                }
            }
            smartClipDataElementImpl = smartClipDataElementImpl.traverseNextElement(this.mRootElement);
        }
        String str = obj3 != null ? CONTENT_TYPE_AUDIO : obj4 != null ? CONTENT_TYPE_VIDEO : obj5 != null ? "image" : obj2 != null ? CONTENT_TYPE_YOUTUBE : obj != null ? CONTENT_TYPE_WEB : "image";
        this.mContentType = str;
        return true;
    }

    public boolean dump(boolean z) {
        Log.m29d(TAG, "----- Start of SmartClip repository informations -----");
        Log.m29d(TAG, "** Content type : " + getContentType());
        Log.m29d(TAG, "** Meta area rect : " + getContentRect().toString());
        Log.m29d(TAG, "** Captured image file path : " + this.mCapturedImageFilePath);
        if (z) {
            Log.m29d(TAG, "** mTags");
            if (this.mTags != null) {
                this.mTags.dump();
            } else {
                Log.m29d(TAG, "mTags is null");
            }
            Log.m29d(TAG, "** Element tree **");
            if (this.mRootElement != null) {
                this.mRootElement.dump();
            }
        }
        Log.m29d(TAG, "----- End of SmartClip repository informations -----");
        return true;
    }

    public String encodeRepositoryToString() {
        try {
            JSONObject jSONObject = new JSONObject();
            if (getContentType() != null) {
                jSONObject.put(FIELD_CONTENT_TYPE, getContentType());
            }
            if (getRepositoryId() != null) {
                jSONObject.put(FIELD_REPOSITORY_ID, getRepositoryId());
            }
            Rect contentRect = getContentRect();
            if (contentRect != null) {
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(0, contentRect.left);
                jSONArray.put(1, contentRect.top);
                jSONArray.put(2, contentRect.right);
                jSONArray.put(3, contentRect.bottom);
                jSONObject.put(FIELD_CONTENT_RECT, jSONArray);
            }
            String capturedImageFilePath = getCapturedImageFilePath();
            int capturedImageFileStyle = getCapturedImageFileStyle();
            if (capturedImageFilePath != null) {
                jSONObject.put(FIELD_CAPTURED_IMAGE_PATH, capturedImageFilePath);
                jSONObject.put(FIELD_CAPTURED_IMAGE_STYLE, capturedImageFileStyle);
            }
            Iterable<SemSmartClipMetaTag> allMetaTags = getAllMetaTags();
            if (allMetaTags != null) {
                JSONArray jSONArray2 = new JSONArray();
                for (SemSmartClipMetaTag semSmartClipMetaTag : allMetaTags) {
                    if (semSmartClipMetaTag != null) {
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put(FIELD_META_TAG_TYPE, semSmartClipMetaTag.getType());
                        jSONObject2.put(FIELD_META_TAG_VALUE, semSmartClipMetaTag.getValue());
                        if (semSmartClipMetaTag instanceof SemSmartClipExtendedMetaTag) {
                            SemSmartClipMetaTag semSmartClipMetaTag2 = semSmartClipMetaTag;
                            if (semSmartClipMetaTag2.getExtraData() != null) {
                                byte[] extraData = semSmartClipMetaTag2.getExtraData();
                                Log.m29d(TAG, "Encoding : Length of extra data = " + extraData.length);
                                jSONObject2.put(FIELD_META_TAG_EXTRA_DATA, Base64.encodeToString(extraData, 0));
                            }
                        }
                        jSONArray2.put(jSONObject2);
                    }
                }
                if (jSONArray2.length() > 0) {
                    jSONObject.put(FIELD_META_TAGS, jSONArray2);
                }
            }
            return jSONObject.toString(1);
        } catch (Throwable e) {
            Log.m31e(TAG, "JSONException throwed : " + e.toString());
            e.printStackTrace();
            return MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        }
    }

    public SemSmartClipMetaTagArray extractMetaTagFromString(Context context, String str) {
        return null;
    }

    public SemSmartClipMetaTagArray getAllMetaTags() {
        if (this.mTags != null) {
            return this.mTags;
        }
        ArrayList smartClipMetaTagArrayImpl = new SmartClipMetaTagArrayImpl();
        for (SmartClipDataElementImpl smartClipDataElementImpl = this.mRootElement; smartClipDataElementImpl != null; smartClipDataElementImpl = smartClipDataElementImpl.traverseNextElement(null)) {
            SmartClipMetaTagArrayImpl smartClipMetaTagArrayImpl2 = (SmartClipMetaTagArrayImpl) smartClipDataElementImpl.getTagTable();
            if (smartClipMetaTagArrayImpl2 != null) {
                int size = smartClipMetaTagArrayImpl2.size();
                for (int i = 0; i < size; i++) {
                    SemSmartClipMetaTag semSmartClipMetaTag = (SemSmartClipMetaTag) smartClipMetaTagArrayImpl2.get(i);
                    if (!semSmartClipMetaTag.getType().equals(SemSmartClipMetaTagType.PLAIN_TEXT)) {
                        smartClipMetaTagArrayImpl.add(semSmartClipMetaTag);
                    }
                }
            }
        }
        String mergedPlainTextTag = getMergedPlainTextTag();
        if (mergedPlainTextTag != null) {
            smartClipMetaTagArrayImpl.add(new SemSmartClipExtendedMetaTag(SemSmartClipMetaTagType.PLAIN_TEXT, mergedPlainTextTag));
        }
        return smartClipMetaTagArrayImpl;
    }

    public String getAppPackageName() {
        return this.mAppPackageName;
    }

    public String getCapturedImageFilePath() {
        return this.mCapturedImageFilePath;
    }

    public int getCapturedImageFileStyle() {
        return this.mCapturedImageFileStyle;
    }

    public Rect getContentRect() {
        if (this.mContentRect != null) {
            return this.mContentRect;
        }
        SmartClipDataElementImpl smartClipDataElementImpl = this.mRootElement;
        Rect rect = new Rect(99999, 99999, 0, 0);
        while (smartClipDataElementImpl != null) {
            if (smartClipDataElementImpl.getChildCount() != 1) {
                Rect metaAreaRect;
                if (smartClipDataElementImpl.getChildCount() > 1) {
                    for (SmartClipDataElementImpl firstChild = smartClipDataElementImpl.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
                        metaAreaRect = firstChild.getMetaAreaRect();
                        if (metaAreaRect != null) {
                            if (rect.left > metaAreaRect.left && metaAreaRect.width() > 0) {
                                rect.left = metaAreaRect.left;
                            }
                            if (rect.top > metaAreaRect.top && metaAreaRect.height() > 0) {
                                rect.top = metaAreaRect.top;
                            }
                            if (rect.right < metaAreaRect.right && metaAreaRect.width() > 0) {
                                rect.right = metaAreaRect.right;
                            }
                            if (rect.bottom < metaAreaRect.bottom && metaAreaRect.height() > 0) {
                                rect.bottom = metaAreaRect.bottom;
                            }
                        }
                    }
                } else {
                    metaAreaRect = smartClipDataElementImpl.getMetaAreaRect();
                    if (metaAreaRect != null) {
                        if (rect.left > metaAreaRect.left && metaAreaRect.width() > 0) {
                            rect.left = metaAreaRect.left;
                        }
                        if (rect.top > metaAreaRect.top && metaAreaRect.height() > 0) {
                            rect.top = metaAreaRect.top;
                        }
                        if (rect.right < metaAreaRect.right && metaAreaRect.width() > 0) {
                            rect.right = metaAreaRect.right;
                        }
                        if (rect.bottom < metaAreaRect.bottom && metaAreaRect.height() > 0) {
                            rect.bottom = metaAreaRect.bottom;
                        }
                    }
                }
            }
            smartClipDataElementImpl = smartClipDataElementImpl.traverseNextElement(this.mRootElement);
        }
        if (rect.left > rect.right) {
            return new Rect();
        }
        if (!(this.mScaleRect.width() == 1.0f && this.mScaleRect.height() == 1.0f)) {
            float width = this.mScaleRect.width();
            float height = this.mScaleRect.height();
            if (!(width == 0.0f || height == 0.0f)) {
                Rect rect2 = new Rect();
                rect2.left = this.mWinFrameRect.left;
                rect2.top = this.mWinFrameRect.top;
                rect2.right = (int) ((((float) this.mWinFrameRect.left) + (((float) this.mWinFrameRect.width()) / width)) + 0.5f);
                rect2.bottom = (int) ((((float) this.mWinFrameRect.top) + (((float) this.mWinFrameRect.height()) / height)) + 0.5f);
                if (this.mPenWindowBorder > 0) {
                    if (rect.left < this.mPenWindowBorder) {
                        rect.left += this.mPenWindowBorder;
                    }
                    if (rect.right > rect2.width() - this.mPenWindowBorder) {
                        rect.right -= this.mPenWindowBorder;
                    }
                    if (rect.top < this.mPenWindowBorder) {
                        rect.top += this.mPenWindowBorder;
                    }
                    if (rect.bottom > rect2.height() - this.mPenWindowBorder) {
                        rect.bottom -= this.mPenWindowBorder;
                    }
                }
                int width2 = rect.width();
                int height2 = rect.height();
                rect.left = rect2.left + ((int) (((float) rect.left) * width));
                rect.top = rect2.top + ((int) (((float) rect.top) * height));
                rect.right = rect.left + ((int) (((float) width2) * width));
                rect.bottom = rect.top + ((int) (((float) height2) * height));
            }
        }
        return rect;
    }

    public String getContentType() {
        return this.mContentType;
    }

    public String getMergedPlainTextTag() {
        return this.mRootElement == null ? null : this.mRootElement.collectPlainTextTag();
    }

    public SemSmartClipMetaTagArray getMetaTag(String str) {
        ArrayList smartClipMetaTagArrayImpl = new SmartClipMetaTagArrayImpl();
        int size;
        int i;
        if (this.mTags != null) {
            size = this.mTags.size();
            for (i = 0; i < size; i++) {
                if (((SemSmartClipMetaTag) this.mTags.get(i)).getType().equals(str)) {
                    smartClipMetaTagArrayImpl.add((SemSmartClipMetaTag) this.mTags.get(i));
                }
            }
        } else if (SemSmartClipMetaTagType.PLAIN_TEXT.equals(str)) {
            String mergedPlainTextTag = getMergedPlainTextTag();
            if (mergedPlainTextTag != null) {
                smartClipMetaTagArrayImpl.add(new SemSmartClipExtendedMetaTag(SemSmartClipMetaTagType.PLAIN_TEXT, mergedPlainTextTag));
            }
        } else {
            for (SmartClipDataElementImpl smartClipDataElementImpl = this.mRootElement; smartClipDataElementImpl != null; smartClipDataElementImpl = smartClipDataElementImpl.traverseNextElement(null)) {
                SmartClipMetaTagArrayImpl smartClipMetaTagArrayImpl2 = (SmartClipMetaTagArrayImpl) smartClipDataElementImpl.getTagTable();
                if (smartClipMetaTagArrayImpl2 != null) {
                    size = smartClipMetaTagArrayImpl2.size();
                    for (i = 0; i < size; i++) {
                        SemSmartClipMetaTag semSmartClipMetaTag = (SemSmartClipMetaTag) smartClipMetaTagArrayImpl2.get(i);
                        if (semSmartClipMetaTag.getValue() != null && semSmartClipMetaTag.getType().equals(str)) {
                            smartClipMetaTagArrayImpl.add(semSmartClipMetaTag);
                        }
                    }
                }
            }
        }
        return smartClipMetaTagArrayImpl;
    }

    public String getRepositoryId() {
        return this.mRepositoryId;
    }

    public SemSmartClipDataElement getRootElement() {
        return this.mRootElement;
    }

    public SemSmartClipDataCropper getSmartClipDataCropper() {
        return this.mCropper;
    }

    public int getWindowLayer() {
        return this.mTargetWindowLayer;
    }

    public void readFromParcel(Parcel parcel) {
        this.mContentType = parcel.readString();
        this.mRepositoryId = parcel.readString();
        this.mContentRect = (Rect) parcel.readParcelable(Rect.class.getClassLoader());
        this.mCapturedImageFilePath = parcel.readString();
        this.mCapturedImageFileStyle = parcel.readInt();
        this.mAppPackageName = parcel.readString();
        this.mTargetWindowLayer = parcel.readInt();
        SmartClipMetaTagArrayImpl smartClipMetaTagArrayImpl = new SmartClipMetaTagArrayImpl();
        this.mTags = (SmartClipMetaTagArrayImpl) parcel.readParcelable(SemSmartClipExtendedMetaTag.class.getClassLoader());
    }

    public void setAppPackageName(String str) {
        this.mAppPackageName = str;
    }

    public void setCapturedImage(String str, int i) {
        this.mCapturedImageFilePath = str;
        this.mCapturedImageFileStyle = i;
    }

    public void setCapturedImageFilePath(String str) {
        setCapturedImage(str, 1);
    }

    public void setContentType(String str) {
        this.mContentType = str;
    }

    public void setRepositoryId(String str) {
        this.mRepositoryId = str;
    }

    public void setWindowLayer(int i) {
        this.mTargetWindowLayer = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.mContentType == null) {
            determineContentType();
        }
        parcel.writeString(this.mContentType);
        parcel.writeString(this.mRepositoryId);
        this.mContentRect = getContentRect();
        parcel.writeParcelable(this.mContentRect, i);
        parcel.writeString(this.mCapturedImageFilePath);
        parcel.writeInt(this.mCapturedImageFileStyle);
        parcel.writeString(this.mAppPackageName);
        parcel.writeInt(this.mTargetWindowLayer);
        this.mTags = (SmartClipMetaTagArrayImpl) getAllMetaTags();
        parcel.writeParcelable(this.mTags, i);
    }
}
