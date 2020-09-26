package com.example.first;

import android.app.Application;
import android.content.Context;

import com.example.first.mainScreen.database.CompositeDatabase;
import com.example.first.mainScreen.repositories.InfoRepo;

public class ApplicationModified extends Application {
    private InfoRepo infoRepo;
    private CompositeDatabase compositeDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        compositeDatabase = new CompositeDatabase(getApplicationContext());
        infoRepo = new InfoRepo(compositeDatabase);
    }

    public InfoRepo getInfoRepo() {
        return infoRepo;
    }

    public static  ApplicationModified from(Context context) {
        return (ApplicationModified) context.getApplicationContext();
    }


}
