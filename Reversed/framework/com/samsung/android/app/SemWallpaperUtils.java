package com.samsung.android.app;

import android.app.wallpaperbackup.WallpaperBackupRestoreManager;
import android.content.Context;

public class SemWallpaperUtils {
    private SemWallpaperUtils() {
    }

    public static void startBackup(Context context, String str, String str2) {
        new WallpaperBackupRestoreManager().startBackupWallpaper(context, str, str2);
    }

    public static void startRestore(Context context, String str, String str2) {
        new WallpaperBackupRestoreManager().startRestoreWallpaper(context, str, str2);
    }
}
