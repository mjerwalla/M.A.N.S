package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
//        CustomCalendarView calendarView = (CustomCalendarView) v.findViewById(R.id.calendar_view);
//        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
//        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
//        List decorators = new ArrayList<>();
//        decorators.add(new ColorDecorator());
//        calendarView.setDecorators(decorators);
//        calendarView.refreshCalendar(currentCalendar);
//
//        calendarView.setCalendarListener(new CalendarListener() {
//            @Override
//            public void onDateSelected(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                Toast.makeText(getContext(), df.format(date), Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onMonthChanged(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
//                Toast.makeText(getContext(), df.format(date), Toast.LENGTH_SHORT).show();
//            }
//        });
        MaterialCalendarView calendarView = (MaterialCalendarView) v.findViewById(R.id.calendarView);
        List decorators = new ArrayList<>();
        CalendarDay day = new CalendarDay();
        HashSet<CalendarDay> days = new HashSet<CalendarDay>();
        days.add(day);
        decorators.add(new EventDecorator(0xFFFF0000, days));
        calendarView.addDecorators(decorators);
        calendarView.refreshDrawableState();

    }

}