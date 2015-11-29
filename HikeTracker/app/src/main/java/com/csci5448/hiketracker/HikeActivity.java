package com.csci5448.hiketracker;

import android.content.Intent;
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

    private void setRunningButtons(){
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        resetButton.setEnabled(true);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }

    private void setPausedButtons(){
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        resetButton.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }

    private void setStartingButtons(){
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        resetButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }

    private void saveHike() {
// TODO Implement saving hike to database, updating user information, updating mountain information
        // Launch HikeDialog Fragment w/ dialog displaying collected info, option to save
        HikeData hikeData = new HikeData();
        hikeData.setUserId(user.getUserId());
        hikeData.setHikeLength(updatedTime);
        hikeData.setPeakName(mountain.getmName());
//        hikeData.setHikeDate(new Date(Calendar.getInstance().getTime().getTime()));  // Should be set to the current date with constructor

        // Start SaveHikeData Activity with Tag
        Intent myIntent = new Intent(HikeActivity.this, SaveHikeDataActivity.class);
        myIntent.putExtra(getString(R.string.passHikeData), hikeData);
        myIntent.putExtra(getString(R.string.passUser),user);
        myIntent.putExtra(getString(R.string.sourceString), TAG);

        startActivity(myIntent);
    }

    private void exit() {
// TODO Return to main activity
        finish();
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = System.currentTimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            timerValue.setText(String.format("%02d", hrs) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };




}
