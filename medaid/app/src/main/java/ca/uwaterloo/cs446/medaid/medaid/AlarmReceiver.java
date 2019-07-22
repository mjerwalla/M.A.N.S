package ca.uwaterloo.cs446.medaid.medaid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("In onReceive");
        Intent service = new Intent(context, NotificationService.class);
//        service.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(service);
        System.out.println("exit onReceive");
    }
}
