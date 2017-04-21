package com.jaapps.billing;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class Billing_Login extends AppCompatActivity {


    void print(String str)
    {
        Log.d("JKS",str);
    }

    private LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_billing__login);
        Profile profile = Profile.getCurrentProfile();

        if(profile != null) {
            Intent activation = new Intent(Billing_Login.this, Billing_FrontPage.class);
            startActivity(activation);
            finish();
        }
        else {

        }
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        loginButton = (LoginButton) findViewById(R.id.connectWithFbButton);
        loginButton.setReadPermissions("email");



        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                print("SUCCESS");

                print("SUCCESS FROM HERE load billing front page.");
               /* while(Profile.getCurrentProfile() == null)
                {
                    print("Cant access profile; waiting");
                }*/

                Intent activation = new Intent(Billing_Login.this, Billing_FrontPage.class);
                activation.putExtra("LOGINSTAT","success");
                startActivity(activation);
                finish();

                // App code
            }

            @Override
            public void onCancel() {
                print("onCANCEL");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                print("onERROR");
                // App code
            }
        });


        }
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("FacebookFragment", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("FacebookFragment", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("FacebookFragment", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(Billing_Login.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        print("ON ACTIVITY RESULT");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
