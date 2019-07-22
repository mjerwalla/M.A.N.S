package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
        String selectedDaysPerWeek = extras.getString("selectedDaysPerWeek");
        String numTimesPerDay = extras.getString("numTimesPerDay");
        String dosagePerIntake = extras.getString("dosagePerIntake");
        String takenInPast = extras.getString("takenInPast");
        String notes = extras.getString("notes");

        TextView t = (TextView) findViewById(R.id.textViewPopup);

        if (takenInPast.equals("1")) {
            t.setText(name + "\n\n" + start + "\n" + end + "\n\nInstructions:\n\n" + notes);
        } else {
            if (TextUtils.isEmpty(notes))
            {
                t.setText(name + "\n\n" + start + "\n" + end + "\n\nInstructions:\n\n" + "Have " + dosagePerIntake + " dosages " + numTimesPerDay + " times a day on " + selectedDaysPerWeek);
            } else {
                t.setText(name + "\n\n" + start + "\n" + end + "\n\nInstructions:\n\n" + "Have " + dosagePerIntake + " dosages " + numTimesPerDay + " times a day on " + selectedDaysPerWeek + "\n\nAdditional:\n" + notes);
            }
        }
    }
}
