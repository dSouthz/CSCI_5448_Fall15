package oodesignanalysis.hiketracker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import java.util.List;

import com.google.android.gms.maps.MapView;

/**
 * Created by Ryan on 10/28/15.
 */
public class LocatorActivity extends ActionBarActivity {

    /*******************  Class variables *******************/
    private MountainDataSource mountainDataSource;
    private MapView mapView;
    private List<Mountain> mountain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
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
