package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayFragmentPresenter {
    private DatabaseHelperModel dbHelperModel;
    private View view;

    public TodayFragmentPresenter(View view, Context context) {
        this.dbHelperModel = new DatabaseHelperModel(context);
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
                        UpcomingMedicine upcomingMedicine = new UpcomingMedicine();
                        upcomingMedicine.medName = obj.getString(Constants.MED_NAME);
                        upcomingMedicine.startDate =
                                new SimpleDateFormat(Constants.DATE_TIME_FORMAT)
                                        .parse(obj.getString(Constants.START_DATE));
                        upcomingMedicine.endDate =
                                new SimpleDateFormat(Constants.DATE_TIME_FORMAT)
                                        .parse(obj.getString(Constants.END_DATE));
                        upcomingMedicine.selectedDaysPerWeek = obj.getString(Constants.SELECTED_DAYS_PER_WEEK).split(",");
                        upcomingMedicine.numTimesPerDay = obj.getInt(Constants.NUM_TIMES_PER_DAY);
                        upcomingMedicine.timesToBeReminded = obj.getString(Constants.TIMES_TO_BE_REMINDED).split(",");
                        upcomingMedicine.dosagePerIntake = obj.getInt(Constants.DOSAGE_PER_INTAKE);
                        upcomingMedicineList.add(upcomingMedicine);
                    }
                    view.updateMedicationListView(upcomingMedicineList);
                }

                catch(Exception e){
                    System.out.println("ERROR: " + e);
                }
                // TODO: Parse timesToBeReminded into an array of times with am/pm
            }

            @Override
            public void onFailure() {
                System.out.println("ERROR: Failed to add new medication.");
            }
        };

        // TODO: Add real userID
        dbHelperModel.getTodayMedication(callback);
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
