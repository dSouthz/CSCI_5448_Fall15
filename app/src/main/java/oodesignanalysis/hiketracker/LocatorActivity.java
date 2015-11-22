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

        // asynchronously retrieves mountain data from table
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
        setMarkerInteraction(map);
        updateCameraPosition(mountains, map);
        debug(map);

    }

    /**
     * Applies and configures an onClick listener for map marker info boxes
     * @param map The map that will be manipulated
     */
    private void setMarkerInteraction(GoogleMap map) {
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
     *  Call Asynchronous task methodsto populate mountain information with information from database
     */
    private void mountainInfo() {
        new GetMountainTask().execute();
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

    public class GetMountainTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("DoINBackGround", "On doInBackground...");
            mountains = mountainDataSource.getMountains();      // Load previously stored mountain data
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
                mountains = mountainDataSource.getMountains();
                Log.d(TAG, "Mountains Loaded");
                Log.d(TAG, "Size: " + String.valueOf(mountains.size()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            addMarkers();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
}
