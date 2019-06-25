package ca.uwaterloo.cs446.medaid.medaid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HistoryActivity extends AppCompatActivity {
    HistoryDatabaseHelper medDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        medDb = new HistoryDatabaseHelper(this);
        medDb.onCreate();
        boolean populatedDate - medDb.insertData("1234", "Paracetamol", "1". "7", 1561003200000, 1561867200000, 1, 7, "Have after dinner");
    }

    //search database for searched word
    private void search() {

    }
}
