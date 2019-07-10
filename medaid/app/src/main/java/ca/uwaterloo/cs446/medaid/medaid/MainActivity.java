package ca.uwaterloo.cs446.medaid.medaid;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CalendarActivityDBHelper medDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medDb = new CalendarActivityDBHelper(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag = null;

                    switch(item.getItemId()) {
                        case R.id.nav_today:
                            selectedFrag = new TodayFragment();
                            break;
                        case R.id.nav_calendar:
                            selectedFrag = new CalendarFragment();
                            break;
                        case R.id.nav_insights:
                            selectedFrag = new HistoryFragment();
                            break;
                        case R.id.nav_personal:
                            selectedFrag = new HistoryFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFrag).commit();

                    return true;
                }
            };

    public void setAddMedPopupBehavior(View view) {
        AlertDialog.Builder medPopupBuilder = new AlertDialog.Builder(MainActivity.this);
        View medPopupView = getLayoutInflater().inflate(R.layout.overlay_add_med, null);

        medPopupBuilder.setView(medPopupView);
        final AlertDialog dialog = medPopupBuilder.create();
        dialog.show();

        // TODO: Modularize this function to find multiple Views by ID
        final TextView medName = medPopupView.findViewById(R.id.txtMedName);
        final TextView totalPills = medPopupView.findViewById(R.id.txtTotalPills);
        final TextView numTimesPerDay = medPopupView.findViewById(R.id.txtNumTimesPerDay);
        // final TextView startDate = medPopupView.findViewById(R.id.startDate);
        final TextView notes = medPopupView.findViewById(R.id.txtNotes);
        Button submitNewMedButton = medPopupView.findViewById(R.id.submitNewMed);

        CheckBox[] daysOfTheWeek = {
                medPopupView.findViewById(R.id.monday),
                medPopupView.findViewById(R.id.tuesday),
                medPopupView.findViewById(R.id.wednesday),
                medPopupView.findViewById(R.id.thursday),
                medPopupView.findViewById(R.id.friday),
                medPopupView.findViewById(R.id.saturday),
                medPopupView.findViewById(R.id.sunday)
        };

        // Call helper (which will be reused) that gets existing fields in order to send to db
        // TODO: Set OnClick behaviour to call the helper, passing in the found ID's strings as parameters

        submitNewMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicalEntry(
                        medName.getText().toString(),
                        numTimesPerDay.getText().toString(), // TODO: CHANGE
                        numTimesPerDay.getText().toString(), // TODO: CHANGE
                        "startDate",
                        "endDate",
                        2,
                        Integer.parseInt(totalPills.getText().toString()),
                        notes.getText().toString());

                // TODO: Check if new med was successfully added (maybe return value of addMedicalEntry?)
                // TODO: If successful, close window, otherwise show prompt that say they are missing fields

                dialog.hide();
            }
        });
    }

    private void addMedicalEntry(
            String medName,
            String timesOfDay,
            String daysPerWeek,
            String startDate,
            String endDate,
            int dosePerIntake,
            int totalPills,
            String notes) {
        String daysOfTheWeekString;

        String[] lettersOfTheWeek = {
                "MO",
                "TU",
                "WE",
                "TH",
                "FR",
                "SA",
                "SU"
        };

        // Get DaysPerWeek
        int counter = 0;
//        for (CheckBox day: daysOfTheWeek) {
////            // TODO
////        }

        String[] timesPerDay = {
                "12",
                "15",
                "18",
                "21",
                "23"
        };

//        int dailyIntake = Integer.parseInt(numTimesPerDay.getText().toString());
//        String timesOfDayString = "9";
//        for (int i = 0; i < dailyIntake - 1; i++) {
//            timesOfDayString += "," + timesPerDay[i];
//        }

        medDb.insertMedicationData(
                1,
                medName,
                timesOfDay,
                "M,W,F",
                new Date(2019,06,26),
                new Date(2020,06,26),
                dosePerIntake,
                totalPills,
                notes);

        this.update();
    }


    private void update() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();
    }

    public void delete(View view){
        medDb.deleteData(Integer.toString(view.getId()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();
    }
}