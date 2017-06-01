package com.samsung.android.graphics.spr.cache;

import android.graphics.Bitmap;
import android.util.Log;
import com.samsung.android.graphics.spr.document.debug.SprDebug;
import java.util.ArrayList;

public class SprCacheManager {
    private ArrayList<SprCache> mCacheList = new ArrayList();
    private String mHashCode = null;
    private String mName = null;

    private static class SprCache {
        public final Bitmap bitmap;
        public final int dpi;
        public final int height;
        public int refCount = 0;
        public final int width;

        public SprCache(Bitmap bitmap, int i) {
            this.bitmap = bitmap;
            this.width = this.bitmap.getWidth();
            this.height = this.bitmap.getHeight();
            this.dpi = i;
            this.refCount = 0;
        }

        public synchronized void lock() {
            this.refCount++;
        }

        public synchronized void unlock() {
            this.refCount--;
        }
    }

    public SprCacheManager(String str, int i) {
        this.mName = str;
        this.mHashCode = String.valueOf(i % 10000);
    }

    public void addCache(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        synchronized (this.mCacheList) {
            Object obj = null;
            for (SprCache sprCache : this.mCacheList) {
                if (sprCache.width == width && sprCache.height == height && sprCache.dpi == i) {
                    obj = 1;
                    break;
                }
            }
            if (obj == null) {
                this.mCacheList.add(new SprCache(bitmap, i));
            }
        }
    }

    public Bitmap getCache(int i, int i2, int i3) {
        Bitmap bitmap = null;
        synchronized (this.mCacheList) {
            for (SprCache sprCache : this.mCacheList) {
                if (sprCache.width == i && sprCache.height == i2 && sprCache.dpi == i3) {
                    bitmap = sprCache.bitmap;
                    break;
                }
            }
        }
        return bitmap;
    }

    public void lock(Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (this.mCacheList) {
                for (SprCache sprCache : this.mCacheList) {
                    if (sprCache.bitmap == bitmap) {
                        sprCache.lock();
                        break;
                    }
                }
            }
            if (SprDebug.IsDebug) {
                Log.d("SprDrawable", "-lock--------------------------");
                printDebug();
            }
        }
    }

    public void printDebug() {
        synchronized (this.mCacheList) {
            Log.d("SprDrawable", this.mName + "(" + this.mHashCode + ")" + " printDebug start");
            for (SprCache sprCache : this.mCacheList) {
                Log.d("SprDrawable", this.mName + "(" + this.mHashCode + ")" + "Cache (" + sprCache.width + ", " + sprCache.height + "[" + sprCache.dpi + "]) " + sprCache.refCount);
            }
            Log.d("SprDrawable", this.mName + "(" + this.mHashCode + ")" + " printDebug end");
        }
    }

    public void unlock(Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (this.mCacheList) {
                for (SprCache sprCache : this.mCacheList) {
                    if (sprCache.bitmap == bitmap) {
                        sprCache.unlock();
                        if (sprCache.refCount == 0) {
                            this.mCacheList.remove(sprCache);
                        }
                    }
                }
            }
            if (SprDebug.IsDebug) {
                Log.d("SprDrawable", "-unlock------------------------");
                printDebug();
            }
        }
    }
}
