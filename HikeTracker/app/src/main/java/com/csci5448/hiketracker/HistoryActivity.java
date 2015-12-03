package com.csci5448.hiketracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {

    /*******************  Class variables *******************/
    public static final String TAG = "History Activity";
    private ListView mListView;
    private HikeDataAdapter adapter;
    private ArrayList<HikeData> hikes;
    private HikeDataSource hikeDataSource;
    private HikeData hikeDB;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        hikes = new ArrayList<>();  // initialize array list to prevent passing null references

        // initialize database
        hikeDataSource = new HikeDataSource(this);

        // start to asynchronously retrieves mountain data from table
        getHikes();

        setupViews();

        Button manualEntryBttn = (Button) findViewById(R.id.manualEntryHistoryBttn);
        manualEntryBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Manual Entry Button clicked");
                newEntry();
            }
        });
    }

    private void setupViews(){
        // Set up list display
        // Create the adapter to convert the array to views
        adapter = new HikeDataAdapter(this, hikes);

        // Attach the adapter to a ListView
        ListView listview = (ListView) findViewById(R.id.historyList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                                    long arg3) {
                                                hikeDB = (HikeData) adapter.getItemAtPosition(position);

                                                // Show dialog
                                                AlertDialog.Builder adb = new AlertDialog.Builder(
                                                        HistoryActivity.this);
                                                adb.setTitle("What do you want to do?");
                                                adb.setPositiveButton("Edit Hike", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        editEntry();
                                                    }
                                                });
                                                adb.setCancelable(true);
                                                adb.setNegativeButton("Delete Hike", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        deleteEntry();
                                                    }
                                                });
                                                adb.setNeutralButton("Cancel", null);
                                                adb.show();

                                            }
                                        }
        );

    }

    // Task Calls
    private void getHikes() {new getHikesTask().execute(); }

    /**
     * Modifies an existing entry in the history
     */
    private void editEntry() {
        // Start SaveHikeData Activity with Tag
        Intent myIntent = new Intent(HistoryActivity.this, SaveHikeDataActivity.class);
        myIntent.putExtra(getString(R.string.passHikeData), hikeDB);
        myIntent.putExtra(getString(R.string.sourceString), TAG);
        myIntent.putExtra(getString(R.string.editString), true);
        myIntent.putExtra(getString(R.string.newString), false);


        startActivityForResult(myIntent, MainActivity.NEW_HIKE_DATA);
    }

    /**
     * Deletes an existing entry in the history
     */
    private void deleteEntry() {
        // Start SaveHikeData Activity with Tag
        Intent myIntent = new Intent(HistoryActivity.this, SaveHikeDataActivity.class);
        myIntent.putExtra(getString(R.string.passHikeData), hikeDB);
        myIntent.putExtra(getString(R.string.sourceString), TAG);
        myIntent.putExtra(getString(R.string.editString), false);
        myIntent.putExtra(getString(R.string.newString), false);


        startActivityForResult(myIntent, MainActivity.NEW_HIKE_DATA);
    }

    /**
     * Creates a new entry in the history
     */
    private void newEntry() {
        // Start SaveHikeData Activity with Tag
        hikeDB = new HikeData();
        hikeDB.setPeakName("Mt. Sneffels");   // Default title
        Intent myIntent = new Intent(HistoryActivity.this, SaveHikeDataActivity.class);
        myIntent.putExtra(getString(R.string.passHikeData), hikeDB);
        myIntent.putExtra(getString(R.string.sourceString), TAG);
        myIntent.putExtra(getString(R.string.editString), true);
        myIntent.putExtra(getString(R.string.newString), true);

        startActivityForResult(myIntent, MainActivity.NEW_HIKE_DATA);
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

    //    Asynchronous Task to Access SQLite Database
    public class getHikesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... types) {
//            Log.d(TAG, "On doInBackground...");
            hikes = (ArrayList)hikeDataSource.getAllHikes();
//                    Collections.sort(hikes);    // Sort by date
            Log.d(TAG, "Hikes Loaded");

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Collections.sort(hikes);
            setupViews();
            Log.d(TAG, "Finished task, new Size = " + hikes.size());
        }
    }

}
