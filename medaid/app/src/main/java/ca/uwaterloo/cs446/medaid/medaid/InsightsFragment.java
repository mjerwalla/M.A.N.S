package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class InsightsFragment extends Fragment {
    View view;
    TextView text1;
    TextView text2;
    TextView foodText;
    BarChart weekChart;
    ArrayList<BarEntry> Meds = new ArrayList();
    ArrayList<String> Day = new ArrayList();

    //Will be removed
    String m = "[{'rowNum': '22', 'userID': '1', 'medName': 'Tylenol', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Advil', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,SAT', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Adderall', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'TUE,WED,THUR', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Xanax', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Ibuprofen', 'startDate': '2018-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,THUR,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Advil Flu and Cold', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'SUN', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Evening Snack'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Antibiotic', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'SUN,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Penicillin', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'SUN,THUR,SAT', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Panadol', 'startDate': '2017-07-02 00:00', 'endDate': '2017-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Buckleys', 'startDate': '2018-07-02 00:00', 'endDate': '2018-08-01 00:00', 'selectedDaysPerWeek': 'TUE,WED,SAT', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'}]";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_insights, container, false);

        text1 = (TextView) view.findViewById(R.id.medication1);
        text2 = (TextView) view.findViewById(R.id.medication2);
        Button submitConflict = view.findViewById(R.id.checkConflictButton);

        submitConflict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMedConflict();
            }
        });

        foodText = (TextView) view.findViewById(R.id.foodMed);
        Button foodConflict = view.findViewById(R.id.foodButton);

        foodConflict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFoodConflict();
            }
        });

        weekChart = (BarChart) view.findViewById(R.id.barChart);

        getDataforChart();

        return view;
    }


    public void goToMedConflict() {
        text1.setError(null);
        text2.setError(null);

        String med1 = text1.getText().toString();
        String med2 = text2.getText().toString();

        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(med1)) {
            text1.setError("Can not be empty");
            focusView = text1;
            cancel = true;
        }
        if (TextUtils.isEmpty(med2)) {
            text2.setError("Can not be empty");
            focusView = text2;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Intent intent = new Intent(getContext(), ConflictsActivity.class);
            Bundle extras = new Bundle();
            extras.putString("med1", med1);
            extras.putString("med2", med2);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }


    public void goToFoodConflict() {
        foodText.setError(null);

        String med = foodText.getText().toString();

        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(med)) {
            foodText.setError("Can not be empty");
            focusView = foodText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Intent intent = new Intent(getContext(), ConflictsActivity.class);
            Bundle extras = new Bundle();
            extras.putString("med1", med);
            extras.putString("med2", "");
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    public void getDataforChart() {
        Day.add("MON");
        Day.add("TUE");
        Day.add("WED");
        Day.add("THUR");
        Day.add("FRI");
        Day.add("SAT");
        Day.add("SUN");

        int mon = 0;
        int tue = 0;
        int wed = 0;
        int thur = 0;
        int fri = 0;
        int sat = 0;
        int sun = 0;

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
                c.add(Calendar.DATE, -1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date start = sdf.parse(s);
                Date end = sdf.parse(e);

                if ((c.getTimeInMillis() >= start.getTime()) && (c.getTimeInMillis() <= end.getTime())) {
                    String selectedDays = x.getString("selectedDaysPerWeek");

                    if (selectedDays.contains("MON")) {
                        ++mon;
                    }
                    if (selectedDays.contains("TUE")) {
                        ++tue;
                    }
                    if (selectedDays.contains("WED")) {
                        ++wed;
                    }
                    if (selectedDays.contains("THUR")) {
                        ++thur;
                    }
                    if (selectedDays.contains("FRI")) {
                        ++fri;
                    }
                    if (selectedDays.contains("SAT")) {
                        ++sat;
                    }
                    if (selectedDays.contains("SUN")) {
                        ++sun;
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to get JSON Object");
            }
        }

        Meds.add(new BarEntry(mon, 0));
        Meds.add(new BarEntry(tue, 1));
        Meds.add(new BarEntry(wed, 2));
        Meds.add(new BarEntry(thur, 3));
        Meds.add(new BarEntry(fri, 4));
        Meds.add(new BarEntry(sat, 5));
        Meds.add(new BarEntry(sun, 6));

        BarDataSet data = new BarDataSet(Meds, "No of Medicines");
        weekChart.animateY(10);
        BarData d = new BarData(Day, data);
        data.setColors(ColorTemplate.PASTEL_COLORS);
        weekChart.setDescription("Number of Current Medication Per Day");
        weekChart.setData(d);
    }
}