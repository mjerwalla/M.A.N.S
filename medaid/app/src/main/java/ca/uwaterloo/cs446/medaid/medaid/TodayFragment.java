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


public class TodayFragment extends Fragment implements TodayFragmentPresenter.View{
    private View v;
    private TodayFragmentPresenter todayFragmentPresenter;
    //    AlarmManager alarmManager;
    AlarmReceiver alarmReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_today, container, false);
        todayFragmentPresenter = new TodayFragmentPresenter(this, this.getContext());
        this.updateMedicationListEverywhere();

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
            String[] hoursMinutes = med.timesToBeReminded[0].split(":");
            int hours = Integer.parseInt(hoursMinutes[0]);
            String minutes = hoursMinutes[1];
            String ampm = " am";
            if (hours >= 12) {
                ampm = " pm";
            }

            hours = hours % 12;
            if (hours == 0) {
                hours = 12;
            }

            time.setText(hours + ":" + minutes + ampm);

            TextView dosage = rowView.findViewById(R.id.txtDosage);
            dosage.setText(med.dosagePerIntake + " pills");

            // Set button ID
            Button takenButton = rowView.findViewById(R.id.btnTaken);
            takenButton.setId(Integer.parseInt(med.medID));
        }
    }

    public void updateMedicationListEverywhere() {
        todayFragmentPresenter.updateMedicationList();
    }
}