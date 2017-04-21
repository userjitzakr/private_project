package com.jaapps.billing;

/**
 * Created by jithin suresh on 29-01-2017.
 */

public class Billing_ListMakeBill {

    String slNo;
    String item;
    String description;
    String amount;
    String itemId;
    String ItemSerialNumber;

    Billing_ListMakeBill()
    {

    }

    public void setSlNo(String str){slNo = str;}
    public String getSlNo(){return slNo;}

    public void setItemName(String str){ item = str;}
    public String getItemName(){return item;}

    public void setDescription(String str){description = str;}
    public String getDescription(){return description;}

    public void setAmount(String str){amount = str;}
    public String getAmount(){return  amount;}

    public void setItemId(String str){itemId = str;}
    public String getItemId(){return  itemId;}

    public void setItemSerialNumber(String str) { ItemSerialNumber = str;}
    public String getItemSerialNumber() { return  ItemSerialNumber;}

}
