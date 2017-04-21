package com.jaapps.billing;

/**
 * Created by jithin suresh on 02-04-2017.
 */

public class Billing_FirebaseData_BillItems {
    String itemId;
    String billId;

    public Billing_FirebaseData_BillItems()
    {

    }
    public Billing_FirebaseData_BillItems(String billId, String itemId)
    {
        this.itemId = itemId;
        this.billId = billId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
}
