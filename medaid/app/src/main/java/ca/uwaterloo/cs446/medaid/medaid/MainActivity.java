package ca.uwaterloo.cs446.medaid.medaid;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    CalendarActivityDBHelper medDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Map<String, String> postData = new HashMap<>();
//        postData.put("userName", "shadowbil");
//        postData.put("password", "abc1231");
//        postData.put("firstName", "abbba");
//        postData.put("lastName", "accca");
//        postData.put("userType", "2");
//
//
//        Callback callback = new Callback() {
//            @Override
//            public void onValueReceived(final String value) {
//                System.out.println("The onValueReceived  for Post: " + value);
//                // call method to update view as required using returned value
//
//            }
//
//            @Override
//            public void onFailure() {
//                System.out.println("I failed :(");
//            }
//        };
//        DatabaseHelperPost task = new DatabaseHelperPost(postData, callback);
//        task.execute("http://10.0.2.2/addUser");
//
//        Callback callbackGet = new Callback() {
//            @Override
//            public void onValueReceived(final String value) {
//                System.out.println("The onValueReceived for Get : " + value);
//                // call method to update view as required using returned value
//
//            }
//
//            @Override
//            public void onFailure() {
//                System.out.println("I failed :(");
//            }
//        };
//        DatabaseHelperGet taskGet = new DatabaseHelperGet(null, callbackGet);
//        taskGet.execute("http://10.0.2.2/getAllUsers");

        medDb = new CalendarActivityDBHelper(this);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag = null;

                    switch(item.getItemId()) {
                        case R.id.nav_today:
                            selectedFrag = new TodayFragment();
                            break;
                        case R.id.nav_calendar:
                            selectedFrag = new CalendarFragment();
                            break;
                        case R.id.nav_insights:
                            selectedFrag = new HistoryFragment();
                            break;
                        case R.id.nav_personal:
                            selectedFrag = new HistoryFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFrag).commit();

                    return true;
                }
            };

    public void setAddMedPopupBehavior(View view) {
        AlertDialog.Builder medPopupBuilder = new AlertDialog.Builder(MainActivity.this);
        View medPopupView = getLayoutInflater().inflate(R.layout.overlay_today_add_med_1, null);

        medPopupBuilder.setView(medPopupView);
        final AlertDialog medInfoDialog = medPopupBuilder.create();
        medInfoDialog.show();

        // TODO: Modularize this function to find multiple Views by ID
        final TextView medName = medPopupView.findViewById(R.id.txtMedName);
        final TextView totalPills = medPopupView.findViewById(R.id.txtTotalPills);
        final TextView dosagePerIntake = medPopupView.findViewById(R.id.txtDosagePerIntake);
        // final TextView startDate = medPopupView.findViewById(R.id.startDate);
        final TextView notes = medPopupView.findViewById(R.id.txtNotes);
        Button nextScreenButton = medPopupView.findViewById(R.id.btnNext);

        CheckBox[] daysOfTheWeek = {
                medPopupView.findViewById(R.id.monday),
                medPopupView.findViewById(R.id.tuesday),
                medPopupView.findViewById(R.id.wednesday),
                medPopupView.findViewById(R.id.thursday),
                medPopupView.findViewById(R.id.friday),
                medPopupView.findViewById(R.id.saturday),
                medPopupView.findViewById(R.id.sunday)
        };
        final StringBuilder daysOfTheWeekString = new StringBuilder();

        for (CheckBox checkBox : daysOfTheWeek) {
            if (checkBox.isChecked()) {
                switch (checkBox.getId()) {
                    case R.id.monday:
                        daysOfTheWeekString.append("MON");
                        break;
                    case R.id.tuesday:
                        daysOfTheWeekString.append("TUES");
                        break;
                    case R.id.wednesday:
                        daysOfTheWeekString.append("WED");
                        break;
                    case R.id.thursday:
                        daysOfTheWeekString.append("THURS");
                        break;
                    case R.id.friday:
                        daysOfTheWeekString.append("FRI");
                        break;
                    case R.id.saturday:
                        daysOfTheWeekString.append("SAT");
                        break;
                    case R.id.sunday:
                        daysOfTheWeekString.append("SUN");
                        break;
                }
            }
            daysOfTheWeekString.append(",");
        }

        // Remove last comma
        int lastCharIndex = daysOfTheWeekString.length() - 1;
        daysOfTheWeekString.substring(0, lastCharIndex);

        // Call helper (which will be reused) that gets existing fields in order to send to db
        // TODO: Set OnClick behaviour to call the helper, passing in the found ID's strings as parameters

        nextScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the next screen
                AlertDialog.Builder medTimePopupBuilder = new AlertDialog.Builder(MainActivity.this);
                final View medTimesPopupView = getLayoutInflater().inflate(R.layout.overlay_today_add_med_2, null);
                medTimePopupBuilder.setView(medTimesPopupView);
                final AlertDialog medTimesDialog = medTimePopupBuilder.create();
                medTimesDialog.show();
                final NumberPicker numberPicker = medTimesPopupView.findViewById(R.id.numberPicker);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(7);

                Button okButton = medTimesPopupView.findViewById(R.id.btnOk);
                final Map<Integer, String> timeIDMap = new HashMap<Integer, String>();
                final Button submitButton = medTimesPopupView.findViewById(R.id.btnSubmit);
                final int dosage;

                // Hide the current pop-up
                medInfoDialog.hide();

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int chosenNumTimesPerDay = numberPicker.getValue();
                        // Populate chosenNumTimesPerDay # of time choosers
                        LinearLayout linearLayout = medTimesPopupView.findViewById(R.id.linearLayoutMedTimes);
                        linearLayout.removeAllViews();

                        for (int i = 1; i <= chosenNumTimesPerDay; i++) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View rowView = inflater.inflate(R.layout.overlay_select_time, linearLayout, false);

                            if (rowView.getParent() != null) {
                                ((ViewGroup)rowView.getParent()).removeView(rowView);
                            }
                            linearLayout.addView(rowView);

                            // Set
                            // new text
                            TextView medTime = rowView.findViewById(R.id.txtMedTime);
                            medTime.setText(medTime.getText() + Integer.toString(i) + ": ");

                            // TODO: May need to double check there are no conflicting id's for txtNumTimesPerDay
                            final Button setMedicineTime = rowView.findViewById(R.id.btnNumTimesPerDay);
                            setMedicineTime.setId(R.id.btnNumTimesPerDay + i);
                            timeIDMap.put(setMedicineTime.getId(), "NULL");

                            setMedicineTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TimePickerDialog timePickerDialog;
                                    timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                            String ampm = "am";

                                            if (hourOfDay >= 12) {
                                                ampm = "pm";
                                            }

                                            hourOfDay = hourOfDay % 12;
                                            if (hourOfDay == 0) {
                                                hourOfDay = 12;
                                            }

                                            String minutesString = Integer.toString(minutes);
                                            if (minutes < 10) {
                                                minutesString = "0" + minutes;
                                            }

                                            String time = hourOfDay + ":" + minutesString;
                                            setMedicineTime.setText(time + " " + ampm);

                                            // Add 0 in front of hour if < 10
                                            if (hourOfDay < 10) {
                                                time = "0" + time;
                                            }
                                            timeIDMap.put(setMedicineTime.getId(), time);

                                            if (!timeIDMap.containsValue("NULL")) {
                                                submitButton.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }, 0, 0, false);
                                    timePickerDialog.show();
                                }
                            });
                        }

                    }
                });

                // TODO: Set submitButton action once all times have been set
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Concatenate all timeIDMap times
                        List<String> times = new ArrayList<>();
                        for (Map.Entry<Integer, String> entry : timeIDMap.entrySet()) {
                            times.add(entry.getValue());
                        }
                        final String medTimes = sortAndConcatMedTimes(times);

                        addMedicalEntry(
                            medName.getText().toString(),
                            medTimes,
                            daysOfTheWeekString.toString(),
                            "startDate",
                            "endDate",
                            Integer.parseInt(dosagePerIntake.getText().toString()),
                            Integer.parseInt(totalPills.getText().toString()),
                            notes.getText().toString());

                        // TODO: Check if new med was successfully added (maybe return value of addMedicalEntry?)
                        // TODO: If successful, close window, otherwise show prompt that say they are missing fields

                        medTimesDialog.hide();
                    }
                });
            }
        });
    }

    final private String sortAndConcatMedTimes(List<String> medTimes) {
        StringBuilder timesBuilder = new StringBuilder();
        String times;
        // TODO: Sort the times from earliest to latest in the day

        for (String time : medTimes) {
            timesBuilder.append(time);
            timesBuilder.append(",");
        }

        times = timesBuilder.toString();
        int lastCharIndex = times.length() - 1;
        times.substring(0, lastCharIndex);
        return times;
    }

    private void addMedicalEntry(
            String medName,
            String timesOfDay,
            String daysPerWeek,
            String startDate,
            String endDate,
            int dosePerIntake,
            int totalPills,
            String notes) {

        medDb.insertMedicationData(
                1,
                medName,
                timesOfDay,
                daysPerWeek,
                new Date(),
                new Date(2020,06,26),
                dosePerIntake,
                totalPills,
                notes);

        this.update();
    }

    private void update() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();
    }

    public void delete(View view){
        medDb.deleteData(Integer.toString(view.getId()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TodayFragment()).commit();
    }
}