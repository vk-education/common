package com.example.first.Account.Repositories;

import android.graphics.Bitmap;

import androidx.annotation.MainThread;

import com.example.first.Account.AccountEdit.EditActivityRepo;
import com.example.first.Profile;

public interface RepoDB {
     void getProfile(CallbackProfile callback);
     void getImage(CallbackImage callback);
     void setProfile(EditActivityRepo.ProfileInfo profile, CallbackUpload callback);
     void setImage(Bitmap image, CallbackUpload callback);
     void exit();

     interface CallbackProfile {
          @MainThread
          void onSuccess(Profile profile);
          void notFound();
          @MainThread
          void Error();
     }

     interface CallbackImage {
          @MainThread
          void onSuccess(Bitmap bitmap);
          void notFound();
          @MainThread
          void Error();
     }

     interface CallbackUpload {
          @MainThread
          void onSuccess();
          @MainThread
          void Error();
     }
}
