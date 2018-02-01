package com.mobisolutions.ams.grocery;

import java.util.ArrayList;

/**
 * Created by vkilari on 1/23/18.
 */

public class GroceryCategoriesBean {

    private int categoryId;
    private String categoryName;
    private ArrayList<GroceryItemsBean> groceryList;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<GroceryItemsBean> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(ArrayList<GroceryItemsBean> groceryList) {
        this.groceryList = groceryList;
    }
}
