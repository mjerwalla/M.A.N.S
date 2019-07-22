package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiuserActivity extends AppCompatActivity implements MultiuserActivityPresenter.View {
    private MultiuserActivityPresenter multiuserActivityPresenter;
    ArrayAdapter<String> adapter;
    ListView listView;
    SearchView searchView;
    SharePreferences mainPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiuser);
        multiuserActivityPresenter = new MultiuserActivityPresenter(this, getBaseContext());
        mainPref = new SharePreferences(this);

        this.getUserData();

        Button addPatient = findViewById(R.id.addPatient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPatient();
            }
        });

        listView = (ListView) findViewById(R.id.patientList);
        searchView = (SearchView) findViewById(R.id.searchPatient);
    }

    public void addNewPatient() {
        AlertDialog.Builder patientPopupBuilder = new AlertDialog.Builder(this);
        View patientPopupView = getLayoutInflater().inflate(R.layout.multiuser_add_patient, null);

        patientPopupBuilder.setView(patientPopupView);
        final AlertDialog dialog = patientPopupBuilder.create();
        dialog.show();

        final TextView firstName = patientPopupView.findViewById(R.id.patientFirstName);
        final TextView lastName = patientPopupView.findViewById(R.id.patientLastName);

        Button submitPatientButton = patientPopupView.findViewById(R.id.addPatientButton);

        submitPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> patient = new HashMap<>();
                patient.put("userName", "abc@abc.com");
                patient.put("password", "abcdef");
                patient.put("firstName", firstName.getText().toString());
                patient.put("lastName", lastName.getText().toString());
                patient.put("userType", "0");
                addNewPatient(patient);

                dialog.hide();
            }
        });
    }

    public void getUserData() {
        multiuserActivityPresenter.getPatients();
    }

    public void addNewPatient(Map<String, String> patient) {
        multiuserActivityPresenter.addPatient(patient);
    }

    public void displayPatients(final ArrayList<String> names, final ArrayList<String> ids) {

        final String parentID = mainPref.getPref(Constants.USER_ID);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, names) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView t2 = (TextView) view.findViewById(android.R.id.text2);
                t2.setText("Patient ID: " + ids.get(position));
                return view;
            }
        };

        listView.setAdapter(adapter);

        final SharePreferences sP = new SharePreferences(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fullName = listView.getItemAtPosition(i).toString();
                String id = "";
                for (int j = 0; j < names.size(); ++j) {
                    if (names.get(j).equals(fullName)) {
                        id = ids.get(j);
                        break;
                    }
                }
                sP.modifyPref("userID",id);
                sP.modifyPref("userType","0");
                sP.modifyPref("parentID", parentID);

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
