package ca.uwaterloo.cs446.medaid.medaid;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    CalendarActivityDBHelper medDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        medDb = new CalendarActivityDBHelper(this);

    }

    public void update(View view) {
        medDb.insertData(1, "abc", "9", "M,W,F",
                1500, 1500, 2, -1, "nothing");
    }
}