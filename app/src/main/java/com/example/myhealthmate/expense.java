package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class expense extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private TextView dateButton;
    TextView name;
    EditText details, amount;
    private Button addbtn, listbtn;
    String details_str, date, amount_str;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_expense);
        initDatePicker();

        SessionManagement sessionManagement = new SessionManagement(expense.this);
        userId = sessionManagement.getSession();

        dateButton = (TextView) findViewById(R.id.date_appointment);
        name = (TextView) findViewById(R.id.name_tv_main_SA);
        details = (EditText) findViewById(R.id.detail_tv);
        amount = (EditText) findViewById(R.id.amount_tv);
        addbtn = (Button) findViewById(R.id.add_expense_btn);
        listbtn = (Button) findViewById(R.id.add_expense_btn2);


        final DatabaseReference refn = FirebaseDatabase.getInstance().getReference("patients").child(userId);
        refn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child("pName").getValue().toString();
                name.setText(n);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(expense.this, expensev.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details_str = details.getText().toString();
                date = dateButton.getText().toString();
                amount_str = amount.getText().toString();
                Log.d("this", details_str);
                Log.d("this", date);
                Log.d("this", amount_str);


                final DatabaseReference refp = FirebaseDatabase.getInstance().getReference("expense");
                refp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((validatedetails() && validateamount() && validatedate()) == true){




                            String ids = "";
                            String id = String.valueOf(refp.child(userId).push());
                            Log.d("this",id.toString());
                            int x = userId.length()+1;
                            System.out.println(userId+"/");

                            int k = id.indexOf(userId+"/");
                            ids += id.substring(k+x);

                            ExpenseData expenseData = new ExpenseData(details_str, date, amount_str, ids);

                            refp.child(userId).child(ids).setValue(expenseData);

                            Toast.makeText(getApplicationContext(), "Expense Added", Toast.LENGTH_SHORT).show();



                            Intent intent = new Intent(expense.this, expensev.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            amount.setText("");
                            details.setText("");
                            dateButton.setText("Date");






                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
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

    private String makeDateString(int day, int month, int year) {
        dateButton.setText(getMonthFormat (month) + " " + day + " " + year);
        dateButton.setTextColor(Color.parseColor("#000000"));
        return   day + " " + getMonthFormat (month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private boolean validatedetails() {
        String val = details.getText().toString();
        if(val.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter Expense Detail", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

    private boolean validateamount() {
        String val = amount.getText().toString();
        if(val.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter the Amount", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed(){
        //Log.d(String.valueOf(getApplicationContext()), "back button");
        //Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(expense.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }
}