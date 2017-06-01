package com.samsung.android.database.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteClosable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.io.File;

public abstract class SemSQLiteSecureOpenHelper {
    private static final boolean DEBUG_STRICT_READONLY = false;
    private static final String TAG = SemSQLiteSecureOpenHelper.class.getSimpleName();
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private final DatabaseErrorHandler mErrorHandler;
    private final CursorFactory mFactory;
    private boolean mIsInitializing;
    private final String mName;
    private final int mNewVersion;

    public SemSQLiteSecureOpenHelper(Context context, String str, CursorFactory cursorFactory, int i) {
        this(context, str, cursorFactory, i, null);
    }

    public SemSQLiteSecureOpenHelper(Context context, String str, CursorFactory cursorFactory, int i, DatabaseErrorHandler databaseErrorHandler) {
        if (i < 1) {
            throw new IllegalArgumentException("Version must be >= 1, was " + i);
        }
        this.mContext = context;
        this.mName = str;
        this.mFactory = cursorFactory;
        this.mNewVersion = i;
        this.mErrorHandler = databaseErrorHandler;
    }

    public static final int changeDatabasePassword(SQLiteDatabase sQLiteDatabase, byte[] bArr) {
        return sQLiteDatabase.changeDBPassword(bArr);
    }

    public static final void convertToPlainDatabase(File file, File file2, byte[] bArr) throws Exception {
        SQLiteDatabase.convertToPlainDatabase(file, file2, bArr);
    }

    public static final void convertToSecureDatabase(File file, File file2, byte[] bArr) throws Exception {
        SQLiteDatabase.convertToSecureDatabase(file, file2, bArr);
    }

    private SQLiteDatabase getDatabaseLocked(boolean z, byte[] bArr) {
        if (this.mDatabase != null) {
            if (!this.mDatabase.isOpen()) {
                this.mDatabase = null;
            } else if (!(z && this.mDatabase.isReadOnly())) {
                return this.mDatabase;
            }
        }
        if (this.mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }
        SQLiteClosable sQLiteClosable = this.mDatabase;
        try {
            this.mIsInitializing = true;
            if (sQLiteClosable != null) {
                if (sQLiteClosable.isOpen()) {
                    sQLiteClosable.close();
                }
                sQLiteClosable = null;
            }
            if (this.mName == null) {
                sQLiteClosable = SQLiteDatabase.createSecureDatabase(null, bArr);
            } else {
                String path = this.mContext.getDatabasePath(this.mName).getPath();
                File parentFile = new File(path).getParentFile();
                if (!(parentFile == null || parentFile.exists())) {
                    if (parentFile.mkdirs()) {
                        Log.i(TAG, "Mkdir for databases directory is successfullly finished.");
                    } else {
                        Log.e(TAG, "Mkdir for databases directory is failed.");
                    }
                }
                sQLiteClosable = SQLiteDatabase.openSecureDatabase(path, this.mFactory, 268435456, this.mErrorHandler, bArr);
            }
            onConfigure(sQLiteClosable);
            if (sQLiteClosable != null) {
                int version = sQLiteClosable.getVersion();
                if (version != this.mNewVersion) {
                    if (sQLiteClosable.isReadOnly()) {
                        throw new SQLiteException("Can't upgrade read-only database from version " + sQLiteClosable.getVersion() + " to " + this.mNewVersion + ": " + this.mName);
                    }
                    sQLiteClosable.beginTransaction();
                    if (version == 0) {
                        onCreate(sQLiteClosable);
                    } else if (version > this.mNewVersion) {
                        onDowngrade(sQLiteClosable, version, this.mNewVersion);
                    } else {
                        onUpgrade(sQLiteClosable, version, this.mNewVersion);
                    }
                    sQLiteClosable.setVersion(this.mNewVersion);
                    sQLiteClosable.setTransactionSuccessful();
                    sQLiteClosable.endTransaction();
                }
                onOpen(sQLiteClosable);
                if (sQLiteClosable.isReadOnly()) {
                    Log.w(TAG, "Opened " + this.mName + " in read-only mode");
                }
            }
            this.mDatabase = sQLiteClosable;
            this.mIsInitializing = false;
            if (!(sQLiteClosable == null || sQLiteClosable == this.mDatabase)) {
                sQLiteClosable.close();
            }
            return sQLiteClosable;
        } catch (SQLiteException e) {
            throw e;
        } catch (Throwable th) {
            this.mIsInitializing = false;
            if (!(sQLiteClosable == null || sQLiteClosable == this.mDatabase)) {
                sQLiteClosable.close();
            }
        }
    }

    public synchronized void close() {
        if (this.mIsInitializing) {
            throw new IllegalStateException("Closed during initialization");
        } else if (this.mDatabase != null && this.mDatabase.isOpen()) {
            this.mDatabase.close();
            this.mDatabase = null;
        }
    }

    public String getDatabaseName() {
        return this.mName;
    }

    public SQLiteDatabase getReadableDatabase(byte[] bArr) {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(false, bArr);
        }
        return databaseLocked;
    }

    public SQLiteDatabase getWritableDatabase(byte[] bArr) {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(true, bArr);
        }
        return databaseLocked;
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
    }

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        throw new SQLiteException("Can't downgrade database from version " + i + " to " + i2);
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
    }

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);
}
