package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryFragment extends Fragment {
    ArrayAdapter<String> adapter;
    private ListView listView;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> duration = new ArrayList<>();
    TabLayout tabLayout;
    SearchView searchView;
    Button addButton;
    View view;
    String type = "medication";
    JSONArray jsonArray;
    String userID;

    String m = "[{'rowNum': '22', 'userID': '1', 'medName': 'Tylenol', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Advil', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Adderall', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Xanax', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Ibuprofen', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Advil Flu and Cold', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Evening Snack'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Antibiotic', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Penicillin', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Dinner'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Panadol', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Breakfast'},\n" +
            "{'rowNum': 23', 'userID': '1', 'medName': 'Buckleys', 'startDate': '2019-07-02 00:00', 'endDate': '2019-08-01 00:00', 'selectedDaysPerWeek': 'MON,WED,FRI', 'numTimesPerDay': '3', 'timesToBeReminded': '8:00,14:00,21:00', 'takenWith': 'Lunch'}]";

    String v = "[{'userID':'1', 'vacName':'Chicken Pox', 'dateTaken':'2009-04-12 00:00'}, {'userID':'1', 'vacName':'Tetanus', 'dateTaken':'2018-10-22 00:00'}]";

    String r = "[]";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_history_new, container, false);

        try {
            jsonArray = new JSONArray(m);

            System.out.println(jsonArray);

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                userID = explrObject.getString("userID");
                System.out.println(explrObject.getString("medName"));
                names.add(explrObject.getString("medName"));

                String start = explrObject.getString("startDate");
                String end = explrObject.getString("endDate");

                duration.add("Start Date: " + start.substring(0, 10) + " End Date: " + end.substring(0, 10));
                System.out.println(names);
            }

        } catch (Exception e) {
            System.out.println("Failed");
        }

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                System.out.println(tabPosition);

                switch (tabPosition) {
                    case 0:
                        type = "medication";
                        break;
                    case 1:
                        type = "vaccination";
                        break;
                    case 2:
                        type = "report";
                        break;
                }

                updateView(type);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        listView = (ListView) view.findViewById(R.id.listView);


        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, names) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
//                    TextView t1 = (TextView) view.findViewById(android.R.id.text1);
                TextView t2 = (TextView) view.findViewById(android.R.id.text2);
//                    t1.setText(names.get(position));
                t2.setText(duration.get(position));
                return view;
            }
        };

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (type == "medication") {
                    Intent p = new Intent(getContext(), PopupActivity.class);
                    Bundle extras = new Bundle();
                    String name = listView.getItemAtPosition(i).toString();

                    extras.putString("name", "Name: " + name);

                    try {
                        for (int j = 0; j < jsonArray.length(); ++j) {
                            JSONObject explrObject = jsonArray.getJSONObject(j);

                            if (explrObject.getString("medName") == name) {
                                System.out.println("YEEEES");
                                String start = explrObject.getString("startDate");
                                String end = explrObject.getString("endDate");
                                extras.putString("start", "Start Date: " + start.substring(0, 10));
                                extras.putString("end", "End Date: " + end.substring(0, 10));
                                extras.putString("daysPerWeek", explrObject.getString("selectedDaysPerWeek"));
                                extras.putString("numTimesPerDay", explrObject.getString("numTimesPerDay"));
                                extras.putString("takenWith", explrObject.getString("takenWith"));
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Failed at Line 212");
                    }
                    p.putExtras(extras);
                    startActivity(p);
                }
            }
        });

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (names.contains(query)) {
                    adapter.getFilter().filter(query);
                    return true;
                }
