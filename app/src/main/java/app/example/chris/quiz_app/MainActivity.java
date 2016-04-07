package app.example.chris.quiz_app;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
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
import android.os.Vibrator;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    JSONArray arr;
    int score =0;
    double time;
    public boolean finish = false;
    JSONArray scores;
    checkScore checkScore;
    public boolean answered = false;
    public int multiplyer = 1;
    MediaPlayer player = MediaPlayer.create(this,Settings.System.DEFAULT_RINGTONE_URI);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setScore(0);
        setupMain();


    }


    public void getScores(){
               checkScore  = new checkScore();
               checkScore.execute();
    }

    public void setupMain(){
        setScore(0);
        setContentView(R.layout.activity_main);
        JSONAsync db = new JSONAsync();
        db.execute();
        getScores();


        final Button buttonS = (Button) findViewById(R.id.button_start);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_main2);
                gameLoop(arr, 0, getScore());
                               time();
            }
        });

        final Button buttonE = (Button) findViewById(R.id.button_score);
        buttonE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), scoreBoard.class);
                intent.putExtra("jsonArray", checkScore.getArr().toString());
                startActivity(intent);
            }
        });

        final Button buttonC = (Button) findViewById(R.id.button_again);
        buttonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
               }
        });


    }

    public void parseQuestion(JSONArray arr){ this.arr = null; this.arr=arr; }

    public void time(){
        final Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final TextView timer = (TextView) findViewById( R.id.timer );
        new CountDownTimer(60000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                timer.setText("" + String.format("%d min, %d sec",TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                              TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                if(answered){
                    Button buttonT = (Button) findViewById(R.id.button_again);
                    Button buttonF = (Button) findViewById(R.id.button_false);
                    buttonT.setBackgroundColor(Color.GRAY);
                    buttonF.setBackgroundColor(Color.GRAY);

                }

                if(millisUntilFinished < 10000 ) {
                    vi.vibrate(1000);

                }


            }

            public void onFinish() {
                finish = true;
                player.stop();
                finish();
            }
        }.start();

    }

    public void finish(){

        setContentView(R.layout.finish);

        this.scores = checkScore.getArr();
        System.out.println(scores.length());
        TextView st = (TextView)findViewById(R.id.score_text);
        st.setText("For this round you scored:" + getScore());

        JSONAsync db = new JSONAsync();
        db.execute();

       boolean highscore = false;
       for(int x = 0; x < scores.length(); x++){

            try {
               JSONObject json_obj = scores.getJSONObject(x);
                int pscore = json_obj.getInt("score");
               if(getScore() > pscore){
                   highscore = true;
            }
                if(highscore & x == scores.length()-1){
                    showInputDialog();

                }

            } catch (JSONException e) {
               throw new RuntimeException(e);
            }

       }



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

    public void resetMult(){

        multiplyer = 1;

    }

    public void setMult(int s){

        multiplyer = multiplyer + s;

    }

    public int getMult(){

        return multiplyer;

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



        player.start();
        if(finish == true){return 0;}// finish(score);

        final String answer;


        TextView tv = (TextView)findViewById(R.id.textView1);
        TextView s = (TextView)findViewById(R.id.score);
        s.setText("SCORE:" + getScore());
        TextView mult = (TextView)findViewById(R.id.mult);
        mult.setText("Multiplyer:" + getMult());
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
        final Button buttonF = (Button) findViewById(R.id.button_false);

        buttonT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if(answer.equals("1") ){
                    buttonT.setBackgroundColor(Color.GREEN);
                    setScore(10 * getMult());
                    setMult(1);
                    answered = true;

                    gameLoop(arr, count, getScore());
                }
                else {
                    buttonT.setBackgroundColor(Color.RED);
                    answered = true;
                    resetMult();
                    gameLoop(arr,count,getScore());
                }
            }
        });


        buttonF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(answer.equals("0") ){
                    buttonF.setBackgroundColor(Color.GREEN);
                    setScore(10 * getMult());
                    setMult(1);
                    answered = true;
                    gameLoop(arr, count, getScore());
                }
                else {
                    buttonF.setBackgroundColor(Color.RED);
                    answered = true;
                    resetMult();
                    gameLoop(arr, count, getScore());
                }
            }
        });
    return 0;

    }


    public void showInputDialog() {


        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = editText.getText().toString();
                        httpPost  post = new httpPost(username, getScore());
                        post.execute();

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
