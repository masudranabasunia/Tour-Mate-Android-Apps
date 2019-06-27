package com.example.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView tripNameTDA, tripDescriptionTDA, tripStartDateTDA, tripEndDateTDA, tripBudgetTDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tripNameTDA = findViewById(R.id.tripNameTVTDAID);
        tripDescriptionTDA = findViewById(R.id.tripDescriptionTVTDAID);
        tripStartDateTDA = findViewById(R.id.tripStartDateTVTDAID);
        tripEndDateTDA = findViewById(R.id.tripEndDateTVTDAID);
        tripBudgetTDA = findViewById(R.id.tripBudgetTVTDAID);

        String tId = getIntent().getStringExtra("key");

        Query query = FirebaseDatabase.getInstance().getReference("Trips")
                .orderByChild("trip_id")
                .equalTo(tId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    TripInfo tripInfo = data.getValue(TripInfo.class);

                    String trp_Name = data.child("trpName").getValue().toString();
                    String trp_Description = data.child("trpDescription").getValue().toString();
                    String trp_StartDate = data.child("trpStartDate").getValue().toString();
                    String trp_EndDate = data.child("trpEndDate").getValue().toString();
                    String trp_Budget = data.child("trpBudget").getValue().toString();
                    tripNameTDA.setText(trp_Name);
                    tripDescriptionTDA.setText(trp_Description);
                    tripStartDateTDA.setText(trp_StartDate);
                    tripEndDateTDA.setText(trp_EndDate);
                    tripBudgetTDA.setText(trp_Budget+" tk");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void tripActivityDetailsBackBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
