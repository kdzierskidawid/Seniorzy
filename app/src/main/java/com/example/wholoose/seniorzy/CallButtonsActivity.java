package com.example.wholoose.seniorzy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CallButtonsActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private Button button3;
    private static final int CALL_PERMISSION_REQUEST_CODE = 7777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_buttons);

        button1 = findViewById(R.id.buttonCall1);
        button2 = findViewById(R.id.buttonCall2);
        button3 = findViewById(R.id.buttonCall3);
        getPermission();
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Log.e("TAG","CALL1");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:112"));

                if (ActivityCompat.checkSelfPermission(CallButtonsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.e("TAG","CALL2");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:000000"));//tutaj jakiś getNumber opiekuna

                if (ActivityCompat.checkSelfPermission(CallButtonsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        Log.e("TAG","CALL3");
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:000001"));//tutaj jakiś getNumber drugiego opiekuna

                if (ActivityCompat.checkSelfPermission(CallButtonsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
    }

    private void getPermission(){
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(CallButtonsActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CallButtonsActivity.this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(CallButtonsActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        CALL_PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}