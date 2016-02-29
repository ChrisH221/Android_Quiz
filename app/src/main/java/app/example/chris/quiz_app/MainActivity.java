package app.example.chris.quiz_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    JSONArray arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbAsync db = new dbAsync();
        db.execute();
        final Button buttonS = (Button) findViewById(R.id.button_start);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView tv = (TextView)findViewById(R.id.textView1);
                tv.setText(arr.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void parseQuestion(JSONArray arr){


       this.arr=arr;


    }

    class dbAsync extends AsyncTask<Void, JSONArray, String> {



        protected String doInBackground(Void... args) {

            String site = "http://192.168.1.67:80/getquestion.php";
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
                JSONArray res = new JSONArray(result);
                parseQuestion(res);
                          } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }

    }

}
