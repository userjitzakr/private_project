package com.jaapps.billing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Billing_FrontPage extends AppCompatActivity {

    AlphaAnimation buttonClick;

    private InterstitialAd mInterstitialAd;

    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    Profile profile;

    void print(String str){
    //    Log.d("JKS",str);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_billing__front_page);

        SharedPreferences prefs = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE);
        String restoredText = prefs.getString("activated", null);
        if (restoredText != null) {

        }
        else
        {
            Intent activation = new Intent(this, Billing_activation.class);
            startActivity(activation);
            finish();
            return;
        }
        displayAdvertisement();
        Intent dataIntent = getIntent();
        String loginstat = dataIntent.getStringExtra("LOGINSTAT");
        if(loginstat != null && loginstat.equals("success"))
        {
            print("LOGIN IS SUCCESS");
            profile = Profile.getCurrentProfile();
            int count = 0;
            while(profile == null && count > 10)
            {
                print("Facebook profile came as null after login, there is some problem,");
                try {
                    Thread.sleep(100);
                }
                catch (Exception e)
                {

                }
                count++;
            }
            if(count >10 && profile == null)
            {
                print("Exit ");
                finish();
            }
        }
        else
        {
            print("LOGIN is not there");
            profile = Profile.getCurrentProfile();
        }



        if(profile != null) {

            print(profile.getId());
            String userId = profile.getId();
            Billing_data data = new Billing_data(this);
            data.openConnection();

            data.updateUserID(userId);
        }
        else {
            print("profile is null; Load login page again");
            Intent activation = new Intent(this, Billing_Login.class);
            startActivity(activation);
            finish();
        }

        /* sync */
        syncWithFirbase();

        buttonClick = new AlphaAnimation(1F, 0.8F);
        Button btnSale = (Button) findViewById(R.id.btn_frontPage_sale);
        Button btnStock = (Button) findViewById(R.id.btn_frontPage_stock);
        Button btnPurchase = (Button) findViewById(R.id.btn_frontPage_purchase);
        Button btnSettings = (Button) findViewById(R.id.btn_frontPage_settings);

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent intx = new Intent(Billing_FrontPage.this, Billing_salePage.class);
                startActivity(intx);
                displayAdvertisement();


            }
        });

        btnStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent intx = new Intent(Billing_FrontPage.this, Billing_stock.class);
                startActivity(intx);
                displayAdvertisement();
            }
        });

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intx = new Intent(Billing_FrontPage.this, Billing_purchase.class);
                intx.putExtra("edit","false");
                startActivity(intx);
                displayAdvertisement();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent intx = new Intent(Billing_FrontPage.this, Billing_settings.class);
                startActivity(intx);
                displayAdvertisement();
            }
        });
    }
    private void displayAdvertisement()
    {
        SharedPreferences Aprefs = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE);
        String activated = Aprefs.getString("activated", null);
        if (activated != null) {
            print("Prime user, no ads");
            return;
        }
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private void loadInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("09CF226096BF999897E02AC6B605CD2F").build();
        //.setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                print("ADD LOADED");
                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                print("ADD failed to load" +errorCode);
            }

            @Override
            public void onAdClosed() {
                print("Add closed");
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            print("SHOWING ADD");
            mInterstitialAd.show();
        }
    }

    void syncWithFirbase()
    {
        print("Start Sqlite - firebase sync");

        /* local sqlite connection */
        Billing_data data = new Billing_data(this);
        data.openConnection();
        data.getUserId();

        /* connect to firebase */
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("Data");

        final String userId = data.getUserId();


        /* backup ITEM */
        /* get item from firebase */
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("Data").child(userId).child("ITEMS");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Billing_data data = new Billing_data(getApplicationContext());
                data.openConnection();
                //Message post = dataSnapshot.getValue(Message.class);
                long firebaseItemsCount = dataSnapshot.getChildrenCount();
                //Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                Cursor items = data.getAllItemsForFirebase();
                if(items.getCount() > firebaseItemsCount) {
                    print("Sqlite has more data than Firebase; Uploading items table to firebase : for items");
                    while (items.moveToNext()) {
                        String key = items.getString(2);
                        Billing_FirebaseData_items firebaseItemsData = new Billing_FirebaseData_items(items.getString(0), items.getString(1));
                        mDatabase.child(userId).child("ITEMS").child(key).setValue(firebaseItemsData);
                    }
                }
                else if(items.getCount() < firebaseItemsCount)
                {
                    print("Firebase has more data than sqlite, data sync from Firebase : for items");

                    /* clear database */
                    data.clearItemTable();

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Billing_FirebaseData_items post = postSnapshot.getValue(Billing_FirebaseData_items.class);

                        if(post != null) {
                            print("ITEMS = " + post.getName() + " auther=" + post.getDescription());
                            data.addItem(post.getName(),post.getDescription());
                        }
                    }
                }
                else
                {
                    print("Sync is not required for ITEMS table");
                }

                data.closeConnection();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                print("The read failed: " + databaseError.getCode());

            }
        });

        DatabaseReference ref_bills = database.getReference("Data").child(userId).child("BILLS");
        ref_bills.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Billing_data data = new Billing_data(getApplicationContext());
                data.openConnection();
                //Message post = dataSnapshot.getValue(Message.class);
                long firebaseItemsCount = dataSnapshot.getChildrenCount();
                //Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                Cursor items = data.getAllBillsForFirebase();
                if(items.getCount() > firebaseItemsCount) {
                    print("Sqlite has more data than Firebase; Uploading bills table to firebase: for bills");
                    while (items.moveToNext()) {
                        String key = items.getString(0);
                        Billing_FirebaseData_Bills firebaseItemsData = new Billing_FirebaseData_Bills(
                                items.getString(0),
                                items.getString(1),
                                items.getString(2),
                                items.getString(3),
                                items.getString(4),
                                items.getString(5),
                                items.getString(6),
                                items.getString(7));
                        mDatabase.child(userId).child("BILLS").child(key).setValue(firebaseItemsData);
                    }

                    final DatabaseReference ref_billItem = database.getReference("Data").child(userId).child("BILL_ITEMS");
                    ref_billItem.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Billing_data data = new Billing_data(getApplicationContext());
                            data.openConnection();

                            long firebaseItemsCount = dataSnapshot.getChildrenCount();

                            //Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                            Cursor items = data.getAllBillItemsForFirebase();

                            dataSnapshot.getRef().setValue(null);
                            print("Sqlite has more data than Firebase; Uploading bill items table to firebase : for bill items");
                            while (items.moveToNext()) {
                                String key = mDatabase.push().getKey();

                                Billing_FirebaseData_BillItems firebaseItemsData = new Billing_FirebaseData_BillItems(
                                        items.getString(0),
                                        items.getString(1));

                                mDatabase.child(userId).child("BILL_ITEMS").child(key).setValue(firebaseItemsData);
                            }

                            data.closeConnection();
                            ref_billItem.removeEventListener(this);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            print("The read failed: " + databaseError.getCode());

                        }
                    });


                    final DatabaseReference ref_pdts = database.getReference("Data").child(userId).child("PRODUCTS");
                    ref_pdts.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Billing_data data = new Billing_data(getApplicationContext());
                            data.openConnection();
                            //Message post = dataSnapshot.getValue(Message.class);
                            long firebaseItemsCount = dataSnapshot.getChildrenCount();
                            //Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                            Cursor items = data.getAllPdtsForFirebase();
                            //if(items.getCount() > firebaseItemsCount) {
                                print("Sqlite has more data than Firebase; Uploading Products table to firebase : for products");
                                while (items.moveToNext()) {
                                    String key = items.getString(0);
                                    Billing_FirebaseData_products firebaseItemsData = new Billing_FirebaseData_products(
                                            items.getString(1),
                                            items.getString(2),
                                            items.getString(3),
                                            items.getString(4),
                                            items.getString(5),
                                            items.getString(6),
                                            items.getString(7),
                                            items.getString(8),
                                            items.getString(9));
                                    mDatabase.child(userId).child("PRODUCTS").child(key).setValue(firebaseItemsData);
                                }
                           // }
                          /*  else if(items.getCount() < firebaseItemsCount)
                            {
                                print("Firebase has more data than sqlite, data sync from Firebase : for products");

                                data.clearPdtTable();
                                data.clearSerialNum();

                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Billing_FirebaseData_products post = postSnapshot.getValue(Billing_FirebaseData_products.class);

                                    if(post != null) {
                                        print("SerialNum="+post.getSerialNum()+
                                                " itemId="+post.getItemId()+
                                                " description="+post.getDescription()+
                                                " price="+post.getPrice()+
                                                " sellingPrice="+post.getSellingPrice()+
                                                " qty="+post.getQty()+
                                                " soldCount="+post.getSoldCount()+
                                                " barcode="+post.getBarcode()+
                                                " stockDate= "+post.getStockDate());

                                        data.addPdtFromFireBase(post.getItemId(),
                                                post.getDescription(),
                                                post.getPrice(),
                                                post.getSellingPrice(),
                                                post.getQty(),
                                                post.getSoldCount(),
                                                post.getBarcode(),
                                                post.getStockDate());
                                    }
                                }

                            }
                            else
                            {
                                print("Sync is not required for PRODUCTS table");
                            }*/
                            data.closeConnection();
                            ref_pdts.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            print("The read failed: " + databaseError.getCode());

                        }
                    });

                }
                else if(items.getCount() < firebaseItemsCount)
                {
                    print("Firebase has more data than sqlite, data sync from Firebase : for bills");

                    data.clearbillTable();
                    data.clearBillSerialNum();

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Billing_FirebaseData_Bills post = postSnapshot.getValue(Billing_FirebaseData_Bills.class);

                        if(post != null) {
                            print("billId="+post.getBillId()+
                                    " date="+post.getDate()+
                                    " discount="+post.getDiscount()+
                                    " total="+post.getTotal()+
                                    " total_tax="+post.getTotal_tax()+
                                    " gtant total="+post.getGrantTotal()+
                                    " custName="+post.getCustomerName()+
                                    " custaddr="+post.getCustomerAddress());

                            data.addBillFromFireBase(post.getBillId(),post.getDate(),post.getDiscount(),post.getTotal(),post.getTotal_tax(),
                                    post.getGrantTotal(),post.getCustomerName(),post.getCustomerAddress());
                        }
                    }


                    final DatabaseReference ref_billItem = database.getReference("Data").child(userId).child("BILL_ITEMS");
                    ref_billItem.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Billing_data data = new Billing_data(getApplicationContext());
                            data.openConnection();

                            long firebaseItemsCount = dataSnapshot.getChildrenCount();
                            //Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                            Cursor items = data.getAllBillItemsForFirebase();

                            if(items.getCount() < firebaseItemsCount)
                            {
                                print("Firebase has more data than sqlite, data sync from Firebase: for bill items");

                                data.clearbillItemTable();

                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Billing_FirebaseData_BillItems post = postSnapshot.getValue(Billing_FirebaseData_BillItems.class);

                                    if(post != null) {
                                        print("billId="+post.getBillId()+
                                                " itemId="+post.getItemId());
                                        data.addBillItemFromFireBase(post.getBillId(),post.getItemId());
                                    }
                                }
                            }

                            data.closeConnection();
                            ref_billItem.removeEventListener(this);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            print("The read failed: " + databaseError.getCode());

                        }
                    });



                    final DatabaseReference ref_pdts = database.getReference("Data").child(userId).child("PRODUCTS");
                    ref_pdts.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Billing_data data = new Billing_data(getApplicationContext());
                            data.openConnection();
                            //Message post = dataSnapshot.getValue(Message.class);
                            long firebaseItemsCount = dataSnapshot.getChildrenCount();
                            //Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                            Cursor items = data.getAllPdtsForFirebase();

                            {
                                print("Firebase has more data than sqlite, data sync from Firebase : for products");

                                data.clearPdtTable();
                                data.clearSerialNum();

                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Billing_FirebaseData_products post = postSnapshot.getValue(Billing_FirebaseData_products.class);

                                    if(post != null) {
                                        print("SerialNum="+post.getSerialNum()+
                                                " itemId="+post.getItemId()+
                                                " description="+post.getDescription()+
                                                " price="+post.getPrice()+
                                                " sellingPrice="+post.getSellingPrice()+
                                                " qty="+post.getQty()+
                                                " soldCount="+post.getSoldCount()+
                                                " barcode="+post.getBarcode()+
                                                " stockDate= "+post.getStockDate());

                                        data.addPdtFromFireBase(post.getItemId(),
                                                post.getDescription(),
                                                post.getPrice(),
                                                post.getSellingPrice(),
                                                post.getQty(),
                                                post.getSoldCount(),
                                                post.getBarcode(),
                                                post.getStockDate());
                                    }
                                }

                            }

                            data.closeConnection();
                            ref_pdts.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            print("The read failed: " + databaseError.getCode());

                        }
                    });

                }
                else
                {
                    print("Sync is not required for BILLS table");
                }

                data.closeConnection();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                print("The read failed: " + databaseError.getCode());

            }
        });

        DatabaseReference ref_company = database.getReference("Data").child(userId).child("COMPANY");
        ref_company.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                print("JKS got data for company " +dataSnapshot.getChildrenCount());

                if(dataSnapshot.getChildrenCount() ==0)
                {
                    SharedPreferences prefs = getSharedPreferences("MYCOMPANY", MODE_PRIVATE);
                    String name = prefs.getString("name", null);
                    String ad1 = prefs.getString("addressline1", null);
                    String ad2 = prefs.getString("addressline2", null);
                    String ad3 = prefs.getString("addressline3", null);
                    String phone = prefs.getString("phone", null);
                    String tin = prefs.getString("tin", null);
                    if(name != null) {
                        Billing_FirebaseData_company compny = new Billing_FirebaseData_company(name, ad1, ad2, ad3, phone, tin);
                        mDatabase.child(userId).child("COMPANY").child("DETAILS").setValue(compny);
                    }
                }
                else {
                    SharedPreferences prefs = getSharedPreferences("MYCOMPANY", MODE_PRIVATE);
                    if(prefs.getString("name", null) == null) {
                        print("Company profile is not set; updating it from firebase");
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Billing_FirebaseData_company post = postSnapshot.getValue(Billing_FirebaseData_company.class);

                            SharedPreferences.Editor editor = getSharedPreferences("MYCOMPANY", MODE_PRIVATE).edit();
                            editor.putString("name", post.getName());
                            editor.putString("addressline1", post.getAddress1());
                            editor.putString("addressline2", post.getAddress2());
                            editor.putString("addressline3", post.getAddress3());
                            editor.putString("phone", post.getPhone());
                            editor.putString("tin", post.getTin());
                            editor.apply();
                            editor.commit();
                        }
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                print("The read failed: " + databaseError.getCode());

            }
        });

        DatabaseReference ref_act = database.getReference("Data").child(userId).child("ACTIVATION");
        ref_act.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                print("JKS got data for company " +dataSnapshot.getChildrenCount());

                if(dataSnapshot.getChildrenCount() ==0)
                {
                    SharedPreferences Aprefs = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE);
                    String activated = Aprefs.getString("activated", null);
                    if (activated != null) {
                        Billing_FirebaseData_activationStatus activationStat = new Billing_FirebaseData_activationStatus("ACTIVATED");
                        mDatabase.child(userId).child("ACTIVATION").child("STATUS").setValue(activationStat);

                    }
                }
                else {
                    SharedPreferences Aprefs = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE);
                    String activated = Aprefs.getString("activated", null);
                    if (activated == null) {
                        SharedPreferences.Editor editor = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE).edit();
                        editor.putString("activated", "true");
                        editor.apply();
                        editor.commit();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                print("The read failed: " + databaseError.getCode());

            }
        });


