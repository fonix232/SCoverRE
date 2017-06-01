package com.samsung.android.app;

import android.content.Context;
import com.android.internal.app.LocalePicker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SemLocalePicker {

    public static class LocaleInfo {
        String label;
        Locale locale;

        private LocaleInfo(com.android.internal.app.LocalePicker.LocaleInfo localeInfo) {
            if (localeInfo != null) {
                this.label = localeInfo.getLabel();
                this.locale = localeInfo.getLocale();
            }
        }

        public String getLabel() {
            return this.label;
        }

        public Locale getLocale() {
            return this.locale;
        }
    }

    private SemLocalePicker() {
    }

    public static List<LocaleInfo> getAllLocales(Context context) {
        if (context == null) {
            return null;
        }
        Iterable<com.android.internal.app.LocalePicker.LocaleInfo> allAssetLocales = LocalePicker.getAllAssetLocales(context, false);
        List<LocaleInfo> list = null;
        if (allAssetLocales != null) {
            list = new ArrayList(allAssetLocales.size());
            for (com.android.internal.app.LocalePicker.LocaleInfo localeInfo : allAssetLocales) {
                if (localeInfo != null) {
                    list.add(new LocaleInfo(localeInfo));
                }
            }
        }
        return list;
    }

    public static void updateLocale(Locale locale) {
        if (locale != null) {
            LocalePicker.updateLocale(locale);
        }
    }
}
