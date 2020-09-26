package com.example.first.authorizationAndRegistration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.first.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

class FirebaseToLocalbaseData {
    private AuthRegDB authRegDB;

    FirebaseToLocalbaseData(Context context) {
        authRegDB = new AuthRegDB(context);
    }

    void pushDate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return;
        pushImageLocalDB(user.getUid());
        pushProfileLocalDB(user.getUid());
    }

    private void pushProfileLocalDB(@NonNull String id) {
        DatabaseReference databaseProfile = FirebaseDatabase.getInstance().getReference("Profiles");

        databaseProfile.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                authRegDB.setProfile(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void pushImageLocalDB(@NonNull String id) {
        final long ONE_MEGABYTE = 1024*1024;

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Profiles");

        storageRef.child(id).child("AvatarImage").getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap = resizeBitmap(bitmap);
                authRegDB.setImage(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
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
