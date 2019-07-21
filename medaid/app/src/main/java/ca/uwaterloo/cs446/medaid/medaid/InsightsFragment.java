package ca.uwaterloo.cs446.medaid.medaid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class InsightsFragment extends Fragment {
    View view;
    BarChart weekChart;
    ArrayList Meds = new ArrayList();
    ArrayList Day = new ArrayList();

    //Will be removed
    String m = "[{'rowNum': '22', 'userID': '1', 'medName': 'Tylenol', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Advil', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Adderall', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Xanax', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Ibuprofen', 'startDate': '2018-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Advil Flu and Cold', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Evening Snack'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Antibiotic', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Penicillin', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Panadol', 'startDate': '2017-07-02 00:00', 'endDate': '2017-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Buckleys', 'startDate': '2018-07-02 00:00', 'endDate': '2018-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'}]";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_insights, container, false);

        final TextView text1 = (TextView) view.findViewById(R.id.medication1);
        final TextView text2 = (TextView) view.findViewById(R.id.medication2);

        Button submitConflict = view.findViewById(R.id.checkConflictButton);

        submitConflict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String med1 = text1.getText().toString();
                String med2 = text2.getText().toString();

                System.out.println(med1);
                System.out.println(med2);

                Intent intent = new Intent(getContext(), ConflictsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("med1", med1);
                extras.putString("med2", med2);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Button foodConflict = view.findViewById(R.id.foodButton);

        final TextView foodText = (TextView) view.findViewById(R.id.foodMed);

        foodConflict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String med = foodText.getText().toString();

                System.out.println(med);

                Intent intent = new Intent(getContext(), ConflictsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("med1", med);
                extras.putString("med2", "");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        weekChart = (BarChart) view.findViewById(R.id.barChart);

        getDataforChart();

        return view;
    }

    public void getDataforChart() {
        Day.add("MON");
        Day.add("TUE");
        Day.add("WED");
        Day.add("THUR");
        Day.add("FRI");
        Day.add("SAT");
        Day.add("SUN");

        JSONArray medication;

        try {
            medication = new JSONArray(m);
        } catch (Exception e) {
            System.out.println("Failed to initialize JSON Array");
            return;
        }

        for (int i = 0; i < medication.length(); ++i) {
            try {
                JSONObject x = medication.getJSONObject(i);
                String s = x.getString("startDate");
                String e = x.getString("endDate");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date start = sdf.parse(s);
                Date end = sdf.parse(e);

                if ((c.getTimeInMillis() >= start.getTime()) && (c.getTimeInMillis() <= end.getTime())) {
                    
                }

            } catch (Exception e) {
                System.out.println("Failed to get JSON Object");
            }
        }

    }
}