package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class connectDoctor extends AppCompatActivity {

    Button check,connect;

    DatabaseReference patientRef, docRef,friendRequestRef;


    TextView name,email,resNo,phoneNo,status;
    TextInputLayout username;

    String userId, doctorUserID, CURRENT_STATE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_connect_doctor);



        SessionManagement sessionManagement = new SessionManagement(connectDoctor.this);
        userId = sessionManagement.getSession();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        patientRef = firebaseDatabase.getReference().child("patients").child(userId);
        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //userId = dataSnapshot.child("pName").getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequest");








        check = (Button) findViewById(R.id.checkButton);
        connect = (Button) findViewById(R.id.sendButton);


        username =  findViewById(R.id.docUsername);
        name = (TextView) findViewById(R.id.docName);
        resNo = (TextView) findViewById(R.id.docRes);
        phoneNo = (TextView) findViewById(R.id.docPhoneNo);
        email = (TextView) findViewById(R.id.docEmail);
        status = (TextView) findViewById(R.id.Status);

        CURRENT_STATE = "not_friends";

        connect.setVisibility(View.INVISIBLE);
        connect.setEnabled(false);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connect.setEnabled(false);
                if(CURRENT_STATE.equals("not_friends")){

                    SendFriendRequest();

                }
                if(CURRENT_STATE.equals("request_sent")){

                    CancelFriendRequest();
                }
            }
        });







        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String duser = username.getEditText().getText().toString();


                SessionManagement sessionManagement = new SessionManagement(connectDoctor.this);
                String userId = sessionManagement.getSession();


                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                docRef = firebaseDatabase.getReference().child("doctors");
                final Query checkdoc = docRef.orderByChild("dUsername").equalTo(duser);

                checkdoc.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String dname = dataSnapshot.child(duser).child("dName").getValue().toString();
                            name.setText(dname);
                            name.setVisibility(View.VISIBLE);

                            String demail = dataSnapshot.child(duser).child("dEmail").getValue().toString();
                            email.setText(demail);
                            email.setVisibility(View.VISIBLE);

                            String phone = dataSnapshot.child(duser).child("dPhoneNo").getValue().toString();
                            phoneNo.setText(phone);
                            phoneNo.setVisibility(View.VISIBLE);

                            String dres = dataSnapshot.child(duser).child("dRegistrationNo").getValue().toString();
                            resNo.setText(dres);
                            resNo.setVisibility(View.VISIBLE);

                            doctorUserID = dataSnapshot.child(duser).child("dUsername").getValue().toString();
                            connect.setVisibility(View.VISIBLE);
                            connect.setEnabled(true);
                            MaintananceofButton();


                        } else {

                            name.setVisibility(View.VISIBLE);

                            name.setText("Doctor's Username does not exist");




                            email.setVisibility(View.INVISIBLE);

                            phoneNo.setVisibility(View.INVISIBLE);

                            resNo.setVisibility(View.INVISIBLE);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });








    }

    private void CancelFriendRequest() {

        friendRequestRef.child(userId).child(doctorUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            friendRequestRef.child(doctorUserID).child(userId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            connect.setEnabled(true);
                                            CURRENT_STATE = "not_friends";
                                            connect.setText("Send Request");
                                            Toast.makeText(getApplicationContext(),"Request Cancelled", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }

                    }
                });


    }

    private void MaintananceofButton() {

        friendRequestRef.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(doctorUserID)){

                            String request_status = dataSnapshot.child(doctorUserID).child("request_status").getValue().toString();

                            if(request_status.equals("sent")){

                                CURRENT_STATE = "request_sent";
                                connect.setText("Cancel Request");

                            }
                            else if(request_status.equals("accepted")){

                                CURRENT_STATE = "friends";
                                connect.setVisibility(View.INVISIBLE);
                                connect.setEnabled(false);
                                status.setVisibility(View.VISIBLE);
                                String connect = "Your already connected with the Doctor.";
                                status.setText(connect);


                            }
                            else
                                connect.setText("Send Request");


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void SendFriendRequest() {

        friendRequestRef.child(userId).child(doctorUserID)
                .child("request_status").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            friendRequestRef.child(doctorUserID).child(userId).child("request_status").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            connect.setEnabled(true);
                                            CURRENT_STATE = "request_sent";


                                            connect.setText("Cancel Request");
                                            Toast.makeText(getApplicationContext(),"Request Sent", Toast.LENGTH_SHORT).show();





                                        }
                                    });

                        }

                    }
                });
    }


    @Override
    public void onBackPressed(){
        //Log.d(String.valueOf(getApplicationContext()), "back button");
        //Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(connectDoctor.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }

}

