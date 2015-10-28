package oodesignanalysis.hiketracker;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/28/15.
 */
public class HistoryActivity extends ListActivity {

    /*******************  Class variables *******************/
    private ListView mListView;
    //private ListViewAdapter TODO: This reference does not resolve, is something else needed?
    private ArrayList<HikeData> hikeDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    /**
     * Retrieves data from a file
     * @return the data in the file
     */
    private String readFromFile() {
        return "";
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
