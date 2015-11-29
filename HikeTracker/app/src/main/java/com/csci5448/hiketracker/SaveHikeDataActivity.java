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
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class SaveHikeDataActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    private static final String TAG = "SaveHikeDatActivity";
    private Button saveHikeBttn;
    private Button cancelHikeBttn;
    private Bundle bundle;
    HikeData hikeData;
    User user;
    HikeDataSource hikeDataSource;

    private long oldTime;   // used for updating hikes

    // Layout Variables
    TextView mountainNameField, hikeDateField, hikeLengthField;
    NumberPicker hr1, hr0, min1, min0;

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
        user = bundle.getParcelable(getString(R.string.passUser));

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

    private long longFromTime(){
        int hrs = hr1.getValue()*10+hr0.getValue();
        int mins = min1.getValue()*10 + min0.getValue();
        long secs = mins*60+hrs*60*60;
        return secs;
    }

    private void setupForEditing() {
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.mountainListRadioGroup);



        // Change save button to update button
        saveHikeBttn.setText(R.string.updateBttnLabel);
        saveHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Save Hike Button clicked");
                updateHikeData();
                updateEntry();
            }
        });

        oldTime = hikeData.getHikeLength();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal)
    {
        // Update hike length field
        hikeLengthField.setText(String.format("%d%d:%d%d:00",
                hr1.getValue(), hr0.getValue(),
                min1.getValue(), min0.getValue()));
    }

    private void updateHikeData() {
        hikeData.setHikeLength(longFromTime());
        hikeData.setPeakName(String.valueOf(mountainNameField.getText()));
    }

    private void setupPickers(){
    // Setup Number Pickers
        hr1 = (NumberPicker)findViewById(R.id.hr1picker);
        hr0 = (NumberPicker)findViewById(R.id.hr0picker);
        min1 = (NumberPicker)findViewById(R.id.min1picker);
        min0 = (NumberPicker)findViewById(R.id.min0picker);

        hr0.setMinValue(0);
        hr0.setMaxValue(9);
        hr0.setMinValue(0);
        hr1.setMaxValue(9);

        min0.setMinValue(0);
        min0.setMaxValue(9);
        min1.setMinValue(0);
        min1.setMaxValue(9);

        hr0.setWrapSelectorWheel(true);
        hr1.setWrapSelectorWheel(true);
        min0.setWrapSelectorWheel(true);
        min1.setWrapSelectorWheel(true);

        hr0.setOnValueChangedListener(this);
        hr1.setOnValueChangedListener(this);
        min0.setOnValueChangedListener(this);
        min1.setOnValueChangedListener(this);
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
        saveHikeBttn.setEnabled(true);
        saveHikeBttn.setVisibility(View.VISIBLE);

        cancelHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel Button clicked");
                Toast.makeText(getApplicationContext(), "This hike was not saved",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        cancelHikeBttn.setEnabled(true);
        cancelHikeBttn.setVisibility(View.VISIBLE);
    }

    // Task Calls
    private void deleteEntry() {new manipulateHikeDataTasks().execute(HikeDataDisplayActions.DELETE_ENTRY); }
    private void updateEntry() {new manipulateHikeDataTasks().execute(HikeDataDisplayActions.UPDATE_ENTRY); }
    private void saveEntry() {new manipulateHikeDataTasks().execute(HikeDataDisplayActions.SAVE_ENTRY); }


    private enum HikeDataDisplayActions {
        DELETE_ENTRY, UPDATE_ENTRY, SAVE_ENTRY
    }

    //    Asynchronous Task to Access SQLite Database
    public class manipulateHikeDataTasks extends AsyncTask<HikeDataDisplayActions, Void, Void> {

        @Override
        protected Void doInBackground(HikeDataDisplayActions... types) {
//            Log.d(TAG, "On doInBackground...");
            Log.d("On doInBackground... ", String.valueOf(types[0]));


            UserDataSource userDataSource = new UserDataSource(getApplicationContext());
            switch (types[0]) {
                case DELETE_ENTRY:  // Delete chosen entry
                    hikeDataSource.deleteHikeData(hikeData);
                    Log.d(TAG, "Hike deleted");
                    Toast.makeText(getApplicationContext(),"Hike was DELETED",
                            Toast.LENGTH_SHORT).show();
                    updateMountainHikedField(false);
                    user.subtractNewHike(hikeData.getHikeLength());

                    userDataSource.update(user);
                    break;
                case UPDATE_ENTRY:  // Edit and update chosen entry
                    hikeDataSource.update(hikeData);
                    Toast.makeText(getApplicationContext(),"Hike was UPDATED",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Hike updated");

                    break;
                case SAVE_ENTRY:
                    hikeDataSource.save(hikeData);
                    Log.d(TAG, "Hike saved");
                    Toast.makeText(getApplicationContext(),"Hike was SAVED!",
                            Toast.LENGTH_SHORT).show();
                    updateMountainHikedField(true);
                    user.setMostRecent(hikeData.getPeakName());
                    user.addNewHike(hikeData.getHikeLength());
                    userDataSource.update(user);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            finish();   // Exit activity
        }

        private void updateMountainHikedField(boolean insertion){
            MountainDataSource mountainDataSource = new MountainDataSource(getApplicationContext());
            ArrayList<Mountain> mountains = (ArrayList) mountainDataSource.getMountains();
            for (Mountain mount : mountains) {
                // Find mountain just hiked
                if (mount.getmName().equals(hikeData.getPeakName())) {
                    if (insertion){
                        // Change mountain hiked to true if not already
                        if (!mount.isHiked()) {
                            mount.setHiked(true);
                            mountainDataSource.save(mount);
                            Log.d(TAG, "Set " + mount.getmName() + " to HIKED");
                            user.addOneSummit();
                            Log.d(TAG, "Incremented summit count");

                        }
                    }
                    else {
                        // Check to see if there are other hikes for this mountain
                        ArrayList<HikeData> hikes = (ArrayList)hikeDataSource.getAllHikes();
                        Collections.sort(hikes); // Sorted by date
                        user.setMostRecent(hikes.get(0).getPeakName());  // also update LastPeakHiked field
                        int hikeCount = 0;
                        for (HikeData hikeData:hikes){
                            if (hikeData.getPeakName().equals(hikeData.getPeakName())){
                                hikeCount++;
                            }
                        }
                        if (hikeCount == 0) {
                            // Removed was the only hike for this mountain
                            mount.setHiked(false);
                            mountainDataSource.save(mount); // Update hiked record
                            Log.d(TAG, "Set " + mount.getmName() + " to NOT Hiked");
                            user.subtractOneSummit();
                            Log.d(TAG, "Decremented summit count");
                        }
                    }
                }
            }
        }

        private void updateLastPeakHikedField(){

        }

   }

}
