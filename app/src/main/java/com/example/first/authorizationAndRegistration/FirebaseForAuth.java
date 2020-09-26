package com.example.first.authorizationAndRegistration;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

class FirebaseForAuth {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context context;

    private FirebaseToLocalbaseData firebaseToLocalbaseData;

    FirebaseForAuth(Context context){
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        startListening();
        mAuth.addAuthStateListener(mAuthListener);

        firebaseToLocalbaseData = new FirebaseToLocalbaseData(context);
    }

    interface Auth {
        void goToAccount();
    }

    interface Toasts {
        void makeToast(String toast);
    }

    void startSignIn(String email, String password) {
        if ((email.equals("")) || (password.equals(""))) {
            ((Toasts)context).makeToast("Fields are empty");

            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    ((Toasts)context).makeToast("Sign in problem");
                }
                else {
                    firebaseToLocalbaseData.pushDate();
                }
            }
        });
    }

    private void startListening(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("information", "onAuthStateChanged in autorisation");
                if (firebaseAuth.getCurrentUser() != null) {
                    ((Auth)context).goToAccount(); // Start account activity cause user != null
                }
            }
        };
    }
}
