package com.example.first.otherDogProf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.R;
import com.example.first.matches.MatchesActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class OtherDogActivity extends AppCompatActivity {

    private TextView name, phone, breed, age, city, address;
    private LinearLayout layoutPhone, layoutBreed, layoutAge, layoutCity, layoutAddress;
    private ImageView photoProfile;
    private MaterialToolbar exitBtn;

    String id;

    private void initView(){
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        breed = findViewById(R.id.breed);
        age = findViewById(R.id.age);
        city = findViewById(R.id.city);
        address = findViewById(R.id.address);
        photoProfile = findViewById(R.id.avatar);
        layoutPhone = findViewById(R.id.layout_telephone);
        layoutBreed = findViewById(R.id.layout_breed);
        layoutAge = findViewById(R.id.layout_age);
        layoutCity = findViewById(R.id.layout_city);
        layoutAddress = findViewById(R.id.add);
        exitBtn = findViewById(R.id.topAppBar);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtherDogActivity.this, MatchesActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_dog);
        id = getIntent().getStringExtra("id");
       // DogCache dogCache = new DogCache(id);

        initView();

        DogViewModel dogViewModel = new ViewModelProvider(this).get(DogViewModel.class);

        dogViewModel.getProfileData().observe(this, new Observer<DogViewModel.ProfileData>() {
            @Override
            public void onChanged(DogViewModel.ProfileData profileData) {
                if ( profileData.getName() != null && !profileData.getName().equals("") ){
                    name.setText(profileData.getName());
                    exitBtn.setTitle(profileData.getName() + " likes you too!");
                } else
                { name.setVisibility(View.GONE);
                  exitBtn.setTitle(profileData.getName() + " likes you too!"); }

                if (profileData.getBreed() != null && !profileData.getBreed().equals("") ){
                    breed.setText(profileData.getBreed());
                } else
                    layoutBreed.setVisibility(View.GONE);

                if (profileData.getAge() != null && !profileData.getAge().equals("") ){
                    age.setText(profileData.getAge());
                } else
                    layoutAge.setVisibility(View.GONE);

                if (profileData.getCity() != null && !profileData.getCity().equals("") ){
                    city.setText(profileData.getCity());
                } else
                   layoutCity.setVisibility(View.GONE);

                if (profileData.getPhone() != null && !profileData.getPhone().equals("") ){
                    phone.setText(profileData.getPhone());
                } else
                    layoutPhone.setVisibility(View.GONE);

                if (profileData.getAddress() != null && !profileData.getAddress().equals("") ){
                    address.setText(profileData.getAddress());
                } else
                    layoutAddress.setVisibility(View.GONE);
            }
        });

        dogViewModel.getUserImage().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap != null)
                    photoProfile.setImageBitmap(bitmap);
                else
                    photoProfile.setImageResource(R.drawable.dog);
            }
        });

        dogViewModel.getData(id);
    }

}