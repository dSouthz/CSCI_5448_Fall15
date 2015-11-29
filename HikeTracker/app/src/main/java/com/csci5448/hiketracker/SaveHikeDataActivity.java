package com.csci5448.hiketracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

public class SaveHikeDataActivity extends AppCompatActivity {

    private static final String TAG = "SaveHikeDataCtivity";
    private Button saveHikeBttn, cancelHikeBttn;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_hike_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        if (bundle == null) {
            // No extras were put in --> something went wrong
            Log.d(TAG, "Bundle is empty");
            finish();
        }

        HikeData hikeData = bundle.getParcelable(MainActivity.SHARED_PARCEL);
        String source = bundle.getString(MainActivity.SOURCE_STRING);

        if (source.equals(HikeActivity.TAG)) {
            // Pull from HikeData parcel
            setupforSaving();
        }

        else {
            // Pull from HikeData parcel

            // Check to see if this actiivty will display editing fields
            Boolean check = getIntent().getExtras().getBoolean(HistoryActivity.EDIT_TAG);

            if (check) {
                setupForEditing();  // Setup fields for editing
            }
            else {
                // If coming from HistoryActivity and it isn't for editing, it's for deleting
                setupForDeleting();
            }
        }

    }

    private void setupForEditing() {

    }

    private void setupforSaving() {

    }

    private void setupForDeleting() {

    }
}
