package com.samsung.android.share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageItemInfo;
import android.os.Handler;
import android.util.Log;
import com.android.internal.app.ChooserActivity.ChooserListAdapter;
import com.android.internal.app.ResolverActivity;
import com.android.internal.app.ResolverActivity.ResolveListAdapter;
import com.samsung.android.share.executor.CommandObserver;
import com.samsung.android.share.executor.ExecutorCommandHandler;
import com.samsung.android.share.executor.IExecutorCommandListener;
import com.samsung.android.share.executor.data.NlgRequestInfo;
import com.samsung.android.share.executor.data.ParamFilling;
import com.samsung.android.share.executor.data.Parameter;
import com.samsung.android.share.executor.data.ScreenParameter;
import com.samsung.android.share.executor.data.ScreenStateInfo;
import com.samsung.android.share.executor.data.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SShareBixby {
    private static final boolean DEBUG = false;
    private static final String EXTRA_APPNAME = "BIXBY_SHAREVIA_APPNAME";
    private static final String INTENT_DEBUG_APPNAME = "com.samsung.android.chooser.DEBUG_APPNAME";
    private static final String TAG = "SShareBixby";
    private static final boolean USE_DISTANCE = false;
    private Activity mActivity;
    private ResolveListAdapter mAdapter;
    BroadcastReceiver mBixbyGetDataReceiver = new C02401();
    private Map<String, Integer> mCandidatesByPkg = new HashMap();
    private String mClass;
    private Context mContext;
    private String mDebugAppName;
    BroadcastReceiver mDebugAppNameReceiver = new C02412();
    private ArrayList<Integer> mDuplicateLabelIndex = new ArrayList();
    private ExecutorCommandHandler mExecutorCommandHandler;
    private SShareCommon mFeature;
    final Handler mHandler = new Handler();
    private boolean mIsBixbyDone;
    private boolean mIsInBixbyState;
    private String mLabel;
    private int mMatchedAppIndex;
    private String mMatchedAppLabel;
    private int mMatchedAppType = 3;
    private String mPackage;
    private String[] mPkgsWithRule;
    private String mSlotAppName;

    class C02401 extends BroadcastReceiver {
        C02401() {
        }

        public void onReceive(Context context, Intent intent) {
            if (SShareConstants.INTENT_ACTION_EM_COMMAND.equals(intent.getAction())) {
                CommandObserver.getInstance().notify(intent);
            }
        }
    }

    class C02412 extends BroadcastReceiver {
        C02412() {
        }

        public void onReceive(Context context, Intent intent) {
            if (SShareBixby.INTENT_DEBUG_APPNAME.equals(intent.getAction())) {
                SShareBixby.this.mDebugAppName = intent.getStringExtra(SShareBixby.EXTRA_APPNAME);
                Log.d(SShareBixby.TAG, "mDebugAppName=" + SShareBixby.this.mDebugAppName);
            }
        }
    }

    class C02423 implements IExecutorCommandListener {
        C02423() {
        }

        public boolean onParamFillingReceived(ParamFilling paramFilling) {
            Log.d(SShareBixby.TAG, "onParamFillingReceived");
            SShareBixby.this.initializeVariables();
            ScreenParameter screenParameter = (ScreenParameter) paramFilling.getScreenParamMap().get(SShareConstants.PARAM_ID_APPNAME);
            if (screenParameter != null) {
                SShareBixby.this.mSlotAppName = screenParameter.getSlotValue();
                Log.d(SShareBixby.TAG, "ParamFilling: mSlotAppName=" + SShareBixby.this.mSlotAppName);
            }
            if (!(SShareBixby.this.mSlotAppName == null || SShareBixby.this.mSlotAppName.isEmpty())) {
                SShareBixby.this.splitSlotValue(SShareBixby.this.mSlotAppName);
                if (SShareBixby.this.getCandidates() && ((ResolverActivity) SShareBixby.this.mActivity).startBixbySelection(SShareBixby.this.mMatchedAppType, SShareBixby.this.mMatchedAppIndex)) {
                    SShareBixby.this.mActivity.finish();
                    return true;
                }
            }
            return true;
        }

        public void onRuleCanceled(String str) {
            Log.d(SShareBixby.TAG, "onRuleCanceled");
        }

        public ScreenStateInfo onScreenStatesRequested() {
            Log.d(SShareBixby.TAG, "onScreenStatesRequested");
            return new ScreenStateInfo("ShareVia");
        }

        public void onStateReceived(State state) {
            Log.d(SShareBixby.TAG, "onStateReceived:" + state.getStateId());
            if (state.getStateId().equals("ShareVia")) {
                SShareBixby.this.mIsInBixbyState = true;
                SShareBixby.this.mExecutorCommandHandler.sendStateCommandResponse(ExecutorCommandHandler.RESULT_SUCCESS);
            } else if (state.getStateId().equals("CrossShareVia")) {
                SShareBixby.this.initializeVariables();
                Parameter parameter = (Parameter) state.getParamMap().get(SShareConstants.PARAM_ID_APPNAME);
                if (parameter != null) {
                    SShareBixby.this.mSlotAppName = parameter.getSlotValue();
                    Log.d(SShareBixby.TAG, "State: mSlotAppName=" + SShareBixby.this.mSlotAppName);
                }
                if (SShareBixby.this.mPkgsWithRule == null && (SShareBixby.this.mSlotAppName == null || SShareBixby.this.mSlotAppName.isEmpty())) {
                    SShareBixby.this.sendNlgRequest(0);
                    return;
                }
                if ((SShareBixby.this.mSlotAppName == null || SShareBixby.this.mSlotAppName.isEmpty()) && SShareBixby.this.mPkgsWithRule != null && SShareBixby.this.mPkgsWithRule.length == 1) {
                    if (SShareBixby.this.mPkgsWithRule[0].isEmpty()) {
                        SShareBixby.this.sendNlgRequest(0);
                        return;
                    }
                    SShareBixby.this.splitPackageClassValue(SShareBixby.this.mPkgsWithRule[0]);
                    if (SShareBixby.this.parsingTargetInfoByPackageName(SShareBixby.this.mPackage) && ((ResolverActivity) SShareBixby.this.mActivity).startBixbySelection(SShareBixby.this.mMatchedAppType, SShareBixby.this.mMatchedAppIndex)) {
                        SShareBixby.this.mActivity.finish();
                        return;
                    }
                }
                if (!(SShareBixby.this.mSlotAppName == null || SShareBixby.this.mSlotAppName.isEmpty())) {
                    SShareBixby.this.splitSlotValue(SShareBixby.this.mSlotAppName);
                    if (SShareBixby.this.getCandidates() && ((ResolverActivity) SShareBixby.this.mActivity).startBixbySelection(SShareBixby.this.mMatchedAppType, SShareBixby.this.mMatchedAppIndex)) {
                        SShareBixby.this.mActivity.finish();
                        return;
                    }
                }
                if (SShareBixby.this.mDuplicateLabelIndex.size() > 1) {
                    SShareBixby.this.sendNlgRequest(2);
                } else if (SShareBixby.this.mSlotAppName == null || SShareBixby.this.mSlotAppName.isEmpty()) {
                    SShareBixby.this.sendNlgRequest(0);
                } else {
                    SShareBixby.this.sendNlgRequest(1);
                }
            } else {
                Log.e(SShareBixby.TAG, "Invalid State Command");
                SShareBixby.this.mExecutorCommandHandler.sendStateCommandResponse(ExecutorCommandHandler.RESULT_FAILURE);
            }
        }
    }

    public SShareBixby(Activity activity, Context context, SShareCommon sShareCommon, String[] strArr) {
        this.mActivity = activity;
        this.mContext = context;
        this.mFeature = sShareCommon;
        this.mPkgsWithRule = strArr;
        this.mAdapter = ((ResolverActivity) this.mActivity).getResolveListAdapter();
        createExecutorCommandHandler();
    }

    private String StringReplace(String str) {
        return str.replaceAll("[[?][$]\\(\\)\\{\\}[*][+]\\^[|]\\[\\]\\n!#%&@`:;~,<>.~'-=]", "").replaceAll("\\s", "").toLowerCase();
    }

    private String StringReplaceSmartView(String str) {
        return str.replaceAll("[[?][$]\\(\\)\\{\\}[*][+]\\^[|]\\[\\]\\n!#%&@`:;~,<>.~'-=]", "").replaceAll(SShareConstants.SURVEY_DETAIL_FEATURE_SCREEN_SHARING, "").toLowerCase();
    }

    private void addReceiverForBixby() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SShareConstants.INTENT_ACTION_EM_COMMAND);
        this.mContext.registerReceiver(this.mBixbyGetDataReceiver, intentFilter);
    }

    private void addReceiverForDebugAppName() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_DEBUG_APPNAME);
        this.mContext.registerReceiver(this.mDebugAppNameReceiver, intentFilter);
    }

    private boolean checkDirectShareItem(int i) {
        return (this.mAdapter instanceof ChooserListAdapter) && ((ChooserListAdapter) this.mAdapter).getPositionTargetType(i) == 1;
    }

    private boolean checkExactMatch(String str, String str2) {
        return StringReplace(str).equals(StringReplace(str2));
    }

    private boolean checkTargetHasRule(String str) {
        if (!(str == null || this.mPkgsWithRule == null)) {
            for (Object equals : this.mPkgsWithRule) {
                if (str.equals(equals)) {
                    Log.d(TAG, "pkg=" + str + " has callee rule");
                    return true;
                }
            }
        }
        Log.d(TAG, "no pkg has callee rule");
        return false;
    }

    private boolean getCandidates() {
        if (this.mPackage != null && this.mLabel != null) {
            return getCandidatesFromPackageLabel();
        }
        if (this.mPackage != null && this.mLabel == null) {
            return getCandidatesFromPackage();
        }
        if (this.mLabel != null && this.mPackage == null) {
            return getCandidatesFromLabel();
        }
        Log.d(TAG, "Package and Label is null, No candidate!!");
        return false;
    }

    private boolean getCandidatesByPackageName(String str) {
        if (str == null) {
            Log.d(TAG, "getCandidatesByPackageName: pkg is null");
            return false;
        }
        if (SShareConstants.SIMPLE_SHARING_PKG.equals(str)) {
            this.mCandidatesByPkg.put(this.mFeature.getMenuName(0), Integer.valueOf(-1));
        } else if (SShareConstants.QUICK_CONNECT_PKG.equals(str)) {
            this.mCandidatesByPkg.put(this.mFeature.getMenuName(1), Integer.valueOf(-2));
        } else if (SShareConstants.SCREEN_MIRRORING_PKG.equals(str) || SShareConstants.SCREEN_MIRRORING_PKG_DREAM.equals(str)) {
            this.mCandidatesByPkg.put(this.mFeature.getMenuName(2), Integer.valueOf(-3));
        }
        if (this.mAdapter != null) {
            int count = this.mAdapter.getCount();
            int i = 0;
            while (i < count) {
                if (!checkDirectShareItem(i) && this.mAdapter.getItem(i).getResolveInfo().activityInfo.packageName.equals(str)) {
                    Object obj = null;
                    String charSequence = this.mAdapter.getItem(i).getDisplayLabel().toString();
                    for (int i2 = 0; i2 < this.mCandidatesByPkg.size(); i2++) {
                        if (this.mCandidatesByPkg.containsKey(charSequence)) {
                            Log.d(TAG, "already name " + charSequence + " is in candidate list");
                            obj = 1;
                            break;
                        }
                    }
                    if (obj == null) {
                        this.mCandidatesByPkg.put(charSequence, Integer.valueOf(i));
                    }
                }
                i++;
            }
        }
        Log.d(TAG, "mCandidatesByPkg = " + this.mCandidatesByPkg);
        return true;
    }

    private boolean getCandidatesFromLabel() {
        return setMatchedTargetByAppName(this.mLabel);
    }

    private boolean getCandidatesFromPackage() {
        return getCandidatesByPackageName(this.mPackage);
    }

    private boolean getCandidatesFromPackageLabel() {
        getCandidatesByPackageName(this.mPackage);
        return setMatchedTargetByAppName(this.mLabel);
    }

    private int getDistance(String str, String str2) {
        int i;
        str = StringReplace(str);
        str2 = StringReplace(str2);
        int length = str.length() + 1;
        int length2 = str2.length() + 1;
        int[] iArr = new int[length];
        int[] iArr2 = new int[length];
        for (i = 0; i < length; i++) {
            iArr[i] = i;
        }
        for (int i2 = 1; i2 < length2; i2++) {
            iArr2[0] = i2;
            for (i = 1; i < length; i++) {
                int i3 = 0;
                if (str.charAt(i - 1) != str2.charAt(i2 - 1)) {
                    i3 = 1;
                }
                iArr2[i] = Math.min(Math.min(iArr[i] + 1, iArr2[i - 1] + 1), iArr[i - 1] + i3);
            }
            int[] iArr3 = iArr;
            iArr = iArr2;
            iArr2 = iArr3;
        }
        return iArr[length - 1];
    }

    private void initializeVariables() {
        this.mSlotAppName = null;
        this.mMatchedAppType = 3;
        this.mMatchedAppIndex = -100;
        this.mDuplicateLabelIndex.clear();
        this.mCandidatesByPkg.clear();
        this.mPackage = null;
        this.mLabel = null;
    }

    private boolean isValidDistance(int i) {
        if (i <= 3) {
            return true;
        }
        Log.d(TAG, "no valid");
        return false;
    }

    private boolean parsingTargetInfoByPackageName(String str) {
        boolean z = false;
        if (str == null) {
            Log.d(TAG, "parsingTargetInfoByPackageName: pkg is null");
            return false;
        }
        if (SShareConstants.SIMPLE_SHARING_PKG.equals(str)) {
            setMatchedTargetInfo(this.mFeature.getMenuName(0), -1);
            z = true;
        } else if (SShareConstants.QUICK_CONNECT_PKG.equals(str)) {
            setMatchedTargetInfo(this.mFeature.getMenuName(1), -2);
            z = true;
        } else if (SShareConstants.SCREEN_MIRRORING_PKG.equals(str) || SShareConstants.SCREEN_MIRRORING_PKG_DREAM.equals(str)) {
            setMatchedTargetInfo(this.mFeature.getMenuName(2), -3);
            z = true;
        } else if (this.mAdapter != null) {
            int count = this.mAdapter.getCount();
            int i = 0;
            while (i < count) {
                if (!checkDirectShareItem(i) && this.mAdapter.getItem(i).getResolveInfo().activityInfo.packageName.equals(str)) {
                    this.mCandidatesByPkg.put(this.mAdapter.getItem(i).getDisplayLabel().toString(), Integer.valueOf(i));
                }
                i++;
            }
            Iterator it;
            String str2;
            if (this.mCandidatesByPkg.size() == 1) {
                it = this.mCandidatesByPkg.keySet().iterator();
                if (it.hasNext()) {
                    str2 = (String) it.next();
                    setMatchedTargetInfo(str2, ((Integer) this.mCandidatesByPkg.get(str2)).intValue());
                    z = true;
                }
            } else if (!(this.mClass == null || this.mClass.isEmpty() || this.mCandidatesByPkg.size() <= 1)) {
                String shortClassName = getShortClassName(str, this.mClass);
                for (String str22 : this.mCandidatesByPkg.keySet()) {
                    PackageItemInfo packageItemInfo = this.mAdapter.getItem(((Integer) this.mCandidatesByPkg.get(str22)).intValue()).getResolveInfo().activityInfo;
                    String shortClassName2 = getShortClassName(packageItemInfo.packageName, packageItemInfo.name);
                    if (shortClassName != null && shortClassName.equalsIgnoreCase(shortClassName2)) {
                        setMatchedTargetInfo(str22, ((Integer) this.mCandidatesByPkg.get(str22)).intValue());
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    private void removeReceiverForBixby() {
        this.mContext.unregisterReceiver(this.mBixbyGetDataReceiver);
    }

    private void sendStateCommandResponse(boolean z) {
        if (this.mExecutorCommandHandler == null) {
            Log.e(TAG, "sendResponse: mExecutorCommandHandler is null!!!");
            return;
        }
        if (z) {
            this.mExecutorCommandHandler.sendStateCommandResponse(ExecutorCommandHandler.RESULT_SUCCESS);
        } else {
            this.mExecutorCommandHandler.sendStateCommandResponse(ExecutorCommandHandler.RESULT_FAILURE);
        }
    }

    private boolean setMatchedTargetByAppName(String str) {
        boolean z = false;
        Iterator it;
        String str2;
        if (str != null) {
            if (this.mCandidatesByPkg.size() > 0) {
                for (String str22 : this.mCandidatesByPkg.keySet()) {
                    if (checkExactMatch(str, str22)) {
                        this.mDuplicateLabelIndex.add((Integer) this.mCandidatesByPkg.get(str22));
                    }
                }
            } else {
                if (checkExactMatch(this.mFeature.getMenuName(0), str)) {
                    this.mDuplicateLabelIndex.add(Integer.valueOf(-1));
                } else if (checkExactMatch(this.mFeature.getMenuName(1), str)) {
                    this.mDuplicateLabelIndex.add(Integer.valueOf(-2));
                } else if (checkExactMatch(StringReplaceSmartView(this.mFeature.getMenuName(2)), str) || checkExactMatch(SShareConstants.SURVEY_DETAIL_FEATURE_SCREEN_SHARING, str)) {
                    this.mDuplicateLabelIndex.add(Integer.valueOf(-3));
                }
                if (this.mAdapter != null) {
                    int count = this.mAdapter.getCount();
                    int i = 0;
                    while (i < count) {
                        if (!checkDirectShareItem(i) && checkExactMatch(str, this.mAdapter.getItem(i).getDisplayLabel().toString())) {
                            this.mDuplicateLabelIndex.add(Integer.valueOf(i));
                        }
                        i++;
                    }
                }
                if (this.mDuplicateLabelIndex.size() == 1) {
                    setMatchedTargetInfo(str, ((Integer) this.mDuplicateLabelIndex.get(0)).intValue());
                    z = true;
                } else {
                    z = false;
                }
            }
        } else if (this.mCandidatesByPkg.size() == 1) {
            Log.d(TAG, "no label.. but only one candidate");
            it = this.mCandidatesByPkg.keySet().iterator();
            if (it.hasNext()) {
                str22 = (String) it.next();
                setMatchedTargetInfo(str22, ((Integer) this.mCandidatesByPkg.get(str22)).intValue());
                z = true;
            }
        }
        if (z) {
            Log.d(TAG, "matched by label: type=" + this.mMatchedAppType + " label=" + this.mMatchedAppLabel + " index=" + this.mMatchedAppIndex);
        }
        return z;
    }

    private boolean setMatchedTargetByDistance(String str, String str2, int i, int i2) {
        int distance = str.length() > str2.length() ? getDistance(str, str2) : getDistance(str2, str);
        if (i != -1 && distance >= i) {
            return false;
        }
        i = distance;
        setMatchedTargetInfo(str2, i2);
        return true;
    }

    private void splitPackageClassValue(String str) {
        if (str == null || !str.contains("/")) {
            this.mPackage = str;
        } else {
            String[] split = str.split("/");
            if (split.length == 2) {
                this.mPackage = split[0];
                this.mClass = split[1];
            } else {
                this.mPackage = split[0];
            }
        }
        Log.d(TAG, "mPackage=" + this.mPackage + " mClass=" + this.mClass);
    }

    private void splitSlotValue(String str) {
        if (str == null || !str.contains("_")) {
            this.mLabel = str;
        } else {
            String[] split = str.split("_");
            if (split.length == 2) {
                this.mPackage = split[0];
                this.mLabel = split[1];
            } else {
                this.mLabel = split[0];
            }
        }
        Log.d(TAG, "mPackage=" + this.mPackage + " mLabel=" + this.mLabel);
    }

    public void createExecutorCommandHandler() {
        this.mExecutorCommandHandler = ExecutorCommandHandler.createInstance(this.mContext, "ShareVia", new C02423());
    }

    public String getShortClassName(String str, String str2) {
        if (str2.startsWith(str)) {
            int length = str.length();
            int length2 = str2.length();
            if (length2 > length && str2.charAt(length) == '.') {
                return str2.substring(length, length2);
            }
        }
        return str2;
    }

    public void registerReceiverRequestCommand() {
        addReceiverForBixby();
        if (this.mExecutorCommandHandler != null) {
            ExecutorCommandHandler executorCommandHandler = this.mExecutorCommandHandler;
            ExecutorCommandHandler.requestStateCommand(this.mContext);
        }
    }

    public void sendAppSelectionForBixby(String str) {
        if (this.mIsBixbyDone) {
            Log.d(TAG, "selectedPackage is not done");
        } else if (this.mIsInBixbyState) {
            Log.d(TAG, "selectedPackage=" + str);
            Intent intent = new Intent(SShareConstants.EM_ACTION_CHOOSER_EVENT);
            intent.putExtra(SShareConstants.EXTRA_CHOOSER_EM_COMMAND, SShareConstants.EXTRA_CHOOSER_EM_APP_SELECTION);
            intent.putExtra(SShareConstants.EXTRA_CHOOSER_EM_SELECTED_PKG, str);
            this.mContext.sendBroadcast(intent);
            this.mIsBixbyDone = true;
            sendStateCommandResponse(true);
        } else {
            Log.d(TAG, "sendAppSelectionForBixby: not in Bixby state");
        }
    }

    public void sendCancelForBixby() {
        Log.d(TAG, "sendCancelForBixby");
        this.mIsBixbyDone = true;
        sendStateCommandResponse(false);
    }

    public void sendNlgRequest(int i) {
        NlgRequestInfo nlgRequestInfo = new NlgRequestInfo("ShareVia");
        Log.d(TAG, "nlgType=" + i);
        if (i == 0) {
            nlgRequestInfo.addScreenParam(SShareConstants.PARAM_ID_APPNAME, "Exist", "No");
        } else if (i == 1) {
            nlgRequestInfo.addScreenParam(SShareConstants.PARAM_ID_APPNAME, "Match", "No");
            nlgRequestInfo.addResultParam(SShareConstants.PARAM_ID_APPNAME, this.mSlotAppName);
        } else if (i == 2) {
            nlgRequestInfo.addScreenParam(SShareConstants.PARAM_ID_APPNAME, "Match", "multi");
            nlgRequestInfo.addResultParam("method_count", Integer.toString(this.mDuplicateLabelIndex.size()));
        }
        this.mExecutorCommandHandler.requestNlg("ShareVia", nlgRequestInfo);
    }

    public void setMatchedTargetInfo(String str, int i) {
        if (i == -1) {
            this.mMatchedAppType = 0;
        } else if (i == -2) {
            this.mMatchedAppType = 1;
        } else if (i == -3) {
            this.mMatchedAppType = 2;
        } else {
            this.mMatchedAppType = 3;
        }
        this.mMatchedAppIndex = i;
        this.mMatchedAppLabel = str;
    }

    public void unregisterReceiver() {
        removeReceiverForBixby();
        if (this.mIsInBixbyState) {
            if (!(this.mExecutorCommandHandler == null || this.mIsBixbyDone)) {
                sendStateCommandResponse(false);
            }
            return;
        }
        Log.d(TAG, "unregisterReceiver: not in Bixby state");
    }
}
