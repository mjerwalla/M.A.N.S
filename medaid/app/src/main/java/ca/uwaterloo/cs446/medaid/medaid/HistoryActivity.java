package ca.uwaterloo.cs446.medaid.medaid;

import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HistoryActivity extends AppCompatActivity {
    HistoryDatabaseHelper historyHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyHelper = new HistoryDatabaseHelper(this);
        historyHelper.onCreate(historyHelper.medDB);
        boolean populatedData = historyHelper.insertData(1234, "Paracetamol", "1","7", "2019-06-20 16:00:00", "2019-06-30 23:59:00", 1, 7, "Have after dinner");

        if (populatedData) {
            Cursor result = historyHelper.getMedication();

            System.out.println("WORKING");

//            while (result.moveToNext()) {
//
//            }

        }
    }

    //search database for searched word
//    private void search() {
//
//    }
}
