package ca.uwaterloo.cs446.medaid.medaid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.style.LineBackgroundSpan;

import com.prolificinteractive.materialcalendarview.DayViewFacade;

import static com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS;

public class CustomAppointmentSpan implements LineBackgroundSpan {

    private final float radius;
    private int color;


    public CustomAppointmentSpan(float radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {

        int total = 1;
        int leftMost = (total - 1) * -10;

        for (int i = 0; i < total; i++) {
            int oldColor = paint.getColor();
            paint.setColor(color);
            canvas.drawCircle((left + right) / 2 - leftMost, bottom + radius + 10, radius, paint);
            paint.setColor(oldColor);
            leftMost = leftMost + 20;
        }
    }

}

