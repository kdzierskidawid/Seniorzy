package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CarerMenuActivity extends AppCompatActivity {

    Button btnReminders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_menu);

        btnReminders= findViewById(R.id.button3);
        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReminders();
            }
        });
    }

    private void openReminders() {
        Intent intent= new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }


}
