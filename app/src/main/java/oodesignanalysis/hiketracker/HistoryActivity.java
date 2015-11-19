package oodesignanalysis.hiketracker;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ryan on 10/28/15.
 */
public class HistoryActivity extends ListActivity {

    /*******************  Class variables *******************/
    private ListView mListView;
    //private ListViewAdapter TODO: This reference does not resolve, is something else needed?
    private List<HikeData> hikeDataArray;
    private HikeDataSource hikeDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // initialize database
        hikeDataSource = new HikeDataSource(this);

        if (hikeDataSource.getAllHikes().size() <= 0) {
            // Currently no hikes
        }

        else {
            hikeDataArray = hikeDataSource.getAllHikes();
            Collections.sort(hikeDataArray);    // Sort by date
        }

    }


    /**
     * Modifies an existing entry in the history
     */
    private void editEntry() {

    }

    /**
     * Creates a new entry in the history
     */
    private void newEntry(String entry) {

    }
}
