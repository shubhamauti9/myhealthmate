package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class influenzaQ extends AppCompatActivity {

    CheckBox minfluenza_checkbox_medicalcond, minfluenza_checkbox_fever, minfluenza_checkbox_cough, minfluenza_checkbox_vomit, minfluenza_checkbox_runnynose, minfluenza_checkbox_headache, minfluenza_checkbox_fatigue, minfluenza_checkbox_throatache, minfluenza_checkbox_myalgia;
    final int result[] = new int[11];
    TextView minfluenza_result_tv1;
    String x, y;
    int z;
    private String url = "http://" + "aspm2700.pythonanywhere.com" + "/influenza";
    private String postBodyString;
    private RequestBody requestBody;
    String userId;
    FirebaseDatabase firebaseDatabase;
    String p_age = "-1";
    String p_gender = "-1";
    String dcode, demail, pname;
    String ah, ld;
    int sysbp, diabp, fs, ams;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_influenza_q);


        SessionManagement sessionManagement = new SessionManagement(influenzaQ.this);
        userId = sessionManagement.getSession();

        final LoadingDialog loadingDialog = new LoadingDialog(influenzaQ.this);



        Log.d("this",userId);

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



        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("patients").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               String ag = dataSnapshot.child("pAge").getValue().toString();
               String g = dataSnapshot.child("gender").getValue().toString();


               p_age = ag;
               p_gender = g;


               Log.d("this",p_age);
               Log.d("this",p_gender);

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







        minfluenza_checkbox_fever = findViewById(R.id.influenza_checkbox_fever);
        minfluenza_checkbox_cough = findViewById(R.id.influenza_checkbox_cough);
        minfluenza_checkbox_vomit = findViewById(R.id.influenza_checkbox_vomit);
        minfluenza_checkbox_runnynose = findViewById(R.id.influenza_checkbox_runnynose);
        minfluenza_checkbox_headache = findViewById(R.id.influenza_checkbox_headache);
        minfluenza_checkbox_fatigue = findViewById(R.id.influenza_checkbox_fatigue);
        minfluenza_checkbox_throatache = findViewById(R.id.influenza_checkbox_throatache);
        minfluenza_checkbox_myalgia = findViewById(R.id.influenza_checkbox_myalgia);
        //minfluenza_checkbox_medicalcond = findViewById(R.id.influenza_checkbox_medicalcond);

        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
        }





        Log.d("this",String.valueOf(p_age));







        /*minfluenza_checkbox_medicalcond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_medicalcond.isChecked())
                    //result[2] = 1;
                else
                    //result[2] = 0;

            }
        });*/




        minfluenza_checkbox_fever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_fever.isChecked())
                    result[10] = 1;
                else
                    result[10] = 0;

            }
        });
        minfluenza_checkbox_cough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_cough.isChecked())
                    result[4] = 1;
                else
                    result[4] = 0;
            }
        });
        minfluenza_checkbox_vomit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_vomit.isChecked())
                    result[9] = 1;
                else
                    result[9] = 0;
            }
        });
        minfluenza_checkbox_runnynose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_runnynose.isChecked())
                    result[3] = 1;
                else
                    result[3] = 0;
            }
        });
        minfluenza_checkbox_headache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_headache.isChecked())
                    result[6] = 1;
                else
                    result[6] = 0;
            }
        });
        minfluenza_checkbox_fatigue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_fatigue.isChecked())
                    result[8] = 1;
                else
                    result[8] = 0;
            }
        });
        minfluenza_checkbox_throatache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_throatache.isChecked())
                    result[7] = 1;
                else
                    result[7] = 0;
            }
        });
        minfluenza_checkbox_myalgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minfluenza_checkbox_myalgia.isChecked())
                    result[5] = 1;
                else
                    result[5] = 0;
            }
        });















        minfluenza_result_tv1 = (TextView) findViewById(R.id.influenza_result_tv1);

        Button minfluenzabtn = (Button) findViewById(R.id.influenzabtn);
        minfluenzabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int age = Integer.parseInt(p_age.substring(6));
                String gd = p_gender;
                int year = Calendar.getInstance().get(Calendar.YEAR);
                Log.d("this",String.valueOf(year));
                Log.d("this",String.valueOf(p_age));
                Log.d("this",gd);



                result[0] = year - age;

                switch (gd){
                    case"Male":
                        result[1] = 1;
                        break;
                    case"Female":
                        result[1] = 0;
                        break;
                    default:
                        break;
                }



                if(ah.equals("true")){
                    result[2] = 1;
                }


                else if(ld.equals("true")){
                    result[2] = 1;
                }


                else if(sysbp > 120 && diabp > 80){
                    result[2] = 1;
                }


                else if((fs > 110 || fs < 70) && (ams > 140 || ams < 90)){
                    result[2] = 1;
                }
                else{
                    result[2] = 0;

                }



                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissdialog();
                    }
                }, 3000);



                x = result[0] + "," + result[1] + "," + result[2] + "," + result[3] + "," + result[4] + "," + result[5] + "," + result[6] + "," + result[7] + "," + result[8] + "," + result[9] + "," + result[10];

                Log.d("this", x.toString());
                postRequest(x, url);

            }
        });
    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg.toString();
        requestBody = new FormBody.Builder().add("value", postBodyString).build();
        return requestBody;
    }


    private void postRequest(String message, String URL) {
        RequestBody requestBody = buildRequestBody(message);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .post(requestBody)
                .url(URL)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(influenzaQ.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();


                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            y = response.body().string();
                            //Toast.makeText(influenzaQ.this, y, Toast.LENGTH_LONG).show();
                            z = Integer.parseInt(y);
                            String res = null;
                            if (z == 1) {
                                res = "Inzfluenza Type:- H1N1";
                                minfluenza_result_tv1.setText(res);
                                DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                rfyoga.child("influenza").setValue("yes");
                            } else if (z == 2) {
                                res = "Inzfluenza Type:- H3N2";
                                minfluenza_result_tv1.setText(res);
                                DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                rfyoga.child("influenza").setValue("yes");
                            } else {
                                res = "No Influenza";
                                minfluenza_result_tv1.setText(res);
                                DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                rfyoga.child("influenza").setValue("no");
                            }

                            insertData(result, res, dcode);

                            if(!dcode.equals("1")) {
                                sendingmail();
                            }



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }





                    public void sendingmail () {



                        String mssgc= "Influenza Assessment is taken by your patient named " + pname;

                        Log.d("this", mssgc);
                        Log.d("this", demail);

                        OkHttpClient client = new OkHttpClient();

                        RequestBody formBody = new FormBody.Builder().add("value1", demail).add("value2", mssgc).add("value3","Influenza Assessment").build();

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


                    public  void insertData(final int [] result, final String res, final String dcode) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH:mm:ss", Locale.getDefault());
                        final String currentDateandTime = sdf.format(new Date());
                        Log.d("this",currentDateandTime.toString());

                        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
                        final DatabaseReference ref = rootnode.getReference().child("influenza").child(userId);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String iz_date = currentDateandTime;
                                String iz_Age = Integer.toString(result[0]);
                                String iz_Gender = num_to_gender(result[1]);
                                String iz_Medicalcond = num_to_string(result[2]);
                                String iz_Runnynose = num_to_string(result[3]);
                                String iz_Cough = num_to_string(result[4]);
                                String iz_Myalgia = num_to_string(result[5]);
                                String iz_Headache = num_to_string(result[6]);
                                String iz_Throatache = num_to_string(result[7]);
                                String iz_Fatigue = num_to_string(result[8]);
                                String iz_Vomiting = num_to_string(result[9]);
                                String iz_Fever = num_to_string(result[10]);
                                String iz_dcode = dcode;
                                String iz_result = res;


                                InfluenzaData iz_data = new InfluenzaData(iz_date, iz_Age,iz_Gender,iz_Medicalcond,iz_Runnynose,iz_Cough,iz_Myalgia,iz_Headache,iz_Throatache,iz_Fatigue,iz_Vomiting,iz_Fever,iz_dcode,iz_result);

                                ref.child(currentDateandTime).setValue(iz_data);


                            }

                            private String  num_to_gender(int i) {
                                if(i == 1){
                                    return "Male";
                                }
                                else{
                                    return "Female";
                                }

                            }

                            private String num_to_string(int i) {

                                if(i == 1){
                                    return "Yes";
                                }
                                else{
                                    return"No";
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

        Intent intent = new Intent(influenzaQ.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }
}
