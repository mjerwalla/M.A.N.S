package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;

public class BluetoothPresenter {
    private DatabaseHelperModel databaseHelperModel;
    private View view;

    public BluetoothPresenter(View view, Context context) {
        this.view = view;
        this.databaseHelperModel = new DatabaseHelperModel(context);
    }

    public void getMedication() {
    }

    public interface View {
        void updatePatientView();
        void updateDoctorView();
    }
}
