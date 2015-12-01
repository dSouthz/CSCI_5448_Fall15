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

/**
 * Created by Ryan on 10/28/15.
 */
public class HistoryActivity extends AppCompatActivity {

    /*******************  Class variables *******************/
    public static final String TAG = "History Activity";
    private ListView mListView;
    private HikeDataAdapter adapter;
    private ArrayList<HikeData> hikes;
    private HikeDataSource hikeDataSource;
    private HikeData hikeDB;
    private ListView listview;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        hikes = new ArrayList<>();  // initialize array list to prevent passing null references

        // initialize database
        hikeDataSource = new HikeDataSource(this);
        user = getIntent().getExtras().getParcelable(getString(R.string.passUser));

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
                                                hikeDB = (HikeData) adapter.getItemAtPosition(position);
                                                String value = hikeDB.toString();
//                Toast.makeText(getApplicationContext(), "You clicked " + value + " at position " + position, Toast.LENGTH_LONG).show();

                                                // Show dialog
                                                AlertDialog.Builder adb = new AlertDialog.Builder(
                                                        HistoryActivity.this);
                                                adb.setTitle("What do you want to do?");
//                adb.setMessage("You selected " + value);
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
    // Insert test hike data into the database
    private void test(){

        hikeDB = new HikeData();
        Log.d(TAG, hikeDB.toString());
        saveEntry();
    }

    // Task Calls
    private void getHikes() {new getHikesTask().execute(HikeDataDisplayActions.LOAD_ALL); }
    private void saveEntry() {new getHikesTask().execute(HikeDataDisplayActions.SAVE_ENTRY); }

    /**
     * Modifies an existing entry in the history
     */
    private void editEntry() {
        // Start SaveHikeData Activity with Tag
        Intent myIntent = new Intent(HistoryActivity.this, SaveHikeDataActivity.class);
        myIntent.putExtra(getString(R.string.passHikeData), hikeDB);
        myIntent.putExtra(getString(R.string.passUser), user);
        myIntent.putExtra(getString(R.string.sourceString), TAG);
        myIntent.putExtra(getString(R.string.editString), true);

        startActivity(myIntent);
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

        startActivity(myIntent);
    }

    /**
     * Creates a new entry in the history
     */
    private void newEntry(String entry) {
        // Start SaveHikeData Activity with Tag
        hikeDB = new HikeData();
        Intent myIntent = new Intent(HistoryActivity.this, SaveHikeDataActivity.class);
        myIntent.putExtra(getString(R.string.passHikeData), hikeDB);
        myIntent.putExtra(getString(R.string.sourceString), TAG);
        myIntent.putExtra(getString(R.string.editString), true);

        startActivity(myIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent returnIntent = new Intent();
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK,returnIntent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED,returnIntent);
            }
        }
        finish();
    }

private enum HikeDataDisplayActions {
    LOAD_ALL, DELETE_ENTRY, UPDATE_ENTRY, SAVE_ENTRY
}

//    Asynchronous Task to Access SQLite Database
public class getHikesTask extends AsyncTask<HikeDataDisplayActions, Void, Void> {

    @Override
    protected Void doInBackground(HikeDataDisplayActions... types) {
//            Log.d(TAG, "On doInBackground...");
        Log.d("On doInBackground... ", String.valueOf(types[0]));

        switch (types[0]) {
            case LOAD_ALL: // Retrieve all saved hike data
                hikes = (ArrayList)hikeDataSource.getAllHikes();
//                    Collections.sort(hikes);    // Sort by date
                Log.d(TAG, "Hikes Loaded");
                break;
            case DELETE_ENTRY:  // Delete chosen entry
                hikeDataSource.deleteHikeData(hikeDB);
                hikes = (ArrayList)hikeDataSource.getAllHikes();
//                    Collections.sort(hikes);    // Sort by date
                Log.d(TAG, "Hike deleted");
                break;
            case UPDATE_ENTRY:  // Edit and update chosen entry
                hikeDataSource.update(hikeDB);
                hikes = (ArrayList)hikeDataSource.getAllHikes();
//                    Collections.sort(hikes);    // Sort by date
                Log.d(TAG, "Hike updated");
                break;
            case SAVE_ENTRY:
                hikeDataSource.save(hikeDB);
                hikes = (ArrayList)hikeDataSource.getAllHikes();
//                    Collections.sort(hikes);    // Sort by date
                Log.d(TAG, "Hike saved" + hikeDB.getHikeLength());
                break;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        setupViews();
        Log.d(TAG, "Finished task, new Size = " + hikes.size());
    }
}

}
