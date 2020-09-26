package com.example.first.Account.Repositories;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.first.Account.AccountEdit.EditActivityRepo;
import com.example.first.Profile;

public class CompositeRepo implements RepoDB {
    private static final String TAG = "Repo";
    private LocalRepo localRepo;
    private AccountRepo accountRepo;

    public CompositeRepo(Context context) {
        localRepo = new LocalRepo(context);
        accountRepo = new AccountRepo();
    }


    @Override
    public void getProfile(final CallbackProfile callback) {
        localRepo.getProfile(new CallbackProfile() {
            @Override
            public void onSuccess(Profile profile) {
                callback.onSuccess(profile);
            }

            @Override
            public void notFound() {
                accountRepo.getProfile(callback);
            }

            @Override
            public void Error() {
                accountRepo.getProfile(callback);
            }
        });
    }

    @Override
    public void getImage(final CallbackImage callback) {
        localRepo.getImage(new CallbackImage() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                callback.onSuccess(bitmap);
            }

            // если в базе фото нет, то при загрузке и файрбейс, еще и кладем в базу
            @Override
            public void notFound() {
                accountRepo.getImage(new CallbackImage() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        callback.onSuccess(bitmap);
                        setImage(bitmap, new CallbackUpload() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "CallbackUpload onSuccess");
                            }

                            @Override
                            public void Error() {
                                Log.d(TAG, "CallbackUpload Error");
                            }
                        });
                    }

                    @Override
                    public void notFound() {
                        callback.notFound();
                    }

                    @Override
                    public void Error() {
                        callback.Error();
                    }
                });
            }

            @Override
            public void Error() {
                accountRepo.getImage(callback);
            }
        });
    }

    @Override
    public void setProfile(final EditActivityRepo.ProfileInfo profile, final CallbackUpload callback) {
        localRepo.setProfile(profile, new CallbackUpload() {
            @Override
            public void onSuccess() {
                accountRepo.setProfile(profile, callback);
            }

            @Override
            public void Error() {
                accountRepo.setProfile(profile, callback);
            }
        });
    }

    @Override
    public void setImage(final Bitmap image, final CallbackUpload callback) {
        final Bitmap resizedImage = resizeBitmap(image);
        localRepo.setImage(resizedImage, new CallbackUpload() {
            @Override
            public void onSuccess() {
                accountRepo.setImage(resizedImage, callback);
            }

            @Override
            public void Error() {
                Log.d(TAG, "localRepo error");
                accountRepo.setImage(resizedImage, callback);
            }
        });
    }

    @Override
    public void exit() {
        localRepo.exit();
        accountRepo.exit();
    }

    static Bitmap resizeBitmap(Bitmap bitmap) {
        float maxResolution = 600f;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / width;
                newHeight = (int) (height * rate);
                newWidth = (int) maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / height;
                newWidth = (int) (width * rate);
                newHeight = (int) maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
}
