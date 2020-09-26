package com.example.first.mainScreen.database;

import android.content.Context;
import android.util.Log;

import com.example.first.mainScreen.repositories.InfoRepo;

public class CompositeDatabase implements ProfileDatabase {
    private ProfileDatabase localDatabase;
    private ProfileDatabase networkDatabase;

    public CompositeDatabase(Context context) {
        networkDatabase = new NetworkDatabase();
        localDatabase = new LocalDatabase(context, networkDatabase);
    }

    @Override
    public void getMyCaseProfile(final GetCaseProfileCallback caseProfileCallback) {
        localDatabase.getMyCaseProfile(new GetCaseProfileCallback() {
            @Override
            public void onSuccess(InfoRepo.CaseProfile caseProfile) {
                caseProfileCallback.onSuccess(caseProfile);
            }

            @Override
            public void onError(int codeError) {
                caseProfileCallback.onError(codeError);
            }

            @Override
            public void onNotFound() {
                networkDatabase.getMyCaseProfile(caseProfileCallback);
            }
        });
    }

    @Override
    public void getCaseProfile(final GetCaseProfileCallback caseCallback) {
        localDatabase.getCaseProfile(new GetCaseProfileCallback() {
            @Override
            public void onSuccess(InfoRepo.CaseProfile caseProfile) {
                caseCallback.onSuccess(caseProfile);
            }

            @Override
            public void onError(int codeError) {
                caseCallback.onError(codeError);
            }

            @Override
            public void onNotFound() {
                networkDatabase.getCaseProfile(new GetCaseProfileCallback() {
                    @Override
                    public void onSuccess(InfoRepo.CaseProfile caseProfile) {
                        Log.d("information", "onSuccess in network");
                        ((LocalDatabase)localDatabase).pullProfile();
                        Log.d("information", "pullProfile");
                        caseCallback.onSuccess(caseProfile);
                    }

                    @Override
                    public void onError(int codeError) {
                        caseCallback.onError(codeError);
                    }

                    @Override
                    public void onNotFound() {
                        caseCallback.onNotFound();
                    }
                });
            }
        });
    }

    @Override
    public void changeProfileByCase(InfoRepo.CaseProfile caseProfile) {
        localDatabase.changeProfileByCase(caseProfile);
    }
}
