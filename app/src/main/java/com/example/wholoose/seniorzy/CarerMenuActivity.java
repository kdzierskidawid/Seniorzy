package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CarerMenuActivity extends AppCompatActivity {

    Button btnReminders, btnLocation, btnAccount, btnGraph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_menu);
        btnLocation = findViewById(R.id.button);
        btnReminders= findViewById(R.id.button3);
        btnAccount = findViewById(R.id.button6);
        btnGraph = findViewById(R.id.button2);

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

    }

    private void openReminders() {
        Intent intent= new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }


}
