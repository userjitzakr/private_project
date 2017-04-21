package com.jaapps.billing;

/**
 * Created by jithin suresh on 02-04-2017.
 */

public class Billing_FirebaseData_company {

    String name;
    String address1;
    String address2;
    String address3;
    String phone;
    String tin;

    public Billing_FirebaseData_company()
    {

    }

    public Billing_FirebaseData_company(String name, String ad1, String ad2, String ad3, String phone, String tin)
    {
        this.name = name;
        this.address1 = ad1;
        this.address2 = ad2;
        this.address3 = ad3;
        this.phone = phone;
        this.tin = tin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }
}
