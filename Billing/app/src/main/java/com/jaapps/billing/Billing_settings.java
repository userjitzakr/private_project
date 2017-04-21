package com.jaapps.billing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Billing_settings extends AppCompatActivity {


    AlphaAnimation buttonClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_settings);

        buttonClick = new AlphaAnimation(1F, 0.8F);


        EditText tax = (EditText)findViewById(R.id.edit_settings_tax_percent);

        SharedPreferences prefs = getSharedPreferences("SERVICE_TAX", MODE_PRIVATE);
        String restoredText = prefs.getString("tax", null);
        if (restoredText != null) {
            print("Set tax rate to "+restoredText);
            tax.setText(restoredText);
        }
        else {
            SharedPreferences.Editor editor = getSharedPreferences("SERVICE_TAX", MODE_PRIVATE).edit();
            editor.putString("tax", "15");
            editor.apply();
            editor.commit();
            tax.setText("15");
        }

        Button btn_organisation = (Button)findViewById(R.id.btn_settings_organisation);
        btn_organisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent company = new Intent(Billing_settings.this, Billing_myCompany.class);
                startActivity(company);
            }
        });


        TextView txt_tax_percent_label = (TextView)findViewById(R.id.txt_tax_percent_label);
        TextView txt_settings_percent_label = (TextView)findViewById(R.id.txt_settings_percent_label);
        TextView txtview4 = (TextView)findViewById(R.id.textView4);
        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        txt_settings_percent_label.setTypeface(font);
        txt_tax_percent_label.setTypeface(font);
        txtview4.setTypeface(font);


        Button btn_updateTax = (Button)findViewById(R.id.btn_Settings_update_tax);
        btn_updateTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                EditText tax = (EditText)findViewById(R.id.edit_settings_tax_percent);

                if(tax.getText().toString().equals(""))
                {
                    Toast.makeText(Billing_settings.this, "Please enter tax rate", Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer taxRat = Integer.parseInt(tax.getText().toString());
                if(taxRat < 0 || taxRat > 30)
                {
                    Toast.makeText(Billing_settings.this, "You have entered tax rate more than 30 percent or less than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(Billing_settings.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = getSharedPreferences("SERVICE_TAX", MODE_PRIVATE).edit();
                editor.putString("tax", Integer.toString(taxRat));
                editor.apply();
                editor.commit();
            }
            
        });

        Button btn_backup = (Button)findViewById(R.id.btn_setting_backup);
        Button btn_restore = (Button)findViewById(R.id.btn_setting_restore);
        Button btn_activate = (Button)findViewById(R.id.btn_settings_activate);

        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                takeCurrentDataBackup();
                Toast.makeText(Billing_settings.this, "Successfully Taken Local Backup of data",Toast.LENGTH_LONG).show();
               // print( "DB path = " + dbFile.getAbsolutePath());
            }
        });

        btn_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent restore = new Intent(Billing_settings.this, Billing_restore.class);
                startActivityForResult(restore,202);
            }
        });
        SharedPreferences Aprefs = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE);
        String activated = Aprefs.getString("activated", null);
        if (activated != null) {
            btn_activate.setVisibility(View.GONE);

        }
        btn_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                Intent restore = new Intent(Billing_settings.this, Billing_activation.class);
                startActivityForResult(restore,205);
            }
        });

    }
    void print(String str){
//        Log.d("JKS",str);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 101 && resultCode == RESULT_OK) {

            String Fpath = data.getDataString();
            print("file path =" + Fpath);
            //TODO handle your request here
            super.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == 202 && resultCode == RESULT_OK) {

            String DbFilePath = data.getStringExtra("path");
            File dbFile = new File(DbFilePath);
            try {
                takeCurrentDataBackup();

                FileInputStream fis = new FileInputStream(dbFile);
                File outFile = getDatabasePath("Billing_Db");
                OutputStream output = new FileOutputStream(outFile);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                output.close();
                fis.close();

                File file = new File(getCacheDir()+"/billingBkup");
                String Date_src = DbFilePath.substring((file.toString().length() + 14));
                Date_src = Date_src.substring(0, Date_src.length() - 3);
                SimpleDateFormat sdfSource = new SimpleDateFormat("ddMMyyyy_HHmmss");
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy HH:mm:ss");
                Date date = sdfSource.parse(Date_src);
                String newDate = sdf.format(date);
                String toastData = ("Successfully Restored Data from "+newDate);
                Toast.makeText(Billing_settings.this, toastData ,Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    void takeCurrentDataBackup()
    {
        File dbFile = getDatabasePath("Billing_Db");

        File file = new File(getCacheDir()+"/billingBkup");
        if (!file.exists())
        {
            file.mkdir();
        }
        try {
            FileInputStream fis = new FileInputStream(dbFile);
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
            String currentDateandTime = sdf.format(new Date());

            String outFileName = getCacheDir()+"/billingBkup/billingBkupDB"+currentDateandTime+
                    ".db";

            OutputStream output = new FileOutputStream(outFileName);
            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer))>0){
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            fis.close();


        }
        catch (FileNotFoundException ex) {

ex.printStackTrace();
        }
        catch (IOException ex)
        {
            print( "IO Exception");
        }
    }
}
