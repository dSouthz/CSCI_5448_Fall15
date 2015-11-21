package oodesignanalysis.hiketracker;

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
    private double mLatitude, mLongitude;
    private boolean hiked;

    public static final int TOTAL_MOUNTAINS = 53;   // Total number of peaks

    /*******************  Constructor *******************/

    public Mountain(String mName, String mRange, int mElevation, double mLatitude, double mLongitude, int id, boolean hiked){
        super();
        this.mName = mName;
        this.mRange = mRange;
        this.mElevation = mElevation;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.id = id;
        this.hiked = hiked;
    }

    // Constructor Variations
    public Mountain(String mName, String mRange, int mElevation, int id) {
        this(mName, mRange, mElevation, 0, 0, id, false);
    }

    public Mountain(String mName, String mRange, int mElevation, int id, boolean hiked){
        this(mName, mRange, mElevation, 0, 0, id, hiked);
    }

    public Mountain(String mName, String mRange, int mElevation, double mlatitude, double mLongitude, boolean hiked){
        this(mName, mRange, mElevation, mlatitude, mLongitude, 0, hiked);
    }

    public Mountain(String mName, String mRange, int mElevation, double mlatitude, double mLongitude){
        this(mName, mRange, mElevation, mlatitude, mLongitude, 0, false);
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
        this.mLongitude = in.readDouble();
        this.hiked = (in.readInt() != 0);
    }

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

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
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
                ", " + String.valueOf(mLongitude) +
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
        dest.writeDouble(getmLongitude());
        dest.writeInt((!isHiked()) ? 0 : 1);
    }


    @Override
    public int compareTo(Mountain another) {
        return getmElevation() - another.getmElevation();
    }
}