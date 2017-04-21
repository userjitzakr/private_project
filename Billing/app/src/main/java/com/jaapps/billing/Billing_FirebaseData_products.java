package com.jaapps.billing;

/**
 * Created by jithin suresh on 02-04-2017.
 */

public class Billing_FirebaseData_products {

    String serialNum;
    String itemId;
    String description;
    String price;
    String sellingPrice;
    String qty;
    String soldCount;
    String barcode;
    String stockDate;

    public Billing_FirebaseData_products()
    {

    }
    public Billing_FirebaseData_products(String serialNum,
                                         String itemId,
                                         String description,
                                         String price,
                                         String sellingPrice,
                                         String qty,
                                         String soldCount,
                                         String barcode,
                                         String stockDate)
    {
        this.serialNum = serialNum;
        this.itemId = itemId;
        this.description = description;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.qty = qty;
        this.soldCount = soldCount;
        this.barcode = barcode;
        this.stockDate = stockDate;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(String soldCount) {
        this.soldCount = soldCount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStockDate() {
        return stockDate;
    }

    public void setStockDate(String stockDate) {
        this.stockDate = stockDate;
    }
}
