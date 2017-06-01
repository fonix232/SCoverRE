package com.samsung.android.game;

import android.util.Slog;
import java.util.HashMap;
import java.util.Map.Entry;

public class GamePkgDataHelper {
    private static final float ASPECT_RATIO_16_9 = 1.7777778f;
    public static final int CPU_GPU_LEVEL_DEFAULT = 0;
    static final String TAG = "GamePkgDataHelper";
    private static GamePkgDataHelper mInstance = null;
    HashMap<String, GamePkgData> mGamePkgDataMap = new HashMap();

    public static class GamePkgData {
        private float mAspectRatio = GamePkgDataHelper.ASPECT_RATIO_16_9;
        private boolean mBlackSurfaceNeeded = false;
        private int mCpuLevel = 0;
        private String mGovernorSetting = null;
        private int mGpuLevel = 0;
        private String mPkgName = null;

        public GamePkgData(String str) {
            this.mPkgName = str;
        }

        public float getAspectRatio() {
            return this.mAspectRatio;
        }

        public int getCpuLevel() {
            return this.mCpuLevel;
        }

        public String getGovernorSetting() {
            return this.mGovernorSetting;
        }

        public int getGpuLevel() {
            return this.mGpuLevel;
        }

        public String getPkgName() {
            return this.mPkgName;
        }

        public boolean isBlackSurfaceNeeded() {
            return this.mBlackSurfaceNeeded;
        }

        public void setAspectRatio(float f) {
            this.mAspectRatio = f;
        }

        public void setBlackSurfaceNeeded(boolean z) {
            this.mBlackSurfaceNeeded = z;
        }

        public void setCpuLevel(int i) {
            this.mCpuLevel = i;
        }

        public void setGovernorSetting(String str) {
            this.mGovernorSetting = str;
        }

        public void setGpuLevel(int i) {
            this.mGpuLevel = i;
        }
    }

    private GamePkgDataHelper() {
    }

    public static synchronized GamePkgDataHelper getInstance() {
        GamePkgDataHelper gamePkgDataHelper;
        synchronized (GamePkgDataHelper.class) {
            if (mInstance == null) {
                mInstance = new GamePkgDataHelper();
            }
            gamePkgDataHelper = mInstance;
        }
        return gamePkgDataHelper;
    }

    public synchronized void clearAllGamePkgData() {
        Slog.d(TAG, "clearAllGamePkgData()");
        this.mGamePkgDataMap.clear();
    }

    public synchronized GamePkgData getGamePkgData(String str) {
        Slog.d(TAG, "getGamePkgData(). " + str);
        if (str == null) {
            return null;
        }
        return (GamePkgData) this.mGamePkgDataMap.get(str);
    }

    public synchronized void putGamePkgData(String str, GamePkgData gamePkgData) {
        if (!(str == null || gamePkgData == null)) {
            Slog.d(TAG, "putGamePkgData(). " + str);
            this.mGamePkgDataMap.put(str, gamePkgData);
        }
    }

    public synchronized void removeGamePkgData(String str) {
        if (str != null) {
            Slog.d(TAG, "removeGamePkgData(). " + str);
            this.mGamePkgDataMap.remove(str);
        }
    }

    public synchronized void showAllGamePkgDataInfo() {
        for (Entry entry : this.mGamePkgDataMap.entrySet()) {
            GamePkgData gamePkgData = (GamePkgData) entry.getValue();
            Slog.d(TAG, "key: " + ((String) entry.getKey()) + ", PkgName: " + gamePkgData.getPkgName() + ", cpuLevel: " + gamePkgData.getCpuLevel() + ", gpuLevel: " + gamePkgData.getGpuLevel() + ", governorSetting: " + gamePkgData.getGovernorSetting() + ", aspectRatio: " + gamePkgData.getAspectRatio() + ", blackSurface: " + gamePkgData.isBlackSurfaceNeeded());
        }
    }
}
