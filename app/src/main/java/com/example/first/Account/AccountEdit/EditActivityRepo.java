package com.example.first.Account.AccountEdit;

import android.graphics.Bitmap;
import com.example.first.Profile;


public class EditActivityRepo {
    private static final String TAG = "EditAccountActivity";

    public static class ProfileInfo {
        private String name;
        private String email;
        private String phone;
        private String breed;
        private String age;
        private String country;
        private String city;
        private String address;

        ProfileInfo(String name, String email, String phone, String breed, String age, String country, String city, String address) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.breed = breed;
            this.age = age;
            this.country = country;
            this.city = city;
            this.address = address;
        }

        public ProfileInfo() {
        }

        public ProfileInfo(Profile profile) {
            this.name = profile.getName();
            this.email = profile.getEmail();
            this.phone = profile.getPhone();
            this.breed = profile.getBreed();
            this.age = profile.getAge();
            this.country = profile.getCountry();
            this.city = profile.getCity();
            this.address = profile.getAddress();
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
    }

    public static class AvatarImage {
        private Bitmap avatarBitmap;

        AvatarImage(Bitmap avatarBitmap) {
            this.avatarBitmap = avatarBitmap;
        }

        public AvatarImage() {  }

        public Bitmap getAvatarBitmap() {
            return avatarBitmap;
        }

        public void setAvatarBitmap(Bitmap avatarBitmap) {
            this.avatarBitmap = avatarBitmap;
        }
    }
}
