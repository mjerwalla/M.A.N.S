package ca.uwaterloo.cs446.medaid.medaid;

import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
            populateDate = dbHelper.insertData(1234, "Advil", "1", "2", sDate, eDate, 1, 10, "Have before sleeping");
        } catch(Exception e) {
            System.out.println("Failed");
        }
        if (populateDate) {
            //populating ListView

            listView = (ListView) findViewById(R.id.listView);
            ArrayList<String> names = new ArrayList<>();

            Cursor medData = dbHelper.getMedication();

            if (medData.moveToFirst()) {
                while (!medData.isAfterLast()) {
                    names.add(medData.getString(medData.getColumnIndex("medName")));
                    medData.moveToNext();
                }
            }

            medData.moveToFirst();

            String[] cols = new String[] {
                    "_id",
                    dbHelper.COL_2,
                    dbHelper.COL_8
            };

            int[] to = new int[] {
                    android.R.id.text1,
                    android.R.id.text1,
                    android.R.id.text2
            };


            medicationAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, medData, cols, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            medicationAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int i) {
                    if (view.getId() == android.R.id.text2) {
                        int getIndex = cursor.getColumnIndex("totalNumPills");
                        int num = cursor.getInt(getIndex);
                        TextView dateTextView = (TextView) view;
                        dateTextView.setText("Total Number of Pills: " + num);
                        return true;
                    }
                    return false;
                }
            });

            listView.setAdapter(medicationAdapter);
        }
    }

    //search database for searched word
//    private void search() {
//
//    }
}
