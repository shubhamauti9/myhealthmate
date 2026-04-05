package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

public class patient_healthprofile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner bg;
    Button update, medicationremind;
    TextInputLayout sysbp,diabp,fsbs,amsbs,height,weight;
    CheckBox chldis, heartdis;
    TextView fullNameLabel, UsernameLabel,heightLabel,WeightLabel,ageLabel,bmino,bmicat;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    String userId;
    String bg_text;
    Boolean chl = false;
    Boolean hdis = false;
    String bmic;
    ImageView bmilogo;
    RelativeLayout bmicard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_healthprofile);

        SessionManagement sessionManagement = new SessionManagement(patient_healthprofile.this);
        userId = sessionManagement.getSession();

        bg = (Spinner) findViewById(R.id.bg);
        sysbp = findViewById(R.id.ph_sysbp);
        diabp = findViewById(R.id.ph_diabp);
        fsbs =  findViewById(R.id.ph_fastsugar);
        amsbs = findViewById(R.id.ph_afmealsugar);
        height = findViewById(R.id.ph_height);
        weight = findViewById(R.id.ph_weight);
        chldis = (CheckBox) findViewById(R.id.ph_lungdisease);
        heartdis = (CheckBox) findViewById(R.id.ph_heartdisease);
        update = (Button) findViewById(R.id.ph_update);
        medicationremind = (Button) findViewById(R.id.ph_medicationremind);


        fullNameLabel = findViewById(R.id.patient_fullnameLable);

        UsernameLabel = findViewById(R.id.patient_usernameLable);

        ageLabel = findViewById(R.id.age_no);

        heightLabel = findViewById(R.id.height_no);

        WeightLabel = findViewById(R.id.weight_no);

        bmicard = (RelativeLayout) findViewById(R.id.bmi_layout);
        bmilogo = (ImageView) findViewById(R.id.bmi_logo);
        bmino = (TextView) findViewById(R.id.bmi_no);
        bmicat = (TextView) findViewById(R.id.bmi_cat);





        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.bloodgroup,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bg.setAdapter(adapter);
        bg.setOnItemSelectedListener(this);

        chldis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chldis.isChecked())
                    chl = true;
                else
                    chl = false;
            }
        });


        heartdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (heartdis.isChecked())
                    hdis = true;
                else
                    hdis = false;
            }
        });


        rootnode = FirebaseDatabase.getInstance();
        DatabaseReference ref = rootnode.getReference().child("patients").child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String patientname = dataSnapshot.child("pName").getValue().toString();
                String patientage = dataSnapshot.child("pAge").getValue().toString();

                int ageyrs = Integer.parseInt(patientage.substring(6));
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int ageresult = year - ageyrs;


                fullNameLabel.setText(patientname);
                UsernameLabel.setText(userId);
                ageLabel.setText(String.valueOf(ageresult));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        reference = rootnode.getReference("patients_healthprofile");
        final DatabaseReference reference1 = rootnode.getReference().child("patients_healthprofile").child(userId);
        final DatabaseReference reference2 = rootnode.getReference().child("patients_healthprofile").child(userId);

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String sbp = dataSnapshot.child("ph_sysbp").getValue().toString();
                    sysbp.getEditText().setText(sbp);
                    String dbp = dataSnapshot.child("ph_diabp").getValue().toString();
                    diabp.getEditText().setText(dbp);
                    String fsl = dataSnapshot.child("ph_fastsugar").getValue().toString();
                    fsbs.getEditText().setText(fsl);
                    String amsl = dataSnapshot.child("ph_aftermealsugar").getValue().toString();
                    amsbs.getEditText().setText(amsl);
                    String h = dataSnapshot.child("ph_height").getValue().toString();
                    height.getEditText().setText(h);
                    String w = dataSnapshot.child("ph_weight").getValue().toString();
                    weight.getEditText().setText(w);
                    String chchld = dataSnapshot.child("ph_chroniclungdisease").getValue().toString();
                    chldis.setChecked(Boolean.parseBoolean(chchld));
                    chl = Boolean.parseBoolean(chchld);

                    String chhdis = dataSnapshot.child("ph_heartdisease").getValue().toString();
                    heartdis.setChecked(Boolean.parseBoolean(chhdis));
                    hdis = Boolean.parseBoolean(chhdis);
                    heightLabel.setText(h);
                    WeightLabel.setText(w);

                    bmi();

                    String bloodg = dataSnapshot.child("ph_bloodgroup").getValue().toString();

                    bg.setAdapter(adapter);
                    if (bloodg != null) {
                        int spinnerPosition = adapter.getPosition(bloodg);
                        bg.setSelection(spinnerPosition);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((validatebg() && validatesysbp() && validatediabp() && validatefsbs() && validateamsbs() && validateh() && validatew()) == true)
                {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String ph_username = userId;
                            String ph_bloodgroup = bg_text;
                            String ph_sysbp = sysbp.getEditText().getText().toString();
                            String ph_diabp = diabp.getEditText().getText().toString();
                            String ph_fastsugar = fsbs.getEditText().getText().toString();
                            String ph_aftermealsugar = amsbs.getEditText().getText().toString();
                            String ph_height = height.getEditText().getText().toString();
                            String ph_weight = weight.getEditText().getText().toString();
                            String ph_chroniclungdisease = String.valueOf(chl);
                            String ph_heartdisease = String.valueOf(hdis);

                            PatientHealthProfile ph_data = new PatientHealthProfile(ph_username, ph_bloodgroup, ph_sysbp, ph_diabp, ph_fastsugar, ph_aftermealsugar, ph_height, ph_weight, ph_chroniclungdisease, ph_heartdisease);

                            reference.child(userId).setValue(ph_data);
                            Toast.makeText(patient_healthprofile.this, "Health Profile Updated Successfully", Toast.LENGTH_SHORT).show();


                            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String sbp = dataSnapshot.child("ph_sysbp").getValue().toString();
                                        sysbp.getEditText().setText(sbp);
                                        String dbp = dataSnapshot.child("ph_diabp").getValue().toString();
                                        diabp.getEditText().setText(dbp);
                                        String fsl = dataSnapshot.child("ph_fastsugar").getValue().toString();
                                        fsbs.getEditText().setText(fsl);
                                        String amsl = dataSnapshot.child("ph_aftermealsugar").getValue().toString();
                                        amsbs.getEditText().setText(amsl);
                                        String h = dataSnapshot.child("ph_height").getValue().toString();
                                        height.getEditText().setText(h);
                                        String w = dataSnapshot.child("ph_weight").getValue().toString();
                                        weight.getEditText().setText(w);
                                        String chchld = dataSnapshot.child("ph_chroniclungdisease").getValue().toString();
                                        chldis.setChecked(Boolean.parseBoolean(chchld));
                                        chl = Boolean.parseBoolean(chchld);

                                        String chhdis = dataSnapshot.child("ph_heartdisease").getValue().toString();
                                        heartdis.setChecked(Boolean.parseBoolean(chhdis));
                                        hdis = Boolean.parseBoolean(chhdis);
                                        heightLabel.setText(h);
                                        WeightLabel.setText(w);

                                        bmi();


                                        if(Integer.parseInt(sbp) > 120 && Integer.parseInt(dbp) > 80){
                                            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                            rfyoga.child("hypertension").setValue("yes");
                                        }

                                        else{
                                            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                            rfyoga.child("hypertension").setValue("no");

                                        }


                                        if((Integer.parseInt(fsl) > 110 || Integer.parseInt(fsl) < 70) && (Integer.parseInt(amsl) > 140 || Integer.parseInt(amsl) < 90)){
                                            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                            rfyoga.child("diabetes").setValue("yes");
                                        }

                                        else{
                                            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                            rfyoga.child("diabetes").setValue("no");

                                        }




                                        String bloodg = dataSnapshot.child("ph_bloodgroup").getValue().toString();

                                        bg.setAdapter(adapter);
                                        if (bloodg != null) {
                                            int spinnerPosition = adapter.getPosition(bloodg);
                                            bg.setSelection(spinnerPosition);
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        /*Intent intent = new Intent(patient_healthprofile.this, patient_dashboard.class);
                        startActivity(intent);*/

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            private boolean validatew(){
                String val = weight.getEditText().getText().toString();
                if(val.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter the Weight", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }

            private boolean validateh() {
                String val = height.getEditText().getText().toString();
                if(val.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter the Height", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }

            private boolean validateamsbs() {
                String val = amsbs.getEditText().getText().toString();
                if(val.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter After Meal Blood Sugar Level", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }

            private boolean validatefsbs() {
                String val = fsbs.getEditText().getText().toString();
                if(val.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Fasting Blood Sugar Level", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }

            private boolean validatediabp() {
                String val = diabp.getEditText().getText().toString();
                if(val.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Diastolic Blood Pressure", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }
            }

            private boolean validatesysbp() {
                String val = sysbp.getEditText().getText().toString();
                if(val.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Systolic Blood Pressure", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }

            }

            private boolean validatebg() {

                if(bg_text.equals("Select the Blood Group")){
                    Toast.makeText(getApplicationContext(), "Select Blood Group", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    return true;
                }

            }
        });





        medicationremind = (Button)findViewById(R.id.ph_medicationremind);
        medicationremind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_healthprofile.this, medicationreminder.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bg_text = parent.getItemAtPosition(position).toString();
        Log.d("this",bg_text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void bmi() {
        String w = weight.getEditText().getText().toString();
        String h = height.getEditText().getText().toString();
        float wkg = Float.parseFloat(w);
        float hm = Float.parseFloat(h);
        hm = hm / 100;
        float bmiind = wkg / (hm * hm);
        DecimalFormat df = new DecimalFormat("0.00");
        String bmiinds = df.format(bmiind);
        Log.d("this", bmiinds);
        if (Float.parseFloat(bmiinds) < 18.5) {
            bmic = "UnderWeight";
            bmino.setText(bmiinds);
            bmicat.setText(bmic);
            bmilogo.setBackgroundResource(R.drawable.underweight);
            bmicard.setBackgroundColor(Color.parseColor("#89A1DD"));
            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
            rfyoga.child("bmi").setValue(bmic);

        } else if (Float.parseFloat(bmiinds) >= 18.5 && Float.parseFloat(bmiinds) <= 24.9) {
            bmic = "Healthy";
            bmino.setText(bmiinds);
            bmicat.setText(bmic);
            bmilogo.setBackgroundResource(R.drawable.healthyperson);
            bmicard.setBackgroundColor(Color.parseColor("#43EC49"));
            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
            rfyoga.child("bmi").setValue(bmic);

        } else if (Float.parseFloat(bmiinds) >= 25 && Float.parseFloat(bmiinds) <= 29.9) {
            bmic = "OverWeight";
            bmino.setText(bmiinds);
            bmicat.setText(bmic);
            bmilogo.setBackgroundResource(R.drawable.overweight);
            bmicard.setBackgroundColor(Color.parseColor("#F46350"));
            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
            rfyoga.child("bmi").setValue(bmic);

        } else if (Float.parseFloat(bmiinds) >= 30 && Float.parseFloat(bmiinds) <= 39.9) {
            bmic = "Obese";
            bmino.setText(bmiinds);
            bmicat.setText(bmic);
            bmilogo.setBackgroundResource(R.drawable.obese);
            bmicard.setBackgroundColor(Color.parseColor("#E50623"));
            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
            rfyoga.child("bmi").setValue(bmic);

        } else {
            bmic = "Severely Obese";
            bmino.setText(bmiinds);
            bmicat.setText(bmic);
            bmilogo.setBackgroundResource(R.drawable.severelyobese);
            bmicard.setBackgroundColor(Color.parseColor("#FF0000"));
            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
            rfyoga.child("bmi").setValue(bmic);

        }


    }

    @Override
    public void onBackPressed(){
        //Log.d(String.valueOf(getApplicationContext()), "back button");
        //Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(patient_healthprofile.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }
}