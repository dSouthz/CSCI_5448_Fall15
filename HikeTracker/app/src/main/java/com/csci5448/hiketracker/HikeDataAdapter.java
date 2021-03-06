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

        // Lookup view for data population
        TextView hikeDate = (TextView) view.findViewById(R.id.hikeDate);
        TextView hikePeak = (TextView) view.findViewById(R.id.hikePeak);
        TextView hikeLength = (TextView) view.findViewById(R.id.hikeLength);

        // Populate the data into the template view using the data object
        String formattedDate = formatter.format(hikeData.getHikeDate().getTime());
        hikeDate.setText(String.format("Hike Date:%s -- ", formattedDate));
        hikePeak.setText(String.format("Peak: %s", hikeData.getPeakName()));

        //hh:mm
        hikeLength.setText(TimeHelper.timeFromLong(hikeData.getHikeLength()));

        // Return the completed view to render on screen
        return view;
    }


}
