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
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;

public class p_signup extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    //variables
    TextInputLayout pName, pUsername, pEmail, pPhoneNo, pPassword, pHeight, pWeight;
    Button pSignUp, pBack;
    RadioGroup pGender;
    DatePickerDialog.OnDateSetListener setListener;
    FirebaseDatabase rootNode, rootNode1;
    DatabaseReference reference, reference1;
    String gender = "-1";
    TextView text_1, text_2, tgender, pAge;
    String dcode = "1";





    private Boolean validateName() {
        String val = pName.getEditText().getText().toString();

        if (val.isEmpty()) {
            pName.setError("Field cannot be empty");
            return false;

        } else {
            pName.setError(null);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = pUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            pUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            pUsername.setError("Username too long");
            return false;
        } else if (val.length() <= 3) {
            pUsername.setError("Username too short");
            return false;

        } else if (!val.matches(noWhiteSpace)) {
            pUsername.setError("White Spaces are not allowed");
            return false;
        } else {
            pUsername.setError(null);
            pUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = pEmail.getEditText().getText().toString();
        String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        if (val.isEmpty()) {
            pEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            pEmail.setError("Invalid email address");
            return false;
        } else {
            pEmail.setError(null);
            pEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = pPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            pPhoneNo.setError("Field cannot be empty");
            return false;

        } else if (val.length() <= 9) {
            pPhoneNo.setError("Invalid Phone No.");
            return false;
        } else {
            pPhoneNo.setError(null);
            pPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateAge() {



        String val = pAge.getText().toString();

        if(val.isEmpty()){
            pAge.setBackgroundResource(R.drawable.border1);
            text_2.setVisibility(View.VISIBLE);
            text_2.setTextColor(Color.parseColor("#FFF30606"));
            return false;
        }
        else{
            pAge.setBackgroundResource(R.drawable.border);
            text_2.setVisibility(View.INVISIBLE);
            text_2.setTextColor(Color.parseColor("#FFBDBCBD"));
            return true;
        }


    }

    private Boolean validateGender() {
        if (gender == "-1") {
            //Toast.makeText(p_signup.this, "Select the Gender", Toast.LENGTH_SHORT).show();
            pGender.setBackgroundResource(R.drawable.border1);
            text_1.setVisibility(View.VISIBLE);
            tgender.setTextColor(Color.parseColor("#FFF30606"));


            return false;
        } else {
            pGender.setBackgroundResource(R.drawable.border);
            text_1.setVisibility(View.INVISIBLE);
            tgender.setTextColor(Color.parseColor("#FFBDBCBD"));
            return true;
        }
    }

    private Boolean validateHeight() {
        String val = pHeight.getEditText().getText().toString();

        if (val.isEmpty()) {
            pHeight.setError("Field cannot be empty");
            return false;

        } else {
            pHeight.setError(null);
            pHeight.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateWeight() {
        String val = pWeight.getEditText().getText().toString();

        if (val.isEmpty()) {
            pWeight.setError("Field cannot be empty");
            return false;

        } else {
            pWeight.setError(null);
            pWeight.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validatePassword() {
        String val = pPassword.getEditText().getText().toString();
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
            pPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            pPassword.setError("Password is too weak");
            return false;
        } else {
            pPassword.setError(null);
            pPassword.setErrorEnabled(false);
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
        setContentView(R.layout.activity_p_signup);

        tgender = (TextView) findViewById(R.id.tgender);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_1.setVisibility(View.GONE);

        text_2 = (TextView) findViewById(R.id.text_2);
        text_2.setVisibility(View.GONE);
        pAge = (TextView) findViewById(R.id.p_age);

        pName = findViewById(R.id.p_name);
        pUsername = findViewById(R.id.p_username);
        pEmail = findViewById(R.id.p_email);
        pPhoneNo = findViewById(R.id.p_phoneNo);
        pPassword = findViewById(R.id.p_password);
        pAge = findViewById(R.id.p_age);
        //pHeight = findViewById(R.id.p_height);
        //pWeight = findViewById(R.id.p_weight);
        pSignUp = findViewById(R.id.p_final_signUp);
        pBack = findViewById(R.id.p_backToLogin);
        pGender = findViewById(R.id.gender);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day  = calendar.get(Calendar.DAY_OF_MONTH);
        pAge.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        p_signup.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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
                pAge.setText(date);
                Log.d("this",date);

            }
        };


        pGender.setOnCheckedChangeListener(this);

        pSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("patients");

                rootNode1 = FirebaseDatabase.getInstance();
                reference1 = rootNode1.getReference("doctors");




                if ((validateName() && validateUsername() && validateEmail() && validatePhoneNo() && validateAge() && validateGender() && validatePassword()) == true) {


                    final String userEnteredUsername = pUsername.getEditText().getText().toString().trim();
                    final Query checkUser = reference.orderByChild("pUsername").equalTo(userEnteredUsername);
                    final Query checkdoc = reference1.orderByChild("dUsername").equalTo(userEnteredUsername);

                    checkdoc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                pUsername.setError("Username Already Exist");

                            }
                            else{
                                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            pUsername.setError("Username Already Exist");
                                        } else {

                                            //Get all the values
                                            String Helper_pName = pName.getEditText().getText().toString();
                                            String Helper_pUsername = pUsername.getEditText().getText().toString();
                                            String Helper_pEmail = pEmail.getEditText().getText().toString();
                                            String Helper_p_PhoneNo = pPhoneNo.getEditText().getText().toString();
                                            String Helper_pAge = pAge.getText().toString();
                                            //String Helper_pHeight = pHeight.getEditText().getText().toString();
                                            //String Helper_pWeight = pWeight.getEditText().getText().toString();
                                            String Helper_pPassword = pPassword.getEditText().getText().toString();
                                            String Helper_pGender = gender;

                                            PatientHelperClass p_helperClass = new PatientHelperClass(Helper_pName, Helper_pUsername,
                                                    Helper_pEmail, Helper_p_PhoneNo, Helper_pAge, dcode, Helper_pPassword, Helper_pGender);

                                            reference.child(Helper_pUsername).setValue(p_helperClass);


                                            final DatabaseReference refyc = FirebaseDatabase.getInstance().getReference("yoga").child(pUsername.getEditText().getText().toString().trim());

                                            refyc.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if(!dataSnapshot.exists()) {

                                                        final DatabaseReference refy = FirebaseDatabase.getInstance().getReference("yoga");
                                                        refy.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                String bmi = "Healthy";
                                                                String influenza = "no";
                                                                String covid = "no";
                                                                String stress = "no";
                                                                String diabetes = "no";
                                                                String hypertension = "no";

                                                                YogaDb ydb = new YogaDb(bmi, influenza, covid, stress, diabetes, hypertension, pUsername.getEditText().getText().toString().trim());
                                                                refy.child(pUsername.getEditText().getText().toString().trim()).setValue(ydb);

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










                                            Toast.makeText(p_signup.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();






                                            Intent intent = new Intent(p_signup.this, patient_login.class);
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

    @Override
    public void onCheckedChanged(RadioGroup pGender, int i) {
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
}