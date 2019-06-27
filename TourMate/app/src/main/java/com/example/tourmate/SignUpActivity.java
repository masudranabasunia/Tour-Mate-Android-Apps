package com.example.tourmate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class SignUpActivity extends AppCompatActivity {

    private EditText userName,userEmail,userPhoneNumber,userPassword,userConfirmPassword;
    private CircularImageView circularImageView;

    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        userName = findViewById(R.id.userNameETID);
        userEmail = findViewById(R.id.userEmailETID);
        userPhoneNumber = findViewById(R.id.userPhoneNumberETID);
        userPassword = findViewById(R.id.userPasswordETID);
        userConfirmPassword = findViewById(R.id.userConfirmPasswordETID);
        //signUpBtnSignACT = findViewById(R.id.signBtnSignUpACTID);
        circularImageView = findViewById(R.id.profileImageCIVID);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
    }


    public void logInActivity(View view) {
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
    }
    public void backBtnLogAct(View view) {
        onBackPressed();
    }

    public void signUpBtnSignACT(View view) {
        final String name = userName.getText().toString().trim();
        final String email = userEmail.getText().toString().trim();
        final String phone = userPhoneNumber.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String confirmPassword = userConfirmPassword.getText().toString().trim();


        if (name.isEmpty()) {
            userName.setError("Enter user full name");
            userName.requestFocus();
            return;
        } else if (email.isEmpty()) {
            userEmail.setError("Enter an email address");
            userEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Enter an valid email address");
            userEmail.requestFocus();
            return;
        } else if (phone.isEmpty()) {
            userPhoneNumber.setError("Enter a phone number");
            userPhoneNumber.requestFocus();
            return;
        } else if (phone.length() != 11) {
            userPhoneNumber.setError("Enter a valid phone number");
            userPhoneNumber.requestFocus();
            return;
        }else if (password.isEmpty()) {
            userPassword.setError("Enter a password");
            userPassword.requestFocus();
            return;
        } else if (password.length() < 8) {
            userPassword.setError("Enter a valid password ");
            userPassword.requestFocus();
            return;
        }

        else if(confirmPassword.isEmpty()){
            userConfirmPassword.setError("Enter password again");
            userConfirmPassword.requestFocus();
        }
        else if (!confirmPassword.equals(password)) {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            return;
        }
        else{

            if(!isConnected(SignUpActivity.this)) buildDialog(SignUpActivity.this).show();
            else{
                final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
                pd.setTitle("Singing up...");
                pd.setMessage("Please wait.");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressBar.setVisibility(View.GONE);
                        pd.dismiss();

                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User user = new User(id,name,email,phone);
                            databaseReference.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        finish();
                                        Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                        //intent.putExtra("mobileNumber",phone);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignUpActivity.this, "User is already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignUpActivity.this, "Unsuccessful Registration", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

        }
    }

    public void userPic(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode ==1){

            Uri uri= data.getData();
            Picasso.with(this).load(uri).rotate(270).into(circularImageView);

        }

    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or Wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
}
