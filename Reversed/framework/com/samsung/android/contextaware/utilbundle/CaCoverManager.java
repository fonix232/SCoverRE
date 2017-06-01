package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.cover.CoverManager;
import com.samsung.android.cover.CoverManager.StateListener;
import com.samsung.android.cover.CoverState;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaCoverManager implements IUtilManager, IBootStatusObserver {
    private static final int COVER_TYPE_FLIP = 1;
    private static final int COVER_TYPE_NONE = 0;
    private static final int COVER_TYPE_VIEW = 2;
    private static volatile CaCoverManager instance;
    private Context mContext;
    private CoverManager mCoverManager = null;
    private final StateListener mCoverStateListener = new C00141();
    private boolean mCurrentCoverState = true;
    private int mCurrentCoverType = 0;
    private final CopyOnWriteArrayList<ICoverStatusChangeObserver> mListeners = new CopyOnWriteArrayList();
    private final Looper mLooper;

    class C00141 extends StateListener {
        C00141() {
        }

        public void onCoverStateChanged(CoverState coverState) {
            CaLogger.info("state:" + coverState);
            if (coverState != null) {
                CaCoverManager.this.mCurrentCoverState = coverState.getSwitchState();
                CaCoverManager.this.mCurrentCoverType = coverState.getType();
                CaCoverManager.this.notifyObservers(coverState);
                return;
            }
            CaLogger.error("state is null");
        }
    }

    class C00152 implements Runnable {
        C00152() {
        }

        public void run() {
            if (CaCoverManager.this.mCoverManager != null && CaCoverManager.this.mCoverStateListener != null) {
                CaCoverManager.this.mCoverManager.unregisterListener(CaCoverManager.this.mCoverStateListener);
            }
        }
    }

    class C00163 implements Runnable {
        C00163() {
        }

        public void run() {
            if (CaCoverManager.this.mCoverManager == null || CaCoverManager.this.mCoverStateListener == null) {
                CaLogger.error("cover null");
            } else {
                CaCoverManager.this.mCoverManager.registerListener(CaCoverManager.this.mCoverStateListener);
            }
        }
    }

    public CaCoverManager(Looper looper) {
        this.mLooper = looper;
    }

    public static CaCoverManager getInstance(Looper looper) {
        if (instance == null) {
            synchronized (CaCoverManager.class) {
                if (instance == null) {
                    instance = new CaCoverManager(looper);
                }
            }
        }
        return instance;
    }

    private void notifyObservers(CoverState coverState) {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ICoverStatusChangeObserver iCoverStatusChangeObserver = (ICoverStatusChangeObserver) it.next();
            if (iCoverStatusChangeObserver != null) {
                iCoverStatusChangeObserver.onCoverStatusChanged(coverState);
            }
        }
    }

    public void bootCompleted() {
        this.mCoverManager = new CoverManager(this.mContext);
        if (this.mLooper != null) {
            new Handler(this.mLooper).postDelayed(new C00163(), 0);
        } else {
            CaLogger.error("looper null");
        }
    }

    public boolean getCoverState() {
        CaLogger.info("State:" + this.mCurrentCoverState);
        return this.mCurrentCoverState;
    }

    public int getCoverType() {
        CaLogger.info("Type:" + this.mCurrentCoverType);
        switch (this.mCurrentCoverType) {
            case 0:
            case 4:
            case 5:
            case 7:
            case 100:
                return 1;
            case 1:
            case 3:
            case 6:
                return 2;
            case 2:
                return 0;
            default:
                return 0;
        }
    }

    public void initializeManager(Context context) {
        CaBootStatus.getInstance().registerObserver(this);
        this.mContext = context;
    }

    public final void registerObserver(ICoverStatusChangeObserver iCoverStatusChangeObserver) {
        if (!this.mListeners.contains(iCoverStatusChangeObserver)) {
            this.mListeners.add(iCoverStatusChangeObserver);
        }
    }

    public void terminateManager() {
        new Handler(this.mLooper).postDelayed(new C00152(), 0);
    }

    public final void unregisterObserver(ICoverStatusChangeObserver iCoverStatusChangeObserver) {
        if (this.mListeners.contains(iCoverStatusChangeObserver)) {
            this.mListeners.remove(iCoverStatusChangeObserver);
        }
    }
}
