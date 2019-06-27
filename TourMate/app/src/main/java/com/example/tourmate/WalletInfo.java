package com.example.tourmate;

public class WalletInfo {

    private String tripWalletId;
    private String tripWalletName;
    private String tripWalletAmount;
    private String tripWalletDate;
    private String userId;

    public WalletInfo(){

    }

    public WalletInfo(String tripWalletId, String tripWalletName, String tripWalletAmount, String tripWalletDate, String userId) {
        this.tripWalletId = tripWalletId;
        this.tripWalletName = tripWalletName;
        this.tripWalletAmount = tripWalletAmount;
        this.tripWalletDate = tripWalletDate;
        this.userId = userId;
    }

    public void setTripWalletId(String tripWalletId) {
        this.tripWalletId = tripWalletId;
    }

    public void setTripWalletName(String tripWalletName) {
        this.tripWalletName = tripWalletName;
    }

    public void setTripWalletAmount(String tripWalletAmount) {
        this.tripWalletAmount = tripWalletAmount;
    }

    public void setTripWalletDate(String tripWalletDate) {
        this.tripWalletDate = tripWalletDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripWalletId() {
        return tripWalletId;
    }

    public String getTripWalletName() {
        return tripWalletName;
    }

    public String getTripWalletAmount() {
        return tripWalletAmount;
    }

    public String getTripWalletDate() {
        return tripWalletDate;
    }

    public String getUserId() {
        return userId;
    }
}
