package oodesignanalysis.hiketracker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Ryan on 10/28/15.
 */
public class LocatorActivity extends FragmentActivity implements OnMapReadyCallback {

    /*******************  Class variables *******************/
    private MountainDataSource mountainDataSource;
    private List<Mountain> mountains;

    private final float DEFAULT_ZOOM = 9.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        mountainInfo();

        // Get the map and register for the ready callback
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setContentView(R.layout.activity_locator);

        mountainInfo();
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

        addMarkers(map);
        setMarkerInteration(map);
        updateCameraPosition(mountains, map);
        debug(map);

    }

    /**
     * Applies and configures an onClick listener for map marker info boxes
     * @param map The map that will be manipulated
     */
    private void setMarkerInteration(GoogleMap map) {
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.d("", marker.getTitle());
            }
        });
    }

    /**
     * Moves the camera from its original to position to a new focal point on a provided map
     *
     * @param mountains A list of the mountains
     * @param map The map that will be adjusted
     */
    private void updateCameraPosition(List<Mountain> mountains, GoogleMap map) {
        Mountain firstMountain = mountains.get(0);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstMountain.getmLatitude(), firstMountain.getmLongitude()), DEFAULT_ZOOM));
    }


    /**
     * Populates mountain information with information from database
     */
    private void mountainInfo() {
        mountainDataSource = new MountainDataSource(this);  // initialize mountain db
        if (mountainDataSource.getMountains().size() <= 0) {
            // Mountains have never been loaded before --> Load mountain data
            mountainDataSource.loadMountains(); // load mountain data into database
        }

        mountains = mountainDataSource.getMountains();   // local list of mountain data
    }

    /**
     * Adds markers to the map
     * @param map The map that will be augmented
     */
    private void addMarkers(GoogleMap map) {
        for (Mountain mount : mountains) {
            // Info that will pop up when user clicks on marker
            String snippet = new String(mount.getmName() + ", "
                    + mount.getmRange() + "\n" + mount.getmElevation());

            map.addMarker(new MarkerOptions()
                            .position(new LatLng(mount.getmLatitude(), mount.getmLongitude()))
                            .title(mount.getmName()).draggable(false)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromBitmap(BitmapHelper.resizeMapIcons(this, "mountain_marker", 110, 110)))
            );
        }
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

    public void debug(GoogleMap map) {
        map.getUiSettings().setZoomControlsEnabled(true);
    }
}
