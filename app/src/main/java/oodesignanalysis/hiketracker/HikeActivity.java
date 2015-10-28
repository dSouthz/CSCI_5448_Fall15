package oodesignanalysis.hiketracker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class HikeActivity extends ActionBarActivity {

    /*******************  Class variables *******************/
    private int hikeTime;
    private HikeData hikeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
    }

    /**
     * Starts tracking a new hike
     */
    public void startHike() {

    }

    /**
     * Ends tracking an existing hike
     */
    public void endHike() {

    }

    /**
     * Resets the active timer
     */
    public void resetClock() {

    }

    /**
     * Persists the current hike data to storage
     */
    public void saveHike() {

    }

    /**
     * Exits the hike activity without saving
     */
    public void exitWithoutSave() {

    }
}
