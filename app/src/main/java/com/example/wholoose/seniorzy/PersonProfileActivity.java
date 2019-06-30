package com.example.wholoose.seniorzy;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/*import com.instabug.library.view.CircularImageView;
import com.squareup.picasso.Picasso;*/

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PersonProfileActivity extends AppCompatActivity {

    private TextView user_email, user_name;
    private Button btn_send_invite, btn_decline_invite, btn_accept_if_not_leader;

    private DatabaseReference FriendRequestRef, UsersRef, FriendsRef, ImageRef,isLeaderRef;
    private FirebaseAuth mAuth;
    private String senderUserId, receiverUserId, saveCurrentDate, CAN_BE_LEADER;
    public String is_user_leader, currentUser;
    public static String CURRENT_STATE;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    public String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        receiverUserId = intent.getStringExtra("visit_user_id");
        // nasze aktualne id
        senderUserId = mAuth.getCurrentUser().getUid();
        //czyjes id, po kliknieciu na profil to bedzie id tej osoby, nie nasze
        //receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
/*
        ImageRef = FirebaseDatabase.getInstance().getReference().child("uploads");
*/
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        InitFields();
/*
        get_person_image();
*/


        UsersRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String user_email_string = dataSnapshot.child("Email").getValue().toString();
                    String user_name_string = dataSnapshot.child("Firstame").getValue().toString();

                    user_name.setText("Name: "+user_name_string);
                    user_email.setText("Email: "+ user_email_string);

                    MaintanceofButtons();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        if(!senderUserId.equals(receiverUserId)){
            btn_send_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_send_invite.setEnabled(false);

                    if(CURRENT_STATE.equals("not_friends")) {
                            SendFriendRequestToaPerson();
                    }

                    if(CURRENT_STATE.equals("request_sent")){
                        CancelFriendrequest();
                    }

                    if(CURRENT_STATE.equals("request_received")){
                        AcceptFriendRequest();
                    }

                    if(CURRENT_STATE.equals("friends")){
/*
                        getFriendLocation();
*/
                        UnFriendAnExistingFriend();
                    }
                }
            });
        }
        else{
            btn_decline_invite.setVisibility(View.INVISIBLE);
            btn_send_invite.setVisibility(View.INVISIBLE);
        }

    }

    private void UnFriendAnExistingFriend() {
        FriendsRef.child(senderUserId).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendsRef.child(receiverUserId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                btn_send_invite.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                btn_send_invite.setText("send group request");

                                                btn_decline_invite.setVisibility(View.INVISIBLE);
                                                btn_decline_invite.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void AcceptFriendRequest() {

        FriendsRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
/*
        getFriendLocation();
*/
        FriendsRef.child(senderUserId).child(receiverUserId).child(senderUserId).setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendsRef.child(receiverUserId).child(senderUserId).child("date").setValue(saveCurrentDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FriendRequestRef.child(senderUserId).child(receiverUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    FriendRequestRef.child(receiverUserId).child(senderUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
                                                                                        btn_send_invite.setEnabled(true);
                                                                                        CURRENT_STATE = "friends";
                                                                                        btn_send_invite.setText("Unfriend this person");
                                                                                        btn_decline_invite.setVisibility(View.INVISIBLE);
                                                                                        btn_decline_invite.setEnabled(false);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void CancelFriendrequest() {
        FriendRequestRef.child(senderUserId).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(receiverUserId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                btn_send_invite.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                btn_send_invite.setText("send group request");

                                                btn_decline_invite.setVisibility(View.INVISIBLE);
                                                btn_decline_invite.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void MaintanceofButtons() {
        FriendRequestRef.child(senderUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if child exists
                        if(dataSnapshot.hasChild(receiverUserId)){
                            String request_type = dataSnapshot.child(receiverUserId)
                                    .child("request_type").getValue().toString();

                            if(request_type.equals("sent")){
                                CURRENT_STATE = "request_sent";
                                btn_send_invite.setText("Cancel friend request");

                                btn_decline_invite.setVisibility(View.INVISIBLE);
                                btn_decline_invite.setEnabled(false);
                            }
                            else if (request_type.equals("received")){
                                CURRENT_STATE = "request_received";
                                btn_send_invite.setText("Accept group invite");

                                btn_decline_invite.setVisibility(View.VISIBLE);
                                btn_decline_invite.setEnabled(true);
                            }
                        }
                        else{
                            FriendsRef.child(senderUserId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(receiverUserId)){
                                                CURRENT_STATE = "friends";
                                                btn_send_invite.setText("Unfriend this person");

                                                btn_decline_invite.setVisibility(View.INVISIBLE);
                                                btn_decline_invite.setEnabled(false);

                                                btn_decline_invite.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        CancelFriendrequest();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void SendFriendRequestToaPerson() {
        FriendRequestRef.child(senderUserId).child(receiverUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FriendRequestRef.child(receiverUserId).child(senderUserId)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                btn_send_invite.setEnabled(true);
                                                CURRENT_STATE = "request_sent";
                                                btn_send_invite.setText("Cancel friend request");

                                                btn_decline_invite.setVisibility(View.INVISIBLE);
                                                btn_decline_invite.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    public void InitFields(){
        user_email = (TextView) findViewById(R.id.person_email);
        user_name = (TextView) findViewById(R.id.person_name);
        btn_send_invite = (Button) findViewById(R.id.btnSendInvite);
        btn_decline_invite = (Button) findViewById(R.id.btnDeclineInvite);

        CURRENT_STATE = "not_friends";
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    /*private void getFriendLocation(){
        UsersRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                *//*String szerokosc = dataSnapshot.child("actual_position").child("latitude").getValue().toString();
                String dlugosc = dataSnapshot.child("actual_position").child("longitude").getValue().toString();
                Log.d("Dlugosc przyjaciela: ", "" + dlugosc);
                Log.d("Szerokosc przyjaciela: ", "" + szerokosc);
*//*
                Double szerokosc_firebase = (dataSnapshot.child("latitude").getValue(Double.class));
                Double dlugosc_firebase = (dataSnapshot.child("longitude").getValue(Double.class));

                Log.d("Dlugosc kolegi: " + receiverUserId, "" + dlugosc_firebase);
                Log.d("Szerokosc kolegi: ", "" + szerokosc_firebase);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

   /* private void getlocationofafriend(){
        FriendRequestRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                *//*String szerokosc = dataSnapshot.child("actual_position").child("latitude").getValue().toString();
                String dlugosc = dataSnapshot.child("actual_position").child("longitude").getValue().toString();
                Log.d("Dlugosc przyjaciela: ", "" + dlugosc);
                Log.d("Szerokosc przyjaciela: ", "" + szerokosc);
*//*
                Double szerokosc__kolegi_firebase = (dataSnapshot.child("latitude").getValue(Double.class));
                Double dlugosc__kolegi_firebase = (dataSnapshot.child("longitude").getValue(Double.class));

                Log.d("Dlugosc kolegi: " + receiverUserId, "" + dlugosc__kolegi_firebase);
                Log.d("Szerokosc kolegi: ", "" + szerokosc__kolegi_firebase);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/

    /*private void get_person_image(){
        ImageRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                *//*String szerokosc = dataSnapshot.child("actual_position").child("latitude").getValue().toString();
                String dlugosc = dataSnapshot.child("actual_position").child("longitude").getValue().toString();
                Log.d("Dlugosc przyjaciela: ", "" + dlugosc);
                Log.d("Szerokosc przyjaciela: ", "" + szerokosc);
*//*
                if(dataSnapshot.exists()) {
                    //Log.d("asd", "asd: " + dataSnapshot.child("imageUrl").getValue());
                    String image = dataSnapshot.child("imageUrl").getValue(String.class);
                }

                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

}