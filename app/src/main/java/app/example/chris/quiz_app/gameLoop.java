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
 * The gameLoop class handles all the logic related to playing a round of the quiz game.
 * This class features a method called gameLoop that populates the
 */

    public class gameLoop extends Activity {
        JSONArray arr;
        int score =0;
        checkScore checkScore;
        public boolean answered = false;
        public int multiplyer = 1;
        MediaPlayer player;
        boolean playing = false;
        boolean tickOne = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            setScore(0);
            this.player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);//Music player setup to play default ringtone
            JSONAsync js = new JSONAsync();// Downloads the questions
            js.execute();
            getScores();


        }
        @Override
        public void onBackPressed() {//disable the back button while playing game
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
            final TextView timer = (TextView) findViewById(R.id.timer);

            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    /* Code modified from method onTick
                     * obtained from http://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android
                     */
                    timer.setText("" + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    if(arr.length() > 3) {//Check question array size
                        if (answered) {// The flag when the true or false buttons are pressed
                            Button buttonT = (Button) findViewById(R.id.button_again);
                            Button buttonF = (Button) findViewById(R.id.button_false);
                            if(tickOne) {//For waiting one full tick
                                /*
                                 * Configuration for resetting the buttons and then calling
                                 * the gameLoop method with the next question.
                                 */
                                tickOne = false;
                                buttonT.setBackgroundColor(Color.GRAY);
                                buttonF.setBackgroundColor(Color.GRAY);
                                answered = false;
                                buttonT.setEnabled(true);
                                buttonF.setEnabled(true);
                                gameLoop(arr);
                            }else{
                                tickOne = true;//Ensure one tick between answering questions

                            }
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
        System.exit(0);
    }

    /*
     * Getters and setters for the score multiplyer
     */
        public void resetMult(){

            multiplyer = 1;

        }

        public void setMult(int s){

            multiplyer = multiplyer + s;

        }

        public int getMult(){

            return multiplyer;

        }


    /*
     * Getters and setters for the score
     */
        public void setScore(int s){

            score = score + s;

        }

        public int getScore(){

            return score;

        }


    /*
     *  Game loop for setting up the next question and deciding if the
     *  player has selected the appropriate answer.
     */

        public int gameLoop(final JSONArray arr){
            if(!playing){//Check to see if game is underway
                time();//Start the time
                playing = true;
            }

            player.start();//Start the music

            final String answer;
            /**
             * Setup the question, display the score and display the multiplier.
             */
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

                    buttonT.setEnabled(false);
                    buttonF.setEnabled(false);

                    if(answer.equals("1") ){//If answer is correct
                        buttonT.setBackgroundColor(Color.GREEN);//Set Button to green
                        setScore(10 * getMult());//Add to the score
                        setMult(1);//Add one to the multiplier
                        answered = true;//Flag for the timer


                    }
                    else {//Wrong answer
                        buttonT.setBackgroundColor(Color.RED);//Set Button to green
                        answered = true;//Flag for the timer
                        resetMult();

                    }
                }
            });


            buttonF.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    buttonT.setEnabled(false);
                    buttonF.setEnabled(false);
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



/*
 * Inner class that extends AsyncTask to download sets of questions from the server.
 */

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
                    parseQuestion(res);//Assigns the resulting array to the global variable


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

        }

    }




