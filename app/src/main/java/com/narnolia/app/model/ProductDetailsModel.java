package com.narnolia.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 05-11-2016.
 */

public class ProductDetailsModel implements Serializable {

    private String prodName;

    private ArrayList<CategoryDetailsModel> categoryModels = new ArrayList<>();
    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public ArrayList<CategoryDetailsModel> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<CategoryDetailsModel> categoryModels) {
        this.categoryModels = categoryModels;
    }


}
