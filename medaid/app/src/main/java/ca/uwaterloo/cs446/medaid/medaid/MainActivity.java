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
import android.widget.LinearLayout;
import android.widget.TextView;

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
                .replace(R.id.fragment_container, new CalendarFragment()).commit();

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
                            selectedFrag = new CalendarFragment();
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
        medDb.insertMedicationData(1, "Janumet", "10,19", "M,W,F",
                new Date(2019,06,26), new Date(2020,06,26), 2, 100, "None");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CalendarFragment()).commit();

        // TextView myAwesomeTextView = (TextView)findViewById(R.id.textView3);
        // myAwesomeTextView.setText(medDb.getAllData().toString());
        // medDb.getAllData();
    }

    public void delete(View view){
        medDb.deleteData(Integer.toString(view.getId()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CalendarFragment()).commit();
    }
}