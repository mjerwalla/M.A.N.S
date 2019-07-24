package ca.uwaterloo.cs446.medaid.medaid;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements CalendarFragmentPresenter.View {
    private View v;
    private CalendarFragmentPresenter calendarFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.fragment_calendar, container, false);
        this.calendarFragmentPresenter = new CalendarFragmentPresenter(this, this.getContext());
        this.decorateCalendar();
        return v;
    }

    @Override
    public void updateCalendar() {
        decorateCalendar();
    }

    @Override
    public void addAppointment() {

    }

    @Override
    public void addLowMedicineEvent() {

    }

    public void decorateCalendar() {
        final MaterialCalendarView calendarView = (MaterialCalendarView) v.findViewById(R.id.calendarView);
        calendarView.setSelectionColor(Color.parseColor("#27CEA7"));
        List decorators = new ArrayList<>();
        CalendarDay day = new CalendarDay();
        HashSet<CalendarDay> days = new HashSet<CalendarDay>();
        days.add(day);
        //decorators.add(new AppointmentDecorator(days));
        int red = Color.RED;
        int blue = Color.BLUE;

        final String userID = new SharePreferences(this.getContext()).getPref(Constants.USER_ID);

//        calendarView.addDecorator(new LowMedicineDecorator(days,red));
//        calendarView.addDecorator(new AppointmentDecorator(days,blue));
//        System.out.println("OLD DAYS - " + days);

        OnDateSelectedListener dateListener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                //System.out.println("Hello A new date was selected : " + date);
                Callback callback = new Callback() {
                    @Override
                    public void onValueReceived(final String value) {
                        AlertDialog.Builder daySelectedDialogBuilder = new AlertDialog.Builder(getContext());
                        View daySelectedView = getLayoutInflater().inflate(R.layout.overlay_calendar_select_day, null);
                        daySelectedDialogBuilder.setView(daySelectedView);
                        final AlertDialog daySelectedDialog = daySelectedDialogBuilder.create();

                        TextView txtAptName = daySelectedView.findViewById(R.id.txtAptName);
                        TextView txtAptTime = daySelectedView.findViewById(R.id.txtAptTime);
                        TextView txtAptDate = daySelectedView.findViewById(R.id.txtAptDate);

                        System.out.println("The onValueReceived for Get: " + value);
                        // call method to update view as required using returned value
                        try {
                            JSONArray jsonArray = new JSONArray(value);
                            if (jsonArray.length() == 0) {
                                return;
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String aptName = obj.getString(Constants.APPOINTMENT_NAME);
                                String aptTimeDate = obj.getString(Constants.TIME_OF_APT);
                                String[] aptTimeDateArray = aptTimeDate.split(" ");
                                String aptDate = aptTimeDateArray[0];
                                String aptTime = aptTimeDateArray[1].substring(0, 5);

                                if (aptTime.charAt(0) == '0') {
                                    aptTime = aptTime.substring(1);
                                }

                                txtAptName.setText(aptName);
                                txtAptTime.setText("Time: " + aptTime);
                                txtAptDate.setText("Date: " + aptDate);
                            }
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e);
                            return;
                        }

                        daySelectedDialog.show();
                    }

                    @Override
                    public void onFailure() {
                        System.out.println("I failed :(");
                    }
                };
                DatabaseHelperGet task = new DatabaseHelperGet(null, callback);
                // pass in the saved userID and the CalenderDay
                // TODO: CHECK THAT THE FORMAT OF date IS CORRECT
                int realMonth = date.getMonth() + 1;
                String dateString = date.getYear() + "-" + realMonth + "-" + date.getDay();
                task.execute("http://3.94.171.162:5000/getAppointmentToday/" + userID + "/" + dateString);
                System.out.println("DATES ARE BEING SELECTED");
            }
        };
        calendarView.setOnDateChangedListener(dateListener);

        calendarView.refreshDrawableState();

        Callback callbackGet = new Callback() {
            @Override
            public void onValueReceived(final String value) {
                HashSet<CalendarDay> days = new HashSet<CalendarDay>();

                System.out.println("The onValueReceived for Get : " + value);
                // call method to update view as required using returned value
                try{
                    JSONArray apts = new JSONArray(value);
                    int len = apts.length();
                    for (int i = 0; i < len; i++){
                        JSONObject obj = apts.getJSONObject(i);
                        String timeOfApt = obj.getString("timeOfApt");
                        String[] firstSplit = timeOfApt.split(" ");
                        String dateOnly = firstSplit[0];
                        String[] yearMonthDay = dateOnly.split("-");
                        int year = Integer.parseInt(yearMonthDay[0]);
                        // -1 for indexing bullshit with months
                        int month = Integer.parseInt(yearMonthDay[1]) - 1;
                        int day = Integer.parseInt(yearMonthDay[2]);
                        CalendarDay dateToAdd = new CalendarDay(year,month,day);
                        System.out.println("The dateToAdd was : " + dateToAdd);
                        days.add(dateToAdd);
                    }
                }
                catch (Exception e){
                    System.out.println("ERROR: Getting Appointments : " + e);
                }
                System.out.println("NEW DAYS - " + days);
                int blue = Color.BLUE;
                calendarView.addDecorator(new AppointmentDecorator(days,blue));
                calendarView.refreshDrawableState();
            }

            @Override
            public void onFailure() {
                System.out.println("ERROR: Failed to decorate Calendar days.");
            }
        };
        DatabaseHelperGet taskGet = new DatabaseHelperGet(null, callbackGet);

        taskGet.execute("http://3.94.171.162:5000/getAppointments/" + userID);
    }
}