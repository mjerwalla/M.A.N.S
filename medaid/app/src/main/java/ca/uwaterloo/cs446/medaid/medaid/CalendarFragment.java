package ca.uwaterloo.cs446.medaid.medaid;

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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    public View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calendar, container, false);
        this.whatev();
        return v;
    }

    public void whatev() {
        MaterialCalendarView calendarView = (MaterialCalendarView) v.findViewById(R.id.calendarView);
        calendarView.setSelectionColor(Color.parseColor("#27CEA7"));
        List decorators = new ArrayList<>();
        CalendarDay day = new CalendarDay();
        HashSet<CalendarDay> days = new HashSet<CalendarDay>();
        days.add(day);
        //decorators.add(new AppointmentDecorator(days));
        int red = Color.RED;
        int green = Color.BLUE;

        calendarView.addDecorator(new LowMedicineDecorator(days,red));
        calendarView.addDecorator(new AppointmentDecorator(days,green));
        OnDateSelectedListener dateListener = new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                System.out.println("Hello A new date was selected : " + date);
                Callback callback = new Callback() {
                    @Override
                    public void onValueReceived(final String value) {
                        System.out.println("The onValueReceived  for Post: " + value);
                        // call method to update view as required using returned value

                    }

                    @Override
                    public void onFailure() {
                        System.out.println("I failed :(");
                    }
                };
                DatabaseHelperPost task = new DatabaseHelperPost(null, callback);
                // pass in the saved userID and the CalenderDay
                //task.execute("http://10.0.2.2/eventsOnThisDay/" + userID + "/" + );
                    }
                };
                calendarView.setOnDateChangedListener(dateListener);
                calendarView.refreshDrawableState();
    }

}