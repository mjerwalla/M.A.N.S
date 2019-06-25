package ca.uwaterloo.cs446.medaid.medaid;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    CalendarActivityDBHelper medDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        medDb = new CalendarActivityDBHelper(this);

    }

    public void update(View view) {
        medDb.insertMedicationData(1, "abc", "9", "M,W,F",
                new Date(), new Date(), 2, -1, "nothing");
    }
}