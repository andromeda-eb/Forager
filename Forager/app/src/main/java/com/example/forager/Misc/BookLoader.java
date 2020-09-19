package com.example.forager.Misc;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class BookLoader extends AsyncTaskLoader<String> {
    private String queryString;

    public BookLoader(@NonNull Context context, String queryString) {
        super(context);
        this.queryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // Won't start loading until this is called
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(queryString);
    }
}
