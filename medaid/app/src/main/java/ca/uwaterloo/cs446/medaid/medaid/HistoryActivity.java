package ca.uwaterloo.cs446.medaid.medaid;

import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {
    HistoryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbHelper = new HistoryDatabaseHelper(this);
        dbHelper.onCreate(dbHelper.medDB);
        String startDate = "2017-01-02 16:02";
        String endDate = "2017-01-02 16:20";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean populateDate = false;
        try {
            Date sDate = sdf.parse(startDate);
            Date eDate = sdf.parse(endDate);
            populateDate = dbHelper.insertData(1234, "Paracetamol", "1", "7", sDate, eDate, 1, 7, "Have after dinner");
        } catch(Exception e) {
            System.out.println("Failed");
        }
        if (populateDate) {
            //populate ListView

        }
    }

    //search database for searched word
//    private void search() {
//
//    }
}
