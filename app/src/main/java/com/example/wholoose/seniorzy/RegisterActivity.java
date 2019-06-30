package com.example.wholoose.seniorzy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    EditText firstname_edittext, lastname_edittext, password_edittext, email_edittext, edittext;
    Button register_btn;
    private RadioGroup rdb_senior_carer;
    private RadioButton rdb_role;

    final Calendar myCalendar = Calendar.getInstance();
    //Firebase auth
    private FirebaseAuth firebaseAuth;
    //Firebase Realtime Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setup();
        setupDatePicker();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            // Jesli jest juz zalogowany to otwieramy glowne menu apki
            finish();
            startActivity(new Intent(getApplicationContext(), CarerMenuActivity.class));
        }
        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void setupDatePicker() {
        edittext= (EditText) findViewById(R.id.input_callendar);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    public void BackClick(View view){
        Intent intent = new Intent(this,WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void setup(){
        firstname_edittext = (EditText) findViewById(R.id.input_firstname);
        lastname_edittext = (EditText) findViewById(R.id.input_lastname);
        password_edittext = (EditText) findViewById(R.id.input_password);
        email_edittext = (EditText) findViewById(R.id.input_email);
        register_btn = (Button) findViewById(R.id.b_register);
        rdb_senior_carer = (RadioGroup) findViewById(R.id.rdb_senior_carer);


    }

    public void Register(){
        final String firstname_string = firstname_edittext.getText().toString();
        final String lastname_string = lastname_edittext.getText().toString();
        final String email_string = email_edittext.getText().toString();
        final String password_string = password_edittext.getText().toString();

        int selectedId = rdb_senior_carer.getCheckedRadioButtonId();
        rdb_role = (RadioButton) findViewById(selectedId);
        final String role_string = rdb_role.getText().toString();

        if(TextUtils.isEmpty(firstname_string)){
            Toast.makeText(this, "Please enter Firstname", Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(lastname_string)){
            Toast.makeText(this, "Please enter Lastname", Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(email_string)){
            Toast.makeText(this, "Please enter Lastname", Toast.LENGTH_SHORT).show();

            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email_string, password_string)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Firstame").setValue(firstname_string);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Lastname").setValue(lastname_string);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Email").setValue(email_string);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Role").setValue(role_string);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Date of Birth").setValue(myCalendar.get(Calendar.DAY_OF_MONTH) + "-" + myCalendar.get(Calendar.MONTH) + "-" + myCalendar.get(Calendar.YEAR));

                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }

                        if(!task.isSuccessful()){
                            try
                            {
                                throw task.getException();
                            }

                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Toast.makeText(RegisterActivity.this, "Email already used", Toast.LENGTH_SHORT).show();

                            }

                            catch (Exception e)
                            {
                                Toast.makeText(RegisterActivity.this, "Could not register please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}