package ca.uwaterloo.cs446.medaid.medaid;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DoctorMainActivity extends AppCompatActivity implements DoctorMainActivityPresenter.View {
    private DoctorMainActivityPresenter doctorMainActivityPresenter;

    // TODO: ADD LOGOUT FUNCTIONALITY

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_doctor_main);
//
//        AlertDialog.Builder bluetoothConnectPopupBuilder = new AlertDialog.Builder(DoctorMainActivity.this);
//        View bluetoothRequestView = getLayoutInflater().inflate(R.layout.overlay_doctor_bluetooth_request, null);
//        bluetoothConnectPopupBuilder.setView(bluetoothRequestView);
//        AlertDialog bluetoothConnectDialog = bluetoothConnectPopupBuilder.create();
//        bluetoothConnectDialog.show();
//
//        final Button connectButton = bluetoothRequestView.findViewById(R.id.btnConnect);
//        connectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: Connect with patient
//
//                // TODO: Once connected, close the pop-up and populate the lists
//            }
//        });
//
//        final ImageButton circleBluetoothButton = this.findViewById(R.id.btnBluetoothConnect);
//        circleBluetoothButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: Disconnect with current patient and be available to connect with another
//            }
//        });
    }

    @Override
    public void updateListView(String patientInfo) {
        // TODO: Update list of medication history
    }

    @Override
    public void reconnectBluetooth() {
        // TODO: Show reconnectBluetooth popup
    }
}
