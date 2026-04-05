package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class connectPatient extends AppCompatActivity {

    Spinner spinner;
    ArrayList<String> namelist;
    String userId, sUsername, CURRENT_STATE;
    String flag = "0";
    ArrayAdapter<String> adapter;


    Button request, check, decline;
    TextView name,email,phoneNo,age;


    DatabaseReference patientRef,friendRequestRef,friendRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_connect_patient);

        SessionManagement sessionManagement = new SessionManagement(connectPatient.this);
        userId = sessionManagement.getSession();



        CURRENT_STATE = "null";


        name = (TextView) findViewById(R.id.PatientName);
        email = (TextView) findViewById(R.id.PatientEmail);
        phoneNo = (TextView) findViewById(R.id.PatientPhoneNo);
        age = (TextView) findViewById(R.id.PatientAge);




        check = (Button) findViewById(R.id.check);
        request = (Button) findViewById(R.id.acceptRequest);
        decline = (Button) findViewById(R.id.declineRequest);

        request.setVisibility(View.INVISIBLE);
        request.setEnabled(false);

        decline.setVisibility(View.INVISIBLE);
        decline.setEnabled(false);



        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends_Status");

        spinner = (Spinner) findViewById(R.id.spinner);
        namelist = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(connectPatient.this, android.R.layout.simple_spinner_dropdown_item, namelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sUsername = "";

                } else {

                    int p = (int) parent.getItemIdAtPosition(position);

                    sUsername = namelist.get(p);
                    flag = "1";
                    Log.d("this", sUsername);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FriendRequest").child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                namelist.clear();
                namelist.add("Select Your Patient's Username");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final String u = snapshot.getKey().toString();
                    Log.d("this",u);
                    DatabaseReference rf1 = FirebaseDatabase.getInstance().getReference("FriendRequest").child(userId).child(u);
                    rf1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String r = dataSnapshot.child("request_status").getValue().toString();

                            Log.d("this",r);
                            Log.d("this",u);

                            if(r.equals("received")){
                                Log.d("this",namelist.toString());
                                namelist.add(u);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AcceptFriendsRequest();


            }
        });


        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeclineFriendsRequest();

            }
        });



            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //final String pUser = sUsername.toString();

                    if(!sUsername.equals("Select Your Patient's Username") && flag == "1" && sUsername != null) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        patientRef = firebaseDatabase.getReference("patients").child(sUsername);

                        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String pName = dataSnapshot.child("pName").getValue().toString();
                                    name.setText(pName);
                                    name.setVisibility(View.VISIBLE);

                                    String pEmail = dataSnapshot.child("pEmail").getValue().toString();
                                    email.setText(pEmail);
                                    email.setVisibility(View.VISIBLE);

                                    String pPhone = dataSnapshot.child("pPhoneNo").getValue().toString();
                                    phoneNo.setText(pPhone);
                                    phoneNo.setVisibility(View.VISIBLE);

                                    String pAge = dataSnapshot.child("pAge").getValue().toString();
                                    int ageyrs = Integer.parseInt(pAge.substring(6));
                                    int year = Calendar.getInstance().get(Calendar.YEAR);
                                    int ageresult = year - ageyrs;
                                    age.setText(String.valueOf(ageresult));
                                    age.setVisibility(View.VISIBLE);

                                    MaintananceofButton();


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        Toast.makeText(connectPatient.this, "Select Patient's Name", Toast.LENGTH_SHORT).show();

                    }


                }
            });







    }

    private void DeclineFriendsRequest() {

        friendRef.child(userId).child(sUsername).child("Connected").setValue("No")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            friendRef.child(sUsername).child(userId).child("Connected").setValue("No")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){


                                                DatabaseReference rfdd = FirebaseDatabase.getInstance().getReference("patients").child(sUsername);
                                                rfdd.child("dcode").setValue(0);





                                                friendRequestRef.child(userId).child(sUsername)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()) {
                                                                    friendRequestRef.child(sUsername).child(userId).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    CURRENT_STATE = "friends";

                                                                                    request.setEnabled(false);
                                                                                    decline.setEnabled(false);
                                                                                    request.setVisibility(View.INVISIBLE);
                                                                                    decline.setVisibility(View.INVISIBLE);

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



    private void AcceptFriendsRequest() {

        friendRef.child(userId).child(sUsername).child("Connected").setValue("Yes")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            friendRef.child(sUsername).child(userId).child("Connected").setValue("Yes")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){


                                                DatabaseReference rfdd = FirebaseDatabase.getInstance().getReference("patients").child(sUsername);
                                                rfdd.child("dcode").setValue(userId);





                                                friendRequestRef.child(userId).child(sUsername).child("request_status").setValue("accepted")
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()) {
                                                                    friendRequestRef.child(sUsername).child(userId).child("request_status").setValue("accepted")
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    CURRENT_STATE = "friends";

                                                                                    request.setEnabled(false);
                                                                                    decline.setEnabled(false);
                                                                                    request.setVisibility(View.INVISIBLE);
                                                                                    decline.setVisibility(View.INVISIBLE);
                                                                                    Toast.makeText(getApplicationContext(),"New Patient Added.", Toast.LENGTH_SHORT).show();

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


    private void CancelFriendRequest() {

        friendRequestRef.child(userId).child(userId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            friendRequestRef.child(userId).child(userId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            //connect.setEnabled(true);
                                            CURRENT_STATE = "not_friends";
                                            //connect.setText("Send Request");

                                        }
                                    });

                        }

                    }
                });


    }

    private void MaintananceofButton(){

        friendRequestRef.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(sUsername)){

                            String request_status = dataSnapshot.child(sUsername).child("request_status").getValue().toString();

                            if(request_status.equals("sent")){

                                CURRENT_STATE = "request_sent";
                                //connect.setText("Cancel Request");

                                request.setVisibility(View.INVISIBLE);
                                request.setEnabled(false);

                            }
                            else if(request_status.equals("received")){


                                CURRENT_STATE="request_received";

                                request.setVisibility(View.VISIBLE);
                                request.setEnabled(true);


                                decline.setVisibility(View.VISIBLE);
                                decline.setEnabled(true);


                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }




    private void SendFriendRequest(){

        friendRequestRef.child(userId).child(userId)
                .child("request_status").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            friendRequestRef.child(userId).child(userId).child("request_status").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            //connect.setEnabled(true);
                                            CURRENT_STATE = "request_sent";
                                            //connect.setText("Cancel Request");

                                        }
                                    });

                        }

                    }
                });
    }








}



