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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setupMain();


    }
    @Override
    public void onBackPressed() {
    }


    public void setupMain(){

        setContentView(R.layout.activity_main);

        final Button buttonS = (Button) findViewById(R.id.button_start);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), gameLoop.class);
                startActivity(intent);
            }
        });

        final Button buttonE = (Button) findViewById(R.id.button_score);
        buttonE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), scoreBoard.class);
                checkScore cs = new checkScore();
                intent.putExtra("jsonArray", cs.getArr().toString());
                startActivity(intent);
            }
        });

        final Button buttonC = (Button) findViewById(R.id.button_again);
        buttonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.exit(0);
               }
        });


    }




}
