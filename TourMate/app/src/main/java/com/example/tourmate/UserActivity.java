package com.example.tourmate;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.LinearLayout;

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

public class UserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> userList;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        recyclerView = findViewById(R.id.recyclerViewID);
        userList = new ArrayList<>();
        initRecyclerView();
        getDataFromDB();
    }

    private void initRecyclerView() {
        userList.add(new User("masud","masud@gmail.com","56956862","fgfgfkgfajkgf"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userList,this);
        recyclerView.setAdapter(adapter);
    }

    private void getDataFromDB() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getUid();

        Query query = FirebaseDatabase.getInstance().getReference("User")
                .orderByChild("id")
                .equalTo(uId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userList.clear();
                    for(DataSnapshot data: dataSnapshot.getChildren()){

                        User user = data.getValue(User.class);
                        User value = new User();
                        String name = data.child("name").getValue().toString();
                        String email = data.child("email").getValue().toString();
                        String phone = data.child("phone").getValue().toString();
                        String id = data.child("id").getValue().toString();
                        value.setName(name);
                        value.setEmail(email);
                        value.setPhone(phone);
                        value.setId(id);
                        value.getName();
                        value.getEmail();
                        value.getPhone();
                        value.getId();

                        userList.add(value);
                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
