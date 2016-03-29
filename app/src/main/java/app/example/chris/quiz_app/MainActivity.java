package app.example.chris.quiz_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    JSONArray arr;
    int score =0;
    double time;
    public boolean finish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScore(0);
        setupMain();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void setupMain(){
        setScore(0);
        setContentView(R.layout.activity_main);
        JSONAsync db = new JSONAsync();
        db.execute();


        final Button buttonS = (Button) findViewById(R.id.button_start);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_main2);
                gameLoop(arr, 0, getScore());
                time();
            }
        });

        final Button buttonE = (Button) findViewById(R.id.button_again);
        buttonE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });


    }

    public void parseQuestion(JSONArray arr){ this.arr = null; this.arr=arr; }

    public void time(){

        final TextView timer = (TextView) findViewById( R.id.timer );
        new CountDownTimer(10000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                timer.setText("" + String.format("%d min, %d sec",TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                              TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));


            }

            public void onFinish() {
                finish = true;
                finish(getScore());
            }
        }.start();

    }

    public void finish(int score){

        setContentView(R.layout.finish);

        TextView st = (TextView)findViewById(R.id.score_text);
        st.setText("For this round you scored:" + score);
        JSONAsync db = new JSONAsync();
        db.execute();

        final Button buttonT = (Button) findViewById(R.id.button_again);
        buttonT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish = false;
                scoreZero();
                setContentView(R.layout.activity_main2);
                gameLoop(arr, 0, 0);
                time();
            }
        });


        final Button buttonM = (Button) findViewById(R.id.button_main);
        buttonM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish = false;
                scoreZero();
                setupMain();

            }
        });


    }

    public void scoreZero(){

       score = 0;

    }

    public void setScore(int s){

       score = score + s;

    }

    public int getScore(){

       return score;

    }

    public int gameLoop(final JSONArray arr, final int count, int score){


        if(finish == true){return 0;}// finish(score);

        final String answer;


        TextView tv = (TextView)findViewById(R.id.textView1);
        TextView s = (TextView)findViewById(R.id.score);
        s.setText("SCORE:" + getScore());
        try {
            JSONObject json_obj = arr.getJSONObject(count);
            String question = json_obj.getString("question");
            answer = json_obj.getString("correct");
            tv.setText(question);
            arr.remove(count);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        final Button buttonT = (Button) findViewById(R.id.button_again);
        buttonT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(answer.equals("1") ){
                    setScore(10);
                     gameLoop(arr, count, getScore());
                }
                else {
                    gameLoop(arr,count,getScore());
                }
            }
        });

        final Button buttonF = (Button) findViewById(R.id.button_false);
        buttonF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(answer.equals("0") ){ setScore(10);gameLoop(arr, count, getScore());}
                else gameLoop(arr,count,getScore());
            }
        });
    return 0;

    }

    class JSONAsync extends AsyncTask<Void, JSONArray, String> {



        protected String doInBackground(Void... args) {

            String site = "http://52.18.108.189/getquestion.php";
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
