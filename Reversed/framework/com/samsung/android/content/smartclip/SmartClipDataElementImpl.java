package com.samsung.android.content.smartclip;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;

public class SmartClipDataElementImpl implements SemSmartClipDataElement {
    protected static final String TAG = "SmartClipDataElementImpl";
    protected SmartClipDataElementImpl mFirstChild;
    protected int mId;
    protected SmartClipDataElementImpl mLastChild;
    protected SmartClipDataElementImpl mNextSibling;
    protected SmartClipDataElementImpl mParent;
    protected SmartClipDataElementImpl mPrevSibling;
    protected Rect mRectOnScreen;
    protected SemSmartClipDataRepository mRepository;
    public SmartClipMetaTagArrayImpl mTags;
    protected View mView;

    public SmartClipDataElementImpl() {
        this.mId = -1;
        this.mRectOnScreen = null;
        this.mView = null;
        this.mRepository = null;
        this.mTags = null;
        this.mParent = null;
        this.mFirstChild = null;
        this.mLastChild = null;
        this.mNextSibling = null;
        this.mPrevSibling = null;
    }

    public SmartClipDataElementImpl(SemSmartClipDataRepository semSmartClipDataRepository) {
        this.mId = -1;
        this.mRectOnScreen = null;
        this.mView = null;
        this.mRepository = null;
        this.mTags = null;
        this.mParent = null;
        this.mFirstChild = null;
        this.mLastChild = null;
        this.mNextSibling = null;
        this.mPrevSibling = null;
        this.mRepository = semSmartClipDataRepository;
    }

    public SmartClipDataElementImpl(SemSmartClipDataRepository semSmartClipDataRepository, Rect rect) {
        this(semSmartClipDataRepository);
        this.mRectOnScreen = new Rect(rect);
    }

    public SmartClipDataElementImpl(SemSmartClipDataRepository semSmartClipDataRepository, View view) {
        this(semSmartClipDataRepository);
        this.mView = view;
    }

    public SmartClipDataElementImpl(SemSmartClipDataRepository semSmartClipDataRepository, View view, Rect rect) {
        this(semSmartClipDataRepository, view);
        this.mRectOnScreen = new Rect(rect);
    }

    private void setNextSibling(SmartClipDataElementImpl smartClipDataElementImpl) {
        this.mNextSibling = smartClipDataElementImpl;
    }

    private void setParent(SmartClipDataElementImpl smartClipDataElementImpl) {
        this.mParent = smartClipDataElementImpl;
    }

    private void setPrevSibling(SmartClipDataElementImpl smartClipDataElementImpl) {
        this.mPrevSibling = smartClipDataElementImpl;
    }

    public boolean addChild(SemSmartClipDataElement semSmartClipDataElement) {
        if (semSmartClipDataElement == null) {
            return false;
        }
        SmartClipDataElementImpl smartClipDataElementImpl = (SmartClipDataElementImpl) semSmartClipDataElement;
        if (this.mFirstChild == null) {
            this.mFirstChild = smartClipDataElementImpl;
            this.mLastChild = smartClipDataElementImpl;
            smartClipDataElementImpl.setNextSibling(null);
            smartClipDataElementImpl.setPrevSibling(null);
            smartClipDataElementImpl.setParent(this);
            return true;
        } else if (this.mLastChild == null) {
            return false;
        } else {
            SmartClipDataElementImpl smartClipDataElementImpl2 = this.mLastChild;
            this.mLastChild = smartClipDataElementImpl;
            smartClipDataElementImpl2.setNextSibling(smartClipDataElementImpl);
            smartClipDataElementImpl.setPrevSibling(smartClipDataElementImpl2);
            smartClipDataElementImpl.setParent(this);
            return true;
        }
    }

    public void addTag(SmartClipMetaTagArrayImpl smartClipMetaTagArrayImpl) {
        if (this.mTags == null) {
            this.mTags = new SmartClipMetaTagArrayImpl();
        }
        this.mTags.addAll(smartClipMetaTagArrayImpl);
    }

    public boolean addTag(SemSmartClipMetaTag semSmartClipMetaTag) {
        if (semSmartClipMetaTag == null) {
            return false;
        }
        if (this.mTags == null) {
            this.mTags = new SmartClipMetaTagArrayImpl();
        }
        if (!SmartClipUtils.isValidMetaTag(semSmartClipMetaTag)) {
            return false;
        }
        this.mTags.add(semSmartClipMetaTag);
        return true;
    }

    public void clearMetaData() {
        this.mRectOnScreen = null;
        setTagTable(null);
    }

