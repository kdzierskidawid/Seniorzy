package com.example.wholoose.seniorzy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarerMenuActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private DatabaseReference UsersRef, FriendsRef;
    private FirebaseAuth mAuth;
    public String currentUser;
    public String seniorID;
    public DataSnapshot datasnap;
    public int a;
    Button btnReminders, btnLocation, /*btnAccount, */ btnGraph, btnFindUsers, btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_menu);
        btnLocation = findViewById(R.id.button);
        btnReminders= findViewById(R.id.button3);
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        seniorID="";
        a=0;
        if(a==0)
        {
            FriendsRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        seniorID = childDataSnapshot.getKey();
                        System.out.println("Uzytkownik "+currentUser + " opiekuje sie uzytkownikiem " + seniorID);
                        a=1;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

/*
        btnAccount = findViewById(R.id.button6);
*/
        btnGraph = findViewById(R.id.button2);
        btnFindUsers = findViewById(R.id.button8);
        btnLogout = findViewById(R.id.button9);
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        FirebaseUser user = firebaseAuth.getCurrentUser();



        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seniorID.isEmpty()){
                    toastMessage("Nie opiekujesz się seniorem");
                }
                else openReminders();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seniorID.isEmpty()){
                    toastMessage("Nie opiekujesz się seniorem");
                }
                else{
                    Intent intent= new Intent(CarerMenuActivity.this, MapActivity.class);
                    startActivity(intent);
                }
            }
        });

       /* btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CarerMenuActivity.this, CallButtonsActivity.class);
                startActivity(intent);
            }
        });*/

        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seniorID.isEmpty()){
                    toastMessage("Nie opiekujesz się seniorem");
                }
                else {
                    Intent profileintent = new Intent(CarerMenuActivity.this, GraphActivity.class);
                    profileintent.putExtra("senior_id", seniorID);
                    startActivity(profileintent);

                }
            }
        });

        btnFindUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(CarerMenuActivity.this, FindAllUsers.class);
                    startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void openReminders() {
        if(seniorID.isEmpty()){
            toastMessage("Nie opiekujesz się seniorem");
        }
        else {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
        }
    }

    public void logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
