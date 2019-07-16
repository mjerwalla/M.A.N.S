package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String name = extras.getString("name");
        String start = extras.getString("start");
        String end = extras.getString("end");
        String timesOfDay = extras.getString("timesOfDay");
        String daysPerWeek = extras.getString("daysPerWeek");
        String dailyNumPills = extras.getString("dailyNumPills");
        String notes = extras.getString("notes");


        TextView t = (TextView)  findViewById(R.id.textViewPopup);
        t.setText(name + "\n" + start + "\n" + end + "\n" + timesOfDay + "\n" + daysPerWeek + "\n" + dailyNumPills + "\n" + notes);
    }
}
