package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeniorMenuActivity extends AppCompatActivity {

    Button btnReminders, btnLocation, btnCalls, btnGraph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_menu);
        btnLocation = findViewById(R.id.button);
        btnReminders= findViewById(R.id.button3);
        btnCalls = findViewById(R.id.button5);
        btnGraph = findViewById(R.id.button6);

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

    }

    private void openReminders() {
        Intent intent= new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }


}
