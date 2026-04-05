package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class medicationreminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    EditText editTextTitle, editTextMessage;
    Button buttonChannel1, button;
    Button buttonCancelAlarm, buttonTimePicker, add, list;
    private NotificationHelper mNotificationHelper;
    ArrayList<Calendar> c1 = new ArrayList<Calendar>();
    Calendar [] cal = new Calendar[5];
    TextView textView;
    TextView m;
    String time;



    CheckBox everyDay;

    CheckBox dvSunday;

    CheckBox dvMonday;


    CheckBox dvTuesday;


    CheckBox dvWednesday;


    CheckBox dvThursday;


    CheckBox dvFriday;


    CheckBox dvSaturday;

    LinearLayout checkboxLayout;

    //ArrayList<PendingIntent> pendingIntent = new ArrayList<>();
    ArrayList<PatientMedicationReminder> md;

    final ArrayList<String> day_db = new ArrayList<String>();
    final ArrayList<String> time_db = new ArrayList<String>();

    int flags = 0;





    private List<String> doseUnitList;
    boolean [] dayOfWeekList = new boolean[8];
    ArrayList<Integer> dc = new ArrayList<>();
    ArrayList<ArrayList<Integer>> dcl = new ArrayList<>();



    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_medicationreminder);
        SessionManagement sessionManagement = new SessionManagement(medicationreminder.this);
        userId = sessionManagement.getSession();



        buttonTimePicker = (Button) findViewById(R.id.button_timepicker);
        m = (TextView) findViewById(R.id.medicine);
        add = (Button) findViewById(R.id.add);
        list = (Button) findViewById(R.id.list);
        //textView = (TextView) findViewById(R.id.textview);

        everyDay = (CheckBox) findViewById(R.id.every_day);
        dvSunday = (CheckBox) findViewById(R.id.dv_sunday);
        dvMonday = (CheckBox) findViewById(R.id.dv_monday);
        dvTuesday = (CheckBox) findViewById(R.id.dv_tuesday);
        dvWednesday = (CheckBox) findViewById(R.id.dv_wednesday);
        dvThursday = (CheckBox) findViewById(R.id.dv_thursday);
        dvFriday = (CheckBox) findViewById(R.id.dv_friday);
        dvSaturday = (CheckBox) findViewById(R.id.dv_saturday);

        //add = (Button) findViewById(R.id.button_add);



        mNotificationHelper = new NotificationHelper(this);






        for(int j = 0; j < 8; j++){
            dc.add(0);
        }




        list = (Button) findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(medicationreminder.this, medicationreminderlist.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);


            }
        });

        everyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(everyDay.isChecked()){

                    dayOfWeekList[7] = true;
                    dvSunday.setChecked(true);
                    dvMonday.setChecked(true);
                    dvTuesday.setChecked(true);
                    dvWednesday.setChecked(true);
                    dvThursday.setChecked(true);
                    dvFriday.setChecked(true);
                    dvSaturday.setChecked(true);
                    dc.set(7,1);
                    for(int j=0;j<7;j++){
                        dc.set(j,1);
                    }



                }
                else{
                    dayOfWeekList[7] = false;
                    /*dvSunday.setChecked(false);
                    dvMonday.setChecked(false);
                    dvTuesday.setChecked(false);
                    dvWednesday.setChecked(false);
                    dvThursday.setChecked(false);
                    dvFriday.setChecked(false);
                    dvSaturday.setChecked(false);*/
                    dc.set(7,0);
                    for(int j=0;j<7;j++){
                        dc.set(j,0);
                    }
                }

                if(!everyDay.isChecked()){
                    dayOfWeekList[7] = false;
                    dvSunday.setChecked(false);
                    dvMonday.setChecked(false);
                    dvTuesday.setChecked(false);
                    dvWednesday.setChecked(false);
                    dvThursday.setChecked(false);
                    dvFriday.setChecked(false);
                    dvSaturday.setChecked(false);
                    dc.set(7,0);
                    for(int j=0;j<7;j++){
                        dc.set(j,0);
                    }


                }
            }
        });


        dvSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvSunday.isChecked()){
                    dayOfWeekList[0] = true;
                    dc.set(0,1);


                }
                else{
                    dayOfWeekList[0] = false;
                    dc.set(0,0);
                    if(!dvSunday.isChecked()){
                        dayOfWeekList[7] = false;
                        dc.set(7,0);
                        everyDay.setChecked(false);
                        dayOfWeekList[0] = false;
                        dc.set(0,0);

                    }

                }
            }
        });



        dvMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvMonday.isChecked()){
                    dayOfWeekList[1] = true;
                    dc.set(1,1);


                }
                else{
                    dayOfWeekList[1] = false;
                    dc.set(1,0);
                    if(!dvMonday.isChecked()){
                        dayOfWeekList[7] = false;
                        dc.set(7,0);
                        everyDay.setChecked(false);
                        dayOfWeekList[1] = false;
                        dc.set(1,0);

                    }

                }
            }
        });


        dvTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvTuesday.isChecked()){

                    dayOfWeekList[2] = true;
                    dc.set(2,1);


                }
                else{
                    dayOfWeekList[2] = false;
                    dc.set(2,0);
                    if(!dvTuesday.isChecked()){
                        dayOfWeekList[7] = false;
                        dc.set(7,0);
                        everyDay.setChecked(false);
                        dayOfWeekList[2] = false;
                        dc.set(2,0);

                    }

                }
            }
        });

        dvWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvWednesday.isChecked()){
                    dayOfWeekList[3] = true;
                    dc.set(3,1);


                }
                else{
                    dayOfWeekList[3] = false;
                    dc.set(3,0);
                    if(!dvWednesday.isChecked()){
                        dayOfWeekList[7] = false;
                        dc.set(7,0);
                        everyDay.setChecked(false);
                        dayOfWeekList[3] = false;
                        dc.set(3,0);


                    }

                }
            }
        });

        dvThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvThursday.isChecked()){
                    dayOfWeekList[4] = true;
                    dc.set(4,1);
                }
                else{
                    dayOfWeekList[4] = false;
                    dc.set(4,0);
                    if(!dvThursday.isChecked()){
                        dayOfWeekList[7] = false;
                        dc.set(7,0);
                        everyDay.setChecked(false);
                        dayOfWeekList[4] = false;
                        dc.set(4,0);

                    }
                }
            }
        });


        dvFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvFriday.isChecked()){
                    dayOfWeekList[5] = true;
                    dc.set(5,1);

                }
                else{
                    dayOfWeekList[5] = false;
                    dc.set(5,0);
                    if(!dvFriday.isChecked()){
                        dayOfWeekList[7] = false;
                        dc.set(7,0);
                        everyDay.setChecked(false);
                        dayOfWeekList[5] = false;
                        dc.set(5,0);

                    }
                }
            }
        });

        dvSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvSaturday.isChecked()){
                    dayOfWeekList[6] = true;
                    dc.set(6,1);

                }
                else{
                    dayOfWeekList[6] = false;
                    dc.set(6,0);
                    if(!dvSaturday.isChecked()){
                        dayOfWeekList[7] = false;
                        dc.set(7,0);
                        everyDay.setChecked(false);
                        dayOfWeekList[6] = false;
                        dc.set(6,0);

                    }
                }
            }
        });







        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });

        if(flags == 0){
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(everyDay.isChecked() == true || dvSunday.isChecked() == true || dvMonday.isChecked() == true || dvTuesday.isChecked() == true || dvWednesday.isChecked() == true || dvThursday.isChecked() == true || dvFriday.isChecked() == true || dvSaturday.isChecked() == true) {
                        if(!buttonTimePicker.getText().toString().equals("Open Time Picker")){
                            if (!m.getText().toString().equals("")) {

                            } else {
                                Toast.makeText(medicationreminder.this, "Medicine name can't be empty", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
                            Toast.makeText(medicationreminder.this, "Please select the time", Toast.LENGTH_SHORT).show();

                        }

                    }
                    else{
                        Toast.makeText(medicationreminder.this, "Please select medicine days", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }








    }

    public void sendOnChannel1(String title, String message){
        /*NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(title, message);
        mNotificationHelper.getManager().notify(1, nb.build());*/


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        final Calendar c = Calendar.getInstance();
        if(dc.get(7) == 1){
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

            c1.add(c);
            dcl.add(dc);
            time = updateTimeText(c);


            flags = 1;

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(everyDay.isChecked() == true || dvSunday.isChecked() == true || dvMonday.isChecked() == true || dvTuesday.isChecked() == true || dvWednesday.isChecked() == true || dvThursday.isChecked() == true || dvFriday.isChecked() == true || dvSaturday.isChecked() == true){
                        if(!buttonTimePicker.getText().toString().equals("Open Time Picker")){
                            if(!m.getText().toString().equals("")){
                                addAlarm(dc, time);
                                everyDay.setChecked(false);
                                dvSunday.setChecked(false);
                                dvMonday.setChecked(false);
                                dvTuesday.setChecked(false);
                                dvWednesday.setChecked(false);
                                dvThursday.setChecked(false);
                                dvFriday.setChecked(false);
                                dvSaturday.setChecked(false);
                                buttonTimePicker.setText("Open Time Picker");


                            }
                            else{
                                Toast.makeText(medicationreminder.this, "Medicine name can't be empty", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
                            Toast.makeText(medicationreminder.this, "Please select the time", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Toast.makeText(medicationreminder.this, "Please select Medicine days", Toast.LENGTH_SHORT).show();

                    }



                }
            });


        }

        else{
            Calendar cc = Calendar.getInstance();
            cc.getTime();
            Log.d("this",cc.getTime().toString());



                int dayy = cc.get(Calendar.DAY_OF_WEEK) - 1;
                Log.d("this", String.valueOf(dayy));
                int fl = 0;
                while (dc.get(dayy) != 1) {
                   dayy = (dayy + 1) % 7;
                   fl = 1;
                }
                if(fl == 1){
                    dayy += 1;
                }
                else{
                    dayy = 0;
                }
                cc.add(Calendar.DATE, dayy);

                c.add(Calendar.DATE, dayy);



            Log.d("this",cc.getTime().toString());
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

            c1.add(c);
            dcl.add(dc);
            time = updateTimeText(c);


            flags = 1;

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(everyDay.isChecked() == true || dvSunday.isChecked() == true || dvMonday.isChecked() == true || dvTuesday.isChecked() == true || dvWednesday.isChecked() == true || dvThursday.isChecked() == true || dvFriday.isChecked() == true || dvSaturday.isChecked() == true){
                        if(!buttonTimePicker.getText().toString().equals("Open Time Picker")){
                            if(!m.getText().toString().equals("")){
                                addAlarm(dc, time);
                                everyDay.setChecked(false);
                                dvSunday.setChecked(false);
                                dvMonday.setChecked(false);
                                dvTuesday.setChecked(false);
                                dvWednesday.setChecked(false);
                                dvThursday.setChecked(false);
                                dvFriday.setChecked(false);
                                dvSaturday.setChecked(false);
                                buttonTimePicker.setText("Open Time Picker");


                            }
                            else{
                                Toast.makeText(medicationreminder.this, "Medicine name can't be empty", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
                            Toast.makeText(medicationreminder.this, "Please select the time", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Toast.makeText(medicationreminder.this, "Please select Medicine days", Toast.LENGTH_SHORT).show();

                    }



                }
            });



        }









    }

    private String updateTimeText(Calendar c) {

        String timeText = "";
        /*for (int i=0;i<c.size();i++){
            timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.get(i).getTime());
            buttonTimePicker.setText(timeText);
        }*/

        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        buttonTimePicker.setText(timeText);
        return c.getTime().toString();


    }




    private void addAlarm(final ArrayList d, final String cal){
        /*AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        textView.setText("Alarm Cancelled");*/



        final String ms = m.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference().child("medication");




        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                StringBuffer sb = new StringBuffer();

                for (Object s1 : d) {
                    sb.append(s1);
                }

                String dt = sb.toString();
                String ids = "";
                String id = String.valueOf(reference.child(userId).push());
                Log.d("this",id.toString());
                int x = userId.length()+1;
                System.out.println(userId+"/");

                int k = id.indexOf(userId+"/");
                ids += id.substring(k+x);

                Log.d("this",ids.toString());

                PatientMedicationReminder ph_data = new PatientMedicationReminder(cal, ms, ids, dt);
                reference.child(userId).child(ids).setValue(ph_data);
                m.setText("");
                Toast.makeText(medicationreminder.this, "Reminder Set", Toast.LENGTH_SHORT).show();
                for(int j = 0; j < 8; j++) {
                    dc.set(j, 0);
                }
                Intent intent2 = new Intent(medicationreminder.this, medicationreminderlist.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){


            }
        });





    }






}