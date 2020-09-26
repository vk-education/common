package com.example.first.mainScreen.database;

import androidx.annotation.MainThread;

import com.example.first.mainScreen.repositories.InfoRepo;

public interface ProfileDatabase {
    int BAD_INTERNET = 1;
    int NOT_ENTER = 2;

    void getMyCaseProfile(GetCaseProfileCallback caseProfileCallback);
    void getCaseProfile(GetCaseProfileCallback caseCallback);
    void changeProfileByCase(InfoRepo.CaseProfile caseProfile);

    interface GetCaseProfileCallback {
        @MainThread
        void onSuccess(InfoRepo.CaseProfile caseProfile);

        @MainThread
        void onError(final int codeError);

        void onNotFound();
    }
}
