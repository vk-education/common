package com.example.first.AccountEdit;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class EditActivityViewModel extends AndroidViewModel {
    private static final String TAG = "EditAccountActivity";
    private final static int NAME_MAX_LENGTH = 12;

    private MutableLiveData<ValidationStatus> mValidationState = new MutableLiveData<>();
    private MediatorLiveData<EditActivityRepo.AvatarImage> userProfileImage = new MediatorLiveData<>();
    private MediatorLiveData<EditActivityRepo.ProfileInfo> userProfileInfo = new MediatorLiveData<>();

    public EditActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // getProfile() returning liveData
    LiveData<ValidationStatus> getProgress() {
        return mValidationState;
    }

    LiveData<EditActivityRepo.AvatarImage> getAvatarImage() {
        return userProfileImage;
    }

    LiveData<EditActivityRepo.ProfileInfo> getProfileInfo() {
        return userProfileInfo;
    }

    void uploadProfileData(EditActivityRepo.ProfileInfo profileInfo) {
        // data validation
        try {
            Integer.parseInt(profileInfo.getAge());
        } catch (NumberFormatException nfe) {
            mValidationState.setValue(ValidationStatus.AGE_FAILURE);
            return;
        }
        if (profileInfo.getName().length() > NAME_MAX_LENGTH){
            mValidationState.setValue(ValidationStatus.NAME_FAILURE);
            return;
        }

        mValidationState.setValue(ValidationStatus.SUCCESS);
        // request data-upload to firebase
        EditActivityRepo.getInstance().uploadProfileData(profileInfo);
    }

    public enum ValidationStatus {
        NONE,
        SUCCESS,
        DEFAULT_FAILURE,
        NAME_FAILURE,
        AGE_FAILURE,
    }

    void uploadAvatarImage(Bitmap bitmap) {
        final LiveData<EditActivityRepo.AvatarImage> avatarImageLiveData = EditActivityRepo.getInstance().getUserImage();

        userProfileImage.addSource(avatarImageLiveData, new Observer<EditActivityRepo.AvatarImage>() {
            @Override
            public void onChanged(EditActivityRepo.AvatarImage avatarImage) {
                Log.d(TAG, "userProfileImage.addSource onChanged() in uploadAvatarImage()");
                userProfileImage.setValue(avatarImage);
                userProfileImage.removeSource(avatarImageLiveData);
            }
        });

        EditActivityRepo.getInstance().updateAvatarImageCashe(bitmap);
        EditActivityRepo.getInstance().uploadAvatarImage();
    }

    void getData() {
        // запршиваем у Repo данные для UI
        final LiveData<EditActivityRepo.AvatarImage> avatarImageLiveData = EditActivityRepo.getInstance().getUserImage();
        final LiveData<EditActivityRepo.ProfileInfo> profileInfoLiveData = EditActivityRepo.getInstance().getUserInfo();
        userProfileImage.addSource(avatarImageLiveData, new Observer<EditActivityRepo.AvatarImage>() {
            @Override
            public void onChanged(EditActivityRepo.AvatarImage avatarImage) {
                Log.d(TAG, "userProfileImage.addSource onChanged() in getData()");
                userProfileImage.setValue(avatarImage);
                userProfileImage.removeSource(avatarImageLiveData);
            }
        });
        userProfileInfo.addSource(profileInfoLiveData, new Observer<EditActivityRepo.ProfileInfo>() {
            @Override
            public void onChanged(EditActivityRepo.ProfileInfo profileInfo) {
                Log.d(TAG, "userProfileInfo.addSource onChanged()");
                userProfileInfo.setValue(profileInfo);
                userProfileInfo.removeSource(profileInfoLiveData);
            }
        });

        Log.d(TAG, "getData: refreshUserCash()");
        EditActivityRepo.getInstance().refreshUserCash();
    }

}
