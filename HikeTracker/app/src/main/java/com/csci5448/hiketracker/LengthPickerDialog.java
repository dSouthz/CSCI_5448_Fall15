package com.csci5448.hiketracker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by diana on 11/29/15.
 */
public class LengthPickerDialog extends Dialog implements View.OnClickListener {

    private TextView toUpdate;
    private Dialog dialog;
    private Button okPickerButton, cancelPickerButton;

    NumberPicker hr1, hr0, min1, min0;

    public LengthPickerDialog(Activity activity, TextView toUpdate) {
        super(activity);
        this.toUpdate = toUpdate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_length_picker);

        setupPickers();

        okPickerButton = (Button) findViewById(R.id.okPickerBttn);
        cancelPickerButton = (Button) findViewById(R.id.cancelPickerBttn);

        okPickerButton.setOnClickListener(this);
        cancelPickerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okPickerBttn:
                toUpdate.setText(String.format("%s%s%s%s:00", String.valueOf(hr1.getValue()), String.valueOf(hr0.getValue() + ":"), String.valueOf(min1.getValue()), String.valueOf(min0.getValue())));
                break;
            case R.id.cancelPickerBttn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
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
        min1.setMaxValue(5);

        hr0.setWrapSelectorWheel(true);
        hr1.setWrapSelectorWheel(true);
        min0.setWrapSelectorWheel(true);
        min1.setWrapSelectorWheel(true);

    }
}
