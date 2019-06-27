package com.example.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameTV, userEmailTV, userPhoneTV;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userNameTV = findViewById(R.id.userNameTVPAID);
        userEmailTV = findViewById(R.id.userEmailTVPAID);
        userPhoneTV = findViewById(R.id.userPhoneTVPAID);

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
                    String user_Email = data.child("email").getValue().toString();
                    String user_Phone = data.child("phone").getValue().toString();

                    userNameTV.setText(user_Name);
                    userEmailTV.setText(user_Email);
                    userPhoneTV.setText(user_Phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void profileActivityBackBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
