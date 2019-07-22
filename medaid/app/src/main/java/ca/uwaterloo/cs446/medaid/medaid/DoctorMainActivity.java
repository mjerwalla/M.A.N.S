package ca.uwaterloo.cs446.medaid.medaid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DoctorMainActivity extends AppCompatActivity implements DoctorMainActivityPresenter.View {
    private DoctorMainActivityPresenter doctorMainActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        AlertDialog.Builder bluetoothConnectPopupBuilder = new AlertDialog.Builder(DoctorMainActivity.this);
        View bluetoothRequestView = getLayoutInflater().inflate(R.layout.overlay_doctor_bluetooth_request, null);
        bluetoothConnectPopupBuilder.setView(bluetoothRequestView);
        AlertDialog bluetoothConnectDialog = bluetoothConnectPopupBuilder.create();
        bluetoothConnectDialog.show();

        final Button connectButton = bluetoothRequestView.findViewById(R.id.btnBluetoothConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Check/request for bluetooth connection with patient
            }
        });
    }

    @Override
    public void updateView(String patientInfo) {
        // TODO: Update list of medication history
    }
}
