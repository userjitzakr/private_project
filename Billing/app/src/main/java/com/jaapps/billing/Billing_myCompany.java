package com.jaapps.billing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class Billing_myCompany extends AppCompatActivity {

    AlphaAnimation buttonClick;

    EditText edit_name;
    EditText edit_address1;
    EditText edit_address2;
    EditText edit_address3;
    EditText edit_phone;
    EditText edit_tin;

    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_my_company);

        buttonClick = new AlphaAnimation(1F, 0.8F);

        TextView txt_name = (TextView)findViewById(R.id.txt_mycompany_name_label);
        TextView txt_addresss = (TextView)findViewById(R.id.txt_mycompany_address_label);
        TextView txt_address3 = (TextView)findViewById(R.id.txt_mycompany_address3);
        TextView txt_address2 = (TextView)findViewById(R.id.txt_mycompany_address2);
        TextView txt_phone = (TextView)findViewById(R.id.txt_mycompnay_phone_label);
        TextView txt_tin = (TextView)findViewById(R.id.txt_mycompany_tin);

        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");
        txt_name.setTypeface(font);
        txt_address2.setTypeface(font);
        txt_address3.setTypeface(font);
        txt_addresss.setTypeface(font);
        txt_phone.setTypeface(font);
        txt_tin.setTypeface(font);

        Button btn_upload_image = (Button)findViewById(R.id.btn_mycomp_uploadLogo);
        Button btn_upload_sign = (Button)findViewById(R.id.btn_myCompany_uploadSign);
        Button btn_cancel = (Button)findViewById(R.id.btn_mycompany_cancel);
        Button btn_update = (Button)findViewById(R.id.btn_mycompany_update);

        edit_name = (EditText)findViewById(R.id.edit_mycompany_name);
        edit_address1 = (EditText)findViewById(R.id.edit_mycompany_address);
        edit_address2 = (EditText)findViewById(R.id.edit_mycompany_address2);
        edit_address3 = (EditText)findViewById(R.id.edit_mycompany_address3);
        edit_phone = (EditText)findViewById(R.id.edit_mycompany_phone);
        edit_tin = (EditText)findViewById(R.id.edit_mycompany_tin);

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Toast.makeText(Billing_myCompany.this, "This feature is not available, wait for update", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select logo"), 101);*/
            }
        });

        btn_upload_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Toast.makeText(Billing_myCompany.this, "This feature is not available, wait for update", Toast.LENGTH_SHORT).show();
            }
        });

        btn_upload_sign.setVisibility(View.GONE);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                String name = edit_name.getText().toString();
                String ad1 = edit_address1.getText().toString();
                String ad2 = edit_address2.getText().toString();
                String ad3 = edit_address3.getText().toString();
                String phone = edit_phone.getText().toString();
                String tin = edit_tin.getText().toString();

                if(name.equals("")|| ad1.equals("")|| tin.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter name, address and tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = getSharedPreferences("MYCOMPANY", MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putString("addressline1",ad1);
                editor.putString("addressline2",ad2);
                editor.putString("addressline3",ad3);
                editor.putString("phone",phone);
                editor.putString("tin",tin);
                editor.apply();
                editor.commit();

                mFirebaseInstance = FirebaseDatabase.getInstance();
                mDatabase = mFirebaseInstance.getReference("Data");
                Billing_data data = new Billing_data(getApplicationContext());
                final String userId = data.getUserId();
                Billing_FirebaseData_company compny = new Billing_FirebaseData_company(name, ad1, ad2, ad3, phone, tin);
                String key = mDatabase.push().getKey();
                mDatabase.child(userId).child("COMPANY").child("DETAILS").setValue(compny);

                Toast.makeText(getApplicationContext(),"Updated company info",Toast.LENGTH_SHORT).show();
                finish();
            }
        });



        SharedPreferences prefs = getSharedPreferences("MYCOMPANY", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String addressline_1 = prefs.getString("addressline1", null);
        String addressline_2 = prefs.getString("addressline2", null);
        String addressline_3 = prefs.getString("addressline3", null);
        String phone = prefs.getString("phone", null);
        String tin = prefs.getString("tin", null);
        if (name != null) {
            edit_name.setText(name);
        }
        if(addressline_1 != null)
        {
            edit_address1.setText(addressline_1);
        }
        if(addressline_2 != null)
        {
            edit_address2.setText(addressline_2);
        }
        if(addressline_3 != null)
        {
            edit_address3.setText(addressline_3);
        }
        if(phone != null)
        {
            edit_phone.setText(phone);
        }
        if(tin != null)
        {
            edit_tin.setText(tin);
        }
    }
    void print(String str)
    {
        Log.d("JKS",str);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            print("Image path is "+uri.toString());

            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();

            print("Picture path is "+picturePath);

            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }
}
