package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class view_report extends AppCompatActivity {
    ListView listView;
    RecyclerView recyclerView;
    ArrayList<StressData> stressDatalist;
    private RecyclerAdapter recyclerAdapter;

    RecyclerView recyclerViewiz;
    ArrayList<InfluenzaData> izDatalist;
    private RecyclerAdapterIz recyclerAdapterIz;
    private Context mContext;
    DatabaseReference reference;
    DatabaseReference referenceiz;

    RecyclerView recyclerViewcd;
    ArrayList<CovidData> cdDatalist;
    private RecyclerAdaptercd recyclerAdaptercd;
    DatabaseReference referencecd;

    TextView recent_reports_tv;

    TextView bmino, bmicat, name_tv_main;

    LinearLayout iz, stress, covid;

    String userId, bmic, bmiinds;
    ImageView bmilogo;
    RelativeLayout bmicard;

    float wkg, hm, bmiind;
    TextView heightLabel,WeightLabel,ageLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_report);

        SessionManagement sessionManagement = new SessionManagement(view_report.this);
        userId = sessionManagement.getSession();


        iz = (LinearLayout) findViewById(R.id.influenza_tv);
        stress = (LinearLayout) findViewById(R.id.stress_tv);
        covid = (LinearLayout) findViewById(R.id.covid_tv);

        bmicard = (RelativeLayout) findViewById(R.id.bmi_layout);
        bmilogo = (ImageView) findViewById(R.id.bmi_logo);
        bmino = (TextView) findViewById(R.id.bmi_no);
        bmicat = (TextView) findViewById(R.id.bmi_cat);
        name_tv_main = (TextView) findViewById(R.id.name_tv_main);
        recent_reports_tv = (TextView) findViewById(R.id.recent_reports_tv);

        ageLabel = findViewById(R.id.age_no);

        heightLabel = findViewById(R.id.height_no);

        WeightLabel = findViewById(R.id.weight_no);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference rn = FirebaseDatabase.getInstance().getReference("patients").child(userId);
        rn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name_tv_main.setText(dataSnapshot.child("pName").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        stressDatalist = new ArrayList<>();

        recyclerViewiz = (RecyclerView) findViewById(R.id.recyclerViewiz);
        LinearLayoutManager layoutManageriz = new LinearLayoutManager(this);
        recyclerViewiz.setLayoutManager(layoutManageriz);
        recyclerViewiz.setHasFixedSize(true);
        recyclerViewiz.setLayoutManager(new LinearLayoutManager(this));

        izDatalist = new ArrayList<>();


        recyclerViewcd = (RecyclerView) findViewById(R.id.recyclerViewcd);
        LinearLayoutManager layoutManagercd = new LinearLayoutManager(this);
        recyclerViewcd.setLayoutManager(layoutManagercd);
        recyclerViewcd.setHasFixedSize(true);
        recyclerViewcd.setLayoutManager(new LinearLayoutManager(this));

        cdDatalist = new ArrayList<>();

        DatabaseReference rfph = FirebaseDatabase.getInstance().getReference("patients_healthprofile").child(userId);
        rfph.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                String w = dataSnapshot.child("ph_weight").getValue().toString();
                Log.d("this",w);
                String h = dataSnapshot.child("ph_height").getValue().toString();
                Log.d("this",h);

                WeightLabel.setText(w);
                heightLabel.setText(h);


                wkg = Float.parseFloat(w);
                hm = Float.parseFloat(h);
                hm = hm / 100;

                bmiind = wkg / (hm * hm);
                DecimalFormat df = new DecimalFormat("0.00");
                bmiinds = df.format(bmiind);



                Log.d("this", bmiinds);
                if (Float.parseFloat(bmiinds) < 18.5) {
                    bmic = "UnderWeight";
                    bmino.setText(bmiinds);
                    bmicat.setText(bmic);
                    bmilogo.setBackgroundResource(R.drawable.underweight);
                    bmicard.setBackgroundColor(Color.parseColor("#89A1DD"));

                } else if (Float.parseFloat(bmiinds) >= 18.5 && Float.parseFloat(bmiinds) <= 24.9) {
                    bmic = "Healthy";
                    bmino.setText(bmiinds);
                    bmicat.setText(bmic);
                    bmilogo.setBackgroundResource(R.drawable.healthyperson);
                    bmicard.setBackgroundColor(Color.parseColor("#43EC49"));

                } else if (Float.parseFloat(bmiinds) >= 25 && Float.parseFloat(bmiinds) <= 29.9) {
                    bmic = "OverWeight";
                    bmino.setText(bmiinds);
                    bmicat.setText(bmic);
                    bmilogo.setBackgroundResource(R.drawable.overweight);
                    bmicard.setBackgroundColor(Color.parseColor("#F46350"));

                } else if (Float.parseFloat(bmiinds) >= 30 && Float.parseFloat(bmiinds) <= 39.9) {
                    bmic = "Obese";
                    bmino.setText(bmiinds);
                    bmicat.setText(bmic);
                    bmilogo.setBackgroundResource(R.drawable.obese);
                    bmicard.setBackgroundColor(Color.parseColor("#E50623"));

                } else {
                    bmic = "Severely Obese";
                    bmino.setText(bmiinds);
                    bmicat.setText(bmic);
                    bmilogo.setBackgroundResource(R.drawable.severelyobese);
                    bmicard.setBackgroundColor(Color.parseColor("#FF0000"));
                }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference rfphp = FirebaseDatabase.getInstance().getReference("patients").child(userId);
        rfphp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String page = dataSnapshot.child("pAge").getValue().toString();
                int ageyrs = Integer.parseInt(page.substring(6));
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int ageresult = year - ageyrs;
                ageLabel.setText(String.valueOf(ageresult));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









        reference = FirebaseDatabase.getInstance().getReference();
        referenceiz = FirebaseDatabase.getInstance().getReference();
        referencecd = FirebaseDatabase.getInstance().getReference();

        stress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClearAll();
                ClearAllIz();
                ClearAllcd();


                GetDataFromFirebase();
            }
        });


        iz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClearAllIz();
                ClearAll();
                ClearAllcd();
                GetDataFromFirebaseIz();
            }
        });

        covid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearAllIz();
                ClearAll();
                ClearAllcd();
                GetDataFromFirebasecd();
            }


        });




    }

    private void ClearAllcd() {
        if(cdDatalist != null){
            cdDatalist.clear();
            if(recyclerAdaptercd != null){
                recyclerAdaptercd.notifyDataSetChanged();
            }

        }
        else{
            cdDatalist = new ArrayList<>();
        }
    }

    private void GetDataFromFirebasecd() {

        Query query = referencecd.child("covid").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAllcd();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        recent_reports_tv.setText("COVID-19 Reports");
                        CovidData cdData = new CovidData();
                        cdData.setCd_date(snapshot.child("cd_date").getValue().toString());
                        cdData.setCd_Fever(snapshot.child("cd_Fever").getValue().toString());
                        cdData.setCd_Drycough(snapshot.child("cd_Drycough").getValue().toString());
                        cdData.setCd_Sorethroat(snapshot.child("cd_Sorethroat").getValue().toString());
                        cdData.setCd_Runnynose(snapshot.child("cd_Runnynose").getValue().toString());
                        cdData.setCd_Headache(snapshot.child("cd_Headache").getValue().toString());
                        cdData.setCd_Fatigue(snapshot.child("cd_Fatigue").getValue().toString());
                        cdData.setCd_Breathingproblem(snapshot.child("cd_Breathingproblem").getValue().toString());
                        cdData.setCd_Chroniclungdisease(snapshot.child("cd_Chroniclungdisease").getValue().toString());
                        cdData.setCd_Heartdisease(snapshot.child("cd_Heartdisease").getValue().toString());
                        cdData.setCd_Diabetes(snapshot.child("cd_Diabetes").getValue().toString());
                        cdData.setCd_Hypertension(snapshot.child("cd_Hypertension").getValue().toString());
                        cdData.setCd_Stomachache(snapshot.child("cd_Stomachache").getValue().toString());
                        cdData.setCd_Abroad(snapshot.child("cd_Abroad").getValue().toString());
                        cdData.setCd_Contact(snapshot.child("cd_Contact").getValue().toString());
                        cdData.setCd_largegathering(snapshot.child("cd_largegathering").getValue().toString());
                        cdData.setCd_publicplacevisit(snapshot.child("cd_publicplacevisit").getValue().toString());
                        cdData.setCd_familyworking(snapshot.child("cd_familyworking").getValue().toString());


                        cdData.setCd_result(snapshot.child("cd_result").getValue().toString());
                        cdDatalist.add(cdData);

                    }

                    recyclerAdaptercd = new RecyclerAdaptercd(getApplicationContext(), cdDatalist);
                    recyclerViewiz.setAdapter(recyclerAdaptercd);
                    recyclerAdaptercd.notifyDataSetChanged();
                }
                else{
                    recent_reports_tv.setText("No Reports");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void GetDataFromFirebaseIz() {

        Query query = referenceiz.child("influenza").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAllIz();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        recent_reports_tv.setText("Influenza Reports");
                        InfluenzaData izData = new InfluenzaData();
                        izData.setIz_date(snapshot.child("iz_date").getValue().toString());
                        izData.setIz_Age(snapshot.child("iz_Age").getValue().toString());
                        izData.setIz_Cough(snapshot.child("iz_Cough").getValue().toString());
                        izData.setIz_Fatigue(snapshot.child("iz_Fatigue").getValue().toString());
                        izData.setIz_Fever(snapshot.child("iz_Fever").getValue().toString());
                        izData.setIz_Headache(snapshot.child("iz_Headache").getValue().toString());
                        izData.setIz_Medicalcond(snapshot.child("iz_Medicalcond").getValue().toString());
                        izData.setIz_Myalgia(snapshot.child("iz_Myalgia").getValue().toString());
                        izData.setIz_Runnynose(snapshot.child("iz_Runnynose").getValue().toString());
                        izData.setIz_Throatache(snapshot.child("iz_Throatache").getValue().toString());
                        izData.setIz_Vomiting(snapshot.child("iz_Vomiting").getValue().toString());
                        izData.setIz_result(snapshot.child("iz_result").getValue().toString());
                        izDatalist.add(izData);

                    }

                    recyclerAdapterIz = new RecyclerAdapterIz(getApplicationContext(), izDatalist);
                    recyclerViewiz.setAdapter(recyclerAdapterIz);
                    recyclerAdapterIz.notifyDataSetChanged();
                }
                else{
                    recent_reports_tv.setText("No Reports");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void GetDataFromFirebase() {

        Query query = reference.child("stress").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        recent_reports_tv.setText("Stress Reports");
                        StressData stressData = new StressData();
                        stressData.setS_date(snapshot.child("s_date").getValue().toString());
                        stressData.setS_nervousness(snapshot.child("s_nervousness").getValue().toString());
                        stressData.setS_control(snapshot.child("s_control").getValue().toString());
                        stressData.setS_worry(snapshot.child("s_worry").getValue().toString());
                        stressData.setS_relaxation(snapshot.child("s_relaxation").getValue().toString());
                        stressData.setS_restlessness(snapshot.child("s_restlessness").getValue().toString());
                        stressData.setS_irritability(snapshot.child("s_irritability").getValue().toString());
                        stressData.setS_fear(snapshot.child("s_fear").getValue().toString());
                        stressData.setS_result(snapshot.child("s_result").getValue().toString());
                        stressDatalist.add(stressData);

                    }

                    recyclerAdapter = new RecyclerAdapter(getApplicationContext(), stressDatalist);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();

                }
                else {
                    recent_reports_tv.setText("No Reports");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void ClearAll(){
        if(stressDatalist != null){
            stressDatalist.clear();
            if(recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }

        }
        else{
            stressDatalist = new ArrayList<>();
        }
    }


    private void ClearAllIz(){
        if(izDatalist != null){
            izDatalist.clear();
            if(recyclerAdapterIz != null){
                recyclerAdapterIz.notifyDataSetChanged();
            }

        }
        else{
            izDatalist = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed(){
        //Log.d(String.valueOf(getApplicationContext()), "back button");
        //Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(view_report.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }


}

