package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelperModel {
    private Context context;
    final private String userID;

    public DatabaseHelperModel(Context context) {
        this.context = context;
        this.userID = new SharePreferences(this.context).getPref(Constants.USER_ID);
    }

    public void addNewMedication(
            String medName,
            String startDate,
            String endDate,
            String selectedDaysPerWeek,
            String numTimesPerDay,
            String timesToBeReminded,
            String dosagePerIntake,
            String totalNumPills,
            String notes,
            Callback callback) {
        Map<String, String> postData = new HashMap<>();

        postData.put(Constants.USER_ID, this.userID);
        postData.put(Constants.MED_NAME, medName);
        postData.put(Constants.START_DATE, startDate);
        postData.put(Constants.END_DATE, endDate);
        postData.put(Constants.SELECTED_DAYS_PER_WEEK, selectedDaysPerWeek);
        postData.put(Constants.NUM_TIMES_PER_DAY, numTimesPerDay);
        postData.put(Constants.TIMES_TO_BE_REMINDED, timesToBeReminded);
        postData.put(Constants.DOSAGE_PER_INTAKE, dosagePerIntake);
        postData.put(Constants.TAKEN_IN_PAST, "0");
        postData.put(Constants.TOTAL_NUM_PILLS, totalNumPills);
        postData.put(Constants.NOTES, notes);

        DatabaseHelperPost task = new DatabaseHelperPost(postData, callback);
        task.execute("http://3.94.171.162:5000/addMedication");
    }

    public void deleteMedication() {

    }

    public void getAllMedication(Callback callback) {
        DatabaseHelperGet task = new DatabaseHelperGet(null, callback);
        task.execute("http://3.94.171.162:5000/getUserMedicalHistory/" + this.userID);
    }

    public void getTodayMedication(Callback callback) {

        DatabaseHelperGet task = new DatabaseHelperGet(null, callback);

        // TODO: Use real userID
        task.execute("http://3.94.171.162:5000/getCurrentMeds/" + this.userID);
    }

    public void addUser(Map<String, String> user, Callback callback) {
        DatabaseHelperPost task = new DatabaseHelperPost(user, callback);
        task.execute("http://3.94.171.162:5000/addUser");
    }

    public void addUserToCaretaker(String patientID, Callback callback) {
        Map<String, String> postData = new HashMap<>();
        postData.put("careTakerID", this.userID);
        postData.put("patientID", patientID);
        System.out.println(postData);
        DatabaseHelperPost task = new DatabaseHelperPost(postData, callback);
        task.execute("http://3.94.171.162:5000/addPatientToCareTaker");
    }

    public void getPatients(Callback callback) {
        DatabaseHelperGet task = new DatabaseHelperGet(null, callback);
        task.execute("http://3.94.171.162:5000/getMultiUserInfo/" + this.userID);
    }
}
