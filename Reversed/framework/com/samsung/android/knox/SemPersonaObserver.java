package com.samsung.android.knox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PersonaAttribute;
import java.util.Set;

public abstract class SemPersonaObserver {
    public static final int FLAG_OBSERVER_ON_ATTRIBUTE_CHANGED = 8;
    public static final int FLAG_OBSERVER_ON_KEYGUARD_STATE_CHANGED = 4;
    public static final int FLAG_OBSERVER_ON_SESSION_EXPIRED = 2;
    public static final int FLAG_OBSERVER_ON_STATE_CHANGED = 1;
    private boolean checkOnAttributeChange = false;
    private boolean checkOnKeyguardStateChanged = false;
    private boolean checkOnSessionExpired = false;
    private boolean checkOnStateChange = false;
    protected int containerId = -1;
    private Context context = null;
    protected int flags = 0;
    private PersonaObserverReceiver personaObserverReceiver;

    private class PersonaObserverReceiver extends BroadcastReceiver {
        private PersonaObserverReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Set categories = intent.getCategories();
            if (SemPersonaObserver.this.checkOnStateChange && categories.contains(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONSTATECHANGE)) {
                SemPersonaObserver.this.onStateChange(SemPersonaState.valueOf(intent.getStringExtra(SemPersonaManager.INTENT_EXTRA_OBSERVER_NEWSTATE)), SemPersonaState.valueOf(intent.getStringExtra(SemPersonaManager.INTENT_EXTRA_OBSERVER_PREVIOUSSTATE)));
            } else if (SemPersonaObserver.this.checkOnSessionExpired && categories.contains(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONSESSIONEXPIRED)) {
                SemPersonaObserver.this.onSessionExpired();
            } else if (SemPersonaObserver.this.checkOnKeyguardStateChanged && categories.contains(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONKEYGUARDSTATECHANGED)) {
                SemPersonaObserver.this.onKeyGuardStateChanged(intent.getBooleanExtra(SemPersonaManager.INTENT_EXTRA_OBSERVER_KEYGUARDSTATE, true));
            } else if (SemPersonaObserver.this.checkOnAttributeChange && categories.contains(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONATTRIBUTECHANGE)) {
                SemPersonaObserver.this.onAttributeChange(PersonaAttribute.valueOf(intent.getStringExtra(SemPersonaManager.INTENT_EXTRA_OBSERVER_ATTRIBUTE)), intent.getBooleanExtra(SemPersonaManager.INTENT_EXTRA_OBSERVER_ATTRIBUTE_STATE, true));
            }
        }
    }

    public SemPersonaObserver(Context context, int i, int i2) {
        this.context = context;
        this.containerId = i;
        this.flags = i2;
        initializeReceiver();
    }

    private void initializeReceiver() {
        IntentFilter intentFilter;
        this.personaObserverReceiver = new PersonaObserverReceiver();
        if ((this.flags & 1) == 1) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(SemPersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONSTATECHANGE);
            this.checkOnStateChange = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, SemPersonaManager.INTENT_PERMISSION_OBSERVER, null);
            }
        }
        if ((this.flags & 2) == 2) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(SemPersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONSESSIONEXPIRED);
            this.checkOnSessionExpired = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, SemPersonaManager.INTENT_PERMISSION_OBSERVER, null);
            }
        }
        if ((this.flags & 4) == 4) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(SemPersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONKEYGUARDSTATECHANGED);
            this.checkOnKeyguardStateChanged = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, SemPersonaManager.INTENT_PERMISSION_OBSERVER, null);
            }
        }
        if ((this.flags & 8) == 8) {
            intentFilter = new IntentFilter();
            intentFilter.addAction(SemPersonaManager.INTENT_ACTION_OBSERVER);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_CONTAINERID + this.containerId);
            intentFilter.addCategory(SemPersonaManager.INTENT_CATEGORY_OBSERVER_ONATTRIBUTECHANGE);
            this.checkOnAttributeChange = true;
            if (this.context != null) {
                this.context.registerReceiver(this.personaObserverReceiver, intentFilter, SemPersonaManager.INTENT_PERMISSION_OBSERVER, null);
            }
        }
    }

    public void onAttributeChange(PersonaAttribute personaAttribute, boolean z) {
    }

    public abstract void onKeyGuardStateChanged(boolean z);

    public abstract void onSessionExpired();

    public abstract void onStateChange(SemPersonaState semPersonaState, SemPersonaState semPersonaState2);

    public void unregisterPersonaObserverReceiver() {
        if (this.context != null) {
            this.context.unregisterReceiver(this.personaObserverReceiver);
        }
    }
}
