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

public class  patient_login extends AppCompatActivity {


    TextInputLayout  pUsername, pPassword;
    Button callSignUP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_login);
        pUsername = findViewById(R.id.p_username);
        pPassword = findViewById(R.id.p_password);
        callSignUP = findViewById(R.id.p_signup);
        callSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(patient_login.this,p_signup.class);
                startActivity(intent);

            }
        });




    }


    private Boolean validateUsername() {
    String val = pUsername.getEditText().getText().toString();
    String noWhiteSpace = "\\A\\w{4,20}\\z";

    if (val.isEmpty()) {
        pUsername.setError("Field cannot be empty");
        return false;
    } else {
        pUsername.setError(null);
        pUsername.setErrorEnabled(false);
        return true;
    }
}

    private Boolean validatePassword() {
        String val = pPassword.getEditText().getText().toString();


        if (val.isEmpty()) {
            pPassword.setError("Field cannot be empty");
            return false;
        } else {
            pPassword.setError(null);
            pPassword.setErrorEnabled(false);
            return true;
        }
    }


    public void loginPatient(View view){

        if(!validateUsername() | !validatePassword()){
            return;
        }
        else{
            isUser();
        }
    }

    private void isUser() {

        final String userEnteredUsername = pUsername.getEditText().getText().toString().trim();
        final String userEnteredPassword = pPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("patients");

        Query checkUser = reference.orderByChild("pUsername").equalTo(userEnteredUsername);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    pUsername.setError(null);
                    pUsername.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("pPassword").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        pUsername.setError(null);
                        pUsername.setErrorEnabled(false);
                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("pName").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("pUsername").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("pPhoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("pEmail").getValue(String.class);
                        String ageFromDB = dataSnapshot.child(userEnteredUsername).child("pAge").getValue(String.class);
                        String heightFromDB = dataSnapshot.child(userEnteredUsername).child("pHeight").getValue(String.class);
                        String weightFromDB = dataSnapshot.child(userEnteredUsername).child("pWeight").getValue(String.class);
                        finish();
                        Intent intent = new Intent(getApplicationContext(), patient_dashboard.class);
                        intent.putExtra("pName", nameFromDB);
                        intent.putExtra("pUsername", usernameFromDB);
                        intent.putExtra("pEmail", emailFromDB);
                        intent.putExtra("pPhoneNo", phoneNoFromDB);
                        intent.putExtra("pPassword", passwordFromDB);
                        intent.putExtra("pAge", ageFromDB);
                        intent.putExtra("pHeight", heightFromDB);
                        intent.putExtra("pWeight", weightFromDB);

                        SessionManagement sessionManagement = new SessionManagement(patient_login.this);
                        User user = new User(pUsername.getEditText().getText().toString().trim());
                        sessionManagement.saveSession(user);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);


                        startActivity(intent);
                    } else {
                        pPassword.setError("Wrong Password");
                        pPassword.requestFocus();
                    }
                } else {
                    pUsername.setError("No such User exist");
                    pUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });

    }

}