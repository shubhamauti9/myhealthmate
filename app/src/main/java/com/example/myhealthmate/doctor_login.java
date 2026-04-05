package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class doctor_login extends AppCompatActivity {
    TextInputLayout dUsername, dPassword;
    Button callSignUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_doctor_login);
        dUsername = findViewById(R.id.d_username);
        dPassword = findViewById(R.id.d_password);
        callSignUP = findViewById(R.id.d_signup);
        callSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(doctor_login.this,d_signup.class);
                startActivity(intent);

            }
        });




    }


    private Boolean validateUsername() {
        String val = dUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            dUsername.setError("Field cannot be empty");
            return false;
        } else {
            dUsername.setError(null);
            dUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = dPassword.getEditText().getText().toString();


        if (val.isEmpty()) {
            dPassword.setError("Field cannot be empty");
            return false;
        } else {
            dPassword.setError(null);
            dPassword.setErrorEnabled(false);
            return true;
        }
    }



    public void loginDoctor(View view){

        if(!validateUsername() | !validatePassword()){
            return;
        }
        else{
            isUser();
        }
    }

    private void isUser() {

        final String userEnteredUsername = dUsername.getEditText().getText().toString().trim();
        final String userEnteredPassword = dPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("doctors");

        Query checkUser = reference.orderByChild("dUsername").equalTo(userEnteredUsername);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    dUsername.setError(null);
                    dUsername.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("dPassword").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        dUsername.setError(null);
                        dUsername.setErrorEnabled(false);
                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("dName").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("dUsername").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("dPhoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("dEmail").getValue(String.class);

                        finish();
                        Intent intent = new Intent(getApplicationContext(), doctor_dashboard.class);
                        intent.putExtra("dName", nameFromDB);
                        intent.putExtra("dUsername", usernameFromDB);
                        intent.putExtra("dEmail", emailFromDB);
                        intent.putExtra("dPhoneNo", phoneNoFromDB);
                        intent.putExtra("dPassword", passwordFromDB);
                        SessionManagement sessionManagement = new SessionManagement(doctor_login.this);
                        User user = new User(dUsername.getEditText().getText().toString().trim());
                        sessionManagement.saveSession(user);


                        startActivity(intent);
                    } else {
                        dPassword.setError("Wrong Password");
                        dPassword.requestFocus();
                    }
                } else {
                    dUsername.setError("No such User exist");
                    dUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });

    }





}