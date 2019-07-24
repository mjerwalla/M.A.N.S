package ca.uwaterloo.cs446.medaid.medaid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class NotificationService extends JobIntentService {

    protected NotificationManager notificationManager;
    final Handler mHandler = new Handler();
    private PendingIntent pendingIntent;
    private static int NOTIFICATION_ID = 2;
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    private static final int JOB_ID = 2;

//    Notification notification;

//    public NotificationService() {
//        super("NotificationService");
////        System.out.println("Notification service initialized");
//    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, NotificationService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        showToast("Job Execution Started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        showToast("Job Execution Finished");
    }


    @Override
    protected void onHandleWork(Intent intent) {
        System.out.println("In Handle");
        createNotificationChannel();
        Notification.Builder builder = new Notification.Builder(this,NOTIFICATION_CHANNEL_ID );
        builder.setContentTitle("Reminder");
        builder.setContentText("Hey! It's time to take your Health Boosters!");
        builder.setSmallIcon(R.drawable.pills);
        builder.setAutoCancel(true);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MEDAID";
            String description = "NOTIFS";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NotificationService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        System.out.println("IN NOTIFICATION HANDLE");
//        Context context = this.getApplicationContext();
////        notificationManager =
////                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
//
//            // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//        Intent mIntent = new Intent(this.getBaseContext(), TodayFragment.class);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        Bundle bundle = new Bundle();
////        bundle.putString("test", "test");
////        mIntent.putExtras(bundle);
//        pendingIntent = PendingIntent.getActivity(this, 0, mIntent,
//                0);
////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        Resources res = this.getResources();
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
////        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        NotificationCompat.Builder notif =  new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.pills)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentTitle("Notif title")
//                .setContentText("Text")
////                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
////        Notification notif = notification.setContentIntent(pendingIntent)
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
////        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
////        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
////        notification.ledARGB = 0xFFFFA500;
////        notification.ledOnMS = 800;
////        notification.ledOffMS = 1000;
////        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFICATION_ID, notif.build());
//        System.out.println("Notif sent");
//    }

}
