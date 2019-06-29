package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeniorMenuActivity extends AppCompatActivity {

    Button btnReminders, btnLocation, btnCalls, btnGraph, btnAccount;
    TextView textView11;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference UserRoleRef;
    public String userName="";
    private String current_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_menu);
        btnLocation = findViewById(R.id.button);
        btnReminders= findViewById(R.id.button3);
        btnCalls = findViewById(R.id.button5);
        btnGraph = findViewById(R.id.button2);
        btnAccount = findViewById(R.id.button6);
        textView11 = findViewById(R.id.textView11);
        firebaseAuth = FirebaseAuth.getInstance();
        UserRoleRef = FirebaseDatabase.getInstance().getReference().child("Users");
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseAuth = FirebaseAuth.getInstance();
        UserRoleRef = FirebaseDatabase.getInstance().getReference().child("Users");
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        if(firebaseAuth.getCurrentUser() != null) {
            UserRoleRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName = dataSnapshot.child("Firstame").getValue().toString();
                    Log.d("Rola Usera Welcome", " Activity: " + userName);
                    textView11.setText(userName);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReminders();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SeniorMenuActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        btnCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SeniorMenuActivity.this, CallButtonsActivity.class);
                startActivity(intent);
            }
        });

        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SeniorMenuActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SeniorMenuActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openReminders() {
        Intent intent= new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }


}
