package com.jaapps.billing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Billing_cutomer extends AppCompatActivity {
    AlphaAnimation buttonClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_cutomer);

        TextView txt_customer_label = (TextView)findViewById(R.id.txt_customer_name_label);
        TextView txt_customer_address_label = (TextView)findViewById(R.id.txt_customer_address_label);
        TextView txt_heading = (TextView)findViewById(R.id.txt_customer_heading_label);

        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");
        txt_customer_label.setTypeface(font);
        txt_customer_address_label.setTypeface(font);
        txt_heading.setTypeface(font,Typeface.BOLD);

        Intent customerInfo = getIntent();

        String name = customerInfo.getStringExtra("name");
        String address = customerInfo.getStringExtra("address");

        //Button btn_skip = (Button)findViewById(R.id.btn_customer_skip);
        Button btn_add = (Button)findViewById(R.id.btn_customer_add);

        EditText edit_name = (EditText)findViewById(R.id.edit_customer_name);
        EditText edit_address = (EditText)findViewById(R.id.edit_customer_address);
        edit_address.setTextColor(Color.BLACK);
        edit_name.setTextColor(Color.BLACK);

        edit_address.setText(address);
        edit_name.setText(name);


        buttonClick = new AlphaAnimation(1F, 0.8F);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                EditText edit_name = (EditText)findViewById(R.id.edit_customer_name);
                EditText edit_address = (EditText)findViewById(R.id.edit_customer_address);

                if(edit_address.getText().toString().equals("") || edit_name.getText().toString().equals(""))
                {
                    Toast.makeText(Billing_cutomer.this, "Enter customer name and address", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent();
                // TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("name", edit_name.getText().toString());
                resultIntent.putExtra("address", edit_address.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
/*
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                finish();
            }
        });*/



    }
}
