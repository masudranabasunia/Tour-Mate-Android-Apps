package com.example.tourmate;

import android.content.Intent;
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

public class TaskPlanActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTPA;
    private TaskPlanAdapter taskPlanAdapter;
    private List<TaskInfo> taskList;
    private ProgressBar progressBarTPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_plan);

        progressBarTPA = findViewById(R.id.progressBarTPAID);

        recyclerViewTPA = findViewById(R.id.recyclerViewTPAID);
        taskList = new ArrayList<>();
        initRecyclerViewTPA();
        getDataFromDBTPA();
    }

    private void initRecyclerViewTPA() {
        recyclerViewTPA.setLayoutManager(new LinearLayoutManager(this));
        taskPlanAdapter = new TaskPlanAdapter(taskList,this);
        recyclerViewTPA.setAdapter(taskPlanAdapter);
    }

    private void getDataFromDBTPA() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uId = firebaseUser.getUid();

        Query query = FirebaseDatabase.getInstance().getReference("Task")
                .orderByChild("userId")
                .equalTo(uId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    taskList.clear();
                    for(final DataSnapshot data: dataSnapshot.getChildren()){

                        TaskInfo taskInfo = data.getValue(TaskInfo.class);

                        TaskInfo value = new TaskInfo();
                        value.setTaskPlanId(data.getKey());

                        final String task_Plan_Date = data.child("taskPlanDate").getValue().toString();
                        final String task_Plan_des = data.child("taskPlanDes").getValue().toString();

                        value.setTaskPlanDate(task_Plan_Date);
                        value.setTaskPlanDes(task_Plan_des);

                        value.getTaskPlanDate();
                        value.getTaskPlanDes();

                        taskList.add(value);
                        taskPlanAdapter.notifyDataSetChanged();
                    }

                }

                progressBarTPA.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void taskPlanActivityBackBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void addTaskPlanBtn(View view) {
        Intent intent = new Intent(getApplicationContext(),AddTaskPlanActivity.class);
        startActivity(intent);
        finish();
    }
}
