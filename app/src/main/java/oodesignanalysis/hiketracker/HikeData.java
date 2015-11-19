package oodesignanalysis.hiketracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Ryan on 10/28/15.
 */
public class HikeData implements Parcelable, Comparable<HikeData>{

    /*******************  Class variables *******************/
    private int id;
    private String peakName;
    private int hikeLength;
    private Date hikeDate;
    private int peakId;

    public HikeData(int id, String peakName, int hikeLength, Date hikeDate, int peakId) {
        this.id = id;
        this.peakName = peakName;
        this.hikeLength = hikeLength;
        this.hikeDate = hikeDate;
        this.peakId = peakId;
    }

    public HikeData(String peakName, int hikeLength, Date hikeDate, int peakId) {
        this(0, peakName, hikeLength, hikeDate, peakId);
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
        this.peakId = in.readInt();
    }

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

    public int getPeakId() { return peakId;}

    public void setPeakId(int peakId) { this.peakId = peakId; }

    public int getId() { return id;}

    public void setId(int id) { this.id = id; }

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
        dest.writeInt(getPeakId());
    }

    @Override
    public int compareTo(HikeData another) {
        return getHikeDate().compareTo(another.getHikeDate());
    }
}
