package ca.uwaterloo.cs446.medaid.medaid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorMainActivity extends AppCompatActivity implements DoctorMainActivityPresenter.View {
    ArrayAdapter<String> adapter;
    private ListView listView;
    private SearchView searchView;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> duration = new ArrayList<>();
    private DoctorMainActivityPresenter doctorMainActivityPresenter;
    private String patientInfo;//= "[{\"medicationID\": 4, \"userID\": 2, \"medName\": \"Advil\", \"startDate\": \"2019-07-23 09:41:00\", \"endDate\": \"2020-06-22 12:00:00\", \"selectedDaysPerWeek\": \"TUES\", \"numTimesPerDay\": 1, \"timesToBeReminded\": \"9:45\", \"dosagePerIntake\": 1, \"takenInPast\": 0, \"totalNumPills\": 6, \"notes\": \"After Food\"}, {\"medicationID\": 8, \"userID\": 2, \"medName\": \"test\", \"startDate\": \"2015-07-23 00:00:00\", \"endDate\": \"2016-07-23 00:00:00\", \"selectedDaysPerWeek\": \"null\", \"numTimesPerDay\": 0, \"timesToBeReminded\": \"0\", \"dosagePerIntake\": 0, \"takenInPast\": 1, \"totalNumPills\": 0, \"notes\": \"text\"}, {\"medicationID\": 9, \"userID\": 2, \"medName\": \"Tylenol\", \"startDate\": \"2010-11-15 00:00:00\", \"endDate\": \"2010-12-23 00:00:00\", \"selectedDaysPerWeek\": \"null\", \"numTimesPerDay\": 0, \"timesToBeReminded\": \"0\", \"dosagePerIntake\": 0, \"takenInPast\": 1, \"totalNumPills\": 0, \"notes\": \"After dinner\"},{\"vaccinationID\": 4, \"userID\": 2, \"vacName\": \"Chicken Pox\", \"timeOfVac\": \"2019-07-06 00:00:00\"}]";

    // TODO: ADD LOGOUT FUNCTIONALITY

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        Intent intent = getIntent();
        patientInfo = intent.getExtras().getString("patientInfo");

        listView = (ListView) findViewById(R.id.listView);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, names) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView t2 = (TextView) view.findViewById(android.R.id.text2);
                t2.setText(duration.get(position));
                return view;
            }
        };

        adapter.setNotifyOnChange(true);

        listView.setAdapter(adapter);

        searchView = (SearchView) findViewById(R.id.doctorSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        searchView.setQueryHint("Search");

//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                adapter = null;
//                listView.setAdapter(null);
//
//                adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_2, android.R.id.text1, names) {
//                    @NonNull
//                    @Override
//                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                        View view = super.getView(position, convertView, parent);
//                        TextView t2 = (TextView) view.findViewById(android.R.id.text2);
//                        t2.setText(duration.get(position));
//                        return view;
//                    }
//                };
//
//                adapter.setNotifyOnChange(true);
//                listView.setAdapter(adapter);
//
//                return false;
//            }
//        });

        populateList();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (type == "medication") {
//                    Intent p = new Intent(getBaseContext(), PopupActivity.class);
//                    Bundle extras = new Bundle();
//                    String name = listView.getItemAtPosition(i).toString();
//
//                    extras.putString("name", "Name: " + name);
//
//                    try {
//                        for (int j = 0; j < jsonArray.length(); ++j) {
//                            JSONObject explrObject = jsonArray.getJSONObject(j);
//
//                            if (explrObject.getString("medName") == name) {
//                                String start = explrObject.getString("startDate");
//                                String end = explrObject.getString("endDate");
//                                extras.putString("start", "Start Date: " + start.substring(0, 10));
//                                extras.putString("end", "End Date: " + end.substring(0, 10));
//                                extras.putString("selectedDaysPerWeek", explrObject.getString("selectedDaysPerWeek"));
//                                extras.putString("numTimesPerDay", explrObject.getString("numTimesPerDay"));
//                                extras.putString("dosagePerIntake", explrObject.getString("dosagePerIntake"));
//                                extras.putString("takenInPast", explrObject.getString("takenInPast"));
//                                extras.putString("notes", explrObject.getString("notes"));
//                                break;
//                            }
//                        }
//                    } catch (Exception e) {
//                        System.out.println("ERROR: " + e);
//                    }
//                    p.putExtras(extras);
//                    startActivity(p);
//                } else if (type == "report") {
//                    String name = listView.getItemAtPosition(i).toString();
//                    String uri = "";
//
//                    try {
//                        for (int j = 0; j < jsonArray.length(); ++j) {
//                            JSONObject explrObject = jsonArray.getJSONObject(j);
//
//                            if (explrObject.getString("reportName") == name) {
//                                uri = explrObject.getString("uri");
//                                break;
//                            }
//                        }
//                    } catch (Exception e) {
//                        System.out.println("Failed at Line 191");
//                    }
//
//                    Uri path = Uri.parse(uri);
//                    System.out.println(path);
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(path);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    try {
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        System.out.println("Unable to open PDF");
//                    }
//                }
//            }
//        });

        ImageButton bluetoothButton = findViewById(R.id.btnBluetoothConnect);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BluetoothActivity.class);
                startActivity(intent);

//                AlertDialog.Builder bluetoothDialogBuilder = new AlertDialog.Builder(getContext());
//                View bluetoothView = getLayoutInflater().inflate(R.layout.overlay_doctor_bluetooth_request, null);
//                bluetoothDialogBuilder.setView(bluetoothView);
//                final AlertDialog bluetoothDialog = bluetoothDialogBuilder.create();
//                bluetoothDialog.show();
            }
        });
    }

    @Override
    public void updateListView(String patientInfo) {
        // TODO: Update list of medication history
    }

    @Override
    public void reconnectBluetooth() {
        // TODO: Show reconnectBluetooth popup
    }

    private void populateList() {
//        JSONArray infoArray = new JSONArray();
        JSONArray medArray = new JSONArray();
        JSONArray vacArray = new JSONArray();
        names.clear();
        duration.clear();
        System.out.println("PATIEEEENT " + patientInfo);
        String med = "";
        String vac = "";

        if (patientInfo.contains("vaccinationID")) {
            med = patientInfo.substring(0, patientInfo.indexOf("vaccinationID")-3) + "]";
            vac = "[" + patientInfo.substring(patientInfo.indexOf("vaccinationID")-2);
            System.out.println("MED   "+med);
            System.out.println("VACCC   "+vac);
        } else {
            med = patientInfo;
        }

        try {
//            infoArray = new JSONArray(patientInfo);
            medArray = new JSONArray(med);
            vacArray = new JSONArray(vac);
        } catch (Exception e) {
            System.out.println("ERROR Couldn't convert JSON array: " + e);
        }

//        int len = infoArray.length();
//        for (int i = 0; i < len; i++) {
//            JSONObject obj;
//            try {
//                obj = infoArray.getJSONObject(i);
//                String start = obj.getString("startDate");
//                String end = obj.getString("endDate");
//
//                names.add(obj.getString("medName"));
//                duration.add("Start Date: " + start.substring(0, 10) + " End Date: " + end.substring(0, 10));
//            } catch (Exception e) {
//                System.out.println("ERROR Couldn't convert JSON array: " + e);
//            }
//
//            adapter.notifyDataSetChanged();
//        }

        for (int i = 0; i < medArray.length(); i++) {
            JSONObject obj;
            try {
                obj = medArray.getJSONObject(i);
                String start = obj.getString("startDate");
                String end = obj.getString("endDate");

                names.add(obj.getString("medName"));
                duration.add("Start Date: " + start.substring(0, 10) + " End Date: " + end.substring(0, 10));
            } catch (Exception e) {
                System.out.println("ERROR Couldn't convert JSON array: " + e);
            }

            adapter.notifyDataSetChanged();
        }

        for (int i = 0; i < vacArray.length(); i++) {
            JSONObject obj;
            try {
                obj = vacArray.getJSONObject(i);
                String taken = obj.getString("timeOfVac");

                names.add(obj.getString("vacName"));
                duration.add("Taken On: " + taken.substring(0, 10));
            } catch (Exception e) {
                System.out.println("ERROR Couldn't convert JSON array: " + e);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
