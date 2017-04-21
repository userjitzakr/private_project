package com.jaapps.billing;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Billing_history extends AppCompatActivity {

    AlphaAnimation buttonClick;

    boolean from;
    boolean to;

    boolean setFrom = false;
    boolean setTo = false;

    String fromDate, toDate;

    int totalSale = 0;
    int totalPrice = 0;

    List<Billing_ListBillView> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_history);

        TextView txt_from = (TextView)findViewById(R.id.txt_hostory_from_label);
        TextView txt_to = (TextView)findViewById(R.id.txt_history_to_label);
        TextView txt_sale_label = (TextView)findViewById(R.id.txt_history_sale_label);
        TextView txt_profit_label = (TextView)findViewById(R.id.txt_history_profit_label);
        TextView txt_totalSale = (TextView)findViewById(R.id.txt_history_total_sale);
        TextView txt_profit = (TextView)findViewById(R.id.txt_histor_profit);




        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        txt_from.setTypeface(font);
        txt_to.setTypeface(font);
        txt_sale_label.setTypeface(font);
        txt_profit_label.setTypeface(font);
        txt_totalSale.setTypeface(font);
        txt_profit.setTypeface(font);




        Button history_from = (Button)findViewById(R.id.btn_history_from);
        Button history_to = (Button)findViewById(R.id.btn_history_to);
        buttonClick = new AlphaAnimation(1F, 0.8F);
        final EditText edit_frm = (EditText)findViewById(R.id.edit_history_from);
        final EditText edit_to = (EditText)findViewById(R.id.edit_history_to);

        edit_frm.setText("");
        edit_to.setText("");
        txt_profit.setText("");
        txt_totalSale.setText("");

        edit_frm.setEnabled(false);
        edit_to.setEnabled(false);
        edit_frm.setTypeface(font);
        edit_to.setTypeface(font);

        list = new ArrayList<>();

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                if(from)
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    myCalendar.set(Calendar.HOUR,0);
                    myCalendar.set(Calendar.MINUTE,0);
                    myCalendar.set(Calendar.SECOND,0);

                    fromDate = dateFormat.format(myCalendar.getTime());

                    print("todate ="+fromDate);

                    edit_frm.setText(sdf.format(myCalendar.getTime()));
                    setFrom = true;
                }
                else {

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    myCalendar.set(Calendar.HOUR,23);
                    myCalendar.set(Calendar.MINUTE,59);
                    myCalendar.set(Calendar.SECOND,59);

                    toDate = dateFormat.format(myCalendar.getTime());
                    print("todate ="+toDate);

                    edit_to.setText(sdf.format(myCalendar.getTime()));
                    setTo = true;
                }

                if(setTo && setFrom)
                {
                    loadBillList();
                }
            }



        };

        edit_frm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Billing_history.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                from = true;
                to = false;

            }
        });

        edit_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Billing_history.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                from = false;
                to = true;
            }
        });

        history_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                new DatePickerDialog(Billing_history.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                from = true;
                to = false;

            }
        });

        history_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                new DatePickerDialog(Billing_history.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                from = false;
                to = true;

            }
        });



    }
    void loadBillList() {


        Billing_adapterBillView adapterBillView = new Billing_adapterBillView(getApplicationContext(), list);
        Billing_data data = new Billing_data(getApplicationContext());
        data.openConnection();
        totalSale = 0;
        totalPrice = 0;
        Cursor c = data.getBillDate(fromDate, toDate);

        list.clear();

        while (c.moveToNext()) {
            Billing_ListBillView item = new Billing_ListBillView();
            item.setbilId(c.getString(0));
            item.setbillDate(c.getString(1));
            item.setbillAmount(c.getString(5));
            totalSale += c.getInt(5);
            list.add(item);
            totalPrice += data.getTotalPriceOfBillId(c.getInt(0));
        }

        data.closeConnection();
        print("Total sale ="+totalSale+" total price="+totalPrice);

        TextView txt_totalSale = (TextView)findViewById(R.id.txt_history_total_sale);
        txt_totalSale.setText(""+totalSale);
        TextView txt_profit = (TextView)findViewById(R.id.txt_histor_profit);
        txt_profit.setText(""+(totalSale - totalPrice));

        ListView lstItem = (ListView) findViewById(R.id.lv_history);
        lstItem.setAdapter(adapterBillView);
        adapterBillView.notifyDataSetChanged();

        lstItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Billing_ListBillView item = list.get(position);
                Intent intent = new Intent(Billing_history.this, Billing_bill.class);
                intent.putExtra("bill_id",Integer.parseInt(item.getbilId()));
                intent.putExtra("history",1);
                startActivity(intent);


            }
        });

    }

    void print(String str) {
        Log.d("JKS",str);}
}

