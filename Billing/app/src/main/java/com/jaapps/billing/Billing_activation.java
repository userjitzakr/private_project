package com.jaapps.billing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Billing_activation extends AppCompatActivity {

    void print(String str){
    //    Log.d("JKS",str);
    }

    String key;
    AlphaAnimation buttonClick;

    String activation_key ;


    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_activation);

        SharedPreferences prefs = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE);
        String restoredText = prefs.getString("activated", null);
        if (restoredText != null) {
            Toast.makeText(this, "Already activated!!!", Toast.LENGTH_SHORT).show();
            finish();
        }

        Typeface font = Typeface.createFromAsset(getAssets(), "Tahoma.ttf");

        TextView txt_label = (TextView)findViewById(R.id.txt_activation_label);
        txt_label.setTypeface(font,Typeface.BOLD);

        buttonClick = new AlphaAnimation(1F, 0.8F);

        activation_key = "11111111";

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1);

        Calendar birthday = Calendar.getInstance();
        birthday.set(Calendar.HOUR_OF_DAY, 0);
        birthday.set(Calendar.MINUTE, 0);
        birthday.set(Calendar.SECOND, 0);
        birthday.set(Calendar.DAY_OF_MONTH,3);
        birthday.set(Calendar.YEAR,1991);
        birthday.set(Calendar.MONTH,Calendar.MAY);

        long keygen = (c.getTimeInMillis()-birthday.getTimeInMillis());
        activation_key = Long.toString(keygen);
        print("ACtivation key = "+activation_key);

        final EditText edit_activation = (EditText)findViewById(R.id.edit_activation_key);
        edit_activation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                print(s.toString());
                if(!(s.toString().equals(""))) {
                    key =(s.toString());
                    print(s.toString());
                    if(key.equals(activation_key))
                    {
                        Toast.makeText(getApplicationContext(),"Congratulations, Billing app is activated succesfully",Toast.LENGTH_LONG).show();

                        mFirebaseInstance = FirebaseDatabase.getInstance();
                        mDatabase = mFirebaseInstance.getReference("Data");
                        Billing_data data = new Billing_data(getApplicationContext());
                        final String userId = data.getUserId();
                        Billing_FirebaseData_activationStatus activationStat = new Billing_FirebaseData_activationStatus("ACTIVATED");

                        mDatabase.child(userId).child("ACTIVATION").child("STATUS").setValue(activationStat);


                        Intent frontPage = new Intent(Billing_activation.this, Billing_FrontPage.class);
                        startActivity(frontPage);
                        finish();
                        SharedPreferences.Editor editor = getSharedPreferences("ACTIVATION_STATUS", MODE_PRIVATE).edit();
                        editor.putString("activated", "true");
                        editor.apply();
                        editor.commit();
                    }
                }
                else
                {

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


        Button btn_edit =(Button)findViewById(R.id.btn_activation_request);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("jaappsdev@gmail.com") +
                        "?subject=" + Uri.encode("Activation Code Request for Billing App") +
                        "&body=" + Uri.encode("Hello,\n\nI am interested in using Billing app for my retail shop. Kindly provide the activation Key.\n\nRegards,\n");
                Uri uri = Uri.parse(uriText);

                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));
            }
        });

    }
}
