package com.csci5448.hiketracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HikeActivity extends AppCompatActivity {

    public static final String TAG = "HikeActivity";
    public final String ENABLED_COLOR = "#666666";
    public final String DISABLED_COLOR = "#555555";


    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button saveButton;
    private Button cancelButton;

    private TextView timerValue;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    // Mountain variable
    Mountain mountain;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve parceled object
        Bundle bundle = getIntent().getExtras();
        mountain = bundle.getParcelable(getString(R.string.passMountain));
        user = bundle.getParcelable(getString(R.string.passUser));

        timerValue = (TextView) findViewById(R.id.timerValue);
        TextView peakName = (TextView) findViewById(R.id.hikepeakname);
        peakName.setText(mountain.getmName());

        startButton = (Button) findViewById(R.id.startButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        saveButton = (Button) findViewById(R.id.reviewButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        setStartingButtons();

        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(TAG, "Clicked on Start Button");
                setRunningButtons();
                startTime = System.currentTimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(TAG, "Clicked on Pause Button");
                customHandler.removeCallbacks(updateTimerThread);
                setPausedButtons();
                timeSwapBuff += timeInMilliseconds;
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(TAG, "Clicked on Reset Button");
                startTime = System.currentTimeMillis();
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;
                setTimerText(0,0,0);
                setButtonAvailable(saveButton, false);
                setButtonAvailable(cancelButton, false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(TAG, "Clicked on Save Button");
                saveHike();
                exit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(TAG, "Clicked on Cancel Button");
                exit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent returnIntent = new Intent();
        if (requestCode == MainActivity.NEW_HIKE_DATA) {
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK,returnIntent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED,returnIntent);
            }
        }
        finish();
    }

    private void setRunningButtons(){
        setButtonAvailable(startButton, false);
        setButtonAvailable(pauseButton, true);
        setButtonAvailable(resetButton, true);
        setButtonAvailable(saveButton, false);
        setButtonAvailable(cancelButton, false);
    }

    private void setPausedButtons(){
        setButtonAvailable(startButton, true);
        setButtonAvailable(pauseButton, false);
        setButtonAvailable(resetButton, true);
        setButtonAvailable(saveButton, true);
        setButtonAvailable(cancelButton, true);
    }

    private void setStartingButtons(){
        setButtonAvailable(startButton, true);
        setButtonAvailable(pauseButton, false);
        setButtonAvailable(resetButton, false);
        setButtonAvailable(saveButton, false);
        setButtonAvailable(cancelButton, false);
    }

    private void setButtonAvailable(Button button, Boolean state) {
        button.setEnabled(state);
        if(state) {
            button.setBackgroundColor(Color.parseColor(ENABLED_COLOR));
        } else {
            button.setBackgroundColor(Color.parseColor(DISABLED_COLOR));
        }
    }

    private void setTimerText(int hrs, int mins, int secs) {
        timerValue.setText(String.format("%02d", hrs) + ":"
                + String.format("%02d", mins) + ":"
                + String.format("%02d", secs));
    }

    private void saveHike() {
// TODO Implement saving hike to database, updating user information, updating mountain information
        // Launch HikeDialog Fragment w/ dialog displaying collected info, option to save
        HikeData hikeData = new HikeData();
        hikeData.setUserId(MainActivity.user.getUserId());
        hikeData.setHikeLength(updatedTime);
        hikeData.setPeakName(mountain.getmName());

        // Start SaveHikeData Activity with Tag
        Intent myIntent = new Intent(HikeActivity.this, SaveHikeDataActivity.class);
        myIntent.putExtra(getString(R.string.passHikeData), hikeData);
        myIntent.putExtra(getString(R.string.sourceString), TAG);

        startActivityForResult(myIntent, MainActivity.NEW_HIKE_DATA);
    }



    private void exit() {
// TODO Return to main activity
        //finish();
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = System.currentTimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            setTimerText(hrs, mins, secs);
            customHandler.postDelayed(this, 0);
        }

    };




}
