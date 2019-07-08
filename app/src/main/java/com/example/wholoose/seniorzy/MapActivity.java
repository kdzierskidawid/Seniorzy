package com.example.wholoose.seniorzy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by User on 10/2/2017.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {



    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    LocationManager locationManager;

    //Firebase
    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;
    FirebaseUser user;

    public double szerokosc, dlugosc;

    private FirebaseAuth mAuth;
    public String currentUser;
    private DatabaseReference UsersRef, FriendsRef;
    public String seniorID, isSenior="", is_user_leader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        if(getIntent().hasExtra("senior_id")) {
            seniorID = getIntent().getExtras().get("senior_id").toString();
            System.out.println("Extra senior klucz: " + seniorID);
        }

        UsersRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                is_user_leader = dataSnapshot.child("Role").getValue().toString();
                Log.e("ERROR", "CHECKING ROLE" );

                if(!is_user_leader.equals("Senior")){
                    isSenior ="NO";
                    System.out.println("Zczytana rola: " + is_user_leader);

                }

                else{
                    isSenior ="YES";
                    System.out.println("Zczytana rola: " + is_user_leader);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getLocationPermission();

        Log.e("ERROR", "HEHE 0" );
        if (firebaseAuth.getCurrentUser() == null) {
           // closing this activity
            finish();
           // starting login activity
             startActivity(new Intent(this, LoginActivity.class));
        } else {
            Toast.makeText(MapActivity.this, "Hello " + user.getEmail(), Toast.LENGTH_LONG).show();
        }

        //

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

//
    }

    /*private void getDeviceLocation(){

    }*/

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(getIntent().hasExtra("senior_id")) {
            seniorID = getIntent().getExtras().get("senior_id").toString();
            System.out.println("Extra senior klucz: " + seniorID);
        }

        UsersRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                is_user_leader = dataSnapshot.child("Role").getValue().toString();
                Log.e("ERROR", "CHECKING ROLE" );

                if(!is_user_leader.equals("Senior")){
                    isSenior ="NO";
                    System.out.println("Zczytana rola w map ready: " + is_user_leader);

                }

                else{
                    isSenior ="YES";
                    System.out.println("Zczytana rola w map ready: " + is_user_leader);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap = googleMap;


        if(isSenior.equals("YES")) {
        if (mLocationPermissionsGranted) {



                Log.d(TAG, "getDeviceLocation: getting the devices current location");
                Log.e("ERROR", "HEHE 2" );


                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                try {
                    if (mLocationPermissionsGranted) {

                        final Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: found location!");
                                    Location currentLocation = (Location) task.getResult();

                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                            DEFAULT_ZOOM);
                                    mDatabase.child("Users").child(user.getUid()).child("Longitude").setValue(currentLocation.getLongitude());
                                    mDatabase.child("Users").child(user.getUid()).child("Latitude").setValue(currentLocation.getLatitude());

                                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, new LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location) {
                                            Log.e("ERROR", "HEHE " );
                                            szerokosc = location.getLatitude();

                                            // pobieram dlugosc
                                            dlugosc = location.getLongitude();
                                            mDatabase.child("Users").child(user.getUid()).child("Latitude").setValue(szerokosc);
                                            mDatabase.child("Users").child(user.getUid()).child("Longitude").setValue(dlugosc);
                                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()),
                                                    DEFAULT_ZOOM);
                                            if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            }
                                            LatLng latt = new LatLng(szerokosc, dlugosc);
                                            mMap.addMarker(new MarkerOptions().position(latt)
                                                    .title("Last Seen"));
                                        }

                                        @Override
                                        public void onStatusChanged(String provider, int status, Bundle extras) {

                                        }

                                        @Override
                                        public void onProviderEnabled(String provider) {

                                        }

                                        @Override
                                        public void onProviderDisabled(String provider) {

                                        }
                                    });

                                    if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this,
                                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }

                                    mMap.setMyLocationEnabled(true);
                                    mMap.getUiSettings().setMyLocationButtonEnabled(false);

                                } else {
                                    Log.d(TAG, "onComplete: current location is null");
                                }
                            }
                        });
                    }
                } catch (SecurityException e) {
                    Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
                }
            }
        }
        else{
            UsersRef.child(seniorID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Double dlugosc_seniora = dataSnapshot.child("Longitude").getValue(Double.class);
                    Double szerokosc_seniora = dataSnapshot.child("Latitude").getValue(Double.class);
                    System.out.println("Zczytana szerokosc: "+szerokosc_seniora + ", zczytana dlugosc: "+dlugosc_seniora);
                    LatLng lokalizacja_seniora = new LatLng(szerokosc_seniora, dlugosc_seniora);
                    System.out.println("Zczytane parametry lokalizacji: " + lokalizacja_seniora);
                    moveCamera(lokalizacja_seniora,
                            DEFAULT_ZOOM);
                    mMap.addMarker(new MarkerOptions().position(lokalizacja_seniora)
                            .title("Last Seen"));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        // wrzucanie na liste moich poprzednich lokacji


    }
}