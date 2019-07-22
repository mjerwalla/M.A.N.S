package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;

public class CalendarFragmentPresenter {
    private DatabaseHelperModel dbHelperModel;
    private View view;

    public CalendarFragmentPresenter(View view, Context context) {
        this.dbHelperModel = new DatabaseHelperModel(context);
        this.view = view;
    }

    public interface View {
        void addAppointment();
        void addLowMedicineEvent();
        void updateCalendar();
    }
}
