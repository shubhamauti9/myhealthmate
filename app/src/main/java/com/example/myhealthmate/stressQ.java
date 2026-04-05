package com.example.myhealthmate;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.util.Log;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.material.slider.Slider;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import org.jetbrains.annotations.NotNull;
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


public class stressQ extends AppCompatActivity {


    Slider nervousness, control, worry, relaxation, restlessness, irritability, fear;
    TextView ans_t, nervousness_t, control_t, worry_t, relaxation_t, restlessness_t, irritability_t, fear_t;
    Button stressanalysis_btn;
    String[] a = new String[200];
    String[] result = new String[20];
    String w, y;
    String dcode, pname, demail;
    int x;
    String userId;





    public static String num_to_string(int v) {
        if (v == 3) {
            return ("Severe");
        } else if (v == 1) {
            return ("Mild");
        } else if (v == 2) {
            return ("Moderate");
        } else {
            return ("Not Present");
        }


    }


    public static Integer string_to_num(String v) {
        if (v == "Severe") {
            return (3);
        } else if (v == "Mild") {
            return (1);
        } else if (v == "Moderate") {
            return (2);
        } else {
            return (0);
        }


    }


    public static String nto4(int v) {
        if (v == 0) {
            return ("1,0,0,0");
        } else if (v == 1) {
            return ("0,1,0,0");
        } else if (v == 2) {
            return ("0,0,1,0");
        } else {
            return ("0,0,0,1");
        }


    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stress_q);


        SessionManagement sessionManagement = new SessionManagement(stressQ.this);
        userId = sessionManagement.getSession();

        final LoadingDialog loadingDialog = new LoadingDialog(stressQ.this);


        nervousness = findViewById(R.id.nervousness);
        nervousness_t = findViewById(R.id.nervousnesst);
        nervousness_t.setText(num_to_string((int) nervousness.getValue()));
        nervousness.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                nervousness_t.setText(num_to_string((int) value));

            }
        });


        control = findViewById(R.id.control);
        control_t = findViewById(R.id.controlt);
        control_t.setText(num_to_string((int) control.getValue()));
        control.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                control_t.setText(num_to_string((int) value));

            }
        });


        worry = findViewById(R.id.worry);
        worry_t = findViewById(R.id.worryt);
        worry_t.setText(num_to_string((int) worry.getValue()));
        worry.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                worry_t.setText(num_to_string((int) value));

            }
        });

        relaxation = findViewById(R.id.relaxation);
        relaxation_t = findViewById(R.id.relaxationt);
        relaxation_t.setText(num_to_string((int) relaxation.getValue()));
        relaxation.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                relaxation_t.setText(num_to_string((int) value));

            }
        });

        restlessness = findViewById(R.id.restlessness);
        restlessness_t = findViewById(R.id.restlessnesst);
        restlessness_t.setText(num_to_string((int) restlessness.getValue()));
        restlessness.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                restlessness_t.setText(num_to_string((int) value));

            }
        });


        irritability = findViewById(R.id.irritability);
        irritability_t = findViewById(R.id.irritabilityt);
        irritability_t.setText(num_to_string((int) irritability.getValue()));
        irritability.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                irritability_t.setText(num_to_string((int) value));

            }
        });


        fear = findViewById(R.id.fear);
        fear_t = findViewById(R.id.feart);
        fear_t.setText(num_to_string((int) fear.getValue()));
        fear.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

                fear_t.setText(num_to_string((int) value));

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




        ans_t = (TextView) findViewById(R.id.ans);
        stressanalysis_btn = (Button) findViewById(R.id.stressanalysisbtn);

        stressanalysis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissdialog();
                    }
                }, 3000);

                a[0] = nto4(string_to_num((String) nervousness_t.getText()));
                a[1] = nto4(string_to_num((String) control_t.getText()));
                a[2] = nto4(string_to_num((String) worry_t.getText()));
                a[3] = nto4(string_to_num((String) relaxation_t.getText()));
                a[4] = nto4(string_to_num((String) restlessness_t.getText()));
                a[5] = nto4(string_to_num((String) irritability_t.getText()));
                a[6] = nto4(string_to_num((String) fear_t.getText()));
                w = a[0] + "," + a[1] + "," + a[2] + "," + a[3] + "," + a[4] + "," + a[5] + "," + a[6];

                result[0] = ((String) nervousness_t.getText());
                result[1] = ((String) control_t.getText());
                result[2] = ((String) worry_t.getText());
                result[3] = ((String) relaxation_t.getText());
                result[4] = ((String) restlessness_t.getText());
                result[5] = ((String) irritability_t.getText());
                result[6] = ((String) fear_t.getText());






                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder().add("value",w).build();

                Request request = new Request.Builder().url("http://aspm2700.pythonanywhere.com/stress").post(formBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("this", "not found");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        y = response.body().string();
                        x = Integer.parseInt(y);
                        Log.d("this",y);
                        String res = num_to_string(x);
                        ans_t.setText(num_to_string(x));
                        insertData(result, res, dcode);
                        if(!dcode.equals("1")) {
                            sendingmail();
                        }


                    }

                    public void sendingmail () {



                        String mssgc= "Stress Assessment is taken by your patient named " + pname;

                        Log.d("this", mssgc);
                        Log.d("this", demail);

                        OkHttpClient client = new OkHttpClient();

                        RequestBody formBody = new FormBody.Builder().add("value1", demail).add("value2", mssgc).add("value3","Stress Assessment").build();

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

                    private void insertData(final String[] result, final String res, final String dcode) {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH:mm:ss", Locale.getDefault());
                        final String currentDateandTime = sdf.format(new Date());
                        Log.d("this",currentDateandTime.toString());

                        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
                        final DatabaseReference ref = rootnode.getReference().child("stress").child(userId);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String s_date = currentDateandTime;
                                String s_nervousness = result[0];
                                String s_control = result[1];
                                String s_worry = result[2];
                                String s_relaxation = result[3];
                                String s_restlessness = result[4];
                                String s_irritability = result[5];
                                String s_fear = result[6];
                                String s_dcode = dcode;
                                String s_result = res;


                                StressData s_data = new StressData(s_date, s_nervousness, s_control, s_worry, s_relaxation, s_restlessness, s_irritability, s_fear, s_dcode, s_result);

                                ref.child(currentDateandTime).setValue(s_data);

                                DatabaseReference rfyoga = FirebaseDatabase.getInstance().getReference("yoga").child(userId);
                                rfyoga.child("stress").setValue(s_result);


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

        Intent intent = new Intent(stressQ.this, patient_dashboard.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }





}






