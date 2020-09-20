package com.example.first.mainScreen.database.local;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Credential {
    @NonNull
    @PrimaryKey
    public String id;
    public String name;
    public String email;
    public String phone;
    public String breed;
    public String age;
    public String country;
    public String city;
    public String address;

    @TypeConverters({ConverterData.class})
    public Bitmap bitmap;

    public Credential() {
    }


    public Credential(@NonNull String id, String name, String email, String phone, String breed, String age, String country, String city, String address) {
        this.id = id;
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
