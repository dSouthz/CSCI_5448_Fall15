package com.csci5448.hiketracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
    public final String ENABLED_COLOR = "#666666";
    public final String DISABLED_COLOR = "#555555";

    private Button saveHikeBttn;
    private Button cancelHikeBttn;
    HikeData hikeData, newHikeData;
    ArrayList<Mountain> mountains;
    ArrayList<String> mountainNameList;
        HikeDataSource hikeDataSource;

    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 100;

    // Layout Variables
    TextView mountainNameField, hikeDateField, hikeLengthField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_hike_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hikeDataSource = new HikeDataSource(this);

        Bundle bundle = getIntent().getExtras();
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

        calendar = Calendar.getInstance();
        calendar.setTime(hikeData.getHikeDate());
        Log.d(TAG, "Calender is: " + calendar.toString());

        // Set up values
        mountainNameField = (TextView)findViewById(R.id.mountainNameField);
        hikeDateField  = (TextView)findViewById(R.id.hikeDateField);
        hikeLengthField  = (TextView)findViewById(R.id.hikeLengthField);

        // Fill in TextView fields
        mountainNameField.setText(hikeData.getPeakName());
        updateHikeDateLabel();  // Setup date field
        hikeLengthField.setText(TimeHelper.timeFromLong(hikeData.getHikeLength()));

        if (source != null) {
            if (source.equals(HikeActivity.TAG)) {
                // new hikedata to be saved
                setupforSaving();
            }

            else {
                // Previously saved hikedata to be edited or deleted
                // Check to see if this activity will display editing fields

                Boolean check = getIntent().getExtras().getBoolean(getString(R.string.editString));

                if (check) {
                    setupForEditing(getIntent().getExtras().getBoolean(getString(R.string.newString)));  // Setup fields for editing
                }
                else {
                    // If coming from HistoryActivity and it isn't for editing, it's for deleting
                    setupForDeleting();
                }
            }
        }

    }


    private void setupForEditing(Boolean newHike) {
        getCurrentDate();

        loadMountains();
        Button editDateBttn = (Button) findViewById(R.id.editDateBttn);
        Button editLengthBttn = (Button) findViewById(R.id.editLengthBttn);

        editDateBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit Date Button clicked");
                showDialog(DATE_DIALOG_ID);
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
        if(!newHike) {
            saveHikeBttn.setText(R.string.updateBttnLabel);
            saveHikeBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Update Button clicked");
                    updateHikeData(false);
                    Toast.makeText(getApplicationContext(), "Hike was Updated!",
                            Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        } else {
            saveHikeBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Update Button clicked");
                    updateHikeData(true);
                    Toast.makeText(getApplicationContext(), "Hike was Saved!",
                            Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

    }

    private void setChangePeakButtn(){
        Button editPeakBttn = (Button) findViewById(R.id.editPeakBttn);
        editPeakBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit Peak Button clicked");
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveHikeDataActivity.this);
                builder.setTitle("Change Peak");
                final String[] list = mountainNameList.toArray(new String[mountainNameList.size()]);
                builder.setItems(list, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SaveHikeDataActivity.this, "Clicked on " + list[which], Toast.LENGTH_SHORT).show();
                        mountainNameField.setText(list[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
            };
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                Log.d(TAG, "Change Date Button pressed");
                return new DatePickerDialog(this, datePickerListener, year, month,day);
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
            updateHikeDateLabel();
        }
    };


    // display current date both on the text view and the Date Picker when the application starts.
    private void getCurrentDate() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setCurrentDate(){
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    private void updateHikeDateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        hikeDateField.setText(sdf.format(calendar.getTimeInMillis()));
    }

    private void updateHikeData(Boolean newHike) {
        // Copy over old data
        Log.d(TAG, "Old Hike Data = " + hikeData.toString());
        newHikeData = hikeData;

        // Delete old data
        if(!newHike) {
            deleteEntry();
        }

        // Insert new data into newHikeData
        newHikeData.setHikeLength(TimeHelper.longFromTime((String) hikeLengthField.getText()));
        newHikeData.setPeakName(String.valueOf(mountainNameField.getText()));

        setCurrentDate();
        newHikeData.setHikeDate(new Date(calendar.getTimeInMillis()));

        Log.d(TAG, "New Hike Data = " + newHikeData.toString());

        // Save new data
        if(!newHike) {
            updateEntry();
        } else {
            saveEntry();
        }
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
                Toast.makeText(getApplicationContext(), "Hike was DELETED!",
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
        saveHikeBttn.setBackgroundColor(Color.parseColor(ENABLED_COLOR));

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
        saveHikeBttn.setBackgroundColor(Color.parseColor(ENABLED_COLOR));

    }

    // Task Calls
    private void deleteEntry() {new manipulateHikeDataTasks().execute(HikeDataDisplayActions.DELETE_ENTRY); }
    private void updateEntry() {new manipulateHikeDataTasks().execute(HikeDataDisplayActions.UPDATE_ENTRY); }
    private void saveEntry() {new manipulateHikeDataTasks().execute(HikeDataDisplayActions.SAVE_ENTRY); }
    private void loadMountains() {new manipulateHikeDataTasks().execute(HikeDataDisplayActions.LOAD_MOUNTAINS); }


    private enum HikeDataDisplayActions {
        DELETE_ENTRY, UPDATE_ENTRY, SAVE_ENTRY, LOAD_MOUNTAINS
    }

    //    Asynchronous Task to Access SQLite Database
    public class manipulateHikeDataTasks extends AsyncTask<HikeDataDisplayActions, Void, Void> {

        HikeDataDisplayActions hikeDataDisplayActions;
        UserDataSource userDataSource;
        MountainDataSource mountainDataSource;
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
                    break;
                case UPDATE_ENTRY:  // Edit and update chosen entry
                    hikeDataDisplayActions = HikeDataDisplayActions.UPDATE_ENTRY;
                    hikeDataSource.save(newHikeData);
                    Log.d(TAG, "Hike updated");
                    break;
                case SAVE_ENTRY:
                    hikeDataDisplayActions = HikeDataDisplayActions.SAVE_ENTRY;
                    hikeDataSource.save(hikeData);
                    Log.d(TAG, "Hike saved");
                    break;
                case LOAD_MOUNTAINS:
                    hikeDataDisplayActions = HikeDataDisplayActions.LOAD_MOUNTAINS;
                    Log.d(TAG, "Loaded all mountains");
                    break;
            }
            if (mountains == null) {
                mountains = (ArrayList) mountainDataSource.getMountains();
                if(mountains.size() == 0) {
                    mountainDataSource.loadMountains();
                    mountains = (ArrayList) mountainDataSource.getMountains();
                }
            }
            hikes = (ArrayList)hikeDataSource.getAllHikes();
            Log.d(TAG, "Finished in background");
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d(TAG, "starting postexecute");
            switch (hikeDataDisplayActions) {
                case DELETE_ENTRY:
                    updateMountainHikedField(false);
                    MainActivity.user.subtractNewHike(hikeData.getHikeLength());
                    break;
                case SAVE_ENTRY:
                    Log.d(TAG, "updating hiked field");
                    updateMountainHikedField(true);
                    Log.d(TAG, "Updated Hiked field");
                    MainActivity.user.setMostRecent(hikeData.getPeakName());
                    MainActivity.user.addNewHike(hikeData.getHikeLength());
                    break;
                case UPDATE_ENTRY:
                    updateMountainHikedField(true);
                    updateLastPeakHikedField();
                    MainActivity.user.addNewHike(hikeData.getHikeLength());
                    break;
                case LOAD_MOUNTAINS:
                    mountainNameList = new ArrayList<>();
                    for (Mountain mount:mountains){
                        mountainNameList.add(mount.getmName());
                    }
                    setChangePeakButtn();
            }
            userDataSource.update(MainActivity.user);
            Log.d(TAG, "Updated user");
        }

        private void updateMountainHikedField(boolean insertion){
            for (Mountain mount : mountains) {
                // Find mountain just hiked
                if (mount.getmName().equals(hikeData.getPeakName())) {
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
                        Collections.sort(hikes); // Sorted by date
                        if (hikes.size() > 0) MainActivity.user.setMostRecent(hikes.get(0).getPeakName());  // also update LastPeakHiked field
                        else MainActivity.user.setMostRecent(getString(R.string.nothingYetLabel));
                        int hikeCount = 0;
                        for (HikeData hike:hikes){
                            if (hike.getPeakName().equals(hikeData.getPeakName())){
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
