package app.example.chris.quiz_app;

import android.os.AsyncTask;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Chris on 02/03/2016.
 */
class httpPost extends AsyncTask<Void, Void, String> {

    String score;
    String playerName;

    public httpPost(String playerName, int score) {
        String string = score + "";
        this.score = string;
        this.playerName = playerName;


    }

    protected String doInBackground(Void... args) {

        String site = "http://52.18.108.189/addUser.php";
        try {
            URL url = new URL(site);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setDoOutput(true);
            OutputStream os = httpConn.getOutputStream();
            String POST_PARAMS = "player=" + playerName + "&score=" + score + "";
            System.out.println("HEY" + POST_PARAMS);
            os.write(POST_PARAMS.getBytes());
            int responseCode = httpConn.getResponseCode();
            httpConn.connect();
            return "true";
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
            return "false";

        }
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        }
}


