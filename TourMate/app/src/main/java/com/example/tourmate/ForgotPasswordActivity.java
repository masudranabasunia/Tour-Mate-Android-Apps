package com.example.tourmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button forgotPasswordBtnPA;
    private EditText userEmailFPAET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPasswordBtnPA = findViewById(R.id.forgotPasswordBtnFPAID);
        userEmailFPAET = findViewById(R.id.userEmailFPAETID);
    }

    public void backBtnForPassAct(View view) {
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
        finish();
    }

    public void resetForgotPasswordBtn(View view) {

        forgotPasswordBtnPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmailFPA = userEmailFPAET.getText().toString().trim();
                if (userEmailFPA.isEmpty()){
                    userEmailFPAET.setError("Enter an email");
                    userEmailFPAET.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(userEmailFPA).matches()) {
                    userEmailFPAET.setError("Enter an valid email address");
                    userEmailFPAET.requestFocus();
                    return;
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(ForgotPasswordActivity.this);
                    pd.setTitle("Processing...");
                    pd.setMessage("Please wait.");
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    pd.show();
                   FirebaseAuth.getInstance()
                           .sendPasswordResetEmail(userEmailFPA)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       Toast.makeText(ForgotPasswordActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                       startActivity(intent);
                                       finish();
                                       pd.dismiss();
                                   }else {
                                       Toast.makeText(ForgotPasswordActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });

                }
            }
        });
    }

}
