package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;
import android.view.View;

import org.json.JSONArray;

import java.util.Map;

public class MultiuserActivityPresenter {
    private DatabaseHelperModel dbHelperModel;
    private View view;

    public MultiuserActivityPresenter(View view, Context context) {
        this.dbHelperModel = new DatabaseHelperModel(context);
        this.view = view;
    }

    public void getPatients() {
        Callback callback = new Callback() {
            @Override
            public void onValueReceived(String value) {
                System.out.println("Patients: " + value);
            }

            @Override
            public void onFailure() {

            }
        };
    }

    public void addPatient(Map<String, String> patient) {
        Callback callback = new Callback() {
            @Override
            public void onValueReceived(String value) {
                try {
                    JSONArray jsonArray = new JSONArray(value);
                    System.out.println("adding new patient: " + String.valueOf(jsonArray.getJSONObject(0).getString("LAST_INSERT_ID()")));
                    String caretakerID = String.valueOf(jsonArray.getJSONObject(0).getString("LAST_INSERT_ID()"));
                    addPatientToCaretaker(caretakerID);
                } catch (Exception e) {
                    System.out.println("Failed at Presenter class: addPatient");
                }
            }

            @Override
            public void onFailure() {
                System.out.println("Failed to add user from Multiuser Presenter");
            }
        };

        dbHelperModel.addUser(patient, callback);
    }

    public void addPatientToCaretaker(String id) {
        Callback callback = new Callback() {
            @Override
            public void onValueReceived(String value) {
                try {
                    System.out.println("addPatientToCareTaker: " + value);
                } catch (Exception e) {
                    System.out.println("Failed at Presenter class: addPatientToCaretaker");
                }
            }

            @Override
            public void onFailure() {
                System.out.println("Failed to add user from Multiuser Presenter");
            }
        };

        dbHelperModel.addUserToCaretaker(id, callback);
    }

    public interface View {
        void getUserData();
        void addNewPatient(Map<String, String> patient);
    }
}
