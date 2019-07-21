package ca.uwaterloo.cs446.medaid.medaid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class InsightsFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_insights, container, false);

        Button submitConflict = view.findViewById(R.id.checkConflictButton);

        submitConflict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView text1 = (TextView) view.findViewById(R.id.medication1);
                TextView text2 = (TextView) view.findViewById(R.id.medication2);

//                String med1 = text1.getText().toString();
//                String med2 = text2.getText().toString();

//                System.out.println(med1);
//                System.out.println(med2);
            }
        });
        return view;
    }
}


