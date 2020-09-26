package com.example.first.otherDogProf;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.first.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

class DogRepository {
    private static final String BRANCH = "Profiles";
    private static final String AVATAR_IMAGE = "AvatarImage";

    private MutableLiveData<Profile> profileLiveData = new MutableLiveData<>();
    private MutableLiveData<Bitmap> imageLiveData = new MutableLiveData<>();

    private DatabaseReference databaseProfile;
    private Profile profileData;
    private StorageReference storageRef;

    DogRepository() {
        databaseProfile = FirebaseDatabase.getInstance().getReference(BRANCH);
        storageRef = FirebaseStorage.getInstance().getReference().child(BRANCH);
    }

    LiveData getProfile(String id) {
        databaseProfile.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileData = dataSnapshot.getValue(Profile.class);
                profileLiveData.postValue(profileData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return profileLiveData;
    }

    LiveData getImage(String id) {
        final long ONE_MEGABYTE = 1024*1024;

        storageRef.child(id).child(AVATAR_IMAGE).getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap = resizeBitmap(bitmap);
                imageLiveData.postValue(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageLiveData.postValue(null);
            }
        });

        return imageLiveData;
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;
        float maxResolution = 600.0f;
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