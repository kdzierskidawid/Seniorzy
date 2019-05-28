package com.example.wholoose.seniorzy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.example.wholoose.seniorzy.adapters.MyPagerAdapter;
import com.example.wholoose.seniorzy.fragments.EmailRegistrationFragment;
import com.example.wholoose.seniorzy.fragments.PhoneRegistrationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
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

    //ViewPager
    private MyPagerAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;


    //Firebase auth
    private FirebaseAuth firebaseAuth;
    //Firebase Realtime Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    //Progressbar
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_viewpager);
        mSectionsPageAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);

        setupViewPager(mViewPager);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            // Jesli jest juz zalogowany to otwieramy glowne menu apki
            finish();
            startActivity(new Intent(getApplicationContext(), LocationActivity.class));
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EmailRegistrationFragment(), "Email registration");
        adapter.addFragment(new PhoneRegistrationFragment(), "Phone registration");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void BackClick(View view){
        Intent intent = new Intent(this,WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void createUserWithPhoneNumber(final String phoneNumber, final String firstname_string, final String lastname_string, final String role_string, final Calendar myCalendar) {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                showInputDialog(verificationId, phoneNumber, firstname_string, lastname_string, role_string,myCalendar);
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    public void createUserWithEmail(final String firstname_string, final String lastname_string, final String email_string, String password_string, final String role_string, final Calendar myCalendar) {
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

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential, final String phoneNumber, final String firstname_string, final String lastname_string, final String role_string, final Calendar myCalendar) {
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

                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            // Sign in failed, display a message and update the UI
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterActivity.this,"Invalid verification code",Toast.LENGTH_SHORT).show();
                            Log.e("PHONE_SIGNIN", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    protected void showInputDialog(final String verificationID, final String phoneNumber, final String firstname_string, final String lastname_string, final String role_string, final Calendar myCalendar) {

        LayoutInflater layoutInflater = LayoutInflater.from(RegisterActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_phone_code, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        final Button btn_confirm = (Button) promptView.findViewById(R.id.btn_confirm);
        btn_confirm.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
        final AlertDialog alert = alertDialogBuilder.create();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,editText.getText().toString());
                signInWithPhoneAuthCredential(credential, phoneNumber, firstname_string, lastname_string, role_string,myCalendar);
                progressBar.setVisibility(View.VISIBLE);
                alert.dismiss();
            }
        });

        alert.show();
    }

}