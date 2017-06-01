package com.samsung.android.content.smartclip;

import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class SmartClipDataCropperImpl extends SemSmartClipDataCropper {
    private static boolean DEBUG = false;
    public static final int EXTRACTION_LEVEL_0 = 0;
    public static final int EXTRACTION_LEVEL_1 = 1;
    private static final int EXTRACTION_RESULT_MAIN_MASKING = 255;
    private static final int MAX_META_VALUE_SIZE = 102400;
    private static final String META_NAME_SUPPORT_THIRD_PARTY_EXTRACTION_INTERFACE = "com.samsung.android.smartclip.support_custom_smartclip_metaextraction";
    private static final String TAG = "SmartClipDataCropperImpl";
    private static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";
    private static final String YOUTUBE_URL_PREFIX = "http://www.youtube.com/watch?v=";
    private String mChromeBrowserContentViewName;
    protected Context mContext;
    protected int mExtractionLevel;
    protected SmartClipDataExtractionEvent mExtractionRequest;
    private long mExtractionStartTime;
    protected boolean mIsExtractingData;
    private int mLastMetaFileId;
    protected String mPackageName;
    private int mPenWindowBorderWidth;
    protected ArrayList<SmartClipDataElementImpl> mPendingElements;
    private RectF mScaleRect;
    protected SemSmartClipDataRepository mSmartClipDataRepository;
    private boolean mSupportThirdPartyExtractionInterface;
    private boolean mUseViewPositionCache;
    private HashMap<View, Point> mViewPositionCache;
    private Rect mWinFrameRect;

    public SmartClipDataCropperImpl(Context context, SmartClipDataExtractionEvent smartClipDataExtractionEvent) {
        this(context, smartClipDataExtractionEvent, new Rect(0, 0, 0, 0), new RectF(0.0f, 0.0f, 1.0f, 1.0f), 0);
    }

    public SmartClipDataCropperImpl(Context context, SmartClipDataExtractionEvent smartClipDataExtractionEvent, Rect rect, RectF rectF, int i) {
        this.mWinFrameRect = null;
        this.mScaleRect = null;
        this.mPenWindowBorderWidth = 0;
        this.mSmartClipDataRepository = null;
        this.mPendingElements = new ArrayList();
        this.mExtractionRequest = null;
        this.mIsExtractingData = false;
        this.mExtractionLevel = 0;
        this.mPackageName = null;
        this.mChromeBrowserContentViewName = null;
        this.mSupportThirdPartyExtractionInterface = false;
        this.mExtractionStartTime = 0;
        this.mLastMetaFileId = 0;
        this.mUseViewPositionCache = false;
        this.mViewPositionCache = new HashMap();
        this.mContext = context;
        this.mExtractionRequest = smartClipDataExtractionEvent;
        this.mWinFrameRect = new Rect(rect);
        this.mScaleRect = new RectF(rectF);
        this.mPenWindowBorderWidth = i;
        this.mPackageName = context.getPackageName();
        if (this.mPackageName == null) {
            this.mPackageName = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        }
        this.mChromeBrowserContentViewName = SmartClipUtils.getChromeViewClassNameFromManifest(context, this.mPackageName);
        this.mSupportThirdPartyExtractionInterface = isThirdPartyExtractionInterfaceEnabledOnManifest(context, this.mPackageName);
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            this.mExtractionLevel = 0;
            this.mExtractionLevel = 1;
            if (packageManager.hasSystemFeature("com.samsung.android.smartclip.DEBUG")) {
                DEBUG = true;
            }
        }
    }

    private Rect adjustMetaAreaRect(View view, Rect rect) {
        Rect viewBoundsOnScreen = getViewBoundsOnScreen(view);
        Rect rect2 = new Rect();
        if (rect == null) {
            Log.m31e(TAG, "adjustMetaAreaRect : rect is null");
            return null;
        }
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof ViewGroup) {
                Rect viewBoundsOnScreen2 = getViewBoundsOnScreen(parent);
                Rect rect3 = new Rect();
                if (rect3.setIntersect(viewBoundsOnScreen, viewBoundsOnScreen2)) {
                    viewBoundsOnScreen = rect3;
                }
            }
        }
        if (rect2.setIntersect(rect, viewBoundsOnScreen)) {
            return rect2;
        }
        Log.m31e(TAG, "adjustMetaAreaRect : there is no intersection " + rect + " and " + viewBoundsOnScreen);
        return null;
    }

    private String allocateMetaTagFilePath() {
        File file = new File(this.mContext.getFilesDir().getAbsolutePath() + "/smartclip");
        if (!file.exists()) {
            file.mkdir();
            file.setWritable(true, false);
            file.setReadable(true, false);
            file.setExecutable(true, false);
        }
        this.mLastMetaFileId++;
        this.mLastMetaFileId %= 3;
        return String.format("%s/SC%02d", new Object[]{r1, Integer.valueOf(this.mLastMetaFileId)});
    }

    private int extractDefaultSmartClipData_GoogleChromeView(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SmartClipDataElementImpl smartClipDataElementImpl) {
        try {
            Method method = view.getClass().getMethod("extractSmartClipData", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
            final Method method2 = view.getClass().getMethod("setSmartClipResultHandler", new Class[]{Handler.class});
            if (!(method == null || method2 == null)) {
                Log.m29d(TAG, "Extracting meta data from Chrome view...");
                final SmartClipDataElementImpl smartClipDataElementImpl2 = smartClipDataElementImpl;
                final View view2 = view;
                C10261 c10261 = new Handler() {
                    public SemSmartClipDataElement mResult = smartClipDataElementImpl2;

                    public void handleMessage(Message message) {
                        Log.m29d(SmartClipDataCropperImpl.TAG, "Meta data arrived from chrome");
                        BaseBundle data = message.getData();
                        if (data == null) {
                            Log.m31e(SmartClipDataCropperImpl.TAG, "The bundle is null!");
                            SmartClipDataCropperImpl.this.setPendingExtractionResult(this.mResult);
                            return;
                        }
                        Object string = data.getString("title");
                        Object string2 = data.getString("url");
                        Object string3 = data.getString(SemSmartClipMetaTagType.HTML);
                        Object string4 = data.getString(StreamItemsColumns.TEXT);
                        Rect rect = (Rect) data.getParcelable("rect");
                        if (SmartClipDataCropperImpl.DEBUG) {
                            Log.m29d(SmartClipDataCropperImpl.TAG, String.format("Title:%s\nURL:%s\nArea:%s\nText:%s\nHTML:%s", new Object[]{string, string2, rect, string4, string3}));
                        }
                        if (!TextUtils.isEmpty(string)) {
                            smartClipDataElementImpl2.setTag(new SemSmartClipMetaTag("title", string));
                        }
                        if (!TextUtils.isEmpty(string2)) {
                            smartClipDataElementImpl2.setTag(new SemSmartClipMetaTag("url", string2));
                        }
                        if (!TextUtils.isEmpty(string3)) {
                            smartClipDataElementImpl2.setTag(new SemSmartClipMetaTag(SemSmartClipMetaTagType.HTML, string3));
                        }
                        if (!TextUtils.isEmpty(string4)) {
                            smartClipDataElementImpl2.setTag(new SemSmartClipMetaTag(SemSmartClipMetaTagType.PLAIN_TEXT, string4));
                        }
                        if (rect != null) {
                            DisplayMetrics displayMetrics = SmartClipDataCropperImpl.this.mContext.getResources().getDisplayMetrics();
                            rect.left = (int) TypedValue.applyDimension(1, (float) rect.left, displayMetrics);
                            rect.top = (int) TypedValue.applyDimension(1, (float) rect.top, displayMetrics);
                            rect.right = (int) TypedValue.applyDimension(1, (float) rect.right, displayMetrics);
                            rect.bottom = (int) TypedValue.applyDimension(1, (float) rect.bottom, displayMetrics);
                            Rect -wrap0 = SmartClipDataCropperImpl.this.getViewBoundsOnScreen(view2);
                            rect.offset(-wrap0.left, -wrap0.top);
                            rect.intersect(-wrap0);
                            smartClipDataElementImpl2.setMetaAreaRect(rect);
                        }
                        try {
                            method2.invoke(view2, new Object[]{null});
                        } catch (Throwable e) {
                            Log.m31e(SmartClipDataCropperImpl.TAG, "Could not invoke set smartclip handler API");
                            e.printStackTrace();
                        }
                        SmartClipDataCropperImpl.this.setPendingExtractionResult(this.mResult);
                    }
                };
                Rect rect = new Rect(semSmartClipCroppedArea.getRect());
                int[] iArr = new int[2];
                view.getLocationOnScreen(iArr);
                rect.offset(-iArr[0], -iArr[1]);
                method2.invoke(view, new Object[]{c10261});
                if (DEBUG) {
                    Log.m29d(TAG, "Converting coordinate : " + semSmartClipCroppedArea.getRect().toString() + " -> " + rect.toString());
                }
                method.invoke(view, new Object[]{Integer.valueOf(rect.left), Integer.valueOf(rect.top), Integer.valueOf(rect.width()), Integer.valueOf(rect.height())});
                return 2;
            }
        } catch (Exception e) {
            Log.m31e(TAG, "Current chrome view does not support smartclip");
        }
        try {
            String str = "url";
            SmartClipDataElementImpl smartClipDataElementImpl3 = smartClipDataElementImpl;
            smartClipDataElementImpl3.setTag(new SemSmartClipMetaTag(str, (String) view.getClass().getMethod("getUrl", new Class[0]).invoke(view, new Object[0])));
            str = "title";
            smartClipDataElementImpl3 = smartClipDataElementImpl;
            smartClipDataElementImpl3.setTag(new SemSmartClipMetaTag(str, (String) view.getClass().getMethod("getTitle", new Class[0]).invoke(view, new Object[0])));
            if (this.mExtractionRequest != null) {
                if (this.mExtractionRequest.mExtractionMode == 0) {
                    smartClipDataElementImpl.setMetaAreaRect(semSmartClipCroppedArea.getRect());
                } else if (this.mExtractionRequest.mExtractionMode == 2) {
                }
            }
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
        return 1;
    }

    private int extractDefaultSmartClipData_ImageView(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SmartClipDataElementImpl smartClipDataElementImpl) {
        if (smartClipDataElementImpl.getTags(SemSmartClipMetaTagType.PLAIN_TEXT).size() == 0) {
            ImageView imageView = (ImageView) view;
            if (!(imageView.getDrawable() == null && imageView.getBackground() == null)) {
                smartClipDataElementImpl.addTag(new SemSmartClipMetaTag(SemSmartClipMetaTagType.PLAIN_TEXT, MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET));
            }
        }
        return 1;
    }

    private int extractDefaultSmartClipData_TextView(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SmartClipDataElementImpl smartClipDataElementImpl) {
        if (smartClipDataElementImpl.getTags(SemSmartClipMetaTagType.PLAIN_TEXT).size() == 0) {
            TextView textView = (TextView) view;
            TransformationMethod transformationMethod = textView.getTransformationMethod();
            if (transformationMethod == null || !(transformationMethod instanceof PasswordTransformationMethod)) {
                CharSequence text = textView.getText();
                if (text == null) {
                    text = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
                }
                if (this.mExtractionRequest != null && this.mExtractionRequest.mExtractionMode == 2) {
                    Rect spannedTextRect = textView.getSpannedTextRect(semSmartClipCroppedArea.getRect());
                    if (spannedTextRect != null) {
                        smartClipDataElementImpl.setMetaAreaRect(spannedTextRect);
                        text = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
                    }
                    if (textView.hasSelection()) {
                        int selectionStart = textView.getSelectionStart();
                        int selectionEnd = textView.getSelectionEnd();
                        CharSequence subSequence = text.subSequence(Math.max(0, Math.min(selectionStart, selectionEnd)), Math.max(0, Math.max(selectionStart, selectionEnd)));
                        if (subSequence != null) {
                            smartClipDataElementImpl.addTag(new SemSmartClipMetaTag(SemSmartClipMetaTagType.TEXT_SELECTION, subSequence.toString()));
                        }
                    }
                }
                smartClipDataElementImpl.addTag(new SemSmartClipMetaTag(SemSmartClipMetaTagType.PLAIN_TEXT, text.toString()));
            }
        }
        return 1;
    }

    private int extractDefaultSmartClipData_TextureView(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SmartClipDataElementImpl smartClipDataElementImpl) {
        if (smartClipDataElementImpl.getTags(SemSmartClipMetaTagType.PLAIN_TEXT).size() == 0) {
            smartClipDataElementImpl.addTag(new SemSmartClipMetaTag(SemSmartClipMetaTagType.PLAIN_TEXT, MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET));
        }
        return 1;
    }

    private int extractDefaultSmartClipData_ThirdPartyInterface(final View view, SemSmartClipCroppedArea semSmartClipCroppedArea, final SmartClipDataElementImpl smartClipDataElementImpl) {
        Object obj = view;
        try {
            Method thirPartyExtractionInterfaceMethod = getThirPartyExtractionInterfaceMethod(view);
            if (thirPartyExtractionInterfaceMethod == null) {
                obj = view.getTag();
                if (obj != null) {
                    thirPartyExtractionInterfaceMethod = getThirPartyExtractionInterfaceMethod(obj);
                }
            }
            if (!(obj == null || thirPartyExtractionInterfaceMethod == null)) {
                Log.m29d(TAG, "Extracting meta data using third party interface...");
                C10272 c10272 = new Handler() {
                    public SemSmartClipDataElement mResult = smartClipDataElementImpl;

                    public void handleMessage(Message message) {
                        Log.m29d(SmartClipDataCropperImpl.TAG, "Pending meta data arrived from third party");
                        Bundle data = message.getData();
                        if (data == null) {
                            Log.m31e(SmartClipDataCropperImpl.TAG, "The bundle is null!");
                            SmartClipDataCropperImpl.this.setPendingExtractionResult(this.mResult);
                            return;
                        }
                        SmartClipDataCropperImpl.this.updateDataElementWithBundle(view, data, smartClipDataElementImpl);
                        SmartClipDataCropperImpl.this.setPendingExtractionResult(this.mResult);
                    }
                };
                Bundle invoke = thirPartyExtractionInterfaceMethod.invoke(obj, new Object[]{semSmartClipCroppedArea.getRect(), c10272});
                if (invoke == null || !(invoke instanceof Bundle)) {
                    Log.m29d(TAG, "Null returned immediately from third party. waiting pending meta data..");
                    return 2;
                }
                Log.m29d(TAG, "Bundle data returned immediately from third party");
                updateDataElementWithBundle(view, invoke, smartClipDataElementImpl);
                return 1;
            }
        } catch (Throwable e) {
            Log.m31e(TAG, "Exception is thrown during execute the third party smartclip interface. e=" + e);
            e.printStackTrace();
        }
        return 1;
    }

    private int extractDefaultSmartClipData_YoutubePlayerView(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SmartClipDataElementImpl smartClipDataElementImpl) {
        return 1;
    }

    private void filterMetaTagForBrowserViews(SmartClipDataElementImpl smartClipDataElementImpl) {
        if (smartClipDataElementImpl == null) {
            Log.m31e(TAG, "filterMetaTagForBrowserViews : element is null!");
            return;
        }
        SmartClipDataElementImpl smartClipDataElementImpl2 = smartClipDataElementImpl;
        while (smartClipDataElementImpl2 != null) {
            Iterable<SemSmartClipMetaTag> tagTable = smartClipDataElementImpl2.getTagTable();
            if (tagTable != null) {
                View view = smartClipDataElementImpl2.getView();
                String simpleName = view != null ? view.getClass().getSimpleName() : "null";
                int size = tagTable.getMetaTags(SemSmartClipMetaTagType.HTML).size();
                int size2 = tagTable.getMetaTags(SemSmartClipMetaTagType.PLAIN_TEXT).size();
                if (size > 0 && size2 > 0) {
                    switch (this.mExtractionLevel) {
                        case 0:
                            tagTable.removeMetaTags(SemSmartClipMetaTagType.HTML);
                            Log.m29d(TAG, "filterMetaTagForBrowserViews : Discarding HTML tag from " + simpleName);
                            break;
                        default:
                            for (SemSmartClipMetaTag semSmartClipMetaTag : tagTable) {
                                if (SemSmartClipMetaTagType.PLAIN_TEXT.equals(semSmartClipMetaTag.getType())) {
                                    semSmartClipMetaTag.setType(SemSmartClipMetaTagType.HTML_TEXT);
                                }
                            }
                            Log.m29d(TAG, "filterMetaTagForBrowserViews : The TEXT tag changed to HTML_TEXT. View=" + simpleName);
                            break;
                    }
                }
                for (SemSmartClipMetaTag semSmartClipMetaTag2 : tagTable) {
                    if (SemSmartClipMetaTagType.HTML.equals(semSmartClipMetaTag2.getType())) {
                        String value = semSmartClipMetaTag2.getValue();
                        if (value.length() > MAX_META_VALUE_SIZE) {
                            Log.m31e(TAG, "filterMetaTagForBrowserViews : Have large HTML data(" + value.length() + " bytes). Converting tag..");
                            String allocateMetaTagFilePath = allocateMetaTagFilePath();
                            if (writeStringToFile(allocateMetaTagFilePath, value)) {
                                Log.m29d(TAG, "filterMetaTagForBrowserViews : Saved the meta tag to " + allocateMetaTagFilePath);
                            } else {
                                Log.m31e(TAG, "filterMetaTagForBrowserViews : Failed to save meta tag! - " + allocateMetaTagFilePath);
                            }
                            semSmartClipMetaTag2.setType(SemSmartClipMetaTagType.FILE_PATH_HTML);
                            semSmartClipMetaTag2.setValue(allocateMetaTagFilePath);
                        }
                    }
                }
            }
            smartClipDataElementImpl2 = smartClipDataElementImpl2.traverseNextElement(smartClipDataElementImpl);
        }
    }

    private ArrayList<View> getChildViewsByZOrder(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        Object<View> arrayList = new ArrayList(childCount);
        Object obj = null;
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            float z = childAt.getZ();
            if (z != 0.0f) {
                obj = 1;
            }
            int i2 = i;
            while (i2 > 0 && ((View) arrayList.get(i2 - 1)).getZ() > z) {
                i2--;
            }
            arrayList.add(i2, childAt);
        }
        if (obj != null) {
            Log.m29d(TAG, "getChildViewsByZOrder : Z order detected");
            for (View view : arrayList) {
                Log.m29d(TAG, "getChildViewsByZOrder : Parent=" + viewGroup + " / View=" + view + " / Z=" + view.getZ());
            }
        }
        return arrayList;
    }

    private int getMainResultFromExtractionResult(int i) {
        return i & 255;
    }

    private Rect getOpaqueBackgroundRect(SmartClipDataElementImpl smartClipDataElementImpl) {
        SmartClipDataElementImpl smartClipDataElementImpl2 = smartClipDataElementImpl;
        Rect rect = null;
        while (smartClipDataElementImpl2 != null) {
            View view = smartClipDataElementImpl2.getView();
            if (view != null) {
                Drawable background = view.getBackground();
                if (!(background == null || !background.isVisible() || background.getOpacity() == -2)) {
                    Rect metaAreaRect = smartClipDataElementImpl2.getMetaAreaRect();
                    if (metaAreaRect != null) {
                        Rect adjustMetaAreaRect = adjustMetaAreaRect(view, metaAreaRect);
                        if (adjustMetaAreaRect != null) {
                            if (rect == null) {
                                rect = new Rect(adjustMetaAreaRect);
                            } else {
                                rect.union(adjustMetaAreaRect);
                            }
                        }
                    }
                }
            }
            smartClipDataElementImpl2 = smartClipDataElementImpl2.traverseNextElement(smartClipDataElementImpl);
        }
        Log.m29d(TAG, "getOpaqueBackgroundRect : opaqueRect=" + rect + "  element=" + smartClipDataElementImpl);
        return rect;
    }

    private Method getThirPartyExtractionInterfaceMethod(Object obj) {
        if (obj == null) {
            return null;
        }
        Method method = null;
        try {
            method = obj.getClass().getMethod("extractSmartClipData", new Class[]{Rect.class, Handler.class});
        } catch (NoSuchMethodException e) {
        }
        return method;
    }

    private Rect getViewBoundsOnScreen(View view) {
        Rect rect = new Rect();
        Point viewLocationOnScreen = getViewLocationOnScreen(view);
        rect.left = viewLocationOnScreen.x;
        rect.top = viewLocationOnScreen.y;
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
        return rect;
    }

    private Point getViewLocationOnScreen(View view) {
        Point point = null;
        if (this.mUseViewPositionCache) {
            point = (Point) this.mViewPositionCache.get(view);
        }
        if (point == null) {
            point = SmartClipUtils.getViewLocationOnScreen(view);
            if (this.mUseViewPositionCache) {
                this.mViewPositionCache.put(view, point);
            }
        }
        return point;
    }

    private boolean isSupportThirdPartyExtractionInterface(View view) {
        boolean z = true;
        if (view == null) {
            return false;
        }
        if (getThirPartyExtractionInterfaceMethod(view) != null) {
            return true;
        }
        Object tag = view.getTag();
        if (tag == null) {
            return false;
        }
        if (getThirPartyExtractionInterfaceMethod(tag) == null) {
            z = false;
        }
        return z;
    }

    private boolean isThirdPartyExtractionInterfaceEnabledOnManifest(Context context, String str) {
        boolean z = false;
        try {
            PackageItemInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
            if (applicationInfo == null) {
                Log.m31e(TAG, "isSupportThirdPartyExtractionInterface : Could not get appInfo! - " + str);
                return false;
            }
            BaseBundle baseBundle = applicationInfo.metaData;
            if (baseBundle != null) {
                z = baseBundle.getBoolean(META_NAME_SUPPORT_THIRD_PARTY_EXTRACTION_INTERFACE, false);
                if (z) {
                    Log.m29d(TAG, "isSupportThirdPartyExtractionInterface : Feature enabled");
                }
            }
            return z;
        } catch (NameNotFoundException e) {
        }
    }

    private boolean removeSmartClipDataElementByRect(SmartClipDataElementImpl smartClipDataElementImpl, Rect rect) {
        SmartClipDataElementImpl lastChild = smartClipDataElementImpl.getLastChild();
        while (lastChild != null) {
            SmartClipDataElementImpl smartClipDataElementImpl2 = lastChild;
            lastChild = lastChild.getPrevSibling();
            removeSmartClipDataElementByRect(smartClipDataElementImpl2, rect);
        }
        if (smartClipDataElementImpl.getFirstChild() == null) {
            Rect metaAreaRect = smartClipDataElementImpl.getMetaAreaRect();
            if (smartClipDataElementImpl.isEmptyTag(false)) {
                smartClipDataElementImpl.getParent().removeChild(smartClipDataElementImpl);
                return true;
            } else if (metaAreaRect != null && Rect.intersects(rect, metaAreaRect)) {
                Log.m29d(TAG, "removeSmartClipDataElementByRect : Removing element due to RECT intersection. element = " + smartClipDataElementImpl.getDumpString(false, true));
                smartClipDataElementImpl.getParent().removeChild(smartClipDataElementImpl);
                return true;
            }
        }
        return false;
    }

    private boolean traverseView(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SemSmartClipDataRepository semSmartClipDataRepository, SmartClipDataElementImpl smartClipDataElementImpl) {
        boolean z = false;
        if (view != null && view.getVisibility() == 0 && view.getWidth() > 0 && view.getHeight() > 0) {
            Rect viewBoundsOnScreen = getViewBoundsOnScreen(view);
            if (Rect.intersects(semSmartClipCroppedArea.getRect(), viewBoundsOnScreen)) {
                SemSmartClipDataElement smartClipDataElementImpl2 = new SmartClipDataElementImpl(semSmartClipDataRepository, view, viewBoundsOnScreen);
                SmartClipMetaTagArrayImpl smartClipMetaTagArrayImpl = (SmartClipMetaTagArrayImpl) view.semGetSmartClipTags();
                if (smartClipMetaTagArrayImpl != null) {
                    smartClipDataElementImpl2.setTagTable(smartClipMetaTagArrayImpl.getCopy());
                }
                SemSmartClipDataExtractionListener semGetSmartClipDataExtractionListener = view.semGetSmartClipDataExtractionListener();
                int extractDefaultSmartClipData_ThirdPartyInterface = (this.mSupportThirdPartyExtractionInterface && isSupportThirdPartyExtractionInterface(view)) ? extractDefaultSmartClipData_ThirdPartyInterface(view, semSmartClipCroppedArea, smartClipDataElementImpl2) : semGetSmartClipDataExtractionListener != null ? semGetSmartClipDataExtractionListener.onExtractSmartClipData(view, semSmartClipCroppedArea, smartClipDataElementImpl2) : view.semExtractSmartClipData(semSmartClipCroppedArea, smartClipDataElementImpl2);
                for (SmartClipDataElementImpl smartClipDataElementImpl3 = smartClipDataElementImpl2; smartClipDataElementImpl3 != null; smartClipDataElementImpl3 = smartClipDataElementImpl3.traverseNextElement(smartClipDataElementImpl2)) {
                    smartClipDataElementImpl3.setMetaAreaRect(adjustMetaAreaRect(view, smartClipDataElementImpl3.getMetaAreaRect()));
                }
                int mainResultFromExtractionResult = getMainResultFromExtractionResult(extractDefaultSmartClipData_ThirdPartyInterface);
                switch (mainResultFromExtractionResult) {
                    case 0:
                        smartClipDataElementImpl2.clearMetaData();
                        break;
                    case 1:
                        break;
                    case 2:
                        this.mPendingElements.add(smartClipDataElementImpl2);
                        z = true;
                        break;
                    default:
                        Log.m31e(TAG, "Unknown main extraction result value : " + mainResultFromExtractionResult + " / View = " + view.toString());
                        smartClipDataElementImpl2.clearMetaData();
                        break;
                }
                Object obj = (extractDefaultSmartClipData_ThirdPartyInterface & 256) != 0 ? 1 : null;
                if ((view instanceof ViewGroup) && obj == null) {
                    ArrayList childViewsByZOrder = getChildViewsByZOrder((ViewGroup) view);
                    int size = childViewsByZOrder.size();
                    for (int i = 0; i < size; i++) {
                        if (traverseView((View) childViewsByZOrder.get(i), semSmartClipCroppedArea, semSmartClipDataRepository, smartClipDataElementImpl2)) {
                            z = true;
                        }
                    }
                }
                if (!smartClipDataElementImpl2.isEmptyTag(true)) {
                    z = true;
                }
                if (!smartClipDataElementImpl2.isEmptyTag(false)) {
                    if (DEBUG) {
                        Log.m29d(TAG, "traverseView : Contains meta data : " + smartClipDataElementImpl2.getDumpString(false, true));
                    } else {
                        Log.m29d(TAG, "traverseView : Contains meta data : " + smartClipDataElementImpl2.getDumpString(false, false));
                    }
                }
                if (z) {
                    if ((view instanceof FrameLayout) || (view instanceof RelativeLayout)) {
                        SmartClipDataElementImpl lastChild = smartClipDataElementImpl2.getLastChild();
                        Rect rect = null;
                        while (lastChild != null) {
                            boolean z2 = false;
                            SmartClipDataElementImpl smartClipDataElementImpl4 = lastChild;
                            lastChild = lastChild.getPrevSibling();
                            if (rect != null) {
                                z2 = removeSmartClipDataElementByRect(smartClipDataElementImpl4, rect);
                            }
                            if (!z2) {
                                Rect opaqueBackgroundRect = getOpaqueBackgroundRect(smartClipDataElementImpl4);
                                if (opaqueBackgroundRect != null) {
                                    if (rect == null) {
                                        rect = opaqueBackgroundRect;
                                    } else {
                                        rect.union(opaqueBackgroundRect);
                                    }
                                }
                            }
                        }
                    }
                    smartClipDataElementImpl.addChild(smartClipDataElementImpl2);
                }
            }
        }
        return z;
    }

    private boolean traverseViewForDragAndDrop(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SemSmartClipDataRepository semSmartClipDataRepository, SmartClipDataElementImpl smartClipDataElementImpl) {
        boolean z = false;
        if (view != null && view.getVisibility() == 0 && view.getWidth() > 0 && view.getHeight() > 0) {
            Rect viewBoundsOnScreen = getViewBoundsOnScreen(view);
            if (Rect.intersects(semSmartClipCroppedArea.getRect(), viewBoundsOnScreen)) {
                SemSmartClipDataElement smartClipDataElementImpl2 = new SmartClipDataElementImpl(semSmartClipDataRepository, view, viewBoundsOnScreen);
                Object obj = null;
                SemSmartClipDataExtractionListener semGetSmartClipDataExtractionListener = view.semGetSmartClipDataExtractionListener();
                if (semGetSmartClipDataExtractionListener != null && (view instanceof SurfaceView)) {
                    obj = 1;
                }
                int onExtractSmartClipData = obj != null ? semGetSmartClipDataExtractionListener.onExtractSmartClipData(view, semSmartClipCroppedArea, smartClipDataElementImpl2) : view.semExtractSmartClipData(semSmartClipCroppedArea, smartClipDataElementImpl2);
                for (SmartClipDataElementImpl smartClipDataElementImpl3 = smartClipDataElementImpl2; smartClipDataElementImpl3 != null; smartClipDataElementImpl3 = smartClipDataElementImpl3.traverseNextElement(smartClipDataElementImpl2)) {
                    smartClipDataElementImpl3.setMetaAreaRect(adjustMetaAreaRect(view, smartClipDataElementImpl3.getMetaAreaRect()));
                }
                int mainResultFromExtractionResult = getMainResultFromExtractionResult(onExtractSmartClipData);
                switch (mainResultFromExtractionResult) {
                    case 0:
                        smartClipDataElementImpl2.clearMetaData();
                        break;
                    case 1:
                        break;
                    case 2:
                        this.mPendingElements.add(smartClipDataElementImpl2);
                        z = true;
                        break;
                    default:
                        Log.m31e(TAG, "Unknown main extraction result value : " + mainResultFromExtractionResult + " / View = " + view.toString());
                        smartClipDataElementImpl2.clearMetaData();
                        break;
                }
                Object obj2 = (onExtractSmartClipData & 256) != 0 ? 1 : null;
                if ((view instanceof ViewGroup) && obj2 == null) {
                    ArrayList childViewsByZOrder = getChildViewsByZOrder((ViewGroup) view);
                    int size = childViewsByZOrder.size() - 1;
                    while (size >= 0) {
                        if (traverseViewForDragAndDrop((View) childViewsByZOrder.get(size), semSmartClipCroppedArea, semSmartClipDataRepository, smartClipDataElementImpl2)) {
                            z = true;
                        } else {
                            size--;
                        }
                    }
                }
                if (!smartClipDataElementImpl2.isEmptyTag(true)) {
                    z = true;
                }
                if (z) {
                    smartClipDataElementImpl.addChild(smartClipDataElementImpl2);
                }
            }
        }
        return z;
    }

    private boolean updateDataElementWithBundle(View view, Bundle bundle, SmartClipDataElementImpl smartClipDataElementImpl) {
        boolean z = false;
        Object string = bundle.getString("title");
        Object string2 = bundle.getString("url");
        Object string3 = bundle.getString("app_link");
        Rect rect = (Rect) bundle.getParcelable("rect");
        if (DEBUG) {
            Log.m29d(TAG, String.format("fillDataElementWithBundle : Title:%s\nLink:%s\nURL:%s\nArea:%s", new Object[]{string, string3, string2, rect}));
        }
        if (!TextUtils.isEmpty(string)) {
            smartClipDataElementImpl.setTag(new SemSmartClipMetaTag("title", string));
            z = true;
        }
        if (!TextUtils.isEmpty(string2)) {
            smartClipDataElementImpl.setTag(new SemSmartClipMetaTag("url", string2));
            z = true;
        }
        if (!TextUtils.isEmpty(string3)) {
            smartClipDataElementImpl.setTag(new SemSmartClipMetaTag(SemSmartClipMetaTagType.APP_DEEP_LINK, string3));
            z = true;
        }
        if (rect == null) {
            return z;
        }
        rect.intersect(getViewBoundsOnScreen(view));
        smartClipDataElementImpl.setMetaAreaRect(rect);
        return true;
    }

    private boolean writeStringToFile(String str, String str2) {
        Object e;
        Throwable th;
        boolean z = true;
        Log.m29d(TAG, "writeStringToFile : " + str);
        File file = new File(str);
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            try {
                fileOutputStream2.write(str2.getBytes());
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (Exception e2) {
                        Log.m31e(TAG, "writeStringToFile : File close failed! " + e2);
                        z = false;
                    }
                }
                fileOutputStream = fileOutputStream2;
            } catch (Exception e3) {
                e = e3;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.m31e(TAG, "writeStringToFile : File write failed! " + e);
                    z = false;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e22) {
                            Log.m31e(TAG, "writeStringToFile : File close failed! " + e22);
                            z = false;
                        }
                    }
                    file.setReadable(true, false);
                    file.setWritable(true, false);
                    return z;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e222) {
                            Log.m31e(TAG, "writeStringToFile : File close failed! " + e222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            Log.m31e(TAG, "writeStringToFile : File write failed! " + e);
            z = false;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            file.setReadable(true, false);
            file.setWritable(true, false);
            return z;
        }
        file.setReadable(true, false);
        file.setWritable(true, false);
        return z;
    }

    protected void addAppMetaTag(SemSmartClipDataElement semSmartClipDataElement) {
        if (this.mContext == null) {
            Log.m31e(TAG, "addAppMetaTag : mContext is null!");
            return;
        }
        Log.m29d(TAG, "addAppMetaTag : package name is " + this.mPackageName);
        semSmartClipDataElement.addTag(new SemSmartClipExtendedMetaTag(SemSmartClipMetaTagType.APP_LAUNCH_INFO, this.mPackageName));
    }

    public boolean doExtractSmartClipData(View view) {
        if (this.mExtractionRequest == null) {
            Log.m31e(TAG, "doExtractSmartClipData : extractionRequest is null!");
            return false;
        }
        this.mExtractionStartTime = System.currentTimeMillis();
        SemSmartClipCroppedArea smartClipCroppedAreaImpl = new SmartClipCroppedAreaImpl(this.mExtractionRequest.mCropRect);
        Rect rect = smartClipCroppedAreaImpl.getRect();
        Log.m29d(TAG, "doExtractSmartClipData : Extraction start! reqId = " + this.mExtractionRequest.mRequestId + "  Cropped area = " + (rect == null ? "null" : rect.toString()) + "  Package = " + this.mPackageName);
        this.mIsExtractingData = true;
        this.mSmartClipDataRepository = new SemSmartClipDataRepository(this, this.mWinFrameRect, this.mScaleRect, this.mPenWindowBorderWidth);
        SmartClipDataElementImpl smartClipDataElementImpl = (SmartClipDataElementImpl) this.mSmartClipDataRepository.getRootElement();
        this.mViewPositionCache.clear();
        if (this.mExtractionRequest.mExtractionMode == 2 || this.mExtractionRequest.mExtractionMode == 3) {
            traverseViewForDragAndDrop(view, smartClipCroppedAreaImpl, this.mSmartClipDataRepository, smartClipDataElementImpl);
        } else {
            traverseView(view, smartClipCroppedAreaImpl, this.mSmartClipDataRepository, smartClipDataElementImpl);
        }
        this.mViewPositionCache.clear();
        addAppMetaTag(smartClipDataElementImpl);
        this.mSmartClipDataRepository.setAppPackageName(this.mPackageName);
        this.mIsExtractingData = false;
        if (this.mPendingElements.size() == 0) {
            this.mSmartClipDataRepository.determineContentType();
            sendExtractionResultToSmartClipService();
        }
        return true;
    }

    public int extractDefaultSmartClipData(View view, SemSmartClipCroppedArea semSmartClipCroppedArea, SmartClipDataElementImpl smartClipDataElementImpl) {
        if (smartClipDataElementImpl == null) {
            Log.m31e(TAG, "extractDefaultSmartClipData : The result element is null!");
            return 0;
        } else if (semSmartClipCroppedArea == null) {
            Log.m31e(TAG, "extractDefaultSmartClipData : The cropped area is null!");
            return 0;
        } else {
            try {
                String name = view.getClass().getName();
                if (this.mPackageName.equals(YOUTUBE_PACKAGE_NAME) && name.endsWith("PlayerView")) {
                    return extractDefaultSmartClipData_YoutubePlayerView(view, semSmartClipCroppedArea, smartClipDataElementImpl);
                }
                if (this.mChromeBrowserContentViewName != null && SmartClipUtils.isInstanceOf(view, this.mChromeBrowserContentViewName)) {
                    Log.m29d(TAG, "extractDefaultSmartClipData : Has chrome view");
                    return extractDefaultSmartClipData_GoogleChromeView(view, semSmartClipCroppedArea, smartClipDataElementImpl);
                } else if (name.equals("org.chromium.content.browser.JellyBeanContentView")) {
                    return extractDefaultSmartClipData_GoogleChromeView(view, semSmartClipCroppedArea, smartClipDataElementImpl);
                } else {
                    if (view instanceof TextView) {
                        return extractDefaultSmartClipData_TextView(view, semSmartClipCroppedArea, smartClipDataElementImpl);
                    }
                    if (view instanceof ImageView) {
                        return extractDefaultSmartClipData_ImageView(view, semSmartClipCroppedArea, smartClipDataElementImpl);
                    }
                    if (view instanceof TextureView) {
                        return extractDefaultSmartClipData_TextureView(view, semSmartClipCroppedArea, smartClipDataElementImpl);
                    }
                    return 1;
                }
            } catch (Throwable e) {
                Toast.makeText(view.getContext(), "ClassCastException in traverseView : target class is " + view.toString(), 1).show();
                e.printStackTrace();
            }
        }
    }

    protected int findElementIndexFromPendingList(SmartClipDataElementImpl smartClipDataElementImpl) {
        int size = this.mPendingElements.size();
        for (int i = 0; i < size; i++) {
            if (this.mPendingElements.get(i) == smartClipDataElementImpl) {
                return i;
            }
        }
        return -1;
    }

    public int getExtractionLevel() {
        return this.mExtractionLevel;
    }

    public int getExtractionMode() {
        return this.mExtractionRequest != null ? this.mExtractionRequest.mExtractionMode : 0;
    }

    protected ArrayList<View> getParentList(View view) {
        ViewParent viewParent;
        ArrayList<View> arrayList = new ArrayList();
        if (view instanceof ViewGroup) {
            viewParent = view;
        } else {
            arrayList.add(view);
            viewParent = view.getParent();
        }
        while (viewParent != null) {
            if (viewParent instanceof ViewGroup) {
                arrayList.add(viewParent);
            }
            viewParent = viewParent.getParent();
        }
        return arrayList;
    }

    public SemSmartClipDataRepository getSmartClipDataRepository() {
        return this.mSmartClipDataRepository;
    }

    protected boolean sendExtractionResultToSmartClipService() {
        if (this.mPendingElements.size() > 0) {
            Log.m31e(TAG, "Cannot send the extraction result due to it still have pending element!");
            return false;
        } else if (this.mSmartClipDataRepository != null) {
            return sendExtractionResultToSmartClipService(this.mSmartClipDataRepository);
        } else {
            Log.m31e(TAG, "Cannot send the extraction result due to it is NULL!");
            return false;
        }
    }

    public boolean sendExtractionResultToSmartClipService(SemSmartClipDataRepository semSmartClipDataRepository) {
        if (this.mExtractionRequest == null) {
            Log.m31e(TAG, "sendExtractionResultToSmartClipService : extractionRequest is null!");
            return false;
        }
        if (semSmartClipDataRepository != null && this.mExtractionRequest.mExtractionMode == 0) {
            filterMetaTagForBrowserViews((SmartClipDataElementImpl) semSmartClipDataRepository.getRootElement());
        }
        if (semSmartClipDataRepository != null) {
            Log.m29d(TAG, "sendExtractionResultToSmartClipService : -- Extracted SmartClip data information --");
            Log.m29d(TAG, "sendExtractionResultToSmartClipService : Request Id : " + this.mExtractionRequest.mRequestId);
            Log.m29d(TAG, "sendExtractionResultToSmartClipService : Extraction mode : " + this.mExtractionRequest.mExtractionMode);
            semSmartClipDataRepository.dump(DEBUG);
        } else {
            Log.m31e(TAG, "sendExtractionResultToSmartClipService : The repository is null");
        }
        SpenGestureManager spenGestureManager = (SpenGestureManager) this.mContext.getSystemService("spengestureservice");
        Parcelable smartClipDataExtractionResponse = new SmartClipDataExtractionResponse(this.mExtractionRequest.mRequestId, this.mExtractionRequest.mExtractionMode, semSmartClipDataRepository);
        if (semSmartClipDataRepository != null && this.mExtractionRequest.mTargetWindowLayer >= 0) {
            semSmartClipDataRepository.setWindowLayer(this.mExtractionRequest.mTargetWindowLayer);
        }
        try {
            spenGestureManager.sendSmartClipRemoteRequestResult(new SmartClipRemoteRequestResult(this.mExtractionRequest.mRequestId, 1, smartClipDataExtractionResponse));
        } catch (RuntimeException e) {
            Log.m31e(TAG, "sendExtractionResultToSmartClipService : Failed to send the result! e=" + e);
            Log.m31e(TAG, "sendExtractionResultToSmartClipService : Send empty response...");
            spenGestureManager.sendSmartClipRemoteRequestResult(new SmartClipRemoteRequestResult(this.mExtractionRequest.mRequestId, 1, null));
        }
        Log.m29d(TAG, "sendExtractionResultToSmartClipService : Elapsed = " + (System.currentTimeMillis() - this.mExtractionStartTime));
        return true;
    }

    public boolean setPendingExtractionResult(SemSmartClipDataElement semSmartClipDataElement) {
        int findElementIndexFromPendingList = findElementIndexFromPendingList((SmartClipDataElementImpl) semSmartClipDataElement);
        if (findElementIndexFromPendingList < 0) {
            return false;
        }
        this.mPendingElements.remove(findElementIndexFromPendingList);
        SmartClipDataElementImpl smartClipDataElementImpl = (SmartClipDataElementImpl) semSmartClipDataElement;
        if (!smartClipDataElementImpl.isEmptyTag(false)) {
            if (DEBUG) {
                Log.m29d(TAG, "setPendingExtractionResult : Contains meta data : " + smartClipDataElementImpl.getDumpString(false, true));
            } else {
                Log.m29d(TAG, "setPendingExtractionResult : Contains meta data : " + smartClipDataElementImpl.getDumpString(false, false));
            }
        }
        if (this.mPendingElements.size() == 0 && !this.mIsExtractingData) {
            this.mSmartClipDataRepository.determineContentType();
            sendExtractionResultToSmartClipService();
        }
        return true;
    }
}
