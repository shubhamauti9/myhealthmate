package com.example.myhealthmate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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

public class medicationreminderalarm {


    ArrayList<PatientMedicationReminder> md;


    PendingIntent[] pendingIntent = new PendingIntent[1000];

    AlarmManager alarmManager;
    String userId;
    Context mContext;
    int mdsize;

    public medicationreminderalarm(ArrayList<PatientMedicationReminder> md, AlarmManager alarmManager, String userId, Context mContext, PendingIntent[] pendingIntent) {
        this.md = md;
        this.alarmManager = alarmManager;
        this.userId = userId;
        this.mContext = mContext;
        this.pendingIntent = pendingIntent;
    }

    public medicationreminderalarm(ArrayList<PatientMedicationReminder> md, AlarmManager alarmManager, String userId, Context mContext, int mdsize) {
        this.md = md;
        this.alarmManager = alarmManager;
        this.userId = userId;
        this.mContext = mContext;
        this.mdsize = mdsize;

    }

    public void cancelAlarm(int mdsize){


        Intent intent = new Intent(mContext, AlertReceiver.class);
        for(int i=0;i<mdsize;i++){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, i, intent, 0);
            alarmManager.cancel(pendingIntent);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarm() throws ParseException {


        cancelAlarm(md.size());


        for(int i=0;i<md.size();i++) {
                    Intent intent = new Intent(mContext, AlertReceiver.class);
                    pendingIntent[i] = PendingIntent.getBroadcast(mContext, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                    //Log.d("this",md.get(i).getCal());
                    Date date = null;
                    try {

                        date = sdf.parse(md.get(i).getCal());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.d("this", date.toString());
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);

                    if (md.get(i).getDt().charAt(7) == '1') {



                        if (c.before(Calendar.getInstance().getTimeInMillis())) {
                            c.add(Calendar.DATE, 1);


                        }
                        else{
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent[i]);
                            c.add(Calendar.DATE, 1);

                        }



                    }

                    else{
                        if(md.get(i).getDt().charAt(0) == '1' && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1){


                            if(c.before(Calendar.getInstance().getTimeInMillis())) {
                                c.add(Calendar.DATE,7);


                            }
                        /*pendingIntent.add(PendingIntent.getBroadcast(medicationreminder.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent.get(i));
                           */
                            else{
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[i]);
                                c.add(Calendar.DATE,7);
                            }


                        }

                        if(md.get(i).getDt().charAt(1) == '1' && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 2){
                            if(c.before(Calendar.getInstance().getTimeInMillis())) {
                                c.add(Calendar.DATE,7);


                            }
                            /*pendingIntent.add(PendingIntent.getBroadcast(medicationreminder.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent.get(i));
                               */
                            else{
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[i]);
                                c.add(Calendar.DATE,7);
                            }


                        }

                        if(md.get(i).getDt().charAt(2) == '1' && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 3){
                            if(c.before(Calendar.getInstance().getTimeInMillis())) {
                                c.add(Calendar.DATE,7);


                            }
                            /*pendingIntent.add(PendingIntent.getBroadcast(medicationreminder.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent.get(i));
                               */
                            else{
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[i]);
                                c.add(Calendar.DATE,7);

                            }
                        }

                        if(md.get(i).getDt().charAt(3) == '1' && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4){
                            if(c.before(Calendar.getInstance().getTimeInMillis())) {
                                c.add(Calendar.DATE,7);


                            }
                                /*pendingIntent.add(PendingIntent.getBroadcast(medicationreminder.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent.get(i));
                                   */
                            else{
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[i]);
                                c.add(Calendar.DATE,7);

                            }
                        }


                        if(md.get(i).getDt().charAt(4) == '1' && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 5){
                            if(c.before(Calendar.getInstance().getTimeInMillis())) {
                                c.add(Calendar.DATE,7);


                            }
                            /*pendingIntent.add(PendingIntent.getBroadcast(medicationreminder.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent.get(i));
                            */
                            else{
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[i]);
                                c.add(Calendar.DATE,7);

                            }
                        }


                        if(md.get(i).getDt().charAt(5) == '1' && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 6){
                            if(c.before(Calendar.getInstance().getTimeInMillis())) {
                                c.add(Calendar.DATE,7);


                            }
                            /*pendingIntent.add(PendingIntent.getBroadcast(medicationreminder.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent.get(i));
                            */
                            else{
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[i]);
                                c.add(Calendar.DATE,7);

                            }
                        }


                        if(md.get(i).getDt().charAt(6) == '1' && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 7){
                            if(c.before(Calendar.getInstance().getTimeInMillis())) {
                                c.add(Calendar.DATE,7);


                            }
                            /*pendingIntent.add(PendingIntent.getBroadcast(medicationreminder.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent.get(i));
                            */
                            else{
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent[i]);
                                c.add(Calendar.DATE,7);

                            }
                        }
                    }

                }




    }



}
