package com.example.first.matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.first.Account.AccountActivity;
import com.example.first.otherDogProf.OtherDogActivity;
import com.example.first.R;
import com.example.first.mainScreen.ui.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MatchesActivity extends AppCompatActivity implements MyAdapter.RecyclerClickListener2 {

    private RecyclerView matchesRecycler;
    private MyAdapter myAdapter;

    public void InitView(){
        matchesRecycler = findViewById(R.id.matchesRecycler);
        matchesRecycler.setLayoutManager(new LinearLayoutManager(this));
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.matches);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(MatchesActivity.this, AccountActivity.class));
                        return true;
                    case R.id.matches:
                        return true;
                    case R.id.cards:
                        startActivity(new Intent(MatchesActivity.this, MainActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        InitView();

        MatchesViewModel model = new ViewModelProvider(this).get(MatchesViewModel.class); // creating the object that we'll get till Activity are closed
        // So, when screen orientation is changed, my MatchesViewModel will survive in provider
        model.init(); // taking the first data matchesMutableLiveData = Repository.getInstance().getMatches();
        model.getLiveData().observe(this, new Observer<ArrayList<UserModel>>() {
            @Override
            public void onChanged(ArrayList<UserModel> userModels) {
                myAdapter.notifyDataSetChanged(); // if the data is changed, adapter know this
            }
        });

        myAdapter = new MyAdapter(model.getLiveData().getValue(), this);
        matchesRecycler.setAdapter(myAdapter);
    }

    @Override
    public void itemClick2(String id) { // for clicking on RV items
        Intent intent = new Intent(MatchesActivity.this, OtherDogActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
