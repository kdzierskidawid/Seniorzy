package com.example.wholoose.seniorzy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference UserRoleRef;
    public String userRole="", userName="";
    private String current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        UserRoleRef = FirebaseDatabase.getInstance().getReference().child("Users");

        if(firebaseAuth.getCurrentUser() != null) {
            current_user_id = firebaseAuth.getCurrentUser().getUid();
            UserRoleRef.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userRole = dataSnapshot.child("Role").getValue().toString();
                    Log.d("Rola Usera Welcome", " Activity: " + userRole);
                    userName = dataSnapshot.child("Firstame").getValue(String.class);
                    Log.d("", "" + userRole);
                    if(userRole.equals(" Senior")){
                                      /*  System.out.println("3. Rolka konta to: " +userRole);
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), SeniorMenuActivity.class));*/
                        Toast.makeText(WelcomeActivity.this,"Welcome Senior!",Toast.LENGTH_LONG).show();
                        System.out.println("Rolka konta to: " +userRole);
                    }

                    if(userRole.equals(" Carer")){
                        System.out.println("4. Rolka konta to: " +userRole);
                                       /* finish();
                                        startActivity(new Intent(getApplicationContext(), CarerMenuActivity.class));*/
                        Toast.makeText(WelcomeActivity.this,"Welcome Carer!",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });


        }
        }

    public void LoginClick(View v){
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
    public void RegisterClick(View v){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}