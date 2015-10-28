package oodesignanalysis.hiketracker;

/**
 * Created by diana on 10/22/15.
 */
public class Mountain {

    /*******************  Class variables *******************/
    private String mName;
    private double mElevation;
    private double mLatitude,mLongtidue;
    private boolean hiked;

    /*******************  Constructor *******************/
    public Mountain(String mName, int mElevation) {
        this.mName = mName;
        this.mElevation = mElevation;
        this.hiked = false;
    }

    /*******************  Getter/Setter Methods *******************/
    public void setmName(String mName) {
        this.mName = mName;
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

    /*******************  toString Methods *******************/
    public String toString(){
        return "Mountain name: " + mName +
                " --  Elevation: " + String.valueOf(mElevation) +
                " -- Location: " + String.valueOf(mLatitude) +
                ", " + String.valueOf(mLongtidue) +
                " -- Hiked: " + String.valueOf(hiked) + "\n";
    }
}
