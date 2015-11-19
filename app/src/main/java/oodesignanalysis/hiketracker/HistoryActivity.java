package oodesignanalysis.hiketracker;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ryan on 10/28/15.
 */
public class HistoryActivity extends ListActivity {

    /*******************  Class variables *******************/
    private ListView mListView;
    //private ListViewAdapter TODO: This reference does not resolve, is something else needed?
    private ArrayList<HikeData> hikeDataArray;
    private HikeDataSource hikeDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define custom screen layout
        setContentView(R.layout.activity_history);

        // initialize database
        hikeDataSource = new HikeDataSource(this);

        hikeDataArray = (ArrayList)hikeDataSource.getAllHikes();
        Collections.sort(hikeDataArray);    // Sort by date

        // Create the adapter to convert the array to views
        HikeDataAdapter adapter = new HikeDataAdapter(this, hikeDataArray);

        // Attach the adapter to a ListView
        ListView listView = getListView();
        listView.setAdapter(adapter);

        // Attach ClickListener
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Module Item Trigger", "Module item was triggered");

                // TODO: Update action for when hikeData item is clicked
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();

                // TODO: Display option to edit or delete entry
            }
        });
    }


    /**
     * Modifies an existing entry in the history
     */
    private void editEntry() {
        // TODO: Populate pop-up window with entry's data
        // TODO: Allow entry data to be manipulated
        // TODO: Call for HikeDataSource update to push to database
    }

    /**
     * Deletes an existing entry in the history
     */
    private void deleteEntry() {
        // TODO: Populate pop-up window with entry's data
        // TODO: Prompt user if they really want to delete entry
        // TODO: Call for HikeDataSource delete to delete entry from database
    }

    /**
     * Creates a new entry in the history
     */
    private void newEntry(String entry) {

    }
}
