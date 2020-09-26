package com.example.first.otherDogProf;

public class DogCache {
    private static DogCache accountCash;
    private DogRepository dogRepo;

    DogRepository getAccountRepo() {
        return dogRepo;
    }

    public static DogCache getInstance() {
        if (accountCash == null) {
            accountCash = new DogCache();
        }
        return accountCash;
    }

    private DogCache() {
        dogRepo = new DogRepository();
    }

}