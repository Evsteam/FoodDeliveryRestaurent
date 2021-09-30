package com.evs.foodexp.commonPkg.DTO;

public class AddBookingRequest {

    private String action;
    private String userId;
    private String categoryId;
    private String serviceId;
    private String bookingDate;
    private String slote;
    private String latitude;
    private String longitude;
    private String vendorId;
    private String address;
    private String TIP;
    private String otherService;
    private String serviceAmount;
    private String totalAmount;
    private String specialNote;
    private String salesTax;
    private String transactionFee;
    private String discountAmount;

    public void setTransactionfee(String transactionfee) {
        this.transactionFee = transactionfee;
    }

    public String getTransactionfee() {
        return transactionFee;
    }

    public void setSalesTax(String salesTax) {
        this.salesTax = salesTax;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public String getserviceAmount() {
        return serviceAmount;
    }

    public void setserviceAmount(String serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOtherService() {
        return otherService;
    }

    public void setOtherService(String otherService) {
        this.otherService = otherService;
    }

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSlote() {
        return slote;
    }

    public void setSlote(String slote) {
        this.slote = slote;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTIP() {
        return TIP;
    }

    public void setTIP(String TIP) {
        this.TIP = TIP;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }
}
