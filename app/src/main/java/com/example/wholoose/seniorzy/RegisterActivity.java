package com.example.wholoose.seniorzy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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
            startActivity(new Intent(getApplicationContext(), LocationActivity.class));
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
            Toast.makeText(this, "Please enter email or phone", Toast.LENGTH_SHORT).show();
            return;
        }

        if(email_string.contains("@"))createUserWithEmail(firstname_string, lastname_string, email_string, password_string, role_string);
        else {
                createUserWithPhoneNumber(email_string, firstname_string, lastname_string, role_string);
        }


    }

    private void createUserWithPhoneNumber(final String phoneNumber, final String firstname_string, final String lastname_string, final String role_string) {
        Log.e("USER_CREATE", "start");
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("PHONE_VERIFICATION", "onVerificationCompleted:" + credential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("PHONE_VERIFICATION", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("PHONE_VERIFICATION", "onCodeSent:" + verificationId);
                showInputDialog(verificationId, phoneNumber, firstname_string, lastname_string, role_string);
                // Save verification ID and resending token so we can use them later
                //mVerificationId = verificationId;
                //mResendToken = token;

                // ...
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void createUserWithEmail(final String firstname_string, final String lastname_string, final String email_string, String password_string, final String role_string) {
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
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential, final String phoneNumber, final String firstname_string, final String lastname_string, final String role_string) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PHONE_SIGNIN", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Firstame").setValue(firstname_string);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Lastname").setValue(lastname_string);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("PhoneNumber").setValue(phoneNumber);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Role").setValue(role_string);
                            myRef.child(firebaseAuth.getCurrentUser().getUid()).child("Date of Birth").setValue(myCalendar.get(Calendar.DAY_OF_MONTH) + "-" + myCalendar.get(Calendar.MONTH) + "-" + myCalendar.get(Calendar.YEAR));


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("PHONE_SIGNIN", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    protected void showInputDialog(final String verificationID, final String phoneNumber, final String firstname_string, final String lastname_string, final String role_string) {

        LayoutInflater layoutInflater = LayoutInflater.from(RegisterActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_phone_code, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        final Button btn_confirm = (Button) promptView.findViewById(R.id.btn_confirm);
        btn_confirm.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!editText.getText().toString().isEmpty()){
                    btn_confirm.setEnabled(true);
                }else{
                    btn_confirm.setEnabled(false);
                }
            }
        });

        alertDialogBuilder.setCancelable(false);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,editText.getText().toString());
                signInWithPhoneAuthCredential(credential, phoneNumber, firstname_string, lastname_string, role_string);
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}