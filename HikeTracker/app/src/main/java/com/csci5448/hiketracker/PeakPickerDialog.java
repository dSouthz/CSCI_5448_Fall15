package com.csci5448.hiketracker;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 11/29/15.
 */
public class PeakPickerDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "PeakPickerDialog";
    private TextView toUpdate;
    private Button okPickerButton, cancelPickerButton;
    ArrayList<Mountain> mountains;
    Activity activity;
    Spinner peakPicker;
    String selectedItem;

    public PeakPickerDialog(Activity activity, TextView toUpdate) {
        super(activity);
        this.activity = activity;
        this.toUpdate = toUpdate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_peak_picker);

        mountains = new ArrayList<>();

        getList();

        okPickerButton = (Button) findViewById(R.id.okPeakPickerBttn);
        cancelPickerButton = (Button) findViewById(R.id.cancelPeakPickerBttn);

        peakPicker = (Spinner)findViewById(R.id.peakPicker);

        peakPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedItem = (String)peakPicker.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        okPickerButton.setOnClickListener(this);
        cancelPickerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okPickerBttn:
//                toUpdate.setText(peakPicker.getSelectedItem().toString());
                toUpdate.invalidate();
                toUpdate.setText("Can");
                toUpdate.invalidate();
                break;
            case R.id.cancelPickerBttn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    private void getList(){
        new GetMountainTask().execute();
    }

    public class GetMountainTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("DoINBackGround", "On doInBackground...");
            MountainDataSource mountainDataSource = new MountainDataSource(activity.getBaseContext());
            mountains = (ArrayList<Mountain>) mountainDataSource.getMountains();      // Load previously stored mountain data

            if (mountains.size() <= 0) {    // no mountain data has yet been loaded
                Log.d(TAG, "No mountains loaded, loading mountains");
                mountainDataSource.loadMountains();  // Load mountain data into the database
                while (mountainDataSource.getMountains().size() < 58)
                    // One time, only 8 mountains were correctly loaded for some reason. This prevents that.
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                mountains = (ArrayList<Mountain>) mountainDataSource.getMountains();
                Log.d(TAG, "Mountains Loaded");
                Log.d(TAG, "Size: " + String.valueOf(mountains.size()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            updateSpinner();
        }

        private void updateSpinner(){
            List<String> list = new ArrayList<>();

        }
    }
}
