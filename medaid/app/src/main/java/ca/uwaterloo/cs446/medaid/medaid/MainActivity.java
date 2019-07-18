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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CalendarActivityDBHelper medDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Button submitButton = medTimesPopupView.findViewById(R.id.btnSubmit);

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

                            setMedicineTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TimePickerDialog timePickerDialog;
                                    timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                            String ampm = "am";
                                            // TODO: Add am/pm
                                            if (hourOfDay > 12) {
                                                ampm = "pm";
                                            }
                                            String minutesString = Integer.toString(minutes);
                                            if (minutes < 10) {
                                                minutesString = "0" + minutes;
                                            }

                                            setMedicineTime.setText(hourOfDay + ":" + minutesString + " " + ampm);
                                        }
                                    }, 0, 0, false);
                                    timePickerDialog.show();
                                }
                            });
                        }
                    }
                });
            }
        });
//        submitNewMedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addMedicalEntry(
//                        medName.getText().toString(),
//                        numTimesPerDay.getText().toString(), // TODO: CHANGE
//                        numTimesPerDay.getText().toString(), // TODO: CHANGE
//                        "startDate",
//                        "endDate",
//                        2,
//                        Integer.parseInt(totalPills.getText().toString()),
//                        notes.getText().toString());
//
//                // TODO: Check if new med was successfully added (maybe return value of addMedicalEntry?)
//                // TODO: If successful, close window, otherwise show prompt that say they are missing fields
//
//                medInfoDialog.hide();
//            }
//        });
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
        String daysOfTheWeekString;

        String[] lettersOfTheWeek = {
                "MO",
                "TU",
                "WE",
                "TH",
                "FR",
                "SA",
                "SU"
        };

        // Get DaysPerWeek
        int counter = 0;
//        for (CheckBox day: daysOfTheWeek) {
////            // TODO
////        }

        String[] timesPerDay = {
                "12",
                "15",
                "18",
                "21",
                "23"
        };

//        int dailyIntake = Integer.parseInt(numTimesPerDay.getText().toString());
//        String timesOfDayString = "9";
//        for (int i = 0; i < dailyIntake - 1; i++) {
//            timesOfDayString += "," + timesPerDay[i];
//        }

        medDb.insertMedicationData(
                1,
                medName,
                timesOfDay,
                "M,W,F",
                new Date(2019,06,26),
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