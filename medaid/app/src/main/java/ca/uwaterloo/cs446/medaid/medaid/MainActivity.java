package ca.uwaterloo.cs446.medaid.medaid;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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

//        // Adds "add medication" behaviour
//
//        Button addMedication = findViewById(R.id.btnAddMed);
//
//        addMedication.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, AddMedOverlay.class));
//            }
//        });
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

    public void setAddBehavior(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddMedOverlay()).commit();
        // startActivity(new Intent(MainActivity.this, AddMedOverlay.class));
    }


    public void update(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();

        // TextView myAwesomeTextView = (TextView)findViewById(R.id.textView3);
        // myAwesomeTextView.setText(medDb.getAllData().toString());
        // medDb.getAllData();
    }

    public void delete(View view){
        medDb.deleteData(Integer.toString(view.getId()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();
    }

    public void addMedicalEntry(View view) {
        TextView medName = findViewById(R.id.txtMedName);
        TextView totalPills = findViewById(R.id.txtTotalPills);
        TextView numTimesPerDay = findViewById(R.id.txtNumTimesPerDay);
        // TextView startDate = findViewById(R.id.startDate);
        TextView notes = findViewById(R.id.txtNotes);
        String daysOfTheWeekString;

        CheckBox[] daysOfTheWeek = {
                findViewById(R.id.monday),
                findViewById(R.id.tuesday),
                findViewById(R.id.wednesday),
                findViewById(R.id.thursday),
                findViewById(R.id.friday),
                findViewById(R.id.saturday),
                findViewById(R.id.sunday)
        };

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
        for (CheckBox day: daysOfTheWeek) {
            // TODO
        }

        String[] timesPerDay = {
                "12",
                "15",
                "18",
                "21",
                "23"
        };

        int dailyIntake = Integer.parseInt(numTimesPerDay.getText().toString());
        String timesOfDayString = "9";
        for (int i = 0; i < dailyIntake - 1; i++) {
            timesOfDayString += "," + timesPerDay[i];
        }

        medDb.insertMedicationData(
                1,
                medName.getText().toString(),
                timesOfDayString,
                "M,W,F",
                new Date(2019,06,26),
                new Date(2020,06,26),
                dailyIntake,
                Integer.parseInt(totalPills.getText().toString()),
                notes.getText().toString());

        this.update(view);
    }
}