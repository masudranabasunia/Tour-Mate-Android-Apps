package com.example.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TripMemoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTM;
    private MemoryAdapter memoryAdapter;
    private List<Upload> uploadList;
    private ProgressBar progressBarTM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_memory);

        progressBarTM = findViewById(R.id.progressBarTMID);

        recyclerViewTM = findViewById(R.id.recyclerViewTMID);
        recyclerViewTM.setHasFixedSize(true);
        recyclerViewTM.setLayoutManager(new LinearLayoutManager(this));

        uploadList = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getUid();

        Query query = FirebaseDatabase.getInstance().getReference("Upload")
                .orderByChild("user_id")
                .equalTo(uId);


       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploadList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Upload upload = data.getValue(Upload.class);

                    Upload value = new Upload();
                    value.setUpload_id(data.getKey());

                    value.setImageUrl(data.child("imageUrl").getValue().toString());
                    value.setImageCaption(data.child("imageCaption").getValue().toString());

                    uploadList.add(value);
                }

                memoryAdapter = new MemoryAdapter(TripMemoryActivity.this,uploadList);
                recyclerViewTM.setAdapter(memoryAdapter);
                memoryAdapter.notifyDataSetChanged();

                progressBarTM.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addTripBtnTM(View view) {
        Intent intent = new Intent(getApplicationContext(),AddMemoryActivity.class);
        startActivity(intent);

    }

    public void tripMemoryActivityBackBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
