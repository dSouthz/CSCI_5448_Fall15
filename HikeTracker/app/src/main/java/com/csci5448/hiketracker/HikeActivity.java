package com.csci5448.hiketracker;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HikeActivity extends AppCompatActivity {


    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button saveButton;
    private Button cancelButton;

    private TextView timerValue;

    private long startTime = 0L;
    private long pausedTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean wasRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timerValue = (TextView) findViewById(R.id.timerValue);

        startButton = (Button) findViewById(R.id.startButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setVisibility(View.INVISIBLE);

        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setVisibility(View.INVISIBLE);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setVisibility(View.INVISIBLE);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setVisibility(View.INVISIBLE);

        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setRunningButtons();
//                if (!wasRunning)startTime = System.currentTimeMillis();
//                else {
//                    startTime = System.currentTimeMillis() - pausedTime;
//                    timeInMilliseconds = savedTime;
//                }
                startTime = System.currentTimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);

            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                customHandler.removeCallbacks(updateTimerThread);
//                pausedTime = System.currentTimeMillis();
                setPausedButtons();
                timeSwapBuff += timeInMilliseconds;
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startTime = System.currentTimeMillis();
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                saveHike();
                exit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                exit();
            }
        });
    }

    private void setRunningButtons(){
        startButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
    }

    private void setPausedButtons(){
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    private void saveHike() {
// TODO Implement saving hike to database, updating user information, updating mountain information
    }

    private void exit() {
// TODO Return to main activity
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
