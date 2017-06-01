package com.samsung.android.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import com.samsung.android.fingerprint.FingerprintManager;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public abstract class SemAbstractIndexer extends DataSetObserver {
    private static final char DIGIT_CHAR = '#';
    public static final char FAVORITE_CHAR = '☆';
    protected static final String INDEXSCROLL_INDEX_COUNTS = "indexscroll_index_counts";
    protected static final String INDEXSCROLL_INDEX_TITLES = "indexscroll_index_titles";
    private static final char SYMBOL_BASE_CHAR = '!';
    private static final char SYMBOL_CHAR = '&';
    private final String TAG;
    private final boolean debug;
    protected SparseIntArray mAlphaMap;
    protected CharSequence mAlphabet;
    protected String[] mAlphabetArray;
    protected int mAlphabetLength;
    private Bundle mBundle;
    private int[] mCachingValue;
    protected Collator mCollator;
    private int mCurrentLang;
    private int mCurrentLangEndIndex;
    private int mCurrentLangStartIndex;
    private int mCurrentLangStartPosition;
    private final DataSetObservable mDataSetObservable;
    private int mDigitItemCount;
    private int mFavoriteItemCount;
    protected String[] mLangAlphabetArray;
    private HashMap<Integer, Integer> mLangIndexMap;
    private int mProfileItemCount;
    private boolean mUseDigitIndex;
    private boolean mUseFavoriteIndex;

    public static class IndexInfo {
        boolean mExists;
        String mIndexString;
        int mLastPosition = -1;
        int mPosition;

        public String toString() {
            return "Exist=" + this.mExists + " Pos=" + this.mPosition + " str=" + this.mIndexString;
        }
    }

    static class PositionResult {
        boolean exactMatch;
        int position;

        PositionResult(int i) {
            this.position = i;
            this.exactMatch = false;
        }

        PositionResult(int i, boolean z) {
            this.position = i;
            this.exactMatch = z;
        }
    }

    public SemAbstractIndexer(CharSequence charSequence) {
        this.TAG = "SemAbstractIndexer";
        this.debug = false;
        this.mDataSetObservable = new DataSetObservable();
        this.mDigitItemCount = 0;
        this.mUseFavoriteIndex = false;
        this.mUseDigitIndex = false;
        this.mLangIndexMap = new HashMap();
        this.mCurrentLang = -1;
        this.mCurrentLangStartIndex = -1;
        this.mCurrentLangEndIndex = -1;
        this.mCurrentLangStartPosition = -1;
        this.mUseFavoriteIndex = false;
        this.mProfileItemCount = 0;
        this.mFavoriteItemCount = 0;
        initIndexer(charSequence);
    }

    public SemAbstractIndexer(CharSequence charSequence, int i, int i2) {
        this.TAG = "SemAbstractIndexer";
        this.debug = false;
        this.mDataSetObservable = new DataSetObservable();
        this.mDigitItemCount = 0;
        this.mUseFavoriteIndex = false;
        this.mUseDigitIndex = false;
        this.mLangIndexMap = new HashMap();
        this.mCurrentLang = -1;
        this.mCurrentLangStartIndex = -1;
        this.mCurrentLangEndIndex = -1;
        this.mCurrentLangStartPosition = -1;
        this.mUseFavoriteIndex = true;
        this.mProfileItemCount = i;
        this.mFavoriteItemCount = i2;
        initIndexer(charSequence);
    }

    public SemAbstractIndexer(String[] strArr, int i) {
        this.TAG = "SemAbstractIndexer";
        this.debug = false;
        this.mDataSetObservable = new DataSetObservable();
        this.mDigitItemCount = 0;
        this.mUseFavoriteIndex = false;
        this.mUseDigitIndex = false;
        this.mLangIndexMap = new HashMap();
        this.mCurrentLang = -1;
        this.mCurrentLangStartIndex = -1;
        this.mCurrentLangEndIndex = -1;
        this.mCurrentLangStartPosition = -1;
        this.mUseFavoriteIndex = false;
        this.mProfileItemCount = 0;
        this.mFavoriteItemCount = 0;
        this.mLangAlphabetArray = strArr;
        setMultiLangIndexer(i);
    }

    public SemAbstractIndexer(String[] strArr, int i, int i2, int i3) {
        this.TAG = "SemAbstractIndexer";
        this.debug = false;
        this.mDataSetObservable = new DataSetObservable();
        this.mDigitItemCount = 0;
        this.mUseFavoriteIndex = false;
        this.mUseDigitIndex = false;
        this.mLangIndexMap = new HashMap();
        this.mCurrentLang = -1;
        this.mCurrentLangStartIndex = -1;
        this.mCurrentLangEndIndex = -1;
        this.mCurrentLangStartPosition = -1;
        this.mUseFavoriteIndex = true;
        this.mProfileItemCount = i2;
        this.mFavoriteItemCount = i3;
        this.mLangAlphabetArray = strArr;
        setMultiLangIndexer(i);
    }

    private void getBundleInfo() {
        String[] stringArray = this.mBundle.getStringArray("indexscroll_index_titles");
        int[] intArray = this.mBundle.getIntArray("indexscroll_index_counts");
        int i = this.mProfileItemCount;
        int i2 = 0;
        for (int i3 = 0; i3 < this.mAlphabetLength; i3++) {
            char charAt = this.mAlphabet.charAt(i3);
            this.mCachingValue[i3] = i;
            Log.d("SemAbstractIndexer", "Get index info from bundle (" + i3 + ") : " + charAt + " = " + i);
            if (charAt == FAVORITE_CHAR) {
                i += this.mFavoriteItemCount;
            }
            for (int i4 = i2; i4 < stringArray.length; i4++) {
                if (charAt == stringArray[i4].charAt(0)) {
                    i += intArray[i4];
                    i2 = i4;
                    break;
                }
            }
            if (charAt == "#".charAt(0)) {
                this.mCachingValue[i3] = getItemCount() - this.mDigitItemCount;
            }
        }
    }

    private boolean isJapaneseIndex(char c) {
        return c > 'ぁ' && c < 'ヺ';
    }

    private boolean isKoreanLocaleLaguage() {
        return Locale.KOREA.getLanguage().equals(Locale.getDefault().getLanguage());
    }

    private boolean isTaiwanLocale() {
        return Locale.TAIWAN.equals(Locale.getDefault());
    }

    protected int compare(String str, String str2) {
        return this.mCollator.compare(str, str2);
    }

    String[] getAlphabetArray() {
        return this.mAlphabetArray;
    }

    protected abstract Bundle getBundle();

    public int getCachingValue(int i) {
        return (i < 0 || i >= this.mAlphabetLength) ? -1 : this.mCachingValue[i];
    }

    public int getCurrentLang() {
        return this.mCurrentLang;
    }

    public int getCurrentLangEndIndex() {
        return this.mCurrentLangEndIndex;
    }

    public int getCurrentLangStartIndex() {
        return this.mCurrentLangStartIndex;
    }

    public int getCurrentLangStartPosition() {
        return this.mCurrentLangStartPosition;
    }

    protected String getHangeulConsonant(String str) {
        int i;
        switch ((str.charAt(0) - 44032) / 588) {
            case 0:
                i = 12593;
                break;
            case 1:
                i = 12593;
                break;
            case 2:
                i = 12596;
                break;
            case 3:
                i = 12599;
                break;
            case 4:
                i = 12599;
                break;
            case 5:
                i = 12601;
                break;
            case 6:
                i = 12609;
                break;
            case 7:
                i = 12610;
                break;
            case 8:
                i = 12610;
                break;
            case 9:
                i = 12613;
                break;
            case 10:
                i = 12613;
                break;
            case 11:
                i = 12615;
                break;
            case 12:
                i = 12616;
                break;
            case 13:
                i = 12616;
                break;
            case 14:
                i = 12618;
                break;
            case 15:
                i = 12619;
                break;
            case 16:
                i = 12620;
                break;
            case 17:
                i = 12621;
                break;
            case 18:
                i = 12622;
                break;
            default:
                i = 12593;
                break;
        }
        return "" + ((char) i);
    }

    ArrayList<IndexInfo> getIndexInfo() {
        return getIndexInfo(null, false);
    }

    public ArrayList<IndexInfo> getIndexInfo(String str) {
        return getIndexInfo(str, false);
    }

    public ArrayList<IndexInfo> getIndexInfo(String str, boolean z) {
        if (!isDataToBeIndexedAvailable()) {
            return null;
        }
        if (getItemCount() == 0) {
            return null;
        }
        this.mBundle = getBundle();
        if (this.mBundle != null && this.mBundle.containsKey("indexscroll_index_titles") && this.mBundle.containsKey("indexscroll_index_counts")) {
            getBundleInfo();
            return null;
        }
        String str2 = str == null ? "" : str;
        ArrayList<IndexInfo> arrayList = new ArrayList();
        try {
            onBeginTransaction();
            Object obj = null;
            int i = -1;
            for (int i2 = 0; i2 < this.mAlphabetLength; i2++) {
                String str3 = str2 + this.mAlphabet.charAt(i2);
                PositionResult positionForString = getPositionForString(str3);
                int i3 = positionForString.position;
                boolean z2 = positionForString.exactMatch;
                boolean isJapaneseIndex = isJapaneseIndex(this.mAlphabet.charAt(i2));
                if (i3 < 0) {
                    i3 = -i3;
                }
                this.mCachingValue[i2] = i3;
                Log.d("SemAbstractIndexer", "Get index info (" + i2 + ") : " + str3 + " = " + i3);
                if (!z || z2) {
                    IndexInfo indexInfo = new IndexInfo();
                    indexInfo.mExists = z2;
                    indexInfo.mPosition = i3;
                    if (!(!isCurrentLanguagePosition(i2) || isTaiwanLocale() || isKoreanLocaleLaguage())) {
                        if (z2) {
                            obj = 1;
                            i = i3;
                        }
                        if (!(isJapaneseIndex || z2 || r3 == null)) {
                            indexInfo.mPosition = i;
                        }
                    }
                    int currentLang = getCurrentLang();
                    if (null != null && currentLang == 0) {
                        indexInfo.mPosition = -1;
                        indexInfo.mLastPosition = i3;
                    }
                    indexInfo.mIndexString = this.mAlphabetArray[i2];
                    if (this.mCurrentLangStartIndex == i2) {
                        this.mCurrentLangStartPosition = indexInfo.mPosition;
                    }
                    arrayList.add(indexInfo);
                }
            }
            onEndTransaction();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<IndexInfo> getIndexInfo(String str, boolean z, int i, int i2) {
        if (!isDataToBeIndexedAvailable()) {
            return null;
        }
        if (getItemCount() == 0) {
            return null;
        }
        if (i2 < 1) {
            return null;
        }
        String str2 = str == null ? "" : str;
        ArrayList<IndexInfo> arrayList = new ArrayList();
        try {
            onBeginTransaction();
            String str3 = this.mLangAlphabetArray[getLangbyIndex(i2)];
            for (int i3 = 0; i3 < str3.length(); i3++) {
                PositionResult positionForString = getPositionForString(str2 + str3.charAt(i3));
                int i4 = positionForString.position;
                boolean z2 = positionForString.exactMatch;
                if (i4 < 0) {
                    i4 = -i4;
                }
                if (!z || z2) {
                    IndexInfo indexInfo = new IndexInfo();
                    indexInfo.mExists = z2;
                    indexInfo.mPosition = i4;
                    indexInfo.mIndexString = "" + str3.charAt(i3);
                    arrayList.add(indexInfo);
                }
            }
            onEndTransaction();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    protected abstract String getItemAt(int i);

    protected abstract int getItemCount();

    public String[] getLangAlphabetArray() {
        return this.mLangAlphabetArray;
    }

    public int getLangbyIndex(int i) {
        if (i >= 0) {
            try {
                if (this.mLangIndexMap != null) {
                    Integer num = new Integer(i);
                    if (num != null) {
                        return ((Integer) this.mLangIndexMap.get(num)).intValue();
                    }
                }
            } catch (Exception e) {
            }
        }
        return -1;
    }

    PositionResult getPositionForString(String str) {
        SparseIntArray sparseIntArray = this.mAlphaMap;
        int itemCount = getItemCount();
        if (itemCount == 0 || this.mAlphabet == null) {
            return new PositionResult(itemCount);
        }
        if (str == null || str.length() == 0) {
            return new PositionResult(itemCount);
        }
        int i = 0;
        int i2 = itemCount;
        boolean z = false;
        String str2 = str;
        char charAt = str.charAt(0);
        int i3 = sparseIntArray.get(charAt, FingerprintManager.PRIVILEGED_TYPE_KEYGUARD);
        if (Integer.MIN_VALUE != i3) {
            i = Math.abs(i3);
        } else {
            int indexOf = this.mAlphabet.toString().indexOf(charAt);
            if (indexOf > 0 && charAt > this.mAlphabet.charAt(indexOf - 1)) {
                int i4 = sparseIntArray.get(this.mAlphabet.charAt(indexOf - 1), FingerprintManager.PRIVILEGED_TYPE_KEYGUARD);
                if (i4 != Integer.MIN_VALUE) {
                    i = Math.abs(i4);
                }
            }
            if (indexOf < this.mAlphabet.length() - 1 && charAt < this.mAlphabet.charAt(indexOf + 1)) {
                int i5 = sparseIntArray.get(this.mAlphabet.charAt(indexOf + 1), FingerprintManager.PRIVILEGED_TYPE_KEYGUARD);
                if (i5 != Integer.MIN_VALUE) {
                    i2 = Math.abs(i5);
                }
            }
        }
        char charAt2 = str.charAt(0);
        if (charAt2 == '&') {
            str2 = "!";
        }
        if (charAt2 == '☆') {
            if (i < this.mProfileItemCount) {
                i = this.mProfileItemCount;
            }
        } else if (i < this.mProfileItemCount + this.mFavoriteItemCount) {
            i = this.mProfileItemCount + this.mFavoriteItemCount;
        }
        i2 -= this.mDigitItemCount;
        if (charAt2 == '#') {
            i = i2;
        }
        i3 = (i2 + i) / 2;
        while (i3 >= i && i3 < r6) {
            String itemAt = getItemAt(i3);
            if (itemAt != null && itemAt != "") {
                int compare = compare(itemAt, str2);
                if (!(charAt2 == '☆' || charAt2 == '&')) {
                    if (charAt2 == '#') {
                    }
                    if (compare == 0) {
                        if (i != i3) {
                            break;
                        }
                        i2 = i3;
                    } else if (compare >= 0) {
                        if (null == null && i3 == 0) {
                            i3 = 1;
                            break;
                        }
                        i2 = i3;
                    } else {
                        i = i3 + 1;
                        if (null != null || i3 != 0) {
                            if (i >= itemCount) {
                                i3 = itemCount;
                                break;
                            }
                        }
                        break;
                    }
                    i3 = (i + i2) / 2;
                }
                compare = 1;
                if (compare == 0) {
                    if (compare >= 0) {
                        i = i3 + 1;
                        if (null != null) {
                        }
                        if (i >= itemCount) {
                            i3 = itemCount;
                            break;
                        }
                    }
                    if (null == null) {
                    }
                    i2 = i3;
                } else if (i != i3) {
                    break;
                } else {
                    i2 = i3;
                }
                i3 = (i + i2) / 2;
            } else if (i3 <= i) {
                break;
            } else {
                i3--;
            }
        }
        if (str.length() == 1) {
            sparseIntArray.put(charAt, i3);
        }
        if (i3 < itemCount) {
            itemAt = getItemAt(i3);
            if (!(itemAt == null || "".equals(itemAt))) {
                z = seeIfExactMatch(itemAt, str2);
            }
        }
        return new PositionResult(i3, z);
    }

    protected void initIndexer(CharSequence charSequence) {
        if (charSequence == null || charSequence.length() == 0) {
            throw new IllegalArgumentException("Invalid indexString :" + charSequence);
        }
        this.mAlphabet = charSequence;
        this.mAlphabetLength = charSequence.length();
        this.mCachingValue = new int[this.mAlphabetLength];
        this.mAlphabetArray = new String[this.mAlphabetLength];
        for (int i = 0; i < this.mAlphabetLength; i++) {
            this.mAlphabetArray[i] = Character.toString(this.mAlphabet.charAt(i));
        }
        this.mAlphaMap = new SparseIntArray(this.mAlphabetLength);
        this.mCollator = Collator.getInstance();
        this.mCollator.setStrength(0);
    }

    public boolean isCurrentLanguagePosition(int i) {
        boolean z = false;
        if (this.mCurrentLangStartIndex == -1 || this.mCurrentLangEndIndex == -1 || (i >= this.mCurrentLangStartIndex && i <= this.mCurrentLangEndIndex)) {
            return true;
        }
        if (this.mUseFavoriteIndex) {
            if (i == 0 || i == 1) {
                z = true;
            }
            return z;
        }
        if (i == 0) {
            z = true;
        }
        return z;
    }

    protected abstract boolean isDataToBeIndexedAvailable();

    public boolean isUseDigitIndex() {
        return this.mUseDigitIndex;
    }

    protected void onBeginTransaction() {
    }

    public void onChanged() {
        super.onChanged();
        this.mAlphaMap.clear();
        this.mDataSetObservable.notifyChanged();
    }

    protected void onEndTransaction() {
    }

    public void onInvalidated() {
        super.onInvalidated();
        this.mAlphaMap.clear();
        this.mDataSetObservable.notifyInvalidated();
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDataSetObservable.registerObserver(dataSetObserver);
    }

    protected boolean seeIfExactMatch(String str, String str2) {
        if (str.length() > str2.length()) {
            str = str.substring(0, str2.length());
        }
        if (str.charAt(0) >= '가' && str.charAt(0) <= '힣') {
            str = getHangeulConsonant(str);
        }
        return compare(str, str2) == 0;
    }

    public void setDigitItem(int i) {
        if (i >= 0) {
            this.mDigitItemCount = i;
            this.mUseDigitIndex = true;
            setMultiLangIndexer(this.mCurrentLang);
        }
    }

    public void setFavoriteItem(int i) {
        if (i >= 0) {
            this.mFavoriteItemCount = i;
            this.mUseFavoriteIndex = true;
            setMultiLangIndexer(this.mCurrentLang);
        }
    }

    public String setMultiLangIndexer(int i) {
        StringBuffer stringBuffer;
        this.mCurrentLang = i;
        Boolean valueOf = Boolean.valueOf(isTaiwanLocale());
        if (this.mUseFavoriteIndex) {
            stringBuffer = new StringBuffer("☆");
            stringBuffer.append("&");
        } else {
            stringBuffer = new StringBuffer("&");
        }
        int i2 = 0;
        while (i2 < this.mLangAlphabetArray.length) {
            this.mCurrentLangStartIndex = stringBuffer.length();
            for (int i3 = 0; i3 < this.mLangAlphabetArray[i2].length(); i3++) {
                this.mLangIndexMap.put(Integer.valueOf(stringBuffer.length()), Integer.valueOf(i2));
                stringBuffer.append(this.mLangAlphabetArray[i2].charAt(i3));
            }
            this.mCurrentLangEndIndex = stringBuffer.length() - 1;
            i2++;
        }
        if (this.mUseDigitIndex) {
            this.mLangIndexMap.put(Integer.valueOf(stringBuffer.length()), Integer.valueOf(i2 - 1));
            stringBuffer.append("#");
        }
        initIndexer(stringBuffer.toString());
        return stringBuffer.toString();
    }

    public void setProfileItem(int i) {
        if (i >= 0) {
            this.mProfileItemCount = i;
        }
    }

    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDataSetObservable.unregisterObserver(dataSetObserver);
    }
}
