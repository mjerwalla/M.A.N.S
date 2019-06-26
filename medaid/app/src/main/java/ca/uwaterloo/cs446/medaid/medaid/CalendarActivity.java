package ca.uwaterloo.cs446.medaid.medaid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    CalendarActivityDBHelper medDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

