package com.example.first.mainScreen.repositories;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.first.ApplicationModified;
import com.example.first.Profile;
import com.example.first.mainScreen.database.ProfileDatabase;

import java.util.ArrayList;

public class InfoRepo {
    private ProfileDatabase database;
    private CaseProfile newCase;
    private MediatorLiveData<CaseProfile> liveDataRepo;
    private MediatorLiveData<statusRepo> liveDataRepoStatus;

    public InfoRepo(ProfileDatabase database) {
        newCase = null;

        this.database = database;
    }

    @NonNull
    public static InfoRepo getInstance(Context context) {
        return ApplicationModified.from(context).getInfoRepo();
    }

    public LiveData<statusRepo> getStatus() {
        liveDataRepoStatus = new MediatorLiveData<>();
        return liveDataRepoStatus;
    }

    public LiveData<CaseProfile> getFirstCaseProfile() {
        liveDataRepo = new MediatorLiveData<>();

        if (newCase == null)
            database.getCaseProfile(new CredentialsCallback());
        else
            liveDataRepo.setValue(newCase);

        return liveDataRepo;
    }

    public LiveData<CaseProfile> getCaseProfile() {
        liveDataRepo = new MediatorLiveData<>();
        newCase = null;

        database.getCaseProfile(new CredentialsCallback());

        return liveDataRepo;
    }

    private void processInformation(CaseProfile myUserCase, CaseProfile changeUserCase) {
        ArrayList<String> mLikes = myUserCase.profile.getLikes();

        if (changeUserCase == null)
            return;

        ArrayList<String> likes;
        likes = changeUserCase.profile.getLikes();
        if (likes == null)
            likes = new ArrayList<>();
        likes.add(myUserCase.id);
        changeUserCase.profile.setLikes(likes);

        database.changeProfileByCase(changeUserCase);

        if ((mLikes != null) && (mLikes.indexOf(changeUserCase.id) != -1)) {
            ArrayList<Profile.Matches> matches;

            Profile.Matches myMatch = new Profile.Matches(changeUserCase.id, changeUserCase.name, "false");
            matches = myUserCase.profile.getMatches();
            if (matches == null)
                matches = new ArrayList<>();
            matches.add(myMatch);
            myUserCase.profile.setMatches(matches);

            database.changeProfileByCase(myUserCase);


            Profile.Matches youMatch = new Profile.Matches(myUserCase.id, myUserCase.name, "false");
            matches = changeUserCase.profile.getMatches();
            if (matches == null)
                matches = new ArrayList<>();
            matches.add(youMatch);
            changeUserCase.profile.setMatches(matches);

            database.changeProfileByCase(changeUserCase);
        }
    }

    public void processInformation() {
        final CaseProfile lastUserCase = newCase;
        database.getMyCaseProfile(new ProfileDatabase.GetCaseProfileCallback() {
            @Override
            public void onSuccess(CaseProfile caseProfile) {
                processInformation(caseProfile, lastUserCase);
            }

            @Override
            public void onError(int codeError) {
                // TODO открыть экран загрузки
            }

            @Override
            public void onNotFound() {
                // TODO выход из аккаунта с сообщением пользователю, что его данных нет в базе
            }
        });
    }

    public static class CaseProfile {
        public Profile profile;
        public Bitmap bitmap;
        public String name;
        public String id;

        public CaseProfile() {
            profile = null;
            bitmap = null;
            name = null;
            id = null;
        }
    }

    private class CredentialsCallback implements ProfileDatabase.GetCaseProfileCallback {

        @Override
        public void onSuccess(CaseProfile caseProfile) {
            newCase = caseProfile;
            liveDataRepo.setValue(caseProfile);
            liveDataRepoStatus.setValue(statusRepo.SUCCESS);
        }

        @Override
        public void onError(final int codeError) {
            liveDataRepoStatus.setValue(statusRepo.INTERNET_ERROR);
        }

        @Override
        public void onNotFound() {
            liveDataRepoStatus.postValue(statusRepo.PROFILE_END);
        }
    }

    public enum statusRepo {
        SUCCESS,
        INTERNET_ERROR,
        PROFILE_END
    }
}
