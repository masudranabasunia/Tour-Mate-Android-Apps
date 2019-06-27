package com.example.tourmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTripContactActivity extends AppCompatActivity {

    private EditText tripContactNameET, tripContactPhoneET, tripContactAddressET;
    DatabaseReference databaseRefContact;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_contact);

        tripContactNameET = findViewById(R.id.tripContactNameETID);
        tripContactPhoneET = findViewById(R.id.tripContactPhoneETID);
        tripContactAddressET = findViewById(R.id.tripContactAddressETID);

        user = mAuth.getInstance().getCurrentUser();
        uId = user.getUid();
        databaseRefContact = FirebaseDatabase.getInstance().getReference("Contacts");
    }



    public void addTripContactBtn(View view) {
        String tripContactName = tripContactNameET.getText().toString().trim();
        String tripContactPhone = tripContactPhoneET.getText().toString().trim();
        String tripContactAddress = tripContactAddressET.getText().toString().trim();

        if(tripContactName.isEmpty()){
            tripContactNameET.setError("Enter name");
            tripContactNameET.requestFocus();
        }
        else if(tripContactPhone.isEmpty()){
            tripContactPhoneET.setError("Enter phone number");
            tripContactPhoneET.requestFocus();
        }

        else if(tripContactPhone.length()!=11){
            tripContactPhoneET.setError("Enter a valid phone number");
            tripContactPhoneET.requestFocus();
        }

        else if(tripContactAddress.isEmpty()){
            tripContactAddressET.setError("Enter address");
            tripContactAddressET.requestFocus();
        }
        else{

            String contactId = databaseRefContact.push().getKey();
            TripContact tripContact = new TripContact(contactId,tripContactName,tripContactPhone,tripContactAddress,uId);

            final ProgressDialog pd = new ProgressDialog(AddTripContactActivity.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

            databaseRefContact.child(contactId).setValue(tripContact).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddTripContactActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),TripContactActivity.class);
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                    }else{
                        Toast.makeText(AddTripContactActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void backBtnAddTripContact(View view) {
        onBackPressed();
    }
}
