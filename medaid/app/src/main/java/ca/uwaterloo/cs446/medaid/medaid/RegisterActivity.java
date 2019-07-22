package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Spinner userSelection;
    private EditText firstName, lastName, email, pass;
    private String postFailed = null;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userSelection = (Spinner) findViewById(R.id.userType);
        TextView mTextView = (TextView)  findViewById(R.id.textView13);
        String text = "<font color=#27CEA7>Med</font><font color=#3A95EB>Aid</font>";
        mTextView.setText(Html.fromHtml(text));
        addItemsToSpinner();
        Button btnRegister = (Button) findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName=(EditText)findViewById(R.id.firstName);
                lastName=(EditText)findViewById(R.id.lastName);
                email=(EditText) findViewById(R.id.email);
                pass=(EditText) findViewById(R.id.password);
                userType= userSelection.getSelectedItemPosition();
                if (checkInput()){
                    postData(userType);
                }
            }

        });
//        finish();
    }

    private void postData(final int userType){
        System.out.println("In postData ");
        String emailID = email.getText().toString();
        String password = pass.getText().toString();
        String firstname = firstName.getText().toString();
        String lastname = lastName.getText().toString();
        final Map<String, String> postData = new HashMap<>();
        postData.put("userName", emailID);
        postData.put("password", password);
        postData.put("firstName", firstname);
        postData.put("lastName", lastname);
        postData.put("userType", String.valueOf(userType));

        Callback callback = new Callback() {
            @Override
            public void onValueReceived(final String value) {
                if (value == null || value.length() == 0){
                    System.out.println("No value received from Post");
                    postFailed = "true";
                    Toast.makeText(getApplicationContext(),"Failed to create user. Please check connection and try again.",Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("NOT FALSE");
                    postFailed = "false";
                    setSharedPreferences(Integer.toString(userType));
                    nextActivity();
                }
                System.out.println("The onValueReceived  for Post: " + value);
                // call method to update view as required using returned value

            }

            @Override
            public void onFailure() {
                postFailed="true";
                System.out.println("I failed :(");
            }
        };

        DatabaseHelperPost task = new DatabaseHelperPost(postData, callback);
        task.execute("http://3.94.171.162:5000/addUser");
    }

    private void nextActivity(){
        SharePreferences preferences = new SharePreferences(this);
        if ( preferences.getPref("userID") != null ){
            String userType = preferences.getPref("userType");
            Intent intent =null;
            switch(userType) {
                case "0":
                    intent = new Intent(getBaseContext(), MainActivity.class);
                    break;
                case "1":
                    // Create multiuser intent
                    break;
                case "2":
//                    // Create doctor user intent
                    intent = new Intent(getBaseContext(), DoctorMainActivity.class);
                    break;
                default:
                    System.out.println("Failed to direct to activity");
            }
//            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void setSharedPreferences(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            String userID = String.valueOf(jsonArray.getJSONObject(0).getString("LAST_INSERT_ID()"));
            SharePreferences preferences = new SharePreferences(this);
            preferences.modifyPref("userID",userID);
            preferences.modifyPref("userType", String.valueOf(userType));

        } catch (Exception e) {
            System.out.println("FAILED WHILE SETTING SHARED PREFERENCES");
            System.out.println(e);
        }

    }

    private boolean checkInput() {

        // Reset errors.
        email.setError(null);
        pass.setError(null);

        // Store values at the time of the login attempt.
        String emailID = email.getText().toString();
        String password = pass.getText().toString();
        String name = firstName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            pass.setError(getString(R.string.error_invalid_password));
            focusView = pass;
            System.out.println("Invalid pass");
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(emailID)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }
        if (!isEmailValid(emailID)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        if (!isNameValid(name)){
            firstName.setError(getString(R.string.error_invalid_name));
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 2;
    }

    public void addItemsToSpinner(){
        // Array Adapter to take drop down options alogn with Layout config
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_mode, android.R.layout.simple_spinner_item);
        // Layout on how to display the text (default layouts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSelection.setAdapter(adapter);
    }



}