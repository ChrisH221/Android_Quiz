package app.example.chris.quiz_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
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

/**
 * Created by Chris on 05/05/2016.
 */

    public class gameLoop extends Activity {
        JSONArray arr;
        int score =0;
        checkScore checkScore;
        public boolean answered = false;
        public int multiplyer = 1;
        MediaPlayer player;
        boolean playing = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            setScore(0);
            this.player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            JSONAsync js = new JSONAsync();
            js.execute();


        }
        @Override
        public void onBackPressed() {
        }

        public void getScores(){
            checkScore  = new checkScore();
            checkScore.execute();
        }






        public void parseQuestion(JSONArray arr){
            this.arr = null;
            this.arr=arr;
            gameLoop(this.arr);
        }

        public void time(){
            final Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            final TextView timer = (TextView) findViewById( R.id.timer );
            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {

                    timer.setText("" + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    if(arr.length() > 1) {
                        if (answered) {
                            Button buttonT = (Button) findViewById(R.id.button_again);
                            Button buttonF = (Button) findViewById(R.id.button_false);
                            buttonT.setBackgroundColor(Color.GRAY);
                            buttonF.setBackgroundColor(Color.GRAY);
                            answered = false;
                            gameLoop(arr);

                        }

                        if (millisUntilFinished < 5000) {
                            vi.vibrate(1000);
                        }

                    }
                    else{
                    JSONAsync async = new JSONAsync();
                    async.execute();
                    }
                }

                public void onFinish() {

                    player.stop();
                    finish();

                }
            }.start();

        }

    public void finish(){

        Intent intent = new Intent(this, finish.class);
        intent.putExtra("score", Integer.toString(getScore()));
        intent.putExtra("jsonArray", checkScore.getArr().toString());

        startActivity(intent);
    }

        public void resetMult(){

            multiplyer = 1;

        }

        public void setMult(int s){

            multiplyer = multiplyer + s;

        }

        public int getMult(){

            return multiplyer;

        }



        public void setScore(int s){

            score = score + s;

        }

        public int getScore(){

            return score;

        }



        public int gameLoop(final JSONArray arr){
            if(!playing){
                time();
                playing = true;
            }

            player.start();

            final String answer;

            TextView tv = (TextView)findViewById(R.id.textView1);
            TextView s = (TextView)findViewById(R.id.score);
            s.setText("SCORE:" + getScore());
            TextView mult = (TextView)findViewById(R.id.mult);
            mult.setText("Multiplyer:" + getMult());
            try {
                JSONObject json_obj = arr.getJSONObject(1);
                String question = json_obj.getString("question");
                answer = json_obj.getString("correct");
                tv.setText(question);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    arr.remove(1);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            final Button buttonT = (Button) findViewById(R.id.button_again);
            final Button buttonF = (Button) findViewById(R.id.button_false);

            buttonT.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    if(answer.equals("1") ){
                        buttonT.setBackgroundColor(Color.GREEN);
                        setScore(10 * getMult());
                        setMult(1);
                        answered = true;


                    }
                    else {
                        buttonT.setBackgroundColor(Color.RED);
                        answered = true;
                        resetMult();

                    }
                }
            });


            buttonF.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (answer.equals("0")) {
                        buttonF.setBackgroundColor(Color.GREEN);
                        setScore(10 * getMult());
                        setMult(1);
                        answered = true;

                    } else {
                        buttonF.setBackgroundColor(Color.RED);
                        answered = true;
                        resetMult();

                    }
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
                    getScores();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

        }

    }




