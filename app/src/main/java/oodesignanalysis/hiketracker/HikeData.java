package oodesignanalysis.hiketracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Ryan on 10/28/15.
 */
public class HikeData implements Parcelable{

    /*******************  Class variables *******************/
    private String peakName;
    private int hikeLength;
    private Date hikeDate;

    public HikeData(String peakName, int hikeLength, Date hikeDate) {
        this.peakName = peakName;
        this.hikeLength = hikeLength;
        this.hikeDate = hikeDate;
    }

    private HikeData(Parcel in){
        super();
        this.peakName = in.readString();
        this.hikeLength = in.readInt();
        this.hikeDate = new Date(in.readLong());
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HikeData other = (HikeData) obj;
        if (hikeDate != other.getHikeDate())
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getPeakName());
        dest.writeInt(getHikeLength());
        dest.writeLong(getHikeDate().getTime());
    }
}
