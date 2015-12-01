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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class SaveHikeDataActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    private static final String TAG = "SaveHikeDatActivity";
    private Button saveHikeBttn;
    private Button cancelHikeBttn;
    private Bundle bundle;
    HikeData hikeData, newHikeData;
    User user;
    HikeDataSource hikeDataSource;

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
    NumberPicker hr1, hr0, min1, min0;
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
        return (long) (mins*60+hrs*60*60);
    }

    private void setupForEditing() {
//        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mountainListRadioGroup);

        setCurrentDate();

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
                showDialog(TIME_DIALOG_ID);
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

//        oldTime = hikeData.getHikeLength();
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

            // set selected date into Text View
            hikeDateField.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year).append(" "));
        }
    };


    // display current date both on the text view and the Date Picker when the application starts.
    public void setCurrentDate() {

//        datePicker = (DatePicker) findViewById(R.id.datePicker);

        final Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
//        hikeDateField.setText(new StringBuilder()
//                // Month is 0 based, so you have to add 1
//                .append(month + 1).append("-")
//                .append(day).append("-")
//                .append(year).append(" "));

        // set current date into Date Picker
//        datePicker.init(year, month, day, null);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        hikeDateField.setText(sdf.format(calendar.getTimeInMillis()));
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
        // Copy over old data
        newHikeData = hikeData;

        // Delete old data
        deleteEntry();

        // Insert new data into newHikeData
        newHikeData.setHikeLength(longFromTime());
        newHikeData.setPeakName(String.valueOf(mountainNameField.getText()));
        newHikeData.setHikeDate(new Date(calendar.getTimeInMillis()));

        // Save new data
        updateEntry();
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

    private void setupDatePicker(){
//        datePicker = (DatePicker) findViewById(R.id.datePicker);
        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

//        datePicker.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(SaveHikeDataActivity.this, date, calendar
//                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
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

        @Override
        protected Void doInBackground(HikeDataDisplayActions... types) {
//            Log.d(TAG, "On doInBackground...");
//            synchronized (hikeData) {
                Log.d("On doInBackground... ", String.valueOf(types[0]));


                UserDataSource userDataSource = new UserDataSource(getApplicationContext());
                switch (types[0]) {
                    case DELETE_ENTRY:  // Delete chosen entry
                        hikeDataSource.deleteHikeData(hikeData);
                        Log.d(TAG, "Hike deleted");
                        updateMountainHikedField(false);
                        user.subtractNewHike(hikeData.getHikeLength());

                        userDataSource.update(user);
                        break;
                    case UPDATE_ENTRY:  // Edit and update chosen entry
                        hikeDataSource.save(newHikeData);
                        Log.d(TAG, "Hike updated");
                        updateMountainHikedField(true);
                        updateLastPeakHikedField();
                        user.addNewHike(hikeData.getHikeLength());
                        userDataSource.update(user);
                        break;
                    case SAVE_ENTRY:
                        hikeDataSource.save(hikeData);
                        Log.d(TAG, "Hike saved");
                        updateMountainHikedField(true);
                        user.setMostRecent(hikeData.getPeakName());
                        user.addNewHike(hikeData.getHikeLength());
                        userDataSource.update(user);
                        break;
                }
                return null;
//            }
        }

        @Override
        protected void onPostExecute(Void v) {
//            finish();   // Exit activity
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
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                        synchronized (hikes){
                            Collections.sort(hikes); // Sorted by date
                            if (hikes.size() > 0) user.setMostRecent(hikes.get(0).getPeakName());  // also update LastPeakHiked field
                            else user.setMostRecent(getString(R.string.nothingYetLabel));
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
//                        }
//                        try {
//                            wait(1000);  // wait for hikes to load
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                    }
                }
            }
        }

        private void updateLastPeakHikedField(){
            ArrayList<HikeData> hikes = (ArrayList)hikeDataSource.getAllHikes();
            Collections.sort(hikes);    // Sort by date
            if (hikes.size() > 0) user.setMostRecent(hikes.get(0).getPeakName());
            else user.setMostRecent(getString(R.string.nothingYetLabel));
        }

    }

}
