package com.example.tourmate;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddTripsActivity extends AppCompatActivity {

    private EditText tripName, tripDescription, tripStartDate, tripEndDate, tripBudget;
    DatabaseReference databaseRefTrip;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uId;

    //private TextView showId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trips);

        tripName = findViewById(R.id.tripNameETID);
        tripDescription = findViewById(R.id.tripDescriptionETID);
        tripStartDate = findViewById(R.id.tripStartDateETID);
        tripEndDate = findViewById(R.id.tripEndDateETID);
        tripBudget = findViewById(R.id.tripBudgetETID);


        String tripKeyIn = getIntent().getStringExtra("key");
        String tripNameIn = getIntent().getStringExtra("tripName");
        String tripDescriptionIn = getIntent().getStringExtra("tripDescription");
        String tripStartDateIn = getIntent().getStringExtra("tripStartDate");
        String tripEndDateIn = getIntent().getStringExtra("tripEndDate");
        String tripBudgetIn = getIntent().getStringExtra("tripBudget");

        tripName.setText(tripNameIn);
        tripDescription.setText(tripDescriptionIn);
        tripStartDate.setText(tripStartDateIn);
        tripEndDate.setText(tripEndDateIn);
        tripBudget.setText(tripBudgetIn);

        user = mAuth.getInstance().getCurrentUser();
        uId = user.getUid();
        databaseRefTrip = FirebaseDatabase.getInstance().getReference("Trips");

        //showId = findViewById(R.id.showIdTV);

    }

    public void backBtnAddTrip(View view) {
        onBackPressed();
    }

    public void pickStartDate(View view) {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int day, int month, int year) {

                String date = year + "/" + month + "/" + day;
                tripStartDate.setText(date);
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

    public void pickEndDate(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int day, int month, int year) {

                String date = year + "/" + month + "/" + day;
                tripEndDate.setText(date);
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

    public void addTripBtnATA(View view) {
        String tName = tripName.getText().toString().trim();
        String tDescription = tripDescription.getText().toString().trim();
        String tStartDate = tripStartDate.getText().toString().trim();
        String tEndDate = tripEndDate.getText().toString().trim();
        String tBudget = tripBudget.getText().toString().trim();
        final String tUserId = String.valueOf(uId);

        if(tName.isEmpty()){
            tripName.setError("Enter trip name");
            tripName.requestFocus();
        }
        else if(tDescription.isEmpty()){
            tripDescription.setError("Enter trip description");
            tripDescription.requestFocus();
        }
        else if(tStartDate.isEmpty()){
            tripStartDate.setError("Choose trip start date");
            tripStartDate.requestFocus();
        }
        else if(tEndDate.isEmpty()){
            tripEndDate.setError("Choose trip end date");
            tripEndDate.requestFocus();
        }
        else if(tBudget.isEmpty()){
            tripEndDate.setError("Enter trip budget");
            tripEndDate.requestFocus();
        }
        else{
            String tripId = databaseRefTrip.push().getKey();
            TripInfo tripInfo = new TripInfo(tripId,tName,tDescription,tStartDate,tEndDate,tBudget,tUserId);

            final ProgressDialog pd = new ProgressDialog(AddTripsActivity.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

            databaseRefTrip.child(tripId).setValue(tripInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddTripsActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),TripActivity.class);
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                    }else{
                        Toast.makeText(AddTripsActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
