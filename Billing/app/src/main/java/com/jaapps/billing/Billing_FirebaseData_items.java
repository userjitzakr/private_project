package com.jaapps.billing;

/**
 * Created by jithin suresh on 02-04-2017.
 */

public class Billing_FirebaseData_items {

    private String name;
    private String description;


    public Billing_FirebaseData_items(String name, String description)
    {
        this.name = name;
        this.description = description;
    }
    public Billing_FirebaseData_items()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
