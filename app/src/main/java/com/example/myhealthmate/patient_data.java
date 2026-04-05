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

public class patient_data extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {



    TextInputLayout fullName,email, phoneNO,username,password,height,weight;
    TextView fullNameLabel, UsernameLabel,heightLabel,WeightLabel,ageLabel,age,text_2;
    RadioGroup Gender;
    RadioButton male, female;
    String gender = "-1";
    DatePickerDialog.OnDateSetListener setListener;
    Button update;
    String userId;
    String dcode;
    ImageView gendericon;


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
        setContentView(R.layout.activity_patient_data);



        fullName = findViewById(R.id.pData_fullname);
        fullNameLabel = findViewById(R.id.patient_fullnameLable);
        email = findViewById(R.id.pData_email);
        phoneNO = findViewById(R.id.pData_PhoneNo);
        //username = findViewById(R.id.pData_username);
        UsernameLabel = findViewById(R.id.patient_usernameLable);
        password = findViewById(R.id.pData_password);
        age = (TextView) findViewById(R.id.pData_age);
        age = findViewById(R.id.pData_age);
        ageLabel = findViewById(R.id.age_no);
        //height = findViewById(R.id.pData_height);
        heightLabel = findViewById(R.id.height_no);
        //weight = findViewById(R.id.pData_weight);
        //WeightLabel = findViewById(R.id.weight_no);
        Gender = findViewById(R.id.pData_gender);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        text_2 = (TextView) findViewById(R.id.text_2);
        update = (Button) findViewById(R.id.update);
        text_2.setVisibility(View.VISIBLE);
        gendericon = (ImageView) findViewById(R.id.gendericon);








        SessionManagement sessionManagement = new SessionManagement(patient_data.this);
        userId = sessionManagement.getSession();

        Gender.setOnCheckedChangeListener(this);



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day  = calendar.get(Calendar.DAY_OF_MONTH);


        age.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        patient_data.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String m = String.valueOf(month);
                String d = String.valueOf(dayOfMonth);
                if(m.length() < 2){
                    m = "0" + m;
                }
                if(d.length() < 2){
                    d = "0" + d;
                }


                String date = d + "/" + m + "/" + String.valueOf(year);
                age.setText(date);
                Log.d("this",date);

            }
        };




        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference().child("patients").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String patientname = dataSnapshot.child("pName").getValue().toString();
                fullName.getEditText().setText(patientname);
                String patientusername = dataSnapshot.child("pUsername").getValue().toString();
                //username.getEditText().setText(patientusername);
                String patientemail = dataSnapshot.child("pEmail").getValue().toString();
                email.getEditText().setText(patientemail);
                String patientphone = dataSnapshot.child("pPhoneNo").getValue().toString();
                phoneNO.getEditText().setText(patientphone);
                String patientpassword = dataSnapshot.child("pPassword").getValue().toString();
                password.getEditText().setText(patientpassword);
                String patientage = dataSnapshot.child("pAge").getValue().toString();
                age.setText(patientage);
                String patientgender = dataSnapshot.child("gender").getValue().toString();
                dcode = dataSnapshot.child("dcode").getValue().toString();
                //String patientheight = dataSnapshot.child("pHeight").getValue().toString();
                //height.getEditText().setText(patientheight);
                //String patientweight = dataSnapshot.child("pWeight").getValue().toString();
                //weight.getEditText().setText(patientweight);


                int ageyrs = Integer.parseInt(patientage.substring(6));
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int ageresult = year - ageyrs;


                fullNameLabel.setText(patientname);
                UsernameLabel.setText(patientusername);
                heightLabel.setText(patientgender);
                //WeightLabel.setText("70");
                ageLabel.setText(String.valueOf(ageresult));

                if (patientgender.equals("Male")) {
                    male.setChecked(true);
                    gendericon.setBackgroundResource(R.drawable.malegender);
                }
                else{
                    female.setChecked(true);
                    gendericon.setBackgroundResource(R.drawable.womangender);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                final DatabaseReference reference1 = firebaseDatabase1.getReference().child("patients");
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if((validateName() && validateEmail() && validatePhoneNo() && validatePassword()) == true) {

                            String Helper_pName = fullName.getEditText().getText().toString();
                            String Helper_pUsername = UsernameLabel.getText().toString();
                            String Helper_pEmail = email.getEditText().getText().toString();
                            String Helper_p_PhoneNo = phoneNO.getEditText().getText().toString();

                            String Helper_pAge = age.getText().toString();

                            //String Helper_pHeight = pHeight.getEditText().getText().toString();
                            //String Helper_pWeight = pWeight.getEditText().getText().toString();
                            String Helper_pPassword = password.getEditText().getText().toString();
                            String Helper_pGender = gender;

                            PatientHelperClass p_helperClass = new PatientHelperClass(Helper_pName, Helper_pUsername,
                                    Helper_pEmail, Helper_p_PhoneNo, Helper_pAge, dcode, Helper_pPassword, Helper_pGender);

                            reference1.child(Helper_pUsername).setValue(p_helperClass);

                            Toast.makeText(patient_data.this, "Data Updated", Toast.LENGTH_SHORT).show();


                            DatabaseReference reference2 = firebaseDatabase1.getReference().child("patients").child(userId);
                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String patientname = dataSnapshot.child("pName").getValue().toString();
                                        fullName.getEditText().setText(patientname);
                                        String patientusername = dataSnapshot.child("pUsername").getValue().toString();
                                        //username.getEditText().setText(patientusername);
                                        String patientemail = dataSnapshot.child("pEmail").getValue().toString();
                                        email.getEditText().setText(patientemail);
                                        String patientphone = dataSnapshot.child("pPhoneNo").getValue().toString();
                                        phoneNO.getEditText().setText(patientphone);
                                        String patientpassword = dataSnapshot.child("pPassword").getValue().toString();
                                        password.getEditText().setText(patientpassword);
                                        String patientage = dataSnapshot.child("pAge").getValue().toString();
                                        age.setText(patientage);
                                        String patientgender = dataSnapshot.child("gender").getValue().toString();


                                        int ageyrs = Integer.parseInt(patientage.substring(6));
                                        int year = Calendar.getInstance().get(Calendar.YEAR);
                                        int ageresult = year - ageyrs;


                                        fullNameLabel.setText(patientname);
                                        UsernameLabel.setText(patientusername);
                                        heightLabel.setText(patientgender);
                                        //WeightLabel.setText("70");
                                        ageLabel.setText(String.valueOf(ageresult));

                                        if (patientgender.equals("Male")) {
                                            male.setChecked(true);
                                            gendericon.setBackgroundResource(R.drawable.malegender);

                                        } else {
                                            female.setChecked(true);
                                            gendericon.setBackgroundResource(R.drawable.womangender);

                                        }

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

            @Override
            public void onCheckedChanged(RadioGroup Gender, int i) {
                switch (i) {
                    case R.id.male:
                        gender = "Male";
                        break;
                    case R.id.female:
                        gender = "Female";
                        break;
                    default:
                }
            }


    @Override
    public void onBackPressed(){
        //Log.d(String.valueOf(getApplicationContext()), "back button");
        //Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(patient_data.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }


}