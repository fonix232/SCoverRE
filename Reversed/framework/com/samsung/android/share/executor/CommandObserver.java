package com.samsung.android.share.executor;

import java.util.Observable;

public class CommandObserver extends Observable {
    private static final CommandObserver instance = new CommandObserver();

    private CommandObserver() {
    }

    public static CommandObserver getInstance() {
        return instance;
    }

    public void notify(Object obj) {
        synchronized (this) {
            setChanged();
            notifyObservers(obj);
        }
    }
}
