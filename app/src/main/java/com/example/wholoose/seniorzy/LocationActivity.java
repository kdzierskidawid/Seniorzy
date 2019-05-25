package com.example.wholoose.seniorzy;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class LocationActivity extends AppCompatActivity {

    private TextView longitudeTextView;//dlugosc
    private TextView latitudeTextView;//szergokosc
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final String LOCALIZATION_PERMISSION=Manifest.permission.ACCESS_FINE_LOCATION;
    private final int LOCATION_REQUEST_CODE =99;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if(ContextCompat.checkSelfPermission(this,LOCALIZATION_PERMISSION)!=PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, R.string.localization_no_permission_alert, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    /*
    * Lokalizacja jest updatowana w czasie kiedy telefon się przemieszcza (metoda onLocationChanged).
    * Należy się przemieścić żeby pierwszy raz lokalizacja się pojawiła.
    * Pobranie od razu lokalizacji rzuca nullPointerException.
    * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        longitudeTextView = (TextView) findViewById(R.id.textViewLongitudeValue);
        latitudeTextView = (TextView) findViewById(R.id.textViewLatitudeValue);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            turnOnLocalizationAlert();

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

        if (ActivityCompat.checkSelfPermission(this, LOCALIZATION_PERMISSION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{LOCALIZATION_PERMISSION}, LOCATION_REQUEST_CODE);
            return;
        }

        //Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//generates always null

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void turnOnLocalizationAlert(){
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
                        Toast.makeText(getApplicationContext(),R.string.localization_off_alert,Toast.LENGTH_LONG).show();
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
    }
}
