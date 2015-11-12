package oodesignanalysis.hiketracker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Ryan on 10/28/15.
 */
public class LocatorActivity extends FragmentActivity implements OnMapReadyCallback {

    /*******************  Class variables *******************/
    private MountainDataSource mountainDataSource;
    private MapView mapView;
    private List<Mountain> mountain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_locator);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    /**
     * Populates mountain info
     */
    public void mountainInfo() {

    }

    /**
     * Transitions to the hike activity
     */
    public void startHikeActivity() {

    }

    /**
     * Allows manual entry of hike data TODO: How?
     */
    public void manualEntry() {

    }
}
