package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static androidx.core.content.ContextCompat.getSystemService;

public class medicationreminderlist extends AppCompatActivity {


    RecyclerView recyclermrlist;
    RecyclerAdaptermrlist recyclerAdaptermrdata;

    ArrayList<PatientMedicationReminder> mrlist;
    //String userId;
    //DatabaseReference reference;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_medicationreminder);

        setContentView(R.layout.activity_medicationreminderlist);

        SessionManagement sessionManagement = new SessionManagement(medicationreminderlist.this);
        final String userId = sessionManagement.getSession();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mContext = getApplicationContext();



        recyclermrlist = (RecyclerView) findViewById(R.id.recyclermrlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclermrlist.setLayoutManager(layoutManager);
        recyclermrlist.setHasFixedSize(true);
        recyclermrlist.setLayoutManager(new LinearLayoutManager(this));

        mrlist = new ArrayList<PatientMedicationReminder>();
        //reference = FirebaseDatabase.getInstance().getReference();

        ClearAll();


        GetDataFromFirebase(userId, alarmManager, mContext);




        

    }

    public void GetDataFromFirebase(final String userId, final AlarmManager alarmManager, final Context mContext) {
        final PendingIntent[] pendingIntent = new PendingIntent[1000];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("medication").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    PatientMedicationReminder mrData = new PatientMedicationReminder();
                    mrData.setDt(snapshot.child("dt").getValue().toString());
                    mrData.setCal(snapshot.child("cal").getValue().toString());
                    mrData.setMs(snapshot.child("ms").getValue().toString());
                    mrData.setUserId(snapshot.child("userId").getValue().toString());



                    mrlist.add(mrData);

                }
                Log.d("this",mrlist.toString());
                recyclerAdaptermrdata = new RecyclerAdaptermrlist(mContext, mrlist, alarmManager, userId);
                recyclermrlist.setAdapter(recyclerAdaptermrdata);
                recyclerAdaptermrdata.notifyDataSetChanged();
                Arrays.fill(pendingIntent, null);

                medicationreminderalarm mralarm = new medicationreminderalarm(mrlist, alarmManager, userId, mContext, pendingIntent);

                try {
                    mralarm.startAlarm();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                recyclerAdaptermrdata.setOnItemClickListener(new RecyclerAdaptermrlist.OnItemClickListener(){

                    @Override
                    public void onItemdelete(int position) {
                        String ids = mrlist.get(position).getUserId();
                        int mrsize = mrlist.size();
                        mrlist.remove(position);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("medication");
                        ref.child(userId).child(ids).removeValue();
                        recyclerAdaptermrdata.notifyItemRemoved(position);
                        medicationreminderalarm mralarm = new medicationreminderalarm(mrlist, alarmManager, userId, mContext, mrsize);
                        mralarm.cancelAlarm(mrsize);
                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ClearAll() {

        if(mrlist != null){
            mrlist.clear();
            if(recyclerAdaptermrdata != null){
                recyclerAdaptermrdata.notifyDataSetChanged();
            }

        }
        else{
            mrlist = new ArrayList<>();
        }

    }
}