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

    public MainViewModel(@NonNull Application application) {
        super(application);
        informationLiveData = InfoRepo.getInstance(getApplication()).getFirstCaseProfile();
        currentUser.addSource(informationLiveData, observer);
    }

    public LiveData<UIInfo> getProfile() {
        return currentUser;
    }

    public void dislike() {
        informationLiveData = InfoRepo.getInstance(getApplication()).getCaseProfile();
        currentUser.addSource(informationLiveData, observer);
    }

    public void like() {
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
                if (userInformation.profile.getName() != null)
                    infoUser += userInformation.profile.getName();
                if (userInformation.profile.getAge() != null)
                    infoUser += userInformation.profile.getAge();
                if (userInformation.profile.getCity() != null)
                    infoUser += userInformation.profile.getCity();

                bitmap = userInformation.bitmap;

                uiInfo = new UIInfo(infoUser, bitmap);
            }

            currentUser.postValue(uiInfo);
            currentUser.removeSource(informationLiveData);
        }
    };

    static class UIInfo {
        public String infoProfile;
        public Bitmap mainImageUser;

        public UIInfo(String infoProfile, Bitmap mainImageUser) {
            this.infoProfile = infoProfile;
            this.mainImageUser = mainImageUser;
        }
    }
}
