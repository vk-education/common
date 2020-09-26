package com.example.first.authorizationAndRegistration;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.first.Account.Executors.ExecutorsDB;
import com.example.first.AccountDB.DatabaseHelper;
import com.example.first.AccountDB.ImageEntity;
import com.example.first.AccountDB.ProfileEntity;
import com.example.first.Profile;

public class AuthRegDB {
    private Context context;

    public AuthRegDB(Context context) {
        this.context = context;
    }

    public void setProfile(final Profile profile) {
        if (profile == null)
            return;

        ExecutorsDB.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProfileEntity profileEntity = new ProfileEntity(
                        profile.getName(),
                        profile.getEmail(),
                        profile.getPhone(),
                        profile.getBreed(),
                        profile.getAge(),
                        profile.getCountry(),
                        profile.getCity(),
                        profile.getAddress());

                DatabaseHelper.getInstance(context)
                        .getProfileDB()
                        .getProfileDao()
                        .setById(profileEntity);
            }
        });
    }

    public void setImage (final Bitmap image) {
        ExecutorsDB.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ImageEntity imageEntity = new ImageEntity(ImageEntity.DEFAULT_NUMBER, image);
                DatabaseHelper.getInstance(context)
                        .getProfileDB()
                        .getProfileDao()
                        .setImageById(imageEntity);
            }
        });
    }
}
