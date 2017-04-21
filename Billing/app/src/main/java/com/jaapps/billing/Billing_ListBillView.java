package com.jaapps.billing;

/**
 * Created by jithin suresh on 04-02-2017.
 */

public class Billing_ListBillView {

    private  String bilId;
    private  String billDate;
    private  String billAmount;


    Billing_ListBillView()
    {

    }

    public void setbilId(String str){bilId = str;}
    public String getbilId(){return bilId;}

    public void setbillDate(String str){ billDate = str;}
    public String getbillDate(){return billDate;}

    public void setbillAmount(String str){billAmount = str;}
    public String getbillAmount(){return billAmount;}

}
