package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;

public class AppUtil {
    private Context context;

    private AppUtil() {
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final AppUtil INSTANCE = new AppUtil();
    }

    public static AppUtil getInstance() {
        return Holder.INSTANCE;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
