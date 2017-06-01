package com.samsung.android.contextaware.utilbundle.logger;

import android.util.Log;
import java.util.Calendar;

public class CaLogger {
    private static final String FILE_NAME = "CAELogger";
    public static final String TAG = "CAE";
    private static volatile CaLogger instance;
    private static boolean isCaller = true;
    private static boolean isConsoleLogging = true;
    private static boolean isFileLogging = false;
    private static boolean isGrayBoxTesting = false;
    private static int mLevel = Level.TRACE.ordinal();
    private static ILoggingObserver mLoggingObserver;

    public enum Level {
        TRACE {
            String consoleLogging(String str) {
                Object obj = CaLogger.isConsoleLogging ? CaLogger.mLevel <= ordinal() ? 1 : null : null;
                String -wrap0 = Level.getCallerInfo(false);
                if (obj != null) {
                    Log.v(CaLogger.TAG, -wrap0);
                }
                return -wrap0;
            }

            void fileLogging(String str) {
                boolean z = CaLogger.isFileLogging ? CaLogger.mLevel <= ordinal() : false;
                if (z) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("T", CaLogger.TAG, Level.getCallerInfo(false), str));
                }
            }
        },
        DEBUG {
            String consoleLogging(String str) {
                Object obj = CaLogger.isConsoleLogging ? CaLogger.mLevel <= ordinal() ? 1 : null : null;
                String str2 = Level.getCallerInfo(true) + str;
                if (obj != null) {
                    Log.d(CaLogger.TAG, str2);
                }
                return str2;
            }

            void fileLogging(String str) {
                boolean z = false;
                if (CaLogger.isFileLogging && CaLogger.mLevel <= ordinal()) {
                    z = true;
                }
                if (z) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("D", CaLogger.TAG, Level.getCallerInfo(true), str));
                }
            }
        },
        INFO {
            String consoleLogging(String str) {
                Object obj = CaLogger.isConsoleLogging ? CaLogger.mLevel <= ordinal() ? 1 : null : null;
                String str2 = Level.getCallerInfo(true) + str;
                if (obj != null) {
                    Log.i(CaLogger.TAG, str2);
                }
                return str2;
            }

            void fileLogging(String str) {
                boolean z = false;
                if (CaLogger.isFileLogging && CaLogger.mLevel <= ordinal()) {
                    z = true;
                }
                if (z) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("I", CaLogger.TAG, Level.getCallerInfo(true), str));
                }
            }
        },
        WARN {
            String consoleLogging(String str) {
                Object obj = CaLogger.isConsoleLogging ? CaLogger.mLevel <= ordinal() ? 1 : null : null;
                String str2 = Level.getCallerInfo(true) + str;
                if (obj != null) {
                    Log.w(CaLogger.TAG, str2);
                }
                return str2;
            }

            void fileLogging(String str) {
                boolean z = false;
                if (CaLogger.isFileLogging && CaLogger.mLevel <= ordinal()) {
                    z = true;
                }
                if (z) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("W", CaLogger.TAG, Level.getCallerInfo(true), str));
                }
            }
        },
        ERROR {
            String consoleLogging(String str) {
                Object obj = CaLogger.isConsoleLogging ? CaLogger.mLevel <= ordinal() ? 1 : null : null;
                String str2 = Level.getCallerInfo(true) + str;
                if (obj != null) {
                    Log.e(CaLogger.TAG, str2);
                }
                return str2;
            }

            void fileLogging(String str) {
                boolean z = false;
                if (CaLogger.isFileLogging && CaLogger.mLevel <= ordinal()) {
                    z = true;
                }
                if (z) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("E", CaLogger.TAG, Level.getCallerInfo(true), str));
                }
            }
        },
        EXCEPTION {
            String consoleLogging(String str) {
                Object obj = null;
                if (CaLogger.isConsoleLogging && CaLogger.mLevel <= ordinal()) {
                    obj = 1;
                }
                if (obj != null) {
                    Log.e(CaLogger.TAG, str);
                }
                return null;
            }

            void fileLogging(String str) {
                Object obj = null;
                if (CaLogger.isFileLogging && CaLogger.mLevel <= ordinal()) {
                    obj = 1;
                }
                if (obj != null) {
                    CaFileLogger.getInstance().logging(CaLogger.FILE_NAME, CaLogger.getFilePattern("X", CaLogger.TAG, "", str));
                }
            }
        };

        private static String getCallerInfo(boolean z) {
            if (!CaLogger.isCaller) {
                return "";
            }
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StringBuffer stringBuffer = new StringBuffer();
            if (stackTrace.length >= 4) {
                String stackTraceElement = stackTrace[6].toString();
                stringBuffer.append(stackTraceElement.substring(stackTraceElement.lastIndexOf(46, stackTraceElement.indexOf(40)) + 1));
            }
            if (z) {
                stringBuffer.append(" - ");
            }
            return stringBuffer.toString();
        }

        abstract String consoleLogging(String str);

        abstract void fileLogging(String str);
    }

    public static void debug(String str) {
        String consoleLogging = Level.DEBUG.consoleLogging(str);
        Level.DEBUG.fileLogging(str);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(consoleLogging);
        }
    }

    public static void error(String str) {
        String consoleLogging = Level.ERROR.consoleLogging(str);
        Level.ERROR.fileLogging(str);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(consoleLogging);
        }
    }

    public static void exception(Throwable th) {
        Level.EXCEPTION.consoleLogging(th.toString());
        Level.EXCEPTION.fileLogging(th.toString());
        StackTraceElement[] stackTrace = th.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            Level.EXCEPTION.consoleLogging(stackTrace[i].toString());
            Level.EXCEPTION.fileLogging(stackTrace[i].toString());
        }
        Throwable cause = th.getCause();
        if (cause != null) {
            exception(cause);
        }
    }

    private static String getFilePattern(String str, String str2, String str3, String str4) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        int i = instance.get(1);
        int i2 = instance.get(2) + 1;
        int i3 = instance.get(5);
        int i4 = instance.get(11);
        int i5 = instance.get(12);
        int i6 = instance.get(13);
        return String.format("[%4d-%02d-%02d %02d:%02d:%02d] [%s] [%s] %s %s", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), str, str2, str3, str4});
    }

    public static CaLogger getInstance() {
        if (instance == null) {
            synchronized (CaLogger.class) {
                if (instance == null) {
                    instance = new CaLogger();
                }
            }
        }
        return instance;
    }

    public static void info(String str) {
        String consoleLogging = Level.INFO.consoleLogging(str);
        Level.INFO.fileLogging(str);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(consoleLogging);
        }
    }

    public static void notifyLoggingObserver(String str) {
        if (mLoggingObserver != null) {
            mLoggingObserver.updateLogMessage(str);
        }
    }

    public static void registerLoggingObserver(ILoggingObserver iLoggingObserver) {
        mLoggingObserver = iLoggingObserver;
    }

    public static void setConsoleLoggingEnable(boolean z) {
        isConsoleLogging = z;
    }

    public static void setFileLoggingEnable(boolean z) {
        if (z ? CaFileLogger.getInstance().startLogging(FILE_NAME) : CaFileLogger.getInstance().stopLogging(FILE_NAME)) {
            isFileLogging = z;
        }
    }

    public static void setGrayBoxTestingEnable(boolean z) {
        isGrayBoxTesting = z;
    }

    public static void setLogOption(int i, boolean z) {
        mLevel = i;
        isCaller = z;
    }

    public static void trace() {
        String consoleLogging = Level.TRACE.consoleLogging("");
        Level.TRACE.fileLogging("");
        if (isGrayBoxTesting) {
            notifyLoggingObserver(consoleLogging);
        }
    }

    public static void unregisterLoggingObserver(ILoggingObserver iLoggingObserver) {
        mLoggingObserver = null;
    }

    public static void warning(String str) {
        String consoleLogging = Level.WARN.consoleLogging(str);
        Level.WARN.fileLogging(str);
        if (isGrayBoxTesting) {
            notifyLoggingObserver(consoleLogging);
        }
    }
}
