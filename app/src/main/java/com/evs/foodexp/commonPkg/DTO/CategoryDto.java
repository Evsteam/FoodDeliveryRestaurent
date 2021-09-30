
package com.evs.foodexp.commonPkg.DTO;

import java.util.List;

import com.evs.foodexp.models.CategoryModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryDto {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<CategoryModel> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CategoryModel> getData() {
        return data;
    }

    public void setData(List<CategoryModel> data) {
        this.data = data;
    }
	

}
