package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class d_signup extends AppCompatActivity {

    //variables
    TextInputLayout dName, dUsername, dEmail, dRegistrationNo ,dPhoneNo, dPassword;
    Button dSignUp, dBack ;

    FirebaseDatabase rootNode,rootNode1;
    DatabaseReference reference,reference1;



    private Boolean validateName() {
        String val = dName.getEditText().getText().toString();

        if (val.isEmpty()) {
            dName.setError("Field cannot be empty");
            return false;

        } else {
            dName.setError(null);
            return true;
        }
    }

    private Boolean validatereg() {
        String val = dRegistrationNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            dName.setError("Field cannot be empty");
            return false;

        } else {
            dName.setError(null);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = dUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            dUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            dUsername.setError("Username too long");
            return false;
        } else if (val.length() <= 3) {
            dUsername.setError("Username too short");
            return false;

        } else if (!val.matches(noWhiteSpace)) {
            dUsername.setError("White Spaces are not allowed");
            return false;
        } else {
            dUsername.setError(null);
            dUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = dEmail.getEditText().getText().toString();
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        if (val.isEmpty()) {
            dEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            dEmail.setError("Invalid email address");
            return false;
        } else {
            dEmail.setError(null);
            dEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = dPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            dPhoneNo.setError("Field cannot be empty");
            return false;

        } else if (val.length() <= 9) {
            dPhoneNo.setError("Invalid Phone No.");
            return false;
        } else {
            dPhoneNo.setError(null);
            dPhoneNo.setErrorEnabled(false);
            return true;
        }
    }



    private Boolean validatePassword() {
        String val = dPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            dPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            dPassword.setError("Password is too weak");
            return false;
        } else {
            dPassword.setError(null);
            dPassword.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_d_signup);

        dName = findViewById(R.id.d_name);
        dUsername = findViewById(R.id.d_username);
        dEmail = findViewById(R.id.d_email);
        dRegistrationNo = findViewById(R.id.d_registrationNo);
        dPhoneNo = findViewById(R.id.d_phoneNo);
        dPassword = findViewById(R.id.d_password);
        dSignUp = findViewById(R.id.d_final_signUp);
        dBack = findViewById(R.id.d_backToLogin);

        dSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("doctors");

                rootNode1 = FirebaseDatabase.getInstance();
                reference1 = rootNode1.getReference("patients");


                if ((validateName() && validateUsername() && validateEmail() && validatereg() && validatePhoneNo() && validatePassword()) == true) {


                    final String userEnteredUsername = dUsername.getEditText().getText().toString().trim();
                    final Query checkUser = reference.orderByChild("dUsername").equalTo(userEnteredUsername);
                    final Query checkpat = reference1.orderByChild("pUsername").equalTo(userEnteredUsername);

                    checkpat.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                dUsername.setError("Username Already Exist");

                            }
                            else{
                                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            dUsername.setError("Username Already Exist");
                                        } else {

                                            //Get all the values
                                            String Helper_dName = dName.getEditText().getText().toString();
                                            String Helper_dUsername = dUsername.getEditText().getText().toString();
                                            String Helper_dEmail = dEmail.getEditText().getText().toString();
                                            String Helper_dPhoneNo = dPhoneNo.getEditText().getText().toString();
                                            String Helper_dRegistrationNo = dRegistrationNo.getEditText().getText().toString();
                                            String Helper_dPassword = dPassword.getEditText().getText().toString();

                                            DoctorHelperClass d_helperClass = new DoctorHelperClass(Helper_dName,Helper_dUsername,
                                                    Helper_dEmail,Helper_dPhoneNo,Helper_dRegistrationNo,Helper_dPassword);

                                            reference.child(Helper_dUsername).setValue(d_helperClass);

                                            Toast.makeText(d_signup.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();


                                            Intent intent = new Intent(d_signup.this, doctor_login.class);
                                            startActivity(intent);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }




            }
        });



    }
}