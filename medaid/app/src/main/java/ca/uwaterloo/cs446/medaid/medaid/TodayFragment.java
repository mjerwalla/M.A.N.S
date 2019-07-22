package ca.uwaterloo.cs446.medaid.medaid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.List;

<<<<<<< HEAD
import static android.content.Context.ALARM_SERVICE;

public class TodayFragment extends Fragment {
    private View v;
    private CalendarActivityDBHelper medDb;
//    AlarmManager alarmManager;
    AlarmReceiver alarmReceiver;
    PendingIntent pendingIntent;
=======
public class TodayFragment extends Fragment implements TodayFragmentPresenter.View{
    private View v;
    private TodayFragmentPresenter todayFragmentPresenter;
>>>>>>> ca28cb2f7c048375b095883f1dbd35f0f1012dce

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_today, container, false);
        todayFragmentPresenter = new TodayFragmentPresenter(this, this.getContext());
        this.updateMedicationListEverywhere();

        backgroundNotifications();

        return v;
    }

    @Override
    public void updateMedicationListView(List<TodayFragmentPresenter.UpcomingMedicine> upcomingMedicines) {
        LinearLayout linearLayout = v.findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        int idCounter = 0;
        for (TodayFragmentPresenter.UpcomingMedicine med: upcomingMedicines) {
            idCounter++;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.upcoming_med_block, linearLayout, false);

            if (rowView.getParent() != null) {
                ((ViewGroup)rowView.getParent()).removeView(rowView);
            }
            linearLayout.addView(rowView);

            TextView medName = rowView.findViewById(R.id.txtMedName);
            medName.setText(med.medName);

            // TODO: Create new medicine reminder for each time
            TextView time = rowView.findViewById(R.id.txtTime);
            time.setText(med.timesToBeReminded[0]);

            TextView dosage = rowView.findViewById(R.id.txtDosage);
            dosage.setText(med.dosagePerIntake + " pills");

            // Set button ID
            Button takenButton = rowView.findViewById(R.id.btnTaken);
            takenButton.setId(Integer.parseInt(med.medID));
        }
    }

<<<<<<< HEAD
    public void backgroundNotifications() {
        String CHANNEL_ID="\"my_channel_id_01\"";
        int NOTIFICATION_ID = 1;
        System.out.println("in background notifs");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "test in prof";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent mIntent = new Intent(this.getContext(), TodayFragment.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        Bundle bundle = new Bundle();
//        bundle.putString("test", "test");
//        mIntent.putExtras(bundle);
        pendingIntent = PendingIntent.getActivity(this.getContext(), 0, mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Resources res = this.getResources();
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notif =  new NotificationCompat.Builder(this.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.pills)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("Notif title")
                .setContentText("Text")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
//        Notification notif = notification.setContentIntent(pendingIntent)

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.getContext());
//        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
//        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
//        notification.ledARGB = 0xFFFFA500;
//        notification.ledOnMS = 800;
//        notification.ledOffMS = 1000;
//        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notif.build());
        System.out.println("Notif sent");
//
//        Intent alarmIntent = new Intent(this.getActivity(), AlarmReceiver.class);
//        System.out.println(alarmIntent);
//        AlarmReceiver ar = new AlarmReceiver();
////        alarmReceiver = new AlarmReceiver();
////        pendingIntent = PendingIntent.getService(this.getContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pendingIntent = PendingIntent.getBroadcast(this.getContext(), 0, alarmIntent, 0);
//        System.out.println(pendingIntent);
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        LocalBroadcastManager.registerReceiver(ar, filter);
////        AlarmManager alarmManager = (AlarmManager) this.getContext().getSystemService(ALARM_SERVICE);
////        System.out.println(SystemClock.elapsedRealtime());
////        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10*1000, pendingIntent );
//
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis() + 9000,3000, pendingIntent );
////        System.out.println(alarmManager);
////        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
////        alarmManager.cancel(pendingIntent);
//
////        Calendar alarmStartTime = Calendar.getInstance();
////        Calendar now = Calendar.getInstance();
////        System.out.println(now);
////        alarmStartTime.set(Calendar.HOUR_OF_DAY, 2);
////        alarmStartTime.set(Calendar.MINUTE, 40);
////        alarmStartTime.set(Calendar.SECOND, 0);
////        if (now.after(alarmStartTime)) {
////            System.out.println("Hey Added a day");
////            alarmStartTime.add(Calendar.DATE, 1);
////        }
//
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
////                alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        System.out.println("Alarm Alarms set for everyday 8 am.");
=======
    public void updateMedicationListEverywhere() {
        todayFragmentPresenter.updateMedicationList();
>>>>>>> ca28cb2f7c048375b095883f1dbd35f0f1012dce
    }
}