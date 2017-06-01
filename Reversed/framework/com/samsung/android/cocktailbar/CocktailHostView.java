package com.samsung.android.cocktailbar;

import android.content.Context;
import android.os.Process;
import android.os.UserHandle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback;

public class CocktailHostView extends FrameLayout {
    static final String TAG = "CocktailHostView";
    private Cocktail mCocktail;
    private int mCocktailId;
    private UserHandle mUser = Process.myUserHandle();

    public CocktailHostView(Context context, Cocktail cocktail) {
        super(context);
        setIsRootNamespace(true);
        setCocktail(cocktail);
    }

    public Cocktail getCocktail() {
        return this.mCocktail;
    }

    public int getCocktailId() {
        return this.mCocktailId;
    }

    public void setCocktail(Cocktail cocktail) {
        this.mCocktailId = 0;
        this.mCocktail = cocktail;
        if (cocktail != null) {
            this.mCocktailId = cocktail.getCocktailId();
        }
    }

    public void setUserId(int i) {
        this.mUser = new UserHandle(i);
    }

    public void viewDataChanged(int i) {
        View findViewById = findViewById(i);
        if (findViewById != null && (findViewById instanceof AdapterView)) {
            View view = findViewById;
            Adapter adapter = view.getAdapter();
            if (adapter instanceof BaseAdapter) {
                adapter.notifyDataSetChanged();
            } else if (adapter == null && (view instanceof RemoteAdapterConnectionCallback)) {
                ((RemoteAdapterConnectionCallback) view).deferNotifyDataSetChanged();
            }
        }
    }
}
