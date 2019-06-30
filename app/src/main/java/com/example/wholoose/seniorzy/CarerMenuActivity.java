package com.example.wholoose.seniorzy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CarerMenuActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    Button btnReminders, btnLocation, btnAccount, btnGraph, btnFindUsers, btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_menu);
        btnLocation = findViewById(R.id.button);
        btnReminders= findViewById(R.id.button3);
        btnAccount = findViewById(R.id.button6);
        btnGraph = findViewById(R.id.button2);
        btnFindUsers = findViewById(R.id.button8);
        btnLogout = findViewById(R.id.button9);
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        FirebaseUser user = firebaseAuth.getCurrentUser();



        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReminders();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CarerMenuActivity.this, MapActivity.class);
                startActivity(intent);
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
                Intent intent= new Intent(CarerMenuActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CarerMenuActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        btnFindUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CarerMenuActivity.this, FindAllUsers.class);
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
        Intent intent= new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }

    public void logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }
}
