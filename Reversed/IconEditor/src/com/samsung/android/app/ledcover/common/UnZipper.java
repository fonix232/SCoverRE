package com.samsung.android.app.ledcover.common;

import com.samsung.android.app.ledcover.fota.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZipper {
    private static final String TAG = "UnZipper";

    public static ArrayList<String> unzip(File zipFile, File destination) throws IOException {
        Throwable th;
        SLog.m12v(TAG, "unzip started");
        ArrayList<String> unzipFiles = new ArrayList();
        long START_TIME = System.currentTimeMillis();
        FileOutputStream fos = null;
        ZipInputStream zin = null;
        try {
            ZipInputStream zin2 = new ZipInputStream(new FileInputStream(zipFile));
            try {
                String workingDir = destination.getAbsolutePath() + "/";
                byte[] buffer = new byte[Constants.DOWNLOAD_BUFFER_SIZE];
                FileOutputStream fos2 = null;
                while (true) {
                    try {
                        ZipEntry entry = zin2.getNextEntry();
                        if (entry == null) {
                            break;
                        } else if (entry.isDirectory()) {
                            File dir = new File(workingDir, entry.getName());
                            if (!dir.exists()) {
                                dir.mkdir();
                            }
                            SLog.m12v(TAG, "[DIR] " + entry.getName());
                        } else {
                            fos = new FileOutputStream(workingDir + entry.getName());
                            while (true) {
                                int bytesRead = zin2.read(buffer);
                                if (bytesRead == -1) {
                                    break;
                                }
                                fos.write(buffer, 0, bytesRead);
                            }
                            fos.close();
                            SLog.m12v(TAG, "[FILE] " + workingDir + entry.getName());
                            unzipFiles.add(workingDir + entry.getName());
                            fos2 = fos;
                        }
                    } catch (Exception e) {
                        zin = zin2;
                        fos = fos2;
                    } catch (Throwable th2) {
                        th = th2;
                        zin = zin2;
                        fos = fos2;
                    }
                }
                long ELAPSED_TIME = System.currentTimeMillis() - START_TIME;
                SLog.m12v(TAG, "COMPLETED in " + (ELAPSED_TIME / 1000) + " seconds.");
                if (fos2 != null) {
                    try {
                        fos2.close();
                    } catch (IOException e2) {
                        SLog.m12v(TAG, "IO Exception while close FileInputStream");
                        zin = zin2;
                        fos = fos2;
                    }
                }
                if (zin2 != null) {
                    zin2.close();
                }
                zin = zin2;
                fos = fos2;
            } catch (Exception e3) {
                zin = zin2;
            } catch (Throwable th3) {
                th = th3;
                zin = zin2;
            }
        } catch (Exception e4) {
            try {
                SLog.m12v(TAG, "FAILED");
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e5) {
                        SLog.m12v(TAG, "IO Exception while close FileInputStream");
                    }
                }
                if (zin != null) {
                    zin.close();
                }
                return unzipFiles;
            } catch (Throwable th4) {
                th = th4;
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e6) {
                        SLog.m12v(TAG, "IO Exception while close FileInputStream");
                        throw th;
                    }
                }
                if (zin != null) {
                    zin.close();
                }
                throw th;
            }
        }
        return unzipFiles;
    }
}
