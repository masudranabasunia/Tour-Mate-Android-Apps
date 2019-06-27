package com.example.tourmate;

public class TripContact {

    private String tripContactId;
    private String tripContactName;
    private String tripContactPhone;
    private String tripContactAddress;
    private String userId;

    public TripContact(){

    }

    public TripContact(String tripContactId, String tripContactName, String tripContactPhone, String tripContactAddress, String userId) {
        this.tripContactId = tripContactId;
        this.tripContactName = tripContactName;
        this.tripContactPhone = tripContactPhone;
        this.tripContactAddress = tripContactAddress;
        this.userId = userId;
    }

    public void setTripContactId(String tripContactId) {
        this.tripContactId = tripContactId;
    }

    public void setTripContactName(String tripContactName) {
        this.tripContactName = tripContactName;
    }

    public void setTripContactPhone(String tripContactPhone) {
        this.tripContactPhone = tripContactPhone;
    }

    public void setTripContactAddress(String tripContactAddress) {
        this.tripContactAddress = tripContactAddress;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripContactId() {
        return tripContactId;
    }

    public String getTripContactName() {
        return tripContactName;
    }

    public String getTripContactPhone() {
        return tripContactPhone;
    }

    public String getTripContactAddress() {
        return tripContactAddress;
    }

    public String getUserId() {
        return userId;
    }
}
