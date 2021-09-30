
package com.evs.foodexp.commonPkg.DTO;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantListDto {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

	public class Data {

    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("experence")
    @Expose
    private String experence;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("device")
    @Expose
    private String device;

    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;

     @SerializedName("foodTag")
    @Expose
    private String foodTag;

 @SerializedName("companyBackground")
    @Expose
    private String companyBackground;

    @SerializedName("created")
    @Expose
    private String created;

        public Data(Integer userId, String fullName, String email, String role, String address, String zipCode, String experence, String designation, String contactNumber, String image, String device, String deviceToken, String created,String companyBackground) {
            this.userId = userId;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.address = address;
            this.zipCode = zipCode;
            this.experence = experence;
            this.designation = designation;
            this.contactNumber = contactNumber;
            this.image = image;
            this.device = device;
            this.deviceToken = deviceToken;
            this.companyBackground = companyBackground;
            this.created = created;
        }

        public String getCompanyBackground() {
            return companyBackground;
        }

        public void setCompanyBackground(String companyBackground) {
            this.companyBackground = companyBackground;
        }

        public String getFoodTag() {
            return foodTag;
        }

        public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getExperence() {
        return experence;
    }

    public void setExperence(String experence) {
        this.experence = experence;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
	
}
