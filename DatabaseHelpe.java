package com.example.first.AccountDB;

import android.content.Context;
import androidx.room.Room;

public class DatabaseHelper {
    private static DatabaseHelper databaseHelper;
    private final ProfileDB profileDB;

    public static DatabaseHelper getInstance(Context context) {
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        profileDB = Room.databaseBuilder(context, ProfileDB.class, "profileInfo.db")
                .build();
    }

    public ProfileDB getProfileDB() {
        return profileDB;
    }
}
