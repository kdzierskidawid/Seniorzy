package com.example.wholoose.seniorzy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.wholoose.seniorzy.R;

import java.text.DecimalFormat;
/*
* Do zrobienia:
* automatyczne pytanie o wlaczanie lokalizacji-obsluzyc jak kliknie NIE
* dodanie last known location
* */

public class LocationActivity extends AppCompatActivity {

    private TextView longitudeTextView;//dlugosc
    private TextView latitudeTextView;//szergokosc

    private LocationManager locationManager;
    private LocationListener locationListener;
    private final String LOCALIZATION_PERM=Manifest.permission.ACCESS_FINE_LOCATION;



    /*
    * Lokalizacja jest updatowana w czasie kiedy telefon się przemieszcza-metoda onLocationChanged
    * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        longitudeTextView = (TextView) findViewById(R.id.textViewLongitudeValue);
        latitudeTextView = (TextView) findViewById(R.id.textViewLatitudeValue);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            messageAlertNoGPSFound();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DecimalFormat format=new DecimalFormat("#.####");
                longitudeTextView.setText(String.valueOf(format.format(location.getLongitude())));
                latitudeTextView.setText(String.valueOf(format.format(location.getLatitude())));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        //TODO
        if (ActivityCompat.checkSelfPermission(this, LOCALIZATION_PERM)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            ActivityCompat.requestPermissions(this,new String[]{LOCALIZATION_PERM},1);
            //TODO
            // to handle the case where the user grants the permission.
            return;
        }
        //last known location, nie dziala, bo lastKnowLocalisation jest nullem, dobrze na poczatku wyswietlac last know location
        //przed "on location changed"
        String locationProvider= LocationManager.GPS_PROVIDER;
        Location lastKnownLocation;
        while (true){
            lastKnownLocation=locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation==null){
                continue;
            }
            else {
                break;
            }
        }
        longitudeTextView.setText(String.valueOf(lastKnownLocation.getLongitude()));
        latitudeTextView.setText(String.valueOf(lastKnownLocation.getLatitude()));


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void messageAlertNoGPSFound(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Lokalizacja jest wyłączona. Czy chcesz ją włączyć aby lokalizować seniora?")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( final DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
    }
}
