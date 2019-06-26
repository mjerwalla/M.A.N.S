package ca.uwaterloo.cs446.medaid.medaid;

import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {
    HistoryDatabaseHelper dbHelper;
    SimpleCursorAdapter medicationAdapter;
    ArrayAdapter<String> adapter;
    private ListView listView;

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
            listView = (ListView) findViewById(R.id.listView);
            ArrayList<String> names = dbHelper.getMedication();
//            Cursor medData = dbHelper.getMedication();
//            String[] cols = new String[] {
//                    "medID",
//                    dbHelper.COL_2,
//                    dbHelper.COL_3,
//                    dbHelper.COL_4
////                    dbHelper.COL_5,
////                    dbHelper.COL_6,
////                    dbHelper.COL_7,
////                    dbHelper.COL_8,
////                    dbHelper.COL_9,
//            };
//
//            int[] to = new int[] {
//                    android.R.id.text1,
//                    android.R.id.text1,
//                    android.R.id.text1,
//                    android.R.id.text1
//            };

//            medicationAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_expandable_list_item_1, medData, cols, to, 0);

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
        }
    }

    //search database for searched word
//    private void search() {
//
//    }
}
