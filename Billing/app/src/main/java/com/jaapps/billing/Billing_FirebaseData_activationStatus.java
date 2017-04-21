package com.jaapps.billing;

/**
 * Created by jithin suresh on 14-04-2017.
 */

public class Billing_FirebaseData_activationStatus {

    String activation;

    public Billing_FirebaseData_activationStatus()
    {

    }

    public Billing_FirebaseData_activationStatus(String data)
    {
        this.activation = data;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

}
