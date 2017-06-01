package com.samsung.android.knox;

import android.app.Command;
import android.content.Context;
import android.content.CustomCursor;
import android.content.ICommandExeCallBack;
import android.content.IProviderCallBack;
import android.content.IRCPGlobalContactsDir;
import android.content.IRCPInterface;
import android.content.ISyncCallBack;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IRunnableCallback;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;

public class SemRemoteContentManager {
    public static final int ERROR = -333;
    private static final String TAG = "SemRemoteContentManager";
    ISemRemoteContentManager mService;

    public SemRemoteContentManager(ISemRemoteContentManager iSemRemoteContentManager) {
        this.mService = iSemRemoteContentManager;
    }

    public void cancelCopyChunks(long j) throws RemoteException {
        if (this.mService != null) {
            this.mService.cancelCopyChunks(j);
        }
    }

    public int changePermissionMigration(String str, int i, int i2, int i3) throws RemoteException {
        return this.mService != null ? this.mService.changePermissionMigration(str, i, i2, i3) : -1;
    }

    public int copyChunks(int i, String str, int i2, String str2, long j, int i3, long j2, boolean z) throws RemoteException {
        return this.mService != null ? this.mService.copyChunks(i, str, i2, str2, j, i3, j2, z) : ERROR;
    }

    public int copyFile(int i, String str, int i2, String str2) throws RemoteException {
        if (this.mService == null) {
            return -1;
        }
        Log.d(TAG, "copyFile: srcContainerId" + i + " srcFilePath" + str + " destContainerId" + i2 + " destFilePath" + str2);
        return this.mService.copyFile(i, str, i2, str2);
    }

    public int copyFileInternal(int i, String str, int i2, String str2) throws RemoteException {
        return this.mService != null ? this.mService.copyFileInternal(i, str, i2, str2) : -1;
    }

    public boolean deleteFile(String str, int i) throws RemoteException {
        return this.mService != null ? this.mService.deleteFile(str, i) : false;
    }

