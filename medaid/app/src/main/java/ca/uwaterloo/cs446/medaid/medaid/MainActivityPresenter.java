package ca.uwaterloo.cs446.medaid.medaid;

import android.app.Fragment;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class MainActivityPresenter {
    private DatabaseHelperModel dbHelperModel;
    private View view;

    public interface View {
        void updateTodayMedicationList();
        void updateCalendar();
        void updateHistoryMedication();
        void updateHistoryVaccination();
        void updateHistoryReports();
        void updateInsights();
    }

    public MainActivityPresenter(View view, Context context) {
        this.dbHelperModel = new DatabaseHelperModel(context);
        this.view = view;
    }

    public void addMedication(
            String medName,
            String startDate,
            String endDate,
            String selectedDaysPerWeek,
            String numTimesPerDay,
            String timesToBeReminded,
            String dosagePerIntake,
            String totalNumPills,
            String notes) {
        Callback callback = new Callback() {
            @Override
            public void onValueReceived(final String value) {
                view.updateTodayMedicationList();
            }

            @Override
            public void onFailure() {
                System.out.println("ERROR: Failed to add new medication.");
            }
        };

        dbHelperModel.addNewMedication(
                medName,
                startDate,
                endDate,
                selectedDaysPerWeek,
                numTimesPerDay,
                timesToBeReminded,
                dosagePerIntake,
                totalNumPills,
                notes,
                callback);
    }
}
