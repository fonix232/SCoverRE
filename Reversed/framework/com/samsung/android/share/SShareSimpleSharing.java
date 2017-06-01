package com.samsung.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SemHorizontalListView;
import android.widget.TextView;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.graphics.spr.document.SprDocument;
import java.util.ArrayList;
import java.util.List;

public class SShareSimpleSharing {
    private static final boolean DEBUG = false;
    private static final String TAG = "SShareSimpleSharing";
    private static boolean mEasySignUpCertificated = false;
    private static boolean mSSharingRecentContactExisted = false;
    private final char[] ELLIPSIS_NORMAL = new char[]{'…'};
    private float defaultTextSize;
    private Activity mActivity;
    private Context mContext;
    private List<Intent> mExtraIntentList;
    private SShareCommon mFeature;
    private SemHorizontalListView mGridRecentHistory;
    private boolean mGroupNameOldConcept;
    final Handler mHandler = new C02441();
    private boolean mIsRecentContactsReceiverRegistered = false;
    private Intent mOrigIntent;
    private long[] mRecentContactsId;
    private int[] mRecentContactsItemContactsCountInGroup;
    private int mRecentContactsListCount = 0;
    private ArrayList<String> mRecentContactsListName = new ArrayList();
    private List<byte[]> mRecentContactsListThumb = new ArrayList();
    BroadcastReceiver mRecentContactsReceiver = new C02452();
    private int mRecentHistoryIndex = 0;
    Intent mRecentHistoryIntent;
    private RecentHistoryListAdapter mRecentHistoryListAdapter;
    private String[] mRecipientDataId;
    private boolean mRemoteShareServiceEnabled = true;
    private float mTunedMargin = 40.0f;

