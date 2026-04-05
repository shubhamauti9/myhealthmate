package com.example.myhealthmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("Medication Reminder","Please take your medicine");
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationHelper.getManager().notify(m, nb.build());



    }
}
