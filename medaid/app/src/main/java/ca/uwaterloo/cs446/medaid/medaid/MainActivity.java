package ca.uwaterloo.cs446.medaid.medaid;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    CalendarActivityDBHelper medDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medDb = new CalendarActivityDBHelper(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
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

    public void update(View view) {
        medDb.insertMedicationData(1, "abc", "9", "M,W,F",
                new Date(), new Date(), 2, -1, "nothing");
        TextView myAwesomeTextView = (TextView)findViewById(R.id.textView3);
        myAwesomeTextView.setText(medDb.getAllData().toString());
        medDb.getAllData();
    }
}