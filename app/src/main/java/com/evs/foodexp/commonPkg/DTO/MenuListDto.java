
package com.evs.foodexp.commonPkg.DTO;

import java.util.ArrayList;
import java.util.List;

import com.evs.foodexp.models.CategoryModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuListDto {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("totalCartItem")
    @Expose
    private String totalCartItem;


    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    @SerializedName("categoryList")
    @Expose
    private ArrayList<CategoryModel> categoryList;

    public ArrayList<CategoryModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    public String getTotalCartItem() {
        return totalCartItem;
    }

    public void setTotalCartItem(String totalCartItem) {
        this.totalCartItem = totalCartItem;
    }

    public String getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
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

        @SerializedName(value = "menuId",alternate = {"foodId"})
        @Expose
        private String menuId;
        @SerializedName("resturentId")
        @Expose
        private String resturentId;
        @SerializedName("categoryId")
        @Expose
        private String categoryId;
        @SerializedName("foodName")
        @Expose
        private String foodName;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("specialPrice")
        @Expose
        private String specialPrice;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName(value = "image_1",alternate = {"foodImage"})
        @Expose
        private String image1;
        @SerializedName("image_2")
        @Expose
        private String image2;
        @SerializedName("image_3")
        @Expose
        private String image3;
        @SerializedName("image_4")
        @Expose
        private String image4;
        @SerializedName("image_5")
        @Expose
        private String image5;
        @SerializedName("resturentName")
        @Expose
        private String resturentName;

        @SerializedName(value = "foodType",alternate = {"foodTag"})
        @Expose
        private String foodType;

        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("created")
        @Expose
        private String created;

        @SerializedName("quantity")
        @Expose
        private String quantity="0";

        public String getFoodType() {
            return foodType;
        }


        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMenuId() {
            return menuId;
        }

        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }

        public String getResturentId() {
            return resturentId;
        }

        public void setResturentId(String resturentId) {
            this.resturentId = resturentId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSpecialPrice() {
            return specialPrice;
        }

        public void setSpecialPrice(String specialPrice) {
            this.specialPrice = specialPrice;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage3() {
            return image3;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }

        public String getImage4() {
            return image4;
        }

        public void setImage4(String image4) {
            this.image4 = image4;
        }

        public String getImage5() {
            return image5;
        }

        public void setImage5(String image5) {
            this.image5 = image5;
        }

        public String getResturentName() {
            return resturentName;
        }

        public void setResturentName(String resturentName) {
            this.resturentName = resturentName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

    }

}
