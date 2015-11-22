package com.csci5448.hiketracker;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by diana on 11/21/15.
 */
public class MountainInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View myView = null;
    private LayoutInflater inflater = null;

    MountainInfoWindowAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (myView == null) {
            myView=inflater.inflate(R.layout.mountaininfowindoadapter, null);
        }

        TextView textView=(TextView)myView.findViewById(R.id.title);

        textView.setText(marker.getTitle());
        textView=(TextView)myView.findViewById(R.id.snippet);
        textView.setText(marker.getSnippet());

        return(myView);
    }

}
