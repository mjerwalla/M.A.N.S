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
        try {
//            Intent service = new Intent(context, NotificationService.class);
//            context.startService(service);
//            service.enqueueWork(context, service);

            Intent i = new Intent(context, NotificationService.class);
            NotificationService.enqueueWork(context, i);
        } catch (Exception e) {
            System.out.println("Failed");
            System.out.println(e);
        }
//        service.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        finally{
            System.out.println("exit onReceive");
        }
    }
}
