package org.apache.commons.lang;

import java.io.File;

public class SystemUtils {
    public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
    public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");
    public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");
    public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");
    public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");
    public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");
    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
    public static final boolean IS_OS_AIX = getOSMatches("AIX");
    public static final boolean IS_OS_HP_UX = getOSMatches("HP-UX");
    public static final boolean IS_OS_IRIX = getOSMatches("Irix");
    public static final boolean IS_OS_LINUX;
    public static final boolean IS_OS_MAC = getOSMatches("Mac");
    public static final boolean IS_OS_MAC_OSX = getOSMatches("Mac OS X");
    public static final boolean IS_OS_OS2 = getOSMatches("OS/2");
    public static final boolean IS_OS_SOLARIS = getOSMatches("Solaris");
    public static final boolean IS_OS_SUN_OS = getOSMatches("SunOS");
    public static final boolean IS_OS_UNIX;
    public static final boolean IS_OS_WINDOWS = getOSMatches(OS_NAME_WINDOWS_PREFIX);
    public static final boolean IS_OS_WINDOWS_2000 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.0");
    public static final boolean IS_OS_WINDOWS_95 = getOSMatches("Windows 9", "4.0");
    public static final boolean IS_OS_WINDOWS_98 = getOSMatches("Windows 9", "4.1");
    public static final boolean IS_OS_WINDOWS_ME = getOSMatches(OS_NAME_WINDOWS_PREFIX, "4.9");
    public static final boolean IS_OS_WINDOWS_NT = getOSMatches("Windows NT");
    public static final boolean IS_OS_WINDOWS_XP = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.1");
    public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
    public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
    public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
    public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
    public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
    public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
    public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
    public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);
    private static final String JAVA_HOME_KEY = "java.home";
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
    public static final String JAVA_VERSION = getSystemProperty("java.version");
    public static final float JAVA_VERSION_FLOAT = getJavaVersionAsFloat();
    public static final int JAVA_VERSION_INT = getJavaVersionAsInt();
    public static final String JAVA_VERSION_TRIMMED = getJavaVersionTrimmed();
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");
    public static final String OS_ARCH = getSystemProperty("os.arch");
    public static final String OS_NAME = getSystemProperty("os.name");
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    public static final String OS_VERSION = getSystemProperty("os.version");
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");
    public static final String USER_COUNTRY;
    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);
    private static final String USER_DIR_KEY = "user.dir";
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);
    private static final String USER_HOME_KEY = "user.home";
    public static final String USER_LANGUAGE = getSystemProperty("user.language");
    public static final String USER_NAME = getSystemProperty("user.name");
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    static {
        String systemProperty;
        boolean z = false;
        if (getSystemProperty("user.country") == null) {
            systemProperty = getSystemProperty("user.region");
        } else {
            systemProperty = getSystemProperty("user.country");
        }
        USER_COUNTRY = systemProperty;
        boolean z2 = getOSMatches("Linux") || getOSMatches("LINUX");
        IS_OS_LINUX = z2;
        if (IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX || IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS) {
            z = true;
        }
        IS_OS_UNIX = z;
    }

    public static float getJavaVersion() {
        return JAVA_VERSION_FLOAT;
    }

    private static float getJavaVersionAsFloat() {
        float f = 0.0f;
        if (JAVA_VERSION_TRIMMED != null) {
            String str = JAVA_VERSION_TRIMMED.substring(0, 3);
            if (JAVA_VERSION_TRIMMED.length() >= 5) {
                str = new StringBuffer().append(str).append(JAVA_VERSION_TRIMMED.substring(4, 5)).toString();
            }
            try {
                f = Float.parseFloat(str);
            } catch (Exception e) {
            }
        }
        return f;
    }

    private static int getJavaVersionAsInt() {
        int i = 0;
        if (JAVA_VERSION_TRIMMED != null) {
            String str = new StringBuffer().append(JAVA_VERSION_TRIMMED.substring(i, 1)).append(JAVA_VERSION_TRIMMED.substring(2, 3)).toString();
            if (JAVA_VERSION_TRIMMED.length() >= 5) {
                str = new StringBuffer().append(str).append(JAVA_VERSION_TRIMMED.substring(4, 5)).toString();
            } else {
                str = new StringBuffer().append(str).append("0").toString();
            }
            try {
                i = Integer.parseInt(str);
            } catch (Exception e) {
            }
        }
        return i;
    }

    private static String getJavaVersionTrimmed() {
        if (JAVA_VERSION != null) {
            for (int i = 0; i < JAVA_VERSION.length(); i++) {
                char ch = JAVA_VERSION.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    return JAVA_VERSION.substring(i);
                }
            }
        }
        return null;
    }

    private static boolean getJavaVersionMatches(String versionPrefix) {
        if (JAVA_VERSION_TRIMMED == null) {
            return false;
        }
        return JAVA_VERSION_TRIMMED.startsWith(versionPrefix);
    }

    private static boolean getOSMatches(String osNamePrefix) {
        if (OS_NAME == null) {
            return false;
        }
        return OS_NAME.startsWith(osNamePrefix);
    }

    private static boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
        if (OS_NAME == null || OS_VERSION == null || !OS_NAME.startsWith(osNamePrefix) || !OS_VERSION.startsWith(osVersionPrefix)) {
            return false;
        }
        return true;
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException e) {
            System.err.println(new StringBuffer().append("Caught a SecurityException reading the system property '").append(property).append("'; the SystemUtils property value will default to null.").toString());
            return null;
        }
    }

    public static boolean isJavaVersionAtLeast(float requiredVersion) {
        return JAVA_VERSION_FLOAT >= requiredVersion;
    }

    public static boolean isJavaVersionAtLeast(int requiredVersion) {
        return JAVA_VERSION_INT >= requiredVersion;
    }

    public static boolean isJavaAwtHeadless() {
        return JAVA_AWT_HEADLESS != null ? JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString()) : false;
    }

    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }
}
