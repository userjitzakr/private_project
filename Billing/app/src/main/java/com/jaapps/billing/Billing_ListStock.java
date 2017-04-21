package com.jaapps.billing;

import java.io.Serializable;
import java.util.SimpleTimeZone;

/**
 * Created by jithin suresh on 29-01-2017.
 */

public class Billing_ListStock implements Serializable{

    private String item;
    private int    total_available;
    private int    total_sold;
    private String itemId;

    Billing_ListStock()
    {
        total_available = 0;
        total_sold = 0;
    }

    public void setItemName(String str) {
        item = str;
    }
    public String getItemName() { return  item;}

    public void setTotal_available(int total) { total_available = total;}
    public int getTotal_available() { return  total_available;}

    public void setTotal_sold(int total){total_sold = total;}
    public int getTotal_sold(){return total_sold;}

    public void setItemId(String str) { itemId = str;}
    public String getItemId(){return  itemId;}
}
