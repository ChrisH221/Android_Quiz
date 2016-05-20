package app.example.chris.quiz_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chris on 30/03/2016.
 */
public class scoreBoard extends Activity {
    JSONArray jArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorelist);

        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonArray");// Gets the JSON string from the previous activity

        try {
             jArray = new JSONArray(jsonArray);
              } catch (JSONException e) {
            e.printStackTrace();
        }

        setupList();

    }
    /*
     * Needed to override the back press so this activity could be
     * properly closed.
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        System.exit(0);
    }

    public void setupList(){


        ArrayList<String> items = new ArrayList<>();
        System.out.println(jArray.length());
        try {


            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                String name = json_data.getString("player");
                String score = json_data.getString("score");
                items.add(name +" scored " + score );//Prints the username and their score


            }
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }


        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        ListView theListView = (ListView)findViewById(R.id.theListView);

        theListView.setAdapter(mArrayAdapter);



    }

}
