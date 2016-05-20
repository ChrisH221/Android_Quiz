package app.example.chris.quiz_app;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Chris on 29/03/2016.
 *
 * Extends AsyncTask to download the list of scores held in the database.
 * This data is in JSON format and used to generate a JSONArray Object.
 */


class checkScore extends AsyncTask<Void, JSONArray, String> {

    JSONArray arr;// The JSONArray


    /*
    * A method for assigning the downloaded JSONArray to the global JSONArray variable.
    * This variable is then accessed by the scoreBoard class and in the finish class.
     */
    public void parseScore(JSONArray arr){

        this.arr=arr;

    }

    public JSONArray getArr() {
        return arr;
    }

    protected String doInBackground(Void... args) {

        String site = "http://52.18.108.189/getscore.php";
        try {
            URL url = new URL(site);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            httpConn.getResponseCode();

            InputStream input =  httpConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            String response = result.toString();

            return response;

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
            return null;

        }}

    @Override
    protected void onPostExecute(String result) {

        try {
            JSONArray res = new JSONArray(result);// Convert the data into a JSONArray
            parseScore(res);//Assign the array to a global variable making it accessible to other activites
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

}