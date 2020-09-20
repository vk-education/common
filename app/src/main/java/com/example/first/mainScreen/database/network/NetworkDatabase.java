package com.example.first.mainScreen.database.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.first.Profile;
import com.example.first.mainScreen.database.ProfileDatabase;
import com.example.first.mainScreen.repositories.InfoRepo;
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

import java.util.ArrayList;

public class NetworkDatabase implements ProfileDatabase {
    private static final String BRANCH_NAME = "Profiles";
    private static final String BRANCH_ID_PROFILES = "IdProfiles";

    private DatabaseReference myRef;
    private FirebaseUser user;
    private StorageReference storageRef;

    private final long ONE_MEGABYTE = 1024 * 1024;

    private ArrayList<String> seen = null;
    private ArrayList<String> allId = null;

    private String myId;

    public NetworkDatabase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child(BRANCH_NAME);
    }

    private void getCaseById(final String id, final GetCaseProfileCallback caseCallback) {

        final InfoRepo.CaseProfile newInfo = new InfoRepo.CaseProfile();
        newInfo.id = id;
        if (id == null) {
            caseCallback.onNotFound();
            return;
        }

        myRef.child(BRANCH_NAME).child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        myRef.child(BRANCH_NAME).child(id).removeEventListener(this);

                        if (profile == null)
                            caseCallback.onNotFound();
                        else {
                            newInfo.profile = profile;
                            newInfo.name = newInfo.profile.getName();

                            storageRef.child(id).child("AvatarImage").
                                    getBytes(5 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bmpImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    bmpImage = resizeBitmap(bmpImage);

                                    newInfo.bitmap = bmpImage;

                                    caseCallback.onSuccess(newInfo);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    caseCallback.onError(BAD_INTERNET);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        caseCallback.onError(BAD_INTERNET);
                    }
                });
    }

    @Override
    public void changeProfileByCase(InfoRepo.CaseProfile caseProfile) {
        final InfoRepo.CaseProfile localCase = caseProfile;
        myRef.child(BRANCH_NAME).child(localCase.id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        myRef.child(BRANCH_NAME).child(localCase.id).removeEventListener(this);

                        profile.setSeen(changeStringParameters(
                                profile.getSeen(), localCase.profile.getSeen()
                        ));

                        profile.setLikes(changeStringParameters(
                                profile.getLikes(), localCase.profile.getLikes()
                        ));

                        profile.setMatches(changeMatches(
                                profile.getMatches(), localCase.profile.getMatches()
                        ));

                        myRef.child(BRANCH_NAME).child(localCase.id).setValue(profile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private ArrayList<Profile.Matches> changeMatches (ArrayList<Profile.Matches> mainList, ArrayList<Profile.Matches> list) {
        if (list == null)
            list = new ArrayList<>();
        if (mainList == null)
            mainList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (mainList.indexOf(list.get(i)) == -1)
                mainList.add(list.get(i));
        }

        return mainList;
    }

    private ArrayList<String> changeStringParameters (ArrayList<String> mainList, ArrayList<String> list) {
        if (list == null)
            list = new ArrayList<>();
        if (mainList == null)
            mainList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (mainList.indexOf(list.get(i)) == -1)
                mainList.add(list.get(i));
        }

        return mainList;
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        float maxResolution = (float) 400.0;    //edit 'maxResolution' to fit your need
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

    @Override
    public void getMyCaseProfile(GetCaseProfileCallback caseProfileCallback) {
        if (user == null) {
            caseProfileCallback.onError(NOT_ENTER);
            return;
        }
        else {
            myId = user.getUid();
        }
        getCaseById(myId, caseProfileCallback);
    }

    @Override
    public void getCaseProfile(final GetCaseProfileCallback getCaseProfileCallback) {
        getAllId(new AllIdCollBack() {
            @Override
            public void onSuccess(final ArrayList<String> allId) {
                getSeen(new SeenCollBack() {
                    @Override
                    public void onSuccess(ArrayList<String> seen) {
                        String id = null;

                        int i = 0;
                        while (i < allId.size() && (id == null)) {
                            if (seen.indexOf(allId.get(i)) == -1) {
                                id = allId.get(i);
                            }
                            i++;
                        }

                        if (id != null) {
                            seen.add(id); // добавление в просмотренные
                            Profile myProfile = new Profile();
                            myProfile.setSeen(seen);
                            InfoRepo.CaseProfile caseProfile = new InfoRepo.CaseProfile();
                            caseProfile.id = user.getUid();
                            caseProfile.profile = myProfile;
                            changeProfileByCase(caseProfile);
                        }

                        getCaseById(id, getCaseProfileCallback);
                    }

                    @Override
                    public void onError() {
                        getCaseProfileCallback.onError(BAD_INTERNET);
                    }
                });
            }

            @Override
            public void onError() {
                getCaseProfileCallback.onError(BAD_INTERNET);
            }
        });
    }

    private void getAllId(final AllIdCollBack allIdCollBack) {
        myRef.child(BRANCH_ID_PROFILES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allId = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren())
                            allId.add(data.getValue(String.class));

                        myRef.child(BRANCH_ID_PROFILES).removeEventListener(this);

                        allIdCollBack.onSuccess(allId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        allIdCollBack.onError();
                    }
                });
    }

    private void getSeen(final SeenCollBack seenCollBack) {
        myRef.child(BRANCH_NAME).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        seen = dataSnapshot.getValue(Profile.class).getSeen();
                        if (seen == null)
                            seen = new ArrayList<>();

                        myRef.child(BRANCH_NAME).child(user.getUid()).removeEventListener(this);

                        seenCollBack.onSuccess(seen);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        seenCollBack.onError();
                    }
                });

    }

    interface AllIdCollBack {
        void onSuccess(ArrayList<String> allId);
        void onError();
    }

    interface SeenCollBack {
        void onSuccess(ArrayList<String> seen);
        void onError();
    }
}
