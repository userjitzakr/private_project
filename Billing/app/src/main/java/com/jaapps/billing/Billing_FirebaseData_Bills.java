package com.jaapps.billing;

/**
 * Created by jithin suresh on 02-04-2017.
 */

public class Billing_FirebaseData_Bills {


    String billId;
    String date;
    String discount;
    String total;
    String total_tax;
    String grantTotal;
    String customerName;
    String customerAddress;

    public Billing_FirebaseData_Bills()
    {

    }

    public Billing_FirebaseData_Bills(String billId,
                                      String date,
                                      String discount,
                                      String total,
                                      String total_tax,
                                      String grantTotal,
                                      String customerName,
                                      String customerAddress)
    {

        this.billId = billId;
        this.date = date;
        this.discount = discount;
        this.total = total;
        this.total_tax = total_tax;
        this.grantTotal = grantTotal;
        this.customerName = customerName;
        this.customerAddress = customerAddress;

    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(String total_tax) {
        this.total_tax = total_tax;
    }

    public String getGrantTotal() {
        return grantTotal;
    }

    public void setGrantTotal(String grantTotal) {
        this.grantTotal = grantTotal;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
