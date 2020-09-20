package com.example.first.mainScreen.database.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Credential.class}, version = 2)
public abstract class CredentialDb extends RoomDatabase {
    public abstract CredentialDao getCredentialDao();
}
