package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class ContentObserverProvider extends AndroidProvider {
    private ContentResolver mContentResolver;

    protected ContentObserverProvider(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
    }

    public void disable() {
        unregisterContentObserver();
    }

    public void enable() {
        registerContentObserver();
    }

    protected abstract ContentObserver getContentObserver();

    protected final ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public String[] getContextValueNames() {
        return new String[]{"Action"};
    }

    protected abstract Uri getUri();

    protected void initializeManager() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null");
        } else {
            this.mContentResolver = super.getContext().getContentResolver();
        }
    }

    protected boolean isNotifyForDescendents() {
        return true;
    }

    protected final void registerContentObserver() {
        if (super.getContext() != null && getUri() != null && getContentObserver() != null) {
            this.mContentResolver.registerContentObserver(getUri(), isNotifyForDescendents(), getContentObserver());
        }
    }

    protected void terminateManager() {
        this.mContentResolver = null;
    }

    protected final void unregisterContentObserver() {
        if (this.mContentResolver != null && getContentObserver() != null) {
            this.mContentResolver.unregisterContentObserver(getContentObserver());
        }
    }

    protected void updateContext(int i) {
        getContextBean().putContext(getContextValueNames()[0], i);
        notifyObserver();
    }
}
