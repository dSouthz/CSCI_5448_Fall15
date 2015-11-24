package com.csci5448.hiketracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Ryan on 10/28/15.
 */
public class HikeData implements Parcelable, Comparable<HikeData>{
    @Override
    public String toString() {
        return "HikeData{" +
                "id=" + id +
                ", userId=" + userId +
                ", peakName='" + peakName + '\'' +
                ", hikeLength=" + hikeLength +
                ", hikeDate=" + hikeDate +
                '}';
    }

    /*******************  Class variables *******************/
    private int id;
    private int userId;
    private String peakName;
    private int hikeLength;
    private Date hikeDate;

    public HikeData(int id, String peakName, int hikeLength, Date hikeDate, int userId) {
        this.id = id;
        this.peakName = peakName;
        this.hikeLength = hikeLength;
        this.hikeDate = hikeDate;
        this.userId = userId;
    }

    public HikeData(String peakName, int hikeLength, Date hikeDate, int userId) {
        this(0, peakName, hikeLength, hikeDate, userId);
    }

    public HikeData() {
        this(0, "Na", 0, new Date(), 0);
    }

    private HikeData(Parcel in){
        super();
        this.id = in.readInt();
        this.peakName = in.readString();
        this.hikeLength = in.readInt();
        this.hikeDate = new Date(in.readLong());
        this.userId = in.readInt();
    }

    public static final Creator<HikeData> CREATOR = new Creator<HikeData>() {
        @Override
        public HikeData createFromParcel(Parcel in) {
            return new HikeData(in);
        }

        @Override
        public HikeData[] newArray(int size) {
            return new HikeData[size];
        }
    };

    public String getPeakName() {
        return this.peakName;
    }

    public void setPeakName(String peakName) {
        this.peakName = peakName;
    }

    public int getHikeLength() {
        return hikeLength;
    }

    public void setHikeLength(int hikeLength) {
        this.hikeLength = hikeLength;
    }

    public Date getHikeDate() {
        return hikeDate;
    }

    public void setHikeDate(Date hikeDate) {
        this.hikeDate = hikeDate;
    }

    public int getId() { return id;}

    public void setId(int id) { this.id = id; }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HikeData other = (HikeData) obj;
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
        dest.writeInt(getId());
        dest.writeString(getPeakName());
        dest.writeInt(getHikeLength());
        dest.writeLong(getHikeDate().getTime());
        dest.writeInt(getUserId());
    }

    @Override
    public int compareTo(HikeData another) {
        return getHikeDate().compareTo(another.getHikeDate());
    }
}
