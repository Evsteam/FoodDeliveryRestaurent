
package com.evs.foodexp.commonPkg.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpDto {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

	public class Data {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("eamil")
    @Expose
    private String eamil;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEamil() {
        return eamil;
    }

    public void setEamil(String eamil) {
        this.eamil = eamil;
    }

}
	
}
