package com.example.currnecyconeverterbyserega.ui.model;

import androidx.annotation.NonNull;

public class Currency {

    private final String code;
    private final String name;
    private final String flagEmoji;

    public Currency(String code, String name, String flagEmoji) {
        this.code = code;
        this.name = name;
        this.flagEmoji = flagEmoji;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFlagEmoji() {
        return flagEmoji;
    }

    @NonNull
    @Override
    public String toString() {
        return code;
    }
}
