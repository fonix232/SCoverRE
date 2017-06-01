package com.samsung.android.sdk.look.cocktailbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;

public class SlookCocktailProvider extends BroadcastReceiver {
    private static final String ACTION_COCKTAIL_DISABLED = "com.samsung.android.cocktail.action.COCKTAIL_DISABLED";
    private static final String ACTION_COCKTAIL_DROPED = "com.samsung.android.cocktail.action.COCKTAIL_DROPED";
    private static final String ACTION_COCKTAIL_ENABLED = "com.samsung.android.cocktail.action.COCKTAIL_ENABLED";
    private static final String ACTION_COCKTAIL_UPDATE = "com.samsung.android.cocktail.action.COCKTAIL_UPDATE";
    private static final String ACTION_COCKTAIL_UPDATE_FEEDS = "com.samsung.android.cocktail.action.COCKTAIL_UPDATE_FEEDS";
    private static final String ACTION_COCKTAIL_UPDATE_V2 = "com.samsung.android.cocktail.v2.action.COCKTAIL_UPDATE";
    private static final String ACTION_COCKTAIL_VISIBILITY_CHANGED = "com.samsung.android.cocktail.action.COCKTAIL_VISIBILITY_CHANGED";
    private static final String EXTRA_COCKTAIL_ID = "cocktailId";
    private static final String EXTRA_COCKTAIL_IDS = "cocktailIds";
    private static final String EXTRA_COCKTAIL_VISIBILITY = "cocktailVisibility";
    private static final String EXTRA_DRAG_EVENT = "com.samsung.android.intent.extra.DRAG_EVENT";
    private static final String TAG = "SlookCocktail";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras;
        if (ACTION_COCKTAIL_UPDATE.equals(action) || ACTION_COCKTAIL_UPDATE_V2.equals(action)) {
            extras = intent.getExtras();
            if (extras != null && extras.containsKey(EXTRA_COCKTAIL_IDS)) {
                onUpdate(context, SlookCocktailManager.getInstance(context), extras.getIntArray(EXTRA_COCKTAIL_IDS));
            }
        } else if (ACTION_COCKTAIL_ENABLED.equals(action)) {
            onEnabled(context);
        } else if (ACTION_COCKTAIL_DISABLED.equals(action)) {
            onDisabled(context);
        } else if (ACTION_COCKTAIL_VISIBILITY_CHANGED.equals(action)) {
            extras = intent.getExtras();
            if (extras != null && extras.containsKey(EXTRA_COCKTAIL_ID)) {
                int cocktailId = extras.getInt(EXTRA_COCKTAIL_ID);
                if (extras.containsKey(EXTRA_COCKTAIL_VISIBILITY)) {
                    onVisibilityChanged(context, cocktailId, extras.getInt(EXTRA_COCKTAIL_VISIBILITY));
                }
            }
        } else if (ACTION_COCKTAIL_DROPED.equals(action)) {
            extras = intent.getExtras();
            if (extras != null && extras.containsKey(EXTRA_DRAG_EVENT)) {
                onDroped(context, SlookCocktailManager.getInstance(context), (DragEvent) intent.getParcelableExtra(EXTRA_DRAG_EVENT));
            }
        } else if (ACTION_COCKTAIL_UPDATE_FEEDS.equals(action)) {
            extras = intent.getExtras();
            if (extras != null && extras.containsKey(EXTRA_COCKTAIL_ID)) {
                onUpdateFeeds(context, SlookCocktailManager.getInstance(context), extras.getInt(EXTRA_COCKTAIL_ID));
            }
        }
    }

    public void onUpdate(Context context, SlookCocktailManager cocktailManager, int[] cocktailIds) {
    }

    public void onEnabled(Context context) {
    }

    public void onDisabled(Context context) {
    }

    public void onVisibilityChanged(Context context, int cocktailId, int visibility) {
    }

    public void onDroped(Context context, SlookCocktailManager cocktailManager, DragEvent dragEvent) {
    }

    public void onUpdateFeeds(Context context, SlookCocktailManager cocktailManager, int cocktailId) {
    }
}
