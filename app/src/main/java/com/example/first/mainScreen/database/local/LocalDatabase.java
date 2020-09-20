package com.example.first.mainScreen.database.local;

import android.content.Context;

import com.example.first.Profile;
import com.example.first.mainScreen.database.ProfileDatabase;
import com.example.first.mainScreen.executors.AppExecutors;
import com.example.first.mainScreen.repositories.InfoRepo;

import java.util.List;

public class LocalDatabase implements ProfileDatabase {
    private final static int MAX_USER = 20;

    private ProfileDatabase mNetworkDatabase;
    private Context mContext;

    private List<Credential> allCred = null;
    private int count = 0;

    public LocalDatabase(Context context, ProfileDatabase networkDatabase) {
        this.mContext = context;
        this.mNetworkDatabase = networkDatabase;

        pullProfile();
    }

    @Override
    public void getCaseProfile(final GetCaseProfileCallback caseProfileCallback) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                CredentialDao dao;
                dao = DBHelper.getInstance(mContext)
                        .getCredentialDb()
                        .getCredentialDao();

                if ((allCred == null) || (allCred.size() == 0))
                    allCred = dao.getAll();

                if ((allCred == null) || allCred.size() < MAX_USER / 2)
                    pullProfile();

                if ((allCred == null) || (allCred.size() == 0))
                    caseProfileCallback.onNotFound();

                else {
                    Credential currentCredential = allCred.get(0);
                    allCred.remove(0);
                    dao.delete(currentCredential);

                    final InfoRepo.CaseProfile caseProfile = new InfoRepo.CaseProfile();
                    Profile profile = new Profile();
                    profile.setName(currentCredential.name);
                    profile.setCity(currentCredential.city);
                    profile.setAge(currentCredential.age);
                    profile.setAddress(currentCredential.address);
                    profile.setBreed(currentCredential.breed);
                    profile.setCountry(currentCredential.country);
                    profile.setEmail(currentCredential.email);
                    profile.setPhone(currentCredential.phone);

                    caseProfile.profile = profile;
                    caseProfile.id = currentCredential.id;
                    caseProfile.name = profile.getName();
                    caseProfile.bitmap = currentCredential.bitmap;

                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            caseProfileCallback.onSuccess(caseProfile);
                        }
                    });
                }
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

    private void pullProfile() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final CredentialDao dao = DBHelper.getInstance(mContext)
                        .getCredentialDb()
                        .getCredentialDao();
                count = dao
                        .getAll()
                        .size();

                newProfile(dao);
            }
        });
    }

    private void newProfile(final CredentialDao dao) {
        if (count > MAX_USER)
            return;

        count++;
        mNetworkDatabase.getCaseProfile(new GetCaseProfileCallback() {
            @Override
            public void onSuccess(InfoRepo.CaseProfile caseProfile) {
                final Credential currentCredential = new Credential(
                        caseProfile.id,
                        caseProfile.profile.getName(),
                        caseProfile.profile.getEmail(),
                        caseProfile.profile.getPhone(),
                        caseProfile.profile.getBreed(),
                        caseProfile.profile.getAge(),
                        caseProfile.profile.getCountry(),
                        caseProfile.profile.getCity(),
                        caseProfile.profile.getAddress());

                currentCredential.bitmap = caseProfile.bitmap;

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        dao.change(currentCredential);
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
