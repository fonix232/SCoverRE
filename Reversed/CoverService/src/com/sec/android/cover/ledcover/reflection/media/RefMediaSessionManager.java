package com.sec.android.cover.ledcover.reflection.media;

import android.content.ComponentName;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener;
import android.os.Handler;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;
import java.util.List;

public class RefMediaSessionManager extends AbstractBaseReflection {
    private static RefMediaSessionManager sInstance;

    public static synchronized RefMediaSessionManager get() {
        RefMediaSessionManager refMediaSessionManager;
        synchronized (RefMediaSessionManager.class) {
            if (sInstance == null) {
                sInstance = new RefMediaSessionManager();
            }
            refMediaSessionManager = sInstance;
        }
        return refMediaSessionManager;
    }

    public List<MediaController> getActiveSessionsForUser(Object instance, ComponentName notificationListener, int userId) {
        Object result = invokeNormalMethod(instance, "getActiveSessionsForUser", new Class[]{ComponentName.class, Integer.TYPE}, notificationListener, Integer.valueOf(userId));
        return result == null ? null : (List) result;
    }

    public void addOnActiveSessionsChangedListener(Object instance, OnActiveSessionsChangedListener sessionListener, ComponentName notificationListener, int userId, Handler handler) {
        invokeNormalMethod(instance, "addOnActiveSessionsChangedListener", new Class[]{OnActiveSessionsChangedListener.class, ComponentName.class, Integer.TYPE, Handler.class}, sessionListener, notificationListener, Integer.valueOf(userId), handler);
    }

    protected String getBaseClassName() {
        return "android.media.session.MediaSessionManager";
    }
}
