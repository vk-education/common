package com.example.first.mainScreen.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.first.Account.AccountActivity;
import com.example.first.matches.MatchesActivity;
import com.example.first.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    MainViewModel mViewModel;

    private void InitView() {

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.cards);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this, AccountActivity.class));
                        return true;
                    case R.id.matches:
                        startActivity(new Intent(MainActivity.this, MatchesActivity.class));
                        return true;
                    case R.id.cards:
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mViewModel.getProfile().observe(this, new Observer<MainViewModel.UIInfo>() {
            @Override
            public void onChanged(MainViewModel.UIInfo dataProfile) {

                DogFragment fragment = DogFragment.newInstance(dataProfile.infoProfile, dataProfile.mainImageUser);
                fragment.setOnSwipeListener(new DogFragment.Listener() {
                    @Override
                    public void swipeLeft() {
                        mViewModel.dislike();
                    }

                    @Override
                    public void swipeRight() {
                        mViewModel.like();
                    }
                });

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.dogFragment, fragment)
                        .commit();
            }
        });
    }
}
