package com.csci5448.hiketracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class LocatorActivity extends FragmentActivity implements OnMapReadyCallback {

    private static String TAG = "Locator Activity"; // for debugging logs
    private GoogleMap mMap;
    private User user;
    private List<Mountain> mountains;
    private MountainDataSource mountainDataSource;
    public static final String PARCEL_NAME = "com.csci5448.hiketracker.Mountain";

    private final float DEFAULT_ZOOM = 6;
    static final int START_NEW_HIKE = 1;    // Request code for new hike activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        user = getIntent().getExtras().getParcelable(getString(R.string.passUser));

        mountainDataSource = new MountainDataSource(this);

        // asynchronously retrieves mountain data from table
        mountainInfo();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getMap().setMyLocationEnabled(true);

    }

    // Call Asynchronous task methods
    public void mountainInfo() { new GetMountainTask().execute(); }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map is ready");
        mMap = googleMap;
    }



    /**
     * Applies and configures an onClick listener for map marker info boxes
     * @param map The map that will be manipulated
     */
    private void setMarkerInteraction(GoogleMap map) {
        map.setInfoWindowAdapter(new MountainInfoWindowAdapter(getLayoutInflater()));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.d(TAG, "Clicked on: " + marker.getTitle());
                startHikeActivity(retrieveMountain(marker.getTitle()));
            }
        });
    }

    private Mountain retrieveMountain(String peakName){
        for (Mountain mount : mountains){
            if (mount.getmName().equals(peakName))
                return mount;
        }
        return new Mountain();
    }

    /**
     * Moves the camera from its original to position to a new focal point on a provided map
     *
     * @param mountains A list of the mountains
     * @param map The map that will be adjusted
     */
    private void updateCameraPosition(List<Mountain> mountains, GoogleMap map) {
        CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(38.924328, -106.320618)) // chose random mountain to zoom to
                .zoom(DEFAULT_ZOOM)
                .tilt(70)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);
    }


    private void addMarkers(GoogleMap map){
        // Add a marker for each mountain
        Log.d(TAG, "Adding markers");
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);//For displaying elevation

        for (Mountain mount : mountains) {

            // Info that will pop up when user clicks on marker
            String snippet = "Range: " + mount.getmRange() + "\n" +
                    "Elevation: " + formatter.format(mount.getmElevation()) + "\n" +
                    "Click to begin hike!";

            map.addMarker(new MarkerOptions().
                    position(new LatLng(mount.getmLatitude(), mount.getmLongtidue())).
                    title(mount.getmName()).draggable(false).
                    snippet(snippet).
                    icon(BitmapDescriptorFactory.fromResource((mount.isHiked()? R.drawable.completed : R.drawable.mountain))));
        }

    }

    public void debug(GoogleMap map) {
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    public void startHikeActivity(Mountain mount) {
        Intent myIntent = new Intent(LocatorActivity.this, HikeActivity.class);
        myIntent.putExtra(getString(R.string.passMountain), mount);
        myIntent.putExtra(getString(R.string.passUser), user);
        myIntent.putExtra(getString(R.string.sourceString), TAG);

        startActivity(myIntent);
    }

//    Asynchronous Task to revtrieve saved mountain information from database
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
            addMarkers(mMap);
            setMarkerInteraction(mMap);
            updateCameraPosition(mountains, mMap);
            debug(mMap);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }
}
