package com.example.first.AccountDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ProfileEntity.class, ImageEntity.class}, version = 1)
public abstract class ProfileDB extends RoomDatabase {
    public abstract ProfileDao getProfileDao();
}
