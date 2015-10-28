package oodesignanalysis.hiketracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by diana on 10/22/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    // Logcat Tag
    private static final String LOG = "DatabaseHelper";

    // SQLiteDatabase
    protected SQLiteDatabase database;

    // Database Version
    private int DATABASE_VERSION;

    // Database Name
    private String DATABASE_NAME;

    // Table Names
    private static final String TABLE_MOUNTAIN = "mountains";
    private static final String TABLE_HIKEDATA = "hikedata";

    // Common Column Names
    private static final String KEY_ID = "id";

    // MOUNTAIN Table - column names
    private static final String KEY_PEAKNAME = "peakname";
    private static final String KEY_ELEVATION = "elevation";
    private static final String KEY_LATLONG = "latlong";

    // HIKEDATA Table - column names
    private static final String KEY_HIKED = "hiked";
    private static final String KEY_HIKELENGTH = "hikelength";
    private static final String KEY_HIKEDATE = "hikedate";

    // Table Create Statements
    // MOUNTAIN table create statement
    private static final String CREATE_TABLE_MOUNTAIN = "CREATE TABLE "
            + TABLE_MOUNTAIN + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PEAKNAME
            + " TEXT," + KEY_ELEVATION + " TEXT," + KEY_LATLONG
            + " TEXT" + ")";

    // HIKEDATA table create statement
    private static final String CREATE_TABLE_HIKEDATA = "CREATE TABLE "
            + TABLE_HIKEDATA + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_HIKED
            + " INTEGER," + KEY_HIKELENGTH + " TEXT," + KEY_HIKEDATE
            + " DATETIME" + ")";



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_MOUNTAIN);
        db.execSQL(CREATE_TABLE_HIKEDATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOUNTAIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKEDATA);

        // create new tables
        onCreate(db);

    }


}
