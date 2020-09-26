package com.example.first.ProfilesDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ProfileEntity.class}, version = 3, exportSchema = false)
public abstract class CacheProfilesDb extends RoomDatabase {
    public abstract CacheProfilesDao getCacheProfilesDao();
}
