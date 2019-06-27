package com.example.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TaskPlanAdapter extends RecyclerView.Adapter<TaskPlanAdapter.ViewHolder> {
    private List<TaskInfo> taskInfoList;
    private Context context;

    public TaskPlanAdapter(List<TaskInfo> taskInfoList, Context context) {
        this.taskInfoList = taskInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_task_details,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskPlanAdapter.ViewHolder viewHolder, int i) {

        TaskInfo taskInfoCurrentUser = taskInfoList.get(i);

        viewHolder.showTaskDateTV.setText(taskInfoCurrentUser.getTaskPlanDate());
        viewHolder.showTaskPlanDesTV.setText(taskInfoCurrentUser.getTaskPlanDes());
    }

    @Override
    public int getItemCount() {
        return taskInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView showTaskDateTV, showTaskPlanDesTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showTaskDateTV = itemView.findViewById(R.id.showTaskPlanDateTVID);
            showTaskPlanDesTV = itemView.findViewById(R.id.showTaskPlanDesTVID);
        }
    }
}