//                else {
//                    Toast toast = Toast.makeText(getApplicationContext(), "No Match Found", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();
//                    adapter.getFilter().filter(query);
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == "medication") {
                    addMedPopUp();
                } else if (type == "vaccination") {
                    addVacPopUp();
                } else if (type == "report") {
//                    addReportPopUp();
                }
            }
        });

        return view;
    }

    public View updateView(String type) {
        System.out.println(("UPDATE VIEW"));

        listView = (ListView) view.findViewById(R.id.listView);


        if (type == "medication") {
            System.out.println(("medication"));
            names.clear();
            duration.clear();
            try {
                jsonArray = new JSONArray(m);

                System.out.println(jsonArray);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    names.add(explrObject.getString("medName"));

                    String start = explrObject.getString("startDate");
                    String end = explrObject.getString("endDate");

                    duration.add("Start Date: " + start.substring(0, 10) + " End Date: " + end.substring(0, 10));
                    System.out.println(names);
                }

            } catch (Exception e) {
                System.out.println("Failed");
            }
        } else if (type == "vaccination") {
            System.out.println(("vaccination"));
            names.clear();
            duration.clear();
            try {
                jsonArray = new JSONArray(v);

                System.out.println(jsonArray);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    names.add(explrObject.getString("vacName"));

                    String taken = explrObject.getString("dateTaken");

                    duration.add("Taken On: " + taken.substring(0, 10));
                }
            } catch (Exception e) {
                System.out.println("Failed");
            }

        } else {
            jsonArray = null;
            names.clear();
            duration.clear();
            System.out.println("Hiiii");
        }

        adapter.notifyDataSetChanged();

        return view;

    }


    public void addMedPopUp() {
        AlertDialog.Builder medPopupBuilder = new AlertDialog.Builder(this.getContext());
        View medPopupView = getLayoutInflater().inflate(R.layout.history_add_medication, null);

        medPopupBuilder.setView(medPopupView);
        final AlertDialog dialog = medPopupBuilder.create();
        dialog.show();

        final TextView medName = medPopupView.findViewById(R.id.addMed);
        final TextView startDay = medPopupView.findViewById(R.id.day1);
        final TextView startMonth = medPopupView.findViewById(R.id.month1);
        final TextView startYear = medPopupView.findViewById(R.id.year1);
        final TextView endDay = medPopupView.findViewById(R.id.day2);
        final TextView endMonth = medPopupView.findViewById(R.id.month2);
        final TextView endYear = medPopupView.findViewById(R.id.year2);
        final TextView notes = medPopupView.findViewById(R.id.instructions);

        Button submitNewMedButton = medPopupView.findViewById(R.id.addMedSubmit);

        submitNewMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ADDDDDING");
                System.out.println(medName.getText().toString());
                System.out.println(startYear.getText().toString() + "-" + startMonth.getText().toString() + "-" + startDay.getText().toString() + " 00:00");
                System.out.println(endYear.getText().toString() + "-" + endMonth.getText().toString() + "-" + endDay.getText().toString() + " 00:00");
                System.out.println(notes.getText().toString());

                JSONObject med = new JSONObject();
                try {
                    med.put("rowNum", "1");
                    med.put("userID", userID);
                    med.put("medName", medName.getText().toString());
                    med.put("startDate", startYear.getText().toString() + "-" + startMonth.getText().toString() + "-" + startDay.getText().toString() + " 00:00");
                    med.put("endDate", endYear.getText().toString() + "-" + endMonth.getText().toString() + "-" + endDay.getText().toString() + " 00:00");
                    med.put("selectedDaysPerWeek", "");
                    med.put("numTimesPerDay", "");
                    med.put("timesToBeReminded", "");
                    med.put("takenWith", notes.getText().toString());
                } catch (Exception e) {
                    System.out.println("Failed at Add medication from history");
                }

                System.out.println(med);

                m = m.substring(0, m.length()-1) + "," + med.toString() + "]";

                updateView("medication");

//                addToMedicationTable(med);

                dialog.hide();
            }
        });
    }

    public void addVacPopUp() {
        AlertDialog.Builder vacPopupBuilder = new AlertDialog.Builder(this.getContext());
        View vacPopupView = getLayoutInflater().inflate(R.layout.history_add_vaccination, null);

        vacPopupBuilder.setView(vacPopupView);
        final AlertDialog dialog = vacPopupBuilder.create();
        dialog.show();

        final TextView vacName = vacPopupView.findViewById(R.id.addVac);
        final TextView takenDay = vacPopupView.findViewById(R.id.day3);
        final TextView takenMonth = vacPopupView.findViewById(R.id.month3);
        final TextView takenYear = vacPopupView.findViewById(R.id.year3);

        Button submitNewVacButton = vacPopupView.findViewById(R.id.addVacSubmit);

        submitNewVacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ADDDDDING");
                System.out.println(vacName.getText().toString());
                System.out.println(takenDay.getText().toString() + "-" + takenMonth.getText().toString() + "-" + takenYear.getText().toString() + " 00:00");
                JSONObject vac = new JSONObject();
                try {
//                    vac.put("rowNum", "1");
                    vac.put("userID", userID);
                    vac.put("vacName", vacName.getText().toString());
                    vac.put("dateTaken", takenDay.getText().toString() + "-" + takenMonth.getText().toString() + "-" + takenYear.getText().toString() + " 00:00");
                } catch (Exception e) {
                    System.out.println("Failed at Add vaccination from history");
                }

                System.out.println(vac);

                v = v.substring(0, v.length()-1) + "," + vac.toString() + "]";

                updateView("vaccination");

//                addToVaccinationTable(vac);

                dialog.hide();
            }
        });
    }
}