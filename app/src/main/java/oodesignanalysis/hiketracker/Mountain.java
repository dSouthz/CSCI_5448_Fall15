package oodesignanalysis.hiketracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by diana on 10/22/15.
 */
public class Mountain implements Parcelable{



    /*******************  Class variables *******************/
    private int id;
    private String mName;
    private String mRange;
    private double mElevation;
    private double mLatitude,mLongtidue;
    private boolean hiked;

    public static final int TOTAL_MOUNTAINS = 53;   // Total number of peaks

    /*******************  Constructor *******************/

    public Mountain(String mName, String mRange, int mElevation, double mlatitude, double mLongtidue, int id, boolean hiked){
        super();
        this.mName = mName;
        this.mRange = mRange;
        this.mElevation = mElevation;
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

    public Mountain(String mName, String mRange, int mElevation, double mlatitude, double mLongtidue, boolean hiked){
        this(mName, mRange, mElevation, mlatitude, mLongtidue, 0, hiked);
    }

    public Mountain(String mName, String mRange, int mElevation, double mlatitude, double mLongtidue){
        this(mName, mRange, mElevation, mlatitude, mLongtidue, 0, false);
    }

    public Mountain() {
        super();
    }

    // Construct from parcel
    private Mountain(Parcel in) {
        super();
        this.mName = in.readString();
        this.mRange = in.readString();
        this.mElevation = in.readDouble();
        this.mLatitude = in.readDouble();
        this.mLongtidue = in.readDouble();
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

    public void setmElevation(double mElevation) {
        this.mElevation = mElevation;
    }

    public double getmElevation() {
        return mElevation;
    }

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
        dest.writeDouble(getmElevation());
        dest.writeDouble(getmLatitude());
        dest.writeDouble(getmLongtidue());
        dest.writeInt((!isHiked()) ? 0 : 1);
    }


}