package com.example.first.AccountDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.first.Account.AccountEdit.EditActivityRepo;

@Entity(tableName = "myProfile")
public class ProfileEntity {
    public static final String DEFAULT_NUMBER = "def";

    @PrimaryKey
    @NonNull
    public String id = DEFAULT_NUMBER;
    public String name;
    public String email;
    public String phone;
    public String breed;
    public String age;
    public String country;
    public String city;
    public String address;

    public ProfileEntity() {
    }

    public ProfileEntity(EditActivityRepo.ProfileInfo profileInfo) {
        this.id = DEFAULT_NUMBER;
        this.name = profileInfo.getName();
        this.email = profileInfo.getEmail();
        this.phone = profileInfo.getPhone();
        this.breed = profileInfo.getBreed();
        this.age = profileInfo.getAge();
        this.country = profileInfo.getCountry();
        this.city = profileInfo.getCity();
        this.address = profileInfo.getAddress();
    }

    public ProfileEntity(String name, String email, String phone, String breed, String age, String country, String city, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.breed = breed;
        this.age = age;
        this.country = country;
        this.city = city;
        this.address = address;
    }
}
