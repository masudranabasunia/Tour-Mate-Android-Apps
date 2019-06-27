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

public class AddTaskPlanActivity extends AppCompatActivity {

    private EditText taskPlanET, taskPlanDateET;
    DatabaseReference databaseRefTask;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_plan);

        taskPlanET = findViewById(R.id.taskPlanETID);
        taskPlanDateET = findViewById(R.id.taskPlanDateETID);

        user = mAuth.getInstance().getCurrentUser();
        uId = user.getUid();
        databaseRefTask = FirebaseDatabase.getInstance().getReference("Task");
    }

    public void backBtnAddTask(View view) {
        Intent intent = new Intent(getApplicationContext(),TaskPlanActivity.class);
        startActivity(intent);
        finish();
    }

    public void pickStartDateTPA(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int day, int month, int year) {

                String date = year + "/" + month + "/" + day;
                taskPlanDateET.setText(date);
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

    public void addTripBtnTPA(View view) {

        String taskPlan = taskPlanET.getText().toString().trim();
        String taskPlanD = taskPlanDateET.getText().toString().trim();

        if(taskPlan.isEmpty()){
            taskPlanET.setError("Enter task plan");
            taskPlanET.requestFocus();
        }
        else if (taskPlanD.isEmpty()){
            taskPlanDateET.setError("Enter task plan date");
            taskPlanDateET.requestFocus();
        }
        else {
            String taskId = databaseRefTask.push().getKey();
            TaskInfo taskInfo = new TaskInfo(taskId,taskPlan,taskPlanD,uId);

            final ProgressDialog pd = new ProgressDialog(AddTaskPlanActivity.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

            databaseRefTask.child(taskId).setValue(taskInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddTaskPlanActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),TaskPlanActivity.class);
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                    }else{
                        Toast.makeText(AddTaskPlanActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
