package com.csci5448.hiketracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 10/22/15.
 */
public class UserDataSource extends HikeTrackerDBDAO{

    private static final String WHERE_ID_EQUALS = DatabaseHelper.KEY_ID
            + " =?";

    private static final String SELECT_QUERY = "SELECT * FROM " + DatabaseHelper.TABLE_USERS;

    public UserDataSource(Context context) {
        super(context);
    }

    public long save(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_USERNAME, user.getUserName());
        values.put(DatabaseHelper.KEY_SUMMITCOUNT, user.getSummitCount());
        values.put(DatabaseHelper.KEY_AVERAGELENGTH, user.getAverageLength());
        values.put(DatabaseHelper.KEY_MOSTRECENT, user.getMostRecent());
        values.put(DatabaseHelper.KEY_TOTALCOUNT, user.getTotalCount());

        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public long update(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_USERNAME, user.getUserName());
        values.put(DatabaseHelper.KEY_SUMMITCOUNT, user.getSummitCount());
        values.put(DatabaseHelper.KEY_AVERAGELENGTH, user.getAverageLength());
        values.put(DatabaseHelper.KEY_MOSTRECENT, user.getMostRecent());
        values.put(DatabaseHelper.KEY_TOTALCOUNT, user.getTotalCount());
        long result = database.update(DatabaseHelper.TABLE_USERS, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(user.getUserId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int deleteHikeData(User user) {
        return database.delete(DatabaseHelper.TABLE_USERS,
                WHERE_ID_EQUALS, new String[] { String.valueOf(user.getUserId()) });
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        try {
            Cursor cursor = database.rawQuery(SELECT_QUERY, null);

            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        User user = new User();
                        user.setUserId(cursor.getInt(0));
                        user.setUserName(cursor.getString(1));
                        user.setSummitCount(cursor.getInt(2));
                        user.setTotalCount(cursor.getInt(3));
                        user.setMostRecent(cursor.getString(4));
                        user.setAverageLength(cursor.getLong(5));

                        users.add(user);
                    } while (cursor.moveToNext());
                }
            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
//            try { database.close(); } catch (Exception ignore) {}
        }

        return users;
    }

}
