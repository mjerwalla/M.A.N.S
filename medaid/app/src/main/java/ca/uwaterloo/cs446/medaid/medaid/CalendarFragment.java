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

public class CalendarFragment extends Fragment {
    private View v;
    private CalendarActivityDBHelper medDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_today, container, false);
        medDb = new CalendarActivityDBHelper(getContext());
        this.UpdateMedList();

        return v;
    }

    private void UpdateMedList() {
        LinearLayout linearLayout = v.findViewById(R.id.linearLayout);
        List<CalendarActivityDBHelper.MyData> data = medDb.getAllData();

        for (CalendarActivityDBHelper.MyData myData: data) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.upcoming_med_block, linearLayout, false);

            if (rowView.getParent() != null) {
                ((ViewGroup)rowView.getParent()).removeView(rowView);
            }
            linearLayout.addView(rowView);

            TextView medName = rowView.findViewById(R.id.txtMedName);
            medName.setText(myData.column2);

            TextView time = rowView.findViewById(R.id.txtTime);
            time.setText(myData.column3.charAt(0) + ":00am");

            TextView dosage = rowView.findViewById(R.id.txtDosage);
            dosage.setText(myData.column7 + " pills");

            // Set button ID
            Button takenButton = rowView.findViewById(R.id.btnTaken);
            takenButton.setId(Integer.parseInt(myData.column0));
        }
    }
}