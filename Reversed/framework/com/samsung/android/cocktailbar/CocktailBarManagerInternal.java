package com.samsung.android.cocktailbar;

public abstract class CocktailBarManagerInternal {
    public abstract void turnOffWakupCocktailBarFromPowerManager(int i, String str);

    public abstract void updateCocktailBarStateFromWindowManager(int i);

    public abstract void updateSysfsGripDisableFromWindowManager(boolean z);

    public abstract void wakupCocktailBarFromWindowManager(boolean z, int i, int i2);
}
