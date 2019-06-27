package com.example.tourmate;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class TripContactActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTCA;
    private TripContactAdapter tripContactAdapter;
    private List<TripContact> contactList;
    private ProgressBar progressBarTCA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_contact);

        progressBarTCA = findViewById(R.id.progressBarTCAID);

        recyclerViewTCA = findViewById(R.id.recyclerViewTCAID);
        contactList = new ArrayList<>();
        initRecyclerViewTCA();
        getDataFromDBTCA();
    }

    private void initRecyclerViewTCA() {

        recyclerViewTCA.setLayoutManager(new LinearLayoutManager(this));
        tripContactAdapter = new TripContactAdapter(contactList,this);
        recyclerViewTCA.setAdapter(tripContactAdapter);
    }

    private void getDataFromDBTCA() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getUid();

        Query query = FirebaseDatabase.getInstance().getReference("Contacts")
                .orderByChild("userId")
                .equalTo(uId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    contactList.clear();

                    for(final DataSnapshot data: dataSnapshot.getChildren()){

                        TripContact tripContact = data.getValue(TripContact.class);

                        TripContact value = new TripContact();
                        value.setTripContactId(data.getKey());

                        String contactName = data.child("tripContactName").getValue().toString();
                        String contactPhone = data.child("tripContactPhone").getValue().toString();
                        String contactAddress = data.child("tripContactAddress").getValue().toString();

                        value.setTripContactName(contactName);
                        value.setTripContactPhone(contactPhone);
                        value.setTripContactAddress(contactAddress);
                        value.getTripContactName();
                        value.getTripContactPhone();
                        value.getTripContactAddress();

                        contactList.add(value);
                        tripContactAdapter.notifyDataSetChanged();

                        tripContactAdapter.setOnItemClickListener(new TripContactAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position) {
                                String cName = contactList.get(position).getTripContactName();
                                //Toast.makeText(TripContactActivity.this, cName, Toast.LENGTH_SHORT).show();
                            }

                           /* @Override
                            public void onEdit(int position) {

                            } */

                            @Override
                            public void onCall(int position) {
                                String number = contactList.get(position).getTripContactPhone();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                                startActivity(intent);
                            }

                            @Override
                            public void onDelete(final int position) {

                                TripContact selectedItem = contactList.get(position);
                                String key = selectedItem.getTripContactId();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");
                                databaseReference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(TripContactActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                            tripContactAdapter.removeContact(position); // this is what you need
                                            tripContactAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }

                progressBarTCA.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addTripContactBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),AddTripContactActivity.class);
        startActivity(intent);
    }

    public void tripContactActivityBackBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
