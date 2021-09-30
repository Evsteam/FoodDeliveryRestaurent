package com.evs.foodexp.commonPkg.DTO;

public class ToGetRequest {

    private String action;
    private String userId;
    private String wahtUwant;
    private String StoreCity;
    private String EPrice;
    private String TIP;
    private String deliveryFee;
    private String totalAmount;
    private String notes;
    private String noContact;
    private String transactionId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWahtUwant() {
        return wahtUwant;
    }

    public void setWahtUwant(String wahtUwant) {
        this.wahtUwant = wahtUwant;
    }

    public String getStoreCity() {
        return StoreCity;
    }

    public void setStoreCity(String storeCity) {
        StoreCity = storeCity;
    }

    public String getEPrice() {
        return EPrice;
    }

    public void setEPrice(String EPrice) {
        this.EPrice = EPrice;
    }

    public String getTIP() {
        return TIP;
    }

    public void setTIP(String TIP) {
        this.TIP = TIP;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNoContact() {
        return noContact;
    }

    public void setNoContact(String noContact) {
        this.noContact = noContact;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
