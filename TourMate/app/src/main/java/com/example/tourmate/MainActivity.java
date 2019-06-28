package com.example.tourmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView actionBarTV;
    private CardView tripsCardView, momentsCardView, walletCardView, weatherCardView, contactCardView, taskCardView;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uId;
    private TextView userNameShowMATV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarTV = findViewById(R.id.actionBarTVID);
        actionBarTV.setText("Tour Mate");

        tripsCardView = findViewById(R.id.tripsCardViewID);
        momentsCardView = findViewById(R.id.momentsCardViewID);
        walletCardView = findViewById(R.id.walletCardViewID);
        weatherCardView = findViewById(R.id.weatherCardViewID);
        contactCardView = findViewById(R.id.contactCardViewID);
        taskCardView = findViewById(R.id.taskCardViewID);

        tripsCardView.setOnClickListener(this);
        momentsCardView.setOnClickListener(this);
        walletCardView.setOnClickListener(this);
        weatherCardView.setOnClickListener(this);
        contactCardView.setOnClickListener(this);
        taskCardView.setOnClickListener(this);

        userNameShowMATV = navigationView.getHeaderView(0).findViewById(R.id.userNameShowMATVID);

        user = mAuth.getInstance().getCurrentUser();
        uId = user.getUid();

        Query query = FirebaseDatabase.getInstance().getReference("User")
                .orderByChild("id")
                .equalTo(uId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    User userInfo = data.getValue(User.class);

                    String user_Name = data.child("name").getValue().toString();
                    userNameShowMATV.setText(user_Name);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profileMenuId){
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.signOutMenuId){
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.tripsCardViewID){
            Intent intent = new Intent(getApplicationContext(), TripActivity.class);
            startActivity(intent);
        }
        else if(v.getId()== R.id.momentsCardViewID){
            Intent intent = new Intent(getApplicationContext(), TripMemoryActivity.class);
            startActivity(intent);
        }
        else if(v.getId()== R.id.walletCardViewID){
            Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
            startActivity(intent);
        }
        else if(v.getId()== R.id.weatherCardViewID){
            //Intent intent = getPackageManager().getLaunchIntentForPackage("com.androstock.myweatherapp");
            Intent intent = new Intent(getApplicationContext(), MyWeatherActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId()== R.id.taskCardViewID){
            Intent intent = new Intent(getApplicationContext(), TaskPlanActivity.class);
            startActivity(intent);
        }
        else if(v.getId()== R.id.contactCardViewID){
            Intent intent = new Intent(getApplicationContext(), TripContactActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
