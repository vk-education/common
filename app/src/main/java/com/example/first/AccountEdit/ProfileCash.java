package com.example.first.AccountEdit;

import android.graphics.Bitmap;

import com.example.first.Profile;

public class ProfileCash {
    private static ProfileCash mProfileCash;

    public boolean isEmpty; // временное решение, для проверки, пустой ли кэш

    private Bitmap avatarBitmap;
    private String name;
    private String email;
    private String phone;
    private String breed;
    private String age;
    private String country;
    private String city;
    private String address;

    public static ProfileCash getInstance() {
        if (mProfileCash == null) {
            mProfileCash = new ProfileCash();
        }
        return mProfileCash;
    }

    private ProfileCash() {
        isEmpty = true;
    }

    private ProfileCash(String name, String email, String phone, String breed, String age, String country, String city, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.breed = breed;
        this.age = age;
        this.country = country;
        this.city = city;
        this.address = address;
        isEmpty = false;
    }
    public void setProfileData(Profile profileInfo) {
        this.name = profileInfo.getName();
        this.email = profileInfo.getEmail();
        this.breed = profileInfo.getBreed();
        this.age = profileInfo.getAge();
        this.country = profileInfo.getCountry();
        this.city = profileInfo.getCity();
        this.address = profileInfo.getAddress();
        this.phone = profileInfo.getPhone();
        isEmpty = false;
    }

    public void setProfileData(EditActivityRepo.ProfileInfo profileInfo) {
        this.name = profileInfo.getName();
        this.email = profileInfo.getEmail();
        this.phone = profileInfo.getPhone();
        this.breed = profileInfo.getBreed();
        this.age = profileInfo.getAge();
        this.country = profileInfo.getCountry();
        this.city = profileInfo.getCity();
        this.address = profileInfo.getAddress();
        isEmpty = false;
    }

    public void setAvatarBitmap(Bitmap avatarBitmap) {
        this.avatarBitmap = avatarBitmap;
        isEmpty = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EditActivityRepo.ProfileInfo getProfileData() {
        EditActivityRepo.ProfileInfo profileInfo = new EditActivityRepo.ProfileInfo(
                name, email, phone, breed, age, country, city, address
        );
        return profileInfo;
    }

    public EditActivityRepo.AvatarImage getProfileImage() {
        EditActivityRepo.AvatarImage avatarImage = new EditActivityRepo.AvatarImage(
                avatarBitmap
        );
        return avatarImage;
    }

    public Bitmap getAvatarBitmap() {
        return avatarBitmap;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBreed() {
        return breed;
    }

    public String getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }
}