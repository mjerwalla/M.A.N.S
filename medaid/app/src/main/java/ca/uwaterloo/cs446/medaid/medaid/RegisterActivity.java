package ca.uwaterloo.cs446.medaid.medaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Spinner userSelection;
    private EditText firstName, lastName, email, pass;
    private String postFailed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userSelection = (Spinner) findViewById(R.id.userType);
        addItemsToSpinner();
        Button btnRegister = (Button) findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName=(EditText)findViewById(R.id.firstName);
                lastName=(EditText)findViewById(R.id.lastName);
                email=(EditText) findViewById(R.id.email);
                pass=(EditText) findViewById(R.id.password);
                int userType= userSelection.getSelectedItemPosition();
                if (checkInput()){
//                    System.out.println("After check ");
//                    System.out.println(postFailed);
                    postData(userType);
//                    System.out.println(postFailed);
//                    while (postFailed != null){
//                        System.out.println("Waiting");
//                    }
//                    System.out.println("After while loop");
//                    System.out.println(postFailed);
//                    if (postFailed == "true"){
//                        Toast.makeText(getApplicationContext(),"Failed to create user. Please check connection and try again.",Toast.LENGTH_LONG).show();
////                        postFailed=false;
//                    }
                    // Do something
                }
            }

        });
//        finish();
    }

    private void postData(int userType){
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
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
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