    public SemSmartClipDataElement createChildInstance() {
        SemSmartClipDataElement newInstance = newInstance();
        addChild(newInstance);
        return newInstance;
    }

    public boolean dump() {
        Log.m31e(TAG, getDumpString(true, true));
        for (SmartClipDataElementImpl firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            firstChild.dump();
        }
        return true;
    }

    public SemSmartClipMetaTagArray getAllTags() {
        return this.mTags == null ? new SmartClipMetaTagArrayImpl() : this.mTags.getCopy();
    }

    public int getChildCount() {
        int i = 0;
        for (SmartClipDataElementImpl smartClipDataElementImpl = this.mFirstChild; smartClipDataElementImpl != null; smartClipDataElementImpl = smartClipDataElementImpl.getNextSibling()) {
            i++;
        }
        return i;
    }

    public SemSmartClipDataRepository getDataRepository() {
        return this.mRepository;
    }

    public String getDumpString(boolean z, boolean z2) {
        int i;
        String str = new String();
        int parentCount = getParentCount();
        if (z) {
            for (i = 0; i < parentCount; i++) {
                str = str + "\t";
            }
        }
        str = this.mRectOnScreen != null ? str + "Rect(" + this.mRectOnScreen.left + ", " + this.mRectOnScreen.top + ", " + this.mRectOnScreen.right + ", " + this.mRectOnScreen.bottom + ")\t" : str + "mRectOnScreen(NULL)\t";
        if (this.mView != null) {
            str = str + this.mView.getClass().getSimpleName();
            int id = this.mView.getId();
            if (id == -1 || id < 0) {
                str = str + "@" + Integer.toHexString(this.mView.hashCode()) + "\t";
            } else {
                try {
                    str = str + "/" + this.mView.getResources().getResourceEntryName(id) + "\t";
                } catch (Exception e) {
                    str = str + "@" + Integer.toHexString(this.mView.hashCode()) + "\t";
                }
            }
            Drawable background = this.mView.getBackground();
            if (!(background == null || !background.isVisible() || background.getOpacity() == -2)) {
                str = str + "Opacity BG(" + background.getOpacity() + ")\t";
            }
        }
        if (this.mTags == null) {
            return str + "No meta tag\t";
        }
        int size = this.mTags.size();
        for (i = 0; i < size; i++) {
            SemSmartClipMetaTag semSmartClipMetaTag = (SemSmartClipMetaTag) this.mTags.get(i);
            String type = semSmartClipMetaTag.getType();
            String value = semSmartClipMetaTag.getValue();
            String str2 = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
            if (value == null) {
                value = new String("null");
            }
            if (semSmartClipMetaTag instanceof SemSmartClipExtendedMetaTag) {
                SemSmartClipMetaTag semSmartClipMetaTag2 = semSmartClipMetaTag;
                if (semSmartClipMetaTag2.getExtraData() != null) {
                    str2 = ", Extra data size = " + semSmartClipMetaTag2.getExtraData().length;
                }
                if (semSmartClipMetaTag2.getParcelableData() != null) {
                    str2 = (str2 + ", Extra parcelable = ") + semSmartClipMetaTag2.getParcelableData().toString();
                }
            }
            str = z2 ? str + type + "(" + value + str2 + ")\t" : str + type + "\t";
        }
        return str;
    }

    public int getExtractionLevel() {
        if (this.mRepository == null) {
            return 0;
        }
        SmartClipDataCropperImpl smartClipDataCropperImpl = (SmartClipDataCropperImpl) this.mRepository.getSmartClipDataCropper();
        return smartClipDataCropperImpl == null ? 0 : smartClipDataCropperImpl.getExtractionLevel();
    }

    public int getExtractionMode() {
        if (this.mRepository == null) {
            return 0;
        }
        SmartClipDataCropperImpl smartClipDataCropperImpl = (SmartClipDataCropperImpl) this.mRepository.getSmartClipDataCropper();
        return smartClipDataCropperImpl == null ? 0 : smartClipDataCropperImpl.getExtractionMode();
    }

    public SmartClipDataElementImpl getFirstChild() {
        return this.mFirstChild;
    }

    public SmartClipDataElementImpl getLastChild() {
        return this.mLastChild;
    }

    public Rect getMetaAreaRect() {
        return this.mRectOnScreen;
    }

    public SmartClipDataElementImpl getNextSibling() {
        return this.mNextSibling;
    }

    public SmartClipDataElementImpl getParent() {
        return this.mParent;
    }

    public int getParentCount() {
        int i = 0;
        for (SmartClipDataElementImpl parent = getParent(); parent != null; parent = parent.getParent()) {
            i++;
        }
        return i;
    }

