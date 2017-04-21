package com.jaapps.billing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class Billing_purchase extends AppCompatActivity {

    AlphaAnimation buttonClick;
    GridView lv_item;
    Billing_adapterItem adapterItem;
    List<Billing_ListItem> list;

    boolean editFlag;
    String pdtId;


    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;

    void print(String str){ Log.d("JKS",str);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_purchase);

        buttonClick = new AlphaAnimation(1F, 0.8F);

        lv_item = (GridView) findViewById(R.id.lv_purchase_select_item);
        list = new ArrayList<>();
        lv_item.setNumColumns(2);

        TextView txt_select_label = (TextView)findViewById(R.id.txt_purchase_select_itemType);
        TextView txt_brand_label = (TextView)findViewById(R.id.txt_purchase_brand_label);
        TextView txt_price_label = (TextView)findViewById(R.id.txt_purchase_price_label);
        TextView txt_sellprice_label = (TextView)findViewById(R.id.txt_purchase_sellprice_label);
        TextView txt_qty_label = (TextView)findViewById(R.id.txt_purchase_qty_label);
        TextView txt_barcode = (TextView)findViewById(R.id.txt_purchase_barcode);

        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        txt_barcode.setText("");

        txt_select_label.setTypeface(font);
        txt_brand_label.setTypeface(font);
        txt_price_label.setTypeface(font);
        txt_sellprice_label.setTypeface(font);
        txt_qty_label.setTypeface(font);
        txt_barcode.setTypeface(font);

        Button btn_addItem = (Button)findViewById(R.id.btn_purchase_addItem);
        Button btn_scan = (Button)findViewById(R.id.btn_purchase_scancode);
        Button btn_save = (Button)findViewById(R.id.btn_purchase_save);

        btn_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent = new Intent(Billing_purchase.this, Billing_addItem.class);
                intent.putExtra("edit","false");
                startActivityForResult(intent,101);
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
           //     Toast.makeText(Billing_purchase.this, "This feature is not available, wait for update", Toast.LENGTH_SHORT).show();

                IntentIntegrator scanIntegrator = new IntentIntegrator(Billing_purchase.this);
                scanIntegrator.initiateScan();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                EditText edit_descritpion = (EditText)findViewById(R.id.edit_purchase_description);
                EditText edit_price = (EditText)findViewById(R.id.edit_purchase_price);
                EditText edit_selling_price = (EditText)findViewById(R.id.edit_purchase_selprice);
                EditText edit_qty = (EditText)findViewById(R.id.edit_purchase_qty);

                TextView txt_barcode = (TextView)findViewById(R.id.txt_purchase_barcode);

                int selected = -1;

                for (Billing_ListItem item:list ) {
                    if(item.getSelected() == true)
                    {
                        selected = item.getPosition();
                        break;
                    }
                }
                if(selected == -1)
                {
                    Toast.makeText(getApplicationContext(), "Please select the product",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(edit_descritpion.getText().toString().equals("") || edit_price.getText().toString().equals("") || edit_selling_price.getText().toString().equals("") )
                {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                String  description = edit_descritpion.getText().toString();
                int price = Integer.parseInt(edit_price.getText().toString());
                int sellingPrice = Integer.parseInt(edit_selling_price.getText().toString());
                int qty = Integer.parseInt(edit_qty.getText().toString());

                String barcode = txt_barcode.getText().toString();

                Billing_data data = new Billing_data(getApplicationContext());
                data.openConnection();

                int serialNo = 0;

                if(editFlag)
                {
                    serialNo = data.updateProduct(selected, description, price, sellingPrice, qty, barcode, pdtId);
                    Toast.makeText(getApplicationContext(), "Data updated",Toast.LENGTH_SHORT).show();
                }
                else {
                    serialNo = data.addPdt(selected, description, price, sellingPrice, qty, barcode);
                    Toast.makeText(getApplicationContext(), "Serial Number = "+serialNo,Toast.LENGTH_SHORT).show();
                }


                                /* update to database */
                print("update firbase with new product");
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                mFirebaseInstance = FirebaseDatabase.getInstance();
                mDatabase = mFirebaseInstance.getReference("Data");
                final String userId = data.getUserId();
                final DatabaseReference ref_pdts = database.getReference("Data").child(userId).child("PRODUCTS");
                ref_pdts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Billing_data data = new Billing_data(getApplicationContext());
                        data.openConnection();
                        //Message post = dataSnapshot.getValue(Message.class);
                        long firebaseItemsCount = dataSnapshot.getChildrenCount();
                        Log.e("JKS" ,""+dataSnapshot.getChildrenCount());
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

                data.closeConnection();


                finish();
            }
        });

        fillItemList();

        Intent argument = getIntent();

        String edit = argument.getStringExtra("edit");
        if(edit.equals("true"))
        {
            editFlag = true;
            pdtId = argument.getStringExtra("id");
        }
        else
        {
            editFlag = false;
            print("Edit mode is disabled for purchase");
        }

        if(editFlag)
        {
            print("Edit mode is on");
            Billing_data data = new Billing_data(this);
            data.openConnection();

            Cursor pdtData = data.getPdtInfoFromId(pdtId);


            for (Billing_ListItem item:list ) {

            }

            while (pdtData.moveToNext())
            {
                int position = pdtData.getInt(2);

                Cursor Citem = data.getItem(pdtData.getString(2));
                String itemName="";
                while(Citem.moveToNext())
                {
                    itemName = Citem.getString(0);
                }
                for (Billing_ListItem item:list ) {
                    if(item.getItemText().equals(itemName))
                    {
                        item.setSelected(true);
                    }
                }

                adapterItem.notifyDataSetChanged();
                EditText edit_descritpion = (EditText)findViewById(R.id.edit_purchase_description);
                EditText edit_price = (EditText)findViewById(R.id.edit_purchase_price);
                EditText edit_selling_price = (EditText)findViewById(R.id.edit_purchase_selprice);
                EditText edit_qty = (EditText)findViewById(R.id.edit_purchase_qty);

                edit_descritpion.setText(pdtData.getString(3));
                edit_price.setText(pdtData.getString(4));
                edit_selling_price.setText(pdtData.getString(5));
                edit_qty.setText(pdtData.getString(6));
                txt_barcode.setText(pdtData.getString(8));

            }

            data.closeConnection();
        }
    }

    private void fillItemList()
    {
        Billing_data data = new Billing_data(this);
        data.openConnection();
        Cursor itemData = data.getItemList();

        while (itemData.moveToNext())
        {
            print("item = "+itemData.getString(0)+ "  id = "+itemData.getInt(2));
            Billing_ListItem item = new Billing_ListItem();
            item.setItemText(itemData.getString(0));
            item.setPosition(itemData.getInt(2));
            list.add(item);
        }

        data.closeConnection();

        adapterItem = new Billing_adapterItem(this,list);
        lv_item.setAdapter(adapterItem);

        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Billing_ListItem data = list.get(position);

                for (Billing_ListItem item:list ) {
                        item.setSelected(false);
                }
                data.setSelected(true);
                adapterItem.notifyDataSetChanged();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 101) {
            // Make sure the request was successful
           list.clear();

            Billing_data dat = new Billing_data(this);
            dat.openConnection();
            Cursor itemData = dat.getItemList();

            while (itemData.moveToNext())
            {

                Billing_ListItem item = new Billing_ListItem();
                item.setItemText(itemData.getString(0));
                item.setPosition(itemData.getInt(2));
                list.add(item);
            }

            dat.closeConnection();
            adapterItem.notifyDataSetChanged();

        }
        else
        {
            TextView barcode = (TextView)findViewById(R.id.txt_purchase_barcode);
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                print("scan content! " + scanContent);
                print("scan fmt ! " + scanFormat);

                Billing_data dataB = new Billing_data(this);
                dataB.openConnection();
                if(!dataB.isRepeatedBarcode(scanContent)) {
                    barcode.setText(scanContent);
                }
                else
                {
                    Toast.makeText(this, "This barcode is already in use. Please don't use this barcode.", Toast.LENGTH_LONG).show();
                }
                dataB.closeConnection();

            }else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Barcode Scanning Failed!", Toast.LENGTH_SHORT);
                toast.show();

            }
        }
    }
}

