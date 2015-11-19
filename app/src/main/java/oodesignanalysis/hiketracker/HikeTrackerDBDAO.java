package oodesignanalysis.hiketracker;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by diana on 11/8/15.
 *
 * DBDAO: Database Data Access Object
 */
public class HikeTrackerDBDAO {

    /*******************  Class variables *******************/
//    private DatabaseHelper dbHelper;

    protected SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context mContext;

    public HikeTrackerDBDAO(Context context) {
        this.mContext = context;
        dbHelper = DatabaseHelper.getHelper(mContext);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DatabaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    /*public void close() {
        dbHelper.close();
        database = null;
    }*/
    // Creating a mountain

}
