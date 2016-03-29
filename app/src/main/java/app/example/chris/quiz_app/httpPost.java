package app.example.chris.quiz_app;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Chris on 02/03/2016.
 */
class httpPost extends AsyncTask<Void, Void, String> {

    int score;
    String playerName;

    public httpPost(int score, String playerName){




    }

    protected String doInBackground(Void... args) {

        String site = "http://52.18.108.189/checkUser.php";
        try {
            URL url = new URL(site);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setDoOutput(true);
            OutputStream os = httpConn.getOutputStream();
            String POST_PARAMS = "username=" +playerName + "&PASSWORD=" + score;
            System.out.println(POST_PARAMS);
            os.write(POST_PARAMS.getBytes());
            int responseCode = httpConn.getResponseCode();
            httpConn.connect();
            return "true";
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
            return"false";

        }}

    //@Override
   // protected void onPostExecute(String result) {

     //   try {

     //   } catch (JSONException e) {
    //        throw new RuntimeException(e);
     //   }


  //  }

}
