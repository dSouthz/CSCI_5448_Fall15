package com.csci5448.hiketracker;

import android.util.Log;

/**
 * Created by Mountain on 12/4/15.
 */
public class TimeHelper {
    public static String timeFromLong(Long time){
        int secs = (int) (time / 1000);
        int mins = secs / 60;
        int hrs = mins / 60;
        mins = mins - (hrs * 60);
        secs = secs % 60;
        return (String.format("%02d", hrs) + ":"
                + String.format("%02d", mins) + ":"
                + String.format("%02d", secs));
    }

    public static long longFromTime(String time){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String hrs = time.substring(0,2);
        String minutes = time.substring(3,5);
        String seconds = time.substring((6));
        int hour = Integer.parseInt(hrs);
        int minute = Integer.parseInt(minutes);
        int sec = Integer.parseInt(seconds);
        return ((hour*60+minute)*60 + sec%60) * 1000;   // Return time in milliseconds
    }

}
