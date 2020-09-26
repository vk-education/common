package com.example.first.Account.Repositories;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.first.Account.AccountEdit.EditActivityRepo;
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

public class AccountRepo implements RepoDB{
    private static final String BRANCH = "Profiles";
    private static final String AVATAR_IMAGE = "AvatarImage";
    private static final int INITIAL_COMPRESS_QUALITY = 100;

    private DatabaseReference databaseProfile;
    private FirebaseUser user;
    private Profile profileData;
    private StorageReference storageRef;

    AccountRepo() {
        getUser();
        databaseProfile = FirebaseDatabase.getInstance().getReference(BRANCH);
        storageRef = FirebaseStorage.getInstance().getReference().child(BRANCH);
    }

    @Override
    public void getProfile(final CallbackProfile callback) {
        if (user == null)
            getUser();

        databaseProfile.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileData = dataSnapshot.getValue(Profile.class);
                callback.onSuccess(profileData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.Error();
            }
        });
    }

    @Override
    public void getImage(final CallbackImage callback) {
        final long ONE_MEGABYTE = 1024*1024;

        if (user == null)
            getUser();

        storageRef.child(user.getUid()).child(AVATAR_IMAGE).getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap = CompositeRepo.resizeBitmap(bitmap);
                callback.onSuccess(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.Error();
            }
        });
    }

    public void exit() {
        FirebaseAuth.getInstance().signOut();
        user = null;
    }

    @Override
    public void setProfile(EditActivityRepo.ProfileInfo profile, CallbackUpload callback) {

        if (user == null)
            getUser();

        String userID = user.getUid();

        // TODO: данные разместить в отдельной папке, чтобы не было такого хардкода
        DatabaseReference ref = databaseProfile.child(userID);
        ref.child("name").setValue(profile.getName());
        ref.child("phone").setValue(profile.getPhone());
        ref.child("breed").setValue(profile.getBreed());
        ref.child("age").setValue(profile.getAge());
        ref.child("country").setValue(profile.getCountry());
        ref.child("city").setValue(profile.getCity());
        ref.child("address").setValue(profile.getAddress());

        callback.onSuccess();
    }

    @Override
    public void setImage(Bitmap image, final CallbackUpload callback) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.WEBP, INITIAL_COMPRESS_QUALITY, baos);
        byte[] bytes = baos.toByteArray();

        if (user == null)
            getUser();
        StorageReference avatarRef = storageRef.child(user.getUid()).child("AvatarImage");

        avatarRef.putBytes(bytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.Error();
                e.printStackTrace();
            }
        });
    }

    private void getUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
}
