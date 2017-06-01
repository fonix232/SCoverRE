package com.samsung.android.game;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.samsung.android.game.IGameManagerService.Stub;
import java.util.List;
import java.util.Map;

public class SemGameManager {
    public static final int MODE_CUSTOM = 4;
    public static final int MODE_EXTREME_SAVING = 3;
    public static final int MODE_POWER_SAVING = 2;
    public static final int MODE_SMART = 5;
    public static final int MODE_STANDARD = 1;
    public static final int MODE_UNMANAGED = 0;
    private static final String TAG = "SemGameManager";
    private IGameManagerService mService = null;

    public SemGameManager() {
        IBinder service = ServiceManager.getService("gamemanager");
        if (service != null) {
            this.mService = Stub.asInterface(service);
        }
    }

    public static boolean isAvailable() {
        return ServiceManager.getService("gamemanager") != null;
    }

    public static boolean isGamePackage(String str) throws IllegalStateException {
        IBinder service = ServiceManager.getService("gamemanager");
        if (service == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            int identifyGamePackage = Stub.asInterface(service).identifyGamePackage(str);
            if (identifyGamePackage != -1) {
                return identifyGamePackage == 1;
            } else {
                throw new IllegalStateException("gamemanager system service is not initialized yet");
            }
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public boolean addGame(String str, boolean z) throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.addGame(str, z);
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public void cancelDeathRestart(IBinder iBinder) throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            this.mService.cancelDeathRestart(iBinder);
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public String getForegroundApp() throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.getForegroundApp();
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public List<String> getGameList() throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.getGameList();
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public int getMode() throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.getMode();
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public String getVersion() throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.getVersion();
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public boolean init(int i, Map map) throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.initGameManager(i, map);
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public boolean isForegroundGame() throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            int identifyForegroundApp = this.mService.identifyForegroundApp();
            if (identifyForegroundApp != -1) {
                return identifyForegroundApp == 1;
            } else {
                throw new IllegalStateException("gamemanager system service is not initialized yet");
            }
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public void requestDeathRestart(IBinder iBinder, Intent intent) throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            this.mService.requestDeathRestart(iBinder, intent);
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public String requestWithJson(String str, String str2) throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.requestWithJson(str, str2);
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }

    public boolean setMode(int i) throws IllegalStateException {
        if (this.mService == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            return this.mService.setMode(i);
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }
}
