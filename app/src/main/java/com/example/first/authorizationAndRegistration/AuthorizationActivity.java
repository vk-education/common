package com.example.first.authorizationAndRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.first.Account.AccountActivity;
import com.example.first.R;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AuthorizationActivity extends AppCompatActivity implements FirebaseForAuth.Auth, FirebaseForAuth.Toasts {

    private TextInputEditText mEmailField;
    private TextInputEditText mPasswordField;
    private Button mLoginButton;

    public void InitView(){
        mEmailField = findViewById(R.id.emailFieldInp);
        mPasswordField = findViewById(R.id.passwordFieldInp);
        mLoginButton = findViewById(R.id.loginBtn);
        Button mRegistrationButton = findViewById(R.id.registrationBtn);
        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthorizationActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                AuthorizationActivity.this.finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        InitView();

        final FirebaseForAuth firebase = new FirebaseForAuth(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(mEmailField.getText()).toString();
                String password = Objects.requireNonNull(mPasswordField.getText()).toString();
                firebase.startSignIn(email,password);
            }
        });

    }

    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void goToAccount() {
        Intent intent = new Intent(AuthorizationActivity.this, AccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        AuthorizationActivity.this.finish();
        startActivity(intent);
    }

    @Override
    public void makeToast(String toast) {
        Toast.makeText(AuthorizationActivity.this, toast, Toast.LENGTH_LONG).show();
    }
}
