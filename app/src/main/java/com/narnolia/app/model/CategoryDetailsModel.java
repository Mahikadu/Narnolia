package com.narnolia.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 05-11-2016.
 */

public class CategoryDetailsModel implements Serializable {
    private String category;
    private String subcategory;

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }


    private ArrayList<SubCategoryDetailsModel> subcategoryModels = new ArrayList<>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<SubCategoryDetailsModel> getSubcategoryModels() {
        return subcategoryModels;
    }

    public void setSubcategoryModels(ArrayList<SubCategoryDetailsModel> subcategoryModels) {
        this.subcategoryModels = subcategoryModels;
    }
}
