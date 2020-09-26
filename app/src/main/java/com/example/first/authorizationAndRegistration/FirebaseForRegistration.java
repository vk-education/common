package com.example.first.authorizationAndRegistration;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.first.Profile;
import com.example.first.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

class FirebaseForRegistration {
    private FirebaseAuth mAuth;
    private FirebaseAuth.IdTokenListener mAuthListener;
    private Context context;
    private StorageReference storageRef;
    private String email;

    private FirebaseToLocalbaseData firebaseToLocalbaseData;

    FirebaseForRegistration(Context context){
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        startListening();
        mAuth.addIdTokenListener(mAuthListener);

        firebaseToLocalbaseData = new FirebaseToLocalbaseData(context);
    }

    interface Auth {
        void goToAccount();
    }

    interface Toasts {
        void makeToast(String toast);
    }


    void startRegister(String email, String password) {
        this.email = email;
        if ((email.equals("")) || (password.equals(""))) {
            ((Toasts)context).makeToast("Fields are empty");

            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    ((FirebaseForRegistration.Toasts)context).makeToast("Sign in problem");
                }
                else {
                    databaseFilling();

                    firebaseToLocalbaseData.pushDate();
                }
            }
        });
    }

    private void startListening() {
        mAuthListener = new FirebaseAuth.IdTokenListener() {
            @Override
            public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d("information", "onIdTokenChanged in registration");
                    ((FirebaseForRegistration.Auth) context).goToAccount(); // Start account activity cause user != null
                }
            }
        };

    }

    private void databaseFilling() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Profile profile = new Profile();
        String str = "";
        profile.setName(str);
        profile.setEmail(email);
        profile.setPhone(str);
        profile.setAddress(str);
        profile.setAge(str);
        profile.setCountry(str);
        profile.setCity(str);
        profile.setBreed(str);
        ArrayList<String> seen = new ArrayList<>();
        assert user != null;
        seen.add(user.getUid());
        profile.setSeen(seen);

        myRef.child("Profiles").child(user.getUid()).setValue(profile);

        myRef.child("IdProfiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> idProfiles = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren ()) {
                    idProfiles.add(postSnapshot.getValue(String.class));
                }
                idProfiles.add(user.getUid());
                myRef.child("IdProfiles").setValue(idProfiles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // uploading default avatar to firebase storage
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(R.drawable.default_avatar)
                + '/' + context.getResources().getResourceTypeName(R.drawable.default_avatar) + '/' + context.getResources().getResourceEntryName(R.drawable.default_avatar) );
        StorageReference ref = storageRef.child("Profiles").child(user.getUid()).child("AvatarImage");
        ref.putFile(imageUri);
    }

}
