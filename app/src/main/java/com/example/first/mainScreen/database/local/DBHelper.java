package com.example.first.mainScreen.database.local;

import android.content.Context;

import androidx.room.Room;

public class DBHelper {
    private static DBHelper ourInstance;
    private final CredentialDb mCredentialDb;

    public static DBHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DBHelper(context);
        }
        return ourInstance;
    }

    public CredentialDb getCredentialDb() {
        return mCredentialDb;
    }

    private DBHelper(Context context) {
        this.mCredentialDb = Room.databaseBuilder(context, CredentialDb.class, "credential.db")
                .build();
    }

}
