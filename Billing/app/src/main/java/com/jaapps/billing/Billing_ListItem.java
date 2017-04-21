package com.jaapps.billing;

/**
 * Created by jithin suresh on 29-01-2017.
 */

public class Billing_ListItem {

    String itemText;
    boolean isSelected;
    int position;

    Billing_ListItem()
    {
        isSelected = false;
    }

    public void setItemText(String str)
    {
        itemText = str;
    }
    public String getItemText()
    {
        return itemText;
    }

    public void setSelected(boolean val)
    {
        isSelected = val;
    }
    public boolean getSelected()
    {
        return isSelected;
    }

    public void setPosition(int pos) {position = pos;}
    public int getPosition() { return  position;}
}
