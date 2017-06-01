package com.sec.android.cover.ledcover.reflection.service;

import android.content.ComponentName;
import android.content.Context;
import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefNotificationListenerService extends AbstractBaseReflection {
    private static RefNotificationListenerService sInstance;

    public static synchronized RefNotificationListenerService get() {
        RefNotificationListenerService refNotificationListenerService;
        synchronized (RefNotificationListenerService.class) {
            if (sInstance == null) {
                sInstance = new RefNotificationListenerService();
            }
            refNotificationListenerService = sInstance;
        }
        return refNotificationListenerService;
    }

    public void registerAsSystemService(Object instance, Context context, ComponentName componentName, int currentUser) {
        invokeNormalMethod(instance, "registerAsSystemService", new Class[]{Context.class, ComponentName.class, Integer.TYPE}, context, componentName, Integer.valueOf(currentUser));
    }

    public void unregisterAsSystemService(Object instance) {
        invokeNormalMethod(instance, "unregisterAsSystemService");
    }

    protected String getBaseClassName() {
        return "android.service.notification.NotificationListenerService";
    }
}
