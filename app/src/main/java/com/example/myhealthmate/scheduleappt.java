package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class scheduleappt extends AppCompatActivity {

    Spinner spinner;

    ArrayList<String> namelist;
    ArrayList<String> unamelist;
    ArrayList<String> enamelist;
    String userId, sname, usname, demail, dname, pname;
    ArrayAdapter<String> adapter;

    private DatePickerDialog datePickerDialog;
    private TextView dateButton;
    private TextView timeButton;
    private Button addbtn;
    EditText reason;
    int hour, minute;
    public String date,time,reasonstr, ename;
    String flag = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scheduleappt);

        initDatePicker();

        SessionManagement sessionManagement = new SessionManagement(scheduleappt.this);
        userId = sessionManagement.getSession();


        spinner = (Spinner) findViewById(R.id.spinner);
        namelist = new ArrayList<String>();
        unamelist = new ArrayList<String>();
        enamelist = new ArrayList<String>();



        dateButton = findViewById(R.id.date_appointment);

        timeButton = findViewById(R.id.time_appointment);
        reason = findViewById(R.id.reason_tv);
        addbtn = findViewById(R.id.schedule_appointment_btn);



        adapter = new ArrayAdapter<String>(scheduleappt.this, android.R.layout.simple_spinner_dropdown_item,namelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sname = "Select Patient Name";
                    flag = "0";

                } else {
                    sname = String.valueOf(parent.getItemIdAtPosition(position));

                    Log.d("this", sname);
                    usname = unamelist.get(position - 1);
                    ename = enamelist.get(position - 1);
                    Log.d("this", usname);
                    Log.d("this", unamelist.toString());
                    pname = namelist.get(position);
                    flag = "1";



                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        DatabaseReference refd = FirebaseDatabase.getInstance().getReference("doctors").child(userId);
        refd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dname = dataSnapshot.child("dName").getValue().toString();
                demail = dataSnapshot.child("dEmail").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("patients");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                namelist.clear();
                namelist.add("Select Patient Name");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String ds = snapshot.child("dcode").getValue().toString();
                    Log.d("this",ds);

                    if(ds.equals(userId)){

                        namelist.add(snapshot.child("pName").getValue().toString());
                        unamelist.add(snapshot.child("pUsername").getValue().toString());
                        enamelist.add(snapshot.child("pEmail").getValue().toString());
                    }


                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonstr = reason.getText().toString();
                date = dateButton.getText().toString();
                time = timeButton.getText().toString();

                if((validatepatients() && validatedate() && validatetime() && validatedetails()) == true){
                    String mssgc= "Dear " + pname + ", " +"An Appointment is Schedule by your Doctor at Date:- " + date + " Time:- " + time + " Please look the details " + reasonstr;

                    Log.d("this", mssgc);


                    OkHttpClient client = new OkHttpClient();

                    RequestBody formBody = new FormBody.Builder().add("value1", ename).add("value2", mssgc).add("value3","Appointment").build();

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


                    String message = "Dear Doctor " + dname + ", " +
                            "You have Schedule an Appointment with your patient named " + pname + " on Date:- " + date + " Time:- " + time + " Details mentioned " + reasonstr;

                    OkHttpClient client1 = new OkHttpClient();

                    RequestBody formBody1 = new FormBody.Builder().add("value1", demail).add("value2", message).add("value3","Appointment").build();

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



                    Toast.makeText(getApplicationContext(), "Appointment Notification Sent", Toast.LENGTH_SHORT).show();
                    reason.setText("");
                    dateButton.setText("Date");
                    timeButton.setText("Time");
                    flag = "0";
                    spinner.setAdapter(adapter);


                }
            }
        });




        }

    private void initDatePicker() {


        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        dateButton.setTextColor(Color.parseColor("#000000"));
        return  day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                String mins = String.valueOf(selectedMinute);
                if(mins.length() < 2){
                    mins = "0" + mins;
                }



                String t = "";
                String am = "am";
                if(hour > 12){
                    am = "pm";
                    t += String.valueOf(hour - 12) + ":" + mins + " " + am;
                }
                else if(hour == 00){
                    t += String.valueOf(12) + ":" + mins + " " + am;

                }

                else if(hour == 12){
                    am = "pm";
                    t += String.valueOf(hour) + ":" + mins + " " + am;
                }

                else{
                    t += String.valueOf(hour) + ":" + mins + " " + am;

                }

                timeButton.setTextColor(Color.parseColor("#000000"));

                timeButton.setText(t);
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private boolean validatedetails() {
        String val = reason.getText().toString();
        if(val.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter the Details", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }


    private boolean validatedate() {
        String val = dateButton.getText().toString();
        if(val.equals("Date")){
            Toast.makeText(getApplicationContext(), "Select the Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

    private boolean validatetime() {
        String val = timeButton.getText().toString();
        if(val.equals("Time")){
            Toast.makeText(getApplicationContext(), "Select the Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

    private boolean validatepatients() {

        if(flag == "0"){
            Toast.makeText(getApplicationContext(), "Select the Patient", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

}



