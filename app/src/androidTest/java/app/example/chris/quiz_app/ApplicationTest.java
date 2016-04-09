package app.example.chris.quiz_app;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;
import android.widget.TextView;

import app.example.chris.quiz_app.MainActivity;
import app.example.chris.quiz_app.R;
import com.robotium.solo.Solo;

public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private Solo solo;
    Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(scoreBoard.class.getName(), null, false);
    public ApplicationTest() {
        super(MainActivity.class);
        try {
            test_MainActivityCheckScoreBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void test_MainActivityCheckScoreBoard() throws Exception{
        solo.unlockScreen();
        Button btn_test = (Button) solo.getView(R.id.button_score);
        solo.clickOnView(btn_test);
        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity.finish();
       // solo.waitForText("hi", 1, 2000);
      //  TextView tv = (TextView) solo.getView(R.id.main_tv);
       // String result = tv.getText().toString();
        //assertEquals(result, "hi");
    }

}