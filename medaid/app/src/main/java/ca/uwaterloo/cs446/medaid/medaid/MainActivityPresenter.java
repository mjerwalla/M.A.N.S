package ca.uwaterloo.cs446.medaid.medaid;

import android.app.Fragment;

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
    }

    public MainActivityPresenter(View view) {
        this.dbHelperModel = new DatabaseHelperModel();
        this.view = view;
    }

    public void addMedication(
            String userID,
            String medName,
            String startDate,
            String endDate,
            String selectedDaysPerWeek,
            String numTimesPerDay,
            String timesToBeReminded,
            String dosagePerIntake) {
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
                userID,
                medName,
                startDate,
                endDate,
                selectedDaysPerWeek,
                numTimesPerDay,
                timesToBeReminded,
                callback);
    }
}
