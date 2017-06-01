package com.samsung.android.contextaware.manager;

import android.os.IBinder;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.creator.ContextProviderCreator;
import com.samsung.android.contextaware.creator.builtin.AggregatorConcreteCreator;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareService.Watcher;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListenerListManager {
    private static volatile ListenerListManager instance;
    private CopyOnWriteArrayList<ContextProviderCreator> mCreator;
    private final CopyOnWriteArrayList<Listener> mListenerList = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<Watcher> mWatcherList = new CopyOnWriteArrayList();

    public static ListenerListManager getInstance() {
        if (instance == null) {
            synchronized (ListenerListManager.class) {
                if (instance == null) {
                    instance = new ListenerListManager();
                }
            }
        }
        return instance;
    }

    private int getUsedSubCollectionCount(String str, String str2) {
        int i = 0;
        Iterator it = this.mCreator.iterator();
        while (it.hasNext()) {
            ContextProviderCreator contextProviderCreator = (ContextProviderCreator) it.next();
            if (contextProviderCreator != null && contextProviderCreator.existContextProvider(str) && (contextProviderCreator instanceof AggregatorConcreteCreator)) {
                CopyOnWriteArrayList subCollectionList = contextProviderCreator.getSubCollectionList(str);
                if (!(subCollectionList == null || subCollectionList.isEmpty())) {
                    Iterator it2 = subCollectionList.iterator();
                    while (it2.hasNext()) {
                        String str3 = (String) it2.next();
                        if (!(str3 == null || str3.isEmpty())) {
                            if (isAggregator(str3)) {
                                i += getUsedSubCollectionCount(str3, str2);
                            }
                            if (str3.equals(str2)) {
                                i++;
                            }
                        }
                    }
                }
            }
        }
        return i;
    }

    private boolean isAggregator(String str) {
        Iterator it = this.mCreator.iterator();
        while (it.hasNext()) {
            ContextProviderCreator contextProviderCreator = (ContextProviderCreator) it.next();
            if (contextProviderCreator != null && contextProviderCreator.existContextProvider(str) && (contextProviderCreator instanceof AggregatorConcreteCreator)) {
                return true;
            }
        }
        return false;
    }

    protected final void addListener(Listener listener) {
        if (listener != null && !this.mListenerList.contains(listener)) {
            this.mListenerList.add(listener);
        }
    }

    protected final void addWatcher(Watcher watcher) {
        if (watcher != null && !this.mWatcherList.contains(watcher)) {
            this.mWatcherList.add(watcher);
        }
    }

    protected final Listener getListener(IBinder iBinder) {
        Iterator it = this.mListenerList.iterator();
        while (it.hasNext()) {
            Listener listener = (Listener) it.next();
            if (iBinder.equals(listener.getToken())) {
                return listener;
            }
        }
        return null;
    }

    protected final CopyOnWriteArrayList<Listener> getListenerList() {
        return this.mListenerList;
    }

    public final int getUsedServiceCount(String str) {
        int i = 0;
        Iterator it = this.mListenerList.iterator();
        while (it.hasNext()) {
            Listener listener = (Listener) it.next();
            int serviceOrdinal = ContextList.getInstance().getServiceOrdinal(str);
            if (listener.getServices().containsKey(Integer.valueOf(serviceOrdinal))) {
                i += ((Integer) listener.getServices().get(Integer.valueOf(serviceOrdinal))).intValue();
            }
        }
        return i;
    }

    public final int getUsedSubCollectionCount(String str) {
        int i = 0;
        Iterator it = this.mListenerList.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((Listener) it.next()).getServices().keySet().iterator();
            Iterator it3 = it2;
            while (it2.hasNext()) {
                String serviceCode = ContextList.getInstance().getServiceCode(((Integer) it2.next()).intValue());
                if (!(serviceCode == null || serviceCode.isEmpty())) {
                    i += getUsedSubCollectionCount(serviceCode, str);
                }
            }
        }
        return i;
    }

    public final int getUsedTotalCount(String str) {
        return getUsedServiceCount(str) + getUsedSubCollectionCount(str);
    }

    protected final CopyOnWriteArrayList<Watcher> getWatcherList() {
        return this.mWatcherList;
    }

    public final void removeListener(Listener listener) {
        if (listener != null && this.mListenerList.contains(listener)) {
            this.mListenerList.remove(listener);
        }
    }

    public final void removeWatcher(Watcher watcher) {
        if (watcher != null && this.mWatcherList.contains(watcher)) {
            this.mWatcherList.remove(watcher);
        }
    }

    protected final void setCreator(CopyOnWriteArrayList<ContextProviderCreator> copyOnWriteArrayList) {
        this.mCreator = copyOnWriteArrayList;
    }
}
