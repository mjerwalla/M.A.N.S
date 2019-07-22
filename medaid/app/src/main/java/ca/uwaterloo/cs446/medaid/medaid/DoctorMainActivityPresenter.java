package ca.uwaterloo.cs446.medaid.medaid;

public class DoctorMainActivityPresenter {
    private DoctorHelperModel doctorHelperModel;
    private MainActivityPresenter.View view;

    public interface View {
        void updateListView(String patientInfo);
        void reconnectBluetooth();
    }
}
