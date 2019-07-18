package ca.uwaterloo.cs446.medaid.medaid;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;



public class DatabaseHelperGet extends AsyncTask<String, Void, String> {
    JSONObject postData;
    String postResponse;
    private Callback callback;
    public DatabaseHelperGet(Map<String, String> postData, final Callback callback) {
        if (postData != null) {
            this.postData = new JSONObject(postData);
        }
        this.callback = callback;
    }
    @Override
    protected String doInBackground(String... params) {

        String response = "";

        try {
            // This is getting the url from the string we passed in
            System.out.println("The URL to the Get was : " + params[0]);
            URL url = new URL(params[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            urlConnection.setDoInput(true);
            //urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestMethod("GET");


            // OPTIONAL - Sets an authorization header
            //urlConnection.setRequestProperty("Authorization", "someAuthString");

            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();
            System.out.println("The status Code for the get was : " + statusCode);

            if (statusCode ==  200) {

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                response = convertInputStreamToString(inputStream);
                System.out.println("The response was: ");
                System.out.println(response);
                // From here you can convert the string to JSON with whatever JSON parser you like to use
                // After converting the string to JSON, I call my custom callback. You can follow this process too, or you can implement the onPostExecute(Result) method
            } else {
                // Status code is not 200
                // Do something to handle the error
            }

        } catch (Exception e) {
            Log.d("DATA", e.getLocalizedMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute (String result){
        callback.onValueReceived(result);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
