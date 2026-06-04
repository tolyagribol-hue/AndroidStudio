package com.example.currnecyconeverterbyserega.utils;

import java.util.Locale;

public final class NumberFormatUtils {

    private NumberFormatUtils() {
    }

    public static String formatAmount(double value) {
        if (value == 0) {
            return "";
        }
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        }
        if (value < 0.001) {
            return String.format(Locale.US, "%.6f", value);
        }
        if (value < 0.1) {
            return String.format(Locale.US, "%.4f", value);
        }
        return String.format(Locale.US, "%.2f", value);
    }
}
