package com.example.first.mainScreen.ui;

import android.app.Application;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.first.mainScreen.repositories.InfoRepo;

public class MainViewModel extends AndroidViewModel {
    private MediatorLiveData<UIInfo> currentUser = new MediatorLiveData<>();
    private LiveData<InfoRepo.CaseProfile> informationLiveData;
    private MediatorLiveData<status> statusLiveData = new MediatorLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        informationLiveData = InfoRepo.getInstance(getApplication()).getFirstCaseProfile();
        currentUser.addSource(informationLiveData, observer);

        LiveData<InfoRepo.statusRepo> statusRepoLiveData = InfoRepo.getInstance(getApplication()).getStatus();
        statusLiveData.addSource(statusRepoLiveData, new Observer<InfoRepo.statusRepo>() {
            @Override
            public void onChanged(InfoRepo.statusRepo statusRepo) {
                if (statusRepo == InfoRepo.statusRepo.SUCCESS)
                    statusLiveData.postValue(status.SUCCESS);
                else if (statusRepo == InfoRepo.statusRepo.INTERNET_ERROR)
                    statusLiveData.postValue(status.INTERNET_ERROR);
                else if (statusRepo == InfoRepo.statusRepo.PROFILE_END)
                    statusLiveData.postValue(status.PROFILE_END);
            }
        });
    }

    LiveData<UIInfo> getProfile() {
        return currentUser;
    }

    LiveData<status> getStatus() {
        return statusLiveData;
    }

    void dislike() {
        informationLiveData = InfoRepo.getInstance(getApplication()).getCaseProfile();
        currentUser.addSource(informationLiveData, observer);
    }

    void like() {
        InfoRepo.getInstance(getApplication()).processInformation();
        informationLiveData = InfoRepo.getInstance(getApplication()).getCaseProfile();
        currentUser.addSource(informationLiveData, observer);
    }

    private Observer<InfoRepo.CaseProfile> observer = new Observer<InfoRepo.CaseProfile>() {
        @Override
        public void onChanged(InfoRepo.CaseProfile userInformation) {
            String infoUser = "";
            Bitmap bitmap;
            UIInfo uiInfo;

            if ((userInformation == null) || (userInformation.profile == null)) {
                uiInfo = new UIInfo(null, null);
            }
            else {
                if ((userInformation.profile.getName() != null) && (!userInformation.profile.getName().equals("")))
                    infoUser += userInformation.profile.getName();
                if ((userInformation.profile.getAge() != null) && (!userInformation.profile.getAge().equals("")))
                    infoUser += ", " + userInformation.profile.getAge();
                if ((userInformation.profile.getCity() != null) && (!userInformation.profile.getCity().equals("")))
                    infoUser += ", " + userInformation.profile.getCity();

                bitmap = userInformation.bitmap;

                uiInfo = new UIInfo(infoUser, bitmap);
            }

            currentUser.postValue(uiInfo);
            currentUser.removeSource(informationLiveData);
        }
    };

    static class UIInfo {
        String infoProfile;
        Bitmap mainImageUser;

        UIInfo(String infoProfile, Bitmap mainImageUser) {
            this.infoProfile = infoProfile;
            this.mainImageUser = mainImageUser;
        }
    }

    enum status {
        SUCCESS,
        INTERNET_ERROR,
        PROFILE_END
    }
}
