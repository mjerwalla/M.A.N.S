package ca.uwaterloo.cs446.medaid.medaid;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class AppointmentDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;


    public AppointmentDecorator(Collection<CalendarDay> dates, int color) {
        //this.color = color;
        this.dates = new HashSet<>(dates);

        this.color = color;

    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {

        if (dates.contains(day)){
            System.out.println("The day matches one in the collection DECORATE IT: " + day);
        }
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan((new CustomAppointmentSpan(5,color)));

    }
}
