package com.jaapps.billing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class Billing_salePage extends AppCompatActivity {

    AlphaAnimation buttonClick;

    ListView lv_makeBill;
    Billing_adapterMakeBill adapterMakeBill;
    List<Billing_ListMakeBill> list;
    List<Billing_ListMakeBill> origList;


    int slNo = 0;

    int total = 0;
    int stax = 0;
    int discount = 0;
    int grantTotal = 0;

    int taxPercentage = 15;

    TextView txt_bill_total;
    TextView txt_bill_serviceTax;
    TextView txt_bill_grant_total;

    int editBill;
    int editBillId;

    String customerName = "Customer";
    String customerAddress = "nil";

    String custName = "";
    String custAddr = "";

    List<String> listOfString ;
    List<Billing_SearchQueryData> listSearchQuery;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_sale_page);

        SharedPreferences prefs = getSharedPreferences("SERVICE_TAX", MODE_PRIVATE);
        String restoredText = prefs.getString("tax", null);
        if (restoredText != null) {
            taxPercentage = Integer.parseInt(restoredText);
        }

        listOfString = new ArrayList<String>();
        listSearchQuery = new ArrayList<>();

        Billing_data getList_Data = new Billing_data(this);
        getList_Data.openConnection();

        Cursor dat = getList_Data.getListItemDescription();

        while (dat.moveToNext())
        {
            Billing_SearchQueryData itemSearch = new Billing_SearchQueryData();
            itemSearch.setSerialNum(dat.getString(0));
            itemSearch.setDescripition(dat.getString(1));
            listSearchQuery.add(itemSearch);
            listOfString.add(dat.getString(1));
        }

        getList_Data.closeConnection();

        // In the onCreate method
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.edit_sale_pdtId);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listOfString);
        textView.setAdapter(adapter);


        total = 0;
        discount = 0;
        stax = 0;
        grantTotal = 0;

        txt_bill_total = (TextView)findViewById(R.id.txt_sale_total);
        txt_bill_serviceTax = (TextView)findViewById(R.id.txt_sale_tax);
        txt_bill_grant_total = (TextView)findViewById(R.id.txt_sale_grant_total);

        txt_bill_total.setText("");
        txt_bill_serviceTax.setText("");
        txt_bill_grant_total.setText("");

        lv_makeBill = (ListView)findViewById(R.id.lv_sale);
        list = new ArrayList<>();
        origList = new ArrayList<>();
        adapterMakeBill = new Billing_adapterMakeBill(this, list);
        lv_makeBill.setAdapter(adapterMakeBill);
        lv_makeBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Billing_ListMakeBill item = list.get(position);
                print("amount = "+item.getAmount()) ;
                int amount = Integer.parseInt(item.getAmount());
                total -= amount;
                stax = total * taxPercentage /100;
                grantTotal = total + stax - discount;
                list.remove(position);
                adapterMakeBill.notifyDataSetChanged();

                txt_bill_total.setText(""+total);
                txt_bill_serviceTax.setText(""+stax);
                txt_bill_grant_total.setText(""+grantTotal);
            }
        });


        buttonClick = new AlphaAnimation(1F, 0.8F);
        slNo = 0;

        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        TextView txtItemCode = (TextView)findViewById(R.id.txt_sale_itemCode);
        TextView txt_slno = (TextView)findViewById(R.id.txt_sale_slno);
        TextView txt_item = (TextView)findViewById(R.id.txt_sale_item);
        TextView txt_descr = (TextView)findViewById(R.id.txt_sale_descr);
        TextView txt_amt = (TextView)findViewById(R.id.txt_sale_amount);
        TextView txt_total_label = (TextView)findViewById(R.id.txt_sale_total_label);
        TextView txt_tax_label = (TextView)findViewById(R.id.txt_sale_tax_label);
        TextView txt_discount = (TextView)findViewById(R.id.txt_sale_discount_label);
        TextView txt_payable_label = (TextView)findViewById(R.id.txt_sale_grant_total_label);


        txtItemCode.setTypeface(font);
        txt_slno.setTypeface(font);
        txt_item.setTypeface(font);
        txt_descr.setTypeface(font);
        txt_amt.setTypeface(font);

        txt_total_label.setTypeface(font);
        txt_tax_label.setTypeface(font);
        txt_discount.setTypeface(font);
        txt_payable_label.setTypeface(font);

        txt_bill_grant_total.setTypeface(font);
        txt_bill_serviceTax.setTypeface(font);
        txt_bill_grant_total.setTypeface(font);

        Button btn_history = (Button) findViewById(R.id.btn_sale_history);
        Button btn_search = (Button) findViewById(R.id.btn_sale_search);
        Button btn_clear = (Button) findViewById(R.id.btn_sale_clear);
        Button btn_scan = (Button) findViewById(R.id.btn_sale_scan);
        Button btn_makebill = (Button) findViewById(R.id.btn_sale_makeBill);

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent = new Intent(Billing_salePage.this,Billing_history.class);
                startActivity(intent);

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AutoCompleteTextView id = (AutoCompleteTextView)findViewById(R.id.edit_sale_pdtId);
                if(id.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter serial number",Toast.LENGTH_SHORT).show();
                    return;
                }
                String data = id.getText().toString();
                if(isInteger(data)) {
                    int serialNumber = Integer.parseInt(data);
                    populateBillItemFromSearch(serialNumber);
                }
                else
                {
                    for (Billing_SearchQueryData item:listSearchQuery
                         ) {
                        if(item.getDescripition().equals(data))
                        {
                            populateBillItemFromSearch(Integer.parseInt(item.getSerialNum()));
                            break;
                        }
                    }
                }

            }
        });


        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                //Toast.makeText(Billing_salePage.this, "This feature is not available, wait for update", Toast.LENGTH_SHORT).show();

                IntentIntegrator scanIntegrator = new IntentIntegrator(Billing_salePage.this);
                scanIntegrator.initiateScan();
            }
        });
        btn_makebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                if(list.size() == 0)
                {
                    Toast.makeText(getApplicationContext(),"please enter products to bill",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent customer = new Intent(Billing_salePage.this, Billing_cutomer.class);
                customer.putExtra("name",custName);
                customer.putExtra("address",custAddr);
                startActivityForResult(customer,101);
                //updateBillToDatabase();

            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                list.clear();
                total = 0;
                grantTotal = 0;
                stax = 0;
                slNo = 0;
                txt_bill_total.setText(""+total);
                txt_bill_serviceTax.setText(""+stax);
                txt_bill_grant_total.setText(""+grantTotal);
                adapterMakeBill.notifyDataSetChanged();
            }
        });

        EditText edit_pdt_id = (EditText)findViewById(R.id.edit_sale_pdtId);
        edit_pdt_id.clearFocus();

        final EditText EDdiscount = (EditText)findViewById(R.id.edit_sale_discount);
        EDdiscount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                print(s.toString());
                if(!(s.toString().equals(""))) {
                    discount = Integer.parseInt(s.toString());
                    grantTotal = total + stax - discount;

                    txt_bill_total.setText("" + total);
                    txt_bill_serviceTax.setText("" + stax);
                    txt_bill_grant_total.setText("" + grantTotal);
                }
                else
                {
                    discount = 0;
                    grantTotal = total + stax - discount;

                    txt_bill_total.setText("" + total);
                    txt_bill_serviceTax.setText("" + stax);
                    txt_bill_grant_total.setText("" + grantTotal);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Intent intent = getIntent();
        editBill = intent.getIntExtra("editBill",0);
        if(editBill == 1)
        {
            print("Edit bill mode is on");
            btn_history.setEnabled(false);
            editBillId = intent.getIntExtra("billId",0);
            print("bill id = "+editBillId);

            Billing_data pdtId = new Billing_data(this);
            pdtId.openConnection();
            Cursor billPdtId = pdtId.getBillItems(editBillId);

            while(billPdtId.moveToNext())
            {
                populateBillItemFromSearch(billPdtId.getInt(0));
                Cursor pdtData = pdtId.getPdtInfo(billPdtId.getInt(0));
                while (pdtData.moveToNext()) {

                    Billing_ListMakeBill item = new Billing_ListMakeBill();
                    item.setSlNo(Integer.toString(slNo));
                    item.setItemId(pdtData.getString(0));
                    item.setItemName(pdtId.getItemName(pdtData.getInt(0)));
                    item.setDescription(pdtData.getString(1));
                    item.setAmount(pdtData.getString(2));
                    item.setItemSerialNumber(Integer.toString(billPdtId.getInt(0)));
                    origList.add(item);
                }
            }
            Cursor customer = pdtId.getBillInfo(editBillId);
            while(customer.moveToNext())
            {
                custName = customer.getString(6);
                custAddr = customer.getString(7);
            }
            pdtId.closeConnection();
        }
        else
        {
            print("Sale mode is loaded");
            btn_history.setEnabled(true);
        }
    }

    void updateBillToDatabase()
    {

        int billId = 0;
        if(editBill == 1)
        {
            billId = editBillId;
        }
        else {
            billId = getNew_billId();
        }

        Billing_data data = new Billing_data(getApplicationContext());
        data.openConnection();

        if(editBill == 1)
        {
            data.addBill(billId, list, discount, total, stax, grantTotal, origList,customerName,customerAddress);
        }
        else {
            data.addBill(billId, list, discount, total, stax, grantTotal, null,customerName,customerAddress);
        }

        data.closeConnection();

        Intent intent = new Intent(Billing_salePage.this, Billing_bill.class);
        intent.putExtra("bill_id",billId);
        startActivity(intent);
        finish();

    }
    int getNew_billId()
    {
        SharedPreferences prefs = getSharedPreferences("BILL_SERIAL_NUMBER", MODE_PRIVATE);
        String restoredText = prefs.getString("bill_no", null);
        int bill_no = 1000;
        if (restoredText != null) {
            bill_no = Integer.parseInt(restoredText);
        }
        else {
            SharedPreferences.Editor editor = getSharedPreferences("BILL_SERIAL_NUMBER", MODE_PRIVATE).edit();
            editor.putString("bill_no", "1000");
            editor.apply();
            editor.commit();
        }
        bill_no++;
        return  bill_no;
    }

    void populateBillItemFromSearch(int serialNumber)
    {

        Billing_data data = new Billing_data(getApplicationContext());
        data.openConnection();
        Cursor pdtData = data.getPdtInfo(serialNumber);
        while (pdtData.moveToNext())
        {

            slNo++;
            Billing_ListMakeBill item = new Billing_ListMakeBill();
            item.setSlNo(Integer.toString(slNo));
            item.setItemId(pdtData.getString(0));
            item.setItemName(data.getItemName(pdtData.getInt(0)));
            item.setDescription(pdtData.getString(1));
            item.setAmount(pdtData.getString(2));
            item.setItemSerialNumber(Integer.toString(serialNumber));
            list.add(item);
            adapterMakeBill.notifyDataSetChanged();

            int amount = pdtData.getInt(2);
            total += amount;
            stax = total * taxPercentage /100;
            grantTotal = total + stax - discount;

            txt_bill_total.setText(""+total);
            txt_bill_serviceTax.setText(""+stax);
            txt_bill_grant_total.setText(""+grantTotal);
        }

        EditText id = (EditText)findViewById(R.id.edit_sale_pdtId);
        if(pdtData.getCount() >0)
        {
            id.setText("");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Couldn't find the product, check the serialNumber",Toast.LENGTH_SHORT).show();
            id.setText("");
        }

        data.closeConnection();

    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (101) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    customerName= data.getStringExtra("name");
                    customerAddress = data.getStringExtra("address");

                    updateBillToDatabase();
                }
                break;

            }
            default: {
                int serialNumber;
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (scanningResult != null) {
                    String scanContent = scanningResult.getContents();
                    String scanFormat = scanningResult.getFormatName();
                    print("scan content! " + scanContent);
                    print("scan fmt ! " + scanFormat);

                    if ((scanContent != null)) {
                        print("scan content is not null");
                        print("scaned value = " + scanContent);
                        Billing_data dataB = new Billing_data(this);
                        dataB.openConnection();
                        serialNumber = dataB.getSerialNumberFromBarcode(scanContent);
                        populateBillItemFromSearch(serialNumber);
                        dataB.closeConnection();
                    }

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Barcode Scanning Failed!", Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        }
    }
    void print(String str) {
        Log.d("JKS",str);
    }


    class Billing_SearchQueryData
    {
        String serialNum;
        String descripition;

        public String getSerialNum() {
            return serialNum;
        }

        public void setSerialNum(String serialNum) {
            this.serialNum = serialNum;
        }

        public String getDescripition() {
            return descripition;
        }

        public void setDescripition(String descripition) {
            this.descripition = descripition;
        }
    };
}
