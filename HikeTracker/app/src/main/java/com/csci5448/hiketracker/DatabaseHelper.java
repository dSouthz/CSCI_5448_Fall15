package com.csci5448.hiketracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by diana on 10/22/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    // Logcat Tag
    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static int DATABASE_VERSION = 1;

    // Database Name
    private static String DATABASE_NAME = "HikeTrackerDB";

    // Table Names
    public static final String TABLE_MOUNTAIN = "mountains";
    public static final String TABLE_HIKEDATA = "hikedata";
    public static final String TABLE_USERS = "users";

    // Common Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_PEAKNAME = "peakname";

    // MOUNTAIN Table - column names
    public static final String KEY_RANGE = "range";
    public static final String KEY_ELEVATION = "elevation";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_HIKED = "hiked";

    // HIKEDATA Table - column names
    public static final String KEY_HIKELENGTH = "hikelength";
    public static final String KEY_HIKEDATE = "hikedate";

    // HIKEDATA Table - column names
    public static final String KEY_USERNAME = "username";
    public static final String KEY_SUMMITCOUNT = "summitcount";
    public static final String KEY_TOTALCOUNT = "totalcount";
    public static final String KEY_MOSTRECENT = "mostrecent";
    public static final String KEY_AVERAGELENGTH = "averagelength";

    // Table Create Statements
    // MOUNTAIN table create statement
    public static final String CREATE_TABLE_MOUNTAIN = "CREATE TABLE "
            + TABLE_MOUNTAIN + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_PEAKNAME
            + " TEXT, " + KEY_RANGE + " TEXT, " +  KEY_ELEVATION + " TEXT, " + KEY_LATITUDE
            + " INTEGER, " + KEY_LONGITUDE + " INTEGER, " + KEY_HIKED + " INTEGER" +")";

    // HIKEDATA table create statement
    public static final String CREATE_TABLE_HIKEDATA = "CREATE TABLE "
            + TABLE_HIKEDATA + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_HIKELENGTH
            + " TEXT, " + KEY_HIKEDATE + " DATETIME, " + KEY_PEAKNAME + " TEXT" + ")";

    // USER table create statement
    public static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_HIKEDATA + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_USERNAME
            + " TEXT, " + KEY_SUMMITCOUNT + " INT, " + KEY_TOTALCOUNT + " INT, "
            + KEY_MOSTRECENT + " TEXT, " + KEY_AVERAGELENGTH + " INT" +")";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating databases");
        Log.d(TAG, "Mountains: " + CREATE_TABLE_MOUNTAIN);
        Log.d(TAG, "Hikes: " + CREATE_TABLE_HIKEDATA);
        Log.d(TAG, "Users: " + CREATE_TABLE_USERS);
        // creating required tables
        db.execSQL(CREATE_TABLE_MOUNTAIN);
        db.execSQL(CREATE_TABLE_HIKEDATA);
        db.execSQL(CREATE_TABLE_USERS);
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOUNTAIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKEDATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // create new tables
        onCreate(db);

    }

    // Synchronized access to the database, necessary for the multiple
    // tables and helper classes accessing the data
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context) {
        // Use the application context, which will ensure that we
        // don't accidentally leak an Activity's context.
        if (instance == null)
            instance = new DatabaseHelper(context);
        return instance;
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
