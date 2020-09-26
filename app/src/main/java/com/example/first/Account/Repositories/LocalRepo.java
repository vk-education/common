package com.example.first.Account.Repositories;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.first.Account.AccountEdit.EditActivityRepo;
import com.example.first.AccountDB.DatabaseHelper;
import com.example.first.AccountDB.ImageEntity;
import com.example.first.AccountDB.ProfileEntity;
import com.example.first.Account.Executors.ExecutorsDB;
import com.example.first.Profile;
import com.example.first.ProfilesDB.DBHelper;

import java.util.ArrayList;

public class LocalRepo implements RepoDB {
    private Context context;

    LocalRepo(Context context) {
        this.context = context;
    }

    @Override
    public void getProfile(final CallbackProfile callback) {
        ExecutorsDB.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProfileEntity profileEntity = DatabaseHelper
                        .getInstance(context)
                        .getProfileDB()
                        .getProfileDao()
                        .getById(ProfileEntity.DEFAULT_NUMBER);

                final Profile profile = new Profile();

                if (profileEntity == null) {
                    ExecutorsDB.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.notFound();
                        }
                    });
                }
                else {

                    profile.setName(profileEntity.name);
                    profile.setPhone(profileEntity.phone);
                    profile.setEmail(profileEntity.email);
                    profile.setCountry(profileEntity.country);
                    profile.setBreed(profileEntity.breed);
                    profile.setAddress(profileEntity.address);
                    profile.setAge(profileEntity.age);
                    profile.setCity(profileEntity.city);

                    ExecutorsDB.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(profile);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void getImage(final CallbackImage callback) {
        ExecutorsDB.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final ImageEntity imageEntity = DatabaseHelper
                        .getInstance(context)
                        .getProfileDB()
                        .getProfileDao()
                        .getImageById(ImageEntity.DEFAULT_NUMBER);

                if (imageEntity == null) {
                    ExecutorsDB.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.notFound();
                        }
                    });
                }
                else {
                    ExecutorsDB.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(imageEntity.image);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void setProfile(final EditActivityRepo.ProfileInfo profile, final CallbackUpload callback) {
        ExecutorsDB.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProfileEntity profileEntity = new ProfileEntity(profile);
                DatabaseHelper.getInstance(context)
                        .getProfileDB()
                        .getProfileDao()
                        .setById(profileEntity);

                // TODO: понять, как отслеживать состояние таска setById(success/failure)
                // После загрузки в базу вызываем onSuccess
                // TODO: разобраться почему этот коллбэк вызывается именно на mainThread
                ExecutorsDB.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess();
                    }
                });
            }
        });
    }

    @Override
    public void setImage(final Bitmap image, final CallbackUpload callback) {
        ExecutorsDB.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // пока фотка одна - испульзую дефолтный id
                ImageEntity imageEntity = new ImageEntity(ImageEntity.DEFAULT_NUMBER, image);
                DatabaseHelper.getInstance(context)
                        .getProfileDB()
                        .getProfileDao()
                        .setImageById(imageEntity);

                // После загрузки в базу вызываем onSuccess
                ExecutorsDB.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess();
                    }
                });
            }
        });
    }

    @Override
    public void exit() {
        ExecutorsDB.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> list = new ArrayList<>();
                list.add(ProfileEntity.DEFAULT_NUMBER);
                DatabaseHelper.getInstance(context).getProfileDB().getProfileDao().deleteByIdList(list);
                DatabaseHelper.getInstance(context).getProfileDB().getProfileDao().deleteImageByIdList(list);
                DBHelper.getInstance(context).getCacheProfilesDb().getCacheProfilesDao().deleteAll();
            }
        });
    }
}
