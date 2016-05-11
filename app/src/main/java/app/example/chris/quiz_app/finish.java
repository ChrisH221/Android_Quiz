package app.example.chris.quiz_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Chris on 05/05/2016.
 */
public class finish extends Activity {


    JSONArray scores;
    checkScore checkScore;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        finish();


    }



    public void finish() {

        setContentView(R.layout.finish);
        Intent intent = getIntent();
        String scoreString = intent.getStringExtra("score");
        score = Integer.parseInt(scoreString);

        String jsonArray = intent.getStringExtra("jsonArray");

        try {
            this.scores  = new JSONArray(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView st = (TextView) findViewById(R.id.score_text);
        st.setText("For this round you scored:" + score);


        boolean highscore = false;
        for (int x = 0; x < scores.length(); x++) {

            try {
                JSONObject json_obj = scores.getJSONObject(x);
                int pscore = json_obj.getInt("score");
                if (score > pscore) {
                    highscore = true;
                }
                if (highscore & x == scores.length() - 1) {
                    showInputDialog();
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        final Intent intentAgain = new Intent(this, gameLoop.class);
        final Intent intentHome = new Intent(this, MainActivity.class);

        Button buttonT = (Button) findViewById(R.id.button_again);
        buttonT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intentAgain);
            }
        });


        Button buttonM = (Button) findViewById(R.id.button_main);
        buttonM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intentHome);
            }
        });

    }


    public void showInputDialog() {


        LayoutInflater layoutInflater = LayoutInflater.from(finish.this);
        View promptView = layoutInflater.inflate(R.layout.input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(finish.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = editText.getText().toString();
                        httpPost post = new httpPost(username, score);
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
}
