package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(value);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject x = jsonArray.getJSONObject(i);
                        String fullName = x.getString("firstName") + " " + x.getString("lastName");
                        names.add(fullName);
                        ids.add(x.getString("patientID"));
                        view.displayPatients(names, ids);
                    }

                } catch (Exception e)
                {
                    System.out.println("Failed to create JSONArray");
                }
            }

            @Override
            public void onFailure() {
                System.out.println("Failed to getPatients from Multiuser Presenter");
            }
        };

        dbHelperModel.getPatients(callback);
    }

    public void addPatient(Map<String, String> patient) {
        Callback callback = new Callback() {
            @Override
            public void onValueReceived(String value) {
                try {
                    JSONArray jsonArray = new JSONArray(value);
                    String patientID = String.valueOf(jsonArray.getJSONObject(0).getString("LAST_INSERT_ID()"));
                    System.out.println("addPatient " + patientID);
                    addPatientToCaretaker(patientID);
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
                getPatients();
                System.out.println("addPatientToCaretaker " + value);
            }

            @Override
            public void onFailure() {
                System.out.println("Failed to add user to CareTaker from Multiuser Presenter");
            }
        };

        dbHelperModel.addUserToCaretaker(id, callback);
    }

    public interface View {
        void getUserData();
        void addNewPatient(Map<String, String> patient);
        void displayPatients(ArrayList<String> names, ArrayList<String> ids);
    }
}
