package com.example.first.ProfilesDB;

import android.content.Context;

import androidx.room.Room;

public class DBHelper {
    private static DBHelper ourInstance;
    private final CacheProfilesDb mCacheProfilesDb;

    public static DBHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DBHelper(context);
        }
        return ourInstance;
    }

    public CacheProfilesDb getCacheProfilesDb() {
        return mCacheProfilesDb;
    }

    private DBHelper(Context context) {
        this.mCacheProfilesDb = Room.databaseBuilder(context, CacheProfilesDb.class, "credential.db")
                .fallbackToDestructiveMigration()
                .build();
    }

}