    public SmartClipDataElementImpl getPrevSibling() {
        return this.mPrevSibling;
    }

    public SemSmartClipMetaTagArray getTagTable() {
        return this.mTags;
    }

    public SemSmartClipMetaTagArray getTags(String str) {
        return this.mTags == null ? new SmartClipMetaTagArrayImpl() : this.mTags.getMetaTags(str);
    }

    public View getView() {
        return this.mView;
    }

    public boolean isEmptyTag(boolean z) {
        if (!z) {
            return this.mTags == null || this.mTags.size() <= 0;
        } else {
            SmartClipDataElementImpl smartClipDataElementImpl = this;
            while (smartClipDataElementImpl != null) {
                if (smartClipDataElementImpl.mTags != null && smartClipDataElementImpl.mTags.size() > 0) {
                    return false;
                }
                smartClipDataElementImpl = smartClipDataElementImpl.traverseNextElement(this);
            }
            return true;
        }
    }

    public SemSmartClipDataElement newInstance() {
        return new SmartClipDataElementImpl(this.mRepository);
    }

    public boolean removeChild(SemSmartClipDataElement semSmartClipDataElement) {
        if (semSmartClipDataElement == null) {
            return false;
        }
        SmartClipDataElementImpl smartClipDataElementImpl = (SmartClipDataElementImpl) semSmartClipDataElement;
        if (smartClipDataElementImpl.getParent() != this) {
            Log.m31e(TAG, "removeChild : Incorrect parent of SemSmartClipDataElement. element=" + smartClipDataElementImpl);
            smartClipDataElementImpl.dump();
            return false;
        }
        if (this.mFirstChild == smartClipDataElementImpl) {
            this.mFirstChild = smartClipDataElementImpl.getNextSibling();
        }
        if (this.mLastChild == smartClipDataElementImpl) {
            this.mLastChild = smartClipDataElementImpl.getPrevSibling();
        }
        if (smartClipDataElementImpl.getPrevSibling() != null) {
            smartClipDataElementImpl.getPrevSibling().setNextSibling(smartClipDataElementImpl.getNextSibling());
        }
        if (smartClipDataElementImpl.getNextSibling() != null) {
            smartClipDataElementImpl.getNextSibling().setPrevSibling(smartClipDataElementImpl.getPrevSibling());
        }
        return true;
    }

    public int removeTags(String str) {
        return this.mTags == null ? 0 : this.mTags.removeMetaTags(str);
    }

    public boolean sendSuspendedExtractionData() {
        SmartClipDataCropperImpl smartClipDataCropperImpl = null;
        SemSmartClipDataRepository dataRepository = getDataRepository();
        if (dataRepository != null) {
            smartClipDataCropperImpl = (SmartClipDataCropperImpl) dataRepository.getSmartClipDataCropper();
        }
        return smartClipDataCropperImpl != null ? smartClipDataCropperImpl.setPendingExtractionResult(this) : false;
    }

    public void setDataRepository(SemSmartClipDataRepository semSmartClipDataRepository) {
        this.mRepository = semSmartClipDataRepository;
    }

    public void setMetaAreaRect(Rect rect) {
        this.mRectOnScreen = rect;
    }

    public boolean setTag(SemSmartClipMetaTag semSmartClipMetaTag) {
        if (semSmartClipMetaTag == null) {
            return false;
        }
        if (this.mTags == null) {
            this.mTags = new SmartClipMetaTagArrayImpl();
        }
        if (semSmartClipMetaTag.getType() == null) {
            return false;
        }
        this.mTags.removeMetaTags(semSmartClipMetaTag.getType());
        this.mTags.add(semSmartClipMetaTag);
        return true;
    }

    public void setTagTable(SmartClipMetaTagArrayImpl smartClipMetaTagArrayImpl) {
        this.mTags = smartClipMetaTagArrayImpl;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public SmartClipDataElementImpl traverseNextElement(SmartClipDataElementImpl smartClipDataElementImpl) {
        if (this.mFirstChild != null) {
            return this.mFirstChild;
        }
        if (this == smartClipDataElementImpl) {
            return null;
        }
        if (this.mNextSibling != null) {
            return this.mNextSibling;
        }
        SmartClipDataElementImpl smartClipDataElementImpl2 = this;
        while (smartClipDataElementImpl2 != null && smartClipDataElementImpl2.mNextSibling == null && (smartClipDataElementImpl == null || smartClipDataElementImpl2.mParent != smartClipDataElementImpl)) {
            smartClipDataElementImpl2 = smartClipDataElementImpl2.mParent;
        }
        return smartClipDataElementImpl2 != null ? smartClipDataElementImpl2.mNextSibling : null;
    }
}
