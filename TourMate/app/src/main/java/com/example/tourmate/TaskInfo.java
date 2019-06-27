package com.example.tourmate;

public class TaskInfo {

    private String taskPlanId;
    private String taskPlanDes;
    private String taskPlanDate;
    private String userId;

    public TaskInfo(){

    }

    public TaskInfo(String taskPlanId, String taskPlanDes, String taskPlanDate, String userId) {
        this.taskPlanId = taskPlanId;
        this.taskPlanDes = taskPlanDes;
        this.taskPlanDate = taskPlanDate;
        this.userId = userId;
    }

    public void setTaskPlanId(String taskPlanId) {
        this.taskPlanId = taskPlanId;
    }

    public void setTaskPlanDes(String taskPlanDes) {
        this.taskPlanDes = taskPlanDes;
    }

    public void setTaskPlanDate(String taskPlanDate) {
        this.taskPlanDate = taskPlanDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTaskPlanId() {
        return taskPlanId;
    }

    public String getTaskPlanDes() {
        return taskPlanDes;
    }

    public String getTaskPlanDate() {
        return taskPlanDate;
    }

    public String getUserId() {
        return userId;
    }
}