/*        final DatabaseReference ref_pdts = database.getReference("Data").child(userId).child("PRODUCTS");
        ref_pdts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Billing_data data = new Billing_data(getApplicationContext());
                data.openConnection();
                //Message post = dataSnapshot.getValue(Message.class);
                long firebaseItemsCount = dataSnapshot.getChildrenCount();
                Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                Cursor items = data.getAllPdtsForFirebase();
                *//*if(items.getCount() > firebaseItemsCount) {
                    print("Sqlite has more data than Firebase; Uploading Products table to firebase : for products");
                    while (items.moveToNext()) {
                        String key = items.getString(0);
                        Billing_FirebaseData_products firebaseItemsData = new Billing_FirebaseData_products(
                                items.getString(1),
                                items.getString(2),
                                items.getString(3),
                                items.getString(4),
                                items.getString(5),
                                items.getString(6),
                                items.getString(7),
                                items.getString(8),
                                items.getString(9));
                        mDatabase.child(userId).child("PRODUCTS").child(key).setValue(firebaseItemsData);
                    }
                }
                else*//* if(items.getCount() < firebaseItemsCount)
                {
                    print("Firebase has more data than sqlite, data sync from Firebase : for products");

                    data.clearPdtTable();
                    data.clearSerialNum();

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Billing_FirebaseData_products post = postSnapshot.getValue(Billing_FirebaseData_products.class);

                        if(post != null) {
                            print("SerialNum="+post.getSerialNum()+
                                    " itemId="+post.getItemId()+
                                    " description="+post.getDescription()+
                                    " price="+post.getPrice()+
                                    " sellingPrice="+post.getSellingPrice()+
                                    " qty="+post.getQty()+
                                    " soldCount="+post.getSoldCount()+
                                    " barcode="+post.getBarcode()+
                                    " stockDate= "+post.getStockDate());

                            data.addPdtFromFireBase(post.getItemId(),
                                    post.getDescription(),
                                    post.getPrice(),
                                    post.getSellingPrice(),
                                    post.getQty(),
                                    post.getSoldCount(),
                                    post.getBarcode(),
                                    post.getStockDate());
                        }
                    }

                }
                else
                {
                    print("Sync is not required for PRODUCTS table");
                }
                data.closeConnection();
                ref_pdts.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                print("The read failed: " + databaseError.getCode());

            }
        });*/

