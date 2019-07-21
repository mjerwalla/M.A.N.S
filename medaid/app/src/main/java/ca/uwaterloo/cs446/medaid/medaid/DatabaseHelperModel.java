package ca.uwaterloo.cs446.medaid.medaid;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelperModel {
    private String fakeUserId = "25";
    public void addNewMedication(
            String userID,
            String medName,
            String startDate,
            String endDate,
            String selectedDaysPerWeek,
            String numTimesPerDay,
            String timesToBeReminded,
            Callback callback) {
        Map<String, String> postData = new HashMap<>();

        // TODO: Use real userID
        postData.put(Constants.USER_ID, fakeUserId);
        postData.put(Constants.MED_NAME, medName);
        postData.put(Constants.START_DATE, startDate);
        postData.put(Constants.END_DATE, endDate);
        postData.put(Constants.SELECTED_DAYS_PER_WEEK, selectedDaysPerWeek);
        postData.put(Constants.NUM_TIMES_PER_DAY, numTimesPerDay);
        postData.put(Constants.TIMES_TO_BE_REMINDED, timesToBeReminded);

        DatabaseHelperPost task = new DatabaseHelperPost(postData, callback);
        task.execute("http://3.94.171.162:5000/addMedication");
    }

    public void addPastMedication() {

    }

    public void deleteMedication() {

    }

    public void getAllMedication(String userID, Callback callback) {
        DatabaseHelperGet task = new DatabaseHelperGet(null, callback);

        // TODO: Use real userID
        task.execute("http://3.94.171.162:5000/getCurrentMeds/" + fakeUserId);
    }

    public String getTodayMedication() {
        return null;
    }

    public String getVaccinations() {
        return null;
    }

    public String getReports() {
        return null;
    }
}