    class C02441 extends Handler {
        C02441() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 2000:
                    SShareSimpleSharing.this.initRecentHistoryList();
                    return;
                default:
                    return;
            }
        }
    }

    class C02452 extends BroadcastReceiver {
        C02452() {
        }

        public void onReceive(Context context, Intent intent) {
            if (SShareConstants.RESPONSE_RECENT_GROUP_CONTACTS.equals(intent.getAction())) {
                SShareSimpleSharing.this.mRecentContactsListCount = 0;
                if (SShareSimpleSharing.this.mRecentContactsListThumb.size() != 0) {
                    SShareSimpleSharing.this.mRecentContactsListThumb.clear();
                }
                if (SShareSimpleSharing.this.mRecentContactsListName.size() != 0) {
                    SShareSimpleSharing.this.mRecentContactsListName.clear();
                }
                int i = 0;
                while (i < 5 && intent.hasExtra(SShareConstants.EXTRA_KEY_RECENT_GROUP_DATAIDS + i)) {
                    ArrayList stringArrayListExtra = intent.getStringArrayListExtra(SShareConstants.EXTRA_KEY_RECENT_GROUP_NAME_LIST + i);
                    if (stringArrayListExtra != null) {
                        String str = "";
                        for (int i2 = 0; i2 < stringArrayListExtra.size(); i2++) {
                            str = str + ((String) stringArrayListExtra.get(i2));
                            if (i2 != stringArrayListExtra.size() - 1) {
                                str = str + FingerprintManager.FINGER_PERMISSION_DELIMITER;
                            }
                        }
                        SShareSimpleSharing.this.mRecentContactsListName.add(str);
                    } else {
                        SShareSimpleSharing.this.mGroupNameOldConcept = true;
                        SShareSimpleSharing.this.mRecentContactsListName.add(intent.getStringExtra(SShareConstants.EXTRA_KEY_RECENT_GROUP_NAMES + i));
                    }
                    SShareSimpleSharing.this.mRecipientDataId[i] = intent.getStringExtra(SShareConstants.EXTRA_KEY_RECENT_GROUP_DATAIDS + i);
                    SShareSimpleSharing.this.mRecentContactsId[i] = intent.getLongExtra(SShareConstants.EXTRA_KEY_RECENT_GROUP_CONTACTID + i, 0);
                    SShareSimpleSharing.this.mRecentContactsItemContactsCountInGroup[i] = intent.getIntExtra(SShareConstants.EXTRA_KEY_RECENT_GROUP_COUNT + i, 0);
                    SShareSimpleSharing.this.mRecentContactsListThumb.add(intent.getByteArrayExtra(SShareConstants.EXTRA_KEY_RECENT_GROUP_THUMBNAILS + i));
                    SShareSimpleSharing sShareSimpleSharing;
                    if (SShareSimpleSharing.this.mRecentContactsListThumb.get(i) != null) {
                        sShareSimpleSharing = SShareSimpleSharing.this;
                        sShareSimpleSharing.mRecentContactsListCount = sShareSimpleSharing.mRecentContactsListCount + 1;
                        i++;
                    } else {
                        sShareSimpleSharing = SShareSimpleSharing.this;
                        sShareSimpleSharing.mRecentContactsListCount = sShareSimpleSharing.mRecentContactsListCount + 1;
                        i++;
                    }
                }
                SShareSimpleSharing.this.mHandler.sendEmptyMessage(2000);
            }
        }
    }

    static final class DisplayDeviceInfo {
        int devType = 0;
        CharSequence deviceId;
        CharSequence deviceName;
        CharSequence displayLabel;
        int iconType;
        int netType = 0;
        CharSequence number;

        DisplayDeviceInfo(CharSequence charSequence) {
            this.displayLabel = charSequence;
            this.deviceName = charSequence;
            this.deviceId = null;
            this.number = null;
            this.iconType = 0;
        }

        DisplayDeviceInfo(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, int i, int i2, int i3) {
            this.displayLabel = charSequence2;
            this.deviceName = charSequence;
            this.deviceId = charSequence3;
            this.number = charSequence4;
            this.iconType = i;
            this.netType = i2;
            this.devType = i3;
        }
    }

    private final class RecentHistoryListAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final Intent[] mInitialIntents;
        private final Intent mIntent;
        private List<RecentHistoryListInfo> mRecentHistoryList = new ArrayList();

        public RecentHistoryListAdapter(Context context, Intent intent, Intent[] intentArr, int i) {
            this.mIntent = new Intent(intent);
            this.mIntent.setComponent(null);
            this.mInitialIntents = intentArr;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        private final void setDefaultView(View view, RecentHistoryListInfo recentHistoryListInfo) {
            float -wrap0 = SShareSimpleSharing.this.getFontScale();
            TextView textView = (TextView) view.findViewById(16908308);
            TextView textView2 = (TextView) view.findViewById(16908309);
            SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(17105628);
            textView.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * -wrap0);
            textView.setText(recentHistoryListInfo.displayLabel);
            textView2.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * -wrap0);
            textView2.setText(recentHistoryListInfo.extraInfo);
            ImageView imageView = (ImageView) view.findViewById(16909520);
            imageView.setBackgroundResource(17302766);
            imageView.setImageResource(17302724);
            GradientDrawable gradientDrawable = (GradientDrawable) imageView.getBackground();
            if (gradientDrawable != null) {
                gradientDrawable.setColor(SShareSimpleSharing.this.mContext.getResources().getColor(17170729));
            }
        }

        private final void setItemView(View view, RecentHistoryListInfo recentHistoryListInfo) {
            float -wrap0 = SShareSimpleSharing.this.getFontScale();
            TextView textView = (TextView) view.findViewById(16908308);
            TextView textView2 = (TextView) view.findViewById(16908309);
            TextView textView3 = (TextView) view.findViewById(16909513);
            if (recentHistoryListInfo.extraInfo == null) {
                SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(17105628);
                textView.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * -wrap0);
                textView.setText(recentHistoryListInfo.displayLabel);
                textView.setVisibility(0);
                textView2.setVisibility(8);
                textView3.setVisibility(8);
            } else {
                SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(17105629);
                textView2.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * -wrap0);
                textView2.setText(recentHistoryListInfo.displayLabel);
                SShareSimpleSharing.this.defaultTextSize = (float) SShareSimpleSharing.this.mContext.getResources().getDimensionPixelSize(17105628);
                textView3.setTextSize(0, SShareSimpleSharing.this.defaultTextSize * -wrap0);
                textView3.setText(recentHistoryListInfo.extraInfo);
                textView.setVisibility(8);
                textView2.setVisibility(0);
                textView3.setVisibility(0);
            }
            ImageView imageView = (ImageView) view.findViewById(16909519);
            int i = 17170729;
            imageView.setBackgroundResource(17302766);
            if (recentHistoryListInfo.photoIcon != null || recentHistoryListInfo.iconType == 3) {
                imageView.setImageDrawable(recentHistoryListInfo.photoIcon);
                if (((GradientDrawable) imageView.getBackground()) != null) {
                    i = recentHistoryListInfo.iconType == 3 ? 17170731 : 17170730;
                }
            } else {
                int i2;
                switch (recentHistoryListInfo.iconType) {
                    case 0:
                        i2 = 17302724;
                        i = 17170729;
                        break;
                    case 1:
                        i2 = 17302726;
                        i = 17170730;
                        break;
                    case 2:
                        i2 = 17302725;
                        i = 17170730;
                        break;
                    case 4:
                        i2 = 17301707;
                        i = 17170732;
                        break;
                    case 5:
                        i2 = 17301708;
                        i = 17170732;
                        break;
                    default:
                        i2 = 17302726;
                        i = 17170730;
                        break;
                }
                imageView.setImageResource(i2);
            }
            GradientDrawable gradientDrawable = (GradientDrawable) imageView.getBackground();
            if (gradientDrawable != null) {
                gradientDrawable.setColor(SShareSimpleSharing.this.mContext.getResources().getColor(i));
            }
        }

        public int getCount() {
            return this.mRecentHistoryList.size();
        }

        public Intent getIntent() {
            return this.mIntent;
        }

        public Object getItem(int i) {
            return this.mRecentHistoryList.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = SShareSimpleSharing.this.shouldShowRecentHistoryView() ? (view == null || view.findViewById(16909513) == null) ? this.mInflater.inflate(17367312, viewGroup, false) : view : (view == null || view.findViewById(16909513) != null) ? this.mInflater.inflate(17367313, viewGroup, false) : view;
            if (SShareSimpleSharing.this.shouldShowRecentHistoryView()) {
                setItemView(inflate, (RecentHistoryListInfo) this.mRecentHistoryList.get(i));
            } else {
                setDefaultView(inflate, (RecentHistoryListInfo) this.mRecentHistoryList.get(i));
            }
            return inflate;
        }
    }

    static final class RecentHistoryListInfo {
        DisplayDeviceInfo deviceInfo;
        CharSequence displayLabel;
        CharSequence extraInfo;
        int iconType;
        Drawable photoIcon;

        RecentHistoryListInfo(CharSequence charSequence) {
            this.displayLabel = charSequence;
            this.extraInfo = null;
            this.iconType = 0;
            this.photoIcon = null;
            this.deviceInfo = null;
        }

        RecentHistoryListInfo(CharSequence charSequence, int i, Drawable drawable, CharSequence charSequence2) {
            this.displayLabel = charSequence;
            this.iconType = i;
            this.photoIcon = drawable;
            this.extraInfo = charSequence2;
            this.deviceInfo = null;
        }

        RecentHistoryListInfo(CharSequence charSequence, int i, Drawable drawable, CharSequence charSequence2, DisplayDeviceInfo displayDeviceInfo) {
            this.displayLabel = charSequence;
            this.iconType = i;
            this.photoIcon = drawable;
            this.extraInfo = charSequence2;
            this.deviceInfo = displayDeviceInfo;
        }
    }

    public SShareSimpleSharing(Activity activity, Context context, SShareCommon sShareCommon, Intent intent, int i, List<Intent> list) {
        this.mActivity = activity;
        this.mContext = context;
        this.mFeature = sShareCommon;
        this.mOrigIntent = intent;
        this.mRecentHistoryListAdapter = new RecentHistoryListAdapter(context, intent, null, i);
        this.mRecipientDataId = new String[5];
        this.mRecentContactsId = new long[5];
        this.mRecentContactsItemContactsCountInGroup = new int[5];
        this.mExtraIntentList = list;
        checkEasySignUpCertificated();
        checkSSharingRecentContactExisted();
        checkRemoteShareServiceEnabled();
    }

    private void checkEasySignUpCertificated() {
        mEasySignUpCertificated = SShareSignUpManager.isJoined(this.mContext, 2);
        Log.d(TAG, "isJoined=" + mEasySignUpCertificated);
    }

    private void checkRemoteShareServiceEnabled() {
        if (mEasySignUpCertificated) {
            int serviceStatus = SShareSignUpManager.getServiceStatus(this.mContext, 2);
            Log.d(TAG, "ServiceStatus=" + serviceStatus);
            if (serviceStatus != 1) {
                this.mRemoteShareServiceEnabled = false;
                return;
            } else {
                this.mRemoteShareServiceEnabled = true;
                return;
            }
        }
        this.mRemoteShareServiceEnabled = true;
    }

    private void checkSSharingRecentContactExisted() {
        mSSharingRecentContactExisted = false;
        Log.d(TAG, "isRecentContactExisted=" + mSSharingRecentContactExisted);
    }

    private void clearRecentHistoryList(boolean z) {
        if (z && this.mRecentHistoryListAdapter != null && this.mRecentHistoryListAdapter.mRecentHistoryList != null) {
            this.mRecentHistoryListAdapter.mRecentHistoryList.clear();
        }
    }

    private float getFontScale() {
        float f = this.mContext.getResources().getConfiguration().fontScale;
        return f > SShareConstants.MAX_FONT_SCALE ? SShareConstants.MAX_FONT_SCALE : f;
    }

    private RecentHistoryListInfo getRecentHistoryInfo(int i) {
        return (RecentHistoryListInfo) this.mRecentHistoryListAdapter.getItem(i);
    }

    private int getRecentIconType(byte[] bArr, int i, long j, int i2) {
        return i2 != -1 ? i2 == 1 ? 4 : i2 == 2 ? 5 : 4 : bArr != null ? 3 : i > 1 ? 2 : 1;
    }

    private void initRecentHistoryDefault() {
        this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, new RecentHistoryListInfo(this.mContext.getResources().getText(17041137), 0, null, this.mContext.getResources().getText(17041138)));
        this.mRecentHistoryIndex++;
        this.mRecentHistoryListAdapter.notifyDataSetChanged();
    }

    private void initRecentHistoryList() {
        if (this.mRecentHistoryListAdapter != null && this.mGridRecentHistory != null) {
            clearRecentHistoryList(true);
            this.mRecentHistoryIndex = 0;
            if (this.mRecentContactsListCount >= 1) {
                float dimensionPixelSize = (float) this.mContext.getResources().getDimensionPixelSize(17105489);
                float dimensionPixelSize2 = (float) this.mContext.getResources().getDimensionPixelSize(17105490);
                float integer = (float) this.mContext.getResources().getInteger(17694974);
                Object obj = this.mContext.getResources().getConfiguration().orientation == 2 ? 1 : null;
                this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, new RecentHistoryListInfo(this.mContext.getResources().getText(17041138)));
                this.mRecentHistoryIndex++;
                float applyDimension = TypedValue.applyDimension(0, ((float) this.mContext.getResources().getDimensionPixelSize(17105628)) * getFontScale(), this.mContext.getResources().getDisplayMetrics());
                int i = 0;
                while (i < this.mRecentContactsListCount && i < 5) {
                    CharSequence charSequence;
                    CharSequence charSequence2 = null;
                    String str = "";
                    String str2 = "";
                    Drawable drawable = null;
                    if (this.mRecentContactsItemContactsCountInGroup[i] > 1) {
                        int i2;
                        CharSequence charSequence3 = (CharSequence) this.mRecentContactsListName.get(i);
                        String str3 = "";
                        String str4 = "";
                        float f = 0.0f;
                        Object obj2 = null;
                        Paint textPaint = new TextPaint();
                        textPaint.setTextSize(applyDimension);
                        textPaint.setTypeface(Typeface.create("sec-roboto-light", 0));
                        textPaint.setAntiAlias(true);
                        textPaint.setTextAlign(Align.CENTER);
                        textPaint.density = this.mContext.getResources().getDisplayMetrics().density;
                        if (TextUtils.ellipsize(charSequence3, textPaint, obj != null ? (dimensionPixelSize2 * integer) - 0.0f : ((dimensionPixelSize * integer) - 0.0f) - this.mTunedMargin, TruncateAt.END).toString().toLowerCase().endsWith("" + this.ELLIPSIS_NORMAL[0])) {
                            obj2 = 1;
                        }
                        if (this.mGroupNameOldConcept) {
                            str = this.mContext.getString(17041139);
                            str4 = String.format(str, new Object[]{str3, Integer.valueOf(this.mRecentContactsItemContactsCountInGroup[i] - 1)});
                        } else {
                            if (obj2 != null) {
                                str = "(" + this.mRecentContactsItemContactsCountInGroup[i] + ")";
                            }
                            str4 = str;
                        }
                        float[] fArr = new float[str4.length()];
                        for (i2 = 0; i2 < textPaint.getTextWidths(str4, fArr); i2++) {
                            f += fArr[i2];
                        }
                        float f2 = obj != null ? (dimensionPixelSize2 * integer) - f : obj2 != null ? ((dimensionPixelSize * integer) - f) - this.mTunedMargin : (dimensionPixelSize * integer) - f;
                        CharSequence ellipsize = TextUtils.ellipsize(charSequence3, textPaint, f2, TruncateAt.END);
                        if (obj != null) {
                            if (this.mGroupNameOldConcept && ("" + this.ELLIPSIS_NORMAL[0]).equals(ellipsize)) {
                                ellipsize = charSequence3.subSequence(0, 1).toString() + this.ELLIPSIS_NORMAL[0];
                            }
                            charSequence = ellipsize.toString() + str;
                        } else {
                            float[] fArr2 = new float[charSequence3.toString().length()];
                            int textWidths = textPaint.getTextWidths(charSequence3.toString(), fArr2);
                            float f3 = 0.0f;
                            float f4 = 0.0f;
                            int i3 = 0;
                            String str5 = "";
                            String str6 = "";
                            i2 = 0;
                            while (i2 < textWidths) {
                                f3 += fArr2[i2];
                                if (f3 >= dimensionPixelSize) {
                                    i3 = i2;
                                    break;
                                } else {
                                    str5 = str5 + charSequence3.charAt(i2);
                                    i2++;
                                }
                            }
                            Object obj3 = str5;
                            if (i3 > 0) {
                                float f5 = obj2 != null ? (dimensionPixelSize - f) - this.mTunedMargin : dimensionPixelSize - f;
                                for (i2 = i3; i2 < textWidths; i2++) {
                                    f4 += fArr2[i2];
                                    if (f4 >= f5) {
                                        break;
                                    }
                                    str6 = str6 + charSequence3.charAt(i2);
                                }
                                Object obj4 = obj2 != null ? str6 + this.ELLIPSIS_NORMAL[0] + str : str6 + str;
                            }
                        }
                    } else {
                        charSequence = (CharSequence) this.mRecentContactsListName.get(i);
                    }
                    int recentIconType = getRecentIconType((byte[]) this.mRecentContactsListThumb.get(i), this.mRecentContactsItemContactsCountInGroup[i], this.mRecentContactsId[i], -1);
                    if (recentIconType == 3) {
                        drawable = makeContactPhotoImage((byte[]) this.mRecentContactsListThumb.get(i));
                    } else if (!(recentIconType == 2 || TextUtils.isEmpty(charSequence))) {
                        char charAt = charSequence.charAt(0);
                        if (Character.isAlphabetic(charAt)) {
                            drawable = makeBitmapWithText(this.mContext, String.valueOf(charAt));
                        }
                    }
                    RecentHistoryListInfo recentHistoryListInfo;
                    if (this.mRecentContactsItemContactsCountInGroup[i] > 1) {
                        recentHistoryListInfo = new RecentHistoryListInfo(charSequence, recentIconType, drawable, charSequence2);
                    } else {
                        recentHistoryListInfo = new RecentHistoryListInfo(charSequence, recentIconType, drawable, null);
                    }
                    this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, r37);
                    this.mRecentHistoryIndex++;
                    i++;
                }
            } else if (this.mRecentContactsListCount == 0) {
                this.mRecentHistoryListAdapter.mRecentHistoryList.add(this.mRecentHistoryIndex, new RecentHistoryListInfo(this.mContext.getResources().getText(17041138)));
                this.mRecentHistoryIndex++;
            }
            this.mRecentHistoryListAdapter.notifyDataSetChanged();
        }
    }

    private Drawable makeBitmapWithText(Context context, String str) {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(17105485);
        float dimensionPixelSize2 = (float) this.mContext.getResources().getDimensionPixelSize(17105486);
        Bitmap createBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColor(this.mContext.getResources().getColor(17170730));
        paint.setAntiAlias(true);
        canvas.drawCircle(((float) dimensionPixelSize) / SprDocument.DEFAULT_DENSITY_SCALE, ((float) dimensionPixelSize) / SprDocument.DEFAULT_DENSITY_SCALE, ((float) dimensionPixelSize) / SprDocument.DEFAULT_DENSITY_SCALE, paint);
        Paint paint2 = new Paint();
        paint2.setTextSize(dimensionPixelSize2);
        paint2.setAntiAlias(true);
        paint2.setColor(this.mContext.getResources().getColor(17170731));
        paint2.setTextAlign(Align.CENTER);
        paint2.getTextBounds(str, 0, str.length(), new Rect());
        canvas.drawText(str, ((float) dimensionPixelSize) / SprDocument.DEFAULT_DENSITY_SCALE, ((float) (dimensionPixelSize * 3)) / 4.0f, paint2);
        return new BitmapDrawable(this.mContext.getResources(), createBitmap);
    }

    private Drawable makeContactPhotoImage(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        Bitmap decodeResource = BitmapFactory.decodeResource(this.mContext.getResources(), 17302723);
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(bArr, 0, bArr.length), decodeResource.getWidth(), decodeResource.getHeight(), true);
        Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(createScaledBitmap, 0.0f, 0.0f, null);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(decodeResource, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return new BitmapDrawable(this.mContext.getResources(), createBitmap);
    }

    public void buildUpSimpleSharingData() {
        if (shouldShowRecentHistoryView()) {
            sendRequestRecentContactsHistoryList();
            this.mRecentHistoryListAdapter.notifyDataSetChanged();
            this.mHandler.sendEmptyMessage(2000);
            return;
        }
        initRecentHistoryDefault();
    }

    public Intent getRecentHistoryIntent(int i) {
        Parcelable intent = this.mRecentHistoryListAdapter.getIntent();
        this.mRecentHistoryIntent = new Intent(SShareConstants.INTENT_ACTION_REQUESTSEND);
        this.mRecentHistoryIntent.addFlags(67108864);
        this.mRecentHistoryIntent.putExtra("android.intent.extra.INTENT", intent);
        if (this.mExtraIntentList != null) {
            int size = this.mExtraIntentList.size();
            Parcelable[] parcelableArr = new Intent[size];
            for (int i2 = 0; i2 < size; i2++) {
                parcelableArr[i2] = (Intent) this.mExtraIntentList.get(i2);
            }
            this.mRecentHistoryIntent.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
        }
        if (i > 0) {
            this.mRecentHistoryIntent.putExtra(SShareConstants.TAG_RECIPIENT_DATAIDS, this.mRecipientDataId[i - 1]);
        }
        return this.mRecentHistoryIntent;
    }

    public RecentHistoryListAdapter getRecentHistoryListAdapter() {
        return this.mRecentHistoryListAdapter;
    }

    public boolean hasExtraIntentUriInfo() {
        if (this.mExtraIntentList != null) {
            for (int i = 0; i < this.mExtraIntentList.size(); i++) {
                Bundle extras = ((Intent) this.mExtraIntentList.get(i)).getExtras();
                if (extras != null && ((Uri) extras.getParcelable("android.intent.extra.STREAM")) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEasySignUpCertificated() {
        return mEasySignUpCertificated;
    }

    public boolean isRemoteShareServiceEnabled() {
        return this.mRemoteShareServiceEnabled;
    }

    public void recentHistoryDefaultGridItemClick(int i) {
        switch (i) {
            case 0:
                Intent intent = new Intent(SShareConstants.ACTION_REQ_AUTH);
                intent.addFlags(67108864);
                intent.putExtra("fromOOBE", false);
                intent.putExtra("agreeMarketing", false);
                intent.putExtra("AuthRequestFrom", "shareVia");
                try {
                    this.mActivity.startActivityForResult(intent, 1);
                    return;
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Easy signUp agent is not found");
                    return;
                }
            default:
                return;
        }
    }

    public void recentHistoryGridItemClick(int i) {
        if (getRecentHistoryListAdapter() != null) {
            if (this.mFeature.getSupportLogging()) {
                String str;
                SShareLogging sShareLogging = new SShareLogging(this.mContext, this.mOrigIntent);
                switch (getRecentHistoryInfo(i).iconType) {
                    case 2:
                        str = SShareConstants.SURVEY_DETAIL_FEATURE_CONTACTGROUP;
                        break;
                    default:
                        str = SShareConstants.SURVEY_DETAIL_FEATURE_CONTACTPRIV;
                        break;
                }
                sShareLogging.insertLog(SShareConstants.SURVEY_FEATURE_EASYSHARE, str);
            }
            try {
                this.mActivity.startActivity(getRecentHistoryIntent(i));
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "RecentHistoryGridItemClick : startActivity failed!!");
            }
        }
    }

    public void registerRecentContactsReceiver() {
        checkEasySignUpCertificated();
        checkSSharingRecentContactExisted();
        checkRemoteShareServiceEnabled();
        if (shouldShowRecentHistoryView() && this.mRemoteShareServiceEnabled && !this.mIsRecentContactsReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SShareConstants.RESPONSE_RECENT_GROUP_CONTACTS);
            this.mContext.registerReceiver(this.mRecentContactsReceiver, intentFilter);
            this.mIsRecentContactsReceiverRegistered = true;
        }
    }

    public void sendRequestRecentContactsHistoryList() {
        registerRecentContactsReceiver();
        this.mContext.sendBroadcast(new Intent(SShareConstants.INTENT_REQUEST_RECENT_GROUP));
    }

    public void setSimpleSharingView(SemHorizontalListView semHorizontalListView, OnItemClickListener onItemClickListener) {
        this.mGridRecentHistory = semHorizontalListView;
        if (this.mGridRecentHistory != null) {
            this.mGridRecentHistory.setAdapter(this.mRecentHistoryListAdapter);
            this.mGridRecentHistory.setOnItemClickListener(onItemClickListener);
        }
    }

    public boolean shouldShowRecentHistoryView() {
        return mEasySignUpCertificated ? mSSharingRecentContactExisted : false;
    }

    public void unregisterRecentContactsReceiver() {
        if (this.mIsRecentContactsReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mRecentContactsReceiver);
            this.mIsRecentContactsReceiverRegistered = false;
        }
    }
}
