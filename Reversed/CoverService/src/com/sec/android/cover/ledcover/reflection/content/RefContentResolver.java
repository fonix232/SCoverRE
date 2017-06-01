package com.sec.android.cover.ledcover.reflection.content;

import android.database.ContentObserver;
import android.net.Uri;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefContentResolver extends AbstractBaseReflection {
    private static RefContentResolver sInstance;

    public static synchronized RefContentResolver get() {
        RefContentResolver refContentResolver;
        synchronized (RefContentResolver.class) {
            if (sInstance == null) {
                sInstance = new RefContentResolver();
            }
            refContentResolver = sInstance;
        }
        return refContentResolver;
    }

    public void registerContentObserver(Object instance, Uri uri, boolean notifyForDescendents, Object observer, int userHandle) {
        invokeNormalMethod(instance, "registerContentObserver", new Class[]{Uri.class, Boolean.TYPE, ContentObserver.class, Integer.TYPE}, uri, Boolean.valueOf(notifyForDescendents), observer, Integer.valueOf(userHandle));
    }

    protected String getBaseClassName() {
        return "android.content.ContentResolver";
    }
}
