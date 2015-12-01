package com.csci5448.hiketracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class SaveHikeDataActivity extends AppCompatActivity {

    private static final String TAG = "SaveHikeDatActivity";
    private Button saveHikeBttn;
    private Button cancelHikeBttn;
    private Bundle bundle;
    HikeData hikeData, newHikeData;
//    User user;
    HikeDataSource hikeDataSource;
    private boolean readyToFinish = false;

    private long oldTime;   // used for updating hikes
    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 100;
    static final int TIME_DIALOG_ID = 200;
    static final int PEAK_DIALOG_ID = 300;

    // Layout Variables
    TextView mountainNameField, hikeDateField, hikeLengthField;

//    DatePicker datePicker;

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
//        user = bundle.getParcelable(getString(R.string.passUser));

        String source = bundle.getString(getString(R.string.sourceString));

        // Setup Buttons
        saveHikeBttn = (Button)findViewById(R.id.saveHikeBttn);
        cancelHikeBttn = (Button)findViewById(R.id.cancelHikeBttn);
        buttonSetup();

        calendar = Calendar.getInstance();
        calendar.setTime(hikeData.getHikeDate());

        // Set up values
        mountainNameField = (TextView)findViewById(R.id.mountainNameField);
        hikeDateField  = (TextView)findViewById(R.id.hikeDateField);
        hikeLengthField  = (TextView)findViewById(R.id.hikeLengthField);

        // Fill in TextView fields
        mountainNameField.setText(hikeData.getPeakName());
//        hikeDateField.setText(String.valueOf(hikeData.getHikeDate()));
        updateLabel();  // Setup date field
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
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = (String) hikeLengthField.getText();
        String hrs = time.substring(0,2);
        String minutes = time.substring(3,5);
        int hour = Integer.parseInt(hrs);
        int minute = Integer.parseInt(minutes);
        return (hour*60+minute)*60;
    }

    private void setupForEditing() {
//        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mountainListRadioGroup);

        getCurrentDate();

        Button editPeakBttn = (Button) findViewById(R.id.editPeakBttn);
        Button editDateBttn = (Button) findViewById(R.id.editDateBttn);
        Button editLengthBttn = (Button) findViewById(R.id.editLengthBttn);

        editDateBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit Date Button clicked");
                showDialog(DATE_DIALOG_ID);
            }
        });

        editPeakBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit Peak Button clicked");
                showDialog(PEAK_DIALOG_ID);
            }
        });

        editLengthBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit Length Button clicked");
                // custom dialog
                LengthPickerDialog lengthPickerDialog = new LengthPickerDialog(SaveHikeDataActivity.this, hikeLengthField);
                lengthPickerDialog.show();
            }
        });

        // Change save button to update button
        saveHikeBttn.setText(R.string.updateBttnLabel);
        saveHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Update Button clicked");
                updateHikeData();
                Toast.makeText(getApplicationContext(),"Hike was Updated!",
                        Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                Log.d(TAG, "Change Date Button pressed");
                return new DatePickerDialog(this, datePickerListener, year, month,day);
            case TIME_DIALOG_ID:
                Log.d(TAG, "Change Length Button pressed");
                break;
            case PEAK_DIALOG_ID:
                Log.d(TAG, "Change Peak Button pressed");
                break;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            setCurrentDate();
            updateLabel();
        }
    };


    // display current date both on the text view and the Date Picker when the application starts.
    public void getCurrentDate() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setCurrentDate(){
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        hikeDateField.setText(sdf.format(calendar.getTimeInMillis()));
    }

    private void updateHikeData() {
        // Copy over old data
        newHikeData = hikeData;

        // Delete old data
        deleteEntry();

        // Insert new data into newHikeData
        newHikeData.setHikeLength(longFromTime());
        newHikeData.setPeakName(String.valueOf(mountainNameField.getText()));

        setCurrentDate();
        newHikeData.setHikeDate(new Date(calendar.getTimeInMillis()));

        // Save new data
        updateEntry();
    }

    private void setupforSaving() {
        // Hide editing fields
        hideEditingFields();
    }

    // Hide editing fields
    private void hideEditingFields(){
        LinearLayout editLayout = (LinearLayout)findViewById(R.id.editBttnsLayout);
        editLayout.setVisibility(View.INVISIBLE);
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
//                while (!readyToFinish); // Wait for updates to occur
                Toast.makeText(getApplicationContext(),"Hike was DELETED!",
                        Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void buttonSetup(){
        saveHikeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Save Hike Button clicked");
                saveEntry();
//                while (!readyToFinish); // Wait for updates to occur
                Toast.makeText(getApplicationContext(),"Hike was SAVED!",
                        Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
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
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
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

        HikeDataDisplayActions hikeDataDisplayActions;
        UserDataSource userDataSource;
        MountainDataSource mountainDataSource;
        ArrayList<Mountain> mountains;
        ArrayList<HikeData> hikes;

        @Override
        protected Void doInBackground(HikeDataDisplayActions... types) {
            // Access database objects
            Log.d("On doInBackground... ", String.valueOf(types[0]));
            userDataSource = new UserDataSource(getApplicationContext());
            mountainDataSource = new MountainDataSource(getApplicationContext());

            switch (types[0]) {
                case DELETE_ENTRY:  // Delete chosen entry
                    hikeDataDisplayActions = HikeDataDisplayActions.DELETE_ENTRY;
                    hikeDataSource.deleteHikeData(hikeData);
                    Log.d(TAG, "Hike deleted");
//                    updateMountainHikedField(false);
//                    user.subtractNewHike(hikeData.getHikeLength());
//                    userDataSource.update(user);
                    break;
                case UPDATE_ENTRY:  // Edit and update chosen entry
                    hikeDataDisplayActions = HikeDataDisplayActions.UPDATE_ENTRY;
                    hikeDataSource.save(newHikeData);
                    Log.d(TAG, "Hike updated");
//                    updateMountainHikedField(true);
//                    updateLastPeakHikedField();
//                    user.addNewHike(hikeData.getHikeLength());
//                    userDataSource.update(user);
//                    mountains = (ArrayList) mountainDataSource.getMountains();
                    break;
                case SAVE_ENTRY:
                    hikeDataDisplayActions = HikeDataDisplayActions.SAVE_ENTRY;
                    hikeDataSource.save(hikeData);
                    Log.d(TAG, "Hike saved");
//                    updateMountainHikedField(true);
//                    user.setMostRecent(hikeData.getPeakName());
//                    user.addNewHike(hikeData.getHikeLength());
//                    userDataSource.update(user);
//                    mountains = (ArrayList) mountainDataSource.getMountains();
                    break;
            }
            mountains = (ArrayList) mountainDataSource.getMountains();
            hikes = (ArrayList)hikeDataSource.getAllHikes();
            Log.d(TAG, "Finished in background");
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d(TAG, "starting postexecute");
//            synchronized (MainActivity.user) {
                Log.d(TAG, "synchronized user");
                switch (hikeDataDisplayActions) {
                    case DELETE_ENTRY:
                        updateMountainHikedField(false);
                        MainActivity.user.subtractNewHike(hikeData.getHikeLength());
//                        userDataSource.update(user);
                        break;
                    case SAVE_ENTRY:
                        Log.d(TAG, "updating hiked field");
                        updateMountainHikedField(true);
                        Log.d(TAG, "Updated Hiked field");
                        MainActivity.user.setMostRecent(hikeData.getPeakName());
                        MainActivity.user.addNewHike(hikeData.getHikeLength());
//                        userDataSource.update(user);
                        break;
                    case UPDATE_ENTRY:
                        updateMountainHikedField(true);
                        updateLastPeakHikedField();
                        MainActivity.user.addNewHike(hikeData.getHikeLength());

                        break;
                }
            userDataSource.update(MainActivity.user);
                Log.d(TAG, "Updated user");
//            }
            readyToFinish = true;
            Log.d(TAG, "Finished in post execute");
        }

        private void updateMountainHikedField(boolean insertion){
//            ArrayList<Mountain> mountains = (ArrayList) mountainDataSource.getMountains();
            for (Mountain mount : mountains) {
                Log.d(TAG, "Finding mountain just hiked");
                // Find mountain just hiked
                if (mount.getmName().equals(hikeData.getPeakName())) {
                    Log.d(TAG, "Found it");
                    if (insertion){
                        // Change mountain hiked to true if not already
                        if (!mount.isHiked()) {
                            mount.setHiked(true);
                            mountainDataSource.update(mount);
                            Log.d(TAG, "Set " + mount.getmName() + " to HIKED");
                            MainActivity.user.addOneSummit();
                            Log.d(TAG, "Incremented summit count");
                        }
                    }
                    else {
                        // Check to see if there are other hikes for this mountain
//                        ArrayList<HikeData> hikes = (ArrayList)hikeDataSource.getAllHikes();
//                        synchronized (hikes){
                        Log.d(TAG, "Sorting by date");
                            Collections.sort(hikes); // Sorted by date
                            if (hikes.size() > 0) MainActivity.user.setMostRecent(hikes.get(0).getPeakName());  // also update LastPeakHiked field
                            else MainActivity.user.setMostRecent(getString(R.string.nothingYetLabel));
                            int hikeCount = 0;
                            for (HikeData hikeData:hikes){
                                if (hikeData.getPeakName().equals(hikeData.getPeakName())){
                                    hikeCount++;
                                }
                            }
                            if (hikeCount == 0) {
                                // Removed was the only hike for this mountain
                                mount.setHiked(false);
                                mountainDataSource.update(mount); // Update hiked record
                                Log.d(TAG, "Set " + mount.getmName() + " to NOT Hiked");
                                MainActivity.user.subtractOneSummit();
                                Log.d(TAG, "Decremented summit count");
                            }
//                        }
                    }
                }
            }
        }

        private void updateLastPeakHikedField(){
            ArrayList<HikeData> hikes = (ArrayList)hikeDataSource.getAllHikes();
            Collections.sort(hikes);    // Sort by date
            if (hikes.size() > 0) MainActivity.user.setMostRecent(hikes.get(0).getPeakName());
            else MainActivity.user.setMostRecent(getString(R.string.nothingYetLabel));
        }
    }

}
