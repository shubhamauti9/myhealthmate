package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private ImageButton patient,doctor;
    TextView text_t;

    @Override
    protected void onStart(){
        //direct login if previously logged in
        super.onStart();
        checkSession();
    }





    private void checkSession(){





    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);


        doctor = (ImageButton) findViewById(R.id.doctor_profile);
        patient = (ImageButton) findViewById(R.id.patient_profile);
        text_t = (TextView) findViewById(R.id.textView);




        SessionManagement sessionManagement = new SessionManagement(Profile.this);
        final String userId = sessionManagement.getSession();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("patients");
        Query checkUser = reference.orderByChild("pUsername").equalTo(userId);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    moveToDashboard();
                }
                else{




                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("doctors");
                    Query checkdoc = reference1.orderByChild("dUsername").equalTo(userId);
                    checkdoc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                moveTodocDashboard();
                            }
                            else{
                                text_t.setVisibility(View.VISIBLE);
                                doctor.setVisibility(View.VISIBLE);
                                patient.setVisibility(View.VISIBLE);
                            }


                        }

                        private void moveTodocDashboard() {

                            Intent intent = new Intent(Profile.this,doctor_dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError){

                        }

                    });







                }

            }


            private void moveToDashboard() {

                Intent intent = new Intent(Profile.this,patient_dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }


            @Override
            public void onCancelled(DatabaseError databaseError){

            }

        });










        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this, doctor_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this, patient_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



    }
}

