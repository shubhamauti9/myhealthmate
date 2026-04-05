package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class doctor_dashboard extends AppCompatActivity {

    ImageView DoctorLogout;
    LinearLayout connectPatient, pdreport, sappt;
    String userId;
    TextView dname, duname;
    Button eprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_dashboard);
        SessionManagement sessionManagement = new SessionManagement(doctor_dashboard.this);
        userId = sessionManagement.getSession();


        dname = (TextView) findViewById(R.id.dname_text);
        duname = (TextView) findViewById(R.id.duname_text);

        eprofile = (Button) findViewById(R.id.profile_button);

        pdreport = (LinearLayout) findViewById(R.id.pdreport);


        DatabaseReference refd = FirebaseDatabase.getInstance().getReference("doctors").child(userId);
        refd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child("dName").getValue().toString();
                dname.setText(n);
                String u = dataSnapshot.child("dUsername").getValue().toString();
                duname.setText(u);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        eprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor_dashboard.this, doctoreprofile.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        pdreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor_dashboard.this, doctor_s_preport.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        connectPatient = (LinearLayout) findViewById(R.id.connectPatient);
        connectPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(doctor_dashboard.this, connectPatient.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        sappt = (LinearLayout) findViewById(R.id.sappt);
        sappt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(doctor_dashboard.this, scheduleappt.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        DoctorLogout = (ImageView) findViewById(R.id.doclogout);
        DoctorLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement sessionManagement = new SessionManagement(doctor_dashboard.this);
                sessionManagement.removeSession();

                moveToLogin();
            }
        });

    }
    private String getUserId() {
        return userId;
    }
    private void moveToLogin() {

        Intent intent = new Intent(doctor_dashboard.this,Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}