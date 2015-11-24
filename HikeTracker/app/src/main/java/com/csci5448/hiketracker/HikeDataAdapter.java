package com.csci5448.hiketracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by diana on 11/18/15.
 */
public class HikeDataAdapter extends ArrayAdapter<HikeData> {

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "MM-dd-yyyy", Locale.ENGLISH);

    private static final String TAG = "HikeDataAdapter";

    public HikeDataAdapter(Context context, ArrayList<HikeData> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HikeData hikeData = getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view;
        if (null == convertView) {
            view = inflater.inflate(R.layout.row_layout, parent, false);
        } else {
            view = (View)convertView;
        }

        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
//        }

//        view = inflater.inflate(R.layout.row_layout, null);
        // Lookup view for data population
        TextView hikeDate = (TextView) view.findViewById(R.id.hikeDate);
        TextView hikePeak = (TextView) view.findViewById(R.id.hikePeak);
        TextView hikeLength = (TextView) view.findViewById(R.id.hikeLength);

        // Populate the data into the template view using the data object
        String formattedDate = formatter.format(hikeData.getHikeDate().getTime());
        hikeDate.setText(String.format("Hike Date:%s -- ", formattedDate));
        hikePeak.setText(String.format("Peak: %s", hikeData.getPeakName()));

        //hh:mm
        // TODO: Currently projecting that we'll be storing total seconds; may change to total minutes
        int seconds = hikeData.getHikeLength();
        hikeLength.setText(String.format("Time: %s", String.format("%02d:%02d",
                TimeUnit.SECONDS.toHours(seconds),
                TimeUnit.SECONDS.toMinutes(seconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds))
        )));

        // Return the completed view to render on screen
        return view;
    }

}
