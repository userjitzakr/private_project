package com.jaapps.billing;

import android.content.Intent;
import android.database.Cursor;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Billing_addItem extends AppCompatActivity {

    AlphaAnimation buttonClick;
    boolean editFlag = false;
    String itemId = "";


    void print(String str){Log.d("JKS",str);}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_add_item);

        buttonClick = new AlphaAnimation(1F, 0.8F);

        Intent argument = getIntent();
        String edit = argument.getStringExtra("edit");
        if(edit.equals("true"))
        {
            editFlag = true;
            itemId = argument.getStringExtra("id");

        }
        else
            editFlag = false;

        TextView txt_item = (TextView)findViewById(R.id.txt_additem_itemName_label);
        TextView txt_descr = (TextView)findViewById(R.id.txt_additem_item_description);

        final Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        txt_item.setTypeface(font);
        txt_descr.setTypeface(font);

        EditText edit_itemName = (EditText)findViewById(R.id.edit_additem_itemname);
        EditText edit_description = (EditText)findViewById(R.id.edit_additem_itemdescription);
        edit_description.setTextColor(getResources().getColor(R.color.bgColor));
        edit_itemName.setTextColor(getResources().getColor(R.color.bgColor));

        if(editFlag)
        {
            Billing_data data = new Billing_data(this);
            data.openConnection();
            Cursor itemData = data.getItem(itemId);

            while (itemData.moveToNext())
            {

                edit_itemName.setText(itemData.getString(0).toString());
                edit_description.setText(itemData.getString(1).toString());
            }
        }

        Button btn_save = (Button)findViewById(R.id.btn_additem_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                EditText edit_itemName = (EditText)findViewById(R.id.edit_additem_itemname);
                EditText edit_description = (EditText)findViewById(R.id.edit_additem_itemdescription);

                String itemName = edit_itemName.getText().toString();
                String description = edit_description.getText().toString();
                if(itemName.equals("")|| description.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter item name and description",Toast.LENGTH_SHORT).show();
                    return;
                }
                Billing_data data = new Billing_data(getApplicationContext());
                data.openConnection();
                if(editFlag)
                {
                    data.updateItem(itemName, description, itemId);
                }
                else {
                    data.addItem(itemName, description);
                }
                data.closeConnection();


                Toast.makeText(getApplicationContext(),"Item added",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
