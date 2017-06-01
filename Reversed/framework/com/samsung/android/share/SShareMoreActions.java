package com.samsung.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.android.knox.SemPersonaManager;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class SShareMoreActions {
    private static final boolean DEBUG = false;
    private static final String TAG = "SShareMoreActions";
    private ArrayList<ActionItem> arItem;
    private boolean bottomPanelExpaned = false;
    private float defaultTextSize;
    private Activity mActivity;
    private SShareBixby mBixby;
    private BottomPanelAdapter mBottomAdapter;
    private ViewGroup mBottomPanel;
    private Context mContext;
    private boolean mEnabledShowBtnBg = false;
    private SShareCommon mFeature;
    private GridView mGridMoreActions;
    private String mLaunchedFromPackage;
    private int mSharePanelVisibleHeight = 0;
    private boolean mSupportEnhancedMoreActions = false;
    private Window mWindow;
    private WifiManager wifiManager = null;

    static class ActionItem {
        int icon;
        int id;
        String name;

        ActionItem(int i, int i2, String str) {
            this.id = i;
            this.icon = i2;
            this.name = str;
        }
    }

    private final class BottomPanelAdapter extends BaseAdapter {
        ArrayList<ActionItem> arSrc;
        Context context;
        private final LayoutInflater inflater = ((LayoutInflater) this.context.getSystemService("layout_inflater"));
        int layout;

        public BottomPanelAdapter(Context context, int i, ArrayList<ActionItem> arrayList) {
            this.context = context;
            this.arSrc = arrayList;
            this.layout = i;
        }

        public int getCount() {
            return this.arSrc.size();
        }

        public String getItem(int i) {
            return ((ActionItem) this.arSrc.get(i)).name;
        }

        public int getItemActionId(int i) {
            return ((ActionItem) this.arSrc.get(i)).id;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            int i2 = i;
            if (view == null) {
                view = this.inflater.inflate(this.layout, viewGroup, false);
            }
            float f = SShareMoreActions.this.mContext.getResources().getConfiguration().fontScale;
            if (f > SShareConstants.MAX_FONT_SCALE) {
                f = SShareConstants.MAX_FONT_SCALE;
            }
            if (SShareMoreActions.this.mEnabledShowBtnBg) {
                view.setBackgroundResource(17303577);
            }
            ((ImageView) view.findViewById(16908294)).setImageResource(((ActionItem) this.arSrc.get(i)).icon);
            TextView textView = (TextView) view.findViewById(16908432);
            SShareMoreActions.this.defaultTextSize = SShareMoreActions.this.mContext.getResources().getDimension(17105629);
            textView.setTextSize(0, SShareMoreActions.this.defaultTextSize * f);
            textView.setText(((ActionItem) this.arSrc.get(i)).name);
            return view;
        }
    }

    public SShareMoreActions(Activity activity, Context context, SShareCommon sShareCommon, Window window, String str, SShareBixby sShareBixby) {
        this.mActivity = activity;
        this.mContext = context;
        this.mWindow = window;
        this.mFeature = sShareCommon;
        this.mLaunchedFromPackage = str;
        this.mBixby = sShareBixby;
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver != null && System.getInt(contentResolver, "show_button_background", 0) == 1) {
            this.mEnabledShowBtnBg = true;
        }
    }

    private boolean checkAPConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(13);
        return (networkInfo != null && networkInfo.getDetailedState() == DetailedState.CONNECTED) || (networkInfo2 != null && networkInfo2.getDetailedState() == DetailedState.CONNECTED);
    }

    private boolean checkHdmiPlugged() {
        Object e;
        Reader reader;
        Object e2;
        Throwable th;
        boolean z = false;
        if (new File("/sys/devices/virtual/switch/hdmi/state").exists()) {
            String str = "/sys/class/switch/hdmi/state";
            InputStreamReader inputStreamReader = null;
            try {
                Reader fileReader = new FileReader("/sys/class/switch/hdmi/state");
                try {
                    char[] cArr = new char[15];
                    int read = fileReader.read(cArr);
                    if (read > 1) {
                        z = Integer.parseInt(new String(cArr, 0, read + -1)) != 0;
                    }
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e3) {
                        }
                    }
                } catch (IOException e4) {
                    e = e4;
                    reader = fileReader;
                    Log.d(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + e);
                    if (inputStreamReader != null) {
                        try {
                            inputStreamReader.close();
                        } catch (IOException e5) {
                        }
                    }
                    return z;
                } catch (NumberFormatException e6) {
                    e2 = e6;
                    reader = fileReader;
                    try {
                        Log.d(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + e2);
                        if (inputStreamReader != null) {
                            try {
                                inputStreamReader.close();
                            } catch (IOException e7) {
                            }
                        }
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        if (inputStreamReader != null) {
                            try {
                                inputStreamReader.close();
                            } catch (IOException e8) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    reader = fileReader;
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    throw th;
                }
            } catch (IOException e9) {
                e = e9;
                Log.d(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + e);
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                return z;
            } catch (NumberFormatException e10) {
                e2 = e10;
                Log.d(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + e2);
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                return z;
            }
        }
        return z;
    }

    private boolean checkScreenMirroringRunning() {
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
        return displayManager.semGetActiveDlnaState() == 1 || displayManager.getWifiDisplayStatus().getActiveDisplayState() == 2;
    }

    private boolean checkScreenSharingCondition() {
        boolean z = false;
        if (SemPersonaManager.isKioskModeEnabled(this.mContext)) {
            return false;
        }
        MediaRouter mediaRouter = (MediaRouter) this.mContext.getSystemService("media_router");
        if (mediaRouter != null) {
            RouteInfo selectedRoute = mediaRouter.getSelectedRoute(4);
            boolean z2 = (selectedRoute == null || !selectedRoute.matchesTypes(4)) ? false : selectedRoute.getDeviceAddress() == null;
            if (z2) {
                Log.d(TAG, "checkScreenSharingCondition: chromecast connected");
                return false;
            }
        }
        if (checkHdmiPlugged()) {
            Log.d(TAG, "checkScreenSharingCondition: Hdmi plugged");
            return false;
        }
        if ((this.mFeature.getScreenSharingEnable() == 1 || this.mFeature.getScreenSharingEnable() == 2) && !checkScreenMirroringRunning()) {
            z = true;
        }
        return z;
    }

    private int getSharePanelVisibieHeight() {
        return this.mSharePanelVisibleHeight;
    }

    private boolean isKnoxMode() {
        return this.mFeature.isKnoxModeEnabled();
    }

    public void setMoreActionsView(ViewGroup viewGroup, OnItemClickListener onItemClickListener) {
        if (viewGroup != null) {
            boolean checkAPConnection = checkAPConnection();
            this.mSupportEnhancedMoreActions = this.mFeature.getSupportEnhancedMoreActions();
            int integer = this.mContext.getResources().getInteger(17694976);
            Log.d(TAG, "isAPConnected = " + checkAPConnection + " mSupportEnhancedMoreActions = " + this.mSupportEnhancedMoreActions + " maxColumnsMoreActions = " + integer);
            this.mBottomPanel = viewGroup;
            this.mBottomPanel.setVisibility(0);
            this.arItem = new ArrayList();
            if (this.mSupportEnhancedMoreActions) {
                if (this.mFeature.getQuickConnectEnable() == 1) {
                    this.arItem.add(new ActionItem(104, 17303582, this.mContext.getString(17041146)));
                }
                if (checkScreenSharingCondition()) {
                    this.arItem.add(new ActionItem(103, 17303581, this.mContext.getString(17041147)));
                }
                if (this.mFeature.getChangePlayerEnable() == 1 && checkAPConnection) {
                    this.arItem.add(new ActionItem(101, 17304349, this.mContext.getString(17041141)));
                }
            } else {
                if (this.mFeature.getChangePlayerEnable() == 1 && checkAPConnection) {
                    this.arItem.add(new ActionItem(101, 17304349, this.mContext.getString(17041141)));
                }
                if (this.mFeature.getScreenMirroringEnable() == 1) {
                    this.arItem.add(new ActionItem(102, 17303326, this.mContext.getString(17041142)));
                }
                if (checkScreenSharingCondition()) {
                    this.arItem.add(new ActionItem(103, 17303326, SShareConstants.SURVEY_DETAIL_FEATURE_SCREEN_SHARING));
                }
                if (this.mFeature.getQuickConnectEnable() == 1) {
                    this.arItem.add(new ActionItem(104, 17303258, this.mContext.getString(17041144)));
                }
                if (this.mFeature.getPrintEnable() == 1) {
                    this.arItem.add(new ActionItem(105, 17303204, this.mContext.getString(17041145)));
                }
            }
            this.mBottomAdapter = new BottomPanelAdapter(this.mContext, 17367300, this.arItem);
            this.mGridMoreActions = (GridView) this.mWindow.findViewById(16909516);
            if (this.mGridMoreActions != null && this.arItem.size() > 0) {
                this.mGridMoreActions.setVisibility(0);
                this.mGridMoreActions.setAdapter(this.mBottomAdapter);
                this.mGridMoreActions.setOnItemClickListener(onItemClickListener);
                if (this.mSupportEnhancedMoreActions || this.arItem.size() == integer) {
                    this.mGridMoreActions.setNumColumns(this.arItem.size());
                }
            }
        }
    }

    public void setSharePanelVisibleHeight(int i) {
        this.mSharePanelVisibleHeight = i;
    }

    public void startAction(int i, Intent intent, View view) {
        if (view != null) {
            setSharePanelVisibleHeight(view.getHeight());
        } else {
            Log.d(TAG, "mVisibleArea is null !");
        }
        if (this.mFeature.getSupportLogging()) {
            String str;
            SShareLogging sShareLogging = new SShareLogging(this.mContext, intent);
            switch (i) {
                case 101:
                    str = SShareConstants.SURVEY_DETAIL_FEATURE_CHANGEPLAYER;
                    break;
                case 102:
                    str = SShareConstants.SURVEY_DETAIL_FEATURE_MIRRORING;
                    break;
                case 103:
                    str = SShareConstants.SURVEY_DETAIL_FEATURE_SCREEN_SHARING;
                    break;
                case 104:
                    str = SShareConstants.SURVEY_DETAIL_FEATURE_NEARBY_SHARING;
                    break;
                case 105:
                    str = SShareConstants.SURVEY_DETAIL_FEATURE_PRINT;
                    break;
                default:
                    str = "Wrong ID";
                    break;
            }
            sShareLogging.insertLog(SShareConstants.SURVEY_FEATURE_MOREACTION, str);
        }
        Intent intent2;
        switch (i) {
            case 101:
                try {
                    intent2 = new Intent(SShareConstants.INTENT_CHANGE_PLAYER);
                    intent2.putExtra(SShareConstants.MORE_ACTIONS_PACKAGE_NAME, this.mLaunchedFromPackage);
                    this.mContext.sendBroadcast(intent2);
                    return;
                } catch (ActivityNotFoundException e) {
                    Log.w(TAG, "MoreActions : ActivityNotFoundException !!! ");
                    return;
                } catch (Throwable e2) {
                    Log.w(TAG, "MoreActions : Exception !!!");
                    e2.printStackTrace();
                    return;
                }
            case 102:
            case 103:
                if (SShareConstants.SMART_MIRRING_DIALOG_SUPPORT) {
                    if (this.mBixby != null) {
                        this.mBixby.sendAppSelectionForBixby(SShareConstants.SCREEN_MIRRORING_PKG_DREAM);
                    }
                    ComponentName componentName = new ComponentName(SShareConstants.SCREEN_MIRRORING_PKG_DREAM, "com.samsung.android.smartmirroring.CastingDialog");
                    intent2 = new Intent("android.intent.action.MAIN");
                    intent2.addCategory("android.intent.category.LAUNCHER");
                    intent2.setComponent(componentName);
                } else {
                    if (this.mBixby != null) {
                        this.mBixby.sendAppSelectionForBixby(SShareConstants.SCREEN_MIRRORING_PKG);
                    }
                    intent2 = new Intent(SShareConstants.SCREEN_MIRRORING_CLASS);
                }
                intent2.putExtra(SShareConstants.SCREEN_MIRRORING_EXTRA_DIALOG_ONCE, true);
                intent2.putExtra(SShareConstants.SCREEN_MIRRORING_EXTRA_TAG_WRITE, false);
                intent2.putExtra(SShareConstants.MORE_ACTIONS_PACKAGE_NAME, this.mLaunchedFromPackage);
                intent2.putExtra(SShareConstants.MORE_ACTIONS_SCREEN_SHARING_MODE, this.mFeature.getScreenSharingEnable());
                intent2.putExtra(SShareConstants.MORE_ACTIONS_KNOX_STATE, isKnoxMode());
                intent2.putExtra("android.intent.extra.INTENT", intent);
                intent2.addFlags(343932928);
                this.mActivity.startActivityAsUser(intent2, UserHandle.CURRENT);
                return;
            case 104:
                if (this.mBixby != null) {
                    this.mBixby.sendAppSelectionForBixby(SShareConstants.QUICK_CONNECT_PKG);
                }
                if (this.mFeature.isIntentFileUriScheme(intent)) {
                    intent2 = new Intent(SShareConstants.QUICK_CONNECT_ACTION);
                    intent2.setPackage(SShareConstants.QUICK_CONNECT_PKG);
                    intent2.setType("*/*");
                    intent2.putExtra("android.intent.extra.INTENT", intent);
                } else {
                    intent2 = new Intent(intent);
                    intent2.setPackage(SShareConstants.QUICK_CONNECT_PKG);
                    intent2.setAction(SShareConstants.QUICK_CONNECT_ACTION);
                }
                intent2.putExtra(SShareConstants.QUICK_CONNECT_EXTRA_HEIGHT, getSharePanelVisibieHeight());
                if (this.mSupportEnhancedMoreActions) {
                    intent2.putExtra(SShareConstants.MORE_ACTIONS_PRINT, this.mFeature.getPrintEnable());
                    intent2.putExtra(SShareConstants.MORE_ACTIONS_QUICK_CONNECT, this.mFeature.getQuickConnectEnable());
                    intent2.putExtra(SShareConstants.MORE_ACTIONS_PACKAGE_NAME, this.mLaunchedFromPackage);
                }
                Log.d(TAG, "mFeature.getPrintEnable()= " + this.mFeature.getPrintEnable() + "mFeature.getQuickConnectEnable()= " + this.mFeature.getQuickConnectEnable());
                this.mActivity.startActivityAsUser(intent2, UserHandle.CURRENT);
                return;
            case 105:
                Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.STREAM");
                intent2 = new Intent(SShareConstants.INTENT_MOBILE_PRINT);
                intent2.putExtra(SShareConstants.MORE_ACTIONS_PACKAGE_NAME, this.mLaunchedFromPackage);
                intent2.putExtra("android.intent.extra.STREAM", uri);
                this.mContext.sendBroadcast(intent2);
                return;
            default:
                return;
        }
    }

    public void startMoreActions(int i, Intent intent, View view) {
        startAction(this.mBottomAdapter.getItemActionId(i), intent, view);
    }
}