/*
        final DatabaseReference ref_billItem = database.getReference("Data").child(userId).child("BILL_ITEMS");

        // Attach a listener to read the data at our posts reference
        ref_billItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Billing_data data = new Billing_data(getApplicationContext());
                data.openConnection();
                //Message post = dataSnapshot.getValue(Message.class);
                long firebaseItemsCount = dataSnapshot.getChildrenCount();
                Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
                Cursor items = data.getAllBillItemsForFirebase();
                if(items.getCount() > firebaseItemsCount) {
                    dataSnapshot.getRef().setValue(null);
                    print("Sqlite has more data than Firebase; Uploading bill items table to firebase : for bill items");
                    while (items.moveToNext()) {
                        String key = mDatabase.push().getKey();
                        Billing_FirebaseData_BillItems firebaseItemsData = new Billing_FirebaseData_BillItems(
                                items.getString(0),
                                items.getString(1));
                        mDatabase.child(userId).child("BILL_ITEMS").child(key).setValue(firebaseItemsData);
                    }
                }
                else if(items.getCount() < firebaseItemsCount)
                {
                    print("Firebase has more data than sqlite, data sync from Firebase: for bill items");

                    data.clearbillItemTable();

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Billing_FirebaseData_BillItems post = postSnapshot.getValue(Billing_FirebaseData_BillItems.class);

                        if(post != null) {
                            print("billId="+post.getBillId()+
                                    " itemId="+post.getItemId());
                            data.addBillItemFromFireBase(post.getBillId(),post.getItemId());
                        }
                    }
                }
                else
                {
                    print("Sync is not required for BILL ITEMS table");
                }
                data.closeConnection();
                ref_billItem.removeEventListener(this);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                print("The read failed: " + databaseError.getCode());

            }
        });
*/


        print("Sync Sqlite is Done");
        data.closeConnection();

    }
}
