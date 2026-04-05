package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhealthmate.HomeAdapter.YogaAdapter;
import com.example.myhealthmate.HomeAdapter.YogaHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class patient_dashboard extends AppCompatActivity {

    RecyclerView yogaRecycler;
    RecyclerView.Adapter adapter;

    Button profileButton,stressButton,influenzaButton, report, emergency;
    LinearLayout covidClickable, influenzaClickable, stressClickable,connect;
    RelativeLayout healthprofile, checkappt, expense;
    ImageView l;
    TextView name;
    FirebaseDatabase firebaseDatabase;
    String userId, phone, email, pphone, pemail, n, email1, docname1, docname;

    String db_bmi, db_influenza, db_covid, db_stress, db_diabetes, db_hypertension;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_patient_dashboard);

        SessionManagement sessionManagement = new SessionManagement(patient_dashboard.this);
        userId = sessionManagement.getSession();


        yogaRecycler = findViewById(R.id.yoga_recycler_view);
        //db_bmi = "Healthy";
        //db_covid = "yes";







        //yogarecommend();



        final DatabaseReference refycdb = FirebaseDatabase.getInstance().getReference("yoga").child(userId);



        refycdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                db_bmi = dataSnapshot.child("bmi").getValue().toString();
                //Log.d("this", db_bmi);
                //Toast.makeText(patient_dashboard.this, db_bmi, Toast.LENGTH_SHORT).show();
                db_covid = dataSnapshot.child("covid").getValue().toString();
                db_influenza = dataSnapshot.child("influenza").getValue().toString();
                db_stress = dataSnapshot.child("stress").getValue().toString();
                db_diabetes = dataSnapshot.child("diabetes").getValue().toString();
                db_hypertension = dataSnapshot.child("hypertension").getValue().toString();

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

                n = dataSnapshot.child("pName").getValue().toString();
                pphone = dataSnapshot.child("pPhoneNo").getValue().toString();
                pemail = dataSnapshot.child("pEmail").getValue().toString();

                name = (TextView) findViewById(R.id.name_text);
                name.setText(n);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){


                }
            });




        profileButton = (Button) findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, patient_data.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        emergency = (Button) findViewById(R.id.emergency_button);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("this", userId);

                DatabaseReference refemg = FirebaseDatabase.getInstance().getReference("patients").child(userId);
                refemg.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String docuname = dataSnapshot.child("dcode").getValue().toString();

                        Log.d("this", docuname);


                        if (!docuname.equals("1")) {
                            ActivityCompat.requestPermissions(patient_dashboard.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
                            //docuname = "kd29";
                            Log.d("this", docuname.toString());
                            DatabaseReference refemgd = FirebaseDatabase.getInstance().getReference("doctors").child(docuname);
                            refemgd.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    phone = dataSnapshot.child("dPhoneNo").getValue().toString();
                                    email = dataSnapshot.child("dEmail").getValue().toString();
                                    docname1 = dataSnapshot.child("dName").getValue().toString();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            final AlertDialog eadlg = new AlertDialog.Builder(patient_dashboard.this)
                                    .setTitle("Alert")
                                    .setMessage("If Emergency Click Yes Else No")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            String message = "Dear Doctor " + docname1 + ", " + "There is an Emergency with your patient named " + n + " PhoneNo " + pphone;
                                            smsManager.sendTextMessage(phone, null, message, null, null);

                                            OkHttpClient client = new OkHttpClient();

                                            RequestBody formBody = new FormBody.Builder().add("value1", email).add("value2", message).add("value3","Emergency").build();

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



                                            Toast.makeText(patient_dashboard.this, "Emergency Notification send to the Doctor", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();





                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create();

                            eadlg.show();

                        }


                        else{
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, "Hospital near me");
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }




        });


        stressClickable = (LinearLayout) findViewById(R.id.stress);
        stressClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, stressQ.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                patient_dashboard.this.finish();
                startActivity(intent);


            }
        });


        influenzaClickable = (LinearLayout) findViewById(R.id.influenza);
        influenzaClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, influenzaQ.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                patient_dashboard.this.finish();
                startActivity(intent);

            }
        });




        covidClickable = (LinearLayout) findViewById(R.id.covid19);
        covidClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, covidQ.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                patient_dashboard.this.finish();
                startActivity(intent);

                //System.exit(0);

            }
        });

        connect = (LinearLayout) findViewById(R.id.connectDoc);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, connectDoctor.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                patient_dashboard.this.finish();
                startActivity(intent);

            }
        });






        l = (ImageView) findViewById(R.id.logout);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionManagement sessionManagement = new SessionManagement(patient_dashboard.this);
                sessionManagement.removeSession();

                moveToLogin();

            }
        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("patients_healthprofile");
        final Query checkp = ref.orderByChild("ph_username").equalTo(userId);
        checkp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                }
                else{
                    AlertDialog adlg = new AlertDialog.Builder(patient_dashboard.this)
                            .setTitle("Alert")
                            .setMessage("Health Profile not filled")
                            .setPositiveButton("OK",new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(patient_dashboard.this, patient_healthprofile.class);
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .create();
                    adlg.show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        healthprofile = (RelativeLayout) findViewById(R.id.healthprofile_btn);
        healthprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, patient_healthprofile.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                patient_dashboard.this.finish();
                startActivity(intent);
            }
        });






        checkappt = (RelativeLayout) findViewById(R.id.checkappt);
        checkappt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DatabaseReference refemg = FirebaseDatabase.getInstance().getReference("patients").child(userId);
                refemg.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String docuname = dataSnapshot.child("dcode").getValue().toString();

                        Log.d("this", docuname);


                        if (!docuname.equals("1")) {
                            //ActivityCompat.requestPermissions(patient_dashboard.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
                            //docuname = "kd29";
                            Log.d("this", docuname.toString());

                            DatabaseReference refemgd = FirebaseDatabase.getInstance().getReference("doctors").child(docuname);
                            refemgd.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    email1 = dataSnapshot.child("dEmail").getValue().toString();
                                    docname = dataSnapshot.child("dName").getValue().toString();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            final AlertDialog eadlg = new AlertDialog.Builder(patient_dashboard.this)
                                    .setTitle("Alert")
                                    .setMessage("Click Yes for Appointment Request Else No")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            String message = "Dear Doctor " + docname + ", " + "There is an Appointment Request from your patient named " + n;


                                            OkHttpClient client = new OkHttpClient();

                                            RequestBody formBody = new FormBody.Builder().add("value1", email1).add("value2", message).add("value3","Appointment Request").build();

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

                                            String mssg = "Dear " + n + ", " + "Appointment Request has been sent to Doctor " + docname;


                                            OkHttpClient client1 = new OkHttpClient();

                                            RequestBody formBody1 = new FormBody.Builder().add("value1", pemail).add("value2", mssg).add("value3","Appointment Request").build();

                                            Request request1 = new Request.Builder().url("http://aspm2700.pythonanywhere.com/sendemail").post(formBody1).build();
                                            client.newCall(request1).enqueue(new Callback() {
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







                                            Toast.makeText(patient_dashboard.this, "Appointment Request Notification send to the Doctor", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();





                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create();

                            eadlg.show();

                        }


                        else{
                            Toast.makeText(patient_dashboard.this, "Please Connect to a Doctor", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });

        report = (Button) findViewById(R.id.report_button);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, view_report.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                patient_dashboard.this.finish();
                startActivity(intent);
            }
        });

        expense = (RelativeLayout) findViewById(R.id.expnse);
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patient_dashboard.this, expense.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                patient_dashboard.this.finish();
                startActivity(intent);


            }
        });



        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                yogaRecycler();

            }
        }, 3000);




    }




    public void yogaRecycler() {





        yogaRecycler.setHasFixedSize(true);
        yogaRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<YogaHelperClass> yogaHelperLocations = new ArrayList<>();



        Log.d("this",db_bmi);

        if(db_bmi.equals("Healthy")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.tadasana, "Tadasana", "Tadasana, Mountain Pose or Samasthiti is a standing asana in modern yoga as exercise"));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.vrikshasan, "Vrikshasan", "It is one of the very few standing poses in medieval hatha yoga and remains popular in modern yoga as exercise."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.kursiasana, "Kursiasana", "Chair Pose is a standing asana in modern yoga as exercise,it was a low squatting asana in medieval hatha yoga."));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }

        if(db_bmi.equals("UnderWeight")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.suptabadhakonasana, "Supta Badhakonasana", "It benefits the other parts of the body like the prostate gland kidneys, and urinary bladder."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.vajrasana, "Vajrasana", "Vajrasana, Thunderbolt Pose, or Diamond Pose, is a kneeling asana in hatha yoga and modern yoga as exercise."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.sarvangasana, "Sarvangasana", "Sarvangasana, Shoulderstand, or more fully Salamba Sarvangasana, is an inverted asana in modern yoga as exercise"));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }

        if(db_bmi.equals("OverWeight")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.jalandharabandha, "Jalandharabandha", "It is one of the three interior locks used in asana and pranayama practice to control and harness the flow of energy through the body."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.virabhadrasana, "Virabhadrasana", "It is a group of related lunging standing asanas in modern yoga as exercise commemorating the exploits of a mythical warrior, Virabhadra."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.simhasana, "Simhasana", "Simhasana or Lion Pose is an asana in hatha yoga and modern yoga as exercise."));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }

        if(db_bmi.equals("Obese") || db_bmi.equals("Severly Obese")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.ardha_pincha_mayurasana, "Ardha Pincha Mayurasana", "In this Asana you have to keep your forearms and your toes on the floor"));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.naukasana, "Naukasana", "Navasana, Naukasana, Boat Pose, or Paripurna Navasana is a seated asana in modern yoga as exercise"));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }


        if(db_covid.equals("yes")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.shishuasana, "Shishuasana", "Balasana is a counter asana for various asanas and is usually practiced before and after Sirsasana."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.setu_bandhasana, "Setu Bandhasana", "Shoulder supported bridge or simply Bridge"));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.halasana, "Halasana", "It is an inverted asana in hatha yoga and modern yoga as exercise."));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }

        if(db_stress.equals("Mild") || db_stress.equals("Moderate") || db_stress.equals("Severe")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.dhanurasana, "Dhanurasana", "Dhanurasana, Bow pose, is a backbending asana in hatha yoga and modern yoga as exercise. "));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.matsyasana, "Matsyasana", "Matsyasana or Fish pose is a reclining back-bending asana in hatha yoga and modern yoga as exercise."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.janu_shirsasana, "Janu Shirsasana", "Head-to-Knee Pose, is a seated twisting and forward bending asana in diverse schools of modern yoga as exercise."));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }

        if(db_influenza.equals("yes")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.adho_mukha_svanasana, "Adho Mukha Svanasana", " It is an inversion asana in modern yoga as exercise, often practised as part of a flowing sequence of poses, especially Surya Namaskar, the Salute to the Sun"));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.bhujangasana, "Bhujangasana", "It is commonly performed in a cycle of asanas in Surya Namaskar as an alternative to Urdhva Mukha Svanasana."));
            yogaHelperLocations.add(new YogaHelperClass(R.drawable.kapal_bhati_pranayama, "Kapal Bhati", "Kapalabhati, also called breath of fire, is an important Shatkarma, a purification in hatha yoga."));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }


        if(db_hypertension.equals("yes")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.paschimottanasana, "Paschimottanasana", "Intense Dorsal Stretch is a seated forward-bending asana in hatha yoga and modern yoga as exercise."));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }


        if(db_diabetes.equals("yes")) {

            yogaHelperLocations.add(new YogaHelperClass(R.drawable.supta_matsyendrasana, "Supta Matsyendrasana", "It improves spinal mobility and can aid digestion."));


            adapter = new YogaAdapter(yogaHelperLocations);
            yogaRecycler.setAdapter(adapter);

        }
    }





    private String getUserId() {
        return userId;
    }

    private void moveToLogin() {

        Intent intent = new Intent(patient_dashboard.this,Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onBackPressed(){
        patient_dashboard.this.finish();
        System.exit(0);
        super.onBackPressed();
    }
}