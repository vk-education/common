package com.example.first.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.Account.AccountEdit.AccountEditActivity;
import com.example.first.R;
import com.example.first.authorizationAndRegistration.AuthorizationActivity;
import com.example.first.mainScreen.ui.MainActivity;
import com.example.first.matches.MatchesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {
    private TextView labName, labPhone, labBreed, labAge, labCity;
    private ImageView photoProfile;
    private LinearLayout layoutPhone, layoutBreed, layoutAge, layoutCity;
    private String str = "";

    private AccountViewModel accountViewModel;

    private void init() {
        labName = findViewById(R.id.i_name);
        labPhone = findViewById(R.id.i_phone);
        labBreed = findViewById(R.id.i_breed);
        labAge = findViewById(R.id.i_age);
        labCity = findViewById(R.id.i_city);

        photoProfile = findViewById(R.id.photo_profile);

        layoutPhone = findViewById(R.id.layout_telephone);
        layoutBreed = findViewById(R.id.layout_breed);
        layoutAge = findViewById(R.id.layout_age);
        layoutCity = findViewById(R.id.layout_city);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.testNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        return true;
                    case R.id.matches:
                        startActivity(new Intent(AccountActivity.this, MatchesActivity.class));
                        return true;
                    case R.id.cards:
                        startActivity(new Intent(AccountActivity.this, MainActivity.class));
                        return true;
                }
                return false;
            }
        });

        ImageButton accountEdit = findViewById(R.id.editActivity);
        accountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, AccountEditActivity.class));
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        init();

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        accountViewModel.getProfileData().observe(this, new Observer<AccountViewModel.ProfileData>() {
            @Override
            public void onChanged(AccountViewModel.ProfileData profileData) {
                if ( profileData.getName() != null && !profileData.getName().equals(str) ){
                    labName.setText(profileData.getName());
                    labName.setVisibility(View.VISIBLE);
                } else
                    labName.setVisibility(View.GONE);

                if (profileData.getBreed() != null && !profileData.getBreed().equals(str) ){
                    labBreed.setText(profileData.getBreed());
                    labBreed.setVisibility(View.VISIBLE);
                } else
                    layoutBreed.setVisibility(View.GONE);

                if (profileData.getAge() != null && !profileData.getAge().equals(AccountViewModel.DEFAULT_AGE)){
                    labAge.setText(profileData.getAge());
                    labAge.setVisibility(View.VISIBLE);
                } else
                    layoutAge.setVisibility(View.GONE);

                if (profileData.getCity() != null && !profileData.getCity().equals(str) ){
                    labCity.setText(profileData.getCity());
                    labCity.setVisibility(View.VISIBLE);
                } else
                    layoutCity.setVisibility(View.GONE);

                if (profileData.getPhone() != null && !profileData.getPhone().equals(str) ){
                    labPhone.setText(profileData.getPhone());
                    labPhone.setVisibility(View.VISIBLE);
                } else
                    layoutPhone.setVisibility(View.GONE);
            }
        });

        accountViewModel.getUserImage().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap != null)
                    photoProfile.setImageBitmap(bitmap);
                else
                    photoProfile.setImageResource(R.drawable.default_avatar);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            accountViewModel.exit();

            Intent intent = new Intent(AccountActivity.this, AuthorizationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }

}
