package ca.uwaterloo.cs446.medaid.medaid;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayFragmentPresenter {
    private DatabaseHelperModel dbHelperModel;
    private View view;

    public TodayFragmentPresenter(View view) {
        this.dbHelperModel = new DatabaseHelperModel();
        this.view = view;
    }

    public void updateMedicationList() {
        Callback callback = new Callback() {
            @Override
            public void onValueReceived(final String value) {
                List<UpcomingMedicine> upcomingMedicineList = new ArrayList<>();
                JSONArray medListArray;
                try {
                    medListArray = new JSONArray(value);

                    int arrLength = medListArray.length();
                    for (int i = 0; i < arrLength; i++) {
                        JSONObject obj = medListArray.getJSONObject(i);
                        System.out.println("THIS IS EACH JSON OBJ: " + obj);
                    }
                }

                catch(Exception e){
                    System.out.println("ERROR: " + e);
                }
                // TODO: Parse timesToBeReminded into an array of times with am/pm

                view.updateMedicationListView(upcomingMedicineList);
            }

            @Override
            public void onFailure() {
                System.out.println("ERROR: Failed to add new medication.");
            }
        };

        // TODO: Add real userID
        dbHelperModel.getAllMedication("fake-ID", callback);
    }

    public class UpcomingMedicine {
        String medName;
        Date startDate;
        Date endDate;
        String[] selectedDaysPerWeek;
        int numTimesPerDay;
        String[] timesToBeReminded;
        int dosagePerIntake;
    }

    public interface View {
        void updateMedicationListView(List<UpcomingMedicine> upcomingMedicineList);
    }
}
