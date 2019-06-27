package com.example.tourmate;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText addExpenseNameET, addExpenseAmountET, addExpenseDateET;
    DatabaseReference databaseRefWallet;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        addExpenseNameET = findViewById(R.id.addExpenseNameETID);
        addExpenseAmountET = findViewById(R.id.addExpenseAmountETID);
        addExpenseDateET = findViewById(R.id.addExpenseDateETID);

        user = mAuth.getInstance().getCurrentUser();
        uId = user.getUid();
        databaseRefWallet = FirebaseDatabase.getInstance().getReference("Wallet");
    }

    public void addExpenseBtnBtn(View view) {
        String expenseName = addExpenseNameET.getText().toString().trim();
        String expenseAmount = addExpenseAmountET.getText().toString().trim();
        String expenseDate = addExpenseDateET.getText().toString().trim();

        if(expenseName.isEmpty()){
            addExpenseNameET.setError("Enter expense name");
            addExpenseNameET.requestFocus();
        }
        else if(expenseAmount.isEmpty()){
            addExpenseAmountET.setError("Enter expense amount");
            addExpenseAmountET.requestFocus();
        }
        else if(expenseDate.isEmpty()){
            addExpenseDateET.setError("Enter expense date");
            addExpenseDateET.requestFocus();
        }
        else{

            String walletId = databaseRefWallet.push().getKey();
            WalletInfo walletInfo = new WalletInfo(walletId,expenseName,expenseAmount,expenseDate,uId);

            final ProgressDialog pd = new ProgressDialog(AddExpenseActivity.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

            databaseRefWallet.child(walletId).setValue(walletInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddExpenseActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),WalletActivity.class);
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                    }else{
                        Toast.makeText(AddExpenseActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void backBtnAddExpense(View view) {
        Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
        startActivity(intent);
        finish();
    }



    public void pickEndDateADE(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int day, int month, int year) {

                String date = year + "/" + month + "/" + day;
                addExpenseDateET.setText(date);
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
