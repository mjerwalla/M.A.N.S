package ca.uwaterloo.cs446.medaid.medaid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.DatePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    TextView uriText;
    TextView pdfFile;
    SharePreferences preferences;

    Callback callbackGet = new Callback() {
        @Override
        public void onValueReceived(final String value) {
            System.out.println("The onValueReceived for Get : " + value);
            System.out.println("The type is " + type);
            updateViewFromCallback(type, value);
        }

        @Override
        public void onFailure() {
            System.out.println("Failed at callbackGet");
        }
    };

    Callback callbackPost = new Callback() {
        @Override
        public void onValueReceived(final String value) {
            System.out.println("The onValueReceived for Post : " + value);
            System.out.println("The type is " + type);
            updateViewFromCallback(type, value);
        }

        @Override
        public void onFailure() {
            System.out.println("Failed at callbackPost");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_history_new, container, false);
        preferences = new SharePreferences(this.getContext());
        userID = preferences.getPref("userID");

        final DatabaseHelperGet taskGet = new DatabaseHelperGet(null, callbackGet);
        taskGet.execute("http://3.94.171.162:5000/getUserMedicalHistory/" + userID);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                System.out.println(tabPosition);
                DatabaseHelperGet get = new DatabaseHelperGet(null, callbackGet);

                switch (tabPosition) {
                    case 0:
                        type = "medication";
                        get.execute("http://3.94.171.162:5000/getUserMedicalHistory/" + userID);
                        break;
                    case 1:
                        type = "vaccination";
                        get.execute("http://3.94.171.162:5000/getVaccinations/" + userID);
                        break;
                    case 2:
                        type = "report";
                        get.execute("http://3.94.171.162:5000/getReports/" + userID);
                        break;
                }
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
                TextView t2 = (TextView) view.findViewById(android.R.id.text2);
                t2.setText(duration.get(position));
                return view;
            }
        };

        adapter.setNotifyOnChange(true);

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
                                String start = explrObject.getString("startDate");
                                String end = explrObject.getString("endDate");
                                extras.putString("start", "Start Date: " + start.substring(0, 10));
                                extras.putString("end", "End Date: " + end.substring(0, 10));
                                extras.putString("selectedDaysPerWeek", explrObject.getString("selectedDaysPerWeek"));
                                extras.putString("numTimesPerDay", explrObject.getString("numTimesPerDay"));
                                extras.putString("dosagePerIntake", explrObject.getString("dosagePerIntake"));
                                extras.putString("takenInPast", explrObject.getString("takenInPast"));
                                extras.putString("notes", explrObject.getString("notes"));
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Failed at Line 212");
                    }
                    p.putExtras(extras);
                    startActivity(p);
                } else if (type == "report") {
                    String name = listView.getItemAtPosition(i).toString();
                    String uri = "";

                    try {
                        for (int j = 0; j < jsonArray.length(); ++j) {
                            JSONObject explrObject = jsonArray.getJSONObject(j);

                            if (explrObject.getString("reportName") == name) {
                                uri = explrObject.getString("uri");
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Failed at Line 191");
                    }

                    Uri path = Uri.parse(uri);
                    System.out.println(path);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        System.out.println("Unable to open PDF");
                    }
                }
            }
        });

        searchView = (SearchView) view.findViewById(R.id.searchView);
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

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter = null;
                listView.setAdapter(null);

                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, names) {
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

                return false;
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
                    addReportPopUp();
                }
            }
        });

        return view;
    }


    public View updateViewFromCallback(String type, String data) {
        System.out.println(("UPDATE VIEW FROM CALLBACK"));

        listView = (ListView) view.findViewById(R.id.listView);

        if (type == "medication") {
            System.out.println(("medication"));
            names.clear();
            duration.clear();
            try {
                jsonArray = new JSONArray(data);

                System.out.println(jsonArray);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    names.add(explrObject.getString("medName"));

                    String start = explrObject.getString("startDate");
                    String end = explrObject.getString("endDate");

                    duration.add("Start Date: " + start.substring(0, 10) + " End Date: " + end.substring(0, 10));
                }

            } catch (Exception e) {
                System.out.println("Failed at update medication");
            }
        }

        else if (type == "vaccination") {
            System.out.println(("vaccination"));
            names.clear();
            duration.clear();
            try {
                jsonArray = new JSONArray(data);

                System.out.println(jsonArray);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    names.add(explrObject.getString("vacName"));

                    String taken = explrObject.getString("timeOfVac");

                    duration.add("Taken On: " + taken.substring(0, 10));
                }
            } catch (Exception e) {
                System.out.println("Failed at update vaccination");
            }

        }

        else if (type == "report") {
            System.out.println(("report"));
            names.clear();
            duration.clear();
            try {
                jsonArray = new JSONArray(data);

                System.out.println(jsonArray);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    names.add(explrObject.getString("reportName"));
                    duration.add("File Name: " + explrObject.getString("pdfName"));
                }
            } catch (Exception e) {
                System.out.println("Failed at update report");
            }
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
        final DatePicker start = medPopupView.findViewById(R.id.datePickerStart);
        final DatePicker end = medPopupView.findViewById(R.id.datePickerEnd);
        final TextView notes = medPopupView.findViewById(R.id.instructions);

        Button submitNewMedButton = medPopupView.findViewById(R.id.addMedSubmit);

        submitNewMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Adding Medication");

                Map<String, String> med = new HashMap<>();
                try {
                    med.put("userID", userID);
                    med.put("medName", medName.getText().toString());
                    String dayS = String.valueOf(start.getDayOfMonth());
                    String monthS = String.valueOf(start.getMonth() + 1);
                    if (dayS.length() == 1) {
                        dayS = "0" + dayS;
                    }
                    if (monthS.length() == 1) {
                        monthS = "0" + monthS;
                    }
                    String dayE = String.valueOf(end.getDayOfMonth());
                    String monthE = String.valueOf(end.getMonth() + 1);
                    if (dayE.length() == 1) {
                        dayE = "0" + dayE;
                    }
                    if (monthE.length() == 1) {
                        monthE = "0" + monthE;
                    }
                    med.put("startDate", start.getYear() + "-" + monthS + "-" + dayS + " 00:00");
                    med.put("endDate", end.getYear() + "-" + monthE + "-" + dayE + " 00:00");
                    med.put("selectedDaysPerWeek", "null");
                    med.put("numTimesPerDay", "0");
                    med.put("timesToBeReminded", "0");
                    med.put("dosagePerIntake", "0");
                    med.put("takenInPast", "1");
                    med.put("totalNumPills", "0");
                    med.put("notes", notes.getText().toString());
                } catch (Exception e) {
                    System.out.println("Failed at Add medication from history");
                }

                System.out.println(med);

                DatabaseHelperPost taskPost = new DatabaseHelperPost(med, callbackPost);
                taskPost.execute("http://3.94.171.162:5000/addMedication");

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
        final DatePicker date = vacPopupView.findViewById(R.id.datePickerVac);

        Button submitNewVacButton = vacPopupView.findViewById(R.id.addVacSubmit);

        submitNewVacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Adding vaccination");
                Map<String, String> vac = new HashMap<>();
                try {
                    vac.put("userID", userID);
                    vac.put("vacName", vacName.getText().toString());
                    String day = String.valueOf(date.getDayOfMonth());
                    String month = String.valueOf(date.getMonth() + 1);
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    vac.put("timeOfVac", date.getYear() + "-" + month + "-" + day + " 00:00");
                } catch (Exception e) {
                    System.out.println("Failed at Add vaccination from history");
                }

                System.out.println(vac);

                DatabaseHelperPost taskPost = new DatabaseHelperPost(vac, callbackPost);
                taskPost.execute("http://3.94.171.162:5000/addVaccination");

                dialog.hide();
            }
        });
    }

    public void addReportPopUp() {
        AlertDialog.Builder repPopupBuilder = new AlertDialog.Builder(this.getContext());
        View repPopupView = getLayoutInflater().inflate(R.layout.history_add_report, null);

        repPopupBuilder.setView(repPopupView);
        final AlertDialog dialog = repPopupBuilder.create();
        dialog.show();

        final TextView repName = repPopupView.findViewById(R.id.addRep);

        Button browseButton = repPopupView.findViewById(R.id.browseButton);

        pdfFile = (TextView) repPopupView.findViewById(R.id.pdfName);
        uriText = (TextView) repPopupView.findViewById(R.id.uriName);

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Open pdf dialog");

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");

                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
            }
        });

        final TextView pdfName = repPopupView.findViewById(R.id.pdfName);
        final TextView uriName = repPopupView.findViewById(R.id.uriName);

        Button submitNewRepButton = repPopupView.findViewById(R.id.addReportSubmit);

        submitNewRepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Adding Report");
                Map<String, String> report = new HashMap<>();
                try {
                    report.put("userID", userID);
                    report.put("reportName", repName.getText().toString());
                    report.put("pdfName", pdfName.getText().toString());
                    report.put("uri", uriName.getText().toString());
                } catch (Exception e) {
                    System.out.println("Failed at Add report from history");
                }

                System.out.println(report);

                DatabaseHelperPost taskPost = new DatabaseHelperPost(report, callbackPost);
                taskPost.execute("http://3.94.171.162:5000/addReport");

                dialog.hide();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = resultData.getData();
                File myFile = new File(uri.toString());
                String path = myFile.getAbsolutePath();
                String displayName = "";

                if (uri.toString().startsWith("content://")) {
                    Cursor c = null;

                    try {
                        c = getActivity().getContentResolver().query(uri, null, null, null, null);

                        if (c != null && c.moveToFirst()) {
                            displayName = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        c.close();
                    }
                }

                uriText.setText(uri.toString());
                pdfFile.setText(displayName);
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }
}