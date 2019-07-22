package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;

public class InsightsFragmentPresenter {
    private DatabaseHelperModel dbHelperModel;
    private View view;

    public InsightsFragmentPresenter (View view, Context context) {
        this.dbHelperModel = new DatabaseHelperModel(context);
        this.view = view;
    }

    public void getMedications() {
        Callback callback = new Callback() {
            @Override
            public void onValueReceived(String value) {
                view.getBarChart(value);
            }

            @Override
            public void onFailure() {
                System.out.println("Failed to get Medication from Presenter");
            }
        };

        dbHelperModel.getAllMedication(callback);
    }

    public interface View {
        void getBarChart(String m);
    }
}
