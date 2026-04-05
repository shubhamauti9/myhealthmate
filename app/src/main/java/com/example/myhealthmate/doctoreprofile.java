


package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class doctoreprofile extends AppCompatActivity {



    TextInputLayout fullName,email, regno, phoneNO, password;
    TextView fullNameLabel, UsernameLabel;


    Button update;
    String userId;
    String dcode;



    private Boolean validateName() {
        String val = fullName.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullName.setError("Field cannot be empty");
            return false;

        } else {
            fullName.setError(null);
            return true;
        }
    }

    private Boolean validateregno() {
        String val = regno.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullName.setError("Field cannot be empty");
            return false;

        } else {
            fullName.setError(null);
            return true;
        }
    }


    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = phoneNO.getEditText().getText().toString();

        if (val.isEmpty()) {
            phoneNO.setError("Field cannot be empty");
            return false;

        } else if (val.length() <= 9) {
            phoneNO.setError("Invalid Phone No.");
            return false;
        } else {
            phoneNO.setError(null);
            phoneNO.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
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
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctoreprofile);



        fullName = findViewById(R.id.pData_fullname);
        fullNameLabel = findViewById(R.id.patient_fullnameLable);
        email = findViewById(R.id.pData_email);
        regno = findViewById(R.id.dregno);
        phoneNO = findViewById(R.id.pData_PhoneNo);

        UsernameLabel = findViewById(R.id.patient_usernameLable);
        password = findViewById(R.id.pData_password);

        update = (Button) findViewById(R.id.update);








        SessionManagement sessionManagement = new SessionManagement(doctoreprofile.this);
        userId = sessionManagement.getSession();












        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference().child("doctors").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String patientname = dataSnapshot.child("dName").getValue().toString();
                fullName.getEditText().setText(patientname);
                String patientusername = dataSnapshot.child("dUsername").getValue().toString();

                String patientemail = dataSnapshot.child("dEmail").getValue().toString();
                email.getEditText().setText(patientemail);
                String patientphone = dataSnapshot.child("dPhoneNo").getValue().toString();
                phoneNO.getEditText().setText(patientphone);
                String patientpassword = dataSnapshot.child("dPassword").getValue().toString();
                password.getEditText().setText(patientpassword);

                String docreg = dataSnapshot.child("dRegistrationNo").getValue().toString();
                regno.getEditText().setText(docreg);





                fullNameLabel.setText(patientname);
                UsernameLabel.setText(patientusername);







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                final DatabaseReference reference1 = firebaseDatabase1.getReference().child("doctors");
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if((validateName() && validateregno() && validateEmail() && validatePhoneNo() && validatePassword()) == true) {

                            String Helper_pName = fullName.getEditText().getText().toString();
                            String Helper_pUsername = UsernameLabel.getText().toString();
                            String Helper_pEmail = email.getEditText().getText().toString();
                            String Helper_p_PhoneNo = phoneNO.getEditText().getText().toString();
                            String Helper_dregno = regno.getEditText().getText().toString();
                            String Helper_pPassword= password.getEditText().getText().toString();


                            DoctorHelperClass p_helperClass = new DoctorHelperClass(Helper_pName, Helper_pUsername,
                                    Helper_pEmail, Helper_p_PhoneNo, Helper_dregno, Helper_pPassword);

                            reference1.child(Helper_pUsername).setValue(p_helperClass);

                            Toast.makeText(doctoreprofile.this, "Data Updated", Toast.LENGTH_SHORT).show();


                            DatabaseReference reference2 = firebaseDatabase1.getReference().child("doctors").child(userId);
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String patientname = dataSnapshot.child("dName").getValue().toString();
                                        fullName.getEditText().setText(patientname);
                                        String patientusername = dataSnapshot.child("dUsername").getValue().toString();

                                        String patientemail = dataSnapshot.child("dEmail").getValue().toString();
                                        email.getEditText().setText(patientemail);
                                        String patientphone = dataSnapshot.child("dPhoneNo").getValue().toString();
                                        phoneNO.getEditText().setText(patientphone);
                                        String patientpassword = dataSnapshot.child("dPassword").getValue().toString();
                                        password.getEditText().setText(patientpassword);

                                        String docreg = dataSnapshot.child("dRegistrationNo").getValue().toString();
                                        regno.getEditText().setText(docreg);





                                        fullNameLabel.setText(patientname);
                                        UsernameLabel.setText(patientusername);
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

        });
    }







}


