package ca.uwaterloo.cs446.medaid.medaid;

import android.content.DialogInterface;
import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class LowMedicineDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;


    public LowMedicineDecorator(Collection<CalendarDay> dates, int color) {
        //this.color = color;
        this.dates = new HashSet<>(dates);

        this.color = color;

    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan((new CustomMedicineSpan(5,color)));


    }

}
