package com.example.first.Account.AccountEdit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.first.Account.AccountActivity;
import com.example.first.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Objects;

public class AccountEditActivity extends AppCompatActivity {
    private static final String TAG = "EditAccountActivity";
    public final static int PICK_IMAGE_REQUEST = 71;


    EditActivityViewModel mViewModel;

    // layout references
    private TextInputEditText mNameField;
    private TextInputEditText mEmailField;
    private TextInputEditText mBreedField;
    private TextInputEditText mAgeField;
    private TextInputEditText mCountryField;
    private TextInputEditText mCityField;
    private TextInputEditText mAddressField;
    private TextInputEditText mPhoneField;
    private ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        UISetup();

        mViewModel = new ViewModelProvider(this).get(EditActivityViewModel.class);
        mViewModel.getProgress().observe(this, new Observer<EditActivityViewModel.ValidationStatus>() {
            @Override
            public void onChanged(EditActivityViewModel.ValidationStatus validationStatus) {
                // TODO: update textedit color
                switch (validationStatus) {
                    case SUCCESS:
                        Log.d(TAG, "Data validation success. Starting AccountActivity..");
                        startActivity(new Intent(AccountEditActivity.this, AccountActivity.class));
                        break;
                    case NONE:
                        Log.d(TAG, "Data validation status is NONE");
                        break;
                    case DEFAULT_FAILURE:
                        Toast.makeText(AccountEditActivity.this, "Unknown validation failure", Toast.LENGTH_SHORT).show();
                        break;
                    case NAME_FAILURE:
                        Toast.makeText(AccountEditActivity.this, "Name is too long", Toast.LENGTH_SHORT).show();
                        break;
                    case AGE_FAILURE:
                        Toast.makeText(AccountEditActivity.this, "Age should be a number", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mViewModel.getAvatarImage().observe(this, new Observer<EditActivityRepo.AvatarImage>() {
            @Override
            public void onChanged(EditActivityRepo.AvatarImage avatarImage) {
                // update avatar image
                Log.d(TAG, "getAvatarImage onChanged()");
                imgPreview.setImageBitmap(avatarImage.getAvatarBitmap());
            }
        });

        mViewModel.getProfileInfo().observe(this, new Observer<EditActivityRepo.ProfileInfo>() {
            @Override
            public void onChanged(EditActivityRepo.ProfileInfo profileInfo) {
                Log.d(TAG, "getProfileInfo onChanged()");
                // update text fields
                mNameField.setText(profileInfo.getName());
                mEmailField.setText(profileInfo.getEmail());
                mBreedField.setText(profileInfo.getBreed());
                mAgeField.setText(profileInfo.getAge());
                mCountryField.setText(profileInfo.getCountry());
                mCityField.setText(profileInfo.getCity());
                mAddressField.setText(profileInfo.getAddress());
                mPhoneField.setText(profileInfo.getPhone());
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        Log.d(TAG, "User id: " + user.getUid());

        // getData either from cash or from firebase
        Log.d(TAG, "mViewModel.getData();");
        mViewModel.getData();
    }



    private void UISetup() {
        // init views
        mNameField = findViewById(R.id.nameFieldInp);
        mEmailField = findViewById(R.id.emailFieldInp);
        mBreedField = findViewById(R.id.dogBreedFieldInp);
        mAgeField = findViewById(R.id.dogAgeFieldInp);
        mCountryField = findViewById(R.id.countryFieldInp);
        mCityField = findViewById(R.id.cityFieldInp);
        mAddressField = findViewById(R.id.addressFieldInp);
        mPhoneField = findViewById(R.id.phoneFieldInp);
        Button doneBtn = findViewById(R.id.saveBtn);
        Button changeProfilePhotoBtn = findViewById(R.id.changePhotoBtn);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        imgPreview = findViewById(R.id.imageView);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // убрать создание здесь структуры - чтобы структура формировалась уже во ViewModel
                EditActivityRepo.ProfileInfo profileInfo = new EditActivityRepo.ProfileInfo(
                        Objects.requireNonNull(mNameField.getText()).toString(),
                        Objects.requireNonNull(mEmailField.getText()).toString(),
                        Objects.requireNonNull(mPhoneField.getText()).toString(),
                        Objects.requireNonNull(mBreedField.getText()).toString(),
                        Objects.requireNonNull(mAgeField.getText()).toString(),
                        Objects.requireNonNull(mCountryField.getText()).toString(),
                        Objects.requireNonNull(mCityField.getText()).toString(),
                        Objects.requireNonNull(mAddressField.getText()).toString()
                );
                mViewModel.uploadProfileData(profileInfo);
            }
        });

        changeProfilePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "UISetup: NavigationOnClickListener is set");
            topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AccountEditActivity.this, com.example.first.Account.AccountActivity.class));
                }
            });
        }

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQUEST);
        // When user chose photo - onActivityResult() is called
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // set chosen photo to imageview
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), filepath));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                }

                // запрос на загрузку фото в кэш и firebase + загрузка в imageview
                Log.d(TAG, "onActivityResult: mViewModel.uploadAvatarImage()");
                mViewModel.uploadAvatarImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Failed to load photo", Toast.LENGTH_SHORT).show();
        }
    }
}
