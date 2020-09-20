package com.example.first.AccountEdit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.first.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditActivityRepo {
    private static final String TAG = "EditAccountActivity";
    private static final int INITIAL_COMPRESS_QUALITY = 100;
    private final static float PREVIEW_IMG_QUALITY = 600.0f;
    private final static float FULL_IMG_QUALITY = 800.0f;

    private ProfileCash mProfileCash;

    private MutableLiveData<AvatarImage> userImage = new MutableLiveData<>();
    private MutableLiveData<ProfileInfo> userInfo = new MutableLiveData<>();

    private DatabaseReference userProfileRef;
    private StorageReference userStorageRef;

    private static final EditActivityRepo sInstance = new EditActivityRepo();
    private EditActivityRepo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "EditActivityRepo: assert user != null;");
        assert user != null;
        userProfileRef = FirebaseDatabase.getInstance().getReference("Profiles").child(user.getUid());
        userStorageRef = FirebaseStorage.getInstance().getReference().child("Profiles").child(user.getUid());

        mProfileCash = ProfileCash.getInstance();
    }

    public static EditActivityRepo getInstance() {
        return sInstance;
    }

    LiveData<AvatarImage> getUserImage() {
        return userImage;
    }

    LiveData<ProfileInfo> getUserInfo() {
        return userInfo;
    }

    void refreshUserCash() {
        // проверка, есть ли что то в кэше - потом сделать проверку нормальной (не за счет вспомогательной пер. isEmpty)
        if (mProfileCash.isEmpty) {
            Log.d(TAG, "refreshUserCash: mProfileCash.isEmpty == true");
            updateUserCash();
            return;
        }
        Log.d(TAG, "refreshUserCash: mProfileCash.isEmpty == false");
        userInfo.setValue(mProfileCash.getProfileData());
        userImage.setValue(mProfileCash.getProfileImage());
    }

    private void updateUserCash() {
        // получение текущих данных с Database
        Log.d(TAG, "updateUserCash()");
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "updateUserCash: onDataChange()");
                // получение данных текущего пользователя
                Profile profile = dataSnapshot.getValue(Profile.class);

                if (profile == null) {
                    Log.d(TAG, "onDataChange: updateUserCash() profile is null");
                    return;
                }
                Log.d(TAG, "updateUserCash: onDataChange(): profile != null");
                mProfileCash.setProfileData(profile);
                userInfo.postValue(mProfileCash.getProfileData());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: updateUserCash()");
            }
        });

        // получение аватарки со Storage
        StorageReference avatarRef = userStorageRef.child("AvatarImage");
        final long BATCH_SIZE = 1024 * 1024; // 1 mb
        avatarRef.getBytes(BATCH_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d(TAG, "updateUserCash: AvatarImage getBytes onSuccess()");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap = resizeBitmap(bitmap, PREVIEW_IMG_QUALITY);

                mProfileCash.setAvatarBitmap(bitmap);
                userImage.postValue(mProfileCash.getProfileImage());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "updateUserCash: AvatarImage getBytes onFailure()");
            }
        });
    }

    void updateAvatarImageCashe(Bitmap bitmap) {
        bitmap = resizeBitmap(bitmap, PREVIEW_IMG_QUALITY);
        
        // обновление кэша
        Log.d(TAG, "updateAvatarImageCashe: ");
        mProfileCash.setAvatarBitmap(bitmap);

        // запрос на обновление UI
        userImage.setValue(mProfileCash.getProfileImage());
    }

    void uploadAvatarImage() {
        // загрузка в firebase
        Log.d(TAG, "uploadAvatarImage: ");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mProfileCash.getAvatarBitmap().compress(Bitmap.CompressFormat.WEBP, INITIAL_COMPRESS_QUALITY, baos);
        byte[] bytes = baos.toByteArray();
        StorageReference ref = userStorageRef.child("AvatarImage");
        ref.putBytes(bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.d(TAG, "onComplete: in uploadAvatarImage()");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: in uploadAvatarImage()");
                e.printStackTrace();
            }
        });
    }

    void uploadProfileData(ProfileInfo profileInfo) {
        // обновление кэша
        mProfileCash.setProfileData(profileInfo);

        // загрузка в firebase
        userProfileRef.child("name").setValue(profileInfo.getName());
        userProfileRef.child("phone").setValue(profileInfo.getPhone());
        userProfileRef.child("breed").setValue(profileInfo.getBreed());
        userProfileRef.child("age").setValue(profileInfo.getAge());
        userProfileRef.child("country").setValue(profileInfo.getCountry());
        userProfileRef.child("city").setValue(profileInfo.getCity());
        userProfileRef.child("address").setValue(profileInfo.getAddress());
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, float maxResolution) {
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

        String getName() {
            return name;
        }

        String getEmail() {
            return email;
        }

        String getPhone() {
            return phone;
        }

        String getBreed() {
            return breed;
        }

        String getAge() {
            return age;
        }

        String getCountry() {
            return country;
        }

        String getCity() {
            return city;
        }

        String getAddress() {
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

        Bitmap getAvatarBitmap() {
            return avatarBitmap;
        }

        public void setAvatarBitmap(Bitmap avatarBitmap) {
            this.avatarBitmap = avatarBitmap;
        }
    }
}
