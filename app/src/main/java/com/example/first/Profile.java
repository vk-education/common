package com.example.first;

import android.widget.ProgressBar;

import java.util.ArrayList;

public class Profile {
    private String name;
    private String email;
    private String phone;
    private String breed;
    private String age;
    private String country;
    private String city;
    private String address;
    private String key;
    private ArrayList<String> likes;
    private ArrayList<Matches> matches;
    private ArrayList<String> seen;

    public Profile() {

    }

    public Profile (String name, String email, String phone,
                    String breed, String age, String country,
                    String city, String address, String key,
                    ArrayList<String> likes, ArrayList<Matches> matches, ArrayList<String> seen) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.breed = breed;
        this.age = age;
        this.country = country;
        this.city = city;
        this.address = address;
        this.key = key;
        this.likes = likes;
        this.matches = matches;
        this.seen = seen;
    }


    // getters
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

    public String getKey() {
        return key;
    }

    public ArrayList<String> getLikes() { return likes; }

    public ArrayList<String> getSeen() { return seen; }

    public ArrayList<Matches> getMatches() { return matches; }


    // setters
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

    public void setKey(String address) {
        this.key = key;
    }

    public void setLikes(ArrayList<String> likes) { this.likes = likes; }

    public void setSeen(ArrayList<String> seen) { this.seen = seen; }

    public void setMatches(ArrayList<Matches> matches) { this.matches = matches; }


    public static class Matches {
        private String id;
        private String name;
        private String seen;

        public void setId(String id) { this.id = id; }

        public void setName(String name) { this.name = name; }

        public void setSeen(String seen) { this.seen = seen; }

        public Matches() {
        }

        public Matches(String id, String name, String seen) {
            this.id = id;
            this.name = name;
            this.seen = seen;
        }

        public String getId() { return id; }

        public String getName() { return name; }

        public String getSeen() { return seen; }


        @Override
        public boolean equals(Object obj) {
            return ((Matches) obj).getId().equals(this.getId());
        }
    }
}

