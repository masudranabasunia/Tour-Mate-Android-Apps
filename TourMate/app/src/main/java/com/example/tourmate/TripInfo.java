package com.example.tourmate;

import com.google.firebase.database.Exclude;

public class TripInfo {
    private String trip_id;
    private String trpName;
    private String trpDescription;
    private String trpStartDate;
    private String trpEndDate;
    private String trpBudget;
    private String userId;

    public TripInfo(){

    }

    public TripInfo(String trip_id, String trpName, String trpDescription, String trpStartDate, String trpEndDate, String trpBudget, String userId) {
        this.trip_id = trip_id;
        this.trpName = trpName;
        this.trpDescription = trpDescription;
        this.trpStartDate = trpStartDate;
        this.trpEndDate = trpEndDate;
        this.trpBudget = trpBudget;
        this.userId = userId;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public void setTrpName(String trpName) {
        this.trpName = trpName;
    }

    public void setTrpDescription(String trpDescription) {
        this.trpDescription = trpDescription;
    }

    public void setTrpStartDate(String trpStartDate) {
        this.trpStartDate = trpStartDate;
    }

    public void setTrpEndDate(String trpEndDate) {
        this.trpEndDate = trpEndDate;
    }

    public void setTrpBudget(String trpBudget) {
        this.trpBudget = trpBudget;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTrip_id() {
        return trip_id;
    }

    public String getTrpName() {
        return trpName;
    }

    public String getTrpDescription() {
        return trpDescription;
    }

    public String getTrpStartDate() {
        return trpStartDate;
    }

    public String getTrpEndDate() {
        return trpEndDate;
    }

    public String getTrpBudget() {
        return trpBudget;
    }

    public String getUserId() {
        return userId;
    }
}
