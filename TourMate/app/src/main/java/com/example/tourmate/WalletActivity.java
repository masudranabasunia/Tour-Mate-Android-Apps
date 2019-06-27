package com.example.tourmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;

import java.text.SimpleDateFormat;

public class WalletActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWA;
    private WalletAdapter walletAdapter;
    private List<WalletInfo> walletList;
    private ProgressBar progressBarWA;
    private TextView searchByDateTV;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        progressBarWA = findViewById(R.id.progressBarWAID);
        searchByDateTV = findViewById(R.id.searchDateTVID);
        searchBtn = findViewById(R.id.searchBtnID);

        recyclerViewWA = findViewById(R.id.recyclerViewWAID);
        walletList = new ArrayList<>();
        initRecyclerViewWA();
        //getDataFromDBWA();


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        String defaultDate = simpledateformat.format(cal.getTime());
        searchByDateTV.setText(defaultDate);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getUid();

        Query query1 = FirebaseDatabase.getInstance().getReference("Wallet")
                .orderByChild("userId")
                .equalTo(uId);

        query1.addListenerForSingleValueEvent(valueEventListener);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchDate = searchByDateTV.getText().toString();

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String uId = firebaseUser.getUid();

                Query query1 = FirebaseDatabase.getInstance().getReference("Wallet")
                        .orderByChild("userId")
                        .equalTo(uId);

                query1.addListenerForSingleValueEvent(valueEventListener);

                Query query2 = FirebaseDatabase.getInstance().getReference("Wallet")
                        .orderByChild("tripWalletDate")
                        .equalTo(searchDate);
                query2.addListenerForSingleValueEvent(valueEventListener);

            }
        });

    }

    private void initRecyclerViewWA() {
        recyclerViewWA.setLayoutManager(new LinearLayoutManager(this));
        walletAdapter = new WalletAdapter(walletList,this);
        recyclerViewWA.setAdapter(walletAdapter);
    }

    /* private void getDataFromDBWA() {

    } */


    public void walletActivityBackBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void addExpenseBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),AddExpenseActivity.class);
        startActivity(intent);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()) {
                walletList.clear();

                for(final DataSnapshot data: dataSnapshot.getChildren()){
                    WalletInfo walletInfo = data.getValue(WalletInfo.class);

                    WalletInfo value = new WalletInfo();
                    value.setTripWalletId(data.getKey());

                    String expenseName = data.child("tripWalletName").getValue().toString();
                    String expenseAmount = data.child("tripWalletAmount").getValue().toString();
                    String expenseDate = data.child("tripWalletDate").getValue().toString();

                    value.setTripWalletName(expenseName);
                    value.setTripWalletAmount(expenseAmount);
                    value.setTripWalletDate(expenseDate);

                    value.getTripWalletName();
                    value.getTripWalletAmount();
                    value.getTripWalletDate();

                    walletList.add(value);
                    walletAdapter.notifyDataSetChanged();

                    walletAdapter.setOnItemClickListener(new WalletAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(int position) {

                        }

                        @Override
                        public void onDelete(final int position) {

                            WalletInfo selectedItem = walletList.get(position);
                            String key = selectedItem.getTripWalletId();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Wallet");
                            databaseReference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(WalletActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                        walletAdapter.removeContact(position); // this is what you need
                                        walletAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    });
                }
            }

            progressBarWA.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void pickDateSearch(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int day, int month, int year) {

                String date = year + "/" + month + "/" + day;
                searchByDateTV.setText(date);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        month = month+1;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }
}
