package com.csci5448.hiketracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Diana on 11/21/15.
 */
public class User implements Parcelable, Comparable<User>{

    /*******************  Class variables *******************/
    private int userId;             // Id, auto-incremented when entered into database
    private String userName;        // User-entered string name
    private String mostRecent;      // Most recent peak hiked
    private int summitCount;        // Number of 14ers hiked at least once
    private int totalCount;         // Total number of recorded hikes
    private long averageLength;     // Average time for total hikes

    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        mostRecent = in.readString();
        summitCount = in.readInt();
        totalCount = in.readInt();
        averageLength = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {

    }

    /*******************  Getter/Setter Methods *******************/
    public synchronized long getAverageLength() {
        return averageLength;
    }

    public synchronized void setAverageLength(long averageLength) {
        this.averageLength = averageLength;
    }

    public synchronized void addNewHike(long newTime){
        long totalTime = averageLength*(totalCount);
        totalTime += newTime;
        addOneHike();
        setAverageLength(totalTime/totalCount);
    }

    public synchronized void subtractNewHike(long newTime){
        long totalTime = averageLength*(totalCount);
        totalTime -= newTime;
        subtractOneHike();
        setAverageLength(totalTime/totalCount);
    }


    public synchronized int getTotalCount() {
        return totalCount;
    }

    public synchronized void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public synchronized void subtractOneHike(){
        totalCount--;
    }

    public synchronized void addOneHike(){
        totalCount++;
    }

    public synchronized int getSummitCount() {
        return summitCount;
    }

    public synchronized void addOneSummit() {
        summitCount++;
    }

    public synchronized void subtractOneSummit(){
        summitCount--;
    }

    public synchronized void setSummitCount(int summitCount) {
        this.summitCount = summitCount;
    }

    public synchronized String getMostRecent() {
        return mostRecent;
    }

    public synchronized void setMostRecent(String mostRecent) {
        this.mostRecent = mostRecent;
    }

    public synchronized String getUserName() {
        return userName;
    }

    public synchronized void setUserName(String userName) {
        this.userName = userName;
    }

    public synchronized int getUserId() {
        return userId;
    }

    public synchronized void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public synchronized int compareTo(User user) {
        return userId - user.getUserId();
    }

    @Override
    public synchronized int describeContents() {
        return 0;
    }

    @Override
    public synchronized void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userId);
        parcel.writeString(userName);
        parcel.writeString(mostRecent);
        parcel.writeInt(summitCount);
        parcel.writeInt(totalCount);
        parcel.writeLong(averageLength);
    }
}
