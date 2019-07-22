package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TodayFragment extends Fragment implements TodayFragmentPresenter.View{
    private View v;
    private TodayFragmentPresenter todayFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_today, container, false);
        todayFragmentPresenter = new TodayFragmentPresenter(this);
        this.updateMedicationListEverywhere();

        return v;
    }

    @Override
    public void updateMedicationListView(List<TodayFragmentPresenter.UpcomingMedicine> upcomingMedicines) {
        LinearLayout linearLayout = v.findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        int idCounter = 0;
        for (TodayFragmentPresenter.UpcomingMedicine med: upcomingMedicines) {
            idCounter++;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.upcoming_med_block, linearLayout, false);

            if (rowView.getParent() != null) {
                ((ViewGroup)rowView.getParent()).removeView(rowView);
            }
            linearLayout.addView(rowView);

            TextView medName = rowView.findViewById(R.id.txtMedName);
            medName.setText(med.medName);

            // TODO: Create new medicine reminder for each time
            TextView time = rowView.findViewById(R.id.txtTime);
            time.setText(med.timesToBeReminded[0]);

            TextView dosage = rowView.findViewById(R.id.txtDosage);
            dosage.setText(med.dosagePerIntake + " pills");

            // Set button ID
            Button takenButton = rowView.findViewById(R.id.btnTaken);
            takenButton.setId(R.id.btnTaken + idCounter);
        }
    }

    public void updateMedicationListEverywhere() {
        todayFragmentPresenter.updateMedicationList();
    }
}