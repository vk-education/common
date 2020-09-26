package com.example.first.Account;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.first.Account.Repositories.RepoDB;
import com.example.first.Profile;

public class AccountViewModel extends AndroidViewModel {
    static final String DEFAULT_AGE = " y.o.";

    private MutableLiveData<ProfileData> userProfile = new MutableLiveData<>();
    private MutableLiveData<Bitmap> userImage = new MutableLiveData<>();

    LiveData<ProfileData> getProfileData() {
        return userProfile;
    }

    LiveData<Bitmap> getUserImage() {
        return userImage;
    }

    public AccountViewModel(@NonNull Application application) {
        super(application);

        AccountCache.getInstance(getApplication()).getRepo().getProfile(new RepoDB.CallbackProfile() {
            @Override
            public void onSuccess(Profile profile) {
                ProfileData profileData = new ProfileData();
                profileData.setName(profile.getName());
                profileData.setBreed(profile.getBreed());
                profileData.setCity(profile.getCity());

                profileData.setAge(profile.getAge() + DEFAULT_AGE);
                String str = profile.getPhone();
                String phone;
                if (str.length() == 11) {
                    phone = str.charAt(0) + " ("
                            + str.charAt(1) + str.charAt(2) + str.charAt(3) + ") - "
                            + str.charAt(4) + str.charAt(5) + str.charAt(6) + " - "
                            + str.charAt(7) + str.charAt(8) + " - "
                            + str.charAt(9) + str.charAt(10);
                }
                else
                    phone = str;

                profileData.setPhone(phone);

                userProfile.setValue(profileData);
            }

            @Override
            public void notFound() {
                // TODO exit account
            }

            @Override
            public void Error() {
                // TODO message for user
            }
        });

        AccountCache.getInstance(getApplication()).getRepo().getImage(new RepoDB.CallbackImage() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                userImage.setValue(bitmap);
            }

            @Override
            public void notFound() {
                 userImage.postValue(null);
            }

            @Override
            public void Error() {
                userImage.setValue(null);
            }
        });

    }

    void exit() {
        AccountCache.getInstance(getApplication()).getRepo().exit();
    }

    static class ProfileData {
        public String name;
        private String phone;
        private String breed;
        private String age;
        private String city;

        public ProfileData(String name, String phone, String breed, String age, String city) {
            this.name = name;
            this.phone = phone;
            this.breed = breed;
            this.age = age;
            this.city = city;
        }

        ProfileData() {
        }

        public String getName() {
            return name;
        }

        String getPhone() {
            return phone;
        }

        String getBreed() {
            return breed;
        }

        String getAge() {
            return age;
        }

        String getCity() {
            return city;
        }

        public void setName(String name) {
            this.name = name;
        }

        void setPhone(String phone) {
            this.phone = phone;
        }

        void setBreed(String breed) {
            this.breed = breed;
        }

        void setAge(String age) {
            this.age = age;
        }

        void setCity(String city) {
            this.city = city;
        }
    }
}
