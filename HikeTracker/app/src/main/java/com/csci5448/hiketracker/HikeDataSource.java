package com.csci5448.hiketracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 10/22/15.
 */
public class HikeDataSource extends HikeTrackerDBDAO{

    private static final String WHERE_ID_EQUALS = DatabaseHelper.KEY_ID
            + " =?";
//
//    private static final SimpleDateFormat formatter = new SimpleDateFormat(
//            "MM-dd-yyyy", Locale.ENGLISH);

    private static final String SELECT_QUERY = "SELECT * FROM " + DatabaseHelper.TABLE_HIKEDATA;

    public HikeDataSource(Context context) {
        super(context);
    }

    public long save(HikeData hikeData) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_HIKEDATE, persistDate(hikeData.getHikeDate()));
        values.put(DatabaseHelper.KEY_HIKELENGTH, hikeData.getHikeLength());
        values.put(DatabaseHelper.KEY_PEAKNAME, hikeData.getPeakName());

        return database.insert(DatabaseHelper.TABLE_HIKEDATA, null, values);
    }

    public long update(HikeData hikeData) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_HIKEDATE, persistDate(hikeData.getHikeDate()));
        values.put(DatabaseHelper.KEY_HIKELENGTH, hikeData.getHikeLength());
        values.put(DatabaseHelper.KEY_PEAKNAME, hikeData.getPeakName());

        long result = database.update(DatabaseHelper.TABLE_HIKEDATA, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(hikeData.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int deleteHikeData(HikeData hikeData) {
        return database.delete(DatabaseHelper.TABLE_HIKEDATA,
                WHERE_ID_EQUALS, new String[] { String.valueOf(hikeData.getId()) });
    }

    public List<HikeData> getAllHikes() {
        List<HikeData> hikes = new ArrayList<>();

        try {
            Cursor cursor = database.rawQuery(SELECT_QUERY, null);

            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        HikeData hikeData = new HikeData();
                        hikeData.setId(cursor.getInt(0));
                        hikeData.setHikeLength(cursor.getLong(1));
                        hikeData.setHikeDate(loadDate(cursor, 2));
                        hikeData.setPeakName(cursor.getString(3));

                        hikes.add(hikeData);
                    } while (cursor.moveToNext());
                }
            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
//            try { database.close(); } catch (Exception ignore) {}
        }

        return hikes;
    }

    public static Long persistDate(Date date){
        return (date == null)? null: date.getTime();
    }

    public static Date loadDate(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return new Date(cursor.getLong(index));
    }

    public int getHikeDataCount(){
        Cursor cursor = database.rawQuery(SELECT_QUERY, null);
        return cursor.getCount();
    }

}
