package com.mobisolutions.ams.grocery;

/**
 * Created by vkilari on 1/23/18.
 */

public class GroceryItemsBean {

    private int itemId;
    private String itemName;
    private String itemImage;
    private String itemQuantityType;
    private String itemQuantity;
    private boolean isChecked;
    private int categoryItemSelectedCount;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public int getCategoryItemSelectedCount() {
        return categoryItemSelectedCount;
    }

    public void setCategoryItemSelectedCount(int categoryItemSelectedCount) {
        this.categoryItemSelectedCount = categoryItemSelectedCount;
    }

    public String getItemQuantityType() {
        return itemQuantityType;
    }

    public void setItemQuantityType(String itemQuantityType) {
        this.itemQuantityType = itemQuantityType;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

}
