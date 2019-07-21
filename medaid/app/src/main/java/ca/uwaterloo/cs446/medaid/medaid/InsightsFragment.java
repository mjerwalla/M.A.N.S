package ca.uwaterloo.cs446.medaid.medaid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class InsightsFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_insights, container, false);

        final TextView text1 = (TextView) view.findViewById(R.id.medication1);
        final TextView text2 = (TextView) view.findViewById(R.id.medication2);

        Button submitConflict = view.findViewById(R.id.checkConflictButton);

        submitConflict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String med1 = text1.getText().toString();
                String med2 = text2.getText().toString();

                System.out.println(med1);
                System.out.println(med2);

                Intent intent = new Intent(getContext(), ConflictsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("med1", med1);
                extras.putString("med2", med2);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Button foodConflict = view.findViewById(R.id.foodButton);

        final TextView foodText = (TextView) view.findViewById(R.id.foodMed);

        foodConflict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String med = foodText.getText().toString();

                System.out.println(med);

                Intent intent = new Intent(getContext(), ConflictsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("med1", med);
                extras.putString("med2", "");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        return view;
    }
}