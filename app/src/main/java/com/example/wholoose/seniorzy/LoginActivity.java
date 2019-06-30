package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email_or_number_edittext, password_edittext;
    Button login_btn;

    public String userRole;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference UserRoleRef;

    private String current_user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();
        UserRoleRef = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            logout();
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    public void BackClick(View view){
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

}

    public void login(){
        String email_string = email_or_number_edittext.getText().toString();
        String password_string  = password_edittext.getText().toString();

        if(TextUtils.isEmpty(email_string)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        else if(TextUtils.isEmpty(password_string)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        else {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email_string, password_string)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkIfLoginPossible();
                                /*if(userRole.equals("Senior")){
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), SeniorMenuActivity.class));
                                    Toast.makeText(LoginActivity.this,"Welcome Senior!",Toast.LENGTH_LONG).show();
                                }

                                if(userRole.equals("Carer")){
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), CarerMenuActivity.class));
                                    Toast.makeText(LoginActivity.this,"Welcome Carer!",Toast.LENGTH_LONG).show();
                                }*/

                            }
                            if (!task.isSuccessful())
                            {
                                try
                                {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    Toast.makeText(LoginActivity.this,"That email does not exist",Toast.LENGTH_LONG).show();
                                    return;
                                }
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                {
                                    Toast.makeText(LoginActivity.this,"You used wrong password",Toast.LENGTH_LONG).show();
                                    return;
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(LoginActivity.this,"Could not log in",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }
                    });
        }
    }

    public void setup(){
        email_or_number_edittext = (EditText) findViewById(R.id.input_email_phone);
        password_edittext = (EditText) findViewById(R.id.input_password);
        login_btn = (Button) findViewById(R.id.b_login);
    }

    public void checkIfLoginPossible(){
        current_user_id = firebaseAuth.getCurrentUser().getUid();
        if(firebaseAuth.getCurrentUser() != null) {
            UserRoleRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userRole = dataSnapshot.child("Role").getValue(String.class);
                    Log.d("Rola Usera: ", ": " + userRole);

                    if(userRole.equals("Senior")){
                      finish();
                      startActivity(new Intent(getApplicationContext(), SeniorMenuActivity.class));
                        Toast.makeText(LoginActivity.this,"Welcome Senior!",Toast.LENGTH_LONG).show();
                    }

                    if(userRole.equals("Carer")){
                        finish();
                        startActivity(new Intent(getApplicationContext(), CarerMenuActivity.class));
                        Toast.makeText(LoginActivity.this,"Welcome Carer!",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });


        }
    }
    public void logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}