package com.csci5448.hiketracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SaveHikeDataActivity extends AppCompatActivity {

    private static final String TAG = "SaveHikeDatActivity";
    private Button saveHikeBttn;
    private Button cancelHikeBttn;
    private Bundle bundle;
    HikeData hikeData;
    HikeDataSource hikeDataSource;

    // Layout Variables
    TextView mountainNameField, hikeDateField, hikeLengthField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_hike_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hikeDataSource = new HikeDataSource(this);

        bundle = getIntent().getExtras();
        if (bundle == null) {
            // No extras were put in --> something went wrong
            Log.d(TAG, "Bundle is empty");
            finish();
        }

        hikeData = bundle.getParcelable(getString(R.string.passHikeData));
        String source = bundle.getString(getString(R.string.sourceString));

        // Setup Buttons
        saveHikeBttn = (Button)findViewById(R.id.saveHikeBttn);
        cancelHikeBttn = (Button)findViewById(R.id.cancelHikeBttn);
        buttonSetup();

        // Set up values
        mountainNameField = (TextView)findViewById(R.id.mountainNameField);
        hikeDateField  = (TextView)findViewById(R.id.hikeDateField);
        hikeLengthField  = (TextView)findViewById(R.id.hikeLengthField);

        // Fill in TextView fields
        mountainNameField.setText(hikeData.getPeakName());
        hikeDateField.setText(String.valueOf(hikeData.getHikeDate()));
        hikeLengthField.setText(timeFromLong(hikeData.getHikeLength()));

        if (source != null) {
            if (source.equals(HikeActivity.TAG)) {
                // new hikedata to be saved
                setupforSaving();
            }

            else {
                // Previously saved hikedata to be edited or deleted
                // Check to see if this actiivty will display editing fields
                Boolean check = getIntent().getExtras().getBoolean(getString(R.string.editString));

                if (check) {
                    setupForEditing();  // Setup fields for editing
                }
                else {
                    // If coming from HistoryActivity and it isn't for editing, it's for deleting
                    setupForDeleting();
                }
            }
        }

    }

    private String timeFromLong(Long time){
        int secs = (int) (time / 1000);
        int mins = secs / 60;
        int hrs = mins / 60;
        secs = secs % 60;
        return (String.format("%02d", hrs) + ":"
                + String.format("%02d", mins) + ":"
                + String.format("%02d", secs));
    }

    private void setupForEditing() {
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.mountainListRadioGroup);


        // Change save button to update button
        saveHikeBttn.setText(R.string.updateBttnLabel);

        saveHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Save Hike Button clicked");
                updateEntry();
            }
        });
    }

    private void setupforSaving() {
        // Hide editing fields
        hideEditingFields();

    }

    // Hide editing fields
    private void hideEditingFields(){
        TextView pickMountainLabel, pickDateLabel, pickTimeLabel, colon;
        pickMountainLabel = (TextView)findViewById(R.id.pickMountainLabel);
        pickDateLabel = (TextView)findViewById(R.id.pickDateLabel);
        pickTimeLabel = (TextView)findViewById(R.id.pickTimeLabel);
        colon = (TextView)findViewById(R.id.colon);

        pickMountainLabel.setVisibility(View.INVISIBLE);
        pickDateLabel.setVisibility(View.INVISIBLE);
        pickTimeLabel.setVisibility(View.INVISIBLE);
        colon.setVisibility(View.INVISIBLE);

        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.setVisibility(View.INVISIBLE);

        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        datePicker.setVisibility(View.INVISIBLE);

        LinearLayout timePicker = (LinearLayout)findViewById(R.id.timePickerLayout);
        timePicker.setVisibility(View.INVISIBLE);
    }

    private void setupForDeleting() {
        // Hide editing fields
        hideEditingFields();

        // Change save button to delete button
        saveHikeBttn.setText(R.string.deleteBttnLabel);

        saveHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Save Hike Button clicked");
                deleteEntry();
            }
        });
    }

    private void buttonSetup(){
        saveHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Save Hike Button clicked");
                saveEntry();

            }
        });

        cancelHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel Button clicked");
                Toast.makeText(getApplicationContext(),"This hike was not saved",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // Task Calls
    private void deleteEntry() {new getHikesTask().execute(HikeDataDisplayActions.DELETE_ENTRY); }
    private void updateEntry() {new getHikesTask().execute(HikeDataDisplayActions.UPDATE_ENTRY); }
    private void saveEntry() {new getHikesTask().execute(HikeDataDisplayActions.SAVE_ENTRY); }


    private enum HikeDataDisplayActions {
        DELETE_ENTRY, UPDATE_ENTRY, SAVE_ENTRY
    }

    //    Asynchronous Task to Access SQLite Database
    public class getHikesTask extends AsyncTask<HikeDataDisplayActions, Void, Void> {

        @Override
        protected Void doInBackground(HikeDataDisplayActions... types) {
//            Log.d(TAG, "On doInBackground...");
            Log.d("On doInBackground... ", String.valueOf(types[0]));

            switch (types[0]) {
                case DELETE_ENTRY:  // Delete chosen entry
                    hikeDataSource.deleteHikeData(hikeData);
                    Log.d(TAG, "Hike deleted");
                    break;
                case UPDATE_ENTRY:  // Edit and update chosen entry
                    hikeDataSource.update(hikeData);
                    Log.d(TAG, "Hike updated");
                    break;
                case SAVE_ENTRY:
                    hikeDataSource.save(hikeData);
                    Log.d(TAG, "Hike saved");
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Toast.makeText(getApplicationContext(),"This hike was not saved",
                    Toast.LENGTH_SHORT).show();
            finish();   // Exit activity
        }
    }

}
