package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "AccountActivity";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private ListView mListView;
    private String current_user_id;
    private String firstname, lastname, role;
    public  ArrayList<String> array  = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference UserRoleRef;
    public String userRole="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mListView = (ListView) findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        firebaseAuth = FirebaseAuth.getInstance();
        UserRoleRef = FirebaseDatabase.getInstance().getReference().child("Users");
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Account informations");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };

        int i = 1;

        if(firebaseAuth.getCurrentUser() != null) {
            UserRoleRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userRole = dataSnapshot.child("Role").getValue().toString();
                    Log.d("Rola Usera Welcome", " Activity: " + userRole);
                    array.add(userID);

                    role = dataSnapshot.child("Role").getValue(String.class);
                    firstname = dataSnapshot.child("Firstame").getValue(String.class);
                    lastname = dataSnapshot.child("Lastname").getValue(String.class);
                    Log.d("Rola Usera Welcome", " Activity: " + role);
                    Log.d("Rola Usera Welcome", " Activity: " + firstname);
                    Log.d("Rola Usera Welcome", " Activity: " + lastname);
                    array.add(role);
                    array.add(firstname);
                    array.add(lastname);


                    ArrayAdapter adapter = new ArrayAdapter(AccountActivity.this,android.R.layout.simple_list_item_1,array);
                    mListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}