package app.example.chris.quiz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    checkScore cs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        cs = new checkScore();
        cs.execute();
        setupMain();


    }

    public void setupMain(){

        setContentView(R.layout.activity_main);

        final Button buttonS = (Button) findViewById(R.id.button_start);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), gameLoop.class);
                startActivity(intent);
                System.exit(0);
            }
        });

        final Button buttonE = (Button) findViewById(R.id.button_score);
        buttonE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), scoreBoard.class);
                intent.putExtra("jsonArray", cs.getArr().toString());
                startActivity(intent);
                System.exit(0);
            }
        });

        final Button buttonC = (Button) findViewById(R.id.button_again);
        buttonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.exit(0);
            }
        });// Exit the app


    }




}
