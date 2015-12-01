package com.csci5448.hiketracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by diana on 10/22/15.
 */
public class Mountain implements Parcelable, Comparable<Mountain>{



    /*******************  Class variables *******************/
    private int id;
    private String mName;
    private String mRange;
    private int mElevation;
    private double mLatitude,mLongtidue;
    private boolean hiked;

    public static final int TOTAL_MOUNTAINS = 53;   // Total number of peaks

    public Mountain(String mName, String mRange, int mElevation, double mlatitude, double mLongtidue){
        super();
        this.mName = mName;
        this.mRange = mRange;
        this.mElevation = mElevation;
        this.id = 0;
        this.hiked = false;
        this.mLatitude = mlatitude;
        this.mLongtidue = mLongtidue;
    }

    public Mountain() {
        super();
    }

    // Construct from parcel
    private Mountain(Parcel in) {
        super();
        this.mName = in.readString();
        this.mRange = in.readString();
        this.mElevation = in.readInt();
        this.mLatitude = in.readDouble();
        this.mLongtidue = in.readDouble();
        this.hiked = (in.readInt() != 0);
    }

    public static final Creator<Mountain> CREATOR = new Creator<Mountain>() {
        @Override
        public Mountain createFromParcel(Parcel in) {
            return new Mountain(in);
        }

        @Override
        public Mountain[] newArray(int size) {
            return new Mountain[size];
        }
    };

    /*******************  Getter/Setter Methods *******************/

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmName() {
        return mName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongtidue() {
        return mLongtidue;
    }

    public void setmLongtidue(double mLongtidue) {
        this.mLongtidue = mLongtidue;
    }

    public boolean isHiked() {
        return hiked;
    }

    public void setHiked(boolean hiked) {
        this.hiked = hiked;
    }

    public void setmElevation(int mElevation) {
        this.mElevation = mElevation;
    }

    public int getmElevation() {return mElevation; }

    public String getmRange() { return mRange; }

    public void setmRange(String mRange) { this.mRange = mRange; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    /*******************  toString Methods *******************/
    public String toString(){
        return "Mountain name: " + mName +
                " --  Elevation: " + String.valueOf(mElevation) +
                " -- Location: " + String.valueOf(mLatitude) +
                ", " + String.valueOf(mLongtidue) +
                " -- Hiked: " + String.valueOf(hiked) + "\n";
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mountain other = (Mountain) obj;
        if (id != other.getId())
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getmName());
        dest.writeString(getmRange());
        dest.writeInt(getmElevation());
        dest.writeDouble(getmLatitude());
        dest.writeDouble(getmLongtidue());
        dest.writeInt((!isHiked()) ? 0 : 1);
    }


    @Override
    public int compareTo(Mountain another) {
        return getmElevation() - another.getmElevation();
    }
}