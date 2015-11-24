package com.csci5448.hiketracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Ryan on 10/28/15.
 */
public class HistoryActivity extends AppCompatActivity {

    /*******************  Class variables *******************/
    private static final String TAG = "History Activity";
    private ListView mListView;
    private HikeDataAdapter adapter;
    private ArrayList<HikeData> hikes;
    private HikeDataSource hikeDataSource;
    private HikeData hikeDB;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define custom screen layout
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
                test();
            }
        });
    }

    private void setupViews(){
        // Set up list display
        // Create the adapter to convert the array to views
        adapter = new HikeDataAdapter(this, hikes);

        // Attach the adapter to a ListView
        listview = (ListView) findViewById (R.id.historyList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "You clicked " + value + " at position " + position, Toast.LENGTH_LONG).show();
            }
        });

    }
    // Insert test hike data into the database
    private void test(){
        long yourmilliseconds = System.currentTimeMillis();
        Date resultdate = new Date(yourmilliseconds);

        hikeDB = new HikeData("Test Peak Name", 999, resultdate, 1);
        saveEntry();
    }

    // Task Calls
    private void getHikes() {new getHikesTask().execute(HikeDataDisplayActions.LOAD_ALL); }
    private void deleteEntry() {new getHikesTask().execute(HikeDataDisplayActions.DELETE_ENTRY); }
    private void updateEntry() {new getHikesTask().execute(HikeDataDisplayActions.UPDATE_ENTRY); }
    private void saveEntry() {new getHikesTask().execute(HikeDataDisplayActions.SAVE_ENTRY); }

    /**
     * Modifies an existing entry in the history
     */
    private void editEntry() {
        // TODO: Populate pop-up window with entry's data
        // TODO: Allow entry data to be manipulated
        // TODO: Call for HikeDataSource update to push to database
    }

    /**
     * Deletes an existing entry in the history
     */
    private void delete() {
        // TODO: Populate pop-up window with entry's data
        // TODO: Prompt user if they really want to delete entry
        // TODO: Call for HikeDataSource delete to delete entry from database
    }

    /**
     * Creates a new entry in the history
     */
    private void newEntry(String entry) {

    }

    private enum HikeDataDisplayActions {
        LOAD_ALL, DELETE_ENTRY, UPDATE_ENTRY, SAVE_ENTRY
    }

    //    Asynchronous Task to Access SQLite Database
    public class getHikesTask extends AsyncTask<HikeDataDisplayActions, Void, Void> {

        @Override
        protected Void doInBackground(HikeDataDisplayActions... types) {
//            Log.d(TAG, "On doInBackground...");
            Log.d("Task Action: ", String.valueOf(types[0]));

            switch (types[0]) {
                case LOAD_ALL: // Retrieve all saved hike data
                    hikes = (ArrayList)hikeDataSource.getAllHikes();
                    Collections.sort(hikes);    // Sort by date
                    Log.d(TAG, "Hikes Loaded");
                    break;
                case DELETE_ENTRY:  // Delete chosen entry
                    hikeDataSource.deleteHikeData(hikeDB);
                    Log.d(TAG, "Hike deleted");
                    break;
                case UPDATE_ENTRY:  // Edit and update chosen entry
                    hikeDataSource.update(hikeDB);
                    Log.d(TAG, "Hike updated");
                    break;
                case SAVE_ENTRY:
                    hikeDataSource.save(hikeDB);
                    Log.d(TAG, "Hike saved");
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            listview.invalidateViews();
            Log.d(TAG, "Finished task, new Size = " + hikes.size());
        }
    }

}
