package com.example.first.mainScreen.database;

import android.content.Context;
import android.util.Log;

import com.example.first.Profile;
import com.example.first.ProfilesDB.ProfileEntity;
import com.example.first.ProfilesDB.CacheProfilesDao;
import com.example.first.ProfilesDB.DBHelper;
import com.example.first.mainScreen.executors.AppExecutors;
import com.example.first.mainScreen.repositories.InfoRepo;

public class LocalDatabase implements ProfileDatabase {
    private final static int MAX_USER = 20;

    private ProfileDatabase mNetworkDatabase;
    private Context mContext;

    private int count = 0;

    LocalDatabase(Context context, ProfileDatabase networkDatabase) {
        this.mContext = context;
        this.mNetworkDatabase = networkDatabase;

        pullProfile();
    }

    @Override
    public void getCaseProfile(final GetCaseProfileCallback caseProfileCallback) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                CacheProfilesDao dao;
                dao = DBHelper.getInstance(mContext)
                        .getCacheProfilesDb()
                        .getCacheProfilesDao();

                count = dao.getCount();

                if (count == 0) {
                    Log.d("information", "not accaunt in localbase");
                    caseProfileCallback.onNotFound();
                    return;
                }
                else {
                    ProfileEntity currentProfileEntity = dao.getProfileEntity();
                    dao.delete(currentProfileEntity);

                    final InfoRepo.CaseProfile caseProfile = new InfoRepo.CaseProfile();
                    Profile profile = new Profile();
                    profile.setName(currentProfileEntity.name);
                    profile.setCity(currentProfileEntity.city);
                    profile.setAge(currentProfileEntity.age);
                    profile.setAddress(currentProfileEntity.address);
                    profile.setBreed(currentProfileEntity.breed);
                    profile.setCountry(currentProfileEntity.country);
                    profile.setEmail(currentProfileEntity.email);
                    profile.setPhone(currentProfileEntity.phone);

                    caseProfile.profile = profile;
                    caseProfile.id = currentProfileEntity.id;
                    caseProfile.name = profile.getName();
                    caseProfile.bitmap = currentProfileEntity.bitmap;

                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            caseProfileCallback.onSuccess(caseProfile);
                        }
                    });
                }

                if (count < MAX_USER / 2)
                    pullProfile();
            }
        });
    }

    @Override
    public void getMyCaseProfile(GetCaseProfileCallback caseCallback) {
        caseCallback.onNotFound();
    }

    @Override
    public void changeProfileByCase(InfoRepo.CaseProfile caseProfile) {
        mNetworkDatabase.changeProfileByCase(caseProfile);
    }

    public void pullProfile() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final CacheProfilesDao dao = DBHelper.getInstance(mContext)
                        .getCacheProfilesDb()
                        .getCacheProfilesDao();
                count = dao
                        .getCount();

                newProfile(dao);
            }
        });
    }

    private void newProfile(final CacheProfilesDao dao) {
        if (count > MAX_USER)
            return;

        count++;
        mNetworkDatabase.getCaseProfile(new GetCaseProfileCallback() {
            @Override
            public void onSuccess(InfoRepo.CaseProfile caseProfile) {
                final ProfileEntity currentProfileEntity = new ProfileEntity(
                        caseProfile.id,
                        caseProfile.profile.getName(),
                        caseProfile.profile.getEmail(),
                        caseProfile.profile.getPhone(),
                        caseProfile.profile.getBreed(),
                        caseProfile.profile.getAge(),
                        caseProfile.profile.getCountry(),
                        caseProfile.profile.getCity(),
                        caseProfile.profile.getAddress());

                currentProfileEntity.bitmap = caseProfile.bitmap;

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        dao.change(currentProfileEntity);
                        newProfile(dao);
                    }
                });
            }

            @Override
            public void onError(int codeError) {
            }

            @Override
            public void onNotFound() {
            }
        });
    }
}
