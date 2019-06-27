package com.example.tourmate;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTA;
    private TripAdapter tripAdapter;
    private List<TripInfo> infoList;
    private ProgressBar progressBarTA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        progressBarTA = findViewById(R.id.progressBarTAID);

        recyclerViewTA = findViewById(R.id.recyclerViewTAID);
        infoList = new ArrayList<>();
        initRecyclerViewTA();
        getDataFromDBTA();
    }

    private void initRecyclerViewTA() {
        recyclerViewTA.setLayoutManager(new LinearLayoutManager(this));
        tripAdapter = new TripAdapter(infoList,this);
        recyclerViewTA.setAdapter(tripAdapter);
    }

    private void getDataFromDBTA() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getUid();

        Query query = FirebaseDatabase.getInstance().getReference("Trips")
                .orderByChild("userId")
                .equalTo(uId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    infoList.clear();
                    for(final DataSnapshot data: dataSnapshot.getChildren()){

                        TripInfo tripInfo = data.getValue(TripInfo.class);

                        TripInfo value = new TripInfo();
                        value.setTrip_id(data.getKey());

                        final String trp_Name = data.child("trpName").getValue().toString();
                        final String trp_Description = data.child("trpDescription").getValue().toString();
                        final String trp_StartDate = data.child("trpStartDate").getValue().toString();
                        final String trp_EndDate = data.child("trpEndDate").getValue().toString();
                        final String trp_Budget = data.child("trpBudget").getValue().toString();
                        //String trp_Description = data.child("trpDescription").getValue().toString();
                        //String trp_Budget = data.child("trpBudget").getValue().toString();

                        value.setTrpName(trp_Name);
                        value.setTrpStartDate(trp_StartDate);
                        value.setTrpEndDate(trp_EndDate);
                        value.getTrpName();
                        value.getTrpStartDate();
                        value.getTrpEndDate();
                        infoList.add(value);
                        tripAdapter.notifyDataSetChanged();

                        tripAdapter.setOnItemClickListener(new TripAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {
                                String tName = infoList.get(position).getTrpName();
                            }

                          /*  @Override
                            public void onEdit(int position) {
                                TripInfo selectedItemValue = infoList.get(position);
                                String key = selectedItemValue.getTrip_id();
                                String tName = trp_Name;
                                String tDescription = trp_Description;
                                String tStartDate = trp_StartDate;
                                String tEndDate = trp_EndDate;
                                String tBudget = trp_Budget;

                                Intent intent = new Intent(getApplicationContext(),AddTripsActivity.class);
                                intent.putExtra("tripKey",key);
                                intent.putExtra("tripName",tName);
                                intent.putExtra("tripDescription",tDescription);
                                intent.putExtra("tripStartDate",tStartDate);
                                intent.putExtra("tripEndDate",tEndDate);
                                intent.putExtra("tripBudget",tBudget);
                                startActivity(intent);
                            }  */

                            @Override
                            public void onDelete(final int position) {
                                TripInfo selectedItem = infoList.get(position);
                                String key = selectedItem.getTrip_id();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Trips");
                                databaseReference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(TripActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                            tripAdapter.remove(position); // this is what you need
                                            tripAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onDetails(int position) {
                                TripInfo selectedItem = infoList.get(position);
                                String key = selectedItem.getTrip_id();
                                Intent intent = new Intent(getApplicationContext(),TripDetailsActivity.class);
                                intent.putExtra("key",key);
                                startActivity(intent);

                            }
                        });
                    }

                }

                progressBarTA.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addTripBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),AddTripsActivity.class);
        startActivity(intent);
    }

    public void tripActivityBackBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
