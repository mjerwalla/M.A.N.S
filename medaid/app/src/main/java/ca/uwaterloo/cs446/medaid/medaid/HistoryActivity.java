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

public class HistoryActivity extends AppCompatActivity {
    HistoryDatabaseHelper dbHelper;
    SimpleCursorAdapter medicationAdapter;
    ArrayAdapter<String> adapter;
    private ListView listView;
    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new HistoryDatabaseHelper(this);
        dbHelper.onCreate(dbHelper.medDB);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean populateDate = false;

        try {
            populateDate = dbHelper.insertData(1234, "Paracetamol", "1", "7", sdf.parse("2017-06-20 16:02"), sdf.parse("2017-06-30 16:02"), 1, 7, "Have after dinner");
            populateDate = dbHelper.insertData(1234, "Advil", "1", "2", sdf.parse("2017-06-23 16:02"), sdf.parse("2017-07-10 16:02"), 1, 10, "Have before sleeping");
            populateDate = dbHelper.insertData(1234, "Adderall", "2", "4", sdf.parse("2017-06-23 16:02"), sdf.parse("2017-07-19 16:02"), 1, 10, "Have before sleeping");
            populateDate = dbHelper.insertData(1234, "Xanax", "1", "5", sdf.parse("2017-06-19 16:02"), sdf.parse("2017-06-30 16:02"), 1, 10, "Have before sleeping");
            populateDate = dbHelper.insertData(1234, "Ibuprofen", "1", "1", sdf.parse("2017-06-10 16:02"), sdf.parse("2017-08-10 16:02"), 1, 10, "Have before sleeping");
            populateDate = dbHelper.insertData(1234, "Panadol", "2", "7", sdf.parse("2017-06-25 16:02"), sdf.parse("2017-06-28 16:02"), 1, 10, "Have before sleeping");
        } catch(Exception e) {
            System.out.println("Failed");
        }
        if (populateDate) {
            //populating ListView

            listView = (ListView) findViewById(R.id.listView);

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
                        int getIndex1 = cursor.getColumnIndex("startDate");
                        String start = cursor.getString(getIndex1);
                        int getIndex2 = cursor.getColumnIndex("endDate");
                        String end = cursor.getString(getIndex2);
                        TextView dateTextView = (TextView) view;
                        dateTextView.setText("Needs to be taken from " + start.substring(0,10) + " to " + end.substring(0,10));
                        return true;
                    }
                    return false;
                }
            });

            listView.setAdapter(medicationAdapter);
        }
    }
}