    public void doSyncForSyncer(String str, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "doSyncForSyncer, SyncerName " + str + " , providerID :" + i);
                this.mService.doSyncForSyncer(str, i);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }

    public Bundle exchangeData(Context context, int i, Bundle bundle) throws RemoteException {
        if (this.mService == null) {
            return null;
        }
        return this.mService.exchangeData(context.getPackageName(), i, bundle);
    }

    public void executeCommandForPersona(Command command) {
        if (this.mService != null) {
            try {
                this.mService.executeCommandForPersona(command);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to switch persona.", e);
                e.printStackTrace();
            }
        }
    }

    public CustomCursor getCallerInfo(String str) {
        if (this.mService != null) {
            try {
                return this.mService.getCallerInfo(str);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to get getCallerInfo(). ", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bundle getFileInfo(String str, int i) throws RemoteException {
        return this.mService != null ? this.mService.getFileInfo(str, i) : null;
    }

    public List<String> getFiles(String str, int i) throws RemoteException {
        return this.mService != null ? this.mService.getFiles(str, i) : null;
    }

    public IRCPInterface getRCPInterface() {
        if (this.mService != null) {
            try {
                return this.mService.getRCPInterface();
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to get RCPInterface from getRCPInterface().", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public IRCPGlobalContactsDir getRCPProxy() {
        if (this.mService != null) {
            try {
                return this.mService.getRCPProxy();
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to get IRCPGlobalContactsDir from getRCPProxy().", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public void handleShortcut(int i, String str, String str2, Bitmap bitmap, String str3, String str4) {
        Log.d(TAG, " in createShortcut() for packageName: " + str + " userId" + i);
        if (this.mService != null) {
            try {
                this.mService.handleShortcut(i, str, str2, bitmap, str3, str4);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to get createShortcut(). ", e);
                e.printStackTrace();
            }
        }
    }

    public boolean isFileExist(String str, int i) throws RemoteException {
        return this.mService != null ? this.mService.isFileExist(str, i) : false;
    }

    public int moveFile(int i, String str, int i2, String str2) throws RemoteException {
        return this.mService != null ? this.mService.moveFile(i, str, i2, str2) : -1;
    }

    public long moveFiles(int i, List<String> list, List<String> list2, int i2) throws RemoteException {
        if (i < 0) {
            Log.d(TAG, "Invalid App Id : " + i);
            return -1;
        } else if (list == null || (list != null && list.size() == 0)) {
            Log.d(TAG, "invalid srcFilePaths");
            return -1;
        } else if (list2 != null && (list2 == null || list2.size() != 0)) {
            return this.mService != null ? this.mService.moveFilesForAppEx(i, list, list2, i2) : -1;
        } else {
            Log.d(TAG, "invalid destFilePaths");
            return -1;
        }
    }

    public long moveFilesForApp(int i, List<String> list, List<String> list2) throws RemoteException {
        return this.mService != null ? this.mService.moveFilesForApp(i, list, list2) : -1;
    }

    public List<CustomCursor> queryAllProviders(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                return this.mService.queryAllProviders(str, str2, i, strArr, str3, strArr2, str4);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to query provider  queryAllProviders()", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public CustomCursor queryProvider(String str, String str2, int i, String[] strArr, String str3, String[] strArr2, String str4) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                return this.mService.queryProvider(str, str2, i, strArr, str3, strArr2, str4);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to query provider  queryProvider", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    public void registerCommandExe(ICommandExeCallBack iCommandExeCallBack, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                if (iCommandExeCallBack != null) {
                    this.mService.registerCommandExe(iCommandExeCallBack, i);
                } else {
                    Log.d(TAG, "registerCommandExe callback object is null!");
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to register command executor callback registerSync", e);
                e.printStackTrace();
            }
        }
    }

    public boolean registerExchangeData(Context context, IRunnableCallback iRunnableCallback, int i) throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.registerExchangeData(context.getPackageName(), iRunnableCallback, i);
    }

    public boolean registerMonitorCb(Context context, IRunnableCallback iRunnableCallback) throws RemoteException {
        if (this.mService == null) {
            return true;
        }
        return this.mService.registerMonitorCb(context.getPackageName(), iRunnableCallback);
    }

    public void registerObserver(String str, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "registerObserver, SyncerName " + str + " ,userId :" + i);
                this.mService.registerObserver(str, i);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }

    public void registerProvider(String str, IProviderCallBack iProviderCallBack, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                this.mService.registerProvider(str, iProviderCallBack, i);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to register provider callback registerProvider", e);
                e.printStackTrace();
            }
        }
    }

    public void registerRCPGlobalContactsDir(IRCPGlobalContactsDir iRCPGlobalContactsDir, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                this.mService.registerRCPGlobalContactsDir(iRCPGlobalContactsDir, i);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }

    public void registerRCPInterface(IRCPInterface iRCPInterface, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "registerRCPInterface(): My Context is " + this);
                this.mService.registerRCPInterface(iRCPInterface, i);
            } catch (Throwable e) {
                Log.e(TAG, "registerRCPInterface: RemoteException trying to register rcpInterface", e);
                e.printStackTrace();
            }
        }
    }

    public void registerSync(ISyncCallBack iSyncCallBack, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "My Context is " + this);
                this.mService.registerSync(iSyncCallBack, i);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to register sync callback registerSync", e);
                e.printStackTrace();
            }
        }
    }

    public void switchPersona(int i) {
        if (this.mService != null) {
            try {
                this.mService.switchPersona(i);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to switch persona.", e);
                e.printStackTrace();
            }
        }
    }

    public void unRegisterObserver(String str, int i) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "registerObserver, SyncerName " + str + " ,userId :" + i);
                this.mService.unRegisterObserver(str, i);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException trying to register globalContactsDir", e);
                e.printStackTrace();
            }
        }
    }
}
