package com.example.myhealthmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class covidQ extends AppCompatActivity {

    private CheckBox mfever,mdry_cough,msore_throat,mrunnynose,mheadache,mfatigue,mbreathing_problem,mlung_disease,mheart_disease,mdiabetes,mhyper_tension,mstomach_ache;
    int result[] = new int[17];
    String userId;
    String res;
    String dcode;
    String demail;
    String pname;
    TextView c_result;
    private CheckBox mtravel_abroad_CB,mcontact_covid_CB,mlarge_gathering_CB,mvisit_public_places_CB,mfamily_member_traced_CB;

    String x, y, ah, ld;
    int z, sysbp, diabp, ams, fs;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_covid_q);
        SessionManagement sessionManagement = new SessionManagement(covidQ.this);
        userId = sessionManagement.getSession();

        final LoadingDialog loadingDialog = new LoadingDialog(covidQ.this);


        DatabaseReference refhp = FirebaseDatabase.getInstance().getReference("patients_healthprofile").child(userId);
        refhp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ah = dataSnapshot.child("ph_heartdisease").getValue().toString();

                ld = dataSnapshot.child("ph_chroniclungdisease").getValue().toString();

                sysbp = Integer.parseInt(dataSnapshot.child("ph_sysbp").getValue().toString());
                diabp = Integer.parseInt(dataSnapshot.child("ph_diabp").getValue().toString());



                fs = Integer.parseInt(dataSnapshot.child("ph_fastsugar").getValue().toString());
                ams = Integer.parseInt(dataSnapshot.child("ph_aftermealsugar").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
        }

        mfever = findViewById(R.id.covid_CB_fever);
        mdry_cough = findViewById(R.id.covid_CB_drycough);
        msore_throat = findViewById(R.id.covid_CB_sorethroat);
        mrunnynose = findViewById(R.id.covid_CB_runnynose);
        mheadache = findViewById(R.id.covid_CB_headache);
        mfatigue = findViewById(R.id.covid_CB_fatigue);
        mbreathing_problem = findViewById(R.id.covid_CB_breathingproblem);
        /*mlung_disease = findViewById(R.id.covid_CB_lungdisease);
        mheart_disease = findViewById(R.id.covid_CB_heartdisease);
        mdiabetes = findViewById(R.id.covid_CB_diabetes);
        mhyper_tension = findViewById(R.id.covid_CB_hypertension);*/
        mstomach_ache = findViewById(R.id.covid_CB_stomachache);

        Button mcovid_btn = (Button) findViewById(R.id.covidbtn);

        mtravel_abroad_CB = findViewById(R.id.covid_CB_travelabroad);

        mcontact_covid_CB = findViewById(R.id.covid_CB_contactCovidpatient);

        mlarge_gathering_CB = findViewById(R.id.covid_CB_largegathering);

        mvisit_public_places_CB = findViewById(R.id.covid_CB_visitpublicplace);

        mfamily_member_traced_CB = findViewById(R.id.covid_CB_family);

        mfever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mfever.isChecked())
                    result[1] = 1;
                else
                    result[1] = 0;
            }
        });
        mdry_cough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdry_cough.isChecked())
                    result[2] = 1;
                else
                    result[2] = 0;
            }
        });
        msore_throat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msore_throat.isChecked())
                    result[3] = 1;
                else
                    result[3] = 0;
            }
        });
        mrunnynose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mrunnynose.isChecked())
                    result[4] = 1;
                else
                    result[4] = 0;
            }
        });
        mheadache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mheadache.isChecked())
                    result[6] = 1;
                else
                    result[6] = 0;
            }
        });
        mfatigue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mfatigue.isChecked())
                    result[10] = 1;
                else
                    result[10] = 0;
            }
        });
        mbreathing_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbreathing_problem.isChecked())
                    result[0] = 1;
                else
                    result[0] = 0;
            }
        });
        /*mlung_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlung_disease.isChecked())
                    result[5] = 1;
                else
                    result[5] = 0;
            }
        });
        mheart_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mheart_disease.isChecked())
                    result[7] = 0;
                else
                    result[7] = 0;

            }
        });
        mdiabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdiabetes.isChecked())
                    result[8] = 1;
                else
                    result[8] = 0;
            }
        });
        mhyper_tension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mhyper_tension.isChecked())
                    result[9] = 1;
                else
                    result[9] = 0;
            }
        });*/

        mstomach_ache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mstomach_ache.isChecked())
                    result[11] = 1;
                else
                    result[11] = 0;
            }
        });
        mtravel_abroad_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mtravel_abroad_CB.isChecked())
                    result[12] = 1;
                else
                    result[12] = 0;
            }
        });
        mcontact_covid_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcontact_covid_CB.isChecked())
                    result[13] = 1;
                else
                    result[13] = 0;
            }
        });
        mlarge_gathering_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlarge_gathering_CB.isChecked())
                    result[14] = 1;
                else
                    result[14] = 0;
            }
        });
        mvisit_public_places_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvisit_public_places_CB.isChecked())
                    result[15] = 1;
                else
                    result[15] = 0;
            }
        });
        mfamily_member_traced_CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mfamily_member_traced_CB.isChecked())
                    result[16] = 1;
                else
                    result[16] = 0;
            }
        });




        DatabaseReference rfc = FirebaseDatabase.getInstance().getReference("patients").child(userId);
        rfc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pname = dataSnapshot.child("pName").getValue().toString();
                dcode = dataSnapshot.child("dcode").getValue().toString();
                Log.d("this", dcode);
                if (!dcode.equals("1")) {
                    DatabaseReference rfcd = FirebaseDatabase.getInstance().getReference("doctors").child(dcode);
                    rfcd.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            demail = dataSnapshot1.child("dEmail").getValue().toString();

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




        c_result = (TextView) findViewById(R.id.covid_result_tv);
        mcovid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ah.equals("false")){
                    result[7] = 0;
                }
                else {
                    result[7] = 1;
                }

                if(ld.equals("false")){
                    result[5] = 0;
                }
                else {
                    result[5] = 1;
                }

                if(sysbp <= 120 && diabp <= 80){
                    result[9] = 0;
                }
                else{
                    result[9] = 1;

                }

                if(fs <= 110 && fs >= 70 && ams <= 140 && ams >= 90){
                    result[8] = 0;
                }
                else{
                    result[8] = 1;

                }

                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissdialog();
                    }
                }, 3000);



                x = result[0] + "," + result[1] + "," + result[2] + "," + result[3] + "," + result[4] + "," + result[5] + "," + result[6] + "," + result[7] + "," + result[8] + "," + result[9] + "," + result[10] + "," + result[11] + "," + result[12] + "," + result[13] + "," + result[14] + "," + result[15] + "," + result[16];

                Log.d("this", x.toString());

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder().add("value", x).build();

                Request request = new Request.Builder().url("http://aspm2700.pythonanywhere.com/covid").post(formBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("this", "not found");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        y = response.body().string();
                        z = Integer.parseInt(y);
                        if (z == 1) {
                            res = "COVID-19 Positive";
                            c_result.setText(res);
                            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                            rfyoga.child("covid").setValue("yes");
                        } else {
                            res = "COVID-19 Negative";
                            c_result.setText(res);
                            DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                            rfyoga.child("covid").setValue("no");

                        }






                        insertData(result, res, dcode);


                    }

                    private void insertData(final int[] result, final String res, final String dcode) {




                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH:mm:ss", Locale.getDefault());
                        final String currentDateandTime = sdf.format(new Date());
                        Log.d("this", currentDateandTime.toString());

                        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
                        final DatabaseReference ref = rootnode.getReference().child("covid").child(userId);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                                String cd_date = currentDateandTime;
                                String cd_Breathingproblem = num_to_string(result[0]);
                                String cd_Fever = num_to_string(result[1]);
                                String cd_Drycough = num_to_string(result[2]);
                                String cd_Sorethroat = num_to_string(result[3]);
                                String cd_Runnynose = num_to_string(result[4]);
                                String cd_Chroniclungdisease = num_to_string(result[5]);
                                String cd_Headache = num_to_string(result[6]);
                                String cd_Heartdisease = num_to_string(result[7]);
                                String cd_Diabetes = num_to_string(result[8]);
                                String cd_Hypertension = num_to_string(result[9]);
                                String cd_Fatigue = num_to_string(result[10]);
                                String cd_Stomachache = num_to_string(result[11]);
                                String cd_Abroad = num_to_string(result[12]);
                                String cd_Contact = num_to_string(result[13]);
                                String cd_largegathering = num_to_string(result[14]);
                                String cd_publicplacevisit = num_to_string(result[15]);
                                String cd_familyworking = num_to_string(result[16]);
                                String cd_dcode = dcode;
                                String cd_result = res;


                                CovidData cd_data = new CovidData(cd_date, cd_Breathingproblem, cd_Fever, cd_Drycough, cd_Sorethroat, cd_Runnynose, cd_Chroniclungdisease, cd_Headache, cd_Heartdisease, cd_Diabetes, cd_Fatigue, cd_Stomachache, cd_Hypertension, cd_Abroad, cd_Contact, cd_largegathering, cd_publicplacevisit, cd_familyworking, cd_dcode, cd_result);

                                ref.child(currentDateandTime).setValue(cd_data);




                                if(!dcode.equals("1")) {
                                    sendingmail();
                                }

                            }

                            public void sendingmail () {



                                String mssgc= "COVID-19 Assessment is taken by your patient named " + pname;

                                Log.d("this", mssgc);
                                Log.d("this", demail);

                                OkHttpClient client = new OkHttpClient();

                                RequestBody formBody = new FormBody.Builder().add("value1", demail).add("value2", mssgc).add("value3","COVID-19 Assessment").build();

                                Request request = new Request.Builder().url("http://aspm2700.pythonanywhere.com/sendemail").post(formBody).build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        Log.d("this", "not found");
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        String y1 = response.body().string();
                                        Log.d("this", y1);


                                    }


                                });
                            }


                            private String num_to_string(int i) {

                                if (i == 1) {
                                    return "Yes";
                                } else {
                                    return "No";
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }


                });


            }
        });




    }

    @Override
    public void onBackPressed(){
        //Log.d(String.valueOf(getApplicationContext()), "back button");
        //Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(covidQ.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }



}
