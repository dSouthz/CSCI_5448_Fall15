package oodesignanalysis.hiketracker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Ryan on 10/28/15.
 */
public class LocatorActivity extends FragmentActivity implements OnMapReadyCallback {

    /*******************  Class variables *******************/
    private MountainDataSource mountainDataSource;
    private List<Mountain> mountains;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        mountainDataSource = new MountainDataSource(this);  // initialize mountain db

        mountainDataSource.database.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_MOUNTAIN);
        mountainDataSource.database.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_HIKEDATA);

        DatabaseHelper.getHelper(this).onCreate(mountainDataSource.database);

        if (mountainDataSource.getMountains().size() <= 0) {
            // Mountains have never been loaded before --> Load mountain data
            mountainDataSource.loadMountains(); // load mountain data into database
        }

        mountains = mountainDataSource.getMountains();   // local list of mountain data


        // Get the map and register for the ready callback
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setContentView(R.layout.activity_locator);

        mountainDataSource = new MountainDataSource(this);  // initialize mountain db
        if (mountainDataSource.getMountains().size() <= 0) {
            // Mountains have never been loaded before --> Load mountain data
            mountainDataSource.loadMountains(); // load mountain data into database
        }

        mountains = mountainDataSource.getMountains();   // local list of mountain data


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


        mMap = map;
        // Add all mountain markers to map
        for (Mountain mount : mountains) {
            // Info that will pop up when user clicks on marker
            String snippet = new String(mount.getmName() + ", "
            + mount.getmRange() + "\n" + mount.getmElevation());

            // TODO: Update Marker snippet to include listener to start hike recording

            map.addMarker(new MarkerOptions()
                .position(new LatLng(mount.getmLatitude(), mount.getmLongitude()))
                .title(mount.getmName()).draggable(false)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mountain_marker))
            );
        }

        Mountain firstMountain = mountains.get(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstMountain.getmLatitude(), firstMountain.getmLongitude()), 4.0f));
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
