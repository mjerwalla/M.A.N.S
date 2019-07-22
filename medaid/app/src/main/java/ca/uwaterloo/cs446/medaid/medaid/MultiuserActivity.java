package ca.uwaterloo.cs446.medaid.medaid;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MultiuserActivity extends AppCompatActivity implements MultiuserActivityPresenter.View {
    private MultiuserActivityPresenter multiuserActivityPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiuser);
        multiuserActivityPresenter = new MultiuserActivityPresenter(this, getBaseContext());

        this.getUserData();

        Button addPatient = findViewById(R.id.addPatient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPatient();
            }
        });

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
//                updatePatientList();

                dialog.hide();
            }
        });
    }

//    public updatePatientList(){
//
//    }

    public void getUserData(){
        multiuserActivityPresenter.getPatients();
    }

    public void addNewPatient(Map<String, String> patient){
        multiuserActivityPresenter.addPatient(patient);
    }
}
