package com.jaapps.billing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.print.PrintHelper;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Billing_bill extends AppCompatActivity {

    AlphaAnimation buttonClick;
    TextView txt_declartion;

    int history;
    int billId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_bill);

        TextView txt_bill_no_label = (TextView) findViewById(R.id.txt_bill_no_label);
        TextView txt_bill_no = (TextView) findViewById(R.id.txt_bill_no);
        TextView txt_bill_date_label = (TextView) findViewById(R.id.txt_bill_date_label);
        TextView txt_bill_date = (TextView) findViewById(R.id.txt_bill_date);
        TextView txt_bill_slno = (TextView) findViewById(R.id.txt_bill_slno_label);
        TextView txt_bill_item = (TextView) findViewById(R.id.txt_bill_item_label);
        TextView txt_bill_decription = (TextView) findViewById(R.id.txt_bill_description_label);
        TextView txt_bill_amount = (TextView) findViewById(R.id.txt_bill_amount_label);
        TextView txt_bill_total_label = (TextView) findViewById(R.id.txt_bill_total_label);
        TextView txt_bill_total = (TextView) findViewById(R.id.txt_bill_total);
        TextView txt_bill_service_tax_label = (TextView) findViewById(R.id.txt_bill_service_tax_label);
        TextView txt_bill_service_tax = (TextView) findViewById(R.id.txt_bill_service_tax);
        TextView txt_bill_dscount_label = (TextView) findViewById(R.id.txt_bill_discount_label);
        TextView txt_bill_discount = (TextView) findViewById(R.id.txt_bill_discount);
        TextView txt_bill_gtotal_label = (TextView) findViewById(R.id.txt_bill_grant_total_label);
        TextView txt_bill_gtotal = (TextView) findViewById(R.id.txt_bill_grant_total);

        TextView txt_company = (TextView)findViewById(R.id.txt_bill_company_name);
        TextView txt_address1 = (TextView)findViewById(R.id.txt_bill_address1);
        //TextView txt_address2 = (TextView)findViewById(R.id.txt_bill_address2);
        TextView txt_address3 = (TextView)findViewById(R.id.txt_bill_address3);
        //TextView txt_phone = (TextView)findViewById(R.id.txt_bill_phone);
        TextView txt_tin = (TextView)findViewById(R.id.txt_bill_tin);
        TextView txt_tin_label = (TextView)findViewById(R.id.txt_bill_tin_label);
        TextView txt_to_label = (TextView)findViewById(R.id.txt_bill_to_label);
        TextView txt_customer = (TextView)findViewById(R.id.txt_bill_customer);
        TextView txt_customer_address = (TextView)findViewById(R.id.txt_bill_customer_address);
        TextView txt_form_label = (TextView)findViewById(R.id.txt_bill_form_8b);


        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        txt_bill_no_label.setTypeface(font);
        txt_bill_no.setTypeface(font);
        txt_bill_date_label.setTypeface(font);
        txt_bill_date.setTypeface(font);
        txt_bill_slno.setTypeface(font);
        txt_bill_item.setTypeface(font);
        txt_bill_decription.setTypeface(font);
        txt_bill_amount.setTypeface(font);
        txt_bill_total_label.setTypeface(font);
        txt_bill_total.setTypeface(font);
        txt_bill_service_tax_label.setTypeface(font);
        txt_bill_service_tax.setTypeface(font);
        txt_bill_dscount_label.setTypeface(font);
        txt_bill_discount.setTypeface(font);
        txt_bill_gtotal_label.setTypeface(font);
        txt_bill_gtotal.setTypeface(font,Typeface.BOLD);
        txt_form_label.setTypeface(font);

        txt_company.setTypeface(font,Typeface.BOLD);
        txt_address1.setTypeface(font);
        //txt_address2.setTypeface(font);
        txt_address3.setTypeface(font);
       // txt_phone.setTypeface(font);
        txt_tin.setTypeface(font);
        txt_tin_label.setTypeface(font);
        txt_to_label.setTypeface(font);
        txt_customer.setTypeface(font);
        txt_customer_address.setTypeface(font);

        txt_customer.setText("Customer");
        txt_customer_address.setText("Customer address is not given");

        SharedPreferences prefs = getSharedPreferences("MYCOMPANY", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String addressline_1 = prefs.getString("addressline1", null);
        String addressline_2 = prefs.getString("addressline2", null);
        String addressline_3 = prefs.getString("addressline3", null);
        String phone = prefs.getString("phone", null);
        String tin = prefs.getString("tin", null);
        if (name != null) {
            txt_company.setText(name);
        }
        else txt_company.setText("Company Name");
        if(addressline_1 != null)
        {
            addressline_1 += ", "+addressline_2;
            txt_address1.setText(addressline_1);
        }
        else txt_address1.setText("nil");
      /*  if(addressline_2 != null)
        {
            //txt_address2.setText(addressline_2);
        }*/
        if(addressline_3 != null)
        {
            addressline_3 += ", Phone :"+phone;
            txt_address3.setText(addressline_3);
        }
        else txt_address3.setText("nil");
        /*if(phone != null)
        {
            phone = "Phone :"+phone;
            txt_phone.setText(phone);
        }*/
        if(tin != null)
        {
            txt_tin.setText(tin);
        }
        else
        {
            txt_tin.setText("nill");
            Toast.makeText(this, "Please go to settings and fill your company profile", Toast.LENGTH_LONG).show();
        }

        Button btn_edit = (Button) findViewById(R.id.btn_bill_edit);
        Button btn_ok = (Button) findViewById(R.id.btn_bill_ok);
        Button btn_print = (Button) findViewById(R.id.btn_bill_print);
        buttonClick = new AlphaAnimation(1F, 0.8F);

         txt_declartion = (TextView)findViewById(R.id.txt_bill_declaration);
        String declaration = "E & OE\nDeclaration\nCertified that all the particulars shown in the"+
                " above tax invoice are true and correct in all respects and on which"+
                " the tax charged and collected are in accordance with the provisions of the "+
                " KVAT act 2003 and the rules made their under. It is also certified that my our registration are under KVAT act"+
                " 2003 is not subject to any Suspension Cancellation and it is valid as on the date of this bill.";
        txt_declartion.setText(declaration);
        txt_declartion.setVisibility(View.INVISIBLE);



        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent data = new Intent(Billing_bill.this,Billing_salePage.class);
                data.putExtra("editBill",1);
                data.putExtra("billId",billId);
                startActivity(data);
                finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                finish();
            }
        });

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                print("print clicked");
                printPDF();
            }
        });
        Intent data = getIntent();
        billId = data.getIntExtra("bill_id", -1);
        history = data.getIntExtra("history", -1);

        txt_bill_no.setText(""+billId);


        if (billId == -1) {
            Toast.makeText(this, "didnt prepare bill", Toast.LENGTH_SHORT).show();
        } else {
            ListView lv_makeBill = (ListView) findViewById(R.id.lv_bill_billitem);
            Billing_adapterMakeBill adapterMakeBill;
            List<Billing_ListMakeBill> list;
            list = new ArrayList<>();
            adapterMakeBill = new Billing_adapterMakeBill(this, list);
            lv_makeBill.setAdapter(adapterMakeBill);

            Billing_data database = new Billing_data(this);
            database.openConnection();

           // print("view bill info =============");
            Cursor basicBillData = database.getBillInfo(billId);
            while (basicBillData.moveToNext())
            {
                txt_bill_date.setText(basicBillData.getString(1));
                txt_bill_total.setText(basicBillData.getString(3));
                txt_bill_service_tax.setText(basicBillData.getString(4));
                txt_bill_discount.setText(basicBillData.getString(2));
                txt_bill_gtotal.setText(basicBillData.getString(5));
                txt_customer.setText(basicBillData.getString(6));
                txt_customer_address.setText(basicBillData.getString(7));

            }
          //  print("bill basic info is set");

            Cursor billItem = database.getBillItems(billId);

            int slNo = 1;

            if (billItem.getCount() != 0) {

               // print("bill has"+billItem.getCount());

                while (billItem.moveToNext()) {
                    Billing_ListMakeBill item = new Billing_ListMakeBill();
                    //print("item id = "+billItem.getInt(0));
                    Cursor itemInfo = database.getPdtInfo_byid(billItem.getInt(0));
                   // print("Call cursor with itemId");
                    if (itemInfo.getCount() != 0) {
                        while (itemInfo.moveToNext()) {

                            item.setItemId(itemInfo.getString(0));
                            item.setItemName(database.getItemName(itemInfo.getInt(1)));
                            item.setDescription(itemInfo.getString(2));
                            item.setSlNo(Integer.toString(slNo));

                            item.setAmount(itemInfo.getString(3));

                            //print("Item = "+itemInfo.getString(0)+" item Name ="+database.getItemName(itemInfo.getInt(1))+" item Description ="+itemInfo.getString(2)+" item Amount = "+itemInfo.getString(3));

                        }
                    }
                    list.add(item);
                    slNo++;
                    adapterMakeBill.notifyDataSetChanged();
                }
            }
            //adapterMakeBill.notifyDataSetChanged();
            database.closeConnection();



        }
    }
    void print(String str) {
        Log.d("JKS",str);}

    /**
     * Color matrix that flips the components (<code>-1.0f * c + 255 = 255 - c</code>)
     * and keeps the alpha intact.
     */
    private static final float[] NEGATIVE = {
            -1.0f,     0,     0,    0, 255, // red
            0, -1.0f,     0,    0, 255, // green
            0,     0, -1.0f,    0, 255, // blue
            0,     0,     0, 1.0f,   0  // alpha
    };

    private static final int RGB_MASK = 0x00FFFFFF;
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();

        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        view.draw(canvas);

        int length = returnedBitmap.getWidth()*returnedBitmap.getHeight();
        int[] array = new int[length];
        returnedBitmap.getPixels(array,0,returnedBitmap.getWidth(),0,0,returnedBitmap.getWidth(),returnedBitmap.getHeight());
        for (int i=0;i<length;i++){

            if(array[i] == 0xff03030d)
            {
                array[i]= 0;

            }
            else {

                array[i] ^= RGB_MASK;
            }
        }

        returnedBitmap.setPixels(array,0,returnedBitmap.getWidth(),0,0,returnedBitmap.getWidth(),returnedBitmap.getHeight());

        return returnedBitmap;
    }
    void printPDF()
    {
        Button btn_edit = (Button) findViewById(R.id.btn_bill_edit);
        Button btn_ok = (Button) findViewById(R.id.btn_bill_ok);
        Button btn_print = (Button) findViewById(R.id.btn_bill_print);
        btn_edit.setVisibility(View.INVISIBLE);
        btn_ok.setVisibility(View.INVISIBLE);
        btn_print.setVisibility(View.INVISIBLE);
        btn_print.setBackgroundResource(0);
        txt_declartion.setVisibility(View.VISIBLE);
        RelativeLayout view = (RelativeLayout)findViewById(R.id.activity_billing_bill);

        Bitmap bmp = getBitmapFromView(view);

        PrintHelper photoPrinter = new PrintHelper(Billing_bill.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        photoPrinter.printBitmap("bill_"+billId, bmp);
        btn_edit.setVisibility(View.VISIBLE);
        btn_ok.setVisibility(View.VISIBLE);
        btn_print.setVisibility(View.VISIBLE);
        btn_print.setBackgroundResource(R.drawable.print);
        txt_declartion.setVisibility(View.INVISIBLE);

    }

}
