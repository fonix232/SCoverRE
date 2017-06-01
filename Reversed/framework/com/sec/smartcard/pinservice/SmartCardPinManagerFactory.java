package com.sec.smartcard.pinservice;

import android.content.Context;
import android.os.UserHandle;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class SmartCardPinManagerFactory {
    private static final String TAG = "SmartCardPinManagerFactory";
    private static SmartCardPinManagerFactory mScPinMgrFactory = null;
    private static Map<Integer, SmartCardPinManager> mScPinMgrMap = new HashMap();

    private SmartCardPinManagerFactory() {
    }

    public static SmartCardPinManagerFactory getInstance() {
        if (mScPinMgrFactory == null) {
            Log.i(TAG, "Creating a new factory");
            mScPinMgrFactory = new SmartCardPinManagerFactory();
        }
        Log.i(TAG, "Returning factory" + mScPinMgrFactory);
        return mScPinMgrFactory;
    }

    public void deinitializeCAC(UserHandle userHandle) {
        SmartCardPinManager smartCardPinManager = null;
        Log.i(TAG, "deinitializeCAC entered");
        if (userHandle != null) {
            Log.i(TAG, "deinitializeCAC: userHandle identifier: " + userHandle.getIdentifier());
            Integer num = new Integer(userHandle.getIdentifier());
            if (mScPinMgrMap.containsKey(num)) {
                Log.i(TAG, "deinitializeCAC: Already contains key for userHandle identifier: " + userHandle.getIdentifier());
                smartCardPinManager = (SmartCardPinManager) mScPinMgrMap.get(num);
            }
            if (smartCardPinManager != null) {
                smartCardPinManager.unbindSmartCardPinService();
                mScPinMgrMap.remove(num);
            }
        }
    }

    public SmartCardPinManager getSmartcardPinMgr(Context context, UserHandle userHandle) {
        SmartCardPinManager smartCardPinManager = null;
        Log.i(TAG, "getSmartcardPinMgr entered");
        if (userHandle == null || context == null) {
            return null;
        }
        Log.i(TAG, "getSmartcardPinMgr: userHandle identifier: " + userHandle.getIdentifier() + " and context: " + context);
        Integer num = new Integer(userHandle.getIdentifier());
        if (mScPinMgrMap.containsKey(num)) {
            Log.i(TAG, "getSmartcardPinMgr: Already contains key for userHandle identifier: " + userHandle.getIdentifier());
            smartCardPinManager = (SmartCardPinManager) mScPinMgrMap.get(num);
        }
        if (smartCardPinManager == null) {
            Log.i(TAG, "getSmartcardPinMgr: Pin mgr for userHandle: " + userHandle.getIdentifier() + "not in map");
            smartCardPinManager = new SmartCardPinManager(userHandle, context, 0);
            mScPinMgrMap.put(num, smartCardPinManager);
        }
        Log.i(TAG, "getSmartcardPinMgr: Returning smartcard pin mgr: " + smartCardPinManager + " for user handle: " + userHandle.getIdentifier());
        return smartCardPinManager;
    }

    public SmartCardPinManager getSmartcardPinMgr_Sync(Context context, UserHandle userHandle) {
        SmartCardPinManager smartCardPinManager = null;
        Log.i(TAG, "getSmartcardPinMgr_Sync entered");
        if (userHandle == null || context == null) {
            return null;
        }
        Log.i(TAG, "getSmartcardPinMgr: userHandle identifier: " + userHandle.getIdentifier() + " and context: " + context);
        Integer num = new Integer(userHandle.getIdentifier());
        if (mScPinMgrMap.containsKey(num)) {
            Log.i(TAG, "getSmartcardPinMgr: Already contains key for userHandle identifier: " + userHandle.getIdentifier());
            smartCardPinManager = (SmartCardPinManager) mScPinMgrMap.get(num);
        }
        if (smartCardPinManager == null) {
            Log.i(TAG, "getSmartcardPinMgr: Pin mgr for userHandle: " + userHandle.getIdentifier() + "not in map");
            smartCardPinManager = new SmartCardPinManager(userHandle, context, 1);
            smartCardPinManager.bindSmartCardPinService_Sync(userHandle);
            mScPinMgrMap.put(num, smartCardPinManager);
        }
        Log.i(TAG, "getSmartcardPinMgr: Returning smartcard pin mgr: " + smartCardPinManager + " for user handle: " + userHandle.getIdentifier());
        return smartCardPinManager;
    }
}
