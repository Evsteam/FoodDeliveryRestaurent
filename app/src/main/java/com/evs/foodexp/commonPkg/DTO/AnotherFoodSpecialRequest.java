package com.evs.foodexp.commonPkg.DTO;

public class AnotherFoodSpecialRequest {

    private String action;
    private String userId;
    private String whatYouWant;
    private String price;
    private String TIP;
    private String deliveryFee;

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

    public String getWhatYouWant() {
        return whatYouWant;
    }

    public void setWhatYouWant(String whatYouWant) {
        this.whatYouWant = whatYouWant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
