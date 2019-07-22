package ca.uwaterloo.cs446.medaid.medaid;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainActivityPresenter.View{
    private MainActivityPresenter mainActivityPresenter;
    private MainActivityHelper mainActivityHelper;

    TodayFragment todayFragment = new TodayFragment();
    CalendarFragment calendarFragment = new CalendarFragment();
    HistoryFragment insightsFragment = new HistoryFragment();
    HistoryFragment historyFragment = new HistoryFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityPresenter = new MainActivityPresenter(this, getBaseContext());
        mainActivityHelper = new MainActivityHelper();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, todayFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag = null;

                    switch(item.getItemId()) {
                        case R.id.nav_today:
                            selectedFrag = todayFragment;
                            break;
                        case R.id.nav_calendar:
                            selectedFrag = calendarFragment;
                            break;
                        case R.id.nav_history:
                            selectedFrag = new HistoryFragment();
                            break;
                        case R.id.nav_insights:
                            selectedFrag = new InsightsFragment();
                            break;
                        case R.id.nav_logout:
                            deleteUserSettings();
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFrag).commit();
                    return true;
                }
            };

    @Override
    public void updateTodayMedicationList() {
        todayFragment.updateMedicationListEverywhere();
    }

    @Override
    public void updateCalendar() {
        calendarFragment.updateCalendar();
    }

    @Override
    public void updateHistoryMedication() {

    }

    @Override
    public void updateHistoryReports() {

    }

    @Override
    public void updateHistoryVaccination() {

    }

    @Override
    public void updateInsights() {

    }

    public void deleteUserSettings(){
        SharePreferences user = new SharePreferences(this);
        user.modifyPref("userID",null);
        user.modifyPref("userType", null);
    }

    public void setAddMedPopupBehavior(View view) {
        AlertDialog.Builder medPopupBuilder = new AlertDialog.Builder(MainActivity.this);
        View medPopupView = getLayoutInflater().inflate(R.layout.overlay_today_add_med_1, null);
        medPopupBuilder.setView(medPopupView);
        final AlertDialog medInfoDialog = medPopupBuilder.create();
        medInfoDialog.show();

        final TextView medName = medPopupView.findViewById(R.id.txtMedName);
        final TextView dosagePerIntake = medPopupView.findViewById(R.id.txtDosagePerIntake);
        // final TextView startDate = medPopupView.findViewById(R.id.startDate);
        final TextView totalNumPills = medPopupView.findViewById(R.id.txtTotalPills);
        final TextView notes = medPopupView.findViewById(R.id.txtNotes);
        Button nextScreenButton = medPopupView.findViewById(R.id.btnNext);

        final CheckBox[] daysOfTheWeek = {
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
                final String daysOfTheWeekString = mainActivityHelper.getDaysOfTheWeekString(daysOfTheWeek);

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
                        timeIDMap.clear();
                        submitButton.setVisibility(View.INVISIBLE);
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

                            // Number each time slot
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

                                            String time = mainActivityHelper.getTimesToTakeMedication(hourOfDay, minutes);

                                            setMedicineTime.setText(time + " " + ampm);
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
                        final String medTimes = mainActivityHelper.sortAndConcatMedTimes(timeIDMap);
                        Date startDate = new Date();
                        Date endDate = new Date(120, 05, 22);
                        SimpleDateFormat ft = new SimpleDateFormat (Constants.DATE_TIME_FORMAT);
                        String startDateString = ft.format(startDate);
                        String endDateString = ft.format(endDate);
                        mainActivityPresenter.addMedication(
                                medName.getText().toString(),
                                startDateString,
                                endDateString,
                                daysOfTheWeekString,
                                Integer.toString(timeIDMap.size()),
                                medTimes,
                                dosagePerIntake.getText().toString(),
                                totalNumPills.getText().toString(),
                                notes.getText().toString());

                        medTimesDialog.hide();
                    }
                });
            }
        });
    }

    public void delete(View view){
        // TODO: replace medDb.deleteData(Integer.toString(view.getId()));
        mainActivityPresenter.deleteMedication(view.getId());
    }

    public void addAppointment(View view) {
        AlertDialog.Builder addAppointmentDialogBuilder = new AlertDialog.Builder(this);
        View addAppointmentView = getLayoutInflater().inflate(R.layout.overlay_calendar_add_appointment, null);
        addAppointmentDialogBuilder.setView(addAppointmentView);
        final AlertDialog addAppointmentDialog = addAppointmentDialogBuilder.create();
        addAppointmentDialog.show();

        final TextView aptName = addAppointmentView.findViewById(R.id.txtAptName);
        final Button aptTime = addAppointmentView.findViewById(R.id.btnSelectAptTime);
        final Button addAptButton = addAppointmentView.findViewById(R.id.btnAddAppointment);
        final String[] timeArray = new String[1];
        addAptButton.setEnabled(false);

        aptTime.setOnClickListener(new View.OnClickListener() {
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

                        String minutesString = Integer.toString(minutes);
                        if (minutes < 10) {
                            minutesString = "0" + minutesString;
                        }
                        timeArray[0] = hourOfDay + ":" + minutesString;

                        String time = mainActivityHelper.getTimesToTakeMedication(hourOfDay, minutes);

                        aptTime.setText(time + " " + ampm);

                        addAptButton.setEnabled(true);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        addAptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityPresenter.addAppointment(
                        aptName.getText().toString(),
                        timeArray[0]);
                addAppointmentDialog.hide();
            }
        });
    }
}